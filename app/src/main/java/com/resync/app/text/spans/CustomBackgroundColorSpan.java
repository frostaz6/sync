package com.resync.app.text.spans;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineBackgroundSpan;
import androidx.core.graphics.ColorUtils;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: mb.c */
/* JADX INFO: compiled from: CustomBackgroundColorSpan.java */
/* JADX INFO: loaded from: classes3.dex */
public class CustomBackgroundColorSpan implements LineBackgroundSpan, LeadingMarginSpan {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Paint paint = new Paint();
    private int indentLevel;

    public CustomBackgroundColorSpan(int spacing) {
        Logger.verbose("SPACING: " + spacing);
        this.indentLevel = spacing + (-1);
    }

    @Override // android.text.style.LineBackgroundSpan
    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence charSequence, int start, int end, int lineNumber) {
        int color = paint.getColor();
        this.paint.setColor(ColorUtils.setAlphaComponent(MonetUtil.getNeutralTextColorAuto(), 20));
        this.paint.setStyle(Paint.Style.FILL);
        float leftF = left;
        float topF = top;
        float bottomF = bottom;
        canvas.drawRect((SizeHelper.dpToPxFloat(16) - SizeHelper.dpToPx(4)) + leftF, topF, right, bottomF, this.paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(MonetUtil.getLinkColorAuto());
        canvas.drawRect(SizeHelper.dpToPxFloat(12) + leftF + (this.indentLevel * SizeHelper.dpToPx(8)), topF, SizeHelper.dpToPxFloat(12) + leftF + SizeHelper.dpToPx(4) + (this.indentLevel * SizeHelper.dpToPx(8)), bottomF, paint);
        paint.setColor(color);
    }

    @Override // android.text.style.LeadingMarginSpan
    public void drawLeadingMargin(Canvas canvas, Paint paint, int x, int dir, int top, int baseline, int bottom, CharSequence charSequence, int start, int end, boolean first, Layout layout) {
    }

    @Override // android.text.style.LeadingMarginSpan
    public int getLeadingMargin(boolean first) {
        return SizeHelper.dpToPx(8) + SizeHelper.dpToPx(16) + (this.indentLevel * SizeHelper.dpToPx(8));
    }
}
