package com.resync.app.events;

import android.net.Uri;

/* JADX INFO: renamed from: h7.o */
/* JADX INFO: compiled from: OnImageKeyboardImageEvent.java */
/* JADX INFO: loaded from: classes.dex */
public class OnImageKeyboardImageEvent {

    public Uri uri;
    public String mimeType;

    public OnImageKeyboardImageEvent(Uri uri, String str) {
        this.uri = uri;
        this.mimeType = str;
    }
}
