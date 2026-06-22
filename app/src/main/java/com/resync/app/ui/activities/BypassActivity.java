package com.resync.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.resync.app.R;
import com.resync.app.ui.fragments.preferences.bypass.BypassFragment;
import com.resync.app.widgets.drag.DragManager;
import com.resync.app.widgets.drag.DragManagerImpl;
import com.resync.app.helpers.FragmentHelper;

/* JADX INFO: loaded from: classes.dex */
public class BypassActivity extends LegacyActivity {
    protected DragManager dragManager;

    public static Intent createIntent(Context context) {
        return new Intent(context, (Class<?>) BypassActivity.class);
    }

    @Override // com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            FragmentHelper.replaceFragment(this, BypassFragment.newInstance(), R.id.content);
        }
    }

    @Override // com.resync.app.ui.activities.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.dragManager.onDestroy();
    }

    @Override // com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.dragManager.refresh(this);
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected void setupContentView() {
        DragManagerImpl dragManagerImpl = new DragManagerImpl();
        this.dragManager = dragManagerImpl;
        dragManagerImpl.attach(this, R.layout.activity_basic);
    }
}
