package com.resync.app.net;

import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

/* JADX INFO: renamed from: x8.b */
/* JADX INFO: compiled from: RedditSubmitRetryPolicy.java */
/* JADX INFO: loaded from: classes.dex */
public class RedditSubmitRetryPolicy implements RetryPolicy {
    @Override // com.android.volley.RetryPolicy
    public int getCurrentRetryCount() {
        return 0;
    }

    @Override // com.android.volley.RetryPolicy
    public int getCurrentTimeout() {
        return 0;
    }

    @Override // com.android.volley.RetryPolicy
    public void retry(VolleyError volleyError) throws VolleyError {
        throw volleyError;
    }
}
