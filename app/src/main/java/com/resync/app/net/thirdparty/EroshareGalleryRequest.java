package com.resync.app.net.thirdparty;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import org.json.JSONArray;
import org.json.JSONObject;
import com.resync.app.model.Gallery;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: r8.c */
/* JADX INFO: compiled from: EroshareGalleryRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class EroshareGalleryRequest extends Request<Gallery> {

    private final Response.Listener<Gallery> responseListener;

    public static class AlbumNotFoundError extends VolleyError {
    }

    public EroshareGalleryRequest(String str, Response.Listener<Gallery> listener, Response.ErrorListener errorListener) {
        super(0, buildUrl(str), errorListener);
        this.responseListener = listener;
        setShouldCache(true);
        Logger.verbose(getUrl());
    }

    public static String buildUrl(String str) {
        return "https://api.eroshare.com/api/v1/albums/" + com.resync.app.util.links.LinkHandler.getEroshareId(str);
    }

    @Override // com.android.volley.Request
    public void deliverResponse(Gallery gallery) {
        this.responseListener.onResponse(gallery);
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
            JSONArray jSONArray = new JSONObject(str).getJSONArray("items");
            Gallery gallery = new Gallery(jSONArray.length());
            for (int index = 0; index < jSONArray.length(); index++) {
                String string = jSONArray.getJSONObject(index).getString("type");
                if (string.equalsIgnoreCase("Image")) {
                    gallery.urls[index] = "https:" + jSONArray.getJSONObject(index).getString("url_full");
                } else if (string.equalsIgnoreCase("Video")) {
                    gallery.urls[index] = jSONArray.getJSONObject(index).getString("url_mp4_lowres");
                }
                gallery.thumbnails[index] = "https:" + jSONArray.getJSONObject(index).getString("url_thumb");
            }
            gallery.position = new JSONObject(str).optInt("position", 0);
            return Response.success(gallery, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
