package com.resync.app.ui.views.preferences;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class SingleThemePreferencePreview extends RelativeLayout implements DarkModeAware {

    final boolean darkMode;

    View bottomNav;

    ImageView bottomNavItemOne;

    ImageView bottomNavItemThree;

    ImageView bottomNavItemTwo;

    View contentOne;

    TextView contentOneLink;

    TextView contentOneSelftext;

    TextView contentOneTitle;

    View contentThree;

    TextView contentThreeLink;

    TextView contentThreeSelftext;

    TextView contentThreeTitle;

    View contentTwo;

    TextView contentTwoLink;

    TextView contentTwoSelftext;

    TextView contentTwoTitle;

    View fab;

    TextView toolbar;

    class RoundedOutlineProvider extends ViewOutlineProvider {

        final /* synthetic */ int radius;

        RoundedOutlineProvider(int radius) {
            this.radius = radius;
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), this.radius);
        }
    }

    class OvalOutlineProvider extends ViewOutlineProvider {
        OvalOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, view.getWidth(), view.getHeight());
        }
    }

    public SingleThemePreferencePreview(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_single_theme_preference_preview, (ViewGroup) this, true);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, SyncStyleable.SINGLE_THEME_PREVIEW);
        this.darkMode = typedArrayObtainStyledAttributes.getBoolean(0, false);
        typedArrayObtainStyledAttributes.recycle();
        ButterKnife.bind(this);
        applyColors();
    }

    private void applyRoundedOutline(View view, int radius) {
        view.setOutlineProvider(new RoundedOutlineProvider(radius));
        view.setClipToOutline(true);
    }

    public void applyColors() {
        setBackgroundColor(MonetUtil.getWindowBackgroundColor(this.darkMode));
        applyRoundedOutline(this, SizeHelper.dpToPx(12));
        int primaryTextColor = MonetUtil.getPrimaryTextColor(this.darkMode);
        int linkColor = MonetUtil.getLinkColor(this.darkMode);
        int secondaryTextColor = MonetUtil.getSecondaryTextColor(this.darkMode);
        this.contentOne.setBackgroundColor(MonetUtil.getBackgroundColor(this.darkMode));
        applyRoundedOutline(this.contentOne, SizeHelper.dpToPx(8));
        this.contentOneTitle.setTextColor(primaryTextColor);
        this.contentOneLink.setTextColor(linkColor);
        this.contentOneSelftext.setTextColor(secondaryTextColor);
        this.contentTwo.setBackgroundColor(MonetUtil.getBackgroundColor(this.darkMode));
        applyRoundedOutline(this.contentTwo, SizeHelper.dpToPx(8));
        this.contentTwoTitle.setTextColor(primaryTextColor);
        this.contentTwoLink.setTextColor(linkColor);
        this.contentTwoSelftext.setTextColor(secondaryTextColor);
        this.contentThree.setBackgroundColor(MonetUtil.getBackgroundColor(this.darkMode));
        applyRoundedOutline(this.contentThree, SizeHelper.dpToPx(8));
        this.contentThreeTitle.setTextColor(primaryTextColor);
        this.contentThreeLink.setTextColor(linkColor);
        this.contentThreeSelftext.setTextColor(secondaryTextColor);
        this.contentThree.setBackgroundColor(MonetUtil.getBackgroundColor(this.darkMode));
        this.toolbar.setTextColor(primaryTextColor);
        this.toolbar.setTypeface(TypefaceManager.get(11));
        this.fab.setBackgroundColor(MonetUtil.getProgressColor(this.darkMode));
        this.fab.setOutlineProvider(new OvalOutlineProvider());
        this.fab.setClipToOutline(true);
        this.bottomNav.setBackgroundColor(MonetUtil.getToolbarColor(this.darkMode));
        this.bottomNavItemOne.setImageTintList(ColorStateList.valueOf(primaryTextColor));
        this.bottomNavItemTwo.setImageTintList(ColorStateList.valueOf(primaryTextColor));
        this.bottomNavItemThree.setImageTintList(ColorStateList.valueOf(primaryTextColor));
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }
}
