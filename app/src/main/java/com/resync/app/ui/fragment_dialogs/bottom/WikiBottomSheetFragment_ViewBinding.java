package com.resync.app.ui.fragment_dialogs.bottom;

import android.view.View;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.text.spannable.children.HtmlTableView;

/* JADX INFO: loaded from: classes.dex */
public class WikiBottomSheetFragment_ViewBinding implements Unbinder {
    private WikiBottomSheetFragment target;

    public WikiBottomSheetFragment_ViewBinding(WikiBottomSheetFragment wikiBottomSheetFragment, View view) {
        this.target = wikiBottomSheetFragment;
        wikiBottomSheetFragment.mRootWrapper = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.fragment_wiki_main_root_wrapper, "field 'mRootWrapper'", RelativeLayout.class);
        wikiBottomSheetFragment.mMainWrapper = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.fragment_wiki_main_wrapper, "field 'mMainWrapper'", RelativeLayout.class);
        wikiBottomSheetFragment.mAsyncWrapper = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.fragment_wiki_async_wrapper, "field 'mAsyncWrapper'", RelativeLayout.class);
        wikiBottomSheetFragment.mHeader = (SheetHeaderView) Utils.findRequiredViewAsType(view, R.id.header, "field 'mHeader'", SheetHeaderView.class);
        wikiBottomSheetFragment.mContent = (HtmlTableView) Utils.findRequiredViewAsType(view, R.id.fragment_wiki_content, "field 'mContent'", HtmlTableView.class);
    }

    @Override
    public void unbind() {
        WikiBottomSheetFragment target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.mRootWrapper = null;
        target.mMainWrapper = null;
        target.mAsyncWrapper = null;
        target.mHeader = null;
        target.mContent = null;
    }
}
