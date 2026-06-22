package com.resync.app.ui.viewholders.comments;

import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.buttons.DownvoteButton;
import com.resync.app.ui.views.buttons.ModButton;
import com.resync.app.ui.views.buttons.MoreButton;
import com.resync.app.ui.views.buttons.ParentButton;
import com.resync.app.ui.views.buttons.ProfileButton;
import com.resync.app.ui.views.buttons.ReplyButton;
import com.resync.app.ui.views.buttons.SaveButton;
import com.resync.app.ui.views.buttons.UpvoteButton;
import com.resync.app.ui.views.comments.CommentRow;
import com.resync.app.ui.views.comments.CommentsHtmlTextView;
import com.resync.app.ui.views.responsive.CustomButtonsWrapper;
import com.resync.app.ui.views.responsive.HideIndicator;
import com.resync.app.ui.views.responsive.NewIndicator;
import com.resync.app.ui.views.text.spannable.children.comments.CommentDescriptionTextView;
import com.resync.app.ui.views.text.spannable.children.image_components.awards.AwardsTextView;

/* JADX INFO: loaded from: classes3.dex */
public class CommentHolder_ViewBinding implements Unbinder {
    private CommentHolder target;
    private View indicatorView;
    private View upvoteView;
    private View downvoteView;
    private View saveView;
    private View modView;
    private View moreView;
    private View replyView;
    private View parentView;
    private View profileView;

    class ParentLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ CommentHolder boundTarget;

        ParentLongClickListener(CommentHolder commentHolder) {
            this.boundTarget = commentHolder;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.boundTarget.onParentLongClicked();
        }
    }

    class ProfileClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CommentHolder boundTarget;

        ProfileClickListener(CommentHolder commentHolder) {
            this.boundTarget = commentHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onProfileClicked(view);
        }
    }

    class IndicatorClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CommentHolder boundTarget;

        IndicatorClickListener(CommentHolder commentHolder) {
            this.boundTarget = commentHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onRowIndicatorClicked();
        }
    }

    class UpvoteClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CommentHolder boundTarget;

        UpvoteClickListener(CommentHolder commentHolder) {
            this.boundTarget = commentHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onUpvoteClicked(view);
        }
    }

    class DownvoteClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CommentHolder boundTarget;

        DownvoteClickListener(CommentHolder commentHolder) {
            this.boundTarget = commentHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onDownvoteClicked(view);
        }
    }

    class SaveClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CommentHolder boundTarget;

        SaveClickListener(CommentHolder commentHolder) {
            this.boundTarget = commentHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onSaveClicked(view);
        }
    }

    class SaveLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ CommentHolder boundTarget;

        SaveLongClickListener(CommentHolder commentHolder) {
            this.boundTarget = commentHolder;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.boundTarget.onSaveLongClicked();
        }
    }

    class ModClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CommentHolder boundTarget;

        ModClickListener(CommentHolder commentHolder) {
            this.boundTarget = commentHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onModClicked(view);
        }
    }

    class MoreClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CommentHolder boundTarget;

        MoreClickListener(CommentHolder commentHolder) {
            this.boundTarget = commentHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onMoreClicked(view);
        }
    }

    class ReplyClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CommentHolder boundTarget;

        ReplyClickListener(CommentHolder commentHolder) {
            this.boundTarget = commentHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onReplyClicked(view);
        }
    }

    class ParentClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CommentHolder boundTarget;

        ParentClickListener(CommentHolder commentHolder) {
            this.boundTarget = commentHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onParentClicked();
        }
    }

    public CommentHolder_ViewBinding(CommentHolder commentHolder, View view) {
        this.target = commentHolder;
        commentHolder.mCommentRow = (CommentRow) Utils.findRequiredViewAsType(view, R.id.comment_row_base, "field 'mCommentRow'", CommentRow.class);
        commentHolder.mCommentNewIndicator = (NewIndicator) Utils.findRequiredViewAsType(view, R.id.comment_row_new, "field 'mCommentNewIndicator'", NewIndicator.class);
        View viewC = Utils.findRequiredView(view, R.id.comment_row_indicator, "field 'mCommentCollapsedIndicator' and method 'onRowIndicatorClicked'");
        commentHolder.mCommentCollapsedIndicator = (HideIndicator) Utils.castView(viewC, R.id.comment_row_indicator, "field 'mCommentCollapsedIndicator'", HideIndicator.class);
        this.indicatorView = viewC;
        viewC.setOnClickListener(new IndicatorClickListener(commentHolder));
        commentHolder.mCommentRowInfo = (CommentDescriptionTextView) Utils.findRequiredViewAsType(view, R.id.comment_row_info, "field 'mCommentRowInfo'", CommentDescriptionTextView.class);
        commentHolder.mCommentRowAwards = (AwardsTextView) Utils.findRequiredViewAsType(view, R.id.comment_row_awards, "field 'mCommentRowAwards'", AwardsTextView.class);
        commentHolder.mCommentRowContent = (CommentsHtmlTextView) Utils.findRequiredViewAsType(view, R.id.comment_row_content, "field 'mCommentRowContent'", CommentsHtmlTextView.class);
        commentHolder.mCommentRowProfile = (ImageView) Utils.findRequiredViewAsType(view, R.id.comment_row_profile, "field 'mCommentRowProfile'", ImageView.class);
        commentHolder.mCommentActions = (CustomButtonsWrapper) Utils.findRequiredViewAsType(view, R.id.comment_actions, "field 'mCommentActions'", CustomButtonsWrapper.class);
        View viewC2 = Utils.findRequiredView(view, R.id.comment_actions_upvote, "field 'mButtonUpvote' and method 'onUpvoteClicked'");
        commentHolder.mButtonUpvote = (UpvoteButton) Utils.castView(viewC2, R.id.comment_actions_upvote, "field 'mButtonUpvote'", UpvoteButton.class);
        this.upvoteView = viewC2;
        viewC2.setOnClickListener(new UpvoteClickListener(commentHolder));
        View viewC3 = Utils.findRequiredView(view, R.id.comment_actions_downvote, "field 'mButtonDownvote' and method 'onDownvoteClicked'");
        commentHolder.mButtonDownvote = (DownvoteButton) Utils.castView(viewC3, R.id.comment_actions_downvote, "field 'mButtonDownvote'", DownvoteButton.class);
        this.downvoteView = viewC3;
        viewC3.setOnClickListener(new DownvoteClickListener(commentHolder));
        View viewC4 = Utils.findRequiredView(view, R.id.comment_actions_save, "field 'mButtonSave', method 'onSaveClicked', and method 'onSaveLongClicked'");
        commentHolder.mButtonSave = (SaveButton) Utils.castView(viewC4, R.id.comment_actions_save, "field 'mButtonSave'", SaveButton.class);
        this.saveView = viewC4;
        viewC4.setOnClickListener(new SaveClickListener(commentHolder));
        viewC4.setOnLongClickListener(new SaveLongClickListener(commentHolder));
        View viewC5 = Utils.findRequiredView(view, R.id.comment_actions_mod, "field 'mButtonMod' and method 'onModClicked'");
        commentHolder.mButtonMod = (ModButton) Utils.castView(viewC5, R.id.comment_actions_mod, "field 'mButtonMod'", ModButton.class);
        this.modView = viewC5;
        viewC5.setOnClickListener(new ModClickListener(commentHolder));
        View viewC6 = Utils.findRequiredView(view, R.id.comment_actions_more, "field 'mButtonMore' and method 'onMoreClicked'");
        commentHolder.mButtonMore = (MoreButton) Utils.castView(viewC6, R.id.comment_actions_more, "field 'mButtonMore'", MoreButton.class);
        this.moreView = viewC6;
        viewC6.setOnClickListener(new MoreClickListener(commentHolder));
        View viewC7 = Utils.findRequiredView(view, R.id.comment_actions_reply, "field 'mButtonReply' and method 'onReplyClicked'");
        commentHolder.mButtonReply = (ReplyButton) Utils.castView(viewC7, R.id.comment_actions_reply, "field 'mButtonReply'", ReplyButton.class);
        this.replyView = viewC7;
        viewC7.setOnClickListener(new ReplyClickListener(commentHolder));
        View viewC8 = Utils.findRequiredView(view, R.id.comment_actions_parent, "field 'mButtonParent', method 'onParentClicked', and method 'onParentLongClicked'");
        commentHolder.mButtonParent = (ParentButton) Utils.castView(viewC8, R.id.comment_actions_parent, "field 'mButtonParent'", ParentButton.class);
        this.parentView = viewC8;
        viewC8.setOnClickListener(new ParentClickListener(commentHolder));
        viewC8.setOnLongClickListener(new ParentLongClickListener(commentHolder));
        View viewC9 = Utils.findRequiredView(view, R.id.comment_actions_profile, "field 'mButtonProfile' and method 'onProfileClicked'");
        commentHolder.mButtonProfile = (ProfileButton) Utils.castView(viewC9, R.id.comment_actions_profile, "field 'mButtonProfile'", ProfileButton.class);
        this.profileView = viewC9;
        viewC9.setOnClickListener(new ProfileClickListener(commentHolder));
        commentHolder.mSearchDivider = Utils.findRequiredView(view, R.id.search_divider, "field 'mSearchDivider'");
    }

    @Override
    public void unbind() {
        CommentHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        target.mCommentRow = null;
        target.mCommentNewIndicator = null;
        target.mCommentCollapsedIndicator = null;
        target.mCommentRowInfo = null;
        target.mCommentRowAwards = null;
        target.mCommentRowContent = null;
        target.mCommentRowProfile = null;
        target.mCommentActions = null;
        target.mButtonUpvote = null;
        target.mButtonDownvote = null;
        target.mButtonSave = null;
        target.mButtonMod = null;
        target.mButtonMore = null;
        target.mButtonReply = null;
        target.mButtonParent = null;
        target.mButtonProfile = null;
        target.mSearchDivider = null;
        this.indicatorView.setOnClickListener(null);
        this.indicatorView = null;
        this.upvoteView.setOnClickListener(null);
        this.upvoteView = null;
        this.downvoteView.setOnClickListener(null);
        this.downvoteView = null;
        this.saveView.setOnClickListener(null);
        this.saveView.setOnLongClickListener(null);
        this.saveView = null;
        this.modView.setOnClickListener(null);
        this.modView = null;
        this.moreView.setOnClickListener(null);
        this.moreView = null;
        this.replyView.setOnClickListener(null);
        this.replyView = null;
        this.parentView.setOnClickListener(null);
        this.parentView.setOnLongClickListener(null);
        this.parentView = null;
        this.profileView.setOnClickListener(null);
        this.profileView = null;
    }
}
