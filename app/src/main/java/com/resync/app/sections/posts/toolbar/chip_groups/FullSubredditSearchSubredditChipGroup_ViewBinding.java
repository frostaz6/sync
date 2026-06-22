package com.resync.app.sections.posts.toolbar.chip_groups;

import android.view.View;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.util.DebouncingOnClickListener;

/* JADX INFO: loaded from: classes.dex */
public class FullSubredditSearchSubredditChipGroup_ViewBinding extends AbstractChipGroup_ViewBinding {
    private FullSubredditSearchSubredditChipGroup target;

    private View viewChipSearchOptions;

    class OnSearchOptionsClickListener extends DebouncingOnClickListener {

        final /* synthetic */ FullSubredditSearchSubredditChipGroup target;

        OnSearchOptionsClickListener(FullSubredditSearchSubredditChipGroup fullSubredditSearchSubredditChipGroup) {
            this.target = fullSubredditSearchSubredditChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onSearchOptionsClicked();
        }
    }

    public FullSubredditSearchSubredditChipGroup_ViewBinding(FullSubredditSearchSubredditChipGroup fullSubredditSearchSubredditChipGroup, View view) {
        super(fullSubredditSearchSubredditChipGroup, view);
        this.target = fullSubredditSearchSubredditChipGroup;
        View viewC = Utils.findRequiredView(view, R.id.chip_search_options, "method 'onSearchOptionsClicked'");
        this.viewChipSearchOptions = viewC;
        viewC.setOnClickListener(new OnSearchOptionsClickListener(fullSubredditSearchSubredditChipGroup));
    }

    @Override
    public void unbind() {
        FullSubredditSearchSubredditChipGroup fullSubredditSearchSubredditChipGroup = this.target;
        if (fullSubredditSearchSubredditChipGroup == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        if (this.viewChipSearchOptions != null) {
            this.viewChipSearchOptions.setOnClickListener(null);
            this.viewChipSearchOptions = null;
        }
        super.unbind();
    }
}
