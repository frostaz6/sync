package com.resync.app.color;

import android.R;
import android.content.Context;
import java.util.HashMap;
import java.util.Map;
import okhttp3.internal.http.HttpStatusCodesKt;

/* JADX INFO: renamed from: zc.b */
/* JADX INFO: compiled from: MonetPalette.java */
/* JADX INFO: loaded from: classes3.dex */
public class MonetPalette implements Palette {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    Map<Integer, Integer> neutral1 = new HashMap();
    Map<Integer, Integer> neutral2 = new HashMap();
    Map<Integer, Integer> accent1 = new HashMap();
    Map<Integer, Integer> accent2 = new HashMap();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    Map<Integer, Integer> accent3 = new HashMap();

    public MonetPalette(Context context) {
        this.neutral1.put(0, Integer.valueOf(context.getColor(R.color.system_neutral1_0)));
        this.neutral1.put(10, Integer.valueOf(context.getColor(R.color.system_neutral1_10)));
        this.neutral1.put(50, Integer.valueOf(context.getColor(R.color.system_neutral1_50)));
        this.neutral1.put(100, Integer.valueOf(context.getColor(R.color.system_neutral1_100)));
        Map<Integer, Integer> map = this.neutral1;
        Integer numValueOf = Integer.valueOf(HttpStatusCodesKt.HTTP_OK);
        map.put(numValueOf, Integer.valueOf(context.getColor(R.color.system_neutral1_200)));
        Map<Integer, Integer> map2 = this.neutral1;
        Integer numValueOf2 = Integer.valueOf(HttpStatusCodesKt.HTTP_MULT_CHOICE);
        map2.put(numValueOf2, Integer.valueOf(context.getColor(R.color.system_neutral1_300)));
        Map<Integer, Integer> map3 = this.neutral1;
        Integer numValueOf3 = Integer.valueOf(HttpStatusCodesKt.HTTP_BAD_REQUEST);
        map3.put(numValueOf3, Integer.valueOf(context.getColor(R.color.system_neutral1_400)));
        Map<Integer, Integer> map4 = this.neutral1;
        Integer numValueOf4 = Integer.valueOf(HttpStatusCodesKt.HTTP_INTERNAL_SERVER_ERROR);
        map4.put(numValueOf4, Integer.valueOf(context.getColor(R.color.system_neutral1_500)));
        this.neutral1.put(600, Integer.valueOf(context.getColor(R.color.system_neutral1_600)));
        this.neutral1.put(700, Integer.valueOf(context.getColor(R.color.system_neutral1_700)));
        this.neutral1.put(800, Integer.valueOf(context.getColor(R.color.system_neutral1_800)));
        this.neutral1.put(900, Integer.valueOf(context.getColor(R.color.system_neutral1_900)));
        this.neutral1.put(1000, Integer.valueOf(context.getColor(R.color.system_neutral1_1000)));
        this.neutral2.put(0, Integer.valueOf(context.getColor(R.color.system_neutral2_0)));
        this.neutral2.put(10, Integer.valueOf(context.getColor(R.color.system_neutral2_10)));
        this.neutral2.put(50, Integer.valueOf(context.getColor(R.color.system_neutral2_50)));
        this.neutral2.put(100, Integer.valueOf(context.getColor(R.color.system_neutral2_100)));
        this.neutral2.put(numValueOf, Integer.valueOf(context.getColor(R.color.system_neutral2_200)));
        this.neutral2.put(numValueOf2, Integer.valueOf(context.getColor(R.color.system_neutral2_300)));
        this.neutral2.put(numValueOf3, Integer.valueOf(context.getColor(R.color.system_neutral2_400)));
        this.neutral2.put(numValueOf4, Integer.valueOf(context.getColor(R.color.system_neutral2_500)));
        this.neutral2.put(600, Integer.valueOf(context.getColor(R.color.system_neutral2_600)));
        this.neutral2.put(700, Integer.valueOf(context.getColor(R.color.system_neutral2_700)));
        this.neutral2.put(800, Integer.valueOf(context.getColor(R.color.system_neutral2_800)));
        this.neutral2.put(900, Integer.valueOf(context.getColor(R.color.system_neutral2_900)));
        this.neutral2.put(1000, Integer.valueOf(context.getColor(R.color.system_neutral2_1000)));
        this.accent1.put(0, Integer.valueOf(context.getColor(R.color.system_accent1_0)));
        this.accent1.put(10, Integer.valueOf(context.getColor(R.color.system_accent1_10)));
        this.accent1.put(50, Integer.valueOf(context.getColor(R.color.system_accent1_50)));
        this.accent1.put(100, Integer.valueOf(context.getColor(R.color.system_accent1_100)));
        this.accent1.put(numValueOf, Integer.valueOf(context.getColor(R.color.system_accent1_200)));
        this.accent1.put(numValueOf2, Integer.valueOf(context.getColor(R.color.system_accent1_300)));
        this.accent1.put(numValueOf3, Integer.valueOf(context.getColor(R.color.system_accent1_400)));
        this.accent1.put(numValueOf4, Integer.valueOf(context.getColor(R.color.system_accent1_500)));
        this.accent1.put(600, Integer.valueOf(context.getColor(R.color.system_accent1_600)));
        this.accent1.put(700, Integer.valueOf(context.getColor(R.color.system_accent1_700)));
        this.accent1.put(800, Integer.valueOf(context.getColor(R.color.system_accent1_800)));
        this.accent1.put(900, Integer.valueOf(context.getColor(R.color.system_accent1_900)));
        this.accent1.put(1000, Integer.valueOf(context.getColor(R.color.system_accent1_1000)));
        this.accent2.put(0, Integer.valueOf(context.getColor(R.color.system_accent2_0)));
        this.accent2.put(10, Integer.valueOf(context.getColor(R.color.system_accent2_10)));
        this.accent2.put(50, Integer.valueOf(context.getColor(R.color.system_accent2_50)));
        this.accent2.put(100, Integer.valueOf(context.getColor(R.color.system_accent2_100)));
        this.accent2.put(numValueOf, Integer.valueOf(context.getColor(R.color.system_accent2_200)));
        this.accent2.put(numValueOf2, Integer.valueOf(context.getColor(R.color.system_accent2_300)));
        this.accent2.put(numValueOf3, Integer.valueOf(context.getColor(R.color.system_accent2_400)));
        this.accent2.put(numValueOf4, Integer.valueOf(context.getColor(R.color.system_accent2_500)));
        this.accent2.put(600, Integer.valueOf(context.getColor(R.color.system_accent2_600)));
        this.accent2.put(700, Integer.valueOf(context.getColor(R.color.system_accent2_700)));
        this.accent2.put(800, Integer.valueOf(context.getColor(R.color.system_accent2_800)));
        this.accent2.put(900, Integer.valueOf(context.getColor(R.color.system_accent2_900)));
        this.accent2.put(1000, Integer.valueOf(context.getColor(R.color.system_accent2_1000)));
        this.accent3.put(0, Integer.valueOf(context.getColor(R.color.system_accent3_0)));
        this.accent3.put(10, Integer.valueOf(context.getColor(R.color.system_accent3_10)));
        this.accent3.put(50, Integer.valueOf(context.getColor(R.color.system_accent3_50)));
        this.accent3.put(100, Integer.valueOf(context.getColor(R.color.system_accent3_100)));
        this.accent3.put(numValueOf, Integer.valueOf(context.getColor(R.color.system_accent3_200)));
        this.accent3.put(numValueOf2, Integer.valueOf(context.getColor(R.color.system_accent3_300)));
        this.accent3.put(numValueOf3, Integer.valueOf(context.getColor(R.color.system_accent3_400)));
        this.accent3.put(numValueOf4, Integer.valueOf(context.getColor(R.color.system_accent3_500)));
        this.accent3.put(600, Integer.valueOf(context.getColor(R.color.system_accent3_600)));
        this.accent3.put(700, Integer.valueOf(context.getColor(R.color.system_accent3_700)));
        this.accent3.put(800, Integer.valueOf(context.getColor(R.color.system_accent3_800)));
        this.accent3.put(900, Integer.valueOf(context.getColor(R.color.system_accent3_900)));
        this.accent3.put(1000, Integer.valueOf(context.getColor(R.color.system_accent3_1000)));
    }

    @Override // com.resync.app.color.Palette
    public int neutral2(int shade) {
        return this.neutral2.get(Integer.valueOf(shade)).intValue();
    }

    @Override // com.resync.app.color.Palette
    public int accent1(int shade) {
        return this.accent1.get(Integer.valueOf(shade)).intValue();
    }

    @Override // com.resync.app.color.Palette
    public int accent2(int shade) {
        return this.accent2.get(Integer.valueOf(shade)).intValue();
    }

    @Override // com.resync.app.color.Palette
    public int neutral1(int shade) {
        return this.neutral1.get(Integer.valueOf(shade)).intValue();
    }

    @Override // com.resync.app.color.Palette
    public int accent3(int shade) {
        return this.accent3.get(Integer.valueOf(shade)).intValue();
    }
}
