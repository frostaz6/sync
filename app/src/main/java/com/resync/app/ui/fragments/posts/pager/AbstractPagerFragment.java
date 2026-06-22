package com.resync.app.ui.fragments.posts.pager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.resync.app.R;
import com.resync.app.ui.fragments.RedditFragment;
import com.resync.app.ui.fragments.posts.HorizontalPostsFragment;
import com.resync.app.ui.fragments.CommentsFragment;
import com.resync.app.ui.views.behaviour.CommentsBottomSheetBehavior;
import com.resync.app.ui.views.buttons.CommentButton;
import com.resync.app.ui.views.buttons.DownloadButton;
import com.resync.app.ui.views.buttons.DownvoteButton;
import com.resync.app.ui.views.buttons.MoreButton;
import com.resync.app.ui.views.buttons.SaveButton;
import com.resync.app.ui.views.buttons.UpvoteButton;
import com.resync.app.ui.views.posts.swipe.SwipeLinearLayout;
import com.resync.app.ui.views.text.BaseTextView;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.managers.SaveManager;
import com.resync.app.managers.VoteManager;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.helpers.PostsHelper;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.presenters.BaseCursorPresenter;
import com.resync.app.presenters.PostsPresenter;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AbstractPagerFragment extends RedditFragment {

    @BindView(R.id.fragment_swipe_bottomsheet)
    LinearLayout mBottomSheetWrapper;

    @BindView(R.id.fragment_swipe_comments)
    CommentButton mButtonComment;

    @BindView(R.id.fragment_swipe_download)
    DownloadButton mButtonDownload;

    @BindView(R.id.fragment_swipe_downvote)
    DownvoteButton mButtonDownvote;

    @BindView(R.id.fragment_swipe_more)
    MoreButton mButtonMore;

    @BindView(R.id.fragment_swipe_save)
    SaveButton mButtonSave;

    @BindView(R.id.fragment_swipe_upvote)
    UpvoteButton mButtonUpvote;

    @BindView(R.id.fragment_swipe_buttons_wrapper)
    LinearLayout mButtonsWrapper;

    @BindView(R.id.fragment_swipe_buttons_wrapper_inner)
    SwipeLinearLayout mButtonsWrapperInner;

    @BindView(R.id.fragment_swipe_comments_holder)
    FrameLayout mCommentsHolder;

    @BindView(R.id.fragment_swipe_desc)
    BaseTextView mDesc;

    @BindView(R.id.fragment_swipe_text_wrapper)
    LinearLayout mTextWrapper;

    @BindView(R.id.fragment_swipe_title)
    BaseTextView mTitle;
    protected CommentsBottomSheetBehavior commentsSheet;

    /* JADX INFO: renamed from: s0, reason: collision with root package name */
    protected BaseCursorPresenter presenter;
    protected Post post;

    class CommentsSheetCallback extends BottomSheetBehavior.BottomSheetCallback {
        CommentsSheetCallback() {
        }

        @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
        public void onSlide(View view, float slideOffset) {
            AbstractPagerFragment.this.mButtonsWrapperInner.setProgress(slideOffset);
        }

        @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
        public void onStateChanged(View view, int newState) {
            if (newState == 3 && AbstractPagerFragment.this.getFragmentManager().findFragmentById(R.id.fragment_swipe_comments_holder) == null) {
                AbstractPagerFragment.this.getFragmentManager().beginTransaction().replace(R.id.fragment_swipe_comments_holder, CommentsFragment.newInstanceForSwipe(AbstractPagerFragment.this.getPost())).commit();
            }
        }
    }

    public void setPresenter(BaseCursorPresenter baseCursorPresenter) {
        this.presenter = baseCursorPresenter;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(getLayoutResId(), viewGroup, false);
        ButterKnife.bind(this, viewInflate);
        return viewInflate;
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: U1 */
    public void onDestroy() {
        Logger.verbose(HorizontalPostsFragment.TAG, "Destroying fragment: " + getClass().getSimpleName());
        super.onDestroy();
    }

    @OnClick
    public void downvotePost(View view) {
        if (ConnectivityHelper.isConnected(getActivity())) {
            VoteManager.voteOnPost(getActivity(), 0, getPost(), -1);
        } else {
            StaticToast.showWithContext(getActivity(), "You must be online to perform post actions");
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.post = (Post) getArguments().getSerializable("Post");
        setChromeVisible(getArguments().getBoolean("CHROME_VISIBLE", false));
        bindPost(this.post);
        CommentsBottomSheetBehavior commentsBottomSheetBehavior = CommentsBottomSheetBehavior.from(this.mBottomSheetWrapper);
        this.commentsSheet = commentsBottomSheetBehavior;
        commentsBottomSheetBehavior.addBottomSheetCallback(new CommentsSheetCallback());
    }

    @OnClick
    public void openComments(View view) {
        if (this.commentsSheet.getState() == 3) {
            this.commentsSheet.setState(4);
        } else {
            this.commentsSheet.setState(3);
        }
    }

    @OnClick
    public void savePost(View view) {
        if (ConnectivityHelper.isConnected(getActivity())) {
            SaveManager.toggleSave(getActivity(), getPost());
        } else {
            StaticToast.showWithContext(getActivity(), "You must be online to perform post actions");
        }
    }

    @OnClick
    public void upvotePost(View view) {
        if (ConnectivityHelper.isConnected(getActivity())) {
            VoteManager.voteOnPost(getActivity(), 0, getPost(), 1);
        } else {
            StaticToast.showWithContext(getActivity(), "You must be online to perform post actions");
        }
    }

    public void bindPost(Post post) {
        setPost(post);
        Logger.verbose("Post: " + getPost());
        PostsHelper.markViewed(getActivity(), post);
        BaseCursorPresenter baseCursorPresenter = this.presenter;
        if (baseCursorPresenter instanceof PostsPresenter) {
            ((PostsPresenter) baseCursorPresenter).getSubredditName();
        }
        this.mTitle.setText(getPost().getTitle());
        this.mButtonUpvote.setActive(getPost().isUpvoted());
        this.mButtonDownvote.setActive(getPost().isDownvoted());
        this.mButtonSave.setActive(getPost().isSaved());
    }

    @OnClick
    public void viewMore(View view) {
    }

    public Post getPost() {
        return this.post;
    }

    public abstract /* synthetic */ void releaseMedia();

    public void setChromeVisible(boolean visible) {
        if (visible) {
            return;
        }
        this.mTextWrapper.setAlpha(0.0f);
        this.mButtonsWrapper.setAlpha(0.0f);
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
