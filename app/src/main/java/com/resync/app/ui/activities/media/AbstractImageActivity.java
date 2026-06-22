package com.resync.app.ui.activities.media;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.views.AdWrapper;
import java.lang.reflect.Field;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.helpers.GestureNavigationHelper;
import com.resync.app.helpers.WindowUtils;
import com.resync.app.util.ColorUtils;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractImageActivity extends BaseActivity {
    private String subreddit;
    private String previewUrl;
    private String contentUrl;
    private String title;
    private String commentsUrl;
    private boolean transitionEnabled;

    private boolean viewed;

    private boolean nsfw;
    private int sourceWidth;

    private int sourceHeight;

    private int startVideoPosition;
    protected Bundle savedState;

    @BindView(R.id.ad_wrapper)
    AdWrapper mAdWrapper;

    @BindView(R.id.activity_image_root)
    RelativeLayout mRootView;
    protected int position = 0;
    protected boolean screenOn = true;

    private void clearKeepScreenOn() {
        try {
            getWindow().clearFlags(128);
        } catch (Exception unused) {
        }
    }

    public static Intent createMultiIntent(Context context, String str, String str2, String str3, String str4, String str5) {
        Intent intent = new Intent(context, (Class<?>) MultiImageActivity.class);
        putExtras(intent, str, str2, str3, str4, str5, false, false, false, WindowUtils.getDisplayWidth(), WindowUtils.getDisplayHeight(), 0, -16777216);
        return intent;
    }

    public static Intent createSingleIntent(Context context, String str, String str2, String str3, String str4, String str5, boolean transition, boolean viewed, boolean nsfw, int sourceWidth, int sourceHeight, int startVideoPosition, int domColor) {
        Intent intent = new Intent(context, (Class<?>) SingleImageActivity.class);
        putExtras(intent, str, str2, str3, str4, str5, transition, viewed, nsfw, sourceWidth, sourceHeight, startVideoPosition, domColor);
        return intent;
    }

    public static Intent createGalleryIntent(Context context, String[] strArr, String[] strArr2, int position) {
        Intent intent = new Intent(context, (Class<?>) MultiImageActivity.class);
        intent.putExtra("urls_content", strArr);
        intent.putExtra("descriptions", strArr2);
        intent.putExtra("position", position);
        return intent;
    }

    private void applyFullscreenFlags() {
        try {
            getWindow().addFlags(-2147483520);
            getWindow().clearFlags(67108864);
        } catch (Exception unused) {
        }
    }

    public static void putExtras(Intent intent, String str, String str2, String str3, String str4, String str5, boolean transition, boolean viewed, boolean nsfw, int sourceWidth, int sourceHeight, int startVideoPosition, int domColor) {
        if (!TextUtils.isEmpty(str)) {
            intent.putExtra("subreddit", str);
        }
        intent.putExtra("url_content", str2);
        if (!TextUtils.isEmpty(str3)) {
            intent.putExtra("url_preview", str3);
        }
        if (!TextUtils.isEmpty(str4)) {
            intent.putExtra("url_title", str4);
        }
        if (!TextUtils.isEmpty(str5)) {
            intent.putExtra("comments_url", str5);
        }
        intent.putExtra("transition", transition);
        intent.putExtra("viewed", viewed);
        intent.putExtra("nsfw", nsfw);
        intent.putExtra("source_width", sourceWidth);
        intent.putExtra("source_height", sourceHeight);
        intent.putExtra("start_video_position", startVideoPosition);
        intent.putExtra("dom_color", domColor);
    }

    protected void readIntentExtras(Bundle bundle) {
        this.subreddit = getIntent().getStringExtra("subreddit");
        this.contentUrl = getIntent().getStringExtra("url_content");
        this.viewed = getIntent().getBooleanExtra("viewed", false);
        this.nsfw = getIntent().getBooleanExtra("nsfw", false);
        this.sourceWidth = getIntent().getIntExtra("source_width", WindowUtils.getDisplayWidth());
        this.sourceHeight = getIntent().getIntExtra("source_height", WindowUtils.getDisplayHeight());
        setTransitionEnabled(getIntent().getBooleanExtra("transition", false));
        if (bundle != null) {
            setTransitionEnabled(false);
        }
        this.previewUrl = getIntent().getStringExtra("url_preview");
        this.title = getIntent().getStringExtra("url_title");
        this.commentsUrl = getIntent().getStringExtra("comments_url");
        Logger.verbose("Comments URL: " + this.commentsUrl);
        this.startVideoPosition = getIntent().getIntExtra("start_video_position", 0);
        getIntent().removeExtra("start_video_position");
    }

    protected String getCommentsUrl() {
        return this.commentsUrl;
    }

    protected String getContentUrl() {
        return this.contentUrl;
    }

    protected int getPosition() {
        return this.position;
    }

    public int getDomColor() {
        if (!SettingsSingleton.getSettings().imageColorfulBackground || getIntent() == null) {
            return -16777216;
        }
        return getIntent().getIntExtra("dom_color", -16777216);
    }

    protected String getTitle2() {
        return this.title;
    }

    public boolean isNsfw() {
        return this.nsfw;
    }

    protected boolean isViewed() {
        return this.viewed;
    }

    protected String getPreviewUrl() {
        return this.previewUrl;
    }

    protected int getStartVideoPosition() {
        return this.startVideoPosition;
    }

    protected String getSubreddit() {
        return this.subreddit;
    }

    public boolean isTransitionEnabled() {
        return this.transitionEnabled;
    }

    public void close() {
        ActivityCompat.finishAfterTransition(this);
        if (isTransitionEnabled()) {
            return;
        }
        getActivity().overridePendingTransition(R.anim.video_enter, R.anim.video_exit);
    }

    protected void setPosition(int position) {
        this.position = position;
    }

    public void setTransitionEnabled(boolean enabled) {
        Logger.verbose("Setting transition enabled: " + enabled);
        this.transitionEnabled = enabled;
        getIntent().putExtra("transition", enabled);
    }

    @Override // com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.savedState = bundle;
        BusSingleton.getBus().register(this);
        readIntentExtras(bundle);
        if (SettingsSingleton.getSettings().forceAutoRotate) {
            setRequestedOrientation(4);
        }
        if (!GestureNavigationHelper.isGestureNavEnabled(this)) {
            getWindow().addFlags(-2147483520);
            getWindow().clearFlags(67108864);
            getWindow().getDecorView().setSystemUiVisibility(1280);
        } else if (ColorUtils.isDark(getDomColor())) {
            getWindow().getDecorView().setSystemUiVisibility(1792);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(9984);
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image, menu);
        return true;
    }

    @Override // com.resync.app.ui.activities.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        BusSingleton.getBus().unregister(this);
        super.onDestroy();
    }

    @Override // com.resync.app.ui.activities.BaseActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() != 4) {
            return super.onKeyUp(keyCode, keyEvent);
        }
        close();
        return true;
    }

    @Override // com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        clearKeepScreenOn();
    }

    @Override // com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        applyFullscreenFlags();
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected void setupContentView() {
        ButterKnife.bind(this);
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected void setupWindowBackground() {
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected void setupToolbar() {
        super.setupToolbar();
        this.toolbar.setNavigationIcon(new ColorDrawable(0));
        this.toolbar.setBackgroundColor(0);
        this.toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Dark_ActionBar);
        // Hide the Toolbar's navigation button. androidx.appcompat.Toolbar exposes no public
        // accessor for the nav button view, so reflect into its private field. (The original APK
        // used the R8-minified name "q"; "mNavButtonView" is the real field name in appcompat 1.6.1.)
        try {
            Field navButtonField = Toolbar.class.getDeclaredField("mNavButtonView");
            navButtonField.setAccessible(true);
            ((ImageButton) navButtonField.get(this.toolbar)).setVisibility(8);
        } catch (ReflectiveOperationException | ClassCastException e) {
            Logger.logException(e);
        }
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected void setupNavigationBar() {
        if (GestureNavigationHelper.isGestureNavEnabled(this)) {
            getWindow().setNavigationBarColor(0);
        } else {
            getWindow().setNavigationBarColor(getDomColor());
            this.mAdWrapper.setBackgroundColor(getDomColor());
        }
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    public void setupStatusBar() {
        getWindow().setStatusBarColor(0);
    }
}
