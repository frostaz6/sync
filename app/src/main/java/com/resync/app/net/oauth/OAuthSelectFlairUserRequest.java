package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.StaticToast;
import java.util.HashMap;
import com.resync.app.model.AbstractFlair;
import com.resync.app.model.NoFlair;
import com.resync.app.model.RichTextFlair;
import com.resync.app.model.TextFlair;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.s0 */
/* JADX INFO: compiled from: OAuthSelectFlairUserRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthSelectFlairUserRequest extends RedditRequest<Void> {
    public OAuthSelectFlairUserRequest(Context context, String str, AbstractFlair abstractFlair) {
        super(context, 1, SettingsSingleton.getOauthUrl(context) + "r/" + str + "/api/selectflair", null, null);
        HashMap map = new HashMap();
        this.params = map;
        map.put("name", AccountSingleton.getInstance().getUsername());
        if (!(abstractFlair instanceof NoFlair)) {
            this.params.put("flair_template_id", abstractFlair.id);
            if (abstractFlair instanceof RichTextFlair) {
                this.params.put("text", ((RichTextFlair) abstractFlair).getDisplayString());
            } else if (abstractFlair instanceof TextFlair) {
                this.params.put("text", ((TextFlair) abstractFlair).getDisplayText().toString());
            }
        }
        RedditApplication.oauthRequestQueue.getCache().remove(SettingsSingleton.getOauthUrl(context) + "r/" + str + "/about.json");
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            StaticToast.showWithContext(this.context, "Flair updated");
            Logger.verbose(new String(networkResponse.data));
            return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
