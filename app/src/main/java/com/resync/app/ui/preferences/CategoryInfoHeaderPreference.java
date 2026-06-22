package com.resync.app.ui.preferences;

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
import com.resync.app.helpers.TypefaceManager;

/* JADX INFO: loaded from: classes.dex */
public class CategoryInfoHeaderPreference extends Preference {

    BaseTextView titleTextView;
    String title;

    public CategoryInfoHeaderPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, SyncStyleable.CATEGORY_INFO_HEADER);
        this.title = typedArrayObtainStyledAttributes.getString(1);
        typedArrayObtainStyledAttributes.recycle();
        setLayoutResource(R.layout.preference_category_info_header);
        setEnabled(false);
        setIconSpaceReserved(false);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        BaseTextView baseTextView = (BaseTextView) preferenceViewHolder.findViewById(R.id.title_textview);
        this.titleTextView = baseTextView;
        baseTextView.setText(this.title);
        this.titleTextView.setTextSize(1, RedditHelper.getSmallHeaderTextSize(SettingsSingleton.getSettings().fontSize));
        this.titleTextView.setTextColor(MonetUtil.getSecondaryTextColorAuto());
        this.titleTextView.setTypeface(TypefaceManager.get(9));
    }
}
