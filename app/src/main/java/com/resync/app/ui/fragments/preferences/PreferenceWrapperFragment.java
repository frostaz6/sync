package com.resync.app.ui.fragments.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.RedditHelper;
import com.google.android.material.appbar.AppBarLayout;
import com.resync.app.R;
import com.resync.app.ui.fragments.RedditFragment;
import com.resync.app.receiver.UltraReceiver;
import com.resync.app.sections.preferences.toolbar.PreferencesSmallSubredditToolbarView;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.views.buttons.SettingsButton;
import com.resync.app.ui.views.core.MaterialYouToolbar;
import com.resync.app.ui.views.transparent_status.CustomAppBarLayout;
import com.resync.app.ui.views.transparent_status.CustomCoordinatorLayout;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.ui.TabletHelper;
import com.resync.app.events.ResetSettingsEvent;
import com.resync.app.events.SettingsChangedEvent;
import com.resync.app.events.UiModeChangedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.fragments.preferences.PreferencesPostCardViewFragment;
import com.resync.app.fragments.preferences.PreferencesPostCompactViewFragment;
import com.resync.app.fragments.preferences.PreferencesPostListViewFragment;
import com.resync.app.fragments.preferences.PreferencesPostSlideViewFragment;
import com.resync.app.fragments.preferences.PreferencesPostSmallCardViewFragment;
import com.resync.app.fragments.preferences.PreferencesPostSmallerCardViewFragment;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.MonetBottomSheetFragment;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.dialogs.DataSaverAlertDialogBottomSheet;
import com.resync.app.helpers.DataSavingHelper;
import com.resync.app.util.ColorUtils;
import com.resync.app.fragments.preferences.BasePreferencesFragment;
import com.resync.app.fragments.preferences.PreferencesAnalyticsFragment;
import com.resync.app.fragments.preferences.PreferencesAnimationsFragment;
import com.resync.app.fragments.preferences.PreferencesAwardsFragment;
import com.resync.app.fragments.preferences.PreferencesCommentViewCustomizationFragment;
import com.resync.app.fragments.preferences.PreferencesCommentsFragment;
import com.resync.app.fragments.preferences.PreferencesDarkModeFragment;
import com.resync.app.fragments.preferences.PreferencesDataFragment;
import com.resync.app.fragments.preferences.PreferencesDeveloperFragment;
import com.resync.app.fragments.preferences.PreferencesExperimentalFragment;
import com.resync.app.fragments.preferences.PreferencesFiltersFragment;
import com.resync.app.fragments.preferences.PreferencesGeneralFragment;
import com.resync.app.fragments.preferences.PreferencesHistoryFragment;
import com.resync.app.fragments.preferences.PreferencesImagesFragment;
import com.resync.app.fragments.preferences.PreferencesLegacyFragment;
import com.resync.app.fragments.preferences.PreferencesLinksFragment;
import com.resync.app.fragments.preferences.PreferencesMessagingFragment;
import com.resync.app.fragments.preferences.PreferencesMoneteThemeAdvancedFragment;
import com.resync.app.fragments.preferences.PreferencesNewRootFragment;
import com.resync.app.fragments.preferences.PreferencesOtherFragment;
import com.resync.app.fragments.preferences.PreferencesPostsFragment;
import com.resync.app.fragments.preferences.PreferencesRedditWebPreferencesFragment;
import com.resync.app.fragments.preferences.PreferencesSecurityFragment;
import com.resync.app.fragments.preferences.PreferencesTabletFragment;
import com.resync.app.fragments.preferences.PreferencesUltraFragment;
import com.resync.app.fragments.preferences.PreferencesBackupFragment;
import com.resync.app.events.UltraStatusChangedEvent;

/* JADX INFO: loaded from: classes2.dex */
public class PreferenceWrapperFragment extends RedditFragment implements Toolbar.OnMenuItemClickListener {

    @BindView(R.id.appbar)
    public CustomAppBarLayout mAppBarLayout;

    @BindView(R.id.coordinator)
    public CustomCoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.toolbar_small)
    public PreferencesSmallSubredditToolbarView mPreferencesSmallSubredditToolbarView;

    @BindView(R.id.toolbar)
    public MaterialYouToolbar mToolbar;
    private int mode;

    /* JADX INFO: renamed from: s0, reason: collision with root package name */
    private String highlightKey;
    protected UltraReceiver ultraReceiver;

    class UltraStatusReceiver extends UltraReceiver {
        UltraStatusReceiver() {
        }

        @Override // com.resync.app.receiver.UltraReceiver, android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            PreferenceWrapperFragment.this.showFragment();
            BusSingleton.getBus().post(new UltraStatusChangedEvent());
        }
    }

    private Fragment createPreferenceFragment(int mode) {
        if (mode == getInteger(R.integer.POST_VIEW_OPTIONS)) {
            if (UiSingleton.getInstance().getUiMode() == 5) {
                return PreferencesPostSlideViewFragment.newInstance(this.highlightKey);
            }
            if (UiSingleton.getInstance().getUiMode() == 4) {
                return PreferencesPostCardViewFragment.newInstance(this.highlightKey);
            }
            if (UiSingleton.getInstance().getUiMode() == 3) {
                return PreferencesPostSmallCardViewFragment.newInstance(this.highlightKey);
            }
            if (UiSingleton.getInstance().getUiMode() == 2) {
                return PreferencesPostSmallerCardViewFragment.newInstance(this.highlightKey);
            }
            if (UiSingleton.getInstance().getUiMode() == 1) {
                return PreferencesPostCompactViewFragment.newInstance(this.highlightKey);
            }
            if (UiSingleton.getInstance().getUiMode() == 0) {
                return PreferencesPostListViewFragment.newInstance(this.highlightKey);
            }
        } else {
            if (mode == getInteger(R.integer.COMMENT_VIEW_OPTIONS)) {
                return PreferencesCommentViewCustomizationFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.ROOT)) {
                return PreferencesNewRootFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.OTHER)) {
                return PreferencesOtherFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.COMMENTS)) {
                return PreferencesCommentsFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.GENERAL)) {
                return PreferencesGeneralFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.MESSAGING)) {
                return PreferencesMessagingFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.LINKS)) {
                return PreferencesLinksFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.POSTS)) {
                return PreferencesPostsFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.IMAGES)) {
                return PreferencesImagesFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.FILTERS)) {
                return PreferencesFiltersFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.DATA)) {
                return PreferencesDataFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.ANIMATIONS)) {
                return PreferencesAnimationsFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.BACKUP)) {
                return PreferencesBackupFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.HISTORY)) {
                return PreferencesHistoryFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.ANALYTICS)) {
                return PreferencesAnalyticsFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.SECURITY)) {
                return PreferencesSecurityFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.WEB_PREFERENCES)) {
                return PreferencesRedditWebPreferencesFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.DEVELOPER_OPTIONS)) {
                return PreferencesDeveloperFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.DARK_MODE)) {
                return PreferencesDarkModeFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.AWARDS)) {
                return PreferencesAwardsFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.ULTRA)) {
                return PreferencesUltraFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.TABLET)) {
                return PreferencesTabletFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.EXPERIMENTS)) {
                return PreferencesExperimentalFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.MONET_ADVANCED)) {
                return PreferencesMoneteThemeAdvancedFragment.newInstance(this.highlightKey);
            }
            if (mode == getInteger(R.integer.LEGACY)) {
                return PreferencesLegacyFragment.newInstance(this.highlightKey);
            }
        }
        throw new RuntimeException("Unsupported preference fragment.");
    }

    private int getInteger(int resId) {
        return getResources().getInteger(resId);
    }

    public static PreferenceWrapperFragment newInstance(int mode, String highlight) {
        PreferenceWrapperFragment preferenceWrapperFragment = new PreferenceWrapperFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("mode", mode);
        bundle.putString("highlight", highlight);
        preferenceWrapperFragment.setArguments(bundle);
        return preferenceWrapperFragment;
    }

    private boolean isRoot() {
        return this.mode == getInteger(R.integer.ROOT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambdaShowDataSaver(View view) {
        FragmentUtils.show(DataSaverAlertDialogBottomSheet.class, getFragmentManager());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambdaFinish(View view) {
        getActivity().finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambdaScrollToTop(View view) {
        scrollToTop();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showFragment() {
        getFragmentManager().beginTransaction().replace(R.id.content, createPreferenceFragment(this.mode)).commit();
    }

    public AppBarLayout getAppBar() {
        return this.mAppBarLayout;
    }

    public String getScreenTitle() {
        if (this.mode == getInteger(R.integer.ROOT)) {
            return "Settings";
        }
        if (this.mode == getInteger(R.integer.OTHER)) {
            return "Everything else";
        }
        if (this.mode == getInteger(R.integer.COMMENTS)) {
            return "Comment options";
        }
        if (this.mode == getInteger(R.integer.GENERAL)) {
            return "General";
        }
        if (this.mode == getInteger(R.integer.MESSAGING)) {
            return "Messaging";
        }
        if (this.mode == getInteger(R.integer.LINKS)) {
            return "Link handling";
        }
        if (this.mode == getInteger(R.integer.POSTS)) {
            return "Post options";
        }
        if (this.mode == getInteger(R.integer.IMAGES)) {
            return "Media";
        }
        if (this.mode == getInteger(R.integer.FILTERS)) {
            return "Filters";
        }
        if (this.mode == getInteger(R.integer.DATA)) {
            return "Data Options";
        }
        if (this.mode == getInteger(R.integer.ANIMATIONS)) {
            return "Animations";
        }
        if (this.mode == getInteger(R.integer.BACKUP)) {
            return "Backup";
        }
        if (this.mode == getInteger(R.integer.HISTORY)) {
            return "History";
        }
        if (this.mode == getInteger(R.integer.ANALYTICS)) {
            return "Privacy";
        }
        if (this.mode == getInteger(R.integer.SECURITY)) {
            return "Security";
        }
        if (this.mode == getInteger(R.integer.WEB_PREFERENCES)) {
            return "Reddit web preferences";
        }
        if (this.mode == getInteger(R.integer.DEVELOPER_OPTIONS)) {
            return "Developer options";
        }
        if (this.mode == getInteger(R.integer.DARK_MODE)) {
            return "Dark mode";
        }
        if (this.mode == getInteger(R.integer.POST_VIEW_OPTIONS)) {
            return "View type";
        }
        if (this.mode == getInteger(R.integer.COMMENT_VIEW_OPTIONS)) {
            return "Comments";
        }
        if (this.mode == getInteger(R.integer.AWARDS)) {
            return "Awards";
        }
        if (this.mode == getInteger(R.integer.ULTRA)) {
            return "Sync Ultra";
        }
        if (this.mode == getInteger(R.integer.TABLET)) {
            return "Tablet";
        }
        if (this.mode == getInteger(R.integer.EXPERIMENTS)) {
            return "Experimental";
        }
        if (this.mode == getInteger(R.integer.MONET_ADVANCED)) {
            return "Theme management";
        }
        if (this.mode == getInteger(R.integer.LEGACY)) {
            return "Legacy settings";
        }
        throw new RuntimeException("Unsupported preference fragment.");
    }

    public int getMode() {
        return this.mode;
    }

    public void onDarkModeChanged() {
        if (getFragmentManager().findFragmentById(R.id.content) instanceof BasePreferencesFragment) {
            ((BasePreferencesFragment) getFragmentManager().findFragmentById(R.id.content)).onDarkModeChanged();
        }
    }

    public void scrollToTop() {
        if (getFragmentManager().findFragmentById(R.id.content) instanceof BasePreferencesFragment) {
            ((BasePreferencesFragment) getFragmentManager().findFragmentById(R.id.content)).scrollToTopIfEnabled();
        }
    }

    public void setHighlight(String str) {
        this.highlightKey = str;
        if (getFragmentManager().findFragmentById(R.id.content) instanceof BasePreferencesFragment) {
            ((BasePreferencesFragment) getFragmentManager().findFragmentById(R.id.content)).setHighlightKey(str);
        }
    }

    public void setToolbarAlpha(float alpha) {
        PreferencesSmallSubredditToolbarView preferencesSmallSubredditToolbarView = this.mPreferencesSmallSubredditToolbarView;
        if (preferencesSmallSubredditToolbarView != null) {
            preferencesSmallSubredditToolbarView.setAlpha(alpha);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(getLayoutResId(), viewGroup, false);
        ButterKnife.bind(this, viewInflate);
        return viewInflate;
    }

    @Override // com.resync.app.ui.interfaces.RedditFragmentInterface
    public int getLayoutResId() {
        return R.layout.fragment_preference_wrapper;
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: m2 */
    public void onStart() {
        super.onStart();
        this.ultraReceiver = new UltraStatusReceiver();
        ContextCompat.registerReceiver(getActivity(), this.ultraReceiver, new IntentFilter(UltraReceiver.getAction()), ContextCompat.RECEIVER_NOT_EXPORTED);
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: n2 */
    public void onStop() {
        super.onStop();
        UltraReceiver.unregister(getActivity(), this.ultraReceiver);
        this.ultraReceiver = null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (getArguments() == null || !getArguments().containsKey("mode")) {
            throw new RuntimeException("You forgot the mode");
        }
        this.mode = getArguments().getInt("mode", getResources().getInteger(R.integer.ROOT));
        this.highlightKey = getArguments().getString("highlight");
        this.mPreferencesSmallSubredditToolbarView.setTitle(getScreenTitle());
        if (this.mode != getInteger(R.integer.ROOT) || !TabletHelper.isTabletMode()) {
            this.mCoordinatorLayout.markWindowBackground();
            this.mToolbar.useWindowBackground();
        }
        if (this.mode != getInteger(R.integer.ROOT) && TabletHelper.isTabletMode()) {
            this.mToolbar.setNavigationIcon((android.graphics.drawable.Drawable) null);
        }
        if (this.mode == getInteger(R.integer.ROOT) && DataSavingHelper.isFullDataSaving()) {
            int backgroundColor = MonetUtil.getAccent2BackgroundColorAuto();
            int textColor = MonetUtil.getAccent2TextColorAuto();
            ViewGroup infoRow = (ViewGroup) View.inflate(getActivity(), R.layout.view_info_row, null);
            infoRow.setBackgroundColor(backgroundColor);
            infoRow.setOnClickListener(new View.OnClickListener() { // from class: pa.f
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    PreferenceWrapperFragment.this.lambdaShowDataSaver(view2);
                }
            });
            ((SettingsButton) infoRow.findViewById(R.id.info_row_icon)).setImageResource(R.drawable.ic_help_outline_black_24dp);
            ((SettingsButton) infoRow.findViewById(R.id.info_row_icon)).setFilterColor(textColor);
            ((TextView) infoRow.findViewById(R.id.info_row_text)).setText("Data savings mode is enabled");
            ((TextView) infoRow.findViewById(R.id.info_row_text)).setTextColor(textColor);
            ((TextView) infoRow.findViewById(R.id.info_row_text)).setTextSize(1, 14.0f);
            ((TextView) infoRow.findViewById(R.id.info_row_text)).setTypeface(TypefaceManager.get(11));
            this.mAppBarLayout.addView(infoRow);
        }
        this.mToolbar.setOnMenuItemClickListener(this);
        this.mToolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: pa.g
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                PreferenceWrapperFragment.this.lambdaFinish(view2);
            }
        });
        if (this.mode != getInteger(R.integer.ROOT) && this.mode != getInteger(R.integer.LEGACY)) {
            this.mToolbar.inflateMenu(R.menu.settings);
        }
        if (bundle == null) {
            showFragment();
        }
        this.mToolbar.setOnClickListener(new View.OnClickListener() { // from class: pa.e
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                PreferenceWrapperFragment.this.lambdaScrollToTop(view2);
            }
        });
    }

    @Override // androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menu_reset) {
            BusSingleton.getBus().post(new ResetSettingsEvent());
            return true;
        }
        if (menuItem.getItemId() == R.id.menu_monet) {
            FragmentUtils.show(MonetBottomSheetFragment.class, getFragmentManager());
            return true;
        }
        if (menuItem.getItemId() != R.id.menu_monet_share) {
            return true;
        }
        if (SettingsSingleton.getSettings().monet_system) {
            StaticToast.show("You cannot share an automatic theme");
            return true;
        }
        RedditHelper.shareText(getActivity(), ColorUtils.toHexColor(SettingsSingleton.getSettings().monet_manual_theme_color));
        return true;
    }

    @Subscribe
    public void onResetSettings(ResetSettingsEvent resetSettingsEvent) {
        if (!isRoot() && (getFragmentManager().findFragmentById(R.id.content) instanceof BasePreferencesFragment)) {
            ((BasePreferencesFragment) getFragmentManager().findFragmentById(R.id.content)).onResetSettings();
        }
    }

    @Subscribe
    public void onSettingsChanged(SettingsChangedEvent settingsChangedEvent) {
        if (isRoot()) {
            return;
        }
        showFragment();
    }

    @Subscribe
    public void onUiModeChanged(UiModeChangedEvent uiModeChangedEvent) {
        if (isRoot()) {
            return;
        }
        showFragment();
    }

    public void clearHighlight() {
        this.highlightKey = null;
    }
}
