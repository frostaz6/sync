package com.resync.app.ui.views.posts.cards;

import android.content.Context;
import android.util.AttributeSet;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.widgets.AbstractPostPreviewView;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.WindowUtils;

/* JADX INFO: loaded from: classes3.dex */
public class CardPostPreviewHelper extends AbstractPostPreviewView {
    public CardPostPreviewHelper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.resync.app.widgets.AbstractPostPreviewView
    protected int getColumnWidth() {
        int availableWidth = WindowUtils.getDisplayWidth();
        if (isTabletLayout() && (isTablet() || isPreferencesPreview())) {
            int halfWidth = availableWidth / 2;
            if (isCommentMode()) {
                return halfWidth;
            }
            if (!isPreferencesPreview()) {
                halfWidth -= SizeHelper.dpToPx(80);
            }
            return !SettingsSingleton.getSettings().cardFullWidth ? halfWidth - SizeHelper.dpToPx(32) : halfWidth;
        }
        if (isTabletLayout() && !isPreferencesPreview()) {
            availableWidth -= SizeHelper.dpToPx(80);
        }
        int columnCount = UiSingleton.getColumnCount(4, getContext().getResources().getBoolean(R.bool.landscape));
        if (isPreferencesPreview()) {
            columnCount = 1;
        }
        if (!SettingsSingleton.getSettings().cardFullWidth || columnCount > 1) {
            int padding = SizeHelper.dpToPx(16);
            if (isCommentMode()) {
                padding = 0;
            }
            availableWidth -= padding * (columnCount + 1);
        }
        return (int) Math.ceil((float) availableWidth / columnCount);
    }

    @Override // com.resync.app.widgets.AbstractPostPreviewView
    protected int getDefaultHeight() {
        return SizeHelper.dpToPx(SubsamplingScaleImageView.ORIENTATION_180);
    }

    @Override // com.resync.app.widgets.AbstractPostPreviewView
    protected boolean isFullHeight() {
        return isCommentMode() ? SettingsSingleton.getSettings().commentMediaFull : SettingsSingleton.getSettings().cardImageFullHeight;
    }
}
