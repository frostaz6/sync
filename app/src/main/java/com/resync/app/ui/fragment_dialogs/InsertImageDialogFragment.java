package com.resync.app.ui.fragment_dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.ui.views.editor.EditorView;
import com.resync.app.helpers.ui.AlertDialogHelper;

/* JADX INFO: loaded from: classes.dex */
public class InsertImageDialogFragment extends AppCompatDialogFragment {
    public static final String TAG = InsertImageDialogFragment.class.getSimpleName();
    private EditorView mEditorView;

    @BindView(R.id.dialog_insert_image_title)
    EditText mTitle;

    @BindView(R.id.dialog_insert_image_url)
    EditText mUrl;

    class InsertClickListener implements DialogInterface.OnClickListener {
        InsertClickListener() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            String str;
            String string = InsertImageDialogFragment.this.mTitle.getText().toString();
            String string2 = InsertImageDialogFragment.this.mUrl.getText().toString();
            if (TextUtils.isEmpty(string)) {
                str = "[" + string2 + "](" + string2 + ")";
            } else {
                str = "[" + string + "](" + string2 + ")";
            }
            if (InsertImageDialogFragment.this.mEditorView != null) {
                InsertImageDialogFragment.this.mEditorView.insertText(str);
            }
        }
    }

    public static InsertImageDialogFragment newInstance(String str) {
        InsertImageDialogFragment insertImageDialogFragment = new InsertImageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", str);
        insertImageDialogFragment.setArguments(bundle);
        return insertImageDialogFragment;
    }

    @Override // androidx.appcompat.app.AppCompatDialogFragment, androidx.fragment.app.c
    public Dialog onCreateDialog(Bundle bundle) {
        View viewInflate = View.inflate(getActivity(), R.layout.dialog_insert_image, null);
        ButterKnife.bind(this, viewInflate);
        this.mUrl.setText(getArguments().getString("url"));
        return AlertDialogHelper.newBuilder(getContext()).setMessage("Insert image").setPositiveButton("Insert", new InsertClickListener()).setNegativeButton("Cancel", null).setView(viewInflate).create();
    }

    public void setEditorView(EditorView editorView) {
        this.mEditorView = editorView;
    }
}
