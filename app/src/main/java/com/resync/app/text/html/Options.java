package com.resync.app.text.html;

import com.resync.app.model.Post;

/* JADX INFO: renamed from: nc.a */
/* JADX INFO: compiled from: Options.java */
/* JADX INFO: loaded from: classes3.dex */
public class Options {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public boolean showWebPreviews;
    public boolean showInlineImages;
    public boolean showBlockquoteBackground;
    public boolean trimTrailing;
    public int width;
    public Post post;

    public Options() {
        this.showWebPreviews = true;
        this.showInlineImages = true;
        this.showBlockquoteBackground = true;
        this.trimTrailing = false;
    }

    public static Options disabled() {
        return new Options(false, false, false, false);
    }

    public static Options compact() {
        return new Options(true, false, true, false);
    }

    public static Options markdownOnly() {
        return new Options(false, false, false, true);
    }

    public Options withWidth(int width) {
        this.width = width;
        return this;
    }

    public Options withPost(Post post) {
        this.post = post;
        return this;
    }

    public Options(boolean showWebPreviews, boolean showInlineImages, boolean showBlockquoteBackground, boolean trimTrailing) {
        this.showWebPreviews = showWebPreviews;
        this.showInlineImages = showInlineImages;
        this.showBlockquoteBackground = showBlockquoteBackground;
        this.trimTrailing = trimTrailing;
    }
}
