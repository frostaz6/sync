package com.theartofdev.edmodo.cropper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/* Stub: public API surface preserved (enums, result class, listener
 * interfaces, and the methods CropImageActivity/CropImage call).
 * Actual cropping/rendering is not wired up - bodies are no-ops. */
public class CropImageView extends FrameLayout {

    private Uri mLoadedImageUri;
    private int mDegreesRotated;
    private Bitmap mBitmap;
    private OnSetImageUriCompleteListener mOnSetImageUriCompleteListener;
    private OnCropImageCompleteListener mOnCropImageCompleteListener;

    /* Crop result holder; CropImage.ActivityResult extends this. */
    public static class CropResult {

        private final Bitmap originalBitmap;
        private final Uri originalUri;
        private final Bitmap bitmap;
        private final Uri uri;
        private final Exception error;
        private final float[] cropPoints;
        private final Rect cropRect;
        private final Rect wholeImageRect;
        private final int sampleSize;
        private final int rotation;

        CropResult(Bitmap originalBitmap, Uri originalUri, Bitmap bitmap, Uri uri, Exception error, float[] cropPoints, Rect cropRect, Rect wholeImageRect, int sampleSize, int rotation) {
            this.originalBitmap = originalBitmap;
            this.originalUri = originalUri;
            this.bitmap = bitmap;
            this.uri = uri;
            this.error = error;
            this.cropPoints = cropPoints;
            this.cropRect = cropRect;
            this.wholeImageRect = wholeImageRect;
            this.sampleSize = sampleSize;
            this.rotation = rotation;
        }

        public float[] getCropPoints() {
            return this.cropPoints;
        }

        public Rect getCropRect() {
            return this.cropRect;
        }

        public Exception getError() {
            return this.error;
        }

        public Uri getOriginalUri() {
            return this.originalUri;
        }

        public int getSampleSize() {
            return this.sampleSize;
        }

        public int getRotation() {
            return this.rotation;
        }

        public Uri getUri() {
            return this.uri;
        }

        public Rect getWholeImageRect() {
            return this.wholeImageRect;
        }
    }

    public enum CropShape {
        RECTANGLE,
        OVAL
    }

    public enum Guidelines {
        OFF,
        ON_TOUCH,
        ON
    }

    public interface OnCropImageCompleteListener {
        void onCropImageComplete(CropImageView cropImageView, CropResult result);
    }

    public interface OnSetCropWindowChangeListener {
        void onCropWindowChanged(Rect rect);
    }

    public interface OnSetCropOverlayReleasedListener {
        void onCropOverlayReleased(Rect rect);
    }

    public interface OnSetCropOverlayMovedListener {
        void onCropOverlayMoved();
    }

    public interface OnSetImageUriCompleteListener {
        void onSetImageUriComplete(CropImageView cropImageView, Uri uri, Exception error);
    }

    public enum RequestSizeOptions {
        NONE,
        SAMPLING,
        RESIZE_INSIDE,
        RESIZE_FIT,
        RESIZE_EXACT
    }

    public enum ScaleType {
        FIT_CENTER,
        CENTER,
        CENTER_CROP,
        CENTER_INSIDE
    }

    public CropImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setRotatedDegrees(int degrees) {
        this.mDegreesRotated = degrees;
    }

    public void flipImageHorizontally() {
    }

    public void flipImageVertically() {
    }

    public float[] getCropPoints() {
        return new float[8];
    }

    public Rect getCropRect() {
        Bitmap bitmap = this.mBitmap;
        if (bitmap == null) {
            return null;
        }
        return new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    }

    public Uri getImageUri() {
        return this.mLoadedImageUri;
    }

    public int getRotatedDegrees() {
        return this.mDegreesRotated;
    }

    public Rect getWholeImageRect() {
        Bitmap bitmap = this.mBitmap;
        if (bitmap == null) {
            return null;
        }
        return new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    }

    public void rotateImage(int degrees) {
        this.mDegreesRotated = (this.mDegreesRotated + degrees) % 360;
    }

    public void startCropWorkerTask(Uri saveUri, Bitmap.CompressFormat saveCompressFormat, int saveCompressQuality, int reqWidth, int reqHeight, RequestSizeOptions options) {
        // No-op crop: immediately report a (failed) completion so callers
        // do not hang. Cropping is not wired up.
        OnCropImageCompleteListener listener = this.mOnCropImageCompleteListener;
        if (listener != null) {
            listener.onCropImageComplete(this, new CropResult(this.mBitmap, this.mLoadedImageUri, null, saveUri, null, getCropPoints(), getCropRect(), getWholeImageRect(), getRotatedDegrees(), 0));
        }
    }

    public void setCropRect(Rect rect) {
    }

    public void setImageResource(int resId) {
    }

    public void setImageUriAsync(Uri uri) {
        this.mLoadedImageUri = uri;
        OnSetImageUriCompleteListener listener = this.mOnSetImageUriCompleteListener;
        if (listener != null) {
            listener.onSetImageUriComplete(this, uri, null);
        }
    }

    public void setOnCropImageCompleteListener(OnCropImageCompleteListener listener) {
        this.mOnCropImageCompleteListener = listener;
    }

    public void setOnSetImageUriCompleteListener(OnSetImageUriCompleteListener listener) {
        this.mOnSetImageUriCompleteListener = listener;
    }
}
