package com.resync.app.ui.views.main;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes3.dex */
public class PostsFragmentFrameLayout extends FrameLayout {
    public PostsFragmentFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) ContextHelper.getBaseActivity(getContext()).findViewById(R.id.navigation);
        Logger.verbose("BOTTOM", "Bottom nav:" + bottomNavigationView);
        if (bottomNavigationView == null || !SyncBottomNavigationView.shouldShow() || SettingsSingleton.getSettings().navigationBottomHide) {
            return;
        }
        setPadding(0, 0, 0, SizeHelper.dpToPx(80));
    }
}
