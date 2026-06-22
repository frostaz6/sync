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
import com.resync.app.ui.transitions.NavigationTransition;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.helpers.ActionBarHelper;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.GestureNavigationHelper;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes2.dex */
@TargetApi(21)
public class NavigationTransition extends Transition {

    private int mode;
    private int toolbarColor;
    private int domColor;

    boolean gestureNavEnabled;

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

    public NavigationTransition(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (ActionBarHelper.getToolbarColor() == -1) {
            this.toolbarColor = 0;
        } else {
            this.toolbarColor = ActionBarHelper.getToolbarColor();
        }
        BaseActivity baseActivityA = ContextHelper.getBaseActivity(context);
        if (baseActivityA instanceof SingleImageActivity) {
            this.domColor = ((SingleImageActivity) baseActivityA).getDomColor();
        }
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, SyncStyleable.NAVIGATION_TRANSITION);
        int direction = typedArrayObtainStyledAttributes.getInt(0, 0);
        typedArrayObtainStyledAttributes.recycle();
        if (direction == 0) {
            throw new IllegalArgumentException("No mode was specified");
        }
        setMode(direction);
        Logger.verbose("Transition", "CREATED NAVIGATION TRANSITION: " + direction);
        this.gestureNavEnabled = GestureNavigationHelper.isGestureNavEnabled(context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void applyTint(TransitionValues transitionValues, ValueAnimator valueAnimator) {
        transitionValues.view.getBackground().setTint(((Integer) valueAnimator.getAnimatedValue()).intValue());
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
    }

    @Override // android.transition.Transition
    public Animator createAnimator(ViewGroup viewGroup, final TransitionValues transitionValues, TransitionValues transitionValues2) {
        ValueAnimator valueAnimatorOfArgb;
        Logger.verbose("Transition", "Create animator: " + this.mode);
        int mode = this.mode;
        if (mode == 1) {
            valueAnimatorOfArgb = ValueAnimator.ofArgb(this.toolbarColor, this.domColor);
        } else {
            if (mode != 2) {
                throw new RuntimeException("Unsupported transition mode");
            }
            valueAnimatorOfArgb = ValueAnimator.ofArgb(this.domColor, this.toolbarColor);
        }
        valueAnimatorOfArgb.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: fb.a
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                NavigationTransition.applyTint(transitionValues, valueAnimator);
            }
        });
        valueAnimatorOfArgb.addListener(new LoggingAnimatorListener());
        return valueAnimatorOfArgb;
    }

    @Override // android.transition.Transition
    public boolean isTransitionRequired(TransitionValues transitionValues, TransitionValues transitionValues2) {
        return !this.gestureNavEnabled;
    }
}
