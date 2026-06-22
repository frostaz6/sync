package com.resync.app.sections.posts.toolbar.chip_groups;

import android.view.View;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.responsive.chips.ContentChip;

/* JADX INFO: loaded from: classes.dex */
public class PlainSubredditChipGroup_ViewBinding extends AbstractChipGroup_ViewBinding {
    private PlainSubredditChipGroup target;

    private View viewChipWiki;

    private View viewChipAbout;
    private View viewChipJoin;
    private View viewChipFlair;

    private View viewChipFav;
    private View viewChipWatch;
    private View viewChipMore;

    class OnWikiClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ PlainSubredditChipGroup target;

        OnWikiClickListener(PlainSubredditChipGroup plainSubredditChipGroup) {
            this.target = plainSubredditChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onWiki();
        }
    }

    class OnAboutClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ PlainSubredditChipGroup target;

        OnAboutClickListener(PlainSubredditChipGroup plainSubredditChipGroup) {
            this.target = plainSubredditChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onAbout();
        }
    }

    class OnJoinClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ PlainSubredditChipGroup target;

        OnJoinClickListener(PlainSubredditChipGroup plainSubredditChipGroup) {
            this.target = plainSubredditChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onJoin();
        }
    }

    class OnFlairClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ PlainSubredditChipGroup target;

        OnFlairClickListener(PlainSubredditChipGroup plainSubredditChipGroup) {
            this.target = plainSubredditChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onFlair();
        }
    }

    class OnFavClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ PlainSubredditChipGroup target;

        OnFavClickListener(PlainSubredditChipGroup plainSubredditChipGroup) {
            this.target = plainSubredditChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onFav();
        }
    }

    class OnWatchClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ PlainSubredditChipGroup target;

        OnWatchClickListener(PlainSubredditChipGroup plainSubredditChipGroup) {
            this.target = plainSubredditChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onWatch();
        }
    }

    class OnMoreClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ PlainSubredditChipGroup target;

        OnMoreClickListener(PlainSubredditChipGroup plainSubredditChipGroup) {
            this.target = plainSubredditChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onMoreClicked();
        }
    }

    public PlainSubredditChipGroup_ViewBinding(PlainSubredditChipGroup plainSubredditChipGroup, View view) {
        super(plainSubredditChipGroup, view);
        this.target = plainSubredditChipGroup;
        View viewC = Utils.findRequiredView(view, R.id.chip_wiki, "field 'chipWiki' and method 'onWiki'");
        plainSubredditChipGroup.chipWiki = (ContentChip) Utils.castView(viewC, R.id.chip_wiki, "field 'chipWiki'", ContentChip.class);
        this.viewChipWiki = viewC;
        viewC.setOnClickListener(new OnWikiClickListener(plainSubredditChipGroup));
        View viewC2 = Utils.findRequiredView(view, R.id.chip_about, "field 'chipAbout' and method 'onAbout'");
        plainSubredditChipGroup.chipAbout = (ContentChip) Utils.castView(viewC2, R.id.chip_about, "field 'chipAbout'", ContentChip.class);
        this.viewChipAbout = viewC2;
        viewC2.setOnClickListener(new OnAboutClickListener(plainSubredditChipGroup));
        View viewC3 = Utils.findRequiredView(view, R.id.chip_join, "field 'chipJoin' and method 'onJoin'");
        plainSubredditChipGroup.chipJoin = (ContentChip) Utils.castView(viewC3, R.id.chip_join, "field 'chipJoin'", ContentChip.class);
        this.viewChipJoin = viewC3;
        viewC3.setOnClickListener(new OnJoinClickListener(plainSubredditChipGroup));
        View viewC4 = Utils.findRequiredView(view, R.id.chip_flair, "field 'chipFlair' and method 'onFlair'");
        plainSubredditChipGroup.chipFlair = (ContentChip) Utils.castView(viewC4, R.id.chip_flair, "field 'chipFlair'", ContentChip.class);
        this.viewChipFlair = viewC4;
        viewC4.setOnClickListener(new OnFlairClickListener(plainSubredditChipGroup));
        View viewC5 = Utils.findRequiredView(view, R.id.chip_fav, "field 'chipFav' and method 'onFav'");
        plainSubredditChipGroup.chipFav = (ContentChip) Utils.castView(viewC5, R.id.chip_fav, "field 'chipFav'", ContentChip.class);
        this.viewChipFav = viewC5;
        viewC5.setOnClickListener(new OnFavClickListener(plainSubredditChipGroup));
        View viewC6 = Utils.findRequiredView(view, R.id.chip_watch, "field 'chipWatch' and method 'onWatch'");
        plainSubredditChipGroup.chipWatch = (ContentChip) Utils.castView(viewC6, R.id.chip_watch, "field 'chipWatch'", ContentChip.class);
        this.viewChipWatch = viewC6;
        viewC6.setOnClickListener(new OnWatchClickListener(plainSubredditChipGroup));
        View viewC7 = Utils.findRequiredView(view, R.id.chip_more, "field 'chipMore' and method 'onMoreClicked'");
        plainSubredditChipGroup.chipMore = (ContentChip) Utils.castView(viewC7, R.id.chip_more, "field 'chipMore'", ContentChip.class);
        this.viewChipMore = viewC7;
        viewC7.setOnClickListener(new OnMoreClickListener(plainSubredditChipGroup));
    }

    @Override
    public void unbind() {
        PlainSubredditChipGroup plainSubredditChipGroup = this.target;
        if (plainSubredditChipGroup == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        plainSubredditChipGroup.chipWiki = null;
        plainSubredditChipGroup.chipAbout = null;
        plainSubredditChipGroup.chipJoin = null;
        plainSubredditChipGroup.chipFlair = null;
        plainSubredditChipGroup.chipFav = null;
        plainSubredditChipGroup.chipWatch = null;
        plainSubredditChipGroup.chipMore = null;
        if (this.viewChipWiki != null) {
            this.viewChipWiki.setOnClickListener(null);
            this.viewChipWiki = null;
        }
        if (this.viewChipAbout != null) {
            this.viewChipAbout.setOnClickListener(null);
            this.viewChipAbout = null;
        }
        if (this.viewChipJoin != null) {
            this.viewChipJoin.setOnClickListener(null);
            this.viewChipJoin = null;
        }
        if (this.viewChipFlair != null) {
            this.viewChipFlair.setOnClickListener(null);
            this.viewChipFlair = null;
        }
        if (this.viewChipFav != null) {
            this.viewChipFav.setOnClickListener(null);
            this.viewChipFav = null;
        }
        if (this.viewChipWatch != null) {
            this.viewChipWatch.setOnClickListener(null);
            this.viewChipWatch = null;
        }
        if (this.viewChipMore != null) {
            this.viewChipMore.setOnClickListener(null);
            this.viewChipMore = null;
        }
        super.unbind();
    }
}
