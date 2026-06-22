package com.github.chrisbanes.photoview.animation;

/* JADX INFO: renamed from: sc.a */
/* JADX INFO: compiled from: OnErrorListener.java */
/* JADX INFO: loaded from: classes3.dex */
public interface OnErrorListener {
    void onException(Exception exc);

    void onMessage(String str);

    void onErrorCode(int errorCode);
}
