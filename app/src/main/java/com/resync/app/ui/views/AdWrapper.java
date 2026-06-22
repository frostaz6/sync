package com.resync.app.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.resync.app.ui.interfaces.DarkModeAware;

/**
 * Ads removed. No-op stub view preserving the original AdWrapper API so layouts that inflate it
 * and activities that reference it still compile; it simply renders nothing.
 */
public class AdWrapper extends FrameLayout implements DarkModeAware {

    public static final String TAG = AdWrapper.class.getSimpleName();

    public AdWrapper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setVisibility(GONE);
    }

    public static int getAdHeight() {
        return 0;
    }

    public void refresh() {
    }

    public boolean isLoaded() {
        return false;
    }

    public void pause() {
    }

    public void resume() {
    }

    @Override
    public void onDarkModeChanged() {
    }

    public void destroy() {
    }
}
