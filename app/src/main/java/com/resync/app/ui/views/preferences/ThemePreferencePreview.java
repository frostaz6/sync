package com.resync.app.ui.views.preferences;

import android.content.Context;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class ThemePreferencePreview extends FrameLayout implements DarkModeAware {

    SingleThemePreferencePreview darkView;

    SingleThemePreferencePreview lightView;

    class RoundedOutlineProvider extends ViewOutlineProvider {
        RoundedOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), SizeHelper.dpToPx(12));
        }
    }

    public ThemePreferencePreview(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setWillNotDraw(false);
        LayoutInflater.from(context).inflate(R.layout.view_theme_preference_preview, (ViewGroup) this, true);
        ButterKnife.bind(this);
        applyColors();
        setOutlineProvider(new RoundedOutlineProvider());
        setClipToOutline(true);
    }

    private void applyColors() {
        setBackgroundColor(MonetUtil.getBackgroundColorAuto());
    }

    public void refresh() {
        applyColors();
        this.lightView.applyColors();
        this.darkView.applyColors();
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }
}
