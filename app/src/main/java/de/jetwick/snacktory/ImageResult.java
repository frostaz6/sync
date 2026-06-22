package de.jetwick.snacktory;

import org.jsoup.nodes.Element;

/* JADX INFO: loaded from: classes3.dex */
public class ImageResult {
    public String alt;
    public Element element;
    public int height;
    public boolean noFollow;
    public String src;
    public String title;
    public Integer weight;
    public int width;

    public ImageResult(String str, Integer num, String str2, int height, int width, String str3, boolean noFollow) {
        this.src = str;
        this.weight = num;
        this.title = str2;
        this.height = height;
        this.width = width;
        this.alt = str3;
        this.noFollow = noFollow;
    }
}
