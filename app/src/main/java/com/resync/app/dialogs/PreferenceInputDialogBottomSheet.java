package com.resync.app.dialogs;

import android.os.Bundle;
import androidx.preference.DialogPreference;
import androidx.preference.ListPreference;
import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet;
import java.util.Iterator;

/* JADX INFO: renamed from: t9.k1 */
/* JADX INFO: compiled from: PreferenceInputDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class PreferenceInputDialogBottomSheet extends AbstractRadioSelectionDialogBottomSheet {
    private String key;
    protected DialogPreference.TargetFragment targetFragment;
    protected ListPreference listPreference;
    protected CharSequence summary;
    protected int selectedIndex;
    protected CharSequence[] entries;
    protected CharSequence[] entryValues;

    public static PreferenceInputDialogBottomSheet newInstance(String str) {
        PreferenceInputDialogBottomSheet preferenceInputDialogBottomSheet = new PreferenceInputDialogBottomSheet();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", str);
        preferenceInputDialogBottomSheet.setArguments(bundle);
        return preferenceInputDialogBottomSheet;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet
    public void setupItems() {
        getView().findViewById(R.id.abstract_radio_selection_dialog_bottom_sheet_buttons).setVisibility(8);
        getView().findViewById(R.id.abstract_radio_selection_dialog_bottom_sheet_items).setPadding(0, 0, 0, 0);
        int i = 0;
        while (true) {
            CharSequence[] charSequenceArr = this.entries;
            if (i >= charSequenceArr.length) {
                return;
            }
            addItem(new AbstractRadioSelectionDialogBottomSheet.RadioItem(charSequenceArr[i].toString(), i == this.selectedIndex));
            i++;
        }
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return null;
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractRadioSelectionDialogBottomSheet.RadioItem item) {
        Iterator<AbstractRadioSelectionDialogBottomSheet.RadioItem> it = getItems().iterator();
        while (it.hasNext()) {
            it.next().setChecked(false);
        }
        item.setChecked(true);
        notifyItemsChanged();
        int index = indexOf(item);
        this.selectedIndex = index;
        String string = this.entryValues[index].toString();
        if (this.listPreference.callChangeListener(string)) {
            this.listPreference.setValue(string);
        }
        dismiss();
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.key = getArguments().getString("key");
        DialogPreference.TargetFragment parent = (DialogPreference.TargetFragment) getParentFragment();
        this.targetFragment = parent;
        ListPreference listPreference = (ListPreference) parent.findPreference(this.key);
        this.listPreference = listPreference;
        this.summary = listPreference.getSummary();
        ListPreference listPreference2 = this.listPreference;
        this.selectedIndex = listPreference2.findIndexOfValue(listPreference2.getValue());
        this.entries = this.listPreference.getEntries();
        this.entryValues = this.listPreference.getEntryValues();
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public String getTitle() {
        return this.summary.toString();
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public void onNegativeButtonClicked() {
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet
    public String getNegativeButtonText() {
        return null;
    }
}
