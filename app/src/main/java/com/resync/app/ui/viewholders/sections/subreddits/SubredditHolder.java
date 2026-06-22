package com.resync.app.ui.viewholders.sections.subreddits;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.graphics.ColorUtils;
import butterknife.ButterKnife;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.SwipeActivity;
import com.resync.app.ui.views.buttons.MoreButton;
import com.resync.app.ui.views.buttons.SaveButton;
import com.resync.app.ui.views.buttons.SortButton;
import com.resync.app.ui.views.buttons.ViewModeButton;
import com.resync.app.ui.views.core.SubView;
import com.resync.app.helpers.ui.AlertDialogHelper;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.PopupMenuHelper;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.PerSubSortHelper;
import com.resync.app.helpers.PerSubViewModeHelper;
import com.resync.app.managers.FavouriteSubredditManager;
import com.resync.app.managers.SubredditsManager;
import com.resync.app.managers.TopSubredditHelper;
import com.resync.app.events.SubredditSelectedEvent;
import com.resync.app.events.SubredditsChangedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.holders.BaseViewHolder;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.SubredditAboutBottomSheetFragment;
import com.resync.app.helpers.SubredditHelper;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.dialogs.DeleteCustomFeedAlertDialogBottomSheet;
import com.resync.app.dialogs.ToggleJoinAlertDialogBottomSheet;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes3.dex */
public class SubredditHolder extends BaseViewHolder {
    int mode;
    String subreddit;
    GradientDrawable backgroundDrawable;

    LinearLayout mActionsWrapper;

    TextView mCustomTextview;

    View mDefaultTextview;

    MoreButton mMoreButton;

    SaveButton mSaveButton;

    SortButton mSortButton;

    SubView mSubView;

    ViewModeButton mViewModeButton;

    class MoreMenuClickListener implements PopupMenu.OnMenuItemClickListener {

        final /* synthetic */ MenuItem favoriteItem;
        final /* synthetic */ MenuItem setDefaultItem;
        final /* synthetic */ MenuItem aboutItem;
        final /* synthetic */ MenuItem openNewWindowItem;

        final /* synthetic */ MenuItem openSwipeItem;

        final /* synthetic */ MenuItem deleteCustomFeedItem;
        final /* synthetic */ MenuItem updateSortItem;
        final /* synthetic */ MenuItem updateViewModeItem;

        final /* synthetic */ MenuItem subscribeItem;
        final /* synthetic */ MenuItem unsubscribeItem;
        final /* synthetic */ MenuItem resetViewedItem;

        MoreMenuClickListener(MenuItem menuItem, MenuItem menuItem2, MenuItem menuItem3, MenuItem menuItem4, MenuItem menuItem5, MenuItem menuItem6, MenuItem menuItem7, MenuItem menuItem8, MenuItem menuItem9, MenuItem menuItem10, MenuItem menuItem11) {
            this.favoriteItem = menuItem;
            this.setDefaultItem = menuItem2;
            this.aboutItem = menuItem3;
            this.openNewWindowItem = menuItem4;
            this.openSwipeItem = menuItem5;
            this.deleteCustomFeedItem = menuItem6;
            this.updateSortItem = menuItem7;
            this.updateViewModeItem = menuItem8;
            this.subscribeItem = menuItem9;
            this.unsubscribeItem = menuItem10;
            this.resetViewedItem = menuItem11;
        }

        @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.equals(this.favoriteItem)) {
                if (FavouriteSubredditManager.getInstance().isFavourite(SubredditHolder.this.subreddit)) {
                    FavouriteSubredditManager.getInstance().removeFavourite(SubredditHolder.this.subreddit);
                } else {
                    FavouriteSubredditManager.getInstance().addFavourite(SubredditHolder.this.subreddit);
                }
            } else if (menuItem.equals(this.setDefaultItem)) {
                SettingsSingleton.getSettings().defaultSub = SubredditHolder.this.subreddit;
                SettingsSingleton.getInstance().putString("default_sub", SubredditHolder.this.subreddit);
                BusSingleton.getBus().post(new SubredditsChangedEvent());
                StaticToast.show("Default updated");
            } else if (menuItem.equals(this.aboutItem)) {
                FragmentUtils.showWithSubreddit(SubredditAboutBottomSheetFragment.class, SubredditHolder.this.getFragmentManager(), SubredditHolder.this.subreddit);
            } else if (menuItem.equals(this.openNewWindowItem)) {
                BusSingleton.getBus().post(new SubredditSelectedEvent(SubredditHolder.this.subreddit, true));
            } else if (menuItem.equals(this.openSwipeItem)) {
                SwipeActivity.start(SubredditHolder.this.getContext(), SubredditHolder.this.subreddit);
            } else if (menuItem.equals(this.deleteCustomFeedItem)) {
                SubredditHolder subredditHolder = SubredditHolder.this;
                subredditHolder.showDeleteCustomFeed(subredditHolder.subreddit);
            } else if (menuItem.equals(this.updateSortItem)) {
                SubredditHolder subredditHolder2 = SubredditHolder.this;
                subredditHolder2.showUpdateSort(subredditHolder2.subreddit);
            } else if (menuItem.equals(this.updateViewModeItem)) {
                SubredditHolder subredditHolder3 = SubredditHolder.this;
                subredditHolder3.showUpdateViewMode(subredditHolder3.subreddit);
            } else if (menuItem.equals(this.subscribeItem)) {
                SubredditHolder subredditHolder4 = SubredditHolder.this;
                subredditHolder4.showSubscribe(subredditHolder4.subreddit);
            } else if (menuItem.equals(this.unsubscribeItem)) {
                SubredditHolder subredditHolder5 = SubredditHolder.this;
                subredditHolder5.showUnsubscribe(subredditHolder5.subreddit);
            } else if (menuItem.equals(this.resetViewedItem)) {
                TopSubredditHelper.removeSubVisit(SubredditHolder.this.context, SubredditHolder.this.subreddit);
                BusSingleton.getBus().post(new SubredditsChangedEvent());
            }
            return true;
        }
    }

    class SortResetListener implements DialogInterface.OnClickListener {

        final /* synthetic */ String subredditName;

        SortResetListener(String subreddit) {
            this.subredditName = subreddit;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            PerSubSortHelper.removeSort(this.subredditName);
        }
    }

    class SortOverrideListener implements DialogInterface.OnClickListener {

        final /* synthetic */ String subredditName;

        class SortSelectedListener implements DialogInterface.OnClickListener {

            final /* synthetic */ String[] sortValues;

            SortSelectedListener(String[] sortValues) {
                this.sortValues = sortValues;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int which) {
                PerSubSortHelper.setSort(SortOverrideListener.this.subredditName, this.sortValues[which]);
            }
        }

        SortOverrideListener(String subreddit) {
            this.subredditName = subreddit;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            AlertDialogHelper.newBuilder(SubredditHolder.this.context).setTitle("Update sort").setItems(SubredditHolder.this.context.getResources().getStringArray(this.subredditName.equalsIgnoreCase("frontpage") ? R.array.post_sort_frontpage : R.array.post_sort), new SortSelectedListener(SubredditHolder.this.context.getResources().getStringArray(this.subredditName.equalsIgnoreCase("frontpage") ? R.array.post_sort_frontpage_values : R.array.post_sort_values))).show();
        }
    }

    class ViewModeResetListener implements DialogInterface.OnClickListener {

        final /* synthetic */ String subredditName;

        ViewModeResetListener(String subreddit) {
            this.subredditName = subreddit;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            PerSubViewModeHelper.removeViewMode(this.subredditName);
        }
    }

    class ViewModeOverrideListener implements DialogInterface.OnClickListener {

        final /* synthetic */ String subredditName;

        class ViewModeSelectedListener implements DialogInterface.OnClickListener {

            final /* synthetic */ int[] viewModeValues;

            ViewModeSelectedListener(int[] viewModeValues) {
                this.viewModeValues = viewModeValues;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int which) {
                PerSubViewModeHelper.setViewMode(ViewModeOverrideListener.this.subredditName, this.viewModeValues[which]);
            }
        }

        ViewModeOverrideListener(String subreddit) {
            this.subredditName = subreddit;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            AlertDialogHelper.newBuilder(SubredditHolder.this.context).setTitle("Update view mode").setItems(new String[]{"List", "Compact", "Smaller cards", "Small cards", "Cards", "Slides"}, new ViewModeSelectedListener(new int[]{0, 1, 2, 3, 4, 5})).show();
        }
    }

    class RowClickListener implements View.OnClickListener {
        RowClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            BusSingleton.getBus().post(new SubredditSelectedEvent(SubredditHolder.this.subreddit, false));
        }
    }

    class RowLongClickListener implements View.OnLongClickListener {
        RowLongClickListener() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            BusSingleton.getBus().post(new SubredditSelectedEvent(SubredditHolder.this.subreddit, true));
            return true;
        }
    }

    public SubredditHolder(Context context, View view) {
        super(context, view);
        ButterKnife.bind(this, view);
        GradientDrawable gradientDrawable = new GradientDrawable();
        this.backgroundDrawable = gradientDrawable;
        view.setBackground(gradientDrawable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showUpdateViewMode(String subreddit) {
        AlertDialogHelper.newBuilder(this.context).setTitle("Update subreddit view mode").setPositiveButton("Override view", new ViewModeOverrideListener(subreddit)).setNegativeButton("Cancel", null).setNeutralButton("Reset", new ViewModeResetListener(subreddit)).create().show();
    }

    private boolean isMultiFeed() {
        return this.subreddit.startsWith("multi_");
    }

    public static SubredditHolder create(ViewGroup viewGroup) {
        return new SubredditHolder(viewGroup.getContext(), LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.holder_subreddit, viewGroup, false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSubscribe(String subreddit) {
        FragmentUtils.showWithSubreddit(ToggleJoinAlertDialogBottomSheet.class, getFragmentManager(), getSubreddit());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDeleteCustomFeed(String subreddit) {
        FragmentUtils.showWithSubreddit(DeleteCustomFeedAlertDialogBottomSheet.class, getFragmentManager(), subreddit);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showUnsubscribe(String subreddit) {
        FragmentUtils.showWithSubreddit(ToggleJoinAlertDialogBottomSheet.class, getFragmentManager(), getSubreddit());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showUpdateSort(String subreddit) {
        AlertDialogHelper.newBuilder(this.context).setTitle("Update subreddit sort").setPositiveButton("Override sort", new SortOverrideListener(subreddit)).setNegativeButton("Cancel", null).setNeutralButton("Reset", new SortResetListener(subreddit)).create().show();
    }

    public void bind(int mode, String subreddit, String selectedSubreddit) {
        this.mode = mode;
        this.subreddit = subreddit;
        if (mode == 5) {
            this.mCustomTextview.setText(SubredditHelper.getShortTitle(this.subreddit, true) + " (" + TopSubredditHelper.getVisitCount(this.subreddit) + ")");
        } else {
            this.mCustomTextview.setText(SubredditHelper.getShortTitle(subreddit, true));
        }
        boolean zEqualsAnyIgnoreCase = StringUtils.equalsAnyIgnoreCase(subreddit, selectedSubreddit);
        Logger.verbose("CURRENT: " + subreddit + " SELECTED: " + selectedSubreddit);
        this.mCustomTextview.setTypeface(TypefaceManager.get(zEqualsAnyIgnoreCase ? 3 : 9));
        this.mCustomTextview.setTextColor(zEqualsAnyIgnoreCase ? MonetUtil.getLinkColorAuto() : StyleSingleton.getTextColorAuto(getContext(), false));
        if (zEqualsAnyIgnoreCase) {
            resetBackgroundSelected();
        } else {
            resetBackground();
        }
        this.itemView.setOnClickListener(new RowClickListener());
        if (getMode() != 1) {
            this.itemView.setOnLongClickListener(new RowLongClickListener());
        } else {
            this.itemView.setOnLongClickListener(null);
        }
        if (!this.subreddit.equalsIgnoreCase("random")) {
            this.mMoreButton.setVisibility(0);
        } else if (this.subreddit.equalsIgnoreCase("random")) {
            this.mMoreButton.setVisibility(8);
        }
        this.mSortButton.setVisibility(PerSubSortHelper.hasSort(this.subreddit) ? 0 : 8);
        this.mViewModeButton.setVisibility(PerSubViewModeHelper.hasViewMode(this.subreddit) ? 0 : 8);
        this.mDefaultTextview.setVisibility((SettingsSingleton.getSettings().defaultSub.equals(this.subreddit) && SettingsSingleton.getSettings().highlightDefault) ? 0 : 8);
        if (StringUtils.startsWithIgnoreCase(this.subreddit, "u_")) {
            this.mSubView.setUsername(this.subreddit.replace("u_", ""));
        } else {
            this.mSubView.setSubreddit(this.subreddit);
        }
    }

    public void onMoreClicked(View view) {
        PopupMenu popupMenuB = PopupMenuHelper.create(view);
        MenuItem menuItemAdd = popupMenuB.getMenu().add("Open in new window");
        MenuItem menuItemAdd2 = popupMenuB.getMenu().add("Open in swipe mode");
        MenuItem menuItemAdd3 = popupMenuB.getMenu().add(FavouriteSubredditManager.getInstance().isFavourite(this.subreddit) ? "Unfavorite" : "Favorite");
        MenuItem menuItemAdd4 = popupMenuB.getMenu().add("About");
        menuItemAdd4.setVisible(MaterialPostsHelper.isSubscribable(this.subreddit));
        MenuItem menuItemAdd5 = popupMenuB.getMenu().add("Set as default");
        MenuItem menuItemAdd6 = popupMenuB.getMenu().add("Delete custom feed");
        menuItemAdd6.setVisible(isMultiFeed());
        MenuItem menuItemAdd7 = popupMenuB.getMenu().add("Subscribe");
        menuItemAdd7.setVisible((SubredditsManager.getInstance().isSubscribed(this.subreddit) || MaterialPostsHelper.isAggregateFeed(this.subreddit) || MaterialPostsHelper.isMultireddit(this.subreddit)) ? false : true);
        MenuItem menuItemAdd8 = popupMenuB.getMenu().add("Unsubscribe");
        menuItemAdd8.setVisible((!SubredditsManager.getInstance().isSubscribed(this.subreddit) || MaterialPostsHelper.isAggregateFeed(this.subreddit) || MaterialPostsHelper.isMultireddit(this.subreddit)) ? false : true);
        MenuItem menuItemAdd9 = popupMenuB.getMenu().add("Update sort");
        MenuItem menuItemAdd10 = popupMenuB.getMenu().add("Update view mode");
        MenuItem menuItemAdd11 = popupMenuB.getMenu().add("Reset viewed");
        menuItemAdd11.setVisible(this.mode == 5);
        popupMenuB.setOnMenuItemClickListener(new MoreMenuClickListener(menuItemAdd3, menuItemAdd5, menuItemAdd4, menuItemAdd, menuItemAdd2, menuItemAdd6, menuItemAdd9, menuItemAdd10, menuItemAdd7, menuItemAdd8, menuItemAdd11));
        popupMenuB.show();
    }

    public void onSaveClicked() {
        if (FavouriteSubredditManager.getInstance().isFavourite(this.subreddit)) {
            FavouriteSubredditManager.getInstance().removeFavourite(this.subreddit);
        } else {
            FavouriteSubredditManager.getInstance().addFavourite(this.subreddit);
        }
    }

    public int getMode() {
        return this.mode;
    }

    public String getSubreddit() {
        return this.subreddit;
    }

    public void highlight() {
        this.backgroundDrawable.setColor(ColorUtils.setAlphaComponent(MonetUtil.getLinkColorAuto(), 42));
    }

    public void resetBackground() {
        this.backgroundDrawable.setColor(0);
    }

    public void resetBackgroundSelected() {
        this.backgroundDrawable.setColor(0);
    }
}
