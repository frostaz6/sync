package com.resync.app.fragments.preferences;

import android.content.Intent;
import android.os.Bundle;
import androidx.preference.Preference;
import com.resync.app.R;
import com.resync.app.ui.preferences.PushPreference;
import com.resync.app.helpers.NotificationHelper;
import com.resync.app.helpers.PreferencesHelper;

/* JADX INFO: renamed from: pa.q0 */
/* JADX INFO: compiled from: PreferencesMessagingFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesMessagingFragment extends BasePreferencesFragment {

    /* JADX INFO: renamed from: pa.q0$a */
    /* JADX INFO: compiled from: PreferencesMessagingFragment.java */
    class MessagePreferenceChangeListener implements Preference.OnPreferenceChangeListener {
        MessagePreferenceChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            PreferencesMessagingFragment.this.updateMessageSummary(obj);
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.q0$b */
    /* JADX INFO: compiled from: PreferencesMessagingFragment.java */
    class MessagingSystemClickListener implements Preference.OnPreferenceClickListener {
        MessagingSystemClickListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            PreferencesMessagingFragment.this.startActivity(new Intent("android.settings.APP_NOTIFICATION_SETTINGS").putExtra("android.provider.extra.APP_PACKAGE", PreferencesMessagingFragment.this.getContext().getPackageName()).putExtra("android.provider.extra.CHANNEL_ID", NotificationHelper.getMessagingChannel()));
            return true;
        }
    }

    public static PreferencesMessagingFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesMessagingFragment preferencesMessagingFragment = new PreferencesMessagingFragment();
        preferencesMessagingFragment.setArguments(bundle);
        return preferencesMessagingFragment;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMessageSummary(Object obj) {
        findPreference("message_preference_new").setSummary(getResources().getStringArray(R.array.message_types)[Integer.parseInt(obj != null ? (String) obj : PreferencesHelper.getAccountPrefs("PreferencesMessagingFragment").getString("message_preference_new", "3"))]);
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_messaging);
        findPreference("message_preference_new").setOnPreferenceChangeListener(new MessagePreferenceChangeListener());
        updateMessageSummary(null);
        if (findPreference("messaging_system") != null) {
            findPreference("messaging_system").setOnPreferenceClickListener(new MessagingSystemClickListener());
        }
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: k2 */
    public void onResume() {
        super.onResume();
        ((PushPreference) findPreference(PushPreference.KEY)).refresh();
    }
}
