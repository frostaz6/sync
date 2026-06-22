package com.resync.app.ui.activities;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.widgets.SidebarFrameLayout;
import com.resync.app.R;
import com.resync.app.ui.views.app_bars.PostsAppBarLayout;
import com.resync.app.ui.views.drawer.SyncDrawerLayout;
import com.resync.app.ui.views.main.SyncBottomNavigationView;
import com.resync.app.ui.views.main.SyncNavigationRailView;
import com.resync.app.ui.views.panel.CustomPanelLayout;
import com.resync.app.ui.views.video.VideoHelperView;

/* JADX INFO: loaded from: classes.dex */
public class MainActivity_ViewBinding implements Unbinder {
    private MainActivity target;

    public MainActivity_ViewBinding(MainActivity mainActivity, View view) {
        this.target = mainActivity;
        mainActivity.appBarLayout = (PostsAppBarLayout) Utils.findRequiredViewAsType(view, R.id.appbar, "field 'appBarLayout'", PostsAppBarLayout.class);
        mainActivity.panel = (CustomPanelLayout) Utils.findOptionalViewAsType(view, R.id.overlapping_panels, "field 'panel'", CustomPanelLayout.class);
        mainActivity.drawer = (SyncDrawerLayout) Utils.findOptionalViewAsType(view, R.id.drawer, "field 'drawer'", SyncDrawerLayout.class);
        mainActivity.sidebarLayout = (SidebarFrameLayout) Utils.findOptionalViewAsType(view, R.id.end_panel, "field 'sidebarLayout'", SidebarFrameLayout.class);
        mainActivity.bottomNavigationView = (SyncBottomNavigationView) Utils.findRequiredViewAsType(view, R.id.navigation, "field 'bottomNavigationView'", SyncBottomNavigationView.class);
        mainActivity.navigationRail = (SyncNavigationRailView) Utils.findOptionalViewAsType(view, R.id.navigation_rail, "field 'navigationRail'", SyncNavigationRailView.class);
        mainActivity.videoHelperView = (VideoHelperView) Utils.findRequiredViewAsType(view, R.id.video_helper, "field 'videoHelperView'", VideoHelperView.class);
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        MainActivity mainActivity = this.target;
        if (mainActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        mainActivity.appBarLayout = null;
        mainActivity.panel = null;
        mainActivity.drawer = null;
        mainActivity.sidebarLayout = null;
        mainActivity.bottomNavigationView = null;
        mainActivity.navigationRail = null;
        mainActivity.videoHelperView = null;
    }
}
