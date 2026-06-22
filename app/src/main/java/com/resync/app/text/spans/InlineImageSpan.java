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
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.resync.app.ui.activities.SwipeActivity;
import com.resync.app.ui.views.text.spannable.children.HtmlTableView;
import com.resync.app.helpers.ui.MonetUtil;
import java.lang.ref.WeakReference;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: nb.d */
/* JADX INFO: compiled from: InlineImageSpan.java */
/* JADX INFO: loaded from: classes3.dex */
public class InlineImageSpan extends ReplacementSpan implements LineBackgroundSpan, LineHeightSpan, Target<Drawable>, Drawable.Callback, SyncSpan {
    static int IMAGE_SIZE = SizeHelper.dpToPx(56);
    static int PADDING;
    static int LINE_HEIGHT;

    private Paint paint;
    private Integer index;
    private String url;
    private Drawable drawable;
    private WeakReference<TextView> textViewRef;
    private Request request;
    int bgColor;
    private int width;
    final RectF rectF;
    final Path path;

    static {
        int padding = SizeHelper.dpToPx(12);
        PADDING = padding;
        LINE_HEIGHT = IMAGE_SIZE + padding;
    }

    public InlineImageSpan(String url) {
        this(url, null);
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
        String label;
        if (this.index != null) {
            label = "(" + this.index + ") " + getUrl();
        } else {
            label = getUrl();
        }
        if (isSettingsShortcut()) {
            label = label.split(":")[1].trim();
        }
        canvas.drawText((String) TextUtils.ellipsize(label, (TextPaint) paint, (right - (left + IMAGE_SIZE + SizeHelper.dpToPx(8))) - SizeHelper.dpToPx(8), TextUtils.TruncateAt.END), left + IMAGE_SIZE + SizeHelper.dpToPx(8), ((top + PADDING) + (IMAGE_SIZE / 2.0f)) - ((paint.descent() + paint.ascent()) / 2.0f), paint);
    }

    public static int getHighlightColor() {
        return MonetUtil.getPressedColor(NightHelper.isNight());
    }

    public static int getDefaultBgColor() {
        return MonetUtil.getCardColorAuto();
    }

    private void invalidate() {
        WeakReference<TextView> weakReference = this.textViewRef;
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        TextView textView = this.textViewRef.get();
        if (textView.isAttachedToWindow() && textView.isShown() && textView.isLaidOut() && !textView.isDirty()) {
            textView.invalidate();
            Logger.verbose("EfficientImageSpan", "No other spans were still pending. Invalidating: " + this);
            return;
        }
        Logger.verbose("EfficientImageSpan", "Skipped Invalidating: Shown: " + textView.isShown() + " - Attached: " + textView.isShown() + " - Laid out: " + textView.isLaidOut() + " - Dirty: " + textView.isDirty());
    }

    public static boolean shouldHighlight(TextView textView) {
        if (textView.getContext() instanceof SwipeActivity) {
            return true;
        }
        Logger.verbose("PARENT: " + textView.getParent());
        return (textView.getParent() instanceof HtmlTableView) && ((HtmlTableView) textView.getParent()).hasGradientBackground();
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
        if (drawable instanceof BitmapDrawable) {
            if (((BitmapDrawable) drawable).getBitmap() != null && !((BitmapDrawable) this.drawable).getBitmap().isRecycled()) {
                if (isSettingsShortcut()) {
                    this.paint.setStyle(Paint.Style.FILL);
                    this.paint.setColor(MonetUtil.getAccent2BackgroundColorAuto());
                    float cornerRadius = SizeHelper.dpToPx(12);
                    this.rectF.set(leftF, top + PADDING, IMAGE_SIZE + left, bottomF);
                    this.path.addRoundRect(this.rectF, new float[]{cornerRadius, cornerRadius, 0.0f, 0.0f, 0.0f, 0.0f, cornerRadius, cornerRadius}, Path.Direction.CW);
                    canvas.drawPath(this.path, this.paint);
                    this.drawable.setTintList(ColorStateList.valueOf(MonetUtil.getAccent2TextColorAuto()));
                    int inset = SizeHelper.dpToPx(8);
                    this.drawable.setBounds(inset + left, top + PADDING + inset, (IMAGE_SIZE + left) - inset, bottom - inset);
                    this.drawable.draw(canvas);
                } else {
                    this.drawable.setBounds(left, top + PADDING, IMAGE_SIZE + left, bottom);
                    this.drawable.draw(canvas);
                }
            }
        } else if ((drawable instanceof GifDrawable) && ((GifDrawable) drawable).isRunning()) {
            try {
                this.drawable.setBounds(left, top + PADDING, IMAGE_SIZE + left, bottom);
                this.drawable.draw(canvas);
            } catch (Exception e) {
                Logger.logException(e);
            }
        }
        drawLabel(paint, canvas, left, right, top, bottom, start, end);
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
        int size = IMAGE_SIZE;
        sizeReadyCallback.onSizeReady(size, size);
    }

    @Override // android.text.style.ReplacementSpan
    public int getSize(Paint paint, CharSequence charSequence, int start, int end, Paint.FontMetricsInt fontMetricsInt) {
        return this.width;
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

    public String getUrl() {
        return this.url;
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onDestroy() {
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onStop() {
    }

    public boolean isSettingsShortcut() {
        return StringUtils.startsWith(getUrl(), "Settings shortcut");
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
        Logger.verbose("InlineImageSpan", "Loaded: " + drawable);
        invalidate();
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public void setTextView(TextView textView) {
        this.textViewRef = new WeakReference<>(textView);
        if (shouldHighlight(textView)) {
            this.bgColor = getHighlightColor();
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long when) {
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
    }

    public InlineImageSpan(String url, Integer index) {
        this.rectF = new RectF();
        this.path = new Path();
        this.url = url;
        this.index = index;
        this.paint = new Paint();
        this.bgColor = getDefaultBgColor();
    }
}
