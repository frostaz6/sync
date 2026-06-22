package com.resync.app.helpers.ui;

import android.content.Context;
import android.util.Pair;
import com.resync.app.helpers.WindowHelper;

/* JADX INFO: renamed from: gb.f */
/* JADX INFO: compiled from: ImageViewerHelper.java */
/* JADX INFO: loaded from: classes2.dex */
public final class ImageViewerHelper {
    public static Pair<Integer, Integer> getActionBarInsets(Context context) {
        return new Pair<Integer, Integer>(Integer.valueOf(WindowHelper.getActionBarHeight(context)), Integer.valueOf(WindowHelper.getActionBarHeight(context)));
    }
}
