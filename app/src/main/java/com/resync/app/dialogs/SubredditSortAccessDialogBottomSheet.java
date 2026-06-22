package com.resync.app.dialogs;

import android.os.Bundle;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.events.SubredditSortUpdatedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.dialogs.FragmentUtils;

/* JADX INFO: compiled from: SubredditSortAccessDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class SubredditSortAccessDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    String access;
    String sort;

    public static Bundle buildArguments(String str, String str2, String str3) {
        Bundle bundle = new Bundle();
        bundle.putString("subreddit", str);
        bundle.putString("access", str2);
        bundle.putString("sort", str3);
        return bundle;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        if (MaterialPostsHelper.isSearch(getSubreddit())) {
            addItem(new AbstractSelectionDialogBottomSheet.ExpandableSelectionItem(R.drawable.comment_search_outline, "Relevance", StringUtils.equalsIgnoreCase("Relevance", this.access)));
            addItem(new AbstractSelectionDialogBottomSheet.ExpandableSelectionItem(R.drawable.outline_new_24, "New", StringUtils.equalsIgnoreCase("New", this.access)));
            addItem(new AbstractSelectionDialogBottomSheet.ExpandableSelectionItem(R.drawable.outline_whatshot_24, "Hot", StringUtils.equalsIgnoreCase("Hot", this.access)));
            addItem(new AbstractSelectionDialogBottomSheet.ExpandableSelectionItem(R.drawable.outline_bar_chart_24, "Top", StringUtils.equalsIgnoreCase("Top", this.access)));
            addItem(new AbstractSelectionDialogBottomSheet.ExpandableSelectionItem(R.drawable.outline_forum_24, "Comments", StringUtils.equalsIgnoreCase("Comments", this.access)));
            return;
        }
        if (MaterialPostsHelper.isUserProfile(getSubreddit())) {
            addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_new_24, "New", StringUtils.equalsIgnoreCase("New", this.access)));
            addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_whatshot_24, "Hot", StringUtils.equalsIgnoreCase("Hot", this.access)));
            addItem(new AbstractSelectionDialogBottomSheet.ExpandableSelectionItem(R.drawable.outline_bar_chart_24, "Top", StringUtils.equalsIgnoreCase("Top", this.access)));
            addItem(new AbstractSelectionDialogBottomSheet.ExpandableSelectionItem(R.drawable.outline_trending_down_24, "Controversial", StringUtils.equalsIgnoreCase("Controversial", this.access)));
            return;
        }
        if (StringUtils.equalsAnyIgnoreCase("frontpage", getSubreddit())) {
            addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.ic_star_outline_white_24dp, "Best", StringUtils.equalsIgnoreCase("Best", this.access)));
        }
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_whatshot_24, "Hot", StringUtils.equalsIgnoreCase("Hot", this.access)));
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_new_24, "New", StringUtils.equalsIgnoreCase("New", this.access)));
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.ic_trending_up_white_24dp, "Rising", StringUtils.equalsIgnoreCase("Rising", this.access)));
        addItem(new AbstractSelectionDialogBottomSheet.ExpandableSelectionItem(R.drawable.outline_bar_chart_24, "Top", StringUtils.equalsIgnoreCase("Top", this.access)));
        addItem(new AbstractSelectionDialogBottomSheet.ExpandableSelectionItem(R.drawable.outline_trending_down_24, "Controversial", StringUtils.equalsIgnoreCase("Controversial", this.access)));
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_forum_24, "Comments", StringUtils.equalsIgnoreCase("Comments", this.access)));
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isTopSheet() {
        return this.topSheet;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        setSubreddit(getArguments().getString("subreddit"));
        this.access = getArguments().getString("access");
        this.sort = getArguments().getString("sort");
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return MaterialPostsHelper.isSearch(getSubreddit()) ? "Search sort" : MaterialPostsHelper.isMultireddit(getSubreddit()) ? "Multireddit sort" : MaterialPostsHelper.isUserProfile(getSubreddit()) ? "Profile sort" : "Subreddit sort";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        if (item instanceof AbstractSelectionDialogBottomSheet.ExpandableSelectionItem) {
            FragmentUtils.showWithArguments(SubredditSortSortDialogBottomSheet.class, getParentFragmentManager(), SubredditSortSortDialogBottomSheet.buildArguments(getSubreddit(), item.getTitle(), this.access, this.sort));
        } else {
            BusSingleton.getBus().post(new SubredditSortUpdatedEvent(item.title, null));
        }
        dismiss();
    }
}
