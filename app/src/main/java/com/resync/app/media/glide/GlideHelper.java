package com.resync.app.media.glide;

import com.bumptech.glide.Glide;
import com.resync.app.RedditApplication;
import com.resync.app.media.ImageManager;

/* JADX INFO: renamed from: x7.a */
/* JADX INFO: compiled from: GlideHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class GlideHelper {

    /* JADX INFO: renamed from: x7.a$a */
    /* JADX INFO: compiled from: GlideHelper.java */
    static class ClearMemoryThread extends Thread {
        ClearMemoryThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Glide.get(RedditApplication.getAppContext()).clearMemory();
        }
    }

    public static void clearCacheTwice() {
        clearCache();
        clearCache();
    }

    protected static void clearCache() {
        ImageManager.clearOldCache(RedditApplication.getAppContext(), 0);
        new ClearMemoryThread().start();
    }

    public static void preload(String str) {
        Glide.with(RedditApplication.getAppContext()).asBitmap().load(str).submit();
    }
}
