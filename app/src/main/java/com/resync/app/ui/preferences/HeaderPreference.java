package com.resync.app.ui.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.resync.app.R;
import com.resync.app.ui.views.text.basic.ProductSansTextView;
import com.resync.app.helpers.ui.MonetUtil;

/* JADX INFO: loaded from: classes.dex */
public class HeaderPreference extends Preference {
    public static final String KEY = HeaderPreference.class.getSimpleName();

    public View boundView;

    public HeaderPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setKey(KEY);
        setLayoutResource(R.layout.preference_header);
        setSelectable(false);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View view = preferenceViewHolder.itemView;
        this.boundView = view;
        ProductSansTextView productSansTextView = (ProductSansTextView) view.findViewById(R.id.title);
        productSansTextView.setTextColor(MonetUtil.getPrimaryTextColorAuto());
        productSansTextView.setText((String) preferenceViewHolder.itemView.getTag(R.id.preferences_key_title));
        preferenceViewHolder.itemView.setEnabled(false);
    }
}
