package com.resync.app.ui.views.behaviour;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes3.dex */
public class ImageViewerBottomSheetBehavior<V extends View> extends BottomSheetBehavior<V> {
    private Context context;

    class CollapseRunnable implements Runnable {
        CollapseRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ImageViewerBottomSheetBehavior.this.setState(4);
        }
    }

    public ImageViewerBottomSheetBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
    }

    public static <V extends View> ImageViewerBottomSheetBehavior<V> from(V v) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        if (!(layoutParams instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
        if (behavior instanceof BottomSheetBehavior) {
            return (ImageViewerBottomSheetBehavior) behavior;
        }
        throw new IllegalArgumentException("The view is not associated with BottomSheetBehavior");
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.c
    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        return super.onTouchEvent(coordinatorLayout, v, motionEvent);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.c
    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        if (getState() == 3 && motionEvent.getAction() == 1) {
            float y = motionEvent.getY();
            float top = v.getTop();
            Logger.verbose("Y: " + y);
            Logger.verbose("TOP: " + coordinatorLayout.getTop());
            if (y < top) {
                v.postDelayed(new CollapseRunnable(), 120L);
                return true;
            }
        }
        return super.onInterceptTouchEvent(coordinatorLayout, v, motionEvent);
    }
}
