package com.resync.app.model;

import java.io.Serializable;

/* JADX INFO: renamed from: o8.g */
/* JADX INFO: compiled from: PostsInfo.java */
/* JADX INFO: loaded from: classes.dex */
public class PostsInfo implements Serializable {

    public String after;             // a: pagination "after" token
    public int count;                // b: total posts loaded
    public boolean stagnant;         // c: stagnant (no new posts)

    public int insertCount;          // q: rows bulk-inserted

    public boolean filtersBlocked;   // r: all results blocked by filters

    public int page;                 // s: page / list index
    public long networkTimeMs;       // t: network time (ms)

    public long requestDuration;     // u: total request duration (ms)

    public void reset() {
        this.after = null;
        this.count = 0;
        this.page = 0;
    }

    public String toString() {
        return this.after + ": " + this.count;
    }
}
