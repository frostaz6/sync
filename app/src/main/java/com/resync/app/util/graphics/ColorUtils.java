package com.resync.app.util.graphics;

/* JADX INFO: renamed from: ad.a */
/* JADX INFO: compiled from: ColorUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public class ColorUtils {
    public static int argbFromLstar(double lstar) {
        double fy = (lstar + 16.0d) / 116.0d;
        double y = (lstar > 8.0d ? 1 : (lstar == 8.0d ? 0 : -1)) > 0 ? fy * fy * fy : lstar / 903.2962962962963d;
        double fyCubed = fy * fy * fy;
        boolean aboveEpsilon = fyCubed > 0.008856451679035631d;
        double x = aboveEpsilon ? fyCubed : lstar / 903.2962962962963d;
        if (!aboveEpsilon) {
            fyCubed = lstar / 903.2962962962963d;
        }
        double[] dArrK = whitePointD65();
        return argbFromXyz(x * dArrK[0], y * dArrK[1], fyCubed * dArrK[2]);
    }

    public static int argbFromRgb(int red, int green, int blue) {
        return ((red & 255) << 16) | (-16777216) | ((green & 255) << 8) | (blue & 255);
    }

    public static int argbFromXyz(double x, double y, double z) {
        double[] dArrC = MathUtils.matrixMultiply(new double[]{x, y, z}, xyzToSrgb());
        return argbFromRgb(delinearized(dArrC[0]), delinearized(dArrC[1]), delinearized(dArrC[2]));
    }

    public static int blueFromArgb(int argb) {
        return argb & 255;
    }

    public static int delinearized(double rgbComponent) {
        double normalized = rgbComponent / 100.0d;
        return MathUtils.clampInt(0, 255, (int) Math.round((normalized <= 0.0031308d ? normalized * 12.92d : (Math.pow(normalized, 0.4166666666666667d) * 1.055d) - 0.055d) * 255.0d));
    }

    public static int greenFromArgb(int argb) {
        return (argb >> 8) & 255;
    }

    public static double linearized(int rgbComponent) {
        double normalized = ((double) rgbComponent) / 255.0d;
        return (normalized <= 0.040449936d ? normalized / 12.92d : Math.pow((normalized + 0.055d) / 1.055d, 2.4d)) * 100.0d;
    }

    public static double lstarFromArgb(int argb) {
        double y = xyzFromArgb(argb)[1] / 100.0d;
        return y <= 0.008856451679035631d ? y * 903.2962962962963d : (Math.pow(y, 0.3333333333333333d) * 116.0d) - 16.0d;
    }

    public static int redFromArgb(int argb) {
        return (argb >> 16) & 255;
    }

    public static double[][] srgbToXyz() {
        return new double[][]{new double[]{0.41233895d, 0.35762064d, 0.18051042d}, new double[]{0.2126d, 0.7152d, 0.0722d}, new double[]{0.01932141d, 0.11916382d, 0.95034478d}};
    }

    public static double[] whitePointD65() {
        return new double[]{95.047d, 100.0d, 108.883d};
    }

    public static double[] xyzFromArgb(int argb) {
        return MathUtils.matrixMultiply(new double[]{linearized(redFromArgb(argb)), linearized(greenFromArgb(argb)), linearized(blueFromArgb(argb))}, srgbToXyz());
    }

    public static double[][] xyzToSrgb() {
        return new double[][]{new double[]{3.2406d, -1.5372d, -0.4986d}, new double[]{-0.9689d, 1.8758d, 0.0415d}, new double[]{0.0557d, -0.204d, 1.057d}};
    }

    public static double yFromLstar(double lstar) {
        return (lstar > 8.0d ? Math.pow((lstar + 16.0d) / 116.0d, 3.0d) : (lstar / 24389.0d) / 27.0d) * 100.0d;
    }
}
