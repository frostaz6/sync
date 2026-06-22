package com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.monet.buttons.MonetActionButtons;
import com.resync.app.ui.views.text.BaseTextView;

/* JADX INFO: loaded from: classes.dex */
public class AbstractMemeBottomSheetFragment_ViewBinding implements Unbinder {
    private AbstractMemeBottomSheetFragment target;

    public AbstractMemeBottomSheetFragment_ViewBinding(AbstractMemeBottomSheetFragment abstractMemeBottomSheetFragment, View view) {
        this.target = abstractMemeBottomSheetFragment;
        abstractMemeBottomSheetFragment.headerView = (SheetHeaderView) Utils.findRequiredViewAsType(view, R.id.header, "field 'headerView'", SheetHeaderView.class);
        abstractMemeBottomSheetFragment.buttons = (MonetActionButtons) Utils.findRequiredViewAsType(view, R.id.monet_action_buttons, "field 'buttons'", MonetActionButtons.class);
        abstractMemeBottomSheetFragment.iv = (ImageView) Utils.findRequiredViewAsType(view, R.id.image, "field 'iv'", ImageView.class);
        abstractMemeBottomSheetFragment.textView = (BaseTextView) Utils.findRequiredViewAsType(view, R.id.text, "field 'textView'", BaseTextView.class);
        abstractMemeBottomSheetFragment.progressBar = (ProgressBar) Utils.findRequiredViewAsType(view, R.id.image_progress_bar, "field 'progressBar'", ProgressBar.class);
    }

    @Override
    public void unbind() {
        AbstractMemeBottomSheetFragment target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.headerView = null;
        target.buttons = null;
        target.iv = null;
        target.textView = null;
        target.progressBar = null;
    }
}
