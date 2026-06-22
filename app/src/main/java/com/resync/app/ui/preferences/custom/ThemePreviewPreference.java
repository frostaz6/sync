package com.resync.app.ui.preferences.custom;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.google.android.material.button.MaterialButton;
import com.resync.app.R;
import com.resync.app.receiver.MonetThemeReceiver;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.preferences.custom.ThemePreviewPreference;
import com.resync.app.ui.views.preferences.ThemePreferencePreview;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.ScrollToTopOfPreferencesEvent;

/* JADX INFO: loaded from: classes.dex */
public class ThemePreviewPreference extends Preference {
    public static final String KEY = ThemePreviewPreference.class.getSimpleName();

    public View boundView;

    public ThemePreviewPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setKey(KEY);
        setLayoutResource(R.layout.preference_theme_preview);
        setSelectable(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void onAutomaticClicked(MaterialButton materialButton, MaterialButton materialButton2, View view) {
        if (Build.VERSION.SDK_INT < 31) {
            StaticToast.show("You need to be on Android 12 for this feature");
            return;
        }
        BusSingleton.getBus().post(new ScrollToTopOfPreferencesEvent());
        SettingsSingleton.getInstance().putBoolean("monet_system", true);
        SettingsSingleton.getSettings().monet_system = true;
        materialButton.setSelected(true);
        materialButton2.setSelected(false);
        MonetUtil.reload();
        MonetThemeReceiver.broadcast();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void onManualClicked(MaterialButton materialButton, MaterialButton materialButton2, View view) {
        BusSingleton.getBus().post(new ScrollToTopOfPreferencesEvent());
        SettingsSingleton.getInstance().putBoolean("monet_system", false);
        SettingsSingleton.getSettings().monet_system = false;
        materialButton.setSelected(false);
        materialButton2.setSelected(true);
        MonetUtil.reload();
        MonetThemeReceiver.broadcast();
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View view = preferenceViewHolder.itemView;
        this.boundView = view;
        view.setEnabled(false);
        ((ThemePreferencePreview) this.boundView.findViewById(R.id.theme_preference_preview)).refresh();
        final MaterialButton materialButton = (MaterialButton) this.boundView.findViewById(R.id.button_automatic);
        final MaterialButton materialButton2 = (MaterialButton) this.boundView.findViewById(R.id.button_manual);
        materialButton.setSelected(SettingsSingleton.getSettings().monet_system);
        materialButton2.setSelected(!SettingsSingleton.getSettings().monet_system);
        materialButton.setOnClickListener(new View.OnClickListener() { // from class: ab.e
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ThemePreviewPreference.onAutomaticClicked(materialButton, materialButton2, view2);
            }
        });
        materialButton2.setOnClickListener(new View.OnClickListener() { // from class: ab.f
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ThemePreviewPreference.onManualClicked(materialButton, materialButton2, view2);
            }
        });
    }
}
