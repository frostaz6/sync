package com.resync.app.ui.fragment_dialogs.bottom;

import android.view.View;
import android.widget.FrameLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.monet.MonetFab;
import com.resync.app.ui.views.monet.MonetSearchBoxView;
import com.resync.app.ui.views.recycler.SubredditsRecyclerView;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;
import com.resync.app.util.DebouncingOnClickListener;

/* JADX INFO: loaded from: classes.dex */
public class SubredditsBottomSheetFragment_ViewBinding implements Unbinder {
    private SubredditsBottomSheetFragment target;
    private View searchWrapperClickView;

    class ClickListener0 extends DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ SubredditsBottomSheetFragment target;

        ClickListener0(SubredditsBottomSheetFragment subredditsBottomSheetFragment) {
            this.target = subredditsBottomSheetFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onSearchWrapperClicked();
        }
    }

    public SubredditsBottomSheetFragment_ViewBinding(SubredditsBottomSheetFragment subredditsBottomSheetFragment, View view) {
        this.target = subredditsBottomSheetFragment;
        View viewC = Utils.findRequiredView(view, R.id.search_wrapper, "field 'searchWrapper' and method 'onSearchWrapperClicked'");
        subredditsBottomSheetFragment.searchWrapper = (FrameLayout) Utils.castView(viewC, R.id.search_wrapper, "field 'searchWrapper'", FrameLayout.class);
        this.searchWrapperClickView = viewC;
        viewC.setOnClickListener(new ClickListener0(subredditsBottomSheetFragment));
        subredditsBottomSheetFragment.searchBox = (MonetSearchBoxView) Utils.findRequiredViewAsType(view, R.id.monet_search_box, "field 'searchBox'", MonetSearchBoxView.class);
        subredditsBottomSheetFragment.mRecyclerView = (SubredditsRecyclerView) Utils.findRequiredViewAsType(view, R.id.bottom_sheet_subreddits_recyclerview, "field 'mRecyclerView'", SubredditsRecyclerView.class);
        subredditsBottomSheetFragment.mFab = (MonetFab) Utils.findRequiredViewAsType(view, R.id.fab, "field 'mFab'", MonetFab.class);
        subredditsBottomSheetFragment.mFastScroller = (FastScrollerView) Utils.findRequiredViewAsType(view, R.id.bottom_sheet_subreddits_recyclerview_scroller, "field 'mFastScroller'", FastScrollerView.class);
        subredditsBottomSheetFragment.mFastScrollerThumb = (FastScrollerThumbView) Utils.findRequiredViewAsType(view, R.id.bottom_sheet_subreddits_recyclerview_scroller_thumb, "field 'mFastScrollerThumb'", FastScrollerThumbView.class);
    }

    @Override
    public void unbind() {
        SubredditsBottomSheetFragment target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.searchWrapper = null;
        target.searchBox = null;
        target.mRecyclerView = null;
        target.mFab = null;
        target.mFastScroller = null;
        target.mFastScrollerThumb = null;
        this.searchWrapperClickView.setOnClickListener(null);
        this.searchWrapperClickView = null;
    }
}
