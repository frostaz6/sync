package com.resync.app.ui.views.buttons;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.resync.app.R;
import com.resync.app.widgets.ColorFilterProvider;

/* JADX INFO: loaded from: classes3.dex */
public class SaveButton extends AbstractRedditButton {
    public SaveButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.resync.app.ui.views.buttons.AbstractRedditButton
    protected void applyStyle() {
        super.applyStyle();
        setImageResource(R.drawable.outline_bookmark_border_24);
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        setActive(false);
    }

    public void setActive(boolean active) {
        if (active) {
            setColorFilter(ColorFilterProvider.getColorFilter(-1324481));
            setImageResource(R.drawable.outline_bookmark_24);
            setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else {
            setColorFilter(ColorFilterProvider.getColorFilter(getFilterColor()));
            setImageResource(R.drawable.outline_bookmark_border_24);
            setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
    }

    public void setButtonVisible(boolean visible) {
        setVisibility(visible ? 0 : 8);
    }
}
