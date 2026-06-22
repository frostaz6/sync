package com.resync.app.singletons;

import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.PerSubSortHelper;
import com.resync.app.helpers.PerSubViewModeHelper;
import com.resync.app.managers.FavouriteSubredditManager;
import com.resync.app.managers.FriendsManager;
import com.resync.app.managers.ModeratedSubredditManager;
import com.resync.app.managers.MultiredditManager;
import com.resync.app.managers.NewUserTagManager;
import com.resync.app.managers.SubredditsManager;
import com.resync.app.helpers.ActionsManager;
import com.resync.app.helpers.BypassHelper;
import com.resync.app.helpers.FilterHelper;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.RecentHelper;
import com.resync.app.helpers.messaging.MessageCountHelper;

/* JADX INFO: renamed from: j9.e */
/* JADX INFO: compiled from: SingletonManager.java */
/* JADX INFO: loaded from: classes.dex */
public final class SingletonManager {
    public static void reloadAll(boolean z) {
        SettingsSingleton.getInstance().reload();
        UiSingleton.getInstance().reload();
        SpanSingleton.rebuildInstance();
        NewUserTagManager.getInstance().recreate();
        com.resync.app.premium.UserPaintManager.getInstance().reload();
        FilterHelper.getInstance().reset();
        BypassHelper.getInstance().reset();
        MessageCountHelper.reset();
        if (z) {
            RecentHelper.clear();
        }
        PerSubSortHelper.load();
        PerSubViewModeHelper.load();
        FavouriteSubredditManager.getInstance().load();
        SubredditsManager.getInstance().load();
        MultiredditManager.getInstance().load();
        ModeratedSubredditManager.getInstance().load();
        FriendsManager.getInstance().load();
        ActionsManager.getInstance().load();
        NightHelper.refresh(false);
        MonetUtil.reload();
    }
}
