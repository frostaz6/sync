package com.resync.app.text.spans;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.widget.TextView;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.helpers.ui.MonetUtil;
import java.lang.ref.WeakReference;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.util.Logger;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: renamed from: nb.h */
/* JADX INFO: compiled from: WebPreviewImageSpan.java */
/* JADX INFO: loaded from: classes3.dex */
public class WebPreviewImageSpan extends ReplacementSpan implements Target<Drawable>, Drawable.Callback, SyncSpan {
    static int PADDING = SizeHelper.dpToPx(12);
    private final Path topPath;
    private final Path bottomPath;
    private final float cornerRadius;
    int bgColor;
    int borderColor;
    private String url;
    private String displayUrl;
    private Integer index;
    private Drawable drawable;
    private WeakReference<TextView> textViewRef;
    private int textColor;
    private Request request;
    private final int width;
    private final int height;
    private final Paint paint;
    private final Drawable linkDrawable;
    public String tag = WebPreviewImageSpan.class.getSimpleName();
    boolean loadFailed = false;
    private boolean attempted = false;

    public WebPreviewImageSpan(String url, int width, Integer index) {
        this.url = url;
        // displayUrl is used only for label/host checks (e.g. isGithub); the actual
        // preview image is now resolved client-side via og:image (see WebsitePreviewModelLoader),
        // not the discontinued ap.syncforreddit.com proxy.
        this.displayUrl = url;
        this.index = index;
        this.width = width;
        int computedHeight = (int) (((float) width / getMaxWidth()) * getMaxHeight());
        this.height = computedHeight;
        Path path = new Path();
        this.topPath = path;
        Path path2 = new Path();
        this.bottomPath = path2;
        float radius = SizeHelper.dpToPxFloat(12);
        this.cornerRadius = radius;
        if (radius > 0.0f && width > 0 && computedHeight > 0) {
            path.rewind();
            path2.rewind();
            path.moveTo(radius, 0.0f);
            path.quadTo(0.0f, 0.0f, 0.0f, radius);
            path.lineTo(0.0f, 0.0f);
            path.moveTo(width - radius, 0.0f);
            path.quadTo(width, 0.0f, width, radius);
            path.lineTo(width, 0.0f);
            path2.moveTo(0.0f, 0.0f);
            path2.quadTo(0.0f, radius, radius, radius);
            path2.lineTo(0.0f, radius);
            path2.moveTo(width, 0.0f);
            path2.quadTo(width, radius, width - radius, radius);
            path2.lineTo(width, radius);
        }
        this.paint = new Paint();
        Drawable drawableMutate = RedditApplication.getAppContext().getDrawable(R.drawable.outline_link_24).mutate();
        this.linkDrawable = drawableMutate;
        drawableMutate.setTint(MonetUtil.getSecondaryTextColorAuto());
        this.bgColor = InlineImageSpan.getDefaultBgColor();
        this.borderColor = MonetUtil.getBackgroundColorAuto();
    }

    private void clearTextView() {
        WeakReference<TextView> weakReference = this.textViewRef;
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        this.textViewRef.clear();
        this.textViewRef = null;
    }

    private void invalidate() {
        String suffix;
        String tagLocal = this.tag;
        StringBuilder sb = new StringBuilder();
        sb.append("Starting invalidate: ");
        sb.append(this.textViewRef);
        WeakReference<TextView> weakReference = this.textViewRef;
        if (weakReference == null || weakReference.get() == null) {
            suffix = "";
        } else {
            suffix = " - " + this.textViewRef.get();
        }
        sb.append(suffix);
        Logger.verbose(tagLocal, sb.toString());
        WeakReference<TextView> weakReference2 = this.textViewRef;
        if (weakReference2 == null || weakReference2.get() == null) {
            return;
        }
        this.textViewRef.get().invalidate();
        Logger.verbose(this.tag, "Invalidating: " + this);
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onStart() {
    }

    @Override // com.bumptech.glide.request.target.Target
    public final void setRequest(Request request) {
        this.request = request;
        if (request == null) {
            Logger.verbose(this.tag, "Cleaned up: " + this);
            Drawable drawable = this.drawable;
            if (drawable instanceof GifDrawable) {
                ((GifDrawable) drawable).stop();
                ((GifDrawable) this.drawable).setCallback(null);
            }
            setDrawable(null);
            clearTextView();
        }
    }

    @Override // com.bumptech.glide.request.target.Target
    public final void removeCallback(SizeReadyCallback sizeReadyCallback) {
    }

    @Override // android.text.style.ReplacementSpan
    public void draw(Canvas canvas, CharSequence charSequence, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        String label;
        if (this.drawable == null) {
            // Collapsed: draw nothing until a real image loads (getSize returns 0 too), so links with
            // no fetchable preview (empty/no og:image, 404, failed screenshot) show no box at all.
            return;
        }
        canvas.save();
        canvas.translate(x, bottom - this.height);
        this.paint.setColor(this.bgColor);
        canvas.drawRect(0.0f, 0.0f, this.width, this.height, this.paint);
        Drawable drawable3 = this.drawable;
        if (drawable3 instanceof BitmapDrawable) {
            if (((BitmapDrawable) drawable3).getBitmap() != null && !((BitmapDrawable) this.drawable).getBitmap().isRecycled()) {
                this.drawable.setBounds(0, 0, this.width, this.height);
                this.drawable.draw(canvas);
            }
        } else if ((drawable3 instanceof GifDrawable) && ((GifDrawable) drawable3).isRunning()) {
            try {
                this.drawable.setBounds(0, 0, this.width, this.height);
                this.drawable.draw(canvas);
            } catch (Exception e) {
                Logger.logException(e);
            }
        }
        float textSize = paint.getTextSize() + paint.descent() + paint.ascent() + SizeHelper.dpToPxFloat(8);
        Logger.verbose("Height: " + textSize);
        this.paint.setColor(this.bgColor);
        canvas.drawRect(0.0f, ((((float) this.height) - textSize) - paint.getTextSize()) - ((float) SizeHelper.dpToPx(8)), (float) this.width, (float) this.height, this.paint);
        float maxTextWidth = (float) (this.width - SizeHelper.dpToPx(16));
        if (this.index != null) {
            label = "(" + this.index + ") " + getUrl();
        } else {
            label = getUrl();
        }
        String ellipsized = (String) TextUtils.ellipsize(label, (TextPaint) paint, maxTextWidth, TextUtils.TruncateAt.END);
        int color = paint.getColor();
        paint.setColor(MonetUtil.getPrimaryTextColorAuto());
        canvas.drawText(ellipsized, SizeHelper.dpToPx(8), this.height - textSize, paint);
        paint.setColor(color);
        if (LinkTypeHelper.isYouTube(this.url) && this.drawable != null) {
            canvas.drawBitmap(RedditApplication.playVideoBitmap, (this.width / 2) - (RedditApplication.playVideoBitmap.getWidth() / 2), (this.height / 2) - (RedditApplication.playVideoBitmap.getHeight() / 2), (Paint) null);
        }
        this.paint.setColor(this.borderColor);
        canvas.drawPath(this.topPath, this.paint);
        canvas.translate(0.0f, this.height - this.cornerRadius);
        canvas.drawPath(this.bottomPath, this.paint);
        canvas.restore();
    }

    public int getMaxHeight() {
        return isGithub() ? 600 : 350;
    }

    @Override // com.bumptech.glide.request.target.Target
    public void onLoadFailed(Drawable drawable) {
        this.loadFailed = true;
        setDrawable(null);
        // Stay collapsed — no box for links with no fetchable preview.
        clearTextView();
        Logger.verbose(this.tag, "Load failed: " + this);
    }

    @Override // com.bumptech.glide.request.target.Target
    public final void getSize(SizeReadyCallback sizeReadyCallback) {
        sizeReadyCallback.onSizeReady(this.width, this.height);
    }

    @Override // android.text.style.ReplacementSpan
    public int getSize(Paint paint, CharSequence charSequence, int start, int end, Paint.FontMetricsInt fontMetricsInt) {
        if (this.drawable == null) {
            // Collapsed (no reserved space) until an image loads, then requestRelayout() expands it.
            if (fontMetricsInt != null) {
                fontMetricsInt.ascent = 0;
                fontMetricsInt.descent = 0;
                fontMetricsInt.top = 0;
                fontMetricsInt.bottom = 0;
            }
            return 0;
        }
        if (fontMetricsInt != null) {
            int negHeight = -(this.height + PADDING);
            fontMetricsInt.ascent = negHeight;
            fontMetricsInt.descent = 0;
            fontMetricsInt.top = negHeight;
            fontMetricsInt.bottom = 0;
        }
        return this.width;
    }

    public String getDisplayUrl() {
        return this.displayUrl;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        TextView textView;
        WeakReference<TextView> weakReference = this.textViewRef;
        if (weakReference == null || (textView = weakReference.get()) == null) {
            return;
        }
        textView.invalidate();
    }

    @Override // com.bumptech.glide.request.target.Target
    public void onLoadStarted(Drawable drawable) {
    }

    @Override // com.bumptech.glide.request.target.Target
    public final Request getRequest() {
        return this.request;
    }

    public int getMaxWidth() {
        return isGithub() ? 1200 : 600;
    }

    @Override // com.bumptech.glide.request.target.Target
    public void onLoadCleared(Drawable drawable) {
    }

    public String getUrl() {
        return this.url;
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onDestroy() {
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onStop() {
    }

    public boolean isGithub() {
        return StringUtils.contains(getDisplayUrl(), "github.com");
    }

    @Override // com.bumptech.glide.request.target.Target
    public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
        if (drawable instanceof BitmapDrawable) {
            setDrawable(drawable);
        } else if (drawable instanceof GifDrawable) {
            setDrawable((GifDrawable) drawable);
            ((GifDrawable) this.drawable).setLoopCount(-1);
            ((GifDrawable) this.drawable).setCallback(this);
            ((GifDrawable) this.drawable).start();
            ((GifDrawable) this.drawable).invalidateSelf();
        }
        Logger.verbose(this.tag, "Loaded: " + drawable + " - " + this);
        // Re-measure so the (previously collapsed) span expands to show the image now that it loaded.
        requestRelayout();
    }

    /** The span was collapsed (0 size) while loading; re-run the text layout so it expands once loaded. */
    private void requestRelayout() {
        WeakReference<TextView> weakReference = this.textViewRef;
        TextView textView = weakReference != null ? weakReference.get() : null;
        if (textView == null) {
            return;
        }
        if (textView instanceof com.resync.app.widgets.text.SpannableTextView) {
            ((com.resync.app.widgets.text.SpannableTextView) textView).refresh();
        } else {
            textView.requestLayout();
            textView.invalidate();
        }
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public boolean isAttempted() {
        return this.attempted;
    }

    public void setAttempted(boolean attempted) {
        this.attempted = attempted;
    }

    public void setTextView(TextView textView) {
        this.textViewRef = new WeakReference<>(textView);
        this.textColor = textView.getCurrentTextColor();
        if (InlineImageSpan.shouldHighlight(textView)) {
            this.bgColor = InlineImageSpan.getHighlightColor();
            this.borderColor = MonetUtil.getCardColorAuto();
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long when) {
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
    }
}
