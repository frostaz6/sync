package com.resync.app.ui.preferences.defaults;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.TwoStatePreference;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.responsive.CustomMaterialRadioButton;
import com.resync.app.helpers.ui.MonetUtil;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.helpers.TypefaceManager;

/* JADX INFO: loaded from: classes.dex */
public class SyncRadioButtonPreference extends TwoStatePreference {
    public View boundView;

    CustomMaterialRadioButton radioButton;
    boolean checked;

    public SyncRadioButtonPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setWidgetLayoutResource(R.layout.preference_radio_button);
    }

    @Override // androidx.preference.TwoStatePreference
    public boolean isChecked() {
        return this.checked;
    }

    @Override // androidx.preference.TwoStatePreference
    /* JADX INFO: renamed from: P0 */
    public void setChecked(boolean checked) {
        if (this.checked != checked) {
            this.checked = checked;
            CustomMaterialRadioButton customMaterialRadioButton = this.radioButton;
            if (customMaterialRadioButton != null) {
                customMaterialRadioButton.setChecked(checked);
            }
        }
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View view = preferenceViewHolder.itemView;
        this.boundView = view;
        PaddingHelper.setPaddingHorizontal(view, 16, 16);
        CustomMaterialRadioButton customMaterialRadioButton = (CustomMaterialRadioButton) this.boundView.findViewById(R.id.radio_button);
        this.radioButton = customMaterialRadioButton;
        customMaterialRadioButton.setChecked(this.checked);
        TextView textView = (TextView) this.boundView.findViewById(android.R.id.title);
        textView.setTextColor(StyleSingleton.getTextColorAuto(getContext(), false));
        textView.setTextSize(1, RedditHelper.getTitleTextSize(SettingsSingleton.getSettings().fontSize));
        textView.setSingleLine(false);
        textView.setTypeface(TypefaceManager.get(9));
        TextView textView2 = (TextView) this.boundView.findViewById(android.R.id.summary);
        textView2.setTextColor(MonetUtil.getSecondaryTextColorAuto());
        textView2.setTextSize(1, RedditHelper.getSmallHeaderTextSize(SettingsSingleton.getSettings().fontSize));
        textView2.setTypeface(TypefaceManager.get(9));
        if (StringUtils.equalsIgnoreCase(getKey(), (String) preferenceViewHolder.itemView.getTag(R.id.preferences_key_highlight))) {
            this.boundView.setForeground(new ColorDrawable(getHighlightColor()));
        } else {
            this.boundView.setForeground(new ColorDrawable(0));
        }
        preferenceViewHolder.itemView.setTag(R.id.preference_title, getSummary());
        preferenceViewHolder.itemView.setTag(R.id.preference_key, getKey());
    }

    protected int getHighlightColor() {
        int linkColor = MonetUtil.getLinkColorAuto();
        return Color.argb(60, Color.red(linkColor), Color.green(linkColor), Color.blue(linkColor));
    }

    @Override // androidx.preference.TwoStatePreference, androidx.preference.Preference
    /* JADX INFO: renamed from: c0 */
    protected Parcelable onSaveInstanceState() {
        return null;
    }
}
