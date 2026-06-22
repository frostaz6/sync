package com.resync.app.managers;

import android.app.NotificationManager;
import android.content.Context;
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
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.NotificationHelper;
import com.resync.app.helpers.PaintAndTagsUpdatedEvent;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.dialogs.TagUserInputDialogBottomSheet;
import com.resync.app.premium.UltraHelper;
import com.resync.app.util.Logger;
import com.resync.app.net.oauth.OAuthSubredditInfoRequest;
import com.resync.app.model.Subreddit;

/* JADX INFO: renamed from: d8.m */
/* JADX INFO: compiled from: NewUserTagManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class NewUserTagManager {
    private static final String TAG = "m";
    private static final Object LOCK = new Object();
    private static NewUserTagManager instance;

    private final Map<String, String> tagMap = getTagMap(AccountSingleton.getInstance().getUsername());

    private NewUserTagManager() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void onRemoveTagResult(HttpsCallableResult httpsCallableResult) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void onBatchTagResult(HttpsCallableResult httpsCallableResult) {
    }

    private static void notifyChange(String str) {
        String str2 = TAG;
        Logger.verbose(str2, "Notifying change...");
        if (StringUtils.equalsAnyIgnoreCase(AccountSingleton.getInstance().getUsername(), str)) {
            Logger.verbose(str2, "Username was current");
            getInstance().recreate();
            RedditApplication.getAppContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
            BusSingleton.getBus().post(new PaintAndTagsUpdatedEvent());
        }
    }

    private static void saveTagMap(String str, Map<String, String> map) {
        String str2 = TAG;
        Logger.verbose(str2, "Saving tag map for username: " + str);
        Logger.verbose(str2, "Size of saved map: " + map.size());
        Logger.verbose(str2, "\t\tUsing lookup: " + getTagPrefKey());
        if (!StringUtils.equals("Logged out", str) && !PreferencesHelper.hasPrefsForUser(str)) {
            Logger.debug(str2, "Trying to save to an account that doesn't exist: " + str);
            PreferencesHelper.markPrefsAsSet(str);
            Logger.verbose(str2, "Setup settings for account: " + str);
        }
        if (map.size() > 0) {
            HashSet hashSet = new HashSet();
            for (String str3 : map.keySet()) {
                hashSet.add(str3 + getTagDelimiter() + map.get(str3));
            }
            PreferencesHelper.getAccountPrefsForUser(RedditApplication.getAppContext(), str, TAG).edit().putStringSet(getTagPrefKey(), hashSet).apply();
        } else {
            PreferencesHelper.getAccountPrefsForUser(RedditApplication.getAppContext(), str, str2).edit().remove(getTagPrefKey()).apply();
        }
        notifyChange(str);
    }

    private static void showNotification(int id, String str, String str2, boolean ultra) {
        ((NotificationManager) RedditApplication.getAppContext().getSystemService("notification")).notify(id, new NotificationCompat.Builder(RedditApplication.getAppContext(), ultra ? NotificationHelper.getUltraChannel() : NotificationHelper.getGenericChannel()).setSmallIcon(R.drawable.format_text_variant_outline).setContentTitle(str).setContentText(str2).setAutoCancel(true).setSound(RingtoneManager.getDefaultUri(2)).build());
    }

    public static void toggleTag(Context context, String str) {
        if (!getInstance().hasTag(str)) {
            FragmentUtils.showWithUser(TagUserInputDialogBottomSheet.class, ContextHelper.getFragmentManager(context), str);
        } else {
            StaticToast.showWithContext(context, "Tag removed");
            getInstance().removeTag(AccountSingleton.getInstance().getUsername(), str);
        }
    }

    public static void importTags(HashMap<String, Object> map) {
        int updatedCount = 0;
        for (String str : map.keySet()) {
            if (!StringUtils.equalsAnyIgnoreCase(str, "timestamp")) {
                Object obj = map.get(str);
                Logger.verbose(TAG, "Found key: " + str + StringUtils.SPACE + obj.getClass().getSimpleName() + StringUtils.SPACE + obj);
                if (obj instanceof HashMap) {
                    if (StringUtils.equalsAnyIgnoreCase("fullnames", str)) {
                        requestAuthorThumbnails((HashMap) obj);
                    } else {
                        addTagHashMap(str, (HashMap) obj);
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
        showNotification(98, "User tags updated!", sb2.toString(), false);
        getInstance().recreate();
    }

    private static void addTagHashMap(String str, HashMap<String, Object> map) {
        Logger.verbose(TAG, "\t\tAdding tag hashmap: " + str);
        TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        try {
            for (String str2 : map.keySet()) {
                treeMap.remove(str2);
                treeMap.put(str2, (String) map.get(str2));
            }
        } catch (Exception e) {
            Logger.logException(e);
        }
        saveTagMap(str, treeMap);
    }

    public static NewUserTagManager getInstance() {
        NewUserTagManager newUserTagManager;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new NewUserTagManager();
            }
            newUserTagManager = instance;
        }
        return newUserTagManager;
    }

    public static Map<String, String> getTagMap(String str) {
        String str2 = TAG;
        Logger.verbose(str2, "Getting tag map for username: " + str);
        Logger.verbose(str2, "\t\tUsing lookup: " + getTagPrefKey());
        TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (String str3 : new HashSet<String>(PreferencesHelper.getAccountPrefsForUser(RedditApplication.getAppContext(), str, str2).getStringSet(getTagPrefKey(), new HashSet()))) {
            String str4 = str3.split(getTagDelimiter())[0];
            String str5 = str3.split(getTagDelimiter())[1];
            if (!TextUtils.isEmpty(str4)) {
                treeMap.put(str4, str5);
            }
        }
        Logger.verbose(TAG, "Tag map size: " + treeMap.size());
        return treeMap;
    }

    public static final String getTagDelimiter() {
        return UltraHelper.isUltra() ? "###USER_TAG_ULTRA###" : "###USER_TAG###";
    }

    public static final String getTagPrefKey() {
        return UltraHelper.isUltra() ? "user_tags_ultra" : "user_tags";
    }

    private static void requestAuthorThumbnails(HashMap<String, String> map) {
        ArrayList arrayList = new ArrayList();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            arrayList.add(map.get(it.next()));
        }
        UserIconManager.requestIcons(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void onAddTagResult(HttpsCallableResult httpsCallableResult) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void onSubredditInfoLoaded(String str, String str2, String str3, Subreddit subreddit) {
        String str4 = subreddit.data.name;
        StaticToast.show("Tag added [" + str4 + " : " + str + "]");
        Map<String, String> tags = getTagMap(str2);
        tags.remove(str4);
        tags.remove(str3);
        tags.put(str4, str);
        saveTagMap(str2, tags);
        // LIBERATED: the tag is already saved locally above (saveTagMap()). The block below mirrored it to
        // Sync's dead Ultra backend (Firebase "addTagWithDetails"). Forced skip so it never fires
        // against the dead host.
        android.util.Log.i("m", "Skipped adding tag remotely (dead Ultra server)");
    }

    public void recreate() {
        synchronized (LOCK) {
            Logger.verbose(TAG, "Creating a new tag manager");
            instance = new NewUserTagManager();
        }
    }

    public void removeTag(String str, String str2) {
        Logger.verbose(TAG, "Removing tag: " + str + " [" + str2 + "]");
        if (StringUtils.isAnyEmpty(str, str2)) {
            return;
        }
        StaticToast.show("Tag removed [" + str2 + "]");
        Map<String, String> tags = getTagMap(str);
        tags.remove(str2);
        saveTagMap(str, tags);
        // LIBERATED: the tag is already removed locally above (saveTagMap()). The block below mirrored the
        // removal to Sync's dead Ultra backend (Firebase "removeTag"). Forced skip so it never
        // fires against the dead host.
        android.util.Log.i("m", "Skipped removing tag remotely (dead Ultra server)");
    }

    public void removeTagFromNotification(String str, String str2) {
        Logger.verbose(TAG, "Removing tag from notification: " + str + " [" + str2 + "]");
        if (StringUtils.isAnyEmpty(str, str2)) {
            return;
        }
        Map<String, String> tags = getTagMap(str);
        tags.remove(str2);
        saveTagMap(str, tags);
    }

    public void addTag(final String str, final String str2, final String str3) {
        Logger.verbose(TAG, "Adding tag: " + str + " [" + str2 + ":" + str3 + "]");
        if (StringUtils.isAnyEmpty(str, str2, str3)) {
            return;
        }
        OAuthRequestManager.sendWithAppContext(new OAuthSubredditInfoRequest(str2, new Response.Listener() { // from class: d8.f
            @Override // com.android.volley.Response.Listener
            public final void onResponse(Object obj) {
                NewUserTagManager.onSubredditInfoLoaded(str3, str, str2, (Subreddit) obj);
            }
        }, new Response.ErrorListener() { // from class: d8.e
            @Override // com.android.volley.Response.ErrorListener
            public final void onErrorResponse(VolleyError volleyError) {
                StaticToast.show("Error adding tag");
            }
        }));
    }

    public void addTagFromNotification(String str, String str2, String str3) {
        Logger.verbose(TAG, "Adding tag from notification: " + str + " [" + str2 + " : " + str3 + "]");
        if (StringUtils.isAnyEmpty(str2, str3)) {
            return;
        }
        Map<String, String> tags = getTagMap(str);
        tags.remove(str2);
        tags.put(str2, str3);
        saveTagMap(str, tags);
    }

    public void importLocalTags(String str, Map<String, String> map) {
        Logger.verbose(TAG, "Batch adding tags: " + str);
        // LIBERATED: this was a server-only push of local tags to Sync's dead Ultra backend
        // (Firebase "addTagBatch"). There is no server to import into, and local tags already
        // work from SharedPreferences. No-op so the "Import local" action never hits the dead host.
        android.util.Log.i("m", "Skipped batch tag import (dead Ultra server)");
    }

    public void clearTags(String str) {
        Map<String, String> tags = getTagMap(str);
        tags.clear();
        saveTagMap(str, tags);
    }

    public String getTag(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return this.tagMap.get(str);
    }

    public Map<String, String> getTags() {
        return this.tagMap;
    }

    public boolean hasTag(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return this.tagMap.containsKey(str);
    }
}
