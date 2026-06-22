package com.resync.app.ui.views.transparent_status;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class CustomCoordinatorLayout extends CoordinatorLayout implements DarkModeAware {
    public Paint statusPaint;
    public Paint dividerPaint;
    private int insetTop;
    private boolean useWindowBackground;

    class InsetListener implements View.OnApplyWindowInsetsListener {
        InsetListener() {
        }

        @Override // android.view.View.OnApplyWindowInsetsListener
        public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
            CustomCoordinatorLayout.this.insetTop = windowInsets.getSystemWindowInsetTop();
            return windowInsets;
        }
    }

    public CustomCoordinatorLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.statusPaint = new Paint();
        this.dividerPaint = new Paint();
        this.insetTop = 0;
        applyColors();
        this.dividerPaint.setAlpha(0);
        setWillNotDraw(false);
        setOnApplyWindowInsetsListener(new InsetListener());
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (SettingsSingleton.getSettings().transparentStatus) {
            return;
        }
        canvas.drawRect(0.0f, 0.0f, getWidth(), this.insetTop, this.statusPaint);
        if (this.statusPaint.getAlpha() == 255) {
            canvas.drawRect(0.0f, 0.0f, getWidth(), this.insetTop, this.dividerPaint);
        }
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }

    public void markWindowBackground() {
        this.useWindowBackground = true;
        applyColors();
    }

    public void setStatusAlpha(float alpha) {
        int alphaInt;
        if (SettingsSingleton.getSettings().transparentStatus || this.statusPaint.getAlpha() == (alphaInt = (int) (alpha * 255.0f))) {
            return;
        }
        this.statusPaint.setAlpha(alphaInt);
        invalidate();
    }

    public void setDividerAlpha(float alpha) {
        int alphaInt;
        if (SettingsSingleton.getSettings().transparentStatus || this.dividerPaint.getAlpha() == (alphaInt = (int) (alpha * 255.0f))) {
            return;
        }
        this.dividerPaint.setAlpha(alphaInt);
        invalidate();
    }

    public void applyColors() {
        int alpha = this.statusPaint.getAlpha();
        if (this.useWindowBackground) {
            this.statusPaint.setColor(MonetUtil.getWindowBackgroundColorAuto());
        } else {
            this.statusPaint.setColor(MonetUtil.getBackgroundColorAuto());
        }
        this.statusPaint.setAlpha(alpha);
        int alpha2 = this.dividerPaint.getAlpha();
        if (this.useWindowBackground) {
            this.dividerPaint.setColor(MonetUtil.getDividerColorAuto());
        } else {
            this.dividerPaint.setColor(MonetUtil.getToolbarColorAuto());
        }
        this.dividerPaint.setAlpha(alpha2);
    }
}
