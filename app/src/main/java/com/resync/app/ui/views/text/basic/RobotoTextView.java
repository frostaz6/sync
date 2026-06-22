package com.resync.app.ui.views.text.basic;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
@Deprecated
public class RobotoTextView extends AppCompatTextView implements DarkModeAware {
    public RobotoTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setTypeface(TypefaceManager.get(9));
        applyTextColor();
    }

    private void applyTextColor() {
        setTextColor(StyleSingleton.getTextColorAuto(getContext(), false));
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyTextColor();
    }
}
