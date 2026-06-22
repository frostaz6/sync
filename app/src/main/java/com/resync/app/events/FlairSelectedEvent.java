package com.resync.app.events;

import com.resync.app.model.AbstractFlair;

/* JADX INFO: renamed from: h7.i */
/* JADX INFO: compiled from: FlairSelectedEvent.java */
/* JADX INFO: loaded from: classes.dex */
public class FlairSelectedEvent {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public AbstractFlair flair;

    public FlairSelectedEvent(AbstractFlair abstractFlair) {
        this.flair = abstractFlair;
    }
}
