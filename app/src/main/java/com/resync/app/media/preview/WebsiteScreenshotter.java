package com.resync.app.media.preview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import com.resync.app.RedditApplication;
import com.resync.app.auth.WebViewCdpDriver;
import com.resync.app.util.Logger;

/**
 * Renders a JPEG screenshot of a web page — the fallback preview when a page
 * exposes no usable OpenGraph image (see {@link OgImageResolver}). Website
 * preview links are rare in a thread (typically 2-3), so a one-shot WebView per
 * link is acceptable. Captures are serialised so only one exists at a time.
 *
 * <p>The WebView is attached to the current Activity's window — off-screen-sized,
 * effectively invisible (alpha ~0) and non-touchable — so Chromium actually lays
 * out, runs JS and paints the page (a detached WebView never gets a real surface
 * and captures a blank/garbage frame). Capture is then done via CDP
 * {@link WebViewCdpDriver#captureScreenshot()} (the real Chromium render), with a
 * software {@link View#draw} fallback. Best-effort: returns {@code null} on
 * failure, and the caller shows a neutral link icon.
 */
public final class WebsiteScreenshotter {

    // Limit concurrent off-screen WebView captures: a swarm (from prefetch) thrashes the device and
    // makes captures fail/time out. Bind-triggered (visible) captures naturally grab slots first.
    private static final java.util.concurrent.Semaphore SCREENSHOT_SLOTS = new java.util.concurrent.Semaphore(2, true);

    // Capture at landscape so Glide's centerCrop to the preview box (600x350) doesn't distort much.
    private static final int CAPTURE_WIDTH = 1200;
    private static final int CAPTURE_HEIGHT = 700;
    private static final long PAGE_SETTLE_MS = 1500;   // let the page paint after onPageFinished
    private static final long PAGE_TIMEOUT_MS = 12000; // hard cap if the page never finishes

    private static final String BROWSER_USER_AGENT =
            "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) "
                    + "Chrome/124.0.0.0 Mobile Safari/537.36";

    private WebsiteScreenshotter() {
    }

    /**
     * @return JPEG bytes of the page, or {@code null} if it couldn't be captured. Multiple links in
     * a thread capture concurrently (each on its own Glide thread); CDP targeting is by URL path so
     * concurrent same-host pages don't collide.
     */
    public static byte[] capture(final String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        return captureInternal(url);
    }

    private static byte[] captureInternal(final String url) {
        final Activity activity = RedditApplication.getCurrentActivity();
        if (activity == null) {
            // No window to attach to → can't render reliably; skip (caller shows no preview).
            return null;
        }
        // Bound concurrent WebView captures so a prefetch swarm can't thrash the device.
        boolean slot = false;
        try {
            slot = SCREENSHOT_SLOTS.tryAcquire(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (!slot) {
            return null;
        }
        try {
            return captureWithSlot(activity, url);
        } finally {
            SCREENSHOT_SLOTS.release();
        }
    }

    private static byte[] captureWithSlot(final Activity activity, final String url) {
        final AtomicReference<WebView> webViewRef = new AtomicReference<>();
        final CountDownLatch loadedLatch = new CountDownLatch(1);
        final Handler main = new Handler(Looper.getMainLooper());

        main.post(new Runnable() {
            @Override
            public void run() {
                try {
                    WebView.setWebContentsDebuggingEnabled(true);
                    WebView webView = new WebView(activity);
                    // Software layer so View.draw() can capture (the fallback) even while attached.
                    webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    WebSettings settings = webView.getSettings();
                    settings.setJavaScriptEnabled(true);
                    settings.setLoadWithOverviewMode(true);
                    settings.setUseWideViewPort(true);
                    settings.setUserAgentString(BROWSER_USER_AGENT);
                    final AtomicBoolean finished = new AtomicBoolean(false);
                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String finishedUrl) {
                            if (finished.compareAndSet(false, true)) {
                                loadedLatch.countDown();
                            }
                        }
                    });

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.width = CAPTURE_WIDTH;
                    lp.height = CAPTURE_HEIGHT;
                    lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
                    lp.token = activity.getWindow().getDecorView().getWindowToken();
                    lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
                    lp.gravity = Gravity.TOP | Gravity.START;
                    lp.x = 0;
                    lp.y = 0;
                    lp.alpha = 0.004f; // effectively invisible, but still composited so it renders
                    lp.format = PixelFormat.TRANSLUCENT;

                    webViewRef.set(webView);
                    activity.getWindowManager().addView(webView, lp);
                    webView.loadUrl(url);
                } catch (Throwable t) {
                    Logger.logException(new Exception(t));
                    loadedLatch.countDown();
                }
            }
        });

        try {
            loadedLatch.await(PAGE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            Thread.sleep(PAGE_SETTLE_MS);
            byte[] bytes = captureViaCdp(url);
            if (bytes == null) {
                bytes = captureViaDraw(main, webViewRef.get());
            }
            return bytes;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            removeAndDestroy(main, activity, webViewRef.get());
        }
    }

    private static byte[] captureViaCdp(String url) {
        WebViewCdpDriver driver = new WebViewCdpDriver();
        try {
            // Prefer a distinctive path segment (survives host-changing redirects, e.g.
            // status.anthropic.com -> status.claude.com, and disambiguates concurrent same-host captures).
            Uri uri = Uri.parse(url);
            String path = uri.getPath();
            String match = (path != null && path.length() > 1) ? path : uri.getHost();
            if (!driver.connect(match != null ? match : url)) {
                return null;
            }
            driver.pageEnable();
            return driver.captureScreenshot();
        } catch (Throwable t) {
            Logger.logException(new Exception(t));
            return null;
        } finally {
            driver.close();
        }
    }

    private static byte[] captureViaDraw(Handler main, final WebView webView) {
        if (webView == null) {
            return null;
        }
        final AtomicReference<byte[]> out = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        main.post(new Runnable() {
            @Override
            public void run() {
                try {
                    int w = webView.getWidth() > 0 ? webView.getWidth() : CAPTURE_WIDTH;
                    int h = webView.getHeight() > 0 ? webView.getHeight() : CAPTURE_HEIGHT;
                    Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    canvas.drawColor(Color.WHITE);
                    webView.draw(canvas);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                    bitmap.recycle();
                    out.set(baos.toByteArray());
                } catch (Throwable t) {
                    Logger.logException(new Exception(t));
                } finally {
                    latch.countDown();
                }
            }
        });
        try {
            latch.await(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return out.get();
    }

    private static void removeAndDestroy(Handler main, final Activity activity, final WebView webView) {
        if (webView == null) {
            return;
        }
        final CountDownLatch latch = new CountDownLatch(1);
        main.post(new Runnable() {
            @Override
            public void run() {
                try {
                    activity.getWindowManager().removeView(webView);
                } catch (Throwable ignored) {
                } finally {
                    try {
                        webView.destroy();
                    } catch (Throwable ignored) {
                    }
                    latch.countDown();
                }
            }
        });
        try {
            latch.await(3000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
