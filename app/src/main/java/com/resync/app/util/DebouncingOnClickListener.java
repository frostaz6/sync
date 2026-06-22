package com.resync.app.util;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

/* compiled from: DebouncingOnClickListener.java (ButterKnife runtime, decompiled — kept local, not
   the Maven class, to avoid a duplicate-class clash with the butterknife:10.2.3 AAR). */
public abstract class DebouncingOnClickListener implements View.OnClickListener {

    private static final Runnable ENABLE_AGAIN = new Runnable() {
        @Override
        public final void run() {
            enabled = true;
        }
    };
    private static final Handler MAIN = new Handler(Looper.getMainLooper());
    static boolean enabled = true;

    public abstract void doClick(View view);

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        if (enabled) {
            enabled = false;
            MAIN.post(ENABLE_AGAIN);
            doClick(view);
        }
    }
}
