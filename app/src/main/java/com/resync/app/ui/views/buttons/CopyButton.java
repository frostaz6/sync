package com.resync.app.ui.views.buttons;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.resync.app.R;
import com.resync.app.widgets.ColorFilterProvider;

/* JADX INFO: loaded from: classes3.dex */
public class CopyButton extends AbstractRedditButton {
    public CopyButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.resync.app.ui.views.buttons.AbstractRedditButton
    protected void applyStyle() {
        super.applyStyle();
        setImageResource(R.drawable.ic_content_copy_white_24dp);
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        setColorFilter(ColorFilterProvider.getColorFilter(getFilterColor()));
    }
}
