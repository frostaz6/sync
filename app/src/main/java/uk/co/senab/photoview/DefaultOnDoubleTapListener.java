package uk.co.senab.photoview;

import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

/* JADX INFO: compiled from: DefaultOnDoubleTapListener.java */
/* JADX INFO: loaded from: classes3.dex */
public class DefaultOnDoubleTapListener implements GestureDetector.OnDoubleTapListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private PhotoViewAttacher mAttacher;

    public DefaultOnDoubleTapListener(PhotoViewAttacher attacher) {
        setPhotoViewAttacher(attacher);
    }

    public void setPhotoViewAttacher(PhotoViewAttacher attacher) {
        this.mAttacher = attacher;
    }

    @Override // android.view.GestureDetector.OnDoubleTapListener
    public boolean onDoubleTap(MotionEvent motionEvent) {
        PhotoViewAttacher attacher = this.mAttacher;
        if (attacher == null) {
            return false;
        }
        try {
            float scale = attacher.getScale();
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (scale < this.mAttacher.getMediumScale()) {
                PhotoViewAttacher mediumAttacher = this.mAttacher;
                mediumAttacher.setScale(mediumAttacher.getMediumScale(), x, y, true);
            } else if (scale < this.mAttacher.getMediumScale() || scale >= this.mAttacher.getMaximumScale()) {
                PhotoViewAttacher minAttacher = this.mAttacher;
                minAttacher.setScale(minAttacher.getMinimumScale(), x, y, true);
            } else {
                PhotoViewAttacher maxAttacher = this.mAttacher;
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
        PhotoViewAttacher attacher = this.mAttacher;
        if (attacher == null) {
            return false;
        }
        ImageView imageView = attacher.getImageView();
        if (this.mAttacher.getOnPhotoTapListener() != null && (displayRect = this.mAttacher.getDisplayRect()) != null) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (displayRect.contains(x, y)) {
                this.mAttacher.getOnPhotoTapListener().onPhotoTap(imageView, (x - displayRect.left) / displayRect.width(), (y - displayRect.top) / displayRect.height());
                return true;
            }
            this.mAttacher.getOnPhotoTapListener().onOutsidePhotoTap();
        }
        if (this.mAttacher.getOnViewTapListener() != null) {
            this.mAttacher.getOnViewTapListener().onViewTap(imageView, motionEvent.getX(), motionEvent.getY());
        }
        return false;
    }
}
