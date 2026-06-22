package com.resync.app.ui.views.buttons;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.resync.app.R;
import com.resync.app.widgets.ColorFilterProvider;

/* JADX INFO: loaded from: classes3.dex */
public class DownvoteButton extends AbstractRedditButton {
    public DownvoteButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.resync.app.ui.views.buttons.AbstractRedditButton
    protected void applyStyle() {
        super.applyStyle();
        setImageResource(R.drawable.ic_arrow_downward_white_24dp);
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        setActive(false);
    }

    public void setActive(boolean active) {
        if (active) {
            setColorFilter(ColorFilterProvider.getColorFilter(-7039745));
        } else {
            setColorFilter(ColorFilterProvider.getColorFilter(getFilterColor()));
        }
    }
}
