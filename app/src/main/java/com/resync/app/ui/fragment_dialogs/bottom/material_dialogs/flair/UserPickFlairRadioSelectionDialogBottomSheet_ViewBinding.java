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
public class UserPickFlairRadioSelectionDialogBottomSheet_ViewBinding implements Unbinder {
    private UserPickFlairRadioSelectionDialogBottomSheet target;

    public UserPickFlairRadioSelectionDialogBottomSheet_ViewBinding(UserPickFlairRadioSelectionDialogBottomSheet userPickFlairRadioSelectionDialogBottomSheet, View view) {
        this.target = userPickFlairRadioSelectionDialogBottomSheet;
        userPickFlairRadioSelectionDialogBottomSheet.mSectionHeader = (SheetHeaderView) Utils.findRequiredViewAsType(view, R.id.abstract_radio_selection_dialog_bottom_sheet_header, "field 'mSectionHeader'", SheetHeaderView.class);
        userPickFlairRadioSelectionDialogBottomSheet.mAsyncRootWrapper = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.abstract_radio_selection_dialog_bottom_sheet_root_wrapper, "field 'mAsyncRootWrapper'", RelativeLayout.class);
        userPickFlairRadioSelectionDialogBottomSheet.mProgressBar = (CustomProgressBar) Utils.findRequiredViewAsType(view, R.id.abstract_radio_selection_dialog_bottom_sheet_async_progress_bar, "field 'mProgressBar'", CustomProgressBar.class);
        userPickFlairRadioSelectionDialogBottomSheet.mAsyncWrapper = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.abstract_radio_selection_dialog_bottom_sheet_async_wrapper, "field 'mAsyncWrapper'", ViewGroup.class);
        userPickFlairRadioSelectionDialogBottomSheet.mMainWrapper = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.abstract_radio_selection_dialog_bottom_sheet_main_wrapper, "field 'mMainWrapper'", ViewGroup.class);
        userPickFlairRadioSelectionDialogBottomSheet.mRecyclerView = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.abstract_radio_selection_dialog_bottom_sheet_items, "field 'mRecyclerView'", RecyclerView.class);
        userPickFlairRadioSelectionDialogBottomSheet.mButtonsWrapper = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.abstract_radio_selection_dialog_bottom_sheet_buttons, "field 'mButtonsWrapper'", ViewGroup.class);
        userPickFlairRadioSelectionDialogBottomSheet.mMonetActionButtons = (MonetActionButtons) Utils.findRequiredViewAsType(view, R.id.monet_action_buttons, "field 'mMonetActionButtons'", MonetActionButtons.class);
    }

    @Override
    public void unbind() {
        UserPickFlairRadioSelectionDialogBottomSheet target = this.target;
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
