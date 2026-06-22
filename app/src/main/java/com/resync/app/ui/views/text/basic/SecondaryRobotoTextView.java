package com.resync.app.ui.views.text.basic;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
@Deprecated
public class SecondaryRobotoTextView extends AppCompatTextView implements DarkModeAware {
    public SecondaryRobotoTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setTypeface(TypefaceManager.get(9));
        applyTextColor();
    }

    private void applyTextColor() {
        setTextColor(MonetUtil.getSecondaryTextColorAuto());
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyTextColor();
    }
}
