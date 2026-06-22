package com.resync.app.ui.views.preferences;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class ThemePreferencePreview_ViewBinding implements Unbinder {
    private ThemePreferencePreview target;

    public ThemePreferencePreview_ViewBinding(ThemePreferencePreview themePreferencePreview, View view) {
        this.target = themePreferencePreview;
        themePreferencePreview.lightView = (SingleThemePreferencePreview) Utils.findRequiredViewAsType(view, R.id.light_view, "field 'lightView'", SingleThemePreferencePreview.class);
        themePreferencePreview.darkView = (SingleThemePreferencePreview) Utils.findRequiredViewAsType(view, R.id.dark_view, "field 'darkView'", SingleThemePreferencePreview.class);
    }

    @Override
    public void unbind() {
        ThemePreferencePreview themePreferencePreview = this.target;
        if (themePreferencePreview == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        themePreferencePreview.lightView = null;
        themePreferencePreview.darkView = null;
    }
}
