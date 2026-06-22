package com.resync.app.dialogs;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import androidx.fragment.app.FragmentTransaction;
import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: w9.a */
/* JADX INFO: compiled from: AbstractLocationSelectionBottomSheetFragment.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractLocationSelectionBottomSheetFragment extends AbstractDeviceAlertDialogBottomSheet {
    private OnCompleteListener completeListener;

    /* JADX INFO: compiled from: AbstractLocationSelectionBottomSheetFragment.java */
    public interface OnCompleteListener {
        void onComplete();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet
    public int getDeviceIconRes() {
        return R.drawable.outline_folder_24;
    }

    protected void dismissAndRemove() {
        if (getDialog() != null) {
            getDialog().dismiss();
        }
        try {
            FragmentTransaction fragmentTransactionM = requireFragmentManager().beginTransaction();
            fragmentTransactionM.remove(this);
            fragmentTransactionM.commit();
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    protected OnCompleteListener getCompleteListener() {
        return this.completeListener;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 101) {
            if (intent == null) {
                StaticToast.show("You must select a directory to continue");
                return;
            }
            Uri data = intent.getData();
            getActivity().getContentResolver().takePersistableUriPermission(data, 3);
            saveLocation(data.toString());
            StaticToast.show("Directory location updated");
            if (getCompleteListener() != null) {
                getCompleteListener().onComplete();
            } else {
                onLocationSaved();
            }
            dismissAndRemove();
        }
    }

    protected void onLocationSaved() {
    }

    protected void saveLocation(String str) {
        throw new RuntimeException("TODO");
    }

    public void setCompleteListener(OnCompleteListener listener) {
        this.completeListener = listener;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        try {
            Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
            intent.putExtra("android.content.extra.SHOW_ADVANCED", true);
            intent.putExtra("android.content.extra.FANCY", true);
            intent.putExtra("android.content.extra.SHOW_FILESIZE", true);
            intent.addFlags(2);
            startActivityForResult(intent, 101);
        } catch (ActivityNotFoundException unused) {
            StaticToast.show("Could not open the file explorer, do you have this disabled?");
        }
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return null;
    }
}
