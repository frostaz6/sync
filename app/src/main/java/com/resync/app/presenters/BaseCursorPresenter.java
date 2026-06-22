package com.resync.app.presenters;

import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import androidx.core.util.Pair;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: ya.a */
/* JADX INFO: compiled from: BaseCursorPresenter.java */
/* JADX INFO: loaded from: classes.dex */
public interface BaseCursorPresenter extends BasePresenter {
    void showPostMore(Post post);

    void showModOptions(Post post);

    void sharePost(Post post);

    void unhidePost(Post post);

    void peekImage(Post post, ImageView imageView);

    void hidePost(Post post);

    void openLink(Pair<View, String>[] pairArr, Post post);

    void openLinkExternally(Post post);

    Cursor getCursor();

    void openComments(Post post);

    void loadCursor();

    void toggleHide(Post post);
}
