package com.resync.app.ui.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.resync.app.R;
import com.resync.app.receiver.download.ShareReceiver;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.fragment_dialogs.ImagePeekDialogFragment;
import com.resync.app.ui.fragments.posts.VerticalPostsFragment;
import com.resync.app.ui.views.core.MaterialYouToolbar;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.PerSubViewModeHelper;
import com.resync.app.helpers.ActionBarHelper;
import com.resync.app.helpers.IconHelper;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.helpers.PreviousActivityHelper;
import com.resync.app.ui.interfaces.DarkModeAware;
import com.resync.app.security.AdManager;
import com.resync.app.security.KeyManager;
import com.resync.app.premium.UltraHelper;
import com.resync.app.util.ColorUtils;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public class BaseActivity extends AppCompatActivity {
    protected MaterialYouToolbar toolbar;
    protected Class<? extends Activity> previousActivity;
    protected AdManager adManager;
    protected boolean closing;
    protected boolean stateSaved;
    protected long createdTime;
    protected ShareReceiver shareReceiver;
    public boolean peekActive;
    public float downY;
    public float lastY = 0.0f;
    public float lastX = 0.0f;

    class InstanceIdFailureListener implements OnFailureListener {
        InstanceIdFailureListener() {
        }

        @Override // com.google.android.gms.tasks.OnFailureListener
        public void onFailure(Exception exc) {
            BaseActivity.this.reportUltraLicense("unspecified");
        }
    }

    class InstanceIdSuccessListener implements OnSuccessListener<InstanceIdResult> {
        InstanceIdSuccessListener() {
        }

        @Override // com.google.android.gms.tasks.OnSuccessListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onSuccess(InstanceIdResult instanceIdResult) {
            BaseActivity.this.reportUltraLicense(instanceIdResult.getToken());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportUltraLicense(String str) {
        // LIBERATED: this was the Ultra license "phone-home" — it fired only for an Ultra owner
        // whose stored purchase token did NOT look like a real Play token (i.e. a "suspicious"
        // unlock), POSTing token+sku+fcm+account+install-source to Sync's dead backend (Firebase
        // callable "hmmm"). With Ultra liberated, there is nothing to report and the host is dead.
        // No-op so it never fires. (It is now also unreachable: the synthetic token contains
        // ".AO-", so the onCreate() trigger condition is false.)
        android.util.Log.i("BaseActivity", "Skipped Ultra license phone-home (dead Ultra server)");
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        this.lastX = motionEvent.getX();
        this.lastY = motionEvent.getY();
        if (motionEvent.getAction() == 0) {
            this.downY = motionEvent.getY();
        }
        if (!this.peekActive || motionEvent.getAction() != 1) {
            if (this.peekActive) {
                return false;
            }
            try {
                return super.dispatchTouchEvent(motionEvent);
            } catch (Exception e) {
                Logger.logException(e);
                return false;
            }
        }
        this.peekActive = false;
        Fragment peekFragment = getSupportFragmentManager().findFragmentByTag(ImagePeekDialogFragment.TAG);
        if (peekFragment != null) {
            if (peekFragment instanceof ImagePeekDialogFragment) {
                ((ImagePeekDialogFragment) peekFragment).handlePeekTouch(motionEvent.getX(), motionEvent.getY());
            }
            getSupportFragmentManager().beginTransaction().remove(peekFragment).commit();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Activity getActivity() {
        return this;
    }

    public String getTaskLabel() {
        return "Sync for reddit";
    }

    @Override // android.app.Activity
    public void finish() {
        this.closing = true;
        super.finish();
    }

    protected int getAdMode() {
        return 0;
    }

    protected Context getContext() {
        return this;
    }

    public <T extends Fragment> T findFragmentByType(Class<T> cls, int containerId) {
        T t = (T) getSupportFragmentManager().findFragmentById(containerId);
        if (t == null || !t.getClass().equals(cls)) {
            return null;
        }
        return t;
    }

    public String getPreviousActivityName() {
        Class<? extends Activity> cls = this.previousActivity;
        if (cls != null) {
            return cls.getSimpleName();
        }
        return null;
    }

    public MaterialYouToolbar getToolbar() {
        return this.toolbar;
    }

    public long getCreatedTime() {
        return this.createdTime;
    }

    public boolean isClosing() {
        return this.closing;
    }

    public boolean wasStateSaved() {
        return this.stateSaved;
    }

    public void showAd() {
        this.adManager.onShowAd(this);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.createdTime = System.currentTimeMillis();
        this.adManager = new AdManager(getAdMode());
        this.previousActivity = PreviousActivityHelper.getPreviousActivity(this);
        setupWindowBackground();
        setupContentView();
        setupToolbar();
        setupNavigationBar();
        setupStatusBar();
        setupTaskDescription();
        setupSystemUiFlags();
        if ((this instanceof MainActivity) && UltraHelper.isUltra() && !StringUtils.containsIgnoreCase(PreferencesHelper.getDefaultPrefs("UltraHelper").getString(KeyManager.purchaseTokenKey(), null), ".AO-")) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new InstanceIdSuccessListener()).addOnFailureListener(new InstanceIdFailureListener());
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        this.adManager.onDestroy(this);
        super.onDestroy();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() != 4) {
            return super.onKeyUp(keyCode, keyEvent);
        }
        this.closing = true;
        finish();
        return true;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        finishAfterTransition();
        return true;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        Logger.verbose("BaseActivity", "onPause: " + this);
        this.adManager.onPause(this);
        ShareReceiver.unregister(this, this.shareReceiver);
        this.shareReceiver = null;
        super.onPause();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity
    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        this.adManager.onPostCreate(this);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPostResume() {
        super.onPostResume();
        this.peekActive = false;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        Logger.verbose("BaseActivity", "onResume: " + this);
        super.onResume();
        this.shareReceiver = ShareReceiver.register(this);
        this.stateSaved = false;
        this.adManager.onResume(this);
        if ((getContext() instanceof MainActivity) || (getContext() instanceof CasualActivity)) {
            int uiMode = UiSingleton.getInstance().getUiMode();
            ArrayList<Fragment> arrayList = new ArrayList(getSupportFragmentManager().getFragments());
            Logger.verbose("googoo", "Fragments: " + arrayList.size());
            for (Fragment fragment : arrayList) {
                Logger.verbose("googoo", "Fragment: " + fragment);
                if (fragment instanceof VerticalPostsFragment) {
                    VerticalPostsFragment verticalPostsFragment = (VerticalPostsFragment) fragment;
                    boolean hasPerSubViewMode = PerSubViewModeHelper.hasViewMode(verticalPostsFragment.getSubredditName());
                    boolean uiModeChanged = verticalPostsFragment.getUiMode() != uiMode;
                    if (!hasPerSubViewMode && uiModeChanged) {
                        verticalPostsFragment.applyUiMode(uiMode);
                        StaticToast.showWithContext(getActivity(), "Default view changed");
                    }
                }
            }
        }
    }

    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        this.stateSaved = true;
        super.onSaveInstanceState(bundle);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        Logger.verbose("BaseActivity", "onStart: " + this);
        super.onStart();
        this.stateSaved = false;
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        this.adManager.onStop(this);
        Logger.verbose("BaseActivity", "onStop: " + this);
        super.onStop();
        this.peekActive = false;
    }

    public void refreshDarkModeViews(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            KeyEvent.Callback childAt = viewGroup.getChildAt(i);
            if (childAt instanceof ViewGroup) {
                refreshDarkModeViews((ViewGroup) childAt);
                if (childAt instanceof DarkModeAware) {
                    ((DarkModeAware) childAt).onDarkModeChanged();
                }
            } else if (childAt instanceof DarkModeAware) {
                ((DarkModeAware) childAt).onDarkModeChanged();
            }
        }
    }

    public void onScrolled() {
        this.adManager.onScrolled(this);
    }

    protected void setupContentView() {
        setContentView(com.resync.app.R.layout.activity_base);
    }

    protected void setupTaskDescription() {
        Bitmap iconBitmap = BitmapFactory.decodeResource(getResources(), IconHelper.getCurrentIconRes());
        if (iconBitmap == null || iconBitmap.isRecycled()) {
            return;
        }
        try {
            setTaskDescription(new ActivityManager.TaskDescription(getTaskLabel(), iconBitmap));
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    protected void setupWindowBackground() {
        getWindow().getDecorView().setBackgroundColor(MonetUtil.getWindowBackgroundColorAuto());
    }

    protected void setupToolbar() {
        if (findViewById(com.resync.app.R.id.toolbar) == null || !(findViewById(com.resync.app.R.id.toolbar) instanceof MaterialYouToolbar)) {
            return;
        }
        MaterialYouToolbar materialYouToolbar = (MaterialYouToolbar) findViewById(com.resync.app.R.id.toolbar);
        this.toolbar = materialYouToolbar;
        setSupportActionBar(materialYouToolbar);
        getSupportActionBar().setTitle(new String());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.toolbar.refreshPaints();
    }

    protected void setupSystemUiFlags() {
        if (ColorUtils.isDark(ActionBarHelper.getToolbarColor()) || Build.VERSION.SDK_INT < 26) {
            return;
        }
        getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | 16);
    }

    protected void setupNavigationBar() {
        if (Build.VERSION.SDK_INT >= 26) {
            getWindow().setNavigationBarColor(ActionBarHelper.getToolbarColor());
        }
    }

    public void setupStatusBar() {
        getWindow().setStatusBarColor(MonetUtil.getBackgroundColorAuto());
        if (MonetUtil.getBackgroundColorAuto() == -1) {
            getWindow().getDecorView().setSystemUiVisibility(8192);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(0);
        }
    }
}
