package com.resync.app.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.ui.activities.CasualActivity;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.mp4parser.boxes.iso14496.part12.FreeBox;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.BackersSelectionDialogBottomSheet;
import com.resync.app.dialogs.RatingAlertDialogBottomSheet;
import com.resync.app.util.links.LinkHelper;

/* JADX INFO: renamed from: pa.w0 */
/* JADX INFO: compiled from: PreferencesOtherFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesOtherFragment extends BasePreferencesFragment {

    /* JADX INFO: renamed from: pa.w0$a */
    /* JADX INFO: compiled from: PreferencesOtherFragment.java */
    class BackersClickListener implements Preference.OnPreferenceClickListener {
        BackersClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            FragmentUtils.show(BackersSelectionDialogBottomSheet.class, PreferencesOtherFragment.this.getChildFragmentManager());
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.w0$b */
    /* JADX INFO: compiled from: PreferencesOtherFragment.java */
    class AboutClickListener implements Preference.OnPreferenceClickListener {
        AboutClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            LinkHelper.open(PreferencesOtherFragment.this.getActivity(), "/r/redditsync/comments/11529s5");
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.w0$c */
    /* JADX INFO: compiled from: PreferencesOtherFragment.java */
    class LicensesClickListener implements Preference.OnPreferenceClickListener {
        LicensesClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            LinkHelper.open(PreferencesOtherFragment.this.getActivity(), "https://todo.syncforreddit.com/licenses.html");
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.w0$d */
    /* JADX INFO: compiled from: PreferencesOtherFragment.java */
    class FeedbackClickListener implements Preference.OnPreferenceClickListener {
        FeedbackClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            CasualActivity.openSubreddit(PreferencesOtherFragment.this.getActivity(), "/r/redditsync");
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.w0$e */
    /* JADX INFO: compiled from: PreferencesOtherFragment.java */
    class RateClickListener implements Preference.OnPreferenceClickListener {
        RateClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            FragmentUtils.show(RatingAlertDialogBottomSheet.class, PreferencesOtherFragment.this.getActivity().getSupportFragmentManager());
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.w0$f */
    /* JADX INFO: compiled from: PreferencesOtherFragment.java */
    class CreditDevClickListener implements Preference.OnPreferenceClickListener {
        CreditDevClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            LinkHelper.open(PreferencesOtherFragment.this.getActivity(), "/r/redditsync");
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onModClick(Preference preference) {
        LinkHelper.open(getActivity(), preference.getTitle().toString());
        return true;
    }

    public static PreferencesOtherFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesOtherFragment preferencesOtherFragment = new PreferencesOtherFragment();
        preferencesOtherFragment.setArguments(bundle);
        return preferencesOtherFragment;
    }

    private void addModPreference(String str, String str2) {
        findPreference(str).setSummary(str2);
        findPreference(str).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: pa.v0
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesOtherFragment.this.onModClick(preference);
            }
        });
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_other);
        findPreference("backers").setOnPreferenceClickListener(new BackersClickListener());
        findPreference("about_preference").setTitle("Sync for Reddit " + StringUtils.capitalize(FreeBox.TYPE));
        findPreference("about_preference").setSummary("v23.06.30-13:39 (23033)");
        findPreference("about_preference").setOnPreferenceClickListener(new AboutClickListener());
        findPreference("licenses_preference").setOnPreferenceClickListener(new LicensesClickListener());
        findPreference("feedback_preference").setOnPreferenceClickListener(new FeedbackClickListener());
        findPreference("rate_preference").setOnPreferenceClickListener(new RateClickListener());
        ArrayList arrayList = new ArrayList();
        arrayList.add("/u/elchaghi");
        arrayList.add("/u/brett1337");
        arrayList.add("/u/Featherstoned");
        arrayList.add("/u/Quinny898");
        arrayList.add("/u/Felimenta970");
        arrayList.add("/u/AMO124");
        arrayList.add("/u/abhijeeeeeet");
        Collections.shuffle(arrayList);
        for (int i = 0; i < arrayList.size(); i++) {
            addModPreference("mod_" + i, (String) arrayList.get(i));
        }
    }
}
