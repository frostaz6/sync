package com.resync.app.ui.views.images;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;
import com.bumptech.glide.Glide;
import com.resync.app.RedditApplication;
import com.resync.app.widgets.RecyclableView;

/* JADX INFO: loaded from: classes3.dex */
public class BackgroundImageView extends AppCompatImageView implements RecyclableView {

    Paint backgroundPaint;

    public BackgroundImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.backgroundPaint = new Paint();
    }

    public int getBackgroundColor() {
        return -16777216;
    }

    public boolean hasValidBitmap() {
        return (!(getDrawable() instanceof BitmapDrawable) || ((BitmapDrawable) getDrawable()).getBitmap() == null || ((BitmapDrawable) getDrawable()).getBitmap().isRecycled()) ? false : true;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(0.0f, 0.0f, getWidth(), getHeight(), this.backgroundPaint);
    }

    @Override // com.resync.app.widgets.RecyclableView
    public final void recycle() {
        Glide.with(RedditApplication.getAppContext()).clear(this);
        setAlpha(1.0f);
        setImageDrawable(null);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        if (hasValidBitmap()) {
            super.onDraw(canvas);
        }
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        this.backgroundPaint.setColor(getBackgroundColor());
    }
}
