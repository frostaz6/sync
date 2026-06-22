package com.resync.app.premium.dialogs;

import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.PopupMenu;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSearchSelectionDialogBottomSheet;
import com.resync.app.ultra.jobs.UltraGrabTagsJob;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.PopupMenuHelper;
import com.resync.app.helpers.ui.StaticToast;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.NewUserTagManager;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.helpers.PaintAndTagsUpdatedEvent;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.helpers.RemoteConfigHelper;
import com.resync.app.security.KeyManager;
import com.resync.app.premium.UltraHelper;
import com.resync.app.util.links.LinkHelper;

/* JADX INFO: renamed from: ka.e */
/* JADX INFO: compiled from: UltraManageTagSelectionBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class UltraManageTagSelectionBottomSheet extends AbstractSearchSelectionDialogBottomSheet {
    private void populateList(CharSequence charSequence) {
        clearItems();
        Map<String, String> tags = NewUserTagManager.getInstance().getTags();
        for (String str : new TreeSet<String>(tags.keySet())) {
            if (charSequence == null || StringUtils.containsIgnoreCase(str, charSequence)) {
                addItem(new AbstractSearchSelectionDialogBottomSheet.TaggedSearchItem(str, tags.get(str)));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onOverflowMenuClick(MenuItem menuItem, MenuItem menuItem2, MenuItem menuItem3, HashSet hashSet, MenuItem menuItem4) {
        if (menuItem4.equals(menuItem)) {
            if (UltraHelper.isUltra()) {
                FragmentUtils.show(UltraConfirmTagClearAlertDialogBottomSheet.class, getChildFragmentManager());
            } else {
                StaticToast.show("TODO");
            }
        } else if (menuItem4.equals(menuItem2)) {
            StaticToast.show("Refreshing");
            UltraGrabTagsJob.schedule();
        } else if (menuItem4.equals(menuItem3)) {
            Map<String, String> treeMap = new TreeMap<>((Comparator<? super String>) String.CASE_INSENSITIVE_ORDER);
            Iterator it = hashSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                String str2 = str.split("###USER_TAG###")[0];
                String str3 = str.split("###USER_TAG###")[1];
                if (!str2.startsWith("__")) {
                    treeMap.put(str2, str3);
                }
            }
            Map<String, String> accountTags = NewUserTagManager.getTagMap(AccountSingleton.getInstance().getUsername());
            for (String str4 : accountTags.keySet()) {
                if (treeMap.containsKey(str4)) {
                    treeMap.remove(str4);
                }
                String str5 = accountTags.get(str4);
                if (!str4.startsWith("__")) {
                    treeMap.put(str4, str5);
                }
            }
            NewUserTagManager.getInstance().importLocalTags(AccountSingleton.getInstance().getUsername(), treeMap);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onItemMenuClick(MenuItem menuItem, AbstractSearchSelectionDialogBottomSheet.SearchItem item, MenuItem menuItem2, MenuItem menuItem3, MenuItem menuItem4) {
        if (menuItem4.equals(menuItem)) {
            FragmentUtils.showWithArguments(AddUltraTagInputDialogBottomSheet.class, getChildFragmentManager(), AddUltraTagInputDialogBottomSheet.createArgs(item.label, ((AbstractSearchSelectionDialogBottomSheet.TaggedSearchItem) item).secondary));
            return true;
        }
        if (menuItem4.equals(menuItem2)) {
            NewUserTagManager.getInstance().removeTag(AccountSingleton.getInstance().getUsername(), item.label);
            return true;
        }
        if (!menuItem4.equals(menuItem3)) {
            return true;
        }
        LinkHelper.open(getActivity(), "/u/" + item.label);
        dismissAllowingStateLoss();
        return true;
    }

    @Override // com.resync.app.dialogs.interfaces.SearchSelectionDialogBottomSheetInterface
    public void onAddClicked(View view) {
        if (ConnectivityHelper.isConnected()) {
            FragmentUtils.showWithArguments(AddUltraTagInputDialogBottomSheet.class, getChildFragmentManager(), AddUltraTagInputDialogBottomSheet.createArgs(null, null));
        } else {
            StaticToast.show("You must be online to perform this action");
        }
    }

    @Override // com.resync.app.dialogs.interfaces.SearchSelectionDialogBottomSheetInterface
    public String getSearchHint() {
        return "Search user tags";
    }

    @Override // com.resync.app.dialogs.interfaces.SearchSelectionDialogBottomSheetInterface
    public void onItemClicked(AbstractSearchSelectionDialogBottomSheet.SearchItem item) {
        if (item instanceof AbstractSearchSelectionDialogBottomSheet.TaggedSearchItem) {
            FragmentUtils.showWithArguments(AddUltraTagInputDialogBottomSheet.class, getChildFragmentManager(), AddUltraTagInputDialogBottomSheet.createArgs(item.label, ((AbstractSearchSelectionDialogBottomSheet.TaggedSearchItem) item).secondary));
        }
    }

    @Override // com.resync.app.dialogs.interfaces.SearchSelectionDialogBottomSheetInterface
    public void onSearchTextChanged(CharSequence charSequence) {
        populateList(charSequence);
    }

    @Override // com.resync.app.dialogs.interfaces.SearchSelectionDialogBottomSheetInterface
    public void onSearchClicked(View view) {
        PopupMenu popupMenuC = PopupMenuHelper.create(view, 5);
        final MenuItem menuItemAdd = popupMenuC.getMenu().add("Clear all");
        final MenuItem menuItemAdd2 = popupMenuC.getMenu().add("Force refresh");
        menuItemAdd2.setVisible(UltraHelper.isUltra());
        final HashSet hashSet = new HashSet(PreferencesHelper.getAccountPrefsForUser(RedditApplication.getAppContext(), AccountSingleton.getInstance().getUsername(), "UltraManageTagSelectionBottomSheet").getStringSet("user_tags", new HashSet()));
        int size = IteratorUtils.size(hashSet.iterator());
        final MenuItem menuItemAdd3 = popupMenuC.getMenu().add("Import local (" + size + ")");
        menuItemAdd3.setVisible(UltraHelper.isUltra());
        popupMenuC.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: ka.c
            @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
            public final boolean onMenuItemClick(MenuItem menuItem) {
                return UltraManageTagSelectionBottomSheet.this.onOverflowMenuClick(menuItemAdd, menuItemAdd2, menuItemAdd3, hashSet, menuItem);
            }
        });
        popupMenuC.show();
    }

    @Subscribe
    public void onTagsUpdated(PaintAndTagsUpdatedEvent paintAndTagsUpdatedEvent) {
        populateList(null);
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSearchSelectionDialogBottomSheet
    public void setupItems() {
        super.setupItems();
        populateList(null);
        if (RemoteConfigHelper.isC4266fa() && UltraHelper.isUltra()) {
            String string = PreferencesHelper.getDefaultPrefs().getString(KeyManager.purchaseTokenKey(), null);
            if (StringUtils.isEmpty(string) || !StringUtils.containsIgnoreCase(string, ".AO-")) {
                dismissAllowingStateLoss();
            }
        }
    }

    @Override // com.resync.app.dialogs.interfaces.SearchSelectionDialogBottomSheetInterface
    public void onItemMoreClicked(View view, final AbstractSearchSelectionDialogBottomSheet.SearchItem item) {
        PopupMenu popupMenuC = PopupMenuHelper.create(view, 5);
        final MenuItem menuItemAdd = popupMenuC.getMenu().add("Edit");
        final MenuItem menuItemAdd2 = popupMenuC.getMenu().add("Remove");
        final MenuItem menuItemAdd3 = popupMenuC.getMenu().add("Profile");
        popupMenuC.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: ka.d
            @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
            public final boolean onMenuItemClick(MenuItem menuItem) {
                return UltraManageTagSelectionBottomSheet.this.onItemMenuClick(menuItemAdd, item, menuItemAdd2, menuItemAdd3, menuItem);
            }
        });
        popupMenuC.show();
    }
}
