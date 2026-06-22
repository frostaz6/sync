package com.resync.app.ui.views.comments;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.transparent_status.CustomCoordinatorLayout;

/* JADX INFO: loaded from: classes3.dex */
public class CommentsCoordinatorLayout extends CustomCoordinatorLayout {
    private boolean scrollEnabled;
    View navBar;
    private int scrollType;
    private ValueAnimator animator;
    private boolean snapEnabled;

    class TranslationUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ View targetView;

        TranslationUpdateListener(View view) {
            this.targetView = view;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            this.targetView.setTranslationY(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    public CommentsCoordinatorLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.scrollEnabled = true;
        this.snapEnabled = SettingsSingleton.getSettings().snap;
    }

    private void animateNavBar(View navBar, boolean show) {
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator == null) {
            ValueAnimator newAnimator = new ValueAnimator();
            this.animator = newAnimator;
            newAnimator.setInterpolator(new DecelerateInterpolator());
            this.animator.setDuration(150L);
            this.animator.addUpdateListener(new TranslationUpdateListener(navBar));
        } else {
            valueAnimator.cancel();
        }
        this.animator.setFloatValues(navBar.getTranslationY(), show ? 0.0f : navBar.getHeight());
        this.animator.start();
    }

    @Override // android.view.View
    public boolean canScrollVertically(int direction) {
        return super.canScrollVertically(direction);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout, androidx.core.view.NestedScrollingParent2
    public boolean onStartNestedScroll(View child, View target, int axes, int type) {
        this.scrollType = type;
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        return super.onStartNestedScroll(child, target, axes, type);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.navBar = findViewById(com.resync.app.R.id.navigation_material);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout, androidx.core.view.NestedScrollingParent2
    public void onStopNestedScroll(View target, int type) {
        super.onStopNestedScroll(target, type);
        if (this.snapEnabled && SettingsSingleton.getSettings().commentNavBarHide) {
            if (this.scrollType == 0 || type == 1) {
                if (this.navBar.getTranslationY() >= this.navBar.getHeight() * 0.5f) {
                    animateNavBar(this.navBar, false);
                } else {
                    animateNavBar(this.navBar, true);
                }
            }
        }
    }

    public boolean isScrollEnabled() {
        return this.scrollEnabled;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout, androidx.core.view.NestedScrollingParent2
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed, int type) {
        if (isScrollEnabled()) {
            super.onNestedPreScroll(target, dx, dy, consumed, type);
            if (SettingsSingleton.getSettings().commentNavBarHide) {
                this.navBar.setTranslationY(Math.max(0.0f, Math.min(this.navBar.getHeight(), this.navBar.getTranslationY() + dy)));
            }
        }
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        this.scrollEnabled = scrollEnabled;
    }
}
