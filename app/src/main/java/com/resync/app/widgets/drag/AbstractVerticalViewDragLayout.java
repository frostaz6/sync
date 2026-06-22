package com.resync.app.widgets.drag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.media.MultiImageActivity;
import com.resync.app.ui.activities.media.SingleImageActivity;
import com.resync.app.events.WindowAlphaEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.WindowUtils;

/* JADX INFO: renamed from: bc.a */
/* JADX INFO: compiled from: AbstractVerticalViewDragLayout.java */
/* JADX INFO: loaded from: classes3.dex */
public abstract class AbstractVerticalViewDragLayout<T extends View> extends FrameLayout implements VerticalViewDragLayoutInterface {

    protected ViewDragHelper dragHelper;
    protected T view;
    protected int lastAlpha;

    /* JADX INFO: renamed from: bc.a$b */
    /* JADX INFO: compiled from: AbstractVerticalViewDragLayout.java */
    private class DragCallback extends ViewDragHelper.Callback {
        private DragCallback() {
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public int clampViewPositionHorizontal(View view, int left, int dx) {
            return view.getLeft();
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public int clampViewPositionVertical(View view, int top, int dy) {
            return top;
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public int getViewHorizontalDragRange(View view) {
            return AbstractVerticalViewDragLayout.this.view.getWidth();
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public int getViewVerticalDragRange(View view) {
            return AbstractVerticalViewDragLayout.this.view.getHeight();
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            AbstractVerticalViewDragLayout abstractVerticalViewDragLayout = AbstractVerticalViewDragLayout.this;
            abstractVerticalViewDragLayout.dragHelper.captureChildView(abstractVerticalViewDragLayout.view, pointerId);
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onViewPositionChanged(View view, int left, int top, int dx, int dy) {
            AbstractVerticalViewDragLayout.this.invalidate();
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public void onViewReleased(View view, float xvel, float yvel) {
            AbstractVerticalViewDragLayout.this.onReleased(yvel);
        }

        @Override // androidx.customview.widget.ViewDragHelper.Callback
        public boolean tryCaptureView(View view, int pointerId) {
            if (AbstractVerticalViewDragLayout.this.isReady() && SettingsSingleton.getSettings().swipeDismiss) {
                return view.equals(AbstractVerticalViewDragLayout.this.view);
            }
            return false;
        }
    }

    public AbstractVerticalViewDragLayout(Context context) {
        super(context);
        init();
    }

    private void settleToCenter() {
        if (this.dragHelper.smoothSlideViewTo(this.view, 0, (getHeight() - this.view.getHeight()) / 2)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onReleased(float yvel) {
        float bottom = this.view.getBottom() - (this.view.getHeight() / 2);
        float height = getHeight() / 2;
        boolean movedFarEnough = Math.abs(Math.max(bottom, height) - Math.min(bottom, height)) >= ((float) ((int) SizeHelper.dpToPxFloat(50)));
        boolean hasVelocity = Math.abs(yvel) > 0.0f;
        if (!movedFarEnough && !hasVelocity) {
            settleToCenter();
            return;
        }
        this.view.setVisibility(4);
        this.view.setTranslationX(WindowUtils.getDisplayWidth());
        // getViewHeight()/getViewTop() read the child view's geometry/scale; on a fast dismiss the view can be mid-teardown
        // and throw. If that happens, still finish the host cleanly rather than crashing the app.
        try {
            if (getContext() instanceof SingleImageActivity) {
                ((SingleImageActivity) getContext()).resizeImage(getViewHeight(), getViewTop());
            } else if (getContext() instanceof MultiImageActivity) {
                ((MultiImageActivity) getContext()).close();
            }
        } catch (Throwable t) {
            if (getContext() instanceof android.app.Activity) {
                ((android.app.Activity) getContext()).finish();
            }
        }
    }

    private void init() {
        float minVelocity = RedditApplication.getAppContext().getResources().getDisplayMetrics().density * 10000.0f;
        ViewDragHelper viewDragHelperCreate = ViewDragHelper.create(this, 1.0f, new DragCallback());
        this.dragHelper = viewDragHelperCreate;
        viewDragHelperCreate.setMinVelocity(minVelocity);
    }

    protected void addContentView(T t) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.gravity = 17;
        t.setLayoutParams(layoutParams);
        addView(t);
    }

    @Override // android.view.View
    public void computeScroll() {
        if (getHeight() == 0) {
            return;
        }
        float top = this.view.getTop() + (this.view.getHeight() / 2);
        float height = getHeight() / 2;
        if (top > height) {
            top = height - (top - height);
        }
        int alpha = (int) (Math.min(Math.abs(Math.max(0.0f, top) / height), 1.0f) * 255.0f);
        if (alpha != this.lastAlpha) {
            BusSingleton.getBus().post(new WindowAlphaEvent(alpha));
            this.lastAlpha = alpha;
        }
        if (this.dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public T getView() {
        return this.view;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (getVisibility() != 0) {
            return false;
        }
        int action = MotionEventCompat.getActionMasked(motionEvent);
        if (action == 3 || action == 1) {
            this.dragHelper.cancel();
            return false;
        }
        try {
            return this.dragHelper.shouldInterceptTouchEvent(motionEvent);
        } catch (ArrayIndexOutOfBoundsException unused) {
            return super.onInterceptTouchEvent(motionEvent);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (getVisibility() != 0) {
            return false;
        }
        try {
            this.dragHelper.processTouchEvent(motionEvent);
            return true;
        } catch (IllegalArgumentException | IndexOutOfBoundsException unused) {
            return true;
        }
    }

    public AbstractVerticalViewDragLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }
}
