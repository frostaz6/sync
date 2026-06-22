package com.resync.app.ui.viewholders.posts.card;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.fragment_dialogs.SavedFragment;
import com.resync.app.ui.util.animation.recycler.helper.AnimationHelper;
import com.resync.app.ui.views.buttons.DownvoteButton;
import com.resync.app.ui.views.buttons.HideButton;
import com.resync.app.ui.views.buttons.ModButton;
import com.resync.app.ui.views.buttons.ReplyButton;
import com.resync.app.ui.views.buttons.SaveButton;
import com.resync.app.ui.views.buttons.ShareButton;
import com.resync.app.ui.views.buttons.UpvoteButton;
import com.resync.app.ui.views.cards.CardButton;
import com.resync.app.ui.views.responsive.CustomMaterialCardView;
import com.resync.app.ui.views.text.spannable.children.image_components.awards.AwardsTextView;
import com.resync.app.ui.views.text.spannable.children.image_components.flair.PostFlairTextView;
import com.resync.app.ui.views.text.spannable.children.posts.PostDescriptionTextView;
import com.resync.app.ui.views.text.spannable.children.posts.PostStatsTextView;
import com.resync.app.ui.views.text.spannable.children.posts.PostTitleTextView;
import com.resync.app.events.ReplyClickedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.managers.SaveManager;
import com.resync.app.helpers.CrashlyticsHelper;
import com.resync.app.helpers.ModHelper;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.helpers.PostMoreHelper;
import com.resync.app.holders.BasePostViewHolder;
import com.resync.app.model.Post;
import com.resync.app.presenters.BaseCursorPresenter;

/* JADX INFO: loaded from: classes3.dex */
public abstract class AbstractCardPostHolder extends BasePostViewHolder {

    AwardsTextView mAwards;

    CustomMaterialCardView mBase;

    View mButtonsWrapper;

    PostDescriptionTextView mDescription;

    DownvoteButton mDownvoteButton;

    View mExpandLayer;

    ViewGroup mExtraWrapper;

    HideButton mHideButton;

    ModButton mModButton;

    CardButton mMoreButton;

    PostFlairTextView mPostFlair;

    ReplyButton mReplyButton;

    SaveButton mSaveButton;

    ShareButton mShareButton;

    PostStatsTextView mStats;

    PostTitleTextView mTitle;

    UpvoteButton mUpvoteButton;

    class DescriptionClickListener implements View.OnClickListener {
        DescriptionClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            AbstractCardPostHolder.this.openComments();
        }
    }

    public AbstractCardPostHolder(Context context, BaseCursorPresenter baseCursorPresenter, View view, int uiMode) {
        super(context, view, uiMode);
        setPresenter(baseCursorPresenter);
        setupButtons();
        this.mDescription.setOnClickListener(new DescriptionClickListener());
        if (UiSingleton.isFullWidth(getUiMode())) {
            applyFullWidthLayout();
        }
    }

    private void setupButtons() {
        this.mUpvoteButton.setVisibility(0);
        this.mDownvoteButton.setVisibility(0);
        this.mSaveButton.setButtonVisible(true);
        this.mModButton.setVisibility(0);
        this.mShareButton.setVisibility(0);
        this.mHideButton.setVisibility(0);
        boolean landscape = this.context.getResources().getBoolean(R.bool.landscape);
        int columnCount = UiSingleton.getColumnCount(getUiMode(), landscape);
        if (getUiMode() == 4) {
            this.mSaveButton.setButtonVisible(SettingsSingleton.getSettings().card_buttons_save);
            this.mHideButton.setVisibility(SettingsSingleton.getSettings().card_buttons_hide ? 0 : 8);
            this.mShareButton.setVisibility(SettingsSingleton.getSettings().card_buttons_share ? 0 : 8);
        } else if (getUiMode() == 3) {
            this.mSaveButton.setButtonVisible(SettingsSingleton.getSettings().small_card_buttons_save);
            this.mHideButton.setVisibility(SettingsSingleton.getSettings().small_card_buttons_hide ? 0 : 8);
            this.mShareButton.setVisibility(SettingsSingleton.getSettings().small_card_buttons_share ? 0 : 8);
        }
        if (isPreviewMode() || this.context.getResources().getBoolean(R.bool.tablet)) {
            return;
        }
        if ((!landscape || columnCount <= 2) && (landscape || columnCount <= 1)) {
            return;
        }
        this.mUpvoteButton.setVisibility(8);
        this.mDownvoteButton.setVisibility(8);
        this.mSaveButton.setButtonVisible(false);
        this.mModButton.setVisibility(8);
        this.mShareButton.setVisibility(8);
        this.mHideButton.setVisibility(8);
    }

    protected void applyFullWidthLayout() {
        this.mBase.setRadius(0.0f);
        PaddingHelper.setPaddingHorizontal(this.mTitle, 16, 16);
        PaddingHelper.setPaddingHorizontal(this.mDescription, 16, 16);
        PaddingHelper.setPaddingHorizontal(this.mStats, 16, 16);
        PaddingHelper.setPaddingHorizontal(this.mExtraWrapper, 16, 16);
        PaddingHelper.setPaddingEnd(this.mButtonsWrapper, 6);
    }

    protected void showMoreSheet() {
        PostMoreHelper.showMoreSheet(this.context, getUiMode(), getPost());
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public void bindPost(Post post, int position) {
        super.bindPost(post, position);
        int extraWrapperVisibility = 0;
        this.mModButton.setVisibility((SettingsSingleton.getSettings().card_buttons_mod && getPost().isMod()) ? 0 : 8);
        this.mSaveButton.setActive(getPost().isSaved());
        this.mTitle.bind(getPost());
        this.mDescription.bind(getPost(), getUiMode(), shouldShowSubreddit(), isPreviewMode(), false);
        this.mAwards.bindWithExpanded(getPost(), isPreviewMode());
        this.mPostFlair.bindPost(getPost());
        ViewGroup viewGroup = this.mExtraWrapper;
        if (this.mPostFlair.getVisibility() != 0 && this.mAwards.getVisibility() != 0) {
            extraWrapperVisibility = 8;
        }
        viewGroup.setVisibility(extraWrapperVisibility);
        PostStatsTextView postStatsTextView = this.mStats;
        if (postStatsTextView != null) {
            postStatsTextView.bind(getPresenter(), getPost(), isPreviewMode());
        }
        UpvoteButton upvoteButton = this.mUpvoteButton;
        if (upvoteButton != null) {
            upvoteButton.setActive(getPost().isUpvoted());
        }
        DownvoteButton downvoteButton = this.mDownvoteButton;
        if (downvoteButton != null) {
            downvoteButton.setActive(getPost().isDownvoted());
        }
    }

    protected boolean onCardLongClicked() {
        if (getPresenter() == null) {
            return true;
        }
        if (SettingsSingleton.getSettings().postsCommentsNewActivity) {
            getPresenter().openLinkExternally(getPost());
            return true;
        }
        showMoreSheet();
        return true;
    }

    public void onDownvoteClicked(View view) {
        AnimationHelper.performAnimatedPostAction(this.context, getPost(), 1, this.mExpandLayer, (ImageButton) view);
    }

    public void onHideClicked() {
        if (getPresenter() != null) {
            if (getPost().isHidden()) {
                getPresenter().unhidePost(getPost());
            } else {
                getPresenter().hidePost(getPost());
            }
        }
    }

    public void onModMenuClicked() {
        ModHelper.showModSheet(this.context, getPost());
    }

    public void onMoreClicked() {
        showMoreSheet();
    }

    protected void onPostclicked() {
        openComments();
    }

    public void onReplyClicked() {
        BusSingleton.getBus().post(new ReplyClickedEvent());
    }

    public void onSaveClicked() {
        AnimationHelper.performAnimatedPostAction(this.context, getPost(), 2, this.mExpandLayer, this.mSaveButton);
    }

    public boolean onSaveLongClicked() {
        if (getPost().isSaved()) {
            SaveManager.toggleSave(this.context, getPost());
            return true;
        }
        try {
            SavedFragment.newInstance(getPost()).show(((BaseActivity) this.context).getSupportFragmentManager(), "saved_fragment");
            return true;
        } catch (Exception e) {
            CrashlyticsHelper.logException(e);
            return true;
        }
    }

    public void onShareClickedMaterial() {
        getPost().share(getContext(), true);
    }

    public boolean onShareLongClickedMaterial() {
        getPost().share(getContext(), false);
        return true;
    }

    public void onUpvoteClicked(View view) {
        AnimationHelper.performAnimatedPostAction(this.context, getPost(), 0, this.mExpandLayer, (ImageButton) view);
    }

    protected boolean hasPresenter() {
        return getPresenter() != null;
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public void setPreviewMode() {
        super.setPreviewMode();
        this.mTitle.setVisitedDimDisabled(true);
        this.mBase.setRippleColor(ColorStateList.valueOf(0));
        applyFullWidthLayout();
        setupButtons();
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public void setScrolling() {
        super.setScrolling();
        this.mTitle.setVisitedDimDisabled(true);
        this.mBase.setEnabled(false);
        this.mUpvoteButton.setEnabled(false);
        this.mDownvoteButton.setEnabled(false);
        this.mSaveButton.setEnabled(false);
        this.mModButton.setEnabled(false);
        this.mShareButton.setEnabled(false);
        this.mHideButton.setEnabled(false);
        this.mMoreButton.setEnabled(false);
    }

    protected void openComments() {
        if (isScrolling() || isPreviewMode() || !hasPresenter()) {
            return;
        }
        getPresenter().openComments(getPost());
    }
}
