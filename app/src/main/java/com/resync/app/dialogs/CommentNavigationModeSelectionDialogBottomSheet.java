package com.resync.app.dialogs;

import android.os.Bundle;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import java.util.ArrayList;
import com.resync.app.events.CommentNavigationModeUpdatedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.util.CommentNavigation;

/* JADX INFO: renamed from: t9.t */
/* JADX INFO: compiled from: CommentNavigationModeSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class CommentNavigationModeSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    CommentNavigation navigation;
    ArrayList<String> authors;
    AbstractSelectionDialogBottomSheet.SelectionItem threadsOption;
    AbstractSelectionDialogBottomSheet.SelectionItem opOption;
    AbstractSelectionDialogBottomSheet.SelectionItem meOption;
    AbstractSelectionDialogBottomSheet.SelectionItem linksOption;
    AbstractSelectionDialogBottomSheet.SelectionItem imagesOption;
    AbstractSelectionDialogBottomSheet.SelectionItem videosOption;
    AbstractSelectionDialogBottomSheet.SelectionItem newCommentsOption;
    AbstractSelectionDialogBottomSheet.SelectionItem authorOption;
    AbstractSelectionDialogBottomSheet.SelectionItem keywordOption;

    public static Bundle buildArguments(CommentNavigation commentNavigation, ArrayList<String> arrayList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("navigation", commentNavigation);
        bundle.putSerializable("authors", arrayList);
        return bundle;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        this.threadsOption = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(CommentNavigation.getModeIcon(7), "Threads (" + this.navigation.getCount(7) + ")"));
        if (this.navigation.getCount(0) > 0) {
            this.opOption = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(CommentNavigation.getModeIcon(0), "OP (" + this.navigation.getCount(0) + ")"));
        }
        if (this.navigation.getCount(1) > 0) {
            this.meOption = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(CommentNavigation.getModeIcon(1), "Me (" + this.navigation.getCount(1) + ")"));
        }
        if (this.navigation.getCount(4) > 0) {
            this.linksOption = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(CommentNavigation.getModeIcon(4), "Links (" + this.navigation.getCount(4) + ")"));
        }
        if (this.navigation.getCount(5) > 0) {
            this.imagesOption = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(CommentNavigation.getModeIcon(5), "Images (" + this.navigation.getCount(5) + ")"));
        }
        if (this.navigation.getCount(6) > 0) {
            this.videosOption = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(CommentNavigation.getModeIcon(6), "Videos (" + this.navigation.getCount(6) + ")"));
        }
        if (this.navigation.getCount(8) > 0) {
            this.newCommentsOption = addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(CommentNavigation.getModeIcon(8), "New comments (" + this.navigation.getCount(8) + ")"));
        }
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.navigation = (CommentNavigation) getArguments().getSerializable("navigation");
        this.authors = (ArrayList) getArguments().getSerializable("authors");
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Navigation mode";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        if (item.equals(this.threadsOption)) {
            BusSingleton.getBus().post(new CommentNavigationModeUpdatedEvent(7));
        } else if (item.equals(this.opOption)) {
            BusSingleton.getBus().post(new CommentNavigationModeUpdatedEvent(0));
        } else if (item.equals(this.meOption)) {
            BusSingleton.getBus().post(new CommentNavigationModeUpdatedEvent(1));
        } else if (item.equals(this.linksOption)) {
            BusSingleton.getBus().post(new CommentNavigationModeUpdatedEvent(4));
        } else if (item.equals(this.imagesOption)) {
            BusSingleton.getBus().post(new CommentNavigationModeUpdatedEvent(5));
        } else if (item.equals(this.videosOption)) {
            BusSingleton.getBus().post(new CommentNavigationModeUpdatedEvent(6));
        } else if (item.equals(this.newCommentsOption)) {
            BusSingleton.getBus().post(new CommentNavigationModeUpdatedEvent(8));
        } else if (item.equals(this.authorOption)) {
            FragmentUtils.showWithArguments(CommentsAuthorInputDialogBottomSheet.class, getParentFragmentManager(), CommentsAuthorInputDialogBottomSheet.buildArguments(this.authors));
        } else if (item.equals(this.keywordOption)) {
            FragmentUtils.show(CommentsKeywordInputDialogBottomSheet.class, getParentFragmentManager());
        }
        dismiss();
    }
}
