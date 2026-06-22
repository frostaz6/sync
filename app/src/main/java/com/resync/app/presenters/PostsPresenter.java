package com.resync.app.presenters;

import android.os.Bundle;
import com.resync.app.model.Subreddit;

/* JADX INFO: renamed from: ya.g */
/* JADX INFO: compiled from: PostsPresenter.java */
/* JADX INFO: loaded from: classes.dex */
public interface PostsPresenter extends BaseCursorPresenter {
    void saveState(Bundle bundle);

    void onPause();

    void setUiMode(Bundle bundle, int uiMode);

    boolean onInfiniteScroll();

    void changeSubreddit(Bundle bundle, String str, String str2, String str3);

    String getSubredditName();

    boolean isExpanded();

    boolean load(boolean refresh);

    String getSort();

    void markAllRead();

    String getAccess();

    void changeAccessAndSort(Bundle bundle, String str, String str2);

    Subreddit getUserOrSubredditInfo();

    void toggleExpanded();

    int getUiMode();
}
