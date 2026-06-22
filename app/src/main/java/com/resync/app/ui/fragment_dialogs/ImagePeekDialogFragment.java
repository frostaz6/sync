package com.resync.app.ui.fragment_dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RedditHelper;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.animation.OnStartListener;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.reddit.requests.external.requests.gfycat.GrabGfycatRequest;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.views.buttons.CommentButton;
import com.resync.app.ui.views.buttons.CopyButton;
import com.resync.app.ui.views.buttons.ExternalButton;
import com.resync.app.ui.views.buttons.ShareButton;
import com.resync.app.ui.views.images.CustomImageView;
import com.resync.app.ui.views.video.CustomExoPlayerView;
import java.util.Locale;
import com.resync.app.fragments.ImageGridFragment;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.media.imgur.ImgurImageHelper;
import com.resync.app.media.ImageErrors;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.StopExoplayerEvent;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.Gallery;
import com.resync.app.net.thirdparty.CheckDeviantArtRequest;
import com.resync.app.net.thirdparty.CheckStreamableRequest;
import com.resync.app.ui.views.video.NewVideoInterface;
import com.resync.app.net.thirdparty.ImgurGalleryHelper;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.CrashlyticsHelper;
import com.resync.app.helpers.PostsHelper;
import com.resync.app.helpers.WindowUtils;
import com.resync.app.net.thirdparty.RedgifRequestWrapper;
import com.resync.app.dialogs.ShareImageSelectionDialogBottomSheet;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;
import com.resync.app.util.VibrateUtils;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: loaded from: classes.dex */
public class ImagePeekDialogFragment extends AppCompatDialogFragment {
    public static final String TAG = ImagePeekDialogFragment.class.getSimpleName();
    static String KEY_SOURCE_WIDTH = "source_width";
    static String KEY_SOURCE_HEIGHT = "source_height";
    Post mPost;
    String mUrl;
    boolean mDestroyed;
    int mSourceWidth;
    int mSourceHeight;

    @BindView(R.id.peek_comments)
    CommentButton mCommentButton;

    @BindView(R.id.peek_copy)
    CopyButton mCopyButton;

    @BindView(R.id.dialog_fragment_image_preview_error)
    TextView mErrorMessage;

    @BindView(R.id.peek_external)
    ExternalButton mExternalButton;

    @BindView(R.id.image_grid)
    GridView mImageGrid;

    @BindView(R.id.dialog_fragment_image_preview_image)
    CustomImageView mImageView;

    @BindView(R.id.peek_options)
    LinearLayout mPeekOptionsWrapper;

    @BindView(R.id.dialog_fragment_image_preview_progress)
    ProgressBar mProgressBar;

    @BindView(R.id.dialog_fragment_image_preview_root)
    RelativeLayout mRootView;

    @BindView(R.id.peek_share)
    ShareButton mShareButton;

    @BindView(R.id.dialog_fragment_image_preview_video)
    CustomExoPlayerView mVideoView;

    class RedgifErrorListener implements Response.ErrorListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String requestUrl;

        RedgifErrorListener(String str) {
            this.requestUrl = str;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (ImagePeekDialogFragment.this.getActivity() == null) {
                return;
            }
            ImagePeekDialogFragment.this.showError(this.requestUrl, 1);
        }
    }

    class DeviantArtListener implements Response.Listener<String> {
        DeviantArtListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String str) {
            if (str.contains(".gif")) {
                ImagePeekDialogFragment.this.loadGif(str);
            } else {
                ImagePeekDialogFragment.this.loadImage(str);
            }
        }
    }

    class DeviantArtErrorListener implements Response.ErrorListener {
        DeviantArtErrorListener() {
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
        }
    }

    class ImgurGalleryListener implements Response.Listener<Gallery> {
        ImgurGalleryListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(Gallery gallery) {
            if (ImagePeekDialogFragment.this.isActive()) {
                ImagePeekDialogFragment.this.mProgressBar.setVisibility(8);
                if (gallery.urls.length != 1 || TextUtils.isEmpty(ImagePeekDialogFragment.this.mPost.getPreviewUrl())) {
                    ImagePeekDialogFragment.this.mImageGrid.setAdapter((ListAdapter) new ImageGridFragment.ImageGridAdapter(ImagePeekDialogFragment.this.getActivity(), ImagePeekDialogFragment.this.mImageGrid, gallery.thumbnails));
                    return;
                }
                Logger.verbose(ImagePeekDialogFragment.TAG, "Opening preview: " + ImagePeekDialogFragment.this.mPost.getPreviewUrl());
                ImagePeekDialogFragment imagePeekDialogFragment = ImagePeekDialogFragment.this;
                imagePeekDialogFragment.mUrl = imagePeekDialogFragment.mPost.getPreviewUrl();
                ImagePeekDialogFragment imagePeekDialogFragment2 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment2.loadImage(imagePeekDialogFragment2.mPost.getPreviewUrl());
            }
        }
    }

    class ImgurGalleryErrorListener implements Response.ErrorListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String requestUrl;

        ImgurGalleryErrorListener(String str) {
            this.requestUrl = str;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            ImagePeekDialogFragment.this.showError(this.requestUrl, 0);
        }
    }

    class GlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        GlobalLayoutListener() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            ImagePeekDialogFragment.this.mRootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            if (ImagePeekDialogFragment.this.mPost.getSyncType() == 2) {
                ImagePeekDialogFragment imagePeekDialogFragment = ImagePeekDialogFragment.this;
                imagePeekDialogFragment.mUrl = imagePeekDialogFragment.mPost.getPreviewUrl();
                if (StringUtils.isEmpty(ImagePeekDialogFragment.this.mPost.getPreviewUrl())) {
                    ImagePeekDialogFragment imagePeekDialogFragment2 = ImagePeekDialogFragment.this;
                    imagePeekDialogFragment2.mUrl = imagePeekDialogFragment2.mPost.getThumbnail();
                }
                ImagePeekDialogFragment imagePeekDialogFragment3 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment3.loadImage(imagePeekDialogFragment3.mUrl);
                return;
            }
            String url = ImagePeekDialogFragment.this.mPost.getUrl();
            if (!TextUtils.isEmpty(url)) {
                url = url.replaceAll("\\.gifv", "\\.mp4");
            }
            String str = ImagePeekDialogFragment.TAG;
            Logger.verbose(str, "URL: " + url);
            if (TextUtils.isEmpty(url)) {
                ImagePeekDialogFragment imagePeekDialogFragment4 = ImagePeekDialogFragment.this;
                if (TextUtils.isEmpty(imagePeekDialogFragment4.mPost.getThumbnailPlaceholder(imagePeekDialogFragment4.getContext()))) {
                    return;
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Opening thumbnail: ");
                ImagePeekDialogFragment imagePeekDialogFragment5 = ImagePeekDialogFragment.this;
                sb2.append(imagePeekDialogFragment5.mPost.getThumbnailPlaceholder(imagePeekDialogFragment5.getContext()));
                Logger.verbose(str, sb2.toString());
                ImagePeekDialogFragment imagePeekDialogFragment6 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment6.mUrl = imagePeekDialogFragment6.mPost.getThumbnailPlaceholder(imagePeekDialogFragment6.getContext());
                ImagePeekDialogFragment imagePeekDialogFragment7 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment7.loadImage(imagePeekDialogFragment7.mPost.getThumbnailPlaceholder(imagePeekDialogFragment7.getContext()));
                return;
            }
            if (!android.text.TextUtils.isEmpty(url) && url.contains("v.redd.it")) {
                Logger.verbose(str, "Opening reddit video: " + url);
                String str2 = "https://v.redd.it/" + com.resync.app.util.links.LinkHandler.getRedditVideoId(url) + "/HLSPlaylist.m3u8";
                ImagePeekDialogFragment imagePeekDialogFragment8 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment8.mUrl = str2;
                imagePeekDialogFragment8.loadVideo(str2);
                return;
            }
            if (url.contains("mediadownloads.mlb.com") && url.endsWith(".mp4")) {
                Logger.verbose(str, "Opening mlb video: " + url);
                ImagePeekDialogFragment imagePeekDialogFragment9 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment9.mUrl = url;
                imagePeekDialogFragment9.loadVideo(url);
                return;
            }
            if (ImgurImageHelper.isAlbum(url)) {
                Logger.verbose(str, "Opening album: " + url);
                ImagePeekDialogFragment imagePeekDialogFragment10 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment10.mUrl = url;
                imagePeekDialogFragment10.loadImgurGallery(url);
                return;
            }
            if (LinkTypeHelper.isStreamable(url)) {
                Logger.verbose(str, "Opening streamable: " + url);
                ImagePeekDialogFragment imagePeekDialogFragment11 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment11.mUrl = url;
                imagePeekDialogFragment11.loadStreamable(url);
                return;
            }
            if (LinkTypeHelper.isGfycat(url)) {
                Logger.verbose(str, "Opening Gfycat: " + url);
                ImagePeekDialogFragment imagePeekDialogFragment12 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment12.mUrl = url;
                imagePeekDialogFragment12.loadGfycat(url);
                return;
            }
            if (StringUtils.contains(url, "redgifs.com")) {
                Logger.verbose(str, "Opening redgifs: " + url);
                ImagePeekDialogFragment imagePeekDialogFragment13 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment13.mUrl = url;
                imagePeekDialogFragment13.loadRedgif(url);
                return;
            }
            if (LinkTypeHelper.isDeviantArt(url)) {
                Logger.verbose(str, "Opening DeviantArt: " + url);
                ImagePeekDialogFragment imagePeekDialogFragment14 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment14.mUrl = url;
                imagePeekDialogFragment14.loadDeviantArt(url);
                return;
            }
            if (LinkTypeHelper.isGiphy(url)) {
                Logger.verbose(str, "Opening Giphy: " + url);
                ImagePeekDialogFragment imagePeekDialogFragment15 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment15.mUrl = url;
                imagePeekDialogFragment15.loadVideo(url);
                return;
            }
            if (LinkTypeHelper.isMixtape(url) && (url.contains(".mp4") || url.contains(".webm"))) {
                Logger.verbose(str, "Opening Mixtape.moe: " + url);
                ImagePeekDialogFragment imagePeekDialogFragment16 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment16.mUrl = url;
                imagePeekDialogFragment16.loadVideo(url);
                return;
            }
            if (LinkTypeHelper.isSliMg(url) && url.contains(".mp4")) {
                Logger.verbose(str, "Opening SLi.MG: " + url);
                ImagePeekDialogFragment imagePeekDialogFragment17 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment17.mUrl = url;
                imagePeekDialogFragment17.loadVideo(url);
                return;
            }
            if (url.contains("redditmedia.com") && url.contains("fm=mp4")) {
                Logger.verbose(str, "Opening reddit media: " + url);
                ImagePeekDialogFragment imagePeekDialogFragment18 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment18.mUrl = url;
                imagePeekDialogFragment18.loadVideo(url);
                return;
            }
            if (LinkTypeHelper.isTwitterVideo(url)) {
                Logger.verbose(str, "Opening Twitter video: " + url);
                ImagePeekDialogFragment imagePeekDialogFragment19 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment19.mUrl = url;
                imagePeekDialogFragment19.loadVideo(url);
                return;
            }
            Locale locale = Locale.ENGLISH;
            if (url.toLowerCase(locale).contains("mp4") && url.contains(".gif")) {
                Logger.verbose(str, "Opening MP4: " + url);
                ImagePeekDialogFragment imagePeekDialogFragment20 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment20.mUrl = url;
                imagePeekDialogFragment20.loadVideo(url);
                return;
            }
            if (url.toLowerCase(locale).contains("mp4")) {
                Logger.verbose(str, "Opening MP4: " + url);
                ImagePeekDialogFragment imagePeekDialogFragment21 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment21.mUrl = url;
                imagePeekDialogFragment21.loadVideo(url);
                return;
            }
            if (url.toLowerCase(locale).contains(".gif")) {
                Logger.verbose(str, "Opening GIF: " + url);
                ImagePeekDialogFragment imagePeekDialogFragment22 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment22.mUrl = url;
                imagePeekDialogFragment22.loadGif(url);
                return;
            }
            if (!TextUtils.isEmpty(ImagePeekDialogFragment.this.mPost.getPreviewUrl())) {
                Logger.verbose(str, "Opening preview: " + ImagePeekDialogFragment.this.mPost.getPreviewUrl());
                ImagePeekDialogFragment imagePeekDialogFragment23 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment23.mUrl = imagePeekDialogFragment23.mPost.getPreviewUrl();
                ImagePeekDialogFragment imagePeekDialogFragment24 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment24.loadImage(imagePeekDialogFragment24.mPost.getPreviewUrl());
                return;
            }
            if (LinkTypeHelper.isImage(url)) {
                Logger.verbose(str, "Opening image: " + url);
                ImagePeekDialogFragment imagePeekDialogFragment25 = ImagePeekDialogFragment.this;
                imagePeekDialogFragment25.mUrl = url;
                imagePeekDialogFragment25.loadImage(url);
                return;
            }
            ImagePeekDialogFragment imagePeekDialogFragment26 = ImagePeekDialogFragment.this;
            if (TextUtils.isEmpty(imagePeekDialogFragment26.mPost.getThumbnailPlaceholder(imagePeekDialogFragment26.getContext()))) {
                return;
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Opening thumbnail: ");
            ImagePeekDialogFragment imagePeekDialogFragment27 = ImagePeekDialogFragment.this;
            sb3.append(imagePeekDialogFragment27.mPost.getThumbnailPlaceholder(imagePeekDialogFragment27.getContext()));
            Logger.verbose(str, sb3.toString());
            ImagePeekDialogFragment imagePeekDialogFragment28 = ImagePeekDialogFragment.this;
            imagePeekDialogFragment28.mUrl = imagePeekDialogFragment28.mPost.getThumbnailPlaceholder(imagePeekDialogFragment28.getContext());
            ImagePeekDialogFragment imagePeekDialogFragment29 = ImagePeekDialogFragment.this;
            imagePeekDialogFragment29.loadImage(imagePeekDialogFragment29.mPost.getThumbnailPlaceholder(imagePeekDialogFragment29.getContext()));
        }
    }

    class ImageRequestListener implements RequestListener<Drawable> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String requestUrl;

        ImageRequestListener(String str) {
            this.requestUrl = str;
        }

        @Override // com.bumptech.glide.request.RequestListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            ImagePeekDialogFragment.this.mProgressBar.setVisibility(8);
            return false;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException glideException, Object obj, Target<Drawable> target, boolean isFirstResource) {
            ImagePeekDialogFragment.this.showError(this.requestUrl, 37);
            return false;
        }
    }

    class GifRequestListener implements RequestListener<GifDrawable> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String requestUrl;

        GifRequestListener(String str) {
            this.requestUrl = str;
        }

        @Override // com.bumptech.glide.request.RequestListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public boolean onResourceReady(GifDrawable gifDrawable, Object obj, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
            ImagePeekDialogFragment.this.mProgressBar.setVisibility(8);
            return false;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException glideException, Object obj, Target<GifDrawable> target, boolean isFirstResource) {
            ImagePeekDialogFragment.this.showError(this.requestUrl, 6);
            return false;
        }
    }

    class VideoStartListener implements OnStartListener {
        VideoStartListener() {
        }

        @Override // com.github.chrisbanes.photoview.animation.OnStartListener
        public void onStart() {
            ImagePeekDialogFragment.this.mProgressBar.setVisibility(8);
        }
    }

    class StreamableListener implements Response.Listener<String> {
        StreamableListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String str) {
            ImagePeekDialogFragment.this.loadVideo(str);
        }
    }

    class StreamableErrorListener implements Response.ErrorListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String requestUrl;

        StreamableErrorListener(String str) {
            this.requestUrl = str;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            ImagePeekDialogFragment.this.showError(this.requestUrl, 12);
        }
    }

    class GfycatListener implements Response.Listener<String> {
        GfycatListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String str) {
            if (ImagePeekDialogFragment.this.getActivity() == null) {
                return;
            }
            ImagePeekDialogFragment.this.loadVideo(str);
        }
    }

    class GfycatErrorListener implements Response.ErrorListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String requestUrl;

        GfycatErrorListener(String str) {
            this.requestUrl = str;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (ImagePeekDialogFragment.this.getActivity() == null) {
                return;
            }
            ImagePeekDialogFragment.this.showError(this.requestUrl, 1);
        }
    }

    class RedgifListener implements Response.Listener<String> {
        RedgifListener() {
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String str) {
            if (ImagePeekDialogFragment.this.getActivity() == null) {
                return;
            }
            ImagePeekDialogFragment.this.loadVideo(str);
        }
    }

    private CustomExoPlayerView getVideoView() {
        CustomExoPlayerView customExoPlayerView = this.mVideoView;
        if (customExoPlayerView != null) {
            return customExoPlayerView;
        }
        throw new RuntimeException("There we no video players found");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showError(String str, int errorCode) {
        if (isActive()) {
            this.mProgressBar.setVisibility(8);
            this.mImageView.setVisibility(8);
            getVideoView().setVisibility(8);
            this.mErrorMessage.setVisibility(0);
            this.mErrorMessage.setText(ImageErrors.getMessage(errorCode));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadDeviantArt(String str) {
        if (isActive()) {
            RedditApplication.thirdPartyRequestQueue.add(new CheckDeviantArtRequest(str, new DeviantArtListener(), new DeviantArtErrorListener()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadImgurGallery(String str) {
        if (isActive()) {
            ImgurGalleryHelper.load(str, new ImgurGalleryListener(), new ImgurGalleryErrorListener(str));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadGfycat(String str) {
        if (isActive()) {
            OAuthRequestManager.enqueueAux(new GrabGfycatRequest(str, false, new GfycatListener(), new GfycatErrorListener(str)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadRedgif(String str) {
        if (isActive()) {
            OAuthRequestManager.sendRedgif(new RedgifRequestWrapper(str, false, new RedgifListener(), new RedgifErrorListener(str)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadStreamable(String str) {
        if (isActive()) {
            RedditApplication.thirdPartyRequestQueue.add(new CheckStreamableRequest(str, new StreamableListener(), new StreamableErrorListener(str)));
        }
    }

    public static void show(Context context, Post post) {
        show(context, post, WindowUtils.getDisplayWidth(), WindowUtils.getDisplayHeight());
    }

    public static void show(Context context, Post post, int width, int height) {
        if (SettingsSingleton.getSettings().peek) {
            BusSingleton.getBus().post(new StopExoplayerEvent());
            if (!(context instanceof BaseActivity)) {
                Logger.verbose("Bailed: wasn't an instance of BaseActivity");
                return;
            }
            BaseActivity baseActivity = (BaseActivity) context;
            if (baseActivity.wasStateSaved()) {
                Logger.verbose("Bailed: was stopped");
                return;
            }
            if (baseActivity.peekActive) {
                Logger.verbose("Bailed: was peeking");
                return;
            }
            baseActivity.peekActive = true;
            ImagePeekDialogFragment imagePeekDialogFragment = new ImagePeekDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("Post", post);
            bundle.putInt(KEY_SOURCE_WIDTH, width);
            bundle.putInt(KEY_SOURCE_HEIGHT, height);
            imagePeekDialogFragment.setArguments(bundle);
            try {
                imagePeekDialogFragment.show(((BaseActivity) context).getSupportFragmentManager(), TAG);
            } catch (Exception e) {
                CrashlyticsHelper.logException(e);
                Logger.logException(e);
            }
        }
    }

    @Override // androidx.appcompat.app.AppCompatDialogFragment, androidx.fragment.app.c
    public Dialog onCreateDialog(Bundle bundle) {
        View viewInflate = View.inflate(getActivity(), getLayoutRes(), null);
        ButterKnife.bind(this, viewInflate);
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(getActivity(), R.style.ImagePeekDialogStyle).setView(viewInflate).setCancelable(false).create();
        try {
            dialog.getWindow().setNavigationBarColor(0);
        } catch (Exception e) {
            Logger.logException(e);
        }
        return dialog;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (!SettingsSingleton.getSettings().peekOptions) {
            this.mPeekOptionsWrapper.setVisibility(8);
        }
        this.mPost = (Post) getArguments().getSerializable("Post");
        this.mSourceWidth = getArguments().getInt(KEY_SOURCE_WIDTH, WindowUtils.getDisplayWidth());
        this.mSourceHeight = getArguments().getInt(KEY_SOURCE_HEIGHT, WindowUtils.getDisplayHeight());
        if (TextUtils.isEmpty(this.mPost.getId()) || StringUtils.equals("id", this.mPost.getId())) {
            this.mCommentButton.setVisibility(8);
        }
        this.mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new GlobalLayoutListener());
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    public boolean isActive() {
        return !this.mDestroyed;
    }

    @Override // androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: U1 */
    public void onDestroy() {
        if (SettingsSingleton.getSettings().viewedPeek) {
            PostsHelper.markViewed(getActivity(), this.mPost);
        }
        this.mDestroyed = true;
        if (this.mImageView.getDrawable() instanceof GifDrawable) {
            ((GifDrawable) this.mImageView.getDrawable()).stop();
            this.mImageView.setImageDrawable(null);
            com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).clear(this.mImageView);
        }
        com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).clear(this.mImageView);
        ((NewVideoInterface) getVideoView()).onStop();
        ((NewVideoInterface) getVideoView()).onDestroy();
        this.mImageView.setVisibility(8);
        getVideoView().setVisibility(8);
        super.onDestroy();
    }

    void loadGif(String str) {
        if (isActive()) {
            this.mImageView.setVisibility(0);
            com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).asGif().load(str).listener(new GifRequestListener(str)).into(this.mImageView);
        }
    }

    void loadImage(String str) {
        if (isActive()) {
            com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).load(str).override(this.mSourceWidth, this.mSourceHeight).priority(Priority.IMMEDIATE).listener(new ImageRequestListener(str)).into(this.mImageView);
        }
    }

    void loadVideo(String str) {
        if (isActive()) {
            ((NewVideoInterface) getVideoView()).setOnStartListener(new VideoStartListener());
            ((NewVideoInterface) getVideoView()).setSource(str, false, SettingsSingleton.getSettings().peekMute);
            ((NewVideoInterface) getVideoView()).start();
        }
    }

    public void handlePeekTouch(float x, float y) {
        String url = this.mPost.getUrl();
        if (android.text.TextUtils.isEmpty(url)) {
            url = this.mUrl;
        }
        Rect rect = new Rect();
        this.mCommentButton.getGlobalVisibleRect(rect);
        if (x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom) {
            VibrateUtils.vibrate();
            com.resync.app.util.links.LinkHandler.openComments(getActivity(), this.mPost);
            return;
        }
        this.mExternalButton.getGlobalVisibleRect(rect);
        if (x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom) {
            VibrateUtils.vibrate();
            com.resync.app.util.links.LinkHandler.openExternal(getActivity(), url);
            return;
        }
        this.mShareButton.getGlobalVisibleRect(rect);
        if (x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom) {
            VibrateUtils.vibrate();
            FragmentUtils.showWithArguments(ShareImageSelectionDialogBottomSheet.class, getParentFragmentManager(), ShareImageSelectionDialogBottomSheet.buildArguments(this.mPost.getTitle(), this.mPost.getSubreddit(), url));
            return;
        }
        this.mCopyButton.getGlobalVisibleRect(rect);
        if (x < rect.left || x > rect.right || y < rect.top || y > rect.bottom) {
            return;
        }
        VibrateUtils.vibrate();
        RedditHelper.copyUrlToClipboard(getActivity(), url);
    }

    int getLayoutRes() {
        return R.layout.dialog_fragment_image_preview;
    }

    @Override // androidx.fragment.app.c, androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: m2 */
    public void onStart() {
        super.onStart();
        BusSingleton.getBus().register(this);
    }

    @Override // androidx.fragment.app.c, androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: n2 */
    public void onStop() {
        BusSingleton.getBus().unregister(this);
        if (getDialog() != null) {
            getDialog().dismiss();
            if (getActivity() instanceof BaseActivity) {
                ((BaseActivity) getActivity()).peekActive = false;
            }
        }
        super.onStop();
    }
}
