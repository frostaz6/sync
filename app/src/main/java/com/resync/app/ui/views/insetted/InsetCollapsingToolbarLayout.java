package com.resync.app.ui.views.insetted;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.internal.ViewUtils;
import com.resync.app.ui.views.insetted.InsetCollapsingToolbarLayout;

/* JADX INFO: loaded from: classes3.dex */
public class InsetCollapsingToolbarLayout extends CollapsingToolbarLayout {
    public InsetCollapsingToolbarLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        ViewUtils.doOnApplyWindowInsets(this, new ViewUtils.OnApplyWindowInsetsListener() { // from class: fc.a
            @Override // com.google.android.material.internal.ViewUtils.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
                return InsetCollapsingToolbarLayout.applyWindowInsets(view, windowInsetsCompat, relativePadding);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ WindowInsetsCompat applyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
        return windowInsetsCompat;
    }
}
