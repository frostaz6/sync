package com.resync.app.ui.views.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/* JADX INFO: loaded from: classes3.dex */
public class ConsumingHorizontalScrollView extends HorizontalScrollView {

    private GestureDetector gestureDetector;

    class ScrollGestureListener extends GestureDetector.SimpleOnGestureListener {
        ScrollGestureListener() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float distanceX, float distanceY) {
            return Math.abs(distanceY) > Math.abs(distanceX);
        }
    }

    public ConsumingHorizontalScrollView(Context context) {
        super(context);
        this.gestureDetector = new GestureDetector(context, new ScrollGestureListener());
        setFadingEdgeLength(0);
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (getScrollX() == 0) {
            getParent().requestDisallowInterceptTouchEvent(false);
            return super.onInterceptTouchEvent(motionEvent);
        }
        if (this.gestureDetector.onTouchEvent(motionEvent)) {
            getParent().requestDisallowInterceptTouchEvent(false);
            return super.onInterceptTouchEvent(motionEvent);
        }
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(motionEvent);
    }

    public ConsumingHorizontalScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.gestureDetector = new GestureDetector(context, new ScrollGestureListener());
        setFadingEdgeLength(0);
    }
}
