package com.resync.app.helpers.ui;

import android.R;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.color.CorePalette;
import com.resync.app.color.Palette;
import com.resync.app.color.MonetPalette;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.SettingsSingleton;
import okhttp3.internal.http.HttpStatusCodesKt;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.widgets.ColorFilterProvider;
import com.resync.app.helpers.NightHelper;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: gb.i */
/* JADX INFO: compiled from: MonetUtil.java */
/* JADX INFO: loaded from: classes2.dex */
public final class MonetUtil {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static Palette palette = buildPalette();

    public static int getAccent2TextColor(boolean night) {
        return night ? palette.accent2(100) : palette.accent2(800);
    }

    public static int getAccent2ColorAuto() {
        return getAccent2Color(NightHelper.isNight());
    }

    public static int getAccent2Color(boolean night) {
        return night ? palette.accent2(100) : palette.accent2(900);
    }

    public static int getPrimaryTextColorAuto() {
        return getPrimaryTextColor(NightHelper.isNight());
    }

    public static int getPrimaryTextColor(boolean night) {
        return night ? SettingsSingleton.getSettings().monet_override_dark_primary_text_color != 0 ? SettingsSingleton.getSettings().monet_override_dark_primary_text_color : palette.neutral1(100) : SettingsSingleton.getSettings().monet_override_light_primary_text_color != 0 ? SettingsSingleton.getSettings().monet_override_light_primary_text_color : palette.neutral1(1000);
    }

    public static int getToolbarColorAuto() {
        return getToolbarColor(NightHelper.isNight());
    }

    public static int getToolbarColor(boolean night) {
        if (!night) {
            return SettingsSingleton.getSettings().monet_custom_palette ? palette.neutral1(25) : SettingsSingleton.getSettings().monet_boost_light_color ? blend(palette.accent2(100), palette.accent2(HttpStatusCodesKt.HTTP_OK), 0.5f) : blend(palette.neutral2(50), palette.neutral2(100), 0.95f);
        }
        if (SettingsSingleton.getSettings().monet_custom_palette) {
            return palette.neutral1(700);
        }
        if (SettingsSingleton.getSettings().monetForceAmoled) {
            return -16777216;
        }
        return SettingsSingleton.getSettings().monet_boost_dark_color ? blend(palette.accent2(800), palette.accent2(900), 0.3f) : blend(palette.neutral1(800), palette.neutral1(900), 0.3f);
    }

    public static int getRippleColorAuto() {
        return getRippleColor(NightHelper.isNight());
    }

    public static int getRippleColor(boolean night) {
        return 1433892727;
    }

    public static Palette getPalette() {
        return palette;
    }

    public static int getSecondaryTextColorAuto() {
        return getSecondaryTextColor(NightHelper.isNight());
    }

    public static int getSecondaryTextColor(boolean night) {
        return night ? SettingsSingleton.getSettings().monet_override_dark_secondary_text_color != 0 ? SettingsSingleton.getSettings().monet_override_dark_secondary_text_color : getNeutralTextColor(night) : SettingsSingleton.getSettings().monet_override_light_secondary_text_color != 0 ? SettingsSingleton.getSettings().monet_override_light_secondary_text_color : getNeutralTextColor(night);
    }

    public static int getAccent2ActiveColorAuto() {
        return getAccent2ActiveColor(NightHelper.isNight());
    }

    public static int getAccent2ActiveColor(boolean night) {
        return night ? palette.accent2(700) : palette.accent2(HttpStatusCodesKt.HTTP_OK);
    }

    public static int getSystemAccentColor() {
        if (!SettingsSingleton.getSettings().monet_system) {
            return 0;
        }
        int color = RedditApplication.getAppContext().getColor(R.color.system_accent1_700);
        Logger.verbose("Using monet system: " + color);
        return color;
    }

    public static int getWindowBackgroundColorAuto() {
        return getWindowBackgroundColor(NightHelper.isNight());
    }

    public static int getWindowBackgroundColor(boolean night) {
        if (!night) {
            return SettingsSingleton.getSettings().monet_custom_palette ? palette.neutral1(100) : SettingsSingleton.getSettings().monet_boost_light_color ? palette.accent2(100) : blend(palette.neutral1(50), palette.neutral1(100), 0.15f);
        }
        if (SettingsSingleton.getSettings().monet_custom_palette) {
            return palette.neutral1(900);
        }
        if (SettingsSingleton.getSettings().monetForceAmoled) {
            return -16777216;
        }
        return SettingsSingleton.getSettings().monet_boost_dark_color ? blend(palette.accent2(1000), palette.accent2(900), 0.75f) : blend(palette.neutral1(1000), palette.neutral1(900), 0.75f);
    }

    public static boolean isAmoled() {
        return SettingsSingleton.getSettings().monetForceAmoled && NightHelper.isNight();
    }

    public static void reload() {
        Logger.verbose("Reloading monet");
        palette = buildPalette();
    }

    private static Palette buildPalette() {
        if (!SettingsSingleton.getSettings().monet_system) {
            return CorePalette.of(SettingsSingleton.getSettings().monet_manual_theme_color, SettingsSingleton.getSettings().monet_color_intensity);
        }
        try {
            return new MonetPalette(RedditApplication.getAppContext());
        } catch (Exception e) {
            Logger.logException(e);
            return CorePalette.of(-14405314, 1.0f);
        }
    }

    public static void disableOverScrollPreS(RecyclerView recyclerView) {
        if (Build.VERSION.SDK_INT < 31) {
            recyclerView.setOverScrollMode(2);
        }
    }

    public static void tintMenu(Menu menu, int color) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.getIcon() != null) {
                Drawable drawableMutate = item.getIcon().mutate();
                drawableMutate.setColorFilter(ColorFilterProvider.getColorFilter(color));
                item.setIcon(drawableMutate);
            }
        }
    }

    private static int blend(int color1, int color2, float ratio) {
        return ColorUtils.blendARGB(color1, color2, ratio);
    }

    public static int getProgressColorAuto() {
        return getProgressColor(NightHelper.isNight());
    }

    public static int getProgressColor(boolean night) {
        return night ? palette.accent1(700) : palette.accent1(600);
    }

    public static int getDividerColorAuto() {
        return getDividerColor(NightHelper.isNight());
    }

    public static int getDividerColor(boolean night) {
        return night ? blend(palette.neutral2(800), palette.neutral2(900), 0.3f) : blend(palette.neutral1(100), palette.neutral1(HttpStatusCodesKt.HTTP_OK), 0.5f);
    }

    public static int getBackgroundColorAuto() {
        return getBackgroundColor(NightHelper.isNight());
    }

    public static int getBackgroundColor(boolean night) {
        if (!night) {
            return SettingsSingleton.getSettings().monet_custom_palette ? palette.neutral1(50) : SettingsSingleton.getSettings().monet_boost_light_color ? blend(palette.accent2(10), palette.accent2(50), 0.75f) : palette.neutral1(10);
        }
        if (SettingsSingleton.getSettings().monet_custom_palette) {
            return palette.neutral1(800);
        }
        if (SettingsSingleton.getSettings().monetForceAmoled) {
            return -16777216;
        }
        return SettingsSingleton.getSettings().monet_boost_dark_color ? blend(palette.accent2(900), palette.accent2(800), 0.1f) : blend(palette.neutral1(900), palette.neutral1(800), 0.1f);
    }

    public static int getCardColorAuto() {
        return getCardColor(NightHelper.isNight());
    }

    public static int getCardColor(boolean night) {
        return night ? SettingsSingleton.getSettings().monet_custom_palette ? palette.neutral1(900) : SettingsSingleton.getSettings().monetForceAmoled ? palette.neutral1(900) : SettingsSingleton.getSettings().monet_boost_dark_color ? blend(palette.accent2(800), palette.accent2(900), 0.25f) : blend(palette.neutral1(800), palette.neutral1(900), 0.25f) : SettingsSingleton.getSettings().monet_custom_palette ? palette.neutral1(100) : SettingsSingleton.getSettings().monet_boost_light_color ? palette.accent2(100) : palette.neutral1(50);
    }

    public static int getHighlightColorAuto() {
        return getHighlightColor(NightHelper.isNight());
    }

    public static int getHighlightColor(boolean night) {
        return palette.accent1(800);
    }

    public static int getPressedColor(boolean night) {
        return night ? blend(palette.neutral1(700), palette.neutral1(800), 0.8f) : blend(palette.neutral1(50), palette.neutral1(100), 0.9f);
    }

    public static int getAccent2BackgroundColorAuto() {
        return getAccent2BackgroundColor(NightHelper.isNight());
    }

    public static int getAccent2BackgroundColor(boolean night) {
        return night ? SettingsSingleton.getSettings().monet_boost_dark_color ? palette.accent2(700) : palette.accent2(800) : SettingsSingleton.getSettings().monet_boost_light_color ? palette.accent2(HttpStatusCodesKt.HTTP_OK) : palette.accent2(HttpStatusCodesKt.HTTP_OK);
    }

    public static int getAccent3ColorAuto() {
        return getAccent3Color(NightHelper.isNight());
    }

    public static int getAccent3Color(boolean night) {
        return night ? palette.accent3(HttpStatusCodesKt.HTTP_INTERNAL_SERVER_ERROR) : palette.accent3(600);
    }

    public static int getLinkColorAuto() {
        return getLinkColor(NightHelper.isNight());
    }

    public static int getLinkColor(boolean night) {
        return night ? SettingsSingleton.getSettings().monet_custom_palette ? palette.accent1(HttpStatusCodesKt.HTTP_BAD_REQUEST) : SettingsSingleton.getSettings().monet_override_dark_link_color != 0 ? SettingsSingleton.getSettings().monet_override_dark_link_color : palette.accent1(HttpStatusCodesKt.HTTP_BAD_REQUEST) : SettingsSingleton.getSettings().monet_override_light_link_color != 0 ? SettingsSingleton.getSettings().monet_override_light_link_color : palette.accent1(HttpStatusCodesKt.HTTP_BAD_REQUEST);
    }

    public static int getConfigHash() {
        return (SettingsSingleton.getSettings().monet_manual_theme_color + StringUtils.SPACE + SettingsSingleton.getSettings().monet_color_intensity + StringUtils.SPACE + SettingsSingleton.getSettings().monet_boost_light_color + StringUtils.SPACE + SettingsSingleton.getSettings().monet_boost_dark_color + StringUtils.SPACE + SettingsSingleton.getSettings().monetForceAmoled + StringUtils.SPACE + SettingsSingleton.getSettings().monet_system + StringUtils.SPACE + SettingsSingleton.getSettings().monet_override_dark_link_color + StringUtils.SPACE + SettingsSingleton.getSettings().monet_override_dark_primary_text_color + StringUtils.SPACE + SettingsSingleton.getSettings().monet_override_dark_secondary_text_color + StringUtils.SPACE + SettingsSingleton.getSettings().monet_override_light_link_color + StringUtils.SPACE + SettingsSingleton.getSettings().monet_override_light_primary_text_color + StringUtils.SPACE + SettingsSingleton.getSettings().monet_override_light_secondary_text_color + StringUtils.SPACE + getSystemAccentColor() + StringUtils.SPACE + SettingsSingleton.getSettings().monet_custom_palette).hashCode();
    }

    public static int getNeutralTextColorAuto() {
        return getNeutralTextColor(NightHelper.isNight());
    }

    public static int getNeutralTextColor(boolean night) {
        return night ? palette.neutral1(HttpStatusCodesKt.HTTP_BAD_REQUEST) : palette.neutral1(HttpStatusCodesKt.HTTP_BAD_REQUEST);
    }

    public static int getAccentSurfaceColorAuto() {
        return getAccentSurfaceColor(NightHelper.isNight());
    }

    public static int getAccentSurfaceColor(boolean night) {
        return night ? palette.accent2(50) : palette.accent1(50);
    }

    public static int getTertiaryTextColorAuto() {
        return getTertiaryTextColor(NightHelper.isNight());
    }

    public static int getTertiaryTextColor(boolean night) {
        return night ? palette.neutral1(HttpStatusCodesKt.HTTP_OK) : palette.neutral1(700);
    }

    public static int getAccent2TextColorAuto() {
        return getAccent2TextColor(NightHelper.isNight());
    }
}
