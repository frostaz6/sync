package com.resync.app.util.graphics;

/* JADX INFO: renamed from: ad.b */
/* JADX INFO: compiled from: MathUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public class MathUtils {
    public static int clampInt(int min, int max, int value) {
        return value < min ? min : value > max ? max : value;
    }

    public static double lerp(double start, double stop, double amount) {
        return ((1.0d - amount) * start) + (amount * stop);
    }

    public static double[] matrixMultiply(double[] dArr, double[][] dArr2) {
        return new double[]{(dArr[0] * dArr2[0][0]) + (dArr[1] * dArr2[0][1]) + (dArr[2] * dArr2[0][2]), (dArr[0] * dArr2[1][0]) + (dArr[1] * dArr2[1][1]) + (dArr[2] * dArr2[1][2]), (dArr[0] * dArr2[2][0]) + (dArr[1] * dArr2[2][1]) + (dArr[2] * dArr2[2][2])};
    }

    public static double sanitizeDegreesDouble(double degrees) {
        double normalized = degrees % 360.0d;
        return normalized < 0.0d ? normalized + 360.0d : normalized;
    }
}
