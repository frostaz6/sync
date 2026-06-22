package com.resync.app.ui.views.responsive;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.google.android.material.card.MaterialCardView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: loaded from: classes3.dex */
public class CustomMaterialCardView extends MaterialCardView {
    private Paint paint;
    private int backgroundColor;
    private boolean highlight;
    private boolean highlightEnabled;

    public CustomMaterialCardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.backgroundColor = 0;
        if (Build.VERSION.SDK_INT >= 29) {
            setForceDarkAllowed(false);
        }
        if (getMaxCardElevation() == 0.0f) {
            init(0);
        } else {
            init(12);
        }
    }

    private void init(int radiusDp) {
        setRadius(SizeHelper.dpToPxFloat(getContext(), radiusDp));
        this.paint = new Paint();
        applyBackgroundColor();
        setCardElevation(0.0f);
        setElevation(0.0f);
        setRippleColor(ColorStateList.valueOf(1433892727));
        setStrokeWidth(0);
    }

    public int resolveBackgroundColor(boolean highlightEnabled) {
        if (!this.highlight || NightHelper.isNight()) {
            return MonetUtil.getBackgroundColorAuto();
        }
        return -10831;
    }

    public void applyBackgroundColor() {
        int color = resolveBackgroundColor(this.highlightEnabled);
        setCardBackgroundColor(color);
        this.backgroundColor = color;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (getForeground() != null) {
            getForeground().setHotspot(motionEvent.getX(), motionEvent.getY());
        }
        return super.onInterceptTouchEvent(motionEvent);
    }
}
