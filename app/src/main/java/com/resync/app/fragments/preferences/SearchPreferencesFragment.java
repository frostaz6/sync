package com.resync.app.fragments.preferences;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.util.Pair;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.internal.ViewUtils;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.activities.PreferencesActivity;
import com.resync.app.ui.preferences.defaults.SyncPreference;
import com.resync.app.helpers.ui.MonetUtil;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.helpers.RemoteConfigHelper;
import com.resync.app.security.KeyManager;
import com.resync.app.premium.UltraHelper;
import com.resync.app.util.Logger;
import com.resync.app.util.RippleUtils;

/* JADX INFO: renamed from: pa.o1 */
/* JADX INFO: compiled from: SearchPreferencesFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class SearchPreferencesFragment extends PreferenceFragmentCompat {

    /* JADX INFO: renamed from: x0, reason: collision with root package name */
    private ArrayList<Pair<String, String>> ultraPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> generalPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> experimentalPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> animationsPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> tabletPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> monetAdvancedPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> postSlideViewPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> postCardViewPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> postSmallCardViewPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> postSmallerCardViewPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> postCompactViewPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> postListViewPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> commentViewPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> awardsPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> postsPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> commentsPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> messagingPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> imagesPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> linksPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> historyPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> dataPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> filtersPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> backupPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> securityPrefs = new ArrayList<>();
    private ArrayList<Pair<String, String>> darkPrefs = new ArrayList<>();

    /* JADX INFO: renamed from: pa.o1$a */
    /* JADX INFO: compiled from: SearchPreferencesFragment.java */
    class RippleAdapter extends PreferenceGroupAdapter {
        RippleAdapter(PreferenceGroup preferenceGroup) {
            super(preferenceGroup);
        }

        @Override // androidx.preference.PreferenceGroupAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: T, reason: merged with bridge method [inline-methods] */
        public PreferenceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            PreferenceViewHolder preferenceViewHolderD = super.onCreateViewHolder(viewGroup, viewType);
            preferenceViewHolderD.itemView.setBackground(RippleUtils.getSelectableBackground());
            return preferenceViewHolderD;
        }
    }

    /* JADX INFO: renamed from: pa.o1$b */
    /* JADX INFO: compiled from: SearchPreferencesFragment.java */
    class InsetsListener implements ViewUtils.OnApplyWindowInsetsListener {
        InsetsListener() {
        }

        @Override // com.google.android.material.internal.ViewUtils.OnApplyWindowInsetsListener
        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
            relativePadding.bottom += windowInsetsCompat.getSystemWindowInsetBottom();
            boolean rtl = androidx.core.view.ViewCompat.getLayoutDirection(view) == 1;
            int insetLeft = windowInsetsCompat.getSystemWindowInsetLeft();
            int insetRight = windowInsetsCompat.getSystemWindowInsetRight();
            relativePadding.start += rtl ? insetRight : insetLeft;
            int end = relativePadding.end;
            if (!rtl) {
                insetLeft = insetRight;
            }
            relativePadding.end = end + insetLeft;
            relativePadding.applyToView(view);
            return windowInsetsCompat;
        }
    }

    /* JADX INFO: renamed from: pa.o1$c */
    /* JADX INFO: compiled from: SearchPreferencesFragment.java */
    class CategoryClickListener implements Preference.OnPreferenceClickListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ int mode;
        final /* synthetic */ boolean darkTheme;

        CategoryClickListener(int mode, boolean darkTheme) {
            this.mode = mode;
            this.darkTheme = darkTheme;
        }

        @Override // androidx.preference.Preference.OnPreferenceClickListener
        public boolean onPreferenceClick(Preference preference) {
            if (SearchPreferencesFragment.this.getActivity() instanceof PreferencesActivity) {
                ((PreferencesActivity) SearchPreferencesFragment.this.getActivity()).openModeHighlight(this.mode, preference.getKey());
            } else {
                Intent intent = new Intent(SearchPreferencesFragment.this.getActivity(), (Class<?>) PreferencesActivity.class);
                intent.putExtra("mode", this.mode);
                intent.putExtra("highlight", preference.getKey());
                if (this.darkTheme) {
                    intent.putExtra("dark_theme", true);
                }
                SearchPreferencesFragment.this.startActivity(intent);
            }
            return true;
        }
    }

    private static int getAlertTheme() {
        return NightHelper.isNight() ? R.style.Alert_Dark : R.style.Alert_Day;
    }

    private boolean isPreferenceVisible(String str) {
        if (UltraHelper.isUltra() || !StringUtils.startsWith(str, "ultra_")) {
            return !StringUtils.startsWith(str, "ultra_disabled");
        }
        return false;
    }

    public static SearchPreferencesFragment newInstance() {
        return new SearchPreferencesFragment();
    }

    private void tintPreferenceIcon(Preference preference) {
        if (preference.getIcon() != null) {
            preference.getIcon().setTint(MonetUtil.getPrimaryTextColorAuto());
            Logger.verbose("Updating icon");
        }
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    @SuppressLint({"RestrictedApi"})
    protected RecyclerView.Adapter onCreateAdapter(PreferenceScreen preferenceScreen) {
        return new RippleAdapter(preferenceScreen);
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        if (RemoteConfigHelper.isC4266fa() && UltraHelper.isUltra()) {
            String string = PreferencesHelper.getDefaultPrefs().getString(KeyManager.purchaseTokenKey(), null);
            if (StringUtils.isEmpty(string) || !StringUtils.containsIgnoreCase(string, ".AO-")) {
                getActivity().finish();
            }
        }
        if (UltraHelper.isUltra()) {
            loadCategory(R.xml.cat_ultra, this.ultraPrefs);
        }
        loadCategory(R.xml.cat_general, this.generalPrefs);
        loadCategory(R.xml.cat_experimental, this.experimentalPrefs);
        loadCategory(R.xml.cat_animations, this.animationsPrefs);
        loadCategory(R.xml.cat_tablet, this.tabletPrefs);
        loadCategory(R.xml.cat_monet_advanced, this.monetAdvancedPrefs);
        loadCategory(R.xml.cat_post_slide_view, this.postSlideViewPrefs);
        loadCategory(R.xml.cat_post_card_view, this.postCardViewPrefs);
        loadCategory(R.xml.cat_post_small_card_view, this.postSmallCardViewPrefs);
        loadCategory(R.xml.cat_post_smaller_card_view, this.postSmallerCardViewPrefs);
        loadCategory(R.xml.cat_post_compact_view, this.postCompactViewPrefs);
        loadCategory(R.xml.cat_post_list_view, this.postListViewPrefs);
        loadCategory(R.xml.cat_comment_view_customization, this.commentViewPrefs);
        loadCategory(R.xml.cat_awards, this.awardsPrefs);
        loadCategory(R.xml.cat_posts, this.postsPrefs);
        loadCategory(R.xml.cat_comments, this.commentsPrefs);
        loadCategory(R.xml.cat_messaging, this.messagingPrefs);
        loadCategory(R.xml.cat_images, this.imagesPrefs);
        loadCategory(R.xml.cat_links, this.linksPrefs);
        loadCategory(R.xml.cat_history, this.historyPrefs);
        loadCategory(R.xml.cat_data, this.dataPrefs);
        loadCategory(R.xml.cat_filters, this.filtersPrefs);
        loadCategory(R.xml.cat_backup, this.backupPrefs);
        loadCategory(R.xml.cat_security, this.securityPrefs);
        loadCategory(R.xml.cat_dark, this.darkPrefs);
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    @SuppressLint({"RestrictedApi"})
    public RecyclerView onCreateRecyclerView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        RecyclerView recyclerViewD3 = super.onCreateRecyclerView(layoutInflater, viewGroup, bundle);
        recyclerViewD3.setItemAnimator(null);
        recyclerViewD3.setLayoutAnimation(null);
        recyclerViewD3.setStateListAnimator(null);
        recyclerViewD3.setBackgroundColor(0);
        MonetUtil.disableOverScrollPreS(recyclerViewD3);
        ViewUtils.doOnApplyWindowInsets(recyclerViewD3, new InsetsListener());
        return recyclerViewD3;
    }

    protected void addMatchingPreferences(String str, String str2, ArrayList<Pair<String, String>> arrayList, int iconResId, int mode, boolean darkTheme) {
        for (Pair<String, String> pair : arrayList) {
            String str3 = pair.first;
            String str4 = pair.second;
            if (!TextUtils.isEmpty(str3) && !TextUtils.isEmpty(str4)) {
                Locale locale = Locale.ENGLISH;
                if (str3.toLowerCase(locale).contains(str.toLowerCase(locale))) {
                    SyncPreference syncPreference = new SyncPreference(new ContextThemeWrapper(getActivity(), getAlertTheme()));
                    syncPreference.setTitle(str3);
                    syncPreference.setSummary(str2);
                    syncPreference.setKey(str4);
                    syncPreference.setIcon(getResources().getDrawable(iconResId).mutate());
                    syncPreference.getIcon().setTint(MonetUtil.getPrimaryTextColorAuto());
                    syncPreference.setOnPreferenceClickListener(new CategoryClickListener(mode, darkTheme));
                    getPreferenceScreen().addPreference(syncPreference);
                }
            }
        }
    }

    protected void loadCategory(int xmlResId, ArrayList<Pair<String, String>> arrayList) {
        addPreferencesFromResource(xmlResId);
        for (int preferenceCount = getPreferenceScreen().getPreferenceCount() - 1; preferenceCount >= 0; preferenceCount--) {
            Preference preference = getPreferenceScreen().getPreference(preferenceCount);
            if (isPreferenceVisible(preference.getKey())) {
                arrayList.add(new Pair<>((String) preference.getSummary(), preference.getKey()));
            }
            if (preference instanceof PreferenceCategory) {
                PreferenceCategory preferenceCategory = (PreferenceCategory) preference;
                for (int preferenceCount2 = preferenceCategory.getPreferenceCount() - 1; preferenceCount2 >= 0; preferenceCount2--) {
                    Preference preference2 = preferenceCategory.getPreference(preferenceCount2);
                    if (isPreferenceVisible(preference2.getKey())) {
                        arrayList.add(new Pair<>((String) preference2.getSummary(), preference2.getKey()));
                    }
                }
            }
            getPreferenceScreen().removePreference(preference);
        }
    }

    public void onDarkModeChanged() {
        Logger.verbose("Dark mode changed here");
        int preferenceCount = getPreferenceScreen().getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            Preference preference = getPreferenceScreen().getPreference(i);
            tintPreferenceIcon(preference);
            if (preference instanceof PreferenceCategory) {
                PreferenceCategory preferenceCategory = (PreferenceCategory) preference;
                for (int preferenceCount2 = preferenceCategory.getPreferenceCount() - 1; preferenceCount2 >= 0; preferenceCount2--) {
                    tintPreferenceIcon(preferenceCategory.getPreference(preferenceCount2));
                }
            }
        }
        getListView().getAdapter().notifyDataSetChanged();
    }

    public void clearPreferences() {
        for (int preferenceCount = getPreferenceScreen().getPreferenceCount() - 1; preferenceCount >= 0; preferenceCount--) {
            getPreferenceScreen().removePreference(getPreferenceScreen().getPreference(preferenceCount));
        }
    }

    public void search(String str) {
        String strTrim = str.trim();
        clearPreferences();
        if (strTrim.length() < 1) {
            return;
        }
        addMatchingPreferences(strTrim, "Ultra", this.ultraPrefs, R.drawable.outline_offline_bolt_24, RedditApplication.getInteger(R.integer.ULTRA), false);
        addMatchingPreferences(strTrim, "General", this.generalPrefs, R.drawable.outline_settings_24, RedditApplication.getInteger(R.integer.GENERAL), false);
        addMatchingPreferences(strTrim, "Experimental", this.experimentalPrefs, R.drawable.test_tube, RedditApplication.getInteger(R.integer.EXPERIMENTS), false);
        addMatchingPreferences(strTrim, "Animations", this.animationsPrefs, R.drawable.outline_animation, RedditApplication.getInteger(R.integer.ANIMATIONS), false);
        addMatchingPreferences(strTrim, "Tablet", this.tabletPrefs, R.drawable.outline_tablet_24, RedditApplication.getInteger(R.integer.TABLET), false);
        addMatchingPreferences(strTrim, "Theme management", this.monetAdvancedPrefs, R.drawable.outline_color_lens_24, RedditApplication.getInteger(R.integer.MONET_ADVANCED), false);
        if (UiSingleton.getInstance().getUiMode() == 5) {
            addMatchingPreferences(strTrim, "Post view customization", this.postSlideViewPrefs, R.drawable.outline_view_agenda_24, RedditApplication.getInteger(R.integer.POST_VIEW_OPTIONS), false);
        }
        if (UiSingleton.getInstance().getUiMode() == 4) {
            addMatchingPreferences(strTrim, "Post view customization", this.postCardViewPrefs, R.drawable.outline_view_agenda_24, RedditApplication.getInteger(R.integer.POST_VIEW_OPTIONS), false);
        }
        if (UiSingleton.getInstance().getUiMode() == 3) {
            addMatchingPreferences(strTrim, "Post view customization", this.postSmallCardViewPrefs, R.drawable.outline_view_agenda_24, RedditApplication.getInteger(R.integer.POST_VIEW_OPTIONS), false);
        }
        if (UiSingleton.getInstance().getUiMode() == 2) {
            addMatchingPreferences(strTrim, "Post view customization", this.postSmallerCardViewPrefs, R.drawable.outline_view_agenda_24, RedditApplication.getInteger(R.integer.POST_VIEW_OPTIONS), false);
        }
        if (UiSingleton.getInstance().getUiMode() == 1) {
            addMatchingPreferences(strTrim, "Post view customization", this.postCompactViewPrefs, R.drawable.outline_view_agenda_24, RedditApplication.getInteger(R.integer.POST_VIEW_OPTIONS), false);
        }
        if (UiSingleton.getInstance().getUiMode() == 0) {
            addMatchingPreferences(strTrim, "Post view customization  ", this.postListViewPrefs, R.drawable.outline_view_agenda_24, RedditApplication.getInteger(R.integer.POST_VIEW_OPTIONS), false);
        }
        addMatchingPreferences(strTrim, "Comment view customization", this.commentViewPrefs, R.drawable.outline_mode_comment_24, RedditApplication.getInteger(R.integer.COMMENT_VIEW_OPTIONS), false);
        addMatchingPreferences(strTrim, "Awards", this.awardsPrefs, R.drawable.outline_medal, RedditApplication.getInteger(R.integer.AWARDS), false);
        addMatchingPreferences(strTrim, "Posts", this.postsPrefs, R.drawable.outline_edit_24, RedditApplication.getInteger(R.integer.POSTS), false);
        addMatchingPreferences(strTrim, "Comments", this.commentsPrefs, R.drawable.outline_comments, RedditApplication.getInteger(R.integer.COMMENTS), false);
        addMatchingPreferences(strTrim, "Messaging", this.messagingPrefs, R.drawable.outline_mail_outline_24, RedditApplication.getInteger(R.integer.MESSAGING), false);
        addMatchingPreferences(strTrim, "Media", this.imagesPrefs, R.drawable.outline_image_24, RedditApplication.getInteger(R.integer.IMAGES), false);
        addMatchingPreferences(strTrim, "Link handling", this.linksPrefs, R.drawable.outline_link_24, RedditApplication.getInteger(R.integer.LINKS), false);
        addMatchingPreferences(strTrim, "History", this.historyPrefs, R.drawable.outline_history_24, RedditApplication.getInteger(R.integer.HISTORY), false);
        addMatchingPreferences(strTrim, "Data options", this.dataPrefs, R.drawable.outline_data_usage_24, RedditApplication.getInteger(R.integer.DATA), false);
        addMatchingPreferences(strTrim, "Filters", this.filtersPrefs, R.drawable.outline_filter_list_24, RedditApplication.getInteger(R.integer.FILTERS), false);
        addMatchingPreferences(strTrim, "Backup", this.backupPrefs, R.drawable.outline_backup_24, RedditApplication.getInteger(R.integer.BACKUP), false);
        addMatchingPreferences(strTrim, "Security", this.securityPrefs, R.drawable.outline_shield, RedditApplication.getInteger(R.integer.SECURITY), false);
        addMatchingPreferences(strTrim, "Dark mode", this.darkPrefs, R.drawable.outline_dark_mode_24, RedditApplication.getInteger(R.integer.DARK_MODE), false);
    }
}
