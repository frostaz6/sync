package com.resync.app.presenters;

import com.android.volley.VolleyError;

/* Recovered MVP base view interface. Generic parameter P is the presenter type
 * (used by sub-interfaces CommentsView/PostsView/MessagingView). The single
 * shared method showError(VolleyError) is implemented by every view fragment under
 * "@Override // com.resync.app.presenters.BaseViewInterface". */
public interface BaseViewInterface<P> {
    void showError(VolleyError volleyError);
}
