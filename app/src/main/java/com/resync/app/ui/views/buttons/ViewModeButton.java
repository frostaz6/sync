package com.resync.app.ui.views.buttons;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.resync.app.R;
import com.resync.app.widgets.ColorFilterProvider;

/* JADX INFO: loaded from: classes3.dex */
public class ViewModeButton extends AbstractRedditButton {
    public ViewModeButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.resync.app.ui.views.buttons.AbstractRedditButton
    protected void applyStyle() {
        super.applyStyle();
        setImageResource(R.drawable.bottom_nav_outline_dashboard_24);
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        setColorFilter(ColorFilterProvider.getColorFilter(getFilterColor()));
    }
}
