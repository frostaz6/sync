package com.resync.app.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.resync.app.util.TestUtils;

/* JADX INFO: renamed from: t7.a0 */
/* JADX INFO: compiled from: PreviousActivityHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class PreviousActivityHelper {
    public static Class<? extends Activity> getPreviousActivity(Context context) {
        if (!(context instanceof Activity)) {
            return null;
        }
        Activity activity = (Activity) context;
        if (activity.getIntent() != null && activity.getIntent().hasExtra("PreviousActivityHelper") && (activity.getIntent().getSerializableExtra("PreviousActivityHelper") instanceof Class)) {
            return (Class) activity.getIntent().getSerializableExtra("PreviousActivityHelper");
        }
        return null;
    }

    private static void putPreviousActivity(Activity activity, Intent intent) {
        intent.putExtra("PreviousActivityHelper", activity.getClass());
    }

    public static void startActivityWithPrevious(Activity activity, Intent intent, Bundle bundle) {
        putPreviousActivity(activity, intent);
        if (TestUtils.isRobolectric()) {
            intent.setFlags(268435456);
        }
        ContextCompat.startActivity(activity, intent, bundle);
    }
}
