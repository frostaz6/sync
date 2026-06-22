package com.resync.app.ui.views.photoview;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.core.view.MotionEventCompat;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.chrisbanes.photoview.gestures.OnGestureListener;
import com.github.chrisbanes.photoview.gestures.VersionedGestureDetector;
import java.lang.ref.WeakReference;
import com.resync.app.ui.views.video.CustomTextureView;
import com.resync.app.util.Logger;

/* JADX INFO: compiled from: CustomExoPlayerViewAttacher.java */
/* JADX INFO: loaded from: classes3.dex */
public class CustomExoPlayerViewAttacher implements View.OnTouchListener, OnGestureListener, ViewTreeObserver.OnGlobalLayoutListener {
    static int SINGLE_TOUCH = 1;
    private OnMatrixChangedListener matrixChangeListener;
    private OnPhotoTapListener photoTapListener;
    private OnViewTapListener viewTapListener;
    private View.OnLongClickListener longClickListener;
    private OnScaleChangeListener scaleChangeListener;
    private OnSingleFlingListener singleFlingListener;
    private int viewTop;
    private int viewRight;
    private int viewBottom;
    private int viewLeft;
    private float baseRotation;
    private boolean zoomEnabled;

    private WeakReference<CustomTextureView> viewRef;
    private GestureDetector gestureDetector;
    private com.github.chrisbanes.photoview.gestures.GestureDetector scaleDragDetector;

    private Interpolator interpolator = new AccelerateDecelerateInterpolator();
    int zoomDuration = 220;
    private float minScale = 1.0f;

    private float midScale = 1.75f;

    private float maxScale = 5.0f;

    private boolean allowParentInterceptOnEdge = true;
    private boolean blockParentIntercept = false;
    private final Matrix baseMatrix = new Matrix();

    private final Matrix drawMatrix = new Matrix();
    private final Matrix suppMatrix = new Matrix();
    private final RectF displayRect = new RectF();
    private final float[] matrixValues = new float[9];
    private int scrollEdge = 2;

    /* JADX INFO: compiled from: CustomExoPlayerViewAttacher.java */
    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        GestureListener() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float velocityX, float velocityY) {
            if (CustomExoPlayerViewAttacher.this.singleFlingListener == null || CustomExoPlayerViewAttacher.this.getScale() > 1.0f || MotionEventCompat.getPointerCount(motionEvent) > SINGLE_TOUCH || MotionEventCompat.getPointerCount(motionEvent2) > SINGLE_TOUCH) {
                return false;
            }
            return CustomExoPlayerViewAttacher.this.singleFlingListener.onFling(motionEvent, motionEvent2, velocityX, velocityY);
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            if (CustomExoPlayerViewAttacher.this.longClickListener != null) {
                CustomExoPlayerViewAttacher.this.longClickListener.onLongClick(CustomExoPlayerViewAttacher.this.getImageView());
            }
        }
    }

    /* JADX INFO: compiled from: CustomExoPlayerViewAttacher.java */
    private class AnimatedZoomRunnable implements Runnable {

        private final float focalX;
        private final float focalY;
        private final long startTime = System.currentTimeMillis();

        private final float zoomStart;

        private final float zoomEnd;

        public AnimatedZoomRunnable(float zoomStart, float zoomEnd, float focalX, float focalY) {
            this.focalX = focalX;
            this.focalY = focalY;
            this.zoomStart = zoomStart;
            this.zoomEnd = zoomEnd;
        }

        private float interpolate() {
            return CustomExoPlayerViewAttacher.this.interpolator.getInterpolation(Math.min(1.0f, ((System.currentTimeMillis() - this.startTime) * 1.0f) / CustomExoPlayerViewAttacher.this.zoomDuration));
        }

        @Override // java.lang.Runnable
        public void run() {
            CustomTextureView imageView = CustomExoPlayerViewAttacher.this.getImageView();
            if (imageView == null) {
                return;
            }
            float fraction = interpolate();
            float zoomStart = this.zoomStart;
            CustomExoPlayerViewAttacher.this.onScale((zoomStart + ((this.zoomEnd - zoomStart) * fraction)) / CustomExoPlayerViewAttacher.this.getScale(), this.focalX, this.focalY);
            if (fraction < 1.0f) {
                imageView.post(this);
                return;
            }
            CustomExoPlayerViewAttacher attacher = CustomExoPlayerViewAttacher.this;
            attacher.setImageViewMatrix(attacher.getDrawMatrix());
            CustomExoPlayerViewAttacher.this.checkAndDisplayMatrix();
        }
    }

    /* JADX INFO: compiled from: CustomExoPlayerViewAttacher.java */
    public interface OnMatrixChangedListener {
        void onMatrixChanged(RectF rectF);
    }

    /* JADX INFO: compiled from: CustomExoPlayerViewAttacher.java */
    public interface OnPhotoTapListener {
        void onPhotoTap(View view, float x, float y);

        void onOutsidePhotoTap();
    }

    /* JADX INFO: compiled from: CustomExoPlayerViewAttacher.java */
    public interface OnScaleChangeListener {
        void onScaleChange(float scaleFactor, float focusX, float focusY);
    }

    /* JADX INFO: compiled from: CustomExoPlayerViewAttacher.java */
    public interface OnSingleFlingListener {
        boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float velocityX, float velocityY);
    }

    /* JADX INFO: compiled from: CustomExoPlayerViewAttacher.java */
    public interface OnViewTapListener {
        void onViewTap(View view, float x, float y);
    }

    public CustomExoPlayerViewAttacher(CustomTextureView customTextureView, boolean zoomable) {
        this.viewRef = new WeakReference<>(customTextureView);
        customTextureView.setDrawingCacheEnabled(true);
        customTextureView.setOnTouchListener(this);
        ViewTreeObserver viewTreeObserver = customTextureView.getViewTreeObserver();
        if (viewTreeObserver != null) {
            viewTreeObserver.addOnGlobalLayoutListener(this);
        }
        if (customTextureView.isInEditMode()) {
            return;
        }
        this.scaleDragDetector = VersionedGestureDetector.newInstance(customTextureView.getContext(), this);
        GestureDetector gestureDetector = new GestureDetector(customTextureView.getContext(), new GestureListener());
        this.gestureDetector = gestureDetector;
        gestureDetector.setOnDoubleTapListener(new com.resync.app.ui.views.photoview.CustomExoPlayerViewDefaultOnDoubleTapListener(this));
        this.baseRotation = 0.0f;
        setZoomable(zoomable);
    }

    private void resetMatrix() {
        this.suppMatrix.reset();
        setImageViewMatrix(getDrawMatrix());
        checkMatrixBounds();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setImageViewMatrix(Matrix matrix) {
        RectF rectFM;
        CustomTextureView customTextureViewO = getImageView();
        if (customTextureViewO != null) {
            customTextureViewO.applyMatrix(matrix);
            if (this.matrixChangeListener == null || (rectFM = getDisplayRect(matrix)) == null) {
                return;
            }
            this.matrixChangeListener.onMatrixChanged(rectFM);
        }
    }

    private void update() {
        if (getImageView() != null) {
            if (this.zoomEnabled) {
                updateBaseMatrix();
            } else {
                resetMatrix();
            }
        }
    }

    private void updateBaseMatrix() {
        CustomTextureView customTextureViewO = getImageView();
        if (customTextureViewO == null) {
            return;
        }
        float viewWidth = getImageViewWidth(customTextureViewO);
        float viewHeight = getImageViewHeight(customTextureViewO);
        Logger.verbose("Height: " + viewHeight);
        float contentWidth = customTextureViewO.contentWidth;
        float contentHeight = customTextureViewO.contentHeight;
        this.baseMatrix.reset();
        RectF rectF = new RectF(0.0f, 0.0f, contentWidth, contentHeight);
        RectF rectF2 = new RectF(0.0f, 0.0f, viewWidth, viewHeight);
        if (((int) this.baseRotation) % SubsamplingScaleImageView.ORIENTATION_180 != 0) {
            rectF = new RectF(0.0f, 0.0f, contentHeight, contentWidth);
        }
        this.baseMatrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.CENTER);
        resetMatrix();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkAndDisplayMatrix() {
        if (checkMatrixBounds()) {
            setImageViewMatrix(getDrawMatrix());
        }
    }

    private boolean checkMatrixBounds() {
        CustomTextureView view = getImageView();
        if (view == null) {
            return false;
        }
        RectF rect = getDisplayRect(view.applyMatrix(this.drawMatrix));
        if (rect == null) {
            return false;
        }
        float height = rect.height();
        float width = rect.width();
        float viewHeight = getImageViewHeight(view);
        float deltaY;
        if (height <= viewHeight) {
            deltaY = (viewHeight - height) / 2.0f - rect.top;
        } else {
            float top = rect.top;
            if (top > 0.0f) {
                deltaY = -top;
            } else {
                float bottom = rect.bottom;
                deltaY = bottom < viewHeight ? viewHeight - bottom : 0.0f;
            }
        }
        float viewWidth = getImageViewWidth(view);
        float deltaX;
        if (width <= viewWidth) {
            deltaX = (viewWidth - width) / 2.0f - rect.left;
            this.scrollEdge = 2;
        } else {
            float left = rect.left;
            if (left > 0.0f) {
                this.scrollEdge = 0;
                deltaX = -left;
            } else {
                float right = rect.right;
                if (right < viewWidth) {
                    deltaX = viewWidth - right;
                    this.scrollEdge = 1;
                } else {
                    this.scrollEdge = -1;
                    deltaX = 0.0f;
                }
            }
        }
        this.suppMatrix.postTranslate(deltaX, deltaY);
        return true;
    }

    private RectF getDisplayRect(Matrix matrix) {
        CustomTextureView imageView = getImageView();
        if (imageView == null) {
            return null;
        }
        this.displayRect.set(0.0f, 0.0f, imageView.getWidth(), imageView.getHeight());
        matrix.mapRect(this.displayRect);
        return this.displayRect;
    }

    private int getImageViewHeight(CustomTextureView customTextureView) {
        if (customTextureView == null) {
            return 0;
        }
        return (customTextureView.getHeight() - customTextureView.getPaddingTop()) - customTextureView.getPaddingBottom();
    }

    private int getImageViewWidth(CustomTextureView customTextureView) {
        if (customTextureView == null) {
            return 0;
        }
        return (customTextureView.getWidth() - customTextureView.getPaddingLeft()) - customTextureView.getPaddingRight();
    }

    private float getValue(Matrix matrix, int index) {
        matrix.getValues(this.matrixValues);
        return this.matrixValues[index];
    }

    private static boolean hasDrawable(CustomTextureView customTextureView) {
        return customTextureView != null;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.longClickListener = onLongClickListener;
    }

    public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        this.photoTapListener = listener;
    }

    public void setScale(float scale, float focusX, float focusY, boolean animate) {
        if (getImageView() != null) {
            float minScale = this.minScale;
            if (scale < minScale || scale > this.maxScale) {
                return;
            }
            this.suppMatrix.setScale(Math.max(minScale, scale), Math.max(this.minScale, scale), focusX, focusY);
            setImageViewMatrix(getDrawMatrix());
            checkAndDisplayMatrix();
        }
    }

    public void setZoomable(boolean zoomable) {
        this.zoomEnabled = zoomable;
        update();
    }

    @Override // com.github.chrisbanes.photoview.gestures.OnGestureListener
    public void onScale(float scaleFactor, float focusX, float focusY) {
        if (getScale() < this.maxScale || scaleFactor < 1.0f) {
            if (getScale() > this.minScale || scaleFactor > 1.0f) {
                OnScaleChangeListener listener = this.scaleChangeListener;
                if (listener != null) {
                    listener.onScaleChange(scaleFactor, focusX, focusY);
                }
                this.suppMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY);
                checkAndDisplayMatrix();
            }
        }
    }

    @Override // com.github.chrisbanes.photoview.gestures.OnGestureListener
    public void onFling(float startX, float startY, float velocityX, float velocityY) {
        checkMatrixBounds();
    }

    @Override // com.github.chrisbanes.photoview.gestures.OnGestureListener
    public void onDrag(float dx, float dy) {
        if (this.scaleDragDetector.isScaling()) {
            return;
        }
        CustomTextureView imageView = getImageView();
        if (getScale() > 1.0f) {
            this.suppMatrix.postTranslate(dx, dy);
            checkAndDisplayMatrix();
            checkAndDisplayMatrix();
        }
        ViewParent parent = imageView.getParent();
        if (!this.allowParentInterceptOnEdge || this.scaleDragDetector.isScaling() || this.blockParentIntercept) {
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
                return;
            }
            return;
        }
        int edge = this.scrollEdge;
        if ((edge == 2 || ((edge == 1 && dy >= 1.0f) || ((edge == 2 && dy <= -1.0f) || ((edge == 0 && dx >= 1.0f) || (edge == 1 && dx <= -1.0f))))) && parent != null) {
            parent.requestDisallowInterceptTouchEvent(false);
        }
    }

    public void cleanup() {
        WeakReference<CustomTextureView> weakReference = this.viewRef;
        if (weakReference == null) {
            return;
        }
        CustomTextureView customTextureView = weakReference.get();
        if (customTextureView != null) {
            ViewTreeObserver viewTreeObserver = customTextureView.getViewTreeObserver();
            if (viewTreeObserver != null && viewTreeObserver.isAlive()) {
                viewTreeObserver.removeGlobalOnLayoutListener(this);
            }
            customTextureView.setOnTouchListener(null);
        }
        GestureDetector gestureDetector = this.gestureDetector;
        if (gestureDetector != null) {
            gestureDetector.setOnDoubleTapListener(null);
        }
        this.matrixChangeListener = null;
        this.photoTapListener = null;
        this.viewTapListener = null;
        this.viewRef = null;
    }

    public RectF getDisplayRect() {
        checkMatrixBounds();
        return getDisplayRect(getDrawMatrix());
    }

    @Deprecated
    public Matrix getDrawMatrix() {
        this.drawMatrix.set(this.baseMatrix);
        this.drawMatrix.postConcat(this.suppMatrix);
        return this.drawMatrix;
    }

    public CustomTextureView getImageView() {
        WeakReference<CustomTextureView> weakReference = this.viewRef;
        CustomTextureView customTextureView = weakReference != null ? weakReference.get() : null;
        if (customTextureView == null) {
            cleanup();
        }
        return customTextureView;
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
        CustomTextureView customTextureViewO = getImageView();
        if (customTextureViewO != null) {
            if (!this.zoomEnabled) {
                updateBaseMatrix();
                return;
            }
            int top = customTextureViewO.getTop();
            int right = customTextureViewO.getRight();
            int bottom = customTextureViewO.getBottom();
            int left = customTextureViewO.getLeft();
            if (top == this.viewTop && bottom == this.viewBottom && left == this.viewLeft && right == this.viewRight) {
                return;
            }
            updateBaseMatrix();
            this.viewTop = top;
            this.viewRight = right;
            this.viewBottom = bottom;
            this.viewLeft = left;
        }
    }

    @Override // android.view.View.OnTouchListener
    @android.annotation.SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean handled = false;
        if (this.zoomEnabled && hasDrawable((CustomTextureView) view)) {
            ViewParent parent = view.getParent();
            int action = motionEvent.getAction();
            if (action != 0) {
                if (action == 1 || action == 3) {
                    if (getScale() < this.minScale) {
                        RectF rect = getDisplayRect();
                        if (rect != null) {
                            view.post(new AnimatedZoomRunnable(getScale(), this.minScale, rect.centerX(), rect.centerY()));
                            handled = true;
                        }
                    } else {
                        checkAndDisplayMatrix();
                    }
                }
            } else if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
            com.github.chrisbanes.photoview.gestures.GestureDetector gestureDetector = this.scaleDragDetector;
            if (gestureDetector != null) {
                boolean wasScaling = gestureDetector.isScaling();
                boolean wasDragging = this.scaleDragDetector.isDragging();
                boolean detectorResult = this.scaleDragDetector.onTouchEvent(motionEvent);
                boolean scalingChanged = !wasScaling && !this.scaleDragDetector.isScaling();
                boolean draggingChanged = !wasDragging && !this.scaleDragDetector.isDragging();
                this.blockParentIntercept = scalingChanged && draggingChanged;
                handled = detectorResult;
            }
            GestureDetector gestureDetector2 = this.gestureDetector;
            if (gestureDetector2 != null && gestureDetector2.onTouchEvent(motionEvent)) {
                handled = true;
            }
        }
        return handled;
    }

    public float getMaximumScale() {
        return this.maxScale;
    }

    public float getMediumScale() {
        return this.midScale;
    }

    public float getMinimumScale() {
        return this.minScale;
    }

    @Deprecated
    public OnPhotoTapListener getOnPhotoTapListener() {
        return this.photoTapListener;
    }

    @Deprecated
    public OnViewTapListener getOnViewTapListener() {
        return this.viewTapListener;
    }

    public float getScale() {
        return (float) Math.sqrt(((float) Math.pow(getValue(this.suppMatrix, 0), 2.0d)) + ((float) Math.pow(getValue(this.suppMatrix, 3), 2.0d)));
    }

    public void refresh() {
        update();
        checkAndDisplayMatrix();
        checkAndDisplayMatrix();
    }
}
