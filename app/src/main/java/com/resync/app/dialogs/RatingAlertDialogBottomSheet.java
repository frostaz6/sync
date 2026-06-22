package com.resync.app.dialogs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.util.links.LinkHandler;

/* JADX INFO: renamed from: t9.o1 */
/* JADX INFO: compiled from: RatingAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class RatingAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    public static void openRatingPage(Context context) {
        if (context == null) {
            return;
        }
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.resync.app")));
        } catch (Exception unused) {
            LinkHandler.openExternal(context, "https://play.google.com/store/apps/details?id=com.resync.app");
        }
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Rate";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        SharedPreferences.Editor editorEdit = PreferencesHelper.getDefaultPrefs("RatingsHelper").edit();
        editorEdit.putBoolean("sync_checked", true);
        editorEdit.apply();
        openRatingPage(getActivity());
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Thank you for using Sync for reddit";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return "If you enjoy using Sync for reddit, would you mind taking a moment to rate it?";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getNegativeButtonText() {
        return "Not now";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public final String getNeutralButtonText() {
        return "Never";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public void onNeutralAction() {
        SharedPreferences.Editor editorEdit = PreferencesHelper.getDefaultPrefs("RatingsHelper").edit();
        editorEdit.putBoolean("sync_checked", true);
        editorEdit.apply();
        dismiss();
    }
}
