package com.resync.app.sections.posts.toolbar.chip_groups;

import android.view.View;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.util.DebouncingOnClickListener;

/* JADX INFO: loaded from: classes.dex */
public class WatchingSubredditChipGroup_ViewBinding extends AbstractChipGroup_ViewBinding {
    private WatchingSubredditChipGroup target;

    private View viewChipRemoveWatching;

    class OnRemoveWatchingClickListener extends DebouncingOnClickListener {

        final /* synthetic */ WatchingSubredditChipGroup target;

        OnRemoveWatchingClickListener(WatchingSubredditChipGroup watchingSubredditChipGroup) {
            this.target = watchingSubredditChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onRemoveWatchingClicked();
        }
    }

    public WatchingSubredditChipGroup_ViewBinding(WatchingSubredditChipGroup watchingSubredditChipGroup, View view) {
        super(watchingSubredditChipGroup, view);
        this.target = watchingSubredditChipGroup;
        View viewC = Utils.findRequiredView(view, R.id.chip_remote_watching, "method 'onRemoveWatchingClicked'");
        this.viewChipRemoveWatching = viewC;
        viewC.setOnClickListener(new OnRemoveWatchingClickListener(watchingSubredditChipGroup));
    }

    @Override
    public void unbind() {
        WatchingSubredditChipGroup watchingSubredditChipGroup = this.target;
        if (watchingSubredditChipGroup == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        if (this.viewChipRemoveWatching != null) {
            this.viewChipRemoveWatching.setOnClickListener(null);
            this.viewChipRemoveWatching = null;
        }
        super.unbind();
    }
}
