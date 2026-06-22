package com.resync.app.helpers.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: renamed from: gb.q */
/* JADX INFO: compiled from: SubredditArrayList.java */
/* JADX INFO: loaded from: classes2.dex */
public class SubredditArrayList extends ArrayList<String> {

    public static int SORT_DEFAULT = 0;
    public static int SORT_FEEDS_LAST = 1;
    public static int SORT_FEEDS_FIRST = 2;

    /* JADX INFO: renamed from: gb.q$a */
    /* JADX INFO: compiled from: SubredditArrayList.java */
    static class MultiAwareComparator implements Comparator<String> {
        MultiAwareComparator() {
        }

        @Override // java.util.Comparator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(String first, String second) {
            return first.replace("multi_", "").compareToIgnoreCase(second.replace("multi_", ""));
        }
    }

    /* JADX INFO: renamed from: gb.q$b */
    /* JADX INFO: compiled from: SubredditArrayList.java */
    static class MultiAwareComparatorFeeds implements Comparator<String> {
        MultiAwareComparatorFeeds() {
        }

        @Override // java.util.Comparator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(String first, String second) {
            return first.replace("multi_", "").compareToIgnoreCase(second.replace("multi_", ""));
        }
    }

    public SubredditArrayList(List<String> list) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            addIfAbsent(it.next());
        }
    }

    static boolean equalsIgnoreCase(String first, String second) {
        return StringUtils.equalsIgnoreCase(first, second);
    }

    public void addIfAbsent(int index, String subreddit) {
        if (contains(subreddit)) {
            return;
        }
        add(index, subreddit);
    }

    @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean contains(Object obj) {
        String subreddit = (String) obj;
        Iterator<String> it = iterator();
        while (it.hasNext()) {
            if (equalsIgnoreCase(subreddit, it.next())) {
                return true;
            }
        }
        return false;
    }

    public void addIfAbsent(String subreddit) {
        if (contains(subreddit)) {
            return;
        }
        add(subreddit);
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        if (!(obj instanceof SubredditArrayList)) {
            return super.equals(obj);
        }
        SubredditArrayList subredditArrayList = (SubredditArrayList) obj;
        if (subredditArrayList.size() != size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            if (!equalsIgnoreCase(get(i), subredditArrayList.get(i))) {
                return false;
            }
        }
        return true;
    }

    public void addAll(SubredditArrayList subredditArrayList) {
        Iterator<String> it = subredditArrayList.iterator();
        while (it.hasNext()) {
            addIfAbsent(it.next());
        }
    }

    public void addAll(ArrayList<String> arrayList) {
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            addIfAbsent(it.next());
        }
    }

    public void addAll(String[] strArr) {
        for (String str : strArr) {
            addIfAbsent(str);
        }
    }

    public String[] toStringArray() {
        return (String[]) toArray(new String[size()]);
    }

    public boolean isMultiAt(int index) {
        if (index < size()) {
            return get(index).startsWith("multi_");
        }
        return false;
    }

    public boolean removeIgnoreCase(String subreddit) {
        Iterator it = iterator();
        while (it.hasNext()) {
            String existing = (String) it.next();
            if (equalsIgnoreCase(existing, subreddit)) {
                remove(existing);
                return true;
            }
        }
        return false;
    }

    public void sortDefault() {
        sort(SORT_DEFAULT);
    }

    public void sort(int sortMode) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (sortMode != SORT_DEFAULT) {
            for (int size = size() - 1; size >= 0; size--) {
                if (isMultiAt(size)) {
                    arrayList.add(remove(size));
                }
            }
        }
        Collections.sort(this, new MultiAwareComparator());
        Collections.sort(arrayList, new MultiAwareComparatorFeeds());
        if (sortMode == SORT_FEEDS_LAST) {
            addAll(arrayList);
        } else if (sortMode == SORT_FEEDS_FIRST) {
            for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
                addIfAbsent(0, arrayList.remove(size2));
            }
        }
    }

    public Set<String> toSet() {
        return new HashSet(this);
    }

    public SubredditArrayList(Set<String> set) {
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            addIfAbsent(it.next());
        }
    }

    public SubredditArrayList() {
    }

    public SubredditArrayList(String... strArr) {
        if (strArr == null) {
            return;
        }
        for (String str : strArr) {
            addIfAbsent(str);
        }
    }
}
