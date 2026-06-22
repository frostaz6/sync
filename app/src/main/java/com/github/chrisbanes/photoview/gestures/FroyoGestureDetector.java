package com.github.chrisbanes.photoview.gestures;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/* JADX INFO: renamed from: re.c */
/* JADX INFO: compiled from: FroyoGestureDetector.java */
/* JADX INFO: loaded from: classes3.dex */
@TargetApi(8)
public class FroyoGestureDetector extends EclairGestureDetector {
    protected final ScaleGestureDetector mDetector;

    /* JADX INFO: renamed from: re.c$a */
    /* JADX INFO: compiled from: FroyoGestureDetector.java */
    class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
        ScaleListener() {
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor) || scaleFactor < 0.0f) {
                return false;
            }
            FroyoGestureDetector.this.mListener.onScale(scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
            return true;
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        }
    }

    public FroyoGestureDetector(Context context) {
        super(context);
        this.mDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override // com.github.chrisbanes.photoview.gestures.EclairGestureDetector, com.github.chrisbanes.photoview.gestures.CupcakeGestureDetector, com.github.chrisbanes.photoview.gestures.GestureDetector
    public boolean onTouchEvent(MotionEvent motionEvent) {
        try {
            this.mDetector.onTouchEvent(motionEvent);
            return super.onTouchEvent(motionEvent);
        } catch (IllegalArgumentException unused) {
            return true;
        }
    }

    @Override // com.github.chrisbanes.photoview.gestures.GestureDetector
    public boolean isScaling() {
        return this.mDetector.isInProgress();
    }
}
