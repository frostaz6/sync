package com.resync.app.ui.preferences.custom;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.google.android.material.slider.RangeSlider;
import com.resync.app.R;
import com.resync.app.receiver.MonetThemeReceiver;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.monet.MonetSlider;
import com.resync.app.helpers.ui.MonetUtil;

/* JADX INFO: loaded from: classes.dex */
public class SliderPreference extends Preference {
    public static final String KEY = SliderPreference.class.getSimpleName();

    float colorIntensity;

    class SliderTouchListener implements RangeSlider.OnSliderTouchListener {
        SliderTouchListener() {
        }

        @Override // com.google.android.material.slider.BaseOnSliderTouchListener
        public void onStartTrackingTouch(RangeSlider rangeSlider) {
        }

        @Override // com.google.android.material.slider.BaseOnSliderTouchListener
        public void onStopTrackingTouch(RangeSlider rangeSlider) {
            SettingsSingleton.getInstance().putString("monet_color_intensity", Float.toString(SliderPreference.this.colorIntensity));
            SettingsSingleton.getSettings().monet_color_intensity = SliderPreference.this.colorIntensity;
            MonetUtil.reload();
            MonetThemeReceiver.broadcast();
        }
    }

    class SliderChangeListener implements RangeSlider.OnChangeListener {
        SliderChangeListener() {
        }

        @Override // com.google.android.material.slider.BaseOnChangeListener
        public void onValueChange(RangeSlider rangeSlider, float value, boolean fromUser) {
            SliderPreference.this.colorIntensity = value;
        }
    }

    public SliderPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R.layout.preference_slider);
        setSelectable(false);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        MonetSlider monetSlider = (MonetSlider) preferenceViewHolder.itemView.findViewById(R.id.chroma);
        monetSlider.applyColors();
        monetSlider.setValues(Float.valueOf(SettingsSingleton.getSettings().monet_color_intensity));
        monetSlider.addOnSliderTouchListener(new SliderTouchListener());
        monetSlider.addOnChangeListener(new SliderChangeListener());
    }
}
