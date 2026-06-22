package com.resync.app.ui.views.buttons;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;
import com.resync.app.widgets.ColorFilterProvider;
import com.resync.app.helpers.NightHelper;

/* JADX INFO: loaded from: classes3.dex */
public class CustomAppCompatButton extends AppCompatImageView {
    public CustomAppCompatButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyColorFilter(NightHelper.isNight());
    }

    public void applyColorFilter(boolean night) {
        setColorFilter(ColorFilterProvider.getColorFilter(night ? -1 : -16777216));
    }
}
