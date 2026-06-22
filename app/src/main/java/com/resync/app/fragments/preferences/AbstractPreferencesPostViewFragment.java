package com.resync.app.fragments.preferences;

import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import com.resync.app.ui.preferences.custom.ViewTypePreviewPreference;
import com.resync.app.fragments.preferences.BasePreferencesFragment;

/* JADX INFO: renamed from: ra.a */
/* JADX INFO: compiled from: AbstractPreferencesPostViewFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class AbstractPreferencesPostViewFragment extends BasePreferencesFragment {
    private ViewTypePreviewPreference previewPreference;

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean notifyPreviewChanged() {
        this.previewPreference.refresh();
        return true;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.previewPreference = (ViewTypePreviewPreference) findPreference(ViewTypePreviewPreference.KEY);
    }
}
