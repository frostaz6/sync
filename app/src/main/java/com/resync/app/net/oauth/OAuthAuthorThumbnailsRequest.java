package com.resync.app.net.oauth;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.RedditApplication;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONObject;
import com.resync.app.managers.UserIconManager;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.util.Logger;
import com.resync.app.util.WebsafeUtils;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.b */
/* JADX INFO: compiled from: OAuthAuthorThumbnailsRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthAuthorThumbnailsRequest extends RedditRequest<Void> {

    static String TAG = OAuthAuthorThumbnailsRequest.class.getSimpleName();
    final ArrayList<String> authorIds;

    public OAuthAuthorThumbnailsRequest(ArrayList<String> arrayList) {
        super(RedditApplication.getAppContext(), 0, buildUrl(arrayList), null, null);
        this.authorIds = arrayList;
        Logger.verbose(TAG, "Starting lookup: " + getUrl());
        Logger.verbose(TAG, "Authors count after: " + arrayList.size());
    }

    private static String buildUrl(ArrayList<String> arrayList) {
        Logger.verbose(TAG, "Authors count: " + arrayList.size());
        Logger.verbose(TAG, "Authors removing: " + Math.min(arrayList.size(), 100));
        StringBuilder sb2 = new StringBuilder();
        sb2.append("https://oauth.reddit.com/api/user_data_by_account_ids.json?ids=");
        int iMin = Math.min(arrayList.size(), 100);
        for (int index = 0; index < iMin; index++) {
            if (index > 0) {
                sb2.append(",");
            }
            sb2.append(arrayList.get(index));
        }
        while (iMin > 0) {
            arrayList.remove(0);
            iMin--;
        }
        return sb2.toString();
    }

    @Override // com.resync.app.net.RedditRequest, com.android.volley.Request
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    public void deliverResponse(Void result) {
        if (this.authorIds.size() <= 0) {
            Logger.verbose(TAG, "All done!");
        } else {
            Logger.verbose(TAG, "Authors remain, starting another request");
            OAuthRequestManager.sendWithAppContext(new OAuthAuthorThumbnailsRequest(this.authorIds));
        }
    }

    @Override // com.android.volley.Request
    public void deliverError(VolleyError volleyError) {
        super.deliverError(volleyError);
    }

    @Override // com.android.volley.Request
    public Response<Void> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            JSONObject jSONObject = new JSONObject(new String(networkResponse.data));
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                String string = jSONObject.getJSONObject(next).getString("name");
                String profileImg = WebsafeUtils.unescapeHtml(jSONObject.getJSONObject(next).getString("profile_img"));
                Logger.verbose(TAG, "Adding: " + string + " - " + profileImg);
                UserIconManager.getInstance().setIcon(string, profileImg);
            }
        } catch (Exception e) {
            Logger.logException(e);
        }
        Logger.verbose(TAG, "Response: " + new String(networkResponse.data));
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
