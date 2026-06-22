package com.resync.app.events;

import com.bumptech.glide.MaterialPostsHelper;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: renamed from: h7.d0 */
/* JADX INFO: compiled from: SubredditSelectedEvent.java */
/* JADX INFO: loaded from: classes.dex */
public class SubredditSelectedEvent {

    public String subreddit;
    public boolean openExternally;

    public SubredditSelectedEvent(String str) {
        if (StringUtils.startsWith(str, "u_")) {
            this.subreddit = StringUtils.trim(MaterialPostsHelper.buildOverviewProfileAddress(str.replace("u_", "")));
        } else {
            this.subreddit = StringUtils.trim(str);
        }
        this.openExternally = false;
    }

    public SubredditSelectedEvent(String str, boolean z) {
        if (StringUtils.startsWith(str, "u_")) {
            this.subreddit = StringUtils.trim(MaterialPostsHelper.buildOverviewProfileAddress(str.replace("u_", "")));
        } else {
            this.subreddit = StringUtils.trim(str);
        }
        this.openExternally = z;
    }
}
