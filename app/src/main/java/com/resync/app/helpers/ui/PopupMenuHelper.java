package com.resync.app.helpers.ui;

import android.view.ContextThemeWrapper;
import android.view.View;
import androidx.appcompat.widget.PopupMenu;
import com.resync.app.R;
import com.resync.app.helpers.NightHelper;

/* JADX INFO: renamed from: gb.j */
/* JADX INFO: compiled from: PopupMenuHelper.java */
/* JADX INFO: loaded from: classes2.dex */
public final class PopupMenuHelper {
    private static int getThemeRes() {
        return NightHelper.isNight() ? R.style.Popup_Night : R.style.Popup_Day;
    }

    public static PopupMenu create(View view) {
        return new PopupMenu(new ContextThemeWrapper(view.getContext(), getThemeRes()), view);
    }

    public static PopupMenu create(View view, int gravity) {
        return new PopupMenu(new ContextThemeWrapper(view.getContext(), getThemeRes()), view, gravity);
    }
}
