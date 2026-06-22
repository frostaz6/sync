package com.resync.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import com.resync.app.ui.activities.IntentActivity;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;
import com.resync.app.util.links.LinkHelper;

/* JADX INFO: loaded from: classes.dex */
public class IntentActivity extends AppCompatActivity {
    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean keepSplash() {
        return true;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SplashScreen.installSplashScreen(this).setKeepOnScreenCondition(new SplashScreen.KeepOnScreenCondition() { // from class: m9.f
            @Override // androidx.core.splashscreen.SplashScreen.KeepOnScreenCondition
            public final boolean shouldKeepOnScreen() {
                return IntentActivity.keepSplash();
            }
        });
        String dataString = getIntent().getDataString();
        if (TextUtils.isEmpty(dataString)) {
            dataString = getIntent().getStringExtra("android.intent.extra.TEXT");
        }
        Logger.verbose("FOUND URL: " + dataString);
        if (isTaskRoot()) {
            Intent intent = new Intent(this, (Class<?>) HomeActivity.class);
            intent.putExtra(MainActivity.EXTRA_LINK, dataString);
            startActivity(intent);
        } else {
            LinkHelper.open(this, dataString);
        }
        finish();
    }
}
