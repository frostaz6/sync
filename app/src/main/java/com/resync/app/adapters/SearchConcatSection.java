package com.resync.app.adapters;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.viewholders.sections.search.SearchItemHolder;
import com.resync.app.helpers.ui.SubredditArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: renamed from: eb.d */
/* JADX INFO: compiled from: SearchConcatSection.java */
/* JADX INFO: loaded from: classes.dex */
public class SearchConcatSection extends RecyclerView.Adapter<SearchItemHolder> implements ConcatSection {
    private final int sectionId;

    private final SubredditArrayList subreddits = new SubredditArrayList();

    private final HashMap<String, String> subscriberCounts = new HashMap<>();
    private String query;
    private boolean recentEnabled;

    public SearchConcatSection(int sectionId) {
        this.sectionId = sectionId;
        setHasStableIds(true);
    }

    public void addSubreddit(String str) {
        this.subreddits.addIfAbsent(str);
    }

    public void setSubreddits(SubredditArrayList subredditArrayList) {
        this.subreddits.clear();
        if (subredditArrayList != null) {
            this.subreddits.addAll(subredditArrayList);
        }
    }

    public void putSubscriberCount(String str, String str2) {
        this.subscriberCounts.put(str, str2);
    }

    public void clearAll() {
        this.subreddits.clear();
        this.subscriberCounts.clear();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: R, reason: merged with bridge method [inline-methods] */
    public void onBindViewHolder(SearchItemHolder searchItemHolder, int position) {
        String str = this.subreddits.get(position);
        searchItemHolder.bind(this.sectionId, str, this.subscriberCounts.get(str), this.query);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: S, reason: merged with bridge method [inline-methods] */
    public SearchItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return SearchItemHolder.create(viewGroup.getContext(), viewGroup);
    }

    public void setQuery(String str) {
        this.query = str;
    }

    public void setRecentEnabled(boolean z) {
        this.recentEnabled = z;
    }

    @Override // com.resync.app.adapters.ConcatSection
    public int getSectionId() {
        return this.sectionId;
    }

    @Override // com.resync.app.adapters.ConcatSection
    public boolean hasItems() {
        return this.subreddits.size() > 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: n */
    public int getItemCount() {
        int sectionId = this.sectionId;
        if (sectionId == 9) {
            if (!SettingsSingleton.getSettings().collapseSearchSubscribed && StringUtils.isNotEmpty(this.query)) {
                return this.subreddits.size();
            }
            return 0;
        }
        if (sectionId == 10) {
            if (!SettingsSingleton.getSettings().collapseSearchResults && StringUtils.isNotEmpty(this.query)) {
                return this.subreddits.size();
            }
            return 0;
        }
        if (sectionId == 11) {
            if (!SettingsSingleton.getSettings().collapseSearchAction && StringUtils.isNotEmpty(this.query)) {
                return this.subreddits.size();
            }
            return 0;
        }
        if (sectionId != 8) {
            throw new RuntimeException("Unsupported section: " + this.sectionId);
        }
        if (this.recentEnabled && StringUtils.isEmpty(this.query) && !SettingsSingleton.getSettings().collapseSearchRecent) {
            return this.subreddits.size();
        }
        return 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: o */
    public long getItemId(int position) {
        return this.subreddits.get(position).hashCode();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return 3;
    }
}
