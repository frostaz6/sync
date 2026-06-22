package com.resync.app.helpers.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.ContextThemeWrapper;
import com.resync.app.R;
import com.resync.app.helpers.NightHelper;

/* JADX INFO: renamed from: gb.l */
/* JADX INFO: compiled from: ProgressDialogHelper.java */
/* JADX INFO: loaded from: classes2.dex */
public final class ProgressDialogHelper {
    public static ProgressDialog create(Context context) {
        return new ProgressDialog(new ContextThemeWrapper(context, getThemeRes()));
    }

    private static int getThemeRes() {
        return NightHelper.isNight() ? R.style.Alert_Dark : R.style.Alert_Day;
    }
}
