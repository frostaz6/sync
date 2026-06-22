package com.resync.app.presenters;

import com.resync.app.presenters.MessagingPresenter;

/* JADX INFO: renamed from: za.c */
/* JADX INFO: compiled from: MessagingView.java */
/* JADX INFO: loaded from: classes.dex */
public interface MessagingView extends BaseViewInterface<MessagingPresenter> {
    void showStatusMessage(int message);

    void setEnterExitAnimation();

    void notifyDataChanged();

    void setRefreshing(boolean refreshing);
}
