package com.resync.app.ui.fragments;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.chip.ChipGroup;
import com.resync.app.R;
import com.resync.app.ui.views.comments.tabbed.CommentsTabbedScrollView;

/* JADX INFO: loaded from: classes2.dex */
public class TabbedCommentsFragment_ViewBinding implements Unbinder {
    private TabbedCommentsFragment target;

    public TabbedCommentsFragment_ViewBinding(TabbedCommentsFragment tabbedCommentsFragment, View view) {
        this.target = tabbedCommentsFragment;
        tabbedCommentsFragment.chipScroller = (CommentsTabbedScrollView) Utils.findRequiredViewAsType(view, R.id.chips, "field 'chipScroller'", CommentsTabbedScrollView.class);
        tabbedCommentsFragment.chipWrapper = (ChipGroup) Utils.findRequiredViewAsType(view, R.id.chip_group, "field 'chipWrapper'", ChipGroup.class);
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        TabbedCommentsFragment tabbedCommentsFragment = this.target;
        if (tabbedCommentsFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        tabbedCommentsFragment.chipScroller = null;
        tabbedCommentsFragment.chipWrapper = null;
    }
}
