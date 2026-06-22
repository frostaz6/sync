package com.resync.app.fragments.preferences;

import android.os.Bundle;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import com.resync.app.R;

/* JADX INFO: renamed from: pa.w */
/* JADX INFO: compiled from: PreferencesCommentsFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesCommentsFragment extends BasePreferencesFragment {
    public static PreferencesCommentsFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesCommentsFragment preferencesCommentsFragment = new PreferencesCommentsFragment();
        preferencesCommentsFragment.setArguments(bundle);
        return preferencesCommentsFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_comments);
        super.onCreatePreferences(bundle, str);
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment
    public SimplePreference[] getSummaryPreferences() {
        return new SimplePreference[]{new SimplePreference("comment_sort_preference", "6", R.array.comment_sort), new SimplePreference("comment_navigation_position", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE, R.array.nav_position), new SimplePreference("comment_navigation_first", "0", R.array.comment_swipe), new SimplePreference("comment_navigation_second", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE, R.array.comment_swipe), new SimplePreference("comment_navigation_third", "2", R.array.comment_swipe)};
    }
}
