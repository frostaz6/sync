package com.resync.app.media.preview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import com.resync.app.util.Logger;

/**
 * Glide loader for {@link WebsitePreviewModel}. Resolves the page's og:image on
 * Glide's background thread via {@link OgImageResolver}, then delegates the
 * actual image download to Glide's built-in {@code GlideUrl -> InputStream}
 * loader (reusing the app's OkHttp client, HTTP cache and cancellation).
 */
public class WebsitePreviewModelLoader implements ModelLoader<WebsitePreviewModel, InputStream> {

    private final ModelLoader<GlideUrl, InputStream> urlLoader;

    WebsitePreviewModelLoader(ModelLoader<GlideUrl, InputStream> urlLoader) {
        this.urlLoader = urlLoader;
    }

    @Override
    public boolean handles(@NonNull WebsitePreviewModel model) {
        return true;
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(@NonNull WebsitePreviewModel model, int width, int height,
                                               @NonNull Options options) {
        // The "wp2:" prefix versions the disk-cache key so stale entries from the earlier
        // (favicon/garbage) resolution aren't reused after this fix.
        return new LoadData<>(new ObjectKey("wp2:" + model.getUrl()),
                new OgImageFetcher(this.urlLoader, model.getUrl(), width, height, options));
    }

    // Dedicated pool so the slow og:image-resolve / WebView-screenshot work does NOT block Glide's
    // (small) source-thread pool — otherwise a couple of in-flight previews starve all other image
    // loading and previews only resolve when scrolling re-triggers them. Sized to run several in parallel.
    private static final ExecutorService PREVIEW_EXECUTOR = Executors.newFixedThreadPool(4, new java.util.concurrent.ThreadFactory() {
        private int n = 0;
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "website-preview-" + (n++));
            t.setDaemon(true);
            t.setPriority(Thread.NORM_PRIORITY - 1);
            return t;
        }
    });

    static class OgImageFetcher implements DataFetcher<InputStream> {

        private final ModelLoader<GlideUrl, InputStream> urlLoader;
        private final String pageUrl;
        private final int width;
        private final int height;
        private final Options options;
        private volatile DataFetcher<InputStream> delegate;
        private volatile InputStream ownStream;
        private volatile Future<?> future;
        private volatile boolean cancelled;

        OgImageFetcher(ModelLoader<GlideUrl, InputStream> urlLoader, String pageUrl, int width, int height,
                       Options options) {
            this.urlLoader = urlLoader;
            this.pageUrl = pageUrl;
            this.width = width;
            this.height = height;
            this.options = options;
        }

        @Override
        public void loadData(@NonNull final Priority priority, @NonNull final DataCallback<? super InputStream> callback) {
            // Run off Glide's source thread so slow previews don't block other image loads; this pool
            // also lets multiple previews resolve in parallel.
            this.future = PREVIEW_EXECUTOR.submit(new Runnable() {
                @Override
                public void run() {
                    doLoad(priority, callback);
                }
            });
        }

        private void doLoad(@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
            try {
                String imageUrl = OgImageResolver.resolve(this.pageUrl);
                if (this.cancelled) {
                    callback.onLoadFailed(new IOException("Cancelled"));
                    return;
                }
                if (imageUrl == null || imageUrl.isEmpty()) {
                    // No og:image — fall back to a rendered screenshot of the page.
                    byte[] shot = WebsiteScreenshotter.capture(this.pageUrl);
                    if (this.cancelled) {
                        callback.onLoadFailed(new IOException("Cancelled"));
                        return;
                    }
                    if (shot != null && shot.length > 0) {
                        this.ownStream = new ByteArrayInputStream(shot);
                        callback.onDataReady(this.ownStream);
                        return;
                    }
                    callback.onLoadFailed(new IOException("No preview image for " + this.pageUrl));
                    return;
                }
                LoadData<InputStream> imageLoad =
                        this.urlLoader.buildLoadData(new GlideUrl(imageUrl), this.width, this.height, this.options);
                if (imageLoad == null || imageLoad.fetcher == null) {
                    callback.onLoadFailed(new IOException("No loader for " + imageUrl));
                    return;
                }
                this.delegate = imageLoad.fetcher;
                if (this.cancelled) {
                    this.delegate.cancel();
                    callback.onLoadFailed(new IOException("Cancelled"));
                    return;
                }
                this.delegate.loadData(priority, callback);
            } catch (Exception e) {
                Logger.logException(e);
                callback.onLoadFailed(e instanceof IOException ? (IOException) e : new IOException(e));
            }
        }

        @Override
        public void cleanup() {
            DataFetcher<InputStream> d = this.delegate;
            if (d != null) {
                d.cleanup();
            }
            InputStream s = this.ownStream;
            if (s != null) {
                try {
                    s.close();
                } catch (IOException ignored) {
                }
            }
        }

        @Override
        public void cancel() {
            this.cancelled = true;
            Future<?> f = this.future;
            if (f != null) {
                f.cancel(true);
            }
            DataFetcher<InputStream> d = this.delegate;
            if (d != null) {
                d.cancel();
            }
        }

        @NonNull
        @Override
        public Class<InputStream> getDataClass() {
            return InputStream.class;
        }

        @NonNull
        @Override
        public DataSource getDataSource() {
            return DataSource.REMOTE;
        }
    }

    public static class Factory implements ModelLoaderFactory<WebsitePreviewModel, InputStream> {

        @NonNull
        @Override
        public ModelLoader<WebsitePreviewModel, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
            return new WebsitePreviewModelLoader(multiFactory.build(GlideUrl.class, InputStream.class));
        }

        @Override
        public void teardown() {
        }
    }
}
