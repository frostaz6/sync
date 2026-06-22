package com.resync.app.ui.views.buttons;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.resync.app.R;
import com.resync.app.widgets.ColorFilterProvider;

/* JADX INFO: loaded from: classes3.dex */
public class ExpandButton extends AbstractRedditButton {
    public ExpandButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.resync.app.ui.views.buttons.AbstractRedditButton
    protected void applyStyle() {
        super.applyStyle();
        setImageResource(R.drawable.ic_expand_more_white_24dp);
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        setColorFilter(ColorFilterProvider.getColorFilter(getFilterColor()));
    }

    public void setExpanded(boolean expanded) {
        if (expanded) {
            setImageResource(R.drawable.ic_expand_less_white_24dp);
        } else {
            setImageResource(R.drawable.ic_expand_more_white_24dp);
        }
    }
}
