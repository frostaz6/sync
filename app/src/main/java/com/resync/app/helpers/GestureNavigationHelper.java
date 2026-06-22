package com.resync.app.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.RedditApplication;
import com.resync.app.ui.activities.MainActivity;
import com.resync.app.premium.SyncIapHelper;

/* JADX INFO: renamed from: t7.o */
/* JADX INFO: compiled from: GestureNavigationHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class GestureNavigationHelper {
    public static boolean isGestureNavEnabled(Context context) {
        Resources resources;
        int identifier;
        return Build.VERSION.SDK_INT >= 29 && !WindowHelper.isInMultiWindow(context) && (identifier = (resources = RedditApplication.getAppContext().getResources()).getIdentifier("config_navBarInteractionMode", "integer", "android")) > 0 && resources.getInteger(identifier) == 2 && !SyncIapHelper.areAdsEnabled(RedditApplication.getAppContext());
    }

    public static void applyGestureNavPadding(Context context, RecyclerView recyclerView) {
        if (context == null || recyclerView == null || (context instanceof MainActivity) || !isGestureNavEnabled(context)) {
            return;
        }
        recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop(), recyclerView.getPaddingRight(), recyclerView.getPaddingBottom() + WindowHelper.getNavigationBarHeight(context));
        recyclerView.setClipToPadding(false);
    }
}
