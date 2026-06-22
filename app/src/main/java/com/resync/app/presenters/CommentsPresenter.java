package com.resync.app.presenters;

import android.os.Bundle;
import com.resync.app.model.Post;
import com.resync.app.util.CommentNavigation;

/* JADX INFO: renamed from: ya.c */
/* JADX INFO: compiled from: CommentsPresenter.java */
/* JADX INFO: loaded from: classes.dex */
public interface CommentsPresenter extends BaseCursorPresenter {
    boolean isHighlighted(Post post);

    void viewParentComment(Post post);

    String getPostId();

    String getEmptyMessage();

    void filterByUser(String str);

    void replyToPost();

    boolean isLoadingMoreFor(Post post);

    boolean isPostLocked();

    boolean isContinueInstance();

    boolean isNp();

    void loadAllComments(Bundle bundle);

    void loadMoreComments(Post post);

    boolean isSelected(Post post);

    void shareComment(Post post);

    boolean isSearch();

    void load(boolean z);

    void autoRefresh();

    String getSort();

    void onCommentClicked(Post post);

    void selectComment(Post post);

    Post getPost();

    void deselectComment(boolean z);

    void changeSort(Bundle bundle, String str);

    void setKeywordFilter(String str);

    void continueThread(Post post);

    boolean isLoading();

    boolean isSelf(Post post);

    void scrollToComment(Post post);

    void replyToComment(Post post);

    String getUserFilter();

    Post getScrollTarget();

    String getNewCommentSuffix();

    void onCommentLongClicked(Post post);

    boolean hasScrollTarget();

    String getCommentId();

    CommentNavigation getNavigation();

    String getDatabaseId();

    boolean isPostDeleted();

    String getKeywordFilter();

    void distinguishComment(Post post);

    boolean isPostSpoiler();

    void saveComment(Post post);

    void selectCommentAnimated(Post post, boolean z);

    void cancelMore(Post post);
}
