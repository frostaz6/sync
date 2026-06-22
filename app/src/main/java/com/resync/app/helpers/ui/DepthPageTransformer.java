package com.resync.app.helpers.ui;

import android.view.View;
import androidx.viewpager.widget.ViewPager;

/* JADX INFO: renamed from: gb.d */
/* JADX INFO: compiled from: DepthPageTransformer.java */
/* JADX INFO: loaded from: classes2.dex */
public class DepthPageTransformer implements ViewPager.PageTransformer {
    @Override // androidx.viewpager.widget.ViewPager.PageTransformer
    public void transformPage(View view, float position) {
        int width = view.getWidth();
        if (position < -1.0f) {
            view.setAlpha(0.0f);
            return;
        }
        if (position <= 0.0f) {
            view.setAlpha(1.0f);
            view.setTranslationX(0.0f);
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
            return;
        }
        if (position > 1.0f) {
            view.setAlpha(0.0f);
            return;
        }
        view.setAlpha(1.0f - position);
        view.setTranslationX(width * (-position));
        float fAbs = ((1.0f - Math.abs(position)) * 0.25f) + 0.75f;
        view.setScaleX(fAbs);
        view.setScaleY(fAbs);
    }
}
