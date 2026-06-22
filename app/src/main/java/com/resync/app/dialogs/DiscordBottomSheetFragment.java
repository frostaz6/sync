package com.resync.app.dialogs;

import android.view.View;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment;
import com.resync.app.util.links.LinkHelper;

/* JADX INFO: renamed from: aa.d */
/* JADX INFO: compiled from: DiscordBottomSheetFragment.java */
/* JADX INFO: loaded from: classes.dex */
public class DiscordBottomSheetFragment extends AbstractMemeBottomSheetFragment {
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
        return "https://i.imgur.com/JxTd1iQ.png";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment
    public String getPositiveButtonText() {
        return "Join";
    }

    @Override // com.resync.app.dialogs.interfaces.MemeDialogBottomSheetInterface
    public String getMemeTitle() {
        return null;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment
    public String getNegativeButtonText() {
        return "Dismiss";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment
    public String getBodyText() {
        return "Hi there Sync free user!\n\nEver wanted a certain feature to make it into Sync? Or maybe you have an issue to report.\n\nJoin the Sync Discord and come chat with fellow Sync users and the developer!";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment
    public boolean onProgressReady() {
        return true;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractMemeBottomSheetFragment
    public void onPositiveButtonClicked(View view) {
        LinkHelper.open(getContext(), "https://discord.gg/Se8BaAm5Vp");
        super.onPositiveButtonClicked(view);
    }
}
