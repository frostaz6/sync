package com.resync.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import androidx.fragment.app.Fragment;
import com.resync.app.R;
import com.resync.app.ui.fragments.preferences.PreferenceWrapperFragment;
import com.resync.app.ui.fragments.preferences.SearchPreferenceWrapperFragment;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.ui.TabletHelper;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public class PreferencesActivity extends BaseMaterialDragActivity {
    private void showSearchInPane() {
        getSupportFragmentManager().beginTransaction().replace(R.id.second_pane, SearchPreferenceWrapperFragment.newInstance()).commit();
        Fragment fragmentFindFragmentById = getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragmentFindFragmentById instanceof PreferenceWrapperFragment) {
            ((PreferenceWrapperFragment) fragmentFindFragmentById).setHighlight(null);
        }
    }

    @Override // com.resync.app.ui.activities.BaseMaterialActivity
    protected int getNavigationBarColor(boolean night) {
        return MonetUtil.getDividerColorAuto();
    }

    @Override // com.resync.app.ui.activities.BaseMaterialActivity
    protected void rebuildTheme() {
        super.rebuildTheme();
        Fragment fragmentFindFragmentById = getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragmentFindFragmentById instanceof PreferenceWrapperFragment) {
            ((PreferenceWrapperFragment) fragmentFindFragmentById).onDarkModeChanged();
        }
        Fragment fragmentFindFragmentById2 = getSupportFragmentManager().findFragmentById(R.id.second_pane);
        if (fragmentFindFragmentById2 instanceof PreferenceWrapperFragment) {
            ((PreferenceWrapperFragment) fragmentFindFragmentById2).onDarkModeChanged();
        }
    }

    public void openMode(int mode) {
        openModeHighlight(mode, null);
    }

    public void openModeHighlight(int mode, String str) {
        if (!TabletHelper.isTabletMode()) {
            Intent intent = new Intent(getContext(), (Class<?>) PreferencesActivity.class);
            intent.putExtra("mode", mode);
            intent.putExtra("highlight", str);
            startActivity(intent);
            return;
        }
        try {
            ((InputMethodManager) getContext().getSystemService("input_method")).hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        } catch (Exception e) {
            Logger.logException(e);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.second_pane, PreferenceWrapperFragment.newInstance(mode, str)).commit();
        Fragment fragmentFindFragmentById = getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragmentFindFragmentById instanceof PreferenceWrapperFragment) {
            ((PreferenceWrapperFragment) fragmentFindFragmentById).setHighlight(Integer.toString(mode));
        }
    }

    public void openSearch() {
        if (TabletHelper.isTabletMode()) {
            showSearchInPane();
        } else {
            getContext().startActivity(new Intent(getContext(), (Class<?>) PreferencesSearchActivity.class));
        }
    }

    @Override // com.resync.app.widgets.drag.DragActivity
    public int getDragLayoutRes() {
        return TabletHelper.isTabletMode() ? R.layout.activity_preferences_tablet : R.layout.activity_preferences;
    }

    @Override // com.resync.app.ui.interfaces.SnackbarInterface
    public boolean isContentLoading() {
        return false;
    }

    @Override // com.resync.app.ui.activities.BaseMaterialDragActivity, com.resync.app.ui.activities.BaseMaterialActivity, com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        int intExtra = getIntent().getIntExtra("mode", getResources().getInteger(R.integer.ROOT));
        String stringExtra = getIntent().getStringExtra("highlight");
        try {
            Intent intent = getIntent();
            if ("android.intent.action.VIEW".equals(intent.getAction())) {
                String strReplace = intent.getData().toString().replace("sync-settings://", "");
                intExtra = Integer.parseInt(strReplace.split("-")[0]);
                stringExtra = strReplace.split("-")[1];
            }
        } catch (Exception unused) {
            StaticToast.show("Error loading linked setting");
        }
        if (bundle == null) {
            if (!TabletHelper.isTabletMode()) {
                getSupportFragmentManager().beginTransaction().add(R.id.content, PreferenceWrapperFragment.newInstance(intExtra, stringExtra)).commit();
                return;
            }
            if (intExtra == getResources().getInteger(R.integer.ROOT)) {
                getSupportFragmentManager().beginTransaction().add(R.id.content, PreferenceWrapperFragment.newInstance(getResources().getInteger(R.integer.ROOT), Integer.toString(getResources().getInteger(R.integer.GENERAL)))).commit();
                openMode(getResources().getInteger(R.integer.GENERAL));
            } else {
                getSupportFragmentManager().beginTransaction().add(R.id.content, PreferenceWrapperFragment.newInstance(getResources().getInteger(R.integer.ROOT), Integer.toString(intExtra))).commit();
                openMode(intExtra);
            }
        }
    }
}
