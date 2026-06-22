package com.resync.app.model;

/* JADX INFO: renamed from: o8.c */
/* JADX INFO: compiled from: Kinds.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class Kinds {
    public static String toPrefix(int kind) {
        return kind != 1 ? kind != 2 ? kind != 4 ? kind != 5 ? kind != 6 ? kind != 8 ? kind != 9 ? "t3" : "more" : "t8" : "t6" : "t5" : "t4" : "t2" : "t1";
    }

    public static int fromPrefix(String prefix) {
        if (prefix.equalsIgnoreCase("t1")) {
            return 1;
        }
        if (prefix.equalsIgnoreCase("t2")) {
            return 2;
        }
        if (prefix.equalsIgnoreCase("t3")) {
            return 3;
        }
        if (prefix.equalsIgnoreCase("t4")) {
            return 4;
        }
        if (prefix.equalsIgnoreCase("t5")) {
            return 5;
        }
        if (prefix.equalsIgnoreCase("t6")) {
            return 6;
        }
        if (prefix.equalsIgnoreCase("t8")) {
            return 8;
        }
        return prefix.equalsIgnoreCase("more") ? 9 : 3;
    }
}
