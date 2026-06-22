package com.resync.app.sections.posts.toolbar.chip_groups;

import android.view.View;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes.dex */
public class DefaultSubredditChipGroup_ViewBinding extends AbstractChipGroup_ViewBinding {
    private DefaultSubredditChipGroup target;

    private View viewChipRandom;

    private View viewChipTopGrowing;
    private View viewChipView;

    class OnPreviewsClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ DefaultSubredditChipGroup target;

        OnPreviewsClickListener(DefaultSubredditChipGroup defaultSubredditChipGroup) {
            this.target = defaultSubredditChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onPreviewsClicked();
        }
    }

    class OnTopGrowingClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ DefaultSubredditChipGroup target;

        OnTopGrowingClickListener(DefaultSubredditChipGroup defaultSubredditChipGroup) {
            this.target = defaultSubredditChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onTopGrowingClicked();
        }
    }

    class OnChangeViewClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ DefaultSubredditChipGroup target;

        OnChangeViewClickListener(DefaultSubredditChipGroup defaultSubredditChipGroup) {
            this.target = defaultSubredditChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onChangeViewClicked();
        }
    }

    public DefaultSubredditChipGroup_ViewBinding(DefaultSubredditChipGroup defaultSubredditChipGroup, View view) {
        super(defaultSubredditChipGroup, view);
        this.target = defaultSubredditChipGroup;
        View viewC = Utils.findRequiredView(view, R.id.chip_random, "method 'onPreviewsClicked'");
        this.viewChipRandom = viewC;
        viewC.setOnClickListener(new OnPreviewsClickListener(defaultSubredditChipGroup));
        View viewC2 = Utils.findRequiredView(view, R.id.chip_top_growing, "method 'onTopGrowingClicked'");
        this.viewChipTopGrowing = viewC2;
        viewC2.setOnClickListener(new OnTopGrowingClickListener(defaultSubredditChipGroup));
        View viewC3 = Utils.findRequiredView(view, R.id.chip_view, "method 'onChangeViewClicked'");
        this.viewChipView = viewC3;
        viewC3.setOnClickListener(new OnChangeViewClickListener(defaultSubredditChipGroup));
    }

    @Override
    public void unbind() {
        DefaultSubredditChipGroup defaultSubredditChipGroup = this.target;
        if (defaultSubredditChipGroup == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        if (this.viewChipRandom != null) {
            this.viewChipRandom.setOnClickListener(null);
            this.viewChipRandom = null;
        }
        if (this.viewChipTopGrowing != null) {
            this.viewChipTopGrowing.setOnClickListener(null);
            this.viewChipTopGrowing = null;
        }
        if (this.viewChipView != null) {
            this.viewChipView.setOnClickListener(null);
            this.viewChipView = null;
        }
        super.unbind();
    }
}
