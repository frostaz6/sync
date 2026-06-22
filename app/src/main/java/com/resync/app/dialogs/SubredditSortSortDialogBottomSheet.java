package com.resync.app.dialogs;

import android.os.Bundle;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.events.SubredditSortUpdatedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.dialogs.FragmentUtils;

/* JADX INFO: compiled from: SubredditSortSortDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class SubredditSortSortDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    String access;
    String currentAccess;
    String currentSort;

    public static Bundle buildArguments(String str, String str2, String str3, String str4) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(FragmentUtils.keySubreddit(), str);
        bundle.putSerializable("access", str2);
        bundle.putSerializable("current_access", str3);
        bundle.putSerializable("current_sort", str4);
        return bundle;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(0, "…"));
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(0, "Hour", StringUtils.equalsIgnoreCase("t=hour", this.currentSort)));
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(0, "Day", StringUtils.equalsIgnoreCase("t=day", this.currentSort)));
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(0, "Week", StringUtils.equalsIgnoreCase("t=week", this.currentSort)));
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(0, "Month", StringUtils.equalsIgnoreCase("t=month", this.currentSort)));
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(0, "Year", StringUtils.equalsIgnoreCase("t=year", this.currentSort)));
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(0, "All", StringUtils.equalsIgnoreCase("t=all", this.currentSort)));
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isTopSheet() {
        return this.topSheet;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.access = getArguments().getString("access");
        this.currentAccess = getArguments().getString("current_access");
        this.currentSort = getArguments().getString("current_sort");
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return this.access;
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        if (item.getTitle().equals("…")) {
            FragmentUtils.showWithSubreddit(SubredditSortAccessDialogBottomSheet.class, getChildFragmentManager(), getSubreddit());
            dismiss();
            return;
        }
        BusSingleton.getBus().post(new SubredditSortUpdatedEvent(this.access, "t=" + item.title));
        dismiss();
    }
}
