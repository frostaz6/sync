package com.resync.app.ui.views.buttons;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageButton;

/* JADX INFO: loaded from: classes3.dex */
public class SettingsButton extends AppCompatImageButton {
    public SettingsButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        setBackgroundColor(0);
        setClickable(false);
    }

    public void setFilterColor(int color) {
        setColorFilter(color);
    }
}
