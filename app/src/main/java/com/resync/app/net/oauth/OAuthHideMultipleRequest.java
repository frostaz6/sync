package com.resync.app.net.oauth;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.singleton.SettingsSingleton;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.model.Kinds;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.t */
/* JADX INFO: compiled from: OAuthHideMultipleRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthHideMultipleRequest extends RedditRequest<Void> {
    public OAuthHideMultipleRequest(Context context, String[] strArr) {
        super(context, 1, buildUrl(context), null, null);
        ArrayList arrayList = new ArrayList();
        for (String str : strArr) {
            arrayList.add(Kinds.toPrefix(3) + "_" + str);
        }
        HashMap map = new HashMap();
        this.params = map;
        map.put("id", StringUtils.join(arrayList, ","));
    }

    public static String buildUrl(Context context) {
        return SettingsSingleton.getOauthUrl(context) + "api/hide";
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
