package com.resync.app.helpers.messaging;

import android.content.Context;
import com.resync.app.events.MessageCountUpdatedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: z7.a */
/* JADX INFO: compiled from: MessageCountHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class MessageCountHelper {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static String LOG_TAG = "a";
    public static String COUNT_KEY = "message_count";

    public static int getCount() {
        return PreferencesHelper.getAccountPrefs("MessageCount").getInt(COUNT_KEY, 0);
    }

    public static void reset() {
        PreferencesHelper.getAccountPrefs("MessageCount").edit().remove(COUNT_KEY).apply();
        Logger.verbose(LOG_TAG, "Count reset!");
        BusSingleton.getBus().post(new MessageCountUpdatedEvent());
    }

    public static void setCount(Context context, int count) {
        PreferencesHelper.getAccountPrefs("MessageCount").edit().putInt(COUNT_KEY, count).apply();
        Logger.verbose(LOG_TAG, "Count updated: " + count);
        BusSingleton.getBus().post(new MessageCountUpdatedEvent());
    }
}
