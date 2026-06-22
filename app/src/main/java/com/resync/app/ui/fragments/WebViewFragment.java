package com.resync.app.ui.fragments;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.ui.views.basic.SyncWebView;
import com.resync.app.ui.views.core.MaterialYouToolbar;
import com.resync.app.helpers.ui.AlertDialogHelper;
import com.resync.app.helpers.ui.PopupMenuHelper;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.ActionBarHelper;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes2.dex */
public class WebViewFragment extends RedditFragment {

    @BindView(R.id.webview_progressbar)
    ProgressBar mProgressBar;

    @BindView(R.id.webview)
    SyncWebView mWebView;
    private String url;

    /* JADX INFO: renamed from: s0, reason: collision with root package name */
    private boolean desktopMode;

    class SyncWebViewClient extends WebViewClient {
        SyncWebViewClient() {
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
        }

        @Override // android.webkit.WebViewClient
        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            if (StringUtils.startsWithIgnoreCase(str, "blob:")) {
                str = str.substring(5);
            }
            if (StringUtils.startsWithIgnoreCase(str, "intent:")) {
                com.resync.app.util.links.LinkHandler.openExternal(WebViewFragment.this.getContext(), str);
                return false;
            }
            WebViewFragment.this.mWebView.loadUrl(str);
            return false;
        }
    }

    class SyncWebChromeClient extends WebChromeClient {
        SyncWebChromeClient() {
        }

        @Override // android.webkit.WebChromeClient
        public void onProgressChanged(WebView webView, int progress) {
            WebViewFragment.this.mProgressBar.setProgress(progress);
            if (progress == 100) {
                WebViewFragment.this.mProgressBar.setVisibility(4);
            } else {
                WebViewFragment.this.mProgressBar.setVisibility(0);
            }
            if (WebViewFragment.this.getToolbar() != null) {
                WebViewFragment webViewFragment = WebViewFragment.this;
                if (webViewFragment.mWebView != null) {
                    webViewFragment.getToolbar().setSubtitle(WebViewFragment.this.mWebView.getTitle());
                }
            }
        }
    }

    class FileDownloadListener implements DownloadListener {

        class DownloadConfirmListener implements DialogInterface.OnClickListener {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ String url;

            DownloadConfirmListener(String str) {
                this.url = str;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int which) {
                if (WebViewFragment.this.isAttached()) {
                    com.resync.app.util.links.LinkHandler.openExternal(WebViewFragment.this.getActivity(), this.url);
                }
            }
        }

        FileDownloadListener() {
        }

        @Override // android.webkit.DownloadListener
        public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
            if (WebViewFragment.this.getActivity() == null || WebViewFragment.this.getActivity().isFinishing()) {
                return;
            }
            AlertDialog.Builder builder = AlertDialogHelper.newBuilder(WebViewFragment.this.getContext());
            String[] segments = str.split("/");
            builder.setMessage(segments[segments.length - 1]).setTitle("Download file?");
            builder.setPositiveButton("Download", new DownloadConfirmListener(str));
            builder.setNegativeButton("Cancel", null);
            builder.create().show();
        }
    }

    class CloseClickListener implements View.OnClickListener {
        CloseClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            WebViewFragment.this.getActivity().finish();
        }
    }

    class ToolbarMenuListener implements Toolbar.OnMenuItemClickListener {

        class OverflowMenuListener implements PopupMenu.OnMenuItemClickListener {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ MenuItem refreshItem;
            final /* synthetic */ MenuItem shareItem;
            final /* synthetic */ MenuItem desktopItem;
            final /* synthetic */ MenuItem browserItem;

            OverflowMenuListener(MenuItem menuItem, MenuItem menuItem2, MenuItem menuItem3, MenuItem menuItem4) {
                this.refreshItem = menuItem;
                this.shareItem = menuItem2;
                this.desktopItem = menuItem3;
                this.browserItem = menuItem4;
            }

            @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.equals(this.refreshItem)) {
                    WebViewFragment.this.reload();
                } else if (menuItem.equals(this.shareItem)) {
                    RedditHelper.shareTextWithSubject(WebViewFragment.this.getActivity(), WebViewFragment.this.url, WebViewFragment.this.url);
                } else if (menuItem.equals(this.desktopItem)) {
                    WebViewFragment.this.desktopMode = !WebViewFragment.this.desktopMode;
                    if (WebViewFragment.this.desktopMode) {
                        WebViewFragment.this.mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.2 Safari/537.36");
                    } else {
                        WebViewFragment.this.mWebView.getSettings().setUserAgentString(null);
                    }
                    WebViewFragment.this.mWebView.reload();
                } else if (menuItem.equals(this.browserItem)) {
                    com.resync.app.util.links.LinkHandler.openExternal(WebViewFragment.this.getActivity(), WebViewFragment.this.url);
                }
                return true;
            }
        }

        ToolbarMenuListener() {
        }

        @Override // androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.menu_overflow) {
                PopupMenu popupMenuC = PopupMenuHelper.create(WebViewFragment.this.toolbar, 5);
                MenuItem menuItemAdd = popupMenuC.getMenu().add("Refresh");
                MenuItem menuItemAdd2 = popupMenuC.getMenu().add("Share");
                MenuItem menuItemAdd3 = popupMenuC.getMenu().add("Request desktop");
                menuItemAdd3.setCheckable(true);
                menuItemAdd3.setChecked(WebViewFragment.this.desktopMode);
                popupMenuC.setOnMenuItemClickListener(new OverflowMenuListener(menuItemAdd, menuItemAdd2, menuItemAdd3, popupMenuC.getMenu().add("Open in browser")));
                try {
                    popupMenuC.show();
                } catch (Exception e) {
                    Logger.logException(e);
                }
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reload() {
        this.mProgressBar.setProgress(0);
        this.mWebView.clearHistory();
        this.mWebView.clearCache(true);
        this.mWebView.stopLoading();
        this.mWebView.loadUrl("about:blank");
        this.mWebView.destroyDrawingCache();
        this.mWebView.clearView();
        this.mWebView.loadUrl(this.url);
    }

    public static WebViewFragment newInstance(String str) {
        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", str);
        webViewFragment.setArguments(bundle);
        return webViewFragment;
    }

    public boolean goBack() {
        Logger.verbose("mWebView", "mWebView: " + this.mWebView.canGoBack());
        if (!this.mWebView.canGoBack()) {
            return false;
        }
        this.mWebView.stopLoading();
        this.mWebView.goBack();
        return true;
    }

    protected void setupToolbar() {
        this.toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        this.toolbar.inflateMenu(R.menu.webview);
        this.toolbar.refreshTextColors();
        this.toolbar.setNavigationOnClickListener(new CloseClickListener());
        this.toolbar.setOnMenuItemClickListener(new ToolbarMenuListener());
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(getLayoutResId(), (ViewGroup) null);
        ButterKnife.bind(this, viewInflate);
        return viewInflate;
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: U1 */
    public void onDestroy() {
        super.onDestroy();
        SyncWebView syncWebView = this.mWebView;
        if (syncWebView != null) {
            syncWebView.removeAllViews();
            this.mWebView.destroy();
        }
        this.mWebView = null;
    }

    @Override // com.resync.app.ui.fragments.RedditFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        this.mWebView.onPause();
    }

    @Override // com.resync.app.ui.fragments.RedditFragment, androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: k2 */
    public void onResume() {
        super.onResume();
        this.mWebView.onResume();
    }

    @Override // com.resync.app.ui.interfaces.RedditFragmentInterface
    public int getLayoutResId() {
        return R.layout.fragment_webview;
    }

    @Override // androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        SyncWebView syncWebView = this.mWebView;
        if (syncWebView != null && Build.VERSION.SDK_INT < 24) {
            syncWebView.saveState(bundle);
        }
        super.onSaveInstanceState(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        this.url = getArguments().getString("url");
        this.toolbar = (MaterialYouToolbar) view.findViewById(R.id.toolbar);
        this.mProgressBar.setProgressDrawable(ActionBarHelper.createScrimDrawable(getActivity()));
        try {
            getToolbar().setTitle(new URL(this.url).getHost().replace("www.", ""));
        } catch (MalformedURLException unused) {
            getToolbar().setTitle(this.url);
        }
        this.mWebView.setWebViewClient(new SyncWebViewClient());
        this.mWebView.setWebChromeClient(new SyncWebChromeClient());
        this.mWebView.setDownloadListener(new FileDownloadListener());
        WebSettings settings = this.mWebView.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        this.mWebView.setScrollBarStyle(0);
        settings.setDisplayZoomControls(false);
        registerForContextMenu(this.mWebView);
        setupToolbar();
        if (bundle != null) {
            this.mWebView.restoreState(bundle);
            return;
        }
        this.mWebView.clearHistory();
        this.mWebView.clearFormData();
        this.mWebView.clearCache(true);
        CookieManager.getInstance().removeAllCookie();
        this.mWebView.loadUrl(this.url);
    }
}
