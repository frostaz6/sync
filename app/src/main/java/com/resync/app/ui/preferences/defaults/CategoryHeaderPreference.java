package com.resync.app.ui.preferences.defaults;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.text.BaseTextView;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.TypefaceManager;

/* JADX INFO: loaded from: classes.dex */
public class CategoryHeaderPreference extends Preference {

    BaseTextView titleTextView;
    String title;

    Boolean nightOverride;

    public CategoryHeaderPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, SyncStyleable.CATEGORY_HEADER);
        this.title = typedArrayObtainStyledAttributes.getString(1);
        if (typedArrayObtainStyledAttributes.hasValue(0)) {
            this.nightOverride = Boolean.valueOf(typedArrayObtainStyledAttributes.getBoolean(0, NightHelper.isNight()));
        }
        typedArrayObtainStyledAttributes.recycle();
        setLayoutResource(R.layout.preference_category_header);
        setEnabled(false);
        setIconSpaceReserved(false);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        BaseTextView baseTextView = (BaseTextView) preferenceViewHolder.findViewById(R.id.title_textview);
        this.titleTextView = baseTextView;
        baseTextView.setText(this.title);
        this.titleTextView.setTextSize(1, RedditHelper.getTitleTextSize(SettingsSingleton.getSettings().fontSize));
        this.titleTextView.setTextColor(MonetUtil.getLinkColorAuto());
        this.titleTextView.setTypeface(TypefaceManager.get(11));
    }
}
