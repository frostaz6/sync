package com.resync.app.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.PreferencesActivity;
import com.resync.app.ui.views.images.CustomImageView;
import com.resync.app.helpers.ui.TabletHelper;
import okhttp3.internal.http.HttpStatusCodesKt;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.ui.interfaces.VideoProvider;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: renamed from: kc.a */
/* JADX INFO: compiled from: AbstractPostPreviewView.java */
/* JADX INFO: loaded from: classes3.dex */
public abstract class AbstractPostPreviewView extends CustomImageView {
    int columnWidth;
    int calculatedHeight;
    boolean sharing;

    boolean commentMode;
    Post post;

    public AbstractPostPreviewView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.columnWidth = getColumnWidth();
    }

    private Post getPost() {
        return this.post;
    }

    private void calculateLayout() {
        float targetHeight;
        ImageView.ScaleType scaleType;
        boolean cropped = (this.post.getPreviewHeight() == 0 || this.post.getPreviewWidth() == 0 || (this.post.shouldBlurNsfw() && !this.commentMode) || this.post.isPreviewInfographic() || LinkTypeHelper.isYouTube(this.post.getUrl())) ? true : !isFullHeight();
        if (this.commentMode && SettingsSingleton.getSettings().commentMediaCropped) {
            cropped = true;
        }
        if (cropped || this.post.getSyncType() == 9) {
            targetHeight = isTabletLayout() ? isLargeTablet() ? SizeHelper.dpToPx(260) : SizeHelper.dpToPx(HttpStatusCodesKt.HTTP_MULT_CHOICE) : getDefaultHeight();
            setScaleType(ImageView.ScaleType.CENTER_CROP);
            scaleType = ImageView.ScaleType.CENTER_CROP;
        } else {
            targetHeight = ((float) this.columnWidth / this.post.getPreviewWidth()) * this.post.getPreviewHeight();
            if (!SettingsSingleton.getSettings().fixedHeightVideo) {
                setScaleType(ImageView.ScaleType.FIT_CENTER);
                scaleType = ImageView.ScaleType.FIT_CENTER;
            } else if (this.post.getSyncType() == 4 && targetHeight > SizeHelper.dpToPx(350)) {
                targetHeight = SizeHelper.dpToPx(350);
                setScaleType(ImageView.ScaleType.CENTER_CROP);
                scaleType = ImageView.ScaleType.CENTER_CROP;
            } else if (this.post.getSyncType() != 7 || targetHeight <= SizeHelper.dpToPx(350)) {
                setScaleType(ImageView.ScaleType.FIT_CENTER);
                scaleType = ImageView.ScaleType.FIT_CENTER;
            } else {
                targetHeight = SizeHelper.dpToPx(350);
                setScaleType(ImageView.ScaleType.CENTER_CROP);
                scaleType = ImageView.ScaleType.CENTER_CROP;
            }
        }
        ImageView.ScaleType scaleType2 = scaleType;
        Logger.verbose("Calculated: " + targetHeight);
        if (targetHeight % 1.0f != 0.0f) {
            targetHeight += 2.0f;
        }
        int height = (int) targetHeight;
        Logger.verbose("Calculated 2: " + height);
        getLayoutParams().height = height;
        load(this.post.getPreviewUrl(), this.post.isVisitedOrInternal(), this.post.shouldBlurNsfw() && !this.commentMode, this.columnWidth, height, scaleType2, true);
        this.calculatedHeight = height;
    }

    public boolean canAutoplay(Context context) {
        return canAutoplay(context, true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean canAutoplay(Context context, boolean checkIntersection) {
        if (this.post == null) {
            Logger.debug("no posts");
            return false;
        }
        if (getPost().getSyncType() != 7 && getPost().getSyncType() != 4) {
            Logger.debug("not a video");
            return false;
        }
        if (SettingsSingleton.getSettings().autoplay == 2) {
            Logger.debug("autoplay settings wrong");
            return false;
        }
        if (SettingsSingleton.getSettings().autoplay == 1 && !ConnectivityHelper.isWifiConnected()) {
            Logger.debug("autoplay settings wrong");
            return false;
        }
        if (!isFullHeight()) {
            Logger.debug("not full height");
            return false;
        }
        if (getPost().shouldBlurNsfw()) {
            Logger.debug("nsfw");
            return false;
        }
        if (getPost().isSpoiler()) {
            Logger.debug("was spoiler");
            return false;
        }
        if (isSharing()) {
            Logger.debug("was sharing");
            return false;
        }
        if (!isAttachedToWindow()) {
            Logger.debug("wasnt attached to window");
            return false;
        }
        if (!isCommentMode()) {
            if (!isShown()) {
                Logger.debug("wasnt shown");
                return false;
            }
            if (checkIntersection && (context instanceof VideoProvider) && !((VideoProvider) context).getVideoHelperView().intersects(this)) {
                return false;
            }
        }
        return StringUtils.isNotEmpty(getPost().getVideoPreview());
    }

    protected int getColumnWidth() {
        throw new RuntimeException("TODO");
    }

    protected int getDefaultHeight() {
        return SizeHelper.dpToPx(220);
    }

    public int getCalculatedHeight() {
        return this.calculatedHeight;
    }

    protected boolean isCommentMode() {
        return this.commentMode;
    }

    protected boolean isTablet() {
        return TabletHelper.isDualPane();
    }

    protected boolean isFullHeight() {
        throw new RuntimeException("TODO");
    }

    protected boolean isLargeTablet() {
        return TabletHelper.isLandscape();
    }

    protected boolean isPreferencesPreview() {
        return getContext() instanceof PreferencesActivity;
    }

    protected boolean isSharing() {
        return this.sharing;
    }

    protected boolean isTabletLayout() {
        return TabletHelper.isTabletMode();
    }

    public void applyCommentMode() {
        this.commentMode = true;
        setNsfw(true);
        this.columnWidth = getColumnWidth();
        disableCrossFade();
    }

    public void setPost(Post post) {
        this.post = post;
        calculateLayout();
    }

    public void markSharing() {
        this.sharing = true;
    }
}
