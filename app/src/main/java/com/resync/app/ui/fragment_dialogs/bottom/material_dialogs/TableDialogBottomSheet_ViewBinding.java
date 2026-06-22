package com.resync.app.ui.fragment_dialogs.bottom.material_dialogs;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.text.spannable.children.HtmlTableView;

/* JADX INFO: loaded from: classes.dex */
public class TableDialogBottomSheet_ViewBinding implements Unbinder {
    private TableDialogBottomSheet target;

    public TableDialogBottomSheet_ViewBinding(TableDialogBottomSheet tableDialogBottomSheet, View view) {
        this.target = tableDialogBottomSheet;
        tableDialogBottomSheet.mTextView = (HtmlTableView) Utils.findRequiredViewAsType(view, R.id.fragment_table_tableview, "field 'mTextView'", HtmlTableView.class);
    }

    @Override
    public void unbind() {
        TableDialogBottomSheet target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.mTextView = null;
    }
}
