package com.resync.app.singletons;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/* JADX INFO: renamed from: j9.a */
/* JADX INFO: compiled from: BusSingleton.java */
/* JADX INFO: loaded from: classes.dex */
public final class BusSingleton {

    private static final Bus BUS = new Bus(ThreadEnforcer.ANY);

    public static Bus getBus() {
        return BUS;
    }
}
