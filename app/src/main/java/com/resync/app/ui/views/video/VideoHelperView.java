package com.resync.app.ui.views.video;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.resync.app.singleton.SettingsSingleton;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.widgets.AbstractPostPreviewView;
import com.resync.app.helpers.WindowUtils;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes3.dex */
public class VideoHelperView extends View {
    public VideoHelperView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (SettingsSingleton.getSettings().developerVideo) {
            setBackgroundColor(872349696);
        }
    }

    private Rect getViewRect(View view) {
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        return view instanceof AbstractPostPreviewView ? new Rect(iArr[0], iArr[1], iArr[0] + getWidth(), iArr[1] + ((AbstractPostPreviewView) view).getCalculatedHeight()) : new Rect(iArr[0], iArr[1], iArr[0] + getWidth(), iArr[1] + getHeight());
    }

    public boolean intersects(View view) {
        Rect rectA = getViewRect(this);
        Rect rectA2 = getViewRect(view);
        Logger.verbose("VideoHelperView", "Helper: " + rectA.top + StringUtils.SPACE + rectA.bottom);
        Logger.verbose("VideoHelperView", "Preview: " + rectA2.top + StringUtils.SPACE + rectA2.bottom);
        return rectA.intersect(rectA2);
    }

    @Override // android.view.View
    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        layoutParams.height = (int) (WindowUtils.getDisplayHeight() * 0.5f);
        super.setLayoutParams(layoutParams);
    }
}
