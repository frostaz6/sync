package com.resync.app.color.utilities;

/* JADX INFO: renamed from: yc.b, reason: case insensitive filesystem */
/* JADX INFO: compiled from: Hct.java */
/* JADX INFO: loaded from: classes3.dex */
public final class Hct {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private float hue;
    private float chroma;
    private float tone;

    private Hct(float hue, float chroma, float tone) {
        setInternalState(getInt(hue, chroma, tone));
    }

    private static Cam16 findCam16ByLstar(float hue, float chroma, float lstar) {
        float bestdL = 1000.0f;
        float low = 0.0f;
        Cam16 best = null;
        float high = 100.0f;
        float bestdE = 1000.0f;
        while (Math.abs(low - high) > 0.01f) {
            float mid = ((high - low) / 2.0f) + low;
            int argb = Cam16.fromJch(mid, chroma, hue).toInt();
            float midLstar = (float) com.resync.app.util.graphics.ColorUtils.lstarFromArgb(argb);
            float dLstar = Math.abs(lstar - midLstar);
            if (dLstar < 0.2f) {
                Cam16 camMid = Cam16.fromInt(argb);
                float deltaE = camMid.distance(Cam16.fromJch(camMid.getJ(), camMid.getChroma(), hue));
                if (deltaE <= 1.0f && deltaE <= bestdL) {
                    best = camMid;
                    bestdE = dLstar;
                    bestdL = deltaE;
                }
            }
            if (bestdE == 0.0f && bestdL < 1.0E-9f) {
                break;
            }
            if (midLstar < lstar) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return best;
    }

    public static Hct from(float hue, float chroma, float tone) {
        return new Hct(hue, chroma, tone);
    }

    public static Hct fromInt(int argb) {
        Cam16 cam = Cam16.fromInt(argb);
        return new Hct(cam.getHue(), cam.getChroma(), (float) com.resync.app.util.graphics.ColorUtils.lstarFromArgb(argb));
    }

    private static int getInt(float hue, float chroma, float tone) {
        return getInt(hue, chroma, tone, ViewingConditions.DEFAULT);
    }

    static int getInt(float hue, float chroma, float tone, ViewingConditions vc) {
        if (chroma < 1.0d || Math.round(tone) <= 0.0d || Math.round(tone) >= 100.0d) {
            return com.resync.app.util.graphics.ColorUtils.argbFromLstar(tone);
        }
        float sanitizedHue = (float) com.resync.app.util.graphics.MathUtils.sanitizeDegreesDouble(hue);
        Cam16 best = null;
        boolean firstPass = true;
        float low = 0.0f;
        float mid = chroma;
        while (Math.abs(low - chroma) >= 0.4f) {
            Cam16 candidate = findCam16ByLstar(sanitizedHue, mid, tone);
            if (firstPass) {
                if (candidate != null) {
                    return candidate.viewed(vc);
                }
                firstPass = false;
            } else if (candidate == null) {
                chroma = mid;
            } else {
                low = mid;
                best = candidate;
            }
            mid = ((chroma - low) / 2.0f) + low;
        }
        return best == null ? com.resync.app.util.graphics.ColorUtils.argbFromLstar(tone) : best.viewed(vc);
    }

    private void setInternalState(int argb) {
        Cam16 cam = Cam16.fromInt(argb);
        float lstar = (float) com.resync.app.util.graphics.ColorUtils.lstarFromArgb(argb);
        this.hue = cam.getHue();
        this.chroma = cam.getChroma();
        this.tone = lstar;
    }

    public float getChroma() {
        return this.chroma;
    }

    public float getHue() {
        return this.hue;
    }

    public int toInt() {
        return getInt(this.hue, this.chroma, this.tone);
    }
}
