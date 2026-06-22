package com.resync.app.ui.viewholders.posts.simple;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Outline;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.graphics.ColorUtils;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.OnboardingActivity;
import com.resync.app.ui.fragment_dialogs.ImagePeekDialogFragment;
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
import com.resync.app.ui.views.text.spannable.children.HtmlTableView;
import com.resync.app.ui.views.text.spannable.children.image_components.awards.AwardsTextView;
import com.resync.app.ui.views.text.spannable.children.image_components.flair.PostFlairTextView;
import com.resync.app.ui.views.text.spannable.children.posts.PostDescriptionTextView;
import com.resync.app.ui.views.text.spannable.children.posts.PostTitleTextView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.widgets.SimpleExoPlayerTextureView;
import okhttp3.internal.http.HttpStatusCodesKt;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.media.images.SyncTypeHelper;
import com.resync.app.events.ToggleCollapseEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.holders.BaseViewHolder;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.TransitionHelper;
import com.resync.app.holders.BasePostViewHolder;
import com.resync.app.helpers.DataSavingHelper;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkTypeHelper;
import com.resync.app.presenters.BaseCursorPresenter;

/* JADX INFO: loaded from: classes3.dex */
public class SimpleHolder extends BasePostViewHolder {

    ViewGroup awardFlairWrapper;

    AwardsTextView awards;

    ViewGroup buttonsWrapper;

    ViewGroup buttonsWrapperLeft;

    ViewGroup buttonsWrapperRight;

    CommentChip comments;

    PostDescriptionTextView description;

    ViewGroup descriptionWrapper;

    PostFlairTextView flair;

    SimpleButton hide;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    HtmlTableView tableView;

    SimplePostPreviewHelper image;

    ImageFrameLayout imageWrapper;

    IndicatorView indicator;

    SimpleButton mod;

    SimpleButton more;

    SimpleLinearLayout root;

    SaveButton save;

    SimpleSelftextPreviewTextView selftext;

    SimpleButton share;

    SubView subview;

    PostThumbnailView thumbnail;

    PostTitleTextView title;

    LinearLayout titleInnerWrapper;

    ViewGroup titleThumbnailWrapper;

    TwitterEmbedded twitterEmbedded;

    UrlView urlLabel;

    VotingChip voting;

    class SelftextLongClickListener implements View.OnLongClickListener {

        class CopyConfirmListener implements DialogInterface.OnClickListener {

            final /* synthetic */ TextView selectTextView;

            CopyConfirmListener(TextView textView) {
                this.selectTextView = textView;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int which) {
                int selectionStart = this.selectTextView.getSelectionStart();
                int selectionEnd = this.selectTextView.getSelectionEnd();
                if (selectionStart == selectionEnd) {
                    StaticToast.showWithContext(SimpleHolder.this.context, "No text copied");
                    return;
                }
                String strSubstring = this.selectTextView.getText().toString().substring(selectionStart, selectionEnd);
                RedditHelper.copyToClipboard(SimpleHolder.this.context, strSubstring, false);
                StaticToast.showWithContext(SimpleHolder.this.context, "Text copied: " + strSubstring);
            }
        }

        SelftextLongClickListener() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            View viewInflate = View.inflate(SimpleHolder.this.context, R.layout.dialog_copy, null);
            TextView textView = (TextView) viewInflate.findViewById(R.id.select);
            textView.setText(SimpleHolder.this.getPost().getSelftextRaw());
            new AlertDialog.Builder(SimpleHolder.this.context).setTitle("Select text...").setView(viewInflate).setPositiveButton("Copy", new CopyConfirmListener(textView)).create().show();
            return true;
        }
    }

    class RoundedOutlineProvider extends ViewOutlineProvider {
        RoundedOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), SizeHelper.dpToPx(12));
        }
    }

    public SimpleHolder(Context context, BaseCursorPresenter baseCursorPresenter, View view) {
        super(context, view);
        setPresenter(baseCursorPresenter);
        if (SettingsSingleton.getSettings().slideFullWidth) {
            applyFullWidthLayout();
        }
        if (SettingsSingleton.getSettings().slide_button_vote_right) {
            moveVotingRight();
        }
        this.thumbnail.getLayoutParams().width = thumbnailSize();
        this.thumbnail.getLayoutParams().height = thumbnailSize();
        if (SettingsSingleton.getSettings().slide_hide_all_buttons) {
            this.buttonsWrapper.setVisibility(8);
            PaddingHelper.setPaddingBottom(this.root, 16);
        }
        if (SettingsSingleton.getSettings().slideImageTop) {
            this.root.removeView(this.imageWrapper);
            this.root.addView(this.imageWrapper, 0);
            ((LinearLayout.LayoutParams) ((LinearLayoutCompat.LayoutParams) this.imageWrapper.getLayoutParams())).topMargin = 0;
            if (!SettingsSingleton.getSettings().slideFullWidth) {
                ((LinearLayout.LayoutParams) ((LinearLayoutCompat.LayoutParams) this.imageWrapper.getLayoutParams())).bottomMargin = SizeHelper.dpToPx(16);
            }
        }
        this.hide.setVisibility(SettingsSingleton.getSettings().slide_buttons_hide ? 0 : 8);
        this.save.setVisibility(SettingsSingleton.getSettings().slide_buttons_save ? 0 : 8);
        this.share.setVisibility(SettingsSingleton.getSettings().slide_buttons_share ? 0 : 8);
    }

    private void applyFullWidthLayout() {
        PaddingHelper.setPaddingAll(this.root, 0);
        this.imageWrapper.clearShape();
        PaddingHelper.setPaddingLeftTopRight(this.descriptionWrapper, 16, 16, 16);
        PaddingHelper.setPaddingHorizontal(this.titleThumbnailWrapper, 16, 16);
        ((LinearLayout.LayoutParams) ((LinearLayoutCompat.LayoutParams) this.twitterEmbedded.getLayoutParams())).rightMargin = SizeHelper.dpToPx(16);
        ((LinearLayout.LayoutParams) ((LinearLayoutCompat.LayoutParams) this.twitterEmbedded.getLayoutParams())).leftMargin = SizeHelper.dpToPx(16);
        this.selftext.applyFullWidthMargins();
        PaddingHelper.setPaddingLeftRightBottom(this.buttonsWrapper, 16, 16, 16);
    }

    private int thumbnailSize() {
        if (SettingsSingleton.getSettings().slideThumbnailSize == 0) {
            return SizeHelper.dpToPx(80);
        }
        if (SettingsSingleton.getSettings().slideThumbnailSize == 1) {
            return SizeHelper.dpToPx(60);
        }
        if (SettingsSingleton.getSettings().slideThumbnailSize == 2) {
            return SizeHelper.dpToPx(40);
        }
        throw new RuntimeException("Unsupported thumbnail size");
    }

    private void hideSubview() {
        if (this.subview.getVisibility() == 0) {
            this.subview.setVisibility(8);
            ((FrameLayout.LayoutParams) this.description.getLayoutParams()).leftMargin = 0;
            ((LinearLayout.LayoutParams) ((LinearLayoutCompat.LayoutParams) this.descriptionWrapper.getLayoutParams())).bottomMargin = SizeHelper.dpToPx(4);
        }
    }

    private void hideThumbnail() {
        this.thumbnail.setVisibility(8);
        ((FrameLayout.LayoutParams) this.titleInnerWrapper.getLayoutParams()).rightMargin = 0;
    }

    public static SimpleHolder create(Context context, ViewGroup viewGroup, BaseCursorPresenter baseCursorPresenter) {
        return new SimpleHolder(context, baseCursorPresenter, LayoutInflater.from(context).inflate(R.layout.holder_simple, viewGroup, false));
    }

    private void openComments() {
        if (canInteract() && !isPreviewMode()) {
            getPresenter().openComments(getPost());
        }
    }

    private void moveVotingRight() {
        this.buttonsWrapperLeft.removeView(this.voting);
        this.buttonsWrapperRight.addView(this.voting, this.buttonsWrapperRight.getChildCount() - 1);
    }

    private void showPostMore() {
        if (canInteract()) {
            getPresenter().showPostMore(getPost());
        }
    }

    private void showSubview() {
        if (this.subview.getVisibility() == 8) {
            this.subview.setVisibility(0);
            ((FrameLayout.LayoutParams) this.description.getLayoutParams()).leftMargin = SizeHelper.dpToPx(36);
            ((LinearLayout.LayoutParams) ((LinearLayoutCompat.LayoutParams) this.descriptionWrapper.getLayoutParams())).bottomMargin = SizeHelper.dpToPx(8);
        }
    }

    private void applyThumbnailMargin() {
        ((FrameLayout.LayoutParams) this.titleInnerWrapper.getLayoutParams()).rightMargin = thumbnailSize() + SizeHelper.dpToPx(16);
    }

    public boolean isOnboarding() {
        return ContextHelper.getBaseActivity(getContext()) instanceof OnboardingActivity;
    }

    public void applyCommentImagePreview() {
        setScrolling();
        this.subview.applyOvalOutline();
        this.image.getLayoutParams().height = SizeHelper.dpToPx(HttpStatusCodesKt.HTTP_OK);
        this.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.image.setImageTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(MonetUtil.getLinkColorAuto(), 85)));
        this.itemView.setOutlineProvider(new RoundedOutlineProvider());
        this.itemView.setClipToOutline(true);
    }

    @Override // com.resync.app.holders.BasePostViewHolder, com.resync.app.holders.RedditViewHolder
    public void recycle() {
        super.recycle();
        Logger.verbose("Recycle SimpleHolder");
        this.imageWrapper.setVisibility(8);
        this.indicator.setVisibility(8);
        this.urlLabel.setVisibility(8);
        this.selftext.setVisibility(8);
        this.twitterEmbedded.setVisibility(8);
        hideThumbnail();
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public void bindPost(Post post, int position) {
        this.root.setPosition(getLayoutPosition());
        if (getPost() != null && post != null && post.equals(getPost()) && post.hasSameUserTag(getPost()) && !isPreviewMode()) {
            Logger.verbose("BAILED SimpleHolder");
            return;
        }
        super.bindPost(post, position);
        if (shouldShowSubreddit()) {
            showSubview();
            this.subview.setSubredditFromPost(post);
        } else {
            hideSubview();
        }
        this.voting.bind(post);
        this.comments.bind(getPresenter(), post);
        this.title.bind(post);
        this.description.bind(post, 5, super.shouldShowSubreddit() || isFirstPost() || isOnboarding(), isPreviewMode(), false);
        this.save.bind(post);
        this.mod.setVisibility((SettingsSingleton.getSettings().slide_buttons_mod && getPost().isMod()) ? 0 : 8);
        this.flair.bindPost(getPost());
        this.awards.bindWithExpanded(getPost(), isPreviewMode());
        this.awardFlairWrapper.setVisibility((this.flair.getVisibility() == 0 || this.awards.getVisibility() == 0) ? 0 : 8);
        int syncType = getPost().getSyncType();
        if (DataSavingHelper.isFullDataSaving() && SyncTypeHelper.isMediaPost(syncType)) {
            syncType = 3;
        }
        if (isPreviewMode() && SettingsSingleton.getSettings().commentMediaThumbnail && SyncTypeHelper.isMediaPost(syncType)) {
            syncType = 3;
        }
        if (!SettingsSingleton.getSettings().slideImagePreviews && SyncTypeHelper.isMediaPost(syncType)) {
            syncType = 3;
        }
        if (StringUtils.isNotEmpty(post.getEmbedded()) && post.getUrl() != null && post.getUrl().contains("twitter.com") && SettingsSingleton.getSettings().slideTwitterPreviews) {
            this.twitterEmbedded.bind(post);
            this.twitterEmbedded.setVisibility(0);
        } else if (SyncTypeHelper.isMediaPost(syncType)) {
            this.imageWrapper.setVisibility(0);
            this.image.setPost(getPost());
            this.indicator.bind(getPost(), getUiMode());
            if (syncType == 9 || (post.getSyncType() == 1 && LinkTypeHelper.isYouTube(post.getUrl()))) {
                this.urlLabel.setVisibility(0);
                this.urlLabel.bind(getPost());
            }
        } else if (syncType == 2) {
            if (isPreviewMode()) {
                if (this.tableView == null) {
                    ViewGroup viewGroup = (ViewGroup) this.selftext.getParent();
                    int iIndexOfChild = viewGroup.indexOfChild(this.selftext);
                    HtmlTableView htmlTableView = new HtmlTableView(this.context, "slide_selftext");
                    this.tableView = htmlTableView;
                    viewGroup.addView(htmlTableView, iIndexOfChild);
                    ((LinearLayout.LayoutParams) ((LinearLayoutCompat.LayoutParams) this.tableView.getLayoutParams())).topMargin = SizeHelper.dpToPx(16);
                    if (SettingsSingleton.getSettings().slideSelftextBorder) {
                        this.tableView.applyCardBackground();
                    }
                    if (SettingsSingleton.getSettings().slideFullWidth) {
                        this.tableView.applySlidePadding();
                    }
                }
                this.tableView.bindPost(getPost());
                if (SettingsSingleton.getSettings().postsLongPress) {
                    this.tableView.setOnLongClickListener(new SelftextLongClickListener());
                }
            } else if (SettingsSingleton.getSettings().slideSelftextPreviews && getPost().getSelftextProcessedPreview() != null) {
                this.selftext.setVisibility(0);
                this.selftext.bindPreview(getPost());
                if (this.thumbnail.hasThumbnail(getPost())) {
                    applyThumbnailMargin();
                    this.thumbnail.bind(post);
                }
            }
        } else if (syncType == 3 || syncType == 6) {
            applyThumbnailMargin();
            this.thumbnail.bind(post);
        }
        if (this.selftext.getVisibility() == 8) {
            HtmlTableView htmlTableView2 = this.tableView;
            if ((htmlTableView2 == null || htmlTableView2.getVisibility() == 8) && StringUtils.isNotEmpty(getPost().getSelftextProcessed())) {
                this.selftext.setVisibility(0);
                this.selftext.bind(getPost(), isPreviewMode());
            }
        }
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public boolean canAutoplay() {
        return this.image.canAutoplay(this.context);
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    protected boolean shouldShowSubreddit() {
        if (isFirstPost() || isOnboarding()) {
            return true;
        }
        return super.shouldShowSubreddit() && SettingsSingleton.getSettings().slideSubredditThumbnail;
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public SimpleExoPlayerTextureView getTextureView() {
        for (int i = 0; i < this.imageWrapper.getChildCount(); i++) {
            if (this.imageWrapper.getChildAt(i) instanceof SimpleExoPlayerTextureView) {
                return (SimpleExoPlayerTextureView) this.imageWrapper.getChildAt(i);
            }
        }
        return null;
    }

    public void onCommentsClicked() {
        if (isPreviewMode()) {
            BusSingleton.getBus().post(new ToggleCollapseEvent());
        } else {
            openComments();
        }
    }

    public void onDescriptionClicked() {
        openComments();
    }

    public void onHideClicked() {
        if (canInteract()) {
            getPresenter().toggleHide(getPost());
        }
    }

    public void onImageClicked() {
        if (canInteract()) {
            if (getTextureView() != null) {
                this.image.setOverlayColor(getTextureView().getCurrentPosition());
            }
            getPresenter().openLink(TransitionHelper.buildSharedElements(this.context, this.image), getPost());
        }
    }

    public boolean onImageLongClicked() {
        if (!canInteract()) {
            return true;
        }
        getPresenter().peekImage(getPost(), this.image);
        return true;
    }

    public void onModMenuClicked() {
        if (canInteract()) {
            getPresenter().showModOptions(getPost());
        }
    }

    public void onMoreClicked() {
        showPostMore();
    }

    public void onRootClicked() {
        openComments();
    }

    public boolean onRootLongClicked() {
        if (getPresenter() == null) {
            return true;
        }
        if (SettingsSingleton.getSettings().postsCommentsNewActivity) {
            getPresenter().openLinkExternally(getPost());
            return true;
        }
        showPostMore();
        return true;
    }

    public void onShareClicked() {
        if (canInteract()) {
            getPost().share(getContext(), true);
        }
    }

    public boolean onShareLongClicked() {
        if (!canInteract()) {
            return true;
        }
        getPost().share(getContext(), false);
        return true;
    }

    public void onSubviewClicked() {
        if (canInteract()) {
            com.resync.app.util.links.LinkHandler.openSubreddit(this.context, getPost().getSubreddit());
        }
    }

    public void onThumbnailClicked() {
        if (canInteract()) {
            getPresenter().openLink(TransitionHelper.buildSharedElements(this.context, this.thumbnail), getPost());
        }
    }

    public boolean onThumbnailLongClicked() {
        if (!StringUtils.isNotEmpty(getPost().getThumbnail()) && !StringUtils.isNotEmpty(getPost().getPreviewUrl())) {
            return true;
        }
        ImagePeekDialogFragment.show(this.context, getPost(), this.image.getWidth(), this.image.getHeight());
        return true;
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public void setPreviewMode() {
        super.setPreviewMode();
        hideSubview();
        this.root.removeForeground();
        this.title.setVisitedDimDisabled(true);
        this.selftext.setVisitedDimDisabled(true);
        this.image.applyCommentMode();
        this.thumbnail.markNsfw();
        this.twitterEmbedded.disableVisitedDim();
        this.buttonsWrapper.setVisibility(0);
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public void setScrolling() {
        super.setScrolling();
        this.title.setVisitedDimDisabled(true);
        this.selftext.setVisitedDimDisabled(true);
        this.root.setEnabled(false);
        this.voting.setEnabled(false);
        this.comments.setEnabled(false);
        this.share.setEnabled(false);
        this.mod.setEnabled(false);
        this.hide.setEnabled(false);
        this.save.setEnabled(false);
        this.more.setEnabled(false);
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public void setFirstPost() {
        super.setFirstPost();
        this.title.setVisitedDimDisabled(true);
        this.image.setNsfw(true);
        this.image.markSharing();
        this.share.setVisibility(8);
        this.hide.setVisibility(8);
        this.mod.setVisibility(8);
        this.save.setVisibility(8);
        this.more.setVisibility(8);
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public void attachTextureView(SimpleExoPlayerTextureView simpleExoPlayerTextureView) {
        if (simpleExoPlayerTextureView.isAttachedTo(this.imageWrapper)) {
            return;
        }
        simpleExoPlayerTextureView.stopAndDetach();
        this.imageWrapper.addView(simpleExoPlayerTextureView);
        simpleExoPlayerTextureView.play(getPost());
    }
}
