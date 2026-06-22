package com.resync.app.helpers;

import android.content.SharedPreferences;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: renamed from: t7.b */
/* JADX INFO: compiled from: ActionsManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class ActionsManager {
    private static final Object LOCK = new Object();
    private static ActionsManager instance;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    ArrayList<Integer> actions;

    private ActionsManager() {
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
                return R.drawable.notification_clear_all;
            case 1:
                return R.drawable.outline_bookmark_border_24;
            case 2:
                return R.drawable.outline_person_outline_24;
            case 3:
                return R.drawable.outline_edit_24;
            case 4:
                return R.drawable.outline_view_agenda_24;
            case 5:
                return R.drawable.dice_5_outline;
            case 6:
                return R.drawable.incognito;
            case 7:
                return NightHelper.isFollowSystem() ? R.drawable.ic_dark_mode_auto : NightHelper.isNight() ? R.drawable.ic_dark_mode_selected : R.drawable.outline_dark_mode_24;
            case 8:
                return R.drawable.account_supervisor_outline;
            case 9:
                return R.drawable.outline_settings_24;
            case 10:
                return R.drawable.outline_data_usage_24;
            case 11:
                return R.drawable.ic_filter_list_black_24dp;
            case 12:
                return R.drawable.outline_history_24;
            case 13:
                return R.drawable.account_switch_outline;
            case 14:
                return R.drawable.ic_sort_white_24dp;
            case 15:
                return R.drawable.ic_refresh_white_24dp;
            case 16:
                return R.drawable.ic_info_outline_black_24dp;
            case 17:
                return R.drawable.progress_download;
            case 18:
                return R.drawable.ic_search_white_24dp;
            case 19:
                return R.drawable.outline_access_time_24;
            case 20:
                return R.drawable.swipe;
            case 21:
                return R.drawable.outline_email_24;
            case 22:
                return R.drawable.outline_explore_24;
            case 23:
                return R.drawable.outline_lightbulb_24;
            case 24:
                return R.drawable.chevron_up;
            case 25:
                return R.drawable.outline_person_add_24;
            case 26:
                // NOT outline_chat_bubble_outline_24 — that's a resource-shrinker <x/> tombstone
                // (unused in the original app) and inflating it crashes. Use our own real vector.
                return R.drawable.ic_sync_chat_24;
            default:
                throw new RuntimeException("Unknown action");
        }
    }

    public static ActionsManager getInstance() {
        ActionsManager actionsManager;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new ActionsManager();
            }
            actionsManager = instance;
        }
        return actionsManager;
    }

    public static boolean isEnabled(int actionCode) {
        switch (actionCode) {
            case 0:
                return SettingsSingleton.getSettings().actionsHideRead;
            case 1:
                return SettingsSingleton.getSettings().actionsSaved;
            case 2:
                return SettingsSingleton.getSettings().actionsProfile;
            case 3:
                return SettingsSingleton.getSettings().actionsSubmit;
            case 4:
                return SettingsSingleton.getSettings().actionsView;
            case 5:
                return SettingsSingleton.getSettings().actionsRandom;
            case 6:
                return SettingsSingleton.getSettings().actionsRandomNsfw && !SettingsSingleton.getSettings().nsfw;
            case 7:
                return SettingsSingleton.getSettings().actionsDarkMode;
            case 8:
                return SettingsSingleton.getSettings().actionsFriends;
            case 9:
                return SettingsSingleton.getSettings().actionsSettings;
            case 10:
                return SettingsSingleton.getSettings().actionsDataSaving;
            case 11:
                return SettingsSingleton.getSettings().actionsFilters;
            case 12:
                return SettingsSingleton.getSettings().actionsRecentlyViewed;
            case 13:
                return SettingsSingleton.getSettings().actionsToggleAccount;
            case 14:
                boolean actionsSort = SettingsSingleton.getSettings().actionsSort;
                return false;
            case 15:
                return SettingsSingleton.getSettings().actionsRefresh;
            case 16:
                return SettingsSingleton.getSettings().actionsAbout;
            case 17:
                return SettingsSingleton.getSettings().actionsSync;
            case 18:
                return SettingsSingleton.getSettings().actionsSearch;
            case 19:
                return SettingsSingleton.getSettings().actionsWatched;
            case 20:
                return SettingsSingleton.getSettings().actionsSwipe;
            case 21:
                return SettingsSingleton.getSettings().actionsMessaging;
            case 22:
                return SettingsSingleton.getSettings().actionsSubreddits;
            case 23:
                return SettingsSingleton.getSettings().actionsDarkOverlay;
            case 24:
                return SettingsSingleton.getSettings().actionsScrollToTop;
            case 25:
                // "Sign in" — only shown while logged out (the web-session login entry point).
                return !com.resync.app.singleton.AccountSingleton.getInstance().isLoggedIn();
            case 26:
                // "Chat" — reddit chat (Matrix); only meaningful while logged in.
                return com.resync.app.singleton.AccountSingleton.getInstance().isLoggedIn();
            default:
                throw new RuntimeException("Unknown action");
        }
    }

    private static String getPrefName() {
        return "ActionsManager";
    }

    public static String getLabel(int actionCode) {
        switch (actionCode) {
            case 0:
                return "Hide read";
            case 1:
                return "Saved";
            case 2:
                return "Profile";
            case 3:
                return "Submit";
            case 4:
                return "Change\nView";
            case 5:
                return "Random";
            case 6:
                return "Random\nNSFW";
            case 7:
                return "Dark\nmode";
            case 8:
                return "Friends";
            case 9:
                return "Settings";
            case 10:
                return "Data\nsaving";
            case 11:
                return "Filters";
            case 12:
                return "Recents";
            case 13:
                return "Toggle\naccount";
            case 14:
                return "Sort";
            case 15:
                return "Refresh";
            case 16:
                return "About";
            case 17:
                return "Sync";
            case 18:
                return "Search";
            case 19:
                return "Watched";
            case 20:
                return "Swipe\nmode";
            case 21:
                return "Inbox";
            case 22:
                return "Explore";
            case 23:
                return "Dark overlay";
            case 24:
                return "Scroll\nto top";
            case 25:
                return "Sign in";
            case 26:
                return "Chat";
            default:
                throw new RuntimeException("Unknown action");
        }
    }

    public void refreshFromSettings() {
        toggle(18);
        toggle(2);
        toggle(0);
        toggle(16);
        toggle(3);
        toggle(5);
        toggle(7);
        toggle(9);
        toggle(20);
        toggle(4);
        toggle(1);
        toggle(15);
        toggle(6);
        toggle(8);
        toggle(10);
        toggle(11);
        toggle(12);
        toggle(13);
        toggle(14);
        toggle(17);
        toggle(19);
        toggle(21);
        toggle(22);
        toggle(23);
        toggle(24);
        toggle(25);
        toggle(26);
        save();
    }

    public ArrayList<Integer> getActions() {
        return this.actions;
    }

    public void load() {
        SharedPreferences sharedPreferencesG = PreferencesHelper.getAccountPrefs(getPrefName());
        this.actions = new ArrayList<Integer>();
        String[] strArrSplit = StringUtils.split(sharedPreferencesG.getString("actions_new", null), ",");
        if (strArrSplit != null) {
            for (String str : strArrSplit) {
                this.actions.add(Integer.valueOf(Integer.parseInt(str)));
            }
        }
        if (this.actions.isEmpty()) {
            this.actions.add(18);
            this.actions.add(2);
            this.actions.add(0);
            this.actions.add(16);
            this.actions.add(3);
            this.actions.add(5);
            this.actions.add(7);
            this.actions.add(9);
            this.actions.add(20);
            this.actions.add(4);
            this.actions.add(1);
            this.actions.add(15);
            this.actions.add(8);
            this.actions.add(10);
            this.actions.add(11);
            this.actions.add(12);
            this.actions.add(13);
            this.actions.add(14);
            this.actions.add(19);
            this.actions.add(21);
            this.actions.add(22);
            this.actions.add(23);
            this.actions.add(24);
        }
        refreshFromSettings();
    }

    public void reset() {
        PreferencesHelper.getAccountPrefs(getPrefName()).edit().remove("actions_new").commit();
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
        PreferencesHelper.getAccountPrefs(getPrefName()).edit().putString("actions_new", sb2.toString()).apply();
    }
}
