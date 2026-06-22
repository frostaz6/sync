package com.resync.app.ui.views.media;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.posts.simple.IndicatorView;
import java.io.File;
import com.resync.app.media.images.SyncTypeHelper;
import com.resync.app.model.Post;
import com.resync.app.presenters.PostsPresenter;

/* JADX INFO: loaded from: classes3.dex */
public class SmartMediaView extends FrameLayout {

    Post post;
    PostsPresenter presenter;

    IndicatorView indicatorView;

    SubsamplingScaleImageView subsamplingImageView;

    class FileRequestListener implements RequestListener<File> {

        /* JADX INFO: renamed from: com.resync.app.ui.views.media.SmartMediaView$a$a, reason: collision with other inner class name */
        class ImageEventListener implements SubsamplingScaleImageView.OnImageEventListener {
            ImageEventListener() {
            }

            @Override // com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.OnImageEventListener
            public void onImageLoadError(Exception exc) {
            }

            @Override // com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.OnImageEventListener
            public void onImageLoaded() {
                SmartMediaView.this.subsamplingImageView.animate().alpha(1.0f).setInterpolator(new FastOutSlowInInterpolator()).start();
            }

            @Override // com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.OnImageEventListener
            public void onPreviewLoadError(Exception exc) {
            }

            @Override // com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.OnImageEventListener
            public void onPreviewReleased() {
            }

            @Override // com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.OnImageEventListener
            public void onReady() {
                SmartMediaView.this.subsamplingImageView.setDoubleTapZoomDuration(220);
                SubsamplingScaleImageView subsamplingScaleImageView = SmartMediaView.this.subsamplingImageView;
                subsamplingScaleImageView.setMaxScale(subsamplingScaleImageView.getScale() * SettingsSingleton.getSettings().deepzoomMax);
                SubsamplingScaleImageView subsamplingScaleImageView2 = SmartMediaView.this.subsamplingImageView;
                subsamplingScaleImageView2.setDoubleTapZoomScale(subsamplingScaleImageView2.getScale() * 2.0f);
                SmartMediaView.this.subsamplingImageView.resetScaleAndCenter();
                SmartMediaView smartMediaView = SmartMediaView.this;
                smartMediaView.subsamplingImageView.setZoomEnabled(smartMediaView.indicatorView.getVisibility() == 8);
            }

            @Override // com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.OnImageEventListener
            public void onTileLoadError(Exception exc) {
            }
        }

        FileRequestListener() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void onFileReady(File file) {
            SmartMediaView.this.subsamplingImageView.setOnImageEventListener(new ImageEventListener());
            SmartMediaView.this.subsamplingImageView.setImage(ImageSource.uri(Uri.fromFile(file)));
        }

        @Override // com.bumptech.glide.request.RequestListener
        /* JADX INFO: renamed from: c, reason: merged with bridge method [inline-methods] */
        public boolean onResourceReady(final File file, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
            SmartMediaView.this.post(new Runnable() { // from class: com.resync.app.ui.views.media.a
                @Override // java.lang.Runnable
                public final void run() {
                    FileRequestListener.this.onFileReady(file);
                }
            });
            return false;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException glideException, Object model, Target<File> target, boolean isFirstResource) {
            return false;
        }
    }

    public SmartMediaView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_smart_media, (ViewGroup) this, true);
        ButterKnife.bind(this);
    }

    public void recycle() {
        this.subsamplingImageView.recycle();
    }

    public void bind(Post post) {
        this.post = post;
        this.indicatorView.bind(post, 8);
        if (!SyncTypeHelper.isMediaPost(post.getSyncType()) || this.subsamplingImageView.hasImage()) {
            return;
        }
        this.subsamplingImageView.setAlpha(0.0f);
        Glide.with(RedditApplication.getAppContext()).asFile().load(post.getPreviewUrl()).listener(new FileRequestListener()).submit();
    }

    public void setPresenter(PostsPresenter postsPresenter) {
        this.presenter = postsPresenter;
    }

    @OnClick
    public void onImageClicked() {
        PostsPresenter postsPresenter = this.presenter;
        if (postsPresenter == null || this.post == null) {
            return;
        }
        postsPresenter.toggleExpanded();
    }

    @OnClick
    public void onIndicatorClicked() {
        Post post;
        PostsPresenter postsPresenter = this.presenter;
        if (postsPresenter == null || (post = this.post) == null) {
            return;
        }
        postsPresenter.openLink(null, post);
    }
}
