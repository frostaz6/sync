package com.resync.app.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.ui.activities.BaseActivity;

/* JADX INFO: renamed from: t7.x0 */
/* JADX INFO: compiled from: WindowHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class WindowHelper {

    /* JADX INFO: renamed from: t7.x0$a */
    /* JADX INFO: compiled from: WindowHelper.java */
    public enum WindowSizeClass {
        COMPACT,
        MEDIUM,
        EXPANDED
    }

    public static WindowSizeClass getWindowSizeClass() {
        if (isPortrait()) {
            float widthDp = WindowUtils.getDisplayWidth() / RedditApplication.getAppContext().getResources().getDisplayMetrics().density;
            return widthDp < 600.0f ? WindowSizeClass.COMPACT : widthDp < 840.0f ? WindowSizeClass.MEDIUM : WindowSizeClass.EXPANDED;
        }
        float heightDp = WindowUtils.getDisplayHeight() / RedditApplication.getAppContext().getResources().getDisplayMetrics().density;
        return heightDp < 480.0f ? WindowSizeClass.COMPACT : heightDp < 900.0f ? WindowSizeClass.MEDIUM : WindowSizeClass.EXPANDED;
    }

    public static int getActionBarHeight(Context context) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
            return TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources().getDisplayMetrics());
        }
        return 0;
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (identifier > 0) {
            return resources.getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public static boolean isInMultiWindow(Context context) {
        if (Build.VERSION.SDK_INT < 24 || !(context instanceof BaseActivity)) {
            return false;
        }
        return ((BaseActivity) context).isInMultiWindowMode();
    }

    public static boolean isLandscape(Context context) {
        return context.getResources().getBoolean(R.bool.landscape);
    }

    public static boolean isPortrait() {
        return !RedditApplication.getAppContext().getResources().getBoolean(R.bool.landscape);
    }
}
