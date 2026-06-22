package com.resync.app.ui.fragments.posts.pager;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.image_progressbar.ImageProgressBar;
import com.resync.app.ui.views.video.CustomExoPlayerView;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.StaticToast;
import java.util.Locale;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.WindowChromeChangedEvent;
import com.resync.app.events.WindowChromeToggleEvent;
import com.resync.app.ui.views.video.NewVideoInterface;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkHelper;
import com.resync.app.util.links.LinkTypeHelper;
import com.resync.app.ui.views.photoview.CustomExoPlayerViewAttacher;

/* JADX INFO: loaded from: classes2.dex */
public class PagerImageFragment extends AbstractPagerFragment {

    @BindView(R.id.fragment_swipe_image)
    SubsamplingScaleImageView mImagePreview;

    @BindView(R.id.fragment_swipe_progress_bar)
    ImageProgressBar mImageProgressBar;

    @BindView(R.id.fragment_swipe_indicator)
    ImageView mIndicator;

    @BindView(R.id.fragment_swipe_video_player)
    CustomExoPlayerView mVideoView;

    class PreviewClickListener implements View.OnClickListener {
        PreviewClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            BusSingleton.getBus().post(new WindowChromeToggleEvent());
        }
    }

    class VideoTapListener implements com.resync.app.ui.views.photoview.CustomExoPlayerViewAttacher.OnPhotoTapListener {
        VideoTapListener() {
        }

        @Override // com.resync.app.ui.views.photoview.CustomExoPlayerViewAttacher.OnPhotoTapListener
        public void onPhotoTap(View view, float x, float y) {
            BusSingleton.getBus().post(new WindowChromeToggleEvent());
        }

        @Override // com.resync.app.ui.views.photoview.CustomExoPlayerViewAttacher.OnPhotoTapListener
        public void onOutsidePhotoTap() {
        }
    }

    private <T extends View & NewVideoInterface> T getVideoView() {
        CustomExoPlayerView customExoPlayerView = this.mVideoView;
        if (customExoPlayerView != null) {
            return (T) customExoPlayerView;
        }
        throw new RuntimeException("There we no video players found");
    }

    private void playVideo(String str) {
    }

    private void playGfycat(String str) {
    }

    private void playGif(String str) {
    }

    public void hideProgress() {
        this.mImageProgressBar.setVisibility(8);
        if (this.mIndicator.getDrawable() instanceof BitmapDrawable) {
            this.mIndicator.setVisibility(0);
        }
    }

    void playRedditVideo(String str) {
        if (isAttached()) {
            this.mImageProgressBar.setVisibility(8);
            this.mImagePreview.setVisibility(8);
            getVideoView().setVisibility(0);
            this.mIndicator.setVisibility(8);
            ((NewVideoInterface) getVideoView()).setSource(str, false, SettingsSingleton.getSettings().videoMute);
            ((NewVideoInterface) getVideoView()).start();
        }
    }

    void showPreview(String str) {
        SubsamplingScaleImageView subsamplingScaleImageView = this.mImagePreview;
        if (subsamplingScaleImageView != null) {
            subsamplingScaleImageView.setVisibility(0);
        }
        this.mImagePreview.hasImage();
    }

    public void autoPlay(boolean auto) {
        if (auto) {
            if (SettingsSingleton.getSettings().autoplay == 2) {
                return;
            }
            if (SettingsSingleton.getSettings().autoplay == 1 && !ConnectivityHelper.isWifiConnected()) {
                return;
            }
        }
        this.mIndicator.getVisibility();
        if (getPost().getSyncType() == 7) {
            if (getPost().getUrl().endsWith(".gif")) {
                playGif(getPost().getUrl());
                return;
            } else {
                playVideo(getPost().getUrl());
                return;
            }
        }
        if (LinkTypeHelper.isRedditVideo(getPost().getUrl())) {
            playRedditVideo(getPost().getUrl());
            return;
        }
        if (getPost().getSyncType() == 1 && LinkTypeHelper.isStreamable(getPost().getUrl()) && !auto) {
            LinkHelper.open(getActivity(), null, getPost());
            return;
        }
        if (getPost().getSyncType() == 1 && getPost().getUrl().contains("mediadownloads.mlb.com") && !auto) {
            LinkHelper.open(getActivity(), null, getPost());
            return;
        }
        if (getPost().getSyncType() == 4 && !auto) {
            LinkHelper.open(getActivity(), null, getPost());
            return;
        }
        if (getPost().getSyncType() == 1 && LinkTypeHelper.isGfycat(getPost().getUrl())) {
            playGfycat(getPost().getUrl());
            return;
        }
        if (getPost().getSyncType() == 1 && LinkTypeHelper.isGiphy(getPost().getUrl()) && !auto) {
            LinkHelper.open(getActivity(), null, getPost());
            return;
        }
        if (getPost().getSyncType() == 1 && LinkTypeHelper.isVidme(getPost().getUrl()) && !auto) {
            LinkHelper.open(getActivity(), null, getPost());
            return;
        }
        if (getPost().getSyncType() == 7 && !auto) {
            LinkHelper.open(getActivity(), null, getPost());
            return;
        }
        if (getPost().getSyncType() == 10 && !auto) {
            LinkHelper.open(getActivity(), null, getPost());
            return;
        }
        if ((getPost().getSyncType() == 3 || getPost().getSyncType() == 9) && !auto) {
            LinkHelper.open(getActivity(), null, getPost());
        } else {
            if (auto) {
                return;
            }
            BusSingleton.getBus().post(new WindowChromeToggleEvent());
        }
    }

    public void onPageSelected() {
    }

    public void bindIndicator() {
        if (getPost() == null || TextUtils.isEmpty(getPost().getUrl())) {
            this.mIndicator.setVisibility(8);
            this.mImagePreview.setZoomEnabled(true);
            return;
        }
        if (LinkTypeHelper.isRedditVideo(getPost().getUrl())) {
            this.mIndicator.setImageBitmap(RedditApplication.playRedditBitmap);
            this.mIndicator.setVisibility(0);
            this.mImagePreview.setZoomEnabled(false);
            return;
        }
        if (getPost().getSyncType() == 1 && LinkTypeHelper.isStreamable(getPost().getUrl())) {
            this.mIndicator.setImageBitmap(RedditApplication.playStreamableBitmap);
            this.mIndicator.setVisibility(0);
            this.mImagePreview.setZoomEnabled(false);
            return;
        }
        if (getPost().getSyncType() == 4) {
            this.mIndicator.setImageBitmap(RedditApplication.playVideoBitmap);
            this.mIndicator.setVisibility(0);
            this.mImagePreview.setZoomEnabled(false);
            return;
        }
        if (getPost().getSyncType() == 1 && LinkTypeHelper.isGfycat(getPost().getUrl())) {
            this.mIndicator.setImageBitmap(RedditApplication.playGfycatBitmap);
            this.mIndicator.setVisibility(0);
            this.mImagePreview.setZoomEnabled(false);
            return;
        }
        if (getPost().getSyncType() == 1 && getPost().getUrl().contains("mediadownloads.mlb.com")) {
            this.mIndicator.setImageBitmap(RedditApplication.playVideoBitmap);
            this.mIndicator.setVisibility(0);
            this.mImagePreview.setZoomEnabled(false);
            return;
        }
        if (getPost().getSyncType() == 1 && LinkTypeHelper.isGiphy(getPost().getUrl())) {
            this.mIndicator.setImageBitmap(RedditApplication.playGiphyBitmap);
            this.mIndicator.setVisibility(0);
            this.mImagePreview.setZoomEnabled(false);
            return;
        }
        if (getPost().getSyncType() == 7) {
            this.mIndicator.setImageBitmap(RedditApplication.playGifBitmap);
            this.mIndicator.setVisibility(0);
            this.mImagePreview.setZoomEnabled(false);
        } else if (getPost().getSyncType() == 10) {
            this.mIndicator.setImageBitmap(RedditApplication.playAlbumBitmap);
            this.mIndicator.setVisibility(0);
            this.mImagePreview.setZoomEnabled(false);
        } else if (getPost().getSyncType() != 3 && getPost().getSyncType() != 9) {
            this.mIndicator.setVisibility(8);
            this.mImagePreview.setZoomEnabled(true);
        } else {
            this.mIndicator.setImageBitmap(RedditApplication.playExternalBitmap);
            this.mIndicator.setVisibility(0);
            this.mImagePreview.setZoomEnabled(false);
        }
    }

    void releaseVideo() {
        hideProgress();
        if (((NewVideoInterface) getVideoView()).isVideoPlaying()) {
            ((NewVideoInterface) getVideoView()).onStop();
            getVideoView().setVisibility(8);
            showPreview(getPreviewUrl());
        }
    }

    String getPreviewUrl() {
        Logger.verbose("Title: " + getPost().getTitle());
        Logger.verbose("URL: " + getPost().getUrl());
        Logger.verbose("PREVIEW URL: " + getPost().getPreviewUrl());
        Logger.verbose("TYPE: " + getPost().getSyncType());
        if (getPost().getSyncType() != 1 && getPost().getSyncType() != 7) {
            return !TextUtils.isEmpty(getPost().getPreviewUrl()) ? getPost().getPreviewUrl() : getPost().getThumbnailPlaceholder(getActivity());
        }
        if (!TextUtils.isEmpty(getPost().getPreviewUrl())) {
            if (LinkTypeHelper.isRedditVideo(getPost().getUrl())) {
                return getPost().getPreviewUrl();
            }
            if (LinkTypeHelper.isStreamable(getPost().getUrl())) {
                return getPost().getPreviewUrl();
            }
            if (LinkTypeHelper.isGfycat(getPost().getUrl())) {
                return getPost().getPreviewUrl();
            }
            if (LinkTypeHelper.isDeviantArt(getPost().getUrl())) {
                return getPost().getPreviewUrl();
            }
            if (LinkTypeHelper.isVidme(getPost().getUrl())) {
                return getPost().getPreviewUrl();
            }
            if (LinkTypeHelper.isGiphy(getPost().getUrl())) {
                return getPost().getPreviewUrl();
            }
            if (LinkTypeHelper.isMixtape(getPost().getUrl()) && (getPost().getUrl().contains(".mp4") || getPost().getUrl().contains(".webm"))) {
                return getPost().getPreviewUrl();
            }
            if (LinkTypeHelper.isSliMg(getPost().getUrl()) && getPost().getUrl().contains(".mp4")) {
                return getPost().getPreviewUrl();
            }
            if (getPost().getUrl().contains("redditmedia.com") && getPost().getUrl().contains("fm=mp4")) {
                return getPost().getPreviewUrl();
            }
            if (getPost().getUrl().contains("mediadownloads.mlb.com")) {
                return null;
            }
            String url = getPost().getUrl();
            Locale locale = Locale.ENGLISH;
            if (url.toLowerCase(locale).contains(".gif")) {
                return getPost().getPreviewUrl();
            }
            if (getPost().getUrl().toLowerCase(locale).contains(".mp4")) {
                return getPost().getPreviewUrl();
            }
        }
        return getPost().getUrl();
    }

    @Override // com.resync.app.ui.fragments.posts.pager.AbstractPagerFragment, androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: U1 */
    public void onDestroy() {
        this.mImagePreview.recycle();
        ((NewVideoInterface) getVideoView()).onStop();
        ((NewVideoInterface) getVideoView()).onDestroy();
        getVideoView().setVisibility(8);
        super.onDestroy();
    }

    @Override // com.resync.app.ui.fragments.RedditFragment, androidx.fragment.app.Fragment
    public void onPause() {
        releaseVideo();
        super.onPause();
    }

    @Override // androidx.fragment.app.Fragment
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length <= 0 || requestCode != 100) {
            return;
        }
        if (grantResults[0] != 0) {
            StaticToast.showWithContext(getActivity(), "Permission not granted!");
        } else {
            StaticToast.showWithContext(getActivity(), "Permission granted!");
            onDownloadClicked();
        }
    }

    @Override // com.resync.app.ui.interfaces.RedditFragmentInterface
    public int getLayoutResId() {
        return R.layout.fragment_swipe_image;
    }

    @Override // com.resync.app.ui.fragments.posts.pager.AbstractPagerFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (SettingsSingleton.getSettings().deepzoomDpi != -1) {
            this.mImagePreview.setMinimumTileDpi(SettingsSingleton.getSettings().deepzoomDpi);
        }
    }

    @Subscribe
    public void onChromeChanged(WindowChromeChangedEvent windowChromeChangedEvent) {
        if (windowChromeChangedEvent.visible) {
            this.mTextWrapper.animate().alpha(1.0f).setDuration(200L).start();
            this.mButtonsWrapper.animate().alpha(1.0f).setDuration(200L).start();
        } else {
            this.mTextWrapper.animate().alpha(0.0f).setDuration(200L).start();
            this.mButtonsWrapper.animate().alpha(0.0f).setDuration(200L).start();
        }
        this.mButtonUpvote.setEnabled(windowChromeChangedEvent.visible);
        this.mButtonDownvote.setEnabled(windowChromeChangedEvent.visible);
        this.mButtonSave.setEnabled(windowChromeChangedEvent.visible);
        this.mButtonComment.setEnabled(windowChromeChangedEvent.visible);
        this.mButtonDownload.setEnabled(windowChromeChangedEvent.visible);
        this.mButtonMore.setEnabled(windowChromeChangedEvent.visible);
        this.commentsSheet.setScrollable(windowChromeChangedEvent.visible);
    }

    @OnClick
    public void onDownloadClicked() {
        ContextCompat.checkSelfPermission(getActivity(), "android.permission.WRITE_EXTERNAL_STORAGE");
    }

    @OnClick
    public void onIndicatorClicked() {
        autoPlay(false);
    }

    @Override // com.resync.app.ui.fragments.posts.pager.AbstractPagerFragment
    public void bindPost(Post post) {
        super.bindPost(post);
        if (getPost().getSyncType() == 1 || getPost().getSyncType() == 7) {
            this.mButtonDownload.setVisibility(0);
        }
        showPreview(getPreviewUrl());
        this.mImagePreview.setOnClickListener(new PreviewClickListener());
        CustomExoPlayerView customExoPlayerView = this.mVideoView;
        if (customExoPlayerView != null) {
            customExoPlayerView.setOnPhotoTapListener(new VideoTapListener());
        }
        bindIndicator();
    }

    @Override // com.resync.app.ui.fragments.posts.pager.AbstractPagerFragment
    public void releaseMedia() {
        releaseVideo();
    }
}
