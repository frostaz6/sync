package com.resync.app.premium;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.helpers.ui.StaticToast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.UserIconManager;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.fragments.ColorSelectionFragment;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.NotificationHelper;
import com.resync.app.helpers.PaintAndTagsUpdatedEvent;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.helpers.RemoteConfigHelper;
import com.resync.app.security.KeyManager;
import com.resync.app.premium.UltraHelper;
import com.resync.app.util.ColorUtils;
import com.resync.app.util.Logger;
import com.resync.app.net.oauth.OAuthSubredditInfoRequest;
import com.resync.app.model.Subreddit;

/* JADX INFO: compiled from: UserPaintManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class UserPaintManager {
    private static final String LOG_TAG = "z";
    private static final Object LOCK = new Object();
    private static UserPaintManager instance;

    private final Map<String, Integer> colorMap = loadColorMap(AccountSingleton.getInstance().getUsername());

    private UserPaintManager() {
    }

    private static void saveColorMap(String str, Map<String, Integer> map) {
        String str2 = LOG_TAG;
        Logger.verbose(str2, "Saving color map for username: " + str);
        Logger.verbose(str2, "Size of saved map: " + map.size());
        if (!StringUtils.equals("Logged out", str) && !PreferencesHelper.hasPrefsForUser(str)) {
            Logger.debug(str2, "Trying to save to an account that doesn't exist: " + str);
            PreferencesHelper.markPrefsAsSet(str);
            Logger.verbose(str2, "Setup settings for account: " + str);
        }
        if (map.size() > 0) {
            HashSet hashSet = new HashSet();
            for (String str3 : map.keySet()) {
                hashSet.add(str3 + "###USER_PAINT_ULTRA###" + map.get(str3));
            }
            PreferencesHelper.getAccountPrefsForUser(RedditApplication.getAppContext(), str, LOG_TAG).edit().putStringSet("user_paint_ultra", hashSet).apply();
        } else {
            PreferencesHelper.getAccountPrefsForUser(RedditApplication.getAppContext(), str, str2).edit().remove("user_paint_ultra").apply();
        }
        notifyPaintChanged(str);
    }

    private static void showNotification(int notificationId, String str, String str2, boolean z) {
        ((NotificationManager) RedditApplication.getAppContext().getSystemService("notification")).notify(notificationId, new NotificationCompat.Builder(RedditApplication.getAppContext(), z ? NotificationHelper.getUltraChannel() : NotificationHelper.getGenericChannel()).setSmallIcon(R.drawable.format_paint).setContentTitle(str).setContentText(str2).setAutoCancel(true).setSound(RingtoneManager.getDefaultUri(2)).build());
    }

    public static void applyRemotePaints(HashMap<String, Object> map) {
        int updatedCount = 0;
        for (String str : map.keySet()) {
            if (!StringUtils.equalsAnyIgnoreCase(str, "timestamp")) {
                Object obj = map.get(str);
                Logger.verbose(LOG_TAG, "Found key: " + str + StringUtils.SPACE + obj.getClass().getSimpleName() + StringUtils.SPACE + obj);
                if (obj instanceof HashMap) {
                    if (StringUtils.equalsAnyIgnoreCase("fullnames", str)) {
                        updateUserIcons((HashMap) obj);
                    } else {
                        applyAccountPaints(str, (HashMap) obj);
                        updatedCount++;
                    }
                }
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Updated ");
        sb2.append(updatedCount);
        sb2.append(" account");
        sb2.append(updatedCount != 1 ? "s" : "");
        showNotification(99, "User paint updated!", sb2.toString(), false);
        getInstance().reload();
    }

    private static void applyAccountPaints(String str, HashMap<String, Object> map) {
        Logger.verbose(LOG_TAG, "\t\tAdding paint hashmap: " + str);
        HashMap map2 = new HashMap();
        try {
            for (String str2 : map.keySet()) {
                map2.put(str2, Integer.valueOf(Color.parseColor((String) map.get(str2))));
            }
        } catch (Exception e) {
            Logger.logException(e);
        }
        saveColorMap(str, map2);
    }

    private static Map<String, Integer> loadColorMap(String str) {
        String str2 = LOG_TAG;
        Logger.verbose(str2, "Getting color map for username: " + str);
        HashMap map = new HashMap();
        for (String str3 : new HashSet<String>(PreferencesHelper.getAccountPrefsForUser(RedditApplication.getAppContext(), str, str2).getStringSet("user_paint_ultra", new HashSet()))) {
            String str4 = str3.split("###USER_PAINT_ULTRA###")[0];
            Integer numValueOf = Integer.valueOf(Integer.parseInt(str3.split("###USER_PAINT_ULTRA###")[1]));
            if (!TextUtils.isEmpty(str4)) {
                map.put(str4, numValueOf);
            }
        }
        Logger.verbose(LOG_TAG, "Color map size: " + map.size());
        return map;
    }

    public static UserPaintManager getInstance() {
        UserPaintManager userPaintManager;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new UserPaintManager();
            }
            userPaintManager = instance;
        }
        return userPaintManager;
    }

    private static void updateUserIcons(HashMap<String, String> map) {
        ArrayList arrayList = new ArrayList();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            arrayList.add(map.get(it.next()));
        }
        UserIconManager.requestIcons(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void onAddPaintSuccess(HttpsCallableResult httpsCallableResult) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void onSubredditInfoForPaint(String str, String str2, Subreddit subreddit) {
        String str3 = subreddit.data.name;
        StaticToast.show("Paint added [" + str3 + " : " + str + "]");
        Map<String, Integer> colors = loadColorMap(str2);
        colors.remove(str3);
        colors.put(str3, Integer.valueOf(Color.parseColor(str)));
        saveColorMap(str2, colors);
        // LIBERATED: the paint is already saved locally above (saveColorMap()). The block below mirrored it
        // to Sync's dead Ultra backend (Firebase "addPaintWithDetails"). Forced skip so it never
        // fires against the dead host. (Previously gated on the ".AO-" token; the liberation token
        // would now make that pass, so the skip is made unconditional here instead.)
        android.util.Log.i("z", "Skipped adding paint remotely (dead Ultra server)");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void onRemovePaintSuccess(HttpsCallableResult httpsCallableResult) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onColorSelected(String str, int color) {
        if (color == 0) {
            removePaint(AccountSingleton.getInstance().getUsername(), str);
        } else {
            addPaint(AccountSingleton.getInstance().getUsername(), str, ColorUtils.toHexColor(color));
        }
    }

    private static void notifyPaintChanged(String str) {
        String str2 = LOG_TAG;
        Logger.verbose(str2, "Notifying change...");
        if (StringUtils.equalsAnyIgnoreCase(AccountSingleton.getInstance().getUsername(), str)) {
            Logger.verbose(str2, "Username was current");
            getInstance().reload();
            RedditApplication.getAppContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
            BusSingleton.getBus().post(new PaintAndTagsUpdatedEvent());
        }
    }

    public void reload() {
        synchronized (LOCK) {
            instance = new UserPaintManager();
        }
    }

    public void removePaint(String str, String str2) {
        Logger.verbose(LOG_TAG, "Removing paint: " + str + " [" + str2 + "]");
        if (StringUtils.isAnyEmpty(str, str2)) {
            return;
        }
        StaticToast.show("Paint removed [" + str2 + "]");
        Map<String, Integer> colors = loadColorMap(str);
        colors.remove(str2);
        saveColorMap(str, colors);
        // LIBERATED: the paint is already removed locally above (saveColorMap()). The block below mirrored
        // the removal to Sync's dead Ultra backend (Firebase "removePaint"). Forced skip so it
        // never fires against the dead host.
        android.util.Log.i("z", "Skipped removing paint remotely (dead Ultra server)");
    }

    public void removePaintFromNotification(String str, String str2) {
        Logger.verbose(LOG_TAG, "Removing paint from notification: " + str + " [" + str2 + "]");
        if (StringUtils.isAnyEmpty(str, str2)) {
            return;
        }
        Map<String, Integer> colors = loadColorMap(str);
        colors.remove(str2);
        saveColorMap(str, colors);
    }

    public void showAddPaintDialog(Context context, final String str) {
        if (!ConnectivityHelper.isConnected(context)) {
            StaticToast.show("You must be online to perform this action");
            return;
        }
        if (RemoteConfigHelper.isC4266fa()) {
            String string = PreferencesHelper.getDefaultPrefs("UltraHelper").getString(KeyManager.purchaseTokenKey(), null);
            if (StringUtils.isEmpty(string) || !StringUtils.containsIgnoreCase(string, ".AO-")) {
                return;
            }
        }
        ColorSelectionFragment colorSelectionFragmentI4 = ColorSelectionFragment.newInstance(getColor(str).intValue(), "Remove", true);
        colorSelectionFragmentI4.setColorSelectedListener(new ColorSelectionFragment.ColorSelectedListener() { // from class: d8.y
            @Override // com.resync.app.fragments.ColorSelectionFragment.ColorSelectedListener
            public final void onColorSelected(int color) {
                UserPaintManager.this.onColorSelected(str, color);
            }
        });
        colorSelectionFragmentI4.show(ContextHelper.getFragmentManager(context), ColorSelectionFragment.TAG);
    }

    public void addPaint(final String str, String str2, final String str3) {
        Logger.verbose(LOG_TAG, "Adding color: " + str + " [" + str2 + ":" + str3 + "] ");
        if (StringUtils.isAnyEmpty(str, str2, str3)) {
            return;
        }
        OAuthRequestManager.sendWithAppContext(new OAuthSubredditInfoRequest(str2, new Response.Listener() { // from class: d8.t
            @Override // com.android.volley.Response.Listener
            public final void onResponse(Object obj) {
                UserPaintManager.onSubredditInfoForPaint(str3, str, (Subreddit) obj);
            }
        }, new Response.ErrorListener() { // from class: d8.s
            @Override // com.android.volley.Response.ErrorListener
            public final void onErrorResponse(VolleyError volleyError) {
                StaticToast.show("Error adding paint");
            }
        }));
    }

    public void addPaintFromNotification(String str, String str2, String str3) {
        Logger.verbose(LOG_TAG, "Adding paint from notification: " + str + " [" + str2 + " : " + str3 + "]");
        if (StringUtils.isAnyEmpty(str, str2, str3)) {
            return;
        }
        Map<String, Integer> colors = loadColorMap(str);
        colors.remove(str2);
        colors.put(str2, Integer.valueOf(Color.parseColor(str3)));
        saveColorMap(str, colors);
    }

    public void clearPaints(String str) {
        Map<String, Integer> colors = loadColorMap(str);
        colors.clear();
        saveColorMap(str, colors);
    }

    public Integer getColor(String str) {
        if (this.colorMap.containsKey(str)) {
            return this.colorMap.get(str);
        }
        return 0;
    }

    public Map<String, Integer> getColorMap() {
        return this.colorMap;
    }

    public boolean hasPaint(String str) {
        if (!UltraHelper.isUltra()) {
            return false;
        }
        if (RemoteConfigHelper.isC4266fa()) {
            String string = PreferencesHelper.getDefaultPrefs("UltraHelper").getString(KeyManager.purchaseTokenKey(), null);
            if (StringUtils.isEmpty(string) || !StringUtils.containsIgnoreCase(string, ".AO-")) {
                this.colorMap.clear();
            }
        }
        return this.colorMap.containsKey(str);
    }
}
