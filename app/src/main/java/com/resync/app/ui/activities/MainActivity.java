package com.resync.app.ui.activities;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.resync.app.widgets.SidebarFrameLayout;
import com.resync.app.R;
import com.resync.app.auth.WebSessionManager;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.ActionsBottomSheetFragment;
import com.resync.app.ui.fragments.DrawerFragment;
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
import com.resync.app.workers.ClearCacheWorker;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.ui.TabletHelper;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.dialogs.ClosingBottomSheetFragment;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.StartupHelper;
import com.resync.app.ui.interfaces.PostHost;
import com.resync.app.ui.interfaces.VideoProvider;
import com.resync.app.util.Logger;
import com.resync.app.util.links.LinkHelper;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: loaded from: classes.dex */
public class MainActivity extends BaseMaterialActivity implements VideoProvider, PostHost {
    private static String LOG_TAG = "main_activity";

    public static String EXTRA_LINK = "link";

    @BindView(R.id.appbar)
    PostsAppBarLayout appBarLayout;

    @BindView(R.id.navigation)
    public SyncBottomNavigationView bottomNavigationView;

    private boolean exitPressed = false;

    Integer pendingMessagingSection;

    @BindView(R.id.drawer)
    SyncDrawerLayout drawer;

    @BindView(R.id.navigation_rail)
    public SyncNavigationRailView navigationRail;

    @BindView(R.id.overlapping_panels)
    CustomPanelLayout panel;

    @BindView(R.id.end_panel)
    SidebarFrameLayout sidebarLayout;

    @BindView(R.id.video_helper)
    VideoHelperView videoHelperView;

    private String getDefaultSub() {
        return SettingsSingleton.getSettings().defaultSub;
    }

    private DrawerFragment getDrawerFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("drawer");
        if (fragment instanceof DrawerFragment) {
            return (DrawerFragment) fragment;
        }
        return null;
    }

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

    private void handleMessagingIntent(Intent intent) {
        if (intent != null && intent.hasExtra("messaging")) {
            intent.removeExtra("messaging");
            ((NotificationManager) getActivity().getSystemService("notification")).cancel(1);
            if (!hasNavBar()) {
                MessagingActivity.start(getActivity());
                return;
            }
            this.pendingMessagingSection = Integer.valueOf(intent.getIntExtra("messaging", 1));
            if (getMessagingFragment() != null) {
                getMessagingFragment().changeSection(this.pendingMessagingSection.intValue());
                this.pendingMessagingSection = null;
            }
            selectNav(R.id.menu_messages);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onExpandAppBar() {
        this.appBarLayout.setExpanded(true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onHandleMessagingDelayed() {
        handleMessagingIntent(getIntent());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onPostsAdded() {
        try {
            findViewById(getContentId()).postDelayed(new Runnable() { // from class: m9.i
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.this.onHandleMessagingDelayed();
                }
            }, 300L);
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void selectRailItem(int navItemId) {
        this.navigationRail.setSelected(navItemId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void selectBottomItem(int navItemId) {
        this.bottomNavigationView.setSelected(navItemId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void resetExitPressed() {
        this.exitPressed = false;
    }

    public void addDrawerFragment() {
        Logger.verbose(LOG_TAG, "Adding DrawerFragment");
        getSupportFragmentManager().beginTransaction().replace(R.id.start_panel, DrawerFragment.newInstance(), "drawer").commit();
    }

    public void addMessagingFragment() {
        Logger.verbose(LOG_TAG, "Adding MessagingFragment");
        Integer num = this.pendingMessagingSection;
        MessagingFragment messagingFragment = MessagingFragment.newInstance(num != null ? num.intValue() : 0);
        this.pendingMessagingSection = null;
        getSupportFragmentManager().beginTransaction().hide(getPostsFragment()).add(getContentId(), messagingFragment, "messaging").runOnCommit(new Runnable() { // from class: m9.h
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.onExpandAppBar();
            }
        }).commit();
    }

    public void addPostsFragment(String str) {
        Logger.verbose(LOG_TAG, "Adding VerticalPostsFragment");
        getSupportFragmentManager().beginTransaction().add(getContentId(), BasePostsFragment.newVertical(str, true), "posts").runOnCommit(new Runnable() { // from class: m9.g
            @Override // java.lang.Runnable
            public final void run() {
                MainActivity.this.onPostsAdded();
            }
        }).commit();
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

    public boolean hasNavBar() {
        return this.navigationRail != null || this.bottomNavigationView.getVisibility() == 0;
    }

    public boolean isMessagingSelected() {
        return getSelectedNavId() == R.id.menu_messages;
    }

    public boolean isPostsSelected() {
        return getSelectedNavId() == R.id.menu_posts;
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
            Logger.verbose(LOG_TAG, "Showing MessagingFragment");
            getSupportFragmentManager().beginTransaction().hide(getPostsFragment()).show(getMessagingFragment()).commit();
        }
        return true;
    }

    @Override // com.resync.app.ui.activities.BaseActivity, android.app.Activity
    public void finish() {
        OAuthRequestManager.cancelAll();
        ClearCacheWorker.enqueue(getContext());
        super.finish();
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected int getAdMode() {
        return 1;
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

    public void openStartPanel() {
        CustomPanelLayout customPanelLayout = this.panel;
        if (customPanelLayout != null) {
            customPanelLayout.openStartPanel();
            return;
        }
        SyncDrawerLayout syncDrawerLayout = this.drawer;
        if (syncDrawerLayout != null) {
            syncDrawerLayout.openStart();
        }
    }

    public void removeDrawerFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("drawer");
        if (fragment instanceof DrawerFragment) {
            Logger.verbose(LOG_TAG, "Removing DrawerFragment");
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    @Override // com.resync.app.ui.interfaces.PostHost
    public void showPosts() {
        if (isPostsSelected()) {
            getPostsFragment().scrollToTop();
        } else {
            Logger.verbose(LOG_TAG, "Showing VerticalPostsFragment");
            getSupportFragmentManager().beginTransaction().hide(getMessagingFragment()).show(getPostsFragment()).commit();
        }
    }

    public void clearSidebar() {
        SidebarFrameLayout sidebarFrameLayout = this.sidebarLayout;
        if (sidebarFrameLayout != null) {
            sidebarFrameLayout.clear();
        }
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
    public boolean hasPanelOrDrawer() {
        return (this.panel == null && this.drawer == null) ? false : true;
    }

    @Override // com.resync.app.ui.activities.BaseMaterialActivity, com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ButterKnife.bind(this);
        StartupHelper.onAppStart(this, bundle);
        String defaultSub = getDefaultSub();
        if (getIntent() != null && getIntent().hasExtra(EXTRA_LINK)) {
            String stringExtra = getIntent().getStringExtra(EXTRA_LINK);
            getIntent().removeExtra(EXTRA_LINK);
            if (LinkTypeHelper.isSubreddit(stringExtra)) {
                defaultSub = com.resync.app.util.links.LinkHandler.getSubreddit(stringExtra);
            } else {
                LinkHelper.open(getActivity(), stringExtra);
            }
        }
        if (getPostsFragment() == null) {
            addPostsFragment(defaultSub);
        }
        if (this.panel != null && getDrawerFragment() == null) {
            addDrawerFragment();
        }
        if (this.drawer != null && getDrawerFragment() == null) {
            addDrawerFragment();
        }
        if (findViewById(R.id.second_pane) != null && getTabbedCommentsFragment() == null) {
            addTabbedComments();
        }
        if (bundle != null && bundle.containsKey("nav")) {
            final int savedNavId = bundle.getInt("nav");
            bundle.remove("nav");
            SyncNavigationRailView syncNavigationRailView = this.navigationRail;
            if (syncNavigationRailView != null) {
                syncNavigationRailView.post(new Runnable() { // from class: m9.l
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity.this.selectRailItem(savedNavId);
                    }
                });
            } else if (this.bottomNavigationView.getVisibility() == 0) {
                this.bottomNavigationView.post(new Runnable() { // from class: m9.k
                    @Override // java.lang.Runnable
                    public final void run() {
                        MainActivity.this.selectBottomItem(savedNavId);
                    }
                });
            }
        }
        if (bundle == null) {
            showAd(); // ads removed
            // Shutdown nag removed: the original shows ClosingBottomSheetFragment ("Sync will shut
            // down June 30 2023") on every cold start. The app works again via web-session auth, so
            // the kill-switch dialog is suppressed (same fix ReVanced applies).
            maybeRepairUsername();
        }
    }

    /**
     * Self-heals accounts saved with the bogus "Reddit user" fallback name (web-session logins from
     * before /api/v1/me resolution existed). That literal name contains a space, so every
     * /user/<name>/about request 400/404s (broke the Profile button). If we detect it, silently
     * re-resolve the real username via the bearer token — no re-login needed.
     */
    private void maybeRepairUsername() {
        final AccountSingleton account = AccountSingleton.getInstance();
        if (!account.isLoggedIn() || !"Reddit user".equals(account.getUsername())) {
            return;
        }
        final WebSessionManager mgr = WebSessionManager.get(this);
        mgr.getToken(new WebSessionManager.TokenCallback() {
            @Override
            public void onToken(String jwt) {
                mgr.fetchUsername(jwt, new WebSessionManager.NameCallback() {
                    @Override
                    public void onName(String name) {
                        if (name != null && !name.isEmpty() && !"Reddit user".equals(name)) {
                            android.util.Log.i("MainActivity", "Repaired username -> " + name);
                            account.updateSession(name, account.getRefreshToken(), false);
                        }
                    }
                });
            }
            @Override public void onError(String reason) { /* offline / no session — try again next launch */ }
        });
    }

    @Override // com.resync.app.ui.activities.BaseActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
        if (keyEvent.isCanceled()) {
            return super.onKeyUp(keyCode, keyEvent);
        }
        if (keyCode != 4) {
            try {
                return super.onKeyUp(keyCode, keyEvent);
            } catch (Exception unused) {
                return true;
            }
        }
        if (areAnyPanelsOpen()) {
            closePanels();
            return true;
        }
        if (isMessagingSelected()) {
            selectNav(R.id.menu_posts);
            return true;
        }
        if (SettingsSingleton.getSettings().postsHome) {
            String defaultSub = getDefaultSub();
            if (!StringUtils.equalsIgnoreCase(defaultSub, ContextHelper.getCurrentSubredditName(this))) {
                getPostsFragment().changeSubreddit(defaultSub);
                return true;
            }
        }
        if (!(this.drawer == null && this.panel == null) && SettingsSingleton.getSettings().drawerBack && SettingsSingleton.getSettings().useDrawer) {
            openStartPanel();
            return true;
        }
        if (!SettingsSingleton.getSettings().exitConfirmation) {
            finish();
        } else {
            if (this.exitPressed) {
                finish();
                return true;
            }
            this.exitPressed = true;
            StaticToast.showWithContext(getContext(), "Press BACK again to exit");
            getWindow().getDecorView().postDelayed(new Runnable() { // from class: m9.j
                @Override // java.lang.Runnable
                public final void run() {
                    MainActivity.this.resetExitPressed();
                }
            }, 1000L);
        }
        return true;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.verbose(LOG_TAG, "Got new message");
        handleMessagingIntent(intent);
    }

    @Override // com.resync.app.ui.activities.BaseMaterialActivity, com.resync.app.ui.activities.BaseActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        if (getMessagingFragment() == null || !getMessagingFragment().isAdded()) {
            bundle.putInt("nav", R.id.menu_posts);
        } else {
            bundle.putInt("nav", R.id.menu_messages);
        }
        super.onSaveInstanceState(bundle);
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
            removeDrawerFragment();
            clearSidebar();
        } else {
            if (!SettingsSingleton.getSettings().useDrawer) {
                removeDrawerFragment();
                clearSidebar();
            }
            if (!SyncBottomNavigationView.shouldShow()) {
                selectNav(R.id.menu_posts);
            }
            removeTabbedComments();
        }
        super.recreate();
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected void setupContentView() {
        if (TabletHelper.isTabletModeRaw()) {
            if (TabletHelper.isTabletMode()) {
                setContentView(R.layout.activity_main_tablet);
                return;
            } else {
                setContentView(R.layout.activity_casual);
                return;
            }
        }
        if (SettingsSingleton.getSettings().usePanel && SettingsSingleton.getSettings().useDrawer) {
            setContentView(R.layout.activity_main_panel);
        } else if (SettingsSingleton.getSettings().useDrawer) {
            setContentView(R.layout.activity_main_drawer);
        } else {
            setContentView(R.layout.activity_casual);
        }
    }
}
