package com.resync.app.dialogs;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import androidx.preference.DialogPreference;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet;
import com.resync.app.ui.preferences.custom.ViewTypePreviewPreference;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: renamed from: ea.a */
/* JADX INFO: compiled from: SizeInputDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class SizeInputDialogBottomSheet extends AbstractRadioSelectionDialogBottomSheet {
    private String key;
    protected DialogPreference.TargetFragment targetFragment;
    protected String[] entries;
    protected String[] entryValues;
    protected int selectedIndex = -1;

    private static int sizeForLabel(String str) {
        if (StringUtils.equalsIgnoreCase("Regular", str)) {
            return SizeHelper.dpToPx(RedditHelper.getBodyTextSize(SettingsSingleton.getSettings().fontSize));
        }
        if (StringUtils.equalsIgnoreCase("Medium", str)) {
            return SizeHelper.dpToPx(RedditHelper.getSmallHeaderTextSize(SettingsSingleton.getSettings().fontSize));
        }
        if (StringUtils.equalsIgnoreCase("Large", str)) {
            return SizeHelper.dpToPx(RedditHelper.getMediumTextSize(SettingsSingleton.getSettings().fontSize));
        }
        if (StringUtils.equalsIgnoreCase("Extra large", str)) {
            return SizeHelper.dpToPx(RedditHelper.getSubtitleTextSize(SettingsSingleton.getSettings().fontSize));
        }
        if (StringUtils.equalsIgnoreCase("Huge", str)) {
            return SizeHelper.dpToPx(RedditHelper.getHeaderTextSize(SettingsSingleton.getSettings().fontSize));
        }
        if (StringUtils.equalsIgnoreCase("Header", str)) {
            return SizeHelper.dpToPx(RedditHelper.getFixedLargeTextSize());
        }
        throw new RuntimeException("Unsupported size");
    }

    private static SpannableString buildSizedSpan(String str, int size) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new AbsoluteSizeSpan(size), 0, spannableString.length(), 18);
        return spannableString;
    }

    public static SizeInputDialogBottomSheet newInstance(String str) {
        SizeInputDialogBottomSheet sizeInputDialogBottomSheet = new SizeInputDialogBottomSheet();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", str);
        sizeInputDialogBottomSheet.setArguments(bundle);
        return sizeInputDialogBottomSheet;
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
            String str = strArr[i];
            addItem(new AbstractRadioSelectionDialogBottomSheet.RadioItem(buildSizedSpan(str, sizeForLabel(str)), i == this.selectedIndex));
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
        this.entries = getActivity().getResources().getStringArray(R.array.typeface_sizes);
        this.entryValues = getActivity().getResources().getStringArray(R.array.typeface_sizes_values);
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
