package com.resync.app.helpers.ui;

import android.content.Context;
import android.view.ContextThemeWrapper;
import androidx.appcompat.app.AlertDialog;
import com.resync.app.R;
import com.resync.app.helpers.NightHelper;

/* JADX INFO: renamed from: gb.a */
/* JADX INFO: compiled from: AlertDialogHelper.java */
/* JADX INFO: loaded from: classes2.dex */
public final class AlertDialogHelper {
    public static AlertDialog.Builder newBuilder(Context context) {
        return new AlertDialog.Builder(new ContextThemeWrapper(context, getThemeRes()));
    }

    private static int getThemeRes() {
        return NightHelper.isNight() ? R.style.Alert_Dark : R.style.Alert_Day;
    }
}
