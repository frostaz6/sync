package com.resync.app.ui.views.posts.simple;

import android.content.Context;
import android.util.AttributeSet;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.widgets.AbstractPostPreviewView;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.WindowUtils;

/* JADX INFO: loaded from: classes3.dex */
public class SimplePostPreviewHelper extends AbstractPostPreviewView {
    public SimplePostPreviewHelper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.resync.app.widgets.AbstractPostPreviewView
    protected int getColumnWidth() {
        int availableWidth = WindowUtils.getDisplayWidth();
        if (isTabletLayout()) {
            if (isTablet() || isPreferencesPreview()) {
                availableWidth /= 2;
            }
            if (!isPreferencesPreview()) {
                availableWidth -= SizeHelper.dpToPx(80);
            }
        }
        return !SettingsSingleton.getSettings().slideFullWidth ? availableWidth - SizeHelper.dpToPx(32) : availableWidth;
    }

    @Override // com.resync.app.widgets.AbstractPostPreviewView
    protected boolean isFullHeight() {
        return isCommentMode() ? SettingsSingleton.getSettings().commentMediaFull : SettingsSingleton.getSettings().slideImageFullHeight;
    }
}
