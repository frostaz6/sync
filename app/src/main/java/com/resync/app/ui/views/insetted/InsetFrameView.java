package com.resync.app.ui.views.insetted;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.internal.ViewUtils;
import com.resync.app.ui.views.insetted.InsetFrameView;

/* JADX INFO: loaded from: classes3.dex */
public class InsetFrameView extends FrameLayout {
    @SuppressLint({"RestrictedApi"})
    public InsetFrameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        ViewUtils.doOnApplyWindowInsets(this, new ViewUtils.OnApplyWindowInsetsListener() { // from class: fc.b
            @Override // com.google.android.material.internal.ViewUtils.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
                return InsetFrameView.applyWindowInsets(view, windowInsetsCompat, relativePadding);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ WindowInsetsCompat applyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
        relativePadding.bottom += windowInsetsCompat.getSystemWindowInsetBottom();
        relativePadding.applyToView(view);
        return windowInsetsCompat;
    }
}
