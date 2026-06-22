package com.resync.app.dialogs;

import android.os.Bundle;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractProgressDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.ui.SubredditArrayList;
import com.resync.app.events.SubredditSelectedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.net.thirdparty.RandomRequest;
import com.resync.app.helpers.FilterHelper;

/* JADX INFO: compiled from: RandomProgressDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class RandomProgressDialogBottomSheet extends AbstractProgressDialogBottomSheet {
    boolean nsfw;

    /* JADX INFO: compiled from: RandomProgressDialogBottomSheet.java */
    class RandomSubredditResponseListener implements Response.Listener<String> {
        RandomSubredditResponseListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String str) {
            if (RandomProgressDialogBottomSheet.this.getActivity() == null || RandomProgressDialogBottomSheet.this.getDialog() == null) {
                return;
            }
            if (new SubredditArrayList(FilterHelper.getInstance().getFilterList(2)).contains(str)) {
                RandomProgressDialogBottomSheet.this.loadRandomSubreddit();
            } else {
                BusSingleton.getBus().post(new SubredditSelectedEvent(str, false));
                RandomProgressDialogBottomSheet.this.dismiss();
            }
        }
    }

    /* JADX INFO: compiled from: RandomProgressDialogBottomSheet.java */
    class RandomSubredditErrorListener implements Response.ErrorListener {
        RandomSubredditErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (RandomProgressDialogBottomSheet.this.getActivity() == null || RandomProgressDialogBottomSheet.this.getDialog() == null) {
                return;
            }
            StaticToast.showWithContext("Error grabbing random subreddit", RandomProgressDialogBottomSheet.this.getActivity());
            RandomProgressDialogBottomSheet.this.dismiss();
        }
    }

    public static Bundle buildArgs(boolean z) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("nsfw", Boolean.valueOf(z));
        return bundle;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.nsfw = getArguments().getBoolean("nsfw");
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractProgressDialogBottomSheet
    public void onProgressReady() {
        loadRandomSubreddit();
    }

    void loadRandomSubreddit() {
        OAuthRequestManager.send(getActivity(), new RandomRequest(getActivity(), this.nsfw, new RandomSubredditResponseListener(), new RandomSubredditErrorListener()));
    }
}
