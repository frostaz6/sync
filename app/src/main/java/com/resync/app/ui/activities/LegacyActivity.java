package com.resync.app.ui.activities;

import android.content.res.Configuration;
import com.resync.app.helpers.NightHelper;

/* JADX INFO: loaded from: classes.dex */
public class LegacyActivity extends BaseActivity {
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        NightHelper.refresh(false);
        recreate();
    }
}
