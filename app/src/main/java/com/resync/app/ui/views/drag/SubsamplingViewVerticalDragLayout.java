package com.resync.app.ui.views.drag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.resync.app.widgets.drag.AbstractVerticalViewDragLayout;
import com.resync.app.widgets.drag.DragClickListener;
import com.resync.app.widgets.drag.DragLongPressListener;

/* JADX INFO: loaded from: classes3.dex */
public class SubsamplingViewVerticalDragLayout extends AbstractVerticalViewDragLayout<SubsamplingScaleImageView> {

    private boolean ready;

    class ClickListener implements View.OnClickListener {

        final /* synthetic */ DragClickListener clickListener;

        ClickListener(DragClickListener dragClickListener) {
            this.clickListener = dragClickListener;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
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

    public SubsamplingViewVerticalDragLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.ready = true;
        SubsamplingScaleImageView subsamplingScaleImageView = new SubsamplingScaleImageView(context);
        this.view = subsamplingScaleImageView;
        addView(subsamplingScaleImageView);
    }

    @Override // com.resync.app.widgets.drag.VerticalViewDragLayoutInterface
    public void teardown() {
        // Called on swipe-dismiss teardown. recycle() can race an in-flight deep-zoom tile decode
        // (the decoder callback then touches a recycled view) -> intermittent crash after closing a
        // picture. Teardown must never crash the app, so guard it.
        try {
            SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) this.view;
            if (imageView != null) {
                imageView.setVisibility(8);
                imageView.recycle();
            }
        } catch (Throwable ignored) {
            // ignore: best-effort cleanup
        }
        setVisibility(8);
    }

    @Override // com.resync.app.widgets.drag.VerticalViewDragLayoutInterface
    public void setOnDragClickListener(DragClickListener dragClickListener) {
        getView().setOnClickListener(new ClickListener(dragClickListener));
    }

    @Override // com.resync.app.widgets.drag.VerticalViewDragLayoutInterface
    public int getViewHeight() {
        return (int) (((SubsamplingScaleImageView) this.view).getSHeight() * ((SubsamplingScaleImageView) this.view).getScale());
    }

    @Override // com.resync.app.widgets.drag.VerticalViewDragLayoutInterface
    public void setOnDragLongPressListener(DragLongPressListener dragLongPressListener) {
        getView().setOnLongClickListener(new LongClickListener(dragLongPressListener));
    }

    @Override // com.resync.app.widgets.drag.VerticalViewDragLayoutInterface
    public boolean isReady() {
        return this.ready && ((SubsamplingScaleImageView) this.view).isImageLoaded();
    }

    @Override // com.resync.app.widgets.drag.VerticalViewDragLayoutInterface
    public int getViewTop() {
        return ((((SubsamplingScaleImageView) this.view).getHeight() - getViewHeight()) / 2) + ((SubsamplingScaleImageView) this.view).getTop();
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
