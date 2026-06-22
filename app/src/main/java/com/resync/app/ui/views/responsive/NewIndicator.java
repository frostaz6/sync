package com.resync.app.ui.views.responsive;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import com.resync.app.helpers.ui.MonetUtil;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.util.ColorUtils;

/* JADX INFO: loaded from: classes3.dex */
public class NewIndicator extends AppCompatTextView {
    Paint paint;

    RectF rect;

    public NewIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int padding = (int) SizeHelper.dpToPxFloat(4);
        setPadding(padding, 0, padding, 0);
        this.paint = new Paint();
        this.rect = new RectF(0.0f, 0.0f, getWidth(), getHeight());
        this.paint.setColor(MonetUtil.getLinkColorAuto());
        setTextColor(ColorUtils.isDark(MonetUtil.getLinkColorAuto()) ^ true ? -16777216 : -1);
        setText(StringUtils.SPACE);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(this.rect.centerX(), this.rect.centerY(), this.rect.width() / 4.0f, this.paint);
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
