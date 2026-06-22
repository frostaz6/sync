package com.resync.app.dialogs;

import android.content.Intent;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.activities.HomeActivity;
import com.resync.app.helpers.ui.StaticToast;

/* JADX INFO: renamed from: z9.c */
/* JADX INFO: compiled from: RestoreLegacyDeviceAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class RestoreLegacyDeviceAlertDialogBottomSheet extends AbstractDeviceAlertDialogBottomSheet {
    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet
    public int getDeviceIconRes() {
        return R.drawable.outline_restore_24;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Restore";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        SettingsSingleton.getInstance().putBoolean("toolbar_expanded", false);
        SettingsSingleton.getInstance().putBoolean("navigation_bottom", false);
        SettingsSingleton.getInstance().putBoolean("use_drawer", true);
        SettingsSingleton.getInstance().putBoolean("awards_modern", false);
        SettingsSingleton.getInstance().putBoolean("awards_minimal", true);
        SettingsSingleton.getInstance().putBoolean("awards_none", false);
        SettingsSingleton.getInstance().putBoolean("flair_modern", false);
        SettingsSingleton.getInstance().putBoolean("flair_minimal", true);
        SettingsSingleton.getInstance().putBoolean("flair_none", false);
        SettingsSingleton.getInstance().putString("comment_padding", "4");
        SettingsSingleton.getInstance().putBoolean("comments_colored_preference", true);
        SettingsSingleton.getInstance().putBoolean("dim_images", true);
        SettingsSingleton.getInstance().putBoolean("comments_dividers_", true);
        SettingsSingleton.getInstance().putString("card_font_title", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE);
        SettingsSingleton.getInstance().putString("card_size_title", "3");
        UiSingleton.getInstance().setUiMode(4);
        StaticToast.show("Restored");
        Intent intent = new Intent(getActivity(), (Class<?>) HomeActivity.class);
        intent.addFlags(268468224);
        startActivity(intent);
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Restore entire app to older style";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return "V22 brings lots of changes but if you prefer the old style we can tweak the UI to look familiar.\n\nRestoring will:\n\t\t- Use a smaller toolbar\n\t\t- Use the drawer for subreddit navigation\n\t\t- Disable bottom navigation\n\t\t- Use legacy award styling\n\t\t- Use legacy flair styling.";
    }
}
