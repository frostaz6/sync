package com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.monet.buttons.MonetActionButtons;
import com.resync.app.ui.views.responsive.CustomTextInputEditText;
import com.resync.app.ui.views.responsive.CustomTextInputLayout;

/* JADX INFO: loaded from: classes.dex */
public class AbstractInputDialogBottomSheet_ViewBinding implements Unbinder {
    private AbstractInputDialogBottomSheet target;

    public AbstractInputDialogBottomSheet_ViewBinding(AbstractInputDialogBottomSheet abstractInputDialogBottomSheet, View view) {
        this.target = abstractInputDialogBottomSheet;
        abstractInputDialogBottomSheet.mSectionHeaderView = (SheetHeaderView) Utils.findRequiredViewAsType(view, R.id.abstract_input_dialog_bottom_sheet_header, "field 'mSectionHeaderView'", SheetHeaderView.class);
        abstractInputDialogBottomSheet.mTextInputLayout = (CustomTextInputLayout) Utils.findRequiredViewAsType(view, R.id.abstract_input_dialog_bottom_sheet_input_outlined, "field 'mTextInputLayout'", CustomTextInputLayout.class);
        abstractInputDialogBottomSheet.mTextInputEditText = (CustomTextInputEditText) Utils.findRequiredViewAsType(view, R.id.abstract_input_dialog_bottom_sheet_input, "field 'mTextInputEditText'", CustomTextInputEditText.class);
        abstractInputDialogBottomSheet.mTextInputLayoutSecond = (CustomTextInputLayout) Utils.findRequiredViewAsType(view, R.id.abstract_input_dialog_bottom_sheet_input_outlined_second, "field 'mTextInputLayoutSecond'", CustomTextInputLayout.class);
        abstractInputDialogBottomSheet.mTextInputEditTextSecond = (CustomTextInputEditText) Utils.findRequiredViewAsType(view, R.id.abstract_input_dialog_bottom_sheet_input_second, "field 'mTextInputEditTextSecond'", CustomTextInputEditText.class);
        abstractInputDialogBottomSheet.mMonetActionButtons = (MonetActionButtons) Utils.findRequiredViewAsType(view, R.id.monet_action_buttons, "field 'mMonetActionButtons'", MonetActionButtons.class);
    }

    @Override
    public void unbind() {
        AbstractInputDialogBottomSheet target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.mSectionHeaderView = null;
        target.mTextInputLayout = null;
        target.mTextInputEditText = null;
        target.mTextInputLayoutSecond = null;
        target.mTextInputEditTextSecond = null;
        target.mMonetActionButtons = null;
    }
}
