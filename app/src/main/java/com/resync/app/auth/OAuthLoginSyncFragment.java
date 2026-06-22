package com.resync.app.auth;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractProgressDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.net.oauth.OAuthMeRequest;
import com.resync.app.net.oauth.OAuthAccessTokenRequest;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.dialogs.LoggedInAlertDialogBottomSheet;

/* JADX INFO: renamed from: w8.c */
/* JADX INFO: compiled from: OAuthLoginSyncFragment.java */
/* JADX INFO: loaded from: classes.dex */
public class OAuthLoginSyncFragment extends AbstractProgressDialogBottomSheet {
    private String code;
    private String accessToken;
    private String refreshToken;
    private String username;

    /* JADX INFO: renamed from: w8.c$a */
    /* JADX INFO: compiled from: OAuthLoginSyncFragment.java */
    class AccountCheckListener implements Loader.OnLoadCompleteListener<Cursor> {

        final /* synthetic */ CursorLoader loader;

        AccountCheckListener(CursorLoader cursorLoader) {
            this.loader = cursorLoader;
        }

        @Override // android.content.Loader.OnLoadCompleteListener
        public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
            if (OAuthLoginSyncFragment.this.getActivity() == null) {
                return;
            }
            this.loader.unregisterListener(this);
            boolean alreadyExists = false;
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                if (cursor.getString(cursor.getColumnIndex("account_name")).equalsIgnoreCase(OAuthLoginSyncFragment.this.username)) {
                    alreadyExists = true;
                }
            }
            if (alreadyExists) {
                OAuthLoginSyncFragment.this.showErrorAndDismiss("This account has already been added");
            } else {
                OAuthLoginSyncFragment.this.requestAccessToken();
            }
        }
    }

    /* JADX INFO: renamed from: w8.c$b */
    /* JADX INFO: compiled from: OAuthLoginSyncFragment.java */
    class AccessTokenListener implements Response.Listener<Pair<String, String>> {
        AccessTokenListener() {
        }

        @Override // com.android.volley.Response.Listener
        public void onResponse(Pair<String, String> pair) {
            OAuthLoginSyncFragment.this.accessToken = (String) pair.first;
            OAuthLoginSyncFragment.this.refreshToken = (String) pair.second;
            OAuthLoginSyncFragment.this.requestUserInfo();
        }
    }

    /* JADX INFO: renamed from: w8.c$c */
    /* JADX INFO: compiled from: OAuthLoginSyncFragment.java */
    class AccessTokenErrorListener implements Response.ErrorListener {
        AccessTokenErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            OAuthLoginSyncFragment.this.showErrorAndDismiss("Error grabbing token");
        }
    }

    /* JADX INFO: renamed from: w8.c$d */
    /* JADX INFO: compiled from: OAuthLoginSyncFragment.java */
    class UsernameListener implements Response.Listener<String> {
        UsernameListener() {
        }

        @Override // com.android.volley.Response.Listener
        public void onResponse(String str) {
            OAuthLoginSyncFragment.this.username = str;
            OAuthLoginSyncFragment.this.saveAccount();
        }
    }

    /* JADX INFO: renamed from: w8.c$e */
    /* JADX INFO: compiled from: OAuthLoginSyncFragment.java */
    class UsernameErrorListener implements Response.ErrorListener {
        UsernameErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            OAuthLoginSyncFragment.this.showErrorAndDismiss("Error grabbing user info");
        }
    }

    private void checkExistingAccount() {
        CursorLoader cursorLoader = new CursorLoader(getActivity(), RedditProvider.URI_ACCOUNTS, new String[]{"account_name"}, null, null, null);
        cursorLoader.registerListener(0, new AccountCheckListener(cursorLoader));
        cursorLoader.startLoading();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showErrorAndDismiss(String str) {
        StaticToast.showWithContext(str, getActivity());
        dismissAllowingStateLoss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveAccount() {
        if (getActivity() == null || isRemoving()) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_name", this.username);
        contentValues.put("account_refresh_token", this.refreshToken);
        getActivity().getContentResolver().insert(RedditProvider.URI_ACCOUNTS, contentValues);
        AccountSingleton.getInstance().updateSession(this.username, this.refreshToken, true);
        PreferencesHelper.markPrefsAsSet(this.username);
        showLoggedInDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestAccessToken() {
        if (getDialog() == null || getActivity() == null) {
            return;
        }
        OAuthRequestManager.enqueueDirect(new OAuthAccessTokenRequest(getActivity(), this.code, new AccessTokenListener(), new AccessTokenErrorListener()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestUserInfo() {
        if (getActivity() == null || isRemoving()) {
            return;
        }
        setProgressMessage("Grabbing user info");
        if (TextUtils.isEmpty(this.username)) {
            OAuthRequestManager.sendAuthenticated(getActivity(), this.accessToken, new OAuthMeRequest(getActivity(), new UsernameListener(), new UsernameErrorListener()));
        } else {
            saveAccount();
        }
    }

    public static OAuthLoginSyncFragment newInstance(String str, String str2) {
        OAuthLoginSyncFragment oAuthLoginSyncFragment = new OAuthLoginSyncFragment();
        Bundle bundle = new Bundle();
        bundle.putString("code", str);
        bundle.putString("username", str2);
        oAuthLoginSyncFragment.setArguments(bundle);
        return oAuthLoginSyncFragment;
    }

    private void setProgressMessage(String str) {
    }

    void showLoggedInDialog() {
        FragmentUtils.show(LoggedInAlertDialogBottomSheet.class, getChildFragmentManager());
        dismissAllowingStateLoss();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractProgressDialogBottomSheet
    public void onProgressReady() {
        super.onProgressReady();
        this.code = getArguments().getString("code");
        this.username = getArguments().getString("username");
        checkExistingAccount();
    }
}
