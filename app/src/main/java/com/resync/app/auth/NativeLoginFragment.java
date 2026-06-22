package com.resync.app.auth;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.resync.app.R;
import com.resync.app.fragments.RedditDialogFragment;
import com.resync.app.helpers.ActionsManager;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.RefreshPostsEvent;
import com.resync.app.chat.RedditChatManager;

import org.json.JSONObject;

/**
 * Native-feeling login: native username/password fields, with reddit.com's real login page driven
 * silently in a <b>lazily-created, off-screen</b> WebView. The user never sees a web page on the
 * happy path.
 *
 * <p><b>Why the WebView is created lazily (the bug fix):</b> Android allows exactly one IME
 * "served view" at a time. A WebView present in the layout (even hidden) wins that role from the
 * native EditTexts on load — tearing down their InputConnection every keystroke, which is what
 * produced the "cursor blinks after each letter / hint won't clear / text doesn't register" bug.
 * So no WebView exists while the user types; it's created only at submit, kept non-focusable and
 * behind an opaque overlay, and only made focusable if we have to reveal it.
 *
 * <p><b>Why a WebView at all:</b> reddit's login is gated by Fastly TLS fingerprinting (OkHttp →
 * 403), invisible reCAPTCHA Enterprise, and a JS bot-challenge — only a real Chromium engine clears
 * them. We inject the natively-entered credentials into reddit's form (piercing the Shreddit shadow
 * DOM, using the native value setter + input/change events) and click submit.
 *
 * <p><b>Hybrid silent → reveal:</b> reCAPTCHA Enterprise scores scripted submits as risky and
 * <i>reddit's server</i> decides the threshold, so a fully silent login can't be guaranteed. If we
 * don't get a session in time (challenge / 2FA / SSO / passkey / DOM variant), we REVEAL the same
 * WebView — pre-filled — for the user to finish. The reveal is a normal branch, not an error.
 *
 * <p>On success (the {@code reddit_session} cookie appears) we mint the bearer via
 * {@link WebSessionManager}, resolve the username from {@code /api/v1/me}, then refresh the feed to
 * the personalized one and surface the chat action — so the app immediately becomes "theirs".
 */
public class NativeLoginFragment extends RedditDialogFragment {

    private static final String TAG = "NativeLoginFragment";
    private static final String LOGIN_URL = "https://www.reddit.com/login/";
    private static final long REVEAL_TIMEOUT_MS = 24000L;

    private FrameLayout webViewContainer;
    private View formPanel;
    private View progressOverlay;
    private TextView phaseText;
    private TextView revealHeader;
    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;
    private TextInputEditText usernameField;
    private TextInputEditText passwordField;
    private MaterialButton signInButton;

    private WebView webView;
    private boolean completing;
    private boolean finished;
    private boolean revealed;

    private String pendingUser;
    private String pendingPass;

    // CDP trusted-input path (the "Android nodriver" approach): drive our own WebView's debug socket
    // so keystrokes/clicks arrive as isTrusted==true. Falls back to JS injection on any failure.
    private volatile WebViewCdpDriver cdpDriver;
    private Thread cdpThread;
    private volatile boolean injectionStarted;

    private final Handler handler = new Handler(Looper.getMainLooper());

    public NativeLoginFragment() {
        setStyle(2, R.style.LoginFragment); // RedditSync.Activity -> Theme.Material3, full-screen
    }

    public static NativeLoginFragment newInstance() {
        return new NativeLoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        try {
            return inflater.inflate(R.layout.fragment_native_login, (ViewGroup) null);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        webViewContainer = (FrameLayout) view.findViewById(R.id.login_webview_container);
        formPanel = view.findViewById(R.id.login_form_panel);
        progressOverlay = view.findViewById(R.id.login_progress_overlay);
        phaseText = (TextView) view.findViewById(R.id.login_phase);
        revealHeader = (TextView) view.findViewById(R.id.login_reveal_header);
        usernameLayout = (TextInputLayout) view.findViewById(R.id.login_username_layout);
        passwordLayout = (TextInputLayout) view.findViewById(R.id.login_password_layout);
        usernameField = (TextInputEditText) view.findViewById(R.id.login_username);
        passwordField = (TextInputEditText) view.findViewById(R.id.login_password);
        signInButton = (MaterialButton) view.findViewById(R.id.login_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { attemptLogin(); }
        });
        passwordField.setOnEditorActionListener((tv, actionId, ev) -> {
            attemptLogin();
            return true;
        });
        view.findViewById(R.id.login_web_fallback).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // User opted into the web flow: capture creds (for pre-fill), then reveal.
                pendingUser = text(usernameField);
                pendingPass = text(passwordField);
                ensureWebView();
                webView.loadUrl(LOGIN_URL);
                handler.postDelayed(prefillOnce, 2500);
                revealWebView();
            }
        });
        // Give the username field focus for immediate typing (no WebView to fight it).
        usernameField.requestFocus();
    }

    private void attemptLogin() {
        String user = text(usernameField);
        String pass = text(passwordField);
        usernameLayout.setError(null);
        passwordLayout.setError(null);
        boolean ok = true;
        if (TextUtils.isEmpty(user)) { usernameLayout.setError("Enter your username"); ok = false; }
        if (TextUtils.isEmpty(pass)) { passwordLayout.setError("Enter your password"); ok = false; }
        if (!ok) return;

        pendingUser = user;
        pendingPass = pass;
        hideKeyboard();
        usernameField.clearFocus();
        passwordField.clearFocus();
        setPhase("Signing in…");
        showOverlay();

        ensureWebView();
        webView.loadUrl(LOGIN_URL);
        // Preferred path: drive the WebView over CDP so input is isTrusted==true (best shot at a
        // silent login). On any failure it falls back to the human-like JS injection.
        cdpThread = new Thread(this::runCdpLogin, "cdp-login");
        cdpThread.setDaemon(true);
        cdpThread.start();
        handler.post(sessionPoll);
        handler.postDelayed(revealOnTimeout, REVEAL_TIMEOUT_MS);
    }

    /** JS-injection fallback (human-like typing) — used when the CDP path can't run. */
    private void fallbackToJsInjection() {
        handler.post(new Runnable() {
            @Override public void run() {
                if (completing || finished || injectionStarted) return;
                injectionStarted = true;
                android.util.Log.i(TAG, "CDP unavailable -> JS injection fallback");
                if (webView != null) webView.evaluateJavascript(
                        buildInjectionJs(pendingUser, pendingPass, true), null);
            }
        });
    }

    /** JS to dismiss the cookie wall and return an element's viewport-center "x,y" (or "" if absent). */
    private static String coordsJs(String selectorOrSubmit) {
        String finder = selectorOrSubmit.equals("__submit__")
                ? "deepAll(document,'button').filter(function(x){var t=(x.textContent||'').trim();"
                  + "return /anmelden|log ?in|sign ?in|connexion|iniciar|accedi|entrar/i.test(t)"
                  + "&&!/sso|google|apple/i.test(t);})[0]"
                : "deepAll(document,'input[name=\"" + selectorOrSubmit + "\"]')[0]";
        return "(function(){try{"
          + "function deepAll(r,s,a){a=a||[];r.querySelectorAll(s).forEach(function(e){a.push(e)});"
          + "r.querySelectorAll('*').forEach(function(e){if(e.shadowRoot)deepAll(e.shadowRoot,s,a)});return a;}"
          + "deepAll(document,'button').forEach(function(b){var t=(b.textContent||'').trim().toLowerCase();"
          + "if(t.indexOf('reject optional')>=0||t.indexOf('accept all')>=0||t.indexOf('alle akzeptieren')>=0)b.click();});"
          + "var el=" + finder + ";if(!el)return '';"
          + "var r=el.getBoundingClientRect();"
          + "if(r.width<1||r.height<1)return '';"
          + "return (r.left+r.width/2).toFixed(1)+','+(r.top+r.height/2).toFixed(1);"
          + "}catch(e){return '';}})();";
    }

    /** JS returning "username|passwordLength" so we can confirm the trusted typing actually filled. */
    private static String valuesJs() {
        return "(function(){try{"
          + "function deepAll(r,s,a){a=a||[];r.querySelectorAll(s).forEach(function(e){a.push(e)});"
          + "r.querySelectorAll('*').forEach(function(e){if(e.shadowRoot)deepAll(e.shadowRoot,s,a)});return a;}"
          + "var u=deepAll(document,'input[name=\"username\"]')[0];"
          + "var p=deepAll(document,'input[name=\"password\"]')[0];"
          + "return (u?u.value:'?')+'|'+(p?String(p.value.length):'?');"
          + "}catch(e){return 'err';}})();";
    }

    /** Post-submit diagnostic: where are we, is a reCAPTCHA challenge iframe up, is the form still there. */
    private static String diagJs() {
        return "(function(){try{"
          + "function deepAll(r,s,a){a=a||[];r.querySelectorAll(s).forEach(function(e){a.push(e)});"
          + "r.querySelectorAll('*').forEach(function(e){if(e.shadowRoot)deepAll(e.shadowRoot,s,a)});return a;}"
          + "var cap=deepAll(document,'iframe').some(function(f){return (f.src||'').indexOf('bframe')>=0;});"
          + "var u=deepAll(document,'input[name=\"username\"]')[0];"
          + "return JSON.stringify({href:location.pathname,captcha:cap,form:!!u});"
          + "}catch(e){return 'err';}})();";
    }

    /** Runs the CDP-driven login on a background thread. Trusted moves/clicks/keystrokes with jitter. */
    private void runCdpLogin() {
        WebViewCdpDriver d = null;
        try {
            Thread.sleep(2600); // let the page + JS bot-challenge settle and the debug socket open
            if (completing || finished) return;
            d = new WebViewCdpDriver();
            if (!d.connect()) { fallbackToJsInjection(); return; }
            cdpDriver = d;

            // Behavioral warm-up: trusted mouse movement + scroll + dwell before touching the form,
            // so reCAPTCHA sees human-shaped interaction history (not an instant scripted submit).
            for (int i = 0; i < 6 && !completing && !finished; i++) {
                d.mouseMove(80 + Math.random() * 520, 200 + Math.random() * 760);
                Thread.sleep(110 + (int) (Math.random() * 200));
            }
            d.scroll(360, 600, 240); Thread.sleep(500);
            d.scroll(360, 600, -160); Thread.sleep(700);

            // Wait for the username field (dismisses cookie wall each poll); up to ~14s.
            String uCoords = null;
            for (int i = 0; i < 35 && !completing && !finished; i++) {
                uCoords = d.evaluate(coordsJs("username"));
                if (uCoords != null && uCoords.contains(",")) break;
                Thread.sleep(400);
            }
            if (uCoords == null || !uCoords.contains(",")) { fallbackToJsInjection(); return; }
            injectionStarted = true;
            android.util.Log.i(TAG, "CDP: form found, typing with trusted input");

            moveClickType(d, uCoords, pendingUser);
            Thread.sleep(350 + (int) (Math.random() * 450));
            String pCoords = d.evaluate(coordsJs("password"));
            if (pCoords != null && pCoords.contains(",")) moveClickType(d, pCoords, pendingPass);

            // Confirm the trusted typing actually landed (don't log the password — length only).
            android.util.Log.i(TAG, "CDP: field values -> " + d.evaluate(valuesJs()));

            Thread.sleep(1100 + (int) (Math.random() * 900)); // human dwell before submit
            if (completing || finished) return;
            String bCoords = d.evaluate(coordsJs("__submit__"));
            if (bCoords != null && bCoords.contains(",")) {
                String[] xy = bCoords.split(",");
                d.mouseMove(Double.parseDouble(xy[0]), Double.parseDouble(xy[1]));
                Thread.sleep(140 + (int) (Math.random() * 160));
                d.mouseClick(Double.parseDouble(xy[0]), Double.parseDouble(xy[1]));
                android.util.Log.i(TAG, "CDP: submitted (trusted click)");
            }
            // Diagnose what reddit actually returns (navigated away = success; captcha=challenge).
            for (int i = 0; i < 6 && !completing && !finished; i++) {
                Thread.sleep(1500);
                android.util.Log.i(TAG, "CDP: post-submit -> " + d.evaluate(diagJs()));
            }
        } catch (Throwable t) {
            android.util.Log.i(TAG, "CDP login error: " + t);
            fallbackToJsInjection();
        }
    }

    private static void moveClickType(WebViewCdpDriver d, String coords, String text) {
        String[] xy = coords.split(",");
        double x = Double.parseDouble(xy[0]), y = Double.parseDouble(xy[1]);
        d.mouseMove(x, y);
        try { Thread.sleep(90 + (int) (Math.random() * 120)); } catch (InterruptedException ignored) {}
        d.mouseClick(x, y);
        try { Thread.sleep(140 + (int) (Math.random() * 160)); } catch (InterruptedException ignored) {}
        d.typeText(text == null ? "" : text);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void ensureWebView() {
        if (webView != null) return;
        // Opens this app's @webview_devtools_remote_<pid> socket so our in-process WebViewCdpDriver
        // can feed the page trusted Input.* events (see WebViewCdpDriver). App-internal only.
        try { WebView.setWebContentsDebuggingEnabled(true); } catch (Throwable ignored) {}
        webView = new WebView(requireContext());
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        // Keep the WebView's DEFAULT user-agent (do NOT override): WebView only sends matching
        // Sec-CH-UA Client Hints when the UA is the default. A custom "real Chrome" UA hides the
        // `wv` token but suppresses Client Hints -> a worse inconsistency that lowers the reCAPTCHA
        // score. Consistency beats hiding `wv`. (WebSessionManager's token-mint UA is separate.)
        // Don't let the WebView auto-focus a DOM node (keeps it out of the IME served-view race).
        s.setNeedInitialFocus(false);
        webView.setFocusable(false);
        webView.setFocusableInTouchMode(false);
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        webView.addJavascriptInterface(new Bridge(), "SyncLoginBridge");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onJsAlert(WebView v, String url, String msg, JsResult r) {
                r.confirm(); return true; // never let a JS dialog block teardown
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override public void onPageFinished(WebView v, String url) {
                super.onPageFinished(v, url);
                maybeComplete();
            }
        });
        webViewContainer.addView(webView,
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /** Pre-fill only (no submit) — used when we reveal the WebView for the human. */
    private final Runnable prefillOnce = new Runnable() {
        @Override public void run() {
            if (webView == null || finished) return;
            webView.evaluateJavascript(buildInjectionJs(pendingUser, pendingPass, false), null);
        }
    };

    /**
     * Injection that mimics human input as closely as page JS allows — the "look human so the
     * invisible reCAPTCHA scores high and never escalates" approach (nodriver's avoid-detection
     * principle, applied within a WebView's limits): dismiss the cookie wall, then type each field
     * character-by-character with keydown/input/keyup events at randomized delays, jiggle the pointer
     * for behavioral signal, dwell, and only then click submit (no instant value-dump + submit, which
     * is the most bot-shaped input possible). Page-dispatched events are still isTrusted=false — a
     * hard ceiling we can't cross from JS — so this raises the score floor; the reveal fallback stays.
     */
    private static String buildInjectionJs(String user, String pass, boolean submit) {
        return "(function(){try{"
          + "var U=" + JSONObject.quote(user == null ? "" : user)
          + ",P=" + JSONObject.quote(pass == null ? "" : pass) + ",SUB=" + submit + ";"
          + "function deepAll(r,s,a){a=a||[];r.querySelectorAll(s).forEach(function(e){a.push(e)});"
          + "r.querySelectorAll('*').forEach(function(e){if(e.shadowRoot)deepAll(e.shadowRoot,s,a)});return a;}"
          + "function rnd(a,b){return a+Math.floor(Math.random()*(b-a));}"
          + "function setNative(el,v){var d=Object.getOwnPropertyDescriptor(Object.getPrototypeOf(el),'value');"
          + "if(d&&d.set){d.set.call(el,v);}else{el.value=v;}}"
          + "function typeInto(el,t,done){try{el.focus();el.dispatchEvent(new FocusEvent('focus'));}catch(e){}"
          + "var i=0;(function step(){if(i>=t.length){el.dispatchEvent(new Event('change',{bubbles:true}));done();return;}"
          + "var ch=t.charAt(i);i++;"
          + "el.dispatchEvent(new KeyboardEvent('keydown',{key:ch,bubbles:true}));"
          + "setNative(el,t.slice(0,i));"
          + "try{el.dispatchEvent(new InputEvent('input',{bubbles:true,data:ch,inputType:'insertText'}));}"
          + "catch(e){el.dispatchEvent(new Event('input',{bubbles:true}));}"
          + "el.dispatchEvent(new KeyboardEvent('keyup',{key:ch,bubbles:true}));"
          + "setTimeout(step,rnd(55,150));})();}"
          + "function jiggle(){for(var j=0;j<6;j++){(function(k){setTimeout(function(){"
          + "document.dispatchEvent(new MouseEvent('mousemove',{bubbles:true,clientX:rnd(40,320),clientY:rnd(120,560)}));"
          + "},k*rnd(80,180));})(j);}}"
          + "var n=0,ck=false,started=false;var iv=setInterval(function(){n++;"
          // dismiss reddit's EU cookie consent first
          + "if(!ck){var cbs=deepAll(document,'button');for(var i=0;i<cbs.length;i++){"
          + "var ct=(cbs[i].textContent||'').trim().toLowerCase();"
          + "if(ct.indexOf('reject optional')>=0||ct.indexOf('accept all')>=0"
          + "||ct.indexOf('alle akzeptieren')>=0){cbs[i].click();ck=true;return;}}}"
          + "var u=deepAll(document,'input[name=\"username\"]')[0];"
          + "var p=deepAll(document,'input[name=\"password\"]')[0];"
          + "if(u&&p&&!started){started=true;clearInterval(iv);jiggle();"
          + "typeInto(u,U,function(){setTimeout(function(){typeInto(p,P,function(){"
          + "SyncLoginBridge.onState('filled');if(!SUB){return;}"
          + "setTimeout(function(){var b=deepAll(document,'button').filter(function(x){"
          + "var t=(x.textContent||'').trim();"
          + "return /anmelden|log ?in|sign ?in|connexion|iniciar|accedi|entrar/i.test(t)"
          + "&&!/sso|google|apple/i.test(t);})[0];"
          + "if(b){b.click();SyncLoginBridge.onState('submitted');}else{SyncLoginBridge.onState('nobutton');}"
          + "},rnd(900,1700));});},rnd(280,560));});}"
          + "else if(n>70&&!started){clearInterval(iv);SyncLoginBridge.onState('noform');}"
          + "},200);"
          + "}catch(e){SyncLoginBridge.onState('error');}})();";
    }

    private final Runnable sessionPoll = new Runnable() {
        @Override public void run() {
            if (maybeComplete()) return;
            handler.postDelayed(this, 1000);
        }
    };

    private final Runnable revealOnTimeout = new Runnable() {
        @Override public void run() {
            if (!completing && !finished) {
                // Pre-fill whatever form is now visible, then surface it.
                handler.post(prefillOnce);
                revealWebView();
            }
        }
    };

    /** True (and starts completion) once a reddit.com session cookie is present. */
    private boolean maybeComplete() {
        if (completing) return true;
        String cookies = CookieManager.getInstance().getCookie("https://www.reddit.com");
        if (cookies != null && cookies.contains("reddit_session")) {
            completing = true;
            handler.removeCallbacks(revealOnTimeout);
            CookieManager.getInstance().flush();
            completeLogin();
            return true;
        }
        return false;
    }

    private void completeLogin() {
        closeCdp(); // session obtained; stop driving input
        setPhase("Setting up your account…");
        showOverlay(); // in case we were revealed, cover the WebView again during setup
        final WebSessionManager mgr = WebSessionManager.get(getActivity());
        mgr.invalidate(); // drop any anon token so we mint a USER bearer
        mgr.getToken(new WebSessionManager.TokenCallback() {
            @Override public void onToken(final String jwt) {
                mgr.fetchUsername(jwt, new WebSessionManager.NameCallback() {
                    @Override public void onName(String name) {
                        finishLogin(name != null && !name.isEmpty() ? name : "Reddit user");
                    }
                });
            }
            @Override public void onError(String reason) {
                android.util.Log.i(TAG, "Token error: " + reason);
                completing = false;
                hideOverlay();
                StaticToast.showWithContext(getActivity(), "Login failed: " + reason);
            }
        });
    }

    private void finishLogin(final String username) {
        if (finished) return;
        finished = true;
        android.util.Log.i(TAG, "finishLogin as " + username);
        AccountSingleton.getInstance().updateSession(username, "web_session", true);
        try { RedditChatManager.get(getActivity()).invalidate(); } catch (Throwable ignored) {}
        try { ActionsManager.getInstance().refreshFromSettings(); } catch (Throwable ignored) {} // Chat shows, Sign in hides
        // Phase C: brief success confirmation, then refresh the feed to personalized + dismiss.
        setPhase("Signed in as u/" + username);
        handler.postDelayed(new Runnable() {
            @Override public void run() {
                try { BusSingleton.getBus().post(new RefreshPostsEvent()); } catch (Throwable ignored) {}
                StaticToast.showWithContext("Signed in as u/" + username, getActivity());
                dismissAllowingStateLoss();
            }
        }, 750);
    }

    private void revealWebView() {
        if (revealed || finished) return;
        revealed = true;
        handler.removeCallbacks(revealOnTimeout);
        closeCdp(); // hand control to the human; stop driving input
        if (webView != null) {
            webView.setFocusable(true);
            webView.setFocusableInTouchMode(true);
            webView.getSettings().setNeedInitialFocus(true);
        }
        if (formPanel != null) formPanel.setVisibility(View.GONE);
        if (progressOverlay != null) progressOverlay.setVisibility(View.GONE);
        if (revealHeader != null) revealHeader.setVisibility(View.VISIBLE);
    }

    private void showOverlay() {
        if (formPanel != null) formPanel.setVisibility(View.GONE);
        if (revealHeader != null) revealHeader.setVisibility(View.GONE);
        if (progressOverlay != null) progressOverlay.setVisibility(View.VISIBLE);
        signInButton.setEnabled(false);
    }

    private void hideOverlay() {
        if (progressOverlay != null) progressOverlay.setVisibility(View.GONE);
        if (formPanel != null) formPanel.setVisibility(View.VISIBLE);
        signInButton.setEnabled(true);
    }

    private void setPhase(String t) {
        if (phaseText != null) phaseText.setText(t);
    }

    private static String text(TextInputEditText e) {
        return e == null || e.getText() == null ? "" : e.getText().toString().trim();
    }

    private void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            if (imm != null && getView() != null) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        } catch (Exception ignored) {}
    }

    /** JS -> Java bridge for the injection's status. */
    private final class Bridge {
        @android.webkit.JavascriptInterface
        public void onState(final String state) {
            handler.post(new Runnable() {
                @Override public void run() {
                    android.util.Log.i(TAG, "injection state: " + state);
                    if (("noform".equals(state) || "nobutton".equals(state) || "error".equals(state))
                            && !completing && !finished) {
                        handler.post(prefillOnce);
                        revealWebView();
                    }
                }
            });
        }
    }

    /** Tear down the CDP driver + its worker thread (call when handing to the human or finishing). */
    private void closeCdp() {
        WebViewCdpDriver d = cdpDriver;
        cdpDriver = null;
        if (d != null) d.close();
        Thread t = cdpThread;
        if (t != null) t.interrupt();
        cdpThread = null;
        // Re-close the process-global debug socket so other apps can't attach to our WebViews
        // (incl. the token-mint WebView) once we're done driving input.
        try { WebView.setWebContentsDebuggingEnabled(false); } catch (Throwable ignored) {}
    }

    @Override
    public void onDestroyView() {
        handler.removeCallbacksAndMessages(null);
        closeCdp();
        if (webView != null) {
            try {
                CookieManager.getInstance().flush();
                webView.stopLoading();
                webView.loadUrl("about:blank");
                webView.removeJavascriptInterface("SyncLoginBridge");
                if (webView.getParent() instanceof ViewGroup) {
                    ((ViewGroup) webView.getParent()).removeView(webView);
                }
                webView.destroy();
            } catch (Throwable ignored) {}
            webView = null;
        }
        super.onDestroyView();
    }
}
