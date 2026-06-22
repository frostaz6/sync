package com.resync.app.net.thirdparty;

import android.text.Html;
import android.text.TextUtils;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.media.imgur.ImgurImageHelper;
import com.resync.app.model.media.Image;
import com.resync.app.model.media.SingleImage;
import com.resync.app.model.Gallery;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: s8.d */
/* JADX INFO: compiled from: ImgurSingleImageRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class ImgurSingleImageRequest extends Request<Gallery> {

    private final Response.Listener<Gallery> responseListener;

    public ImgurSingleImageRequest(String str, Response.Listener<Gallery> listener, Response.ErrorListener errorListener) {
        super(0, buildUrl(str), errorListener);
        this.responseListener = listener;
        setShouldCache(true);
        Logger.verbose(getUrl());
    }

    public static String buildUrl(String str) {
        // The Sync proxy (images.syncforreddit.com) is dead (NXDOMAIN); hit the imgur API directly.
        // Response shape (data.link/title/description) is identical, so parseNetworkResponse is unchanged.
        return "https://api.imgur.com/3/image/" + ImgurImageHelper.getId(ImgurImageHelper.normalizeUrl(str));
    }

    @Override // com.android.volley.Request
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
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
    public Response<Gallery> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String str = new String(networkResponse.data);
            JSONObject jSONObject = new JSONObject(str).getJSONObject("data");
            Gallery gallery = new Gallery(1);
            Image image = new Image(jSONObject.getString("link"));
            gallery.urls[0] = image.getUrl();
            gallery.thumbnails[0] = image.getBigThumbnailUrl();
            String strOptString = jSONObject.optString("title");
            String str2 = null;
            if (strOptString != null && "null".equals(strOptString)) {
                strOptString = null;
            }
            String strOptString2 = jSONObject.optString("description");
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
                gallery.captions[0] = Html.fromHtml(stringBuffer.toString()).toString();
            }
            gallery.position = new JSONObject(str).optInt("position", 0);
            return Response.success(gallery, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
