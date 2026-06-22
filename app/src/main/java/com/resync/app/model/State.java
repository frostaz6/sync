package com.resync.app.model;

import com.resync.app.helpers.ui.SubredditArrayList;

/* JADX INFO: renamed from: o8.i */
/* JADX INFO: compiled from: State.java */
/* JADX INFO: loaded from: classes.dex */
public class State {

    public int status;                                    // a: 0 = cached, 1 = fresh
    public SubredditArrayList subscribed = new SubredditArrayList();  // b
    public SubredditArrayList favourites = new SubredditArrayList();  // c
    public SubredditArrayList moderated = new SubredditArrayList();   // d

    public SubredditArrayList multireddits = new SubredditArrayList(); // e
}
