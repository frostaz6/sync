package com.resync.app.ui.util.spans;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

/* JADX INFO: loaded from: classes3.dex */
public class CustomTypefaceSpan extends TypefaceSpan {

    private final Typeface typeface;
    private final float textScale;
    private int color;

    public CustomTypefaceSpan(String str, Typeface typeface) {
        super(str);
        this.color = 0;
        this.typeface = typeface;
        this.textScale = 1.0f;
    }

    private void applyTypeface(Paint paint, Typeface typeface) {
        Typeface typeface2 = paint.getTypeface();
        if (((typeface2 == null ? 0 : typeface2.getStyle()) & (~typeface.getStyle()) & 1) != 0) {
            paint.setFakeBoldText(true);
        }
        paint.setTypeface(typeface);
    }

    @Override // android.text.style.TypefaceSpan, android.text.style.CharacterStyle
    public void updateDrawState(TextPaint textPaint) {
        textPaint.setTextSize(textPaint.getTextSize() * this.textScale);
        int color = this.color;
        if (color != 0) {
            textPaint.setColor(color);
        }
        applyTypeface(textPaint, this.typeface);
    }

    @Override // android.text.style.TypefaceSpan, android.text.style.MetricAffectingSpan
    public void updateMeasureState(TextPaint textPaint) {
        applyTypeface(textPaint, this.typeface);
        textPaint.setTextSize(textPaint.getTextSize() * this.textScale);
    }
}
