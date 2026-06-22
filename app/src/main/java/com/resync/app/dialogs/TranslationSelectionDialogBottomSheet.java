package com.resync.app.dialogs;
import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import java.util.ArrayList;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.TranslationHelper;

/* JADX INFO: renamed from: da.e */
/* JADX INFO: compiled from: TranslationSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class TranslationSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    ArrayList<String> languageCodes = new ArrayList<>();

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        this.languageCodes.add("af");
        this.languageCodes.add("sq");
        this.languageCodes.add("ar");
        this.languageCodes.add("be");
        this.languageCodes.add("bg");
        this.languageCodes.add("bn");
        this.languageCodes.add("ca");
        this.languageCodes.add("zh");
        this.languageCodes.add("hr");
        this.languageCodes.add("cs");
        this.languageCodes.add("da");
        this.languageCodes.add("nl");
        this.languageCodes.add("en");
        this.languageCodes.add("eo");
        this.languageCodes.add("et");
        this.languageCodes.add("fi");
        this.languageCodes.add("fr");
        this.languageCodes.add("gl");
        this.languageCodes.add("ka");
        this.languageCodes.add("de");
        this.languageCodes.add("el");
        this.languageCodes.add("gu");
        this.languageCodes.add("ht");
        this.languageCodes.add("he");
        this.languageCodes.add("hi");
        this.languageCodes.add("hu");
        this.languageCodes.add("is");
        this.languageCodes.add("id");
        this.languageCodes.add("ga");
        this.languageCodes.add("it");
        this.languageCodes.add("ja");
        this.languageCodes.add("kn");
        this.languageCodes.add("ko");
        this.languageCodes.add("lt");
        this.languageCodes.add("lv");
        this.languageCodes.add("mk");
        this.languageCodes.add("mr");
        this.languageCodes.add("ms");
        this.languageCodes.add("mt");
        this.languageCodes.add("no");
        this.languageCodes.add("fa");
        this.languageCodes.add("pl");
        this.languageCodes.add("pt");
        this.languageCodes.add("ro");
        this.languageCodes.add("ru");
        this.languageCodes.add("sk");
        this.languageCodes.add("sl");
        this.languageCodes.add("es");
        this.languageCodes.add("sv");
        this.languageCodes.add("sw");
        this.languageCodes.add("tl");
        this.languageCodes.add("ta");
        this.languageCodes.add("te");
        this.languageCodes.add("th");
        this.languageCodes.add("tr");
        this.languageCodes.add("uk");
        this.languageCodes.add("ur");
        this.languageCodes.add("vi");
        this.languageCodes.add("cy");
        for (String str : this.languageCodes) {
            AbstractSelectionDialogBottomSheet.SelectionItem item = new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_language_24, TranslationHelper.getLanguageName(str));
            item.tag = str;
            addItem(item);
        }
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Translate from";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        FragmentUtils.showWithArguments(TranslateAsyncAlertDialogBottomSheet.class, getParentFragmentManager(), TranslateAsyncAlertDialogBottomSheet.buildArgs(getPost(), (String) item.tag));
        StaticToast.showWithContext(getActivity(), item.toString());
        dismissAllowingStateLoss();
    }
}
