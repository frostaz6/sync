package com.resync.app.ui.views.posts.swipe;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import com.resync.app.ui.activities.CasualGalleryActivity;
import com.resync.app.helpers.NightHelper;

/* JADX INFO: loaded from: classes3.dex */
public class SwipeDivider extends View {
    public SwipeDivider(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if ((context instanceof CasualGalleryActivity) || NightHelper.isNight()) {
            setBackgroundDrawable(new ColorDrawable(-10066330));
        } else {
            setBackgroundDrawable(new ColorDrawable(-2236963));
        }
    }
}
