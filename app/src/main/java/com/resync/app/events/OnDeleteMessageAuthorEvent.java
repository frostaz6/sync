package com.resync.app.events;

import com.resync.app.model.Message;

/* JADX INFO: renamed from: m7.c */
/* JADX INFO: compiled from: OnDeleteMessageAuthorEvent.java */
/* JADX INFO: loaded from: classes.dex */
public class OnDeleteMessageAuthorEvent {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Message message;

    public OnDeleteMessageAuthorEvent(Message message) {
        this.message = message;
    }
}
