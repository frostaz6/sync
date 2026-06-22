package com.resync.app.net;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import org.apache.commons.lang3.ObjectUtils;

/* JADX INFO: renamed from: v8.a */
/* JADX INFO: compiled from: TagRequestFilter.java */
/* JADX INFO: loaded from: classes.dex */
public class TagRequestFilter implements RequestQueue.RequestFilter {

    Object tag;

    public TagRequestFilter(Object obj) {
        this.tag = obj;
    }

    @Override // com.android.volley.RequestQueue.RequestFilter
    public boolean apply(Request<?> request) {
        if (request == null) {
            return false;
        }
        return ObjectUtils.equals(this.tag, request.getTag());
    }
}
