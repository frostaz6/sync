package com.resync.app.dialogs;

import android.os.Bundle;
import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;

/* JADX INFO: renamed from: ea.c */
/* JADX INFO: compiled from: TypefacePickerSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class TypefacePickerSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    private String typefaceKey;
    private String sizeKey;
    AbstractSelectionDialogBottomSheet.SelectionItem typefaceItem;
    AbstractSelectionDialogBottomSheet.SelectionItem relativeSizeItem;

    public static TypefacePickerSelectionDialogBottomSheet newInstance(String str, String str2) {
        TypefacePickerSelectionDialogBottomSheet typefacePickerSelectionDialogBottomSheet = new TypefacePickerSelectionDialogBottomSheet();
        Bundle bundle = new Bundle(1);
        bundle.putString("typeface_key", str);
        bundle.putString("size_key", str2);
        typefacePickerSelectionDialogBottomSheet.setArguments(bundle);
        return typefacePickerSelectionDialogBottomSheet;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        this.typefaceItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_format_size_24, "Typeface"));
        this.relativeSizeItem = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_height_24, "Relative size"));
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.typefaceKey = getArguments().getString("typeface_key");
        this.sizeKey = getArguments().getString("size_key");
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Select property";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        if (item.equals(this.typefaceItem)) {
            TypefaceInputDialogBottomSheet typefaceInputDialogBottomSheetI4 = TypefaceInputDialogBottomSheet.newInstance(this.typefaceKey);
            typefaceInputDialogBottomSheetI4.setTargetFragment(getTargetFragment(), 0);
            typefaceInputDialogBottomSheetI4.show(getChildFragmentManager(), "androidx.preference.PreferenceFragment.DIALOG");
        } else if (item.equals(this.relativeSizeItem)) {
            SizeInputDialogBottomSheet sizeInputDialogBottomSheetJ4 = SizeInputDialogBottomSheet.newInstance(this.sizeKey);
            sizeInputDialogBottomSheetJ4.setTargetFragment(getTargetFragment(), 0);
            sizeInputDialogBottomSheetJ4.show(getChildFragmentManager(), "androidx.preference.PreferenceFragment.DIALOG");
        }
        dismissAllowingStateLoss();
    }
}
