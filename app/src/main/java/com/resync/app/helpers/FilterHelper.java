package com.resync.app.helpers;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: t7.m */
/* JADX INFO: compiled from: FilterHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class FilterHelper {

    private static final Object LOCK = new Object();
    private static FilterHelper instance;

    private ArrayList<String> keywordFilters = loadFilter(0);
    private ArrayList<String> domainFilters = loadFilter(1);
    private ArrayList<String> subredditFilters = loadFilter(2);
    private ArrayList<String> userFilters = loadFilter(3);

    private ArrayList<String> flairFilters = loadFilter(4);

    private FilterHelper() {
    }

    private ArrayList<String> loadFilter(int filterType) {
        ArrayList<String> arrayList = new ArrayList<String>(new HashSet<String>(PreferencesHelper.getAccountPrefs("Filter").getStringSet(getPrefKey(filterType), new HashSet<String>())));
        Collections.sort(arrayList);
        return arrayList;
    }

    public static FilterHelper getInstance() {
        FilterHelper filterHelper;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new FilterHelper();
            }
            filterHelper = instance;
        }
        return filterHelper;
    }

    public boolean add(int filterType, String str) {
        if (TextUtils.isEmpty(str) || getFilterList(filterType).contains(str)) {
            return false;
        }
        getFilterList(filterType).add(str);
        PreferencesHelper.getAccountPrefs("Filter").edit().putStringSet(getPrefKey(filterType), new HashSet(getFilterList(filterType))).apply();
        return true;
    }

    public void clear(int filterType) {
        getFilterList(filterType).clear();
        PreferencesHelper.getAccountPrefs("Filter").edit().remove(getPrefKey(filterType)).apply();
    }

    public ArrayList<String> getFilterList(int filterType) {
        if (filterType == 0) {
            return this.keywordFilters;
        }
        if (filterType == 1) {
            return this.domainFilters;
        }
        if (filterType == 2) {
            return this.subredditFilters;
        }
        if (filterType == 3) {
            return this.userFilters;
        }
        if (filterType == 4) {
            return this.flairFilters;
        }
        throw new RuntimeException("Invalid mode");
    }

    public String getPrefKey(int filterType) {
        if (filterType == 0) {
            return "filter_keyword";
        }
        if (filterType == 1) {
            return "filter_domain";
        }
        if (filterType == 2) {
            return "filter_subreddit";
        }
        if (filterType == 3) {
            return "filter_user";
        }
        if (filterType == 4) {
            return "filter_flair";
        }
        throw new RuntimeException("Invalid mode");
    }

    public void reset() {
        synchronized (LOCK) {
            instance = new FilterHelper();
        }
    }

    public void removeFilter(int filterType, String str) {
        Logger.verbose("Removing: " + str);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        ArrayList<String> arrayListC = getFilterList(filterType);
        if (arrayListC.contains(str)) {
            arrayListC.remove(str);
            PreferencesHelper.getAccountPrefs("Filter").edit().putStringSet(getPrefKey(filterType), new HashSet(arrayListC)).apply();
        }
    }
}
