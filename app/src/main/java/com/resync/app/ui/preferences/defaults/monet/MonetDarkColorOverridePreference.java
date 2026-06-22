package com.resync.app.ui.preferences.defaults.monet;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.receiver.MonetThemeReceiver;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.preferences.defaults.monet.MonetDarkColorOverridePreference;
import com.resync.app.ui.views.preferences.ColorView;
import com.resync.app.helpers.ui.MonetUtil;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.fragments.ColorSelectionFragment;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.util.ColorUtils;

/* JADX INFO: loaded from: classes.dex */
public class MonetDarkColorOverridePreference extends Preference {

    public View boundView;

    public MonetDarkColorOverridePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setWidgetLayoutResource(R.layout.preference_color_override);
        setSummary("TODO");
    }

    private int getColorForKey(String str) {
        if (str.equalsIgnoreCase("monet_override_dark_link_color")) {
            return MonetUtil.getLinkColor(true);
        }
        if (str.equalsIgnoreCase("monet_override_dark_primary_text_color")) {
            return MonetUtil.getPrimaryTextColor(true);
        }
        if (str.equalsIgnoreCase("monet_override_dark_secondary_text_color")) {
            return MonetUtil.getSecondaryTextColor(true);
        }
        if (str.equalsIgnoreCase("monet_override_light_link_color")) {
            return MonetUtil.getLinkColor(false);
        }
        if (str.equalsIgnoreCase("monet_override_light_primary_text_color")) {
            return MonetUtil.getPrimaryTextColor(false);
        }
        if (str.equalsIgnoreCase("monet_override_light_secondary_text_color")) {
            return MonetUtil.getSecondaryTextColor(false);
        }
        throw new RuntimeException("Unexpected color key: " + str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void applyColorSelection(String str, int color) {
        if (color == 0) {
            SettingsSingleton.getInstance().putString(str, null);
        } else {
            SettingsSingleton.getInstance().putString(str, ColorUtils.toHexColor(color));
        }
        SettingsSingleton.getInstance().setIntField(str, color);
        MonetUtil.reload();
        MonetThemeReceiver.broadcast();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onColorRowClicked(boolean hasOverride, int color, final String str, View view) {
        if (!hasOverride) {
            color = getColorForKey(str);
        }
        ColorSelectionFragment colorSelectionFragmentI4 = ColorSelectionFragment.newInstance(color, "Auto", false);
        colorSelectionFragmentI4.setColorSelectedListener(new ColorSelectionFragment.ColorSelectedListener() { // from class: db.b
            @Override // com.resync.app.fragments.ColorSelectionFragment.ColorSelectedListener
            public final void onColorSelected(int selectedColor) {
                MonetDarkColorOverridePreference.applyColorSelection(str, selectedColor);
            }
        });
        colorSelectionFragmentI4.show(ContextHelper.getFragmentManager(getContext()), ColorSelectionFragment.TAG);
    }

    protected int getHighlightColor() {
        int linkColor = MonetUtil.getLinkColorAuto();
        return Color.argb(60, Color.red(linkColor), Color.green(linkColor), Color.blue(linkColor));
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.boundView = preferenceViewHolder.itemView;
        final String key = getKey();
        final int overrideColor = SettingsSingleton.getInstance().getIntField(key);
        final boolean hasOverride = overrideColor != 0;
        TextView textView = (TextView) this.boundView.findViewById(android.R.id.title);
        textView.setTextColor(MonetUtil.getPrimaryTextColorAuto());
        textView.setTextSize(1, RedditHelper.getTitleTextSize(SettingsSingleton.getSettings().fontSize));
        textView.setTypeface(TypefaceManager.get(11));
        textView.setSingleLine(false);
        TextView textView2 = (TextView) this.boundView.findViewById(android.R.id.summary);
        textView2.setTextColor(MonetUtil.getSecondaryTextColorAuto());
        textView2.setTextSize(1, RedditHelper.getSmallHeaderTextSize(SettingsSingleton.getSettings().fontSize));
        textView2.setTypeface(TypefaceManager.get(9));
        if (hasOverride) {
            textView2.setText(ColorUtils.toHexColor(overrideColor));
        } else {
            textView2.setText("Automatic themeing enabled");
        }
        if (StringUtils.equalsIgnoreCase(getKey(), (String) preferenceViewHolder.itemView.getTag(R.id.preferences_key_highlight))) {
            this.boundView.setForeground(new ColorDrawable(getHighlightColor()));
        } else {
            this.boundView.setForeground(new ColorDrawable(0));
        }
        ColorView colorView = (ColorView) preferenceViewHolder.itemView.findViewById(R.id.preview_color);
        colorView.setColor(overrideColor != 0 ? overrideColor : getColorForKey(key));
        ((ImageView) preferenceViewHolder.itemView.findViewById(R.id.preview_color_automatic)).setImageTintList(ColorStateList.valueOf(ColorUtils.isDark(colorView.getColor()) ? -1 : -16777216));
        preferenceViewHolder.itemView.findViewById(R.id.preview_color_automatic).setVisibility(hasOverride ? 8 : 0);
        preferenceViewHolder.itemView.setTag(R.id.preference_title, getSummary());
        preferenceViewHolder.itemView.setTag(R.id.preference_key, getKey());
        preferenceViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: db.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MonetDarkColorOverridePreference.this.onColorRowClicked(hasOverride, overrideColor, key, view);
            }
        });
    }
}
