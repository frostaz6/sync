package com.resync.app.ui.views.photoview;

import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.resync.app.ui.views.video.CustomTextureView;

/* JADX INFO: compiled from: CustomExoPlayerViewDefaultOnDoubleTapListener.java */
/* JADX INFO: loaded from: classes3.dex */
public class CustomExoPlayerViewDefaultOnDoubleTapListener implements GestureDetector.OnDoubleTapListener {

    private CustomExoPlayerViewAttacher attacher;

    public CustomExoPlayerViewDefaultOnDoubleTapListener(CustomExoPlayerViewAttacher attacher) {
        setAttacher(attacher);
    }

    public void setAttacher(CustomExoPlayerViewAttacher attacher) {
        this.attacher = attacher;
    }

    @Override // android.view.GestureDetector.OnDoubleTapListener
    public boolean onDoubleTap(MotionEvent motionEvent) {
        CustomExoPlayerViewAttacher attacher = this.attacher;
        if (attacher == null) {
            return false;
        }
        try {
            float scale = attacher.getScale();
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (scale < this.attacher.getMediumScale()) {
                CustomExoPlayerViewAttacher mediumAttacher = this.attacher;
                mediumAttacher.setScale(mediumAttacher.getMediumScale(), x, y, true);
            } else if (scale < this.attacher.getMediumScale() || scale >= this.attacher.getMaximumScale()) {
                CustomExoPlayerViewAttacher minAttacher = this.attacher;
                minAttacher.setScale(minAttacher.getMinimumScale(), x, y, true);
            } else {
                CustomExoPlayerViewAttacher maxAttacher = this.attacher;
                maxAttacher.setScale(maxAttacher.getMaximumScale(), x, y, true);
            }
        } catch (ArrayIndexOutOfBoundsException unused) {
        }
        return true;
    }

    @Override // android.view.GestureDetector.OnDoubleTapListener
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override // android.view.GestureDetector.OnDoubleTapListener
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        RectF displayRect;
        CustomExoPlayerViewAttacher attacher = this.attacher;
        if (attacher == null) {
            return false;
        }
        CustomTextureView imageView = attacher.getImageView();
        if (this.attacher.getOnPhotoTapListener() != null && (displayRect = this.attacher.getDisplayRect()) != null) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (displayRect.contains(x, y)) {
                this.attacher.getOnPhotoTapListener().onPhotoTap(imageView, (x - displayRect.left) / displayRect.width(), (y - displayRect.top) / displayRect.height());
                return true;
            }
            this.attacher.getOnPhotoTapListener().onOutsidePhotoTap();
        }
        if (this.attacher.getOnViewTapListener() != null) {
            this.attacher.getOnViewTapListener().onViewTap(imageView, motionEvent.getX(), motionEvent.getY());
        }
        return false;
    }
}
