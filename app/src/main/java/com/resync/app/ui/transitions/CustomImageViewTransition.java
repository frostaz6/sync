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
import android.view.View;
import android.view.ViewGroup;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.activities.CommentsActivity;
import com.resync.app.ui.views.images.CustomImageView;
import com.resync.app.R;
import com.resync.app.widgets.ColorFilterProvider;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes2.dex */
@TargetApi(21)
public class CustomImageViewTransition extends Transition {

    private int mode;
    private boolean notFromComments;

    class ColorFilterUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        final /* synthetic */ CustomImageView imageView;

        ColorFilterUpdateListener(CustomImageView customImageView) {
            this.imageView = customImageView;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            this.imageView.setColorFilter(ColorFilterProvider.getColorFilter(((Integer) valueAnimator.getAnimatedValue()).intValue()));
        }
    }

    class AnimatorCleanupListener extends AnimatorListenerAdapter {

        final /* synthetic */ ValueAnimator animator;

        AnimatorCleanupListener(ValueAnimator valueAnimator) {
            this.animator = valueAnimator;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.animator.removeAllUpdateListeners();
            this.animator.removeAllListeners();
            animator.removeAllListeners();
            Logger.verbose("CustomImageViewTransition", "Animation cancelled");
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.animator.removeAllUpdateListeners();
            this.animator.removeAllListeners();
            animator.removeAllListeners();
            Logger.verbose("CustomImageViewTransition", "Animation ended");
        }
    }

    public CustomImageViewTransition(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Logger.verbose("CustomImageViewTransition", "Creating a new alpha transition");
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{R.attr.iv_alphaMode});
        int direction = typedArrayObtainStyledAttributes.getInt(0, 0);
        typedArrayObtainStyledAttributes.recycle();
        if (direction == 0) {
            throw new IllegalArgumentException("No mode was specified");
        }
        setMode(direction);
        if (context instanceof BaseActivity) {
            this.notFromComments = !CommentsActivity.class.getSimpleName().equals(((BaseActivity) context).getPreviousActivityName());
        }
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
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        ValueAnimator valueAnimatorOfArgb;
        View view = transitionValues.view;
        if (view instanceof CustomImageView) {
            CustomImageView customImageView = (CustomImageView) view;
            boolean shouldDim = customImageView.shouldDim();
            if (this.mode == 2 && ConnectivityHelper.isConnected(RedditApplication.getAppContext()) && SettingsSingleton.getSettings().markAsRead && this.notFromComments) {
                shouldDim = true;
            }
            if (!SettingsSingleton.getSettings().dimImages) {
                shouldDim = false;
            }
            boolean isBlur = customImageView.isBlur();
            if (this.mode == 1 && isBlur && customImageView.isNsfw()) {
                isBlur = true;
            }
            Logger.verbose("CustomImageViewTransition", "Was viewed: " + shouldDim);
            Logger.verbose("CustomImageViewTransition", "Was NSFW: " + isBlur);
            viewGroup.getContext();
            int overlayColor = CustomImageView.resolveOverlayColor(shouldDim, isBlur);
            if (shouldDim || isBlur) {
                Logger.verbose("MODE: " + this.mode);
                int mode = this.mode;
                if (mode == 1) {
                    valueAnimatorOfArgb = ValueAnimator.ofArgb(overlayColor, 0);
                } else {
                    if (mode != 2) {
                        throw new RuntimeException("Unsupported transition mode");
                    }
                    valueAnimatorOfArgb = ValueAnimator.ofArgb(0, overlayColor);
                }
                valueAnimatorOfArgb.addUpdateListener(new ColorFilterUpdateListener(customImageView));
                valueAnimatorOfArgb.addListener(new AnimatorCleanupListener(valueAnimatorOfArgb));
                return valueAnimatorOfArgb;
            }
        }
        return super.createAnimator(viewGroup, transitionValues, transitionValues2);
    }

    @Override // android.transition.Transition
    public boolean isTransitionRequired(TransitionValues transitionValues, TransitionValues transitionValues2) {
        return true;
    }
}
