package com.resync.app.dialogs;

import android.content.ComponentName;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.workers.RestartWorker;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.IconHelper;

/* JADX INFO: renamed from: t9.s0 */
/* JADX INFO: compiled from: IconPickerSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class IconPickerSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    AbstractSelectionDialogBottomSheet.ImageSelectionItem defaultIcon;
    AbstractSelectionDialogBottomSheet.ImageSelectionItem defaultDarkIcon;
    AbstractSelectionDialogBottomSheet.ImageSelectionItem legacyIcon;
    AbstractSelectionDialogBottomSheet.ImageSelectionItem fancyIcon;
    AbstractSelectionDialogBottomSheet.ImageSelectionItem prideIcon;
    AbstractSelectionDialogBottomSheet.ImageSelectionItem blmIcon;

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        // ReSync: Default/Default-dark previews bundled locally (was the dead todo.syncforreddit.com).
        this.defaultIcon = (AbstractSelectionDialogBottomSheet.ImageSelectionItem) addItem(new AbstractSelectionDialogBottomSheet.ImageSelectionItem("Default", "file:///android_asset/iconpreview/default.png"));
        this.defaultDarkIcon = (AbstractSelectionDialogBottomSheet.ImageSelectionItem) addItem(new AbstractSelectionDialogBottomSheet.ImageSelectionItem("Default dark", "file:///android_asset/iconpreview/default_dark.png"));
        // ReSync: icon-picker previews bundled locally (was the original dev's GitHub repo).
        this.legacyIcon = (AbstractSelectionDialogBottomSheet.ImageSelectionItem) addItem(new AbstractSelectionDialogBottomSheet.ImageSelectionItem("Legacy", "file:///android_asset/iconpreview/legacy.png"));
        this.fancyIcon = (AbstractSelectionDialogBottomSheet.ImageSelectionItem) addItem(new AbstractSelectionDialogBottomSheet.ImageSelectionItem("Fancy", "file:///android_asset/iconpreview/fancy.png"));
        this.prideIcon = (AbstractSelectionDialogBottomSheet.ImageSelectionItem) addItem(new AbstractSelectionDialogBottomSheet.ImageSelectionItem("Pride", "file:///android_asset/iconpreview/pride.png"));
        this.blmIcon = (AbstractSelectionDialogBottomSheet.ImageSelectionItem) addItem(new AbstractSelectionDialogBottomSheet.ImageSelectionItem("BLM", "file:///android_asset/iconpreview/blm.png"));
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Select an icon";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        if (item == this.defaultIcon) {
            IconHelper.setIcon(0);
        } else if (item == this.legacyIcon) {
            IconHelper.setIcon(1);
        } else if (item == this.fancyIcon) {
            IconHelper.setIcon(2);
        } else if (item == this.prideIcon) {
            IconHelper.setIcon(3);
        } else if (item == this.blmIcon) {
            IconHelper.setIcon(4);
        } else if (item == this.defaultDarkIcon) {
            IconHelper.setIcon(5);
        }
        getActivity().getPackageManager().setComponentEnabledSetting(new ComponentName(getActivity(), "com.resync.app.ui.activities.HomeActivity.Default"), item.equals(this.defaultIcon) ? 1 : 2, 1);
        getActivity().getPackageManager().setComponentEnabledSetting(new ComponentName(getActivity(), "com.resync.app.ui.activities.HomeActivity.DefaultDark"), item.equals(this.defaultDarkIcon) ? 1 : 2, 1);
        getActivity().getPackageManager().setComponentEnabledSetting(new ComponentName(getActivity(), "com.resync.app.ui.activities.HomeActivity.Legacy"), item.equals(this.legacyIcon) ? 1 : 2, 1);
        getActivity().getPackageManager().setComponentEnabledSetting(new ComponentName(getActivity(), "com.resync.app.ui.activities.HomeActivity.Fancy"), item.equals(this.fancyIcon) ? 1 : 2, 1);
        getActivity().getPackageManager().setComponentEnabledSetting(new ComponentName(getActivity(), "com.resync.app.ui.activities.HomeActivity.Pride"), item.equals(this.prideIcon) ? 1 : 2, 1);
        getActivity().getPackageManager().setComponentEnabledSetting(new ComponentName(getActivity(), "com.resync.app.ui.activities.HomeActivity.Blm"), item.equals(this.blmIcon) ? 1 : 2, 1);
        StaticToast.show("Changing icon...");
        RestartWorker.enqueue(getActivity());
        getActivity().finishAffinity();
    }
}
