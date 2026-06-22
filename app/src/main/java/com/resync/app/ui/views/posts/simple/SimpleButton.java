package com.resync.app.ui.views.posts.simple;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.widgets.ColorFilterProvider;
import com.resync.app.util.RippleUtils;

/* JADX INFO: loaded from: classes3.dex */
public class SimpleButton extends AppCompatImageView {
    public SimpleButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setup();
    }

    private void setup() {
        setBackground(RippleUtils.getNoBoundsRipple());
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        applyDefaultTint();
    }

    public void applyDefaultTint() {
        setColorFilter(ColorFilterProvider.getColorFilter(MonetUtil.getTertiaryTextColorAuto()));
    }
}
