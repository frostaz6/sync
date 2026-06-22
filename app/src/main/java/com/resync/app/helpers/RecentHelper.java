package com.resync.app.helpers;

import java.util.ArrayList;
import org.apache.commons.lang3.ObjectUtils;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: t7.b0 */
/* JADX INFO: compiled from: RecentHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class RecentHelper {

    static ArrayList<RecentEntry> recents = new ArrayList<>();

    /* JADX INFO: renamed from: t7.b0$a */
    /* JADX INFO: compiled from: RecentHelper.java */
    public static class RecentEntry {

        public String id;
        public String title;
        public String subreddit;

        public RecentEntry(Post post) {
            this.id = post.getId();
            this.title = post.getSubreddit();
            this.subreddit = post.getTitle();
        }

        public boolean equals(Object obj) {
            return obj instanceof RecentEntry ? ObjectUtils.equals(this.id, ((RecentEntry) obj).id) : super.equals(obj);
        }
    }

    public static void add(Post post) {
        if (post == null) {
            return;
        }
        RecentEntry entry = new RecentEntry(post);
        if (recents.contains(entry)) {
            recents.remove(entry);
        }
        recents.add(0, entry);
        if (recents.size() > 15) {
            recents.remove(recents.size() - 1);
        }
    }

    public static void clear() {
        recents.clear();
    }

    public static ArrayList<RecentEntry> getRecent() {
        return recents;
    }
}
