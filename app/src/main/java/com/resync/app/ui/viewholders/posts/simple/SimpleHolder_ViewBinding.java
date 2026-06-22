package com.resync.app.ui.viewholders.posts.simple;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.SubView;
import com.resync.app.ui.views.posts.simple.CommentChip;
import com.resync.app.ui.views.posts.simple.ImageFrameLayout;
import com.resync.app.ui.views.posts.simple.IndicatorView;
import com.resync.app.ui.views.posts.simple.PostThumbnailView;
import com.resync.app.ui.views.posts.simple.SaveButton;
import com.resync.app.ui.views.posts.simple.SimpleButton;
import com.resync.app.ui.views.posts.simple.SimpleLinearLayout;
import com.resync.app.ui.views.posts.simple.SimplePostPreviewHelper;
import com.resync.app.ui.views.posts.simple.SimpleSelftextPreviewTextView;
import com.resync.app.ui.views.posts.simple.TwitterEmbedded;
import com.resync.app.ui.views.posts.simple.UrlView;
import com.resync.app.ui.views.posts.simple.VotingChip;
import com.resync.app.ui.views.text.spannable.children.image_components.awards.AwardsTextView;
import com.resync.app.ui.views.text.spannable.children.image_components.flair.PostFlairTextView;
import com.resync.app.ui.views.text.spannable.children.posts.PostDescriptionTextView;
import com.resync.app.ui.views.text.spannable.children.posts.PostTitleTextView;

/* JADX INFO: loaded from: classes3.dex */
public class SimpleHolder_ViewBinding implements Unbinder {
    private SimpleHolder target;
    private View rootView;
    private View subviewView;
    private View descriptionView;
    private View imageView;
    private View thumbnailView;
    private View commentsView;
    private View shareView;
    private View modView;
    private View hideView;
    private View moreView;

    class ShareClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SimpleHolder boundTarget;

        ShareClickListener(SimpleHolder simpleHolder) {
            this.boundTarget = simpleHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onShareClicked();
        }
    }

    class ShareLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ SimpleHolder boundTarget;

        ShareLongClickListener(SimpleHolder simpleHolder) {
            this.boundTarget = simpleHolder;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.boundTarget.onShareLongClicked();
        }
    }

    class ModClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SimpleHolder boundTarget;

        ModClickListener(SimpleHolder simpleHolder) {
            this.boundTarget = simpleHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onModMenuClicked();
        }
    }

    class HideClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SimpleHolder boundTarget;

        HideClickListener(SimpleHolder simpleHolder) {
            this.boundTarget = simpleHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onHideClicked();
        }
    }

    class MoreClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SimpleHolder boundTarget;

        MoreClickListener(SimpleHolder simpleHolder) {
            this.boundTarget = simpleHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onMoreClicked();
        }
    }

    class RootClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SimpleHolder boundTarget;

        RootClickListener(SimpleHolder simpleHolder) {
            this.boundTarget = simpleHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onRootClicked();
        }
    }

    class RootLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ SimpleHolder boundTarget;

        RootLongClickListener(SimpleHolder simpleHolder) {
            this.boundTarget = simpleHolder;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.boundTarget.onRootLongClicked();
        }
    }

    class SubviewClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SimpleHolder boundTarget;

        SubviewClickListener(SimpleHolder simpleHolder) {
            this.boundTarget = simpleHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onSubviewClicked();
        }
    }

    class DescriptionClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SimpleHolder boundTarget;

        DescriptionClickListener(SimpleHolder simpleHolder) {
            this.boundTarget = simpleHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onDescriptionClicked();
        }
    }

    class ImageClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SimpleHolder boundTarget;

        ImageClickListener(SimpleHolder simpleHolder) {
            this.boundTarget = simpleHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onImageClicked();
        }
    }

    class ImageLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ SimpleHolder boundTarget;

        ImageLongClickListener(SimpleHolder simpleHolder) {
            this.boundTarget = simpleHolder;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.boundTarget.onImageLongClicked();
        }
    }

    class ThumbnailClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SimpleHolder boundTarget;

        ThumbnailClickListener(SimpleHolder simpleHolder) {
            this.boundTarget = simpleHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onThumbnailClicked();
        }
    }

    class ThumbnailLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ SimpleHolder boundTarget;

        ThumbnailLongClickListener(SimpleHolder simpleHolder) {
            this.boundTarget = simpleHolder;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.boundTarget.onThumbnailLongClicked();
        }
    }

    class CommentsClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SimpleHolder boundTarget;

        CommentsClickListener(SimpleHolder simpleHolder) {
            this.boundTarget = simpleHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onCommentsClicked();
        }
    }

    public SimpleHolder_ViewBinding(SimpleHolder simpleHolder, View view) {
        this.target = simpleHolder;
        View viewC = Utils.findRequiredView(view, R.id.root, "field 'root', method 'onRootClicked', and method 'onRootLongClicked'");
        simpleHolder.root = (SimpleLinearLayout) Utils.castView(viewC, R.id.root, "field 'root'", SimpleLinearLayout.class);
        this.rootView = viewC;
        viewC.setOnClickListener(new RootClickListener(simpleHolder));
        viewC.setOnLongClickListener(new RootLongClickListener(simpleHolder));
        View viewC2 = Utils.findRequiredView(view, R.id.subview, "field 'subview' and method 'onSubviewClicked'");
        simpleHolder.subview = (SubView) Utils.castView(viewC2, R.id.subview, "field 'subview'", SubView.class);
        this.subviewView = viewC2;
        viewC2.setOnClickListener(new SubviewClickListener(simpleHolder));
        simpleHolder.descriptionWrapper = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.description_wrapper, "field 'descriptionWrapper'", ViewGroup.class);
        View viewC3 = Utils.findRequiredView(view, R.id.description, "field 'description' and method 'onDescriptionClicked'");
        simpleHolder.description = (PostDescriptionTextView) Utils.castView(viewC3, R.id.description, "field 'description'", PostDescriptionTextView.class);
        this.descriptionView = viewC3;
        viewC3.setOnClickListener(new DescriptionClickListener(simpleHolder));
        simpleHolder.titleThumbnailWrapper = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.title_thumbnail_wrapper, "field 'titleThumbnailWrapper'", ViewGroup.class);
        simpleHolder.title = (PostTitleTextView) Utils.findRequiredViewAsType(view, R.id.title, "field 'title'", PostTitleTextView.class);
        simpleHolder.titleInnerWrapper = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.title_inner_wrapper, "field 'titleInnerWrapper'", LinearLayout.class);
        simpleHolder.imageWrapper = (ImageFrameLayout) Utils.findRequiredViewAsType(view, R.id.image_wrapper, "field 'imageWrapper'", ImageFrameLayout.class);
        View viewC4 = Utils.findRequiredView(view, R.id.image, "field 'image', method 'onImageClicked', and method 'onImageLongClicked'");
        simpleHolder.image = (SimplePostPreviewHelper) Utils.castView(viewC4, R.id.image, "field 'image'", SimplePostPreviewHelper.class);
        this.imageView = viewC4;
        viewC4.setOnClickListener(new ImageClickListener(simpleHolder));
        viewC4.setOnLongClickListener(new ImageLongClickListener(simpleHolder));
        simpleHolder.urlLabel = (UrlView) Utils.findRequiredViewAsType(view, R.id.url_label, "field 'urlLabel'", UrlView.class);
        simpleHolder.indicator = (IndicatorView) Utils.findRequiredViewAsType(view, R.id.indicator, "field 'indicator'", IndicatorView.class);
        View viewC5 = Utils.findRequiredView(view, R.id.thumbnail, "field 'thumbnail', method 'onThumbnailClicked', and method 'onThumbnailLongClicked'");
        simpleHolder.thumbnail = (PostThumbnailView) Utils.castView(viewC5, R.id.thumbnail, "field 'thumbnail'", PostThumbnailView.class);
        this.thumbnailView = viewC5;
        viewC5.setOnClickListener(new ThumbnailClickListener(simpleHolder));
        viewC5.setOnLongClickListener(new ThumbnailLongClickListener(simpleHolder));
        simpleHolder.selftext = (SimpleSelftextPreviewTextView) Utils.findRequiredViewAsType(view, R.id.selftext, "field 'selftext'", SimpleSelftextPreviewTextView.class);
        simpleHolder.twitterEmbedded = (TwitterEmbedded) Utils.findRequiredViewAsType(view, R.id.twitter, "field 'twitterEmbedded'", TwitterEmbedded.class);
        simpleHolder.awardFlairWrapper = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.flair_award_wrapper, "field 'awardFlairWrapper'", ViewGroup.class);
        simpleHolder.awards = (AwardsTextView) Utils.findRequiredViewAsType(view, R.id.awards, "field 'awards'", AwardsTextView.class);
        simpleHolder.flair = (PostFlairTextView) Utils.findRequiredViewAsType(view, R.id.flair, "field 'flair'", PostFlairTextView.class);
        simpleHolder.voting = (VotingChip) Utils.findRequiredViewAsType(view, R.id.voting, "field 'voting'", VotingChip.class);
        View viewC6 = Utils.findRequiredView(view, R.id.comments, "field 'comments' and method 'onCommentsClicked'");
        simpleHolder.comments = (CommentChip) Utils.castView(viewC6, R.id.comments, "field 'comments'", CommentChip.class);
        this.commentsView = viewC6;
        viewC6.setOnClickListener(new CommentsClickListener(simpleHolder));
        simpleHolder.buttonsWrapper = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.buttons_wrapper, "field 'buttonsWrapper'", ViewGroup.class);
        simpleHolder.buttonsWrapperLeft = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.buttons_wrapper_left, "field 'buttonsWrapperLeft'", ViewGroup.class);
        simpleHolder.buttonsWrapperRight = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.buttons_wrapper_right, "field 'buttonsWrapperRight'", ViewGroup.class);
        View viewC7 = Utils.findRequiredView(view, R.id.share, "field 'share', method 'onShareClicked', and method 'onShareLongClicked'");
        simpleHolder.share = (SimpleButton) Utils.castView(viewC7, R.id.share, "field 'share'", SimpleButton.class);
        this.shareView = viewC7;
        viewC7.setOnClickListener(new ShareClickListener(simpleHolder));
        viewC7.setOnLongClickListener(new ShareLongClickListener(simpleHolder));
        View viewC8 = Utils.findRequiredView(view, R.id.mod, "field 'mod' and method 'onModMenuClicked'");
        simpleHolder.mod = (SimpleButton) Utils.castView(viewC8, R.id.mod, "field 'mod'", SimpleButton.class);
        this.modView = viewC8;
        viewC8.setOnClickListener(new ModClickListener(simpleHolder));
        View viewC9 = Utils.findRequiredView(view, R.id.hide, "field 'hide' and method 'onHideClicked'");
        simpleHolder.hide = (SimpleButton) Utils.castView(viewC9, R.id.hide, "field 'hide'", SimpleButton.class);
        this.hideView = viewC9;
        viewC9.setOnClickListener(new HideClickListener(simpleHolder));
        simpleHolder.save = (SaveButton) Utils.findRequiredViewAsType(view, R.id.save, "field 'save'", SaveButton.class);
        View viewC10 = Utils.findRequiredView(view, R.id.more, "field 'more' and method 'onMoreClicked'");
        simpleHolder.more = (SimpleButton) Utils.castView(viewC10, R.id.more, "field 'more'", SimpleButton.class);
        this.moreView = viewC10;
        viewC10.setOnClickListener(new MoreClickListener(simpleHolder));
    }

    @Override
    public void unbind() {
        SimpleHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        target.mod = null;
        target.more = null;
        this.rootView.setOnClickListener(null);
        this.rootView.setOnLongClickListener(null);
        this.rootView = null;
        this.subviewView.setOnClickListener(null);
        this.subviewView = null;
        this.descriptionView.setOnClickListener(null);
        this.descriptionView = null;
        this.imageView.setOnClickListener(null);
        this.imageView.setOnLongClickListener(null);
        this.imageView = null;
        this.thumbnailView.setOnClickListener(null);
        this.thumbnailView.setOnLongClickListener(null);
        this.thumbnailView = null;
        this.commentsView.setOnClickListener(null);
        this.commentsView = null;
        this.shareView.setOnClickListener(null);
        this.shareView.setOnLongClickListener(null);
        this.shareView = null;
        this.modView.setOnClickListener(null);
        this.modView = null;
        this.hideView.setOnClickListener(null);
        this.hideView = null;
        this.moreView.setOnClickListener(null);
        this.moreView = null;
    }
}
