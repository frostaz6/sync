package com.resync.app.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.ColorUtils;
import androidx.core.util.Pair;
import androidx.core.widget.NestedScrollView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RedditHelper;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.chrisbanes.photoview.animation.OnErrorListener;
import com.github.chrisbanes.photoview.animation.OnStartListener;
import com.github.chrisbanes.photoview.animation.OnUpdateListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.jobs.media.DownloadMediaJob;
import com.resync.app.jobs.media.DownloadRedditVideoJob;
import com.resync.app.reddit.requests.external.requests.gfycat.GrabGfycatRequest;
import com.resync.app.reddit.requests.external.requests.gfycat.ScrapeGfycatRequest;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.media.AbstractImageActivity;
import com.resync.app.ui.activities.media.SingleImageActivity;
import com.resync.app.ui.views.behaviour.ImageViewerBottomSheetBehavior;
import com.resync.app.ui.views.drag.CustomPhotoViewVerticalDragLayout;
import com.resync.app.ui.views.drag.SubsamplingViewVerticalDragLayout;
import com.resync.app.ui.views.image_progressbar.ImageProgressBar;
import com.resync.app.ui.views.text.BaseTextView;
import com.resync.app.ui.views.text.spannable.children.HtmlTextView;
import com.resync.app.ui.views.video.CustomExoPlayerView;
import com.resync.app.helpers.ui.StaticToast;
import java.io.File;
import java.util.Formatter;
import java.util.Locale;
import com.resync.app.text.html.Options;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.widgets.drag.AbstractVerticalViewDragLayout;
import com.resync.app.widgets.drag.CustomExoPlayerViewVerticalDragLayout;
import com.resync.app.widgets.drag.DragClickListener;
import com.resync.app.widgets.drag.DragLongPressListener;
import com.resync.app.widgets.ImageEventListener;
import com.resync.app.media.imgur.ImgurImageHelper;
import com.resync.app.model.media.Image;
import com.resync.app.events.CommentsLoaded;
import com.resync.app.media.ImageErrors;
import com.resync.app.media.ImageUtils;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.net.thirdparty.CheckDeviantArtRequest;
import com.resync.app.net.thirdparty.CheckStreamableRequest;
import com.resync.app.net.thirdparty.XkcdRequest;
import com.resync.app.ui.views.video.NewVideoInterface;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.GoogleAnalyticsHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.WindowUtils;
import com.resync.app.net.thirdparty.RedgifRequestWrapper;
import com.resync.app.dialogs.ExtractTextAsyncAlertDialogBottomSheet;
import com.resync.app.dialogs.GfycatMissingDialogBottomSheet;
import com.resync.app.dialogs.ShareImageSelectionDialogBottomSheet;
import com.resync.app.ui.interfaces.AnimationCompleteListener;
import com.resync.app.util.Logger;
import com.resync.app.util.TestUtils;
import com.resync.app.util.links.LinkHelper;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: loaded from: classes2.dex */
public class ImageViewerFragment extends RedditFragment {
    boolean destroyed;
    public Integer videoPosition;
    public Boolean muted;
    public Boolean videoPaused;
    protected boolean hdEnabled;

    @BindView(R.id.image_bottomsheet)
    LinearLayout mBottomSheet;

    @BindView(R.id.image_bottomsheet_content)
    LinearLayout mBottomSheetContent;

    @BindView(R.id.image_options_description)
    HtmlTextView mBottomSheetDescription;

    @BindView(R.id.image_link)
    BaseTextView mBottomSheetLinkTextView;

    @BindView(R.id.image_bottomsheet_peek_area)
    RelativeLayout mBottomSheetPeekArea;

    @BindView(R.id.image_options_nested)
    NestedScrollView mBottomSheetScrollView;

    @BindView(R.id.image_options_text_button)
    View mBottomSheetTextButton;

    @BindView(R.id.image_options_title)
    TextView mBottomSheetTitle;

    @BindView(R.id.coordinator)
    CoordinatorLayout mCoordinator;

    @BindView(R.id.image_debug_log_message)
    BaseTextView mDebugLogMessage;

    @BindView(R.id.image_options_debug_wrapper)
    View mDebugWrapper;

    @BindView(R.id.image_options_description_wrapper)
    View mDescriptionWrapper;

    @BindView(R.id.image_error_open_button)
    Button mErrorButton;

    @BindView(R.id.image_error_message)
    TextView mErrorMessage;

    @BindView(R.id.image_error_message_wrapper)
    LinearLayout mErrorMessageWrapper;

    @BindView(R.id.image_gif_back)
    AppCompatImageView mGifBackButton;

    @BindView(R.id.image_gif_controls)
    AppCompatImageView mGifButton;

    @BindView(R.id.image_hd)
    AppCompatImageView mGifHdButton;

    @BindView(R.id.gif_speed_label)
    TextView mGifSpeedLabel;

    @BindView(R.id.gif_speed_wrapper)
    View mGifSpeedWrapper;

    @BindView(R.id.image_gif_time)
    TextView mGifTime;

    @BindView(R.id.image_gif_volume)
    AppCompatImageView mGifVolumeButton;

    @BindView(R.id.image_content)
    RelativeLayout mImageContent;

    @BindView(R.id.image_progress_bar)
    ImageProgressBar mImageProgressBar;

    @BindView(R.id.image_size)
    TextView mImageSize;

    @BindView(R.id.image_view)
    CustomPhotoViewVerticalDragLayout mImageView;

    @BindView(R.id.image_options_button)
    AppCompatImageView mOptionsButton;

    @BindView(R.id.image_gif_seek)
    SeekBar mSeekBar;

    @BindView(R.id.image_gif_seek_wrapper)
    FrameLayout mSeekBarWrapper;

    @BindView(R.id.subsampling_image_view)
    SubsamplingViewVerticalDragLayout mSubsamplingImageView;
    CustomExoPlayerViewVerticalDragLayout videoPlayerWrapper;

    /* JADX INFO: renamed from: s0, reason: collision with root package name */
    ImageViewerBottomSheetBehavior bottomSheetBehavior;
    private String subreddit;

    /* JADX INFO: renamed from: u0, reason: collision with root package name */
    private String url;

    /* JADX INFO: renamed from: v0, reason: collision with root package name */
    private String linkTitle;
    private String title;

    /* JADX INFO: renamed from: x0, reason: collision with root package name */
    private String commentsUrl;
    private int total;
    public int position;
    StringBuilder formatBuilder = new StringBuilder();
    Formatter formatter = new Formatter(this.formatBuilder, Locale.getDefault());
    boolean seeking = false;
    private Runnable updateRunnable = new PlaybackProgressRunnable();
    int animationDelay = 0;
    float speed = 1.0f;
    float[] speeds = {0.125f, 0.25f, 0.5f, 0.75f, 1.0f, 1.5f, 2.0f, 4.0f};
    String[] speedLabels = {"0.125x", "0.25x", "0.5x", "0.75x", "1x", "1.5x", "2x", "4x"};

    class GfycatErrorListener implements Response.ErrorListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String url;

        /* JADX INFO: renamed from: com.resync.app.ui.fragments.ImageViewerFragment$a$a, reason: collision with other inner class name */
        class DescriptionResponseListener implements Response.Listener<String> {
            DescriptionResponseListener() {
            }

            @Override // com.android.volley.Response.Listener
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void onResponse(String str) {
                if (ImageViewerFragment.this.getActivity() == null) {
                    return;
                }
                ImageViewerFragment.this.appendDebugLog("Gfycat API responded: " + str);
                ImageViewerFragment.this.setupStreaming(str);
            }
        }

        class GfycatScrapeErrorListener implements Response.ErrorListener {
            GfycatScrapeErrorListener() {
            }

            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                NetworkResponse networkResponse;
                if (volleyError != null && (networkResponse = volleyError.networkResponse) != null && networkResponse.statusCode == 404 && StringUtils.contains(GfycatErrorListener.this.url, "gfycat.com")) {
                    FragmentUtils.showWithUrl(GfycatMissingDialogBottomSheet.class, ContextHelper.getFragmentManager(ImageViewerFragment.this.getActivity()), GfycatErrorListener.this.url);
                }
                GfycatErrorListener self = GfycatErrorListener.this;
                ImageViewerFragment.this.showError(self.url, 22);
                ImageViewerFragment.this.appendDebugLog("Error loading Gfycat image");
                if (volleyError != null) {
                    ImageViewerFragment.this.appendDebugLog("Error returned: " + volleyError.getMessage());
                    if (volleyError.networkResponse != null) {
                        ImageViewerFragment.this.appendDebugLog("\tError code: " + volleyError.networkResponse.statusCode);
                        ImageViewerFragment.this.appendDebugLog("\tDuration: " + volleyError.networkResponse.networkTimeMs);
                        ImageViewerFragment.this.appendDebugLog("\tReturned: " + new String(volleyError.networkResponse.data));
                    }
                }
                ImageViewerFragment.this.notifyLoaded();
            }
        }

        GfycatErrorListener(String str) {
            this.url = str;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (ImageViewerFragment.this.getActivity() == null) {
                return;
            }
            ImageViewerFragment.this.appendDebugLog("Gfycat API failed, trying alt method...");
            OAuthRequestManager.enqueueAux(new ScrapeGfycatRequest(this.url, new DescriptionResponseListener(), new GfycatScrapeErrorListener()));
        }
    }

    class AnimationListener implements AnimationCompleteListener {

        final /* synthetic */ long startTime;
        final /* synthetic */ Runnable runnable;
        final /* synthetic */ int delay;

        AnimationListener(long j, Runnable runnable, int delay) {
            this.startTime = j;
            this.runnable = runnable;
            this.delay = delay;
        }

        @Override // com.resync.app.ui.interfaces.AnimationCompleteListener
        public void onAnimationComplete() {
            if (ImageViewerFragment.this.getView() == null || ImageViewerFragment.this.destroyed) {
                return;
            }
            Logger.verbose("Final time pre: " + (System.currentTimeMillis() - this.startTime));
            ImageViewerFragment.this.getView().postDelayed(this.runnable, (long) this.delay);
        }
    }

    class GfycatScrapeListener implements Response.Listener<Pair<String, String>> {
        GfycatScrapeListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(Pair<String, String> pair) {
            ImageViewerFragment.this.title = pair.first;
            ImageViewerFragment.this.bindBottomSheet();
            ImageViewerFragment.this.loadImage(pair.second);
        }
    }

    class ToggleChromeLongPressListener implements DragLongPressListener {
        ToggleChromeLongPressListener() {
        }

        @Override // com.resync.app.widgets.drag.DragLongPressListener
        public void onLongPress() {
            if (ImageViewerFragment.this.mCoordinator.getVisibility() == 8) {
                ImageViewerFragment.this.mCoordinator.setVisibility(0);
            } else {
                ImageViewerFragment.this.mCoordinator.setVisibility(8);
            }
        }
    }

    class XkcdErrorListener implements Response.ErrorListener {
        XkcdErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (ImageViewerFragment.this.getActivity() == null) {
                return;
            }
            ImageViewerFragment.this.appendDebugLog("Error loading XKCD image");
            ImageViewerFragment.this.notifyLoaded();
        }
    }

    class WindowInsetsListener implements View.OnApplyWindowInsetsListener {
        WindowInsetsListener() {
        }

        @Override // android.view.View.OnApplyWindowInsetsListener
        public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
            // NOTE: setExtraSpaceTop/Bottom were custom additions in the app's forked
            // SubsamplingScaleImageView; the stock Maven library (subsampling-scale-image-view-androidx
            // 3.10.0) does not expose them, so the system-inset extra-space tweak is a no-op here.
            return windowInsets;
        }
    }

    class DeviantArtListener implements Response.Listener<String> {
        DeviantArtListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String str) {
            if (ImageViewerFragment.this.getActivity() == null) {
                return;
            }
            ImageViewerFragment.this.loadImage(str);
        }
    }

    class BottomSheetSlideCallback extends BottomSheetBehavior.BottomSheetCallback {

        final /* synthetic */ ColorDrawable scrim;
        final /* synthetic */ View bottomView;

        BottomSheetSlideCallback(ColorDrawable colorDrawable, View view) {
            this.scrim = colorDrawable;
            this.bottomView = view;
        }

        @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
        public void onSlide(View view, float slideOffset) {
            this.scrim.setAlpha(Math.max(0, Math.min((int) (255.0f * slideOffset), 220)));
            ImageViewerFragment.this.mBottomSheetTextButton.setAlpha(1.0f - slideOffset);
            try {
                this.bottomView.findViewById(R.id.bottom).setBackgroundColor(ColorUtils.setAlphaComponent(((AbstractImageActivity) ImageViewerFragment.this.getActivity()).getDomColor(), this.scrim.getAlpha()));
            } catch (Exception e) {
                Logger.logException(e);
            }
        }

        @Override // com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
        public void onStateChanged(View view, int newState) {
            if (newState == 4) {
                ImageViewerFragment.this.mOptionsButton.setImageResource(R.drawable.ic_more_vert_white_24dp);
            } else {
                ImageViewerFragment.this.mOptionsButton.setImageResource(R.drawable.ic_close_white_24dp);
            }
        }
    }

    class DeviantArtErrorListener implements Response.ErrorListener {
        DeviantArtErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (ImageViewerFragment.this.getActivity() == null) {
                return;
            }
            ImageViewerFragment.this.appendDebugLog("Error loading DeviantArt image");
            ImageViewerFragment.this.notifyLoaded();
        }
    }

    class BackKeyListener implements View.OnKeyListener {
        BackKeyListener() {
        }

        @Override // android.view.View.OnKeyListener
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if (keyCode != 4 || keyEvent.getAction() != 1 || ImageViewerFragment.this.bottomSheetBehavior.getState() != 3) {
                return false;
            }
            ImageViewerFragment.this.bottomSheetBehavior.setState(4);
            return true;
        }
    }

    class StreamableListener implements Response.Listener<String> {
        StreamableListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String str) {
            ImageViewerFragment.this.appendDebugLog("Setting Streamable video path: " + str);
            ImageViewerFragment.this.setupStreaming(str);
        }
    }

    class RedgifListener implements Response.Listener<String> {
        RedgifListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String str) {
            if (ImageViewerFragment.this.getActivity() == null) {
                return;
            }
            ImageViewerFragment.this.appendDebugLog("Redgif API responded: " + str);
            ImageViewerFragment.this.setupStreaming(str);
        }
    }

    class StreamableErrorListener implements Response.ErrorListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String url;

        StreamableErrorListener(String str) {
            this.url = str;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            ImageViewerFragment.this.showError(this.url, 12);
        }
    }

    class RedgifErrorListener implements Response.ErrorListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String url;

        RedgifErrorListener(String str) {
            this.url = str;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (ImageViewerFragment.this.getActivity() == null) {
                return;
            }
            ImageViewerFragment.this.showError(this.url, 40);
            ImageViewerFragment.this.appendDebugLog(ImageErrors.getMessage(40));
            ImageViewerFragment.this.notifyLoaded();
        }
    }

    class PlaybackProgressRunnable implements Runnable {
        PlaybackProgressRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (ImageViewerFragment.this.isRemoving()) {
                return;
            }
            ImageViewerFragment.this.updateSeekbar();
            CustomPhotoViewVerticalDragLayout customPhotoViewVerticalDragLayout = ImageViewerFragment.this.mImageView;
            if (customPhotoViewVerticalDragLayout != null && customPhotoViewVerticalDragLayout.getVisibility() == 0 && ImageViewerFragment.this.mImageView.getView().isGif()) {
                ImageViewerFragment imageViewerFragment = ImageViewerFragment.this;
                imageViewerFragment.mImageSize.postDelayed(imageViewerFragment.updateRunnable, 30L);
            } else if (ImageViewerFragment.this.hasVideoPlayer() && ((NewVideoInterface) ImageViewerFragment.this.getVideoView()).isVideoPlaying()) {
                ImageViewerFragment.this.getVideoView().postDelayed(ImageViewerFragment.this.updateRunnable, 30L);
            }
        }
    }

    class GfycatGrabListener implements Response.Listener<String> {
        GfycatGrabListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String str) {
            if (ImageViewerFragment.this.getActivity() == null) {
                return;
            }
            ImageViewerFragment.this.appendDebugLog("Gfycat API responded: " + str);
            ImageViewerFragment.this.setupStreaming(str);
        }
    }

    class GifLoadRunnable implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String url;
        final /* synthetic */ int videoType;

        class GifResourceListener implements RequestListener<GifDrawable> {
            GifResourceListener() {
            }

            @Override // com.bumptech.glide.request.RequestListener
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public boolean onResourceReady(GifDrawable gifDrawable, Object obj, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                if (!ContextHelper.isActivityFinishing(ImageViewerFragment.this.getActivity()) && ImageViewerFragment.this.isAttached()) {
                    ImageViewerFragment.this.mImageProgressBar.hide();
                    ImageViewerFragment.this.mSubsamplingImageView.setVisibility(8);
                    if (ImageViewerFragment.this.hasVideoPlayer()) {
                        ImageViewerFragment.this.getVideoWrapper().setVisibility(8);
                    }
                    ImageViewerFragment.this.showVideoControls(false);
                    ImageViewerFragment.this.notifyLoaded();
                }
                return false;
            }

            @Override // com.bumptech.glide.request.RequestListener
            public boolean onLoadFailed(GlideException glideException, Object obj, Target<GifDrawable> target, boolean isFirstResource) {
                if (!ContextHelper.isActivityFinishing(ImageViewerFragment.this.getActivity()) && ImageViewerFragment.this.isAttached()) {
                    GifLoadRunnable self = GifLoadRunnable.this;
                    ImageViewerFragment.this.onDeepzoomError(self.url);
                }
                return false;
            }
        }

        class GifProgressListener implements ImageEventListener {
            GifProgressListener() {
            }

            @Override // com.resync.app.widgets.ImageEventListener
            public void onCancel() {
                ImageViewerFragment.this.appendDebugLog("Request cancelled");
                com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).clear(ImageViewerFragment.this.mImageView.getView());
            }

            @Override // com.resync.app.widgets.ImageEventListener
            public void onRetry() {
                ImageViewerFragment.this.clearDebugLog();
                ImageViewerFragment.this.appendDebugLog("Retrying request");
                GifLoadRunnable self = GifLoadRunnable.this;
                ImageViewerFragment.this.loadVideo(self.url, self.videoType);
            }
        }

        GifLoadRunnable(String str, int videoType) {
            this.url = str;
            this.videoType = videoType;
        }

        @Override // java.lang.Runnable
        public void run() {
            ImageViewerFragment imageViewerFragment = ImageViewerFragment.this;
            if (imageViewerFragment.destroyed || imageViewerFragment.getActivity() == null) {
                return;
            }
            ImageViewerFragment.this.recreateVideoPlayer();
            ImageViewerFragment.this.mImageView.setVisibility(0);
            ImageViewerFragment.this.mImageProgressBar.show();
            com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).asGif().load(this.url).priority(Priority.IMMEDIATE).addListener(new GifResourceListener()).into(ImageViewerFragment.this.mImageView.getView());
            ImageViewerFragment.this.mImageProgressBar.setListener(new GifProgressListener());
        }
    }

    class SeekBarListener implements SeekBar.OnSeekBarChangeListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        boolean wasPlaying = false;

        SeekBarListener() {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                ((NewVideoInterface) ImageViewerFragment.this.getVideoView()).seekTo(progress);
                ImageViewerFragment.this.updateSeekbar();
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
            ImageViewerFragment imageViewerFragment = ImageViewerFragment.this;
            imageViewerFragment.seeking = true;
            boolean playing = !imageViewerFragment.mGifButton.isSelected();
            this.wasPlaying = playing;
            if (playing) {
                ((NewVideoInterface) ImageViewerFragment.this.getVideoView()).pause();
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
            ImageViewerFragment imageViewerFragment = ImageViewerFragment.this;
            imageViewerFragment.seeking = false;
            if (this.wasPlaying) {
                ((NewVideoInterface) imageViewerFragment.getVideoView()).resume();
            }
        }
    }

    class DismissClickListener implements DragClickListener {
        DismissClickListener() {
        }

        @Override // com.resync.app.widgets.drag.DragClickListener
        public void onClick() {
            ImageViewerFragment.this.closeActivity();
        }
    }

    class VideoErrorListener implements OnErrorListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String url;

        VideoErrorListener(String str) {
            this.url = str;
        }

        @Override // com.github.chrisbanes.photoview.animation.OnErrorListener
        public void onException(Exception exc) {
            ImageViewerFragment.this.appendDebugLog("\nException thrown");
            ImageViewerFragment.this.appendDebugLog("\t" + exc.getClass().getSimpleName());
            ImageViewerFragment.this.appendDebugLog("\t" + exc.getMessage());
        }

        @Override // com.github.chrisbanes.photoview.animation.OnErrorListener
        public void onMessage(String str) {
            ImageViewerFragment.this.appendDebugLog("Error: " + str);
        }

        @Override // com.github.chrisbanes.photoview.animation.OnErrorListener
        public void onErrorCode(int errorCode) {
            ImageViewerFragment.this.showError(this.url, errorCode);
            ImageViewerFragment.this.notifyLoaded();
        }
    }

    class VideoUpdateListener implements OnUpdateListener {
        VideoUpdateListener() {
        }

        @Override // com.github.chrisbanes.photoview.animation.OnUpdateListener
        public void onUpdate(String str) {
            ImageViewerFragment.this.appendDebugLog(str);
        }
    }

    class VideoStartListener implements OnStartListener {

        class VideoStartRunnable implements Runnable {
            VideoStartRunnable() {
            }

            @Override // java.lang.Runnable
            public void run() {
                if (ImageViewerFragment.this.hasVideoPlayer()) {
                    ImageViewerFragment imageViewerFragment = ImageViewerFragment.this;
                    if (imageViewerFragment.muted == null) {
                        if (imageViewerFragment.shouldMute()) {
                            ImageViewerFragment.this.mGifVolumeButton.setImageResource(R.drawable.ic_volume_mute_white_24dp);
                            ImageViewerFragment.this.mGifVolumeButton.setSelected(true);
                        } else {
                            ImageViewerFragment.this.mGifVolumeButton.setImageResource(R.drawable.ic_volume_up_white_24dp);
                            ImageViewerFragment.this.mGifVolumeButton.setSelected(false);
                        }
                    }
                    if (((NewVideoInterface) ImageViewerFragment.this.getVideoView()).shouldDisplayHdLogo()) {
                        ImageViewerFragment.this.mGifHdButton.setVisibility(0);
                        ImageViewerFragment.this.mGifHdButton.setAlpha(0.0f);
                        ImageViewerFragment.this.mGifHdButton.animate().alpha(1.0f);
                    }
                    ImageViewerFragment.this.showVideoControls(((CustomExoPlayerView) ImageViewerFragment.this.getVideoView()).hasAudio());
                    ImageViewerFragment.this.updateSeekbar();
                    ImageViewerFragment.this.getVideoView().post(ImageViewerFragment.this.updateRunnable);
                    ImageViewerFragment.this.appendDebugLog("MP4 loaded. Starting MP4 playback");
                    ImageViewerFragment.this.notifyLoaded();
                    ImageViewerFragment.this.getVideoWrapper().setAlpha(1.0f);
                    ImageViewerFragment.this.getVideoWrapper().invalidate();
                    Logger.verbose("POSITION: " + ImageViewerFragment.this.videoPosition);
                    ImageViewerFragment imageViewerFragment2 = ImageViewerFragment.this;
                    if (imageViewerFragment2.videoPosition != null) {
                        ((NewVideoInterface) imageViewerFragment2.getVideoView()).seekTo(ImageViewerFragment.this.videoPosition.intValue());
                        ImageViewerFragment.this.videoPosition = null;
                    }
                    ((NewVideoInterface) ImageViewerFragment.this.getVideoView()).start();
                    Boolean bool = ImageViewerFragment.this.muted;
                    if (bool != null && bool.booleanValue()) {
                        ((NewVideoInterface) ImageViewerFragment.this.getVideoView()).mute();
                        ImageViewerFragment imageViewerFragment3 = ImageViewerFragment.this;
                        imageViewerFragment3.muted = null;
                        imageViewerFragment3.mGifVolumeButton.setImageResource(R.drawable.ic_volume_mute_white_24dp);
                        ImageViewerFragment.this.mGifVolumeButton.setSelected(true);
                    }
                    Boolean bool2 = ImageViewerFragment.this.videoPaused;
                    if (bool2 == null || !bool2.booleanValue()) {
                        return;
                    }
                    Logger.verbose("PAUSED: SHOULD BE PAUSED");
                    ((NewVideoInterface) ImageViewerFragment.this.getVideoView()).pause();
                    ImageViewerFragment imageViewerFragment4 = ImageViewerFragment.this;
                    imageViewerFragment4.videoPaused = null;
                    imageViewerFragment4.mGifButton.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                    ImageViewerFragment.this.mGifButton.setSelected(true);
                }
            }
        }

        VideoStartListener() {
        }

        @Override // com.github.chrisbanes.photoview.animation.OnStartListener
        public void onStart() {
            if (ImageViewerFragment.this.getActivity() == null) {
                return;
            }
            ImageViewerFragment.this.appendDebugLog("MP4 start listener triggered");
            ImageViewerFragment.this.mImageProgressBar.hide();
            VideoStartRunnable startRunnable = new VideoStartRunnable();
            ImageViewerFragment imageViewerFragment = ImageViewerFragment.this;
            imageViewerFragment.postWhenReady(startRunnable, imageViewerFragment.animationDelay);
        }
    }

    class VideoDismissClickListener implements DragClickListener {
        VideoDismissClickListener() {
        }

        @Override // com.resync.app.widgets.drag.DragClickListener
        public void onClick() {
            ImageViewerFragment.this.closeActivity();
        }
    }

    class ToggleChromeLongPressListener2 implements DragLongPressListener {
        ToggleChromeLongPressListener2() {
        }

        @Override // com.resync.app.widgets.drag.DragLongPressListener
        public void onLongPress() {
            if (ImageViewerFragment.this.mCoordinator.getVisibility() == 8) {
                ImageViewerFragment.this.mCoordinator.setVisibility(0);
            } else {
                ImageViewerFragment.this.mCoordinator.setVisibility(8);
            }
        }
    }

    class ImageFileListener implements RequestListener<File> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String url;

        ImageFileListener(String str) {
            this.url = str;
        }

        @Override // com.bumptech.glide.request.RequestListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public boolean onResourceReady(File file, Object obj, Target<File> target, DataSource dataSource, boolean isFirstResource) {
            if (!ContextHelper.isActivityFinishing(ImageViewerFragment.this.getActivity()) && ImageViewerFragment.this.isAttached()) {
                ImageViewerFragment.this.processImageFile(this.url, file);
            }
            return false;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException glideException, Object obj, Target<File> target, boolean isFirstResource) {
            if (!ContextHelper.isActivityFinishing(ImageViewerFragment.this.getActivity()) && ImageViewerFragment.this.isAttached()) {
                ImageViewerFragment.this.onDeepzoomError(this.url);
            }
            return false;
        }
    }

    class ImageFileEventListener implements ImageEventListener {

        final /* synthetic */ Target target;
        final /* synthetic */ String url;

        ImageFileEventListener(Target target, String str) {
            this.target = target;
            this.url = str;
        }

        @Override // com.resync.app.widgets.ImageEventListener
        public void onCancel() {
            ImageViewerFragment.this.appendDebugLog("Request cancelled");
            com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).clear(this.target);
        }

        @Override // com.resync.app.widgets.ImageEventListener
        public void onRetry() {
            ImageViewerFragment.this.clearDebugLog();
            ImageViewerFragment.this.appendDebugLog("Retrying request");
            ImageViewerFragment.this.loadImage(this.url);
        }
    }

    class ImageDecodeThread extends Thread {
        final /* synthetic */ File file2;
        final /* synthetic */ String sourceUrl;

        ImageDecodeThread(File file, String str) {
            this.file2 = file;
            this.sourceUrl = str;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void loadDeepzoomFile(File file) {
            ImageViewerFragment.this.loadDeepzoom(file);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void setupMp4() {
            ImageViewerFragment imageViewerFragment = ImageViewerFragment.this;
            imageViewerFragment.setupStreaming(imageViewerFragment.url);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(this.file2.getAbsolutePath(), options);
                int height = options.outHeight;
                ImageViewerFragment imageViewerFragment = ImageViewerFragment.this;
                imageViewerFragment.setImageInfo("Image resolution: " + options.outWidth + "x" + height + "\nFile size: " + String.format("%.2f", Double.valueOf(this.file2.length() / 1048576.0f)) + "MB");
                String str = options.outMimeType;
                if (str == null) {
                    ImageViewerFragment imageViewerFragment2 = ImageViewerFragment.this;
                    imageViewerFragment2.onImageError(imageViewerFragment2.url);
                    return;
                }
                Logger.verbose("MIME: " + str);
                if (!StringUtils.startsWith(str, "image/gif")) {
                    SubsamplingViewVerticalDragLayout subsamplingViewVerticalDragLayout = ImageViewerFragment.this.mSubsamplingImageView;
                    final File file = this.file2;
                    subsamplingViewVerticalDragLayout.post(new Runnable() { // from class: com.resync.app.ui.fragments.b
                        @Override // java.lang.Runnable
                        public final void run() {
                            ImageDecodeThread.this.loadDeepzoomFile(file);
                        }
                    });
                } else {
                    if (!str.equalsIgnoreCase("image/gif") || !ImageViewerFragment.this.url.contains("imgur.com") || !ImageViewerFragment.this.url.contains(".jpg")) {
                        ImageViewerFragment.this.loadVideo(this.sourceUrl, 0);
                        return;
                    }
                    ImageViewerFragment imageViewerFragment3 = ImageViewerFragment.this;
                    imageViewerFragment3.url = imageViewerFragment3.url.replace(".jpg", ".mp4");
                    ImageViewerFragment.this.mSubsamplingImageView.post(new Runnable() { // from class: com.resync.app.ui.fragments.a
                        @Override // java.lang.Runnable
                        public final void run() {
                            ImageDecodeThread.this.setupMp4();
                        }
                    });
                }
            } catch (Exception e) {
                Logger.logException(e);
            }
        }
    }

    class DeepzoomEventListener implements SubsamplingScaleImageView.OnImageEventListener {

        class DeepzoomReadyRunnable implements Runnable {
            DeepzoomReadyRunnable() {
            }

            @Override // java.lang.Runnable
            public void run() {
                SubsamplingViewVerticalDragLayout subsamplingViewVerticalDragLayout;
                ImageViewerFragment imageViewerFragment = ImageViewerFragment.this;
                if (imageViewerFragment.destroyed || imageViewerFragment.getActivity() == null || (subsamplingViewVerticalDragLayout = ImageViewerFragment.this.mSubsamplingImageView) == null) {
                    return;
                }
                subsamplingViewVerticalDragLayout.setAlpha(1.0f);
                ImageViewerFragment.this.mSubsamplingImageView.invalidate();
                ImageViewerFragment.this.notifyLoaded();
            }
        }

        DeepzoomEventListener() {
        }

        @Override // com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.OnImageEventListener
        public void onImageLoadError(Exception exc) {
            Logger.debug("Error here");
            Logger.logException(exc);
            ImageViewerFragment imageViewerFragment = ImageViewerFragment.this;
            imageViewerFragment.showError(imageViewerFragment.url, 17);
        }

        @Override // com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.OnImageEventListener
        public void onImageLoaded() {
        }

        @Override // com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.OnImageEventListener
        public void onPreviewLoadError(Exception exc) {
            Logger.logException(exc);
        }

        @Override // com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.OnImageEventListener
        public void onPreviewReleased() {
        }

        @Override // com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.OnImageEventListener
        public void onReady() {
            ImageViewerFragment.this.appendDebugLog("Deepzoom image loaded");
            ImageViewerFragment.this.mImageProgressBar.hide();
            SubsamplingViewVerticalDragLayout subsamplingViewVerticalDragLayout = ImageViewerFragment.this.mSubsamplingImageView;
            if (subsamplingViewVerticalDragLayout != null) {
                subsamplingViewVerticalDragLayout.getView().setDoubleTapZoomDuration(220);
                Logger.verbose("ImageViewerFragment", "Deepzoom scale: " + SettingsSingleton.getSettings().deepzoomMax);
                ImageViewerFragment.this.mSubsamplingImageView.getView().setMaxScale(ImageViewerFragment.this.mSubsamplingImageView.getView().getScale() * ((float) SettingsSingleton.getSettings().deepzoomMax));
                ImageViewerFragment.this.mSubsamplingImageView.getView().setDoubleTapZoomScale(ImageViewerFragment.this.mSubsamplingImageView.getView().getScale() * 2.0f);
                ImageViewerFragment.this.mSubsamplingImageView.getView().resetScaleAndCenter();
            }
            ImageViewerFragment.this.postWhenReady(new DeepzoomReadyRunnable(), 0);
            if (SettingsSingleton.getSettings().zoomTall && ImageUtils.isTallImage(ImageViewerFragment.this.mSubsamplingImageView.getView().getSWidth(), ImageViewerFragment.this.mSubsamplingImageView.getView().getSHeight())) {
                ImageViewerFragment.this.mSubsamplingImageView.setReady(false);
                ImageViewerFragment.this.mSubsamplingImageView.getView().animateScaleAndCenter(WindowUtils.getDisplayWidth() / ImageViewerFragment.this.mSubsamplingImageView.getView().getSWidth(), new PointF(0.0f, 0.0f)).start();
            }
        }

        @Override // com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.OnImageEventListener
        public void onTileLoadError(Exception exc) {
            Logger.logException(exc);
            ImageViewerFragment imageViewerFragment = ImageViewerFragment.this;
            imageViewerFragment.showError(imageViewerFragment.url, 38);
        }
    }

    class ErrorWrapperClickListener implements View.OnClickListener {
        ErrorWrapperClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ImageViewerFragment.this.closeActivity();
        }
    }

    class ProgressClickListener implements View.OnClickListener {
        ProgressClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ImageViewerFragment.this.closeActivity();
        }
    }

    class ErrorButtonClickListener implements View.OnClickListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String url;

        ErrorButtonClickListener(String str) {
            this.url = str;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            com.resync.app.util.links.LinkHandler.openExternalOrInternal(ImageViewerFragment.this.getActivity(), this.url);
        }
    }

    class LinkClickListener implements View.OnClickListener {
        LinkClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            RedditHelper.copyUrlToClipboard(ImageViewerFragment.this.getActivity(), ImageViewerFragment.this.url);
        }
    }

    class VideoQualityNoListener implements DialogInterface.OnClickListener {
        VideoQualityNoListener() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            SettingsSingleton.getInstance().putBoolean("reddit_video_quality_check", true);
            SettingsSingleton.getSettings().redditVideoQualityCheck = true;
        }
    }

    class VideoQualityYesListener implements DialogInterface.OnClickListener {
        VideoQualityYesListener() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            SettingsSingleton.getInstance().putString("reddit_video_quality", "0");
            SettingsSingleton.getSettings().redditVideoQuality = 0;
            SettingsSingleton.getInstance().putBoolean("reddit_video_quality_check", true);
            SettingsSingleton.getSettings().redditVideoQualityCheck = true;
        }
    }

    private void setLinkText(String str) {
        this.url = str;
        String host = LinkTypeHelper.getHost(str);
        if ("i.imgur.com".equalsIgnoreCase(host)) {
            host = "Imgur.com";
        }
        this.mBottomSheetLinkTextView.setText(host + "\n" + this.url);
        this.mBottomSheetLinkTextView.setOnClickListener(new LinkClickListener());
    }

    private void setupVideoPlayer() {
        appendDebugLog("Setting up normal player");
        CustomExoPlayerViewVerticalDragLayout customExoPlayerViewVerticalDragLayout = this.videoPlayerWrapper;
        if (customExoPlayerViewVerticalDragLayout != null) {
            customExoPlayerViewVerticalDragLayout.teardown();
            this.mImageContent.removeView(this.videoPlayerWrapper);
            this.videoPlayerWrapper = null;
        }
        CustomExoPlayerViewVerticalDragLayout customExoPlayerViewVerticalDragLayout2 = new CustomExoPlayerViewVerticalDragLayout(getActivity());
        this.videoPlayerWrapper = customExoPlayerViewVerticalDragLayout2;
        customExoPlayerViewVerticalDragLayout2.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        this.mImageContent.addView(this.videoPlayerWrapper, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recreateVideoPlayer() {
        setupVideoPlayer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setImageInfo(final String str) {
        if (isAttached()) {
            this.mBottomSheetLinkTextView.post(new Runnable() { // from class: ma.l
                @Override // java.lang.Runnable
                public final void run() {
                    ImageViewerFragment.this.appendLinkDebug(str);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupStreaming(String str) {
        if (isDestroyedOrDetached()) {
            return;
        }
        setupVideoPlayer();
        hideImageSize();
        appendDebugLog("Setting up streaming!");
        appendDebugLog("Setting up MP4 playback");
        this.mImageProgressBar.show();
        this.mImageProgressBar.setState(0);
        this.mSeekBar.setOnSeekBarChangeListener(new SeekBarListener());
        ((NewVideoInterface) getVideoView()).setOnErrorListener(new VideoErrorListener(str));
        ((NewVideoInterface) getVideoView()).setOnUpdateListener(new VideoUpdateListener());
        ((NewVideoInterface) getVideoView()).setOnStartListener(new VideoStartListener());
        this.mImageView.setVisibility(8);
        this.mSubsamplingImageView.setVisibility(8);
        getVideoWrapper().setVisibility(0);
        getVideoWrapper().setAlpha(0.0f);
        getVideoWrapper().invalidate();
        if (SettingsSingleton.getSettings().quickDismiss) {
            getVideoWrapper().setOnDragClickListener(new VideoDismissClickListener());
        }
        getVideoWrapper().setOnDragLongPressListener(new ToggleChromeLongPressListener2());
        Logger.verbose("STOPPING MUTED - STARTING PLAYBACK SHOULD MUTE: " + shouldMute());
        ((NewVideoInterface) getVideoView()).setSource(str, this.hdEnabled, shouldMute());
        getVideoWrapper().setClickable(true);
        getVideoWrapper().setFocusable(true);
        getVideoWrapper().requestFocus();
    }

    private String formatTime(int millis) {
        int totalSeconds = millis / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        this.formatBuilder.setLength(0);
        return hours > 0 ? this.formatter.format("%d:%02d:%02d", Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)).toString() : this.formatter.format("%02d:%02d", Integer.valueOf(minutes), Integer.valueOf(seconds)).toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSeekbar() {
        int duration;
        CustomPhotoViewVerticalDragLayout customPhotoViewVerticalDragLayout = this.mImageView;
        int progress = 0;
        if (customPhotoViewVerticalDragLayout != null && customPhotoViewVerticalDragLayout.getVisibility() == 0 && this.mImageView.getView().isGif()) {
            progress = this.mImageView.getView().getCurrentPosition();
            duration = this.mImageView.getView().getDuration();
        } else if (hasVideoPlayer()) {
            progress = ((NewVideoInterface) getVideoView()).getProgress();
            duration = ((NewVideoInterface) getVideoView()).getDuration();
        } else {
            duration = 0;
        }
        this.mGifTime.setText(formatTime(progress) + " / " + formatTime(duration));
        if (this.seeking) {
            return;
        }
        this.mSeekBar.setMax(duration);
        if (Build.VERSION.SDK_INT >= 24) {
            this.mSeekBar.setProgress(progress, true);
        } else {
            this.mSeekBar.setProgress(progress);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearDebugLog() {
        this.mDebugLogMessage.setText("");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public View getVideoView() {
        CustomExoPlayerViewVerticalDragLayout customExoPlayerViewVerticalDragLayout = this.videoPlayerWrapper;
        if (customExoPlayerViewVerticalDragLayout != null) {
            return customExoPlayerViewVerticalDragLayout.getView();
        }
        throw new RuntimeException("There we no video players found");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AbstractVerticalViewDragLayout getVideoWrapper() {
        CustomExoPlayerViewVerticalDragLayout customExoPlayerViewVerticalDragLayout = this.videoPlayerWrapper;
        if (customExoPlayerViewVerticalDragLayout != null) {
            return customExoPlayerViewVerticalDragLayout;
        }
        throw new RuntimeException("There we no video player wrappers found");
    }

    private void detectAndLoad(String str) {
        this.mErrorMessage.setText("");
        if (str.contains("mediadownloads.mlb.com") && str.endsWith(".mp4")) {
            setupStreaming(str);
            return;
        }
        if (str.contains("v.redd.it")) {
            String str2 = "https://v.redd.it/" + com.resync.app.util.links.LinkHandler.getRedditVideoId(str) + "/HLSPlaylist.m3u8";
            appendDebugLog("Detected reddit streaming video");
            setupStreaming(str2);
            return;
        }
        if (LinkTypeHelper.isTwitterVideo(str)) {
            appendDebugLog("Detected Twitter video");
            loadVideo(str, 1);
            return;
        }
        Locale locale = Locale.ENGLISH;
        if (str.toLowerCase(locale).contains("streamable.com/")) {
            appendDebugLog("Detected Streamable video");
            loadStreamable(str);
            return;
        }
        if (str.toLowerCase(locale).contains("gfycat.com")) {
            appendDebugLog("Detected Gfycat image");
            loadGfycat(str, false);
            return;
        }
        if (str.toLowerCase(locale).contains("redgifs.com")) {
            appendDebugLog("Detected RedGifs image");
            loadGfycat(str, false);
            return;
        }
        if (LinkTypeHelper.isImgflip(str)) {
            appendDebugLog("Detected XKCD image");
            loadXkcd(str);
            return;
        }
        if (LinkTypeHelper.isDeviantArt(str)) {
            appendDebugLog("Detected DeviantArt image");
            loadDeviantArt(str);
            return;
        }
        if (str.contains("giphy") && str.contains(".mp4")) {
            appendDebugLog("Detected Giphy MP4: " + str);
            loadVideo(str, 1);
            return;
        }
        if (str.contains("mixtape.moe") && (str.contains(".mp4") || str.contains(".webm"))) {
            appendDebugLog("Detected mixtape.moe MP4: " + str);
            loadVideo(str, 1);
            return;
        }
        if (str.contains("sli.mg") && str.contains(".mp4")) {
            appendDebugLog("Detected SLi.MG MP4: " + str);
            loadVideo(str, 1);
            return;
        }
        if (str.contains("redditmedia.com") && str.contains("fm=mp4")) {
            loadVideo(str, 1);
            return;
        }
        if (str.contains("preview.redd.it") && str.contains(".mp4")) {
            loadVideo(str, 1);
            return;
        }
        if (str.toLowerCase(locale).contains(".gif")) {
            loadGif();
        } else if (str.toLowerCase(locale).contains(".mp4")) {
            loadGif();
        } else {
            appendDebugLog("Detected standard image");
            loadImage(str);
        }
    }

    private void hideImageSize() {
        this.mImageSize.setVisibility(8);
    }

    private boolean isDestroyedOrDetached() {
        return isRemoving() || isDetached() || !isAdded() || getActivity() == null || getActivity().isFinishing();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasVideoPlayer() {
        return this.videoPlayerWrapper != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void appendLinkDebug(String str) {
        appendDebugLog(str);
        this.mBottomSheetLinkTextView.append("\n" + str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void notifyLoadedGuarded() {
        if (this.destroyed || getActivity() == null || this.mSubsamplingImageView == null) {
            return;
        }
        notifyLoaded();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onTileError(String str) {
        showError(str, 37);
        postWhenReady(new Runnable() { // from class: ma.i
            @Override // java.lang.Runnable
            public final void run() {
                ImageViewerFragment.this.notifyLoadedGuarded();
            }
        }, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void notifyLoadedGuarded2() {
        if (this.destroyed || getActivity() == null || this.mSubsamplingImageView == null) {
            return;
        }
        notifyLoaded();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onGifLoadError(String str) {
        showError(str, 15);
        postWhenReady(new Runnable() { // from class: ma.h
            @Override // java.lang.Runnable
            public final void run() {
                ImageViewerFragment.this.notifyLoadedGuarded2();
            }
        }, 0);
    }

    private void loadDeviantArt(String str) {
        RedditApplication.thirdPartyRequestQueue.add(new CheckDeviantArtRequest(str, new DeviantArtListener(), new DeviantArtErrorListener()));
    }

    private void loadGfycat(String str, boolean highQuality) {
        this.mImageProgressBar.show();
        appendDebugLog("Connecting to Gfycat API");
        if (str.contains("redgifs.com")) {
            OAuthRequestManager.sendRedgif(new RedgifRequestWrapper(str, false, new RedgifListener(), new RedgifErrorListener(str)));
        } else {
            OAuthRequestManager.enqueueAux(new GrabGfycatRequest(str, false, new GfycatGrabListener(), new GfycatErrorListener(str)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadImage(String str) {
        appendDebugLog("Downloading regular image");
        appendDebugLog("Submitting to the ImageManager: " + str);
        try {
            this.mImageProgressBar.show();
            this.mImageProgressBar.setState(1);
            this.mImageProgressBar.setListener(new ImageFileEventListener(com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).asFile().load(str).priority(Priority.IMMEDIATE).addListener(new ImageFileListener(str)).submit(), str));
        } catch (Exception e) {
            StaticToast.show("Error loading image: " + e.getClass().getSimpleName());
            Logger.logException(e);
        }
    }

    private void loadXkcd(String str) {
        RedditApplication.thirdPartyRequestQueue.add(new XkcdRequest(str, new GfycatScrapeListener(), new XkcdErrorListener()));
    }

    public static ImageViewerFragment newInstance(int total, int position, String str, String str2, String str3, String str4, String str5, int startVideoPosition, boolean startVideoPaused) {
        ImageViewerFragment imageViewerFragment = new ImageViewerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("total", total);
        bundle.putInt("pos", position);
        bundle.putString("subreddit", str);
        bundle.putString("url", str2);
        bundle.putString("link_title", str3);
        bundle.putString("title", str4);
        bundle.putString("comments_url", str5);
        bundle.putInt("start_video_position", startVideoPosition);
        bundle.putBoolean("start_video_paused", startVideoPaused);
        imageViewerFragment.setArguments(bundle);
        return imageViewerFragment;
    }

    private void loadGif() {
        appendDebugLog("Detected GIF: " + this.url);
        if (ImgurImageHelper.isImgur(this.url)) {
            setLinkText(new Image(this.url, "gifv").getUrl());
        }
        appendDebugLog("Formatted GIF: " + this.url);
        loadFormattedGif(this.url);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyLoaded() {
        if (getActivity() == null || !(getActivity() instanceof SingleImageActivity)) {
            return;
        }
        ((SingleImageActivity) getActivity()).notifyLoaded();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showError(String str, int errorCode) {
        GoogleAnalyticsHelper.trackEvent("Errors", "ImageViewer", errorCode + "#" + str);
        this.mImageProgressBar.hide();
        appendDebugLog(ImageErrors.getMessage(errorCode));
        this.mErrorMessage.setVisibility(0);
        this.mErrorMessage.setText(ImageErrors.getMessage(errorCode));
        this.mErrorMessageWrapper.setVisibility(0);
        this.mErrorMessageWrapper.setOnClickListener(new ErrorWrapperClickListener());
        if (errorCode == 15 || errorCode == 36) {
            this.mErrorButton.setVisibility(0);
            this.mErrorButton.setOnClickListener(new ErrorButtonClickListener(str));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeActivity() {
        if (TestUtils.isRobolectric() || !(getActivity() instanceof AbstractImageActivity)) {
            return;
        }
        ((AbstractImageActivity) getActivity()).close();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadVideo(String str, int videoType) {
        if (videoType == 1) {
            setupStreaming(str);
            return;
        }
        if (videoType == 0) {
            appendDebugLog("Downloading GIF");
            appendDebugLog("Submitting to the Glide: " + str);
            postWhenReady(new GifLoadRunnable(str, videoType), 0);
        }
    }

    private void loadStreamable(String str) {
        appendDebugLog("Grabbing Streamable file location");
        RedditApplication.thirdPartyRequestQueue.add(new CheckStreamableRequest(str, new StreamableListener(), new StreamableErrorListener(str)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postWhenReady(Runnable runnable, int delay) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (!(getActivity() instanceof SingleImageActivity)) {
            if (getView() == null || this.destroyed) {
                return;
            }
            Logger.verbose("Final time after: " + (System.currentTimeMillis() - jCurrentTimeMillis));
            getView().postDelayed(runnable, (long) delay);
            return;
        }
        if (!((SingleImageActivity) getActivity()).canShowAd()) {
            appendDebugLog("Adding animation runnable");
            ((SingleImageActivity) getActivity()).setAnimationCompleteListener(new AnimationListener(jCurrentTimeMillis, runnable, delay));
            return;
        }
        appendDebugLog("Skipping animation runnable");
        if (getView() == null || this.destroyed) {
            return;
        }
        Logger.verbose("Final time: " + (System.currentTimeMillis() - jCurrentTimeMillis));
        getView().post(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void appendDebugLog(String str) {
        if (this.mDebugLogMessage.getText().length() > 0) {
            this.mDebugLogMessage.append("\n");
        }
        this.mDebugLogMessage.append(str);
        Logger.verbose(str);
    }

    public void bindBottomSheet() {
        StringBuilder sb = new StringBuilder();
        if (this.total > 1) {
            sb.append((this.position + 1) + " / " + this.total);
        }
        this.mBottomSheetTitle.setText(sb);
        if (TextUtils.isEmpty(sb.toString())) {
            this.mBottomSheetTitle.setVisibility(8);
        }
        if (TextUtils.isEmpty(this.title)) {
            this.mBottomSheetTextButton.setVisibility(8);
            return;
        }
        this.mDescriptionWrapper.setVisibility(0);
        this.mBottomSheetTextButton.setVisibility(0);
        this.mBottomSheetDescription.setHtmlWithOptions(Options.compact(), RedditHelper.processMarkdownToHtml(null, this.title));
    }

    boolean shouldMute() {
        Boolean bool = this.muted;
        if (bool != null) {
            return bool.booleanValue();
        }
        if (getActivity() instanceof AbstractImageActivity) {
            boolean nsfw = ((AbstractImageActivity) getActivity()).isNsfw();
            if (SettingsSingleton.getSettings().nsfwMute && nsfw) {
                return true;
            }
        }
        return SettingsSingleton.getSettings().videoMute;
    }

    void showVideoControls(boolean hasAudio) {
        CustomPhotoViewVerticalDragLayout customPhotoViewVerticalDragLayout;
        if (this.mGifBackButton.getVisibility() == 8) {
            this.mGifBackButton.setVisibility(0);
            this.mGifBackButton.setAlpha(0.0f);
            this.mGifBackButton.animate().alpha(1.0f);
        }
        if (this.mGifButton.getVisibility() == 8) {
            this.mGifButton.setVisibility(0);
            this.mGifButton.setAlpha(0.0f);
            this.mGifButton.animate().alpha(1.0f);
        }
        if (this.mGifTime.getVisibility() == 8 && ((NewVideoInterface) getVideoView()).getDuration() > 1000) {
            this.mGifTime.setVisibility(0);
            this.mGifTime.setAlpha(0.0f);
            this.mGifTime.animate().alpha(1.0f);
        }
        if (SettingsSingleton.getSettings().gifSeek && (((customPhotoViewVerticalDragLayout = this.mImageView) == null || customPhotoViewVerticalDragLayout.getVisibility() != 0 || this.mImageView.getView().getDuration() <= 1000) && getVideoWrapper().getVisibility() == 0 && ((NewVideoInterface) getVideoView()).getDuration() > 1000)) {
            this.mSeekBarWrapper.setVisibility(0);
            this.mSeekBarWrapper.setAlpha(0.0f);
            this.mSeekBarWrapper.animate().alpha(1.0f);
        }
        if (SettingsSingleton.getSettings().gifSpeed) {
            this.mGifSpeedWrapper.setVisibility(0);
        }
        if (hasAudio) {
            this.mGifVolumeButton.setVisibility(0);
            this.mGifVolumeButton.setAlpha(0.0f);
            this.mGifVolumeButton.animate().alpha(1.0f);
        }
    }

    public void onDeepzoomError(final String str) {
        this.mSubsamplingImageView.post(new Runnable() { // from class: ma.k
            @Override // java.lang.Runnable
            public final void run() {
                ImageViewerFragment.this.onTileError(str);
            }
        });
    }

    public void onImageError(final String str) {
        this.mSubsamplingImageView.post(new Runnable() { // from class: ma.j
            @Override // java.lang.Runnable
            public final void run() {
                ImageViewerFragment.this.onGifLoadError(str);
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (TestUtils.isRobolectric()) {
            return;
        }
        detectAndLoad(this.url);
    }

    public void loadFormattedGif(String str) {
        if (str.contains("preview.redd.it") && str.contains("format=mp4")) {
            appendDebugLog("Detected reddit mp4: " + str);
            loadVideo(str, 1);
            return;
        }
        if (str.contains("imgur") && str.contains(".gifv")) {
            String strReplace = str.replace(".gifv", ".mp4");
            appendDebugLog("Detected Imgur GIFV: " + strReplace);
            loadVideo(strReplace, 1);
            return;
        }
        if (str.contains("imgur") && str.contains(".mp4")) {
            loadVideo(str, 1);
            return;
        }
        if (str.contains("eroshare") && str.contains(".mp4")) {
            loadVideo(str, 1);
            return;
        }
        if (str.contains("giphy") && str.contains(".mp4")) {
            appendDebugLog("Detected Giphy MP4: " + str);
            loadVideo(str, 1);
            return;
        }
        if (!str.contains("sli.mg") || !str.contains(".mp4")) {
            loadVideo(str, 0);
            return;
        }
        appendDebugLog("Detected SLi.MG MP4: " + str);
        loadVideo(str, 1);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(getLayoutResId(), (ViewGroup) null);
        ButterKnife.bind(this, viewInflate);
        this.mSeekBar.setPadding(SizeHelper.dpToPx(16), 0, SizeHelper.dpToPx(16), 0);
        return viewInflate;
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: U1 */
    public void onDestroy() {
        cleanup();
        super.onDestroy();
    }

    protected int getCurrentSpeedIndex() {
        int i = 0;
        while (true) {
            float[] fArr = this.speeds;
            if (i >= fArr.length) {
                throw new RuntimeException("Unsupported speed");
            }
            if (fArr[i] == this.speed) {
                return i;
            }
            i++;
        }
    }

    boolean isSingleImage() {
        return this.total == 1;
    }

    @Override // androidx.fragment.app.Fragment
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
        if (isInPictureInPictureMode) {
            this.mCoordinator.setVisibility(8);
        } else {
            this.mCoordinator.setVisibility(0);
        }
    }

    public void processImageFile(String str, File file) {
        new ImageDecodeThread(file, str).start();
    }

    @Override // com.resync.app.ui.fragments.RedditFragment, androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: k2 */
    public void onResume() {
        super.onResume();
        if (hasVideoPlayer() && getVideoWrapper().getVisibility() == 0 && ((NewVideoInterface) getVideoView()).hasPathSet() && !((NewVideoInterface) getVideoView()).isVideoPlaying() && !TestUtils.isRobolectric()) {
            ((NewVideoInterface) getVideoView()).setMuted(shouldMute());
            ((NewVideoInterface) getVideoView()).onResume(true);
            if (this.videoPosition != null) {
                ((NewVideoInterface) getVideoView()).seekTo(this.videoPosition.intValue());
                Boolean bool = this.videoPaused;
                if (bool != null) {
                    if (bool.booleanValue()) {
                        this.mGifButton.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                        this.mGifButton.setSelected(true);
                    } else {
                        ((NewVideoInterface) getVideoView()).resume();
                        this.mGifButton.setImageResource(R.drawable.ic_pause_white_24dp);
                        this.mGifButton.setSelected(false);
                    }
                    this.videoPaused = null;
                }
                StaticToast.debugToast("SEEKING TO: " + this.videoPosition);
                this.videoPosition = null;
            }
            updateSeekbar();
            this.mImageSize.postDelayed(this.updateRunnable, 30L);
            Logger.verbose("FRAGENT SGIYKD STARGT");
        }
    }

    @Override // com.resync.app.ui.interfaces.RedditFragmentInterface
    public int getLayoutResId() {
        return R.layout.fragment_image;
    }

    @Override // androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        if (hasVideoPlayer()) {
            Integer num = this.videoPosition;
            if (num != null) {
                bundle.putInt("position", num.intValue());
            }
            Boolean bool = this.videoPaused;
            if (bool != null) {
                bundle.putBoolean("paused", bool.booleanValue());
            }
            Boolean bool2 = this.muted;
            if (bool2 != null) {
                bundle.putBoolean("muted", bool2.booleanValue());
            }
        }
        super.onSaveInstanceState(bundle);
    }

    public void cleanup() {
        if (this.destroyed) {
            return;
        }
        Logger.verbose("Manual cleanup called: " + this.position);
        if (getActivity() instanceof SingleImageActivity) {
            ((SingleImageActivity) getActivity()).setAnimationCompleteListener(null);
        }
        CustomPhotoViewVerticalDragLayout customPhotoViewVerticalDragLayout = this.mImageView;
        if (customPhotoViewVerticalDragLayout != null) {
            customPhotoViewVerticalDragLayout.teardown();
        }
        if (hasVideoPlayer()) {
            getVideoWrapper().teardown();
        }
        CustomExoPlayerViewVerticalDragLayout customExoPlayerViewVerticalDragLayout = this.videoPlayerWrapper;
        if (customExoPlayerViewVerticalDragLayout != null) {
            customExoPlayerViewVerticalDragLayout.teardown();
            this.mImageContent.removeView(this.videoPlayerWrapper);
            this.videoPlayerWrapper = null;
        }
        SubsamplingViewVerticalDragLayout subsamplingViewVerticalDragLayout = this.mSubsamplingImageView;
        if (subsamplingViewVerticalDragLayout != null) {
            subsamplingViewVerticalDragLayout.teardown();
        }
        this.destroyed = true;
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: n2 */
    public void onStop() {
        if (hasVideoPlayer()) {
            this.videoPosition = Integer.valueOf((int) ((NewVideoInterface) getVideoView()).onStop());
            this.videoPaused = Boolean.valueOf(this.mGifButton.isSelected());
            this.muted = Boolean.valueOf(this.mGifVolumeButton.isSelected());
            Logger.verbose("STOPPING MUTED: " + this.muted);
        }
        super.onStop();
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(android.view.View view, android.os.Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (getArguments().containsKey("start_video_position")) {
            this.videoPosition = getArguments().getInt("start_video_position", 0);
            getArguments().remove("start_video_position");
        }
        if (getArguments().containsKey("start_video_paused")) {
            this.videoPaused = getArguments().getBoolean("start_video_paused");
            getArguments().remove("start_video_paused");
        }
        Logger.verbose("PAUSED: " + this.videoPaused);
        if (bundle != null && bundle.containsKey("position")) {
            this.videoPosition = bundle.getInt("position");
            bundle.remove("position");
        }
        if (bundle != null && bundle.containsKey("paused")) {
            this.videoPaused = bundle.getBoolean("paused");
            bundle.remove("paused");
        }
        if (bundle != null && bundle.containsKey("muted")) {
            this.muted = bundle.getBoolean("muted");
            bundle.remove("muted");
        }
        if (SettingsSingleton.getSettings().viewerShowSave) {
            view.findViewById(R.id.image_options_alt_download_button).setVisibility(0);
            view.findViewById(R.id.image_action_save).setVisibility(8);
        }
        if (SettingsSingleton.getSettings().imageDebug) {
            this.mDebugWrapper.setVisibility(0);
        }
        if (SettingsSingleton.getSettings().quickDismiss) {
            DismissClickListener dismissListener = new DismissClickListener();
            this.mSubsamplingImageView.setOnDragClickListener(dismissListener);
            this.mImageView.setOnDragClickListener(dismissListener);
            this.mImageProgressBar.setOnClickListener(new ProgressClickListener());
        }
        ToggleChromeLongPressListener toggleChromeListener = new ToggleChromeLongPressListener();
        view.setOnApplyWindowInsetsListener(new WindowInsetsListener());
        ((SubsamplingScaleImageView) this.mSubsamplingImageView.getView()).setBitmapDecoderFactory(new com.davemorrissey.labs.subscaleview.decoder.CompatDecoderFactory(com.davemorrissey.labs.subscaleview.decoder.SkiaImageDecoder.class, android.graphics.Bitmap.Config.ARGB_8888));
        ((SubsamplingScaleImageView) this.mSubsamplingImageView.getView()).setRegionDecoderFactory(new com.davemorrissey.labs.subscaleview.decoder.CompatDecoderFactory(com.davemorrissey.labs.subscaleview.decoder.SkiaImageRegionDecoder.class, android.graphics.Bitmap.Config.ARGB_8888));
        this.mSubsamplingImageView.setOnDragLongPressListener(toggleChromeListener);
        this.mImageView.setOnDragLongPressListener(toggleChromeListener);
        this.total = getArguments().getInt("total");
        this.position = getArguments().getInt("pos");
        this.subreddit = getArguments().getString("subreddit");
        setLinkText(getArguments().getString("url"));
        this.linkTitle = getArguments().getString("link_title");
        this.title = getArguments().getString("title");
        this.commentsUrl = getArguments().getString("comments_url");
        Logger.verbose("ImageViewerFragment", "Deepzoom DPI: " + SettingsSingleton.getSettings().deepzoomDpi);
        if (!isSingleImage() && SettingsSingleton.getSettings().deepzoomDpi != -1) {
            ((SubsamplingScaleImageView) this.mSubsamplingImageView.getView()).setMinimumTileDpi(SettingsSingleton.getSettings().deepzoomDpi);
        }
        if (TextUtils.isEmpty(this.commentsUrl)) {
            view.findViewById(R.id.image_action_comments).setVisibility(8);
        }
        ColorDrawable colorDrawable = new ColorDrawable(((AbstractImageActivity) getActivity()).getDomColor());
        colorDrawable.setAlpha(0);
        this.mBottomSheetContent.setBackgroundDrawable(colorDrawable);
        ImageViewerBottomSheetBehavior behavior = ImageViewerBottomSheetBehavior.from(this.mBottomSheet);
        this.bottomSheetBehavior = behavior;
        behavior.setPeekHeight(com.resync.app.helpers.WindowHelper.getActionBarHeight(getContext()));
        this.bottomSheetBehavior.setHideable(true);
        this.bottomSheetBehavior.addBottomSheetCallback(new BottomSheetSlideCallback(colorDrawable, view));
        bindBottomSheet();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new BackKeyListener());
        this.mBottomSheet.setAlpha(0.0f);
        this.mBottomSheet.animate().alpha(1.0f).start();
        boolean paintVisible = false;
        if (com.resync.app.premium.UltraHelper.isUltra()) {
            if (!com.resync.app.helpers.RemoteConfigHelper.isC4266fa()) {
                paintVisible = true;
            } else {
                String value = com.resync.app.helpers.PreferencesHelper.getDefaultPrefs().getString(com.resync.app.security.KeyManager.purchaseTokenKey(), null);
                if (!StringUtils.isEmpty(value) && StringUtils.containsIgnoreCase(value, ".AO-")) {
                    paintVisible = true;
                }
            }
        }
        getView().findViewById(R.id.image_options_extract_text).setVisibility(paintVisible ? 0 : 8);
    }

    @OnClick
    public void onCopyUrl() {
        RedditHelper.copyUrlToClipboard(getActivity(), this.url);
        this.bottomSheetBehavior.setState(4);
    }

    @OnClick
    public void onExtractClicked() {
        FragmentUtils.showWithUrl(ExtractTextAsyncAlertDialogBottomSheet.class, getFragmentManager(), this.url);
        this.bottomSheetBehavior.setState(4);
    }

    @OnClick
    public void onGifBackButtonClicked() {
        if (this.mImageView.getVisibility() == 0) {
            this.mImageView.getView().recycleGif();
        } else {
            ((NewVideoInterface) getVideoView()).restart();
        }
    }

    @OnClick
    public void onGifButtonClicked(View view) {
        if (!view.isSelected()) {
            if (this.mImageView.getVisibility() == 0) {
                this.mImageView.getView().stopGif();
            } else {
                ((NewVideoInterface) getVideoView()).pause();
            }
            ((AppCompatImageView) view).setImageResource(R.drawable.ic_play_arrow_white_24dp);
            view.setSelected(true);
            return;
        }
        if (this.mImageView.getVisibility() == 0) {
            this.mImageView.getView().startGif();
        } else {
            ((NewVideoInterface) getVideoView()).resume();
        }
        ((AppCompatImageView) view).setImageResource(R.drawable.ic_pause_white_24dp);
        view.setSelected(false);
        getVideoView().post(this.updateRunnable);
    }

    @OnClick
    public void onGifHdButtonClicked() {
        if (hasVideoPlayer()) {
            this.mGifHdButton.setVisibility(8);
            String source = ((NewVideoInterface) getVideoView()).getSource();
            if (this.videoPlayerWrapper != null) {
                ((NewVideoInterface) getVideoView()).onStop();
                this.videoPlayerWrapper.teardown();
                this.mImageContent.removeView(this.videoPlayerWrapper);
                this.videoPlayerWrapper = null;
            }
            enableHd();
            setupStreaming(source);
            this.mGifBackButton.setVisibility(8);
            this.mGifButton.setVisibility(8);
            this.mGifTime.setVisibility(8);
            this.mSeekBarWrapper.setVisibility(8);
            this.mGifSpeedWrapper.setVisibility(8);
            this.mGifVolumeButton.setVisibility(8);
            if (SettingsSingleton.getSettings().redditVideoQualityCheck) {
                StaticToast.showWithContext(getActivity(), "Enabling HD playback");
            } else {
                new AlertDialog.Builder(getActivity()).setTitle("HD playback enabled").setMessage("Default all playback to HD?").setPositiveButton("Yes", new VideoQualityYesListener()).setNegativeButton("No", new VideoQualityNoListener()).create().show();
            }
        }
    }

    @OnClick
    public void onOpenComments() {
        LinkHelper.open(getActivity(), this.commentsUrl);
        this.bottomSheetBehavior.setState(4);
        if ((getActivity() instanceof SingleImageActivity) && ((SingleImageActivity) getActivity()).isTransitionEnabled()) {
            BusSingleton.getBus().post(new CommentsLoaded());
        }
        getActivity().finish();
    }

    @OnClick
    public void onOpenExternally() {
        com.resync.app.util.links.LinkHandler.openExternal(getActivity(), this.url);
        this.bottomSheetBehavior.setState(4);
    }

    @OnClick
    public void onPeekAreaClicked() {
        if (this.bottomSheetBehavior.getState() == 3) {
            this.bottomSheetBehavior.setState(4);
        } else {
            this.bottomSheetBehavior.setState(3);
        }
    }

    @OnClick
    public void onSaveImage() {
        if ((this.url.endsWith(".mp4") || this.url.endsWith(".gifv") || this.url.endsWith(".gif")) && this.url.contains("imgur.com")) {
            this.url.replace(".gifv", ".mp4");
            this.url.replace(".gif", ".mp4");
            DownloadMediaJob.download(getParentFragmentManager(), this.linkTitle, this.subreddit, this.url, 1);
        } else if (LinkTypeHelper.isRedditVideo(this.url)) {
            DownloadRedditVideoJob.download(getParentFragmentManager(), this.linkTitle, this.subreddit, this.url, 1);
        } else {
            DownloadMediaJob.download(getParentFragmentManager(), this.linkTitle, this.subreddit, this.url, 1);
        }
        this.bottomSheetBehavior.setState(4);
    }

    @OnClick
    public void onSearchCliced() {
        com.resync.app.util.links.LinkHandler.openExternal(getActivity(), "https://images.google.com/searchbyimage?image_url=" + this.url);
    }

    @OnClick
    public void onShareUrl() {
        FragmentUtils.showWithArguments(ShareImageSelectionDialogBottomSheet.class, getParentFragmentManager(), ShareImageSelectionDialogBottomSheet.buildArguments(this.linkTitle, this.subreddit, this.url));
        this.bottomSheetBehavior.setState(4);
    }

    @OnClick
    public void onSpeedMinusClicked() {
        int currentIndex = getCurrentSpeedIndex();
        if (currentIndex == 0) {
            return;
        }
        int newIndex = currentIndex - 1;
        this.speed = this.speeds[newIndex];
        this.mGifSpeedLabel.setText(this.speedLabels[newIndex]);
        ((CustomExoPlayerView) getVideoView()).setSpeed(this.speed);
    }

    @OnClick
    public void onSpeedPlusClicked() {
        int currentIndex = getCurrentSpeedIndex();
        float[] fArr = this.speeds;
        if (currentIndex == fArr.length - 1) {
            return;
        }
        int newIndex = currentIndex + 1;
        this.speed = fArr[newIndex];
        this.mGifSpeedLabel.setText(this.speedLabels[newIndex]);
        ((CustomExoPlayerView) getVideoView()).setSpeed(this.speed);
    }

    @OnClick
    public void onVolumeButtonClicked(View view) {
        if (view.isSelected()) {
            ((NewVideoInterface) getVideoView()).unmute();
            ((AppCompatImageView) view).setImageResource(R.drawable.ic_volume_up_white_24dp);
            view.setSelected(false);
        } else {
            ((NewVideoInterface) getVideoView()).mute();
            ((AppCompatImageView) view).setImageResource(R.drawable.ic_volume_mute_white_24dp);
            view.setSelected(true);
        }
    }

    public void loadDeepzoom(File file) {
        if (this.url.contains("imgur.com") && this.url.contains(".jpg")) {
            try {
                com.resync.app.util.ImageUtils.JpegInfo jpegInfo = com.resync.app.util.ImageUtils.readJpegInfo(file);
                if (jpegInfo != null && StringUtils.containsIgnoreCase(jpegInfo.toString(), "Lavc")) {
                    appendDebugLog("Detected a disguised GIF");
                    this.url = this.url.replace(".jpg", ".mp4");
                    loadGif();
                    return;
                }
            } catch (Exception e) {
                Logger.logException(e);
            }
        }
        this.mImageView.setVisibility(8);
        if (hasVideoPlayer()) {
            getVideoWrapper().setVisibility(8);
        }
        this.mSubsamplingImageView.setVisibility(8);
        this.mSubsamplingImageView.getView().setOnImageEventListener(new DeepzoomEventListener());
        this.mImageView.setVisibility(8);
        this.mSubsamplingImageView.setVisibility(0);
        this.mSubsamplingImageView.setAlpha(0.0f);
        this.mSubsamplingImageView.getView().setTileBackgroundColor(-1);
        appendDebugLog("Deepzoom resampling enabled and skipping preview");
        this.mSubsamplingImageView.getView().setImage(ImageSource.uri(Uri.fromFile(file)));
    }

    public void pauseVideo() {
        if (hasVideoPlayer() && ((NewVideoInterface) getVideoView()).isVideoPlaying()) {
            ((NewVideoInterface) getVideoView()).pause();
            this.mGifButton.setImageResource(R.drawable.ic_play_arrow_white_24dp);
            this.mGifButton.setSelected(true);
        }
        ImageViewerBottomSheetBehavior imageViewerBottomSheetBehavior = this.bottomSheetBehavior;
        if (imageViewerBottomSheetBehavior != null) {
            imageViewerBottomSheetBehavior.setState(4);
        }
    }

    public void resumeVideo() {
        if (hasVideoPlayer() && ((NewVideoInterface) getVideoView()).hasPathSet()) {
            ((NewVideoInterface) getVideoView()).resume();
            updateSeekbar();
            this.mImageSize.postDelayed(this.updateRunnable, 30L);
            this.mGifButton.setImageResource(R.drawable.ic_pause_white_24dp);
            this.mGifButton.setSelected(false);
        }
    }

    protected void enableHd() {
        this.hdEnabled = true;
    }
}
