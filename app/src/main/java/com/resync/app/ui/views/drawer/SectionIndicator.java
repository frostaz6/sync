package com.resync.app.ui.views.drawer;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;
import com.squareup.otto.Subscribe;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.DrawerClosedEvent;

/* JADX INFO: loaded from: classes3.dex */
public class SectionIndicator extends AppCompatImageView {
    public SectionIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BusSingleton.getBus().register(this);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BusSingleton.getBus().unregister(this);
    }

    @Subscribe
    public void onDrawerClosed(DrawerClosedEvent drawerClosedEvent) {
        setRotation(0.0f);
    }
}
