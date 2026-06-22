package com.resync.app.ui.fragments;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.drag.CustomPhotoViewVerticalDragLayout;
import com.resync.app.ui.views.drag.SubsamplingViewVerticalDragLayout;
import com.resync.app.ui.views.image_progressbar.ImageProgressBar;
import com.resync.app.ui.views.text.BaseTextView;
import com.resync.app.ui.views.text.spannable.children.HtmlTextView;

/* JADX INFO: loaded from: classes2.dex */
public class ImageViewerFragment_ViewBinding implements Unbinder {
    private ImageViewerFragment target;
    private View view1;
    private View view2;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private View view3;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private View view4;
    private View view5;
    private View view6;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private View view7;
    private View view8;
    private View view9;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private View view10;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private View view11;
    private View view12;
    private View view13;
    private View view14;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private View view15;

    class OnCopyUrlListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ ImageViewerFragment target;

        OnCopyUrlListener(ImageViewerFragment imageViewerFragment) {
            this.target = imageViewerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onCopyUrl();
        }
    }

    class OnShareUrlListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ ImageViewerFragment target;

        OnShareUrlListener(ImageViewerFragment imageViewerFragment) {
            this.target = imageViewerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onShareUrl();
        }
    }

    class OnSearchClicedListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ ImageViewerFragment target;

        OnSearchClicedListener(ImageViewerFragment imageViewerFragment) {
            this.target = imageViewerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onSearchCliced();
        }
    }

    class OnExtractClickedListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ ImageViewerFragment target;

        OnExtractClickedListener(ImageViewerFragment imageViewerFragment) {
            this.target = imageViewerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onExtractClicked();
        }
    }

    class OnSaveImageListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ ImageViewerFragment target;

        OnSaveImageListener(ImageViewerFragment imageViewerFragment) {
            this.target = imageViewerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onSaveImage();
        }
    }

    class OnSaveImage2Listener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ ImageViewerFragment target;

        OnSaveImage2Listener(ImageViewerFragment imageViewerFragment) {
            this.target = imageViewerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onSaveImage();
        }
    }

    class OnGifHdButtonClickedListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ ImageViewerFragment target;

        OnGifHdButtonClickedListener(ImageViewerFragment imageViewerFragment) {
            this.target = imageViewerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onGifHdButtonClicked();
        }
    }

    class OnGifBackButtonClickedListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ ImageViewerFragment target;

        OnGifBackButtonClickedListener(ImageViewerFragment imageViewerFragment) {
            this.target = imageViewerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onGifBackButtonClicked();
        }
    }

    class OnVolumeButtonClickedListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ ImageViewerFragment target;

        OnVolumeButtonClickedListener(ImageViewerFragment imageViewerFragment) {
            this.target = imageViewerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onVolumeButtonClicked(view);
        }
    }

    class OnGifButtonClickedListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ ImageViewerFragment target;

        OnGifButtonClickedListener(ImageViewerFragment imageViewerFragment) {
            this.target = imageViewerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onGifButtonClicked(view);
        }
    }

    class OnPeekAreaClickedListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ ImageViewerFragment target;

        OnPeekAreaClickedListener(ImageViewerFragment imageViewerFragment) {
            this.target = imageViewerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onPeekAreaClicked();
        }
    }

    class OnSpeedMinusClickedListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ ImageViewerFragment target;

        OnSpeedMinusClickedListener(ImageViewerFragment imageViewerFragment) {
            this.target = imageViewerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onSpeedMinusClicked();
        }
    }

    class OnSpeedPlusClickedListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ ImageViewerFragment target;

        OnSpeedPlusClickedListener(ImageViewerFragment imageViewerFragment) {
            this.target = imageViewerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onSpeedPlusClicked();
        }
    }

    class OnOpenCommentsListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ ImageViewerFragment target;

        OnOpenCommentsListener(ImageViewerFragment imageViewerFragment) {
            this.target = imageViewerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onOpenComments();
        }
    }

    class OnOpenExternallyListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ ImageViewerFragment target;

        OnOpenExternallyListener(ImageViewerFragment imageViewerFragment) {
            this.target = imageViewerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onOpenExternally();
        }
    }

    public ImageViewerFragment_ViewBinding(ImageViewerFragment imageViewerFragment, View view) {
        this.target = imageViewerFragment;
        imageViewerFragment.mCoordinator = (CoordinatorLayout) Utils.findRequiredViewAsType(view, R.id.coordinator, "field 'mCoordinator'", CoordinatorLayout.class);
        imageViewerFragment.mImageContent = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.image_content, "field 'mImageContent'", RelativeLayout.class);
        imageViewerFragment.mImageView = (CustomPhotoViewVerticalDragLayout) Utils.findRequiredViewAsType(view, R.id.image_view, "field 'mImageView'", CustomPhotoViewVerticalDragLayout.class);
        imageViewerFragment.mSubsamplingImageView = (SubsamplingViewVerticalDragLayout) Utils.findRequiredViewAsType(view, R.id.subsampling_image_view, "field 'mSubsamplingImageView'", SubsamplingViewVerticalDragLayout.class);
        imageViewerFragment.mErrorMessage = (TextView) Utils.findRequiredViewAsType(view, R.id.image_error_message, "field 'mErrorMessage'", TextView.class);
        imageViewerFragment.mErrorButton = (Button) Utils.findRequiredViewAsType(view, R.id.image_error_open_button, "field 'mErrorButton'", Button.class);
        imageViewerFragment.mDebugLogMessage = (BaseTextView) Utils.findRequiredViewAsType(view, R.id.image_debug_log_message, "field 'mDebugLogMessage'", BaseTextView.class);
        imageViewerFragment.mErrorMessageWrapper = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.image_error_message_wrapper, "field 'mErrorMessageWrapper'", LinearLayout.class);
        imageViewerFragment.mImageProgressBar = (ImageProgressBar) Utils.findRequiredViewAsType(view, R.id.image_progress_bar, "field 'mImageProgressBar'", ImageProgressBar.class);
        imageViewerFragment.mOptionsButton = (AppCompatImageView) Utils.findRequiredViewAsType(view, R.id.image_options_button, "field 'mOptionsButton'", AppCompatImageView.class);
        imageViewerFragment.mBottomSheet = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.image_bottomsheet, "field 'mBottomSheet'", LinearLayout.class);
        imageViewerFragment.mBottomSheetPeekArea = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.image_bottomsheet_peek_area, "field 'mBottomSheetPeekArea'", RelativeLayout.class);
        imageViewerFragment.mBottomSheetContent = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.image_bottomsheet_content, "field 'mBottomSheetContent'", LinearLayout.class);
        imageViewerFragment.mBottomSheetTitle = (TextView) Utils.findRequiredViewAsType(view, R.id.image_options_title, "field 'mBottomSheetTitle'", TextView.class);
        imageViewerFragment.mBottomSheetDescription = (HtmlTextView) Utils.findRequiredViewAsType(view, R.id.image_options_description, "field 'mBottomSheetDescription'", HtmlTextView.class);
        imageViewerFragment.mBottomSheetScrollView = (NestedScrollView) Utils.findRequiredViewAsType(view, R.id.image_options_nested, "field 'mBottomSheetScrollView'", NestedScrollView.class);
        imageViewerFragment.mBottomSheetTextButton = Utils.findRequiredView(view, R.id.image_options_text_button, "field 'mBottomSheetTextButton'");
        imageViewerFragment.mBottomSheetLinkTextView = (BaseTextView) Utils.findRequiredViewAsType(view, R.id.image_link, "field 'mBottomSheetLinkTextView'", BaseTextView.class);
        View viewC = Utils.findRequiredView(view, R.id.image_hd, "field 'mGifHdButton' and method 'onGifHdButtonClicked'");
        imageViewerFragment.mGifHdButton = (AppCompatImageView) Utils.castView(viewC, R.id.image_hd, "field 'mGifHdButton'", AppCompatImageView.class);
        this.view1 = viewC;
        viewC.setOnClickListener(new OnGifHdButtonClickedListener(imageViewerFragment));
        View viewC2 = Utils.findRequiredView(view, R.id.image_gif_back, "field 'mGifBackButton' and method 'onGifBackButtonClicked'");
        imageViewerFragment.mGifBackButton = (AppCompatImageView) Utils.castView(viewC2, R.id.image_gif_back, "field 'mGifBackButton'", AppCompatImageView.class);
        this.view2 = viewC2;
        viewC2.setOnClickListener(new OnGifBackButtonClickedListener(imageViewerFragment));
        View viewC3 = Utils.findRequiredView(view, R.id.image_gif_volume, "field 'mGifVolumeButton' and method 'onVolumeButtonClicked'");
        imageViewerFragment.mGifVolumeButton = (AppCompatImageView) Utils.castView(viewC3, R.id.image_gif_volume, "field 'mGifVolumeButton'", AppCompatImageView.class);
        this.view3 = viewC3;
        viewC3.setOnClickListener(new OnVolumeButtonClickedListener(imageViewerFragment));
        View viewC4 = Utils.findRequiredView(view, R.id.image_gif_controls, "field 'mGifButton' and method 'onGifButtonClicked'");
        imageViewerFragment.mGifButton = (AppCompatImageView) Utils.castView(viewC4, R.id.image_gif_controls, "field 'mGifButton'", AppCompatImageView.class);
        this.view4 = viewC4;
        viewC4.setOnClickListener(new OnGifButtonClickedListener(imageViewerFragment));
        imageViewerFragment.mGifTime = (TextView) Utils.findRequiredViewAsType(view, R.id.image_gif_time, "field 'mGifTime'", TextView.class);
        imageViewerFragment.mImageSize = (TextView) Utils.findRequiredViewAsType(view, R.id.image_size, "field 'mImageSize'", TextView.class);
        imageViewerFragment.mSeekBar = (SeekBar) Utils.findRequiredViewAsType(view, R.id.image_gif_seek, "field 'mSeekBar'", SeekBar.class);
        imageViewerFragment.mSeekBarWrapper = (FrameLayout) Utils.findRequiredViewAsType(view, R.id.image_gif_seek_wrapper, "field 'mSeekBarWrapper'", FrameLayout.class);
        imageViewerFragment.mDebugWrapper = Utils.findRequiredView(view, R.id.image_options_debug_wrapper, "field 'mDebugWrapper'");
        imageViewerFragment.mDescriptionWrapper = Utils.findRequiredView(view, R.id.image_options_description_wrapper, "field 'mDescriptionWrapper'");
        imageViewerFragment.mGifSpeedWrapper = Utils.findRequiredView(view, R.id.gif_speed_wrapper, "field 'mGifSpeedWrapper'");
        imageViewerFragment.mGifSpeedLabel = (TextView) Utils.findRequiredViewAsType(view, R.id.gif_speed_label, "field 'mGifSpeedLabel'", TextView.class);
        View viewC5 = Utils.findRequiredView(view, R.id.image_bottomsheet_peek_area_button, "method 'onPeekAreaClicked'");
        this.view5 = viewC5;
        viewC5.setOnClickListener(new OnPeekAreaClickedListener(imageViewerFragment));
        View viewC6 = Utils.findRequiredView(view, R.id.gif_speed_minus, "method 'onSpeedMinusClicked'");
        this.view6 = viewC6;
        viewC6.setOnClickListener(new OnSpeedMinusClickedListener(imageViewerFragment));
        View viewC7 = Utils.findRequiredView(view, R.id.gif_speed_plus, "method 'onSpeedPlusClicked'");
        this.view7 = viewC7;
        viewC7.setOnClickListener(new OnSpeedPlusClickedListener(imageViewerFragment));
        View viewC8 = Utils.findRequiredView(view, R.id.image_action_comments, "method 'onOpenComments'");
        this.view8 = viewC8;
        viewC8.setOnClickListener(new OnOpenCommentsListener(imageViewerFragment));
        View viewC9 = Utils.findRequiredView(view, R.id.image_action_external, "method 'onOpenExternally'");
        this.view9 = viewC9;
        viewC9.setOnClickListener(new OnOpenExternallyListener(imageViewerFragment));
        View viewC10 = Utils.findRequiredView(view, R.id.image_action_copy, "method 'onCopyUrl'");
        this.view10 = viewC10;
        viewC10.setOnClickListener(new OnCopyUrlListener(imageViewerFragment));
        View viewC11 = Utils.findRequiredView(view, R.id.image_action_share, "method 'onShareUrl'");
        this.view11 = viewC11;
        viewC11.setOnClickListener(new OnShareUrlListener(imageViewerFragment));
        View viewC12 = Utils.findRequiredView(view, R.id.image_link_search, "method 'onSearchCliced'");
        this.view12 = viewC12;
        viewC12.setOnClickListener(new OnSearchClicedListener(imageViewerFragment));
        View viewC13 = Utils.findRequiredView(view, R.id.image_options_extract_text, "method 'onExtractClicked'");
        this.view13 = viewC13;
        viewC13.setOnClickListener(new OnExtractClickedListener(imageViewerFragment));
        View viewC14 = Utils.findRequiredView(view, R.id.image_action_save, "method 'onSaveImage'");
        this.view14 = viewC14;
        viewC14.setOnClickListener(new OnSaveImageListener(imageViewerFragment));
        View viewC15 = Utils.findRequiredView(view, R.id.image_options_alt_download_button, "method 'onSaveImage'");
        this.view15 = viewC15;
        viewC15.setOnClickListener(new OnSaveImage2Listener(imageViewerFragment));
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        ImageViewerFragment imageViewerFragment = this.target;
        if (imageViewerFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        imageViewerFragment.mCoordinator = null;
        imageViewerFragment.mImageContent = null;
        imageViewerFragment.mImageView = null;
        imageViewerFragment.mSubsamplingImageView = null;
        imageViewerFragment.mErrorMessage = null;
        imageViewerFragment.mErrorButton = null;
        imageViewerFragment.mDebugLogMessage = null;
        imageViewerFragment.mErrorMessageWrapper = null;
        imageViewerFragment.mImageProgressBar = null;
        imageViewerFragment.mOptionsButton = null;
        imageViewerFragment.mBottomSheet = null;
        imageViewerFragment.mBottomSheetPeekArea = null;
        imageViewerFragment.mBottomSheetContent = null;
        imageViewerFragment.mBottomSheetTitle = null;
        imageViewerFragment.mBottomSheetDescription = null;
        imageViewerFragment.mBottomSheetScrollView = null;
        imageViewerFragment.mBottomSheetTextButton = null;
        imageViewerFragment.mBottomSheetLinkTextView = null;
        imageViewerFragment.mGifHdButton = null;
        imageViewerFragment.mGifBackButton = null;
        imageViewerFragment.mGifVolumeButton = null;
        imageViewerFragment.mGifButton = null;
        imageViewerFragment.mGifTime = null;
        imageViewerFragment.mImageSize = null;
        imageViewerFragment.mSeekBar = null;
        imageViewerFragment.mSeekBarWrapper = null;
        imageViewerFragment.mDebugWrapper = null;
        imageViewerFragment.mDescriptionWrapper = null;
        imageViewerFragment.mGifSpeedWrapper = null;
        imageViewerFragment.mGifSpeedLabel = null;
        this.view1 = null;
        this.view2 = null;
        this.view3 = null;
        this.view4 = null;
        this.view5 = null;
        this.view6 = null;
        this.view7 = null;
        this.view8 = null;
        this.view9 = null;
        this.view10 = null;
        this.view11 = null;
        this.view12 = null;
        this.view13 = null;
        this.view14 = null;
        this.view15 = null;
    }
}
