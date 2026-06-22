package com.resync.app.ui.transitions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.view.ViewGroup;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.activities.media.SingleImageActivity;
import com.resync.app.R;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes2.dex */
@TargetApi(21)
public class WindowTransition extends Transition {

    private int mode;
    private int startAlpha;
    int domColor;

    class BackgroundColorUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        final /* synthetic */ TransitionValues transitionValues;

        BackgroundColorUpdateListener(TransitionValues transitionValues) {
            this.transitionValues = transitionValues;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            this.transitionValues.view.setBackgroundColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
        }
    }

    class BackgroundAlphaUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        final /* synthetic */ TransitionValues transitionValues;

        BackgroundAlphaUpdateListener(TransitionValues transitionValues) {
            this.transitionValues = transitionValues;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            try {
                this.transitionValues.view.getBackground().setAlpha(((Integer) valueAnimator.getAnimatedValue()).intValue());
            } catch (Exception e) {
                Logger.logException(e);
            }
        }
    }

    class LoggingAnimatorListener extends AnimatorListenerAdapter {
        LoggingAnimatorListener() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            Logger.verbose("Transition", "Animation ended!!");
            animator.removeAllListeners();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            Logger.verbose("Transition", "Animation started!!");
        }
    }

    public WindowTransition(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Logger.verbose("DOM COLOR STARTING TRANSITION");
        BaseActivity baseActivityA = ContextHelper.getBaseActivity(context);
        if (baseActivityA instanceof SingleImageActivity) {
            this.domColor = ((SingleImageActivity) baseActivityA).getDomColor();
        } else {
            this.domColor = -16777216;
        }
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{R.attr.transitionDirection});
        int direction = typedArrayObtainStyledAttributes.getInt(0, 0);
        typedArrayObtainStyledAttributes.recycle();
        if (direction == 0) {
            throw new IllegalArgumentException("No mode was specified");
        }
        setMode(direction);
        Logger.verbose("Transition", "CREATED NAVIGATION TRANSITION: " + direction);
    }

    public void setMode(int mode) {
        if (mode != 1 && mode != 2) {
            throw new IllegalArgumentException("Only MODE_IN and MODE_OUT flags are allowed");
        }
        this.mode = mode;
    }

    @Override // android.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
    }

    @Override // android.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        try {
            this.startAlpha = transitionValues.view.getBackground().getAlpha();
        } catch (Exception unused) {
        }
    }

    @Override // android.transition.Transition
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        ValueAnimator valueAnimatorOfInt;
        int domColor = this.domColor;
        int mode = this.mode;
        if (mode == 1) {
            valueAnimatorOfInt = ValueAnimator.ofArgb(0, domColor);
            valueAnimatorOfInt.addUpdateListener(new BackgroundColorUpdateListener(transitionValues));
        } else {
            if (mode != 2) {
                throw new RuntimeException("Unsupported transition mode");
            }
            valueAnimatorOfInt = ValueAnimator.ofInt(this.startAlpha, 0);
            valueAnimatorOfInt.addUpdateListener(new BackgroundAlphaUpdateListener(transitionValues));
        }
        valueAnimatorOfInt.addListener(new LoggingAnimatorListener());
        return valueAnimatorOfInt;
    }

    @Override // android.transition.Transition
    public boolean isTransitionRequired(TransitionValues transitionValues, TransitionValues transitionValues2) {
        return true;
    }
}
