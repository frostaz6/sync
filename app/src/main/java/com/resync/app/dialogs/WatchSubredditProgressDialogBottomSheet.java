package com.resync.app.dialogs;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.jobs.WatchSubredditJob;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractProgressDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.events.WatchEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.helpers.WatchHelper;
import com.resync.app.net.oauth.OAuthLatestNewPostRequest;

/* JADX INFO: renamed from: t9.f3 */
/* JADX INFO: compiled from: WatchSubredditProgressDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class WatchSubredditProgressDialogBottomSheet extends AbstractProgressDialogBottomSheet {
    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onLatestPostLoaded(String str) {
        if (isDialogShowing()) {
            WatchHelper.addOrUpdateWatch(getSubreddit(), str);
            BusSingleton.getBus().post(new WatchEvent());
            WatchSubredditJob.schedule(getContext());
            dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onLatestPostError(VolleyError volleyError) {
        if (isDialogShowing()) {
            StaticToast.showWithContext(getContext(), "Error: " + volleyError);
            dismiss();
        }
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractProgressDialogBottomSheet
    public void onProgressReady() {
        OAuthRequestManager.sendWithAppContext(new OAuthLatestNewPostRequest(getContext(), getSubreddit(), new Response.Listener() { // from class: t9.e3
            @Override // com.android.volley.Response.Listener
            public final void onResponse(Object obj) {
                WatchSubredditProgressDialogBottomSheet.this.onLatestPostLoaded((String) obj);
            }
        }, new Response.ErrorListener() { // from class: t9.d3
            @Override // com.android.volley.Response.ErrorListener
            public final void onErrorResponse(VolleyError volleyError) {
                WatchSubredditProgressDialogBottomSheet.this.onLatestPostError(volleyError);
            }
        }));
    }
}
