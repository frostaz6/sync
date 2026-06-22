package com.resync.app.ui.activities.media;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.Transition;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.ui.activities.media.SingleImageActivity;
import com.resync.app.ui.fragments.ImageViewerFragment;
import com.resync.app.ui.views.images.CustomImageView;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.events.WindowAlphaEvent;
import com.resync.app.helpers.FragmentHelper;
import com.resync.app.ui.interfaces.AnimationCompleteListener;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public class SingleImageActivity extends AbstractImageActivity {

    protected boolean transitionComplete;
    protected AnimationCompleteListener animationCompleteListener;

    @BindView(R.id.placeholder_image_preview)
    CustomImageView mImageView;

    class EnterTransitionListener implements Transition.TransitionListener {

        final /* synthetic */ long startTime;

        EnterTransitionListener(long startTime) {
            this.startTime = startTime;
        }

        @Override // android.transition.Transition.TransitionListener
        public void onTransitionStart(Transition transition) {
        }

        @Override // android.transition.Transition.TransitionListener
        public void onTransitionCancel(Transition transition) {
        }

        @Override // android.transition.Transition.TransitionListener
        public void onTransitionPause(Transition transition) {
        }

        @Override // android.transition.Transition.TransitionListener
        public void onTransitionResume(Transition transition) {
        }

        @Override // android.transition.Transition.TransitionListener
        public void onTransitionEnd(Transition transition) {
            transition.removeListener(this);
            Logger.verbose("Transition length: " + (System.currentTimeMillis() - this.startTime));
            SingleImageActivity singleImageActivity = SingleImageActivity.this;
            singleImageActivity.transitionComplete = true;
            AnimationCompleteListener animationCompleteListener = singleImageActivity.animationCompleteListener;
            if (animationCompleteListener != null) {
                animationCompleteListener.onAnimationComplete();
            }
            SingleImageActivity.this.onTransitionComplete();
        }
    }

    class ImageRequestListener implements RequestListener<Drawable> {

        final /* synthetic */ boolean addFragment;

        ImageRequestListener(boolean addFragment) {
            this.addFragment = addFragment;
        }

        @Override // com.bumptech.glide.request.RequestListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            SingleImageActivity.this.requestLayout();
            if (!this.addFragment) {
                return false;
            }
            SingleImageActivity.this.addImageFragment();
            return false;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException glideException, Object obj, Target<Drawable> target, boolean isFirstResource) {
            SingleImageActivity.this.requestLayout();
            SingleImageActivity.this.setTransitionEnabled(false);
            if (this.addFragment) {
                SingleImageActivity.this.addImageFragment();
            }
            return false;
        }
    }

    class GlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        GlobalLayoutListener() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            SingleImageActivity.this.getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            SingleImageActivity.this.startPostponedEnterTransition();
        }
    }

    class CloseRunnable implements Runnable {
        CloseRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            SingleImageActivity.this.close();
        }
    }

    class TapGestureListener extends GestureDetector.SimpleOnGestureListener {
        TapGestureListener() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent motionEvent) {
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            SingleImageActivity.this.close();
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onTouch(GestureDetector gestureDetector, View view, MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addImageFragment() {
        FragmentHelper.replaceFragment(this, ImageViewerFragment.newInstance(1, getPosition(), getSubreddit(), getContentUrl(), getTitle2(), null, getCommentsUrl(), getStartVideoPosition(), false), R.id.fragment_test_wrapper);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTransitionComplete() {
        Logger.verbose("SingleImageActivity transition complete");
        this.adManager.onShowAd(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestLayout() {
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new GlobalLayoutListener());
        getWindow().getDecorView().requestLayout();
    }

    @Override // com.resync.app.ui.activities.media.AbstractImageActivity
    public void close() {
        if (!isTransitionEnabled() || this.transitionComplete) {
            this.closing = true;
            Logger.verbose("Shared element transition enabled: " + isTransitionEnabled());
            loadImage(false);
            FragmentHelper.removeFragment(this, ImageViewerFragment.class, R.id.fragment_test_wrapper);
            if (isTransitionEnabled()) {
                Logger.verbose("Performing shared element return transition");
                ActivityCompat.finishAfterTransition(this);
            } else {
                Logger.verbose("Skipping animation");
                finish();
                overridePendingTransition(R.anim.video_enter, R.anim.video_exit);
            }
        }
    }

    public boolean canShowAd() {
        return this.transitionComplete || this.savedState != null;
    }

    public void notifyLoaded() {
        Logger.verbose("Notifying loaded: " + this.mImageView);
        this.mImageView.setVisibility(8);
        Logger.verbose("Changed visibility: " + this.mImageView.getVisibility());
    }

    public void resizeImage(int height, int y) {
        if (isTransitionEnabled()) {
            loadImage(false);
            this.mImageView.setLayoutParams(new RelativeLayout.LayoutParams(-1, height));
            this.mImageView.setY(y);
            this.mImageView.post(new CloseRunnable());
            return;
        }
        loadImage(false);
        this.mImageView.setLayoutParams(new RelativeLayout.LayoutParams(-1, height));
        this.mImageView.setY(y);
        close();
    }

    public void setAnimationCompleteListener(AnimationCompleteListener animationCompleteListener) {
        this.animationCompleteListener = animationCompleteListener;
    }

    protected void loadImage(boolean addFragment) {
        this.mImageView.setVisibility(0);
        if (this.mImageView.hasDrawable() && isTransitionEnabled()) {
            return;
        }
        this.mImageView.setNsfw(true);
        this.mImageView.configure(isViewed(), isNsfw());
        if (!TextUtils.isEmpty(getPreviewUrl())) {
            com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).load(getPreviewUrl()).apply(new RequestOptions()).placeholder(new ColorDrawable(-16777216)).skipMemoryCache(true).listener(new ImageRequestListener(addFragment)).into(this.mImageView);
            final GestureDetector gestureDetector = new GestureDetector(this, new TapGestureListener());
            this.mImageView.setOnTouchListener(new View.OnTouchListener() { // from class: o9.a
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    return SingleImageActivity.lambda$onTouch(gestureDetector, view, motionEvent);
                }
            });
        } else {
            requestLayout();
            setTransitionEnabled(false);
            if (addFragment) {
                addImageFragment();
            }
        }
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected int getAdMode() {
        return 1;
    }

    @Override // com.resync.app.ui.activities.media.AbstractImageActivity, com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        postponeEnterTransition();
        super.onCreate(bundle);
        this.toolbar.setVisibility(8);
        if (bundle != null) {
            this.adManager.onShowAd(this);
            this.mRootView.setBackgroundDrawable(new ColorDrawable(getDomColor()));
            return;
        }
        if (!isTransitionEnabled()) {
            Logger.verbose("Transition not enabled");
            this.transitionComplete = true;
            overridePendingTransition(R.anim.video_enter, R.anim.video_exit);
            this.mRootView.setBackgroundDrawable(new ColorDrawable(getDomColor()));
            onTransitionComplete();
            loadImage(true);
            return;
        }
        Logger.verbose("Transition enabled");
        Logger.verbose("DOM COLOR: " + getDomColor() + " - PRE TRANSITION");
        getWindow().getSharedElementEnterTransition().addListener(new EnterTransitionListener(System.currentTimeMillis()));
        loadImage(true);
    }

    @Override // com.resync.app.ui.activities.media.AbstractImageActivity, androidx.activity.ComponentActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_grid).setVisible(false);
        menu.findItem(R.id.menu_download).setVisible(false);
        MonetUtil.tintMenu(menu, -1);
        return true;
    }

    @Override // com.resync.app.ui.activities.media.AbstractImageActivity, com.resync.app.ui.activities.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.animationCompleteListener = null;
        com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).clear(this.mImageView);
    }

    @Subscribe
    public void onWindowAlphaChanged(WindowAlphaEvent windowAlphaEvent) {
        RelativeLayout relativeLayout = this.mRootView;
        if (relativeLayout == null || relativeLayout.getBackground() == null) {
            return;
        }
        this.mRootView.getBackground().setAlpha(windowAlphaEvent.alpha);
    }

    @Override // com.resync.app.ui.activities.media.AbstractImageActivity, com.resync.app.ui.activities.BaseActivity
    public void setupContentView() {
        setContentView(R.layout.activity_single_image);
        super.setupContentView();
    }
}
