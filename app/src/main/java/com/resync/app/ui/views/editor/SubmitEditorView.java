package com.resync.app.ui.views.editor;

import android.content.Context;
import android.util.AttributeSet;
import com.resync.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class SubmitEditorView extends EditorView {
    public SubmitEditorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.resync.app.ui.views.editor.EditorView
    protected int getLayoutRes() {
        return R.layout.view_editor_submit;
    }
}
