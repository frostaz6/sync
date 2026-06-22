package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.Response;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.v0 */
/* JADX INFO: compiled from: OAuthSidebarRequest.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class OAuthSidebarRequest extends RedditRequest<String> {
    public OAuthSidebarRequest(Context context, int method, String str, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(context, method, str, listener, errorListener);
    }

    public static String buildUrl(Context context, String str) {
        return SettingsSingleton.getOauthUrl(context) + "r/" + str + "/about/.json";
    }
}
