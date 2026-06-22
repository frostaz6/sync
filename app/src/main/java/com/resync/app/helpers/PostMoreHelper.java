package com.resync.app.helpers;

import android.content.Context;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.fragment_dialogs.bottom.PostMoreBottomSheetFragment;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: t7.x */
/* JADX INFO: compiled from: PostMoreHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class PostMoreHelper {
    public static void showMoreSheet(Context context, int uiMode, Post post) {
        try {
            FragmentUtils.showWithArguments(PostMoreBottomSheetFragment.class, ((BaseActivity) context).getSupportFragmentManager(), PostMoreBottomSheetFragment.buildArgs(post, uiMode));
        } catch (IllegalStateException e) {
            Logger.logException(e);
        }
    }
}
