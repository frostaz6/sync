package com.resync.app.dialogs.interfaces;

import android.view.View;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSearchSelectionDialogBottomSheet;

/* JADX INFO: renamed from: v9.e */
/* JADX INFO: compiled from: SearchSelectionDialogBottomSheetInterface.java */
/* JADX INFO: loaded from: classes.dex */
public interface SearchSelectionDialogBottomSheetInterface {
    void onAddClicked(View view);

    String getSearchHint();

    void onItemClicked(AbstractSearchSelectionDialogBottomSheet.SearchItem searchItem);

    void onSearchTextChanged(CharSequence charSequence);

    void onSearchClicked(View view);

    void onItemMoreClicked(View view, AbstractSearchSelectionDialogBottomSheet.SearchItem searchItem);
}
