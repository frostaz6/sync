package com.resync.app.color.utilities;

/* JADX INFO: renamed from: yc.c, reason: case insensitive filesystem */
/* JADX INFO: compiled from: ViewingConditions.java */
/* JADX INFO: loaded from: classes3.dex */
public final class ViewingConditions {
    public static final ViewingConditions DEFAULT = make(new float[]{(float) com.resync.app.util.graphics.ColorUtils.whitePointD65()[0], (float) com.resync.app.util.graphics.ColorUtils.whitePointD65()[1], (float) com.resync.app.util.graphics.ColorUtils.whitePointD65()[2]}, (float) ((com.resync.app.util.graphics.ColorUtils.yFromLstar(50.0d) * 63.66197723675813d) / 100.0d), 50.0f, 2.0f, false);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final float aw;
    private final float nbb;
    private final float ncb;
    private final float surroundC;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final float nc;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final float backgroundN;
    private final float[] rgbD;
    private final float fl;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final float flRoot;
    private final float baseExponentZ;

    private ViewingConditions(float backgroundN, float aw, float nbb, float ncb, float surroundC, float nc, float[] rgbD, float fl, float flRoot, float baseExponentZ) {
        this.backgroundN = backgroundN;
        this.aw = aw;
        this.nbb = nbb;
        this.ncb = ncb;
        this.surroundC = surroundC;
        this.nc = nc;
        this.rgbD = rgbD;
        this.fl = fl;
        this.flRoot = flRoot;
        this.baseExponentZ = baseExponentZ;
    }

    static ViewingConditions make(float[] whitePoint, float adaptingLuminance, float backgroundLstar, float surround, boolean discountingIlluminant) {
        float[][] matrix = Cam16.XYZ_TO_CAM16RGB;
        float rWhite = (whitePoint[0] * matrix[0][0]) + (whitePoint[1] * matrix[0][1]) + (whitePoint[2] * matrix[0][2]);
        float gWhite = (whitePoint[0] * matrix[1][0]) + (whitePoint[1] * matrix[1][1]) + (whitePoint[2] * matrix[1][2]);
        float bWhite = (whitePoint[0] * matrix[2][0]) + (whitePoint[1] * matrix[2][1]) + (whitePoint[2] * matrix[2][2]);
        float surroundFactor = (surround / 10.0f) + 0.8f;
        float surroundCoeff = (float) (((double) surroundFactor) >= 0.9d ? com.resync.app.util.graphics.MathUtils.lerp(0.5899999737739563d, 0.6899999976158142d, (surroundFactor - 0.9f) * 10.0f) : com.resync.app.util.graphics.MathUtils.lerp(0.5249999761581421d, 0.5899999737739563d, (surroundFactor - 0.8f) * 10.0f));
        float degreeOfAdaptation = discountingIlluminant ? 1.0f : (1.0f - (((float) Math.exp(((-adaptingLuminance) - 42.0f) / 92.0f)) * 0.2777778f)) * surroundFactor;
        double dClamp = degreeOfAdaptation;
        if (dClamp > 1.0d) {
            degreeOfAdaptation = 1.0f;
        } else if (dClamp < 0.0d) {
            degreeOfAdaptation = 0.0f;
        }
        float[] rgbD = {(((100.0f / rWhite) * degreeOfAdaptation) + 1.0f) - degreeOfAdaptation, (((100.0f / gWhite) * degreeOfAdaptation) + 1.0f) - degreeOfAdaptation, (((100.0f / bWhite) * degreeOfAdaptation) + 1.0f) - degreeOfAdaptation};
        float kFactor = 1.0f / ((5.0f * adaptingLuminance) + 1.0f);
        float kFourth = kFactor * kFactor * kFactor * kFactor;
        float k4F = 1.0f - kFourth;
        float fl = (kFourth * adaptingLuminance) + (0.1f * k4F * k4F * ((float) Math.cbrt(((double) adaptingLuminance) * 5.0d)));
        float backgroundRatio = (float) (com.resync.app.util.graphics.ColorUtils.yFromLstar(backgroundLstar) / ((double) whitePoint[1]));
        double nDouble = backgroundRatio;
        float baseExponent = ((float) Math.sqrt(nDouble)) + 1.48f;
        float nbb = 0.725f / ((float) Math.pow(nDouble, 0.2d));
        float[] rgbAFactors = {(float) Math.pow(((double) ((rgbD[0] * fl) * rWhite)) / 100.0d, 0.42d), (float) Math.pow(((double) ((rgbD[1] * fl) * gWhite)) / 100.0d, 0.42d), (float) Math.pow(((double) ((rgbD[2] * fl) * bWhite)) / 100.0d, 0.42d)};
        float[] rgbA = {(rgbAFactors[0] * 400.0f) / (rgbAFactors[0] + 27.13f), (rgbAFactors[1] * 400.0f) / (rgbAFactors[1] + 27.13f), (rgbAFactors[2] * 400.0f) / (rgbAFactors[2] + 27.13f)};
        return new ViewingConditions(backgroundRatio, ((rgbA[0] * 2.0f) + rgbA[1] + (rgbA[2] * 0.05f)) * nbb, nbb, nbb, surroundCoeff, surroundFactor, rgbD, fl, (float) Math.pow(fl, 0.25d), baseExponent);
    }

    public float getAw() {
        return this.aw;
    }

    float getC() {
        return this.surroundC;
    }

    float getFl() {
        return this.fl;
    }

    public float getFlRoot() {
        return this.flRoot;
    }

    public float getN() {
        return this.backgroundN;
    }

    public float getNbb() {
        return this.nbb;
    }

    float getNc() {
        return this.nc;
    }

    float getNcb() {
        return this.ncb;
    }

    public float[] getRgbD() {
        return this.rgbD;
    }

    float getZ() {
        return this.baseExponentZ;
    }
}
