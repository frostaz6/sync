package com.resync.app.fragments;

import android.view.View;
import androidx.fragment.app.DialogFragment;
import butterknife.ButterKnife;

/* JADX INFO: renamed from: la.a */
/* JADX INFO: compiled from: AbstractOnboardingNavigationFragment.java */
/* JADX INFO: loaded from: classes.dex */
public class AbstractOnboardingNavigationFragment extends DialogFragment {
    public int getLayoutResId() {
        throw new RuntimeException("Must be called");
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: q1 */
    public View getView() {
        View viewInflate = View.inflate(getActivity(), getLayoutResId(), null);
        ButterKnife.bind(this, viewInflate);
        return viewInflate;
    }
}
