package com.resync.app.ui.fragment_dialogs.bottom;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.util.DebouncingOnClickListener;

/* JADX INFO: loaded from: classes.dex */
public class CommentActionsBottomSheetFragment_ViewBinding implements Unbinder {
    private CommentActionsBottomSheetFragment target;
    private View sectionHeaderViewMoreClickView;

    class ClickListener0 extends DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ CommentActionsBottomSheetFragment target;

        ClickListener0(CommentActionsBottomSheetFragment commentActionsBottomSheetFragment) {
            this.target = commentActionsBottomSheetFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.showMore(view);
        }
    }

    public CommentActionsBottomSheetFragment_ViewBinding(CommentActionsBottomSheetFragment commentActionsBottomSheetFragment, View view) {
        this.target = commentActionsBottomSheetFragment;
        commentActionsBottomSheetFragment.headerView = (SheetHeaderView) Utils.findRequiredViewAsType(view, R.id.header, "field 'headerView'", SheetHeaderView.class);
        commentActionsBottomSheetFragment.recyclerView = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.recycler, "field 'recyclerView'", RecyclerView.class);
        View viewC = Utils.findRequiredView(view, R.id.section_header_view_more, "method 'showMore'");
        this.sectionHeaderViewMoreClickView = viewC;
        viewC.setOnClickListener(new ClickListener0(commentActionsBottomSheetFragment));
    }

    @Override
    public void unbind() {
        CommentActionsBottomSheetFragment target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.headerView = null;
        target.recyclerView = null;
        this.sectionHeaderViewMoreClickView.setOnClickListener(null);
        this.sectionHeaderViewMoreClickView = null;
    }
}
