package com.resync.app.net;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;
import java.util.Map;

/* JADX INFO: renamed from: d9.b */
/* JADX INFO: compiled from: LegacyCustomHttpHeaderParser.java */
/* JADX INFO: loaded from: classes.dex */
@Deprecated
public final class LegacyCustomHttpHeaderParser {
    public static Cache.Entry parseCacheEntry(NetworkResponse networkResponse) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        Map<String, String> map = networkResponse.headers;
        String str = map.get("Date");
        long dateAsEpoch = str != null ? HttpHeaderParser.parseDateAsEpoch(str) : 0L;
        String str2 = map.get("ETag");
        long expiry = jCurrentTimeMillis + 10800000;
        Cache.Entry entry = new Cache.Entry();
        entry.data = networkResponse.data;
        entry.etag = str2;
        entry.softTtl = expiry;
        entry.ttl = expiry;
        entry.serverDate = dateAsEpoch;
        entry.responseHeaders = map;
        return entry;
    }
}
