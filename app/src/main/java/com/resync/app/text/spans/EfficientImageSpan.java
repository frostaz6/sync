package com.resync.app.text.spans;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.LineBackgroundSpan;
import android.text.style.LineHeightSpan;
import android.text.style.ReplacementSpan;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import java.lang.ref.WeakReference;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.widgets.text.SpannableTextView;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: nb.b */
/* JADX INFO: compiled from: EfficientImageSpan.java */
/* JADX INFO: loaded from: classes3.dex */
public class EfficientImageSpan extends ReplacementSpan implements LineBackgroundSpan, LineHeightSpan, Target<Drawable> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private String url;
    private BitmapDrawable drawable;
    private WeakReference<SpannableTextView> textViewRef;
    private Request request;
    private boolean loaded;
    private final int size;

    public EfficientImageSpan(String url, int size) {
        this.url = url;
        this.size = size;
    }

    private boolean hasPendingSpans() {
        SpannableTextView spannableTextView;
        WeakReference<SpannableTextView> weakReference = this.textViewRef;
        if (weakReference == null || (spannableTextView = weakReference.get()) == null) {
            return false;
        }
        return spannableTextView.hasUnloadedImages();
    }

    private void invalidate() {
        WeakReference<SpannableTextView> weakReference = this.textViewRef;
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        SpannableTextView spannableTextView = this.textViewRef.get();
        boolean pending = hasPendingSpans();
        if (pending || spannableTextView.getVisibility() != 0 || !spannableTextView.isAttachedToWindow() || !spannableTextView.isShown() || !spannableTextView.isLaidOut() || spannableTextView.isDirty() || spannableTextView.isLayoutRequested() || spannableTextView.getWidth() <= 0 || spannableTextView.getHeight() <= 0 || !StringUtils.isNotEmpty(spannableTextView.getText())) {
            Logger.verbose("EfficientImageSpan", "Skipped Invalidating: Shown: " + spannableTextView.isShown() + " - Attached: " + spannableTextView.isShown() + " - Laid out: " + spannableTextView.isLaidOut() + " - Dirty: " + spannableTextView.isDirty() + " - Other spans pending: " + pending + " - Layout requested: " + spannableTextView.isLayoutRequested() + " - Width: " + spannableTextView.getWidth() + " - Height: " + spannableTextView.getHeight() + " - Not empty: " + StringUtils.isNotEmpty(spannableTextView.getText()));
        } else {
            spannableTextView.invalidate();
            Logger.verbose("EfficientImageSpan", "No other spans were still pending. Invalidating: " + this);
        }
        this.textViewRef.clear();
        this.textViewRef = null;
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onStart() {
    }

    @Override // com.bumptech.glide.request.target.Target
    public final void setRequest(Request request) {
        this.request = request;
        if (request == null) {
            Logger.verbose("Cleaned up!");
            setDrawable(null);
            WeakReference<SpannableTextView> weakReference = this.textViewRef;
            if (weakReference == null || weakReference.get() == null) {
                return;
            }
            this.textViewRef.clear();
            this.textViewRef = null;
        }
    }

    @Override // com.bumptech.glide.request.target.Target
    public final void removeCallback(SizeReadyCallback sizeReadyCallback) {
    }

    @Override // android.text.style.LineHeightSpan
    public void chooseHeight(CharSequence charSequence, int start, int end, int spanstartv, int lineHeight, Paint.FontMetricsInt fontMetricsInt) {
        int size = this.size;
        int descentBottom = fontMetricsInt.descent + lineHeight;
        int ascent = fontMetricsInt.ascent;
        int extraAscent = size - ((descentBottom - ascent) - spanstartv);
        if (extraAscent > 0) {
            fontMetricsInt.ascent = ascent - extraAscent;
        }
        int bottomEdge = lineHeight + fontMetricsInt.bottom;
        int top = fontMetricsInt.top;
        int extraTop = size - ((bottomEdge - top) - spanstartv);
        if (extraTop > 0) {
            fontMetricsInt.top = top - extraTop;
        }
    }

    @Override // android.text.style.ReplacementSpan
    public void draw(Canvas canvas, CharSequence charSequence, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        BitmapDrawable bitmapDrawable = this.drawable;
        if (bitmapDrawable == null || bitmapDrawable.getBitmap() == null || bitmapDrawable.getBitmap().isRecycled()) {
            return;
        }
        Logger.verbose("EfficientImageSpan", "Drawing: " + this.drawable);
        int left = (int) x;
        int size = this.size;
        int boundsTop = (top + ((bottom - top) / 2)) - (size / 2);
        this.drawable.setBounds(left, boundsTop, left + size, size + boundsTop);
        this.drawable.draw(canvas);
    }

    @Override // android.text.style.LineBackgroundSpan
    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence charSequence, int start, int end, int lineNumber) {
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    @Override // com.bumptech.glide.request.target.Target
    public void onLoadFailed(Drawable drawable) {
        this.loaded = true;
        setDrawable(null);
        invalidate();
    }

    @Override // com.bumptech.glide.request.target.Target
    public final void getSize(SizeReadyCallback sizeReadyCallback) {
        int size = this.size;
        sizeReadyCallback.onSizeReady(size, size);
    }

    @Override // android.text.style.ReplacementSpan
    public int getSize(Paint paint, CharSequence charSequence, int start, int end, Paint.FontMetricsInt fontMetricsInt) {
        return this.size;
    }

    public int getImageSize() {
        return this.size;
    }

    @Override // com.bumptech.glide.request.target.Target
    public void onLoadStarted(Drawable drawable) {
    }

    @Override // com.bumptech.glide.request.target.Target
    public final Request getRequest() {
        return this.request;
    }

    public String getUrl() {
        return this.url;
    }

    @Override // com.bumptech.glide.request.target.Target
    public void onLoadCleared(Drawable drawable) {
    }

    @Override // com.bumptech.glide.request.target.Target
    public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
        this.loaded = true;
        if (drawable instanceof BitmapDrawable) {
            setDrawable((BitmapDrawable) drawable);
        }
        invalidate();
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onDestroy() {
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onStop() {
    }

    public void setDrawable(BitmapDrawable bitmapDrawable) {
        this.drawable = bitmapDrawable;
    }

    public void setTextView(SpannableTextView spannableTextView) {
        this.textViewRef = new WeakReference<>(spannableTextView);
    }
}
