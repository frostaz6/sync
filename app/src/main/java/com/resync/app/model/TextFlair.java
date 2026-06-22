package com.resync.app.model;

/* JADX INFO: renamed from: p8.d */
/* JADX INFO: compiled from: TextFlair.java */
/* JADX INFO: loaded from: classes.dex */
public class TextFlair extends AbstractFlair {
    private String text;   // c: flair display text

    public TextFlair(String str, String str2) {
        super(str, str2);
    }

    public String getDisplayText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }
}
