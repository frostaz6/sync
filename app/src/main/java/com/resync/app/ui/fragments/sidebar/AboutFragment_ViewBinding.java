package com.resync.app.ui.fragments.sidebar;

import android.view.View;
import androidx.core.widget.NestedScrollView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.chips.ContextualChipsDropInView;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.text.spannable.children.HtmlTableView;

/* JADX INFO: loaded from: classes2.dex */
public class AboutFragment_ViewBinding implements Unbinder {
    private AboutFragment target;

    public AboutFragment_ViewBinding(AboutFragment aboutFragment, View view) {
        this.target = aboutFragment;
        aboutFragment.mScrollView = (NestedScrollView) Utils.findRequiredViewAsType(view, R.id.scrollview, "field 'mScrollView'", NestedScrollView.class);
        aboutFragment.mHeader = (SheetHeaderView) Utils.findRequiredViewAsType(view, R.id.header, "field 'mHeader'", SheetHeaderView.class);
        aboutFragment.mChips = (ContextualChipsDropInView) Utils.findRequiredViewAsType(view, R.id.contextual_chips, "field 'mChips'", ContextualChipsDropInView.class);
        aboutFragment.mContent = (HtmlTableView) Utils.findRequiredViewAsType(view, R.id.content, "field 'mContent'", HtmlTableView.class);
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        AboutFragment aboutFragment = this.target;
        if (aboutFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        aboutFragment.mScrollView = null;
        aboutFragment.mHeader = null;
        aboutFragment.mChips = null;
        aboutFragment.mContent = null;
    }
}
