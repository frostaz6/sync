package com.resync.app.net;

import com.android.volley.VolleyError;

/* JADX INFO: renamed from: x8.c */
/* JADX INFO: compiled from: SubredditNotFoundError.java */
/* JADX INFO: loaded from: classes.dex */
public class SubredditNotFoundError extends VolleyError {
    public SubredditNotFoundError() {
        super("This subreddit does not exist");
    }
}
