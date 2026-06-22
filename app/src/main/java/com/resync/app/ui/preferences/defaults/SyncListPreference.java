package com.resync.app.ui.preferences.defaults;

import android.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceViewHolder;
import com.bumptech.glide.RedditHelper;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.MonetUtil;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.helpers.TypefaceManager;

/* JADX INFO: loaded from: classes.dex */
public class SyncListPreference extends ListPreference {
    public View boundView;

    public SyncListPreference(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View view = preferenceViewHolder.itemView;
        this.boundView = view;
        TextView textView = (TextView) view.findViewById(R.id.title);
        textView.setTextColor(StyleSingleton.getTextColorAuto(getContext(), false));
        textView.setTextSize(1, RedditHelper.getTitleTextSize(SettingsSingleton.getSettings().fontSize));
        textView.setTypeface(TypefaceManager.get(11));
        textView.setSingleLine(false);
        TextView textView2 = (TextView) this.boundView.findViewById(R.id.summary);
        textView2.setTextColor(MonetUtil.getSecondaryTextColorAuto());
        textView2.setTextSize(1, RedditHelper.getSmallHeaderTextSize(SettingsSingleton.getSettings().fontSize));
        textView2.setTypeface(TypefaceManager.get(9));
        if (StringUtils.equalsIgnoreCase(getKey(), (String) preferenceViewHolder.itemView.getTag(com.resync.app.R.id.preferences_key_highlight))) {
            this.boundView.setForeground(new ColorDrawable(getHighlightColor()));
        } else {
            this.boundView.setForeground(new ColorDrawable(0));
        }
        preferenceViewHolder.itemView.setTag(com.resync.app.R.id.preference_title, getSummary());
        preferenceViewHolder.itemView.setTag(com.resync.app.R.id.preference_key, getKey());
    }

    protected int getHighlightColor() {
        int linkColor = MonetUtil.getLinkColorAuto();
        return Color.argb(60, Color.red(linkColor), Color.green(linkColor), Color.blue(linkColor));
    }

    public SyncListPreference(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        super(context, attributeSet, defStyleAttr, defStyleRes);
    }

    public SyncListPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setIconSpaceReserved(false);
    }

    public SyncListPreference(Context context) {
        super(context);
    }
}
