package com.resync.app.dialogs;

import android.content.Intent;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.activities.PreferencesActivity;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet;
import java.util.Iterator;
import com.resync.app.events.OnUiModeChanged;
import com.resync.app.singletons.BusSingleton;

/* JADX INFO: renamed from: t9.q */
/* JADX INFO: compiled from: ChangeViewSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ChangeViewSelectionDialogBottomSheet extends AbstractRadioSelectionDialogBottomSheet {
    AbstractRadioSelectionDialogBottomSheet.RadioItem swipeItem;
    AbstractRadioSelectionDialogBottomSheet.RadioItem listItem;
    AbstractRadioSelectionDialogBottomSheet.RadioItem compactItem;
    AbstractRadioSelectionDialogBottomSheet.RadioItem smallerCardsItem;
    AbstractRadioSelectionDialogBottomSheet.RadioItem smallCardsItem;
    AbstractRadioSelectionDialogBottomSheet.RadioItem cardsItem;
    AbstractRadioSelectionDialogBottomSheet.RadioItem slidesItem;

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet
    public void setupItems() {
        this.swipeItem = new AbstractRadioSelectionDialogBottomSheet.RadioItem("Swipe", UiSingleton.getInstance().getUiMode() == 8);
        this.listItem = new AbstractRadioSelectionDialogBottomSheet.RadioItem("List", UiSingleton.getInstance().getUiMode() == 0);
        this.compactItem = new AbstractRadioSelectionDialogBottomSheet.RadioItem("Compact", UiSingleton.getInstance().getUiMode() == 1);
        this.smallerCardsItem = new AbstractRadioSelectionDialogBottomSheet.RadioItem("Smaller cards", UiSingleton.getInstance().getUiMode() == 2);
        this.smallCardsItem = new AbstractRadioSelectionDialogBottomSheet.RadioItem("Small cards", UiSingleton.getInstance().getUiMode() == 3);
        this.cardsItem = new AbstractRadioSelectionDialogBottomSheet.RadioItem("Cards", UiSingleton.getInstance().getUiMode() == 4);
        this.slidesItem = new AbstractRadioSelectionDialogBottomSheet.RadioItem("Slides", UiSingleton.getInstance().getUiMode() == 5);
        addItem(this.listItem);
        addItem(this.compactItem);
        addItem(this.smallerCardsItem);
        addItem(this.smallCardsItem);
        addItem(this.cardsItem);
        addItem(this.slidesItem);
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Customize view";
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        Intent intent = new Intent(getActivity(), (Class<?>) PreferencesActivity.class);
        intent.putExtra("mode", RedditApplication.getInteger(R.integer.POST_VIEW_OPTIONS));
        startActivity(intent);
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractRadioSelectionDialogBottomSheet.RadioItem item) {
        Iterator<AbstractRadioSelectionDialogBottomSheet.RadioItem> it = getItems().iterator();
        while (it.hasNext()) {
            it.next().setChecked(false);
        }
        item.setChecked(true);
        notifyItemsChanged();
        if (item.equals(this.swipeItem)) {
            BusSingleton.getBus().post(new OnUiModeChanged(8));
        } else if (item.equals(this.listItem)) {
            BusSingleton.getBus().post(new OnUiModeChanged(0));
        } else if (item.equals(this.compactItem)) {
            BusSingleton.getBus().post(new OnUiModeChanged(1));
        } else if (item.equals(this.smallerCardsItem)) {
            BusSingleton.getBus().post(new OnUiModeChanged(2));
        } else if (item.equals(this.smallCardsItem)) {
            BusSingleton.getBus().post(new OnUiModeChanged(3));
        } else if (item.equals(this.cardsItem)) {
            BusSingleton.getBus().post(new OnUiModeChanged(4));
        } else if (item.equals(this.slidesItem)) {
            BusSingleton.getBus().post(new OnUiModeChanged(5));
        }
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Change view";
    }

    @Override // com.resync.app.dialogs.interfaces.RadioSelectionDialogBottomSheetInterface
    public void onNegativeButtonClicked() {
        dismiss();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractRadioSelectionDialogBottomSheet
    public String getNegativeButtonText() {
        return "Cancel";
    }
}
