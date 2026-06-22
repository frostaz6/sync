package com.resync.app.ui.views.responsive;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: loaded from: classes3.dex */
public class HideIndicator extends AppCompatTextView {
    Paint paint;

    RectF rect;

    public HideIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int padding = (int) SizeHelper.dpToPxFloat(5);
        setPadding(padding, 0, padding, 0);
        this.paint = new Paint();
        this.rect = new RectF(0.0f, 0.0f, getWidth(), getHeight());
        setTextColor(MonetUtil.getAccent2ColorAuto());
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        this.paint.setColor(MonetUtil.getAccent2ActiveColorAuto());
        canvas.drawRoundRect(this.rect, 20.0f, 20.0f, this.paint);
        super.onDraw(canvas);
    }

    @Override // android.view.View
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        RectF rectF = this.rect;
        rectF.right = width;
        rectF.bottom = height;
    }
}
