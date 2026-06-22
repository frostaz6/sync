package com.resync.app.ui.views.transparent_status;

import android.content.Context;
import android.util.AttributeSet;
import com.google.android.material.appbar.AppBarLayout;
import com.resync.app.R;
import com.resync.app.ui.views.core.MaterialYouToolbar;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes3.dex */
public class CustomAppBarLayout extends AppBarLayout implements AppBarLayout.LiftOnScrollListener {
    private MaterialYouToolbar toolbar;
    public float elevationThreshold;

    public CustomAppBarLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.elevationThreshold = getResources().getDimension(R.dimen.design_appbar_elevation);
        addLiftOnScrollListener(this);
    }

    @Override // com.google.android.material.appbar.AppBarLayout
    public void setLiftOnScroll(boolean liftOnScroll) {
        super.setLiftOnScroll(true);
    }

    protected MaterialYouToolbar getToolbar() {
        return this.toolbar;
    }

    @Override // com.google.android.material.appbar.AppBarLayout.LiftOnScrollListener
    public void onUpdate(float elevation, int backgroundColor) {
        float dividerAlpha = elevation / this.elevationThreshold;
        if (this.toolbar != null) {
            Logger.verbose("CALLED");
            this.toolbar.setDividerPaintAlpha(dividerAlpha);
        }
        if (getParent() instanceof CustomCoordinatorLayout) {
            ((CustomCoordinatorLayout) getParent()).setDividerAlpha(dividerAlpha);
        }
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.toolbar = (MaterialYouToolbar) findViewById(R.id.toolbar);
    }
}
