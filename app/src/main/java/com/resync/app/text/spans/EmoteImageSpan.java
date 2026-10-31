package com.resync.app.text.spans;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.ReplacementSpan;
import android.widget.TextView;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.resync.app.RedditApplication;
import java.lang.ref.WeakReference;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: nb.c */
/* JADX INFO: compiled from: EmoteImageSpan.java */
/* JADX INFO: loaded from: classes3.dex */
public class EmoteImageSpan extends ReplacementSpan implements Target<Drawable>, Drawable.Callback, SyncSpan {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private String url;
    private Drawable drawable;
    private WeakReference<TextView> textViewRef;
    private int textColor;
    private Request request;
    private int width;
    private int height;
    private final boolean showPlaceholder;
    // Only Reddit's full-size inline comment images opt into native aspect ratios.
    // Small emotes and all existing callers retain their fixed dimensions.
    private final boolean preserveAspectRatio;
    private final int maxDisplayWidth;
    private final int maxDisplayHeight;

    public EmoteImageSpan(String url, int size) {
        this(url, size, size);
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
        WeakReference<TextView> weakReference = this.textViewRef;
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        TextView textView = this.textViewRef.get();
        if (textView.isAttachedToWindow() && textView.isShown() && textView.isLaidOut() && !textView.isDirty()) {
            textView.invalidate();
            Logger.verbose("InlineImageSpan", "No other spans were still pending. Invalidating: " + this);
            return;
        }
        Logger.verbose("InlineImageSpan", "Skipped Invalidating: Shown: " + textView.isShown() + " - Attached: " + textView.isShown() + " - Laid out: " + textView.isLaidOut() + " - Dirty: " + textView.isDirty());
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onStart() {
    }

    @Override // com.bumptech.glide.request.target.Target
    public final void setRequest(Request request) {
        this.request = request;
        if (request == null) {
            Logger.verbose("Cleaned up!");
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
        if (this.drawable == null) {
            if (this.showPlaceholder) {
                canvas.save();
                canvas.translate(x, bottom - this.height);
                RedditApplication.linkDrawable.setTint(this.textColor);
                RedditApplication.linkDrawable.setBounds(0, 0, this.width, this.height);
                RedditApplication.linkDrawable.draw(canvas);
                canvas.restore();
                return;
            }
            return;
        }
        canvas.save();
        canvas.translate(x, bottom - this.height);
        Drawable drawable = this.drawable;
        if (drawable instanceof BitmapDrawable) {
            if (((BitmapDrawable) drawable).getBitmap() != null && !((BitmapDrawable) this.drawable).getBitmap().isRecycled()) {
                this.drawable.setBounds(0, 0, this.width, this.height);
                try {
                    this.drawable.draw(canvas);
                } catch (Exception unused) {
                }
            }
        } else if ((drawable instanceof GifDrawable) && ((GifDrawable) drawable).isRunning()) {
            this.drawable.setBounds(0, 0, this.width, this.height);
            this.drawable.draw(canvas);
        }
        canvas.restore();
    }

    /** Creates a bounded inline comment image whose height follows the downloaded image. */
    public static EmoteImageSpan forCommentImage(String url, int maxWidth) {
        // The placeholder is short while the image loads. Once Glide supplies
        // actual image dimensions, onResourceReady() requests a fresh layout.
        return new EmoteImageSpan(url, maxWidth,
                Math.max(1, maxWidth * 3 / 4), false, true);
    }

    public boolean preservesAspectRatio() {
        return preserveAspectRatio;
    }

    /**
     * Use the loaded drawable's actual aspect ratio. Glide's load uses a
     * bounded decode with no crop/resize transformation for comment images.
     */
    private void updateImageDimensions(Drawable loaded) {
        if (!preserveAspectRatio || loaded == null) return;
        int sourceWidth = loaded.getIntrinsicWidth();
        int sourceHeight = loaded.getIntrinsicHeight();
        if (loaded instanceof BitmapDrawable) {
            android.graphics.Bitmap bitmap = ((BitmapDrawable) loaded).getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                sourceWidth = bitmap.getWidth();
                sourceHeight = bitmap.getHeight();
            }
        }
        if (sourceWidth <= 0 || sourceHeight <= 0) return;
        double ratio = (double) sourceHeight / (double) sourceWidth;
        // Bound both dimensions without changing aspect ratio. Tall portraits
        // may be narrower rather than being compressed into a landscape box.
        int calculatedWidth = maxDisplayWidth;
        int calculatedHeight = Math.max(1, (int) Math.round(calculatedWidth * ratio));
        if (calculatedHeight > maxDisplayHeight) {
            calculatedHeight = maxDisplayHeight;
            calculatedWidth = Math.max(1, (int) Math.round(calculatedHeight / ratio));
        }
        if (this.width != calculatedWidth || this.height != calculatedHeight) {
            this.width = calculatedWidth;
            this.height = calculatedHeight;
            TextView view = textViewRef == null ? null : textViewRef.get();
            if (view != null) {
                view.requestLayout();
                view.invalidate();
            }
        }
    }

    public int getWidth() {
        return this.width;
    }

    @Override // com.bumptech.glide.request.target.Target
    public void onLoadFailed(Drawable drawable) {
        setDrawable(null);
        invalidate();
        clearTextView();
        Logger.verbose("InlineImageSpan", "Load failed: " + this);
    }

    @Override // com.bumptech.glide.request.target.Target
    public final void getSize(SizeReadyCallback sizeReadyCallback) {
        sizeReadyCallback.onSizeReady(this.width, this.height);
    }

    @Override // android.text.style.ReplacementSpan
    public int getSize(Paint paint, CharSequence charSequence, int start, int end, Paint.FontMetricsInt fontMetricsInt) {
        if (fontMetricsInt != null) {
            int negHeight = -this.height;
            fontMetricsInt.ascent = negHeight;
            fontMetricsInt.descent = 0;
            fontMetricsInt.top = negHeight;
            fontMetricsInt.bottom = 0;
        }
        return this.width;
    }

    public String getUrl() {
        return this.url;
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

    @Override // com.bumptech.glide.request.target.Target
    public void onLoadCleared(Drawable drawable) {
    }

    @Override // com.bumptech.glide.request.target.Target
    public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
        if (drawable instanceof BitmapDrawable) {
            setDrawable((BitmapDrawable) drawable);
        } else if (drawable instanceof GifDrawable) {
            setDrawable((GifDrawable) drawable);
            ((GifDrawable) this.drawable).setLoopCount(-1);
            ((GifDrawable) this.drawable).setCallback(this);
            ((GifDrawable) this.drawable).start();
            ((GifDrawable) this.drawable).invalidateSelf();
        }
        updateImageDimensions(drawable);
        Logger.verbose("InlineImageSpan", "Loaded: " + drawable);
        invalidate();
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onDestroy() {
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onStop() {
    }

    public void setTextView(TextView textView) {
        this.textViewRef = new WeakReference<>(textView);
        this.textColor = textView.getCurrentTextColor();
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long when) {
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
    }

    public EmoteImageSpan(String url, int size, boolean showPlaceholder) {
        this(url, size, size, showPlaceholder);
    }

    public EmoteImageSpan(String url, int width, int height) {
        this(url, width, height, false);
    }

    public EmoteImageSpan(String url, int width, int height, boolean showPlaceholder) {
        this(url, width, height, showPlaceholder, false);
    }

    private EmoteImageSpan(String url, int width, int height,
            boolean showPlaceholder, boolean preserveAspectRatio) {
        this.url = url;
        this.width = width;
        this.height = height;
        this.showPlaceholder = showPlaceholder;
        this.preserveAspectRatio = preserveAspectRatio;
        this.maxDisplayWidth = width;
        this.maxDisplayHeight = Math.max(height, width * 2);
    }
}
