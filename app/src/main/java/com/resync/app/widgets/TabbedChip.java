package com.resync.app.widgets;

import android.content.Context;
import com.resync.app.ui.views.responsive.chips.ContentChip;

/* JADX INFO: renamed from: zb.a */
/* JADX INFO: compiled from: TabbedChip.java */
/* JADX INFO: loaded from: classes3.dex */
public class TabbedChip extends ContentChip {
    public TabbedChip(Context context) {
        super(context);
    }

    @Override // com.resync.app.ui.views.responsive.chips.ContentChip
    protected boolean shouldTintCloseIcon() {
        return false;
    }
}
