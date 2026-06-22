package com.resync.app.ui.views.cards;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageView;
import com.resync.app.R;
import com.resync.app.widgets.ColorFilterProvider;
import com.resync.app.helpers.NightHelper;

/* JADX INFO: loaded from: classes3.dex */
public class CardButton extends AppCompatImageView {
    public CardButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setBackgroundResource(R.drawable.button_ripple);
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        applyColors();
    }

    public void applyColors() {
        setColorFilter(ColorFilterProvider.getColorFilter(NightHelper.isNight() ? -8947849 : -6645094));
    }
}
