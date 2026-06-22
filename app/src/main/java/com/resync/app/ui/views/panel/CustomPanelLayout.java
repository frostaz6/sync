package com.resync.app.ui.views.panel;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.WindowInsets;
import androidx.fragment.app.Fragment;
import com.resync.app.R;
import com.resync.app.discord.OverlappingPanelsLayout;
import com.resync.app.discord.PanelState;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.activities.CasualActivity;
import com.resync.app.ui.activities.MessagingActivity;
import com.resync.app.ui.activities.ModActivity;
import com.resync.app.ui.activities.PreferencesActivity;
import com.resync.app.ui.fragments.DrawerFragment;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.ObjectUtils;
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
public class CustomPanelLayout extends OverlappingPanelsLayout {

    class StartPanelStateListener implements OverlappingPanelsLayout.PanelStateListener {
        StartPanelStateListener() {
        }

        @Override // com.resync.app.discord.OverlappingPanelsLayout.PanelStateListener
        public void onPanelStateChange(PanelState panelState) {
            if (ObjectUtils.equals(panelState, PanelState.Closed.INSTANCE)) {
                Fragment fragmentFindFragmentById = ContextHelper.getFragmentManager(CustomPanelLayout.this.getContext()).findFragmentById(com.resync.app.R.id.start_panel);
                if (fragmentFindFragmentById instanceof DrawerFragment) {
                    Logger.verbose("DRAWER", "Hiding fragment");
                    ((DrawerFragment) fragmentFindFragmentById).onDrawerClosed();
                }
            }
        }
    }

    class CloseRunnable implements Runnable {
        CloseRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            CustomPanelLayout.this.closePanels();
        }
    }

    public CustomPanelLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFitsSystemWindows(false);
        registerStartPanelStateListeners(new StartPanelStateListener());
        setEndPanelLockState(OverlappingPanelsLayout.LockState.UNLOCKED);
    }

    public void closePanelsDelayed() {
        postDelayed(new CloseRunnable(), 600L);
    }

    public boolean isPanelOpen() {
        PanelState panelStateT = getStartPanelState();
        PanelState.Opened opened = PanelState.Opened.INSTANCE;
        return panelStateT == opened || getEndPanelState() == opened;
    }

    @Override // android.view.View
    @TargetApi(20)
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        int childCount = getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            getChildAt(childIndex).dispatchApplyWindowInsets(windowInsets);
        }
        return windowInsets;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BusSingleton.getBus().register(this);
    }

    @Subscribe
    public void onCloseDrawerEvent(CloseDrawerEvent closeDrawerEvent) {
        closePanels();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        BusSingleton.getBus().unregister(this);
        super.onDetachedFromWindow();
    }

    @Subscribe
    public void onDrawerOpenMessaging(DrawerOpenMessagingEvent drawerOpenMessagingEvent) {
        if (!AccountSingleton.getInstance().isLoggedIn()) {
            StaticToast.show(getContext(), com.resync.app.R.string.common_generic_error_logged_out);
        } else {
            MessagingActivity.start(getContext());
            closePanelsDelayed();
        }
    }

    @Subscribe
    public void onDrawerOpenMod(DrawerOpenModEvent drawerOpenModEvent) {
        if (AccountSingleton.getInstance().isLoggedIn()) {
            ModActivity.start(getContext());
            closePanelsDelayed();
        } else {
            StaticToast.show(getContext(), com.resync.app.R.string.common_generic_error_logged_out);
            closePanels();
        }
    }

    @Subscribe
    public void onDrawerOpenProfile(DrawerOpenProfileEvent drawerOpenProfileEvent) {
        if (AccountSingleton.getInstance().isLoggedIn()) {
            CasualActivity.openUser(getContext(), AccountSingleton.getInstance().getUsername());
            closePanelsDelayed();
        } else {
            StaticToast.show(getContext(), com.resync.app.R.string.common_generic_error_logged_out);
            closePanels();
        }
    }

    @Subscribe
    public void onDrawerOpenSettings(DrawerOpenSettingsEvent drawerOpenSettingsEvent) {
        closePanelsDelayed();
        getContext().startActivity(new Intent(getContext(), (Class<?>) PreferencesActivity.class));
    }

    @Subscribe
    public void onDrawerOpenSubmit(DrawerOpenSubmitEvent drawerOpenSubmitEvent) {
        if (!AccountSingleton.getInstance().isLoggedIn()) {
            StaticToast.show(getContext(), com.resync.app.R.string.common_generic_error_logged_out);
        } else {
            SubmitSelectionDialogBottomSheet.show(getContext());
            closePanels();
        }
    }

    @Subscribe
    public void onOpenDrawerEvent(OpenDrawerEvent openDrawerEvent) {
        openStartPanel();
    }

    @Subscribe
    public void onSubredditSelected(SubredditSelectedEvent subredditSelectedEvent) {
        closePanels();
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
        closePanels();
    }

    @Subscribe
    public void openModSection(OpenModSection openModSection) {
        ModActivity.startSection(getContext(), openModSection.section);
        closePanelsDelayed();
    }

    @Subscribe
    public void openProfileSection(OpenMessagingSection openMessagingSection) {
        MessagingActivity.startSection(getContext(), openMessagingSection.section);
        closePanelsDelayed();
    }
}
