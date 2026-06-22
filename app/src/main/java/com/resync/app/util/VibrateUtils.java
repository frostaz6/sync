package com.resync.app.util;

import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import com.resync.app.RedditApplication;

/* JADX INFO: renamed from: wc.o */
/* JADX INFO: compiled from: VibrateUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public class VibrateUtils {
    protected static boolean isHapticFeedbackEnabled() {
        return Settings.System.getInt(RedditApplication.getAppContext().getContentResolver(), "haptic_feedback_enabled", 0) != 0;
    }

    public static void vibrate() {
        vibrate(false);
    }

    public static void vibrate(boolean z) {
        if (isHapticFeedbackEnabled() || z) {
            Vibrator vibrator = (Vibrator) RedditApplication.getAppContext().getSystemService("vibrator");
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(10L, -1));
            } else {
                vibrator.vibrate(10L);
            }
        }
    }
}
