package com.resync.app.ui.viewholders.posts.simple_swipe;

import android.view.View;
import androidx.appcompat.widget.LinearLayoutCompat;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
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

/* JADX INFO: loaded from: classes3.dex */
public class SimpleSwipeHolder_ViewBinding implements Unbinder {
    private SimpleSwipeHolder target;
    private View upvoteView;
    private View downvoteView;
    private View saveView;
    private View commentView;
    private View downloadView;
    private View moreView;

    class UpvoteClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SimpleSwipeHolder boundTarget;

        UpvoteClickListener(SimpleSwipeHolder simpleSwipeHolder) {
            this.boundTarget = simpleSwipeHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onBottomButtonPressed(view);
        }
    }

    class DownvoteClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SimpleSwipeHolder boundTarget;

        DownvoteClickListener(SimpleSwipeHolder simpleSwipeHolder) {
            this.boundTarget = simpleSwipeHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onBottomButtonPressed(view);
        }
    }

    class SaveClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SimpleSwipeHolder boundTarget;

        SaveClickListener(SimpleSwipeHolder simpleSwipeHolder) {
            this.boundTarget = simpleSwipeHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onBottomButtonPressed(view);
        }
    }

    class CommentClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SimpleSwipeHolder boundTarget;

        CommentClickListener(SimpleSwipeHolder simpleSwipeHolder) {
            this.boundTarget = simpleSwipeHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onBottomButtonPressed(view);
        }
    }

    class DownloadClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SimpleSwipeHolder boundTarget;

        DownloadClickListener(SimpleSwipeHolder simpleSwipeHolder) {
            this.boundTarget = simpleSwipeHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onBottomButtonPressed(view);
        }
    }

    class MoreClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SimpleSwipeHolder boundTarget;

        MoreClickListener(SimpleSwipeHolder simpleSwipeHolder) {
            this.boundTarget = simpleSwipeHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onBottomButtonPressed(view);
        }
    }

    public SimpleSwipeHolder_ViewBinding(SimpleSwipeHolder simpleSwipeHolder, View view) {
        this.target = simpleSwipeHolder;
        simpleSwipeHolder.subView = (SubView) Utils.findRequiredViewAsType(view, R.id.subview, "field 'subView'", SubView.class);
        simpleSwipeHolder.contentWrapper = (LinearLayoutCompat) Utils.findRequiredViewAsType(view, R.id.contet_wrapper, "field 'contentWrapper'", LinearLayoutCompat.class);
        simpleSwipeHolder.title = (PostTitleTextView) Utils.findRequiredViewAsType(view, R.id.title, "field 'title'", PostTitleTextView.class);
        simpleSwipeHolder.description = (PostDescriptionTextView) Utils.findRequiredViewAsType(view, R.id.description, "field 'description'", PostDescriptionTextView.class);
        simpleSwipeHolder.buttonWrapper = (InsetLinearLayout) Utils.findRequiredViewAsType(view, R.id.button_wrapper, "field 'buttonWrapper'", InsetLinearLayout.class);
        View viewC = Utils.findRequiredView(view, R.id.upvote, "field 'buttonUpvote' and method 'onBottomButtonPressed'");
        simpleSwipeHolder.buttonUpvote = (UpvoteButton) Utils.castView(viewC, R.id.upvote, "field 'buttonUpvote'", UpvoteButton.class);
        this.upvoteView = viewC;
        viewC.setOnClickListener(new UpvoteClickListener(simpleSwipeHolder));
        View viewC2 = Utils.findRequiredView(view, R.id.downvote, "field 'buttonDownvote' and method 'onBottomButtonPressed'");
        simpleSwipeHolder.buttonDownvote = (DownvoteButton) Utils.castView(viewC2, R.id.downvote, "field 'buttonDownvote'", DownvoteButton.class);
        this.downvoteView = viewC2;
        viewC2.setOnClickListener(new DownvoteClickListener(simpleSwipeHolder));
        View viewC3 = Utils.findRequiredView(view, R.id.save, "field 'buttonSave' and method 'onBottomButtonPressed'");
        simpleSwipeHolder.buttonSave = (SaveButton) Utils.castView(viewC3, R.id.save, "field 'buttonSave'", SaveButton.class);
        this.saveView = viewC3;
        viewC3.setOnClickListener(new SaveClickListener(simpleSwipeHolder));
        View viewC4 = Utils.findRequiredView(view, R.id.comment, "field 'buttonComment' and method 'onBottomButtonPressed'");
        simpleSwipeHolder.buttonComment = (CommentButton) Utils.castView(viewC4, R.id.comment, "field 'buttonComment'", CommentButton.class);
        this.commentView = viewC4;
        viewC4.setOnClickListener(new CommentClickListener(simpleSwipeHolder));
        View viewC5 = Utils.findRequiredView(view, R.id.download, "field 'buttonDownload' and method 'onBottomButtonPressed'");
        simpleSwipeHolder.buttonDownload = (DownloadButton) Utils.castView(viewC5, R.id.download, "field 'buttonDownload'", DownloadButton.class);
        this.downloadView = viewC5;
        viewC5.setOnClickListener(new DownloadClickListener(simpleSwipeHolder));
        View viewC6 = Utils.findRequiredView(view, R.id.more, "field 'buttonMore' and method 'onBottomButtonPressed'");
        simpleSwipeHolder.buttonMore = (MoreButton) Utils.castView(viewC6, R.id.more, "field 'buttonMore'", MoreButton.class);
        this.moreView = viewC6;
        viewC6.setOnClickListener(new MoreClickListener(simpleSwipeHolder));
        simpleSwipeHolder.mediaView = (SmartMediaView) Utils.findRequiredViewAsType(view, R.id.mediaview, "field 'mediaView'", SmartMediaView.class);
        simpleSwipeHolder.videoContainer = (SimpleSwipeVideoContainer) Utils.findRequiredViewAsType(view, R.id.video_container, "field 'videoContainer'", SimpleSwipeVideoContainer.class);
        simpleSwipeHolder.htmlView = (HtmlTextView) Utils.findRequiredViewAsType(view, R.id.html, "field 'htmlView'", HtmlTextView.class);
    }

    @Override
    public void unbind() {
        SimpleSwipeHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        target.mediaView = null;
        this.upvoteView.setOnClickListener(null);
        this.upvoteView = null;
        this.downvoteView.setOnClickListener(null);
        this.downvoteView = null;
        this.saveView.setOnClickListener(null);
        this.saveView = null;
        this.commentView.setOnClickListener(null);
        this.commentView = null;
        this.downloadView.setOnClickListener(null);
        this.downloadView = null;
        this.moreView.setOnClickListener(null);
        this.moreView = null;
    }
}
