package com.resync.app.ui.views.onboarding;

import android.content.Context;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.appcompat.widget.AppCompatTextView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.ui.interfaces.DarkModeAware;
import com.resync.app.util.RippleUtils;

/* JADX INFO: loaded from: classes3.dex */
public class OnboardingBackButton extends AppCompatTextView implements DarkModeAware {

    class RoundedOutlineProvider extends ViewOutlineProvider {
        RoundedOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), SizeHelper.dpToPx(32));
        }
    }

    public OnboardingBackButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOutlineProvider(new RoundedOutlineProvider());
        setClipToOutline(true);
        applyColors();
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }

    public void applyColors() {
        setBackgroundColor(MonetUtil.getBackgroundColorAuto());
        setTextColor(MonetUtil.getLinkColorAuto());
        setForeground(RippleUtils.getSelectableBackground());
    }
}
