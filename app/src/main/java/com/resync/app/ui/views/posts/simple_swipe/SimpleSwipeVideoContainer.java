package com.resync.app.ui.views.posts.simple_swipe;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.resync.app.singleton.SettingsSingleton;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.helpers.WindowUtils;
import com.resync.app.ui.interfaces.VideoProvider;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes3.dex */
public class SimpleSwipeVideoContainer extends FrameLayout {

    private Post post;

    public SimpleSwipeVideoContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean shouldAutoplay() {
        Post post = this.post;
        if (post == null) {
            return false;
        }
        if ((post.getSyncType() != 7 && this.post.getSyncType() != 4) || SettingsSingleton.getSettings().autoplay == 2) {
            return false;
        }
        if ((SettingsSingleton.getSettings().autoplay == 1 && !ConnectivityHelper.isWifiConnected()) || this.post.shouldBlurNsfw() || this.post.isSpoiler() || !isAttachedToWindow() || !isShown()) {
            return false;
        }
        if (!(getContext() instanceof VideoProvider) || ((VideoProvider) getContext()).getVideoHelperView().intersects(this)) {
            return StringUtils.isNotEmpty(this.post.getVideoPreview());
        }
        return false;
    }

    protected int displayWidth() {
        return WindowUtils.getDisplayWidth();
    }

    public void bind(Post post) {
        this.post = post;
        getLayoutParams().height = (int) (((float) displayWidth() / post.getPreviewWidth()) * post.getPreviewHeight());
    }
}
