package com.resync.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.core.content.ContextCompat;
import com.resync.app.R;
import com.resync.app.receiver.MonetThemeReceiver;
import com.resync.app.receiver.ThemeReceiver;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.AdWrapper;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.singletons.SingletonManager;
import com.resync.app.premium.SyncIapHelper;
import com.resync.app.helpers.ActionBarHelper;
import com.resync.app.helpers.GestureNavigationHelper;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.util.ColorUtils;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public class BaseMaterialActivity extends BaseActivity {
    protected ThemeReceiver themeReceiver;
    protected MonetThemeReceiver monetReceiver;
    protected int nightMode;
    protected int monetHash;
    protected int settingsHash;
    protected boolean adsEnabled;
    protected View darkOverlay;

    class AppThemeReceiver extends ThemeReceiver {
        AppThemeReceiver() {
        }

        @Override // com.resync.app.receiver.ThemeReceiver, android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Logger.verbose("THEME CHANGED = " + BaseMaterialActivity.this.getClass().getSimpleName());
            BaseMaterialActivity.this.refreshNightMode();
        }
    }

    class AppMonetReceiver extends MonetThemeReceiver {
        AppMonetReceiver() {
        }

        @Override // com.resync.app.receiver.MonetThemeReceiver, android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Logger.verbose("THEME CHANGED = " + BaseMaterialActivity.this.getClass().getSimpleName());
            Logger.verbose("Monet", "Theme changed");
            BaseMaterialActivity.this.reloadMonet();
        }
    }

    protected void reloadMonet() {
        Logger.verbose("Monet", "Cached hash: " + this.monetHash);
        Logger.verbose("Monet", "Current hash: " + MonetUtil.getConfigHash());
        if (this.monetHash != MonetUtil.getConfigHash()) {
            MonetUtil.reload();
            this.monetHash = MonetUtil.getConfigHash();
            rebuildTheme();
        }
    }

    protected int getNavigationBarColor(boolean night) {
        return ActionBarHelper.getToolbarColor(night);
    }

    protected void rebuildTheme() {
        setupWindowBackground();
        setupToolbar();
        setupNavigationBar();
        setupStatusBar();
        setupSystemUiFlags();
        refreshDarkModeViews((ViewGroup) getWindow().getDecorView());
        updateDarkOverlay();
    }

    protected void updateDarkOverlay() {
        if (SettingsSingleton.getSettings().darkOverlay) {
            if (!NightHelper.isNight()) {
                if (this.darkOverlay != null) {
                    ((ViewGroup) getWindow().getDecorView()).removeView(this.darkOverlay);
                    this.darkOverlay = null;
                    return;
                }
                return;
            }
            if (this.darkOverlay != null) {
                return;
            }
            View view = new View(this);
            this.darkOverlay = view;
            view.setBackgroundColor(1711276032);
            this.darkOverlay.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
            ((ViewGroup) getWindow().getDecorView()).addView(this.darkOverlay);
        }
    }

    protected final void applySystemUiFlags(boolean night) {
        if (GestureNavigationHelper.isGestureNavEnabled(this)) {
            if (ColorUtils.isDark(MonetUtil.getBackgroundColor(night), night)) {
                getWindow().getDecorView().setSystemUiVisibility(1792);
                return;
            } else {
                getWindow().getDecorView().setSystemUiVisibility(9984);
                return;
            }
        }
        getWindow().addFlags(Integer.MIN_VALUE);
        getWindow().clearFlags(67108864);
        if (night) {
            getWindow().getDecorView().setSystemUiVisibility(1280);
            return;
        }
        if (ColorUtils.isDark(ActionBarHelper.getToolbarColor(night), night) || Build.VERSION.SDK_INT < 26) {
            if (ColorUtils.isDark(MonetUtil.getBackgroundColor(night), night)) {
                getWindow().getDecorView().setSystemUiVisibility(1280);
                return;
            } else {
                getWindow().getDecorView().setSystemUiVisibility(9472);
                return;
            }
        }
        if (ColorUtils.isDark(MonetUtil.getBackgroundColor(night), night)) {
            getWindow().getDecorView().setSystemUiVisibility(1296);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(9488);
        }
    }

    protected final void applyNavigationBarColor(boolean night) {
        if (GestureNavigationHelper.isGestureNavEnabled(this)) {
            getWindow().setNavigationBarColor(0);
        } else if (Build.VERSION.SDK_INT >= 26) {
            getWindow().setNavigationBarColor(getNavigationBarColor(night));
        }
    }

    protected void applyStatusBarColor(boolean night) {
        getWindow().setStatusBarColor(0);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Logger.verbose("BaseActivity", "Changing night mode due to system = " + getClass().getSimpleName());
        refreshNightMode();
    }

    @Override // com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.nightMode = NightHelper.getNightMode();
        this.monetHash = MonetUtil.getConfigHash();
        if (bundle != null && bundle.containsKey("primary_color")) {
            this.monetHash = bundle.getInt("primary_color");
            bundle.remove("primary_color");
        }
        Logger.verbose("Monet", "Starting hash: " + this.monetHash);
        this.settingsHash = SettingsSingleton.getSettings().getHash(true);
        this.adsEnabled = SyncIapHelper.areAdsEnabled(this);
        updateDarkOverlay();
    }

    @Override // com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if ((getContext() instanceof MainActivity) || (getContext() instanceof CasualActivity) || (getContext() instanceof CommentsActivity)) {
            Logger.verbose(PreferencesHelper.LOG_TAG, "Checking for changes...");
            if (SettingsSingleton.getInstance().hasSettingsChanged(this.settingsHash, true)) {
                Logger.verbose(PreferencesHelper.LOG_TAG, "Settings changed!");
                SingletonManager.reloadAll(false);
                Logger.verbose(PreferencesHelper.LOG_TAG, "Settings reloaded!");
                recreate();
            }
        }
        if ((getContext() instanceof MainActivity) && !SyncIapHelper.areAdsEnabled(getContext()) && this.adsEnabled) {
            StaticToast.show("Ads were removed");
            if (findViewById(R.id.ad_wrapper) instanceof AdWrapper) {
                ((AdWrapper) findViewById(R.id.ad_wrapper)).refresh();
            }
            recreate();
            return;
        }
        if ((getContext() instanceof MainActivity) && SyncIapHelper.areAdsEnabled(getContext()) && !this.adsEnabled) {
            StaticToast.show("Ads were enabled");
            recreate();
        }
    }

    @Override // com.resync.app.ui.activities.BaseActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt("primary_color", this.monetHash);
        super.onSaveInstanceState(bundle);
    }

    @Override // com.resync.app.ui.activities.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        refreshNightMode();
        reloadMonet();
        AppThemeReceiver themeReceiver = new AppThemeReceiver();
        this.themeReceiver = themeReceiver;
        ContextCompat.registerReceiver(this, themeReceiver, new IntentFilter(ThemeReceiver.getAction()), ContextCompat.RECEIVER_NOT_EXPORTED);
        AppMonetReceiver monetReceiver = new AppMonetReceiver();
        this.monetReceiver = monetReceiver;
        ContextCompat.registerReceiver(this, monetReceiver, new IntentFilter(MonetThemeReceiver.getAction()), ContextCompat.RECEIVER_NOT_EXPORTED);
    }

    @Override // com.resync.app.ui.activities.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        Logger.verbose("Stopping receivers");
        ThemeReceiver.unregister(this, this.themeReceiver);
        MonetThemeReceiver.unregister(this, this.monetReceiver);
        this.themeReceiver = null;
        this.monetReceiver = null;
    }

    @Override // android.app.Activity
    public void recreate() {
        this.settingsHash = SettingsSingleton.getSettings().getHash(true);
        super.recreate();
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected final void setupWindowBackground() {
        super.setupWindowBackground();
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected final void setupToolbar() {
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected void setupSystemUiFlags() {
        applySystemUiFlags(NightHelper.isNight());
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected final void setupNavigationBar() {
        applyNavigationBarColor(NightHelper.isNight());
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    public final void setupStatusBar() {
        applyStatusBarColor(NightHelper.isNight());
    }

    protected void refreshNightMode() {
        NightHelper.refresh(false);
        if (this.nightMode != NightHelper.getNightMode()) {
            this.nightMode = NightHelper.getNightMode();
            rebuildTheme();
        }
    }
}
