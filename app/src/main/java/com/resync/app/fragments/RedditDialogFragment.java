package com.resync.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;

/* JADX INFO: renamed from: r9.g */
/* JADX INFO: compiled from: RedditDialogFragment.java */
/* JADX INFO: loaded from: classes.dex */
public class RedditDialogFragment extends AppCompatDialogFragment {
    public static final String TAG = RedditDialogFragment.class.toString();

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    protected void resetOrientation() {
        if (getActivity() != null) {
            getActivity().setRequestedOrientation(2);
        }
    }

    @Override // androidx.fragment.app.c, androidx.fragment.app.Fragment
    public void onDestroyView() {
        resetOrientation();
        super.onDestroyView();
    }
}
