package com.resync.app.ui.util.animation.recycler.helper;

import android.view.View;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes3.dex */
public class TransientHelper {
    public static void setHasTransientState(View view, boolean z) {
        if (view != null) {
            Logger.verbose("TransientHelper", "Setting transient state: " + view + StringUtils.SPACE + z);
        }
    }
}
