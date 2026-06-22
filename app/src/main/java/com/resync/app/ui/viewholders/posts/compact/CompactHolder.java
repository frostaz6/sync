package com.resync.app.ui.viewholders.posts.compact;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.ColorUtils;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.fragment_dialogs.ImagePeekDialogFragment;
import com.resync.app.ui.fragment_dialogs.SavedFragment;
import com.resync.app.ui.util.animation.recycler.helper.AnimationHelper;
import com.resync.app.ui.views.buttons.DownvoteButton;
import com.resync.app.ui.views.buttons.ExpandButton;
import com.resync.app.ui.views.buttons.HideButton;
import com.resync.app.ui.views.buttons.ModButton;
import com.resync.app.ui.views.buttons.MoreButton;
import com.resync.app.ui.views.buttons.ProfileButton;
import com.resync.app.ui.views.buttons.SaveButton;
import com.resync.app.ui.views.buttons.TypeButton;
import com.resync.app.ui.views.buttons.UpvoteButton;
import com.resync.app.ui.views.images.CustomImageView;
import com.resync.app.ui.views.responsive.CustomButtonsWrapper;
import com.resync.app.ui.views.text.spannable.children.image_components.awards.AwardsTextView;
import com.resync.app.ui.views.text.spannable.children.image_components.flair.PostFlairTextView;
import com.resync.app.ui.views.text.spannable.children.posts.PostDescriptionTextView;
import com.resync.app.ui.views.text.spannable.children.posts.PostTitleTextView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.managers.SaveManager;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.helpers.ModHelper;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.PostMoreHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.ThemeHelper;
import com.resync.app.holders.BasePostViewHolder;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.presenters.BaseCursorPresenter;
import com.resync.app.util.links.LinkHandler;

/* JADX INFO: loaded from: classes3.dex */
public class CompactHolder extends BasePostViewHolder {

    AwardsTextView mAwards;

    PostDescriptionTextView mDescription;

    DownvoteButton mDownvote;

    ExpandButton mExpand;

    CustomButtonsWrapper mExpandedWrapper;

    ViewGroup mExtraWrapper;

    HideButton mHide;

    ModButton mMod;

    MoreButton mMore;

    LinearLayout mMoreWrapper;

    PostFlairTextView mPostFlair;

    ProfileButton mProfile;

    SaveButton mSave;

    CustomImageView mThumbnail;

    FrameLayout mThumbnailWrapper;

    PostTitleTextView mTitle;

    LinearLayout mTitleWrapper;

    TypeButton mType;

    UpvoteButton mUpvote;

    private CompactHolder(Context context, BaseCursorPresenter baseCursorPresenter, View view, int uiMode) {
        super(context, view, uiMode);
        setPresenter(baseCursorPresenter);
        ThemeHelper.applyButtonBackground(context, this.mMore);
        if (!SettingsSingleton.getSettings().typeIndicators) {
            this.mType.setVisibility(8);
        }
        if (SettingsSingleton.getSettings().alignThumbnailsRight) {
            ((FrameLayout.LayoutParams) this.mThumbnailWrapper.getLayoutParams()).gravity = 5;
            this.mThumbnailWrapper.requestLayout();
        }
        if (!NightHelper.isNight()) {
            this.mUpvote.disable();
            this.mDownvote.disable();
            this.mSave.disable();
            this.mHide.disable();
            this.mMod.disable();
            this.mProfile.disable();
            this.mMore.disable();
        }
        if (uiMode == 2) {
            ((CardView) view.findViewById(R.id.holder_list_root)).setCardBackgroundColor(MonetUtil.getBackgroundColorAuto());
        } else {
            view.setBackgroundColor(MonetUtil.getBackgroundColorAuto());
        }
        if (SettingsSingleton.getSettings().compactSingleLine) {
            this.mTitle.setEllipsize(TextUtils.TruncateAt.END);
            this.mTitle.setSingleLine();
        }
    }

    public static CompactHolder create(Context context, ViewGroup viewGroup, BaseCursorPresenter baseCursorPresenter, int uiMode) {
        View viewInflate;
        if (uiMode == 0) {
            viewInflate = LayoutInflater.from(context).inflate(R.layout.holder_list, viewGroup, false);
        } else if (uiMode == 1) {
            viewInflate = LayoutInflater.from(context).inflate(R.layout.holder_compact, viewGroup, false);
        } else {
            if (uiMode != 2) {
                throw new RuntimeException("Unsupported mode: " + uiMode);
            }
            viewInflate = LayoutInflater.from(context).inflate(R.layout.holder_smaller_cards, viewGroup, false);
        }
        return new CompactHolder(context, baseCursorPresenter, viewInflate, uiMode);
    }

    private int typePadding() {
        return (getUiMode() == 0 && this.mType.getVisibility() == 0) ? 38 : 0;
    }

    public void applyCommentImagePreview() {
        setScrolling();
        this.mThumbnail.setImageTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(MonetUtil.getLinkColorAuto(), 63)));
    }

    @Override // com.resync.app.holders.BasePostViewHolder, com.resync.app.holders.RedditViewHolder
    public void recycle() {
        super.recycle();
        this.mExpandedWrapper.setVisibility(8);
        this.mExpand.setExpanded(false);
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public void bindPost(Post post, int position) {
        super.bindPost(post, position);
        this.mMod.setVisibility(getPost().isMod() ? 0 : 8);
        this.mType.applyPostType(getPost());
        Logger.verbose("Thumbnail: " + getPost().getThumbnailPlaceholder(this.context));
        if (com.resync.app.util.TextUtils.isEmpty(getPost().getThumbnailPlaceholder(this.context)) || !SettingsSingleton.getSettings().compactThumbnails) {
            this.mThumbnailWrapper.setVisibility(8);
            if (SettingsSingleton.getSettings().alignThumbnailsRight) {
                LinearLayout linearLayout = this.mTitleWrapper;
                linearLayout.setPadding(linearLayout.getPaddingLeft(), this.mTitleWrapper.getPaddingTop(), (int) SizeHelper.dpToPxFloat(typePadding() + 38), this.mTitleWrapper.getPaddingBottom());
                ((FrameLayout.LayoutParams) this.mMoreWrapper.getLayoutParams()).rightMargin = 0;
                this.mMoreWrapper.requestLayout();
            } else {
                this.mTitleWrapper.setPadding((int) SizeHelper.dpToPxFloat(8), this.mTitleWrapper.getPaddingTop(), (int) SizeHelper.dpToPxFloat(typePadding() + 38), this.mTitleWrapper.getPaddingBottom());
            }
        } else {
            this.mThumbnailWrapper.setVisibility(0);
            if (SettingsSingleton.getSettings().alignThumbnailsRight) {
                LinearLayout linearLayout2 = this.mTitleWrapper;
                linearLayout2.setPadding(linearLayout2.getPaddingLeft(), this.mTitleWrapper.getPaddingTop(), (int) SizeHelper.dpToPxFloat(typePadding() + 108), this.mTitleWrapper.getPaddingBottom());
                ((FrameLayout.LayoutParams) this.mMoreWrapper.getLayoutParams()).rightMargin = (int) SizeHelper.dpToPxFloat(70);
                this.mMoreWrapper.requestLayout();
            } else {
                this.mTitleWrapper.setPadding((int) SizeHelper.dpToPxFloat(78), this.mTitleWrapper.getPaddingTop(), (int) SizeHelper.dpToPxFloat(typePadding() + 38), this.mTitleWrapper.getPaddingBottom());
            }
            this.mThumbnail.load(getPost().getThumbnailPlaceholder(this.context), getPost().isVisitedOrInternal(), false);
        }
        this.mTitle.bind(this.post);
        this.mDescription.bind(getPost(), getUiMode(), shouldShowSubreddit(), false, true);
        this.mAwards.bind(getPost());
        this.mPostFlair.bindPost(getPost());
        this.mExtraWrapper.setVisibility((this.mPostFlair.getVisibility() == 0 || this.mAwards.getVisibility() == 0) ? 0 : 8);
        this.mUpvote.setActive(getPost().isUpvoted());
        this.mDownvote.setActive(getPost().isDownvoted());
        this.mSave.setActive(getPost().isSaved());
        if (getPost().isDownvoted()) {
            this.mMoreWrapper.setBackgroundColor(1721013503);
        } else if (getPost().isUpvoted()) {
            this.mMoreWrapper.setBackgroundColor(1728023392);
        } else {
            this.mMoreWrapper.setBackgroundColor(0);
        }
    }

    protected void onExpandedButtonPressed(View view) {
        if (view.getId() == R.id.holder_list_more) {
            PostMoreHelper.showMoreSheet(this.context, getUiMode(), getPost());
            return;
        }
        onMoreClicked();
        if (view.getId() == R.id.holder_list_hide) {
            if (getPost().isHidden()) {
                getPresenter().unhidePost(getPost());
                return;
            } else {
                getPresenter().hidePost(getPost());
                return;
            }
        }
        if (view.getId() == R.id.holder_list_upvote) {
            if (ConnectivityHelper.isConnected(this.context)) {
                AnimationHelper.performAction(this.context, getPost(), 0);
                return;
            } else {
                StaticToast.showWithContext(this.context, "You must be online to perform post actions");
                return;
            }
        }
        if (view.getId() == R.id.holder_list_downupvote) {
            if (ConnectivityHelper.isConnected(this.context)) {
                AnimationHelper.performAction(this.context, getPost(), 1);
                return;
            } else {
                StaticToast.showWithContext(this.context, "You must be online to perform post actions");
                return;
            }
        }
        if (view.getId() == R.id.holder_list_save) {
            if (ConnectivityHelper.isConnected(this.context)) {
                AnimationHelper.performAction(this.context, getPost(), 2);
                return;
            } else {
                StaticToast.showWithContext(this.context, "You must be online to perform post actions");
                return;
            }
        }
        if (view.getId() == R.id.holder_list_profile) {
            LinkHandler.openProfile(this.context, getPost().getAuthor());
        } else if (view.getId() == R.id.holder_list_more) {
            PostMoreHelper.showMoreSheet(this.context, getUiMode(), getPost());
        }
    }

    protected boolean onImageLongClicked() {
        ImagePeekDialogFragment.show(this.context, getPost());
        return true;
    }

    public void onModMenuClicked(View view) {
        ModHelper.showModSheet(this.context, getPost());
    }

    protected void onMoreClicked() {
        if (this.mExpandedWrapper.getVisibility() == 8) {
            this.mExpandedWrapper.setVisibility(0);
            this.mExpand.setExpanded(true);
        } else {
            this.mExpandedWrapper.setVisibility(8);
            this.mExpand.setExpanded(false);
        }
    }

    protected boolean onPostLongClicked() {
        if (this.mExpandedWrapper.getVisibility() == 8) {
            this.mExpandedWrapper.setVisibility(0);
            this.mExpand.setExpanded(true);
        } else {
            this.mExpandedWrapper.setVisibility(8);
            this.mExpand.setExpanded(false);
        }
        return true;
    }

    protected void onRowClicked() {
        getPresenter().openComments(getPost());
    }

    public boolean onSaveLongClicked() {
        onMoreClicked();
        if (getPost().isSaved()) {
            SaveManager.toggleSave(this.context, getPost());
            return true;
        }
        SavedFragment.newInstance(getPost()).show(((BaseActivity) this.context).getSupportFragmentManager(), "saved_fragment");
        return true;
    }

    protected void onThumbnailClicked() {
        getPresenter().openLink(null, getPost());
    }

    protected void onTypeClicked() {
        if (getPresenter() != null) {
            getPresenter().openLink(null, getPost());
        }
    }

    protected boolean onTypeLongClicked() {
        if (getPresenter() == null) {
            return false;
        }
        ImagePeekDialogFragment.show(this.context, getPost());
        return true;
    }

    @Override // com.resync.app.holders.BasePostViewHolder
    public void setScrolling() {
        super.setScrolling();
        this.mTitleWrapper.setEnabled(false);
        this.mDescription.setEnabled(false);
        this.mExpand.setEnabled(false);
        this.mType.setEnabled(false);
        this.mThumbnailWrapper.setEnabled(false);
    }
}
