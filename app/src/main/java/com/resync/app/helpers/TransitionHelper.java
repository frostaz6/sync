package com.resync.app.helpers;

import android.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import androidx.core.util.Pair;
import com.resync.app.ui.views.images.CustomImageView;
import java.util.ArrayList;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;

/* JADX INFO: renamed from: t7.o0 */
/* JADX INFO: compiled from: TransitionHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class TransitionHelper {
    public static Pair<View, String>[] buildSharedElements(Context context, View... viewArr) {
        if (viewArr == null || viewArr.length == 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (View view : viewArr) {
            if (isViewTransitionable(view)) {
                arrayList.add(new Pair(view, view.getTransitionName()));
            }
        }
        if (arrayList.size() == 0) {
            Logger.verbose("Transition skipped");
            return null;
        }
        if (context instanceof Activity) {
            View viewFindViewById = ((Activity) context).getWindow().getDecorView().findViewById(R.id.navigationBarBackground);
            if (isViewTransitionable(viewFindViewById)) {
                Logger.verbose("Transition", "Added the navigation bar to the transition");
                arrayList.add(new Pair(viewFindViewById, "android:navigation:background"));
            }
        }
        return (Pair[]) arrayList.toArray(new Pair[arrayList.size()]);
    }

    @TargetApi(21)
    public static boolean isViewTransitionable(View view) {
        boolean transitionable = view != null && view.getHeight() > 0 && view.getWidth() > 0 && view.isShown() && view.isAttachedToWindow() && view.getParent() != null && !TextUtils.isEmpty(view.getTransitionName());
        if (view instanceof CustomImageView) {
            return transitionable && ((CustomImageView) view).hasDrawable();
        }
        return transitionable;
    }
}
