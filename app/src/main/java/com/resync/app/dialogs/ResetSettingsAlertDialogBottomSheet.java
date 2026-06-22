package com.resync.app.dialogs;

import android.content.SharedPreferences;
import android.os.Bundle;
import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet;
import com.resync.app.receiver.MonetThemeReceiver;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.MonetUtil;
import java.util.ArrayList;
import java.util.Iterator;
import com.resync.app.events.SettingsChangedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.helpers.PreferencesHelper;

/* JADX INFO: compiled from: ResetSettingsAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ResetSettingsAlertDialogBottomSheet extends AbstractDeviceAlertDialogBottomSheet {
    ArrayList<String> keys;

    public static Bundle buildArguments(ArrayList<String> arrayList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("keys", arrayList);
        return bundle;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet
    public int getDeviceIconRes() {
        return R.drawable.cog_refresh_outline;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Reset";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        SharedPreferences.Editor editorEdit = PreferencesHelper.getAccountPrefs("ResetSettingsAlertDialogBottomSheet").edit();
        Iterator<String> it = this.keys.iterator();
        while (it.hasNext()) {
            editorEdit.remove(it.next());
        }
        editorEdit.apply();
        SettingsSingleton.getInstance().reload();
        BusSingleton.getBus().post(new SettingsChangedEvent());
        MonetUtil.reload();
        MonetThemeReceiver.broadcast();
        dismiss();
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.keys = (ArrayList) getArguments().getSerializable("keys");
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Reset to default";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return null;
    }
}
