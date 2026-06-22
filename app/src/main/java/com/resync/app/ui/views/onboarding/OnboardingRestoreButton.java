package com.resync.app.ui.views.onboarding;

import android.content.Context;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.appcompat.widget.AppCompatTextView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.ui.interfaces.DarkModeAware;
import com.resync.app.util.RippleUtils;

/* JADX INFO: loaded from: classes3.dex */
public class OnboardingRestoreButton extends AppCompatTextView implements DarkModeAware {

    class RoundedOutlineProvider extends ViewOutlineProvider {
        RoundedOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), SizeHelper.dpToPx(32));
        }
    }

    public OnboardingRestoreButton(Context context, AttributeSet attributeSet) {
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
        int backgroundColor = MonetUtil.getAccent2BackgroundColorAuto();
        int textColor = MonetUtil.getAccent2TextColorAuto();
        setBackgroundColor(backgroundColor);
        setTextColor(textColor);
        setForeground(RippleUtils.getSelectableBackground(NightHelper.isNight() ? 301989887 : 872415231));
        setTypeface(TypefaceManager.get(11));
    }
}
