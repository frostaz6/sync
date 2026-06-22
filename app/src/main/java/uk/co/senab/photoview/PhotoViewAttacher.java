package uk.co.senab.photoview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import androidx.core.view.MotionEventCompat;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.chrisbanes.photoview.gestures.OnGestureListener;
import com.github.chrisbanes.photoview.gestures.VersionedGestureDetector;
import com.github.chrisbanes.photoview.scrollers.ScrollerProxy;
import java.lang.ref.WeakReference;
import com.resync.app.util.Compat;

/* JADX INFO: compiled from: PhotoViewAttacher.java */
/* JADX INFO: loaded from: classes3.dex */
public class PhotoViewAttacher implements IPhotoView, View.OnTouchListener, OnGestureListener, ViewTreeObserver.OnGlobalLayoutListener {
    static int SINGLE_TOUCH = 1;
    private final RectF mDisplayRect;
    private final float[] mMatrixValues;
    private OnMatrixChangedListener mMatrixChangeListener;
    private OnPhotoTapListener mPhotoTapListener;
    private OnViewTapListener mViewTapListener;
    private View.OnLongClickListener mLongClickListener;
    private OnScaleChangeListener mScaleChangeListener;
    private OnSingleFlingListener mSingleFlingListener;
    private int mIvTop;
    private int mIvRight;
    private int mIvBottom;
    private int mIvLeft;
    private FlingRunnable mCurrentFlingRunnable;
    private int mScrollEdge;
    private float mBaseRotation;
    private boolean mZoomEnabled;
    private ImageView.ScaleType mScaleType;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Interpolator mInterpolator;
    int mZoomDuration;
    private float mMinScale;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private float mMidScale;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private float mMaxScale;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private boolean mAllowParentInterceptOnEdge;
    private boolean mBlockParentIntercept;

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    private WeakReference<ImageView> mImageView;
    private GestureDetector mGestureDetector;
    private com.github.chrisbanes.photoview.gestures.GestureDetector mScaleDragDetector;
    private final Matrix mBaseMatrix;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    private final Matrix mDrawMatrix;
    private final Matrix mSuppMatrix;

    /* JADX INFO: compiled from: PhotoViewAttacher.java */
    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        GestureListener() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float velocityX, float velocityY) {
            if (PhotoViewAttacher.this.mSingleFlingListener == null || PhotoViewAttacher.this.getScale() > 1.0f || MotionEventCompat.getPointerCount(motionEvent) > SINGLE_TOUCH || MotionEventCompat.getPointerCount(motionEvent2) > SINGLE_TOUCH) {
                return false;
            }
            return PhotoViewAttacher.this.mSingleFlingListener.onFling(motionEvent, motionEvent2, velocityX, velocityY);
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            if (PhotoViewAttacher.this.mLongClickListener != null) {
                PhotoViewAttacher.this.mLongClickListener.onLongClick(PhotoViewAttacher.this.getImageView());
            }
        }
    }

    /* JADX INFO: compiled from: PhotoViewAttacher.java */
    static /* synthetic */ class EnumSwitchMapping {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] SCALE_TYPES;

        static {
            int[] iArr = new int[ImageView.ScaleType.values().length];
            SCALE_TYPES = iArr;
            try {
                iArr[ImageView.ScaleType.MATRIX.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                SCALE_TYPES[ImageView.ScaleType.FIT_START.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                SCALE_TYPES[ImageView.ScaleType.FIT_END.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                SCALE_TYPES[ImageView.ScaleType.FIT_CENTER.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                SCALE_TYPES[ImageView.ScaleType.FIT_XY.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    /* JADX INFO: renamed from: uk.co.senab.photoview.c$c, reason: collision with other inner class name */
    /* JADX INFO: compiled from: PhotoViewAttacher.java */
    private class AnimatedZoomRunnable implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final float mFocalX;
        private final float mFocalY;
        private final long mStartTime = System.currentTimeMillis();

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        private final float mZoomStart;

        /* JADX INFO: renamed from: r, reason: collision with root package name */
        private final float mZoomEnd;

        public AnimatedZoomRunnable(float zoomStart, float zoomEnd, float focalX, float focalY) {
            this.mFocalX = focalX;
            this.mFocalY = focalY;
            this.mZoomStart = zoomStart;
            this.mZoomEnd = zoomEnd;
        }

        private float interpolate() {
            return PhotoViewAttacher.this.mInterpolator.getInterpolation(Math.min(1.0f, ((System.currentTimeMillis() - this.mStartTime) * 1.0f) / PhotoViewAttacher.this.mZoomDuration));
        }

        @Override // java.lang.Runnable
        public void run() {
            ImageView imageView = PhotoViewAttacher.this.getImageView();
            if (imageView == null) {
                return;
            }
            float interpolation = interpolate();
            float zoomStart = this.mZoomStart;
            PhotoViewAttacher.this.onScale((zoomStart + ((this.mZoomEnd - zoomStart) * interpolation)) / PhotoViewAttacher.this.getScale(), this.mFocalX, this.mFocalY);
            if (interpolation < 1.0f) {
                Compat.postOnAnimation(imageView, this);
            }
        }
    }

    /* JADX INFO: compiled from: PhotoViewAttacher.java */
    private class FlingRunnable implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final ScrollerProxy mScroller;
        private int mCurrentX;
        private int mCurrentY;

        public FlingRunnable(Context context) {
            this.mScroller = ScrollerProxy.getScroller(context);
        }

        public void cancelFling() {
            this.mScroller.forceFinished(true);
        }

        public void fling(int viewWidth, int viewHeight, int velocityX, int velocityY) {
            int minX;
            int maxX;
            int minY;
            int maxY;
            RectF displayRect = PhotoViewAttacher.this.getDisplayRect();
            if (displayRect == null) {
                return;
            }
            int startX = Math.round(-displayRect.left);
            float widthF = viewWidth;
            if (widthF < displayRect.width()) {
                maxX = Math.round(displayRect.width() - widthF);
                minX = 0;
            } else {
                minX = startX;
                maxX = minX;
            }
            int startY = Math.round(-displayRect.top);
            float heightF = viewHeight;
            if (heightF < displayRect.height()) {
                maxY = Math.round(displayRect.height() - heightF);
                minY = 0;
            } else {
                minY = startY;
                maxY = minY;
            }
            this.mCurrentX = startX;
            this.mCurrentY = startY;
            if (startX == maxX && startY == maxY) {
                return;
            }
            this.mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, 0, 0);
        }

        @Override // java.lang.Runnable
        public void run() {
            ImageView imageView;
            if (this.mScroller.isFinished() || (imageView = PhotoViewAttacher.this.getImageView()) == null || !this.mScroller.computeScrollOffset()) {
                return;
            }
            int newX = this.mScroller.getCurrX();
            int newY = this.mScroller.getCurrY();
            PhotoViewAttacher.this.mSuppMatrix.postTranslate(this.mCurrentX - newX, this.mCurrentY - newY);
            PhotoViewAttacher attacher = PhotoViewAttacher.this;
            attacher.setImageViewMatrix(attacher.getDrawMatrix());
            this.mCurrentX = newX;
            this.mCurrentY = newY;
            Compat.postOnAnimation(imageView, this);
        }
    }

    /* JADX INFO: compiled from: PhotoViewAttacher.java */
    public interface OnMatrixChangedListener {
        void onMatrixChanged(RectF rectF);
    }

    /* JADX INFO: compiled from: PhotoViewAttacher.java */
    public interface OnPhotoTapListener {
        void onPhotoTap(View view, float x, float y);

        void onOutsidePhotoTap();
    }

    /* JADX INFO: compiled from: PhotoViewAttacher.java */
    public interface OnScaleChangeListener {
        void onScaleChange(float scaleFactor, float focusX, float focusY);
    }

    /* JADX INFO: compiled from: PhotoViewAttacher.java */
    public interface OnSingleFlingListener {
        boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float velocityX, float velocityY);
    }

    /* JADX INFO: compiled from: PhotoViewAttacher.java */
    public interface OnViewTapListener {
        void onViewTap(View view, float x, float y);
    }

    public PhotoViewAttacher(ImageView imageView) {
        this(imageView, true);
    }

    private float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(this.mMatrixValues);
        return this.mMatrixValues[whichValue];
    }

    private static boolean hasDrawable(ImageView imageView) {
        return (imageView == null || imageView.getDrawable() == null) ? false : true;
    }

    private static boolean isSupportedScaleType(ImageView.ScaleType scaleType) {
        if (scaleType == null) {
            return false;
        }
        if (EnumSwitchMapping.SCALE_TYPES[scaleType.ordinal()] != 1) {
            return true;
        }
        throw new IllegalArgumentException(scaleType.name() + " is not supported in PhotoView");
    }

    private void resetMatrix() {
        this.mSuppMatrix.reset();
        setRotationBy(this.mBaseRotation);
        setImageViewMatrix(getDrawMatrix());
        checkMatrixBounds();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setImageViewMatrix(Matrix matrix) {
        RectF displayRect;
        ImageView imageView = getImageView();
        if (imageView != null) {
            checkImageViewScaleType();
            imageView.setImageMatrix(matrix);
            if (this.mMatrixChangeListener == null || (displayRect = getDisplayRect(matrix)) == null) {
                return;
            }
            this.mMatrixChangeListener.onMatrixChanged(displayRect);
        }
    }

    private static void setImageViewScaleTypeMatrix(ImageView imageView) {
        if (imageView == null || (imageView instanceof IPhotoView) || ImageView.ScaleType.MATRIX.equals(imageView.getScaleType())) {
            return;
        }
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
    }

    private void updateBaseMatrix(Drawable drawable) {
        ImageView imageView = getImageView();
        if (imageView == null || drawable == null) {
            return;
        }
        float viewWidth = getImageViewWidth(imageView);
        float viewHeight = getImageViewHeight(imageView);
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        this.mBaseMatrix.reset();
        float drawableWidth = intrinsicWidth;
        float widthScale = viewWidth / drawableWidth;
        float drawableHeight = intrinsicHeight;
        float heightScale = viewHeight / drawableHeight;
        ImageView.ScaleType scaleType = this.mScaleType;
        if (scaleType == ImageView.ScaleType.CENTER) {
            this.mBaseMatrix.postTranslate((viewWidth - drawableWidth) / 2.0f, (viewHeight - drawableHeight) / 2.0f);
        } else if (scaleType == ImageView.ScaleType.CENTER_CROP) {
            float maxScale = Math.max(widthScale, heightScale);
            this.mBaseMatrix.postScale(maxScale, maxScale);
            this.mBaseMatrix.postTranslate((viewWidth - (drawableWidth * maxScale)) / 2.0f, (viewHeight - (drawableHeight * maxScale)) / 2.0f);
        } else if (scaleType == ImageView.ScaleType.CENTER_INSIDE) {
            float minScale = Math.min(1.0f, Math.min(widthScale, heightScale));
            this.mBaseMatrix.postScale(minScale, minScale);
            this.mBaseMatrix.postTranslate((viewWidth - (drawableWidth * minScale)) / 2.0f, (viewHeight - (drawableHeight * minScale)) / 2.0f);
        } else {
            RectF rectF = new RectF(0.0f, 0.0f, drawableWidth, drawableHeight);
            RectF rectF2 = new RectF(0.0f, 0.0f, viewWidth, viewHeight);
            if (((int) this.mBaseRotation) % SubsamplingScaleImageView.ORIENTATION_180 != 0) {
                rectF = new RectF(0.0f, 0.0f, drawableHeight, drawableWidth);
            }
            int scaleTypeCase = EnumSwitchMapping.SCALE_TYPES[this.mScaleType.ordinal()];
            if (scaleTypeCase == 2) {
                this.mBaseMatrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.START);
            } else if (scaleTypeCase == 3) {
                this.mBaseMatrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.END);
            } else if (scaleTypeCase == 4) {
                this.mBaseMatrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.CENTER);
            } else if (scaleTypeCase == 5) {
                this.mBaseMatrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.FILL);
            }
        }
        resetMatrix();
    }

    private void cancelFling() {
        FlingRunnable flingRunnable = this.mCurrentFlingRunnable;
        if (flingRunnable != null) {
            flingRunnable.cancelFling();
            this.mCurrentFlingRunnable = null;
        }
    }

    private void checkAndDisplayMatrix() {
        if (checkMatrixBounds()) {
            setImageViewMatrix(getDrawMatrix());
        }
    }

    private void checkImageViewScaleType() {
        ImageView imageView = getImageView();
        if (imageView != null && !(imageView instanceof IPhotoView) && !ImageView.ScaleType.MATRIX.equals(imageView.getScaleType())) {
            throw new IllegalStateException("The ImageView's ScaleType has been changed since attaching a PhotoViewAttacher. You should call setScaleType on the PhotoViewAttacher instead of on the ImageView");
        }
    }

    private boolean checkMatrixBounds() {
        RectF rect;
        float rectTop;
        float vEdge;
        float deltaY;
        float hDelta;
        float centeredX;
        float rectLeft;
        ImageView imageView = getImageView();
        if (imageView == null || (rect = getDisplayRect(getDrawMatrix())) == null) {
            return false;
        }
        float fHeight = rect.height();
        float fWidth = rect.width();
        float viewHeight = getImageViewHeight(imageView);
        float deltaX = 0.0f;
        if (fHeight <= viewHeight) {
            int scaleTypeCase = EnumSwitchMapping.SCALE_TYPES[this.mScaleType.ordinal()];
            if (scaleTypeCase != 2) {
                if (scaleTypeCase != 3) {
                    viewHeight = (viewHeight - fHeight) / 2.0f;
                    vEdge = rect.top;
                } else {
                    viewHeight -= fHeight;
                    vEdge = rect.top;
                }
                deltaY = viewHeight - vEdge;
            } else {
                rectTop = rect.top;
                deltaY = -rectTop;
            }
        } else {
            rectTop = rect.top;
            if (rectTop > 0.0f) {
                deltaY = -rectTop;
            } else {
                vEdge = rect.bottom;
                deltaY = vEdge < viewHeight ? viewHeight - vEdge : 0.0f;
            }
        }
        float viewWidth = getImageViewWidth(imageView);
        if (fWidth <= viewWidth) {
            int scaleTypeCaseH = EnumSwitchMapping.SCALE_TYPES[this.mScaleType.ordinal()];
            if (scaleTypeCaseH != 2) {
                if (scaleTypeCaseH != 3) {
                    centeredX = (viewWidth - fWidth) / 2.0f;
                    rectLeft = rect.left;
                } else {
                    centeredX = viewWidth - fWidth;
                    rectLeft = rect.left;
                }
                hDelta = centeredX - rectLeft;
            } else {
                hDelta = -rect.left;
            }
            deltaX = hDelta;
            this.mScrollEdge = 2;
        } else {
            float rectLeftPos = rect.left;
            if (rectLeftPos > 0.0f) {
                this.mScrollEdge = 0;
                deltaX = -rectLeftPos;
            } else {
                float rectRight = rect.right;
                if (rectRight < viewWidth) {
                    deltaX = viewWidth - rectRight;
                    this.mScrollEdge = 1;
                } else {
                    this.mScrollEdge = -1;
                }
            }
        }
        this.mSuppMatrix.postTranslate(deltaX, deltaY);
        return true;
    }

    private RectF getDisplayRect(Matrix matrix) {
        Drawable drawable;
        ImageView imageView = getImageView();
        if (imageView == null || (drawable = imageView.getDrawable()) == null) {
            return null;
        }
        this.mDisplayRect.set(0.0f, 0.0f, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        matrix.mapRect(this.mDisplayRect);
        return this.mDisplayRect;
    }

    private int getImageViewHeight(ImageView imageView) {
        if (imageView == null) {
            return 0;
        }
        return (imageView.getHeight() - imageView.getPaddingTop()) - imageView.getPaddingBottom();
    }

    private int getImageViewWidth(ImageView imageView) {
        if (imageView == null) {
            return 0;
        }
        return (imageView.getWidth() - imageView.getPaddingLeft()) - imageView.getPaddingRight();
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.mLongClickListener = onLongClickListener;
    }

    public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        this.mPhotoTapListener = listener;
    }

    public void setRotationBy(float rotationDegrees) {
        this.mSuppMatrix.postRotate(rotationDegrees % 360.0f);
        checkAndDisplayMatrix();
    }

    public void setScale(float scale, float focusX, float focusY, boolean animate) {
        if (getImageView() == null || scale < this.mMinScale || scale > this.mMaxScale) {
            return;
        }
        this.mSuppMatrix.setScale(scale, scale, focusX, focusY);
        checkAndDisplayMatrix();
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        if (!isSupportedScaleType(scaleType) || scaleType == this.mScaleType) {
            return;
        }
        this.mScaleType = scaleType;
        update();
    }

    public void setZoomable(boolean zoomable) {
        this.mZoomEnabled = zoomable;
        update();
    }

    public void update() {
        ImageView imageView = getImageView();
        if (imageView != null) {
            if (!this.mZoomEnabled) {
                resetMatrix();
            } else {
                setImageViewScaleTypeMatrix(imageView);
                updateBaseMatrix(imageView.getDrawable());
            }
        }
    }

    @Override // com.github.chrisbanes.photoview.gestures.OnGestureListener
    public void onScale(float scaleFactor, float focusX, float focusY) {
        if (getScale() < this.mMaxScale || scaleFactor < 1.0f) {
            if (getScale() > this.mMinScale || scaleFactor > 1.0f) {
                OnScaleChangeListener listener = this.mScaleChangeListener;
                if (listener != null) {
                    listener.onScaleChange(scaleFactor, focusX, focusY);
                }
                this.mSuppMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY);
                checkAndDisplayMatrix();
            }
        }
    }

    @Override // com.github.chrisbanes.photoview.gestures.OnGestureListener
    public void onFling(float startX, float startY, float velocityX, float velocityY) {
        ImageView imageView = getImageView();
        FlingRunnable flingRunnable = new FlingRunnable(imageView.getContext());
        this.mCurrentFlingRunnable = flingRunnable;
        flingRunnable.fling(getImageViewWidth(imageView), getImageViewHeight(imageView), (int) velocityX, (int) velocityY);
        imageView.post(this.mCurrentFlingRunnable);
    }

    @Override // com.github.chrisbanes.photoview.gestures.OnGestureListener
    public void onDrag(float dx, float dy) {
        if (this.mScaleDragDetector.isScaling()) {
            return;
        }
        ImageView imageView = getImageView();
        this.mSuppMatrix.postTranslate(dx, dy);
        checkAndDisplayMatrix();
        ViewParent parent = imageView.getParent();
        if (!this.mAllowParentInterceptOnEdge || this.mScaleDragDetector.isScaling() || this.mBlockParentIntercept) {
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
                return;
            }
            return;
        }
        int scrollEdge = this.mScrollEdge;
        if ((scrollEdge == 2 || ((scrollEdge == 0 && dx >= 1.0f) || (scrollEdge == 1 && dx <= -1.0f))) && parent != null) {
            parent.requestDisallowInterceptTouchEvent(false);
        }
    }

    public void cleanup() {
        WeakReference<ImageView> weakReference = this.mImageView;
        if (weakReference == null) {
            return;
        }
        ImageView imageView = weakReference.get();
        if (imageView != null) {
            ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
            if (viewTreeObserver != null && viewTreeObserver.isAlive()) {
                viewTreeObserver.removeGlobalOnLayoutListener(this);
            }
            imageView.setOnTouchListener(null);
            cancelFling();
        }
        GestureDetector gestureDetector = this.mGestureDetector;
        if (gestureDetector != null) {
            gestureDetector.setOnDoubleTapListener(null);
        }
        this.mMatrixChangeListener = null;
        this.mPhotoTapListener = null;
        this.mViewTapListener = null;
        this.mImageView = null;
    }

    public RectF getDisplayRect() {
        checkMatrixBounds();
        return getDisplayRect(getDrawMatrix());
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
        ImageView imageView = getImageView();
        if (imageView != null) {
            if (!this.mZoomEnabled) {
                updateBaseMatrix(imageView.getDrawable());
                return;
            }
            int top = imageView.getTop();
            int right = imageView.getRight();
            int bottom = imageView.getBottom();
            int left = imageView.getLeft();
            if (top == this.mIvTop && bottom == this.mIvBottom && left == this.mIvLeft && right == this.mIvRight) {
                return;
            }
            updateBaseMatrix(imageView.getDrawable());
            this.mIvTop = top;
            this.mIvRight = right;
            this.mIvBottom = bottom;
            this.mIvLeft = left;
        }
    }

    @Override // android.view.View.OnTouchListener
    @android.annotation.SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean handled = false;
        if (this.mZoomEnabled && hasDrawable((ImageView) view)) {
            ViewParent parent = view.getParent();
            int action = motionEvent.getAction();
            if (action == 0) {
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                cancelFling();
            } else if (action == 1 || action == 3) {
                if (getScale() < this.mMinScale) {
                    RectF displayRect = getDisplayRect();
                    if (displayRect != null) {
                        view.post(new AnimatedZoomRunnable(getScale(), this.mMinScale, displayRect.centerX(), displayRect.centerY()));
                        handled = true;
                    }
                }
            }
            com.github.chrisbanes.photoview.gestures.GestureDetector gestureDetector = this.mScaleDragDetector;
            if (gestureDetector != null) {
                boolean wasScaling = gestureDetector.isScaling();
                boolean wasDragging = this.mScaleDragDetector.isDragging();
                boolean touchHandled = this.mScaleDragDetector.onTouchEvent(motionEvent);
                boolean scalingEnded = (wasScaling || this.mScaleDragDetector.isScaling()) ? false : true;
                boolean draggingEnded = (wasDragging || this.mScaleDragDetector.isDragging()) ? false : true;
                if (scalingEnded && draggingEnded) {
                    handled = true;
                }
                this.mBlockParentIntercept = handled;
                handled = touchHandled;
            }
            GestureDetector gestureDetector2 = this.mGestureDetector;
            if (gestureDetector2 != null && gestureDetector2.onTouchEvent(motionEvent)) {
                handled = true;
            }
        }
        return handled;
    }

    @Deprecated
    public Matrix getDrawMatrix() {
        this.mDrawMatrix.set(this.mBaseMatrix);
        this.mDrawMatrix.postConcat(this.mSuppMatrix);
        return this.mDrawMatrix;
    }

    public ImageView getImageView() {
        WeakReference<ImageView> weakReference = this.mImageView;
        ImageView imageView = weakReference != null ? weakReference.get() : null;
        if (imageView == null) {
            cleanup();
        }
        return imageView;
    }

    public float getMaximumScale() {
        return this.mMaxScale;
    }

    public float getMediumScale() {
        return this.mMidScale;
    }

    public float getMinimumScale() {
        return this.mMinScale;
    }

    @Deprecated
    public OnPhotoTapListener getOnPhotoTapListener() {
        return this.mPhotoTapListener;
    }

    @Deprecated
    public OnViewTapListener getOnViewTapListener() {
        return this.mViewTapListener;
    }

    public float getScale() {
        return (float) Math.sqrt(((float) Math.pow(getValue(this.mSuppMatrix, 0), 2.0d)) + ((float) Math.pow(getValue(this.mSuppMatrix, 3), 2.0d)));
    }

    public ImageView.ScaleType getScaleType() {
        return this.mScaleType;
    }

    public PhotoViewAttacher(ImageView imageView, boolean zoomable) {
        this.mInterpolator = new AccelerateDecelerateInterpolator();
        this.mZoomDuration = 220;
        this.mMinScale = 1.0f;
        this.mMidScale = 1.75f;
        this.mMaxScale = 5.0f;
        this.mAllowParentInterceptOnEdge = true;
        this.mBlockParentIntercept = false;
        this.mBaseMatrix = new Matrix();
        this.mDrawMatrix = new Matrix();
        this.mSuppMatrix = new Matrix();
        this.mDisplayRect = new RectF();
        this.mMatrixValues = new float[9];
        this.mScrollEdge = 2;
        this.mScaleType = ImageView.ScaleType.FIT_CENTER;
        this.mImageView = new WeakReference<>(imageView);
        imageView.setDrawingCacheEnabled(true);
        imageView.setOnTouchListener(this);
        ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
        if (viewTreeObserver != null) {
            viewTreeObserver.addOnGlobalLayoutListener(this);
        }
        setImageViewScaleTypeMatrix(imageView);
        if (imageView.isInEditMode()) {
            return;
        }
        this.mScaleDragDetector = VersionedGestureDetector.newInstance(imageView.getContext(), this);
        GestureDetector gestureDetector = new GestureDetector(imageView.getContext(), new GestureListener());
        this.mGestureDetector = gestureDetector;
        gestureDetector.setOnDoubleTapListener(new uk.co.senab.photoview.DefaultOnDoubleTapListener(this));
        this.mBaseRotation = 0.0f;
        setZoomable(zoomable);
    }
}
