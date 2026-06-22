package com.resync.app.dialogs;

import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.FavouriteSubredditManager;

/* JADX INFO: renamed from: t9.r2 */
/* JADX INFO: compiled from: ToggleFavAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ToggleFavAlertDialogBottomSheet extends AbstractDeviceAlertDialogBottomSheet {
    private boolean isFavourite() {
        return FavouriteSubredditManager.getInstance().isFavourite(getSubreddit());
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractDeviceAlertDialogBottomSheet
    public int getDeviceIconRes() {
        return R.drawable.outline_bookmark_border_24;
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return !isFavourite() ? "Favorite" : "Unfavorite";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        FavouriteSubredditManager.getInstance().toggleFavourite(getSubreddit());
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(!isFavourite() ? "Favorite" : "Unfavorite");
        sb2.append(StringUtils.SPACE);
        sb2.append(getSubreddit());
        sb2.append("?");
        return sb2.toString();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public String getMessage() {
        return null;
    }
}
