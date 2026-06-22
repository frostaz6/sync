package com.resync.app.ui.views.drawer;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.resync.app.widgets.PanelClosedListener;
import com.resync.app.R;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.activities.CasualActivity;
import com.resync.app.ui.activities.MessagingActivity;
import com.resync.app.ui.activities.ModActivity;
import com.resync.app.ui.activities.PreferencesActivity;
import com.resync.app.ui.fragments.DrawerFragment;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.events.SubredditSelectedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.CloseDrawerEvent;
import com.resync.app.events.DrawerOpenMessagingEvent;
import com.resync.app.events.DrawerOpenModEvent;
import com.resync.app.events.DrawerOpenProfileEvent;
import com.resync.app.events.DrawerOpenSettingsEvent;
import com.resync.app.events.DrawerOpenSubmitEvent;
import com.resync.app.events.OpenDrawerEvent;
import com.resync.app.events.OpenMessagingSection;
import com.resync.app.events.OpenModSection;
import com.resync.app.events.OpenProfileSection;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.dialogs.SubmitSelectionDialogBottomSheet;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes3.dex */
public class SyncDrawerLayout extends DrawerLayout {

    class PanelClosedHandler implements PanelClosedListener {
        PanelClosedHandler() {
        }

        @Override // com.resync.app.widgets.PanelClosedListener
        public void onPanelClosed() {
            Fragment fragmentFindFragmentById = ContextHelper.getFragmentManager(SyncDrawerLayout.this.getContext()).findFragmentById(R.id.start_panel);
            if (fragmentFindFragmentById instanceof DrawerFragment) {
                Logger.verbose("DRAWER", "Hiding fragment");
                ((DrawerFragment) fragmentFindFragmentById).onDrawerClosed();
            }
        }
    }

    class CloseRunnable implements Runnable {
        CloseRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            SyncDrawerLayout.this.closeStart();
        }
    }

    class DrawerListener extends DrawerLayout.SimpleDrawerListener {

        final /* synthetic */ PanelClosedListener panelClosedListener;

        DrawerListener(PanelClosedListener panelClosedListener) {
            this.panelClosedListener = panelClosedListener;
        }

        @Override // androidx.drawerlayout.widget.DrawerLayout.DrawerListener
        public void onDrawerClosed(View view) {
            PanelClosedListener panelClosedListener = this.panelClosedListener;
            if (panelClosedListener != null) {
                panelClosedListener.onPanelClosed();
            }
        }
    }

    public SyncDrawerLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setPanelClosedListener(new PanelClosedHandler());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onSubmitClicked() {
        if (AccountSingleton.getInstance().isLoggedIn()) {
            SubmitSelectionDialogBottomSheet.show(getContext());
        } else {
            StaticToast.show(getContext(), R.string.common_generic_error_logged_out);
        }
    }

    public void setPanelClosedListener(PanelClosedListener panelClosedListener) {
        addDrawerListener(new DrawerListener(panelClosedListener));
    }

    public void closeDelayed() {
        postDelayed(new CloseRunnable(), 600L);
    }

    public void closeStart() {
        try {
            closeDrawer(3);
        } catch (Exception unused) {
        }
    }

    public void closeStartThen(Runnable runnable) {
        closeStart();
        postDelayed(runnable, 280L);
    }

    public void closeEnd() {
        try {
            closeDrawer(5);
        } catch (Exception unused) {
        }
    }

    public boolean isStartOpen() {
        return isDrawerOpen(3);
    }

    public boolean isEndOpen() {
        return isDrawerOpen(5);
    }

    public void openStart() {
        try {
            openDrawer(3);
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    public void openEnd() {
        try {
            openDrawer(5);
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    @Override // androidx.drawerlayout.widget.DrawerLayout, android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BusSingleton.getBus().register(this);
    }

    @Subscribe
    public void onCloseDrawerEvent(CloseDrawerEvent closeDrawerEvent) {
        closeStart();
    }

    @Override // androidx.drawerlayout.widget.DrawerLayout, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        BusSingleton.getBus().unregister(this);
        super.onDetachedFromWindow();
    }

    @Subscribe
    public void onDrawerOpenMessaging(DrawerOpenMessagingEvent drawerOpenMessagingEvent) {
        if (!AccountSingleton.getInstance().isLoggedIn()) {
            StaticToast.show(getContext(), R.string.common_generic_error_logged_out);
        } else {
            MessagingActivity.start(getContext());
            closeDelayed();
        }
    }

    @Subscribe
    public void onDrawerOpenMod(DrawerOpenModEvent drawerOpenModEvent) {
        if (AccountSingleton.getInstance().isLoggedIn()) {
            ModActivity.start(getContext());
            closeDelayed();
        } else {
            StaticToast.show(getContext(), R.string.common_generic_error_logged_out);
            closeStart();
        }
    }

    @Subscribe
    public void onDrawerOpenProfile(DrawerOpenProfileEvent drawerOpenProfileEvent) {
        if (AccountSingleton.getInstance().isLoggedIn()) {
            CasualActivity.openUser(getContext(), AccountSingleton.getInstance().getUsername());
            closeDelayed();
        } else {
            StaticToast.show(getContext(), R.string.common_generic_error_logged_out);
            closeStart();
        }
    }

    @Subscribe
    public void onDrawerOpenSettings(DrawerOpenSettingsEvent drawerOpenSettingsEvent) {
        closeDelayed();
        getContext().startActivity(new Intent(getContext(), (Class<?>) PreferencesActivity.class));
    }

    @Subscribe
    public void onDrawerOpenSubmit(DrawerOpenSubmitEvent drawerOpenSubmitEvent) {
        closeStartThen(new Runnable() { // from class: cc.c
            @Override // java.lang.Runnable
            public final void run() {
                SyncDrawerLayout.this.onSubmitClicked();
            }
        });
    }

    @Subscribe
    public void onOpenDrawerEvent(OpenDrawerEvent openDrawerEvent) {
        openStart();
    }

    @Subscribe
    public void onSubredditSelected(SubredditSelectedEvent subredditSelectedEvent) {
        closeStart();
    }

    @Subscribe
    public void openDrawerProfileSection(OpenProfileSection openProfileSection) {
        if (StringUtils.equals("Friends", openProfileSection.section)) {
            CasualActivity.openSubreddit(getContext(), "Friends");
        } else if (StringUtils.equals("Watching", openProfileSection.section)) {
            CasualActivity.openWatching(getContext());
        } else {
            CasualActivity.openUserSection(getContext(), AccountSingleton.getInstance().getUsername(), openProfileSection.section);
        }
        closeDelayed();
    }

    @Subscribe
    public void openModSection(OpenModSection openModSection) {
        ModActivity.startSection(getContext(), openModSection.section);
        closeDelayed();
    }

    @Subscribe
    public void openProfileSection(OpenMessagingSection openMessagingSection) {
        MessagingActivity.startSection(getContext(), openMessagingSection.section);
        closeDelayed();
    }
}
