package com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.resync.app.R;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.dialogs.CustomBottomSheetDialogFragment;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractProgressDialogBottomSheet extends CustomBottomSheetDialogFragment {

    @BindView(R.id.abstract_radio_selection_dialog_bottom_sheet_progress_bar)
    CircularProgressIndicator mProgressbar;

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isInitiallyExpanded() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.abstract_progress_dialog_bottom_sheet;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        if (isIndeterminate()) {
            this.mProgressbar.setIndeterminate(true);
            this.mProgressbar.getIndeterminateDrawable().setColorFilter(MonetUtil.getProgressColorAuto(), PorterDuff.Mode.SRC_IN);
        } else {
            this.mProgressbar.setIndeterminate(false);
            this.mProgressbar.getProgressDrawable().setColorFilter(MonetUtil.getProgressColorAuto(), PorterDuff.Mode.SRC_IN);
        }
        onProgressReady();
    }

    public CircularProgressIndicator getProgressIndicator() {
        return this.mProgressbar;
    }

    public boolean isIndeterminate() {
        return true;
    }

    public void onProgressReady() {
    }
}
