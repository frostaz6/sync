package com.resync.app.ui.viewholders.onboarding;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.internal.ViewUtils;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class OnboardingRelativeLayout extends RelativeLayout implements DarkModeAware {

    Paint paint;
    int insetTop;
    int insetBottom;

    public OnboardingRelativeLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.paint = new Paint();
        setWillNotDraw(false);
        ViewUtils.doOnApplyWindowInsets(this, new ViewUtils.OnApplyWindowInsetsListener() { // from class: rb.a
            @Override // com.google.android.material.internal.ViewUtils.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
                return OnboardingRelativeLayout.this.applyWindowInsets(view, windowInsetsCompat, relativePadding);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ WindowInsetsCompat applyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
        this.insetTop = windowInsetsCompat.getSystemWindowInsetTop();
        int insetBottom = windowInsetsCompat.getSystemWindowInsetBottom();
        this.insetBottom = insetBottom;
        setPadding(0, this.insetTop, 0, insetBottom);
        invalidate();
        return windowInsetsCompat;
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.paint.setColor(MonetUtil.getWindowBackgroundColorAuto());
        canvas.drawRect(0.0f, 0.0f, getWidth(), getHeight(), this.paint);
    }
}
