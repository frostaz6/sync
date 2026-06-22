package com.resync.app.text.parsing;

import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.helpers.DataSavingHelper;
import com.resync.app.util.TextUtils;

/* JADX INFO: renamed from: c9.d */
/* JADX INFO: compiled from: StreamableParserHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class StreamableParserHelper {
    public static String normalizeUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (!str.startsWith("//")) {
            return str;
        }
        return "https:" + str;
    }

    public static String parseVideoUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("files");
            if (jSONObjectOptJSONObject != null) {
                if (jSONObjectOptJSONObject.optJSONObject("mp4-mobile") == null || !DataSavingHelper.shouldSaveData()) {
                    JSONObject jSONObjectOptJSONObject2 = jSONObjectOptJSONObject.optJSONObject("mp4");
                    if (jSONObjectOptJSONObject2 != null) {
                        String mp4Url = normalizeUrl(jSONObjectOptJSONObject2.optString("url"));
                        if (!TextUtils.isEmpty(mp4Url)) {
                            return mp4Url;
                        }
                    }
                } else {
                    String mobileUrl = normalizeUrl(jSONObjectOptJSONObject.optJSONObject("mp4-mobile").optString("url"));
                    if (!TextUtils.isEmpty(mobileUrl)) {
                        return mobileUrl;
                    }
                    if (jSONObjectOptJSONObject.optJSONObject("mp4") != null) {
                        String mp4Url = normalizeUrl(jSONObjectOptJSONObject.optJSONObject("mp4").optString("url"));
                        if (!TextUtils.isEmpty(mp4Url)) {
                            return mp4Url;
                        }
                    }
                }
            }
            if (jSONObject.optString("url_root") != null) {
                String rootUrl = normalizeUrl(jSONObject.optString("url_root"));
                if (!TextUtils.isEmpty(rootUrl)) {
                    return rootUrl;
                }
            }
        } catch (JSONException unused) {
        }
        return null;
    }
}
