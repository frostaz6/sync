package com.resync.app.ui.activities;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.HomeActivity;
import com.resync.app.net.OkHttpHelper;
import com.resync.app.singletons.SingletonManager;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.premium.UltraHelper;

/* JADX INFO: loaded from: classes.dex */
public final class HomeActivity extends AppCompatActivity {

    class BiometricCallback extends BiometricPrompt.AuthenticationCallback {
        BiometricCallback() {
        }

        @Override // androidx.biometric.BiometricPrompt.AuthenticationCallback
        public void onAuthenticationError(int errorCode, CharSequence charSequence) {
            super.onAuthenticationError(errorCode, charSequence);
            Toast.makeText(HomeActivity.this, "Authentication error: " + ((Object) charSequence), 0).show();
            HomeActivity.this.finish();
        }

        @Override // androidx.biometric.BiometricPrompt.AuthenticationCallback
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            Toast.makeText(HomeActivity.this, "Authentication failed", 0).show();
            HomeActivity.this.finish();
        }

        @Override // androidx.biometric.BiometricPrompt.AuthenticationCallback
        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            HomeActivity.this.onAuthSucceeded();
        }
    }

    class AuthenticateRunnable implements Runnable {

        final /* synthetic */ BiometricPrompt prompt;
        final /* synthetic */ BiometricPrompt.PromptInfo promptInfo;

        AuthenticateRunnable(BiometricPrompt biometricPrompt, BiometricPrompt.PromptInfo promptInfo) {
            this.prompt = biometricPrompt;
            this.promptInfo = promptInfo;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.prompt.authenticate(this.promptInfo);
        }
    }

    class SwapAccountLoaderListener implements Loader.OnLoadCompleteListener<Cursor> {

        final /* synthetic */ CursorLoader loader;

        SwapAccountLoaderListener(CursorLoader cursorLoader) {
            this.loader = cursorLoader;
        }

        @Override // android.content.Loader.OnLoadCompleteListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
            this.loader.unregisterListener(this);
            cursor.moveToPosition(0);
            AccountSingleton.getInstance().updateSession(cursor.getString(cursor.getColumnIndex("account_name")), cursor.getString(cursor.getColumnIndex("account_refresh_token")), true);
            SingletonManager.reloadAll(true);
            HomeActivity.this.getContentResolver().delete(RedditProvider.URI_SWAP_ACCOUNTS, null, null);
            HomeActivity.this.launchNext();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void showBiometricPrompt() {
        getWindow().getDecorView().post(new AuthenticateRunnable(new BiometricPrompt(this, ContextCompat.getMainExecutor(this), new BiometricCallback()), new BiometricPrompt.PromptInfo.Builder().setTitle("Biometric login").setConfirmationRequired(true).setDeviceCredentialAllowed(false).build()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean keepSplash() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchNext() {
        OkHttpHelper.getUserAgentClient().dispatcher().cancelAll();
        OkHttpHelper.getDefaultClient().dispatcher().cancelAll();
        OkHttpHelper.getNoRedirectClient().dispatcher().cancelAll();
        OkHttpHelper.getManifestCacheClient().dispatcher().cancelAll();
        OkHttpHelper.getRedirectClient().dispatcher().cancelAll();
        SharedPreferences prefs = PreferencesHelper.getDefaultPrefs("HomeActivity");
        if (prefs.getBoolean("show___onboarding", true)) {
            prefs.edit().putBoolean("show___onboarding", false).apply();
            startActivity(new Intent(this, (Class<?>) OnboardingActivity.class));
            return;
        }
        Intent intent = new Intent(this, (Class<?>) MainActivity.class);
        if (getIntent() != null && getIntent().hasExtra(MainActivity.EXTRA_LINK)) {
            intent.putExtra(MainActivity.EXTRA_LINK, getIntent().getStringExtra(MainActivity.EXTRA_LINK));
        }
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAuthSucceeded() {
        if (!SettingsSingleton.getSettings().ultra_default_to_logged_out || !UltraHelper.isUltra()) {
            launchNext();
            return;
        }
        CursorLoader cursorLoader = new CursorLoader(this, RedditProvider.URI_ACCOUNTS, new String[]{"account_name", "account_refresh_token"}, null, null, null);
        cursorLoader.registerListener(0, new SwapAccountLoaderListener(cursorLoader));
        cursorLoader.startLoading();
    }

    public void authenticateAndLaunch(String str) {
        if (PreferencesHelper.getAccountPrefsForUser(this, str, "HomeActivity").getBoolean("biometric_start", false)) {
            getWindow().getDecorView().post(new Runnable() { // from class: m9.d
                @Override // java.lang.Runnable
                public final void run() {
                    HomeActivity.this.showBiometricPrompt();
                }
            });
        } else {
            onAuthSucceeded();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        SplashScreen.installSplashScreen(this).setKeepOnScreenCondition(new SplashScreen.KeepOnScreenCondition() { // from class: m9.e
            @Override // androidx.core.splashscreen.SplashScreen.KeepOnScreenCondition
            public final boolean shouldKeepOnScreen() {
                return HomeActivity.keepSplash();
            }
        });
        super.onCreate(bundle);
        if (isTaskRoot()) {
            authenticateAndLaunch(AccountSingleton.getInstance().getUsername());
        } else {
            finish();
            overridePendingTransition(0, 0);
        }
    }
}
