package com.resync.app.ui.fragment_dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import androidx.appcompat.app.AppCompatDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.helpers.ui.AlertDialogHelper;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.managers.SaveManager;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes.dex */
public class SavedNewCategoryFilterFragment extends AppCompatDialogFragment {

    @BindView(R.id.fragment_new_category)
    AutoCompleteTextView categoryInput;

    class SaveClickListener implements DialogInterface.OnClickListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Post post;

        SaveClickListener(Post post) {
            this.post = post;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            String strTrim = SavedNewCategoryFilterFragment.this.categoryInput.getText().toString().trim();
            if (TextUtils.isEmpty(strTrim)) {
                SaveManager.toggleSave(SavedNewCategoryFilterFragment.this.getActivity(), this.post);
            } else {
                SaveManager.toggleSaveWithCategory(SavedNewCategoryFilterFragment.this.getActivity(), this.post, strTrim);
            }
            SavedNewCategoryFilterFragment.this.dismissSafely();
        }
    }

    public static SavedNewCategoryFilterFragment newInstance(Post post) {
        SavedNewCategoryFilterFragment savedNewCategoryFilterFragment = new SavedNewCategoryFilterFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Post", post);
        savedNewCategoryFilterFragment.setArguments(bundle);
        return savedNewCategoryFilterFragment;
    }

    @Override // androidx.appcompat.app.AppCompatDialogFragment, androidx.fragment.app.c
    public Dialog onCreateDialog(Bundle bundle) {
        Post post = (Post) getArguments().getSerializable("Post");
        View viewInflate = View.inflate(getActivity(), R.layout.fragment_new_category_filter, null);
        ButterKnife.bind(this, viewInflate);
        this.categoryInput.setTextColor(StyleSingleton.getTextColorAuto(RedditApplication.getAppContext(), false));
        this.categoryInput.setHintTextColor(MonetUtil.getSecondaryTextColorAuto());
        androidx.appcompat.app.AlertDialog dialog = AlertDialogHelper.newBuilder(getActivity()).setView(viewInflate).setCancelable(true).setTitle("Enter a new category name").setPositiveButton("Save", new SaveClickListener(post)).setNegativeButton("Cancel", null).create();
        dialog.getWindow().setSoftInputMode(20);
        return dialog;
    }

    protected void dismissSafely() {
        try {
            dismiss();
        } catch (Exception unused) {
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        dismissSafely();
        super.onPause();
    }
}
