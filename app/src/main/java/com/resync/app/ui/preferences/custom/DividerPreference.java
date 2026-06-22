package com.resync.app.ui.preferences.custom;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.resync.app.R;

/* JADX INFO: loaded from: classes.dex */
public class DividerPreference extends Preference {

    public static final String KEY = DividerPreference.class.getSimpleName();

    public DividerPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R.layout.preference_divider);
        setSelectable(false);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
    }
}
