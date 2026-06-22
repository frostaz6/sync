package com.resync.app.sections.posts.toolbar.chip_groups;

import android.view.View;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.util.DebouncingOnClickListener;

/* JADX INFO: loaded from: classes.dex */
public class SearchSubredditChipGroup_ViewBinding extends AbstractChipGroup_ViewBinding {
    private SearchSubredditChipGroup target;

    private View viewChipSearchOptions;

    class OnSearchOptionsClickListener extends DebouncingOnClickListener {

        final /* synthetic */ SearchSubredditChipGroup target;

        OnSearchOptionsClickListener(SearchSubredditChipGroup searchSubredditChipGroup) {
            this.target = searchSubredditChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onSearchOptionsClicked();
        }
    }

    public SearchSubredditChipGroup_ViewBinding(SearchSubredditChipGroup searchSubredditChipGroup, View view) {
        super(searchSubredditChipGroup, view);
        this.target = searchSubredditChipGroup;
        View viewC = Utils.findRequiredView(view, R.id.chip_search_options, "method 'onSearchOptionsClicked'");
        this.viewChipSearchOptions = viewC;
        viewC.setOnClickListener(new OnSearchOptionsClickListener(searchSubredditChipGroup));
    }

    @Override
    public void unbind() {
        SearchSubredditChipGroup searchSubredditChipGroup = this.target;
        if (searchSubredditChipGroup == null) {
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
