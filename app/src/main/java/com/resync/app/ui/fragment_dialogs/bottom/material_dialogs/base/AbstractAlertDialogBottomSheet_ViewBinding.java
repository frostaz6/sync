package com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.monet.buttons.MonetActionButtons;
import com.resync.app.ui.views.responsive.CustomProgressBar;

/* JADX INFO: loaded from: classes.dex */
public class AbstractAlertDialogBottomSheet_ViewBinding implements Unbinder {
    private AbstractAlertDialogBottomSheet target;

    public AbstractAlertDialogBottomSheet_ViewBinding(AbstractAlertDialogBottomSheet abstractAlertDialogBottomSheet, View view) {
        this.target = abstractAlertDialogBottomSheet;
        abstractAlertDialogBottomSheet.mAsyncWrapper = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.abstract_alert_dialog_bottom_sheet_async_wrapper, "field 'mAsyncWrapper'", ViewGroup.class);
        abstractAlertDialogBottomSheet.mMainWrapper = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.abstract_alert_dialog_bottom_sheet_main_wrapper, "field 'mMainWrapper'", ViewGroup.class);
        abstractAlertDialogBottomSheet.mAsyncRootWrapper = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.abstract_alert_dialog_bottom_sheet_root_wrapper, "field 'mAsyncRootWrapper'", RelativeLayout.class);
        abstractAlertDialogBottomSheet.mProgressBar = (CustomProgressBar) Utils.findRequiredViewAsType(view, R.id.abstract_alert_dialog_bottom_sheet_async_progress_bar, "field 'mProgressBar'", CustomProgressBar.class);
        abstractAlertDialogBottomSheet.mSectionHeaderView = (SheetHeaderView) Utils.findRequiredViewAsType(view, R.id.abstract_alert_dialog_bottom_sheet_header, "field 'mSectionHeaderView'", SheetHeaderView.class);
        abstractAlertDialogBottomSheet.mContent = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.abstract_alert_dialog_bottom_sheet_content, "field 'mContent'", LinearLayout.class);
        abstractAlertDialogBottomSheet.mMessageTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.abstract_alert_dialog_bottom_sheet_message, "field 'mMessageTextView'", TextView.class);
        abstractAlertDialogBottomSheet.mMonetActionButtons = (MonetActionButtons) Utils.findRequiredViewAsType(view, R.id.monet_action_buttons, "field 'mMonetActionButtons'", MonetActionButtons.class);
    }

    @Override
    public void unbind() {
        AbstractAlertDialogBottomSheet target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.mAsyncWrapper = null;
        target.mMainWrapper = null;
        target.mAsyncRootWrapper = null;
        target.mProgressBar = null;
        target.mSectionHeaderView = null;
        target.mContent = null;
        target.mMessageTextView = null;
        target.mMonetActionButtons = null;
    }
}
