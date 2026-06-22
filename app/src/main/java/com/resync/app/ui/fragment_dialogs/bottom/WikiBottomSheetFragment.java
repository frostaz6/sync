package com.resync.app.ui.fragment_dialogs.bottom;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Property;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import androidx.appcompat.widget.PopupMenu;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.text.spannable.children.HtmlTableView;
import com.resync.app.helpers.ui.PopupMenuHelper;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.dialogs.CustomBottomSheetDialogFragment;
import com.resync.app.util.Logger;
import com.resync.app.util.TestUtils;
import com.resync.app.net.oauth.OAuthWikiRequest;

/* JADX INFO: loaded from: classes.dex */
public class WikiBottomSheetFragment extends CustomBottomSheetDialogFragment implements Response.Listener<String>, Response.ErrorListener {

    @BindView(R.id.fragment_wiki_async_wrapper)
    RelativeLayout mAsyncWrapper;

    @BindView(R.id.fragment_wiki_content)
    HtmlTableView mContent;

    @BindView(R.id.header)
    SheetHeaderView mHeader;

    @BindView(R.id.fragment_wiki_main_wrapper)
    RelativeLayout mMainWrapper;

    @BindView(R.id.fragment_wiki_main_root_wrapper)
    RelativeLayout mRootWrapper;

    class ShareClickListener implements View.OnClickListener {

        class ShareMenuClickListener implements PopupMenu.OnMenuItemClickListener {

            final /* synthetic */ MenuItem shareItem;

            ShareMenuClickListener(MenuItem menuItem) {
                this.shareItem = menuItem;
            }

            @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (!menuItem.equals(this.shareItem)) {
                    return true;
                }
                RedditHelper.shareText(WikiBottomSheetFragment.this.getContext(), "https://reddit.com/r/" + WikiBottomSheetFragment.this.getSubreddit() + "/wiki/" + WikiBottomSheetFragment.this.getWikiUrl());
                return true;
            }
        }

        ShareClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            PopupMenu popupMenuC = PopupMenuHelper.create(view, 80);
            popupMenuC.setOnMenuItemClickListener(new ShareMenuClickListener(popupMenuC.getMenu().add("Share")));
            popupMenuC.show();
        }
    }

    class ExpandPreDrawListener implements ViewTreeObserver.OnPreDrawListener {

        final /* synthetic */ int targetHeight;

        class HeightAnimatorListener implements ValueAnimator.AnimatorUpdateListener {
            HeightAnimatorListener() {
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int iIntValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                ViewGroup.LayoutParams layoutParams = WikiBottomSheetFragment.this.mRootWrapper.getLayoutParams();
                layoutParams.height = iIntValue;
                WikiBottomSheetFragment.this.mRootWrapper.setLayoutParams(layoutParams);
            }
        }

        ExpandPreDrawListener(int targetHeight) {
            this.targetHeight = targetHeight;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            int target;
            int height = WikiBottomSheetFragment.this.mRootWrapper.getHeight();
            Logger.verbose("Previous height: " + this.targetHeight);
            Logger.verbose("New height: " + height);
            if (height == 0 || (target = this.targetHeight) == 0 || height <= target) {
                return true;
            }
            WikiBottomSheetFragment.this.mRootWrapper.getViewTreeObserver().removeOnPreDrawListener(this);
            ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(this.targetHeight, height);
            valueAnimatorOfInt.addUpdateListener(new HeightAnimatorListener());
            WikiBottomSheetFragment.this.mMainWrapper.setAlpha(0.0f);
            ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(WikiBottomSheetFragment.this.mMainWrapper, (Property<View, Float>) View.ALPHA, 1.0f);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(220L);
            animatorSet.setInterpolator(new FastOutSlowInInterpolator());
            animatorSet.playTogether(valueAnimatorOfInt, objectAnimatorOfFloat);
            animatorSet.start();
            return false;
        }
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public String getSubreddit() {
        return com.resync.app.util.links.LinkHandler.getCommentsSubreddit(getUrl());
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.fragment_wiki;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.mHeader.setTitle(getSubreddit() + " - " + getWikiUrl());
        if (TestUtils.isRobolectric()) {
            return;
        }
        OAuthRequestManager.send(getActivity(), new OAuthWikiRequest(getActivity(), getSubreddit(), getWikiUrl(), this, this));
    }

    @Override // com.android.volley.Response.ErrorListener
    public void onErrorResponse(VolleyError volleyError) {
        StaticToast.showWithContext(getActivity(), "Error loading wiki");
        dismiss();
    }

    public String getWikiUrl() {
        return com.resync.app.util.links.LinkHandler.getWikiPage(getUrl());
    }

    @Override // com.android.volley.Response.Listener
    /* JADX INFO: renamed from: s4, reason: merged with bridge method [inline-methods] */
    public void onResponse(String str) {
        this.mContent.buildContent(str);
        this.mContent.setForeground(null);
        this.mContent.setBackground(null);
        this.mHeader.setMoreVisible(true);
        this.mHeader.setMoreClickListener(new ShareClickListener());
        int height = this.mRootWrapper.getHeight();
        this.mAsyncWrapper.setVisibility(8);
        this.mRootWrapper.getViewTreeObserver().addOnPreDrawListener(new ExpandPreDrawListener(height));
        this.mMainWrapper.setVisibility(0);
    }
}
