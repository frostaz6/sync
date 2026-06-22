package com.resync.app.text.spans;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
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
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: nb.g */
/* JADX INFO: compiled from: VectorInlineImageSpan.java */
/* JADX INFO: loaded from: classes3.dex */
public class VectorInlineImageSpan extends ReplacementSpan implements LineBackgroundSpan, LineHeightSpan, Target<Drawable>, Drawable.Callback, SyncSpan {
    private static String TAG = VectorInlineImageSpan.class.getSimpleName();
    static int IMAGE_SIZE = SizeHelper.dpToPx(56);
    static int PADDING;
    static int LINE_HEIGHT;

    private Paint paint;
    private Integer resId;
    private Integer index;
    private String label;
    private Drawable drawable;
    private WeakReference<TextView> textViewRef;
    private Request request;
    int bgColor;
    private int width;
    private final RectF rectF = new RectF();
    private final Path path = new Path();
    private final int cornerRadius;
    private final float[] cornerRadii;

    static {
        int padding = SizeHelper.dpToPx(12);
        PADDING = padding;
        LINE_HEIGHT = IMAGE_SIZE + padding;
    }

    public VectorInlineImageSpan(int resId, String label, Integer index) {
        int radius = SizeHelper.dpToPx(12);
        this.cornerRadius = radius;
        this.cornerRadii = new float[]{radius, radius, 0.0f, 0.0f, 0.0f, 0.0f, radius, radius};
        this.resId = Integer.valueOf(resId);
        this.label = label;
        this.index = index;
        this.paint = new Paint();
        this.bgColor = InlineImageSpan.getDefaultBgColor();
    }

    private void clearTextView() {
        WeakReference<TextView> weakReference = this.textViewRef;
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        this.textViewRef.clear();
        this.textViewRef = null;
    }

    private void drawLabel(Paint paint, Canvas canvas, int left, int right, int top, int bottom, int start, int end) {
        String displayLabel;
        if (this.index != null) {
            displayLabel = "(" + this.index + ") " + this.label;
        } else {
            displayLabel = this.label;
        }
        if (isSettingsShortcut()) {
            displayLabel = displayLabel.split(":")[1].trim();
        }
        canvas.drawText((String) TextUtils.ellipsize(displayLabel, (TextPaint) paint, (right - (left + IMAGE_SIZE + SizeHelper.dpToPx(8))) - SizeHelper.dpToPx(8), TextUtils.TruncateAt.END), left + IMAGE_SIZE + SizeHelper.dpToPx(8), ((top + PADDING) + (IMAGE_SIZE / 2.0f)) - ((paint.descent() + paint.ascent()) / 2.0f), paint);
    }

    private void invalidate() {
        WeakReference<TextView> weakReference = this.textViewRef;
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        TextView textView = this.textViewRef.get();
        if (textView.isAttachedToWindow() && textView.isShown() && textView.isLaidOut() && !textView.isDirty()) {
            textView.invalidate();
            Logger.verbose(TAG,"No other spans were still pending. Invalidating: " + this);
            return;
        }
        Logger.verbose(TAG,"Skipped Invalidating: Shown: " + textView.isShown() + " - Attached: " + textView.isShown() + " - Laid out: " + textView.isLaidOut() + " - Dirty: " + textView.isDirty());
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onStart() {
    }

    @Override // com.bumptech.glide.request.target.Target
    public final void setRequest(Request request) {
        this.request = request;
        if (request == null) {
            Logger.verbose(TAG, "Cleaned up!");
            setDrawable(null);
            clearTextView();
        }
    }

    @Override // com.bumptech.glide.request.target.Target
    public final void removeCallback(SizeReadyCallback sizeReadyCallback) {
    }

    @Override // android.text.style.LineHeightSpan
    public void chooseHeight(CharSequence charSequence, int start, int end, int spanstartv, int lineHeight, Paint.FontMetricsInt fontMetricsInt) {
        int targetHeight = LINE_HEIGHT;
        int descentBottom = fontMetricsInt.descent + lineHeight;
        int ascent = fontMetricsInt.ascent;
        int extraAscent = targetHeight - ((descentBottom - ascent) - spanstartv);
        if (extraAscent > 0) {
            fontMetricsInt.ascent = ascent - extraAscent;
        }
        int bottomEdge = lineHeight + fontMetricsInt.bottom;
        int top = fontMetricsInt.top;
        int extraTop = targetHeight - ((bottomEdge - top) - spanstartv);
        if (extraTop > 0) {
            fontMetricsInt.top = top - extraTop;
        }
    }

    @Override // android.text.style.ReplacementSpan
    public void draw(Canvas canvas, CharSequence charSequence, int start, int end, float x, int top, int y, int bottom, Paint paint) {
    }

    @Override // android.text.style.LineBackgroundSpan
    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence charSequence, int start, int end, int lineNumber) {
        this.width = right - left;
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(this.bgColor);
        float leftF = left;
        float bottomF = bottom;
        canvas.drawRoundRect(leftF, top + PADDING, right, bottomF, SizeHelper.dpToPx(12), SizeHelper.dpToPxFloat(12), this.paint);
        Drawable drawable = this.drawable;
        if ((drawable instanceof BitmapDrawable) && ((BitmapDrawable) drawable).getBitmap() != null && !((BitmapDrawable) this.drawable).getBitmap().isRecycled()) {
            this.paint.setStyle(Paint.Style.FILL);
            this.paint.setColor(MonetUtil.getAccent2BackgroundColorAuto());
            this.rectF.set(leftF, top + PADDING, IMAGE_SIZE + left, bottomF);
            this.path.addRoundRect(this.rectF, this.cornerRadii, Path.Direction.CW);
            canvas.drawPath(this.path, this.paint);
            this.drawable.setTintList(ColorStateList.valueOf(MonetUtil.getAccent2TextColorAuto()));
            int inset = SizeHelper.dpToPx(14);
            this.drawable.setBounds(inset + left, top + PADDING + inset, (IMAGE_SIZE + left) - inset, bottom - inset);
            this.drawable.draw(canvas);
        }
        drawLabel(paint, canvas, left, right, top, bottom, start, end);
    }

    @Override // com.bumptech.glide.request.target.Target
    public void onLoadFailed(Drawable drawable) {
        setDrawable(null);
        invalidate();
        clearTextView();
        Logger.verbose(TAG,"Load failed: " + this);
    }

    @Override // com.bumptech.glide.request.target.Target
    public final void getSize(SizeReadyCallback sizeReadyCallback) {
        int size = IMAGE_SIZE;
        sizeReadyCallback.onSizeReady(size, size);
    }

    @Override // android.text.style.ReplacementSpan
    public int getSize(Paint paint, CharSequence charSequence, int start, int end, Paint.FontMetricsInt fontMetricsInt) {
        return this.width;
    }

    public int getResId() {
        return this.resId.intValue();
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

    public boolean isSettingsShortcut() {
        return StringUtils.startsWith(this.label, "Settings shortcut");
    }

    @Override // com.bumptech.glide.request.target.Target
    public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
        if (drawable instanceof BitmapDrawable) {
            setDrawable(drawable);
        }
        Logger.verbose(TAG,"Loaded: " + drawable);
        invalidate();
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onDestroy() {
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onStop() {
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public void setTextView(TextView textView) {
        this.textViewRef = new WeakReference<>(textView);
        if (InlineImageSpan.shouldHighlight(textView)) {
            this.bgColor = InlineImageSpan.getHighlightColor();
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long when) {
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
    }
}
