package com.resync.app.ui.fragment_dialogs;

import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes.dex */
public class InsertImageDialogFragment_ViewBinding implements Unbinder {
    private InsertImageDialogFragment target;

    public InsertImageDialogFragment_ViewBinding(InsertImageDialogFragment insertImageDialogFragment, View view) {
        this.target = insertImageDialogFragment;
        insertImageDialogFragment.mTitle = (EditText) Utils.findRequiredViewAsType(view, R.id.dialog_insert_image_title, "field 'mTitle'", EditText.class);
        insertImageDialogFragment.mUrl = (EditText) Utils.findRequiredViewAsType(view, R.id.dialog_insert_image_url, "field 'mUrl'", EditText.class);
    }

    @Override
    public void unbind() {
        InsertImageDialogFragment target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.mTitle = null;
        target.mUrl = null;
    }
}
