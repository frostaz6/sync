package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.util.HashMap;
import org.json.JSONObject;
import com.resync.app.model.AbstractFlair;
import com.resync.app.model.RichTextFlair;
import com.resync.app.model.TextFlair;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;
import com.resync.app.net.RedditSubmitRetryPolicy;

/* JADX INFO: renamed from: y8.x0 */
/* JADX INFO: compiled from: OAuthSubmitRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthSubmitRequest extends RedditRequest<String> {
    public OAuthSubmitRequest(Context context, String str, String str2, String str3, int kind, String str4, String str5, boolean sendReplies, boolean spoiler, boolean nsfw, AbstractFlair abstractFlair, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(context, 1, SettingsSingleton.getOauthUrl(context) + "api/submit?validate_on_submit=True", listener, errorListener);
        HashMap map = new HashMap();
        this.params = map;
        map.put("api_type", "json");
        this.params.put("title", str);
        if (str3 != null) {
            this.params.put("sr", str3);
        }
        if (kind == 0) {
            this.params.put("kind", "self");
            this.params.put("text", str2);
        } else {
            this.params.put("kind", "link");
            this.params.put("url", str2);
        }
        if (str4 != null) {
            this.params.put("iden", str4);
            this.params.put("captcha", str5);
        }
        if (abstractFlair != null) {
            if (abstractFlair instanceof RichTextFlair) {
                this.params.put("flair_id", abstractFlair.id);
                this.params.put("flair_text", ((RichTextFlair) abstractFlair).getDisplayString());
            } else if (abstractFlair instanceof TextFlair) {
                this.params.put("flair_id", abstractFlair.id);
                this.params.put("flair_text", ((TextFlair) abstractFlair).getDisplayText().toString());
            }
        }
        this.params.put("sendreplies", Boolean.toString(sendReplies));
        this.params.put("spoiler", Boolean.toString(spoiler));
        this.params.put("nsfw", Boolean.toString(nsfw));
        setRetryPolicy(new RedditSubmitRetryPolicy());
    }

    @Override // com.resync.app.net.RedditRequest, com.android.volley.Request
    public VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    @Override // com.android.volley.Request
    public Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String str = new String(networkResponse.data);
            Logger.verbose(str);
            JSONObject jSONObject = new JSONObject(str).getJSONObject("json");
            return jSONObject.getJSONArray("errors").length() > 0 ? Response.error(new VolleyError(jSONObject.getJSONArray("errors").getJSONArray(0).getString(1))) : Response.success(jSONObject.getJSONObject("data").getString("id"), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
