package com.resync.app.ui.views.drawer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.squareup.otto.Subscribe;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.DrawerClosedEvent;

/* JADX INFO: loaded from: classes3.dex */
public class SectionLinearLayout extends LinearLayout {
    public SectionLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOrientation(1);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BusSingleton.getBus().register(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BusSingleton.getBus().unregister(this);
    }

    @Subscribe
    public void onDrawerClosed(DrawerClosedEvent drawerClosedEvent) {
        setVisibility(8);
    }
}
