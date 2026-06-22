package com.resync.app.fragments.preferences;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.RedditHelper;
import com.google.android.material.internal.ViewUtils;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.OnboardingActivity;
import com.resync.app.ui.fragments.preferences.PreferenceWrapperFragment;
import com.resync.app.ui.preferences.HeaderPreference;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.helpers.WindowHelper;
import com.resync.app.dialogs.PreferenceInputDialogBottomSheet;
import com.resync.app.dialogs.ResetSettingsAlertDialogBottomSheet;
import com.resync.app.util.Logger;
import com.resync.app.util.RippleUtils;
import com.resync.app.util.TextUtils;

/* JADX INFO: renamed from: pa.d */
/* JADX INFO: compiled from: BasePreferencesFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public abstract class BasePreferencesFragment extends PreferenceFragmentCompat {
    public static String TAG = BasePreferencesFragment.class.getSimpleName();
    public static String KEY_HIGHLIGHT = "highlight";

    /* JADX INFO: renamed from: x0, reason: collision with root package name */
    protected String highlightKey;
    protected int highlightPosition = -1;
    float scrollOffset;

    /* JADX INFO: renamed from: pa.d$a */
    /* JADX INFO: compiled from: BasePreferencesFragment.java */
    class HighlightablePreferenceGroupAdapter extends PreferenceGroupAdapter {
        HighlightablePreferenceGroupAdapter(PreferenceGroup preferenceGroup) {
            super(preferenceGroup);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean onPreferenceLongClick(PreferenceViewHolder preferenceViewHolder, int mode, String str, View view) {
            if (!(preferenceViewHolder.itemView.getTag(R.id.preference_key) instanceof String) || !(preferenceViewHolder.itemView.getTag(R.id.preference_title) instanceof String)) {
                return true;
            }
            RedditHelper.copyToClipboard(BasePreferencesFragment.this.getContext(), "[Settings shortcut: " + str + " > " + ((String) preferenceViewHolder.itemView.getTag(R.id.preference_title)) + "](" + ("sync-settings://" + mode + "-" + ((String) preferenceViewHolder.itemView.getTag(R.id.preference_key))) + ")", false);
            StaticToast.show("Copied shortcut");
            return true;
        }

        @Override // androidx.preference.PreferenceGroupAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: S, reason: merged with bridge method [inline-methods] */
        public void onBindViewHolder(final PreferenceViewHolder preferenceViewHolder, int position) {
            if (BasePreferencesFragment.this.getParentFragment() instanceof PreferenceWrapperFragment) {
                preferenceViewHolder.itemView.setTag(R.id.preferences_key_title, ((PreferenceWrapperFragment) BasePreferencesFragment.this.getParentFragment()).getScreenTitle());
                preferenceViewHolder.itemView.setTag(R.id.preferences_key_highlight, BasePreferencesFragment.this.highlightKey);
            }
            super.onBindViewHolder(preferenceViewHolder, position);
            if (BasePreferencesFragment.this.getParentFragment() instanceof PreferenceWrapperFragment) {
                final String screenTitle = ((PreferenceWrapperFragment) BasePreferencesFragment.this.getParentFragment()).getScreenTitle();
                final int mode = ((PreferenceWrapperFragment) BasePreferencesFragment.this.getParentFragment()).getMode();
                preferenceViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() { // from class: pa.c
                    @Override // android.view.View.OnLongClickListener
                    public final boolean onLongClick(View view) {
                        return HighlightablePreferenceGroupAdapter.this.onPreferenceLongClick(preferenceViewHolder, mode, screenTitle, view);
                    }
                });
            }
            preferenceViewHolder.itemView.setBackground(RippleUtils.getSelectableBackground());
        }

        @Override // androidx.preference.PreferenceGroupAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: T */
        public PreferenceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            return super.onCreateViewHolder(viewGroup, viewType);
        }
    }

    /* JADX INFO: renamed from: pa.d$b */
    /* JADX INFO: compiled from: BasePreferencesFragment.java */
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

    /* JADX INFO: renamed from: pa.d$c */
    /* JADX INFO: compiled from: BasePreferencesFragment.java */
    class SummaryChangeListener implements Preference.OnPreferenceChangeListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ SimplePreference simplePreference;

        SummaryChangeListener(SimplePreference simplePreference) {
            this.simplePreference = simplePreference;
        }

        @Override // androidx.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            BasePreferencesFragment.this.updateSummary(this.simplePreference, obj);
            return true;
        }
    }

    /* JADX INFO: renamed from: pa.d$d */
    /* JADX INFO: compiled from: BasePreferencesFragment.java */
    class HeaderScrollListener extends RecyclerView.OnScrollListener {
        HeaderScrollListener() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            HeaderPreference headerPreference;
            BasePreferencesFragment basePreferencesFragment = BasePreferencesFragment.this;
            basePreferencesFragment.scrollOffset += dy;
            if (!(basePreferencesFragment.getParentFragment() instanceof PreferenceWrapperFragment) || (headerPreference = (HeaderPreference) BasePreferencesFragment.this.findPreference(HeaderPreference.KEY)) == null || headerPreference.boundView == null) {
                return;
            }
            headerPreference.boundView.setAlpha(1.0f - (BasePreferencesFragment.this.scrollOffset / WindowHelper.getActionBarHeight(BasePreferencesFragment.this.getContext())));
            ((PreferenceWrapperFragment) BasePreferencesFragment.this.getParentFragment()).setToolbarAlpha(BasePreferencesFragment.this.scrollOffset / WindowHelper.getActionBarHeight(BasePreferencesFragment.this.getContext()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void notifyAdapterDataChanged() {
        if (getListView() == null || getListView().getAdapter() == null) {
            return;
        }
        getListView().getAdapter().notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void collapseAppBar() {
        ((PreferenceWrapperFragment) getParentFragment()).getAppBar().setExpanded(false);
    }

    private void bindSummaryPreferences(SimplePreference[] simplePreferenceArr) {
        if (simplePreferenceArr == null || simplePreferenceArr.length == 0) {
            return;
        }
        for (SimplePreference simplePreference : simplePreferenceArr) {
            findPreference(simplePreference.key).setOnPreferenceChangeListener(new SummaryChangeListener(simplePreference));
            updateSummary(simplePreference, null);
        }
    }

    private void tintPreferenceIcon(Preference preference) {
        if (preference.getIcon() != null) {
            preference.getIcon().setTint(StyleSingleton.getTextColorAuto(getContext(), false));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSummary(SimplePreference simplePreference, Object obj) {
        Logger.verbose("Updating preference: " + simplePreference.key);
        findPreference(simplePreference.key).setSummary(getResources().getStringArray(simplePreference.summaryArrayRes)[Integer.parseInt(obj != null ? (String) obj : PreferencesHelper.getAccountPrefs("BasePreferencesFragment").getString(simplePreference.key, simplePreference.defaultValue))]);
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    @SuppressLint({"RestrictedApi"})
    protected RecyclerView.Adapter onCreateAdapter(PreferenceScreen preferenceScreen) {
        return new HighlightablePreferenceGroupAdapter(preferenceScreen);
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        bindSummaryPreferences(getSummaryPreferences());
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    @SuppressLint({"RestrictedApi"})
    public RecyclerView onCreateRecyclerView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        RecyclerView recyclerViewD3 = super.onCreateRecyclerView(layoutInflater, viewGroup, bundle);
        recyclerViewD3.setItemAnimator(null);
        recyclerViewD3.setLayoutAnimation(null);
        recyclerViewD3.setStateListAnimator(null);
        recyclerViewD3.setBackgroundColor(0);
        if (getActivity() instanceof OnboardingActivity) {
            recyclerViewD3.setOverScrollMode(2);
        } else {
            MonetUtil.disableOverScrollPreS(recyclerViewD3);
        }
        recyclerViewD3.setNestedScrollingEnabled(true);
        recyclerViewD3.getLayoutManager().setItemPrefetchEnabled(true);
        if (getParentFragment() instanceof PreferenceWrapperFragment) {
            ((PreferenceWrapperFragment) getParentFragment()).getAppBar().setLiftOnScroll(true);
            ((PreferenceWrapperFragment) getParentFragment()).getAppBar().setLiftOnScrollTargetViewId(recyclerViewD3.getId());
        }
        if (!(getActivity() instanceof OnboardingActivity)) {
            ViewUtils.doOnApplyWindowInsets(recyclerViewD3, new InsetsListener());
        }
        return recyclerViewD3;
    }

    protected String getHighlightKey() {
        return this.highlightKey;
    }

    public ArrayList<String> getPreferenceKeys() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int preferenceCount = getPreferenceScreen().getPreferenceCount() - 1; preferenceCount >= 0; preferenceCount--) {
            Preference preference = getPreferenceScreen().getPreference(preferenceCount);
            arrayList.add(preference.getKey());
            if (preference instanceof PreferenceCategory) {
                PreferenceCategory preferenceCategory = (PreferenceCategory) preference;
                for (int preferenceCount2 = preferenceCategory.getPreferenceCount() - 1; preferenceCount2 >= 0; preferenceCount2--) {
                    arrayList.add(preferenceCategory.getPreference(preferenceCount2).getKey());
                }
            }
        }
        return arrayList;
    }

    public SimplePreference[] getSummaryPreferences() {
        return null;
    }

    protected int getHighlightPosition() {
        return this.highlightPosition;
    }

    protected boolean isTabletMode() {
        return PreferencesHelper.getAccountPrefs("PreferencesTabletFragment").getBoolean("tablet_mode", getResources().getBoolean(R.bool.tablet));
    }

    protected void refreshList() {
        try {
            if (getListView() != null) {
                getListView().post(new Runnable() { // from class: pa.b
                    @Override // java.lang.Runnable
                    public final void run() {
                        BasePreferencesFragment.this.notifyAdapterDataChanged();
                    }
                });
            }
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    public void onDarkModeChanged() {
        Logger.verbose("Dark mode changed");
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

    public void onResetSettings() {
        if (getParentFragment() instanceof PreferenceWrapperFragment) {
            ((PreferenceWrapperFragment) getParentFragment()).clearHighlight();
        }
        FragmentUtils.showWithArguments(ResetSettingsAlertDialogBottomSheet.class, getChildFragmentManager(), ResetSettingsAlertDialogBottomSheet.buildArguments(getPreferenceKeys()));
    }

    public void scrollToTopIfEnabled() {
        if (SettingsSingleton.getSettings().postsQuickScroll) {
            scrollToTop();
        }
    }

    public void scrollToTop() {
        View view;
        try {
            getListView().scrollToPosition(0);
            if (getParentFragment() instanceof PreferenceWrapperFragment) {
                this.scrollOffset = 0.0f;
                HeaderPreference headerPreference = (HeaderPreference) findPreference(HeaderPreference.KEY);
                if (headerPreference == null || (view = headerPreference.boundView) == null) {
                    return;
                }
                view.setAlpha(1.0f);
                ((PreferenceWrapperFragment) getParentFragment()).setToolbarAlpha(0.0f);
                ((PreferenceWrapperFragment) getParentFragment()).getAppBar().post(new Runnable() { // from class: pa.a
                    @Override // java.lang.Runnable
                    public final void run() {
                        BasePreferencesFragment.this.collapseAppBar();
                    }
                });
            }
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    public void setHighlightKey(String str) {
        this.highlightKey = str;
        try {
            getListView().getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            Logger.logException(e, false);
        }
    }

    protected void computeHighlightPosition() {
        int preferenceCount = getPreferenceScreen().getPreferenceCount();
        if (getHighlightKey() != null) {
            Logger.verbose("Checking: looking for: " + getHighlightKey());
        }
        int position = 0;
        for (int i = 0; i < preferenceCount; i++) {
            Preference preference = getPreferenceScreen().getPreference(i);
            if (getHighlightKey() != null) {
                Logger.verbose("Checking: " + preference.getKey());
            }
            if (preference.getIcon() != null) {
                preference.getIcon().setTint(StyleSingleton.getTextColorAuto(getContext(), false));
            }
            if (getHighlightKey() != null && StringUtils.equalsIgnoreCase(getHighlightKey(), preference.getKey())) {
                this.highlightPosition = position;
                Logger.verbose("Checking: found " + preference.getKey() + " - at: " + this.highlightPosition);
            }
            if (preference instanceof PreferenceCategory) {
                PreferenceCategory preferenceCategory = (PreferenceCategory) preference;
                for (int j = 0; j < preferenceCategory.getPreferenceCount(); j++) {
                    Preference preference2 = preferenceCategory.getPreference(j);
                    if (getHighlightKey() != null) {
                        Logger.verbose("Checking: " + preference2.getKey());
                    }
                    if (preference2.getIcon() != null) {
                        preference2.getIcon().setTint(StyleSingleton.getTextColorAuto(getContext(), false));
                    }
                    if (getHighlightKey() != null && StringUtils.equalsIgnoreCase(getHighlightKey(), preference2.getKey())) {
                        this.highlightPosition = position;
                        if (getHighlightKey() != null) {
                            Logger.verbose("Checking: found " + preference2.getKey() + " - at: " + this.highlightPosition);
                        }
                    }
                    position++;
                }
            }
            position++;
        }
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: m2 */
    public void onStart() {
        super.onStart();
        BusSingleton.getBus().register(this);
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnDisplayPreferenceDialogListener
    public void onDisplayPreferenceDialog(Preference preference) {
        PreferenceInputDialogBottomSheet preferenceInputDialogBottomSheetH4 = PreferenceInputDialogBottomSheet.newInstance(preference.getKey());
        preferenceInputDialogBottomSheetH4.setTargetFragment(this, 0);
        preferenceInputDialogBottomSheetH4.show(getChildFragmentManager(), "androidx.preference.PreferenceFragment.DIALOG");
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: n2 */
    public void onStop() {
        BusSingleton.getBus().unregister(this);
        super.onStop();
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        setDividerHeight(0);
        getListView().addOnScrollListener(new HeaderScrollListener());
        if (getParentFragment() instanceof PreferenceWrapperFragment) {
            int mode = ((PreferenceWrapperFragment) getParentFragment()).getMode();
            String highlightKey = getHighlightKey();
            String string = Integer.toString(getResources().getInteger(R.integer.GENERAL));
            Logger.verbose("Highlight: " + highlightKey + " - General highlight: " + string);
            if ((mode == getResources().getInteger(R.integer.ROOT) && StringUtils.equalsIgnoreCase(getHighlightKey(), string)) || getHighlightPosition() < 0 || TextUtils.isEmpty(getHighlightKey())) {
                return;
            }
            getListView().scrollToPosition(getHighlightPosition());
        }
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public void addPreferencesFromResource(int resId) {
        if (getArguments() != null) {
            setHighlightKey(getArguments().getString(KEY_HIGHLIGHT));
        }
        Logger.verbose(TAG, "Enabled: " + PreferencesHelper.hasAccountPrefs());
        if (PreferencesHelper.hasAccountPrefs()) {
            String accountName = PreferencesHelper.getAccountName();
            Logger.verbose(TAG, "Name: " + accountName);
            getPreferenceManager().setSharedPreferencesName(accountName);
            getPreferenceManager().setSharedPreferencesMode(0);
        }
        super.addPreferencesFromResource(resId);
        computeHighlightPosition();
    }
}
