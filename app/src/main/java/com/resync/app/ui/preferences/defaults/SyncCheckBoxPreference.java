package com.resync.app.ui.preferences.defaults;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreferenceCompat;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.MonetUtil;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.helpers.TypefaceManager;

/* JADX INFO: loaded from: classes.dex */
public class SyncCheckBoxPreference extends SwitchPreferenceCompat {
    public SyncCheckBoxPreference(Context context) {
        super(context);
        init();
    }

    private void init() {
        setIconSpaceReserved(false);
        setWidgetLayoutResource(R.layout.preference_material_switch);
    }

    @Override // androidx.preference.SwitchPreferenceCompat, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.itemView.findViewById(android.R.id.title);
        textView.setTextColor(StyleSingleton.getTextColorAuto(getContext(), false));
        textView.setTextSize(1, RedditHelper.getTitleTextSize(SettingsSingleton.getSettings().fontSize));
        textView.setTypeface(TypefaceManager.get(11));
        textView.setSingleLine(false);
        TextView textView2 = (TextView) preferenceViewHolder.itemView.findViewById(android.R.id.summary);
        textView2.setTextColor(MonetUtil.getSecondaryTextColorAuto());
        textView2.setTextSize(1, RedditHelper.getSmallHeaderTextSize(SettingsSingleton.getSettings().fontSize));
        textView2.setTypeface(TypefaceManager.get(9));
        if (StringUtils.equalsIgnoreCase(getKey(), (String) preferenceViewHolder.itemView.getTag(R.id.preferences_key_highlight))) {
            preferenceViewHolder.itemView.setForeground(new ColorDrawable(getHighlightColor()));
        } else {
            preferenceViewHolder.itemView.setForeground(new ColorDrawable(0));
        }
        preferenceViewHolder.itemView.setTag(R.id.preference_title, getSummary());
        preferenceViewHolder.itemView.setTag(R.id.preference_key, getKey());
    }

    protected int getHighlightColor() {
        int linkColor = MonetUtil.getLinkColorAuto();
        return Color.argb(60, Color.red(linkColor), Color.green(linkColor), Color.blue(linkColor));
    }

    public SyncCheckBoxPreference(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init();
    }

    public SyncCheckBoxPreference(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        super(context, attributeSet, defStyleAttr, defStyleRes);
        init();
    }

    public SyncCheckBoxPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }
}
