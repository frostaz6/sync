package com.resync.app.events;

/* JADX INFO: renamed from: h7.e0 */
/* JADX INFO: compiled from: SubredditSortUpdatedEvent.java */
/* JADX INFO: loaded from: classes.dex */
public class SubredditSortUpdatedEvent {

    public String sort;
    public String timePeriod;

    public SubredditSortUpdatedEvent(String str, String str2) {
        this.sort = str;
        this.timePeriod = str2;
    }
}
