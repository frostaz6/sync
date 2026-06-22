package com.resync.app.net.oauth;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.RedditApplication;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONObject;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.l */
/* JADX INFO: compiled from: OAuthEmojisRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthEmojisRequest extends RedditRequest<ArrayList<OAuthEmojisRequest.Emoji>> {

    /* JADX INFO: renamed from: y8.l$a */
    /* JADX INFO: compiled from: OAuthEmojisRequest.java */
    public static class Emoji {

        public String name;
        public String url;

        public Emoji(String str, String str2) {
            this.name = str;
            this.url = str2;
        }
    }

    public OAuthEmojisRequest(String str, Response.Listener<ArrayList<Emoji>> listener, Response.ErrorListener errorListener) {
        super(RedditApplication.getAppContext(), 0, buildUrl(str), listener, errorListener);
        setShouldCache(false);
    }

    public static String buildUrl(String str) {
        return "https://oauth.reddit.com/api/v1/" + str + "/emojis/all";
    }

    /* JADX WARN: Type inference incomplete: some casts might be missing */
    @Override // com.resync.app.net.RedditRequest, com.android.volley.Request
    /* JADX INFO: renamed from: c, reason: merged with bridge method [inline-methods] */
    public void deliverResponse(ArrayList<Emoji> arrayList) {
        this.responseListener.onResponse(arrayList);
    }

    @Override // com.android.volley.Request
    public Response<ArrayList<Emoji>> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String str = new String(networkResponse.data);
            Logger.verbose(str);
            ArrayList arrayList = new ArrayList();
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                if (jSONObject.get(next) instanceof JSONObject) {
                    JSONObject jSONObject2 = jSONObject.getJSONObject(next);
                    Iterator<String> itKeys2 = jSONObject2.keys();
                    while (itKeys2.hasNext()) {
                        String next2 = itKeys2.next();
                        if (jSONObject2.get(next2) instanceof JSONObject) {
                            arrayList.add(new Emoji(":" + next2 + ":", ((JSONObject) jSONObject2.get(next2)).optString("url")));
                        }
                    }
                }
            }
            return Response.success(arrayList, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            Logger.logException(e);
            return Response.error(new VolleyError("Could not load gallery"));
        }
    }
}
