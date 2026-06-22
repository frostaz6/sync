package com.resync.app.ui.preferences.defaults;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.preferences.ColorView;
import com.resync.app.helpers.ui.MonetUtil;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public class ColorPreference extends Preference {

    public View boundView;

    public ColorPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setWidgetLayoutResource(R.layout.preference_color);
    }

    protected int getHighlightColor() {
        int linkColor = MonetUtil.getLinkColorAuto();
        return Color.argb(60, Color.red(linkColor), Color.green(linkColor), Color.blue(linkColor));
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.boundView = preferenceViewHolder.itemView;
        Logger.verbose("SETTING HERE");
        TextView textView = (TextView) this.boundView.findViewById(android.R.id.title);
        textView.setTextColor(MonetUtil.getPrimaryTextColorAuto());
        textView.setTextSize(1, RedditHelper.getTitleTextSize(SettingsSingleton.getSettings().fontSize));
        textView.setTypeface(TypefaceManager.get(11));
        textView.setSingleLine(false);
        TextView textView2 = (TextView) this.boundView.findViewById(android.R.id.summary);
        textView2.setTextColor(MonetUtil.getSecondaryTextColorAuto());
        textView2.setTextSize(1, RedditHelper.getSmallHeaderTextSize(SettingsSingleton.getSettings().fontSize));
        textView2.setTypeface(TypefaceManager.get(9));
        if (StringUtils.equalsIgnoreCase(getKey(), (String) preferenceViewHolder.itemView.getTag(R.id.preferences_key_highlight))) {
            this.boundView.setForeground(new ColorDrawable(getHighlightColor()));
        } else {
            this.boundView.setForeground(new ColorDrawable(0));
        }
        ((ColorView) preferenceViewHolder.itemView.findViewById(R.id.preview_color)).setColor(SettingsSingleton.getSettings().monet_manual_theme_color);
        preferenceViewHolder.itemView.setTag(R.id.preference_title, getSummary());
        preferenceViewHolder.itemView.setTag(R.id.preference_key, getKey());
    }
}
