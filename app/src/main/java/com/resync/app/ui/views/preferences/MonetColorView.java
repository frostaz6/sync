package com.resync.app.ui.views.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import com.resync.app.R;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.helpers.PaddingHelper;

/* JADX INFO: loaded from: classes3.dex */
public class MonetColorView extends View {

    int color;
    Paint paint;
    Drawable checkDrawable;

    public MonetColorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, SyncStyleable.MONET_COLOR_VIEW);
        if (!typedArray.hasValue(0)) {
            throw new RuntimeException("The swatch must have a color");
        }
        this.color = Color.parseColor(typedArray.getString(0));
        typedArray.recycle();
        this.checkDrawable = getResources().getDrawable(R.drawable.outline_check_24);
        init();
    }

    public int getColor() {
        return this.paint.getColor();
    }

    public void init() {
        PaddingHelper.setPaddingAll(this, 16);
        setWillNotDraw(false);
        Paint paint = new Paint();
        this.paint = paint;
        paint.setAntiAlias(true);
        this.paint.setColor(this.color);
        this.paint.setStyle(Paint.Style.FILL);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, this.paint);
        if (isSelected()) {
            int width = getWidth() / 5;
            this.checkDrawable.setBounds(width, width, getWidth() - width, getHeight() - width);
            this.checkDrawable.draw(canvas);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        setMeasuredDimension(measuredWidth, measuredWidth);
    }
}
