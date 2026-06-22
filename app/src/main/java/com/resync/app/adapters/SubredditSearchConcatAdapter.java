package com.resync.app.adapters;

import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Response;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.RedditApplication;
import com.resync.app.ui.sections.concat.FooterHelper;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.sections.concat.SearchOptionsSection;
import com.resync.app.helpers.ui.SubredditArrayList;
import com.resync.app.widgets.recycler.ItemMoveCallback;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.FavouriteSubredditManager;
import com.resync.app.managers.ModeratedSubredditManager;
import com.resync.app.managers.MultiredditManager;
import com.resync.app.managers.SubredditsManager;
import com.resync.app.managers.TopSubredditHelper;
import com.resync.app.events.SearchSubredditsEvent;
import com.resync.app.events.SubredditsChangedEvent;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.helpers.VisitedManager;
import com.resync.app.util.Logger;
import com.resync.app.net.oauth.OAuthSearchSubredditNames;

/* JADX INFO: renamed from: eb.g */
/* JADX INFO: compiled from: SubredditSearchConcatAdapter.java */
/* JADX INFO: loaded from: classes.dex */
public class SubredditSearchConcatAdapter {
    private String query;

    private ConcatAdapter concatAdapter;
    private SearchOptionsSection searchOptionsSection = new SearchOptionsSection();
    private SubredditHeaderConcatSection recentHeader;
    private SearchConcatSection recentSection;

    private SubredditHeaderConcatSection favHeader;

    private SubredditConcatSection favSection;
    private SubredditHeaderConcatSection feedsHeader;
    private SubredditConcatSection feedsSection;

    private SubredditHeaderConcatSection customFeedHeader;
    private SubredditConcatSection customFeedSection;
    private SubredditHeaderConcatSection subscriptionsHeader;

    private SubredditConcatSection subscriptionsSection;

    private SubredditHeaderConcatSection moderatedHeader;
    private SubredditConcatSection moderatedSection;
    private SubredditHeaderConcatSection topHeader;
    private SubredditConcatSection topSection;

    private SubredditHeaderConcatSection followsHeader;

    private SubredditConcatSection followsSection;

    private SubredditHeaderConcatSection searchSubscribedHeader;
    private SearchConcatSection searchSubscribedSection;

    private SubredditHeaderConcatSection searchResultsHeader;
    private SearchConcatSection searchResultsSection;
    private SubredditHeaderConcatSection searchActionHeader;
    private SearchConcatSection searchActionSection;

    private FooterHelper footerHelper;
    private RecyclerView recyclerView;

    /* JADX INFO: renamed from: eb.g$a */
    /* JADX INFO: compiled from: SubredditSearchConcatAdapter.java */
    class SearchSubredditNamesListener implements Response.Listener<OAuthSearchSubredditNames.SubredditName[]> {
        SearchSubredditNamesListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(OAuthSearchSubredditNames.SubredditName[] subredditNames) {
            SubredditSearchConcatAdapter.this.searchResultsSection.clearAll();
            for (int i = 0; i < subredditNames.length; i++) {
                if (!SubredditsManager.getInstance().isSubscribed(subredditNames[i].getName())) {
                    SubredditSearchConcatAdapter.this.searchResultsSection.addSubreddit(subredditNames[i].getName());
                    SubredditSearchConcatAdapter.this.searchResultsSection.putSubscriberCount(subredditNames[i].getName(), subredditNames[i].getSubscriberCount());
                }
            }
            SubredditSearchConcatAdapter.this.updateFooter();
            SubredditSearchConcatAdapter.this.recyclerView.setItemAnimator(null);
            SubredditSearchConcatAdapter.this.searchResultsSection.notifyDataSetChanged();
            SubredditSearchConcatAdapter.this.searchResultsHeader.notifyDataSetChanged();
        }
    }

    public SubredditSearchConcatAdapter(RecyclerView recyclerView, RecyclerView.Adapter headerAdapter, boolean showRecent) {
        this.recyclerView = recyclerView;
        SearchConcatSection searchConcatSection = new SearchConcatSection(8);
        this.recentSection = searchConcatSection;
        this.recentHeader = new SubredditHeaderConcatSection(searchConcatSection);
        SubredditConcatSection subredditConcatSection = new SubredditConcatSection(1);
        this.favSection = subredditConcatSection;
        this.favHeader = new SubredditHeaderConcatSection(subredditConcatSection);
        SubredditConcatSection subredditConcatSection2 = new SubredditConcatSection(7);
        this.feedsSection = subredditConcatSection2;
        this.feedsHeader = new SubredditHeaderConcatSection(subredditConcatSection2);
        SubredditConcatSection subredditConcatSection3 = new SubredditConcatSection(4);
        this.customFeedSection = subredditConcatSection3;
        this.customFeedHeader = new SubredditHeaderConcatSection(subredditConcatSection3);
        SubredditConcatSection subredditConcatSection4 = new SubredditConcatSection(2);
        this.subscriptionsSection = subredditConcatSection4;
        this.subscriptionsHeader = new SubredditHeaderConcatSection(subredditConcatSection4);
        SubredditConcatSection subredditConcatSection5 = new SubredditConcatSection(3);
        this.moderatedSection = subredditConcatSection5;
        this.moderatedHeader = new SubredditHeaderConcatSection(subredditConcatSection5);
        SubredditConcatSection subredditConcatSection6 = new SubredditConcatSection(5);
        this.topSection = subredditConcatSection6;
        this.topHeader = new SubredditHeaderConcatSection(subredditConcatSection6);
        SubredditConcatSection subredditConcatSection7 = new SubredditConcatSection(6);
        this.followsSection = subredditConcatSection7;
        this.followsHeader = new SubredditHeaderConcatSection(subredditConcatSection7);
        SearchConcatSection searchConcatSection2 = new SearchConcatSection(9);
        this.searchSubscribedSection = searchConcatSection2;
        this.searchSubscribedHeader = new SubredditHeaderConcatSection(searchConcatSection2);
        SearchConcatSection searchConcatSection3 = new SearchConcatSection(10);
        this.searchResultsSection = searchConcatSection3;
        this.searchResultsHeader = new SubredditHeaderConcatSection(searchConcatSection3);
        SearchConcatSection searchConcatSection4 = new SearchConcatSection(11);
        this.searchActionSection = searchConcatSection4;
        this.searchActionHeader = new SubredditHeaderConcatSection(searchConcatSection4);
        ConcatAdapter concatAdapter = new ConcatAdapter(new ConcatAdapter.Config.Builder().setStableIdMode(ConcatAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS).setIsolateViewTypes(true).build(), (RecyclerView.Adapter<? extends RecyclerView.ViewHolder>[]) new RecyclerView.Adapter[]{this.searchOptionsSection, this.favHeader, this.favSection, this.feedsHeader, this.feedsSection, this.customFeedHeader, this.customFeedSection, this.subscriptionsHeader, this.subscriptionsSection, this.moderatedHeader, this.moderatedSection, this.topHeader, this.topSection, this.followsHeader, this.followsSection, this.searchSubscribedHeader, this.searchSubscribedSection, this.searchResultsHeader, this.searchResultsSection, this.searchActionHeader, this.searchActionSection});
        this.concatAdapter = concatAdapter;
        if (showRecent) {
            concatAdapter.addAdapter(1, this.recentHeader);
            this.concatAdapter.addAdapter(2, this.recentSection);
        }
        if (headerAdapter != null) {
            this.concatAdapter.addAdapter(0, headerAdapter);
        }
        recyclerView.setAdapter(this.concatAdapter);
        new ItemTouchHelper(new ItemMoveCallback(this.favSection)).attachToRecyclerView(recyclerView);
    }

    private SubredditArrayList filterByQuery(SubredditArrayList subredditArrayList) {
        if (!StringUtils.isNotEmpty(this.query)) {
            return subredditArrayList;
        }
        SubredditArrayList subredditArrayList2 = new SubredditArrayList(subredditArrayList);
        for (int size = subredditArrayList2.size() - 1; size >= 0; size--) {
            if (!StringUtils.containsIgnoreCase(subredditArrayList.get(size), this.query)) {
                subredditArrayList2.remove(size);
            }
        }
        return subredditArrayList2;
    }

    private void refreshAll() {
        updateFooter();
        for (int i = 0; i < this.concatAdapter.getAdapters().size(); i++) {
            this.concatAdapter.getAdapters().get(i).notifyDataSetChanged();
        }
    }

    private void loadCustomFeeds() {
        this.customFeedSection.setSubreddits(MultiredditManager.getInstance().getMultireddits(), !SettingsSingleton.getSettings().duplicateFavs);
    }

    private void loadFavourites() {
        this.favSection.setSubreddits(FavouriteSubredditManager.getInstance().getFavourites(), false);
    }

    private void loadFeeds() {
        SubredditArrayList subredditArrayList = new SubredditArrayList();
        subredditArrayList.addIfAbsent("Frontpage");
        subredditArrayList.addIfAbsent("All");
        subredditArrayList.addIfAbsent("Popular");
        if (AccountSingleton.getInstance().isLoggedIn()) {
            subredditArrayList.addIfAbsent("Friends");
        }
        this.feedsSection.setSubreddits(subredditArrayList, !SettingsSingleton.getSettings().duplicateFavs);
    }

    private void loadModerated() {
        this.moderatedSection.setSubreddits(ModeratedSubredditManager.getInstance().getModerated(), !SettingsSingleton.getSettings().duplicateFavs);
    }

    private void loadRecent() {
        this.recentSection.setSubreddits(VisitedManager.getVisitedReversed());
    }

    private void loadSubscriptions() {
        SubredditArrayList subredditArrayList = new SubredditArrayList(SubredditsManager.getInstance().getSubreddits());
        SubredditArrayList subredditArrayList2 = new SubredditArrayList();
        for (int size = subredditArrayList.size() - 1; size >= 0; size--) {
            if (subredditArrayList.get(size).startsWith("u_")) {
                subredditArrayList2.addIfAbsent(subredditArrayList.remove(size));
            }
        }
        subredditArrayList2.sortDefault();
        this.subscriptionsSection.setSubreddits(subredditArrayList, !SettingsSingleton.getSettings().duplicateFavs);
        this.followsSection.setSubreddits(subredditArrayList2, !SettingsSingleton.getSettings().duplicateFavs);
    }

    private void loadTop() {
        SubredditArrayList subredditArrayList = new SubredditArrayList(TopSubredditHelper.getTopSubreddits());
        try {
            subredditArrayList = new SubredditArrayList(subredditArrayList.subList(0, Math.min(25, subredditArrayList.size())));
        } catch (Exception e) {
            Logger.logException(e, true);
        }
        this.topSection.setSubreddits(subredditArrayList, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFooter() {
        FooterHelper footerHelper = this.footerHelper;
        if (footerHelper != null) {
            footerHelper.setHeaderCount(getVisibleHeaderCount());
            this.footerHelper.setSectionCount(getVisibleSectionCount());
            this.footerHelper.setSearchOptionsVisible(this.searchOptionsSection.getItemCount() > 0);
            Logger.verbose("DRAWER", "Updating footer");
        }
    }

    public void setFooterHelper(FooterHelper footerHelper) {
        this.footerHelper = footerHelper;
    }

    public ConcatAdapter getConcatAdapter() {
        return this.concatAdapter;
    }

    public String getQuery() {
        return this.query;
    }

    public int getVisibleHeaderCount() {
        return this.favHeader.getItemCount() + 0 + this.feedsHeader.getItemCount() + this.customFeedHeader.getItemCount() + this.subscriptionsHeader.getItemCount() + this.moderatedHeader.getItemCount() + this.topHeader.getItemCount() + this.followsHeader.getItemCount() + this.searchSubscribedHeader.getItemCount() + this.searchResultsHeader.getItemCount() + this.searchActionHeader.getItemCount();
    }

    public int getVisibleSectionCount() {
        return this.favSection.getItemCount() + 0 + this.feedsSection.getItemCount() + this.customFeedSection.getItemCount() + this.subscriptionsSection.getItemCount() + this.moderatedSection.getItemCount() + this.topSection.getItemCount() + this.followsSection.getItemCount() + this.searchSubscribedSection.getItemCount() + this.searchResultsSection.getItemCount() + this.searchActionSection.getItemCount();
    }

    public boolean isRecentSectionVisible() {
        return this.recentHeader.isRecentEnabled() && this.recentHeader.getItemCount() > 0;
    }

    public void onSearchSubreddits(SearchSubredditsEvent searchSubredditsEvent) {
        if (searchSubredditsEvent == null) {
            setQuery(null);
            onSubredditsChanged(null);
            return;
        }
        String str = searchSubredditsEvent.query;
        this.query = str;
        setQuery(str);
        onSubredditsChanged(null);
        RedditApplication.oauthRequestQueue.cancelAll(OAuthSearchSubredditNames.TAG);
        OAuthRequestManager.sendWithAppContext(new OAuthSearchSubredditNames(this.recyclerView.getContext(), searchSubredditsEvent.query, new SearchSubredditNamesListener(), null));
    }

    public void onSubredditsChanged(SubredditsChangedEvent subredditsChangedEvent) {
        Logger.verbose("Subs changed!!!!");
        if (subredditsChangedEvent == null) {
            this.recyclerView.setItemAnimator(null);
        } else {
            this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
        for (int i = 0; i < this.concatAdapter.getAdapters().size(); i++) {
            RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter = this.concatAdapter.getAdapters().get(i);
            if (adapter instanceof SubredditHeaderConcatSection) {
                ((SubredditHeaderConcatSection) adapter).setQuery(this.query);
            } else if (adapter instanceof SubredditConcatSection) {
                ((SubredditConcatSection) adapter).setQuery(this.query);
            } else if (adapter instanceof SearchConcatSection) {
                ((SearchConcatSection) adapter).setQuery(this.query);
            }
        }
        loadRecent();
        loadModerated();
        loadFeeds();
        loadFavourites();
        loadCustomFeeds();
        loadSubscriptions();
        loadTop();
        if (StringUtils.isNotEmpty(this.query)) {
            SubredditArrayList subredditArrayList = new SubredditArrayList();
            subredditArrayList.addIfAbsent("Frontpage");
            subredditArrayList.addIfAbsent("All");
            subredditArrayList.addIfAbsent("Popular");
            if (AccountSingleton.getInstance().isLoggedIn()) {
                subredditArrayList.addIfAbsent("Friends");
            }
            subredditArrayList.addAll(FavouriteSubredditManager.getInstance().getFavourites());
            subredditArrayList.addAll(MultiredditManager.getInstance().getMultireddits());
            subredditArrayList.addAll(SubredditsManager.getInstance().getSubreddits());
            subredditArrayList.addAll(ModeratedSubredditManager.getInstance().getModerated());
            SubredditArrayList subredditArrayListF = filterByQuery(subredditArrayList);
            subredditArrayListF.sort(SubredditArrayList.SORT_DEFAULT);
            this.searchSubscribedSection.setSubreddits(subredditArrayListF);
            SubredditArrayList subredditArrayList2 = new SubredditArrayList();
            if (this.query.startsWith("https://")) {
                subredditArrayList2.add(MaterialPostsHelper.buildOpenUrlAddress(this.query.replace(StringUtils.SPACE, "")));
            } else {
                subredditArrayList2.add(MaterialPostsHelper.buildOverviewProfileAddress(this.query.replace(StringUtils.SPACE, "")));
                subredditArrayList2.add(this.query.replace(StringUtils.SPACE, ""));
            }
            this.searchActionSection.setSubreddits(subredditArrayList2);
        }
        this.searchOptionsSection.setVisible(StringUtils.isNotEmpty(this.query));
        refreshAll();
    }

    public void setShowRecent(boolean z) {
        this.recentSection.setRecentEnabled(z);
        this.recentHeader.setRecentEnabled(z);
        refreshAll();
    }

    public void setQuery(String str) {
        this.query = str;
    }
}
