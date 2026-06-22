package com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base;

import android.widget.TextView;
import com.resync.app.R;
import com.resync.app.ui.views.onboarding.OnboardingDeviceIconView;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: renamed from: com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.a */
/* JADX INFO: compiled from: AbstractDeviceAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractDeviceAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    OnboardingDeviceIconView mDeviceIconView;

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public void onContentSetup() {
        addContentLayout(R.layout.abstract_device_alert_dialog_bottom_sheet);
        this.mDeviceIconView = (OnboardingDeviceIconView) getView().findViewById(R.id.device_icon_view);
        if (isDeviceAnimated()) {
            this.mDeviceIconView.applyWhiteIconTint();
        }
        if (getDeviceIconRes() != 0) {
            this.mDeviceIconView.showIcon(getDeviceIconRes());
        } else if (StringUtils.isNotEmpty(getSubreddit())) {
            this.mDeviceIconView.showSubreddit(getSubreddit());
        }
        if (StringUtils.isNotEmpty(getMessage())) {
            TextView textView = this.mMessageTextView;
            textView.setPadding(textView.getPaddingLeft(), 0, this.mMessageTextView.getPaddingRight(), SizeHelper.dpToPx(32));
        }
    }

    public int getDeviceIconRes() {
        return 0;
    }

    public boolean isDeviceAnimated() {
        return true;
    }
}
