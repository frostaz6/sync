package com.resync.app.discord;

import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: renamed from: com.resync.app.discord.a */
/* JADX INFO: compiled from: PanelState.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class PanelState {

    /* JADX INFO: renamed from: com.resync.app.discord.a$a */
    /* JADX INFO: compiled from: PanelState.kt */
    public static final class Closed extends PanelState {

        public static final Closed INSTANCE = new Closed();

        private Closed() {
            super(null);
        }
    }

    /* JADX INFO: renamed from: com.resync.app.discord.a$b */
    /* JADX INFO: compiled from: PanelState.kt */
    public static final class Opening extends PanelState {

        public static final Opening INSTANCE = new Opening();

        private Opening() {
            super(null);
        }
    }

    /* JADX INFO: renamed from: com.resync.app.discord.a$c */
    /* JADX INFO: compiled from: PanelState.kt */
    public static final class Opened extends PanelState {

        public static final Opened INSTANCE = new Opened();

        private Opened() {
            super(null);
        }
    }

    /* JADX INFO: renamed from: com.resync.app.discord.a$d */
    /* JADX INFO: compiled from: PanelState.kt */
    public static final class Closing extends PanelState {

        public static final Closing INSTANCE = new Closing();

        private Closing() {
            super(null);
        }
    }

    private PanelState() {
    }

    public /* synthetic */ PanelState(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }
}
