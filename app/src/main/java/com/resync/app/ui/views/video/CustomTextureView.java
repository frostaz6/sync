package com.resync.app.ui.views.video;

import android.content.Context;
import android.graphics.Matrix;
import android.view.TextureView;
import android.view.View;
import com.resync.app.ui.views.photoview.CustomExoPlayerViewAttacher;

/* JADX INFO: renamed from: rc.a */
/* JADX INFO: compiled from: CustomTextureView.java */
/* JADX INFO: loaded from: classes3.dex */
public class CustomTextureView extends TextureView {

    Matrix baseMatrix;
    Matrix displayMatrix;
    public float contentWidth;

    public float contentHeight;

    int videoWidth;

    int videoHeight;
    private CustomExoPlayerViewAttacher attacher;

    public CustomTextureView(Context context) {
        super(context);
        this.baseMatrix = new Matrix();
        this.displayMatrix = new Matrix();
        init();
    }

    private void init() {
        setVideoSize(0, 0);
    }

    public void setVideoLongClickListener(View.OnLongClickListener onLongClickListener) {
        CustomExoPlayerViewAttacher attacher = this.attacher;
        if (attacher != null) {
            attacher.setOnLongClickListener(onLongClickListener);
        }
    }

    public void setPhotoTapListener(CustomExoPlayerViewAttacher.OnPhotoTapListener photoTapListener) {
        CustomExoPlayerViewAttacher attacher = this.attacher;
        if (attacher != null) {
            attacher.setOnPhotoTapListener(photoTapListener);
        }
    }

    public void setVideoSize(int videoWidth, int videoHeight) {
        this.videoWidth = videoWidth;
        this.videoHeight = videoHeight;
        this.baseMatrix.reset();
        // NOTE: the original bytecode does FLOAT division here (int→float cast on the numerator).
        // JADX dropped the cast → int/int → ArithmeticException("divide by zero") when this view is
        // constructed before layout (getWidth()==0, videoWidth==0). Restoring the cast matches the
        // original semantics (0f/0 = NaN, harmless). Verified against the Vineflower decompile (rc/a.java).
        float width = (float) getWidth() / this.videoWidth;
        float height = (float) getHeight() / this.videoHeight;
        if (this.videoWidth * getHeight() < getWidth() * this.videoHeight) {
            this.contentWidth = height * this.videoWidth;
            this.contentHeight = getHeight();
            this.baseMatrix.setScale(this.contentWidth / getWidth(), 1.0f);
        } else {
            this.contentWidth = getWidth();
            float scaledHeight = width * this.videoHeight;
            this.contentHeight = scaledHeight;
            this.baseMatrix.setScale(1.0f, scaledHeight / getHeight());
        }
        setTransform(this.baseMatrix);
        invalidate();
        CustomExoPlayerViewAttacher attacher = this.attacher;
        if (attacher != null) {
            attacher.refresh();
        }
    }

    public void enableZoom(boolean zoomable) {
        this.attacher = new CustomExoPlayerViewAttacher(this, zoomable);
        setVideoSize(0, 0);
    }

    public Matrix applyMatrix(Matrix matrix) {
        this.displayMatrix.reset();
        this.displayMatrix.set(this.baseMatrix);
        this.displayMatrix.postConcat(matrix);
        setTransform(this.displayMatrix);
        invalidate();
        return this.displayMatrix;
    }

    @Override // android.view.TextureView, android.view.View
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        setVideoSize(this.videoWidth, this.videoHeight);
    }
}
