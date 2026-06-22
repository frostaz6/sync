package com.resync.app.ui.fragments;

import android.view.View;
import android.widget.ProgressBar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.basic.SyncWebView;

/* JADX INFO: loaded from: classes2.dex */
public class WebViewFragment_ViewBinding implements Unbinder {
    private WebViewFragment target;

    public WebViewFragment_ViewBinding(WebViewFragment webViewFragment, View view) {
        this.target = webViewFragment;
        webViewFragment.mProgressBar = (ProgressBar) Utils.findRequiredViewAsType(view, R.id.webview_progressbar, "field 'mProgressBar'", ProgressBar.class);
        webViewFragment.mWebView = (SyncWebView) Utils.findRequiredViewAsType(view, R.id.webview, "field 'mWebView'", SyncWebView.class);
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        WebViewFragment webViewFragment = this.target;
        if (webViewFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        webViewFragment.mProgressBar = null;
        webViewFragment.mWebView = null;
    }
}
