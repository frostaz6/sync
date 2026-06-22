package com.resync.app.ui.fragment_dialogs.bottom;

import android.view.View;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.text.spannable.children.HtmlTableView;

/* JADX INFO: loaded from: classes.dex */
public class SubredditRulesBottomSheetFragment_ViewBinding implements Unbinder {
    private SubredditRulesBottomSheetFragment target;

    public SubredditRulesBottomSheetFragment_ViewBinding(SubredditRulesBottomSheetFragment subredditRulesBottomSheetFragment, View view) {
        this.target = subredditRulesBottomSheetFragment;
        subredditRulesBottomSheetFragment.mRootWrapper = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.fragment_rules_main_root_wrapper, "field 'mRootWrapper'", RelativeLayout.class);
        subredditRulesBottomSheetFragment.mMainWrapper = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.fragment_rules_main_wrapper, "field 'mMainWrapper'", RelativeLayout.class);
        subredditRulesBottomSheetFragment.mAsyncWrapper = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.fragment_rules_async_wrapper, "field 'mAsyncWrapper'", RelativeLayout.class);
        subredditRulesBottomSheetFragment.mHeader = (SheetHeaderView) Utils.findRequiredViewAsType(view, R.id.fragment_rules_header, "field 'mHeader'", SheetHeaderView.class);
        subredditRulesBottomSheetFragment.mContent = (HtmlTableView) Utils.findRequiredViewAsType(view, R.id.fragment_rules_content, "field 'mContent'", HtmlTableView.class);
    }

    @Override
    public void unbind() {
        SubredditRulesBottomSheetFragment target = this.target;
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
