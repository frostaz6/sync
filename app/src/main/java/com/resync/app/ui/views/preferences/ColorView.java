package com.resync.app.ui.views.preferences;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/* JADX INFO: loaded from: classes3.dex */
public class ColorView extends View {

    boolean light;
    int color;
    Paint paint;

    public ColorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public int getColor() {
        return this.color;
    }

    public void init() {
        setWillNotDraw(false);
        Paint paint = new Paint();
        this.paint = paint;
        paint.setAntiAlias(true);
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.paint.setColor(this.color);
        this.paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, this.paint);
        if (this.light) {
            this.paint.setColor(-1);
        } else {
            this.paint.setColor(-16777216);
        }
    }
}
