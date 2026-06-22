package com.resync.app.ui.views.chips;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.sections.posts.toolbar.chip_groups.ChipWrapper;

/* JADX INFO: loaded from: classes3.dex */
public class ContextualChipsDropInView_ViewBinding implements Unbinder {
    private ContextualChipsDropInView target;

    public ContextualChipsDropInView_ViewBinding(ContextualChipsDropInView contextualChipsDropInView, View view) {
        this.target = contextualChipsDropInView;
        contextualChipsDropInView.chipWrapper = (ChipWrapper) Utils.findRequiredViewAsType(view, R.id.chip_wrapper, "field 'chipWrapper'", ChipWrapper.class);
    }

    @Override
    public void unbind() {
        ContextualChipsDropInView contextualChipsDropInView = this.target;
        if (contextualChipsDropInView == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        contextualChipsDropInView.chipWrapper = null;
    }
}
