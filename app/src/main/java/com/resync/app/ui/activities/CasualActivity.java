package com.resync.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.resync.app.widgets.SidebarFrameLayout;
import com.bumptech.glide.MaterialPostsHelper;
import com.google.android.exoplayer2.C;
import com.resync.app.R;
import com.resync.app.discord.OverlappingPanelsLayout;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.ActionsBottomSheetFragment;
import com.resync.app.ui.fragments.MessagingFragment;
import com.resync.app.ui.fragments.TabbedCommentsFragment;
import com.resync.app.ui.fragments.posts.BasePostsFragment;
import com.resync.app.ui.fragments.posts.VerticalPostsFragment;
import com.resync.app.ui.views.app_bars.PostsAppBarLayout;
import com.resync.app.ui.views.drawer.SyncDrawerLayout;
import com.resync.app.ui.views.main.SyncBottomNavigationView;
import com.resync.app.ui.views.main.SyncNavigationRailView;
import com.resync.app.ui.views.panel.CustomPanelLayout;
import com.resync.app.ui.views.video.VideoHelperView;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.ui.TabletHelper;
import com.resync.app.ui.interfaces.PostHost;
import com.resync.app.ui.interfaces.VideoProvider;
import com.resync.app.util.Logger;
import com.resync.app.util.TestUtils;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkTypeHelper;
import com.resync.app.util.links.LinkHandler;

/* JADX INFO: loaded from: classes.dex */
public class CasualActivity extends BaseMaterialDragActivity implements VideoProvider, PostHost {

    @BindView(R.id.appbar)
    PostsAppBarLayout appBarLayout;

    @BindView(R.id.navigation)
    SyncBottomNavigationView bottomNavigationView;

    /* JADX INFO: renamed from: d0, reason: collision with root package name */
    protected String url;

    @BindView(R.id.drawer)
    SyncDrawerLayout drawer;
    protected boolean background;

    @BindView(R.id.navigation_rail)
    SyncNavigationRailView navigationRail;

    @BindView(R.id.overlapping_panels)
    CustomPanelLayout panel;

    @BindView(R.id.end_panel)
    SidebarFrameLayout sidebarLayout;

    @BindView(R.id.video_helper)
    VideoHelperView videoHelperView;

    private MessagingFragment getMessagingFragment() {
        return (MessagingFragment) getSupportFragmentManager().findFragmentByTag("messaging");
    }

    private VerticalPostsFragment getPostsFragment() {
        return (VerticalPostsFragment) getSupportFragmentManager().findFragmentByTag("posts");
    }

    private TabbedCommentsFragment getTabbedCommentsFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("TabbedCommentsFragment");
        if (fragment instanceof TabbedCommentsFragment) {
            return (TabbedCommentsFragment) fragment;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onExpandAppBar() {
        this.appBarLayout.setExpanded(true, false);
    }

    public static void openDomain(Context context, String str) {
        Intent intent = new Intent(context, (Class<?>) CasualActivity.class);
        String str2 = str.split("/domain/")[1];
        if (str2.contains("/")) {
            str2 = str2.split("/")[0];
        }
        if (str2.contains("\\?")) {
            str2 = str2.split("\\?")[0];
        }
        intent.putExtra("url", MaterialPostsHelper.buildDomainAddress(str2));
        if (TestUtils.isRobolectric()) {
            intent.setFlags(268435456);
        }
        context.startActivity(intent);
    }

    public static void openPostDomain(Context context, Post post) {
        Intent intent = new Intent(context, (Class<?>) CasualActivity.class);
        intent.putExtra("url", MaterialPostsHelper.buildDomainAddress(post.getUrl()));
        if (TestUtils.isRobolectric()) {
            intent.setFlags(268435456);
        }
        context.startActivity(intent);
    }

    public static void openSubreddit(Context context, String str) {
        String url;
        Intent intent = new Intent(context, (Class<?>) CasualActivity.class);
        if (LinkTypeHelper.isMulti(str)) {
            url = "multi_" + com.resync.app.util.links.LinkHandler.getUserFromPath(str);
        } else {
            url = com.resync.app.util.links.LinkHandler.getSubreddit(str);
        }
        intent.putExtra("url", url);
        if (TestUtils.isRobolectric()) {
            intent.setFlags(268435456);
        }
        context.startActivity(intent);
    }

    public static void openSubredditNewTask(Context context, String str) {
        String url;
        Intent intent = new Intent(context, (Class<?>) CasualActivity.class);
        if (LinkTypeHelper.isMulti(str)) {
            url = "multi_" + com.resync.app.util.links.LinkHandler.getUserFromPath(str);
        } else {
            url = com.resync.app.util.links.LinkHandler.getSubreddit(str);
        }
        intent.putExtra("url", url);
        intent.setFlags(524288);
        intent.addFlags(C.BUFFER_FLAG_FIRST_SAMPLE);
        if (TestUtils.isRobolectric()) {
            intent.setFlags(268435456);
        }
        context.startActivity(intent);
    }

    public static void openUser(Context context, String str) {
        String username = com.resync.app.util.links.LinkHandler.getUsername(str);
        Intent intent = new Intent(context, (Class<?>) CasualActivity.class);
        intent.putExtra("url", MaterialPostsHelper.buildOverviewProfileAddress(username));
        if (TestUtils.isRobolectric()) {
            intent.setFlags(268435456);
        }
        context.startActivity(intent);
    }

    public static void openUserSection(Context context, String str, String str2) {
        Intent intent = new Intent(context, (Class<?>) CasualActivity.class);
        intent.putExtra("url", MaterialPostsHelper.buildProfileSectionAddress(str, str2));
        if (TestUtils.isRobolectric()) {
            intent.setFlags(268435456);
        }
        context.startActivity(intent);
    }

    public static void openSaved(Context context, String str) {
        String username = com.resync.app.util.links.LinkHandler.getUsername(str);
        Intent intent = new Intent(context, (Class<?>) CasualActivity.class);
        intent.putExtra("url", MaterialPostsHelper.buildProfileSectionAddress(username, "Saved"));
        if (TestUtils.isRobolectric()) {
            intent.setFlags(268435456);
        }
        context.startActivity(intent);
    }

    public static void openSubredditSearch(Context context, String str, String str2) {
        Intent intent = new Intent(context, (Class<?>) CasualActivity.class);
        intent.putExtra("url", MaterialPostsHelper.buildSearchAddress(str, str2));
        if (TestUtils.isRobolectric()) {
            intent.setFlags(268435456);
        }
        context.startActivity(intent);
    }

    public static void openPostUrl(Context context, Post post) {
        if (TextUtils.isEmpty(post.getRawUrl())) {
            StaticToast.showWithContext(context, "This post has no URL!");
            return;
        }
        Intent intent = new Intent(context, (Class<?>) CasualActivity.class);
        intent.putExtra("url", MaterialPostsHelper.buildSeenAddress(post.getRawUrl()));
        if (TestUtils.isRobolectric()) {
            intent.setFlags(268435456);
        }
        context.startActivity(intent);
    }

    public static void openWatching(Context context) {
        Intent intent = new Intent(context, (Class<?>) CasualActivity.class);
        intent.putExtra("url", "list__watching");
        context.startActivity(intent);
    }

    public void addMessagingFragment() {
        Logger.verbose("BaseActivity", "Adding MessagingFragment");
        getSupportFragmentManager().beginTransaction().hide(getPostsFragment()).add(getContentId(), MessagingFragment.newInstance(0), "messaging").runOnCommit(new Runnable() { // from class: m9.c
            @Override // java.lang.Runnable
            public final void run() {
                CasualActivity.this.onExpandAppBar();
            }
        }).commit();
    }

    public void addPostsFragment(String str) {
        Logger.verbose("BaseActivity", "Adding VerticalPostsFragment");
        getSupportFragmentManager().beginTransaction().add(getContentId(), BasePostsFragment.newVertical(str, true), "posts").commit();
    }

    public void addTabbedComments() {
        getSupportFragmentManager().beginTransaction().replace(R.id.second_pane, new TabbedCommentsFragment(), "TabbedCommentsFragment").commit();
    }

    public void closePanels() {
        CustomPanelLayout customPanelLayout = this.panel;
        if (customPanelLayout != null) {
            customPanelLayout.closePanels();
            return;
        }
        SyncDrawerLayout syncDrawerLayout = this.drawer;
        if (syncDrawerLayout != null) {
            if (syncDrawerLayout.isStartOpen()) {
                this.drawer.closeStart();
            } else if (this.drawer.isEndOpen()) {
                this.drawer.closeEnd();
            }
        }
    }

    public int getContentId() {
        return R.id.content_wrapper;
    }

    public int getSelectedNavId() {
        SyncNavigationRailView syncNavigationRailView = this.navigationRail;
        return syncNavigationRailView != null ? syncNavigationRailView.getSelectedItemId() : this.bottomNavigationView.getVisibility() == 0 ? this.bottomNavigationView.getSelectedItemId() : R.id.menu_posts;
    }

    public boolean isMessagingSelected() {
        return getSelectedNavId() == R.id.menu_messages;
    }

    public boolean isPostsSelected() {
        return getSelectedNavId() == R.id.menu_posts;
    }

    public void onRecreatePanels() {
    }

    public void removeTabbedComments() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("TabbedCommentsFragment");
        if (fragment instanceof TabbedCommentsFragment) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    public void selectNav(int navItemId) {
        SyncNavigationRailView syncNavigationRailView = this.navigationRail;
        if (syncNavigationRailView != null) {
            syncNavigationRailView.setSelected(navItemId);
        } else if (this.bottomNavigationView.getVisibility() == 0) {
            this.bottomNavigationView.setSelected(navItemId);
        }
    }

    @Override // com.resync.app.ui.interfaces.PostHost
    public boolean areAnyPanelsOpen() {
        CustomPanelLayout customPanelLayout = this.panel;
        if (customPanelLayout != null && customPanelLayout.isPanelOpen()) {
            return true;
        }
        SyncDrawerLayout syncDrawerLayout = this.drawer;
        if (syncDrawerLayout != null) {
            return syncDrawerLayout.isStartOpen() || this.drawer.isEndOpen();
        }
        return false;
    }

    @Override // com.resync.app.ui.interfaces.PostHost
    public boolean showMessaging() {
        if (!AccountSingleton.getInstance().isLoggedIn()) {
            StaticToast.show(getActivity(), R.string.common_generic_error_logged_out);
            return false;
        }
        if (isMessagingSelected()) {
            getMessagingFragment().scrollToTop();
            return true;
        }
        if (getMessagingFragment() == null) {
            addMessagingFragment();
        } else {
            Logger.verbose("BaseActivity", "Showing MessagingFragment");
            getSupportFragmentManager().beginTransaction().hide(getPostsFragment()).show(getMessagingFragment()).commit();
        }
        return true;
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected int getAdMode() {
        return 0;
    }

    @Override // com.resync.app.widgets.drag.DragActivity
    public int getDragLayoutRes() {
        return TabletHelper.isTabletModeRaw() ? TabletHelper.isTabletMode() ? R.layout.activity_main_tablet : R.layout.activity_casual : (SettingsSingleton.getSettings().usePanel && SettingsSingleton.getSettings().useDrawer) ? R.layout.activity_casual_panel : SettingsSingleton.getSettings().useDrawer ? R.layout.activity_casual_drawer : R.layout.activity_casual;
    }

    @Override // com.resync.app.ui.interfaces.PostHost
    public void openEndPanel() {
        CustomPanelLayout customPanelLayout = this.panel;
        if (customPanelLayout != null) {
            customPanelLayout.openEndPanel();
            return;
        }
        SyncDrawerLayout syncDrawerLayout = this.drawer;
        if (syncDrawerLayout != null) {
            syncDrawerLayout.openEnd();
        }
    }

    @Override // com.resync.app.ui.interfaces.SnackbarInterface
    public boolean isContentLoading() {
        VerticalPostsFragment verticalPostsFragment = (VerticalPostsFragment) findFragmentByType(VerticalPostsFragment.class, R.id.content_wrapper);
        if (verticalPostsFragment != null) {
            return verticalPostsFragment.isLoadingViewActive();
        }
        return false;
    }

    @Override // com.resync.app.ui.interfaces.PostHost
    public void showPosts() {
        if (isPostsSelected()) {
            getPostsFragment().scrollToTop();
        } else {
            Logger.verbose("BaseActivity", "Showing VerticalPostsFragment");
            getSupportFragmentManager().beginTransaction().hide(getMessagingFragment()).show(getPostsFragment()).commit();
        }
    }

    @Override // com.resync.app.ui.interfaces.PostHost
    public boolean hasPanelOrDrawer() {
        return (this.panel == null && this.drawer == null) ? false : true;
    }

    @Override // com.resync.app.ui.activities.BaseMaterialDragActivity, com.resync.app.ui.activities.BaseMaterialActivity, com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        if (getIntent() != null) {
            this.background = (getIntent().getFlags() & 524288) == 524288;
        }
        this.url = getIntent().getStringExtra("url");
        super.onCreate(bundle);
        ButterKnife.bind(this);
        if (getPostsFragment() == null) {
            addPostsFragment(this.url);
        }
        if (findViewById(R.id.second_pane) != null && getTabbedCommentsFragment() == null) {
            addTabbedComments();
        }
        CustomPanelLayout customPanelLayout = this.panel;
        if (customPanelLayout != null) {
            customPanelLayout.setStartPanelLockState(OverlappingPanelsLayout.LockState.CLOSE);
        }
    }

    @Override // com.resync.app.ui.activities.BaseActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
        if (keyEvent.isCanceled()) {
            return super.onKeyUp(keyCode, keyEvent);
        }
        if (keyCode == 4 && areAnyPanelsOpen()) {
            closePanels();
            return true;
        }
        try {
            return super.onKeyUp(keyCode, keyEvent);
        } catch (Exception unused) {
            return true;
        }
    }

    @Override // com.resync.app.ui.interfaces.VideoProvider
    public VideoHelperView getVideoHelperView() {
        return this.videoHelperView;
    }

    @Override // com.resync.app.ui.interfaces.PostHost
    public void updateSidebar(String str) {
        SidebarFrameLayout sidebarFrameLayout = this.sidebarLayout;
        if (sidebarFrameLayout != null) {
            sidebarFrameLayout.update(str);
        }
    }

    @Override // com.resync.app.ui.activities.BaseMaterialActivity, android.app.Activity
    public void recreate() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ActionsBottomSheetFragment.class.getSimpleName());
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        if (TabletHelper.isTabletMode()) {
            onRecreatePanels();
        } else {
            if (!SettingsSingleton.getSettings().useDrawer) {
                onRecreatePanels();
            }
            if (!SyncBottomNavigationView.shouldShow()) {
                selectNav(R.id.menu_posts);
            }
            removeTabbedComments();
        }
        super.recreate();
    }
}
