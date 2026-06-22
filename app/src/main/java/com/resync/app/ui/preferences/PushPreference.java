package com.resync.app.ui.preferences;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.appcompat.widget.SwitchCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.resync.app.R;
import com.resync.app.service.AllNotificationService;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.preferences.PreferenceCheckedView;
import com.resync.app.ui.views.text.BaseTextView;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.TypefaceManager;

/* JADX INFO: loaded from: classes.dex */
public class PushPreference extends Preference {
    public static final String KEY = PushPreference.class.getSimpleName();

    SwitchCompat pushSwitch;
    RelativeLayout accessWrapper;

    PreferenceCheckedView accessIcon;

    BaseTextView accessMessage;

    class ToggleClickListener implements View.OnClickListener {
        ToggleClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            boolean enabled = !SettingsSingleton.getSettings().pushNotifications;
            SettingsSingleton.getInstance().putBoolean("push_notifications", enabled);
            SettingsSingleton.getInstance().getCurrentSettings().pushNotifications = enabled;
            PushPreference.this.pushSwitch.setChecked(enabled);
        }
    }

    class SwitchClickListener implements View.OnClickListener {
        SwitchClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            boolean enabled = !SettingsSingleton.getSettings().pushNotifications;
            SettingsSingleton.getInstance().putBoolean("push_notifications", enabled);
            SettingsSingleton.getInstance().getCurrentSettings().pushNotifications = enabled;
            PushPreference.this.pushSwitch.setChecked(enabled);
        }
    }

    class AccessClickListener implements View.OnClickListener {
        AccessClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            PushPreference.this.getContext().startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }
    }

    public PushPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setKey(KEY);
        setLayoutResource(R.layout.preference_push);
        setIconSpaceReserved(false);
        setSelectable(false);
    }

    protected boolean isNotificationAccessGranted() {
        ComponentName componentName = new ComponentName(getContext(), (Class<?>) AllNotificationService.class);
        String string = Settings.Secure.getString(getContext().getContentResolver(), "enabled_notification_listeners");
        return string != null && string.contains(componentName.flattenToString());
    }

    public void refresh() {
        PreferenceCheckedView preferenceCheckedView = this.accessIcon;
        if (preferenceCheckedView != null) {
            preferenceCheckedView.setChecked(isNotificationAccessGranted());
        }
        if (this.accessMessage != null) {
            if (isNotificationAccessGranted()) {
                this.accessMessage.setText("Notification access is configured!");
            } else {
                this.accessMessage.setText("Tap to configure notification access");
            }
            this.accessMessage.setTextColor(NightHelper.isNight() ? -4736065 : -9868951);
        }
        SwitchCompat switchCompat = this.pushSwitch;
        if (switchCompat != null) {
            switchCompat.setChecked(SettingsSingleton.getSettings().pushNotifications);
        }
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.itemView.setOnClickListener(new ToggleClickListener());
        SwitchCompat switchCompat = (SwitchCompat) preferenceViewHolder.findViewById(R.id.preference_push_check);
        this.pushSwitch = switchCompat;
        switchCompat.setChecked(SettingsSingleton.getSettings().pushNotifications);
        this.pushSwitch.setOnClickListener(new SwitchClickListener());
        this.accessWrapper = (RelativeLayout) preferenceViewHolder.findViewById(R.id.preference_push_access_upper_wrapper);
        this.accessIcon = (PreferenceCheckedView) preferenceViewHolder.findViewById(R.id.preference_push_icon);
        BaseTextView baseTextView = (BaseTextView) preferenceViewHolder.findViewById(R.id.preference_push_access_message);
        this.accessMessage = baseTextView;
        baseTextView.setTypeface(TypefaceManager.get(9));
        this.accessWrapper.setOnClickListener(new AccessClickListener());
        refresh();
    }
}
