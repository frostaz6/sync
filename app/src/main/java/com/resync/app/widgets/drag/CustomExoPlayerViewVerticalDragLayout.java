package com.resync.app.widgets.drag;

import android.content.Context;
import android.view.View;
import com.resync.app.ui.views.video.CustomExoPlayerView;

/* JADX INFO: renamed from: bc.b */
/* JADX INFO: compiled from: CustomExoPlayerViewVerticalDragLayout.java */
/* JADX INFO: loaded from: classes3.dex */
public class CustomExoPlayerViewVerticalDragLayout extends AbstractVerticalViewDragLayout<CustomExoPlayerView> {

    /* JADX INFO: renamed from: bc.b$a */
    /* JADX INFO: compiled from: CustomExoPlayerViewVerticalDragLayout.java */
    class PhotoTapListener implements com.resync.app.ui.views.photoview.CustomExoPlayerViewAttacher.OnPhotoTapListener {

        final /* synthetic */ DragClickListener clickListener;

        PhotoTapListener(DragClickListener dragClickListener) {
            this.clickListener = dragClickListener;
        }

        @Override // com.resync.app.ui.views.photoview.CustomExoPlayerViewAttacher.OnPhotoTapListener
        public void onPhotoTap(View view, float x, float y) {
            DragClickListener dragClickListener = this.clickListener;
            if (dragClickListener != null) {
                dragClickListener.onClick();
            }
        }

        @Override // com.resync.app.ui.views.photoview.CustomExoPlayerViewAttacher.OnPhotoTapListener
        public void onOutsidePhotoTap() {
            DragClickListener dragClickListener = this.clickListener;
            if (dragClickListener != null) {
                dragClickListener.onClick();
            }
        }
    }

    /* JADX INFO: renamed from: bc.b$b */
    /* JADX INFO: compiled from: CustomExoPlayerViewVerticalDragLayout.java */
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

    public CustomExoPlayerViewVerticalDragLayout(Context context) {
        super(context);
        CustomExoPlayerView customExoPlayerView = new CustomExoPlayerView(context, true);
        this.view = customExoPlayerView;
        addView(customExoPlayerView);
    }

    @Override // com.resync.app.widgets.drag.VerticalViewDragLayoutInterface
    public void teardown() {
        getView().onStop();
        getView().setVisibility(8);
        getView().onDestroy();
        setVisibility(8);
    }

    @Override // com.resync.app.widgets.drag.VerticalViewDragLayoutInterface
    public void setOnDragClickListener(DragClickListener dragClickListener) {
        getView().setOnPhotoTapListener(new PhotoTapListener(dragClickListener));
    }

    @Override // com.resync.app.widgets.drag.VerticalViewDragLayoutInterface
    public int getViewHeight() {
        return getView().getHeight();
    }

    @Override // com.resync.app.widgets.drag.VerticalViewDragLayoutInterface
    public void setOnDragLongPressListener(DragLongPressListener dragLongPressListener) {
        getView().setOnPhotoLongPressListener(new LongClickListener(dragLongPressListener));
    }

    @Override // com.resync.app.widgets.drag.VerticalViewDragLayoutInterface
    public boolean isReady() {
        return true;
    }

    @Override // com.resync.app.widgets.drag.VerticalViewDragLayoutInterface
    public int getViewTop() {
        return getView().getTop();
    }
}
