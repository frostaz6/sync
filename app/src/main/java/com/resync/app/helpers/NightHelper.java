package com.resync.app.helpers;

import com.resync.app.RedditApplication;
import com.resync.app.receiver.ThemeReceiver;
import com.resync.app.singleton.SettingsSingleton;
import java.util.Calendar;
import java.util.Date;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: t7.s */
/* JADX INFO: compiled from: NightHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class NightHelper {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static int nightMode;

    public static int getNightMode() {
        return nightMode;
    }

    public static boolean isFollowSystem() {
        return PreferencesHelper.getAccountPrefs("NightHelper").getBoolean("night_mode_follow", true);
    }

    public static boolean isWithinAutoNightWindow() {
        return isWithinAutoNightWindow(Calendar.getInstance().getTime());
    }

    public static boolean isWithinAutoNightWindow(Date date) {
        int startHour = SettingsSingleton.getSettings().autoNightStartHour;
        int startMin = SettingsSingleton.getSettings().autoNightStartMin;
        int endHour = SettingsSingleton.getSettings().autoNightEndHour;
        int endMin = SettingsSingleton.getSettings().autoNightEndMin;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(11, startHour);
            calendar.set(12, startMin);
            calendar.set(13, 0);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(11, endHour);
            calendar2.set(12, endMin);
            calendar2.set(13, 0);
            Logger.verbose("NightHelper", "startHour: " + startHour);
            Logger.verbose("NightHelper", "endHour: " + endHour);
            if (endHour < startHour) {
                Logger.verbose("NightHelper", "Setting the end to tomorrow");
                calendar2.add(5, 1);
                if (date.getHours() < endHour) {
                    Logger.verbose("NightHelper", "Adding a day to the input date");
                    Calendar calendar3 = Calendar.getInstance();
                    calendar3.setTime(date);
                    calendar3.add(5, 1);
                    date = calendar3.getTime();
                }
            }
            if (date.after(calendar.getTime())) {
                return date.before(calendar2.getTime());
            }
            return false;
        } catch (Exception e) {
            Logger.logException(e);
            return false;
        }
    }

    public static boolean isNight() {
        return nightMode == 2;
    }

    public static void refresh(boolean broadcast) {
        boolean followSystem = PreferencesHelper.getAccountPrefs("NightHelper").getBoolean("night_mode_follow", true);
        boolean forceOff = PreferencesHelper.getAccountPrefs("NightHelper").getBoolean("night_mode_force_off", false);
        boolean forceOn = PreferencesHelper.getAccountPrefs("NightHelper").getBoolean("night_mode_force_on", false);
        boolean auto = PreferencesHelper.getAccountPrefs("NightHelper").getBoolean("night_mode_auto", false);
        if (followSystem) {
            int uiMode = RedditApplication.getAppContext().getResources().getConfiguration().uiMode & 48;
            if (uiMode == 16) {
                nightMode = 1;
            } else if (uiMode == 32) {
                nightMode = 2;
            }
        } else if (forceOff) {
            nightMode = 1;
        } else if (forceOn) {
            nightMode = 2;
        } else if (auto) {
            if (isWithinAutoNightWindow()) {
                nightMode = 2;
            } else {
                nightMode = 1;
            }
        }
        if (broadcast) {
            ThemeReceiver.broadcast();
        }
    }
}
