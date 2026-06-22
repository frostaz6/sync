package com.resync.app.widgets.drag;

/* JADX INFO: renamed from: bc.e */
/* JADX INFO: compiled from: VerticalViewDragLayoutInterface.java */
/* JADX INFO: loaded from: classes3.dex */
public interface VerticalViewDragLayoutInterface {
    void teardown();

    void setOnDragClickListener(DragClickListener dragClickListener);

    int getViewHeight();

    void setOnDragLongPressListener(DragLongPressListener dragLongPressListener);

    boolean isReady();

    int getViewTop();
}
