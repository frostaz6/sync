package com.resync.app.text.spans;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: renamed from: mb.i */
/* JADX INFO: compiled from: RadiusBgSpan.java */
/* JADX INFO: loaded from: classes3.dex */
public class RadiusBgSpan extends ReplacementSpan {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int width;
    private int fillColor;
    private int strokeColor;
    private int textColor;
    private int cornerRadius;
    private Paint paint = new Paint();

    public RadiusBgSpan(int fillColor, int strokeColor, int textColor, int cornerRadius) {
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.textColor = textColor;
        this.cornerRadius = cornerRadius;
    }

    @Override // android.text.style.ReplacementSpan
    public void draw(Canvas canvas, CharSequence charSequence, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        this.paint.setAntiAlias(true);
        this.paint.setColor(this.fillColor);
        this.paint.setStyle(Paint.Style.FILL);
        float baseline = y;
        RectF rectF = new RectF(x, paint.ascent() + baseline, this.width + x, paint.descent() + baseline);
        int radius = this.cornerRadius;
        canvas.drawRoundRect(rectF, radius, radius, this.paint);
        int stroke = this.strokeColor;
        if (stroke != this.fillColor) {
            this.paint.setColor(stroke);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(SizeHelper.dpToPxFloat(1));
            RectF rectF2 = new RectF(x, paint.ascent() + baseline, this.width + x, paint.descent() + baseline);
            int strokeRadius = this.cornerRadius;
            canvas.drawRoundRect(rectF2, strokeRadius, strokeRadius, this.paint);
        }
        paint.setColor(this.textColor);
        canvas.drawText(charSequence, start, end, x + (((int) (this.width - paint.measureText(charSequence.subSequence(start, end).toString()))) / 2.0f), baseline, paint);
    }

    @Override // android.text.style.ReplacementSpan
    public int getSize(Paint paint, CharSequence charSequence, int start, int end, Paint.FontMetricsInt fontMetricsInt) {
        int measuredWidth = ((int) paint.measureText(charSequence, start, end)) + SizeHelper.dpToPx(4);
        this.width = measuredWidth;
        return measuredWidth;
    }
}
