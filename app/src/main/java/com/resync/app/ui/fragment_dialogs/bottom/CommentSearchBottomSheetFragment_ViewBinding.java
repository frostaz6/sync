package com.resync.app.ui.fragment_dialogs.bottom;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.monet.MonetSearchBoxView;

/* JADX INFO: loaded from: classes.dex */
public class CommentSearchBottomSheetFragment_ViewBinding implements Unbinder {
    private CommentSearchBottomSheetFragment target;

    public CommentSearchBottomSheetFragment_ViewBinding(CommentSearchBottomSheetFragment commentSearchBottomSheetFragment, View view) {
        this.target = commentSearchBottomSheetFragment;
        commentSearchBottomSheetFragment.mRecycler = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.bottom_sheet_comment_search_recycler, "field 'mRecycler'", RecyclerView.class);
        commentSearchBottomSheetFragment.searchBoxView = (MonetSearchBoxView) Utils.findRequiredViewAsType(view, R.id.monet_search_box, "field 'searchBoxView'", MonetSearchBoxView.class);
    }

    @Override
    public void unbind() {
        CommentSearchBottomSheetFragment target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.mRecycler = null;
        target.searchBoxView = null;
    }
}
