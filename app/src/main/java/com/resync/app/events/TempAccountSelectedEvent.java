package com.resync.app.events;

import com.resync.app.model.UserAccount;

/* JADX INFO: renamed from: h7.g0 */
/* JADX INFO: compiled from: TempAccountSelectedEvent.java */
/* JADX INFO: loaded from: classes.dex */
public class TempAccountSelectedEvent {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public UserAccount account;

    public TempAccountSelectedEvent(UserAccount userAccount) {
        this.account = userAccount;
    }
}
