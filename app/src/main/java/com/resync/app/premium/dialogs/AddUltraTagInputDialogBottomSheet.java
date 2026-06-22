package com.resync.app.premium.dialogs;

import android.os.Bundle;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.NewUserTagManager;
import com.resync.app.managers.UserIconManager;

/* JADX INFO: renamed from: ka.a */
/* JADX INFO: compiled from: AddUltraTagInputDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class AddUltraTagInputDialogBottomSheet extends AbstractInputDialogBottomSheet {
    private String argUsername;
    private String argTag;

    public static Bundle createArgs(String str, String str2) {
        Bundle bundle = new Bundle();
        if (StringUtils.isNotEmpty(str)) {
            bundle.putString("username", str);
        }
        if (StringUtils.isNotEmpty(str2)) {
            bundle.putString("tag", str2);
        }
        return bundle;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public void onInputSetup() {
        if (StringUtils.isNotEmpty(this.argUsername)) {
            this.mTextInputEditText.setText(this.argUsername);
            this.mTextInputLayoutSecond.requestFocus();
        }
        if (StringUtils.isNotEmpty(this.argTag)) {
            this.mTextInputEditTextSecond.setText(this.argTag);
            this.mTextInputLayoutSecond.requestFocus();
        }
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public void onPositiveButtonClicked(String str, String str2) {
        dismissAllowingStateLoss();
        NewUserTagManager.getInstance().addTag(AccountSingleton.getInstance().getUsername(), str, str2);
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public boolean isDoubleInput() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Add";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getHint() {
        return "Username";
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.argUsername = getArguments().getString("username");
        this.argTag = getArguments().getString("tag");
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getTitle() {
        return StringUtils.isNotEmpty(this.argTag) ? "Edit user tag" : "Add user tag";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public String[] getAutocompleteOptions() {
        return UserIconManager.getInstance().getUsernames();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public String getSecondHint() {
        return "Tag";
    }
}
