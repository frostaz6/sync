package com.resync.app.ui.fragment_dialogs;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.MaterialYouToolbar;
import com.resync.app.ui.views.editor.EditorView;
import com.resync.app.ui.views.responsive.CustomEditText;

/* JADX INFO: loaded from: classes.dex */
public class EditFragment_ViewBinding implements Unbinder {
    private EditFragment target;

    public EditFragment_ViewBinding(EditFragment editFragment, View view) {
        this.target = editFragment;
        editFragment.mComposeToolbar = (MaterialYouToolbar) Utils.findRequiredViewAsType(view, R.id.toolbar, "field 'mComposeToolbar'", MaterialYouToolbar.class);
        editFragment.mToolbarButtons = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.fragment_edit_toolbar_buttons, "field 'mToolbarButtons'", LinearLayout.class);
        editFragment.mDiscardButton = (FrameLayout) Utils.findRequiredViewAsType(view, R.id.fragment_edit_toolbar_buttons_discard, "field 'mDiscardButton'", FrameLayout.class);
        editFragment.mDiscardButtonTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.fragment_edit_toolbar_buttons_discard_textview, "field 'mDiscardButtonTextView'", TextView.class);
        editFragment.mDoneButton = (FrameLayout) Utils.findRequiredViewAsType(view, R.id.fragment_edit_toolbar_buttons_done, "field 'mDoneButton'", FrameLayout.class);
        editFragment.mDoneButtonTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.fragment_edit_toolbar_buttons_done_textview, "field 'mDoneButtonTextView'", TextView.class);
        editFragment.mUsernameInput = (CustomEditText) Utils.findRequiredViewAsType(view, R.id.fragment_edit_username, "field 'mUsernameInput'", CustomEditText.class);
        editFragment.mSubjectInput = (CustomEditText) Utils.findRequiredViewAsType(view, R.id.fragment_edit_subject, "field 'mSubjectInput'", CustomEditText.class);
        editFragment.mEditorView = (EditorView) Utils.findRequiredViewAsType(view, R.id.editor_view, "field 'mEditorView'", EditorView.class);
    }

    @Override
    public void unbind() {
        EditFragment target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.mComposeToolbar = null;
        target.mToolbarButtons = null;
        target.mDiscardButton = null;
        target.mDiscardButtonTextView = null;
        target.mDoneButton = null;
        target.mDoneButtonTextView = null;
        target.mUsernameInput = null;
        target.mSubjectInput = null;
        target.mEditorView = null;
    }
}
