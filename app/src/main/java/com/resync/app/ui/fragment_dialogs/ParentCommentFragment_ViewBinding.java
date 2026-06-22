package com.resync.app.ui.fragment_dialogs;

import android.view.View;
import android.widget.FrameLayout;
import androidx.core.widget.NestedScrollView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes.dex */
public class ParentCommentFragment_ViewBinding implements Unbinder {
    private ParentCommentFragment target;

    public ParentCommentFragment_ViewBinding(ParentCommentFragment parentCommentFragment, View view) {
        this.target = parentCommentFragment;
        parentCommentFragment.mScrollView = (NestedScrollView) Utils.findRequiredViewAsType(view, R.id.scrollView, "field 'mScrollView'", NestedScrollView.class);
        parentCommentFragment.mFrameLayout = (FrameLayout) Utils.findRequiredViewAsType(view, R.id.linearLayout, "field 'mFrameLayout'", FrameLayout.class);
    }

    @Override
    public void unbind() {
        ParentCommentFragment target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.mScrollView = null;
        target.mFrameLayout = null;
    }
}
