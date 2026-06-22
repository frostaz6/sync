package com.resync.app.ui.viewholders.comments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.widget.PopupMenu;
import butterknife.ButterKnife;
import com.bumptech.glide.RedditHelper;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.activities.BanActivity;
import com.resync.app.ui.fragment_dialogs.SavedFragment;
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
import com.resync.app.helpers.ui.PopupMenuHelper;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.Kinds;
import com.resync.app.holders.BaseViewHolder;
import com.resync.app.managers.SaveManager;
import com.resync.app.managers.VoteManager;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.helpers.ThemeHelper;
import com.resync.app.dialogs.CommentMoreDialogBottomSheet;
import com.resync.app.dialogs.ReportReasonsDialogBottomSheet;
import com.resync.app.holders.BasePostViewHolder;
import com.resync.app.model.Post;
import com.resync.app.presenters.CommentsPresenter;
import com.resync.app.net.oauth.OAuthModApproveRequest;
import com.resync.app.net.oauth.OAuthModRemoveRequest;

/* JADX INFO: loaded from: classes3.dex */
public class CommentHolder extends BasePostViewHolder {

    CommentsPresenter presenter;
    boolean collapseParent;
    boolean forceSelected;

    boolean copyMode;

    boolean suppressHighlight;

    DownvoteButton mButtonDownvote;

    ModButton mButtonMod;

    MoreButton mButtonMore;

    ParentButton mButtonParent;

    ProfileButton mButtonProfile;

    ReplyButton mButtonReply;

    SaveButton mButtonSave;

    UpvoteButton mButtonUpvote;

    CustomButtonsWrapper mCommentActions;

    HideIndicator mCommentCollapsedIndicator;

    NewIndicator mCommentNewIndicator;

    public CommentRow mCommentRow;

    AwardsTextView mCommentRowAwards;

    CommentsHtmlTextView mCommentRowContent;

    CommentDescriptionTextView mCommentRowInfo;

    ImageView mCommentRowProfile;

    View mSearchDivider;
    boolean showFiltered;

    class RowClickListener implements View.OnClickListener {
        RowClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            CommentHolder commentHolder = CommentHolder.this;
            commentHolder.presenter.onCommentClicked(commentHolder.getPost());
            ThemeHelper.applyButtonBackground(CommentHolder.this.getContext(), CommentHolder.this.mButtonUpvote);
            ThemeHelper.applyButtonBackground(CommentHolder.this.getContext(), CommentHolder.this.mButtonDownvote);
            ThemeHelper.applyButtonBackground(CommentHolder.this.getContext(), CommentHolder.this.mButtonSave);
            ThemeHelper.applyButtonBackground(CommentHolder.this.getContext(), CommentHolder.this.mButtonProfile);
            ThemeHelper.applyButtonBackground(CommentHolder.this.getContext(), CommentHolder.this.mButtonReply);
            ThemeHelper.applyButtonBackground(CommentHolder.this.getContext(), CommentHolder.this.mButtonMod);
            ThemeHelper.applyButtonBackground(CommentHolder.this.getContext(), CommentHolder.this.mButtonMore);
        }
    }

    class RowLongClickListener implements View.OnLongClickListener {
        RowLongClickListener() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            CommentHolder commentHolder = CommentHolder.this;
            commentHolder.presenter.onCommentLongClicked(commentHolder.getPost());
            return true;
        }
    }

    class ModMenuClickListener implements PopupMenu.OnMenuItemClickListener {
        ModMenuClickListener() {
        }

        @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.comment_mod_approve) {
                StaticToast.showWithContext(CommentHolder.this.context, "Comment approved");
                OAuthRequestManager.send(CommentHolder.this.context, new OAuthModApproveRequest(CommentHolder.this.context, Kinds.toPrefix(1), CommentHolder.this.getPost().getId()));
            } else if (menuItem.getItemId() == R.id.comment_mod_remove) {
                StaticToast.showWithContext(CommentHolder.this.context, "Comment removed");
                OAuthRequestManager.send(CommentHolder.this.context, new OAuthModRemoveRequest(CommentHolder.this.context, Kinds.toPrefix(1), CommentHolder.this.getPost().getId(), false));
            } else if (menuItem.getItemId() == R.id.comment_mod_ban) {
                CommentHolder.this.context.startActivity(BanActivity.createIntent(CommentHolder.this.context, CommentHolder.this.post.getAuthor(), CommentHolder.this.post.getSubreddit()));
            } else if (menuItem.getItemId() == R.id.comment_mod_distinguish) {
                CommentHolder commentHolder = CommentHolder.this;
                commentHolder.presenter.distinguishComment(commentHolder.getPost());
            } else if (menuItem.getItemId() == R.id.comment_mod_report_reasons) {
                FragmentUtils.showWithArguments(ReportReasonsDialogBottomSheet.class, CommentHolder.this.getFragmentManager(), ReportReasonsDialogBottomSheet.buildArguments(StringUtils.split(CommentHolder.this.getPost().getUserReports(), ",")));
            }
            return true;
        }
    }

    private CommentHolder(Context context, CommentsPresenter commentsPresenter, View view, boolean showFiltered, boolean suppressHighlight) {
        super(context, view, UiSingleton.getInstance().getUiMode());
        ButterKnife.bind(this, view);
        this.presenter = commentsPresenter;
        this.showFiltered = showFiltered;
        this.suppressHighlight = suppressHighlight;
        if (!NightHelper.isNight()) {
            this.mButtonUpvote.disable();
            this.mButtonDownvote.disable();
            this.mButtonSave.disable();
            this.mButtonMod.disable();
            this.mButtonMore.disable();
            this.mButtonReply.disable();
            this.mButtonParent.disable();
            this.mButtonProfile.disable();
        }
        if (SettingsSingleton.getSettings().commentsReverseActions) {
            this.mCommentActions.removeAllViews();
            this.mCommentActions.addView(this.mButtonMore);
            this.mCommentActions.addView(this.mButtonReply);
            this.mCommentActions.addView(this.mButtonParent);
            this.mCommentActions.addView(this.mButtonProfile);
            this.mCommentActions.addView(this.mButtonMod);
            this.mCommentActions.addView(this.mButtonSave);
            this.mCommentActions.addView(this.mButtonUpvote);
            this.mCommentActions.addView(this.mButtonDownvote);
        }
        this.collapseParent = SettingsSingleton.getSettings().collapseParent;
        if (this.presenter != null) {
            this.mCommentRow.setOnClickListener(new RowClickListener());
        }
        if (this.presenter != null) {
            this.mCommentRow.setOnLongClickListener(new RowLongClickListener());
        }
        if (isSearch()) {
            this.mSearchDivider.setVisibility(0);
        }
        this.mCommentRow.setForeground(new ColorDrawable(0));
    }

    private boolean isContinueComment() {
        CommentsPresenter commentsPresenter = this.presenter;
        return commentsPresenter != null && commentsPresenter.isContinueInstance() && StringUtils.equals(this.post.getId(), this.presenter.getCommentId());
    }

    private boolean isSearch() {
        CommentsPresenter commentsPresenter = this.presenter;
        return commentsPresenter != null && commentsPresenter.isSearch();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onCopyRowClicked(View view) {
        RedditHelper.copyToClipboardWithToast(RedditHelper.htmlToTrimmedText(getPost().getBodyProcessed()).toString(), this.context);
    }

    public static CommentHolder create(Context context, ViewGroup viewGroup, CommentsPresenter commentsPresenter) {
        return new CommentHolder(context, commentsPresenter, LayoutInflater.from(context).inflate(R.layout.holder_comment, viewGroup, false), false, false);
    }

    public static CommentHolder createContinue(Context context, ViewGroup viewGroup, CommentsPresenter commentsPresenter) {
        return new CommentHolder(context, commentsPresenter, LayoutInflater.from(context).inflate(R.layout.holder_comment, viewGroup, false), true, true);
    }

    public void bind(Post post) {
        CommentsPresenter commentsPresenter;
        super.bindPost(post, 0);
        this.mButtonMod.setVisibility(getPost().isMod() ? 0 : 8);
        this.mCommentRowAwards.bind(getPost());
        this.mCommentRowInfo.bind(getPost(), this.presenter, false);
        int childCount = getPost().getChildCount();
        if (this.collapseParent) {
            AwardsTextView awardsTextView = this.mCommentRowAwards;
            awardsTextView.setVisibility(childCount > 0 ? 8 : awardsTextView.getVisibility());
            this.mCommentRowContent.setVisibility(childCount > 0 ? 8 : 0);
        }
        if (this.collapseParent) {
            childCount--;
        }
        if (childCount > 0) {
            this.mCommentCollapsedIndicator.setText("+" + childCount);
            this.mCommentCollapsedIndicator.setVisibility(0);
        } else {
            this.mCommentCollapsedIndicator.setVisibility(8);
        }
        CommentsPresenter commentsPresenter2 = this.presenter;
        boolean selected = commentsPresenter2 != null ? commentsPresenter2.isSelected(getPost()) : false;
        if (this.forceSelected) {
            selected = true;
        }
        int commentLevel = this.post.getLevel();
        if (isSearch()) {
            commentLevel = 0;
        }
        int level = isContinueComment() ? 0 : commentLevel;
        CommentRow commentRow = this.mCommentRow;
        int layoutPosition = getLayoutPosition();
        boolean highlighted = (selected && !isSearch()) || ((commentsPresenter = this.presenter) != null && commentsPresenter.isHighlighted(post));
        boolean showDecorator = (isSearch() || this.suppressHighlight) ? false : true;
        CommentsPresenter commentsPresenter3 = this.presenter;
        commentRow.setup(layoutPosition, level, highlighted, showDecorator, (commentsPresenter3 == null || commentsPresenter3.getCursor() == null || this.presenter.getCursor().isClosed() || getLayoutPosition() != this.presenter.getCursor().getCount() - 1) ? false : true);
        this.mCommentRow.setHighlightNew(getPost().isNewComment());
        if (!getPost().isCommentFiltered() || this.showFiltered) {
            CommentsPresenter commentsPresenter4 = this.presenter;
            if (commentsPresenter4 == null || !commentsPresenter4.isSearch()) {
                CommentsPresenter commentsPresenter5 = this.presenter;
                if (commentsPresenter5 != null && StringUtils.isNotEmpty(commentsPresenter5.getKeywordFilter()) && this.presenter.isHighlighted(getPost())) {
                    this.mCommentRowContent.setHtml(getPost(), this.presenter.getKeywordFilter());
                } else {
                    this.mCommentRowContent.setHtml(getPost(), null);
                }
            } else {
                this.mCommentRowContent.setHtml(getPost(), this.presenter.getUserFilter());
            }
        } else {
            this.mCommentRowContent.setSpannableText("[Filtered]");
        }
        this.mCommentNewIndicator.setVisibility((this.post.isNewComment() && SettingsSingleton.getInstance().getCurrentSettings().commentsHighlightNew) ? 0 : 8);
        if (SettingsSingleton.getSettings().commentsAlwaysShow) {
            this.mCommentActions.setVisibility(0);
        } else {
            int actionsVisibility = selected ? 0 : 8;
            if (this.mCommentActions.getVisibility() != actionsVisibility) {
                this.mCommentActions.setVisibility(actionsVisibility);
            }
        }
        this.mButtonUpvote.setActive(getPost().isUpvoted());
        this.mButtonDownvote.setActive(getPost().isDownvoted());
        this.mButtonSave.setActive(getPost().isSaved());
        CommentsPresenter commentsPresenter6 = this.presenter;
        if (commentsPresenter6 == null || !commentsPresenter6.isSearch()) {
            this.mButtonParent.setVisibility((SettingsSingleton.getSettings().commentsParentButton == 0 || getPost().getLevel() == 0) ? 8 : 0);
        } else {
            this.mButtonParent.setVisibility(8);
        }
        if (!SettingsSingleton.getSettings().commentsAuthorProfilePic || !StringUtils.isNotEmpty(getPost().getPostAuthorProfilePic())) {
            this.mCommentRowProfile.setVisibility(8);
            PaddingHelper.setPaddingStart(this.mCommentRowInfo, 0);
        } else {
            PaddingHelper.setPaddingStart(this.mCommentRowInfo, 26);
            this.mCommentRowProfile.setVisibility(0);
            com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).load(getPost().getPostAuthorProfilePic()).transform(new CircleCrop()).into(this.mCommentRowProfile);
        }
    }

    public void enableCopyMode() {
        this.copyMode = true;
        this.mCommentRow.setOnClickListener(new View.OnClickListener() { // from class: pb.b
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CommentHolder.this.onCopyRowClicked(view);
            }
        });
    }

    @Override // com.resync.app.holders.BasePostViewHolder, com.resync.app.holders.RedditViewHolder
    public void recycle() {
        super.recycle();
        com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).clear(this.mCommentRowProfile);
    }

    public void onDownvoteClicked(View view) {
        if (this.presenter == null) {
            return;
        }
        VoteManager.voteOnPost(this.context, 1, getPost(), -1);
        ThemeHelper.applyEmptyButtonBackground(view);
    }

    public void onModClicked(View view) {
        if (this.presenter == null) {
            return;
        }
        PopupMenu popupMenuB = PopupMenuHelper.create(view);
        popupMenuB.inflate(R.menu.comments_mod_contextual);
        if (TextUtils.isEmpty(getPost().getUserReports())) {
            popupMenuB.getMenu().removeItem(R.id.comment_mod_report_reasons);
        }
        popupMenuB.setOnMenuItemClickListener(new ModMenuClickListener());
        if (this.presenter.isSelf(getPost())) {
            popupMenuB.getMenu().findItem(R.id.comment_mod_distinguish).setVisible(true);
            if (!TextUtils.isEmpty(this.post.getDistinguished())) {
                popupMenuB.getMenu().findItem(R.id.comment_mod_distinguish).setTitle("Undistinguish");
            }
        }
        popupMenuB.show();
        ThemeHelper.applyEmptyButtonBackground(view);
    }

    public void onMoreClicked(View view) {
        if (this.presenter == null) {
            return;
        }
        FragmentUtils.showWithArguments(CommentMoreDialogBottomSheet.class, getFragmentManager(), CommentMoreDialogBottomSheet.buildArgs(getPost(), true));
        this.presenter.deselectComment(false);
        ThemeHelper.applyEmptyButtonBackground(view);
    }

    public void onParentClicked() {
        CommentsPresenter commentsPresenter = this.presenter;
        if (commentsPresenter == null) {
            return;
        }
        commentsPresenter.viewParentComment(getPost());
    }

    public boolean onParentLongClicked() {
        CommentsPresenter commentsPresenter = this.presenter;
        if (commentsPresenter == null) {
            return false;
        }
        commentsPresenter.selectComment(getPost());
        return true;
    }

    public void onProfileClicked(View view) {
        if (this.presenter == null) {
            return;
        }
        com.resync.app.util.links.LinkHandler.openProfile(this.context, getPost().getAuthor());
        this.presenter.deselectComment(false);
        ThemeHelper.applyEmptyButtonBackground(view);
    }

    public void onReplyClicked(View view) {
        CommentsPresenter commentsPresenter = this.presenter;
        if (commentsPresenter == null) {
            return;
        }
        commentsPresenter.replyToComment(getPost());
        this.presenter.deselectComment(false);
        ThemeHelper.applyEmptyButtonBackground(view);
    }

    public void onRowIndicatorClicked() {
        this.presenter.onCommentLongClicked(getPost());
    }

    public void onSaveClicked(View view) {
        CommentsPresenter commentsPresenter = this.presenter;
        if (commentsPresenter == null) {
            return;
        }
        commentsPresenter.saveComment(getPost());
        this.presenter.deselectComment(false);
        ThemeHelper.applyEmptyButtonBackground(view);
    }

    public boolean onSaveLongClicked() {
        if (getPost().isSaved()) {
            SaveManager.toggleSave(this.context, getPost());
            return true;
        }
        SavedFragment.newInstance(getPost()).show(getFragmentManager(), "saved_fragment");
        return true;
    }

    public void onUpvoteClicked(View view) {
        if (this.presenter == null) {
            return;
        }
        VoteManager.voteOnPost(this.context, 1, getPost(), 1);
        ThemeHelper.applyEmptyButtonBackground(view);
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public void setScrolling() {
        super.setScrolling();
    }
}
