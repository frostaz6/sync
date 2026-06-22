package com.resync.app.presenters;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.core.util.Pair;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.fragment_dialogs.ImagePeekDialogFragment;
import com.resync.app.managers.WatchingHelper;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.StopExoplayerEvent;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.PostsInfo;
import com.resync.app.model.Trophy;
import com.resync.app.managers.HideManager;
import com.resync.app.managers.PostsManager;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.helpers.ModHelper;
import com.resync.app.helpers.PostInfoHelper;
import com.resync.app.helpers.PostMoreHelper;
import com.resync.app.helpers.PostsHelper;
import com.resync.app.helpers.VisitedManager;
import com.resync.app.dialogs.SharePostDialogBottomSheet;
import com.resync.app.net.TagRequestFilter;
import com.resync.app.util.Logger;
import com.resync.app.util.TestUtils;
import com.resync.app.util.TextUtils;
import com.resync.app.net.SubredditNotFoundError;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkHelper;
import com.resync.app.net.oauth.OAuthPostsRequest;
import com.resync.app.net.oauth.OAuthSubredditInfoRequest;
import com.resync.app.net.oauth.OAuthTrophiesRequest;
import com.resync.app.presenters.PostsPresenter;
import com.resync.app.model.Data;
import com.resync.app.model.Subreddit;
import com.resync.app.presenters.PostsView;

/* JADX INFO: compiled from: PostsPresenterImpl.java */
/* JADX INFO: loaded from: classes.dex */
public class PostsPresenterImpl implements PostsPresenter {

    private Context context;
    private PostsView view;
    private long presenterTag;
    private CursorLoader cursorLoader;

    private Loader.OnLoadCompleteListener loaderListener;

    private Cursor cursor;
    private boolean toolbar;
    private int uiMode;

    private String subreddit;
    private String access;
    private String sort;

    private PostsInfo postsInfo;

    private Subreddit subredditData;
    private boolean started;
    long lastLinkClickTime;
    private boolean expanded = true;

    /* JADX INFO: compiled from: PostsPresenterImpl.java */
    class SubredditInfoResponseListener implements Response.Listener<Subreddit> {

        final /* synthetic */ boolean refresh;

        /* JADX INFO: renamed from: com.resync.app.presenters.PostsPresenterImpl$a$a, reason: collision with other inner class name */
        /* JADX INFO: compiled from: PostsPresenterImpl.java */
        class TrophiesResponseListener implements Response.Listener<Trophy[]> {
            TrophiesResponseListener() {
            }

            @Override // com.android.volley.Response.Listener
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void onResponse(Trophy[] trophyArr) {
                PostsPresenterImpl.this.subredditData.trophies = trophyArr;
                SubredditInfoResponseListener listener = SubredditInfoResponseListener.this;
                PostsPresenterImpl.this.requestLivePosts(listener.refresh);
            }
        }

        /* JADX INFO: compiled from: PostsPresenterImpl.java */
        class TrophiesErrorListener implements Response.ErrorListener {
            TrophiesErrorListener() {
            }

            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                SubredditInfoResponseListener listener = SubredditInfoResponseListener.this;
                PostsPresenterImpl.this.requestLivePosts(listener.refresh);
            }
        }

        SubredditInfoResponseListener(boolean z) {
            this.refresh = z;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(Subreddit subreddit) {
            PostsPresenterImpl.this.subredditData = subreddit;
            PostsPresenterImpl.this.view.setTitle(PostsPresenterImpl.this.subreddit);
            PostsPresenterImpl.this.view.setSubreddit(PostsPresenterImpl.this.subredditData);
            if (subreddit.errorCode == 403) {
                PostsPresenterImpl.this.showForbidden();
                return;
            }
            if ("Listing".equalsIgnoreCase(subreddit.kind)) {
                PostsPresenterImpl.this.showListing();
                return;
            }
            PostsPresenterImpl.this.view.resetViewState();
            if (subreddit.data.over18) {
                PostsPresenterImpl.this.view.enableNsfwScreenshotBlock();
            } else {
                PostsPresenterImpl.this.view.disableNsfwScreenshotBlock();
            }
            Data data = subreddit.data;
            if (data != null && data.over18 && SettingsSingleton.getInstance().isNsfwEnabled()) {
                PostsPresenterImpl.this.showNsfwSub();
            } else if (MaterialPostsHelper.isUserProfile(PostsPresenterImpl.this.sort)) {
                OAuthRequestManager.send(PostsPresenterImpl.this.context, new OAuthTrophiesRequest(PostsPresenterImpl.this.context, Long.valueOf(PostsPresenterImpl.this.presenterTag), PostsPresenterImpl.this.subreddit, new TrophiesResponseListener(), new TrophiesErrorListener()));
            } else {
                PostsPresenterImpl.this.requestLivePosts(this.refresh);
            }
        }
    }

    /* JADX INFO: compiled from: PostsPresenterImpl.java */
    class SubredditInfoErrorListener implements Response.ErrorListener {
        SubredditInfoErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            NetworkResponse networkResponse;
            NetworkResponse networkResponse2;
            if (volleyError instanceof SubredditNotFoundError) {
                VisitedManager.removeVisited(PostsPresenterImpl.this.getSubredditName());
                PostsPresenterImpl.this.onLoadError(volleyError);
                return;
            }
            if (volleyError != null && (networkResponse2 = volleyError.networkResponse) != null && networkResponse2.statusCode == 403) {
                PostsPresenterImpl.this.showForbidden();
                return;
            }
            if (volleyError == null || (networkResponse = volleyError.networkResponse) == null || networkResponse.statusCode != 404) {
                PostsPresenterImpl.this.onLoadError(volleyError);
            } else {
                VisitedManager.removeVisited(PostsPresenterImpl.this.getSubredditName());
                PostsPresenterImpl.this.requestLivePosts(true);
            }
        }
    }

    /* JADX INFO: compiled from: PostsPresenterImpl.java */
    class LivePostsResponseListener implements Response.Listener<PostsInfo> {
        LivePostsResponseListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(PostsInfo postsInfo) {
            PostsPresenterImpl.this.setPostsInfo(postsInfo);
            Logger.verbose("PostsPresenterImpl", "Live posts loaded: " + PostsPresenterImpl.this.presenterTag);
            if (PostsPresenterImpl.this.cursorLoader == null && PostsPresenterImpl.this.loaderListener == null) {
                PostsPresenterImpl.this.loadCursor();
            }
        }
    }

    /* JADX INFO: compiled from: PostsPresenterImpl.java */
    class LivePostsErrorListener implements Response.ErrorListener {
        LivePostsErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            Logger.verbose("PostsPresenterImpl", "Live posts error!");
            PostsPresenterImpl.this.onLoadError(volleyError);
        }
    }

    /* JADX INFO: compiled from: PostsPresenterImpl.java */
    class CursorLoadListener implements Loader.OnLoadCompleteListener<Cursor> {
        CursorLoadListener() {
        }

        @Override // android.content.Loader.OnLoadCompleteListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
            PostsPresenterImpl.this.onCursorLoaded(cursor);
        }
    }

    public PostsPresenterImpl(Context context, Bundle bundle, PostsView postsView) {
        this.context = context;
        this.view = postsView;
        this.presenterTag = (bundle == null || !bundle.containsKey("PresenterTag")) ? System.currentTimeMillis() : bundle.getLong("PresenterTag");
        setPostsInfo((bundle == null || !bundle.containsKey("PostsInfo")) ? new PostsInfo() : (PostsInfo) bundle.getSerializable("PostsInfo"));
        if (bundle == null || !bundle.containsKey("Subreddit.Data")) {
            return;
        }
        Subreddit subreddit = (Subreddit) bundle.getSerializable("Subreddit.Data");
        this.subredditData = subreddit;
        this.view.setSubreddit(subreddit);
    }

    @Override // com.resync.app.presenters.PostsPresenter
    public void setUiMode(Bundle bundle, int uiMode) {
        setUiMode(uiMode);
        bundle.remove("ui_mode");
        bundle.putInt("ui_mode", uiMode);
    }

    @Override // com.resync.app.presenters.PostsPresenter
    public boolean onInfiniteScroll() {
        if (isLoadBlocked()) {
            Logger.verbose("PostsPresenterImpl", "Posts not requested (presenter locked)");
            return false;
        }
        Logger.verbose("PostsPresenterImpl", "Infinite scrolled, requesting live data");
        return load(false);
    }

    @Override // com.resync.app.presenters.PostsPresenter
    public void changeSubreddit(Bundle bundle, String str, String str2, String str3) {
        bundle.remove("subreddit");
        bundle.putString("subreddit", str);
        bundle.remove("access");
        bundle.putString("access", str2);
        bundle.remove("sort");
        bundle.putString("sort", str3);
        reset();
        setSubredditName(str);
        setAccess(str2);
        setSort(str3);
        this.view.resetViewState();
        load(true);
    }

    @Override // com.resync.app.presenters.PostsPresenter
    public String getSubredditName() {
        Data data;
        Subreddit subreddit = this.subredditData;
        return (subreddit == null || (data = subreddit.data) == null || TextUtils.isEmpty(data.displayName)) ? this.subreddit : this.subredditData.data.displayName;
    }

    @Override // com.resync.app.presenters.BaseCursorPresenter
    public void showPostMore(Post post) {
        PostMoreHelper.showMoreSheet(this.context, getUiMode(), post);
    }

    @Override // com.resync.app.presenters.BaseCursorPresenter
    public void showModOptions(Post post) {
        ModHelper.showModSheet(this.context, post);
    }

    @Override // com.resync.app.presenters.BasePresenter
    public void reset() {
        Loader.OnLoadCompleteListener onLoadCompleteListener;
        Logger.verbose("PostsPresenterImpl", "Resetting presenter");
        this.expanded = true;
        this.postsInfo.reset();
        cancelAllRequests();
        CursorLoader cursorLoader = this.cursorLoader;
        if (cursorLoader != null && (onLoadCompleteListener = this.loaderListener) != null) {
            cursorLoader.unregisterListener(onLoadCompleteListener);
            this.cursorLoader = null;
            this.loaderListener = null;
        }
        this.view.setSwipeEnabled(false);
        this.view.setEnterExitAnimation();
        this.view.setCursor(null);
        closeCursor();
        this.subredditData = null;
        this.view.setSubreddit(null);
        this.presenterTag = System.currentTimeMillis();
        Logger.verbose("PostsPresenterImpl", "Reset resenter tag: " + this.presenterTag);
    }

    @Override // com.resync.app.presenters.BasePresenter
    public void refresh() {
        if (!ConnectivityHelper.isConnected(this.context)) {
            this.view.showOffline();
        } else {
            reset();
            load(true);
        }
    }

    @Override // com.resync.app.presenters.BaseCursorPresenter
    public void sharePost(Post post) {
        FragmentUtils.showWithArguments(SharePostDialogBottomSheet.class, ((BaseActivity) this.context).getSupportFragmentManager(), SharePostDialogBottomSheet.buildArguments(post));
    }

    public boolean canOpenLink() {
        if (System.currentTimeMillis() - getLastLinkClickTime() <= 750) {
            return false;
        }
        setLastLinkClickTime(System.currentTimeMillis());
        return true;
    }

    public void cancelAllRequests() {
        Logger.verbose("PostsPresenterImpl", "Cancelling all requests: " + this.presenterTag);
        RedditApplication.mainRequestQueue.cancelAll((RequestQueue.RequestFilter) new TagRequestFilter(Long.valueOf(this.presenterTag)));
        RedditApplication.oauthRequestQueue.cancelAll((RequestQueue.RequestFilter) new TagRequestFilter(Long.valueOf(this.presenterTag)));
    }

    @Override // com.resync.app.presenters.BasePresenter
    public void saveState(Bundle bundle) {
        bundle.putSerializable("PostsInfo", this.postsInfo);
        bundle.putLong("PresenterTag", this.presenterTag);
        Subreddit subreddit = this.subredditData;
        if (subreddit != null) {
            bundle.putSerializable("Subreddit.Data", subreddit);
        }
    }

    public void closeCursor() {
        Cursor cursor = this.cursor;
        if (cursor == null || cursor.isClosed()) {
            return;
        }
        Logger.verbose("PostsPresenterImpl", "Closing cursor: " + this.cursor);
        this.cursor.close();
        setCursor(null);
    }

    public boolean isRequestInFlight() {
        Logger.verbose("loading", "Checking loading");
        TagRequestFilter tagRequestFilter = new TagRequestFilter(Long.valueOf(this.presenterTag));
        return RedditApplication.bTracker.isRequestInFlight(tagRequestFilter) || RedditApplication.cTracker.isRequestInFlight(tagRequestFilter);
    }

    public long getLastLinkClickTime() {
        return this.lastLinkClickTime;
    }

    @Override // com.resync.app.presenters.PostsPresenter
    public boolean isExpanded() {
        return this.expanded;
    }

    public boolean isLoadBlocked() {
        PostsView postsView;
        PostsInfo postsInfo;
        TagRequestFilter tagRequestFilter = new TagRequestFilter(Long.valueOf(this.presenterTag));
        return RedditApplication.bTracker.isRequestInFlight(tagRequestFilter) || RedditApplication.cTracker.isRequestInFlight(tagRequestFilter) || ((postsView = this.view) != null && postsView.isLoadingViewActive()) || ((postsInfo = this.postsInfo) != null && postsInfo.stagnant);
    }

    public PostsInfo getPostsInfo() {
        return this.postsInfo;
    }

    @Override // com.resync.app.presenters.BaseCursorPresenter
    public void unhidePost(Post post) {
        HideManager.unhide(this.context, post);
    }

    public boolean isCursorEmpty() {
        return getCursor() == null || getCursor().isClosed() || getCursor().getCount() <= 0;
    }

    public void requestLivePosts(boolean z) {
        if (TestUtils.isRobolectric()) {
            return;
        }
        Context context = this.context;
        OAuthRequestManager.send(context, new OAuthPostsRequest(context, Long.valueOf(this.presenterTag), getSubredditName(), getSort(), getAccess(), getPostsInfo(), z, true, new LivePostsResponseListener(), new LivePostsErrorListener()));
    }

    @Override // com.resync.app.presenters.BaseCursorPresenter
    public void peekImage(Post post, ImageView imageView) {
        ImagePeekDialogFragment.show(this.context, post, imageView.getWidth(), imageView.getHeight());
    }

    public void updateViewCursor(Cursor cursor, boolean z) {
        if (z) {
            Logger.verbose("PostsPresenterImpl", "Setting the enter exit animation");
            this.view.setEnterExitAnimation();
        }
        Logger.verbose("PostsPresenterImpl", "Setting cursor: " + this.cursor);
        this.view.setCursor(cursor);
    }

    public void onCursorLoaded(Cursor cursor) {
        Logger.verbose("PostsPresenterImpl", "Cursor loaded: " + cursor);
        if (this.context == null) {
            return;
        }
        if (cursor != null && !cursor.isClosed()) {
            Logger.verbose("PostsPresenterImpl", "Loaded: " + cursor.getCount());
        }
        updateViewCursor(cursor, setCursor(cursor));
        setLoading(false, isRequestInFlight());
        PostsInfo postsInfo = this.postsInfo;
        if (postsInfo.filtersBlocked) {
            Logger.verbose("PostsPresenterImpl", "Filters blocked loading");
            showStatus(R.string.fragment_posts_filters_set);
            return;
        }
        if (!postsInfo.stagnant) {
            if (cursor == null || cursor.getCount() != 0 || ConnectivityHelper.isConnected(this.context)) {
                return;
            }
            this.view.showOffline();
            return;
        }
        if (getCursor() != null && getCursor().getCount() > 0) {
            Logger.verbose("PostsPresenterImpl", "Detected stagnant load");
            showStatus(R.string.fragment_posts_no_more_posts);
        } else if (MaterialPostsHelper.isUserProfile(getSubredditName())) {
            showStatus(R.string.fragment_posts_user_no_posts);
        } else {
            showStatus(R.string.fragment_posts_no_posts);
        }
    }

    @Override // com.resync.app.presenters.BasePresenter
    public void onStart() {
        Logger.verbose("PostsPresenterImpl", "onStart");
        this.started = true;
        registerLoaderListener();
    }

    public void showStatus(int message) {
        this.view.showStatusMessage(message);
    }

    @Override // com.resync.app.presenters.PostsPresenter
    public boolean load(boolean z) {
        if (!ConnectivityHelper.isConnected(this.context)) {
            Logger.verbose("PostsPresenterImpl", "Posts not requested (presenter offline)");
            loadCursor();
            return false;
        }
        if ("list__watching".equalsIgnoreCase(getSubredditName())) {
            WatchingHelper.applyWatchedPosts(this.context);
            loadCursor();
            return true;
        }
        Logger.verbose("PostsPresenterImpl", "Requesting live posts: " + this.presenterTag);
        setLoading(z, true);
        if (isCursorEmpty() && !PostInfoHelper.isSpecialSubreddit(this.subreddit)) {
            Context context = this.context;
            OAuthRequestManager.send(context, new OAuthSubredditInfoRequest(context, Long.valueOf(this.presenterTag), this.subreddit, new SubredditInfoResponseListener(z), new SubredditInfoErrorListener()));
            return true;
        }
        Logger.verbose("PostsPresenterImpl", "Skipping loading post info");
        this.view.disableNsfwScreenshotBlock();
        this.view.setTitle(this.subreddit);
        requestLivePosts(z);
        return true;
    }

    public void onLoadError(VolleyError volleyError) {
        Logger.verbose("PostsPresenterImpl", "Generic error: " + volleyError);
        this.view.showError(volleyError);
    }

    @Override // com.resync.app.presenters.PostsPresenter
    public String getSort() {
        return this.sort;
    }

    @Override // com.resync.app.presenters.PostsPresenter
    public void markAllRead() {
        this.view.setRefreshing(true);
        this.context.getContentResolver().update(RedditProvider.URI_HIDE_ALL, null, null, null);
    }

    public void showListing() {
        setLoading(false, false);
        this.view.showInvalidSub();
    }

    public void setLoading(boolean loading, boolean refreshing) {
        this.view.setLoading(loading, refreshing);
    }

    @Override // com.resync.app.presenters.BasePresenter
    public void recover() {
        Logger.verbose("PostsPresenterImpl", "Recovering from an error");
        cancelAllRequests();
        Cursor cursor = this.cursor;
        if (cursor == null || cursor.getCount() <= 0) {
            refresh();
        } else {
            load(false);
        }
    }

    public void showNsfwSub() {
        setLoading(false, false);
        this.view.showNsfwSub();
    }

    @Override // com.resync.app.presenters.PostsPresenter
    public String getAccess() {
        return this.access;
    }

    public void showForbidden() {
        setLoading(false, false);
        this.view.showPrivateSub();
    }

    @Override // com.resync.app.presenters.BaseCursorPresenter
    public void hidePost(Post post) {
        Logger.verbose("Hide post!");
        this.view.setRefreshing(false);
        BusSingleton.getBus().post(new StopExoplayerEvent());
        if (HideManager.hide(this.context, post)) {
            this.view.onPostHidden(post);
        }
    }

    public void registerLoaderListener() {
        if (this.cursorLoader == null || this.loaderListener != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("The LoaderListener was not registered (");
            sb2.append(this.cursor == null ? "CursorLoadeer was null" : "LoaderListener wasn't null");
            sb2.append(")");
            Logger.verbose("PostsPresenterImpl", sb2.toString());
            return;
        }
        Logger.verbose("PostsPresenterImpl", "Registering the LoaderListener");
        CursorLoadListener listener = new CursorLoadListener();
        this.loaderListener = listener;
        this.cursorLoader.registerListener(0, listener);
        this.cursorLoader.setUpdateThrottle(300L);
        this.cursorLoader.startLoading();
    }

    public void setAccess(String str) {
        this.access = str;
    }

    public boolean setCursor(Cursor cursor) {
        boolean z = this.cursor == null && cursor != null;
        if (z) {
            this.view.setSwipeEnabled(true);
        }
        this.cursor = cursor;
        return z;
    }

    public void setToolbar(boolean z) {
        this.toolbar = z;
    }

    @Override // com.resync.app.presenters.BaseCursorPresenter
    public void openLink(Pair<View, String>[] pairArr, Post post) {
        if (!canOpenLink()) {
            Logger.verbose("PostsPresenterImpl", "Bouncing link!");
            return;
        }
        Logger.verbose("PostsPresenterImpl", "Opening link: " + post);
        Logger.verbose("PostsPresenterImpl", "Sync type: " + post.getSyncType());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Text post: ");
        sb2.append(post.getSyncType() == 2);
        Logger.verbose("PostsPresenterImpl", sb2.toString());
        if (post.getSyncType() == 5 || post.getSyncType() == 2) {
            Logger.verbose("Opening comments");
            setLastLinkClickTime(0L);
            openComments(post);
        } else {
            this.view.setNoAnimation();
            if (pairArr == null || pairArr.length <= 0) {
                PostsHelper.markViewed(this.context, post);
            } else {
                PostsHelper.markViewedDelayed(this.context, post);
            }
            LinkHelper.open(this.context, pairArr, post);
        }
    }

    public void setLastLinkClickTime(long j) {
        this.lastLinkClickTime = j;
    }

    @Override // com.resync.app.presenters.PostsPresenter
    public void changeAccessAndSort(Bundle bundle, String str, String str2) {
        bundle.remove("access");
        bundle.putString("access", str);
        bundle.remove("sort");
        bundle.putString("sort", str2);
        reset();
        setAccess(str);
        setSort(str2);
        load(true);
    }

    public void setPostsInfo(PostsInfo postsInfo) {
        this.postsInfo = postsInfo;
        Logger.verbose("PostsPresenterImpl", "Setting posts info");
    }

    public void setSort(String str) {
        this.sort = str;
    }

    public void setSubredditName(String str) {
        this.subreddit = str;
        this.view.resetViewState();
    }

    @Override // com.resync.app.presenters.BaseCursorPresenter
    public void openLinkExternally(Post post) {
        com.resync.app.util.links.LinkHandler.openCommentsForPost(this.context, post);
        this.view.notifyCommentsOpened();
    }

    public void setUiMode(int uiMode) {
        this.uiMode = uiMode;
    }

    @Override // com.resync.app.presenters.BasePresenter
    public void onDestroy() {
        Logger.verbose("PostsPresenterImpl", "onDestroy");
        cancelAllRequests();
        closeCursor();
    }

    @Override // com.resync.app.presenters.BasePresenter
    public void onPause() {
        Logger.verbose("PostsPresenterImpl", "onPause");
    }

    @Override // com.resync.app.presenters.BasePresenter
    public void onStop() {
        Logger.verbose("PostsPresenterImpl", "onStop");
        this.started = false;
        unregisterLoaderListener();
        PostsView postsView = this.view;
        if (postsView != null) {
            postsView.setCursor(null);
        }
    }

    @Override // com.resync.app.presenters.PostsPresenter
    public Subreddit getUserOrSubredditInfo() {
        Subreddit subreddit = this.subredditData;
        if (subreddit == null) {
            return null;
        }
        if (subreddit.kind.equalsIgnoreCase("t2")) {
            return this.subredditData;
        }
        if (this.subredditData.kind.equals("t5")) {
            return this.subredditData;
        }
        return null;
    }

    public void unregisterLoaderListener() {
        if (this.cursorLoader == null || this.loaderListener == null) {
            Logger.verbose("PostsPresenterImpl", "The LoaderListener was not unregistered");
            return;
        }
        Logger.verbose("PostsPresenterImpl", "Unregistered the LoaderListener");
        this.cursorLoader.unregisterListener(this.loaderListener);
        this.loaderListener = null;
    }

    @Override // com.resync.app.presenters.BasePresenter
    public void restoreArguments(Bundle bundle) {
        setSubredditName(bundle.getString("subreddit"));
        setAccess(bundle.getString("access"));
        setSort(bundle.getString("sort"));
        setUiMode(bundle.getInt("ui_mode"));
        setToolbar(bundle.getBoolean("toolbar"));
    }

    @Override // com.resync.app.presenters.BaseCursorPresenter
    public Cursor getCursor() {
        return this.cursor;
    }

    @Override // com.resync.app.presenters.PostsPresenter
    public void toggleExpanded() {
        this.expanded = !this.expanded;
        RedditApplication.getAppContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
    }

    @Override // com.resync.app.presenters.PostsPresenter
    public int getUiMode() {
        return this.uiMode;
    }

    @Override // com.resync.app.presenters.BaseCursorPresenter
    public void openComments(Post post) {
        if (!canOpenLink()) {
            Logger.verbose("PostsPresenterImpl", "Bouncing link!");
            return;
        }
        Logger.verbose("PostsPresenterImpl", "Opening comments: " + post);
        this.view.setNoAnimation();
        PostsHelper.markViewedDelayed(this.context, post);
        com.resync.app.util.links.LinkHandler.openComments(this.context, post);
    }

    @Override // com.resync.app.presenters.BaseCursorPresenter
    public void loadCursor() {
        if (this.context == null) {
            return;
        }
        Logger.verbose("PostsPresenterImpl", "Instructing the cursor to load data");
        if (this.cursorLoader != null) {
            unregisterLoaderListener();
            this.cursorLoader = null;
        }
        Logger.verbose("PostsPresenterImpl", "Creating a new cursor loader!");
        this.cursorLoader = PostsManager.createPostsLoader(this.context, getSubredditName(), this.uiMode);
        registerLoaderListener();
    }

    @Override // com.resync.app.presenters.BaseCursorPresenter
    public void toggleHide(Post post) {
        BusSingleton.getBus().post(new StopExoplayerEvent());
        if (post.isHidden()) {
            unhidePost(post);
        } else {
            hidePost(post);
        }
    }
}

