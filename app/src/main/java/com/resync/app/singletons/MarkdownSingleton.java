package com.resync.app.singletons;

import com.commonsware.cwac.anddown.AndDown;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: j9.b */
/* JADX INFO: compiled from: MarkdownSingleton.java */
/* JADX INFO: loaded from: classes.dex */
public final class MarkdownSingleton {

    private static final Object LOCK = new Object();
    private static AndDown instance;

    public static AndDown getAndDown() {
        AndDown andDown;
        synchronized (LOCK) {
            if (instance == null) {
                Logger.verbose("Singleton", "Anddown created");
                instance = new AndDown();
            }
            andDown = instance;
        }
        return andDown;
    }
}
