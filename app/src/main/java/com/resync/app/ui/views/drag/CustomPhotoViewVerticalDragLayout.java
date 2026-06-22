package com.resync.app.ui.views.drag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.resync.app.widgets.CustomPhotoView;
import com.resync.app.widgets.drag.AbstractVerticalViewDragLayout;
import com.resync.app.widgets.drag.DragClickListener;
import com.resync.app.widgets.drag.DragLongPressListener;
import uk.co.senab.photoview.PhotoViewAttacher;

/* JADX INFO: loaded from: classes3.dex */
public class CustomPhotoViewVerticalDragLayout extends AbstractVerticalViewDragLayout<CustomPhotoView> {

    class PhotoTapListener implements PhotoViewAttacher.OnPhotoTapListener {

        final /* synthetic */ DragClickListener clickListener;

        PhotoTapListener(DragClickListener dragClickListener) {
            this.clickListener = dragClickListener;
        }

        @Override // uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener
        public void onPhotoTap(View view, float x, float y) {
            DragClickListener dragClickListener = this.clickListener;
            if (dragClickListener != null) {
                dragClickListener.onClick();
            }
        }

        @Override // uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener
        public void onOutsidePhotoTap() {
            DragClickListener dragClickListener = this.clickListener;
            if (dragClickListener != null) {
                dragClickListener.onClick();
            }
        }
    }

    class LongClickListener implements View.OnLongClickListener {

        final /* synthetic */ DragLongPressListener longPressListener;

        LongClickListener(DragLongPressListener dragLongPressListener) {
            this.longPressListener = dragLongPressListener;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            DragLongPressListener dragLongPressListener = this.longPressListener;
            if (dragLongPressListener == null) {
                return true;
            }
            dragLongPressListener.onLongPress();
            return true;
        }
    }

    public CustomPhotoViewVerticalDragLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        CustomPhotoView customPhotoView = new CustomPhotoView(context);
        this.view = customPhotoView;
        customPhotoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        addContentView((CustomPhotoView) this.view);
    }

    @Override // com.resync.app.widgets.drag.VerticalViewDragLayoutInterface
    public void teardown() {
        // Swipe-dismiss teardown: releasing the PhotoView attacher can race the dismiss animation.
        // Best-effort; never let cleanup crash the app.
        try {
            CustomPhotoView photoView = getView();
            if (photoView != null) {
                photoView.clearImage();
                photoView.setVisibility(8);
            }
        } catch (Throwable ignored) {
            // ignore: best-effort cleanup
        }
        setVisibility(8);
    }

    @Override // com.resync.app.widgets.drag.VerticalViewDragLayoutInterface
    public void setOnDragClickListener(DragClickListener dragClickListener) {
        getView().setOnPhotoTapListener(new PhotoTapListener(dragClickListener));
    }

    @Override // com.resync.app.widgets.drag.VerticalViewDragLayoutInterface
    public int getViewHeight() {
        return (int) (((CustomPhotoView) this.view).getDisplayRect().bottom - ((CustomPhotoView) this.view).getDisplayRect().top);
    }

    @Override // com.resync.app.widgets.drag.VerticalViewDragLayoutInterface
    public void setOnDragLongPressListener(DragLongPressListener dragLongPressListener) {
        getView().setOnLongClickListener(new LongClickListener(dragLongPressListener));
    }

    @Override // com.resync.app.widgets.drag.VerticalViewDragLayoutInterface
    public boolean isReady() {
        return ((CustomPhotoView) this.view).hasImage();
    }

    @Override // com.resync.app.widgets.drag.VerticalViewDragLayoutInterface
    public int getViewTop() {
        return (int) (((CustomPhotoView) this.view).getDisplayRect().top + ((CustomPhotoView) this.view).getTop());
    }
}
