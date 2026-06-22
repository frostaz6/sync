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
public class CasualActivity_ViewBinding implements Unbinder {
    private CasualActivity target;

    public CasualActivity_ViewBinding(CasualActivity casualActivity, View view) {
        this.target = casualActivity;
        casualActivity.appBarLayout = (PostsAppBarLayout) Utils.findRequiredViewAsType(view, R.id.appbar, "field 'appBarLayout'", PostsAppBarLayout.class);
        casualActivity.panel = (CustomPanelLayout) Utils.findOptionalViewAsType(view, R.id.overlapping_panels, "field 'panel'", CustomPanelLayout.class);
        casualActivity.drawer = (SyncDrawerLayout) Utils.findOptionalViewAsType(view, R.id.drawer, "field 'drawer'", SyncDrawerLayout.class);
        casualActivity.sidebarLayout = (SidebarFrameLayout) Utils.findOptionalViewAsType(view, R.id.end_panel, "field 'sidebarLayout'", SidebarFrameLayout.class);
        casualActivity.bottomNavigationView = (SyncBottomNavigationView) Utils.findRequiredViewAsType(view, R.id.navigation, "field 'bottomNavigationView'", SyncBottomNavigationView.class);
        casualActivity.navigationRail = (SyncNavigationRailView) Utils.findOptionalViewAsType(view, R.id.navigation_rail, "field 'navigationRail'", SyncNavigationRailView.class);
        casualActivity.videoHelperView = (VideoHelperView) Utils.findRequiredViewAsType(view, R.id.video_helper, "field 'videoHelperView'", VideoHelperView.class);
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        CasualActivity casualActivity = this.target;
        if (casualActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        casualActivity.appBarLayout = null;
        casualActivity.panel = null;
        casualActivity.drawer = null;
        casualActivity.sidebarLayout = null;
        casualActivity.bottomNavigationView = null;
        casualActivity.navigationRail = null;
        casualActivity.videoHelperView = null;
    }
}
