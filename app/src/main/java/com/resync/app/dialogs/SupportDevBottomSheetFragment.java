package com.resync.app.dialogs;

import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment;
import okhttp3.internal.http.HttpStatusCodesKt;

/* JADX INFO: renamed from: aa.g */
/* JADX INFO: compiled from: SupportDevBottomSheetFragment.java */
/* JADX INFO: loaded from: classes.dex */
public class SupportDevBottomSheetFragment extends AbstractMemeBottomSheetFragment {
    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public int getImageHeight() {
        return HttpStatusCodesKt.HTTP_MULT_CHOICE;
    }

    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public int getImageWidth() {
        return HttpStatusCodesKt.HTTP_INTERNAL_SERVER_ERROR;
    }

    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public String getImageUrl() {
        return "https://i.imgur.com/51RcU1q.gif";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment
    public String getPositiveButtonText() {
        return "Dismiss";
    }

    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public String getMemeTitle() {
        return "Support the dev!";
    }
}
