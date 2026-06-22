package com.resync.app.net.oauth;

import android.content.ContentValues;
import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.SettingsSingleton;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;
import com.resync.app.text.parsing.PostParserHelper;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.k */
/* JADX INFO: compiled from: OAuthEditPostRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthEditPostRequest extends RedditRequest<Void> {
    public OAuthEditPostRequest(Context context, String str, String str2) {
        super(context, 1, SettingsSingleton.getOauthUrl(context) + "api/editusertext", null, null);
        HashMap map = new HashMap();
        this.params = map;
        map.put("thing_id", "t3_" + str);
        this.params.put("text", str2);
        this.params.put("api_type", "json");
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            JSONObject jSONObject = new JSONObject(new String(networkResponse.data)).getJSONObject("json").getJSONObject("data").getJSONArray("things").getJSONObject(0).getJSONObject("data");
            ArrayList arrayList = new ArrayList();
            PostParserHelper.parsePostDefaults(this.context, 3, jSONObject, arrayList, false, null);
            this.context.getContentResolver().update(RedditProvider.URI_SINGLE_POST, (ContentValues) arrayList.get(0), null, null);
            this.context.getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
        } catch (Exception unused) {
        }
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
