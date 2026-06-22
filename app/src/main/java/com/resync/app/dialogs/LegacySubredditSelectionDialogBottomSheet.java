package com.resync.app.dialogs;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import com.resync.app.R;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.util.links.LinkHelper;

/* JADX INFO: renamed from: z9.a */
/* JADX INFO: compiled from: LegacySubredditSelectionDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class LegacySubredditSelectionDialogBottomSheet extends AbstractSelectionDialogBottomSheet {

    /* JADX INFO: compiled from: LegacySubredditSelectionDialogBottomSheet.java */
    class LegacySubredditLoadListener implements Loader.OnLoadCompleteListener<Cursor> {

        final /* synthetic */ CursorLoader cursorLoader;

        /* JADX INFO: compiled from: LegacySubredditSelectionDialogBottomSheet.java */
        class LoadFailedRunnable implements Runnable {
            LoadFailedRunnable() {
            }

            @Override // java.lang.Runnable
            public void run() {
                StaticToast.show("Could not load legacy subreddits");
                LegacySubredditSelectionDialogBottomSheet.this.dismissAllowingStateLoss();
            }
        }

        LegacySubredditLoadListener(CursorLoader cursorLoader) {
            this.cursorLoader = cursorLoader;
        }

        @Override // android.content.Loader.OnLoadCompleteListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
            if (LegacySubredditSelectionDialogBottomSheet.this.getActivity() == null || cursor == null) {
                return;
            }
            this.cursorLoader.unregisterListener(this);
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                if (StringUtils.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex("account_name")), AccountSingleton.getInstance().getUsername())) {
                    try {
                        String string = cursor.getString(cursor.getColumnIndex("account_subs"));
                        if (StringUtils.isNotEmpty(string)) {
                            for (String str : string.split(",")) {
                                LegacySubredditSelectionDialogBottomSheet.this.addItem(new AbstractSelectionDialogBottomSheet.SelectionItem(R.drawable.ic_basic_subreddit, str));
                            }
                            return;
                        }
                    } catch (Exception unused) {
                    }
                    LegacySubredditSelectionDialogBottomSheet.this.getView().post(new LoadFailedRunnable());
                }
            }
        }
    }

    private void loadLegacySubreddits() {
        CursorLoader cursorLoader = new CursorLoader(getActivity(), RedditProvider.URI_ACCOUNTS, new String[]{"account_name", "account_refresh_token", "account_subs", "account_mods", "account_multis"}, null, null, null);
        cursorLoader.registerListener(0, new LegacySubredditLoadListener(cursorLoader));
        cursorLoader.startLoading();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractSelectionDialogBottomSheet
    public void setupItems() {
        loadLegacySubreddits();
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public String getTitle() {
        return "Legacy subreddits";
    }

    @Override // com.resync.app.dialogs.interfaces.SelectionDialogBottomSheetInterface
    public void onItemSelected(AbstractSelectionDialogBottomSheet.SelectionItem item) {
        LinkHelper.open(getActivity(), "/r/" + item.getTitle());
    }
}
