package com.resync.app.ui.viewholders.posts.card;

import android.view.View;
import android.view.ViewGroup;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
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

/* JADX INFO: loaded from: classes3.dex */
public class AbstractCardPostHolder_ViewBinding implements Unbinder {
    private AbstractCardPostHolder target;
    private View baseView;
    private View replyView;
    private View upvoteView;
    private View downvoteView;
    private View saveView;
    private View modView;
    private View shareView;
    private View hideView;
    private View moreView;

    class ShareLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ AbstractCardPostHolder boundTarget;

        ShareLongClickListener(AbstractCardPostHolder abstractCardPostHolder) {
            this.boundTarget = abstractCardPostHolder;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.boundTarget.onShareLongClickedMaterial();
        }
    }

    class HideClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ AbstractCardPostHolder boundTarget;

        HideClickListener(AbstractCardPostHolder abstractCardPostHolder) {
            this.boundTarget = abstractCardPostHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onHideClicked();
        }
    }

    class MoreClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ AbstractCardPostHolder boundTarget;

        MoreClickListener(AbstractCardPostHolder abstractCardPostHolder) {
            this.boundTarget = abstractCardPostHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onMoreClicked();
        }
    }

    class PostClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ AbstractCardPostHolder boundTarget;

        PostClickListener(AbstractCardPostHolder abstractCardPostHolder) {
            this.boundTarget = abstractCardPostHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onPostclicked();
        }
    }

    class CardLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ AbstractCardPostHolder boundTarget;

        CardLongClickListener(AbstractCardPostHolder abstractCardPostHolder) {
            this.boundTarget = abstractCardPostHolder;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.boundTarget.onCardLongClicked();
        }
    }

    class ReplyClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ AbstractCardPostHolder boundTarget;

        ReplyClickListener(AbstractCardPostHolder abstractCardPostHolder) {
            this.boundTarget = abstractCardPostHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onReplyClicked();
        }
    }

    class UpvoteClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ AbstractCardPostHolder boundTarget;

        UpvoteClickListener(AbstractCardPostHolder abstractCardPostHolder) {
            this.boundTarget = abstractCardPostHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onUpvoteClicked(view);
        }
    }

    class DownvoteClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ AbstractCardPostHolder boundTarget;

        DownvoteClickListener(AbstractCardPostHolder abstractCardPostHolder) {
            this.boundTarget = abstractCardPostHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onDownvoteClicked(view);
        }
    }

    class SaveClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ AbstractCardPostHolder boundTarget;

        SaveClickListener(AbstractCardPostHolder abstractCardPostHolder) {
            this.boundTarget = abstractCardPostHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onSaveClicked();
        }
    }

    class SaveLongClickListener implements View.OnLongClickListener {

        final /* synthetic */ AbstractCardPostHolder boundTarget;

        SaveLongClickListener(AbstractCardPostHolder abstractCardPostHolder) {
            this.boundTarget = abstractCardPostHolder;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return this.boundTarget.onSaveLongClicked();
        }
    }

    class ModClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ AbstractCardPostHolder boundTarget;

        ModClickListener(AbstractCardPostHolder abstractCardPostHolder) {
            this.boundTarget = abstractCardPostHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onModMenuClicked();
        }
    }

    class ShareClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ AbstractCardPostHolder boundTarget;

        ShareClickListener(AbstractCardPostHolder abstractCardPostHolder) {
            this.boundTarget = abstractCardPostHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onShareClickedMaterial();
        }
    }

    public AbstractCardPostHolder_ViewBinding(AbstractCardPostHolder abstractCardPostHolder, View view) {
        this.target = abstractCardPostHolder;
        View viewC = Utils.findRequiredView(view, R.id.holder_card_generic_wrapper, "field 'mBase', method 'onPostclicked', and method 'onCardLongClicked'");
        abstractCardPostHolder.mBase = (CustomMaterialCardView) Utils.castView(viewC, R.id.holder_card_generic_wrapper, "field 'mBase'", CustomMaterialCardView.class);
        this.baseView = viewC;
        viewC.setOnClickListener(new PostClickListener(abstractCardPostHolder));
        viewC.setOnLongClickListener(new CardLongClickListener(abstractCardPostHolder));
        abstractCardPostHolder.mTitle = (PostTitleTextView) Utils.findRequiredViewAsType(view, R.id.holder_card_generic_title, "field 'mTitle'", PostTitleTextView.class);
        abstractCardPostHolder.mDescription = (PostDescriptionTextView) Utils.findRequiredViewAsType(view, R.id.holder_card_generic_desc, "field 'mDescription'", PostDescriptionTextView.class);
        abstractCardPostHolder.mExtraWrapper = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.holder_card_generic_extra_wrapper, "field 'mExtraWrapper'", ViewGroup.class);
        abstractCardPostHolder.mAwards = (AwardsTextView) Utils.findRequiredViewAsType(view, R.id.holder_card_generic_awards, "field 'mAwards'", AwardsTextView.class);
        abstractCardPostHolder.mPostFlair = (PostFlairTextView) Utils.findRequiredViewAsType(view, R.id.holder_card_generic_flair, "field 'mPostFlair'", PostFlairTextView.class);
        abstractCardPostHolder.mButtonsWrapper = Utils.findRequiredView(view, R.id.holder_card_generic_buttons, "field 'mButtonsWrapper'");
        abstractCardPostHolder.mStats = (PostStatsTextView) Utils.findRequiredViewAsType(view, R.id.holder_card_generic_buttons_stats, "field 'mStats'", PostStatsTextView.class);
        abstractCardPostHolder.mExpandLayer = Utils.findRequiredView(view, R.id.holder_card_generic_buttons_expand, "field 'mExpandLayer'");
        View viewC2 = Utils.findRequiredView(view, R.id.holder_card_buttons_reply, "field 'mReplyButton' and method 'onReplyClicked'");
        abstractCardPostHolder.mReplyButton = (ReplyButton) Utils.castView(viewC2, R.id.holder_card_buttons_reply, "field 'mReplyButton'", ReplyButton.class);
        this.replyView = viewC2;
        viewC2.setOnClickListener(new ReplyClickListener(abstractCardPostHolder));
        View viewC3 = Utils.findRequiredView(view, R.id.holder_card_buttons_up, "field 'mUpvoteButton' and method 'onUpvoteClicked'");
        abstractCardPostHolder.mUpvoteButton = (UpvoteButton) Utils.castView(viewC3, R.id.holder_card_buttons_up, "field 'mUpvoteButton'", UpvoteButton.class);
        this.upvoteView = viewC3;
        viewC3.setOnClickListener(new UpvoteClickListener(abstractCardPostHolder));
        View viewC4 = Utils.findRequiredView(view, R.id.holder_card_buttons_down, "field 'mDownvoteButton' and method 'onDownvoteClicked'");
        abstractCardPostHolder.mDownvoteButton = (DownvoteButton) Utils.castView(viewC4, R.id.holder_card_buttons_down, "field 'mDownvoteButton'", DownvoteButton.class);
        this.downvoteView = viewC4;
        viewC4.setOnClickListener(new DownvoteClickListener(abstractCardPostHolder));
        View viewC5 = Utils.findRequiredView(view, R.id.holder_card_buttons_save, "field 'mSaveButton', method 'onSaveClicked', and method 'onSaveLongClicked'");
        abstractCardPostHolder.mSaveButton = (SaveButton) Utils.castView(viewC5, R.id.holder_card_buttons_save, "field 'mSaveButton'", SaveButton.class);
        this.saveView = viewC5;
        viewC5.setOnClickListener(new SaveClickListener(abstractCardPostHolder));
        viewC5.setOnLongClickListener(new SaveLongClickListener(abstractCardPostHolder));
        View viewC6 = Utils.findRequiredView(view, R.id.holder_card_buttons_mod, "field 'mModButton' and method 'onModMenuClicked'");
        abstractCardPostHolder.mModButton = (ModButton) Utils.castView(viewC6, R.id.holder_card_buttons_mod, "field 'mModButton'", ModButton.class);
        this.modView = viewC6;
        viewC6.setOnClickListener(new ModClickListener(abstractCardPostHolder));
        View viewC7 = Utils.findRequiredView(view, R.id.holder_card_buttons_share, "field 'mShareButton', method 'onShareClickedMaterial', and method 'onShareLongClickedMaterial'");
        abstractCardPostHolder.mShareButton = (ShareButton) Utils.castView(viewC7, R.id.holder_card_buttons_share, "field 'mShareButton'", ShareButton.class);
        this.shareView = viewC7;
        viewC7.setOnClickListener(new ShareClickListener(abstractCardPostHolder));
        viewC7.setOnLongClickListener(new ShareLongClickListener(abstractCardPostHolder));
        View viewC8 = Utils.findRequiredView(view, R.id.holder_card_buttons_hide, "field 'mHideButton' and method 'onHideClicked'");
        abstractCardPostHolder.mHideButton = (HideButton) Utils.castView(viewC8, R.id.holder_card_buttons_hide, "field 'mHideButton'", HideButton.class);
        this.hideView = viewC8;
        viewC8.setOnClickListener(new HideClickListener(abstractCardPostHolder));
        View viewC9 = Utils.findRequiredView(view, R.id.holder_card_buttons_more, "field 'mMoreButton' and method 'onMoreClicked'");
        abstractCardPostHolder.mMoreButton = (CardButton) Utils.castView(viewC9, R.id.holder_card_buttons_more, "field 'mMoreButton'", CardButton.class);
        this.moreView = viewC9;
        viewC9.setOnClickListener(new MoreClickListener(abstractCardPostHolder));
    }

    @Override
    public void unbind() {
        AbstractCardPostHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        target.mBase = null;
        target.mTitle = null;
        target.mDescription = null;
        target.mExtraWrapper = null;
        target.mAwards = null;
        target.mPostFlair = null;
        target.mButtonsWrapper = null;
        target.mStats = null;
        target.mExpandLayer = null;
        target.mReplyButton = null;
        target.mUpvoteButton = null;
        target.mDownvoteButton = null;
        target.mSaveButton = null;
        target.mModButton = null;
        target.mShareButton = null;
        target.mHideButton = null;
        target.mMoreButton = null;
        this.baseView.setOnClickListener(null);
        this.baseView.setOnLongClickListener(null);
        this.baseView = null;
        this.replyView.setOnClickListener(null);
        this.replyView = null;
        this.upvoteView.setOnClickListener(null);
        this.upvoteView = null;
        this.downvoteView.setOnClickListener(null);
        this.downvoteView = null;
        this.saveView.setOnClickListener(null);
        this.saveView.setOnLongClickListener(null);
        this.saveView = null;
        this.modView.setOnClickListener(null);
        this.modView = null;
        this.shareView.setOnClickListener(null);
        this.shareView.setOnLongClickListener(null);
        this.shareView = null;
        this.hideView.setOnClickListener(null);
        this.hideView = null;
        this.moreView.setOnClickListener(null);
        this.moreView = null;
    }
}
