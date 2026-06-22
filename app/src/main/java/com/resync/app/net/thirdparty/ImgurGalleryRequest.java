package com.resync.app.net.thirdparty;

import android.text.Html;
import android.text.TextUtils;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import com.resync.app.media.imgur.ImgurImageHelper;
import com.resync.app.model.media.ExternalGallery;
import com.resync.app.model.media.Image;
import com.resync.app.model.Gallery;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: s8.b */
/* JADX INFO: compiled from: ImgurGalleryRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class ImgurGalleryRequest extends Request<Gallery> {

    private final Response.Listener<Gallery> responseListener;

    public static class AlbumNotFoundError extends VolleyError {
    }

    public ImgurGalleryRequest(String str, Response.Listener<Gallery> listener, Response.ErrorListener errorListener) {
        super(0, buildUrl(str), errorListener);
        this.responseListener = listener;
        setShouldCache(true);
        Logger.verbose(getUrl());
    }

    public static String buildUrl(String str) {
        // The Sync proxy (images.syncforreddit.com) is dead (NXDOMAIN); hit the imgur API directly.
        // Response shape (data.images[]) is identical, so parseNetworkResponse is unchanged.
        return "https://api.imgur.com/3/album/" + ImgurImageHelper.getId(ImgurImageHelper.normalizeUrl(str));
    }

    @Override // com.android.volley.Request
    public void deliverResponse(Gallery gallery) {
        this.responseListener.onResponse(gallery);
    }

    @Override // com.android.volley.Request
    public Map<String, String> getHeaders() {
        HashMap map = new HashMap();
        map.put("User-Agent", "sync_for_reddit");
        map.put("Authorization", "Client-ID YOUR_IMGUR_CLIENT_ID");
        return map;
    }

    @Override // com.android.volley.Request
    public VolleyError parseNetworkError(VolleyError volleyError) {
        try {
            if (new String(volleyError.networkResponse.data).contains("Unable to find an album with the id")) {
                return new AlbumNotFoundError();
            }
        } catch (Exception unused) {
        }
        return super.parseNetworkError(volleyError);
    }

    @Override // com.android.volley.Request
    public Response<Gallery> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String str = new String(networkResponse.data);
            Logger.verbose(str);
            JSONArray jSONArray = new JSONObject(str).getJSONObject("data").getJSONArray("images");
            Gallery gallery = new Gallery(jSONArray.length());
            for (int index = 0; index < jSONArray.length(); index++) {
                Image image = new Image(jSONArray.getJSONObject(index).getString("link"));
                if (!TextUtils.isEmpty(jSONArray.getJSONObject(index).optString("mp4"))) {
                    image = new Image(jSONArray.getJSONObject(index).optString("mp4"));
                }
                jSONArray.getJSONObject(index).getInt("width");
                jSONArray.getJSONObject(index).getInt("height");
                gallery.urls[index] = image.getUrl();
                gallery.thumbnails[index] = image.getBigThumbnailUrl();
                String strOptString = jSONArray.getJSONObject(index).optString("title");
                Logger.verbose("Title: " + strOptString);
                String str2 = null;
                if (strOptString != null && "null".equals(strOptString)) {
                    strOptString = null;
                }
                String strOptString2 = jSONArray.getJSONObject(index).optString("description");
                if (strOptString2 == null || !"null".equals(strOptString2)) {
                    str2 = strOptString2;
                }
                StringBuffer stringBuffer = new StringBuffer();
                if (!TextUtils.isEmpty(strOptString)) {
                    stringBuffer.append(strOptString);
                }
                if (!TextUtils.isEmpty(stringBuffer) && !TextUtils.isEmpty(str2)) {
                    stringBuffer.append(" - ");
                }
                if (!TextUtils.isEmpty(str2)) {
                    stringBuffer.append(str2);
                }
                if (!TextUtils.isEmpty(stringBuffer)) {
                    gallery.captions[index] = Html.fromHtml(stringBuffer.toString()).toString();
                }
            }
            gallery.position = new JSONObject(str).optInt("position", 0);
            return Response.success(gallery, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
