package com.resync.app.helpers.ui;

import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import androidx.appcompat.widget.PopupMenu;
import com.resync.app.jobs.CheckSubredditsJob;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.managers.FavouriteSubredditManager;
import com.resync.app.managers.TopSubredditHelper;
import com.resync.app.events.SubredditsChangedEvent;
import com.resync.app.events.VisitedChangedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.helpers.VisitedManager;

/* JADX INFO: renamed from: gb.r */
/* JADX INFO: compiled from: SubredditListMenuHelper.java */
/* JADX INFO: loaded from: classes2.dex */
public final class SubredditListMenuHelper {

    /* JADX INFO: renamed from: gb.r$a */
    /* JADX INFO: compiled from: SubredditListMenuHelper.java */
    static class MenuClickListener implements PopupMenu.OnMenuItemClickListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ MenuItem itemClearHistory;
        final /* synthetic */ MenuItem itemClearTop;
        final /* synthetic */ MenuItem itemReload;
        final /* synthetic */ MenuItem itemResetFav;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ MenuItem itemExpand;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        final /* synthetic */ MenuItem itemFocus;
        final /* synthetic */ MenuItem itemShowFeeds;
        final /* synthetic */ MenuItem itemShowFav;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        final /* synthetic */ MenuItem itemShowCustom;
        final /* synthetic */ MenuItem itemShowSubscriptions;
        final /* synthetic */ MenuItem itemShowTop;

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        final /* synthetic */ MenuItem itemShowModerated;

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        final /* synthetic */ MenuItem itemShowFollows;
        final /* synthetic */ MenuItem itemHighlight;
        final /* synthetic */ MenuItem itemDuplicateFavs;
        final /* synthetic */ MenuItem itemRememberPosition;

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ MenuItem itemFastScroller;

        MenuClickListener(MenuItem menuItem, MenuItem menuItem2, MenuItem menuItem3, MenuItem menuItem4, MenuItem menuItem5, MenuItem menuItem6, MenuItem menuItem7, MenuItem menuItem8, MenuItem menuItem9, MenuItem menuItem10, MenuItem menuItem11, MenuItem menuItem12, MenuItem menuItem13, MenuItem menuItem14, MenuItem menuItem15, MenuItem menuItem16, MenuItem menuItem17) {
            this.itemClearHistory = menuItem;
            this.itemClearTop = menuItem2;
            this.itemReload = menuItem3;
            this.itemResetFav = menuItem4;
            this.itemExpand = menuItem5;
            this.itemFocus = menuItem6;
            this.itemShowFeeds = menuItem7;
            this.itemShowFav = menuItem8;
            this.itemShowCustom = menuItem9;
            this.itemShowSubscriptions = menuItem10;
            this.itemShowTop = menuItem11;
            this.itemShowModerated = menuItem12;
            this.itemShowFollows = menuItem13;
            this.itemHighlight = menuItem14;
            this.itemDuplicateFavs = menuItem15;
            this.itemRememberPosition = menuItem16;
            this.itemFastScroller = menuItem17;
        }

        @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.equals(this.itemClearHistory)) {
                VisitedManager.clearVisited();
                BusSingleton.getBus().post(new VisitedChangedEvent());
            } else if (menuItem.equals(this.itemClearTop)) {
                TopSubredditHelper.clearTopSubreddits();
            } else if (menuItem.equals(this.itemReload)) {
                CheckSubredditsJob.schedule();
                StaticToast.show("Reloading subscriptions");
            } else {
                if (menuItem.equals(this.itemResetFav)) {
                    SubredditArrayList subredditArrayList = new SubredditArrayList(FavouriteSubredditManager.getInstance().getFavourites());
                    subredditArrayList.sortDefault();
                    FavouriteSubredditManager.getInstance().clearFavourites();
                    FavouriteSubredditManager.getInstance().addAllFavourites(subredditArrayList);
                    return true;
                }
                if (menuItem.equals(this.itemExpand)) {
                    boolean fullyExpand = !SettingsSingleton.getSettings().subredditsFullyExpand;
                    SettingsSingleton.getSettings().subredditsFullyExpand = fullyExpand;
                    SettingsSingleton.getInstance().putBoolean("subreddits_fully_expand", fullyExpand);
                    return true;
                }
                if (menuItem.equals(this.itemFocus)) {
                    boolean focusOnOpen = !SettingsSingleton.getSettings().subredditsFocusOnOpen;
                    SettingsSingleton.getSettings().subredditsFocusOnOpen = focusOnOpen;
                    SettingsSingleton.getInstance().putBoolean("subreddits_focus_on_open", focusOnOpen);
                    return true;
                }
                if (menuItem.equals(this.itemShowFeeds)) {
                    boolean showFeeds = !SettingsSingleton.getSettings().subredditsShowFeeds;
                    SettingsSingleton.getSettings().subredditsShowFeeds = showFeeds;
                    SettingsSingleton.getInstance().putBoolean("subreddits_show_feeds", showFeeds);
                    SettingsSingleton.getSettings().collapseFeeds = !showFeeds;
                    SettingsSingleton.getInstance().putBoolean("collapse_feeds", !showFeeds);
                } else if (menuItem.equals(this.itemShowFav)) {
                    boolean showFav = !SettingsSingleton.getSettings().subredditsShowFav;
                    SettingsSingleton.getSettings().subredditsShowFav = showFav;
                    SettingsSingleton.getInstance().putBoolean("subreddits_show_fav", showFav);
                    SettingsSingleton.getSettings().collapseFav = !showFav;
                    SettingsSingleton.getInstance().putBoolean("collapse_fav", !showFav);
                } else if (menuItem.equals(this.itemShowCustom)) {
                    boolean showCustom = !SettingsSingleton.getSettings().subredditsShowCustom;
                    SettingsSingleton.getSettings().subredditsShowCustom = showCustom;
                    SettingsSingleton.getInstance().putBoolean("subreddits_show_custom", showCustom);
                    SettingsSingleton.getSettings().collapseCustomFeed = !showCustom;
                    SettingsSingleton.getInstance().putBoolean("collapse_custom_feed", !showCustom);
                } else if (menuItem.equals(this.itemShowSubscriptions)) {
                    boolean showSubscriptions = !SettingsSingleton.getSettings().subredditsShowSubscriptions;
                    SettingsSingleton.getSettings().subredditsShowSubscriptions = showSubscriptions;
                    SettingsSingleton.getInstance().putBoolean("subreddits_show_subscriptions", showSubscriptions);
                    SettingsSingleton.getSettings().collapseSubscriptions = !showSubscriptions;
                    SettingsSingleton.getInstance().putBoolean("collapse_subscriptions", !showSubscriptions);
                } else if (menuItem.equals(this.itemShowTop)) {
                    boolean showTop = !SettingsSingleton.getSettings().subredditsShowTop;
                    SettingsSingleton.getSettings().subredditsShowTop = showTop;
                    SettingsSingleton.getInstance().putBoolean("subreddits_show_top", showTop);
                    SettingsSingleton.getSettings().collapseTop = !showTop;
                    SettingsSingleton.getInstance().putBoolean("collapse_top", !showTop);
                } else if (menuItem.equals(this.itemShowModerated)) {
                    boolean showModerated = !SettingsSingleton.getSettings().subredditsShowModerator;
                    SettingsSingleton.getSettings().subredditsShowModerator = showModerated;
                    SettingsSingleton.getInstance().putBoolean("subreddits_show_moderator", showModerated);
                    SettingsSingleton.getSettings().collapseModerated = !showModerated;
                    SettingsSingleton.getInstance().putBoolean("collapse_moderated", !showModerated);
                } else if (menuItem.equals(this.itemShowFollows)) {
                    boolean showFollows = !SettingsSingleton.getSettings().subredditsShowFollows;
                    SettingsSingleton.getSettings().subredditsShowFollows = showFollows;
                    SettingsSingleton.getInstance().putBoolean("subreddits_show_follows", showFollows);
                    SettingsSingleton.getSettings().collapseFollows = !showFollows;
                    SettingsSingleton.getInstance().putBoolean("collapse_follows", !showFollows);
                } else if (menuItem.equals(this.itemHighlight)) {
                    boolean highlightDefault = !SettingsSingleton.getSettings().highlightDefault;
                    SettingsSingleton.getSettings().highlightDefault = highlightDefault;
                    SettingsSingleton.getInstance().putBoolean("highlight_default", highlightDefault);
                } else if (menuItem.equals(this.itemDuplicateFavs)) {
                    boolean duplicateFavs = !SettingsSingleton.getSettings().duplicateFavs;
                    SettingsSingleton.getSettings().duplicateFavs = duplicateFavs;
                    SettingsSingleton.getInstance().putBoolean("duplicate_favs", duplicateFavs);
                } else if (menuItem.equals(this.itemRememberPosition)) {
                    boolean rememberPosition = !SettingsSingleton.getSettings().subredditsRememberPosition;
                    SettingsSingleton.getSettings().subredditsRememberPosition = rememberPosition;
                    SettingsSingleton.getInstance().putBoolean("subreddits_remember_position", rememberPosition);
                } else if (menuItem.equals(this.itemFastScroller)) {
                    boolean fastScroller = !SettingsSingleton.getSettings().fastScroller;
                    SettingsSingleton.getSettings().fastScroller = fastScroller;
                    SettingsSingleton.getInstance().putBoolean("subreddits_fast_scroller", fastScroller);
                }
            }
            BusSingleton.getBus().post(new SubredditsChangedEvent());
            return true;
        }
    }

    public static final void showOptionsMenu(View view, boolean showFullOptions) {
        PopupMenu popupMenuC = PopupMenuHelper.create(view, 5);
        SubMenu subMenuAddSubMenu = popupMenuC.getMenu().addSubMenu("Section visibility");
        MenuItem menuItemAdd = subMenuAddSubMenu.add("Favorites");
        menuItemAdd.setCheckable(true);
        menuItemAdd.setChecked(SettingsSingleton.getSettings().subredditsShowFav);
        MenuItem menuItemAdd2 = subMenuAddSubMenu.add("Feeds");
        menuItemAdd2.setCheckable(true);
        menuItemAdd2.setChecked(SettingsSingleton.getSettings().subredditsShowFeeds);
        MenuItem menuItemAdd3 = subMenuAddSubMenu.add("Custom feeds");
        menuItemAdd3.setCheckable(true);
        menuItemAdd3.setChecked(SettingsSingleton.getSettings().subredditsShowCustom);
        MenuItem menuItemAdd4 = subMenuAddSubMenu.add("Subscriptions");
        menuItemAdd4.setCheckable(true);
        menuItemAdd4.setChecked(SettingsSingleton.getSettings().subredditsShowSubscriptions);
        MenuItem menuItemAdd5 = subMenuAddSubMenu.add("Most visited");
        menuItemAdd5.setCheckable(true);
        menuItemAdd5.setChecked(SettingsSingleton.getSettings().subredditsShowTop);
        MenuItem menuItemAdd6 = subMenuAddSubMenu.add("Moderated");
        menuItemAdd6.setCheckable(true);
        menuItemAdd6.setChecked(SettingsSingleton.getSettings().subredditsShowModerator);
        MenuItem menuItemAdd7 = subMenuAddSubMenu.add("Follows");
        menuItemAdd7.setCheckable(true);
        menuItemAdd7.setChecked(SettingsSingleton.getSettings().subredditsShowFollows);
        MenuItem menuItemAdd8 = popupMenuC.getMenu().add("Highlight default");
        menuItemAdd8.setCheckable(true);
        menuItemAdd8.setChecked(SettingsSingleton.getSettings().highlightDefault);
        MenuItem menuItemAdd9 = popupMenuC.getMenu().add("Duplicate favs");
        menuItemAdd9.setCheckable(true);
        menuItemAdd9.setChecked(SettingsSingleton.getSettings().duplicateFavs);
        MenuItem menuItemAdd10 = popupMenuC.getMenu().add("Expand on open");
        menuItemAdd10.setCheckable(true);
        menuItemAdd10.setChecked(SettingsSingleton.getSettings().subredditsFullyExpand);
        menuItemAdd10.setVisible(showFullOptions);
        MenuItem menuItemAdd11 = popupMenuC.getMenu().add("Focus on open");
        menuItemAdd11.setCheckable(true);
        menuItemAdd11.setChecked(SettingsSingleton.getSettings().subredditsFocusOnOpen);
        menuItemAdd11.setVisible(false);
        MenuItem menuItemAdd12 = popupMenuC.getMenu().add("Remember position");
        menuItemAdd12.setCheckable(true);
        menuItemAdd12.setChecked(SettingsSingleton.getSettings().subredditsRememberPosition);
        menuItemAdd12.setVisible(showFullOptions);
        MenuItem menuItemAdd13 = popupMenuC.getMenu().add("Fast scroller");
        menuItemAdd13.setCheckable(true);
        menuItemAdd13.setChecked(SettingsSingleton.getSettings().fastScroller);
        menuItemAdd13.setVisible(showFullOptions);
        MenuItem menuItemAdd14 = popupMenuC.getMenu().add("Reload subscriptions");
        MenuItem menuItemAdd15 = popupMenuC.getMenu().add("Reset fav order");
        MenuItem menuItemAdd16 = popupMenuC.getMenu().add("Clear search history");
        menuItemAdd16.setVisible(showFullOptions);
        MenuItem menuItemAdd17 = popupMenuC.getMenu().add("Clear top visited");
        menuItemAdd17.setVisible(showFullOptions);
        popupMenuC.setOnMenuItemClickListener(new MenuClickListener(menuItemAdd16, menuItemAdd17, menuItemAdd14, menuItemAdd15, menuItemAdd10, menuItemAdd11, menuItemAdd2, menuItemAdd, menuItemAdd3, menuItemAdd4, menuItemAdd5, menuItemAdd6, menuItemAdd7, menuItemAdd8, menuItemAdd9, menuItemAdd12, menuItemAdd13));
        popupMenuC.show();
    }
}
