package com.resync.app.model;

/* JADX INFO: renamed from: o8.b */
/* JADX INFO: compiled from: Gallery.java */
/* JADX INFO: loaded from: classes.dex */
public class Gallery {

    public int position;       // a: starting position
    public String[] urls;      // b: full/mp4 urls
    public String[] thumbnails; // c: thumbnail urls
    public String[] captions;  // d: captions

    public Gallery(int size) {
        this.urls = new String[size];
        this.captions = new String[size];
        this.thumbnails = new String[size];
    }
}
