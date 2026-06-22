package com.resync.app.net.thirdparty;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import org.json.JSONObject;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: r8.d */
/* JADX INFO: compiled from: RandomRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class RandomRequest extends RedditRequest<String> {
    public RandomRequest(Context context, boolean nsfw, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(context, 0, buildUrl(context, nsfw), listener, errorListener);
        setShouldCache(false);
    }

    static String buildUrl(Context context, boolean nsfw) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(SettingsSingleton.getOauthUrl(context));
        if (nsfw) {
            stringBuffer.append("r/randnsfw.json");
        } else {
            stringBuffer.append("r/random.json");
        }
        return stringBuffer.toString();
    }

    @Override // com.android.volley.Request
    public Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            return Response.success(new JSONObject(new String(networkResponse.data)).getJSONObject("data").getJSONArray("children").getJSONObject(0).getJSONObject("data").getString("subreddit"), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            Logger.logException(e);
            return Response.error(new ParseError(e));
        }
    }
}
