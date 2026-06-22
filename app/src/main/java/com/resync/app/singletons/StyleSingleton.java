package com.resync.app.singletons;

import android.content.Context;
import com.resync.app.ui.activities.CasualGalleryActivity;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.NightHelper;

/* JADX INFO: renamed from: j9.g */
/* JADX INFO: compiled from: StyleSingleton.java */
/* JADX INFO: loaded from: classes.dex */
public final class StyleSingleton {
    public static int getTextColorAuto(Context context, boolean z) {
        return getTextColor(context, z, NightHelper.isNight());
    }

    public static int getTextColor(Context context, boolean secondary, boolean night) {
        if (context instanceof CasualGalleryActivity) {
            return -1;
        }
        return secondary ? MonetUtil.getSecondaryTextColorAuto() : MonetUtil.getPrimaryTextColor(night);
    }
}
