package com.resync.app.dialogs;

import android.os.Bundle;
import com.resync.app.R;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.events.CommentSortUpdatedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: t9.u */
/* JADX INFO: compiled from: CommentSortDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class CommentSortDialogBottomSheet extends AbstractSelectionDialogBottomSheet {
    private String sort;

    public static Bundle buildArguments(Post post, String str) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Post", post);
        bundle.putString("sort", str);
        return bundle;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        String str = StringUtils.isNotEmpty(getPost().getSuggestedSort()) ? " (suggested) " : "";
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Top");
        sb2.append(StringUtils.equalsIgnoreCase("top", this.sort) ? str : "");
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_bar_chart_24, sb2.toString(), StringUtils.equalsIgnoreCase("top", this.sort)));
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Best");
        sb3.append(StringUtils.equalsIgnoreCase("best", this.sort) ? str : "");
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.ic_star_outline_white_24dp, sb3.toString(), StringUtils.equalsIgnoreCase("best", this.sort)));
        StringBuilder sb4 = new StringBuilder();
        sb4.append("New");
        sb4.append(StringUtils.equalsIgnoreCase("new", this.sort) ? str : "");
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_new_24, sb4.toString(), StringUtils.equalsIgnoreCase("new", this.sort)));
        StringBuilder sb5 = new StringBuilder();
        sb5.append("Old");
        sb5.append(StringUtils.equalsIgnoreCase("old", this.sort) ? str : "");
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.ic_history_white_24dp, sb5.toString(), StringUtils.equalsIgnoreCase("old", this.sort)));
        StringBuilder sb6 = new StringBuilder();
        sb6.append("Controversial");
        sb6.append(StringUtils.equalsIgnoreCase("controversial", this.sort) ? str : "");
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_trending_down_24, sb6.toString(), StringUtils.equalsIgnoreCase("Controversial", this.sort)));
        StringBuilder sb7 = new StringBuilder();
        sb7.append("Q&A");
        sb7.append((StringUtils.equalsIgnoreCase("qa", this.sort) || StringUtils.equalsIgnoreCase("q&a", this.sort)) ? str : "");
        addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.outline_forum_24, sb7.toString(), StringUtils.equalsIgnoreCase("qa", this.sort) || StringUtils.equalsIgnoreCase("q&a", this.sort)));
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
        setPost((Post) getArguments().getSerializable("Post"));
        this.sort = getArguments().getString("sort");
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Comment sort";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        BusSingleton.getBus().post(new CommentSortUpdatedEvent(item.title));
        dismiss();
    }
}
