package com.resync.app.color;

import com.resync.app.color.utilities.Hct;

/* JADX INFO: renamed from: zc.a */
/* JADX INFO: compiled from: CorePalette.java */
/* JADX INFO: loaded from: classes3.dex */
public final class CorePalette implements Palette {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public TonalPalette accent1;
    public TonalPalette accent2;
    public TonalPalette accent3;
    public TonalPalette neutral1;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    public TonalPalette neutral2;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    public TonalPalette error;

    private CorePalette(int seed, float intensity) {
        Hct hct = Hct.fromInt(seed);
        float hue = hct.getHue();
        float baseChroma = 24.0f * intensity;
        this.accent1 = TonalPalette.fromHueAndChroma(hue, Math.max(baseChroma, hct.getChroma()));
        this.accent2 = TonalPalette.fromHueAndChroma(hue, 16.0f * intensity);
        this.accent3 = TonalPalette.fromHueAndChroma(60.0f + hue, baseChroma);
        this.neutral1 = TonalPalette.fromHueAndChroma(hue, 4.0f * intensity);
        this.neutral2 = TonalPalette.fromHueAndChroma(hue, intensity * 8.0f);
        this.error = TonalPalette.fromHueAndChroma(25.0f, 84.0f);
    }

    public static CorePalette of(int seed, float intensity) {
        return new CorePalette(seed, intensity);
    }

    @Override // com.resync.app.color.Palette
    public int neutral2(int shade) {
        return this.neutral2.tone(shade);
    }

    @Override // com.resync.app.color.Palette
    public int accent1(int shade) {
        return this.accent1.tone(shade);
    }

    @Override // com.resync.app.color.Palette
    public int accent2(int shade) {
        return this.accent2.tone(shade);
    }

    @Override // com.resync.app.color.Palette
    public int neutral1(int shade) {
        return this.neutral1.tone(shade);
    }

    @Override // com.resync.app.color.Palette
    public int accent3(int shade) {
        return this.accent3.tone(shade);
    }
}
