package com.resync.app.ui.views.image_progressbar;

/* JADX INFO: compiled from: InvalidStateException.java */
/* JADX INFO: loaded from: classes3.dex */
public class InvalidStateException extends RuntimeException {
    public InvalidStateException(int state) {
        super("This state is not supported: " + state);
    }
}
