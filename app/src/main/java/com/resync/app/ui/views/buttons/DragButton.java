package com.resync.app.ui.views.buttons;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;
import com.resync.app.R;
import com.resync.app.helpers.NightHelper;

/* JADX INFO: loaded from: classes3.dex */
public class DragButton extends AppCompatImageView {
    public DragButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyStyle();
    }

    protected void applyStyle() {
        setImageResource(R.drawable.ic_drag);
        setImageTintList(ColorStateList.valueOf(NightHelper.isNight() ? -8947849 : -2236963));
    }
}
