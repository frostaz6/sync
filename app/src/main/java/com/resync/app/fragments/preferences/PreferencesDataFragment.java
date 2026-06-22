package com.resync.app.fragments.preferences;

import android.os.Bundle;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import com.resync.app.R;

/* JADX INFO: renamed from: pa.y */
/* JADX INFO: compiled from: PreferencesDataFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesDataFragment extends BasePreferencesFragment {
    public static PreferencesDataFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesDataFragment preferencesDataFragment = new PreferencesDataFragment();
        preferencesDataFragment.setArguments(bundle);
        return preferencesDataFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_data);
        super.onCreatePreferences(bundle, str);
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment
    public SimplePreference[] getSummaryPreferences() {
        return new SimplePreference[]{new SimplePreference("images_preload", "0", R.array.images_preload_types), new SimplePreference("preview_preload", "0", R.array.images_preload_types), new SimplePreference("thumbs_preload", "0", R.array.images_preload_types), new SimplePreference("gifs_preload", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE, R.array.images_preload_types), new SimplePreference("reddit_video_quality_new", "2", R.array.images_preload_types), new SimplePreference("autoplay", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE, R.array.images_preload_types)};
    }
}
