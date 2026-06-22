package com.resync.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.resync.app.R;
import com.resync.app.ui.fragments.preferences.filters.FilterFragment;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.widgets.drag.DragManager;
import com.resync.app.widgets.drag.DragManagerImpl;
import com.resync.app.helpers.FragmentHelper;

/* JADX INFO: loaded from: classes.dex */
public class FilterActivity extends LegacyActivity {
    public static String EXTRA_MODE = "mode";
    public static int INVALID_MODE = -1;
    protected DragManager dragManager;

    public static Intent createIntent(Context context, int mode) {
        Intent intent = new Intent(context, (Class<?>) FilterActivity.class);
        intent.putExtra(EXTRA_MODE, mode);
        return intent;
    }

    @Override // com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        int intExtra = getIntent().getIntExtra(EXTRA_MODE, INVALID_MODE);
        if (intExtra == INVALID_MODE) {
            StaticToast.showWithContext(getActivity(), "Invalid mode");
            finish();
        }
        if (bundle == null) {
            FragmentHelper.replaceFragment(this, FilterFragment.newInstance(intExtra), R.id.content);
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
