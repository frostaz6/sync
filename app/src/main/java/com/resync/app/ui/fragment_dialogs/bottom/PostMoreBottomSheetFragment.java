package com.resync.app.ui.fragment_dialogs.bottom;

import android.os.Bundle;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.resync.app.R;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.activities.CasualActivity;
import com.resync.app.ui.activities.CommentsActivity;
import com.resync.app.ui.fragment_dialogs.EditFragment;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.flair.ModPostPickFlairRadioSelectionDialogBottomSheet;
import com.resync.app.ui.views.core.MaterialRow;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.WatchingHelper;
import com.resync.app.dialogs.TranslateAsyncAlertDialogBottomSheet;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.StopExoplayerEvent;
import com.resync.app.managers.HideManager;
import com.resync.app.managers.SaveManager;
import com.resync.app.managers.VoteManager;
import com.resync.app.dialogs.CustomBottomSheetDialogFragment;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.PostsHelper;
import com.resync.app.helpers.RemoteConfigHelper;
import com.resync.app.dialogs.AddToMultiAsyncRadioSelectionDialogBottomSheet;
import com.resync.app.dialogs.AwardsPostAsyncSelectionDialogBottomSheet;
import com.resync.app.dialogs.CopyPostDialogBottomSheet;
import com.resync.app.dialogs.DeletePostAlertDialogBottomSheet;
import com.resync.app.dialogs.FilterSelectionDialogBottomSheet;
import com.resync.app.dialogs.PostInboxRepliesAlertDialogBottomSheet;
import com.resync.app.dialogs.ReportPostInputDialogBottomSheet;
import com.resync.app.premium.UltraHelper;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkHelper;
import com.resync.app.util.links.LinkHandler;

/* JADX INFO: loaded from: classes.dex */
public final class PostMoreBottomSheetFragment extends CustomBottomSheetDialogFragment {
    private Post post;
    private int uiMode;

    @BindView(R.id.more_awards)
    MaterialRow mAwards;

    @BindView(R.id.more_crosspost)
    MaterialRow mCrossPost;

    @BindView(R.id.more_delete)
    MaterialRow mDelete;

    @BindView(R.id.more_downvote)
    MaterialRow mDownvote;

    @BindView(R.id.more_edit)
    MaterialRow mEdit;

    @BindView(R.id.more_explore)
    MaterialRow mExplore;

    @BindView(R.id.more_flair)
    MaterialRow mFlair;

    @BindView(R.id.more_hide)
    MaterialRow mHide;

    @BindView(R.id.more_mark_as_read)
    MaterialRow mMarkAsRead;

    @BindView(R.id.more_profile)
    MaterialRow mProfile;

    @BindView(R.id.more_replies)
    MaterialRow mReplies;

    @BindView(R.id.more_save)
    MaterialRow mSave;

    @BindView(R.id.more_share)
    MaterialRow mShare;

    @BindView(R.id.more_subreddit)
    MaterialRow mSubreddit;

    @BindView(R.id.more_translate)
    MaterialRow mTranslate;

    @BindView(R.id.more_upvote)
    MaterialRow mUpvote;

    @BindView(R.id.more_watch)
    MaterialRow mWatch;

    public static Bundle buildArgs(Post post, int uiMode) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Post", post);
        bundle.putInt("ui_mode", uiMode);
        return bundle;
    }

    private boolean onProgressReady() {
        return this.uiMode == 7 && (UiSingleton.getInstance().getUiMode() == 0 || UiSingleton.getInstance().getUiMode() == 2 || UiSingleton.getInstance().getUiMode() == 3 || UiSingleton.getInstance().getUiMode() == 4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onShareClicked(View view) {
        getPost().share(getActivity(), true);
        dismiss();
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.c, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public Post getPost() {
        return this.post;
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
        this.uiMode = getArguments().getInt("ui_mode");
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.dialog_bottom_post_more;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.mAwards.setVisibility(this.post.hasAwards() ? 0 : 8);
        try {
            if ((getActivity() instanceof CommentsActivity) && ((CommentsActivity) getActivity()).getCommentsFragment() != null && !((CommentsActivity) getActivity()).getCommentsFragment().getPresenter().getPost().isOver18() && UltraHelper.isUltra() && RemoteConfigHelper.isUltraTranslate() && this.post.getSyncType() == 2 && StringUtils.isNotEmpty(this.post.getSelftextProcessed())) {
                this.mTranslate.setVisibility(0);
            }
        } catch (Exception e) {
            Logger.logException(e);
        }
        if (this.post.isSaved()) {
            this.mSave.setTitle("Unsave");
        }
        if (this.uiMode != 4 && !onProgressReady()) {
            int mode = this.uiMode;
            if (mode == 5) {
                if (SettingsSingleton.getSettings().slide_buttons_save) {
                    this.mSave.setVisibility(8);
                }
            } else if (mode == 3) {
                if (SettingsSingleton.getSettings().small_card_buttons_upvote) {
                    this.mUpvote.setVisibility(8);
                }
                if (SettingsSingleton.getSettings().small_card_buttons_downvote) {
                    this.mDownvote.setVisibility(8);
                }
                if (SettingsSingleton.getSettings().small_card_buttons_save) {
                    this.mSave.setVisibility(8);
                }
            } else {
                this.mUpvote.setVisibility(8);
                this.mDownvote.setVisibility(8);
            }
        } else if (SettingsSingleton.getSettings().card_buttons_save) {
            this.mSave.setVisibility(8);
        }
        if (this.post.isHidden()) {
            this.mHide.setTitle("Unhide");
            this.mHide.setVisibility(0);
        }
        if (AccountSingleton.getInstance().getUsername().equalsIgnoreCase(this.post.getAuthor())) {
            this.mDelete.setVisibility(0);
            this.mFlair.setVisibility(0);
            this.mReplies.setVisibility(0);
            MaterialRow materialRow = this.mReplies;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Inbox replies (");
            sb2.append(this.post.isSendReplies() ? "enabled" : "disabled");
            sb2.append(")");
            materialRow.setTitle(sb2.toString());
            if (this.post.getSyncType() == 2) {
                this.mEdit.setVisibility(0);
            }
        } else {
            this.mDelete.setVisibility(8);
            this.mFlair.setVisibility(8);
            this.mReplies.setVisibility(8);
            this.mEdit.setVisibility(8);
        }
        if (this.post.isVisitedOrInternal()) {
            this.mMarkAsRead.setVisibility(8);
        }
        if (!AccountSingleton.getInstance().isLoggedIn()) {
            this.mWatch.setVisibility(8);
        } else if (WatchingHelper.isWatched(this.post)) {
            this.mWatch.setTitle("Unwatch");
        }
        if (this.post.getSyncType() != 1 && this.post.getSyncType() != 3 && this.post.getSyncType() != 4 && this.post.getSyncType() != 7 && this.post.getSyncType() != 9 && this.post.getSyncType() != 10 && this.post.getSyncType() != 6) {
            this.mExplore.setVisibility(8);
        }
        if (StringUtils.isEmpty(getPost().getPostCrossLink())) {
            this.mCrossPost.setVisibility(8);
        }
        this.mProfile.setTitle("/u/" + this.post.getAuthor());
        if (StringUtils.equalsIgnoreCase(this.post.getAuthor(), "[deleted]")) {
            this.mProfile.setVisibility(8);
        }
        this.mSubreddit.setTitle("/r/" + this.post.getSubreddit());
        this.mExplore.setTitle("Explore domain (" + this.post.getDomain() + ")");
        this.mShare.showMore();
        this.mShare.setMoreClickListener(new View.OnClickListener() { // from class: s9.i
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                PostMoreBottomSheetFragment.this.onShareClicked(view2);
            }
        });
    }

    @OnClick
    public void onItemClicked(View view) {
        int id = view.getId();
        if (id == R.id.more_replies) {
            FragmentUtils.showWithPost(PostInboxRepliesAlertDialogBottomSheet.class, getChildFragmentManager(), this.post);
        } else if (id == R.id.more_translate) {
            FragmentUtils.showWithArguments(TranslateAsyncAlertDialogBottomSheet.class, getParentFragmentManager(), TranslateAsyncAlertDialogBottomSheet.buildArgs(this.post, null));
        } else if (id == R.id.more_awards) {
            FragmentUtils.showWithPostId(AwardsPostAsyncSelectionDialogBottomSheet.class, getParentFragmentManager(), this.post.getId());
        }
        if (id == R.id.more_upvote) {
            VoteManager.voteOnPost(getActivity(), 0, this.post, 1);
        } else if (id == R.id.more_downvote) {
            VoteManager.voteOnPost(getActivity(), 0, this.post, -1);
        } else if (id == R.id.more_save) {
            SaveManager.toggleSave(getActivity(), this.post);
        }
        if (id == R.id.more_hide) {
            BusSingleton.getBus().post(new StopExoplayerEvent());
            if (getPost().isHidden()) {
                HideManager.unhide(getActivity(), getPost());
            } else {
                HideManager.hide(getActivity(), getPost());
            }
        } else if (id == R.id.more_edit) {
            EditFragment.newEditPostInstance(getPost()).show(getActivity().getSupportFragmentManager(), "EditFragment");
        } else if (id == R.id.more_delete) {
            FragmentUtils.showWithPostId(DeletePostAlertDialogBottomSheet.class, getParentFragmentManager(), getPost().getId());
        } else if (id == R.id.more_flair) {
            FragmentUtils.showWithPost(ModPostPickFlairRadioSelectionDialogBottomSheet.class, getChildFragmentManager(), getPost());
        } else if (id == R.id.more_mark_as_read) {
            PostsHelper.markViewed(getActivity(), getPost());
        } else if (id == R.id.more_share) {
            getPost().share(getActivity(), false);
        } else if (id == R.id.more_watch) {
            if (WatchingHelper.isWatched(getPost())) {
                WatchingHelper.removeWatchedPost(getActivity(), getPost());
            } else {
                WatchingHelper.addWatchedPost(getPost());
                StaticToast.showWithContext(getActivity(), "Watching post!");
            }
        } else if (id == R.id.more_report) {
            if (AccountSingleton.getInstance().isLoggedIn()) {
                FragmentUtils.showWithPostId(ReportPostInputDialogBottomSheet.class, getParentFragmentManager(), getPost().getId());
            } else {
                StaticToast.show(getActivity(), R.string.common_generic_error_logged_out);
            }
        } else if (id == R.id.more_profile) {
            LinkHandler.openProfile(getActivity(), this.post.getAuthor());
        } else if (id == R.id.more_subreddit) {
            LinkHandler.openSubreddit(getActivity(), this.post.getSubreddit());
        } else if (id == R.id.more_explore) {
            CasualActivity.openPostDomain(getActivity(), getPost());
        } else if (id == R.id.more_external) {
            if (TextUtils.isEmpty(getPost().getUrl())) {
                LinkHandler.openExternal(getActivity(), "https://www.reddit.com/r/" + getPost().getSubreddit() + "/comments/" + getPost().getId());
            } else {
                LinkHandler.openExternal(getActivity(), getPost().getUrl());
            }
        } else if (id == R.id.more_copy) {
            FragmentUtils.showWithArguments(CopyPostDialogBottomSheet.class, getParentFragmentManager(), CopyPostDialogBottomSheet.buildArgs(getPost()));
        } else if (id == R.id.more_filter) {
            FragmentUtils.showWithArguments(FilterSelectionDialogBottomSheet.class, getParentFragmentManager(), FilterSelectionDialogBottomSheet.buildArguments(getPost()));
        } else if (id == R.id.more_add_multi) {
            FragmentUtils.showWithSubreddit(AddToMultiAsyncRadioSelectionDialogBottomSheet.class, getChildFragmentManager(), getPost().getSubreddit());
            dismiss();
        } else if (id == R.id.more_crosspost) {
            LinkHelper.open(getContext(), getPost().getPostCrossLink());
        }
        dismiss();
    }
}
