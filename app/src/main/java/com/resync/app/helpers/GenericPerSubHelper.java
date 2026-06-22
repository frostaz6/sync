package com.resync.app.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;

/* JADX INFO: renamed from: b8.a */
/* JADX INFO: compiled from: GenericPerSubHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class GenericPerSubHelper {
    private GenericPerSubHelper() {
    }

    static void loadIntMap(String str, HashMap<String, Integer> map) {
        map.clear();
        String string = PreferencesHelper.getAccountPrefs(GenericPerSubHelper.class.getSimpleName()).getString(str, null);
        if (TextUtils.isEmpty(string)) {
            return;
        }
        for (String str2 : StringUtils.split(string, ",")) {
            map.put(str2.split("###")[0], Integer.valueOf(Integer.parseInt(str2.split("###")[1])));
        }
    }

    static void loadStringMap(String str, HashMap<String, String> map) {
        map.clear();
        String string = PreferencesHelper.getAccountPrefs(GenericPerSubHelper.class.getSimpleName()).getString(str, null);
        if (TextUtils.isEmpty(string)) {
            return;
        }
        for (String str2 : StringUtils.split(string, ",")) {
            map.put(str2.split("###")[0], str2.split("###")[1]);
        }
    }

    static void saveIntMap(String str, HashMap<String, Integer> map) {
        PreferencesHelper.getAccountPrefs(GenericPerSubHelper.class.getSimpleName()).edit().remove(str).apply();
        if (map.size() > 0) {
            ArrayList arrayList = new ArrayList();
            for (String str2 : map.keySet()) {
                arrayList.add(str2 + "###" + map.get(str2));
            }
            String strJoin = StringUtils.join(arrayList, ",");
            Logger.verbose("Combined: " + strJoin);
            PreferencesHelper.getAccountPrefs(GenericPerSubHelper.class.getSimpleName()).edit().putString(str, strJoin).apply();
        }
    }

    static void saveStringMap(String str, HashMap<String, String> map) {
        PreferencesHelper.getAccountPrefs(GenericPerSubHelper.class.getSimpleName()).edit().remove(str).apply();
        if (map.size() > 0) {
            ArrayList arrayList = new ArrayList();
            for (String str2 : map.keySet()) {
                arrayList.add(str2 + "###" + map.get(str2));
            }
            PreferencesHelper.getAccountPrefs(GenericPerSubHelper.class.getSimpleName()).edit().putString(str, StringUtils.join(arrayList, ",")).apply();
        }
    }
}
