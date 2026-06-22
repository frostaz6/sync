package com.resync.app.helpers.ui;

import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.SettingsSingleton;

/* JADX INFO: renamed from: gb.s */
/* JADX INFO: compiled from: TabletHelper.java */
/* JADX INFO: loaded from: classes2.dex */
public final class TabletHelper {
    public static boolean isDualPane() {
        if (isTabletMode() && isLandscape() && SettingsSingleton.getSettings().tabletDualPaneLandscape) {
            return true;
        }
        return isTabletMode() && !isLandscape() && SettingsSingleton.getSettings().tabletDualPanePortrait;
    }

    public static boolean isLandscape() {
        return RedditApplication.getAppContext().getResources().getBoolean(R.bool.landscape);
    }

    public static boolean isTabletResource() {
        return RedditApplication.getAppContext().getResources().getBoolean(R.bool.tablet);
    }

    public static boolean isTabletMode() {
        boolean tabletMode = SettingsSingleton.getSettings().tabletMode;
        if (tabletMode && FoldableHelper.isFoldableModeEnabled() && FoldableHelper.isFoldableCompact()) {
            return false;
        }
        return tabletMode;
    }

    public static boolean isTabletModeRaw() {
        return SettingsSingleton.getSettings().tabletMode;
    }
}
