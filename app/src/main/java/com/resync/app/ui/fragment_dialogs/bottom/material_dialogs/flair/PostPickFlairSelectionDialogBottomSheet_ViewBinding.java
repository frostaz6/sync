package com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.flair;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.responsive.CustomProgressBar;

/* JADX INFO: loaded from: classes.dex */
public class PostPickFlairSelectionDialogBottomSheet_ViewBinding implements Unbinder {
    private PostPickFlairSelectionDialogBottomSheet target;

    public PostPickFlairSelectionDialogBottomSheet_ViewBinding(PostPickFlairSelectionDialogBottomSheet postPickFlairSelectionDialogBottomSheet, View view) {
        this.target = postPickFlairSelectionDialogBottomSheet;
        postPickFlairSelectionDialogBottomSheet.mAsyncRootWrapper = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.abstract_selection_dialog_bottom_sheet_root_wrapper, "field 'mAsyncRootWrapper'", RelativeLayout.class);
        postPickFlairSelectionDialogBottomSheet.mProgressBar = (CustomProgressBar) Utils.findRequiredViewAsType(view, R.id.abstract_selection_dialog_bottom_sheet_async_progress_bar, "field 'mProgressBar'", CustomProgressBar.class);
        postPickFlairSelectionDialogBottomSheet.mAsyncWrapper = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.abstract_selection_dialog_bottom_sheet_async_wrapper, "field 'mAsyncWrapper'", RelativeLayout.class);
        postPickFlairSelectionDialogBottomSheet.mMainWrapper = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.abstract_selection_dialog_bottom_sheet_main_wrapper, "field 'mMainWrapper'", ViewGroup.class);
        postPickFlairSelectionDialogBottomSheet.mSectionHeader = (SheetHeaderView) Utils.findRequiredViewAsType(view, R.id.abstract_selection_dialog_bottom_sheet_header, "field 'mSectionHeader'", SheetHeaderView.class);
        postPickFlairSelectionDialogBottomSheet.mRecyclerView = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.abstract_selection_dialog_bottom_sheet_items, "field 'mRecyclerView'", RecyclerView.class);
    }

    @Override
    public void unbind() {
        PostPickFlairSelectionDialogBottomSheet target = this.target;
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
    }
}
