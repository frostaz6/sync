package com.resync.app.ui.fragments.posts.pager;

import android.view.View;
import android.widget.ImageView;
import butterknife.internal.Utils;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.resync.app.R;
import com.resync.app.ui.views.image_progressbar.ImageProgressBar;
import com.resync.app.ui.views.video.CustomExoPlayerView;

/* JADX INFO: loaded from: classes2.dex */
public class PagerImageFragment_ViewBinding extends AbstractPagerFragment_ViewBinding {
    private PagerImageFragment target;
    private View viewIndicator;
    private View viewDownload;

    class OnIndicatorClickListener extends com.resync.app.util.DebouncingOnClickListener {
        final /* synthetic */ PagerImageFragment target;

        OnIndicatorClickListener(PagerImageFragment pagerImageFragment) {
            this.target = pagerImageFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onIndicatorClicked();
        }
    }

    class OnDownloadClickListener extends com.resync.app.util.DebouncingOnClickListener {
        final /* synthetic */ PagerImageFragment target;

        OnDownloadClickListener(PagerImageFragment pagerImageFragment) {
            this.target = pagerImageFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onDownloadClicked();
        }
    }

    public PagerImageFragment_ViewBinding(PagerImageFragment pagerImageFragment, View view) {
        super(pagerImageFragment, view);
        this.target = pagerImageFragment;
        pagerImageFragment.mImagePreview = (SubsamplingScaleImageView) Utils.findRequiredViewAsType(view, R.id.fragment_swipe_image, "field 'mImagePreview'", SubsamplingScaleImageView.class);
        View viewC = Utils.findRequiredView(view, R.id.fragment_swipe_indicator, "field 'mIndicator' and method 'onIndicatorClicked'");
        pagerImageFragment.mIndicator = (ImageView) Utils.castView(viewC, R.id.fragment_swipe_indicator, "field 'mIndicator'", ImageView.class);
        this.viewIndicator = viewC;
        viewC.setOnClickListener(new OnIndicatorClickListener(pagerImageFragment));
        pagerImageFragment.mImageProgressBar = (ImageProgressBar) Utils.findRequiredViewAsType(view, R.id.fragment_swipe_progress_bar, "field 'mImageProgressBar'", ImageProgressBar.class);
        pagerImageFragment.mVideoView = (CustomExoPlayerView) Utils.findOptionalViewAsType(view, R.id.fragment_swipe_video_player, "field 'mVideoView'", CustomExoPlayerView.class);
        View viewC2 = Utils.findRequiredView(view, R.id.fragment_swipe_download, "method 'onDownloadClicked'");
        this.viewDownload = viewC2;
        viewC2.setOnClickListener(new OnDownloadClickListener(pagerImageFragment));
    }
}
