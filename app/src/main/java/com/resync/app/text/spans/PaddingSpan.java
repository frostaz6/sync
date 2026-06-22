package com.resync.app.text.spans;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/* JADX INFO: renamed from: mb.h */
/* JADX INFO: compiled from: PaddingSpan.java */
/* JADX INFO: loaded from: classes3.dex */
public class PaddingSpan extends MetricAffectingSpan {
    @Override // android.text.style.CharacterStyle
    public void updateDrawState(TextPaint textPaint) {
        textPaint.setTextScaleX(1.025f);
    }

    @Override // android.text.style.MetricAffectingSpan
    public void updateMeasureState(TextPaint textPaint) {
        textPaint.setTextScaleX(1.025f);
    }
}
