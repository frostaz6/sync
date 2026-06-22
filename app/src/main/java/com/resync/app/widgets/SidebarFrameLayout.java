package com.resync.app.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.R;
import com.resync.app.discord.OverlappingPanelsLayout;
import com.resync.app.ui.fragments.sidebar.AboutFragment;
import com.resync.app.ui.fragments.sidebar.FriendsAboutFragment;
import com.resync.app.ui.fragments.sidebar.MultiAboutFragment;
import com.resync.app.ui.views.drawer.SyncDrawerLayout;
import com.resync.app.ui.views.panel.CustomPanelLayout;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.ui.interfaces.DarkModeAware;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: cc.b */
/* JADX INFO: compiled from: SidebarFrameLayout.java */
/* JADX INFO: loaded from: classes3.dex */
public class SidebarFrameLayout extends FrameLayout implements DarkModeAware {

    String currentSubreddit;

    public SidebarFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        lockPanel();
    }

    private FragmentManager getFragmentManager() {
        return ContextHelper.getFragmentManager(getContext());
    }

    private void lockPanel() {
        if (getParent() instanceof CustomPanelLayout) {
            ((CustomPanelLayout) getParent()).setEndPanelLockState(OverlappingPanelsLayout.LockState.CLOSE);
        } else if (getParent() instanceof SyncDrawerLayout) {
            ((SyncDrawerLayout) getParent()).setDrawerLockMode(1, 8388613);
        }
    }

    private void unlockPanel() {
        if (getParent() instanceof CustomPanelLayout) {
            ((CustomPanelLayout) getParent()).setEndPanelLockState(OverlappingPanelsLayout.LockState.UNLOCKED);
        } else if (getParent() instanceof SyncDrawerLayout) {
            ((SyncDrawerLayout) getParent()).setDrawerLockMode(0, 8388613);
        }
    }

    public void clear() {
        Fragment fragmentFindFragmentById = getFragmentManager().findFragmentById(R.id.end_panel);
        if (fragmentFindFragmentById != null) {
            getFragmentManager().beginTransaction().remove(fragmentFindFragmentById).commit();
        }
        this.currentSubreddit = null;
    }

    public void update(String str) {
        update(str, false);
    }

    public void update(String str, boolean z) {
        if (StringUtils.equalsAnyIgnoreCase(this.currentSubreddit, str) && !z) {
            Logger.verbose("UPDATING: BAILED WAS SAME");
            return;
        }
        Logger.verbose("UPDATING: " + str);
        this.currentSubreddit = str;
        if (MaterialPostsHelper.isStandardSubreddit(str) || MaterialPostsHelper.isUserProfile(str)) {
            Fragment fragmentFindFragmentById = getFragmentManager().findFragmentById(R.id.end_panel);
            if (fragmentFindFragmentById instanceof AboutFragment) {
                ((AboutFragment) fragmentFindFragmentById).setSubreddit(str);
            } else {
                getFragmentManager().beginTransaction().replace(R.id.end_panel, AboutFragment.newInstance(str, true)).commit();
            }
            unlockPanel();
            return;
        }
        if (MaterialPostsHelper.isFriends(str)) {
            getFragmentManager().beginTransaction().replace(R.id.end_panel, FriendsAboutFragment.newInstance(str)).commit();
            unlockPanel();
        } else if (MaterialPostsHelper.isMultireddit(str)) {
            getFragmentManager().beginTransaction().replace(R.id.end_panel, MultiAboutFragment.newInstance(str)).commit();
            unlockPanel();
        } else {
            clear();
            lockPanel();
        }
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        update(this.currentSubreddit, true);
    }
}
