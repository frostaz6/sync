package com.resync.app.presenters;

import android.database.Cursor;
import android.os.Parcelable;
import com.resync.app.model.Post;
import com.resync.app.presenters.CommentsPresenter;

/* JADX INFO: renamed from: za.b */
/* JADX INFO: compiled from: CommentsView.java */
/* JADX INFO: loaded from: classes.dex */
public interface CommentsView extends BaseViewInterface<CommentsPresenter> {
    void setSortLabel(String str, String str2);

    void setMoreCommentsAnimation();

    void setCollapseAnimation();

    void showNoComments();

    void setSubredditTitle(String str);

    void showSingleThreadNotification();

    void setEnterExitAnimation();

    void saveCommentState(Parcelable parcelable);

    void onCommentCollapsed(Post post);

    void setNoAnimation();

    void setCursor(Cursor cursor);

    void showOffline();

    void setRefreshing(boolean refreshing);

    void scrollToPosition(int position);

    void setDefaultAnimation();

    void notifyDataSetChanged();

    void setExpandAnimation();

    void setActionsAnimation();
}
