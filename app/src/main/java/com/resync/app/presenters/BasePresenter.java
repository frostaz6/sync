package com.resync.app.presenters;

import android.os.Bundle;

/* Recovered presenter base interface. Method set = intersection of all
 * "@Override // com.resync.app.presenters.BasePresenter" implementations (com.resync.app.presenters.CommentsPresenterImpl / com.resync.app.presenters.PostsPresenterImpl /
 * com.resync.app.presenters.MessagingPresenterImpl). Lifecycle methods overridden by only some
 * impls (R(Bundle), onPause()) are declared on the more specific
 * sub-interfaces (PostsPresenter, MessagingPresenter) instead. */
public interface BasePresenter {
    void reset();

    void refresh();

    void onStart();

    void recover();

    void onDestroy();

    void onStop();

    void restoreArguments(Bundle bundle);
}
