package com.resync.app.ui.viewholders.posts.simple_swipe;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.FragmentManager;
import com.resync.app.R;
import com.resync.app.jobs.media.DownloadMediaJob;
import com.resync.app.jobs.media.DownloadRedditVideoJob;
import com.resync.app.ui.util.animation.recycler.helper.AnimationHelper;
import com.resync.app.ui.views.buttons.CommentButton;
import com.resync.app.ui.views.buttons.DownloadButton;
import com.resync.app.ui.views.buttons.DownvoteButton;
import com.resync.app.ui.views.buttons.MoreButton;
import com.resync.app.ui.views.buttons.SaveButton;
import com.resync.app.ui.views.buttons.UpvoteButton;
import com.resync.app.ui.views.core.SubView;
import com.resync.app.ui.views.insetted.InsetLinearLayout;
import com.resync.app.ui.views.media.SmartMediaView;
import com.resync.app.ui.views.posts.simple_swipe.SimpleSwipeVideoContainer;
import com.resync.app.ui.views.text.spannable.children.HtmlTextView;
import com.resync.app.ui.views.text.spannable.children.posts.PostDescriptionTextView;
import com.resync.app.ui.views.text.spannable.children.posts.PostTitleTextView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.widgets.SimpleExoPlayerTextureView;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.PostMoreHelper;
import com.resync.app.holders.BasePostViewHolder;
import com.resync.app.util.Logger;
import com.resync.app.util.RippleUtils;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkTypeHelper;
import com.resync.app.presenters.BaseCursorPresenter;
import com.resync.app.presenters.PostsPresenter;

/* JADX INFO: loaded from: classes3.dex */
public class SimpleSwipeHolder extends BasePostViewHolder {

    CommentButton buttonComment;

    DownloadButton buttonDownload;

    DownvoteButton buttonDownvote;

    MoreButton buttonMore;

    SaveButton buttonSave;

    UpvoteButton buttonUpvote;

    InsetLinearLayout buttonWrapper;

    LinearLayoutCompat contentWrapper;

    PostDescriptionTextView description;

    HtmlTextView htmlView;

    Rect videoRect;

    SmartMediaView mediaView;

    SubView subView;

    PostTitleTextView title;

    SimpleSwipeVideoContainer videoContainer;

    private SimpleSwipeHolder(Context context, BaseCursorPresenter baseCursorPresenter, View view, int uiMode) {
        super(context, view, uiMode);
        this.videoRect = new Rect();
        setPresenter(baseCursorPresenter);
    }

    public static SimpleSwipeHolder create(Context context, ViewGroup viewGroup, BaseCursorPresenter baseCursorPresenter, int uiMode) {
        return new SimpleSwipeHolder(context, baseCursorPresenter, LayoutInflater.from(context).inflate(R.layout.holder_simple_swipe, viewGroup, false), uiMode);
    }

    @Override // com.resync.app.holders.BasePostViewHolder, com.resync.app.holders.RedditViewHolder
    public void recycle() {
        super.recycle();
        this.mediaView.recycle();
        this.htmlView.setVisibility(8);
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public void bindPost(Post post, int position) {
        float alpha = 1.0f;
        float expandedAlpha = ((PostsPresenter) getPresenter()).isExpanded() ? 1.0f : 0.0f;
        if (!StringUtils.isEmpty(post.getPreviewUrl()) && post.getSyncType() != 2) {
            alpha = expandedAlpha;
        }
        this.contentWrapper.setAlpha(alpha);
        this.buttonWrapper.setAlpha(alpha);
        if (getPost() != null && post.equals(getPost()) && post.hasSameUserTag(getPost())) {
            Logger.verbose("BAILED SimpleSwipeHolder: " + post.equals(getPost()));
            return;
        }
        super.bindPost(post, position);
        this.contentWrapper.setBackgroundColor(ColorUtils.setAlphaComponent(MonetUtil.getBackgroundColorAuto(), 230));
        this.buttonWrapper.setBackgroundColor(ColorUtils.setAlphaComponent(MonetUtil.getBackgroundColorAuto(), 230));
        this.buttonUpvote.setBackground(RippleUtils.getSelectableBackground());
        this.buttonDownvote.setBackground(RippleUtils.getSelectableBackground());
        this.buttonSave.setBackground(RippleUtils.getSelectableBackground());
        this.buttonComment.setBackground(RippleUtils.getSelectableBackground());
        this.buttonDownload.setBackground(RippleUtils.getSelectableBackground());
        this.buttonMore.setBackground(RippleUtils.getSelectableBackground());
        if (shouldShowSubreddit()) {
            this.subView.setVisibility(0);
            this.subView.setSubreddit(post.getSubreddit());
        } else {
            this.subView.setVisibility(8);
        }
        this.title.bind(post);
        this.description.bind(getPost(), getUiMode(), shouldShowSubreddit(), false, true);
        this.mediaView.setPresenter((PostsPresenter) getPresenter());
        this.mediaView.bind(post);
        this.videoContainer.bind(post);
        this.buttonUpvote.setActive(post.isUpvoted());
        this.buttonDownvote.setActive(post.isDownvoted());
        this.buttonSave.setActive(post.isSaved());
        if (post.getSyncType() == 2 && StringUtils.isNotEmpty(post.getSelftextProcessed())) {
            this.htmlView.setVisibility(0);
            this.htmlView.setHtml(post.getSelftextProcessed());
        }
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public boolean canAutoplay() {
        return this.videoContainer.shouldAutoplay();
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public SimpleExoPlayerTextureView getTextureView() {
        for (int i = 0; i < this.videoContainer.getChildCount(); i++) {
            if (this.videoContainer.getChildAt(i) instanceof SimpleExoPlayerTextureView) {
                return (SimpleExoPlayerTextureView) this.videoContainer.getChildAt(i);
            }
        }
        return null;
    }

    protected void onBottomButtonPressed(View view) {
        if (view.getId() == R.id.upvote) {
            if (ConnectivityHelper.isConnected(this.context)) {
                AnimationHelper.performAction(this.context, getPost(), 0);
                return;
            } else {
                StaticToast.showWithContext(this.context, "You must be online to perform post actions");
                return;
            }
        }
        if (view.getId() == R.id.downvote) {
            if (ConnectivityHelper.isConnected(this.context)) {
                AnimationHelper.performAction(this.context, getPost(), 1);
                return;
            } else {
                StaticToast.showWithContext(this.context, "You must be online to perform post actions");
                return;
            }
        }
        if (view.getId() == R.id.save) {
            if (ConnectivityHelper.isConnected(this.context)) {
                AnimationHelper.performAction(this.context, getPost(), 2);
                return;
            } else {
                StaticToast.showWithContext(this.context, "You must be online to perform post actions");
                return;
            }
        }
        if (view.getId() == R.id.comment) {
            getPresenter().openComments(getPost());
            return;
        }
        if (view.getId() != R.id.download) {
            if (view.getId() == R.id.more) {
                PostMoreHelper.showMoreSheet(this.context, getUiMode(), getPost());
                return;
            }
            return;
        }
        FragmentManager fragmentManagerG = ContextHelper.getFragmentManager(this.context);
        if (LinkTypeHelper.isRedditVideo(getPost().getUrl())) {
            DownloadRedditVideoJob.download(fragmentManagerG, getPost().getTitle(), getPost().getSubreddit(), getPost().getUrl(), 1);
            return;
        }
        if (LinkTypeHelper.isImage(getPost().getUrl())) {
            DownloadMediaJob.download(fragmentManagerG, getPost().getTitle(), getPost().getSubreddit(), getPost().getUrl(), 1);
        } else if (StringUtils.isNotEmpty(getPost().getPreviewUrl())) {
            DownloadMediaJob.download(fragmentManagerG, getPost().getTitle(), getPost().getSubreddit(), getPost().getPreviewUrl(), 1);
        } else {
            StaticToast.show("This post has no media to download");
        }
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public void attachTextureView(SimpleExoPlayerTextureView simpleExoPlayerTextureView) {
        if (simpleExoPlayerTextureView.isAttachedTo(this.videoContainer)) {
            return;
        }
        simpleExoPlayerTextureView.stopAndDetach();
        this.videoContainer.addView(simpleExoPlayerTextureView);
        simpleExoPlayerTextureView.play(getPost(), false, false);
    }
}
