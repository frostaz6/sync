package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import org.json.JSONArray;
import org.json.JSONObject;
import com.resync.app.model.AbstractFlair;
import com.resync.app.model.RichTextFlair;
import com.resync.app.model.TextFlair;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.n */
/* JADX INFO: compiled from: OAuthFlairUserSelectorRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthFlairUserSelectorRequest extends RedditRequest<AbstractFlair[]> {
    public OAuthFlairUserSelectorRequest(Context context, Response.Listener<AbstractFlair[]> listener, Response.ErrorListener errorListener, String str) {
        super(context, 0, SettingsSingleton.getOauthUrl(context) + "r/" + str + "/api/user_flair", listener, errorListener);
        setShouldCache(false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2, types: [java.lang.Object, p8.a[]] */
    /* JADX WARN: Type inference failed for: r6v3, types: [p8.d] */
    /* JADX WARN: Type inference failed for: r6v4 */
    /* JADX WARN: Type inference failed for: r6v5, types: [p8.c] */
    @Override // com.android.volley.Request
    public Response<AbstractFlair[]> parseNetworkResponse(NetworkResponse networkResponse) {
        AbstractFlair textFlair;
        try {
            String str = new String(networkResponse.data);
            Logger.verbose(str);
            JSONArray jSONArray = new JSONArray(str);
            AbstractFlair[] flairs = new AbstractFlair[jSONArray.length()];
            for (int flairIndex = 0; flairIndex < jSONArray.length(); flairIndex++) {
                JSONObject jSONObject = jSONArray.getJSONObject(flairIndex);
                String string = jSONObject.getString("type");
                String string2 = jSONObject.getString("id");
                if ("richtext".equalsIgnoreCase(string)) {
                    RichTextFlair richTextFlair = new RichTextFlair(string2, "user_flair");
                    textFlair = richTextFlair;
                    JSONArray jSONArray2 = jSONObject.getJSONArray("richtext");
                    for (int segmentIndex = 0; segmentIndex < jSONArray2.length(); segmentIndex++) {
                        JSONObject jSONObject2 = jSONArray2.getJSONObject(segmentIndex);
                        String string3 = jSONObject2.getString("e");
                        if ("emoji".equalsIgnoreCase(string3)) {
                            richTextFlair.addEmoji(jSONObject2.getString("a"), jSONObject2.getString("u"));
                        } else if ("text".equalsIgnoreCase(string3)) {
                            richTextFlair.addText(jSONObject2.getString("t"));
                        }
                    }
                } else {
                    TextFlair textFlair2 = new TextFlair(string2, "user_flair");
                    textFlair2.setText(jSONObject.getString("text"));
                    textFlair = textFlair2;
                }
                flairs[flairIndex] = textFlair;
            }
            return Response.success(flairs, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            Logger.logException(e);
            return Response.error(new ParseError(e));
        }
    }
}
