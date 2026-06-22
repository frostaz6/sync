package com.resync.app.ui.views.video;

import android.content.Context;
import android.view.TextureView;

/* JADX INFO: renamed from: rc.c */
/* JADX INFO: compiled from: SimpleTextureView.java */
/* JADX INFO: loaded from: classes3.dex */
public class SimpleTextureView extends TextureView {

    public float scaledWidth;
    public float scaledHeight;
    int videoWidth;

    int videoHeight;

    public SimpleTextureView(Context context) {
        super(context);
        init();
    }

    private void init() {
    }

    public void setVideoSize(int width, int height) {
        this.videoWidth = width;
        this.videoHeight = height;
        // restored (float) cast dropped by JADX — see Vineflower rc/c.java; int/int div-by-zero otherwise
        float widthRatio = (float) getWidth() / this.videoWidth;
        float heightRatio = (float) getHeight() / this.videoHeight;
        if (this.videoWidth * getHeight() < getWidth() * this.videoHeight) {
            this.scaledWidth = heightRatio * this.videoWidth;
            this.scaledHeight = getHeight();
        } else {
            this.scaledWidth = getWidth();
            this.scaledHeight = widthRatio * this.videoHeight;
        }
        getLayoutParams().width = (int) this.scaledWidth;
        getLayoutParams().height = (int) this.scaledHeight;
        requestLayout();
    }
}
