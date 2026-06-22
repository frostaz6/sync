package com.resync.app.helpers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.MonetUtil;

/* JADX INFO: renamed from: t7.a */
/* JADX INFO: compiled from: ActionBarHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class ActionBarHelper {
    public static int darken(int color, float factor) {
        return Color.rgb(Math.max(0, (int) (Color.red(color) * factor)), Math.max(0, (int) (Color.green(color) * factor)), Math.max(0, (int) (Color.blue(color) * factor)));
    }

    public static int getToolbarColor() {
        int color = MonetUtil.getToolbarColorAuto();
        if (color != -1 || Build.VERSION.SDK_INT > 26) {
            return color;
        }
        return -16777216;
    }

    public static int getToolbarColor(boolean night) {
        int color = MonetUtil.getToolbarColor(night);
        if (color != -1 || Build.VERSION.SDK_INT > 26) {
            return color;
        }
        return -16777216;
    }

    public static Drawable createScrimDrawable(Context context) {
        int scrimColor = darken(SettingsSingleton.getInstance().getCurrentSettings().primaryColor, 0.6f);
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.getPaint().setColor(scrimColor);
        return new LayerDrawable(new Drawable[]{new ClipDrawable(shapeDrawable, 3, 1)});
    }
}
