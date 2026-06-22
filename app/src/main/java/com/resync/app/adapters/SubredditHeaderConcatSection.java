package com.resync.app.adapters;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.viewholders.sections.subreddits.SubredditHeaderHolder;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: renamed from: eb.f */
/* JADX INFO: compiled from: SubredditHeaderConcatSection.java */
/* JADX INFO: loaded from: classes.dex */
public class SubredditHeaderConcatSection extends RecyclerView.Adapter<SubredditHeaderHolder> implements ConcatSection {
    private final int sectionId;

    private String query;

    private boolean recentEnabled;
    private final RecyclerView.Adapter childAdapter;

    /* JADX WARN: Multi-variable type inference failed */
    public SubredditHeaderConcatSection(RecyclerView.Adapter childAdapter) {
        this.childAdapter = childAdapter;
        if (childAdapter instanceof ConcatSection) {
            this.sectionId = ((ConcatSection) childAdapter).getSectionId();
            setHasStableIds(true);
        } else {
            throw new RuntimeException("Unsupported child adapter: " + childAdapter);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int getChildItemCount() {
        RecyclerView.Adapter childAdapter = this.childAdapter;
        if (childAdapter instanceof ConcatSection) {
            return ((ConcatSection) childAdapter).hasItems() ? 1 : 0;
        }
        throw new RuntimeException("Unsupported child");
    }

    public boolean isRecentEnabled() {
        return this.recentEnabled;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: P, reason: merged with bridge method [inline-methods] */
    public void onBindViewHolder(SubredditHeaderHolder subredditHeaderHolder, int position) {
        subredditHeaderHolder.bind(this.sectionId, this.query);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: Q, reason: merged with bridge method [inline-methods] */
    public SubredditHeaderHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return SubredditHeaderHolder.create(viewGroup);
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
        return false;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: n */
    public int getItemCount() {
        int sectionId = this.sectionId;
        if (sectionId == 9 || sectionId == 10 || sectionId == 11) {
            if (StringUtils.isEmpty(this.query)) {
                return 0;
            }
            return getChildItemCount();
        }
        if (StringUtils.isNotEmpty(this.query)) {
            return 0;
        }
        if (sectionId == 7) {
            if (SettingsSingleton.getSettings().subredditsShowFeeds) {
                return getChildItemCount();
            }
            return 0;
        }
        if (sectionId == 1) {
            if (SettingsSingleton.getSettings().subredditsShowFav) {
                return getChildItemCount();
            }
            return 0;
        }
        if (sectionId == 2) {
            if (SettingsSingleton.getSettings().subredditsShowSubscriptions) {
                return getChildItemCount();
            }
            return 0;
        }
        if (sectionId == 3) {
            if (SettingsSingleton.getSettings().subredditsShowModerator) {
                return getChildItemCount();
            }
            return 0;
        }
        if (sectionId == 4) {
            if (SettingsSingleton.getSettings().subredditsShowCustom) {
                return getChildItemCount();
            }
            return 0;
        }
        if (sectionId == 5) {
            if (SettingsSingleton.getSettings().subredditsShowTop) {
                return getChildItemCount();
            }
            return 0;
        }
        if (sectionId == 6) {
            if (SettingsSingleton.getSettings().subredditsShowFollows) {
                return getChildItemCount();
            }
            return 0;
        }
        if (sectionId != 8) {
            throw new RuntimeException("Unsupported type");
        }
        if (this.recentEnabled && StringUtils.isEmpty(this.query)) {
            return getChildItemCount();
        }
        return 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: o */
    public long getItemId(int position) {
        return this.sectionId;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return 2;
    }
}
