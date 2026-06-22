package com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.resync.app.R;

/* JADX INFO: loaded from: classes.dex */
public class AbstractProgressDialogBottomSheet_ViewBinding implements Unbinder {
    private AbstractProgressDialogBottomSheet target;

    public AbstractProgressDialogBottomSheet_ViewBinding(AbstractProgressDialogBottomSheet abstractProgressDialogBottomSheet, View view) {
        this.target = abstractProgressDialogBottomSheet;
        abstractProgressDialogBottomSheet.mProgressbar = (CircularProgressIndicator) Utils.findRequiredViewAsType(view, R.id.abstract_radio_selection_dialog_bottom_sheet_progress_bar, "field 'mProgressbar'", CircularProgressIndicator.class);
    }

    @Override
    public void unbind() {
        AbstractProgressDialogBottomSheet target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.mProgressbar = null;
    }
}
