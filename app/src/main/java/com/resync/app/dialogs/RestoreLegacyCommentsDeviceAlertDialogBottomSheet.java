package com.resync.app.dialogs;

import android.content.Intent;
import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.HomeActivity;
import com.resync.app.helpers.ui.StaticToast;

/* JADX INFO: renamed from: z9.b */
/* JADX INFO: compiled from: RestoreLegacyCommentsDeviceAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class RestoreLegacyCommentsDeviceAlertDialogBottomSheet extends AbstractDeviceAlertDialogBottomSheet {
    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet
    public int getDeviceIconRes() {
        return R.drawable.outline_comments;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Restore";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        SettingsSingleton.getInstance().putString("comment_padding", "4");
        SettingsSingleton.getInstance().putBoolean("comments_colored_preference", true);
        SettingsSingleton.getInstance().putBoolean("comments_dividers_", true);
        StaticToast.show("Restored");
        Intent intent = new Intent(getActivity(), (Class<?>) HomeActivity.class);
        intent.addFlags(268468224);
        startActivity(intent);
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Restore comments to older style";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return "V22 brings lots of changes to comments but if you prefer the old style we can tweak the UI to look familiar.\n\nRestoring will:\n\t\t- Use smaller padding\n\t\t- Use colorful indents\n\t\t- Show dividers between comments";
    }
}
