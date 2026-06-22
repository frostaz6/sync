package com.github.chrisbanes.photoview.gestures;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/* JADX INFO: renamed from: re.a */
/* JADX INFO: compiled from: CupcakeGestureDetector.java */
/* JADX INFO: loaded from: classes3.dex */
public class CupcakeGestureDetector implements GestureDetector {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected OnGestureListener mListener;
    float mLastTouchX;
    float mLastTouchY;
    final float mTouchSlop;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    final float mMinimumVelocity;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private VelocityTracker mVelocityTracker;
    private boolean mIsDragging;

    public CupcakeGestureDetector(Context context) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
    }

    @Override // com.github.chrisbanes.photoview.gestures.GestureDetector
    public boolean onTouchEvent(MotionEvent motionEvent) {
        VelocityTracker velocityTracker;
        int action = motionEvent.getAction();
        if (action == 0) {
            VelocityTracker velocityTrackerObtain = VelocityTracker.obtain();
            this.mVelocityTracker = velocityTrackerObtain;
            if (velocityTrackerObtain != null) {
                velocityTrackerObtain.addMovement(motionEvent);
            }
            this.mLastTouchX = getActiveX(motionEvent);
            this.mLastTouchY = getActiveY(motionEvent);
            this.mIsDragging = false;
        } else if (action == 1) {
            if (this.mIsDragging && this.mVelocityTracker != null) {
                this.mLastTouchX = getActiveX(motionEvent);
                this.mLastTouchY = getActiveY(motionEvent);
                this.mVelocityTracker.addMovement(motionEvent);
                this.mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = this.mVelocityTracker.getXVelocity();
                float yVelocity = this.mVelocityTracker.getYVelocity();
                if (Math.max(Math.abs(xVelocity), Math.abs(yVelocity)) >= this.mMinimumVelocity) {
                    this.mListener.onFling(this.mLastTouchX, this.mLastTouchY, -xVelocity, -yVelocity);
                }
            }
            VelocityTracker velocityTracker2 = this.mVelocityTracker;
            if (velocityTracker2 != null) {
                velocityTracker2.recycle();
                this.mVelocityTracker = null;
            }
        } else if (action == 2) {
            float activeX = getActiveX(motionEvent);
            float activeY = getActiveY(motionEvent);
            float deltaX = activeX - this.mLastTouchX;
            float deltaY = activeY - this.mLastTouchY;
            if (!this.mIsDragging) {
                this.mIsDragging = Math.sqrt((double) ((deltaX * deltaX) + (deltaY * deltaY))) >= ((double) this.mTouchSlop);
            }
            if (this.mIsDragging) {
                this.mListener.onDrag(deltaX, deltaY);
                this.mLastTouchX = activeX;
                this.mLastTouchY = activeY;
                VelocityTracker velocityTracker3 = this.mVelocityTracker;
                if (velocityTracker3 != null) {
                    velocityTracker3.addMovement(motionEvent);
                }
            }
        } else if (action == 3 && (velocityTracker = this.mVelocityTracker) != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
        return true;
    }

    @Override // com.github.chrisbanes.photoview.gestures.GestureDetector
    public void setOnGestureListener(OnGestureListener onGestureListener) {
        this.mListener = onGestureListener;
    }

    @Override // com.github.chrisbanes.photoview.gestures.GestureDetector
    public boolean isDragging() {
        return this.mIsDragging;
    }

    @Override // com.github.chrisbanes.photoview.gestures.GestureDetector
    public boolean isScaling() {
        return false;
    }

    float getActiveX(MotionEvent motionEvent) {
        throw null;
    }

    float getActiveY(MotionEvent motionEvent) {
        throw null;
    }
}
