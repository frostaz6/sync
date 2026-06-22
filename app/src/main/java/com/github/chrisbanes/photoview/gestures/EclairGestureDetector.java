package com.github.chrisbanes.photoview.gestures;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.MotionEvent;
import com.resync.app.util.Compat;

/* JADX INFO: renamed from: re.b */
/* JADX INFO: compiled from: EclairGestureDetector.java */
/* JADX INFO: loaded from: classes3.dex */
@TargetApi(5)
public class EclairGestureDetector extends CupcakeGestureDetector {
    private int mActivePointerId;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private int mActivePointerIndex;

    public EclairGestureDetector(Context context) {
        super(context);
        this.mActivePointerId = -1;
        this.mActivePointerIndex = 0;
    }

    @Override // com.github.chrisbanes.photoview.gestures.CupcakeGestureDetector, com.github.chrisbanes.photoview.gestures.GestureDetector
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (action == 0) {
            this.mActivePointerId = motionEvent.getPointerId(0);
        } else if (action == 1 || action == 3) {
            this.mActivePointerId = -1;
        } else if (action == 6) {
            int actionPointerIndex = Compat.getGreenComponent(motionEvent.getAction());
            if (motionEvent.getPointerId(actionPointerIndex) == this.mActivePointerId) {
                int newPointerIndex = actionPointerIndex == 0 ? 1 : 0;
                this.mActivePointerId = motionEvent.getPointerId(newPointerIndex);
                this.mLastTouchX = motionEvent.getX(newPointerIndex);
                this.mLastTouchY = motionEvent.getY(newPointerIndex);
            }
        }
        int activePointerId = this.mActivePointerId;
        this.mActivePointerIndex = motionEvent.findPointerIndex(activePointerId != -1 ? activePointerId : 0);
        try {
            return super.onTouchEvent(motionEvent);
        } catch (IllegalArgumentException unused) {
            return true;
        }
    }

    @Override // com.github.chrisbanes.photoview.gestures.CupcakeGestureDetector
    float getActiveX(MotionEvent motionEvent) {
        try {
            return motionEvent.getX(this.mActivePointerIndex);
        } catch (Exception unused) {
            return motionEvent.getX();
        }
    }

    @Override // com.github.chrisbanes.photoview.gestures.CupcakeGestureDetector
    float getActiveY(MotionEvent motionEvent) {
        try {
            return motionEvent.getY(this.mActivePointerIndex);
        } catch (Exception unused) {
            return motionEvent.getY();
        }
    }
}
