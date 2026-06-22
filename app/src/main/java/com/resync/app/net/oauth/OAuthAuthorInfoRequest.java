package com.resync.app.net.oauth;

import android.content.ContentValues;
import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.resync.app.provider.RedditProvider;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.util.Logger;
import com.resync.app.net.RedditRequest;

/* JADX INFO: renamed from: y8.a */
/* JADX INFO: compiled from: OAuthAuthorInfoRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthAuthorInfoRequest extends RedditRequest<Void> {
    final ArrayList<String> authorIds;

    public OAuthAuthorInfoRequest(Context context, ArrayList<String> arrayList, Response.Listener<Void> listener, Response.ErrorListener errorListener) {
        super(context, 0, buildUrl(arrayList), listener, errorListener);
        this.authorIds = arrayList;
        Logger.verbose("OAuthAuthorInfoRequest", "Authors count after: " + arrayList.size());
    }

    private static String buildUrl(ArrayList<String> arrayList) {
        Logger.verbose("OAuthAuthorInfoRequest", "Authors count: " + arrayList.size());
        Logger.verbose("OAuthAuthorInfoRequest", "Authors removing: " + Math.min(arrayList.size(), 100));
        StringBuilder sb2 = new StringBuilder();
        sb2.append("https://oauth.reddit.com/api/user_data_by_account_ids.json?ids=");
        int iMin = Math.min(arrayList.size(), 100);
        for (int index = 0; index < iMin; index++) {
            if (index > 0) {
                sb2.append(",");
                sb2.append(arrayList.get(index));
            }
        }
        while (iMin > 0) {
            arrayList.remove(0);
            iMin--;
        }
        return sb2.toString();
    }

    private boolean isNewAccount(long createdUtc) {
        return (System.currentTimeMillis() / 1000) - createdUtc < 1209600;
    }

    /* JADX WARN: Type inference incomplete: some casts might be missing */
    @Override // com.resync.app.net.RedditRequest, com.android.volley.Request
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    public void deliverResponse(Void response) {
        if (this.authorIds.size() == 0) {
            Logger.verbose("OAuthAuthorInfoRequest", "No authors remain");
            this.responseListener.onResponse(response);
        } else {
            Logger.verbose("OAuthAuthorInfoRequest", "Authors remain, starting another request");
            OAuthRequestManager.sendWithAppContext(new OAuthAuthorInfoRequest(this.context, this.authorIds, this.responseListener, getErrorListener()));
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
            ContentValues[] contentValuesArr = new ContentValues[jSONObject.length()];
            int accountIndex = 0;
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                long createdUtc = jSONObject.getJSONObject(next).getLong("created_utc");
                Logger.verbose("Created: " + createdUtc + StringUtils.SPACE + next);
                contentValuesArr[accountIndex] = new ContentValues();
                contentValuesArr[accountIndex].put("post_author_fullname", next);
                contentValuesArr[accountIndex].put("account_new", Boolean.valueOf(isNewAccount(createdUtc)));
                accountIndex++;
            }
            this.context.getContentResolver().bulkInsert(RedditProvider.URI_NEW_ACCOUNTS, contentValuesArr);
        } catch (Exception e) {
            Logger.logException(e);
        }
        Logger.verbose("Response: " + new String(networkResponse.data));
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
