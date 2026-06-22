package com.resync.app.dialogs;

import android.view.View;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment;
import com.resync.app.util.links.LinkHelper;

/* JADX INFO: renamed from: aa.c */
/* JADX INFO: compiled from: ClosingBottomSheetFragment.java */
/* JADX INFO: loaded from: classes.dex */
public class ClosingBottomSheetFragment extends AbstractMemeBottomSheetFragment {
    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public int getImageHeight() {
        return 580;
    }

    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public int getImageWidth() {
        return 1024;
    }

    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public String getImageUrl() {
        return "https://i.imgur.com/PqVcLN9.png";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment
    public String getPositiveButtonText() {
        return "Signup to Sync for Lemmy";
    }

    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public String getMemeTitle() {
        return null;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment
    public String getNegativeButtonText() {
        return null;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment
    public String getBodyText() {
        return "Dear Sync users,\n\nUnfortunately this will be the last update. Due to API changes Sync for Reddit will shutdown on June 30, 2023 and will no longer function.\n\nBut this isn't goodbye forever! I'm currently working on Sync for Lemmy to bring the same Sync look and feature set to Lemmy. I hope you will join us in this exciting new chapter.\n\nIt's been an honour and a privilege. Thank you all,\nLj";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment
    public boolean onProgressReady() {
        return true;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment
    public void onPositiveButtonClicked(View view) {
        LinkHelper.open(getContext(), "https://syncapps.io");
        super.onPositiveButtonClicked(view);
    }
}
