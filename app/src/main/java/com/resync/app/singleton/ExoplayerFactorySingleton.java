package com.resync.app.singleton;

import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.resync.app.RedditApplication;
import com.resync.app.net.OkHttpHelper;
import com.resync.app.singletons.SimpleCacheSingleton;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: com.resync.app.singleton.b */
/* JADX INFO: compiled from: ExoplayerFactorySingleton.java */
/* JADX INFO: loaded from: classes.dex */
public class ExoplayerFactorySingleton {
    private static final Object LOCK = new Object();

    private static ExoplayerFactorySingleton instance;

    final OkHttpDataSource.Factory okHttpFactory;
    final CacheDataSource.Factory cacheDataSourceFactory;
    final DataSource.Factory defaultDataSourceFactory;

    public ExoplayerFactorySingleton() {
        OkHttpDataSource.Factory factory = new OkHttpDataSource.Factory(OkHttpHelper.getUserAgentClient());
        this.okHttpFactory = factory;
        CacheDataSource.Factory factory2 = new CacheDataSource.Factory();
        this.cacheDataSourceFactory = factory2;
        factory2.setCache(SimpleCacheSingleton.getInstance().getCache());
        factory2.setUpstreamDataSourceFactory(factory);
        this.defaultDataSourceFactory = new DefaultDataSource.Factory(RedditApplication.getAppContext());
    }

    public static ExoplayerFactorySingleton getInstance() {
        ExoplayerFactorySingleton exoplayerFactorySingleton;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new ExoplayerFactorySingleton();
            }
            exoplayerFactorySingleton = instance;
        }
        return exoplayerFactorySingleton;
    }

    public DataSource.Factory getDataSourceFactory() {
        if (SettingsSingleton.getSettings().cacheStreamingVideos) {
            Logger.verbose("ExoplayerFactorySingleton", "Using streaming cache source");
            return this.cacheDataSourceFactory;
        }
        Logger.verbose("ExoplayerFactorySingleton", "Using default source");
        return this.defaultDataSourceFactory;
    }

    public OkHttpDataSource.Factory getHttpDataSourceFactory() {
        return this.okHttpFactory;
    }
}
