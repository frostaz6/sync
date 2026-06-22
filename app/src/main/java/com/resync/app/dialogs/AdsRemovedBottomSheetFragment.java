package com.resync.app.dialogs;

import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment;
import okhttp3.internal.http.HttpStatusCodesKt;

/* JADX INFO: renamed from: aa.a */
/* JADX INFO: compiled from: AdsRemovedBottomSheetFragment.java */
/* JADX INFO: loaded from: classes.dex */
public class AdsRemovedBottomSheetFragment extends AbstractMemeBottomSheetFragment {
    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public int getImageHeight() {
        return 211;
    }

    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public int getImageWidth() {
        return HttpStatusCodesKt.HTTP_INTERNAL_SERVER_ERROR;
    }

    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public String getImageUrl() {
        return "https://i.imgur.com/6WWiqsW.gif";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment
    public String getPositiveButtonText() {
        return "Dismiss";
    }

    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public String getMemeTitle() {
        return "Ads removed";
    }
}
