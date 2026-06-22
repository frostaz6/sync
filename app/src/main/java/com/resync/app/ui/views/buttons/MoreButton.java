package com.resync.app.ui.views.buttons;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.resync.app.R;
import com.resync.app.widgets.ColorFilterProvider;

/* JADX INFO: loaded from: classes3.dex */
public class MoreButton extends AbstractRedditButton {

    GradientDrawable highlightDrawable;

    public MoreButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.resync.app.ui.views.buttons.AbstractRedditButton
    protected void applyStyle() {
        super.applyStyle();
        setImageResource(R.drawable.ic_more_vert_white_24dp);
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        setColorFilter(ColorFilterProvider.getColorFilter(getFilterColor()));
    }

    public void setHighlighted(boolean highlighted) {
        if (this.highlightDrawable != null) {
            setColorFilter(ColorFilterProvider.getColorFilter(-1));
        } else {
            setColorFilter(ColorFilterProvider.getColorFilter(resolveFilterColor(highlighted)));
        }
    }
}
