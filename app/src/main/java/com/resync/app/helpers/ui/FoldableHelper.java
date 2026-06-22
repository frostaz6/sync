package com.resync.app.helpers.ui;

import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.WindowHelper;

/* JADX INFO: renamed from: gb.e */
/* JADX INFO: compiled from: FoldableHelper.java */
/* JADX INFO: loaded from: classes2.dex */
public final class FoldableHelper {
    public static boolean isFoldableCompact() {
        return isFoldableModeEnabled() && WindowHelper.getWindowSizeClass() == WindowHelper.WindowSizeClass.COMPACT;
    }

    public static boolean isFoldableModeEnabled() {
        return SettingsSingleton.getSettings().tabletModeFoldable;
    }

    public static boolean isFoldableExpanded() {
        return isFoldableModeEnabled() && (WindowHelper.getWindowSizeClass() == WindowHelper.WindowSizeClass.MEDIUM || WindowHelper.getWindowSizeClass() == WindowHelper.WindowSizeClass.EXPANDED);
    }
}
