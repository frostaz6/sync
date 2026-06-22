package com.resync.app.widgets;

import com.resync.app.helpers.ui.MonetUtil;

/* JADX INFO: renamed from: jb.d */
/* JADX INFO: compiled from: ShapeOverlayUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public class ShapeOverlayUtils {
    public static int resolveColor(int colorMode) {
        if (colorMode == 1) {
            return MonetUtil.getWindowBackgroundColorAuto();
        }
        if (colorMode == 2) {
            return MonetUtil.getBackgroundColorAuto();
        }
        if (colorMode != 3) {
            throw new RuntimeException("Unsupported color mode");
        }
        if (MonetUtil.isAmoled()) {
            return -16777216;
        }
        return MonetUtil.getCardColorAuto();
    }
}
