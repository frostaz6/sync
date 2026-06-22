package com.resync.app.ui.views.monet;

import android.content.Context;
import android.util.AttributeSet;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class MonetSwipeRefreshLayout extends SwipeRefreshLayout implements DarkModeAware {
    public MonetSwipeRefreshLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyColors();
        setProgressViewOffset(false, SizeHelper.dpToPx(-48), SizeHelper.dpToPx(8));
    }

    public void applyColors() {
        int linkColor = MonetUtil.getLinkColorAuto();
        setColorSchemeColors(linkColor, linkColor, linkColor);
        setProgressBackgroundColorSchemeColor(MonetUtil.getToolbarColorAuto());
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }

    @Override // android.view.View
    public boolean isEnabled() {
        return super.isEnabled() && ConnectivityHelper.isConnected(getContext());
    }

    @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        super.setOnRefreshListener(listener);
    }
}
