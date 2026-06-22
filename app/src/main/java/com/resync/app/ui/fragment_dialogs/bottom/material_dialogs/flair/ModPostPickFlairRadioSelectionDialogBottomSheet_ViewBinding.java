package com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.flair;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.monet.buttons.MonetActionButtons;
import com.resync.app.ui.views.responsive.CustomProgressBar;

/* JADX INFO: loaded from: classes.dex */
public class ModPostPickFlairRadioSelectionDialogBottomSheet_ViewBinding implements Unbinder {
    private ModPostPickFlairRadioSelectionDialogBottomSheet target;

    public ModPostPickFlairRadioSelectionDialogBottomSheet_ViewBinding(ModPostPickFlairRadioSelectionDialogBottomSheet modPostPickFlairRadioSelectionDialogBottomSheet, View view) {
        this.target = modPostPickFlairRadioSelectionDialogBottomSheet;
        modPostPickFlairRadioSelectionDialogBottomSheet.mSectionHeader = (SheetHeaderView) Utils.findRequiredViewAsType(view, R.id.abstract_radio_selection_dialog_bottom_sheet_header, "field 'mSectionHeader'", SheetHeaderView.class);
        modPostPickFlairRadioSelectionDialogBottomSheet.mAsyncRootWrapper = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.abstract_radio_selection_dialog_bottom_sheet_root_wrapper, "field 'mAsyncRootWrapper'", RelativeLayout.class);
        modPostPickFlairRadioSelectionDialogBottomSheet.mProgressBar = (CustomProgressBar) Utils.findRequiredViewAsType(view, R.id.abstract_radio_selection_dialog_bottom_sheet_async_progress_bar, "field 'mProgressBar'", CustomProgressBar.class);
        modPostPickFlairRadioSelectionDialogBottomSheet.mAsyncWrapper = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.abstract_radio_selection_dialog_bottom_sheet_async_wrapper, "field 'mAsyncWrapper'", ViewGroup.class);
        modPostPickFlairRadioSelectionDialogBottomSheet.mMainWrapper = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.abstract_radio_selection_dialog_bottom_sheet_main_wrapper, "field 'mMainWrapper'", ViewGroup.class);
        modPostPickFlairRadioSelectionDialogBottomSheet.mRecyclerView = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.abstract_radio_selection_dialog_bottom_sheet_items, "field 'mRecyclerView'", RecyclerView.class);
        modPostPickFlairRadioSelectionDialogBottomSheet.mButtonsWrapper = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.abstract_radio_selection_dialog_bottom_sheet_buttons, "field 'mButtonsWrapper'", ViewGroup.class);
        modPostPickFlairRadioSelectionDialogBottomSheet.mMonetActionButtons = (MonetActionButtons) Utils.findRequiredViewAsType(view, R.id.monet_action_buttons, "field 'mMonetActionButtons'", MonetActionButtons.class);
    }

    @Override
    public void unbind() {
        ModPostPickFlairRadioSelectionDialogBottomSheet target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.mSectionHeader = null;
        target.mAsyncRootWrapper = null;
        target.mProgressBar = null;
        target.mAsyncWrapper = null;
        target.mMainWrapper = null;
        target.mRecyclerView = null;
        target.mButtonsWrapper = null;
        target.mMonetActionButtons = null;
    }
}
