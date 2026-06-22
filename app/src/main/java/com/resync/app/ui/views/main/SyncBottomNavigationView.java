package com.resync.app.ui.views.main;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationBarView;
import com.resync.app.R;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.behaviour.BottomNavigationBehavior;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.FoldableHelper;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.TabletHelper;
import com.resync.app.events.MessageCountUpdatedEvent;
import com.resync.app.events.ShowBottomNavigationEvent;
import com.resync.app.events.SubredditSelectedEvent;
import com.resync.app.helpers.NavigationViewHelper;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.ui.interfaces.DarkModeAware;
import com.resync.app.ui.interfaces.PostHost;
import com.resync.app.util.Logger;
import com.resync.app.helpers.messaging.MessageCountHelper;

/* JADX INFO: loaded from: classes3.dex */
public class SyncBottomNavigationView extends BottomNavigationView implements View.OnLongClickListener, DarkModeAware, NavigationBarView.OnItemSelectedListener {
    public SyncBottomNavigationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        inflateMenu(R.menu.main_bottom_actions);
        if (!SettingsSingleton.getSettings().navigationBottomActions) {
            getMenu().findItem(R.id.menu_actions).setVisible(false);
        }
        if (!AccountSingleton.getInstance().isLoggedIn()) {
            getMenu().findItem(R.id.menu_messages).setVisible(false);
        }
        setOnItemSelectedListener(this);
        applyColors();
        if (shouldShow()) {
            setVisibility(0);
        } else {
            setVisibility(8);
        }
    }

    private PostHost getPostHost() {
        return (PostHost) getContext();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void attachLongClickListeners() {
        try {
            BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) getMenuView();
            for (int childIndex = 0; childIndex < bottomNavigationMenuView.getChildCount(); childIndex++) {
                bottomNavigationMenuView.getChildAt(childIndex).setOnLongClickListener(this);
            }
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    public static boolean shouldShow() {
        if (TabletHelper.isTabletModeRaw() && FoldableHelper.isFoldableExpanded()) {
            return true;
        }
        return SettingsSingleton.getSettings().navigationBottom && !TabletHelper.isTabletMode();
    }

    @Override // com.google.android.material.navigation.NavigationBarView
    public void setSelectedItemId(int itemId) {
        showBottomNavigation(null);
        super.setSelectedItemId(itemId);
    }

    public void setSelected(int itemId) {
        MenuItem menuItemFindItem = getMenu().findItem(itemId);
        if (menuItemFindItem != null) {
            menuItemFindItem.setChecked(true);
        }
    }

    public void updateBadge() {
        BadgeDrawable badgeDrawableK = getOrCreateBadge(R.id.menu_messages);
        if (MessageCountHelper.getCount() <= 0) {
            removeBadge(R.id.menu_messages);
            return;
        }
        badgeDrawableK.setVisible(true);
        badgeDrawableK.setMaxCharacterCount(3);
        badgeDrawableK.setHorizontalOffset(SizeHelper.dpToPx(4));
        badgeDrawableK.setVerticalOffset(SizeHelper.dpToPx(3));
        badgeDrawableK.setNumber(MessageCountHelper.getCount());
        badgeDrawableK.setBackgroundColor(-42495);
        badgeDrawableK.setBadgeTextColor(-1);
    }

    public void applyColors() {
        setClickable(true);
        setBackgroundColor(MonetUtil.getToolbarColorAuto());
        int primaryTextColor = MonetUtil.getPrimaryTextColorAuto();
        ColorStateList colorStateList = new ColorStateList(new int[][]{new int[]{-16842912}, new int[]{android.R.attr.state_checked}}, new int[]{primaryTextColor, primaryTextColor});
        setItemIconTintList(colorStateList);
        setItemTextColor(colorStateList);
        updateBadge();
        setItemRippleColor(ColorStateList.valueOf(MonetUtil.getRippleColorAuto()));
        if (SettingsSingleton.getSettings().navigationBottomLabels) {
            setLabelVisibilityMode(1);
        } else {
            setLabelVisibilityMode(2);
        }
        setItemActiveIndicatorColor(ColorStateList.valueOf(MonetUtil.getAccent2ActiveColorAuto()));
    }

    @Override // com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        return NavigationViewHelper.onBottomNavItemSelected(getContext(), getPostHost(), menuItem);
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }

    @Override // com.google.android.material.navigation.NavigationBarView
    public void inflateMenu(int resId) {
        super.inflateMenu(resId);
        post(new Runnable() { // from class: hc.b
            @Override // java.lang.Runnable
            public final void run() {
                SyncBottomNavigationView.this.attachLongClickListeners();
            }
        });
    }

    @Override // com.google.android.material.navigation.NavigationBarView, android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BusSingleton.getBus().register(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        BusSingleton.getBus().unregister(this);
        super.onDetachedFromWindow();
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        return NavigationViewHelper.onDrawerItemSelected(getContext(), ((NavigationBarItemView) view).getItemData().getItemId());
    }

    @Subscribe
    public void onMessageCountChanged(MessageCountUpdatedEvent messageCountUpdatedEvent) {
        updateBadge();
    }

    @Subscribe
    public void onSubredditSelected(SubredditSelectedEvent subredditSelectedEvent) {
        if (getSelectedItemId() != R.id.menu_posts) {
            setSelectedItemId(R.id.menu_posts);
        }
    }

    @Override // android.view.View
    public void postInvalidate() {
        super.postInvalidate();
    }

    @Subscribe
    public void showBottomNavigation(ShowBottomNavigationEvent showBottomNavigationEvent) {
        BottomNavigationBehavior.from(this).show(this);
    }
}
