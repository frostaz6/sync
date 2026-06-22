package com.resync.app.ui.views.buttons;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageButton;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.widgets.ColorFilterProvider;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.ThemeHelper;

/* JADX INFO: loaded from: classes3.dex */
public class FormattingButton extends AppCompatImageButton {
    public FormattingButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyStyle();
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, SyncStyleable.FORMATTING_BUTTON);
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(0, 0);
        if (resourceId != 0) {
            setImageResource(resourceId);
        }
        typedArrayObtainStyledAttributes.recycle();
    }

    private void applyStyle() {
        ThemeHelper.applyButtonBackground(getContext(), this);
        if (NightHelper.isNight()) {
            setColorFilter(ColorFilterProvider.getColorFilter(-1));
        } else {
            setColorFilter(ColorFilterProvider.getColorFilter(-16777216));
        }
    }
}
