package com.resync.app.ui.views.comments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import androidx.core.graphics.ColorUtils;
import com.resync.app.ui.views.comments.RowDecorator;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.MonetUtil;

/* JADX INFO: loaded from: classes3.dex */
public class CommentRow extends LinearLayout implements RowDecorator.RowDecoratorHolder {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    RowDecorator decorator;
    Paint highlightPaint;
    boolean highlighted;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    boolean highlightNew;

    public CommentRow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.highlighted = false;
        this.highlightNew = false;
        this.highlightPaint = new Paint();
        this.highlightPaint.setColor(ColorUtils.setAlphaComponent(MonetUtil.getLinkColorAuto(), 30));
        if (Build.VERSION.SDK_INT >= 29) {
            setForceDarkAllowed(false);
        }
        setBaselineAligned(false);
        this.decorator = new RowDecorator(context, this, false);
    }

    public void setHighlightNew(boolean highlightNew) {
        this.highlightNew = highlightNew && SettingsSingleton.getSettings().commentsHighlightNewBackground;
    }

    public void setup(int depth, int color, boolean highlighted, boolean drawLine, boolean drawDot) {
        this.highlighted = highlighted;
        this.decorator.setup(depth, color, drawLine, drawDot);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        this.decorator.draw(canvas);
        super.draw(canvas);
        if (this.highlighted) {
            canvas.drawRect(0.0f, 0.0f, getWidth(), getHeight(), this.highlightPaint);
        }
        if (this.highlightNew) {
            canvas.drawRect(0.0f, 0.0f, getWidth(), getHeight(), this.highlightPaint);
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        this.decorator.setSize(width, height);
        super.onSizeChanged(width, height, oldWidth, oldHeight);
    }
}
