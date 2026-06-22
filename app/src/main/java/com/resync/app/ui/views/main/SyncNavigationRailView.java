package com.resync.app.ui.views.main;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigationrail.NavigationRailMenuView;
import com.google.android.material.navigationrail.NavigationRailView;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.fragment_dialogs.AccountPickerFragment;
import com.resync.app.ui.views.core.SubView;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.events.MessageCountUpdatedEvent;
import com.resync.app.events.SubredditSelectedEvent;
import com.resync.app.helpers.NavigationViewHelper;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.ui.interfaces.DarkModeAware;
import com.resync.app.ui.interfaces.PostHost;
import com.resync.app.helpers.messaging.MessageCountHelper;

/* JADX INFO: loaded from: classes3.dex */
public class SyncNavigationRailView extends NavigationRailView implements DarkModeAware, NavigationBarView.OnItemSelectedListener, View.OnLongClickListener {
    public SyncNavigationRailView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOnItemSelectedListener(this);
        applyColors();
        setLabelVisibilityMode(1);
        setLabelVisibilityMode(2);
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        SubView subView = new SubView(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(SizeHelper.dpToPx(34), SizeHelper.dpToPx(34));
        subView.setLayoutParams(layoutParams);
        layoutParams.bottomMargin = SizeHelper.dpToPx(16);
        subView.applyOvalOutline();
        subView.setUsername(AccountSingleton.getInstance().getUsername());
        subView.setOnClickListener(new View.OnClickListener() { // from class: hc.c
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SyncNavigationRailView.this.onAvatarClicked(view);
            }
        });
        frameLayout.addView(subView);
        addHeaderView(frameLayout);
        updateBadge();
        if (SettingsSingleton.getSettings().navigationRailGravity == 0) {
            setItemMinimumHeight(48);
        } else if (SettingsSingleton.getSettings().navigationRailGravity == 1) {
            setItemMinimumHeight(17);
        } else if (SettingsSingleton.getSettings().navigationRailGravity == 2) {
            setItemMinimumHeight(80);
        }
    }

    private PostHost getPostHost() {
        return (PostHost) getContext();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onAvatarClicked(View view) {
        AccountPickerFragment.show(ContextHelper.getFragmentManager(getContext()));
    }

    private void applyColors() {
        setBackgroundColor(MonetUtil.getToolbarColorAuto());
        int primaryTextColor = MonetUtil.getPrimaryTextColorAuto();
        ColorStateList colorStateList = new ColorStateList(new int[][]{new int[]{-16842912}, new int[]{R.attr.state_checked}}, new int[]{primaryTextColor, primaryTextColor});
        setItemIconTintList(colorStateList);
        setItemTextColor(colorStateList);
        setItemRippleColor(ColorStateList.valueOf(MonetUtil.getRippleColorAuto()));
        setItemActiveIndicatorColor(ColorStateList.valueOf(MonetUtil.getAccent2ActiveColorAuto()));
    }

    public void setSelected(int itemId) {
        MenuItem menuItemFindItem = getMenu().findItem(itemId);
        if (menuItemFindItem != null) {
            menuItemFindItem.setChecked(true);
        }
    }

    public void updateBadge() {
        BadgeDrawable badgeDrawableK = getOrCreateBadge(com.resync.app.R.id.menu_messages);
        if (MessageCountHelper.getCount() <= 0) {
            removeBadge(com.resync.app.R.id.menu_messages);
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
        NavigationRailMenuView navigationRailMenuView = (NavigationRailMenuView) getMenuView();
        for (int childIndex = 0; childIndex < navigationRailMenuView.getChildCount(); childIndex++) {
            navigationRailMenuView.getChildAt(childIndex).setOnLongClickListener(this);
        }
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
        if (getSelectedItemId() != com.resync.app.R.id.menu_posts) {
            setSelectedItemId(com.resync.app.R.id.menu_posts);
        }
    }
}
