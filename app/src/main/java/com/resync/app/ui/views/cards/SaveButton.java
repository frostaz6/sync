package com.resync.app.ui.views.cards;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.resync.app.R;
import com.resync.app.widgets.ColorFilterProvider;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: loaded from: classes3.dex */
public class SaveButton extends CardButton {

    boolean saved;

    public SaveButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setSaved(false);
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
        if (saved) {
            setColorFilter(ColorFilterProvider.getColorFilter(-1324481));
            setImageResource(R.drawable.outline_bookmark_24);
            setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else {
            applyColors();
            setImageResource(R.drawable.outline_bookmark_border_24);
            setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        int padding = SizeHelper.dpToPx(5);
        setPadding(padding, padding, padding, padding);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setSaved(this.saved);
    }
}
