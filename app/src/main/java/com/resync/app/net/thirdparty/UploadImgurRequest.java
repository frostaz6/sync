package com.resync.app.net.thirdparty;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

/* JADX INFO: renamed from: u8.c */
/* JADX INFO: compiled from: UploadImgurRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class UploadImgurRequest extends Request<String> {

    private final Response.Listener<String> responseListener;
    private byte[] imageBytes;

    public UploadImgurRequest(Context context, Uri uri, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(1, "https://imgur-apiv3.p.rapidapi.com/3/image", errorListener);
        this.responseListener = listener;
        try {
            if (uri.toString().startsWith("content://")) {
                this.imageBytes = IOUtils.toByteArray(context.getContentResolver().openInputStream(uri));
            } else {
                this.imageBytes = IOUtils.toByteArray(new FileInputStream(uri.getPath()));
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.android.volley.Request
    public Map<String, String> getHeaders() throws AuthFailureError {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("content-type", "application/x-www-form-urlencoded");
        linkedHashMap.put("authorization", "Client-ID YOUR_IMGUR_CLIENT_ID");
        linkedHashMap.put("x-rapidapi-key", "YOUR_RAPIDAPI_KEY");
        linkedHashMap.put("x-rapidapi-host", "imgur-apiv3.p.rapidapi.com");
        return linkedHashMap;
    }

    @Override // com.android.volley.Request
    protected Map<String, String> getParams() throws AuthFailureError {
        String strEncodeToString = Base64.encodeToString(this.imageBytes, 0);
        HashMap map = new HashMap();
        map.put("image", strEncodeToString);
        return map;
    }

    @Override // com.android.volley.Request
    public VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    @Override // com.android.volley.Request
    public Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            return Response.success(new JSONObject(new String(networkResponse.data)).getJSONObject("data").getString("link"), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override // com.android.volley.Request
    public void deliverResponse(String str) {
        this.responseListener.onResponse(str);
    }
}
