package com.resync.app.ui.views.comments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewPropertyAnimator;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.MonetUtil;
import okhttp3.internal.http.HttpStatusCodesKt;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.ui.interfaces.DarkModeAware;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes3.dex */
public class CommentsNavigationView extends LinearLayout implements DarkModeAware {

    AppCompatImageView mCollapse;

    AppCompatImageView mIcon;

    ViewGroup mInnerWrapper;

    AppCompatImageView mNext;

    AppCompatImageView mPrevious;

    AppCompatImageView mReply;

    class WrapperOutlineProvider extends ViewOutlineProvider {
        WrapperOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), SizeHelper.dpToPx(20));
        }
    }

    class ReplyOutlineProvider extends ViewOutlineProvider {
        ReplyOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), SizeHelper.dpToPx(20));
        }
    }

    class CollapseOutlineProvider extends ViewOutlineProvider {
        CollapseOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), SizeHelper.dpToPx(20));
        }
    }

    public CommentsNavigationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LinearLayout.inflate(getContext(), R.layout.view_comments_navigation, this);
        ButterKnife.bind(this);
        if (!SettingsSingleton.getSettings().commentNavBar) {
            setVisibility(8);
        }
        applyColors();
    }

    private void resetView(View view) {
        view.animate().cancel();
        view.clearAnimation();
        view.setAnimation(null);
        view.setScaleX(1.0f);
        view.setScaleY(1.0f);
        view.setTranslationX(0.0f);
        view.setTranslationY(0.0f);
    }

    public void animateIn() {
        Logger.verbose("ANIMATE IN");
        if (getVisibility() == 8 && SettingsSingleton.getSettings().commentNavBar) {
            setVisibility(0);
            if (isAttachedToWindow()) {
                this.mInnerWrapper.setScaleX(0.0f);
                this.mInnerWrapper.setScaleY(0.0f);
                this.mReply.setScaleX(0.0f);
                this.mReply.setScaleY(0.0f);
                this.mReply.setTranslationY(0.0f);
                this.mCollapse.setTranslationY(0.0f);
                this.mCollapse.setScaleX(0.0f);
                this.mCollapse.setScaleY(0.0f);
                this.mPrevious.setTranslationY(-SizeHelper.dpToPxFloat(40));
                this.mNext.setTranslationY(SizeHelper.dpToPxFloat(40));
                ViewPropertyAnimator viewPropertyAnimatorScaleY = this.mInnerWrapper.animate().scaleX(1.0f).scaleY(1.0f);
                long duration = HttpStatusCodesKt.HTTP_MULT_CHOICE;
                viewPropertyAnimatorScaleY.setDuration(duration).setInterpolator(new FastOutSlowInInterpolator());
                this.mPrevious.animate().translationY(1.0f).setDuration(duration).setInterpolator(new FastOutSlowInInterpolator()).setStartDelay(duration);
                this.mNext.animate().translationY(1.0f).setDuration(duration).setInterpolator(new FastOutSlowInInterpolator()).setStartDelay(duration);
                this.mCollapse.animate().scaleX(1.0f).scaleY(1.0f).setDuration(duration).setInterpolator(new FastOutSlowInInterpolator()).setStartDelay(duration);
                this.mReply.animate().scaleX(1.0f).scaleY(1.0f).setDuration(duration).setInterpolator(new FastOutSlowInInterpolator()).setStartDelay(duration);
            }
        }
    }

    public void resetAnimations() {
        resetView(this.mInnerWrapper);
        resetView(this.mPrevious);
        resetView(this.mNext);
        resetView(this.mCollapse);
        resetView(this.mReply);
        resetView(this.mInnerWrapper);
    }

    public void setModeIcon(int resId) {
        this.mIcon.setImageResource(resId);
    }

    public void applyColors() {
        this.mInnerWrapper.setBackgroundColor(MonetUtil.getProgressColorAuto());
        int accent2ActiveColor = MonetUtil.getAccent2ActiveColorAuto();
        this.mReply.setBackgroundColor(accent2ActiveColor);
        this.mCollapse.setBackgroundColor(accent2ActiveColor);
        int accentSurfaceColor = MonetUtil.getAccentSurfaceColorAuto();
        this.mPrevious.setImageTintList(ColorStateList.valueOf(accentSurfaceColor));
        this.mNext.setImageTintList(ColorStateList.valueOf(accentSurfaceColor));
        this.mIcon.setImageTintList(ColorStateList.valueOf(accentSurfaceColor));
        int accent2Color = MonetUtil.getAccent2ColorAuto();
        this.mReply.setImageTintList(ColorStateList.valueOf(accent2Color));
        this.mCollapse.setImageTintList(ColorStateList.valueOf(accent2Color));
        this.mInnerWrapper.setOutlineProvider(new WrapperOutlineProvider());
        this.mInnerWrapper.setClipToOutline(true);
        this.mReply.setOutlineProvider(new ReplyOutlineProvider());
        this.mReply.setClipToOutline(true);
        this.mCollapse.setOutlineProvider(new CollapseOutlineProvider());
        this.mCollapse.setClipToOutline(true);
        setElevation(SizeHelper.dpToPxFloat(2));
        setOrientation(0);
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }
}
