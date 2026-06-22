package com.resync.app.color;

import com.resync.app.color.utilities.Hct;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: renamed from: zc.d */
/* JADX INFO: compiled from: TonalPalette.java */
/* JADX INFO: loaded from: classes3.dex */
public final class TonalPalette {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    Map<Integer, Integer> cache = new HashMap();
    float hue;
    float chroma;

    private TonalPalette(float hue, float chroma) {
        this.hue = hue;
        this.chroma = chroma;
    }

    public static final TonalPalette fromHueAndChroma(float hue, float chroma) {
        return new TonalPalette(hue, chroma);
    }

    public int tone(int shade) {
        int key = 100 - (shade / 10);
        if (this.cache.containsKey(Integer.valueOf(key))) {
            return this.cache.get(Integer.valueOf(key)).intValue();
        }
        int argb = Hct.from(this.hue, this.chroma, 100.0f - (shade / 10.0f)).toInt();
        this.cache.put(Integer.valueOf(key), Integer.valueOf(argb));
        return argb;
    }
}
