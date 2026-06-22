package com.resync.app.helpers;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Response;
import com.google.firebase.messaging.FirebaseMessaging;
import com.resync.app.jobs.CheckSubredditsJob;
import com.resync.app.jobs.CheckUserJob;
import com.resync.app.jobs.WatchSubredditJob;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.net.thirdparty.RedditStatusRequest;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.RedditIsDownAlertDialogBottomSheet;
import com.resync.app.helpers.messaging.MessageSchedulingHelper;

/* JADX INFO: renamed from: t7.h0 */
/* JADX INFO: compiled from: StartupHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class StartupHelper {
    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void onRedditStatusResult(AppCompatActivity appCompatActivity, Boolean bool) {
        if (ContextHelper.isActivityFinishing(appCompatActivity) || bool.booleanValue()) {
            return;
        }
        FragmentUtils.show(RedditIsDownAlertDialogBottomSheet.class, ContextHelper.getFragmentManager(appCompatActivity));
    }

    public static final void onAppStart(final AppCompatActivity appCompatActivity, Bundle bundle) {
        CheckSubredditsJob.schedule();
        CheckUserJob.schedule();
        if (bundle == null) {
            RemoteConfigHelper.fetch();
        }
        if (bundle == null) {
            MessageSchedulingHelper.scheduleIfEnabled(appCompatActivity);
            WatchSubredditJob.schedule(appCompatActivity);
        }
        if (ConnectivityHelper.isConnected(appCompatActivity) && bundle == null) {
            OAuthRequestManager.enqueueAux(new RedditStatusRequest(new Response.Listener() { // from class: t7.g0
                @Override // com.android.volley.Response.Listener
                public final void onResponse(Object obj) {
                    StartupHelper.onRedditStatusResult(appCompatActivity, (Boolean) obj);
                }
            }));
        }
        if (bundle == null && SettingsSingleton.getSettings().updateReminder) {
            new UpdateHelper().checkForUpdate(appCompatActivity);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("sync");
    }
}
