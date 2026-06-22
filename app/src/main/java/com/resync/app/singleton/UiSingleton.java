package com.resync.app.singleton;

import android.content.Context;
import android.content.SharedPreferences;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.helpers.ui.TabletHelper;
import com.resync.app.helpers.PreferencesHelper;

/* JADX INFO: renamed from: com.resync.app.singleton.c */
/* JADX INFO: compiled from: UiSingleton.java */
/* JADX INFO: loaded from: classes.dex */
public final class UiSingleton {

    public static String PREF_KEY_UI_MODE = "ui_mode_new";
    static final Object LOCK = new Object();
    static UiSingleton instance;
    static int uiMode;

    /* JADX INFO: renamed from: com.resync.app.singleton.c$a */
    /* JADX INFO: compiled from: UiSingleton.java */
    public static final class UnsupportedUiModeException extends RuntimeException {
        public UnsupportedUiModeException() {
            super("Unsupported Ui Mode");
        }
    }

    private UiSingleton() {
        reload();
    }

    public static UiSingleton getInstance() {
        UiSingleton uiSingleton;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new UiSingleton();
            }
            uiSingleton = instance;
        }
        return uiSingleton;
    }

    public static UiSingleton getInstance(Context context) {
        UiSingleton uiSingleton;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new UiSingleton();
            }
            uiSingleton = instance;
        }
        return uiSingleton;
    }

    public static int getColumnCount(int mode) {
        return getColumnCount(mode, RedditApplication.getAppContext().getResources().getBoolean(R.bool.landscape));
    }

    public static int getColumnCount(int mode, boolean landscape) {
        if (TabletHelper.isDualPane() || mode == 1 || mode == 2 || mode == 0 || mode == 5 || mode == 8) {
            return 1;
        }
        return landscape ? SettingsSingleton.getSettings().postColumnsLandscape : SettingsSingleton.getSettings().postColumnsPortrait;
    }

    public static String getViewModeName(int mode) {
        if (mode == 0) {
            return "List";
        }
        if (mode == 1) {
            return "Compact";
        }
        if (mode == 2) {
            return "Smaller cards";
        }
        if (mode == 3) {
            return "Small cards";
        }
        if (mode == 4) {
            return "Cards";
        }
        if (mode == 5) {
            return "Slides";
        }
        if (mode == 8) {
            return "Swipe";
        }
        return null;
    }

    public static boolean isFullWidth(int mode) {
        return SettingsSingleton.getSettings().cardFullWidth && getColumnCount(mode) == 1;
    }

    public static boolean isCards(int mode) {
        return mode == 4;
    }

    public static boolean isListLike(int mode) {
        return mode == 0 || mode == 1 || mode == 2;
    }

    public static boolean isSlides(int mode) {
        return mode == 5;
    }

    public static boolean isSmallCards(int mode) {
        return mode == 3;
    }

    public static boolean isSwipe(int mode) {
        return mode == 8;
    }

    public int getUiMode() {
        return uiMode;
    }

    public void reload() {
        int mode = PreferencesHelper.getAccountPrefs("UiSingleton").getInt(PREF_KEY_UI_MODE, 5);
        uiMode = mode;
        if (mode == 8) {
            uiMode = 5;
        }
    }

    public void setUiMode(int mode) {
        SharedPreferences.Editor editorEdit = PreferencesHelper.getAccountPrefs("UiSingleton").edit();
        editorEdit.putInt(PREF_KEY_UI_MODE, mode);
        editorEdit.apply();
        reload();
    }
}
