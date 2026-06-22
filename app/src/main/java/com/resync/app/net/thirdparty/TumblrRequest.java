package com.resync.app.net.thirdparty;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.net.LegacyCustomHttpHeaderParser;
import com.resync.app.model.Gallery;
import com.resync.app.util.Logger;
import com.resync.app.util.links.LinkHandler;

/* JADX INFO: renamed from: r8.j */
/* JADX INFO: compiled from: TumblrRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class TumblrRequest extends Request<Gallery> {

    private final Response.Listener<Gallery> responseListener;

    public TumblrRequest(String str, Response.Listener<Gallery> listener, Response.ErrorListener errorListener) {
        super(0, buildUrl(str), errorListener);
        this.responseListener = listener;
        setShouldCache(true);
    }

    private static String buildUrl(String str) {
        return "https://api.tumblr.com/v2/blog/" + LinkHandler.getTumblrSubdomain(str) + ".tumblr.com/posts?api_key=fuiKNFp9vQFvjLNvx4sUwti4Yb5yGutBN4Xh10LXZhhRKjWlV4&id=" + LinkHandler.getTumblrPostId(str);
    }

    @Override // com.android.volley.Request
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    public void deliverResponse(Gallery gallery) {
        this.responseListener.onResponse(gallery);
    }

    @Override // com.android.volley.Request
    public Response<Gallery> parseNetworkResponse(NetworkResponse networkResponse) {
        JSONObject jSONObject;
        try {
            String str = new String(networkResponse.data);
            Logger.verbose(str);
            JSONObject jSONObjectOptJSONObject = new JSONObject(str).optJSONObject("response");
            if (jSONObjectOptJSONObject != null) {
                Logger.verbose("Tumblr", "HERE");
                JSONArray jSONArrayOptJSONArray = jSONObjectOptJSONObject.optJSONArray("posts");
                if (jSONArrayOptJSONArray != null && jSONArrayOptJSONArray.length() > 0) {
                    Logger.verbose("Tumblr", "HERE 2");
                    JSONObject jSONObject2 = jSONArrayOptJSONArray.getJSONObject(0);
                    if (jSONObject2 != null) {
                        Logger.verbose("Tumblr", "HERE 3");
                        JSONArray jSONArrayOptJSONArray2 = jSONObject2.optJSONArray("photos");
                        if (jSONArrayOptJSONArray2 != null && jSONArrayOptJSONArray2.length() > 0) {
                            int length = jSONArrayOptJSONArray2.length();
                            String[] strArr = new String[length];
                            String[] strArr2 = new String[jSONArrayOptJSONArray2.length()];
                            for (int index = 0; index < length; index++) {
                                JSONObject jSONObjectOptJSONObject2 = jSONArrayOptJSONArray2.optJSONObject(index);
                                if (jSONObjectOptJSONObject2 != null) {
                                    Logger.verbose("Tumblr", "HERE 4");
                                    JSONObject jSONObjectOptJSONObject3 = jSONObjectOptJSONObject2.optJSONObject("original_size");
                                    if (jSONObjectOptJSONObject3 != null) {
                                        Logger.verbose("Tumblr", "HERE 5");
                                        strArr[index] = jSONObjectOptJSONObject3.optString("url");
                                    }
                                    JSONArray jSONArrayOptJSONArray3 = jSONObjectOptJSONObject2.optJSONArray("alt_sizes");
                                    if (jSONArrayOptJSONArray3 != null && jSONArrayOptJSONArray3.length() > 0 && (jSONObject = jSONArrayOptJSONArray3.getJSONObject(jSONArrayOptJSONArray3.length() - 1)) != null) {
                                        strArr2[index] = jSONObject.optString("url");
                                    }
                                }
                            }
                            Gallery gallery = new Gallery(jSONArrayOptJSONArray2.length());
                            gallery.urls = strArr;
                            gallery.thumbnails = strArr2;
                            return Response.success(gallery, LegacyCustomHttpHeaderParser.parseCacheEntry(networkResponse));
                        }
                    }
                }
            }
            return Response.error(new VolleyError("Video not found"));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
