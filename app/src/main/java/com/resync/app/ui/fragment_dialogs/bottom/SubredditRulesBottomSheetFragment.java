package com.resync.app.ui.fragment_dialogs.bottom;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.text.spannable.children.HtmlTableView;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.dialogs.CustomBottomSheetDialogFragment;
import com.resync.app.util.Logger;
import com.resync.app.net.oauth.OAuthRulesRequest;

/* JADX INFO: loaded from: classes.dex */
public class SubredditRulesBottomSheetFragment extends CustomBottomSheetDialogFragment implements Response.Listener<String>, Response.ErrorListener {

    @BindView(R.id.fragment_rules_async_wrapper)
    RelativeLayout mAsyncWrapper;

    @BindView(R.id.fragment_rules_content)
    HtmlTableView mContent;

    @BindView(R.id.fragment_rules_header)
    SheetHeaderView mHeader;

    @BindView(R.id.fragment_rules_main_wrapper)
    RelativeLayout mMainWrapper;

    @BindView(R.id.fragment_rules_main_root_wrapper)
    RelativeLayout mRootWrapper;

    class PreDrawListener implements ViewTreeObserver.OnPreDrawListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ int previousHeight;

        /* JADX INFO: renamed from: com.resync.app.ui.fragment_dialogs.bottom.SubredditRulesBottomSheetFragment$a$a, reason: collision with other inner class name */
        class AnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {
            AnimatorUpdateListener() {
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int iIntValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                ViewGroup.LayoutParams layoutParams = SubredditRulesBottomSheetFragment.this.mRootWrapper.getLayoutParams();
                layoutParams.height = iIntValue;
                SubredditRulesBottomSheetFragment.this.mRootWrapper.setLayoutParams(layoutParams);
            }
        }

        PreDrawListener(int previousHeight) {
            this.previousHeight = previousHeight;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            int previous;
            int height = SubredditRulesBottomSheetFragment.this.mRootWrapper.getHeight();
            Logger.verbose("Previous height: " + this.previousHeight);
            Logger.verbose("New height: " + height);
            if (height == 0 || (previous = this.previousHeight) == 0 || height <= previous) {
                return true;
            }
            SubredditRulesBottomSheetFragment.this.mRootWrapper.getViewTreeObserver().removeOnPreDrawListener(this);
            ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(this.previousHeight, height);
            valueAnimatorOfInt.addUpdateListener(new AnimatorUpdateListener());
            SubredditRulesBottomSheetFragment.this.mMainWrapper.setAlpha(0.0f);
            ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(SubredditRulesBottomSheetFragment.this.mMainWrapper, (Property<View, Float>) View.ALPHA, 1.0f);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(220L);
            animatorSet.setInterpolator(new FastOutSlowInInterpolator());
            animatorSet.playTogether(valueAnimatorOfInt, objectAnimatorOfFloat);
            animatorSet.start();
            return false;
        }
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.fragment_rules;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.mHeader.setTitle("Rules");
        OAuthRequestManager.send(getActivity(), new OAuthRulesRequest(getActivity(), getSubreddit(), this, this));
    }

    @Override // com.android.volley.Response.ErrorListener
    public void onErrorResponse(VolleyError volleyError) {
        if (isDialogShowing()) {
            StaticToast.showWithContext(getActivity(), "Error loading rules");
            dismiss();
        }
    }

    @Override // com.android.volley.Response.Listener
    /* JADX INFO: renamed from: r4, reason: merged with bridge method [inline-methods] */
    public void onResponse(String str) {
        if (isDialogShowing()) {
            this.mContent.setText(RedditHelper.processMarkdownToHtml(null, str));
            this.mContent.setForeground(null);
            this.mContent.setBackground(null);
            int height = this.mRootWrapper.getHeight();
            this.mAsyncWrapper.setVisibility(8);
            this.mRootWrapper.getViewTreeObserver().addOnPreDrawListener(new PreDrawListener(height));
            this.mMainWrapper.setVisibility(0);
        }
    }
}
