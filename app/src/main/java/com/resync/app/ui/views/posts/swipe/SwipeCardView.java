package com.resync.app.ui.views.posts.swipe;

import android.content.Context;
import android.util.AttributeSet;
import com.google.android.material.card.MaterialCardView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: loaded from: classes3.dex */
public class SwipeCardView extends MaterialCardView {
    public SwipeCardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setCardBackgroundColor(MonetUtil.getBackgroundColorAuto());
        setCardElevation(SizeHelper.dpToPxFloat(1));
        setRadius(SizeHelper.dpToPx(8));
    }
}
