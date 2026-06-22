package com.resync.app.ui.fragment_dialogs;

import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.buttons.CommentButton;
import com.resync.app.ui.views.buttons.CopyButton;
import com.resync.app.ui.views.buttons.ExternalButton;
import com.resync.app.ui.views.buttons.ShareButton;
import com.resync.app.ui.views.images.CustomImageView;
import com.resync.app.ui.views.video.CustomExoPlayerView;

/* JADX INFO: loaded from: classes.dex */
public class ImagePeekDialogFragment_ViewBinding implements Unbinder {
    private ImagePeekDialogFragment target;

    public ImagePeekDialogFragment_ViewBinding(ImagePeekDialogFragment imagePeekDialogFragment, View view) {
        this.target = imagePeekDialogFragment;
        imagePeekDialogFragment.mRootView = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.dialog_fragment_image_preview_root, "field 'mRootView'", RelativeLayout.class);
        imagePeekDialogFragment.mProgressBar = (ProgressBar) Utils.findRequiredViewAsType(view, R.id.dialog_fragment_image_preview_progress, "field 'mProgressBar'", ProgressBar.class);
        imagePeekDialogFragment.mImageView = (CustomImageView) Utils.findRequiredViewAsType(view, R.id.dialog_fragment_image_preview_image, "field 'mImageView'", CustomImageView.class);
        imagePeekDialogFragment.mVideoView = (CustomExoPlayerView) Utils.findOptionalViewAsType(view, R.id.dialog_fragment_image_preview_video, "field 'mVideoView'", CustomExoPlayerView.class);
        imagePeekDialogFragment.mErrorMessage = (TextView) Utils.findRequiredViewAsType(view, R.id.dialog_fragment_image_preview_error, "field 'mErrorMessage'", TextView.class);
        imagePeekDialogFragment.mImageGrid = (GridView) Utils.findRequiredViewAsType(view, R.id.image_grid, "field 'mImageGrid'", GridView.class);
        imagePeekDialogFragment.mPeekOptionsWrapper = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.peek_options, "field 'mPeekOptionsWrapper'", LinearLayout.class);
        imagePeekDialogFragment.mCommentButton = (CommentButton) Utils.findRequiredViewAsType(view, R.id.peek_comments, "field 'mCommentButton'", CommentButton.class);
        imagePeekDialogFragment.mExternalButton = (ExternalButton) Utils.findRequiredViewAsType(view, R.id.peek_external, "field 'mExternalButton'", ExternalButton.class);
        imagePeekDialogFragment.mShareButton = (ShareButton) Utils.findRequiredViewAsType(view, R.id.peek_share, "field 'mShareButton'", ShareButton.class);
        imagePeekDialogFragment.mCopyButton = (CopyButton) Utils.findRequiredViewAsType(view, R.id.peek_copy, "field 'mCopyButton'", CopyButton.class);
    }

    @Override
    public void unbind() {
        ImagePeekDialogFragment target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.mRootView = null;
        target.mProgressBar = null;
        target.mImageView = null;
        target.mErrorMessage = null;
        target.mImageGrid = null;
        target.mPeekOptionsWrapper = null;
        target.mCommentButton = null;
        target.mExternalButton = null;
        target.mShareButton = null;
        target.mCopyButton = null;
    }
}
