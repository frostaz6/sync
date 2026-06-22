package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.helpers.ui.StaticToast;
import java.io.IOException;
import com.resync.app.net.OkHttpHelper;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.MediaLocationSelectionBottomSheetFragment;
import com.resync.app.util.Logger;
import com.resync.app.media.glide.GlideHelper;

/* JADX INFO: renamed from: pa.n0 */
/* JADX INFO: compiled from: PreferencesImagesFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesImagesFragment extends BasePreferencesFragment {

    /* JADX INFO: renamed from: pa.n0$a */
    /* JADX INFO: compiled from: PreferencesImagesFragment.java */
    class MediaDirectoryClickListener implements Preference.OnPreferenceClickListener {
        MediaDirectoryClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            FragmentUtils.showWithArguments(MediaLocationSelectionBottomSheetFragment.class, PreferencesImagesFragment.this.getChildFragmentManager(), MediaLocationSelectionBottomSheetFragment.buildArguments(null, null, null));
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.n0$b */
    /* JADX INFO: compiled from: PreferencesImagesFragment.java */
    class ClearCacheClickListener implements Preference.OnPreferenceClickListener {

        /* JADX INFO: renamed from: pa.n0$b$a */
        /* JADX INFO: compiled from: PreferencesImagesFragment.java */
        class ClearCacheThread extends Thread {
            ClearCacheThread() {
            }

            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    if (OkHttpHelper.getNoRedirectClient() != null && OkHttpHelper.getNoRedirectClient().cache() != null) {
                        Logger.verbose("CLEARED IMAGE CACHE");
                        OkHttpHelper.getNoRedirectClient().cache().evictAll();
                    }
                    if (OkHttpHelper.getDefaultClient() == null || OkHttpHelper.getDefaultClient().cache() == null) {
                        return;
                    }
                    Logger.verbose("CLEARED GLIDE CACHE");
                    OkHttpHelper.getNoRedirectClient().cache().evictAll();
                } catch (IOException e) {
                    Logger.logException(e);
                }
            }
        }

        ClearCacheClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            new ClearCacheThread().start();
            StaticToast.showWithContext(PreferencesImagesFragment.this.getActivity(), "Image cache cleared!");
            GlideHelper.clearCacheTwice();
            return true;
        }
    }

    public static PreferencesImagesFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesImagesFragment preferencesImagesFragment = new PreferencesImagesFragment();
        preferencesImagesFragment.setArguments(bundle);
        return preferencesImagesFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_images);
        findPreference("directory_media").setOnPreferenceClickListener(new MediaDirectoryClickListener());
        findPreference("cache_manual").setOnPreferenceClickListener(new ClearCacheClickListener());
    }
}
