package com.resync.app.ui.viewholders.posts.compact;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
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

/* JADX INFO: loaded from: classes3.dex */
public class CompactHolder_ViewBinding implements Unbinder {
    private CompactHolder target;
    private View thumbnailWrapperView;
    private View titleWrapperView;
    private View upvoteView;
    private View downvoteView;
    private View saveView;
    private View hideView;
    private View modView;
    private View profileView;
    private View moreView;
    private View expandView;
    private View typeView;

    class ModClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CompactHolder boundTarget;

        ModClickListener(CompactHolder compactHolder) {
            this.boundTarget = compactHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onModMenuClicked(view);
        }
    }

    class ProfileClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CompactHolder boundTarget;

        ProfileClickListener(CompactHolder compactHolder) {
            this.boundTarget = compactHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onExpandedButtonPressed(view);
        }
    }

    class MoreButtonClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CompactHolder boundTarget;

        MoreButtonClickListener(CompactHolder compactHolder) {
            this.boundTarget = compactHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onExpandedButtonPressed(view);
        }
    }

    class ExpandClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CompactHolder boundTarget;

        ExpandClickListener(CompactHolder compactHolder) {
            this.boundTarget = compactHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onMoreClicked();
        }
    }

    class TypeClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CompactHolder boundTarget;

        TypeClickListener(CompactHolder compactHolder) {
            this.boundTarget = compactHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onTypeClicked();
        }
    }

    class TypeLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ CompactHolder boundTarget;

        TypeLongClickListener(CompactHolder compactHolder) {
            this.boundTarget = compactHolder;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.boundTarget.onTypeLongClicked();
        }
    }

    class ThumbnailClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CompactHolder boundTarget;

        ThumbnailClickListener(CompactHolder compactHolder) {
            this.boundTarget = compactHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onThumbnailClicked();
        }
    }

    class ThumbnailLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ CompactHolder boundTarget;

        ThumbnailLongClickListener(CompactHolder compactHolder) {
            this.boundTarget = compactHolder;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.boundTarget.onImageLongClicked();
        }
    }

    class RowClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CompactHolder boundTarget;

        RowClickListener(CompactHolder compactHolder) {
            this.boundTarget = compactHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onRowClicked();
        }
    }

    class PostLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ CompactHolder boundTarget;

        PostLongClickListener(CompactHolder compactHolder) {
            this.boundTarget = compactHolder;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.boundTarget.onPostLongClicked();
        }
    }

    class UpvoteClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CompactHolder boundTarget;

        UpvoteClickListener(CompactHolder compactHolder) {
            this.boundTarget = compactHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onExpandedButtonPressed(view);
        }
    }

    class DownvoteClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CompactHolder boundTarget;

        DownvoteClickListener(CompactHolder compactHolder) {
            this.boundTarget = compactHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onExpandedButtonPressed(view);
        }
    }

    class SaveClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CompactHolder boundTarget;

        SaveClickListener(CompactHolder compactHolder) {
            this.boundTarget = compactHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onExpandedButtonPressed(view);
        }
    }

    class SaveLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ CompactHolder boundTarget;

        SaveLongClickListener(CompactHolder compactHolder) {
            this.boundTarget = compactHolder;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.boundTarget.onSaveLongClicked();
        }
    }

    class HideClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ CompactHolder boundTarget;

        HideClickListener(CompactHolder compactHolder) {
            this.boundTarget = compactHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onExpandedButtonPressed(view);
        }
    }

    public CompactHolder_ViewBinding(CompactHolder compactHolder, View view) {
        this.target = compactHolder;
        compactHolder.mThumbnail = (CustomImageView) Utils.findRequiredViewAsType(view, R.id.holder_list_preview, "field 'mThumbnail'", CustomImageView.class);
        View viewC = Utils.findRequiredView(view, R.id.holder_list_preview_wrapper, "field 'mThumbnailWrapper', method 'onThumbnailClicked', and method 'onImageLongClicked'");
        compactHolder.mThumbnailWrapper = (FrameLayout) Utils.castView(viewC, R.id.holder_list_preview_wrapper, "field 'mThumbnailWrapper'", FrameLayout.class);
        this.thumbnailWrapperView = viewC;
        viewC.setOnClickListener(new ThumbnailClickListener(compactHolder));
        viewC.setOnLongClickListener(new ThumbnailLongClickListener(compactHolder));
        View viewC2 = Utils.findRequiredView(view, R.id.holder_list_text_wrapper, "field 'mTitleWrapper', method 'onRowClicked', and method 'onPostLongClicked'");
        compactHolder.mTitleWrapper = (LinearLayout) Utils.castView(viewC2, R.id.holder_list_text_wrapper, "field 'mTitleWrapper'", LinearLayout.class);
        this.titleWrapperView = viewC2;
        viewC2.setOnClickListener(new RowClickListener(compactHolder));
        viewC2.setOnLongClickListener(new PostLongClickListener(compactHolder));
        compactHolder.mTitle = (PostTitleTextView) Utils.findRequiredViewAsType(view, R.id.holder_list_title, "field 'mTitle'", PostTitleTextView.class);
        compactHolder.mDescription = (PostDescriptionTextView) Utils.findRequiredViewAsType(view, R.id.holder_list_description, "field 'mDescription'", PostDescriptionTextView.class);
        compactHolder.mExtraWrapper = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.holder_list_generic_extra_wrapper, "field 'mExtraWrapper'", ViewGroup.class);
        compactHolder.mAwards = (AwardsTextView) Utils.findRequiredViewAsType(view, R.id.holder_list_generic_awards, "field 'mAwards'", AwardsTextView.class);
        compactHolder.mPostFlair = (PostFlairTextView) Utils.findRequiredViewAsType(view, R.id.holder_list_generic_flair, "field 'mPostFlair'", PostFlairTextView.class);
        compactHolder.mExpandedWrapper = (CustomButtonsWrapper) Utils.findRequiredViewAsType(view, R.id.holder_list_expanded_wrapper, "field 'mExpandedWrapper'", CustomButtonsWrapper.class);
        View viewC3 = Utils.findRequiredView(view, R.id.holder_list_upvote, "field 'mUpvote' and method 'onExpandedButtonPressed'");
        compactHolder.mUpvote = (UpvoteButton) Utils.castView(viewC3, R.id.holder_list_upvote, "field 'mUpvote'", UpvoteButton.class);
        this.upvoteView = viewC3;
        viewC3.setOnClickListener(new UpvoteClickListener(compactHolder));
        View viewC4 = Utils.findRequiredView(view, R.id.holder_list_downupvote, "field 'mDownvote' and method 'onExpandedButtonPressed'");
        compactHolder.mDownvote = (DownvoteButton) Utils.castView(viewC4, R.id.holder_list_downupvote, "field 'mDownvote'", DownvoteButton.class);
        this.downvoteView = viewC4;
        viewC4.setOnClickListener(new DownvoteClickListener(compactHolder));
        View viewC5 = Utils.findRequiredView(view, R.id.holder_list_save, "field 'mSave', method 'onExpandedButtonPressed', and method 'onSaveLongClicked'");
        compactHolder.mSave = (SaveButton) Utils.castView(viewC5, R.id.holder_list_save, "field 'mSave'", SaveButton.class);
        this.saveView = viewC5;
        viewC5.setOnClickListener(new SaveClickListener(compactHolder));
        viewC5.setOnLongClickListener(new SaveLongClickListener(compactHolder));
        View viewC6 = Utils.findRequiredView(view, R.id.holder_list_hide, "field 'mHide' and method 'onExpandedButtonPressed'");
        compactHolder.mHide = (HideButton) Utils.castView(viewC6, R.id.holder_list_hide, "field 'mHide'", HideButton.class);
        this.hideView = viewC6;
        viewC6.setOnClickListener(new HideClickListener(compactHolder));
        View viewC7 = Utils.findRequiredView(view, R.id.holder_list_mod, "field 'mMod' and method 'onModMenuClicked'");
        compactHolder.mMod = (ModButton) Utils.castView(viewC7, R.id.holder_list_mod, "field 'mMod'", ModButton.class);
        this.modView = viewC7;
        viewC7.setOnClickListener(new ModClickListener(compactHolder));
        View viewC8 = Utils.findRequiredView(view, R.id.holder_list_profile, "field 'mProfile' and method 'onExpandedButtonPressed'");
        compactHolder.mProfile = (ProfileButton) Utils.castView(viewC8, R.id.holder_list_profile, "field 'mProfile'", ProfileButton.class);
        this.profileView = viewC8;
        viewC8.setOnClickListener(new ProfileClickListener(compactHolder));
        View viewC9 = Utils.findRequiredView(view, R.id.holder_list_more, "field 'mMore' and method 'onExpandedButtonPressed'");
        compactHolder.mMore = (MoreButton) Utils.castView(viewC9, R.id.holder_list_more, "field 'mMore'", MoreButton.class);
        this.moreView = viewC9;
        viewC9.setOnClickListener(new MoreButtonClickListener(compactHolder));
        View viewC10 = Utils.findRequiredView(view, R.id.holder_list_expand, "field 'mExpand' and method 'onMoreClicked'");
        compactHolder.mExpand = (ExpandButton) Utils.castView(viewC10, R.id.holder_list_expand, "field 'mExpand'", ExpandButton.class);
        this.expandView = viewC10;
        viewC10.setOnClickListener(new ExpandClickListener(compactHolder));
        View viewC11 = Utils.findRequiredView(view, R.id.holder_list_type, "field 'mType', method 'onTypeClicked', and method 'onTypeLongClicked'");
        compactHolder.mType = (TypeButton) Utils.castView(viewC11, R.id.holder_list_type, "field 'mType'", TypeButton.class);
        this.typeView = viewC11;
        viewC11.setOnClickListener(new TypeClickListener(compactHolder));
        viewC11.setOnLongClickListener(new TypeLongClickListener(compactHolder));
        compactHolder.mMoreWrapper = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.holder_list_more_wrapper, "field 'mMoreWrapper'", LinearLayout.class);
    }

    @Override
    public void unbind() {
        CompactHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        target.mThumbnail = null;
        target.mThumbnailWrapper = null;
        target.mTitleWrapper = null;
        target.mTitle = null;
        target.mDescription = null;
        target.mExtraWrapper = null;
        target.mAwards = null;
        target.mPostFlair = null;
        target.mExpandedWrapper = null;
        target.mUpvote = null;
        target.mDownvote = null;
        target.mSave = null;
        target.mHide = null;
        target.mMod = null;
        target.mProfile = null;
        target.mMore = null;
        target.mExpand = null;
        target.mType = null;
        target.mMoreWrapper = null;
        this.thumbnailWrapperView.setOnClickListener(null);
        this.thumbnailWrapperView.setOnLongClickListener(null);
        this.thumbnailWrapperView = null;
        this.titleWrapperView.setOnClickListener(null);
        this.titleWrapperView.setOnLongClickListener(null);
        this.titleWrapperView = null;
        this.upvoteView.setOnClickListener(null);
        this.upvoteView = null;
        this.downvoteView.setOnClickListener(null);
        this.downvoteView = null;
        this.saveView.setOnClickListener(null);
        this.saveView.setOnLongClickListener(null);
        this.saveView = null;
        this.hideView.setOnClickListener(null);
        this.hideView = null;
        this.modView.setOnClickListener(null);
        this.modView = null;
        this.profileView.setOnClickListener(null);
        this.profileView = null;
        this.moreView.setOnClickListener(null);
        this.moreView = null;
        this.expandView.setOnClickListener(null);
        this.expandView = null;
        this.typeView.setOnClickListener(null);
        this.typeView.setOnLongClickListener(null);
        this.typeView = null;
    }
}
