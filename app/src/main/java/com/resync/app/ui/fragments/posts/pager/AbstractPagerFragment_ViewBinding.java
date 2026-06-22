package com.resync.app.ui.fragments.posts.pager;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.buttons.CommentButton;
import com.resync.app.ui.views.buttons.DownloadButton;
import com.resync.app.ui.views.buttons.DownvoteButton;
import com.resync.app.ui.views.buttons.MoreButton;
import com.resync.app.ui.views.buttons.SaveButton;
import com.resync.app.ui.views.buttons.UpvoteButton;
import com.resync.app.ui.views.posts.swipe.SwipeLinearLayout;
import com.resync.app.ui.views.text.BaseTextView;

/* JADX INFO: loaded from: classes2.dex */
public class AbstractPagerFragment_ViewBinding implements Unbinder {
    private AbstractPagerFragment target;
    private View viewUpvote;
    private View viewDownvote;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private View viewSave;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private View viewComment;
    private View viewMore;

    class OnUpvoteClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ AbstractPagerFragment target;

        OnUpvoteClickListener(AbstractPagerFragment abstractPagerFragment) {
            this.target = abstractPagerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.upvotePost(view);
        }
    }

    class OnDownvoteClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ AbstractPagerFragment target;

        OnDownvoteClickListener(AbstractPagerFragment abstractPagerFragment) {
            this.target = abstractPagerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.downvotePost(view);
        }
    }

    class OnSaveClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ AbstractPagerFragment target;

        OnSaveClickListener(AbstractPagerFragment abstractPagerFragment) {
            this.target = abstractPagerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.savePost(view);
        }
    }

    class OnCommentsClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ AbstractPagerFragment target;

        OnCommentsClickListener(AbstractPagerFragment abstractPagerFragment) {
            this.target = abstractPagerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.openComments(view);
        }
    }

    class OnMoreClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ AbstractPagerFragment target;

        OnMoreClickListener(AbstractPagerFragment abstractPagerFragment) {
            this.target = abstractPagerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.viewMore(view);
        }
    }

    public AbstractPagerFragment_ViewBinding(AbstractPagerFragment abstractPagerFragment, View view) {
        this.target = abstractPagerFragment;
        abstractPagerFragment.mTitle = (BaseTextView) Utils.findRequiredViewAsType(view, R.id.fragment_swipe_title, "field 'mTitle'", BaseTextView.class);
        abstractPagerFragment.mDesc = (BaseTextView) Utils.findRequiredViewAsType(view, R.id.fragment_swipe_desc, "field 'mDesc'", BaseTextView.class);
        abstractPagerFragment.mTextWrapper = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.fragment_swipe_text_wrapper, "field 'mTextWrapper'", LinearLayout.class);
        abstractPagerFragment.mButtonsWrapper = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.fragment_swipe_buttons_wrapper, "field 'mButtonsWrapper'", LinearLayout.class);
        abstractPagerFragment.mButtonsWrapperInner = (SwipeLinearLayout) Utils.findRequiredViewAsType(view, R.id.fragment_swipe_buttons_wrapper_inner, "field 'mButtonsWrapperInner'", SwipeLinearLayout.class);
        View viewC = Utils.findRequiredView(view, R.id.fragment_swipe_upvote, "field 'mButtonUpvote' and method 'upvotePost'");
        abstractPagerFragment.mButtonUpvote = (UpvoteButton) Utils.castView(viewC, R.id.fragment_swipe_upvote, "field 'mButtonUpvote'", UpvoteButton.class);
        this.viewUpvote = viewC;
        viewC.setOnClickListener(new OnUpvoteClickListener(abstractPagerFragment));
        View viewC2 = Utils.findRequiredView(view, R.id.fragment_swipe_downvote, "field 'mButtonDownvote' and method 'downvotePost'");
        abstractPagerFragment.mButtonDownvote = (DownvoteButton) Utils.castView(viewC2, R.id.fragment_swipe_downvote, "field 'mButtonDownvote'", DownvoteButton.class);
        this.viewDownvote = viewC2;
        viewC2.setOnClickListener(new OnDownvoteClickListener(abstractPagerFragment));
        View viewC3 = Utils.findRequiredView(view, R.id.fragment_swipe_save, "field 'mButtonSave' and method 'savePost'");
        abstractPagerFragment.mButtonSave = (SaveButton) Utils.castView(viewC3, R.id.fragment_swipe_save, "field 'mButtonSave'", SaveButton.class);
        this.viewSave = viewC3;
        viewC3.setOnClickListener(new OnSaveClickListener(abstractPagerFragment));
        View viewC4 = Utils.findRequiredView(view, R.id.fragment_swipe_comments, "field 'mButtonComment' and method 'openComments'");
        abstractPagerFragment.mButtonComment = (CommentButton) Utils.castView(viewC4, R.id.fragment_swipe_comments, "field 'mButtonComment'", CommentButton.class);
        this.viewComment = viewC4;
        viewC4.setOnClickListener(new OnCommentsClickListener(abstractPagerFragment));
        abstractPagerFragment.mButtonDownload = (DownloadButton) Utils.findOptionalViewAsType(view, R.id.fragment_swipe_download, "field 'mButtonDownload'", DownloadButton.class);
        View viewC5 = Utils.findRequiredView(view, R.id.fragment_swipe_more, "field 'mButtonMore' and method 'viewMore'");
        abstractPagerFragment.mButtonMore = (MoreButton) Utils.castView(viewC5, R.id.fragment_swipe_more, "field 'mButtonMore'", MoreButton.class);
        this.viewMore = viewC5;
        viewC5.setOnClickListener(new OnMoreClickListener(abstractPagerFragment));
        abstractPagerFragment.mBottomSheetWrapper = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.fragment_swipe_bottomsheet, "field 'mBottomSheetWrapper'", LinearLayout.class);
        abstractPagerFragment.mCommentsHolder = (FrameLayout) Utils.findRequiredViewAsType(view, R.id.fragment_swipe_comments_holder, "field 'mCommentsHolder'", FrameLayout.class);
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        AbstractPagerFragment abstractPagerFragment = this.target;
        if (abstractPagerFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        abstractPagerFragment.mTitle = null;
        abstractPagerFragment.mDesc = null;
        abstractPagerFragment.mTextWrapper = null;
        abstractPagerFragment.mButtonsWrapper = null;
        abstractPagerFragment.mButtonsWrapperInner = null;
        abstractPagerFragment.mButtonUpvote = null;
        abstractPagerFragment.mButtonDownvote = null;
        abstractPagerFragment.mButtonSave = null;
        abstractPagerFragment.mButtonComment = null;
        abstractPagerFragment.mButtonDownload = null;
        abstractPagerFragment.mButtonMore = null;
        abstractPagerFragment.mBottomSheetWrapper = null;
        abstractPagerFragment.mCommentsHolder = null;
        this.viewUpvote = null;
        this.viewDownvote = null;
        this.viewSave = null;
        this.viewComment = null;
        this.viewMore = null;
    }
}
