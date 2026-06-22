package com.resync.app.util;

import java.lang.reflect.Array;

/* JADX INFO: renamed from: wc.a */
/* JADX INFO: compiled from: ArrayUtil.java */
/* JADX INFO: loaded from: classes3.dex */
public class ArrayUtil {
    public static int getLength(Object obj) {
        if (obj == null) {
            return 0;
        }
        return Array.getLength(obj);
    }

    public static boolean isEmpty(Object[] objArr) {
        return getLength(objArr) == 0;
    }
}
