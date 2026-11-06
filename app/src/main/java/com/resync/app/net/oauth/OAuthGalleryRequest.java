package com.resync.app.net.oauth;

import android.util.Log;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.RedditApplication;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.resync.app.net.CustomHttpHeaderParser;
import com.resync.app.model.Gallery;
import com.resync.app.util.Logger;
import com.resync.app.util.WebsafeUtils;
import com.resync.app.net.RedditRequest;
import com.resync.app.util.links.LinkHandler;

/* JADX INFO: renamed from: y8.q */
/* JADX INFO: compiled from: OAuthGalleryRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthGalleryRequest extends RedditRequest<Gallery> {
    private static final String TAG = "ReSyncGallery";
    public OAuthGalleryRequest(String str, Response.Listener<Gallery> listener, Response.ErrorListener errorListener) {
        this(str, listener, errorListener, false);
    }

    private OAuthGalleryRequest(String str, Response.Listener<Gallery> listener,
            Response.ErrorListener errorListener, boolean byIdFallback) {
        super(RedditApplication.getAppContext(), Method.GET,
                byIdFallback ? buildByIdUrl(str) : buildUrl(str), listener, error -> {
                    int status = error != null && error.networkResponse != null
                            ? error.networkResponse.statusCode : -1;
                    if (!byIdFallback && (status == 403 || status == 404)) {
                        Log.w(TAG, "Gallery lookup HTTP " + status + " from /duplicates/; retrying /by_id/");
                        com.resync.app.net.OAuthRequestManager.sendWithAppContext(
                                new OAuthGalleryRequest(str, listener, errorListener, true));
                    } else if (errorListener != null) {
                        errorListener.onErrorResponse(error);
                    }
                });
        // Always test the actual network route, not an older cached album.
        setShouldCache(false);
        Log.i(TAG, "GET " + getUrl());
    }

    /**
     * Reddit started returning route-specific 403s for /api/info, /comments and
     * /by_id in October 2026. The /duplicates/<id> endpoint also identifies the
     * requested post, with the post's full gallery_data and media_metadata in
     * the first Listing of the response array. Only the original post is used.
     * Fall back to /by_id for sessions where that route still works.
     */
    public static String buildUrl(String str) {
        return "https://oauth.reddit.com/duplicates/" + getPostId(str)
                + ".json?raw_json=1&limit=1";
    }

    public static String buildByIdUrl(String str) {
        return "https://oauth.reddit.com/by_id/t3_" + getPostId(str)
                + ".json?raw_json=1";
    }

    private static String getPostId(String str) {
        String address = str == null ? "" : str.split("[?#]", 2)[0];
        while (address.endsWith("/") && !address.isEmpty()) {
            address = address.substring(0, address.length() - 1);
        }
        String id = LinkHandler.getCommentsId(address);
        if (id != null && id.startsWith("t3_")) id = id.substring(3);
        return id;
    }

    @Override // com.android.volley.Request
    public Response<Gallery> parseNetworkResponse(NetworkResponse networkResponse) {
        String stage = "decode JSON";
        try {
            String str = new String(networkResponse.data, java.nio.charset.StandardCharsets.UTF_8);
            Log.i(TAG, "HTTP " + networkResponse.statusCode + " responseBytes=" + networkResponse.data.length);
            // /duplicates/<id>.json returns [original post Listing, duplicates Listing].
            // /by_id/t3_<id>.json (fallback) returns one Listing object.
            Object responseRoot = new JSONTokener(str).nextValue();
            stage = "read post listing";
            Log.i(TAG, "Root=" + responseRoot.getClass().getSimpleName());
            JSONObject listing = responseRoot instanceof JSONArray
                    ? ((JSONArray) responseRoot).getJSONObject(0)
                    : (JSONObject) responseRoot;
            JSONObject jSONObject = listing.getJSONObject("data")
                    .getJSONArray("children").getJSONObject(0).getJSONObject("data");
            stage = "read media_metadata";
            JSONObject jSONObject2 = jSONObject.getJSONObject("media_metadata");
            stage = "read gallery_data.items";
            JSONArray jSONArray = jSONObject.getJSONObject("gallery_data").getJSONArray("items");
            Log.i(TAG, "Gallery: items=" + jSONArray.length() + " metadataEntries=" + jSONObject2.length());
            Gallery gallery = new Gallery(jSONArray.length());
            int playable = 0;
            for (int index = 0; index < jSONArray.length(); index++) {
                stage = "parse gallery item " + index;
                JSONObject jSONObject3 = jSONObject2.getJSONObject(jSONArray.getJSONObject(index).getString("media_id"));
                if (jSONObject3.has("s")) {
                    JSONObject jSONObject4 = jSONObject3.getJSONObject("s");
                    if (jSONObject4.has("mp4")) {
                        gallery.urls[index] = WebsafeUtils.unescapeHtml(jSONObject4.getString("mp4"));
                    } else {
                        gallery.urls[index] = WebsafeUtils.unescapeHtml(jSONObject4.getString("u"));
                    }
                    if (!android.text.TextUtils.isEmpty(gallery.urls[index])) playable++;
                    try {
                        gallery.thumbnails[index] = WebsafeUtils.unescapeHtml(jSONObject3.getJSONArray("p").getJSONObject(0).getString("u"));
                    } catch (Exception unused) {
                        gallery.thumbnails[index] = gallery.urls[index];
                    }
                    StringBuilder sb2 = new StringBuilder();
                    String strOptString = jSONArray.getJSONObject(index).optString("caption");
                    if (StringUtils.isNotEmpty(strOptString)) {
                        sb2.append(strOptString);
                        sb2.append("\n\n");
                    }
                    String strOptString2 = jSONArray.getJSONObject(index).optString("outbound_url");
                    if (StringUtils.isNotEmpty(strOptString2)) {
                        sb2.append(strOptString2);
                    }
                    gallery.captions[index] = sb2.toString();
                } else {
                    gallery.urls[index] = "";
                    gallery.thumbnails[index] = "";
                    gallery.captions[index] = "";
                }
            }
            Log.i(TAG, "Gallery ready: items=" + jSONArray.length() + " playable=" + playable);
            if (playable == 0) {
                throw new org.json.JSONException("No playable media entries; items=" + jSONArray.length());
            }
            // The gallery parser starts at the first image. Do not try to
            // parse the entire response as a second JSONObject.
            gallery.position = 0;
            return Response.success(gallery, CustomHttpHeaderParser.parseCacheEntry(networkResponse, DateUtils.MILLIS_PER_HOUR));
        } catch (Exception e) {
            String reason = stage + ": " + e.getClass().getSimpleName()
                    + (e.getMessage() == null ? "" : ": " + e.getMessage());
            Log.e(TAG, "Reddit gallery parse failed at " + reason, e);
            return Response.error(new VolleyError("Reddit gallery parse failure - " + reason, e));
        }
    }

    @Override
    public VolleyError parseNetworkError(VolleyError error) {
        int code = error != null && error.networkResponse != null
                ? error.networkResponse.statusCode : -1;
        Log.e(TAG, "Reddit gallery request failed. HTTP=" + code
                + " type=" + (error == null ? "null" : error.getClass().getSimpleName()), error);
        return super.parseNetworkError(error);
    }
}
