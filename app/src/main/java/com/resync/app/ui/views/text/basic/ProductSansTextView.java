package com.resync.app.ui.views.text.basic;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
@Deprecated
public class ProductSansTextView extends AppCompatTextView implements DarkModeAware {
    public ProductSansTextView(Context context) {
        super(context);
        setTypeface(TypefaceManager.get(11));
        applyTextColor();
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyTextColor();
    }

    public void applyTextColor() {
        setTextColor(StyleSingleton.getTextColorAuto(getContext(), false));
    }

    public ProductSansTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setTypeface(TypefaceManager.get(11));
        applyTextColor();
    }
}
