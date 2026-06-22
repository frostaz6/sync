package com.resync.app.text.spans;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import com.resync.app.RedditApplication;

/* JADX INFO: renamed from: mb.a */
/* JADX INFO: compiled from: AwardImageSpan.java */
/* JADX INFO: loaded from: classes3.dex */
public class AwardImageSpan extends ImageSpan {
    public AwardImageSpan() {
        super(RedditApplication.awardBitmap);
    }

    @Override // android.text.style.DynamicDrawableSpan, android.text.style.ReplacementSpan
    public void draw(Canvas canvas, CharSequence charSequence, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        canvas.save();
        canvas.translate(x, (((bottom - top) - drawable.getBounds().bottom) / 2) + top);
        drawable.draw(canvas);
        canvas.restore();
    }

    @Override // android.text.style.DynamicDrawableSpan, android.text.style.ReplacementSpan
    public int getSize(Paint paint, CharSequence charSequence, int start, int end, Paint.FontMetricsInt fontMetricsInt) {
        Rect bounds = getDrawable().getBounds();
        if (fontMetricsInt != null) {
            Paint.FontMetricsInt paintMetrics = paint.getFontMetricsInt();
            int lineHeight = paintMetrics.bottom - paintMetrics.top;
            int halfBounds = (bounds.bottom - bounds.top) / 2;
            int quarterLine = lineHeight / 4;
            int descent = halfBounds - quarterLine;
            int ascent = -(halfBounds + quarterLine);
            fontMetricsInt.ascent = ascent;
            fontMetricsInt.top = ascent;
            fontMetricsInt.bottom = descent;
            fontMetricsInt.descent = descent;
        }
        return bounds.right;
    }
}
