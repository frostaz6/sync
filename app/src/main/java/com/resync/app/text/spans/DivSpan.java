package com.resync.app.text.spans;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

/* JADX INFO: renamed from: mb.e */
/* JADX INFO: compiled from: DivSpan.java */
/* JADX INFO: loaded from: classes3.dex */
public class DivSpan extends ReplacementSpan {
    @Override // android.text.style.ReplacementSpan
    public void draw(Canvas canvas, CharSequence charSequence, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        canvas.drawRect(new RectF(x, (top + bottom) / 2, canvas.getWidth() + x, ((top + bottom) / 2) + 3), paint);
    }

    @Override // android.text.style.ReplacementSpan
    public int getSize(Paint paint, CharSequence charSequence, int start, int end, Paint.FontMetricsInt fontMetricsInt) {
        return 0;
    }
}
