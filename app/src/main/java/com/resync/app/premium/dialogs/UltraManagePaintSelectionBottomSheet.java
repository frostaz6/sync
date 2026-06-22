package com.resync.app.premium.dialogs;

import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.PopupMenu;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSearchSelectionDialogBottomSheet;
import com.resync.app.ultra.jobs.UltraGrabPaintsJob;
import com.squareup.otto.Subscribe;
import com.resync.app.premium.UserPaintManager;
import com.resync.app.helpers.ui.PopupMenuHelper;
import com.resync.app.helpers.ui.StaticToast;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.helpers.PaintAndTagsUpdatedEvent;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.helpers.RemoteConfigHelper;
import com.resync.app.security.KeyManager;
import com.resync.app.premium.UltraHelper;
import com.resync.app.util.ColorUtils;
import com.resync.app.util.links.LinkHelper;

/* JADX INFO: renamed from: ja.e */
/* JADX INFO: compiled from: UltraManagePaintSelectionBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class UltraManagePaintSelectionBottomSheet extends AbstractSearchSelectionDialogBottomSheet {
    private void populateList(CharSequence charSequence) {
        clearItems();
        Map<String, Integer> paints = UserPaintManager.getInstance().getColorMap();
        for (String str : new TreeSet<String>(paints.keySet())) {
            if (charSequence == null || StringUtils.containsIgnoreCase(str, charSequence)) {
                addItem(new AbstractSearchSelectionDialogBottomSheet.PaintedSearchItem(str, paints.get(str).intValue()));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onOverflowMenuClick(MenuItem menuItem, MenuItem menuItem2, MenuItem menuItem3, HashSet hashSet, MenuItem menuItem4) {
        if (menuItem4.equals(menuItem)) {
            if (UltraHelper.isUltra()) {
                FragmentUtils.show(UltraConfirmPaintClearAlertDialogBottomSheet.class, getChildFragmentManager());
            } else {
                StaticToast.show("TODO");
            }
        } else if (menuItem4.equals(menuItem2)) {
            StaticToast.show("Refreshing");
            UltraGrabPaintsJob.schedule();
        } else if (menuItem4.equals(menuItem3)) {
            Iterator it = hashSet.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                UserPaintManager.getInstance().addPaint(AccountSingleton.getInstance().getUsername(), str.split("###user_paint_###")[0], ColorUtils.toHexColor(Integer.parseInt(str.split("###user_paint_###")[1])));
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onItemMenuClick(MenuItem menuItem, AbstractSearchSelectionDialogBottomSheet.SearchItem item, MenuItem menuItem2, MenuItem menuItem3, MenuItem menuItem4) {
        if (menuItem4.equals(menuItem)) {
            UserPaintManager.getInstance().showAddPaintDialog(getContext(), item.label);
            return true;
        }
        if (menuItem4.equals(menuItem2)) {
            UserPaintManager.getInstance().removePaint(AccountSingleton.getInstance().getUsername(), item.label);
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
            FragmentUtils.show(AddUltraPaintInputDialogBottomSheet.class, getChildFragmentManager());
        } else {
            StaticToast.show("You must be online to perform this action");
        }
    }

    @Override // com.resync.app.dialogs.interfaces.SearchSelectionDialogBottomSheetInterface
    public String getSearchHint() {
        return "Search user paints";
    }

    @Override // com.resync.app.dialogs.interfaces.SearchSelectionDialogBottomSheetInterface
    public void onItemClicked(AbstractSearchSelectionDialogBottomSheet.SearchItem item) {
        if (item instanceof AbstractSearchSelectionDialogBottomSheet.PaintedSearchItem) {
            UserPaintManager.getInstance().showAddPaintDialog(getContext(), item.label);
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
        final HashSet hashSet = new HashSet(PreferencesHelper.getDefaultPrefs("UltraManagePaintSelectionBottomSheet").getStringSet("user_paint", new HashSet()));
        int size = IteratorUtils.size(hashSet.iterator());
        final MenuItem menuItemAdd3 = popupMenuC.getMenu().add("Import local (" + size + ")");
        popupMenuC.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: ja.c
            @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
            public final boolean onMenuItemClick(MenuItem menuItem) {
                return UltraManagePaintSelectionBottomSheet.this.onOverflowMenuClick(menuItemAdd, menuItemAdd2, menuItemAdd3, hashSet, menuItem);
            }
        });
        popupMenuC.show();
    }

    @Subscribe
    public void onPaintsUpdated(PaintAndTagsUpdatedEvent paintAndTagsUpdatedEvent) {
        populateList(null);
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSearchSelectionDialogBottomSheet
    public void setupItems() {
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
        popupMenuC.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: ja.d
            @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
            public final boolean onMenuItemClick(MenuItem menuItem) {
                return UltraManagePaintSelectionBottomSheet.this.onItemMenuClick(menuItemAdd, item, menuItemAdd2, menuItemAdd3, menuItem);
            }
        });
        popupMenuC.show();
    }
}
