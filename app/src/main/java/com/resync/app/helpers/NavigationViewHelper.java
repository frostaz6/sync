package com.resync.app.helpers;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import com.resync.app.R;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.PreferencesActivity;
import com.resync.app.ui.fragment_dialogs.EditFragment;
import com.resync.app.ui.fragment_dialogs.bottom.ActionsBottomSheetFragment;
import com.resync.app.ui.fragment_dialogs.bottom.SubredditsBottomSheetFragment;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.events.StopScrollEvent;
import com.resync.app.events.SubredditSelectedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.ui.interfaces.PostHost;

/* JADX INFO: renamed from: hc.a */
/* JADX INFO: compiled from: NavigationViewHelper.java */
/* JADX INFO: loaded from: classes3.dex */
public final class NavigationViewHelper {
    public static boolean onDrawerItemSelected(Context context, int itemId) {
        if (itemId == R.id.menu_posts) {
            BusSingleton.getBus().post(new SubredditSelectedEvent(SettingsSingleton.getSettings().defaultSub));
            return true;
        }
        if (itemId == R.id.menu_actions) {
            ActionsBottomSheetFragment.performFirstAction(context);
            return true;
        }
        if (itemId == R.id.menu_explore) {
            FragmentUtils.showWithArguments(SubredditsBottomSheetFragment.class, ContextHelper.getFragmentManager(context), SubredditsBottomSheetFragment.buildArgs());
            return true;
        }
        if (itemId != R.id.menu_messages) {
            return true;
        }
        if (AccountSingleton.getInstance().isLoggedIn()) {
            EditFragment.newMessageInstance(null).show(ContextHelper.getFragmentManager(context), "EditFragment");
            return true;
        }
        StaticToast.show(context, R.string.common_generic_error_logged_out);
        return true;
    }

    public static boolean onBottomNavItemSelected(Context context, PostHost postHost, MenuItem menuItem) {
        BusSingleton.getBus().post(new StopScrollEvent());
        if (menuItem.getItemId() == R.id.menu_posts) {
            postHost.showPosts();
            return true;
        }
        if (menuItem.getItemId() == R.id.menu_messages) {
            return postHost.showMessaging();
        }
        if (menuItem.getItemId() == R.id.menu_explore) {
            FragmentUtils.showWithArguments(SubredditsBottomSheetFragment.class, ContextHelper.getFragmentManager(context), SubredditsBottomSheetFragment.buildArgs(false));
            return false;
        }
        if (menuItem.getItemId() == R.id.menu_search) {
            FragmentUtils.showWithArguments(SubredditsBottomSheetFragment.class, ContextHelper.getFragmentManager(context), SubredditsBottomSheetFragment.buildArgs());
            return false;
        }
        if (menuItem.getItemId() == R.id.menu_settings) {
            context.startActivity(new Intent(context, (Class<?>) PreferencesActivity.class));
            return false;
        }
        if (menuItem.getItemId() == R.id.menu_actions) {
            ActionsBottomSheetFragment.show(context);
        }
        return false;
    }
}
