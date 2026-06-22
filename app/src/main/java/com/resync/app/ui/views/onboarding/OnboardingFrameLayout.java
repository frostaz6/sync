package com.resync.app.ui.views.onboarding;

import android.content.Context;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class OnboardingFrameLayout extends FrameLayout implements DarkModeAware {

    boolean interceptTouchEvents;

    class RoundedOutlineProvider extends ViewOutlineProvider {
        RoundedOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), SizeHelper.dpToPx(12));
        }
    }

    public OnboardingFrameLayout(Context context) {
        super(context);
        this.interceptTouchEvents = false;
        init();
    }

    private void init() {
        setOutlineProvider(new RoundedOutlineProvider());
        setClipToOutline(true);
        applyColors();
    }

    private void applyColors() {
        setBackgroundColor(MonetUtil.getBackgroundColorAuto());
    }

    public void disableTouch() {
        this.interceptTouchEvents = true;
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.interceptTouchEvents) {
            return true;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    public OnboardingFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.interceptTouchEvents = false;
        init();
    }
}
