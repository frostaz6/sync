package com.resync.app.presenters;

import android.database.Cursor;
import com.resync.app.model.Post;
import com.resync.app.presenters.PostsPresenter;
import com.resync.app.model.Subreddit;

/* JADX INFO: renamed from: za.d */
/* JADX INFO: compiled from: PostsView.java */
/* JADX INFO: loaded from: classes.dex */
public interface PostsView extends BaseViewInterface<PostsPresenter> {
    void showStatusMessage(int message);

    void enableNsfwScreenshotBlock();

    void setTitle(String str);

    void setLoading(boolean loading, boolean refreshing);

    void notifyCommentsOpened();

    void setEnterExitAnimation();

    void setNoAnimation();

    void setCursor(Cursor cursor);

    void onPostHidden(Post post);

    void showOffline();

    boolean isLoadingViewActive();

    void setDefaultAnimation();

    void setRefreshing(boolean refreshing);

    void setSubreddit(Subreddit subreddit);

    void showNsfwSub();

    void resetViewState();

    void setSwipeEnabled(boolean enabled);

    void disableNsfwScreenshotBlock();

    void showPrivateSub();

    void showInvalidSub();
}
