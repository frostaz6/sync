package com.resync.app.singletons;

import android.content.Context;
import com.google.android.exoplayer2.database.DatabaseProvider;
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.resync.app.RedditApplication;
import java.io.File;

/* JADX INFO: renamed from: j9.d */
/* JADX INFO: compiled from: SimpleCacheSingleton.java */
/* JADX INFO: loaded from: classes.dex */
public final class SimpleCacheSingleton {

    static final Object LOCK = new Object();
    static SimpleCacheSingleton instance;
    private static SimpleCache cache;

    private SimpleCacheSingleton() {
        cache = new SimpleCache(resolveCacheDir(RedditApplication.getAppContext()), new LeastRecentlyUsedCacheEvictor(209715200L), createDatabaseProvider(RedditApplication.getAppContext()));
    }

    public static SimpleCacheSingleton getInstance() {
        SimpleCacheSingleton simpleCacheSingleton;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new SimpleCacheSingleton();
            }
            simpleCacheSingleton = instance;
        }
        return simpleCacheSingleton;
    }

    private static synchronized DatabaseProvider createDatabaseProvider(Context context) {
        return new StandaloneDatabaseProvider(context);
    }

    private static synchronized File resolveCacheDir(Context context) {
        File cacheDir;
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir != null && externalCacheDir.isDirectory() && externalCacheDir.canWrite() && externalCacheDir.canRead()) {
            cacheDir = new File(externalCacheDir, "redditVideoCache");
        } else {
            cacheDir = context.getCacheDir();
            if (cacheDir != null && cacheDir.isDirectory() && cacheDir.canWrite() && cacheDir.canRead()) {
                cacheDir = new File(cacheDir, "redditVideoCache");
            }
        }
        return cacheDir;
    }

    public SimpleCache getCache() {
        return cache;
    }
}
