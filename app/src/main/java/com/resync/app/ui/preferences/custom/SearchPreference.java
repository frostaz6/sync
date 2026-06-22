package com.resync.app.ui.preferences.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.resync.app.R;
import com.resync.app.ui.activities.PreferencesActivity;
import com.resync.app.ui.views.monet.MonetSearchBoxView;

/* JADX INFO: loaded from: classes.dex */
public class SearchPreference extends Preference {

    public static final String KEY = SearchPreference.class.getSimpleName();

    public SearchPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setKey(KEY);
        setLayoutResource(R.layout.preference_search);
        setSelectable(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onSearchClicked(View view) {
        ((PreferencesActivity) getContext()).openSearch();
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ((MonetSearchBoxView) preferenceViewHolder.findViewById(R.id.monet_search_box)).setWrapperClickListener(new View.OnClickListener() { // from class: ab.d
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SearchPreference.this.onSearchClicked(view);
            }
        });
    }
}
