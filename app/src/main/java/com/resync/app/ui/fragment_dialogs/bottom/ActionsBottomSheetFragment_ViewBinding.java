package com.resync.app.ui.fragment_dialogs.bottom;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.chips.ContextualChipsDropInView;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.tooltips.TooltipView;
import com.resync.app.util.DebouncingOnClickListener;

/* JADX INFO: loaded from: classes.dex */
public class ActionsBottomSheetFragment_ViewBinding implements Unbinder {
    private ActionsBottomSheetFragment target;
    private View sectionHeaderViewMoreClickView;

    class ClickListener0 extends DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ ActionsBottomSheetFragment target;

        ClickListener0(ActionsBottomSheetFragment actionsBottomSheetFragment) {
            this.target = actionsBottomSheetFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.showMore(view);
        }
    }

    public ActionsBottomSheetFragment_ViewBinding(ActionsBottomSheetFragment actionsBottomSheetFragment, View view) {
        this.target = actionsBottomSheetFragment;
        actionsBottomSheetFragment.headerView = (SheetHeaderView) Utils.findRequiredViewAsType(view, R.id.header, "field 'headerView'", SheetHeaderView.class);
        actionsBottomSheetFragment.contextualChipsDropInView = (ContextualChipsDropInView) Utils.findRequiredViewAsType(view, R.id.contextual_chips, "field 'contextualChipsDropInView'", ContextualChipsDropInView.class);
        actionsBottomSheetFragment.recyclerView = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.recycler, "field 'recyclerView'", RecyclerView.class);
        actionsBottomSheetFragment.tooltipView = (TooltipView) Utils.findRequiredViewAsType(view, R.id.tooltip, "field 'tooltipView'", TooltipView.class);
        View viewC = Utils.findRequiredView(view, R.id.section_header_view_more, "method 'showMore'");
        this.sectionHeaderViewMoreClickView = viewC;
        viewC.setOnClickListener(new ClickListener0(actionsBottomSheetFragment));
    }

    @Override
    public void unbind() {
        ActionsBottomSheetFragment target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.headerView = null;
        target.contextualChipsDropInView = null;
        target.recyclerView = null;
        target.tooltipView = null;
        this.sectionHeaderViewMoreClickView.setOnClickListener(null);
        this.sectionHeaderViewMoreClickView = null;
    }
}
