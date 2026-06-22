package com.resync.app.dialogs;

import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment;

/* JADX INFO: renamed from: aa.f */
/* JADX INFO: compiled from: RemovedBottomSheetFragment.java */
/* JADX INFO: loaded from: classes.dex */
public class RemovedBottomSheetFragment extends AbstractMemeBottomSheetFragment {
    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public int getImageHeight() {
        return 217;
    }

    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public int getImageWidth() {
        return 245;
    }

    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public String getImageUrl() {
        return "https://i.imgur.com/kNUK5EW.gif";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment
    public String getPositiveButtonText() {
        return "Dismiss";
    }

    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public String getMemeTitle() {
        return "👀";
    }
}
