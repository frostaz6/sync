package com.resync.app.sections.posts.toolbar.chip_groups;

import android.view.View;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.util.DebouncingOnClickListener;

/* JADX INFO: loaded from: classes.dex */
public class SeenSubredditChipGroup_ViewBinding extends AbstractChipGroup_ViewBinding {
    private SeenSubredditChipGroup target;

    private View viewChipSeenOptions;

    class OnSeenOptionsClickListener extends DebouncingOnClickListener {

        final /* synthetic */ SeenSubredditChipGroup target;

        OnSeenOptionsClickListener(SeenSubredditChipGroup seenSubredditChipGroup) {
            this.target = seenSubredditChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onSearchOptionsClicked();
        }
    }

    public SeenSubredditChipGroup_ViewBinding(SeenSubredditChipGroup seenSubredditChipGroup, View view) {
        super(seenSubredditChipGroup, view);
        this.target = seenSubredditChipGroup;
        View viewC = Utils.findRequiredView(view, R.id.chip_seen_options, "method 'onSearchOptionsClicked'");
        this.viewChipSeenOptions = viewC;
        viewC.setOnClickListener(new OnSeenOptionsClickListener(seenSubredditChipGroup));
    }

    @Override
    public void unbind() {
        SeenSubredditChipGroup seenSubredditChipGroup = this.target;
        if (seenSubredditChipGroup == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        if (this.viewChipSeenOptions != null) {
            this.viewChipSeenOptions.setOnClickListener(null);
            this.viewChipSeenOptions = null;
        }
        super.unbind();
    }
}
