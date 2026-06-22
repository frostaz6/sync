package com.resync.app.widgets;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.SparseArray;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: k9.a */
/* JADX INFO: compiled from: ColorFilterProvider.java */
/* JADX INFO: loaded from: classes.dex */
public final class ColorFilterProvider {

    private static SparseArray<PorterDuffColorFilter> cache = new SparseArray<>(50);

    public static synchronized PorterDuffColorFilter getColorFilter(int color) {
        PorterDuffColorFilter porterDuffColorFilter;
        porterDuffColorFilter = cache.get(color);
        if (porterDuffColorFilter == null) {
            porterDuffColorFilter = color == -7859180 ? new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_OVER) : new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            cache.put(color, porterDuffColorFilter);
            Logger.verbose("Adding to cache: " + color + "\tCache size: " + cache.size());
        }
        return porterDuffColorFilter;
    }
}
