package com.resync.app.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.resync.app.R;
import com.resync.app.auth.WebSessionManager;
import com.resync.app.singleton.AccountSingleton;

import org.json.JSONObject;

import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.fragments.RedditDialogFragment;
import com.resync.app.helpers.ActionBarHelper;
import com.resync.app.util.Logger;

/**
 * Web-session login: shows reddit.com's real login page in a WebView. The user logs in normally
 * (handles 2FA / SSO / captcha in the genuine web flow); reddit.com sets the session cookies in the
 * Android {@link CookieManager}. Once a session is detected we mint a bearer token via
 * {@link WebSessionManager} and read the account name from /api/v1/me to set the session.
 *
 * <p>Replaces Sync's dead OAuth-app authorize flow. See
 * {@link WebSessionManager} for why the token is fetched inside a WebView (Fastly TLS fingerprinting
 * 403s plain HTTP clients).
 */
public class OAuthLoginFragment extends RedditDialogFragment {

    private static final String LOGIN_URL = "https://www.reddit.com/login/";

    private WebView webView;
    private ProgressBar progressBar;
    private boolean completing;
    private final android.os.Handler pollHandler = new android.os.Handler(android.os.Looper.getMainLooper());

    class LoginWebChromeClient extends WebChromeClient {
        LoginWebChromeClient() {
        }

        @Override
        public void onProgressChanged(WebView webView, int newProgress) {
            OAuthLoginFragment.this.progressBar.setProgress(newProgress);
            OAuthLoginFragment.this.progressBar.setVisibility(newProgress == 100 ? View.INVISIBLE : View.VISIBLE);
        }
    }

    class LoginWebViewClient extends WebViewClient {
        LoginWebViewClient() {
        }

        @Override
        public void onPageFinished(WebView webView, String url) {
            super.onPageFinished(webView, url);
            OAuthLoginFragment.this.maybeComplete();
        }
    }

    /**
     * Reddit's site is a single-page app: after login it navigates client-side to the homepage
     * WITHOUT a full page reload, so onPageFinished can't be relied on to detect login. Instead we
     * poll the cookie jar for the reddit_session cookie and complete as soon as it appears.
     */
    private final Runnable sessionPoll = new Runnable() {
        @Override
        public void run() {
            if (maybeComplete()) {
                return; // completed; stop polling
            }
            pollHandler.postDelayed(this, 1200);
        }
    };

    /** Returns true and starts completion if a reddit.com session cookie is present. */
    private boolean maybeComplete() {
        if (completing) {
            return true;
        }
        String cookies = CookieManager.getInstance().getCookie("https://www.reddit.com");
        android.util.Log.i("OAuthLoginFragment", "poll: cookies=" + (cookies == null ? "null"
                : (cookies.contains("reddit_session") ? "HAS reddit_session" : "no session (" + cookies.length() + " chars)")));
        if (cookies != null && cookies.contains("reddit_session")) {
            completing = true;
            CookieManager.getInstance().flush();
            completeLogin();
            return true;
        }
        return false;
    }

    public OAuthLoginFragment() {
        setStyle(2, R.style.LoginFragment);
    }

    public static OAuthLoginFragment newInstance() {
        return new OAuthLoginFragment();
    }

    private void completeLogin() {
        android.util.Log.i("OAuthLoginFragment", "completeLogin: session detected, minting token");
        StaticToast.showWithContext("Signing in…", getActivity());
        final WebSessionManager mgr = WebSessionManager.get(getActivity());
        // Drop any cached ANONYMOUS bearer so the next mint uses the now-present reddit_session
        // cookie and yields a real user token instead of serving the stale anon one.
        mgr.invalidate();
        mgr.getToken(new WebSessionManager.TokenCallback() {
            @Override
            public void onToken(final String jwt) {
                // Authoritative: ask oauth.reddit.com/api/v1/me for the real username (the JWT is a
                // valid oauth bearer). Only fall back to the DOM scrape if that fails — the scrape
                // returned empty on the post-login SPA and produced the bogus "Reddit user" name
                // (with a space) that 400/404s every /user/<name> request.
                android.util.Log.i("OAuthLoginFragment", "token ok; resolving username via /api/v1/me");
                mgr.fetchUsername(jwt, new WebSessionManager.NameCallback() {
                    @Override
                    public void onName(String name) {
                        if (name != null && !name.isEmpty()) {
                            finishLogin(name);
                        } else {
                            android.util.Log.i("OAuthLoginFragment", "/api/v1/me empty; falling back to DOM scrape");
                            fetchUsername(jwt);
                        }
                    }
                });
            }

            @Override
            public void onError(String reason) {
                android.util.Log.i("OAuthLoginFragment", "Token error: " + reason);
                StaticToast.showWithContext(getActivity(), "Login failed: " + reason);
                completing = false;
            }
        });
    }

    /**
     * Reads the logged-in account name from the reddit.com page the login WebView is already on
     * (same-origin, so no CORS issue — unlike a cross-origin fetch to oauth.reddit.com). Reddit
     * exposes it in window.___r.user.account.displayText / .username. Falls back to "Reddit user".
     */
    private void fetchUsername(final String jwt) {
        webView.addJavascriptInterface(new Object() {
            @android.webkit.JavascriptInterface
            public void onMe(final String name) {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        finishLogin(name == null || name.isEmpty() ? "Reddit user" : name);
                    }
                });
            }
        }, "SyncMeBridge");

        // Same-origin JS reading the username Reddit embeds in the page state.
        String js =
            "(function(){try{"
          + "var n='';"
          + "try{n=(window.___r&&window.___r.user&&window.___r.user.account)?"
          + "(window.___r.user.account.username||window.___r.user.account.displayText||''):'';}catch(e){}"
          + "if(!n){try{var a=document.querySelector('a[href^=\"/user/\"]');"
          + "if(a){n=a.getAttribute('href').split('/user/')[1].replace(/\\/$/,'');}}catch(e){}}"
          + "SyncMeBridge.onMe(n||'');"
          + "}catch(e){SyncMeBridge.onMe('');}})();";
        webView.evaluateJavascript(js, null);

        // Safety net: if the JS doesn't call back within 3s, finish anyway (token already minted).
        pollHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (completing && getActivity() != null && isAdded()) {
                    finishLogin("Reddit user");
                }
            }
        }, 3000);
    }

    private boolean finished;

    private void finishLogin(String username) {
        if (finished) {
            return;
        }
        finished = true;
        android.util.Log.i("OAuthLoginFragment", "finishLogin as " + username);
        // refresh_token = "web_session" sentinel makes AccountSingleton.isLoggedIn() true; the
        // real bearer is served live by WebSessionManager. Username drives the UI.
        AccountSingleton.getInstance().updateSession(username, "web_session", true);
        StaticToast.showWithContext("Signed in as " + username, getActivity());
        dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        try {
            return layoutInflater.inflate(R.layout.fragment_oauth_login, (ViewGroup) null);
        } catch (Exception unused) {
            return null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        WebView webView = this.webView;
        if (webView != null) {
            webView.saveState(bundle);
        }
    }

    @Override
    @android.annotation.SuppressLint("SetJavaScriptEnabled")
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.webView = (WebView) view.findViewById(R.id.oauth_webview);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.oauth_progressbar);
        this.progressBar = progressBar;
        progressBar.setProgressDrawable(ActionBarHelper.createScrimDrawable(getActivity()));
        this.webView.setWebChromeClient(new LoginWebChromeClient());
        this.webView.setWebViewClient(new LoginWebViewClient());
        WebSettings settings = this.webView.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDisplayZoomControls(false);
        // Mobile browser UA — reddit.com serves the modern login and sets the session cookies.
        settings.setUserAgentString("Mozilla/5.0 (Linux; Android 13; SM-G950F) AppleWebKit/537.36 "
                + "(KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36");
        this.webView.setScrollBarStyle(0);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(this.webView, true);
        if (bundle != null) {
            this.webView.restoreState(bundle);
        } else {
            this.webView.loadUrl(LOGIN_URL);
        }
        // Reddit logs in via client-side SPA navigation (no page reload), so poll the cookie jar for
        // the session cookie rather than relying on onPageFinished. Also completes instantly if a
        // reddit.com session already exists.
        pollHandler.post(sessionPoll);
    }

    @Override
    public void onDestroyView() {
        pollHandler.removeCallbacks(sessionPoll);
        super.onDestroyView();
    }
}
