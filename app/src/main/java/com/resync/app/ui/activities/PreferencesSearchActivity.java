package com.resync.app.ui.activities;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.resync.app.R;
import com.resync.app.ui.fragments.preferences.SearchPreferenceWrapperFragment;
import com.resync.app.helpers.FragmentHelper;

/* JADX INFO: loaded from: classes.dex */
public class PreferencesSearchActivity extends BaseMaterialDragActivity {
    @Override // com.resync.app.ui.activities.BaseMaterialActivity
    protected void rebuildTheme() {
        super.rebuildTheme();
        Fragment fragmentFindFragmentById = getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragmentFindFragmentById instanceof SearchPreferenceWrapperFragment) {
            ((SearchPreferenceWrapperFragment) fragmentFindFragmentById).onDarkModeChanged();
        }
    }

    public int getContentId() {
        return R.id.content;
    }

    @Override // com.resync.app.widgets.drag.DragActivity
    public int getDragLayoutRes() {
        return R.layout.activity_search_preferences;
    }

    @Override // com.resync.app.ui.interfaces.SnackbarInterface
    public boolean isContentLoading() {
        return false;
    }

    @Override // com.resync.app.ui.activities.BaseMaterialDragActivity, com.resync.app.ui.activities.BaseMaterialActivity, com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            FragmentHelper.replaceFragment(this, SearchPreferenceWrapperFragment.newInstance(), getContentId());
        }
    }
}
