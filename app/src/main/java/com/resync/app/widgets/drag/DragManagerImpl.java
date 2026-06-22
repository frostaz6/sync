package com.resync.app.widgets.drag;

import com.resync.app.widgets.DragLayout;
import android.app.Activity;

/* JADX INFO: renamed from: kb.b */
/* JADX INFO: compiled from: DragManagerImpl.java */
/* JADX INFO: loaded from: classes3.dex */
public class DragManagerImpl implements DragManager {

    protected DragLayout dragLayout;

    @Override // com.resync.app.widgets.drag.DragManager
    public void setDragEnabled(boolean enabled) {
        if (getDragLayout() != null) {
            getDragLayout().setDisabled(enabled);
        }
    }

    @Override // com.resync.app.widgets.drag.DragManager
    public void attach(Activity activity, int layoutRes) {
        DragLayout dragLayoutS = DragLayout.create(activity, layoutRes);
        this.dragLayout = dragLayoutS;
        dragLayoutS.setDisabled(true);
        activity.setContentView(this.dragLayout);
    }

    @Override // com.resync.app.widgets.drag.DragManager
    public void refresh(Activity activity) {
        getDragLayout().applyBackground();
    }

    @Override // com.resync.app.widgets.drag.DragManager
    public DragLayout getDragLayout() {
        return this.dragLayout;
    }

    @Override // com.resync.app.widgets.drag.DragManager
    public void onDestroy() {
        if (getDragLayout() != null) {
            getDragLayout().setCloseListener(null);
        }
    }
}
