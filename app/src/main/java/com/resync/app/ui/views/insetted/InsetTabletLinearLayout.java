package com.resync.app.ui.views.insetted;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.internal.ViewUtils;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.insetted.InsetTabletLinearLayout;

/* JADX INFO: loaded from: classes3.dex */
public class InsetTabletLinearLayout extends LinearLayout {
    @SuppressLint({"RestrictedApi"})
    public InsetTabletLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (SettingsSingleton.getSettings().tabletInsets) {
            ViewUtils.doOnApplyWindowInsets(this, new ViewUtils.OnApplyWindowInsetsListener() { // from class: fc.d
                @Override // com.google.android.material.internal.ViewUtils.OnApplyWindowInsetsListener
                public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
                    return InsetTabletLinearLayout.applyWindowInsets(view, windowInsetsCompat, relativePadding);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ WindowInsetsCompat applyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
        relativePadding.start += windowInsetsCompat.getSystemWindowInsetLeft();
        relativePadding.end += windowInsetsCompat.getSystemWindowInsetRight();
        relativePadding.bottom += windowInsetsCompat.getSystemWindowInsetBottom();
        relativePadding.applyToView(view);
        return windowInsetsCompat;
    }
}
