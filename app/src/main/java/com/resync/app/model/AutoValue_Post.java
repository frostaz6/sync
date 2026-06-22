package com.resync.app.model;

import android.content.ContentValues;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: renamed from: xa.c */
/* JADX INFO: compiled from: AutoValue_Post.java */
/* JADX INFO: loaded from: classes.dex */
public final class AutoValue_Post extends $AutoValue_Post {
    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_Post(String id, String linkId, String parentId, int hash, int kind, int syncType, int likes, int numReports, int numComments, int gilded, int silvered, int platinumed, int score, int level, int controversiality, int newCommentCount, int viewCount, int awardCount, String subreddit, String subredditId, String author, String timestamp, String editedTimestamp, String title, String distinguished, String approvedBy, String bannedBy, String linkFlairText, String linkFlairCssClass, String linkFlairBackgroundColor, String linkFlairId, String authorFlairText, String authorFlairCssClass, String domain, String domainRaw, String suggestedSort, String upvoteRatio, String userReports, String authorFlairRichtextT, String authorFlairRichtextU, String postCrossLink, String postCrossOrigin, String postAwardString, String postAuthorProfilePic, String postFavIcon, String postAuthorFullname, String postCreatedUtc, String permalink, boolean contestMode, boolean locked, boolean stickied, boolean saved, boolean archived, boolean visited, boolean internallyVisited, boolean hidden, boolean mod, boolean over18, boolean edited, boolean scoreHidden, boolean newComment, boolean spoiler, boolean authorCakeday, boolean isOriginalContent, boolean hasAwards, boolean accountNew, boolean sendReplies, String childIds, int childCount, boolean collapsed, boolean commentFiltered, String commentMoreIds, int commentMoreCount, String url, String rawUrl, String thumbnail, String previewUrl, int previewWidth, int previewHeight, boolean previewInfographic, int previewDuration, String videoPreview, String selftextRaw, String selftextProcessed, String selftextProcessedPreview, String embedded, String embeddedAuthor, String embeddedAuthorHandle, String bodyRaw, String bodyProcessed, int listsTime, String age, String listsId) {
        super(id, linkId, parentId, hash, kind, syncType, likes, numReports, numComments, gilded, silvered, platinumed, score, level, controversiality, newCommentCount, viewCount, awardCount, subreddit, subredditId, author, timestamp, editedTimestamp, title, distinguished, approvedBy, bannedBy, linkFlairText, linkFlairCssClass, linkFlairBackgroundColor, linkFlairId, authorFlairText, authorFlairCssClass, domain, domainRaw, suggestedSort, upvoteRatio, userReports, authorFlairRichtextT, authorFlairRichtextU, postCrossLink, postCrossOrigin, postAwardString, postAuthorProfilePic, postFavIcon, postAuthorFullname, postCreatedUtc, permalink, contestMode, locked, stickied, saved, archived, visited, internallyVisited, hidden, mod, over18, edited, scoreHidden, newComment, spoiler, authorCakeday, isOriginalContent, hasAwards, accountNew, sendReplies, childIds, childCount, collapsed, commentFiltered, commentMoreIds, commentMoreCount, url, rawUrl, thumbnail, previewUrl, previewWidth, previewHeight, previewInfographic, previewDuration, videoPreview, selftextRaw, selftextProcessed, selftextProcessedPreview, embedded, embeddedAuthor, embeddedAuthorHandle, bodyRaw, bodyProcessed, listsTime, age, listsId);
    }

    @Override // com.resync.app.model.Post
    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues(93);
        contentValues.put("_id", getId());
        contentValues.put("link_id", getLinkId());
        contentValues.put("parent_id", getParentId());
        contentValues.put("hash", Integer.valueOf(getHash()));
        contentValues.put("kind", Integer.valueOf(getKind()));
        contentValues.put("sync_type", Integer.valueOf(getSyncType()));
        contentValues.put("likes", Integer.valueOf(getLikes()));
        contentValues.put("num_reports", Integer.valueOf(getNumReports()));
        contentValues.put("num_comments", Integer.valueOf(getNumComments()));
        contentValues.put("gilded", Integer.valueOf(getGilded()));
        contentValues.put("silvered", Integer.valueOf(getSilvered()));
        contentValues.put("platinumed", Integer.valueOf(getPlatinumed()));
        contentValues.put("score", Integer.valueOf(getScore()));
        contentValues.put("level", Integer.valueOf(getLevel()));
        contentValues.put("controversiality", Integer.valueOf(getControversiality()));
        contentValues.put("new_comment_count", Integer.valueOf(getNewCommentCount()));
        contentValues.put("view_count", Integer.valueOf(getViewCount()));
        contentValues.put("award_count", Integer.valueOf(getAwardCount()));
        contentValues.put("subreddit", getSubreddit());
        contentValues.put("subreddit_id", getSubredditId());
        contentValues.put("author", getAuthor());
        contentValues.put("timestamp", getTimestamp());
        contentValues.put("edited_timestamp", getEditedTimestamp());
        contentValues.put("title", getTitle());
        contentValues.put("distinguished", getDistinguished());
        contentValues.put("approved_by", getApprovedBy());
        contentValues.put("banned_by", getBannedBy());
        contentValues.put("link_flair_text", getLinkFlairText());
        contentValues.put("link_flair_css_class", getLinkFlairCssClass());
        contentValues.put("link_flair_background_color", getLinkFlairBackgroundColor());
        contentValues.put("link_flair_id", getLinkFlairId());
        contentValues.put("author_flair_text", getAuthorFlairText());
        contentValues.put("author_flair_css_class", getAuthorFlairCssClass());
        contentValues.put("domain", getDomain());
        contentValues.put("domain_raw", getDomainRaw());
        contentValues.put("suggested_sort", getSuggestedSort());
        contentValues.put("upvote_ratio", getUpvoteRatio());
        contentValues.put("user_reports", getUserReports());
        contentValues.put("author_flair_richtext_t", getAuthorFlairRichtextT());
        contentValues.put("author_flair_richtext_u", getAuthorFlairRichtextU());
        contentValues.put("post_cross_link", getPostCrossLink());
        contentValues.put("post_cross_origin", getPostCrossOrigin());
        contentValues.put("post_award_string", getPostAwardString());
        contentValues.put("post_author_profile_pic", getPostAuthorProfilePic());
        contentValues.put("post_fav_icon", getPostFavIcon());
        contentValues.put("post_author_fullname", getPostAuthorFullname());
        contentValues.put("post_created_utc", getPostCreatedUtc());
        contentValues.put("permalink", getPermalink());
        contentValues.put("contest_mode", Boolean.valueOf(isContestMode()));
        contentValues.put("locked", Boolean.valueOf(isLocked()));
        contentValues.put("stickied", Boolean.valueOf(isStickied()));
        contentValues.put("saved", Boolean.valueOf(isSaved()));
        contentValues.put("archived", Boolean.valueOf(isArchived()));
        contentValues.put("visited", Boolean.valueOf(isVisited()));
        contentValues.put("internally_visited", Boolean.valueOf(isInternallyVisited()));
        contentValues.put("hidden", Boolean.valueOf(isHidden()));
        contentValues.put("mod", Boolean.valueOf(isMod()));
        contentValues.put("over_18", Boolean.valueOf(isOver18()));
        contentValues.put("edited", Boolean.valueOf(isEdited()));
        contentValues.put("score_hidden", Boolean.valueOf(isScoreHidden()));
        contentValues.put("new_comment", Boolean.valueOf(isNewComment()));
        contentValues.put("spoiler", Boolean.valueOf(isSpoiler()));
        contentValues.put("author_cakeday", Boolean.valueOf(isAuthorCakeday()));
        contentValues.put("is_original_content", Boolean.valueOf(isOriginalContent()));
        contentValues.put("has_awards", Boolean.valueOf(hasAwards()));
        contentValues.put("account_new", Boolean.valueOf(isAccountNew()));
        contentValues.put("send_replies", Boolean.valueOf(isSendReplies()));
        contentValues.put("childIds", getChildIds());
        contentValues.put("childCount", Integer.valueOf(getChildCount()));
        contentValues.put("collapsed", Boolean.valueOf(isCollapsed()));
        contentValues.put("comment_filtered", Boolean.valueOf(isCommentFiltered()));
        contentValues.put("commentMoreIds", getCommentMoreIds());
        contentValues.put("commentMoreCount", Integer.valueOf(getCommentMoreCount()));
        contentValues.put("url", getUrl());
        contentValues.put("raw_url", getRawUrl());
        contentValues.put("thumbnail", getThumbnail());
        contentValues.put("preview_url", getPreviewUrl());
        contentValues.put("preview_width", Integer.valueOf(getPreviewWidth()));
        contentValues.put("preview_height", Integer.valueOf(getPreviewHeight()));
        contentValues.put("preview_infographic", Boolean.valueOf(isPreviewInfographic()));
        contentValues.put("preview_duration", Integer.valueOf(getPreviewDuration()));
        contentValues.put("video_preview", getVideoPreview());
        contentValues.put("selftext_raw", getSelftextRaw());
        contentValues.put("selftext_processed", getSelftextProcessed());
        contentValues.put("selftext_processed_preview", getSelftextProcessedPreview());
        contentValues.put("embedded", getEmbedded());
        contentValues.put("embedded_author", getEmbeddedAuthor());
        contentValues.put("embedded_author_handle", getEmbeddedAuthorHandle());
        contentValues.put("body_raw", getBodyRaw());
        contentValues.put("body_processed", getBodyProcessed());
        contentValues.put("lists_time", Integer.valueOf(getListsTime()));
        contentValues.put("age", getAge());
        contentValues.put("lists_id", getListsId());
        return contentValues;
    }
}
