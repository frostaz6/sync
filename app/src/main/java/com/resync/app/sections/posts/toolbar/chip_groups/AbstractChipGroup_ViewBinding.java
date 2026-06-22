package com.resync.app.sections.posts.toolbar.chip_groups;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.responsive.chips.ContentChip;
import com.resync.app.util.DebouncingOnClickListener;

/* JADX INFO: loaded from: classes.dex */
public class AbstractChipGroup_ViewBinding implements Unbinder {
    private AbstractChipGroup target;
    private View viewChipSort;

    class OnSortClickListener extends DebouncingOnClickListener {

        final /* synthetic */ AbstractChipGroup target;

        OnSortClickListener(AbstractChipGroup abstractChipGroup) {
            this.target = abstractChipGroup;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onSort();
        }
    }

    public AbstractChipGroup_ViewBinding(AbstractChipGroup abstractChipGroup, View view) {
        this.target = abstractChipGroup;
        View viewFindViewById = view.findViewById(R.id.chip_sort);
        abstractChipGroup.chipSort = (ContentChip) Utils.castView(viewFindViewById, R.id.chip_sort, "field 'chipSort'", ContentChip.class);
        if (viewFindViewById != null) {
            this.viewChipSort = viewFindViewById;
            viewFindViewById.setOnClickListener(new OnSortClickListener(abstractChipGroup));
        }
    }

    @Override
    public void unbind() {
        AbstractChipGroup abstractChipGroup = this.target;
        if (abstractChipGroup == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        abstractChipGroup.chipSort = null;
        View view = this.viewChipSort;
        if (view != null) {
            view.setOnClickListener(null);
            this.viewChipSort = null;
        }
    }
}
