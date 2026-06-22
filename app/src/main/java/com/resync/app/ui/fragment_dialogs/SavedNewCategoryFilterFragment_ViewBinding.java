package com.resync.app.ui.fragment_dialogs;

import android.view.View;
import android.widget.AutoCompleteTextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes.dex */
public class SavedNewCategoryFilterFragment_ViewBinding implements Unbinder {
    private SavedNewCategoryFilterFragment target;

    public SavedNewCategoryFilterFragment_ViewBinding(SavedNewCategoryFilterFragment savedNewCategoryFilterFragment, View view) {
        this.target = savedNewCategoryFilterFragment;
        savedNewCategoryFilterFragment.categoryInput = (AutoCompleteTextView) Utils.findRequiredViewAsType(view, R.id.fragment_new_category, "field 'categoryInput'", AutoCompleteTextView.class);
    }

    @Override
    public void unbind() {
        SavedNewCategoryFilterFragment target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.categoryInput = null;
    }
}
