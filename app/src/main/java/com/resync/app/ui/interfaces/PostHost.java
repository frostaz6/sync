package com.resync.app.ui.interfaces;

/* JADX INFO: renamed from: ta.d */
/* JADX INFO: compiled from: PostHost.java */
/* JADX INFO: loaded from: classes2.dex */
public interface PostHost {
    boolean areAnyPanelsOpen();

    boolean showMessaging();

    void openEndPanel();

    void showPosts();

    boolean hasPanelOrDrawer();

    void updateSidebar(String str);
}
