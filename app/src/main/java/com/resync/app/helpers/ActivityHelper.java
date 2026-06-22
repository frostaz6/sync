package com.resync.app.helpers;

import android.app.Activity;
import android.content.Intent;
import com.resync.app.R;
import com.resync.app.ui.activities.MainActivity;
import com.resync.app.ui.activities.OnboardingActivity;

/* JADX INFO: renamed from: t7.c */
/* JADX INFO: compiled from: ActivityHelper.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class ActivityHelper {
    public static void restartToMain(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, (Class<?>) MainActivity.class);
        intent.addFlags(268468224);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void restartToOnboarding(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, (Class<?>) OnboardingActivity.class);
        intent.addFlags(268468224);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
