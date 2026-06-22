package com.resync.app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.bumptech.glide.RedditHelper;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.premium.UserPaintManager;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.media.images.PostThumbnailHelper;
import com.resync.app.managers.NewUserTagManager;
import com.resync.app.model.Message;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.dialogs.SharePostDialogBottomSheet;
import com.resync.app.helpers.DataSavingHelper;
import com.resync.app.util.TextUtils;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: renamed from: xa.d */
/* JADX INFO: compiled from: Post.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class Post implements Serializable {

    public int userTagColor = 0;
    public String userTag = null;

    public static Post createForUrl(String str, String str2, String str3) {
        return new AutoValue_Post(str, str, null, 0, 1, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, str2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, null, 0, false, false, null, 0, str3, null, null, null, 0, 0, false, 0, null, null, null, null, null, null, null, null, null, 0, null, null);
    }

    public static Post createExampleImagePost() {
        return new AutoValue_Post("id", null, null, 0, 3, 1, 1, 0, 1234, 0, 0, 0, 9999, 0, 0, 0, 0, 0, "example", "t5_2tvpt", "example_user", "1 hour ago", null, "Example post", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, null, 0, false, false, null, 0, "https://i.imgur.com/RfhJhfx.jpg", "https://i.imgur.com/RfhJhfx.jpg", "https://i.imgur.com/RfhJhfx.jpg", "https://i.imgur.com/RfhJhfx.jpg", 1500, 1000, false, 0, null, null, null, null, null, null, null, null, null, 0, null, null);
    }

    public static Post createExampleCommentPost() {
        return new AutoValue_Post("id", null, null, 0, 1, 11, 1, 0, 0, 0, 0, 0, 1234, 0, 0, 0, 0, 0, "example", "t5_2tvpt", "example_user", "1 hour ago", null, "Example post", null, null, null, null, null, null, null, null, "s1 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, null, 0, false, false, null, 0, null, null, null, null, 0, 0, false, 0, null, null, null, null, null, null, null, null, "An example comment!<br/><br/>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc elementum quis turpis sit amet finibus. Mauris auctor fringilla imperdiet. Nam fringilla sapien congue lacus porttitor bibendum. Ut imperdiet turpis et purus sodales iaculis. Suspendisse vestibulum lorem mi, in auctor mauris commodo nec. Nulla egestas tincidunt eros. Nullam quis eros iaculis, pulvinar erat ut, pretium tellus. Ut volutpat lacinia diam nec malesuada.", 0, null, null);
    }

    public static Post fromMessage(Message message, int kind) {
        return new AutoValue_Post(message.id, null, null, 0, kind, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, null, 0, false, false, null, 0, null, null, null, null, 0, 0, false, 0, null, null, null, null, null, null, null, message.bodyRaw, message.bodyProcessed, 0, null, null);
    }

    public static Post createForImageUrl(String url) {
        return new AutoValue_Post("id", null, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, null, 0, false, false, null, 0, url, null, null, url, 0, 0, false, 0, null, null, null, null, null, null, null, null, null, 0, null, null);
    }

    public static Post createForLink(String url) {
        return new AutoValue_Post("id", url, null, 0, 1, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, null, 0, false, false, null, 0, url, null, null, null, 0, 0, false, 0, null, null, null, null, null, null, null, null, null, 0, null, null);
    }

    public static Post createExampleGooglePhotosPost() {
        return new AutoValue_Post("id", null, null, 0, 3, 1, 0, 0, 1234, 0, 0, 0, 32000, 0, 0, 0, 0, 0, "example", "t5_2tvpt", "author", "1 hour ago", null, "Example post", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, null, 0, false, false, null, 0, "https://lh3.googleusercontent.com/40vgb6QD0kgZPDynfVkxe2wF_DBqkP33eTAp_3KdVSUHRhHSDn5oOytrqzsJPva3gVGPCzDyWpTYLqCHl-lYQPq-FnVuPHHwaRzw8wT-tRPss60lzLMI=w1200-rj", "https://lh3.googleusercontent.com/40vgb6QD0kgZPDynfVkxe2wF_DBqkP33eTAp_3KdVSUHRhHSDn5oOytrqzsJPva3gVGPCzDyWpTYLqCHl-lYQPq-FnVuPHHwaRzw8wT-tRPss60lzLMI=w1200-rj", "https://lh3.googleusercontent.com/40vgb6QD0kgZPDynfVkxe2wF_DBqkP33eTAp_3KdVSUHRhHSDn5oOytrqzsJPva3gVGPCzDyWpTYLqCHl-lYQPq-FnVuPHHwaRzw8wT-tRPss60lzLMI=w1200-rj", "https://lh3.googleusercontent.com/40vgb6QD0kgZPDynfVkxe2wF_DBqkP33eTAp_3KdVSUHRhHSDn5oOytrqzsJPva3gVGPCzDyWpTYLqCHl-lYQPq-FnVuPHHwaRzw8wT-tRPss60lzLMI=w1200-rj", 1500, 1000, false, 0, null, null, null, null, null, null, null, null, null, 0, null, null);
    }

    public static Post fromCursor(Cursor cursor) {
        cursor.moveToFirst();
        return $AutoValue_Post.readFromCursor(cursor).applyUserTag();
    }

    public static Post fromCursorPosition(Cursor cursor, int position) {
        cursor.moveToPosition(position);
        return $AutoValue_Post.readFromCursor(cursor).applyUserTag();
    }

    public abstract String getPostCreatedUtc();

    public abstract String getPostCrossLink();

    public abstract String getPostCrossOrigin();

    public abstract String getPostFavIcon();

    public abstract int getPreviewDuration();

    public abstract int getPreviewHeight();

    public abstract boolean isPreviewInfographic();

    public abstract String getDistinguished();

    public abstract String getPreviewUrl();

    public abstract String getDomain();

    public abstract int getPreviewWidth();

    public abstract String getDomainRaw();

    public abstract String getRawUrl();

    public abstract boolean isEdited();

    public abstract boolean isSaved();

    public abstract String getEditedTimestamp();

    public abstract int getScore();

    public abstract String getEmbedded();

    public abstract boolean isScoreHidden();

    public abstract String getEmbeddedAuthor();

    public abstract String getSelftextProcessed();

    public abstract String getEmbeddedAuthorHandle();

    public abstract String getSelftextProcessedPreview();

    public String getThumbnailPlaceholder(Context context) {
        if (shouldBlurNsfw()) {
            return PostThumbnailHelper.PLAY_NSFW;
        }
        if (isSpoiler()) {
            return PostThumbnailHelper.PLAY_SPOILER;
        }
        if (getSyncType() == 2) {
            return null;
        }
        if (!DataSavingHelper.isFullDataSaving()) {
            if (!TextUtils.isEmpty(getThumbnail())) {
                return getThumbnail();
            }
            if (!TextUtils.isEmpty(getPreviewUrl())) {
                return getPreviewUrl();
            }
        }
        return getPostTypeThumbnailPlaceholder();
    }

    private String getPostTypeThumbnailPlaceholder() {
        String url = getUrl();
        if (getSyncType() == 1 && LinkTypeHelper.isStreamable(url)) {
            return PostThumbnailHelper.PLAY_STREAMABLE;
        }
        if (getSyncType() == 1 && LinkTypeHelper.isYouTube(url)) {
            return PostThumbnailHelper.PLAY_VIDEO;
        }
        if (getSyncType() == 1 && !TextUtils.isEmpty(url) && url.contains("v.redd.it")) {
            return PostThumbnailHelper.PLAY_REDDIT;
        }
        if (getSyncType() == 1 && LinkTypeHelper.isGfycat(url)) {
            return PostThumbnailHelper.PLAY_GFYCAT;
        }
        if (getSyncType() == 1 && LinkTypeHelper.isGiphy(url)) {
            return PostThumbnailHelper.PLAY_GIPHY;
        }
        if (getSyncType() == 7) {
            return PostThumbnailHelper.PLAY_GIF;
        }
        if (getSyncType() == 10 || getSyncType() == 6) {
            return PostThumbnailHelper.PLAY_ALBUM;
        }
        if (getSyncType() == 9 || getSyncType() == 3) {
            return PostThumbnailHelper.PLAY_EXTERNAL;
        }
        if (getSyncType() == 1) {
            return PostThumbnailHelper.PLAY_IMAGE;
        }
        if (getSyncType() == 2) {
            return PostThumbnailHelper.PLAY_TEXT;
        }
        return getThumbnail();
    }

    public abstract String getSelftextRaw();

    public abstract int getGilded();

    public abstract boolean isSendReplies();

    public abstract boolean hasAwards();

    public void share(Context context, boolean z) {
        if (z) {
            FragmentUtils.showWithArguments(SharePostDialogBottomSheet.class, ContextHelper.getFragmentManager(context), SharePostDialogBottomSheet.buildArguments(this));
            return;
        }
        RedditHelper.sharePostLink(context, SettingsSingleton.getSettings().imageShareTitle ? getTitle() : null, "https://www.reddit.com/r/" + getSubreddit() + "/comments/" + getId() + "/_/");
    }

    public abstract int getHash();

    public abstract int getSilvered();

    public abstract boolean isHidden();

    public abstract boolean isSpoiler();

    public abstract String getId();

    public abstract boolean isStickied();

    public abstract boolean isInternallyVisited();

    public abstract String getSubreddit();

    public boolean isDownvoted() {
        return getLikes() == -1;
    }

    public abstract String getSubredditId();

    public boolean hasSameUserTag(Post post) {
        return this.userTagColor == post.userTagColor && StringUtils.equals(this.userTag, post.userTag);
    }

    public abstract String getSuggestedSort();

    public boolean shouldBlurNsfw() {
        if (!isOver18()) {
            return false;
        }
        if (SettingsSingleton.getSettings().nsfwPreviews) {
            return true;
        }
        if ((StringUtils.equalsIgnoreCase("popular", getListsId()) || StringUtils.equalsIgnoreCase("frontpage", getListsId()) || StringUtils.equalsIgnoreCase("all", getListsId())) && SettingsSingleton.getSettings().selectiveNsfwPreviews) {
            return true;
        }
        return isOver18() && SettingsSingleton.getInstance().getCurrentSettings().nsfwPreviews;
    }

    public abstract int getSyncType();

    public boolean isUpvoted() {
        return getLikes() == 1;
    }

    public abstract String getThumbnail();

    public abstract boolean isAccountNew();

    public boolean isVisitedOrInternal() {
        return isVisited() || isInternallyVisited();
    }

    public abstract String getTimestamp();

    public abstract String getAge();

    public abstract boolean isOriginalContent();

    public abstract String getTitle();

    public abstract String getApprovedBy();

    public abstract int getKind();

    public abstract ContentValues toContentValues();

    public abstract boolean isArchived();

    public abstract int getLevel();

    public abstract String getUpvoteRatio();

    public abstract String getAuthor();

    public abstract int getLikes();

    public abstract String getUrl();

    public abstract boolean isAuthorCakeday();

    public abstract String getLinkFlairBackgroundColor();

    public abstract String getUserReports();

    public abstract String getAuthorFlairCssClass();

    public abstract String getLinkFlairCssClass();

    public abstract String getVideoPreview();

    public abstract String getAuthorFlairRichtextT();

    public abstract String getLinkFlairId();

    public abstract int getViewCount();

    public abstract String getAuthorFlairRichtextU();

    public abstract String getLinkFlairText();

    public abstract boolean isVisited();

    public abstract String getLinkId();

    public abstract String getAuthorFlairText();

    public abstract String getListsId();

    public abstract int getAwardCount();

    public abstract int getListsTime();

    public abstract String getBannedBy();

    public abstract boolean isLocked();

    public abstract String getBodyProcessed();

    public abstract boolean isMod();

    public abstract String getBodyRaw();

    public abstract boolean isNewComment();

    public abstract int getChildCount();

    public abstract int getNewCommentCount();

    public abstract String getChildIds();

    public abstract int getNumComments();

    public abstract int getNumReports();

    public abstract boolean isCollapsed();

    public abstract boolean isOver18();

    public abstract int getCommentMoreCount();

    public abstract String getParentId();

    public abstract String getCommentMoreIds();

    public abstract String getPermalink();

    public abstract boolean isCommentFiltered();

    public abstract int getPlatinumed();

    public abstract boolean isContestMode();

    public Post applyUserTag() {
        this.userTagColor = UserPaintManager.getInstance().getColor(getAuthor()).intValue();
        this.userTag = NewUserTagManager.getInstance().getTag(getAuthor());
        return this;
    }

    public abstract int getControversiality();

    public abstract String getPostAuthorFullname();

    public abstract String getPostAuthorProfilePic();

    public abstract String getPostAwardString();
}
