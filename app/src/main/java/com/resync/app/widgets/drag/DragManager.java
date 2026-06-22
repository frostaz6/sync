package com.resync.app.widgets.drag;

import com.resync.app.widgets.DragLayout;
import android.app.Activity;

/* JADX INFO: renamed from: kb.a */
/* JADX INFO: compiled from: DragManager.java */
/* JADX INFO: loaded from: classes3.dex */
public interface DragManager {
    void setDragEnabled(boolean enabled);

    void attach(Activity activity, int layoutRes);

    void refresh(Activity activity);

    DragLayout getDragLayout();

    void onDestroy();
}
