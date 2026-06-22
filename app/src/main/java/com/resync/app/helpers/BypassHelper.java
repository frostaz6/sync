package com.resync.app.helpers;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/* JADX INFO: renamed from: t7.e */
/* JADX INFO: compiled from: BypassHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class BypassHelper {
    private static final Object LOCK = new Object();
    private static BypassHelper instance;

    private ArrayList<String> domains;

    private BypassHelper() {
        ArrayList<String> arrayList = new ArrayList<String>(new HashSet<String>(PreferencesHelper.getAccountPrefs("Bypass").getStringSet("bypass_domains_entries", new HashSet<String>(Arrays.asList("play.google.com", "facebook.com", "twitter.com", "maps.google.com", "netflix.com", "spotify.com", "instagram.com", "clips.twitch.tv")))));
        this.domains = arrayList;
        Collections.sort(arrayList);
    }

    public static BypassHelper getInstance() {
        BypassHelper bypassHelper;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new BypassHelper();
            }
            bypassHelper = instance;
        }
        return bypassHelper;
    }

    public boolean add(String str) {
        if (TextUtils.isEmpty(str) || this.domains.contains(str)) {
            return false;
        }
        this.domains.add(str);
        PreferencesHelper.getAccountPrefs("Bypass").edit().putStringSet("bypass_domains_entries", new HashSet(this.domains)).apply();
        return true;
    }

    public ArrayList<String> getDomains() {
        return this.domains;
    }

    public void reset() {
        synchronized (LOCK) {
            instance = new BypassHelper();
        }
    }

    public boolean remove(String str) {
        if (TextUtils.isEmpty(str) || !this.domains.contains(str)) {
            return false;
        }
        this.domains.remove(str);
        PreferencesHelper.getAccountPrefs("Bypass").edit().putStringSet("bypass_domains_entries", new HashSet(this.domains)).apply();
        return true;
    }
}
