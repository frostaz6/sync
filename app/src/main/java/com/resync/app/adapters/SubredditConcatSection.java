package com.resync.app.adapters;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.viewholders.sections.subreddits.SubredditHolder;
import com.resync.app.helpers.ui.SubredditArrayList;
import java.util.Collections;
import com.resync.app.widgets.recycler.ItemMoveCallback;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.FavouriteSubredditManager;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: eb.e */
/* JADX INFO: compiled from: SubredditConcatSection.java */
/* JADX INFO: loaded from: classes.dex */
public class SubredditConcatSection extends RecyclerView.Adapter<SubredditHolder> implements ItemMoveCallback.Callback, ConcatSection {
    private final SubredditArrayList subreddits = new SubredditArrayList();

    private final int sectionId;

    private String query;

    public SubredditConcatSection(int sectionId) {
        this.sectionId = sectionId;
        setHasStableIds(true);
    }

    public void setSubreddits(SubredditArrayList subredditArrayList, boolean excludeFavourites) {
        this.subreddits.clear();
        if (!excludeFavourites) {
            this.subreddits.addAll(subredditArrayList);
            return;
        }
        SubredditArrayList subredditArrayList2 = new SubredditArrayList(FavouriteSubredditManager.getInstance().getFavourites());
        for (String str : subredditArrayList) {
            if (!subredditArrayList2.contains(str)) {
                this.subreddits.addIfAbsent(str);
            }
        }
    }

    public String getSubredditAt(int position) {
        return this.subreddits.get(position);
    }

    public SubredditArrayList getSubreddits() {
        return this.subreddits;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: Q, reason: merged with bridge method [inline-methods] */
    public void onBindViewHolder(SubredditHolder subredditHolder, int position) {
        subredditHolder.bind(this.sectionId, getSubredditAt(position), ContextHelper.getCurrentSubredditName(subredditHolder.itemView.getContext()));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: R, reason: merged with bridge method [inline-methods] */
    public SubredditHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Logger.verbose("DRAWER", "CREATING VIEW HOLDER");
        return SubredditHolder.create(viewGroup);
    }

    public void setQuery(String str) {
        this.query = str;
    }

    public boolean swapSubreddits(String str, String str2) {
        if (!this.subreddits.contains(str) || !this.subreddits.contains(str2)) {
            return false;
        }
        SubredditArrayList subredditArrayList = this.subreddits;
        Collections.swap(subredditArrayList, subredditArrayList.indexOf(str), this.subreddits.indexOf(str2));
        return true;
    }

    @Override // com.resync.app.widgets.recycler.ItemMoveCallback.Callback
    public void onSelected(RecyclerView.ViewHolder holder) {
        if (holder instanceof SubredditHolder) {
            ((SubredditHolder) holder).highlight();
        }
    }

    @Override // com.resync.app.widgets.recycler.ItemMoveCallback.Callback
    public void onCleared(RecyclerView.ViewHolder holder) {
        notifyDataSetChanged();
    }

    @Override // com.resync.app.widgets.recycler.ItemMoveCallback.Callback
    public void onMove(String str, String str2) {
        if (swapSubreddits(str, str2)) {
            FavouriteSubredditManager.getInstance().setFavourites(getSubreddits());
        }
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
        if (StringUtils.isNotEmpty(this.query)) {
            return 0;
        }
        int sectionId = this.sectionId;
        if (sectionId == 7) {
            if (SettingsSingleton.getSettings().collapseFeeds || !SettingsSingleton.getSettings().subredditsShowFeeds) {
                return 0;
            }
            return this.subreddits.size();
        }
        if (sectionId == 1) {
            if (SettingsSingleton.getSettings().collapseFav) {
                return 0;
            }
            return this.subreddits.size();
        }
        if (sectionId == 2) {
            if (SettingsSingleton.getSettings().collapseSubscriptions) {
                return 0;
            }
            return this.subreddits.size();
        }
        if (sectionId == 3) {
            if (SettingsSingleton.getSettings().collapseModerated) {
                return 0;
            }
            return this.subreddits.size();
        }
        if (sectionId == 4) {
            if (SettingsSingleton.getSettings().collapseCustomFeed) {
                return 0;
            }
            return this.subreddits.size();
        }
        if (sectionId == 5) {
            if (SettingsSingleton.getSettings().collapseTop) {
                return 0;
            }
            return this.subreddits.size();
        }
        if (sectionId == 6) {
            if (SettingsSingleton.getSettings().collapseFollows) {
                return 0;
            }
            return this.subreddits.size();
        }
        if (sectionId == 9) {
            if (SettingsSingleton.getSettings().collapseSearchSubscribed) {
                return 0;
            }
            return this.subreddits.size();
        }
        if (sectionId == 10) {
            if (SettingsSingleton.getSettings().collapseSearchResults) {
                return 0;
            }
            return this.subreddits.size();
        }
        if (sectionId == 11) {
            if (SettingsSingleton.getSettings().collapseSearchAction) {
                return 0;
            }
            return this.subreddits.size();
        }
        if (sectionId != 8) {
            throw new RuntimeException("Unsupported type");
        }
        if (SettingsSingleton.getSettings().collapseSearchRecent) {
            return 0;
        }
        return this.subreddits.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: o */
    public long getItemId(int position) {
        return (this.sectionId + "-" + this.subreddits.get(position)).hashCode();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return 1;
    }
}
