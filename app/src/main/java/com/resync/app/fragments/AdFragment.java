package com.resync.app.fragments;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.resync.app.R;
import com.resync.app.ui.fragments.RedditFragment;
import com.resync.app.helpers.ui.ImageViewerHelper;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: ma.a */
/* JADX INFO: compiled from: AdFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public class AdFragment extends RedditFragment {

    /* JADX INFO: renamed from: s0, reason: collision with root package name */
    static String TAG = AdFragment.class.getSimpleName();
    private FrameLayout adContainer;

    private void clearAdContainer() {
        this.adContainer.removeAllViews();
        ContextHelper.isActivityFinishing(getActivity());
    }

    public static AdFragment newInstance() {
        return new AdFragment();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_gallery_ad, (ViewGroup) null);
    }

    @Override // com.resync.app.ui.interfaces.RedditFragmentInterface
    public int getLayoutResId() {
        return R.id.fragment_ad_wrapper;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.adContainer = (FrameLayout) view.findViewById(getLayoutResId());
        Pair<Integer, Integer> pairA = ImageViewerHelper.getActionBarInsets(getActivity());
        ((RelativeLayout.LayoutParams) this.adContainer.getLayoutParams()).topMargin = ((Integer) pairA.first).intValue();
        ((RelativeLayout.LayoutParams) this.adContainer.getLayoutParams()).bottomMargin = ((Integer) pairA.second).intValue();
        clearAdContainer();
    }

    public void onAdHidden() {
        Logger.verbose(TAG, "Amazon Ad hidden");
    }

    public void onAdShown() {
        Logger.verbose(TAG, "Amazon Ad shown");
    }
}
