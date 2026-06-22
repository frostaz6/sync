package com.resync.app.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: renamed from: ic.a */
/* JADX INFO: compiled from: ContentBackgroundFrameLayout.java */
/* JADX INFO: loaded from: classes3.dex */
public class ContentBackgroundFrameLayout extends FrameLayout implements DarkModeAware {

    /* JADX INFO: renamed from: ic.a$a */
    /* JADX INFO: compiled from: ContentBackgroundFrameLayout.java */
    class RoundedOutlineProvider extends ViewOutlineProvider {
        RoundedOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), SizeHelper.dpToPxFloat(12));
        }
    }

    public ContentBackgroundFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyBackground();
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, SyncStyleable.ROUND_CORNERS_FRAME);
        boolean roundCorners = typedArrayObtainStyledAttributes.getBoolean(0, false);
        typedArrayObtainStyledAttributes.recycle();
        if (roundCorners) {
            setOutlineProvider(new RoundedOutlineProvider());
            setClipToOutline(true);
        }
    }

    private void applyBackground() {
        setBackgroundColor(MonetUtil.getBackgroundColorAuto());
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyBackground();
    }
}
