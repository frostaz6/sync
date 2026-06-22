package com.resync.app.ui.views.buttons.upload;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.view.ViewCompat;
import com.resync.app.R;
import com.resync.app.widgets.ColorFilterProvider;
import com.resync.app.helpers.ThemeHelper;

/* JADX INFO: loaded from: classes3.dex */
public class CameraButton extends AppCompatImageButton {
    public CameraButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyStyle();
    }

    protected void applyStyle() {
        ThemeHelper.applyEmptyButtonBackground(this);
        ViewCompat.setLayerType(this, 0, null);
        setImageResource(R.drawable.ic_camera_alt_black_24dp);
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        setColorFilter(ColorFilterProvider.getColorFilter(-1));
    }
}
