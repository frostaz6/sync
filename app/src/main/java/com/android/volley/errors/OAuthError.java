package com.android.volley.errors;

import com.android.volley.VolleyError;

/* JADX INFO: loaded from: classes.dex */
public class OAuthError extends VolleyError {
    public OAuthError(VolleyError volleyError) {
        super(volleyError.getMessage(), volleyError);
    }
}
