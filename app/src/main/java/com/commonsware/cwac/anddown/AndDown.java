package com.commonsware.cwac.anddown;

/* JADX INFO: loaded from: classes.dex */
public class AndDown {
    static {
        System.loadLibrary("anddown");
    }

    public native String markdownToHtml(String str);
}
