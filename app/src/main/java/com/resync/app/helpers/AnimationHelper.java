package com.resync.app.helpers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.ViewGroup;

/* JADX INFO: renamed from: t7.d */
/* JADX INFO: compiled from: AnimationHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class AnimationHelper {

    /* JADX INFO: renamed from: t7.d$a */
    /* JADX INFO: compiled from: AnimationHelper.java */
    static class ExpandStartListener implements Animator.AnimatorListener {

        final /* synthetic */ ViewGroup viewGroup;

        ExpandStartListener(ViewGroup viewGroup) {
            this.viewGroup = viewGroup;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            for (int i = 0; i < this.viewGroup.getChildCount(); i++) {
                this.viewGroup.getChildAt(i).animate().setDuration(250L).alpha(1.0f);
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* JADX INFO: renamed from: t7.d$b */
    /* JADX INFO: compiled from: AnimationHelper.java */
    static class ExpandUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        final /* synthetic */ ViewGroup viewGroup;

        ExpandUpdateListener(ViewGroup viewGroup) {
            this.viewGroup = viewGroup;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            this.viewGroup.getLayoutParams().height = (int) ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.viewGroup.requestLayout();
        }
    }

    /* JADX INFO: renamed from: t7.d$c */
    /* JADX INFO: compiled from: AnimationHelper.java */
    static class CollapseUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        final /* synthetic */ ViewGroup viewGroup;

        CollapseUpdateListener(ViewGroup viewGroup) {
            this.viewGroup = viewGroup;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            this.viewGroup.getLayoutParams().height = (int) ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.viewGroup.requestLayout();
        }
    }

    /* JADX INFO: renamed from: t7.d$d */
    /* JADX INFO: compiled from: AnimationHelper.java */
    static class CollapseEndListener extends AnimatorListenerAdapter {

        final /* synthetic */ ViewGroup viewGroup;

        CollapseEndListener(ViewGroup viewGroup) {
            this.viewGroup = viewGroup;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.viewGroup.setVisibility(8);
        }
    }

    public static void expand(ViewGroup viewGroup) {
        viewGroup.measure(0, 0);
        float measuredHeight = viewGroup.getMeasuredHeight();
        viewGroup.getLayoutParams().height = 0;
        viewGroup.requestLayout();
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).setAlpha(0.0f);
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, measuredHeight);
        valueAnimatorOfFloat.setDuration(250L);
        valueAnimatorOfFloat.addListener(new ExpandStartListener(viewGroup));
        valueAnimatorOfFloat.addUpdateListener(new ExpandUpdateListener(viewGroup));
        valueAnimatorOfFloat.start();
    }

    public static void collapse(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).setAlpha(0.0f);
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(viewGroup.getHeight(), 0.0f);
        valueAnimatorOfFloat.setDuration(250L);
        valueAnimatorOfFloat.addUpdateListener(new CollapseUpdateListener(viewGroup));
        valueAnimatorOfFloat.addListener(new CollapseEndListener(viewGroup));
        valueAnimatorOfFloat.start();
    }
}
