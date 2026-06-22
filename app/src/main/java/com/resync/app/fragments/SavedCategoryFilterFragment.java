package com.resync.app.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.resync.app.ui.fragment_dialogs.SavedNewCategoryFilterFragment;
import com.resync.app.helpers.ui.AlertDialogHelper;
import org.mp4parser.boxes.iso14496.part12.SubSampleInformationBox;
import com.resync.app.events.SavedFilterCategoryEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.managers.SaveManager;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: r9.h */
/* JADX INFO: compiled from: SavedCategoryFilterFragment.java */
/* JADX INFO: loaded from: classes.dex */
public class SavedCategoryFilterFragment extends AppCompatDialogFragment {
    int selectedCategoryIndex = -1;

    /* JADX INFO: renamed from: r9.h$a */
    /* JADX INFO: compiled from: SavedCategoryFilterFragment.java */
    class SelectCategoryListener implements DialogInterface.OnClickListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String[] categories;

        SelectCategoryListener(String[] strArr) {
            this.categories = strArr;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            BusSingleton.getBus().post(new SavedFilterCategoryEvent(this.categories[which]));
        }
    }

    /* JADX INFO: renamed from: r9.h$b */
    /* JADX INFO: compiled from: SavedCategoryFilterFragment.java */
    class SingleChoiceListener implements DialogInterface.OnClickListener {
        SingleChoiceListener() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            SavedCategoryFilterFragment.this.selectedCategoryIndex = which;
        }
    }

    /* JADX INFO: renamed from: r9.h$c */
    /* JADX INFO: compiled from: SavedCategoryFilterFragment.java */
    class SaveListener implements DialogInterface.OnClickListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Post post;
        final /* synthetic */ String[] categories;

        SaveListener(Post post, String[] strArr) {
            this.post = post;
            this.categories = strArr;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            SavedCategoryFilterFragment savedCategoryFilterFragment = SavedCategoryFilterFragment.this;
            if (savedCategoryFilterFragment.selectedCategoryIndex == -1) {
                SaveManager.toggleSave(savedCategoryFilterFragment.getActivity(), this.post);
            } else {
                SaveManager.toggleSaveWithCategory(savedCategoryFilterFragment.getActivity(), this.post, this.categories[SavedCategoryFilterFragment.this.selectedCategoryIndex]);
            }
        }
    }

    /* JADX INFO: renamed from: r9.h$d */
    /* JADX INFO: compiled from: SavedCategoryFilterFragment.java */
    class NewCategoryListener implements DialogInterface.OnClickListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Post post;

        NewCategoryListener(Post post) {
            this.post = post;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            SavedNewCategoryFilterFragment.newInstance(this.post).show(SavedCategoryFilterFragment.this.getActivity().getSupportFragmentManager(), "saved_new_category_filter_fragment");
            SavedCategoryFilterFragment.this.dismissQuietly();
        }
    }

    public static SavedCategoryFilterFragment newInstance(String[] strArr, Post post) {
        SavedCategoryFilterFragment savedCategoryFilterFragment = new SavedCategoryFilterFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(SubSampleInformationBox.TYPE, strArr);
        if (post != null) {
            bundle.putSerializable("Post", post);
        }
        savedCategoryFilterFragment.setArguments(bundle);
        return savedCategoryFilterFragment;
    }

    @Override // androidx.appcompat.app.AppCompatDialogFragment, androidx.fragment.app.c
    public Dialog onCreateDialog(Bundle bundle) {
        String[] stringArray = getArguments().getStringArray(SubSampleInformationBox.TYPE);
        Post post = getArguments().containsKey("Post") ? (Post) getArguments().getSerializable("Post") : null;
        AlertDialog.Builder aVarQ = AlertDialogHelper.newBuilder(getActivity()).setCancelable(true).setTitle("Filter saved by category");
        if (stringArray == null || stringArray.length == 0) {
            aVarQ.setMessage("You currently have no categories");
        } else {
            aVarQ.setItems(stringArray, new SelectCategoryListener(stringArray));
        }
        if (post != null) {
            aVarQ.setSingleChoiceItems(stringArray, this.selectedCategoryIndex, new SingleChoiceListener());
            aVarQ.setTitle("Select a category");
            aVarQ.setPositiveButton("Save", new SaveListener(post, stringArray));
            aVarQ.setNegativeButton("Cancel", null);
            aVarQ.setNeutralButton("New", new NewCategoryListener(post));
        }
        return aVarQ.create();
    }

    protected void dismissQuietly() {
        try {
            dismiss();
        } catch (Exception unused) {
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        dismissQuietly();
        super.onDestroyView();
    }
}
