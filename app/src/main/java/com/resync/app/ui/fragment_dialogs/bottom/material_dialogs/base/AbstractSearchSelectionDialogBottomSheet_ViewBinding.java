package com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base;

import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.monet.MonetSearchBoxView;

/* JADX INFO: loaded from: classes.dex */
public class AbstractSearchSelectionDialogBottomSheet_ViewBinding implements Unbinder {
    private AbstractSearchSelectionDialogBottomSheet target;

    public AbstractSearchSelectionDialogBottomSheet_ViewBinding(AbstractSearchSelectionDialogBottomSheet abstractSearchSelectionDialogBottomSheet, View view) {
        this.target = abstractSearchSelectionDialogBottomSheet;
        abstractSearchSelectionDialogBottomSheet.mSearchBox = (MonetSearchBoxView) Utils.findRequiredViewAsType(view, R.id.monet_search_box, "field 'mSearchBox'", MonetSearchBoxView.class);
        abstractSearchSelectionDialogBottomSheet.mRecyclerView = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.abstract_search_selection_dialog_bottom_sheet_items, "field 'mRecyclerView'", RecyclerView.class);
        abstractSearchSelectionDialogBottomSheet.mAddButton = (ImageView) Utils.findRequiredViewAsType(view, R.id.abstract_search_selection_dialog_bottom_sheet_add, "field 'mAddButton'", ImageView.class);
    }

    @Override
    public void unbind() {
        AbstractSearchSelectionDialogBottomSheet target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.mSearchBox = null;
        target.mRecyclerView = null;
        target.mAddButton = null;
    }
}
