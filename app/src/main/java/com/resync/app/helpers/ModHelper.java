package com.resync.app.helpers;

import android.content.Context;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.fragment_dialogs.bottom.PostModBottomSheetFragment;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: t7.r */
/* JADX INFO: compiled from: ModHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class ModHelper {
    public static void showModSheet(Context context, Post post) {
        try {
            FragmentUtils.showWithArguments(PostModBottomSheetFragment.class, ((BaseActivity) context).getSupportFragmentManager(), PostModBottomSheetFragment.buildArgs(post));
        } catch (IllegalStateException e) {
            Logger.logException(e);
        }
    }
}
