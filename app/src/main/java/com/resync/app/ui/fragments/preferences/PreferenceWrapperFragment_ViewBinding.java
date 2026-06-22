package com.resync.app.ui.fragments.preferences;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.sections.preferences.toolbar.PreferencesSmallSubredditToolbarView;
import com.resync.app.ui.views.core.MaterialYouToolbar;
import com.resync.app.ui.views.transparent_status.CustomAppBarLayout;
import com.resync.app.ui.views.transparent_status.CustomCoordinatorLayout;

/* JADX INFO: loaded from: classes2.dex */
public class PreferenceWrapperFragment_ViewBinding implements Unbinder {
    private PreferenceWrapperFragment target;

    public PreferenceWrapperFragment_ViewBinding(PreferenceWrapperFragment preferenceWrapperFragment, View view) {
        this.target = preferenceWrapperFragment;
        preferenceWrapperFragment.mCoordinatorLayout = (CustomCoordinatorLayout) Utils.findRequiredViewAsType(view, R.id.coordinator, "field 'mCoordinatorLayout'", CustomCoordinatorLayout.class);
        preferenceWrapperFragment.mAppBarLayout = (CustomAppBarLayout) Utils.findRequiredViewAsType(view, R.id.appbar, "field 'mAppBarLayout'", CustomAppBarLayout.class);
        preferenceWrapperFragment.mToolbar = (MaterialYouToolbar) Utils.findRequiredViewAsType(view, R.id.toolbar, "field 'mToolbar'", MaterialYouToolbar.class);
        preferenceWrapperFragment.mPreferencesSmallSubredditToolbarView = (PreferencesSmallSubredditToolbarView) Utils.findRequiredViewAsType(view, R.id.toolbar_small, "field 'mPreferencesSmallSubredditToolbarView'", PreferencesSmallSubredditToolbarView.class);
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        PreferenceWrapperFragment preferenceWrapperFragment = this.target;
        if (preferenceWrapperFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        preferenceWrapperFragment.mCoordinatorLayout = null;
        preferenceWrapperFragment.mAppBarLayout = null;
        preferenceWrapperFragment.mToolbar = null;
        preferenceWrapperFragment.mPreferencesSmallSubredditToolbarView = null;
    }
}
