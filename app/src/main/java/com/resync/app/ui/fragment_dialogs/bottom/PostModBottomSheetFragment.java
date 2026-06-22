package com.resync.app.ui.fragment_dialogs.bottom;

import android.content.ContentValues;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.resync.app.R;
import com.resync.app.provider.RedditProvider;
import com.resync.app.ui.activities.BanActivity;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.flair.ModPostPickFlairRadioSelectionDialogBottomSheet;
import com.resync.app.ui.views.core.MaterialRow;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.Kinds;
import com.resync.app.dialogs.CustomBottomSheetDialogFragment;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.dialogs.ReportReasonsDialogBottomSheet;
import com.resync.app.model.Post;
import com.resync.app.net.oauth.OAuthModApproveRequest;
import com.resync.app.net.oauth.OAuthModDistinguishRequest;
import com.resync.app.net.oauth.OAuthModLockRequest;
import com.resync.app.net.oauth.OAuthModNsfwRequest;
import com.resync.app.net.oauth.OAuthModRemoveRequest;
import com.resync.app.net.oauth.OAuthModSpoilerRequest;
import com.resync.app.net.oauth.OAuthModStickyRequest;
import com.resync.app.net.oauth.OAuthModUnlockRequest;

/* JADX INFO: loaded from: classes.dex */
public final class PostModBottomSheetFragment extends CustomBottomSheetDialogFragment {
    private Post post;

    @BindView(R.id.mod_approve)
    MaterialRow mApprove;

    @BindView(R.id.mod_ban)
    MaterialRow mBan;

    @BindView(R.id.mod_distinguish)
    MaterialRow mDistinguish;

    @BindView(R.id.mod_flair)
    MaterialRow mFlair;

    @BindView(R.id.mod_lock)
    MaterialRow mLock;

    @BindView(R.id.mod_nsfw)
    MaterialRow mNsfw;

    @BindView(R.id.mod_reasons)
    MaterialRow mReasons;

    @BindView(R.id.mod_remove)
    MaterialRow mRemove;

    @BindView(R.id.mod_spam)
    MaterialRow mSpam;

    @BindView(R.id.mod_spoiler)
    MaterialRow mSpoiler;

    @BindView(R.id.mod_sticky)
    MaterialRow mSticky;

    private void markApproved(String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", str);
        getContext().getContentResolver().update(RedditProvider.URI_UPDATE_APPROVED, contentValues, null, null);
        getContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
    }

    private void markDistinguished(String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", str);
        getContext().getContentResolver().update(RedditProvider.URI_UPDATE_DISTINGUISHED, contentValues, null, null);
        getContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
    }

    public static Bundle buildArgs(Post post) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Post", post);
        return bundle;
    }

    private void markLocked(String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", str);
        getContext().getContentResolver().update(RedditProvider.URI_UPDATE_LOCKED, contentValues, null, null);
        getContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
    }

    private void markNsfw(String str, boolean nsfw) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", str);
        contentValues.put("over_18", Integer.valueOf(nsfw ? 1 : 0));
        getContext().getContentResolver().update(RedditProvider.URI_UPDATE_NSFW, contentValues, null, null);
        getContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
    }

    private void markHidden(String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", str);
        contentValues.put("hidden", (Integer) 1);
        getContext().getContentResolver().update(RedditProvider.URI_UPDATE_HIDDEN, contentValues, null, null);
        getContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
    }

    private void markSpoiler(String str, boolean spoiler) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", str);
        contentValues.put("spoiler", Integer.valueOf(spoiler ? 1 : 0));
        getContext().getContentResolver().update(RedditProvider.URI_UPDATE_SPOILER, contentValues, null, null);
        getContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
    }

    private void markSticky(String str, boolean sticky) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", str);
        contentValues.put("stickied", Integer.valueOf(sticky ? 1 : 0));
        getContext().getContentResolver().update(RedditProvider.URI_UPDATE_STICKY, contentValues, null, null);
        getContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
    }

    private void markUnlocked(String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", str);
        getContext().getContentResolver().update(RedditProvider.URI_UPDATE_UNLOCKED, contentValues, null, null);
        getContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isInitiallyExpanded() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.post = (Post) getArguments().getSerializable("Post");
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.dialog_bottom_post_mod;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        if (this.post.isLocked()) {
            this.mLock.setTitle("Unlock");
        }
        if (TextUtils.isEmpty(this.post.getUserReports())) {
            this.mReasons.setVisibility(8);
        }
        this.mSticky.setTitle(this.post.isStickied() ? "Unsticky post" : "Sticky post");
        this.mNsfw.setTitle(this.post.isOver18() ? "Not NSFW" : "NSFW");
        this.mSpoiler.setTitle(this.post.isSpoiler() ? "Not spoiler" : "Spoiler");
    }

    @OnClick
    public void onApproveClicked() {
        StaticToast.showWithContext(getContext(), "Post approved");
        OAuthRequestManager.sendWithAppContext(new OAuthModApproveRequest(getContext(), Kinds.toPrefix(3), this.post.getId()));
        markApproved(this.post.getId());
        dismiss();
    }

    @OnClick
    public void onBanClicked() {
        startActivity(BanActivity.createIntent(getContext(), this.post.getAuthor(), this.post.getSubreddit()));
        dismiss();
    }

    @OnClick
    public void onDistinguishClicked() {
        StaticToast.showWithContext(getContext(), "Post distinguished");
        OAuthRequestManager.sendWithAppContext(new OAuthModDistinguishRequest(getContext(), Kinds.toPrefix(3), this.post.getId(), true, false));
        markDistinguished(this.post.getId());
        dismiss();
    }

    @OnClick
    public void onFlairClicked() {
        FragmentUtils.showWithPost(ModPostPickFlairRadioSelectionDialogBottomSheet.class, getChildFragmentManager(), this.post);
        dismiss();
    }

    @OnClick
    public void onLockClicked() {
        if (this.post.isLocked()) {
            StaticToast.showWithContext(getContext(), "Post unlocked");
            OAuthRequestManager.sendWithAppContext(new OAuthModUnlockRequest(getContext(), Kinds.toPrefix(3), this.post.getId()));
            markUnlocked(this.post.getId());
        } else {
            StaticToast.showWithContext(getContext(), "Post locked");
            OAuthRequestManager.sendWithAppContext(new OAuthModLockRequest(getContext(), Kinds.toPrefix(3), this.post.getId()));
            markLocked(this.post.getId());
        }
        dismiss();
    }

    @OnClick
    public void onNsfwClicked() {
        StaticToast.showWithContext(getContext(), this.post.isOver18() ? "Marked as not NSFW" : "Marked as NSFW");
        OAuthRequestManager.sendWithAppContext(new OAuthModNsfwRequest(getContext(), Kinds.toPrefix(3), this.post.getId(), !this.post.isOver18()));
        markNsfw(this.post.getId(), !this.post.isOver18());
        dismiss();
    }

    @OnClick
    public void onRemoveClicked() {
        StaticToast.showWithContext(getContext(), "Post removed");
        OAuthRequestManager.sendWithAppContext(new OAuthModRemoveRequest(getContext(), Kinds.toPrefix(3), this.post.getId(), false));
        markHidden(this.post.getId());
        dismiss();
    }

    @OnClick
    public void onReportReasonsClicked() {
        FragmentUtils.showWithArguments(ReportReasonsDialogBottomSheet.class, getParentFragmentManager(), ReportReasonsDialogBottomSheet.buildArguments(StringUtils.split(this.post.getUserReports(), ",")));
        dismiss();
    }

    @OnClick
    public void onSpamClicked() {
        StaticToast.showWithContext(getContext(), "Post marked as spam");
        OAuthRequestManager.sendWithAppContext(new OAuthModRemoveRequest(getContext(), Kinds.toPrefix(3), this.post.getId(), true));
        markHidden(this.post.getId());
        dismiss();
    }

    @OnClick
    public void onSpoilerClicked() {
        StaticToast.showWithContext(getContext(), this.post.isSpoiler() ? "Marked as not a spoiler" : "Marked as a spoiler");
        OAuthRequestManager.sendWithAppContext(new OAuthModSpoilerRequest(getContext(), Kinds.toPrefix(3), this.post.getId(), !this.post.isSpoiler()));
        markSpoiler(this.post.getId(), !this.post.isSpoiler());
        dismiss();
    }

    @OnClick
    public void onStickyClicked() {
        StaticToast.showWithContext(getContext(), this.post.isStickied() ? "Post unstickied" : "Post stickied");
        OAuthRequestManager.sendWithAppContext(new OAuthModStickyRequest(getContext(), "t3", this.post.getId(), !this.post.isStickied()));
        markSticky(this.post.getId(), !this.post.isStickied());
        dismiss();
    }
}
