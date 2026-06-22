package com.resync.app.fragments.preferences;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.R;
import com.resync.app.ui.preferences.defaults.SyncCheckBoxPreference;
import com.resync.app.helpers.ui.AlertDialogHelper;
import com.resync.app.helpers.ui.ProgressDialogHelper;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.net.LiveRedditPreferences;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.util.Logger;
import com.resync.app.net.oauth.OAuthGetUserPrefsRequest;
import com.resync.app.net.oauth.OAuthSetPrefRequest;

/* JADX INFO: renamed from: pa.a1 */
/* JADX INFO: compiled from: PreferencesRedditWebPreferencesFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class PreferencesRedditWebPreferencesFragment extends BasePreferencesFragment {
    private SyncCheckBoxPreference mediaOnPref;
    private SyncCheckBoxPreference mediaOffPref;
    private SyncCheckBoxPreference mediaSubredditPref;
    private SyncCheckBoxPreference linkCompressPref;
    private SyncCheckBoxPreference contentNsfwPref;
    private SyncCheckBoxPreference showPresencePref;
    private SyncCheckBoxPreference allowClickTrackingPref;

    /* JADX INFO: renamed from: pa.a1$a */
    /* JADX INFO: compiled from: PreferencesRedditWebPreferencesFragment.java */
    class MediaPreferenceChangeListener implements Preference.OnPreferenceChangeListener {

        /* JADX INFO: renamed from: pa.a1$a$a, reason: collision with other inner class name */
        /* JADX INFO: compiled from: PreferencesRedditWebPreferencesFragment.java */
        class SetMediaPrefClickListener implements DialogInterface.OnClickListener {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ String mediaValue;

            SetMediaPrefClickListener(String str) {
                this.mediaValue = str;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int which) {
                OAuthRequestManager.send(PreferencesRedditWebPreferencesFragment.this.getActivity(), new OAuthSetPrefRequest(PreferencesRedditWebPreferencesFragment.this.getActivity(), "media", this.mediaValue));
            }
        }

        MediaPreferenceChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            String str;
            if ((obj instanceof Boolean) && !((Boolean) obj).booleanValue()) {
                return false;
            }
            if (preference.equals(PreferencesRedditWebPreferencesFragment.this.mediaOnPref)) {
                PreferencesRedditWebPreferencesFragment.this.mediaOnPref.setChecked(true);
                PreferencesRedditWebPreferencesFragment.this.mediaOffPref.setChecked(false);
                PreferencesRedditWebPreferencesFragment.this.mediaSubredditPref.setChecked(false);
                str = "on";
            } else if (preference.equals(PreferencesRedditWebPreferencesFragment.this.mediaOffPref)) {
                PreferencesRedditWebPreferencesFragment.this.mediaOnPref.setChecked(false);
                PreferencesRedditWebPreferencesFragment.this.mediaOffPref.setChecked(true);
                PreferencesRedditWebPreferencesFragment.this.mediaSubredditPref.setChecked(false);
                str = "off";
            } else {
                if (!preference.equals(PreferencesRedditWebPreferencesFragment.this.mediaSubredditPref)) {
                    throw new RuntimeException("Unsupported type");
                }
                PreferencesRedditWebPreferencesFragment.this.mediaOnPref.setChecked(false);
                PreferencesRedditWebPreferencesFragment.this.mediaOffPref.setChecked(false);
                PreferencesRedditWebPreferencesFragment.this.mediaSubredditPref.setChecked(true);
                str = "subreddit";
            }
            AlertDialogHelper.newBuilder(PreferencesRedditWebPreferencesFragment.this.getContext()).setTitle("Media preference changed").setMessage("Your media preferences were updated. You'll need to refresh any active content for this to take effect.").setPositiveButton("OK", new SetMediaPrefClickListener(str)).show();
            return false;
        }
    }

    /* JADX INFO: renamed from: pa.a1$b */
    /* JADX INFO: compiled from: PreferencesRedditWebPreferencesFragment.java */
    class LinkCompressChangeListener implements Preference.OnPreferenceChangeListener {

        /* JADX INFO: renamed from: pa.a1$b$a */
        /* JADX INFO: compiled from: PreferencesRedditWebPreferencesFragment.java */
        class SetCompressPrefClickListener implements DialogInterface.OnClickListener {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ Object compressValue;

            SetCompressPrefClickListener(Object obj) {
                this.compressValue = obj;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int which) {
                OAuthRequestManager.send(PreferencesRedditWebPreferencesFragment.this.getActivity(), new OAuthSetPrefRequest(PreferencesRedditWebPreferencesFragment.this.getActivity(), "compress", this.compressValue));
            }
        }

        LinkCompressChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            AlertDialogHelper.newBuilder(PreferencesRedditWebPreferencesFragment.this.getContext()).setTitle("Link preference changed").setMessage("Your link preferences were updated. You'll need to refresh any active content for this to take effect.").setPositiveButton("OK", new SetCompressPrefClickListener(obj)).show();
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.a1$c */
    /* JADX INFO: compiled from: PreferencesRedditWebPreferencesFragment.java */
    class ContentNsfwChangeListener implements Preference.OnPreferenceChangeListener {

        /* JADX INFO: renamed from: pa.a1$c$a */
        /* JADX INFO: compiled from: PreferencesRedditWebPreferencesFragment.java */
        class SetNsfwPrefClickListener implements DialogInterface.OnClickListener {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ Object nsfwValue;

            SetNsfwPrefClickListener(Object obj) {
                this.nsfwValue = obj;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int which) {
                OAuthRequestManager.send(PreferencesRedditWebPreferencesFragment.this.getActivity(), new OAuthSetPrefRequest(PreferencesRedditWebPreferencesFragment.this.getActivity(), "search_include_over_18", this.nsfwValue));
            }
        }

        ContentNsfwChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            AlertDialogHelper.newBuilder(PreferencesRedditWebPreferencesFragment.this.getContext()).setTitle("Content preference changed").setMessage("Your content preferences were updated. You'll need to refresh any active content for this to take effect.").setPositiveButton("OK", new SetNsfwPrefClickListener(obj)).show();
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.a1$d */
    /* JADX INFO: compiled from: PreferencesRedditWebPreferencesFragment.java */
    class ShowPresenceChangeListener implements Preference.OnPreferenceChangeListener {
        ShowPresenceChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            OAuthRequestManager.send(PreferencesRedditWebPreferencesFragment.this.getActivity(), new OAuthSetPrefRequest(PreferencesRedditWebPreferencesFragment.this.getActivity(), "show_presence", obj));
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.a1$e */
    /* JADX INFO: compiled from: PreferencesRedditWebPreferencesFragment.java */
    class AllowClickTrackingChangeListener implements Preference.OnPreferenceChangeListener {
        AllowClickTrackingChangeListener() {
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            OAuthRequestManager.send(PreferencesRedditWebPreferencesFragment.this.getActivity(), new OAuthSetPrefRequest(PreferencesRedditWebPreferencesFragment.this.getActivity(), "allow_clicktracking", obj));
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.a1$f */
    /* JADX INFO: compiled from: PreferencesRedditWebPreferencesFragment.java */
    class FinishOnDisabledDismissListener implements DialogInterface.OnDismissListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ PreferenceScreen preferenceScreen;

        FinishOnDisabledDismissListener(PreferenceScreen preferenceScreen) {
            this.preferenceScreen = preferenceScreen;
        }

        @Override // android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialogInterface) {
            if (this.preferenceScreen.isEnabled()) {
                return;
            }
            PreferencesRedditWebPreferencesFragment.this.getActivity().finish();
        }
    }

    /* JADX INFO: renamed from: pa.a1$g */
    /* JADX INFO: compiled from: PreferencesRedditWebPreferencesFragment.java */
    class LivePreferencesResponseListener implements Response.Listener<LiveRedditPreferences> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ ProgressDialog progressDialog;
        final /* synthetic */ PreferenceScreen preferenceScreen;

        LivePreferencesResponseListener(ProgressDialog progressDialog, PreferenceScreen preferenceScreen) {
            this.progressDialog = progressDialog;
            this.preferenceScreen = preferenceScreen;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(LiveRedditPreferences liveRedditPreferences) {
            if (ContextHelper.isActivityFinishing(PreferencesRedditWebPreferencesFragment.this.getActivity())) {
                return;
            }
            try {
                this.progressDialog.dismiss();
                StaticToast.showWithContext(PreferencesRedditWebPreferencesFragment.this.getActivity(), "Fetched live preferences");
                this.preferenceScreen.setEnabled(true);
                if (liveRedditPreferences.mediaPreviewOn) {
                    PreferencesRedditWebPreferencesFragment.this.mediaOnPref.setChecked(true);
                    PreferencesRedditWebPreferencesFragment.this.mediaOffPref.setChecked(false);
                    PreferencesRedditWebPreferencesFragment.this.mediaSubredditPref.setChecked(false);
                } else if (liveRedditPreferences.mediaPreviewOff) {
                    PreferencesRedditWebPreferencesFragment.this.mediaOnPref.setChecked(false);
                    PreferencesRedditWebPreferencesFragment.this.mediaOffPref.setChecked(true);
                    PreferencesRedditWebPreferencesFragment.this.mediaSubredditPref.setChecked(false);
                } else if (liveRedditPreferences.mediaPreviewSubreddit) {
                    PreferencesRedditWebPreferencesFragment.this.mediaOnPref.setChecked(false);
                    PreferencesRedditWebPreferencesFragment.this.mediaOffPref.setChecked(false);
                    PreferencesRedditWebPreferencesFragment.this.mediaSubredditPref.setChecked(true);
                }
                PreferencesRedditWebPreferencesFragment.this.linkCompressPref.setChecked(liveRedditPreferences.compress);
                PreferencesRedditWebPreferencesFragment.this.contentNsfwPref.setChecked(liveRedditPreferences.searchIncludeOver18);
                PreferencesRedditWebPreferencesFragment.this.showPresencePref.setChecked(liveRedditPreferences.showPresence);
                PreferencesRedditWebPreferencesFragment.this.allowClickTrackingPref.setChecked(liveRedditPreferences.allowClickTracking);
            } catch (Exception e) {
                Logger.logException(e);
            }
        }
    }

    /* JADX INFO: renamed from: pa.a1$h */
    /* JADX INFO: compiled from: PreferencesRedditWebPreferencesFragment.java */
    class LivePreferencesErrorListener implements Response.ErrorListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ ProgressDialog progressDialog;

        /* JADX INFO: renamed from: pa.a1$h$a */
        /* JADX INFO: compiled from: PreferencesRedditWebPreferencesFragment.java */
        class FinishOnDismissListener implements DialogInterface.OnDismissListener {
            FinishOnDismissListener() {
            }

            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                PreferencesRedditWebPreferencesFragment.this.getActivity().finish();
            }
        }

        /* JADX INFO: renamed from: pa.a1$h$b */
        /* JADX INFO: compiled from: PreferencesRedditWebPreferencesFragment.java */
        class FinishOnClickListener implements DialogInterface.OnClickListener {
            FinishOnClickListener() {
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int which) {
                PreferencesRedditWebPreferencesFragment.this.getActivity().finish();
            }
        }

        LivePreferencesErrorListener(ProgressDialog progressDialog) {
            this.progressDialog = progressDialog;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (ContextHelper.isActivityFinishing(PreferencesRedditWebPreferencesFragment.this.getActivity())) {
                return;
            }
            try {
                this.progressDialog.dismiss();
                AlertDialogHelper.newBuilder(PreferencesRedditWebPreferencesFragment.this.getContext()).setTitle("Error fetching live preferences").setMessage("Please try again").setPositiveButton("Dismiss", new FinishOnClickListener()).setOnDismissListener(new FinishOnDismissListener()).create().show();
            } catch (Exception e) {
                Logger.logException(e);
            }
        }
    }

    private void loadLivePreferences() {
        PreferenceScreen preferenceScreen = (PreferenceScreen) findPreference("web_preferences_root");
        ProgressDialog progressDialogA = ProgressDialogHelper.create(getActivity());
        progressDialogA.setMessage("Loading live reddit preferences");
        progressDialogA.setOnDismissListener(new FinishOnDisabledDismissListener(preferenceScreen));
        progressDialogA.show();
        OAuthRequestManager.send(getActivity(), new OAuthGetUserPrefsRequest(getActivity(), new LivePreferencesResponseListener(progressDialogA, preferenceScreen), new LivePreferencesErrorListener(progressDialogA)));
    }

    public static PreferencesRedditWebPreferencesFragment newInstance(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(BasePreferencesFragment.KEY_HIGHLIGHT, str);
        PreferencesRedditWebPreferencesFragment preferencesRedditWebPreferencesFragment = new PreferencesRedditWebPreferencesFragment();
        preferencesRedditWebPreferencesFragment.setArguments(bundle);
        return preferencesRedditWebPreferencesFragment;
    }

    @Override // com.resync.app.fragments.preferences.BasePreferencesFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        PreferencesHelper.getAccountPrefs("PreferencesSecurityFragment").edit().remove("media_on").remove("media_off").remove("media_subreddit").remove("link_compress").remove("content_nsfw_search").remove("show_presence").remove("allow_clicktracking").commit();
        addPreferencesFromResource(R.xml.cat_reddit_web_preferences);
        this.mediaOnPref = (SyncCheckBoxPreference) findPreference("media_on");
        this.mediaOffPref = (SyncCheckBoxPreference) findPreference("media_off");
        this.mediaSubredditPref = (SyncCheckBoxPreference) findPreference("media_subreddit");
        this.showPresencePref = (SyncCheckBoxPreference) findPreference("show_presence");
        this.allowClickTrackingPref = (SyncCheckBoxPreference) findPreference("allow_clicktracking");
        MediaPreferenceChangeListener mediaListener = new MediaPreferenceChangeListener();
        this.mediaOnPref.setOnPreferenceChangeListener(mediaListener);
        this.mediaOffPref.setOnPreferenceChangeListener(mediaListener);
        this.mediaSubredditPref.setOnPreferenceChangeListener(mediaListener);
        SyncCheckBoxPreference syncCheckBoxPreference = (SyncCheckBoxPreference) findPreference("link_compress");
        this.linkCompressPref = syncCheckBoxPreference;
        syncCheckBoxPreference.setOnPreferenceChangeListener(new LinkCompressChangeListener());
        SyncCheckBoxPreference syncCheckBoxPreference2 = (SyncCheckBoxPreference) findPreference("content_nsfw_search");
        this.contentNsfwPref = syncCheckBoxPreference2;
        syncCheckBoxPreference2.setOnPreferenceChangeListener(new ContentNsfwChangeListener());
        SyncCheckBoxPreference syncCheckBoxPreference3 = (SyncCheckBoxPreference) findPreference("show_presence");
        this.showPresencePref = syncCheckBoxPreference3;
        syncCheckBoxPreference3.setOnPreferenceChangeListener(new ShowPresenceChangeListener());
        SyncCheckBoxPreference syncCheckBoxPreference4 = (SyncCheckBoxPreference) findPreference("allow_clicktracking");
        this.allowClickTrackingPref = syncCheckBoxPreference4;
        syncCheckBoxPreference4.setOnPreferenceChangeListener(new AllowClickTrackingChangeListener());
        loadLivePreferences();
    }
}
