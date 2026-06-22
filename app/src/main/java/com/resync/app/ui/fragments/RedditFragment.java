package com.resync.app.ui.fragments;

import androidx.fragment.app.Fragment;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.views.core.MaterialYouToolbar;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.ui.interfaces.RedditFragmentInterface;
import com.resync.app.util.Logger;
import com.resync.app.util.TestUtils;

/* JADX INFO: renamed from: com.resync.app.ui.fragments.c */
/* JADX INFO: compiled from: RedditFragment.java */
/* JADX INFO: loaded from: classes2.dex */
public abstract class RedditFragment extends Fragment implements RedditFragmentInterface {
    protected MaterialYouToolbar toolbar;
    protected boolean paused;
    private int instanceId = getClass().getSimpleName().hashCode();

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        Logger.verbose("RedditFragment", "Fragment paused");
        this.paused = true;
        if (TestUtils.isRobolectric()) {
            return;
        }
        BusSingleton.getBus().unregister(this);
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: k2 */
    public void onResume() {
        super.onResume();
        Logger.verbose("RedditFragment", "Fragment resumed");
        this.paused = false;
        if (TestUtils.isRobolectric()) {
            return;
        }
        BusSingleton.getBus().register(this);
    }

    public MaterialYouToolbar getToolbar() {
        MaterialYouToolbar materialYouToolbar = this.toolbar;
        if (materialYouToolbar != null) {
            return materialYouToolbar;
        }
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getToolbar();
        }
        return null;
    }

    public boolean isAttached() {
        return (!isAdded() || getActivity() == null || isRemoving()) ? false : true;
    }
}
