package com.resync.app.util;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import com.resync.app.RedditApplication;
import com.resync.app.helpers.ui.MonetUtil;

/* JADX INFO: renamed from: wc.k */
/* JADX INFO: compiled from: RippleUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public final class RippleUtils {
    public static Drawable getSelectableBackground() {
        TypedValue typedValue = new TypedValue();
        RedditApplication.getAppContext().getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        Drawable drawable = RedditApplication.getAppContext().getDrawable(typedValue.resourceId);
        if (drawable instanceof StateListDrawable) {
            ((StateListDrawable) drawable).setTint(MonetUtil.getRippleColorAuto());
        } else if (drawable instanceof RippleDrawable) {
            ((RippleDrawable) drawable).setColor(ColorStateList.valueOf(MonetUtil.getRippleColorAuto()));
        }
        return drawable;
    }

    public static Drawable getSelectableBackground(int color) {
        TypedValue typedValue = new TypedValue();
        RedditApplication.getAppContext().getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        Drawable drawable = RedditApplication.getAppContext().getDrawable(typedValue.resourceId);
        if (drawable instanceof StateListDrawable) {
            ((StateListDrawable) drawable).setTint(color);
        } else if (drawable instanceof RippleDrawable) {
            ((RippleDrawable) drawable).setColor(ColorStateList.valueOf(color));
        }
        return drawable;
    }

    public static RippleDrawable getNoBoundsRipple() {
        RippleDrawable rippleDrawable = (RippleDrawable) RedditApplication.getAppContext().getResources().getDrawable(com.resync.app.R.drawable.ripple_no_bounds);
        rippleDrawable.setColor(ColorStateList.valueOf(MonetUtil.getRippleColorAuto()));
        return rippleDrawable;
    }

    public static Drawable getRoundedCornersRipple(Context context) {
        RippleDrawable rippleDrawable = (RippleDrawable) context.getResources().getDrawable(com.resync.app.R.drawable.ripple_rounded_corners);
        rippleDrawable.setColor(ColorStateList.valueOf(MonetUtil.getRippleColorAuto()));
        return rippleDrawable;
    }
}
