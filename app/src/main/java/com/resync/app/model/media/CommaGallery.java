package com.resync.app.model.media;

/* JADX INFO: renamed from: g8.a */
/* JADX INFO: compiled from: CommaGallery.java */
/* JADX INFO: loaded from: classes.dex */
public class CommaGallery {

    public Image[] images;   // a: parsed images

    public CommaGallery(String... strArr) {
        this.images = new Image[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            this.images[i] = new Image(strArr[i]);
        }
    }

    public Image getImage(int index) {
        return this.images[index];
    }

    public int size() {
        return this.images.length;
    }
}
