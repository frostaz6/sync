package com.resync.app.fragments.preferences;

import android.content.Intent;
import android.os.Bundle;
import androidx.preference.Preference;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.RedditHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.resync.app.R;
import com.resync.app.jobs.CheckSubredditsJob;
import com.resync.app.jobs.WatchSubredditJob;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.activities.TestDialogActivity;
import com.resync.app.ui.activities.TestMarkdownActivity;
import com.resync.app.ui.preferences.defaults.SyncPreference;
import com.resync.app.workers.ClearCacheWorker;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.mp4parser.boxes.iso14496.part12.SubSampleInformationBox;
import com.resync.app.dialogs.DiscordBottomSheetFragment;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.security.KeyManager;
import com.resync.app.premium.UltraHelper;
import com.resync.app.helpers.messaging.MessageSchedulingHelper;

/* JADX INFO: renamed from: pa.i0 */
/* JADX INFO: compiled from: PreferencesDeveloperFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesDeveloperFragment extends BasePreferencesFragment {

    /* JADX INFO: renamed from: pa.i0$a */
    /* JADX INFO: compiled from: PreferencesDeveloperFragment.java */
    class FcmFailureListener implements OnFailureListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ SyncPreference preference;

        FcmFailureListener(SyncPreference syncPreference) {
            this.preference = syncPreference;
        }

        @Override // com.google.android.gms.tasks.OnFailureListener
        public void onFailure(Exception exc) {
            this.preference.setSummary("unspecified");
        }
    }

    /* JADX INFO: renamed from: pa.i0$b */
    /* JADX INFO: compiled from: PreferencesDeveloperFragment.java */
    class FcmSuccessListener implements OnSuccessListener<InstanceIdResult> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ SyncPreference preference;

        FcmSuccessListener(SyncPreference syncPreference) {
            this.preference = syncPreference;
        }

        @Override // com.google.android.gms.tasks.OnSuccessListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onSuccess(InstanceIdResult instanceIdResult) {
            this.preference.setSummary(instanceIdResult.getToken());
        }
    }

    /* JADX INFO: renamed from: pa.i0$c */
    /* JADX INFO: compiled from: PreferencesDeveloperFragment.java */
    class CacheStatusListener implements Response.Listener<String> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ SyncPreference preference;

        CacheStatusListener(SyncPreference syncPreference) {
            this.preference = syncPreference;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String str) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                this.preference.setSummary(jSONObject.getInt("files") + " files - " + jSONObject.getString("size") + " total");
            } catch (Exception unused) {
                this.preference.setSummary("Error loading cache status");
            }
        }
    }

    /* JADX INFO: renamed from: pa.i0$d */
    /* JADX INFO: compiled from: PreferencesDeveloperFragment.java */
    class CacheStatusErrorListener implements Response.ErrorListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ SyncPreference preference;

        CacheStatusErrorListener(SyncPreference syncPreference) {
            this.preference = syncPreference;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            this.preference.setSummary("Error loading cache status");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onCopyFcm(SyncPreference syncPreference, Preference preference) {
        RedditHelper.copyToClipboard(getContext(), syncPreference.getTitle(), true);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onDiscordDemo(Preference preference) {
        FragmentUtils.show(DiscordBottomSheetFragment.class, getParentFragmentManager());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onDialogsDemo(Preference preference) {
        startActivity(new Intent(getActivity(), (Class<?>) TestDialogActivity.class));
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onMarkdownTests(Preference preference) {
        startActivity(new Intent(getActivity(), (Class<?>) TestMarkdownActivity.class));
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onScheduleMessaging(Preference preference) {
        StaticToast.showWithContext(getActivity(), "Scheduling messaging worker");
        MessageSchedulingHelper.scheduleMessages(getActivity());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onScheduleModMessaging(Preference preference) {
        StaticToast.showWithContext(getActivity(), "Scheduling mod messaging worker");
        MessageSchedulingHelper.scheduleModMessages(getActivity());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onCheckSubreddits(Preference preference) {
        StaticToast.showWithContext(getActivity(), "Scheduling check subreddits worker");
        CheckSubredditsJob.schedule();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onCheckWatched(Preference preference) {
        StaticToast.showWithContext(getActivity(), "Scheduling check watched subreddits worker");
        WatchSubredditJob.schedule(getActivity());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onClearCache(Preference preference) {
        StaticToast.showWithContext(getActivity(), "Clearing cache");
        ClearCacheWorker.enqueue(getContext());
        return true;
    }

    public static PreferencesDeveloperFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesDeveloperFragment preferencesDeveloperFragment = new PreferencesDeveloperFragment();
        preferencesDeveloperFragment.setArguments(bundle);
        return preferencesDeveloperFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.cat_developer);
        ((SyncPreference) findPreference("ultra_status")).setSummary(UltraHelper.isUltra() ? !StringUtils.containsIgnoreCase(PreferencesHelper.getDefaultPrefs("UltraHelper").getString(KeyManager.purchaseTokenKey(), null), ".AO-") ? "Invalid purchase token detected" : "Subscribed" : "Not subscribed");
        SyncPreference syncPreference = (SyncPreference) findPreference("install_status");
        try {
            String installingPackageName = getActivity().getPackageManager().getInstallSourceInfo("com.resync.app").getInstallingPackageName();
            if (StringUtils.isEmpty(installingPackageName)) {
                syncPreference.setSummary("None specified");
            } else {
                syncPreference.setSummary(installingPackageName);
            }
        } catch (Exception unused) {
            syncPreference.setSummary("Error loading status");
        }
        final SyncPreference syncPreference2 = (SyncPreference) findPreference("fcm");
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new FcmSuccessListener(syncPreference2)).addOnFailureListener(new FcmFailureListener(syncPreference2));
        syncPreference2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: pa.h0
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesDeveloperFragment.this.onCopyFcm(syncPreference2, preference);
            }
        });
        ((SyncPreference) findPreference("discord_demo")).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: pa.b0
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesDeveloperFragment.this.onDiscordDemo(preference);
            }
        });
        ((SyncPreference) findPreference("dialogs_demo")).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: pa.z
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesDeveloperFragment.this.onDialogsDemo(preference);
            }
        });
        ((SyncPreference) findPreference("markdown_tests")).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: pa.d0
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesDeveloperFragment.this.onMarkdownTests(preference);
            }
        });
        SyncPreference syncPreference3 = (SyncPreference) findPreference("messaging");
        syncPreference3.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: pa.a0
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesDeveloperFragment.this.onScheduleMessaging(preference);
            }
        });
        if (!AccountSingleton.isLoggedIn(getActivity())) {
            syncPreference3.setEnabled(false);
        }
        SyncPreference syncPreference4 = (SyncPreference) findPreference("mod_messaging");
        syncPreference4.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: pa.g0
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesDeveloperFragment.this.onScheduleModMessaging(preference);
            }
        });
        if (!AccountSingleton.isLoggedIn(getActivity())) {
            syncPreference4.setEnabled(false);
        }
        SyncPreference syncPreference5 = (SyncPreference) findPreference(SubSampleInformationBox.TYPE);
        syncPreference5.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: pa.c0
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesDeveloperFragment.this.onCheckSubreddits(preference);
            }
        });
        if (!AccountSingleton.isLoggedIn(getActivity())) {
            syncPreference5.setEnabled(false);
        }
        SyncPreference syncPreference6 = (SyncPreference) findPreference("watched");
        syncPreference6.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: pa.e0
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesDeveloperFragment.this.onCheckWatched(preference);
            }
        });
        if (!AccountSingleton.isLoggedIn(getActivity())) {
            syncPreference6.setEnabled(false);
        }
        SyncPreference syncPreference7 = (SyncPreference) findPreference("cache");
        syncPreference7.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: pa.f0
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                return PreferencesDeveloperFragment.this.onClearCache(preference);
            }
        });
        if (!AccountSingleton.isLoggedIn(getActivity())) {
            syncPreference7.setEnabled(false);
        }
        SyncPreference syncPreference8 = (SyncPreference) findPreference("cache_status");
        StringRequest stringRequest = new StringRequest(0, "https://ap.syncforreddit.com/stats.json", new CacheStatusListener(syncPreference8), new CacheStatusErrorListener(syncPreference8));
        stringRequest.setShouldCache(false);
        OAuthRequestManager.enqueueAux(stringRequest);
        findPreference("checks").setVisible(true);
    }
}
