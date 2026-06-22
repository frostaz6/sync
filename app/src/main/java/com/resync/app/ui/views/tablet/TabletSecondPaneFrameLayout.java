package com.resync.app.ui.views.tablet;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.resync.app.helpers.ui.TabletHelper;

/* JADX INFO: loaded from: classes3.dex */
public class TabletSecondPaneFrameLayout extends FrameLayout {
    public TabletSecondPaneFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (TabletHelper.isDualPane()) {
            setVisibility(0);
        } else {
            setVisibility(8);
        }
    }
}
