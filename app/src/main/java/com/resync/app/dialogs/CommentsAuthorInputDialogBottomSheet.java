package com.resync.app.dialogs;

import android.os.Bundle;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet;
import java.util.ArrayList;
import com.resync.app.events.CommentNavigationSearchUserUpdatedEvent;
import com.resync.app.singletons.BusSingleton;

/* JADX INFO: renamed from: t9.v */
/* JADX INFO: compiled from: CommentsAuthorInputDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class CommentsAuthorInputDialogBottomSheet extends AbstractInputDialogBottomSheet {
    ArrayList<String> authors;

    public static Bundle buildArguments(ArrayList<String> arrayList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("authors", arrayList);
        return bundle;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public void onPositiveButtonClicked(String str) {
        BusSingleton.getBus().post(new CommentNavigationSearchUserUpdatedEvent(str));
        dismiss();
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Search";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getHint() {
        return "Username";
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.authors = (ArrayList) getArguments().getSerializable("authors");
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getTitle() {
        return "Search user";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public String[] getAutocompleteOptions() {
        ArrayList<String> arrayList = this.authors;
        if (arrayList == null || arrayList.size() <= 0) {
            return null;
        }
        String[] strArr = new String[this.authors.size()];
        this.authors.toArray(strArr);
        return strArr;
    }
}
