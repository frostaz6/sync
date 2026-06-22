package com.resync.app.ui.views.app_bars;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.ImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.MaterialPostsHelper;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.resync.app.R;
import com.resync.app.sections.posts.toolbar.PostsLargeSubredditToolbarView;
import com.resync.app.sections.posts.toolbar.PostsSmallSubredditToolbarView;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.activities.MainActivity;
import com.resync.app.ui.activities.SwipeActivity;
import com.resync.app.ui.fragment_dialogs.AccountPickerFragment;
import com.resync.app.ui.fragment_dialogs.bottom.ActionsBottomSheetFragment;
import com.resync.app.ui.fragment_dialogs.bottom.SubredditsBottomSheetFragment;
import com.resync.app.ui.views.core.SubView;
import com.resync.app.ui.views.transparent_status.CustomAppBarLayout;
import com.resync.app.ui.views.transparent_status.CustomCoordinatorLayout;
import com.resync.app.helpers.ui.TabletHelper;
import java.lang.reflect.Field;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.OpenDrawerEvent;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.MonetBottomSheetFragment;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.dialogs.MarkAllAsReadAlertDialogBottomSheet;
import com.resync.app.dialogs.SubredditSortAccessDialogBottomSheet;
import com.resync.app.model.Subreddit;

/* JADX INFO: loaded from: classes3.dex */
public class PostsAppBarLayout extends CustomAppBarLayout implements AppBarLayout.OnOffsetChangedListener, Toolbar.OnMenuItemClickListener {
    int insetTop;
    private CustomCoordinatorLayout coordinatorLayout;
    private CollapsingToolbarLayout collapsingToolbar;
    private PostsSmallSubredditToolbarView smallToolbar;
    private PostsLargeSubredditToolbarView largeToolbar;
    private int section;
    int offset;

    class NavigationClickListener implements View.OnClickListener {
        NavigationClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if ((PostsAppBarLayout.this.getContext() instanceof MainActivity) && SettingsSingleton.getSettings().useDrawer) {
                BusSingleton.getBus().post(new OpenDrawerEvent());
            } else {
                ((BaseActivity) PostsAppBarLayout.this.getContext()).finish();
            }
        }
    }

    public PostsAppBarLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.insetTop = 0;
        this.offset = 0;
        setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: xb.a
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                return PostsAppBarLayout.this.onApplyInsets(view, windowInsets);
            }
        });
    }

    private FragmentManager getFragmentManager() {
        return ((BaseActivity) getContext()).getSupportFragmentManager();
    }

    private boolean isSwipeActivity() {
        return ContextHelper.getBaseActivity(getContext()) instanceof SwipeActivity;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ WindowInsets onApplyInsets(View view, WindowInsets windowInsets) {
        this.insetTop = windowInsets.getSystemWindowInsetTop();
        return windowInsets;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onAvatarClicked(View view) {
        AccountPickerFragment.show(getFragmentManager());
    }

    private void onCollapseProgress(float progress) {
    }

    @Override // com.google.android.material.appbar.AppBarLayout.BaseOnOffsetChangedListener
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (isSwipeActivity()) {
            return;
        }
        this.offset = verticalOffset;
        if (SettingsSingleton.getSettings().toolbarExpanded && SettingsSingleton.getSettings().autohideToolbar) {
            getToolbar().setTranslationY(Math.min(this.collapsingToolbar.getHeight(), -verticalOffset));
            // restored (float) cast dropped by JADX — see Vineflower com/resync/app/ui/views/app_bars/PostsAppBarLayout.java; int/int div-by-zero otherwise
            float smallAlpha = (float) (Math.abs(verticalOffset) - getToolbar().getHeight()) / (this.largeToolbar.getHeight() - getToolbar().getHeight());
            this.smallToolbar.setAlpha(smallAlpha);
            float smallScale = Math.min(1.0f, smallAlpha);
            this.smallToolbar.subView.setScaleX(smallScale);
            this.smallToolbar.subView.setScaleY(smallScale);
            // restored (float) cast dropped by JADX — see Vineflower com/resync/app/ui/views/app_bars/PostsAppBarLayout.java; int/int div-by-zero otherwise
            float toolbarAlpha = (float) Math.min(Math.abs(verticalOffset), getToolbar().getHeight()) / getToolbar().getHeight();
            getToolbar().setBackgroundPaintAlpha(toolbarAlpha);
            CustomCoordinatorLayout customCoordinatorLayout = this.coordinatorLayout;
            if (customCoordinatorLayout != null) {
                customCoordinatorLayout.setStatusAlpha(toolbarAlpha);
            }
            float largeAlpha = 1.0f - toolbarAlpha;
            this.largeToolbar.setAlpha(largeAlpha);
            float largeScale = Math.min(1.0f, largeAlpha);
            this.largeToolbar.subView.setScaleX(largeScale);
            this.largeToolbar.subView.setScaleY(largeScale);
            onCollapseProgress(toolbarAlpha);
        }
    }

    /**
     * The AppBarLayout's CoordinatorLayout-managed Behavior, or null if it isn't attached to a
     * CoordinatorLayout yet. We read the ATTACHED behavior off the LayoutParams rather than calling
     * AppBarLayout#getBehavior(), because the latter constructs a brand-new Behavior on every call
     * (material 1.9.0) instead of returning the live one driving the scroll state.
     */
    private AppBarLayout.Behavior getAttachedBehavior() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.Behavior<?> behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
            if (behavior instanceof AppBarLayout.Behavior) {
                return (AppBarLayout.Behavior) behavior;
            }
        }
        return null;
    }

    public void restoreBehaviorState(Parcelable parcelable) {
        if (parcelable == null) {
            return;
        }
        AppBarLayout.Behavior behavior = getAttachedBehavior();
        if (behavior != null) {
            behavior.onRestoreInstanceState(this.coordinatorLayout, this, parcelable);
        }
    }

    public Parcelable saveBehaviorState() {
        AppBarLayout.Behavior behavior = getAttachedBehavior();
        if (behavior != null) {
            return behavior.onSaveInstanceState(this.coordinatorLayout, this);
        }
        return null;
    }

    public void setSection(int section, String subreddit, String sort, String period) {
        this.section = section;
        PostsLargeSubredditToolbarView postsLargeSubredditToolbarView = this.largeToolbar;
        if (postsLargeSubredditToolbarView != null) {
            postsLargeSubredditToolbarView.bind(section, subreddit, sort, period);
        }
        this.smallToolbar.setSection(section, subreddit, sort, period);
        if (section == 2) {
            getToolbar().getMenu().findItem(com.resync.app.R.id.menu_search).setVisible(false);
            getToolbar().getMenu().findItem(com.resync.app.R.id.menu_sort).setVisible(false);
            getToolbar().getMenu().findItem(com.resync.app.R.id.menu_mark_as_read).setVisible(true);
        } else if (section != 0) {
            if (section == 0) {
                getToolbar().getMenu().findItem(com.resync.app.R.id.menu_search).setVisible(true);
            }
        } else {
            getToolbar().getMenu().findItem(com.resync.app.R.id.menu_sort).setVisible(true);
            if (MaterialPostsHelper.isSubSearch(subreddit)) {
                getToolbar().getMenu().findItem(com.resync.app.R.id.menu_sort).setVisible(false);
            }
            getToolbar().getMenu().findItem(com.resync.app.R.id.menu_mark_as_read).setVisible(false);
            getToolbar().getMenu().findItem(com.resync.app.R.id.menu_search).setVisible(true);
        }
    }

    public void setSubreddit(Subreddit subreddit) {
        if (SettingsSingleton.getSettings().toolbarExpanded) {
            this.smallToolbar.setSubreddit(subreddit);
            PostsLargeSubredditToolbarView postsLargeSubredditToolbarView = this.largeToolbar;
            if (postsLargeSubredditToolbarView != null) {
                postsLargeSubredditToolbarView.setSubreddit(subreddit);
            }
        }
    }

    @Override // com.google.android.material.appbar.AppBarLayout, android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        CollapsingToolbarLayout collapsingToolbarLayout;
        super.onAttachedToWindow();
        this.coordinatorLayout = (CustomCoordinatorLayout) getParent();
        if (!SettingsSingleton.getSettings().toolbarExpanded && (collapsingToolbarLayout = this.collapsingToolbar) != null) {
            collapsingToolbarLayout.setVisibility(8);
        }
        if (SettingsSingleton.getSettings().autohideToolbar || !SettingsSingleton.getSettings().toolbarExpanded) {
            return;
        }
        this.smallToolbar.setAlpha(0.0f);
        getToolbar().setBackgroundPaintAlpha(0.0f);
        CustomCoordinatorLayout customCoordinatorLayout = this.coordinatorLayout;
        if (customCoordinatorLayout != null) {
            customCoordinatorLayout.setStatusAlpha(0.0f);
        }
    }

    @Override // com.resync.app.ui.views.transparent_status.CustomAppBarLayout, android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.smallToolbar = (PostsSmallSubredditToolbarView) findViewById(com.resync.app.R.id.toolbar_small_header_view);
        this.largeToolbar = (PostsLargeSubredditToolbarView) findViewById(com.resync.app.R.id.toolbar_large_header_view);
        this.collapsingToolbar = (CollapsingToolbarLayout) findViewById(com.resync.app.R.id.collapsing_toolbar);
        addOnOffsetChangedListener(this);
        getToolbar().setOnMenuItemClickListener(this);
        getToolbar().setNavigationOnClickListener(new NavigationClickListener());
        if (TabletHelper.isTabletMode()) {
            getToolbar().setNavigationOnClickListener(null);
            return;
        }
        if (!(getContext() instanceof MainActivity)) {
            getToolbar().setNavigationIcon(com.resync.app.R.drawable.outline_arrow_back_24);
            return;
        }
        if (SettingsSingleton.getSettings().useDrawer && !TabletHelper.isTabletModeRaw()) {
            getToolbar().setNavigationIcon(com.resync.app.R.drawable.outline_menu_24);
            return;
        }
        // Swap the Toolbar's navigation button for the account avatar SubView. The nav button view
        // is private with no public accessor, so reflect into it. (Original APK used the R8-minified
        // name "q"; "mNavButtonView" is the real field name in appcompat 1.6.1.)
        try {
            Field navButtonField = Toolbar.class.getDeclaredField("mNavButtonView");
            navButtonField.setAccessible(true);
            ImageView imageView = (ImageView) navButtonField.get(getToolbar());
            SubView subView = new SubView(getContext());
            subView.applyOvalOutline();
            int size = SizeHelper.dpToPx(30);
            Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(size, size);
            layoutParams.gravity = 8388627;
            ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = SizeHelper.dpToPx(16);
            subView.setLayoutParams(layoutParams);
            int childIndex = 0;
            while (true) {
                if (childIndex >= getToolbar().getChildCount()) {
                    break;
                }
                if (getToolbar().getChildAt(childIndex).equals(imageView)) {
                    getToolbar().removeViewAt(childIndex);
                    getToolbar().addView(subView, childIndex);
                    subView.setUsername(AccountSingleton.getInstance().getUsername());
                    break;
                }
                childIndex++;
            }
            subView.setOnClickListener(new View.OnClickListener() { // from class: xb.b
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PostsAppBarLayout.this.onAvatarClicked(view);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == com.resync.app.R.id.menu_search) {
            FragmentUtils.showWithArguments(SubredditsBottomSheetFragment.class, ContextHelper.getFragmentManager(getContext()), SubredditsBottomSheetFragment.buildArgs());
            return true;
        }
        if (menuItem.getItemId() == com.resync.app.R.id.menu_sort) {
            PostsSmallSubredditToolbarView postsSmallSubredditToolbarView = this.smallToolbar;
            if (postsSmallSubredditToolbarView == null || !StringUtils.isNotEmpty(postsSmallSubredditToolbarView.getSubreddit())) {
                return true;
            }
            FragmentUtils.showWithArguments(SubredditSortAccessDialogBottomSheet.class, getFragmentManager(), SubredditSortAccessDialogBottomSheet.buildArguments(this.smallToolbar.getSubreddit(), this.smallToolbar.getSort(), this.smallToolbar.getPeriod()));
            return true;
        }
        if (menuItem.getItemId() == com.resync.app.R.id.menu_more) {
            ActionsBottomSheetFragment.show(getContext());
            return true;
        }
        if (menuItem.getItemId() == com.resync.app.R.id.menu_monet) {
            FragmentUtils.show(MonetBottomSheetFragment.class, getFragmentManager());
            return true;
        }
        if (menuItem.getItemId() != com.resync.app.R.id.menu_mark_as_read) {
            return true;
        }
        FragmentUtils.show(MarkAllAsReadAlertDialogBottomSheet.class, getFragmentManager());
        return true;
    }
}
