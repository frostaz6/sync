package com.resync.app.dialogs;

import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment;

/* JADX INFO: renamed from: aa.h */
/* JADX INFO: compiled from: UnknownBottomSheetFragment.java */
/* JADX INFO: loaded from: classes.dex */
public class UnknownBottomSheetFragment extends AbstractMemeBottomSheetFragment {
    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public int getImageHeight() {
        return 365;
    }

    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public int getImageWidth() {
        return 498;
    }

    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public String getImageUrl() {
        return "https://i.imgur.com/8sJ3QN2.gif";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment
    public String getPositiveButtonText() {
        return "Dismiss";
    }

    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public String getMemeTitle() {
        return "Unknown";
    }
}
