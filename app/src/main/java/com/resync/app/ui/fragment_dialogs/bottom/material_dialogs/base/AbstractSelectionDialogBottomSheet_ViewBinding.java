package com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.monet.buttons.MonetActionButtons;
import com.resync.app.ui.views.responsive.CustomProgressBar;

/* JADX INFO: loaded from: classes.dex */
public class AbstractSelectionDialogBottomSheet_ViewBinding implements Unbinder {
    private AbstractSelectionDialogBottomSheet target;

    public AbstractSelectionDialogBottomSheet_ViewBinding(AbstractSelectionDialogBottomSheet abstractSelectionDialogBottomSheet, View view) {
        this.target = abstractSelectionDialogBottomSheet;
        abstractSelectionDialogBottomSheet.mAsyncRootWrapper = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.abstract_selection_dialog_bottom_sheet_root_wrapper, "field 'mAsyncRootWrapper'", RelativeLayout.class);
        abstractSelectionDialogBottomSheet.mProgressBar = (CustomProgressBar) Utils.findRequiredViewAsType(view, R.id.abstract_selection_dialog_bottom_sheet_async_progress_bar, "field 'mProgressBar'", CustomProgressBar.class);
        abstractSelectionDialogBottomSheet.mAsyncWrapper = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.abstract_selection_dialog_bottom_sheet_async_wrapper, "field 'mAsyncWrapper'", RelativeLayout.class);
        abstractSelectionDialogBottomSheet.mMainWrapper = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.abstract_selection_dialog_bottom_sheet_main_wrapper, "field 'mMainWrapper'", LinearLayout.class);
        abstractSelectionDialogBottomSheet.mSectionHeader = (SheetHeaderView) Utils.findRequiredViewAsType(view, R.id.abstract_selection_dialog_bottom_sheet_header, "field 'mSectionHeader'", SheetHeaderView.class);
        abstractSelectionDialogBottomSheet.mRecyclerView = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.abstract_selection_dialog_bottom_sheet_items, "field 'mRecyclerView'", RecyclerView.class);
        abstractSelectionDialogBottomSheet.mBottomButtons = Utils.findRequiredView(view, R.id.abstract_selection_dialog_bottom_sheet_buttons, "field 'mBottomButtons'");
        abstractSelectionDialogBottomSheet.mMonetActionButtons = (MonetActionButtons) Utils.findRequiredViewAsType(view, R.id.monet_action_buttons, "field 'mMonetActionButtons'", MonetActionButtons.class);
    }

    @Override
    public void unbind() {
        AbstractSelectionDialogBottomSheet target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.mAsyncRootWrapper = null;
        target.mProgressBar = null;
        target.mAsyncWrapper = null;
        target.mMainWrapper = null;
        target.mSectionHeader = null;
        target.mRecyclerView = null;
        target.mMonetActionButtons = null;
    }
}
