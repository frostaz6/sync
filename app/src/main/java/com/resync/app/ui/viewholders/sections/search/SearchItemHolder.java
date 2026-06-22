package com.resync.app.ui.viewholders.sections.search;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.PopupMenu;
import butterknife.ButterKnife;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.R;
import com.resync.app.ui.activities.SwipeActivity;
import com.resync.app.ui.views.core.SubView;
import com.resync.app.helpers.ui.PopupMenuHelper;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.FavouriteSubredditManager;
import com.resync.app.events.SubredditSelectedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.holders.BaseViewHolder;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.SubredditAboutBottomSheetFragment;
import com.resync.app.helpers.SubredditHelper;
import com.resync.app.helpers.VisitedManager;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;

/* JADX INFO: loaded from: classes3.dex */
public class SearchItemHolder extends BaseViewHolder {
    int mode;
    String subreddit;

    SubView mSubIconView;

    TextView mSubTextView;

    TextView mSubscriberTextView;

    class MoreMenuClickListener implements PopupMenu.OnMenuItemClickListener {

        final /* synthetic */ MenuItem removeItem;
        final /* synthetic */ MenuItem openNewWindowItem;
        final /* synthetic */ MenuItem openSwipeItem;
        final /* synthetic */ MenuItem aboutItem;

        final /* synthetic */ MenuItem favoriteItem;

        final /* synthetic */ String favoriteSubreddit;

        MoreMenuClickListener(MenuItem menuItem, MenuItem menuItem2, MenuItem menuItem3, MenuItem menuItem4, MenuItem menuItem5, String favoriteSubreddit) {
            this.removeItem = menuItem;
            this.openNewWindowItem = menuItem2;
            this.openSwipeItem = menuItem3;
            this.aboutItem = menuItem4;
            this.favoriteItem = menuItem5;
            this.favoriteSubreddit = favoriteSubreddit;
        }

        @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.equals(this.removeItem)) {
                VisitedManager.removeVisited(SearchItemHolder.this.subreddit);
            } else if (menuItem.equals(this.openNewWindowItem)) {
                BusSingleton.getBus().post(new SubredditSelectedEvent(SearchItemHolder.this.subreddit, true));
            } else if (menuItem.equals(this.openSwipeItem)) {
                SwipeActivity.start(SearchItemHolder.this.getContext(), SearchItemHolder.this.subreddit);
            } else if (menuItem.equals(this.aboutItem)) {
                FragmentUtils.showWithSubreddit(SubredditAboutBottomSheetFragment.class, SearchItemHolder.this.getFragmentManager(), SearchItemHolder.this.subreddit);
            } else if (menuItem.equals(this.favoriteItem)) {
                if (FavouriteSubredditManager.getInstance().isFavourite(this.favoriteSubreddit)) {
                    FavouriteSubredditManager.getInstance().removeFavourite(this.favoriteSubreddit);
                    StaticToast.show("Removed from favorites");
                } else {
                    FavouriteSubredditManager.getInstance().addFavourite(this.favoriteSubreddit);
                    StaticToast.show("Added to favorites");
                }
            }
            return true;
        }
    }

    public SearchItemHolder(Context context, View view) {
        super(context, view);
        ButterKnife.bind(this, view);
        view.setOnClickListener(new View.OnClickListener() { // from class: vb.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                SearchItemHolder.this.onItemClicked(view2);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() { // from class: vb.b
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view2) {
                return SearchItemHolder.this.onItemLongClicked(view2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onItemClicked(View view) {
        BusSingleton.getBus().post(new SubredditSelectedEvent(this.subreddit, false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onItemLongClicked(View view) {
        BusSingleton.getBus().post(new SubredditSelectedEvent(this.subreddit, true));
        return true;
    }

    public static SearchItemHolder create(Context context, ViewGroup viewGroup) {
        return new SearchItemHolder(context, LayoutInflater.from(context).inflate(R.layout.holder_search_item, viewGroup, false));
    }

    public void bind(int mode, String subreddit, String subscriberText, String query) {
        this.mode = mode;
        this.subreddit = subreddit;
        this.mSubIconView.setVisibility(0);
        if (MaterialPostsHelper.isUserProfile(subreddit)) {
            this.mSubIconView.setUsername(MaterialPostsHelper.extractProfileUsername(subreddit));
        } else {
            this.mSubIconView.setSubreddit(this.subreddit);
        }
        if (TextUtils.isEmpty(subscriberText)) {
            this.mSubscriberTextView.setText("");
            this.mSubscriberTextView.setVisibility(8);
        } else {
            this.mSubscriberTextView.setText(subscriberText);
            this.mSubscriberTextView.setVisibility(0);
        }
        try {
            SpannableString spannableString = new SpannableString(SubredditHelper.getDisplayTitle(subreddit, true));
            if (mode != 11 && StringUtils.length(query) > 0 && StringUtils.containsIgnoreCase(subreddit, query)) {
                spannableString.setSpan(new StyleSpan(1), StringUtils.indexOfIgnoreCase(subreddit, query), Math.min(subreddit.length(), StringUtils.indexOfIgnoreCase(subreddit, query) + query.length()), 18);
            }
            if (this.mode != 11) {
                if (!MaterialPostsHelper.isUserProfile(this.subreddit)) {
                    this.mSubTextView.setText(spannableString);
                    return;
                }
                this.mSubTextView.setText("u_" + ((Object) spannableString));
                return;
            }
            if (MaterialPostsHelper.isUserProfile(subreddit)) {
                this.mSubTextView.setText("Open u/" + ((Object) spannableString));
                return;
            }
            if (MaterialPostsHelper.isOpenUrl(subreddit)) {
                this.mSubTextView.setText("Open link");
                return;
            }
            this.mSubTextView.setText("Open r/" + ((Object) spannableString));
        } catch (Exception e) {
            Logger.logException(e, true);
            this.mSubTextView.setText("Error loading title: " + subreddit);
        }
    }

    public void onMoreClicked(View view) {
        String favoriteKey;
        PopupMenu popupMenuB = PopupMenuHelper.create(view);
        MenuItem menuItemAdd = popupMenuB.getMenu().add("Remove");
        menuItemAdd.setVisible(this.mode == 8);
        MenuItem menuItemAdd2 = popupMenuB.getMenu().add("Open in new window");
        MenuItem menuItemAdd3 = popupMenuB.getMenu().add("Open in swipe mode");
        MenuItem menuItemAdd4 = popupMenuB.getMenu().add("About");
        menuItemAdd4.setVisible(MaterialPostsHelper.isSubscribable(this.subreddit));
        if (this.mode == 11 && MaterialPostsHelper.isUserProfile(this.subreddit)) {
            favoriteKey = "u_" + MaterialPostsHelper.extractProfileUsername(this.subreddit);
        } else {
            favoriteKey = this.subreddit;
        }
        String favoriteSubreddit = favoriteKey;
        popupMenuB.setOnMenuItemClickListener(new MoreMenuClickListener(menuItemAdd, menuItemAdd2, menuItemAdd3, menuItemAdd4, popupMenuB.getMenu().add(FavouriteSubredditManager.getInstance().isFavourite(favoriteSubreddit) ? "Unfavorite" : "Favorite"), favoriteSubreddit));
        popupMenuB.show();
    }
}
