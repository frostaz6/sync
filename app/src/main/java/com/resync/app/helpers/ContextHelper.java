package com.resync.app.helpers;

import android.content.Context;
import android.content.ContextWrapper;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.fragments.MessagingFragment;
import com.resync.app.ui.fragments.posts.BasePostsFragment;
import com.resync.app.ui.fragments.posts.VerticalPostsFragment;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.SubredditAboutBottomSheetFragment;
import com.resync.app.dialogs.FriendsAsyncSelectionDialogBottomSheet;
import com.resync.app.dialogs.MultiredditAsyncSelectionDialogBottomSheet;
import com.resync.app.ui.interfaces.PostHost;
import com.resync.app.util.Logger;
import com.resync.app.model.Subreddit;

/* JADX INFO: renamed from: t7.j */
/* JADX INFO: compiled from: ContextHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class ContextHelper {
    public static BaseActivity getBaseActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof BaseActivity) {
            return (BaseActivity) context;
        }
        if (context instanceof ContextWrapper) {
            return getBaseActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    public static String getCurrentSubredditName(Context context) {
        BasePostsFragment basePostsFragment;
        FragmentManager fragmentManagerG = getFragmentManager(context);
        if (fragmentManagerG == null || (basePostsFragment = (BasePostsFragment) fragmentManagerG.findFragmentByTag("posts")) == null || !basePostsFragment.isAdded()) {
            return null;
        }
        return basePostsFragment.getSubredditName();
    }

    public static String getAboutTargetSubreddit(Context context) {
        String subredditName = getCurrentSubredditName(context);
        if (MaterialPostsHelper.isSearch(subredditName)) {
            return MaterialPostsHelper.extractSearchSubreddit(subredditName);
        }
        if (MaterialPostsHelper.isSubSearch(subredditName)) {
            return null;
        }
        if (MaterialPostsHelper.isAggregateFeed(subredditName) || MaterialPostsHelper.isModAbout(subredditName) || MaterialPostsHelper.isDomain(subredditName) || MaterialPostsHelper.isUserProfile(subredditName) || MaterialPostsHelper.isFriends(subredditName)) {
            return null;
        }
        return subredditName;
    }

    public static MessagingFragment getMessagingFragment(Context context) {
        MessagingFragment messagingFragment;
        FragmentManager fragmentManagerG = getFragmentManager(context);
        if (fragmentManagerG == null || (messagingFragment = (MessagingFragment) fragmentManagerG.findFragmentByTag("messaging")) == null || !messagingFragment.isAdded()) {
            return null;
        }
        return messagingFragment;
    }

    public static Subreddit getCurrentSubreddit(Context context) {
        VerticalPostsFragment verticalPostsFragment;
        FragmentManager fragmentManagerG = getFragmentManager(context);
        if (fragmentManagerG == null || (verticalPostsFragment = (VerticalPostsFragment) fragmentManagerG.findFragmentByTag("posts")) == null || !verticalPostsFragment.isAdded()) {
            return null;
        }
        return verticalPostsFragment.getSubredditInfo();
    }

    public static BasePostsFragment getPostsFragment(Context context) {
        BasePostsFragment basePostsFragment;
        FragmentManager fragmentManagerG = getFragmentManager(context);
        if (fragmentManagerG == null || (basePostsFragment = (BasePostsFragment) fragmentManagerG.findFragmentByTag("posts")) == null || !basePostsFragment.isAdded()) {
            return null;
        }
        return basePostsFragment;
    }

    public static FragmentManager getFragmentManager(Context context) {
        BaseActivity baseActivityA = getBaseActivity(context);
        if (baseActivityA != null) {
            return baseActivityA.getSupportFragmentManager();
        }
        return null;
    }

    public static boolean isActivityFinishing(Context context) {
        if (context == null) {
            return true;
        }
        if (context instanceof BaseActivity) {
            return ((BaseActivity) context).isClosing();
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void openAbout(Context context, String str) {
        Logger.verbose("OPENING: " + str);
        if (context == null || StringUtils.isEmpty(str)) {
            StaticToast.show("Error opening about");
            return;
        }
        if (MaterialPostsHelper.isAggregateFeed(str)) {
            StaticToast.show("This page does not have an about");
            return;
        }
        BaseActivity baseActivityA = getBaseActivity(context);
        if (baseActivityA instanceof PostHost) {
            PostHost postHost = (PostHost) baseActivityA;
            if (postHost.hasPanelOrDrawer()) {
                postHost.openEndPanel();
                return;
            }
        }
        if (MaterialPostsHelper.isUserProfile(str)) {
            Logger.verbose("OPENING: PROFILE - " + MaterialPostsHelper.extractProfileUsername(str));
            FragmentUtils.showWithSubreddit(SubredditAboutBottomSheetFragment.class, getFragmentManager(baseActivityA), str);
            return;
        }
        if (MaterialPostsHelper.isMultireddit(str)) {
            FragmentUtils.showWithSubreddit(MultiredditAsyncSelectionDialogBottomSheet.class, getFragmentManager(baseActivityA), str);
            return;
        }
        if (MaterialPostsHelper.isFriends(str)) {
            FragmentUtils.show(FriendsAsyncSelectionDialogBottomSheet.class, getFragmentManager(baseActivityA));
        } else if (MaterialPostsHelper.isStandardSubreddit(str)) {
            FragmentUtils.showWithSubreddit(SubredditAboutBottomSheetFragment.class, getFragmentManager(baseActivityA), str);
        } else {
            StaticToast.show("This page does not have an about");
        }
    }
}
