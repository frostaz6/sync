package com.resync.app.ui.fragments.posts;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.volley.VolleyError;
import com.bumptech.glide.MaterialPostsHelper;
import com.google.android.material.snackbar.Snackbar;
import com.resync.app.R;
import com.resync.app.ui.fragments.RedditFragment;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.SwipeActivity;
import com.resync.app.ui.fragment_dialogs.EditFragment;
import com.resync.app.helpers.ui.LoadingViewHelper;
import com.resync.app.helpers.ui.SnackbarHelper;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.PerSubSortHelper;
import com.resync.app.helpers.PerSubViewModeHelper;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.helpers.SubredditHelper;
import com.resync.app.ui.interfaces.PostHost;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.presenters.PostsPresenter;
import com.resync.app.presenters.PresenterUtils;
import com.resync.app.presenters.PostsPresenterImpl;
import com.resync.app.presenters.PostsView;

/* JADX INFO: loaded from: classes2.dex */
public abstract class BasePostsFragment extends RedditFragment implements PostsView, LoadingViewHelper.RetryListener {

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    protected PostsPresenter presenter;

    /* JADX INFO: renamed from: s0, reason: collision with root package name */
    protected Cursor cursor;
    protected LoadingViewHelper loadingViewHelper;

    /* JADX INFO: renamed from: u0, reason: collision with root package name */
    protected Bundle savedState;

    class PrivateSubRetryListener implements LoadingViewHelper.RetryListener {
        PrivateSubRetryListener() {
        }

        @Override // com.resync.app.helpers.ui.LoadingViewHelper.RetryListener
        public void onRetryAction() {
            String title = SubredditHelper.getDisplayTitle(BasePostsFragment.this.getSubredditName(), false);
            if (!AccountSingleton.getInstance().isLoggedIn()) {
                StaticToast.show(BasePostsFragment.this.getContext(), R.string.common_generic_error_logged_out);
                return;
            }
            EditFragment.newMessageInstance("/r/" + title).show(BasePostsFragment.this.getFragmentManager(), "EditFragment");
        }
    }

    class UnhideClickListener implements View.OnClickListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Post post;

        UnhideClickListener(Post post) {
            this.post = post;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            BasePostsFragment.this.presenter.unhidePost(this.post);
        }
    }

    class OfflineRetryListener implements LoadingViewHelper.RetryListener {
        OfflineRetryListener() {
        }

        @Override // com.resync.app.helpers.ui.LoadingViewHelper.RetryListener
        public void onRetryAction() {
            BasePostsFragment.this.presenter.refresh();
        }
    }

    class RestoreScrollClickListener implements View.OnClickListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Runnable runnable;

        RestoreScrollClickListener(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            view.post(this.runnable);
        }
    }

    public static BasePostsFragment newVertical(String subreddit, boolean showToolbar) {
        VerticalPostsFragment verticalPostsFragment = new VerticalPostsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("subreddit", subreddit);
        Pair<String, String> sortPair = PerSubSortHelper.getSortPair(subreddit);
        String access = (String) sortPair.first;
        String sort = (String) sortPair.second;
        int viewMode = PerSubViewModeHelper.getViewMode(subreddit);
        bundle.putString("access", access);
        bundle.putString("sort", sort);
        bundle.putInt("ui_mode", viewMode);
        bundle.putBoolean("toolbar", showToolbar);
        verticalPostsFragment.setArguments(bundle);
        return verticalPostsFragment;
    }

    public static BasePostsFragment newVerticalForUrl(String str) {
        VerticalPostsFragment verticalPostsFragment = new VerticalPostsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("subreddit", str);
        Pair<String, String> sortPair = PerSubSortHelper.getSortPair(str);
        String access = (String) sortPair.first;
        String sort = (String) sortPair.second;
        bundle.putString("access", access);
        bundle.putString("sort", sort);
        bundle.putInt("ui_mode", 8);
        bundle.putBoolean("toolbar", false);
        verticalPostsFragment.setArguments(bundle);
        return verticalPostsFragment;
    }

    @Override // com.resync.app.presenters.PostsView
    public void showStatusMessage(int messageRes) {
        this.loadingViewHelper.showError(messageRes);
    }

    public String getSort() {
        return this.presenter.getSort();
    }

    public String getSubredditName() {
        return this.presenter.getSubredditName();
    }

    public int getUiMode() {
        return this.presenter.getUiMode();
    }

    public boolean hasData() {
        Cursor cursor = this.cursor;
        return cursor != null && cursor.getCount() > 0;
    }

    @Override // com.resync.app.presenters.PostsView
    public void enableNsfwScreenshotBlock() {
        if (SettingsSingleton.getSettings().nsfwRecents) {
            try {
                getActivity().getWindow().setFlags(8192, 8192);
            } catch (Exception e) {
                Logger.logException(e);
            }
        }
    }

    public void setPresenter(PostsPresenter postsPresenter) {
        this.presenter = postsPresenter;
        postsPresenter.restoreArguments(getArguments());
    }

    @Override // com.resync.app.presenters.PostsView
    public void setTitle(String str) {
        if (getActivity() instanceof PostHost) {
            ((PostHost) getActivity()).updateSidebar(str);
        }
    }

    public void changeAccessAndSort(String str, String str2) {
        this.loadingViewHelper.dismiss();
        this.presenter.changeAccessAndSort(getArguments(), str, str2);
    }

    public void changeSubreddit(String str) {
        this.loadingViewHelper.dismiss();
        Pair<String, String> sortPair = PerSubSortHelper.getSortPair(str);
        String access = (String) sortPair.first;
        String sort = (String) sortPair.second;
        if (MaterialPostsHelper.isSearch(str)) {
            access = this.presenter.getAccess();
            sort = this.presenter.getSort();
        }
        int viewMode = PerSubViewModeHelper.getViewMode(str);
        if (getActivity() instanceof SwipeActivity) {
            viewMode = 8;
        }
        if (!(this.presenter.getUiMode() != viewMode)) {
            this.presenter.changeSubreddit(getArguments(), str, access, sort);
            return;
        }
        setDefaultAnimation();
        this.presenter.reset();
        applyUiMode(viewMode);
        this.presenter.changeSubreddit(getArguments(), str, access, sort);
    }

    public void applyUiMode(int uiMode) {
    }

    public void setupViews() {
    }

    public Snackbar showRestoreScrollSnackbar(Runnable runnable) {
        Snackbar snackbarA = SnackbarHelper.make(getCoordinator(), R.string.fragment_posts_restore_scroll, 0);
        snackbarA.getView().setId(R.id.snackbar);
        snackbarA.setAction(R.string.common_restore, new RestoreScrollClickListener(runnable));
        snackbarA.show();
        return snackbarA;
    }

    protected void bindCursor(Cursor cursor) {
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (PresenterUtils.shouldAttachPresenter()) {
            setPresenter(new PostsPresenterImpl(getActivity(), bundle, this));
        }
    }

    @Override // com.resync.app.presenters.PostsView
    public void setLoading(boolean loading, boolean force) {
        this.loadingViewHelper.setRefreshing(!hasData() && force);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(getLayoutResId(), (ViewGroup) null);
        ButterKnife.bind(this, viewInflate);
        setupViews();
        this.loadingViewHelper = new LoadingViewHelper(getActivity(), this.mSwipeRefreshLayout, this);
        return viewInflate;
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: U1 */
    public void onDestroy() {
        super.onDestroy();
        LoadingViewHelper loadingViewHelper = this.loadingViewHelper;
        if (loadingViewHelper != null) {
            loadingViewHelper.hide();
        }
        PostsPresenter postsPresenter = this.presenter;
        if (postsPresenter != null) {
            postsPresenter.onDestroy();
        }
    }

    @Override // com.resync.app.presenters.PostsView
    public void notifyCommentsOpened() {
        this.loadingViewHelper.showMessage("Comments opened in the background");
    }

    @Override // com.resync.app.presenters.PostsView
    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
        bindCursor(cursor);
    }

    @Override // com.resync.app.ui.fragments.RedditFragment, androidx.fragment.app.Fragment
    public void onPause() {
        PostsPresenter postsPresenter = this.presenter;
        if (postsPresenter != null) {
            postsPresenter.onPause();
        }
        super.onPause();
    }

    @Override // com.resync.app.presenters.PostsView
    public void onPostHidden(Post post) {
        Snackbar snackbarA = SnackbarHelper.make(getCoordinator(), R.string.common_hidden, 0);
        snackbarA.getView().setId(R.id.snackbar);
        snackbarA.setAction("Undo", new UnhideClickListener(post));
        snackbarA.show();
    }

    @Override // com.resync.app.presenters.PostsView
    public void showOffline() {
        Logger.verbose("Showing the offline notification: " + this.loadingViewHelper.isShown());
        if (this.loadingViewHelper.isShown()) {
            return;
        }
        this.loadingViewHelper.showError(R.string.common_offline, R.string.common_refresh, new OfflineRetryListener());
    }

    @Override // com.resync.app.presenters.PostsView
    public boolean isLoadingViewActive() {
        return this.loadingViewHelper.isShown();
    }

    @Override // com.resync.app.helpers.ui.LoadingViewHelper.RetryListener
    public void onRetryAction() {
        this.presenter.recover();
    }

    @Override // com.resync.app.ui.fragments.RedditFragment, androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: k2 */
    public void onResume() {
        super.onResume();
    }

    @Override // com.resync.app.ui.interfaces.RedditFragmentInterface
    public int getLayoutResId() {
        throw new RuntimeException("You should overrride this");
    }

    @Override // com.resync.app.presenters.BaseViewInterface
    public void showError(VolleyError volleyError) {
        this.loadingViewHelper.showError(volleyError);
    }

    @Override // androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        PostsPresenter postsPresenter = this.presenter;
        if (postsPresenter != null) {
            postsPresenter.saveState(bundle);
        }
        super.onSaveInstanceState(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: m2 */
    public void onStart() {
        super.onStart();
        PostsPresenter postsPresenter = this.presenter;
        if (postsPresenter != null) {
            postsPresenter.onStart();
        }
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: n2 */
    public void onStop() {
        PostsPresenter postsPresenter = this.presenter;
        if (postsPresenter != null) {
            postsPresenter.onStop();
        }
        super.onStop();
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (this.presenter != null) {
            resetViewState();
            if (bundle == null && ConnectivityHelper.isConnected(getActivity())) {
                Logger.verbose("Requesting live");
                this.presenter.load(true);
            } else {
                Logger.verbose("Loading cursor");
                this.presenter.loadCursor();
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewStateRestored(Bundle bundle) {
        super.onViewStateRestored(bundle);
        this.savedState = bundle;
    }

    @Override // com.resync.app.presenters.PostsView
    public void showNsfwSub() {
        this.loadingViewHelper.showError(R.string.fragment_posts_sub_nsfw);
    }

    @Override // com.resync.app.presenters.PostsView
    public void resetViewState() {
    }

    @Override // com.resync.app.presenters.PostsView
    public void disableNsfwScreenshotBlock() {
        if (SettingsSingleton.getSettings().nsfwRecents) {
            try {
                getActivity().getWindow().clearFlags(8192);
            } catch (Exception e) {
                Logger.logException(e);
            }
        }
    }

    @Override // com.resync.app.presenters.PostsView
    public void showPrivateSub() {
        this.loadingViewHelper.showError(R.string.fragment_posts_sub_private, R.string.common_message_mods, new PrivateSubRetryListener());
    }

    public String getAccess() {
        return this.presenter.getAccess();
    }

    public Cursor getCursor() {
        return this.cursor;
    }

    @Override // com.resync.app.presenters.PostsView
    public void showInvalidSub() {
        this.loadingViewHelper.showError(R.string.fragment_posts_sub_invalid);
    }

    protected CoordinatorLayout getCoordinator() {
        return (CoordinatorLayout) getActivity().findViewById(R.id.coordinator);
    }

    public PostsPresenter getPresenter() {
        return this.presenter;
    }

    public int getColumnCount() {
        if (MaterialPostsHelper.isUserProfile(getSubredditName())) {
            return 1;
        }
        return MaterialPostsHelper.isModAbout(getSubredditName()) ? 3 : 0;
    }
}
