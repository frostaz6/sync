package com.resync.app.dialogs;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.PopupMenu;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.helpers.ui.PopupMenuHelper;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.ui.SubredditArrayList;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.util.Logger;
import com.resync.app.net.oauth.OAuthMultisInnerRequest;
import com.resync.app.net.oauth.OAuthSubredditInfoRequest;

/* JADX INFO: renamed from: t9.y0 */
/* JADX INFO: compiled from: MultiredditAsyncSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class MultiredditAsyncSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {

    /* JADX INFO: compiled from: MultiredditAsyncSelectionDialogBottomSheet.java */
    class RemoveMultiMenuListener implements PopupMenu.OnMenuItemClickListener {

        final /* synthetic */ AbstractSelectionDialogBottomSheet.SelectionItem selectionItem;

        RemoveMultiMenuListener(AbstractSelectionDialogBottomSheet.SelectionItem item) {
            this.selectionItem = item;
        }

        @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            Bundle bundleK4 = RemoveMultiAlertDialogBottomSheet.buildArguments(MaterialPostsHelper.extractMultiredditName(MultiredditAsyncSelectionDialogBottomSheet.this.getSubreddit()), (AbstractSelectionDialogBottomSheet.RemovableSubredditItem) this.selectionItem);
            RemoveMultiAlertDialogBottomSheet removeMultiAlertDialogBottomSheet = new RemoveMultiAlertDialogBottomSheet();
            removeMultiAlertDialogBottomSheet.setArguments(bundleK4);
            removeMultiAlertDialogBottomSheet.setTargetFragment(MultiredditAsyncSelectionDialogBottomSheet.this, 0);
            removeMultiAlertDialogBottomSheet.show(MultiredditAsyncSelectionDialogBottomSheet.this.getChildFragmentManager(), "MultiredditAsyncSelectionDialogBottomSheet");
            return true;
        }
    }

    /* JADX INFO: compiled from: MultiredditAsyncSelectionDialogBottomSheet.java */
    class MultiredditsLoadedListener implements Response.Listener<String[]> {
        MultiredditsLoadedListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String[] strArr) {
            if (MultiredditAsyncSelectionDialogBottomSheet.this.isDialogShowing()) {
                SubredditArrayList subredditArrayList = new SubredditArrayList(strArr);
                subredditArrayList.sort(SubredditArrayList.SORT_DEFAULT);
                for (String str : subredditArrayList) {
                    MultiredditAsyncSelectionDialogBottomSheet.this.addItem(new AbstractSelectionDialogBottomSheet.RemovableSubredditItem(str));
                    OAuthRequestManager.sendWithAppContext(new OAuthSubredditInfoRequest(MultiredditAsyncSelectionDialogBottomSheet.this.getActivity(), null, str, null, null));
                }
                MultiredditAsyncSelectionDialogBottomSheet.this.showItems();
            }
        }
    }

    /* JADX INFO: compiled from: MultiredditAsyncSelectionDialogBottomSheet.java */
    class MultiredditsErrorListener implements Response.ErrorListener {
        MultiredditsErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (MultiredditAsyncSelectionDialogBottomSheet.this.isDialogShowing()) {
                StaticToast.showWithContext(MultiredditAsyncSelectionDialogBottomSheet.this.getContext(), "Error loading multireddits");
                MultiredditAsyncSelectionDialogBottomSheet.this.dismiss();
            }
        }
    }

    /* JADX INFO: compiled from: MultiredditAsyncSelectionDialogBottomSheet.java */
    class AddSubredditMenuListener implements PopupMenu.OnMenuItemClickListener {

        final /* synthetic */ MenuItem menuItem;

        AddSubredditMenuListener(MenuItem menuItem) {
            this.menuItem = menuItem;
        }

        @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (!menuItem.equals(this.menuItem)) {
                return true;
            }
            MultiredditAsyncSelectionDialogBottomSheet.this.showNewMultiInput();
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showNewMultiInput() {
        if (getChildFragmentManager().findFragmentByTag("NewMultiInputDialogBottomSheet") == null) {
            Bundle bundle = new Bundle();
            bundle.putString(FragmentUtils.keySubreddit(), MaterialPostsHelper.extractMultiredditName(getSubreddit()));
            NewMultiInputDialogBottomSheet newMultiInputDialogBottomSheet = new NewMultiInputDialogBottomSheet();
            newMultiInputDialogBottomSheet.setArguments(bundle);
            newMultiInputDialogBottomSheet.setTargetFragment(this, 0);
            newMultiInputDialogBottomSheet.show(getChildFragmentManager(), "NewMultiInputDialogBottomSheet");
        }
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        String str;
        String str2;
        Logger.verbose("RAW: " + getSubreddit());
        String strReplaceFirst = getSubreddit().replaceFirst("multi_", "");
        String username = AccountSingleton.getInstance().getUsername();
        if (strReplaceFirst.contains("/m/")) {
            String[] strArrSplit = strReplaceFirst.split("/m/");
            String str3 = strArrSplit[1];
            String str4 = strArrSplit[0];
            getView().findViewById(R.id.section_header_view_more).setVisibility(8);
            str2 = str4;
            str = str3;
        } else {
            str = strReplaceFirst;
            str2 = username;
        }
        OAuthRequestManager.sendWithAppContext(new OAuthMultisInnerRequest(RedditApplication.getAppContext(), str2, str, new MultiredditsLoadedListener(), new MultiredditsErrorListener()));
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return MaterialPostsHelper.extractMultiredditName(getSubreddit());
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    /* JADX INFO: renamed from: m0 */
    public void onMoreClicked(View view) {
        PopupMenu popupMenuC = PopupMenuHelper.create(view, 80);
        popupMenuC.setOnMenuItemClickListener(new AddSubredditMenuListener(popupMenuC.getMenu().add("Add subreddit")));
        popupMenuC.show();
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        com.resync.app.util.links.LinkHandler.openMulti(getActivity(), item.getTitle());
        dismiss();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public boolean isAsync() {
        return true;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public boolean hasMore() {
        return true;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void onItemMoreClicked(View view, AbstractSelectionDialogBottomSheet.SelectionItem item) {
        PopupMenu popupMenuC = PopupMenuHelper.create(view, 5);
        popupMenuC.getMenu().add("Remove");
        popupMenuC.setOnMenuItemClickListener(new RemoveMultiMenuListener(item));
        popupMenuC.show();
    }
}
