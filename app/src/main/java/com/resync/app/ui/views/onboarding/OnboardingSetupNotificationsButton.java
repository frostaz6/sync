package com.resync.app.ui.views.onboarding;

import android.content.Context;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.os.BuildCompat;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.events.NotificationPermissionEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.ui.interfaces.DarkModeAware;
import com.resync.app.util.RippleUtils;

/* JADX INFO: loaded from: classes3.dex */
public class OnboardingSetupNotificationsButton extends AppCompatTextView implements DarkModeAware {

    class RoundedOutlineProvider extends ViewOutlineProvider {
        RoundedOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), SizeHelper.dpToPx(32));
        }
    }

    public OnboardingSetupNotificationsButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOutlineProvider(new RoundedOutlineProvider());
        setClipToOutline(true);
        applyColors();
        updateButtonState();
    }

    private void updateButtonState() {
        if (!BuildCompat.isAtLeastT()) {
            setText("Not required");
            setEnabled(false);
            return;
        }
        setEnabled(true);
        if (ContextCompat.checkSelfPermission(getContext(), "android.permission.POST_NOTIFICATIONS") == 0) {
            setText("Notifications are setup");
        } else {
            setText("Setup");
        }
    }

    private void applyColors() {
        int backgroundColor = MonetUtil.getAccent2BackgroundColorAuto();
        int textColor = MonetUtil.getAccent2TextColorAuto();
        setBackgroundColor(backgroundColor);
        setTextColor(textColor);
        setForeground(RippleUtils.getSelectableBackground(MonetUtil.getBackgroundColorAuto()));
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BusSingleton.getBus().register(this);
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        BusSingleton.getBus().unregister(this);
        super.onDetachedFromWindow();
    }

    @Subscribe
    public void onNotificationPermissionEvent(NotificationPermissionEvent notificationPermissionEvent) {
        updateButtonState();
    }
}
