package com.resync.app.helpers;

import android.content.SharedPreferences;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.premium.UltraHelper;

/* JADX INFO: renamed from: t7.h */
/* JADX INFO: compiled from: CommentActionsManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class CommentActionsManager {
    private static final Object LOCK = new Object();
    private static CommentActionsManager instance;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    ArrayList<Integer> actions;

    private CommentActionsManager() {
        load();
    }

    private boolean toggle(int actionCode) {
        if (!isEnabled(actionCode) && this.actions.contains(Integer.valueOf(actionCode))) {
            ArrayList<Integer> arrayList = this.actions;
            arrayList.remove(arrayList.indexOf(Integer.valueOf(actionCode)));
            return true;
        }
        if (!isEnabled(actionCode) || this.actions.contains(Integer.valueOf(actionCode))) {
            return false;
        }
        this.actions.add(Integer.valueOf(actionCode));
        return true;
    }

    public static int getIconRes(int actionCode) {
        switch (actionCode) {
            case 0:
                return R.drawable.outline_reply;
            case 1:
                return R.drawable.ic_refresh_white_24dp;
            case 2:
                return R.drawable.outline_mode_comment_24;
            case 3:
                return R.drawable.ic_info_outline_black_24dp;
            case 4:
                return R.drawable.outline_image_24;
            case 5:
                return R.drawable.arrow_collapse;
            case 6:
                return R.drawable.outline_sports_football_24;
            case 7:
                return R.drawable.outline_settings_24;
            case 8:
                return R.drawable.outline_sort_24;
            case 9:
                return R.drawable.outline_search_24;
            case 10:
                return R.drawable.outline_restore_24;
            case 11:
                return R.drawable.outline_translate_24;
            default:
                throw new RuntimeException("Unknown action: " + actionCode);
        }
    }

    public static CommentActionsManager getInstance() {
        CommentActionsManager commentActionsManager;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new CommentActionsManager();
            }
            commentActionsManager = instance;
        }
        return commentActionsManager;
    }

    public static boolean isEnabled(int actionCode) {
        switch (actionCode) {
            case 0:
                return SettingsSingleton.getSettings().comment_action_reply;
            case 1:
                return SettingsSingleton.getSettings().comment_action_refresh;
            case 2:
                return SettingsSingleton.getSettings().comment_action_other;
            case 3:
                return SettingsSingleton.getSettings().comment_action_about;
            case 4:
                return SettingsSingleton.getSettings().comment_action_images;
            case 5:
                return SettingsSingleton.getSettings().comment_action_collapse_all;
            case 6:
                return SettingsSingleton.getSettings().comment_action_sports_mode;
            case 7:
                return SettingsSingleton.getSettings().comment_action_settings;
            case 8:
                return SettingsSingleton.getSettings().comment_action_sort;
            case 9:
                return SettingsSingleton.getSettings().comment_action_search;
            case 10:
                return SettingsSingleton.getSettings().comment_action_recents;
            case 11:
                return SettingsSingleton.getSettings().comment_action_translate && UltraHelper.isUltra();
            default:
                throw new RuntimeException("Unknown action: " + actionCode);
        }
    }

    private static String getPrefName() {
        return "CommentActionsManager";
    }

    public static String getLabel(int actionCode) {
        switch (actionCode) {
            case 0:
                return "Reply";
            case 1:
                return "Refresh";
            case 2:
                return "Other\nDiscussions";
            case 3:
                return "About";
            case 4:
                return "View\nimages";
            case 5:
                return "Collapse\nall";
            case 6:
                return "Sports\nmode";
            case 7:
                return "Settings";
            case 8:
                return "Sort";
            case 9:
                return "Search";
            case 10:
                return "Recents";
            case 11:
                return "Translate\nall";
            default:
                throw new RuntimeException("Unknown action: " + actionCode);
        }
    }

    public void refreshFromSettings() {
        toggle(0);
        toggle(1);
        toggle(2);
        toggle(3);
        toggle(4);
        toggle(5);
        toggle(6);
        toggle(7);
        toggle(8);
        toggle(9);
        toggle(10);
        toggle(11);
        save();
    }

    public ArrayList<Integer> getActions() {
        return this.actions;
    }

    public void load() {
        SharedPreferences sharedPreferencesG = PreferencesHelper.getAccountPrefs(getPrefName());
        this.actions = new ArrayList<Integer>();
        String[] strArrSplit = StringUtils.split(sharedPreferencesG.getString("new_comment_actions", null), ",");
        if (strArrSplit != null) {
            for (String str : strArrSplit) {
                this.actions.add(Integer.valueOf(Integer.parseInt(str)));
            }
        }
        if (this.actions.isEmpty()) {
            this.actions.add(0);
            this.actions.add(1);
            this.actions.add(2);
            this.actions.add(3);
            this.actions.add(4);
            this.actions.add(5);
            this.actions.add(6);
            this.actions.add(7);
            this.actions.add(8);
            this.actions.add(9);
            this.actions.add(10);
            this.actions.add(11);
        }
        refreshFromSettings();
    }

    public void reset() {
        PreferencesHelper.getAccountPrefs(getPrefName()).edit().remove("new_comment_actions").commit();
        load();
    }

    public void save() {
        StringBuilder sb2 = new StringBuilder();
        int i = 0;
        while (i < this.actions.size()) {
            sb2.append(this.actions.get(i));
            i++;
            if (i < this.actions.size()) {
                sb2.append(",");
            }
        }
        PreferencesHelper.getAccountPrefs(getPrefName()).edit().putString("new_comment_actions", sb2.toString()).apply();
    }
}
