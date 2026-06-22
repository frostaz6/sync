package com.resync.app.ui.views.text.basic;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
@Deprecated
public class LinkProductSansTextView extends AppCompatTextView implements DarkModeAware {
    public LinkProductSansTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setTypeface(TypefaceManager.get(11));
        applyTextColor();
    }

    private void applyTextColor() {
        setTextColor(MonetUtil.getLinkColorAuto());
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyTextColor();
    }
}
