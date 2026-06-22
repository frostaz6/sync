package com.squareup.otto;

import android.os.Looper;

/* JADX INFO: renamed from: bd.i */
/* JADX INFO: compiled from: ThreadEnforcer.java */
/* JADX INFO: loaded from: classes3.dex */
public interface ThreadEnforcer {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final ThreadEnforcer ANY = new AnyThreadEnforcer();
    public static final ThreadEnforcer MAIN = new MainThreadEnforcer();

    /* JADX INFO: renamed from: bd.i$a */
    /* JADX INFO: compiled from: ThreadEnforcer.java */
    static class AnyThreadEnforcer implements ThreadEnforcer {
        AnyThreadEnforcer() {
        }

        @Override // com.squareup.otto.ThreadEnforcer
        public void enforce(Bus bus) {
        }
    }

    /* JADX INFO: renamed from: bd.i$b */
    /* JADX INFO: compiled from: ThreadEnforcer.java */
    static class MainThreadEnforcer implements ThreadEnforcer {
        MainThreadEnforcer() {
        }

        @Override // com.squareup.otto.ThreadEnforcer
        public void enforce(Bus bus) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                return;
            }
            throw new IllegalStateException("Event bus " + bus + " accessed from non-main thread " + Looper.myLooper());
        }
    }

    void enforce(Bus bus);
}
