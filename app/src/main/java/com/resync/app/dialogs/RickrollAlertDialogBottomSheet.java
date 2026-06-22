package com.resync.app.dialogs;

import android.content.Intent;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.CustomYouTubeVideoActivity;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.util.links.LinkHandler;

/* JADX INFO: renamed from: t9.i2 */
/* JADX INFO: compiled from: RickrollAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class RickrollAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Open";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        if (SettingsSingleton.getInstance().getCurrentSettings().embedYouTube) {
            Intent intent = new Intent(getActivity(), (Class<?>) CustomYouTubeVideoActivity.class);
            intent.putExtra("url", "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
            getActivity().startActivity(intent);
        } else {
            LinkHandler.openExternal(getActivity(), "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        }
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Looks like someone is trying to Rickroll you...";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return null;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getNegativeButtonText() {
        return "Close";
    }
}
