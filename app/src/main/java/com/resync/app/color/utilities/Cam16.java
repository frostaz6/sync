package com.resync.app.color.utilities;

/* JADX INFO: renamed from: yc.a, reason: case insensitive filesystem */
/* JADX INFO: compiled from: Cam16.java */
/* JADX INFO: loaded from: classes3.dex */
public final class Cam16 {
    static final float[][] XYZ_TO_CAM16RGB = {new float[]{0.401288f, 0.650173f, -0.051461f}, new float[]{-0.250268f, 1.204414f, 0.045854f}, new float[]{-0.002079f, 0.048952f, 0.953127f}};
    static final float[][] CAM16RGB_TO_XYZ = {new float[]{1.8620678f, -1.0112547f, 0.14918678f}, new float[]{0.38752654f, 0.62144744f, -0.00897398f}, new float[]{-0.0158415f, -0.03412294f, 1.0499644f}};

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final float hue;
    private final float chroma;
    private final float lightness;
    private final float brightness;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final float colorfulness;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final float saturation;
    private final float jstar;
    private final float astar;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final float bstar;

    private Cam16(float hue, float chroma, float lightness, float brightness, float colorfulness, float saturation, float jstar, float astar, float bstar) {
        this.hue = hue;
        this.chroma = chroma;
        this.lightness = lightness;
        this.brightness = brightness;
        this.colorfulness = colorfulness;
        this.saturation = saturation;
        this.jstar = jstar;
        this.astar = astar;
        this.bstar = bstar;
    }

    public static Cam16 fromInt(int argb) {
        return fromIntInViewingConditions(argb, ViewingConditions.DEFAULT);
    }

    static Cam16 fromIntInViewingConditions(int argb, ViewingConditions vc) {
        float redL = (float) com.resync.app.util.graphics.ColorUtils.linearized((16711680 & argb) >> 16);
        float greenL = (float) com.resync.app.util.graphics.ColorUtils.linearized((65280 & argb) >> 8);
        float blueL = (float) com.resync.app.util.graphics.ColorUtils.linearized(argb & 255);
        float x = (0.41233894f * redL) + (0.35762063f * greenL) + (0.18051042f * blueL);
        float y = (0.2126f * redL) + (0.7152f * greenL) + (0.0722f * blueL);
        float zVal = (redL * 0.01932141f) + (greenL * 0.11916382f) + (blueL * 0.9503448f);
        float[][] matrix = XYZ_TO_CAM16RGB;
        float rCone = (matrix[0][0] * x) + (matrix[0][1] * y) + (matrix[0][2] * zVal);
        float gCone = (matrix[1][0] * x) + (matrix[1][1] * y) + (matrix[1][2] * zVal);
        float bCone = (x * matrix[2][0]) + (y * matrix[2][1]) + (zVal * matrix[2][2]);
        float rDisc = vc.getRgbD()[0] * rCone;
        float gDisc = vc.getRgbD()[1] * gCone;
        float bDisc = vc.getRgbD()[2] * bCone;
        float rAF = (float) Math.pow(((double) (vc.getFl() * Math.abs(rDisc))) / 100.0d, 0.42d);
        float gAF = (float) Math.pow(((double) (vc.getFl() * Math.abs(gDisc))) / 100.0d, 0.42d);
        float bAF = (float) Math.pow(((double) (vc.getFl() * Math.abs(bDisc))) / 100.0d, 0.42d);
        float rAdapt = ((Math.signum(rDisc) * 400.0f) * rAF) / (rAF + 27.13f);
        float gAdapt = ((Math.signum(gDisc) * 400.0f) * gAF) / (gAF + 27.13f);
        float bAdapt = ((Math.signum(bDisc) * 400.0f) * bAF) / (bAF + 27.13f);
        double bAdaptDouble = bAdapt;
        float aOpponent = ((float) (((((double) rAdapt) * 11.0d) + (((double) gAdapt) * (-12.0d))) + bAdaptDouble)) / 11.0f;
        float bOpponent = ((float) (((double) (rAdapt + gAdapt)) - (bAdaptDouble * 2.0d))) / 9.0f;
        float gAdapt20 = gAdapt * 20.0f;
        float uComponent = (((rAdapt * 20.0f) + gAdapt20) + (21.0f * bAdapt)) / 20.0f;
        float p2Component = (((rAdapt * 40.0f) + gAdapt20) + bAdapt) / 20.0f;
        float atanDegrees = (((float) Math.atan2(bOpponent, aOpponent)) * 180.0f) / 3.1415927f;
        if (atanDegrees < 0.0f) {
            atanDegrees += 360.0f;
        } else if (atanDegrees >= 360.0f) {
            atanDegrees -= 360.0f;
        }
        float hueRadians = (3.1415927f * atanDegrees) / 180.0f;
        float ac = ((float) Math.pow((p2Component * vc.getNbb()) / vc.getAw(), vc.getC() * vc.getZ())) * 100.0f;
        float brightness = (4.0f / vc.getC()) * ((float) Math.sqrt(ac / 100.0f)) * (vc.getAw() + 4.0f) * vc.getFlRoot();
        float alpha = ((float) Math.pow(1.64d - Math.pow(0.29d, vc.getN()), 0.73d)) * ((float) Math.pow((((((((float) (Math.cos(Math.toRadians(((double) atanDegrees) < 20.14d ? 360.0f + atanDegrees : atanDegrees) + 2.0d) + 3.8d)) * 0.25f) * 3846.1538f) * vc.getNc()) * vc.getNcb()) * ((float) Math.hypot(aOpponent, bOpponent))) / (uComponent + 0.305f), 0.9d));
        float chroma = alpha * ((float) Math.sqrt(((double) ac) / 100.0d));
        float colorfulness = chroma * vc.getFlRoot();
        float saturation = ((float) Math.sqrt((alpha * vc.getC()) / (vc.getAw() + 4.0f))) * 50.0f;
        float jstar = (1.7f * ac) / ((0.007f * ac) + 1.0f);
        float mstar = ((float) Math.log1p(0.0228f * colorfulness)) * 43.85965f;
        double hueRadiansDouble = hueRadians;
        return new Cam16(atanDegrees, chroma, ac, brightness, colorfulness, saturation, jstar, mstar * ((float) Math.cos(hueRadiansDouble)), mstar * ((float) Math.sin(hueRadiansDouble)));
    }

    static Cam16 fromJch(float lightness, float chroma, float hue) {
        return fromJchInViewingConditions(lightness, chroma, hue, ViewingConditions.DEFAULT);
    }

    private static Cam16 fromJchInViewingConditions(float lightness, float chroma, float hue, ViewingConditions vc) {
        float brightness = (4.0f / vc.getC()) * ((float) Math.sqrt(((double) lightness) / 100.0d)) * (vc.getAw() + 4.0f) * vc.getFlRoot();
        float colorfulness = chroma * vc.getFlRoot();
        float saturation = ((float) Math.sqrt(((chroma / ((float) Math.sqrt(lightness / 100.0f))) * vc.getC()) / (vc.getAw() + 4.0f))) * 50.0f;
        float jstar = (1.7f * lightness) / ((0.007f * lightness) + 1.0f);
        float mstar = ((float) Math.log1p(((double) colorfulness) * 0.0228d)) * 43.85965f;
        double hueRadians = (3.1415927f * hue) / 180.0f;
        return new Cam16(hue, chroma, lightness, brightness, colorfulness, saturation, jstar, mstar * ((float) Math.cos(hueRadians)), mstar * ((float) Math.sin(hueRadians)));
    }

    float distance(Cam16 other) {
        float deltaJstar = getJstar() - other.getJstar();
        float deltaAstar = getAstar() - other.getAstar();
        float deltaBstar = getBstar() - other.getBstar();
        return (float) (Math.pow(Math.sqrt((deltaJstar * deltaJstar) + (deltaAstar * deltaAstar) + (deltaBstar * deltaBstar)), 0.63d) * 1.41d);
    }

    public float getAstar() {
        return this.astar;
    }

    public float getBstar() {
        return this.bstar;
    }

    public float getChroma() {
        return this.chroma;
    }

    public float getHue() {
        return this.hue;
    }

    public int toInt() {
        return viewed(ViewingConditions.DEFAULT);
    }

    public float getJ() {
        return this.lightness;
    }

    public float getJstar() {
        return this.jstar;
    }

    int viewed(ViewingConditions vc) {
        float tValue = (float) Math.pow(((double) ((((double) getChroma()) == 0.0d || ((double) getJ()) == 0.0d) ? 0.0f : getChroma() / ((float) Math.sqrt(((double) getJ()) / 100.0d)))) / Math.pow(1.64d - Math.pow(0.29d, vc.getN()), 0.73d), 1.1111111111111112d);
        double hueRadians = (getHue() * 3.1415927f) / 180.0f;
        float eHue = ((float) (Math.cos(2.0d + hueRadians) + 3.8d)) * 0.25f;
        float ac = vc.getAw() * ((float) Math.pow(((double) getJ()) / 100.0d, (1.0d / ((double) vc.getC())) / ((double) vc.getZ())));
        float p1Component = eHue * 3846.1538f * vc.getNc() * vc.getNcb();
        float p2Component = ac / vc.getNbb();
        float sinHue = (float) Math.sin(hueRadians);
        float cosHue = (float) Math.cos(hueRadians);
        float gamma = (((0.305f + p2Component) * 23.0f) * tValue) / (((p1Component * 23.0f) + ((11.0f * tValue) * cosHue)) + ((tValue * 108.0f) * sinHue));
        float aOpponent = cosHue * gamma;
        float bOpponent = gamma * sinHue;
        float rAdapt = p2Component * 460.0f;
        float rCAM = (((451.0f * aOpponent) + rAdapt) + (288.0f * bOpponent)) / 1403.0f;
        float gCAM = ((rAdapt - (891.0f * aOpponent)) - (261.0f * bOpponent)) / 1403.0f;
        float rCBase = Math.signum(rCAM) * (100.0f / vc.getFl()) * ((float) Math.pow((float) Math.max(0.0d, (((double) Math.abs(rCAM)) * 27.13d) / (400.0d - ((double) Math.abs(rCAM)))), 2.380952380952381d));
        float gCBase = Math.signum(gCAM) * (100.0f / vc.getFl()) * ((float) Math.pow((float) Math.max(0.0d, (((double) Math.abs(gCAM)) * 27.13d) / (400.0d - ((double) Math.abs(gCAM)))), 2.380952380952381d));
        float bCAM = ((rAdapt - (aOpponent * 220.0f)) - (bOpponent * 6300.0f)) / 1403.0f;
        float bCBase = Math.signum(bCAM) * (100.0f / vc.getFl()) * ((float) Math.pow((float) Math.max(0.0d, (((double) Math.abs(bCAM)) * 27.13d) / (400.0d - ((double) Math.abs(bCAM)))), 2.380952380952381d));
        float rXYZ = rCBase / vc.getRgbD()[0];
        float gXYZ = gCBase / vc.getRgbD()[1];
        float bXYZ = bCBase / vc.getRgbD()[2];
        float[][] matrix = CAM16RGB_TO_XYZ;
        return com.resync.app.util.graphics.ColorUtils.argbFromXyz((matrix[0][0] * rXYZ) + (matrix[0][1] * gXYZ) + (matrix[0][2] * bXYZ), (matrix[1][0] * rXYZ) + (matrix[1][1] * gXYZ) + (matrix[1][2] * bXYZ), (rXYZ * matrix[2][0]) + (gXYZ * matrix[2][1]) + (bXYZ * matrix[2][2]));
    }
}
