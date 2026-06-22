package com.resync.app.ui.views.comments.tabbed;

import android.content.Context;
import android.util.AttributeSet;
import com.resync.app.ui.views.util.ConsumingHorizontalScrollView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class CommentsTabbedScrollView extends ConsumingHorizontalScrollView implements DarkModeAware {
    public CommentsTabbedScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyColors();
    }

    private void applyColors() {
        setBackgroundColor(MonetUtil.getToolbarColorAuto());
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }
}
