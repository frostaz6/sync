package com.resync.app.dialogs;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import androidx.preference.DialogPreference;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet;
import com.resync.app.ui.preferences.custom.ViewTypePreviewPreference;
import com.resync.app.ui.util.spans.CustomTypefaceSpan;
import java.util.Iterator;
import com.resync.app.helpers.TypefaceManager;

/* JADX INFO: renamed from: ea.b */
/* JADX INFO: compiled from: TypefaceInputDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class TypefaceInputDialogBottomSheet extends AbstractRadioSelectionDialogBottomSheet {
    private String key;
    protected DialogPreference.TargetFragment targetFragment;
    protected String[] entries;
    protected String[] entryValues;
    protected int selectedIndex = -1;

    private static SpannableString buildTypefaceSpan(String str, Typeface typeface) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new CustomTypefaceSpan(null, typeface), 0, spannableString.length(), 18);
        return spannableString;
    }

    public static TypefaceInputDialogBottomSheet newInstance(String str) {
        TypefaceInputDialogBottomSheet typefaceInputDialogBottomSheet = new TypefaceInputDialogBottomSheet();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", str);
        typefaceInputDialogBottomSheet.setArguments(bundle);
        return typefaceInputDialogBottomSheet;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet
    public void setupItems() {
        getView().findViewById(R.id.abstract_radio_selection_dialog_bottom_sheet_buttons).setVisibility(8);
        getView().findViewById(R.id.abstract_radio_selection_dialog_bottom_sheet_items).setPadding(0, 0, 0, 0);
        int i = 0;
        while (true) {
            String[] strArr = this.entries;
            if (i >= strArr.length) {
                return;
            }
            addItem(new AbstractRadioSelectionDialogBottomSheet.RadioItem(buildTypefaceSpan(strArr[i], TypefaceManager.get(Integer.parseInt(this.entryValues[i]))), i == this.selectedIndex));
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
        SettingsSingleton.getInstance().putString(this.key, this.entryValues[index]);
        SettingsSingleton.reloadTypefaces();
        if (this.targetFragment.findPreference(ViewTypePreviewPreference.KEY) instanceof ViewTypePreviewPreference) {
            ((ViewTypePreviewPreference) this.targetFragment.findPreference(ViewTypePreviewPreference.KEY)).refresh();
        }
        dismissAllowingStateLoss();
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.key = getArguments().getString("key");
        this.targetFragment = (DialogPreference.TargetFragment) getTargetFragment();
        this.entries = getActivity().getResources().getStringArray(R.array.typeface);
        this.entryValues = getActivity().getResources().getStringArray(R.array.typeface_values);
        String string = Integer.toString(SettingsSingleton.getInstance().getIntField(this.key));
        int i = 0;
        while (true) {
            String[] strArr = this.entryValues;
            if (i >= strArr.length) {
                break;
            }
            if (strArr[i].equalsIgnoreCase(string)) {
                this.selectedIndex = i;
            }
            i++;
        }
        if (this.selectedIndex == -1) {
            throw new RuntimeException("Could not find selected value");
        }
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Select typeface";
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public void onNegativeButtonClicked() {
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet
    public String getNegativeButtonText() {
        return null;
    }
}
