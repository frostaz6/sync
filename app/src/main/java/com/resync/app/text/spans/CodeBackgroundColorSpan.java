package com.resync.app.text.spans;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineBackgroundSpan;
import androidx.core.graphics.ColorUtils;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: renamed from: mb.b */
/* JADX INFO: compiled from: CodeBackgroundColorSpan.java */
/* JADX INFO: loaded from: classes3.dex */
public class CodeBackgroundColorSpan implements LineBackgroundSpan, LeadingMarginSpan {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Paint paint;
    private final Path path;

    public CodeBackgroundColorSpan() {
        Paint paint = new Paint();
        this.paint = paint;
        paint.setColor(ColorUtils.setAlphaComponent(MonetUtil.getToolbarColorAuto(), 168));
        this.path = new Path();
    }

    @Override // android.text.style.LineBackgroundSpan
    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence charSequence, int start, int end, int lineNumber) {
        if (charSequence instanceof Spanned) {
            Spanned spanned = (Spanned) charSequence;
            int spanStart = spanned.getSpanStart(this);
            int spanEnd = spanned.getSpanEnd(this);
            boolean isLastLine = true;
            boolean isFirstLine = start == spanStart;
            if (end != spanEnd && end <= spanEnd) {
                isLastLine = false;
            }
            int cornerRadius = bottom - top;
            int leftInset = left + cornerRadius;
            int rightInset = right - cornerRadius;
            this.path.rewind();
            if (isFirstLine) {
                int cornerTop = bottom - cornerRadius;
                float leftInsetF = leftInset;
                float cornerTopF = cornerTop;
                this.path.moveTo(leftInsetF, cornerTopF);
                float leftF = left;
                float bottomF = bottom;
                this.path.quadTo(leftF, cornerTopF, leftF, bottomF);
                float rightF = right;
                this.path.lineTo(rightF, bottomF);
                this.path.quadTo(rightF, cornerTopF, rightInset, cornerTopF);
                this.path.lineTo(leftInsetF, cornerTopF);
                canvas.drawPath(this.path, this.paint);
                return;
            }
            if (!isLastLine) {
                canvas.drawRect(left, top, right, bottom, this.paint);
                return;
            }
            int cornerBottom = cornerRadius + top;
            float leftF = left;
            float topF = top;
            this.path.moveTo(leftF, topF);
            float cornerBottomF = cornerBottom;
            this.path.quadTo(leftF, cornerBottomF, leftInset, cornerBottomF);
            this.path.lineTo(rightInset, cornerBottomF);
            float rightF = right;
            this.path.quadTo(rightF, cornerBottomF, rightF, topF);
            this.path.lineTo(leftF, topF);
            canvas.drawPath(this.path, this.paint);
        }
    }

    @Override // android.text.style.LeadingMarginSpan
    public void drawLeadingMargin(Canvas canvas, Paint paint, int x, int dir, int top, int baseline, int bottom, CharSequence charSequence, int start, int end, boolean first, Layout layout) {
    }

    @Override // android.text.style.LeadingMarginSpan
    public int getLeadingMargin(boolean first) {
        return SizeHelper.dpToPx(12);
    }
}
