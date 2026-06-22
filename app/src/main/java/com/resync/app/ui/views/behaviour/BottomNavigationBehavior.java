package com.resync.app.ui.views.behaviour;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.main.SyncBottomNavigationView;
import com.resync.app.ui.views.monet.MonetLinearProgressIndicator;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: loaded from: classes3.dex */
public class BottomNavigationBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    private int scrollAxis;
    private ValueAnimator animator;
    private boolean snap;

    class TranslationUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        final /* synthetic */ View view;

        TranslationUpdateListener(View view) {
            this.view = view;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            this.view.setTranslationY(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    public BottomNavigationBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.snap = true;
        this.snap = SettingsSingleton.getSettings().snap;
    }

    public static <V extends View> BottomNavigationBehavior<V> from(V v) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        if (!(layoutParams instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
        if (behavior instanceof BottomNavigationBehavior) {
            return (BottomNavigationBehavior) behavior;
        }
        throw new IllegalArgumentException("The view is not associated with BottomNavigationBehavior");
    }

    private void updateSnackbar(View view, Snackbar.SnackbarLayout snackbarLayout) {
        if (snackbarLayout.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackbarLayout.getLayoutParams();
            params.setAnchorId(R.id.navigation);
            params.anchorGravity = 48;
            params.gravity = 48;
            snackbarLayout.setTranslationY(-SizeHelper.dpToPx(16));
        }
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.c
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, View view2, int axes, int type) {
        if (!SettingsSingleton.getSettings().navigationBottomHide || axes != 2) {
            return false;
        }
        this.scrollAxis = type;
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator == null) {
            return true;
        }
        valueAnimator.cancel();
        return true;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.c
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, int type) {
        if (this.snap) {
            if (this.scrollAxis == 0 || type == 1) {
                if (v.getTranslationY() >= v.getHeight() * 0.5f) {
                    animateTo(v, false);
                } else {
                    animateTo(v, true);
                }
            }
        }
    }

    public void animateTo(View view, boolean show) {
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator == null) {
            ValueAnimator valueAnimator2 = new ValueAnimator();
            this.animator = valueAnimator2;
            valueAnimator2.setInterpolator(new DecelerateInterpolator());
            this.animator.setDuration(100L);
            this.animator.addUpdateListener(new TranslationUpdateListener(view));
        } else {
            valueAnimator.cancel();
        }
        this.animator.setFloatValues(view.getTranslationY(), show ? 0.0f : view.getHeight());
        this.animator.start();
    }

    public void show(V v) {
        animateTo(v, true);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.c
    public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, V v, View view) {
        if ((view instanceof Snackbar.SnackbarLayout) && (v instanceof SyncBottomNavigationView) && v.getVisibility() == 0) {
            updateSnackbar(v, (Snackbar.SnackbarLayout) view);
        }
        if ((view instanceof MonetLinearProgressIndicator) && v.getVisibility() == 0 && (view.getLayoutParams() instanceof CoordinatorLayout.LayoutParams)) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
            params.setAnchorId(R.id.navigation);
            params.anchorGravity = 48;
            params.gravity = 48;
        }
        return super.layoutDependsOn(coordinatorLayout, v, view);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.c
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, V v, View view, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.c
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V v, View view, float velocityX, float velocityY) {
        return false;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.c
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V v, View view, int dx, int dy, int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, v, view, dx, dy, consumed, type);
        v.setTranslationY(Math.max(0.0f, Math.min(v.getHeight(), v.getTranslationY() + dy)));
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.c
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, int[] consumed) {
    }

    public BottomNavigationBehavior() {
        this.snap = true;
        this.snap = SettingsSingleton.getSettings().snap;
    }
}
