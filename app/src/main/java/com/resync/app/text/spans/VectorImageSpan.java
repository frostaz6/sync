package com.resync.app.text.spans;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.text.style.LineBackgroundSpan;
import android.text.style.LineHeightSpan;
import android.text.style.ReplacementSpan;
import android.widget.TextView;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.resync.app.helpers.ui.MonetUtil;
import java.lang.ref.WeakReference;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: nb.f */
/* JADX INFO: compiled from: VectorImageSpan.java */
/* JADX INFO: loaded from: classes3.dex */
public class VectorImageSpan extends ReplacementSpan implements LineBackgroundSpan, LineHeightSpan, Target<Drawable> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public int resId;
    private VectorDrawable drawable;
    private WeakReference<TextView> textViewRef;
    private Request request;
    private boolean loaded;
    private final int size;
    private final int tintMode;

    public VectorImageSpan(int resId, int size) {
        this.resId = resId;
        this.size = size;
        this.tintMode = 1;
    }

    private void invalidate() {
        WeakReference<TextView> weakReference = this.textViewRef;
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        TextView textView = this.textViewRef.get();
        textView.invalidate();
        if (textView.isAttachedToWindow() && textView.isShown() && textView.isLaidOut() && !textView.isDirty()) {
            Logger.verbose("VectorImageSpan", "No other spans were still pending. Invalidating: " + this);
        } else {
            Logger.verbose("VectorImageSpan", "Skipped Invalidating: Shown: " + textView.isShown() + " - Attached: " + textView.isShown() + " - Laid out: " + textView.isLaidOut() + " - Dirty: " + textView.isDirty());
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
            WeakReference<TextView> weakReference = this.textViewRef;
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

    public int getImageSize() {
        return this.size;
    }

    @Override // android.text.style.ReplacementSpan
    public void draw(Canvas canvas, CharSequence charSequence, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        if (this.drawable == null) {
            return;
        }
        int boundsTop = (top + ((bottom - top) / 2)) - (this.size / 2);
        canvas.save();
        canvas.translate(x, boundsTop);
        VectorDrawable vectorDrawable = this.drawable;
        int size = this.size;
        vectorDrawable.setBounds(0, 0, size, size);
        this.drawable.draw(canvas);
        canvas.restore();
    }

    @Override // android.text.style.LineBackgroundSpan
    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence charSequence, int start, int end, int lineNumber) {
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

    @Override // com.bumptech.glide.request.target.Target
    public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
        this.loaded = true;
        if (drawable instanceof VectorDrawable) {
            WeakReference<TextView> weakReference = this.textViewRef;
            if (weakReference != null && weakReference.get() != null) {
                TextView textView = this.textViewRef.get();
                if (this.tintMode == 1) {
                    drawable.setTint(textView.getCurrentTextColor());
                } else {
                    drawable.setTint(MonetUtil.getLinkColorAuto());
                }
            }
            setDrawable((VectorDrawable) drawable);
        }
        invalidate();
    }

    @Override // com.bumptech.glide.request.target.Target
    public void onLoadStarted(Drawable drawable) {
    }

    @Override // com.bumptech.glide.request.target.Target
    public final Request getRequest() {
        return this.request;
    }

    public void setDrawable(VectorDrawable vectorDrawable) {
        this.drawable = vectorDrawable;
    }

    @Override // com.bumptech.glide.request.target.Target
    public void onLoadCleared(Drawable drawable) {
    }

    public void setTextView(TextView textView) {
        this.textViewRef = new WeakReference<>(textView);
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onDestroy() {
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onStop() {
    }

    public VectorImageSpan(int size, int resId, int tintMode) {
        this.resId = resId;
        this.size = size;
        this.tintMode = tintMode;
    }

    public VectorImageSpan(TextView textView, int resId, int tintMode) {
        this.resId = resId;
        this.size = textView.getLineHeight();
        this.tintMode = tintMode;
    }
}
