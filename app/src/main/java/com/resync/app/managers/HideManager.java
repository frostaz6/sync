package com.resync.app.managers;

import android.content.Context;
import com.resync.app.R;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.net.oauth.OAuthHideRequest;

/* JADX INFO: renamed from: q8.b */
/* JADX INFO: compiled from: HideManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class HideManager {
    public static boolean hide(Context context, Post post) {
        Logger.verbose("Hide post: " + post);
        if (AccountSingleton.getInstance().isLoggedIn()) {
            OAuthRequestManager.send(context, new OAuthHideRequest(context, post, 0));
            return true;
        }
        StaticToast.show(context, R.string.common_generic_error_logged_out);
        return false;
    }

    public static void unhide(Context context, Post post) {
        Logger.verbose("Unhide post: " + post);
        if (!AccountSingleton.getInstance().isLoggedIn()) {
            StaticToast.show(context, R.string.common_generic_error_logged_out);
        } else {
            StaticToast.show("Post unhidden");
            OAuthRequestManager.send(context, new OAuthHideRequest(context, post, 1));
        }
    }
}
