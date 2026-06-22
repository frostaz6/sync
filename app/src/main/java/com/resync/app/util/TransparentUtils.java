package com.resync.app.util;

import android.app.Activity;
import android.app.ActivityOptions;
import java.lang.reflect.Method;

/* JADX INFO: renamed from: wc.n */
/* JADX INFO: compiled from: TransparentUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public class TransparentUtils {
    public static void convertFromTranslucent(Activity activity) {
        try {
            Logger.verbose("Converting activity from translucent: " + activity);
            Method declaredMethod = Activity.class.getDeclaredMethod("convertFromTranslucent", new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(activity, new Object[0]);
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    public static void convertToTranslucent(Activity activity) {
        convertToTranslucentInternal(activity);
    }

    private static void convertToTranslucentInternal(Activity activity) {
        try {
            Method declaredMethod = Activity.class.getDeclaredMethod("getActivityOptions", new Class[0]);
            declaredMethod.setAccessible(true);
            Object objInvoke = declaredMethod.invoke(activity, new Object[0]);
            Class<?> cls = null;
            for (Class<?> cls2 : Activity.class.getDeclaredClasses()) {
                if (cls2.getSimpleName().contains("TranslucentConversionListener")) {
                    cls = cls2;
                }
            }
            Method declaredMethod2 = Activity.class.getDeclaredMethod("convertToTranslucent", cls, ActivityOptions.class);
            declaredMethod2.setAccessible(true);
            declaredMethod2.invoke(activity, null, objInvoke);
        } catch (Throwable unused) {
        }
    }
}
