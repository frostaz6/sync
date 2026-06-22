package com.resync.app.model;

import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: $$AutoValue_Post.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class $$AutoValue_Post extends Post {
    private final int platinumed;
    private final boolean isOriginalContent;
    private final int score;
    private final boolean hasAwards;
    private final int level;
    private final boolean accountNew;
    private final int controversiality;
    private final boolean sendReplies;
    private final int newCommentCount;
    private final String childIds;
    private final int viewCount;
    private final int childCount;
    private final int awardCount;
    private final boolean collapsed;
    private final String subreddit;
    private final boolean commentFiltered;
    private final String subredditId;
    private final String commentMoreIds;
    private final String author;
    private final int commentMoreCount;
    private final String timestamp;
    private final String url;
    private final String editedTimestamp;
    private final String rawUrl;
    private final String title;
    private final String thumbnail;
    private final String distinguished;
    private final String previewUrl;
    private final String approvedBy;
    private final int previewWidth;
    private final String bannedBy;
    private final int previewHeight;
    private final String linkFlairText;
    private final boolean previewInfographic;
    private final String linkFlairCssClass;
    private final int previewDuration;
    private final String linkFlairBackgroundColor;
    private final String videoPreview;
    private final String linkFlairId;
    private final String selftextRaw;
    private final String authorFlairText;
    private final String selftextProcessed;
    private final String authorFlairCssClass;
    private final String selftextProcessedPreview;
    private final String domain;
    private final String embedded;
    private final String domainRaw;
    private final String embeddedAuthor;
    private final String suggestedSort;
    private final String embeddedAuthorHandle;
    private final String upvoteRatio;
    private final String bodyRaw;
    private final String userReports;
    private final String bodyProcessed;
    private final String authorFlairRichtextT;
    private final int listsTime;
    private final String id;

    private final String authorFlairRichtextU;

    private final String age;

    private final String postCrossLink;
    private final String listsId;
    private final String postCrossOrigin;

    private final String postAwardString;

    private final String postAuthorProfilePic;
    private final String postFavIcon;
    private final String postAuthorFullname;

    private final String postCreatedUtc;
    private final String permalink;
    private final boolean contestMode;

    private final boolean locked;
    private final boolean stickied;
    private final boolean saved;

    private final boolean archived;

    private final String linkId;
    private final boolean visited;

    private final String parentId;
    private final boolean internallyVisited;

    private final int hash;

    private final boolean hidden;
    private final int kind;
    private final boolean mod;

    private final int syncType;

    private final boolean over18;
    private final int likes;

    private final boolean edited;
    private final int numReports;
    private final boolean scoreHidden;
    private final int numComments;

    private final boolean newComment;

    private final int gilded;
    private final boolean spoiler;
    private final int silvered;
    private final boolean authorCakeday;

    $$AutoValue_Post(String id, String linkId, String parentId, int hash, int kind, int syncType, int likes, int numReports, int numComments, int gilded, int silvered, int platinumed, int score, int level, int controversiality, int newCommentCount, int viewCount, int awardCount, String subreddit, String subredditId, String author, String timestamp, String editedTimestamp, String title, String distinguished, String approvedBy, String bannedBy, String linkFlairText, String linkFlairCssClass, String linkFlairBackgroundColor, String linkFlairId, String authorFlairText, String authorFlairCssClass, String domain, String domainRaw, String suggestedSort, String upvoteRatio, String userReports, String authorFlairRichtextT, String authorFlairRichtextU, String postCrossLink, String postCrossOrigin, String postAwardString, String postAuthorProfilePic, String postFavIcon, String postAuthorFullname, String postCreatedUtc, String permalink, boolean contestMode, boolean locked, boolean stickied, boolean saved, boolean archived, boolean visited, boolean internallyVisited, boolean hidden, boolean mod, boolean over18, boolean edited, boolean scoreHidden, boolean newComment, boolean spoiler, boolean authorCakeday, boolean isOriginalContent, boolean hasAwards, boolean accountNew, boolean sendReplies, String childIds, int childCount, boolean collapsed, boolean commentFiltered, String commentMoreIds, int commentMoreCount, String url, String rawUrl, String thumbnail, String previewUrl, int previewWidth, int previewHeight, boolean previewInfographic, int previewDuration, String videoPreview, String selftextRaw, String selftextProcessed, String selftextProcessedPreview, String embedded, String embeddedAuthor, String embeddedAuthorHandle, String bodyRaw, String bodyProcessed, int listsTime, String age, String listsId) {
        Objects.requireNonNull(id, "Null id");
        this.id = id;
        this.linkId = linkId;
        this.parentId = parentId;
        this.hash = hash;
        this.kind = kind;
        this.syncType = syncType;
        this.likes = likes;
        this.numReports = numReports;
        this.numComments = numComments;
        this.gilded = gilded;
        this.silvered = silvered;
        this.platinumed = platinumed;
        this.score = score;
        this.level = level;
        this.controversiality = controversiality;
        this.newCommentCount = newCommentCount;
        this.viewCount = viewCount;
        this.awardCount = awardCount;
        this.subreddit = subreddit;
        this.subredditId = subredditId;
        this.author = author;
        this.timestamp = timestamp;
        this.editedTimestamp = editedTimestamp;
        this.title = title;
        this.distinguished = distinguished;
        this.approvedBy = approvedBy;
        this.bannedBy = bannedBy;
        this.linkFlairText = linkFlairText;
        this.linkFlairCssClass = linkFlairCssClass;
        this.linkFlairBackgroundColor = linkFlairBackgroundColor;
        this.linkFlairId = linkFlairId;
        this.authorFlairText = authorFlairText;
        this.authorFlairCssClass = authorFlairCssClass;
        this.domain = domain;
        this.domainRaw = domainRaw;
        this.suggestedSort = suggestedSort;
        this.upvoteRatio = upvoteRatio;
        this.userReports = userReports;
        this.authorFlairRichtextT = authorFlairRichtextT;
        this.authorFlairRichtextU = authorFlairRichtextU;
        this.postCrossLink = postCrossLink;
        this.postCrossOrigin = postCrossOrigin;
        this.postAwardString = postAwardString;
        this.postAuthorProfilePic = postAuthorProfilePic;
        this.postFavIcon = postFavIcon;
        this.postAuthorFullname = postAuthorFullname;
        this.postCreatedUtc = postCreatedUtc;
        this.permalink = permalink;
        this.contestMode = contestMode;
        this.locked = locked;
        this.stickied = stickied;
        this.saved = saved;
        this.archived = archived;
        this.visited = visited;
        this.internallyVisited = internallyVisited;
        this.hidden = hidden;
        this.mod = mod;
        this.over18 = over18;
        this.edited = edited;
        this.scoreHidden = scoreHidden;
        this.newComment = newComment;
        this.spoiler = spoiler;
        this.authorCakeday = authorCakeday;
        this.isOriginalContent = isOriginalContent;
        this.hasAwards = hasAwards;
        this.accountNew = accountNew;
        this.sendReplies = sendReplies;
        this.childIds = childIds;
        this.childCount = childCount;
        this.collapsed = collapsed;
        this.commentFiltered = commentFiltered;
        this.commentMoreIds = commentMoreIds;
        this.commentMoreCount = commentMoreCount;
        this.url = url;
        this.rawUrl = rawUrl;
        this.thumbnail = thumbnail;
        this.previewUrl = previewUrl;
        this.previewWidth = previewWidth;
        this.previewHeight = previewHeight;
        this.previewInfographic = previewInfographic;
        this.previewDuration = previewDuration;
        this.videoPreview = videoPreview;
        this.selftextRaw = selftextRaw;
        this.selftextProcessed = selftextProcessed;
        this.selftextProcessedPreview = selftextProcessedPreview;
        this.embedded = embedded;
        this.embeddedAuthor = embeddedAuthor;
        this.embeddedAuthorHandle = embeddedAuthorHandle;
        this.bodyRaw = bodyRaw;
        this.bodyProcessed = bodyProcessed;
        this.listsTime = listsTime;
        this.age = age;
        this.listsId = listsId;
    }

    @Override // com.resync.app.model.Post
    public String getPostCreatedUtc() {
        return this.postCreatedUtc;
    }

    @Override // com.resync.app.model.Post
    public String getPostCrossLink() {
        return this.postCrossLink;
    }

    @Override // com.resync.app.model.Post
    public String getPostCrossOrigin() {
        return this.postCrossOrigin;
    }

    @Override // com.resync.app.model.Post
    public String getPostFavIcon() {
        return this.postFavIcon;
    }

    @Override // com.resync.app.model.Post
    public int getPreviewDuration() {
        return this.previewDuration;
    }

    @Override // com.resync.app.model.Post
    public int getPreviewHeight() {
        return this.previewHeight;
    }

    @Override // com.resync.app.model.Post
    public boolean isPreviewInfographic() {
        return this.previewInfographic;
    }

    @Override // com.resync.app.model.Post
    public String getDistinguished() {
        return this.distinguished;
    }

    @Override // com.resync.app.model.Post
    public String getPreviewUrl() {
        return this.previewUrl;
    }

    @Override // com.resync.app.model.Post
    public String getDomain() {
        return this.domain;
    }

    @Override // com.resync.app.model.Post
    public int getPreviewWidth() {
        return this.previewWidth;
    }

    @Override // com.resync.app.model.Post
    public String getDomainRaw() {
        return this.domainRaw;
    }

    @Override // com.resync.app.model.Post
    public String getRawUrl() {
        return this.rawUrl;
    }

    @Override // com.resync.app.model.Post
    public boolean isEdited() {
        return this.edited;
    }

    @Override // com.resync.app.model.Post
    public boolean isSaved() {
        return this.saved;
    }

    @Override // com.resync.app.model.Post
    public String getEditedTimestamp() {
        return this.editedTimestamp;
    }

    @Override // com.resync.app.model.Post
    public int getScore() {
        return this.score;
    }

    @Override // com.resync.app.model.Post
    public String getEmbedded() {
        return this.embedded;
    }

    @Override // com.resync.app.model.Post
    public boolean isScoreHidden() {
        return this.scoreHidden;
    }

    @Override // com.resync.app.model.Post
    public String getEmbeddedAuthor() {
        return this.embeddedAuthor;
    }

    @Override // com.resync.app.model.Post
    public String getSelftextProcessed() {
        return this.selftextProcessed;
    }

    @Override // com.resync.app.model.Post
    public String getEmbeddedAuthorHandle() {
        return this.embeddedAuthorHandle;
    }

    @Override // com.resync.app.model.Post
    public String getSelftextProcessedPreview() {
        return this.selftextProcessedPreview;
    }

    @Override // com.resync.app.model.Post
    public String getSelftextRaw() {
        return this.selftextRaw;
    }

    @Override // com.resync.app.model.Post
    public int getGilded() {
        return this.gilded;
    }

    @Override // com.resync.app.model.Post
    public boolean isSendReplies() {
        return this.sendReplies;
    }

    @Override // com.resync.app.model.Post
    public boolean hasAwards() {
        return this.hasAwards;
    }

    @Override // com.resync.app.model.Post
    public int getHash() {
        return this.hash;
    }

    @Override // com.resync.app.model.Post
    public int getSilvered() {
        return this.silvered;
    }

    @Override // com.resync.app.model.Post
    public boolean isHidden() {
        return this.hidden;
    }

    @Override // com.resync.app.model.Post
    public boolean isSpoiler() {
        return this.spoiler;
    }

    @Override // com.resync.app.model.Post
    public String getId() {
        return this.id;
    }

    @Override // com.resync.app.model.Post
    public boolean isStickied() {
        return this.stickied;
    }

    @Override // com.resync.app.model.Post
    public boolean isInternallyVisited() {
        return this.internallyVisited;
    }

    @Override // com.resync.app.model.Post
    public String getSubreddit() {
        return this.subreddit;
    }

    @Override // com.resync.app.model.Post
    public String getSubredditId() {
        return this.subredditId;
    }

    @Override // com.resync.app.model.Post
    public String getSuggestedSort() {
        return this.suggestedSort;
    }

    @Override // com.resync.app.model.Post
    public int getSyncType() {
        return this.syncType;
    }

    @Override // com.resync.app.model.Post
    public String getThumbnail() {
        return this.thumbnail;
    }

    @Override // com.resync.app.model.Post
    public boolean isAccountNew() {
        return this.accountNew;
    }

    @Override // com.resync.app.model.Post
    public String getTimestamp() {
        return this.timestamp;
    }

    @Override // com.resync.app.model.Post
    public String getAge() {
        return this.age;
    }

    @Override // com.resync.app.model.Post
    public boolean isOriginalContent() {
        return this.isOriginalContent;
    }

    @Override // com.resync.app.model.Post
    public String getTitle() {
        return this.title;
    }

    @Override // com.resync.app.model.Post
    public String getApprovedBy() {
        return this.approvedBy;
    }

    @Override // com.resync.app.model.Post
    public int getKind() {
        return this.kind;
    }

    @Override // com.resync.app.model.Post
    public boolean isArchived() {
        return this.archived;
    }

    @Override // com.resync.app.model.Post
    public int getLevel() {
        return this.level;
    }

    @Override // com.resync.app.model.Post
    public String getUpvoteRatio() {
        return this.upvoteRatio;
    }

    @Override // com.resync.app.model.Post
    public String getAuthor() {
        return this.author;
    }

    @Override // com.resync.app.model.Post
    public int getLikes() {
        return this.likes;
    }

    @Override // com.resync.app.model.Post
    public String getUrl() {
        return this.url;
    }

    public boolean equals(Object obj) {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String str10;
        String str11;
        String str12;
        String str13;
        String str14;
        String str15;
        String str16;
        String str17;
        String str18;
        String str19;
        String str20;
        String str21;
        String str22;
        String str23;
        String str24;
        String str25;
        String str26;
        String str27;
        String str28;
        String str29;
        String str30;
        String str31;
        String str32;
        String str33;
        String str34;
        String str35;
        String str36;
        String str37;
        String str38;
        String str39;
        String str40;
        String str41;
        String str42;
        String str43;
        String str44;
        String str45;
        String str46;
        String str47;
        String str48;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Post)) {
            return false;
        }
        Post post = (Post) obj;
        if (this.id.equals(post.getId()) && ((str = this.linkId) != null ? str.equals(post.getLinkId()) : post.getLinkId() == null) && ((str2 = this.parentId) != null ? str2.equals(post.getParentId()) : post.getParentId() == null) && this.hash == post.getHash() && this.kind == post.getKind() && this.syncType == post.getSyncType() && this.likes == post.getLikes() && this.numReports == post.getNumReports() && this.numComments == post.getNumComments() && this.gilded == post.getGilded() && this.silvered == post.getSilvered() && this.platinumed == post.getPlatinumed() && this.score == post.getScore() && this.level == post.getLevel() && this.controversiality == post.getControversiality() && this.newCommentCount == post.getNewCommentCount() && this.viewCount == post.getViewCount() && this.awardCount == post.getAwardCount() && ((str3 = this.subreddit) != null ? str3.equals(post.getSubreddit()) : post.getSubreddit() == null) && ((str4 = this.subredditId) != null ? str4.equals(post.getSubredditId()) : post.getSubredditId() == null) && ((str5 = this.author) != null ? str5.equals(post.getAuthor()) : post.getAuthor() == null) && ((str6 = this.timestamp) != null ? str6.equals(post.getTimestamp()) : post.getTimestamp() == null) && ((str7 = this.editedTimestamp) != null ? str7.equals(post.getEditedTimestamp()) : post.getEditedTimestamp() == null) && ((str8 = this.title) != null ? str8.equals(post.getTitle()) : post.getTitle() == null) && ((str9 = this.distinguished) != null ? str9.equals(post.getDistinguished()) : post.getDistinguished() == null) && ((str10 = this.approvedBy) != null ? str10.equals(post.getApprovedBy()) : post.getApprovedBy() == null) && ((str11 = this.bannedBy) != null ? str11.equals(post.getBannedBy()) : post.getBannedBy() == null) && ((str12 = this.linkFlairText) != null ? str12.equals(post.getLinkFlairText()) : post.getLinkFlairText() == null) && ((str13 = this.linkFlairCssClass) != null ? str13.equals(post.getLinkFlairCssClass()) : post.getLinkFlairCssClass() == null) && ((str14 = this.linkFlairBackgroundColor) != null ? str14.equals(post.getLinkFlairBackgroundColor()) : post.getLinkFlairBackgroundColor() == null) && ((str15 = this.linkFlairId) != null ? str15.equals(post.getLinkFlairId()) : post.getLinkFlairId() == null) && ((str16 = this.authorFlairText) != null ? str16.equals(post.getAuthorFlairText()) : post.getAuthorFlairText() == null) && ((str17 = this.authorFlairCssClass) != null ? str17.equals(post.getAuthorFlairCssClass()) : post.getAuthorFlairCssClass() == null) && ((str18 = this.domain) != null ? str18.equals(post.getDomain()) : post.getDomain() == null) && ((str19 = this.domainRaw) != null ? str19.equals(post.getDomainRaw()) : post.getDomainRaw() == null) && ((str20 = this.suggestedSort) != null ? str20.equals(post.getSuggestedSort()) : post.getSuggestedSort() == null) && ((str21 = this.upvoteRatio) != null ? str21.equals(post.getUpvoteRatio()) : post.getUpvoteRatio() == null) && ((str22 = this.userReports) != null ? str22.equals(post.getUserReports()) : post.getUserReports() == null) && ((str23 = this.authorFlairRichtextT) != null ? str23.equals(post.getAuthorFlairRichtextT()) : post.getAuthorFlairRichtextT() == null) && ((str24 = this.authorFlairRichtextU) != null ? str24.equals(post.getAuthorFlairRichtextU()) : post.getAuthorFlairRichtextU() == null) && ((str25 = this.postCrossLink) != null ? str25.equals(post.getPostCrossLink()) : post.getPostCrossLink() == null) && ((str26 = this.postCrossOrigin) != null ? str26.equals(post.getPostCrossOrigin()) : post.getPostCrossOrigin() == null) && ((str27 = this.postAwardString) != null ? str27.equals(post.getPostAwardString()) : post.getPostAwardString() == null) && ((str28 = this.postAuthorProfilePic) != null ? str28.equals(post.getPostAuthorProfilePic()) : post.getPostAuthorProfilePic() == null) && ((str29 = this.postFavIcon) != null ? str29.equals(post.getPostFavIcon()) : post.getPostFavIcon() == null) && ((str30 = this.postAuthorFullname) != null ? str30.equals(post.getPostAuthorFullname()) : post.getPostAuthorFullname() == null) && ((str31 = this.postCreatedUtc) != null ? str31.equals(post.getPostCreatedUtc()) : post.getPostCreatedUtc() == null) && ((str32 = this.permalink) != null ? str32.equals(post.getPermalink()) : post.getPermalink() == null) && this.contestMode == post.isContestMode() && this.locked == post.isLocked() && this.stickied == post.isStickied() && this.saved == post.isSaved() && this.archived == post.isArchived() && this.visited == post.isVisited() && this.internallyVisited == post.isInternallyVisited() && this.hidden == post.isHidden() && this.mod == post.isMod() && this.over18 == post.isOver18() && this.edited == post.isEdited() && this.scoreHidden == post.isScoreHidden() && this.newComment == post.isNewComment() && this.spoiler == post.isSpoiler() && this.authorCakeday == post.isAuthorCakeday() && this.isOriginalContent == post.isOriginalContent() && this.hasAwards == post.hasAwards() && this.accountNew == post.isAccountNew() && this.sendReplies == post.isSendReplies() && ((str33 = this.childIds) != null ? str33.equals(post.getChildIds()) : post.getChildIds() == null) && this.childCount == post.getChildCount() && this.collapsed == post.isCollapsed() && this.commentFiltered == post.isCommentFiltered() && ((str34 = this.commentMoreIds) != null ? str34.equals(post.getCommentMoreIds()) : post.getCommentMoreIds() == null) && this.commentMoreCount == post.getCommentMoreCount() && ((str35 = this.url) != null ? str35.equals(post.getUrl()) : post.getUrl() == null) && ((str36 = this.rawUrl) != null ? str36.equals(post.getRawUrl()) : post.getRawUrl() == null) && ((str37 = this.thumbnail) != null ? str37.equals(post.getThumbnail()) : post.getThumbnail() == null) && ((str38 = this.previewUrl) != null ? str38.equals(post.getPreviewUrl()) : post.getPreviewUrl() == null) && this.previewWidth == post.getPreviewWidth() && this.previewHeight == post.getPreviewHeight() && this.previewInfographic == post.isPreviewInfographic() && this.previewDuration == post.getPreviewDuration() && ((str39 = this.videoPreview) != null ? str39.equals(post.getVideoPreview()) : post.getVideoPreview() == null) && ((str40 = this.selftextRaw) != null ? str40.equals(post.getSelftextRaw()) : post.getSelftextRaw() == null) && ((str41 = this.selftextProcessed) != null ? str41.equals(post.getSelftextProcessed()) : post.getSelftextProcessed() == null) && ((str42 = this.selftextProcessedPreview) != null ? str42.equals(post.getSelftextProcessedPreview()) : post.getSelftextProcessedPreview() == null) && ((str43 = this.embedded) != null ? str43.equals(post.getEmbedded()) : post.getEmbedded() == null) && ((str44 = this.embeddedAuthor) != null ? str44.equals(post.getEmbeddedAuthor()) : post.getEmbeddedAuthor() == null) && ((str45 = this.embeddedAuthorHandle) != null ? str45.equals(post.getEmbeddedAuthorHandle()) : post.getEmbeddedAuthorHandle() == null) && ((str46 = this.bodyRaw) != null ? str46.equals(post.getBodyRaw()) : post.getBodyRaw() == null) && ((str47 = this.bodyProcessed) != null ? str47.equals(post.getBodyProcessed()) : post.getBodyProcessed() == null) && this.listsTime == post.getListsTime() && ((str48 = this.age) != null ? str48.equals(post.getAge()) : post.getAge() == null)) {
            String str49 = this.listsId;
            if (str49 == null) {
                if (post.getListsId() == null) {
                    return true;
                }
            } else if (str49.equals(post.getListsId())) {
                return true;
            }
        }
        return false;
    }

    @Override // com.resync.app.model.Post
    public boolean isAuthorCakeday() {
        return this.authorCakeday;
    }

    @Override // com.resync.app.model.Post
    public String getLinkFlairBackgroundColor() {
        return this.linkFlairBackgroundColor;
    }

    @Override // com.resync.app.model.Post
    public String getUserReports() {
        return this.userReports;
    }

    @Override // com.resync.app.model.Post
    public String getAuthorFlairCssClass() {
        return this.authorFlairCssClass;
    }

    @Override // com.resync.app.model.Post
    public String getLinkFlairCssClass() {
        return this.linkFlairCssClass;
    }

    @Override // com.resync.app.model.Post
    public String getVideoPreview() {
        return this.videoPreview;
    }

    @Override // com.resync.app.model.Post
    public String getAuthorFlairRichtextT() {
        return this.authorFlairRichtextT;
    }

    @Override // com.resync.app.model.Post
    public String getLinkFlairId() {
        return this.linkFlairId;
    }

    @Override // com.resync.app.model.Post
    public int getViewCount() {
        return this.viewCount;
    }

    public int hashCode() {
        int iHashCode = (this.id.hashCode() ^ 1000003) * 1000003;
        String str = this.linkId;
        int iHashCode2 = (iHashCode ^ (str == null ? 0 : str.hashCode())) * 1000003;
        String str2 = this.parentId;
        int iHashCode3 = (((((((((((((((((((((((((((((((iHashCode2 ^ (str2 == null ? 0 : str2.hashCode())) * 1000003) ^ this.hash) * 1000003) ^ this.kind) * 1000003) ^ this.syncType) * 1000003) ^ this.likes) * 1000003) ^ this.numReports) * 1000003) ^ this.numComments) * 1000003) ^ this.gilded) * 1000003) ^ this.silvered) * 1000003) ^ this.platinumed) * 1000003) ^ this.score) * 1000003) ^ this.level) * 1000003) ^ this.controversiality) * 1000003) ^ this.newCommentCount) * 1000003) ^ this.viewCount) * 1000003) ^ this.awardCount) * 1000003;
        String str3 = this.subreddit;
        int iHashCode4 = (iHashCode3 ^ (str3 == null ? 0 : str3.hashCode())) * 1000003;
        String str4 = this.subredditId;
        int iHashCode5 = (iHashCode4 ^ (str4 == null ? 0 : str4.hashCode())) * 1000003;
        String str5 = this.author;
        int iHashCode6 = (iHashCode5 ^ (str5 == null ? 0 : str5.hashCode())) * 1000003;
        String str6 = this.timestamp;
        int iHashCode7 = (iHashCode6 ^ (str6 == null ? 0 : str6.hashCode())) * 1000003;
        String str7 = this.editedTimestamp;
        int iHashCode8 = (iHashCode7 ^ (str7 == null ? 0 : str7.hashCode())) * 1000003;
        String str8 = this.title;
        int iHashCode9 = (iHashCode8 ^ (str8 == null ? 0 : str8.hashCode())) * 1000003;
        String str9 = this.distinguished;
        int iHashCode10 = (iHashCode9 ^ (str9 == null ? 0 : str9.hashCode())) * 1000003;
        String str10 = this.approvedBy;
        int iHashCode11 = (iHashCode10 ^ (str10 == null ? 0 : str10.hashCode())) * 1000003;
        String str11 = this.bannedBy;
        int iHashCode12 = (iHashCode11 ^ (str11 == null ? 0 : str11.hashCode())) * 1000003;
        String str12 = this.linkFlairText;
        int iHashCode13 = (iHashCode12 ^ (str12 == null ? 0 : str12.hashCode())) * 1000003;
        String str13 = this.linkFlairCssClass;
        int iHashCode14 = (iHashCode13 ^ (str13 == null ? 0 : str13.hashCode())) * 1000003;
        String str14 = this.linkFlairBackgroundColor;
        int iHashCode15 = (iHashCode14 ^ (str14 == null ? 0 : str14.hashCode())) * 1000003;
        String str15 = this.linkFlairId;
        int iHashCode16 = (iHashCode15 ^ (str15 == null ? 0 : str15.hashCode())) * 1000003;
        String str16 = this.authorFlairText;
        int iHashCode17 = (iHashCode16 ^ (str16 == null ? 0 : str16.hashCode())) * 1000003;
        String str17 = this.authorFlairCssClass;
        int iHashCode18 = (iHashCode17 ^ (str17 == null ? 0 : str17.hashCode())) * 1000003;
        String str18 = this.domain;
        int iHashCode19 = (iHashCode18 ^ (str18 == null ? 0 : str18.hashCode())) * 1000003;
        String str19 = this.domainRaw;
        int iHashCode20 = (iHashCode19 ^ (str19 == null ? 0 : str19.hashCode())) * 1000003;
        String str20 = this.suggestedSort;
        int iHashCode21 = (iHashCode20 ^ (str20 == null ? 0 : str20.hashCode())) * 1000003;
        String str21 = this.upvoteRatio;
        int iHashCode22 = (iHashCode21 ^ (str21 == null ? 0 : str21.hashCode())) * 1000003;
        String str22 = this.userReports;
        int iHashCode23 = (iHashCode22 ^ (str22 == null ? 0 : str22.hashCode())) * 1000003;
        String str23 = this.authorFlairRichtextT;
        int iHashCode24 = (iHashCode23 ^ (str23 == null ? 0 : str23.hashCode())) * 1000003;
        String str24 = this.authorFlairRichtextU;
        int iHashCode25 = (iHashCode24 ^ (str24 == null ? 0 : str24.hashCode())) * 1000003;
        String str25 = this.postCrossLink;
        int iHashCode26 = (iHashCode25 ^ (str25 == null ? 0 : str25.hashCode())) * 1000003;
        String str26 = this.postCrossOrigin;
        int iHashCode27 = (iHashCode26 ^ (str26 == null ? 0 : str26.hashCode())) * 1000003;
        String str27 = this.postAwardString;
        int iHashCode28 = (iHashCode27 ^ (str27 == null ? 0 : str27.hashCode())) * 1000003;
        String str28 = this.postAuthorProfilePic;
        int iHashCode29 = (iHashCode28 ^ (str28 == null ? 0 : str28.hashCode())) * 1000003;
        String str29 = this.postFavIcon;
        int iHashCode30 = (iHashCode29 ^ (str29 == null ? 0 : str29.hashCode())) * 1000003;
        String str30 = this.postAuthorFullname;
        int iHashCode31 = (iHashCode30 ^ (str30 == null ? 0 : str30.hashCode())) * 1000003;
        String str31 = this.postCreatedUtc;
        int iHashCode32 = (iHashCode31 ^ (str31 == null ? 0 : str31.hashCode())) * 1000003;
        String str32 = this.permalink;
        int iHashCode33 = (((((((((((((((((((((((((((((((((((((((iHashCode32 ^ (str32 == null ? 0 : str32.hashCode())) * 1000003) ^ (this.contestMode ? 1231 : 1237)) * 1000003) ^ (this.locked ? 1231 : 1237)) * 1000003) ^ (this.stickied ? 1231 : 1237)) * 1000003) ^ (this.saved ? 1231 : 1237)) * 1000003) ^ (this.archived ? 1231 : 1237)) * 1000003) ^ (this.visited ? 1231 : 1237)) * 1000003) ^ (this.internallyVisited ? 1231 : 1237)) * 1000003) ^ (this.hidden ? 1231 : 1237)) * 1000003) ^ (this.mod ? 1231 : 1237)) * 1000003) ^ (this.over18 ? 1231 : 1237)) * 1000003) ^ (this.edited ? 1231 : 1237)) * 1000003) ^ (this.scoreHidden ? 1231 : 1237)) * 1000003) ^ (this.newComment ? 1231 : 1237)) * 1000003) ^ (this.spoiler ? 1231 : 1237)) * 1000003) ^ (this.authorCakeday ? 1231 : 1237)) * 1000003) ^ (this.isOriginalContent ? 1231 : 1237)) * 1000003) ^ (this.hasAwards ? 1231 : 1237)) * 1000003) ^ (this.accountNew ? 1231 : 1237)) * 1000003) ^ (this.sendReplies ? 1231 : 1237)) * 1000003;
        String str33 = this.childIds;
        int iHashCode34 = (((((((iHashCode33 ^ (str33 == null ? 0 : str33.hashCode())) * 1000003) ^ this.childCount) * 1000003) ^ (this.collapsed ? 1231 : 1237)) * 1000003) ^ (this.commentFiltered ? 1231 : 1237)) * 1000003;
        String str34 = this.commentMoreIds;
        int iHashCode35 = (((iHashCode34 ^ (str34 == null ? 0 : str34.hashCode())) * 1000003) ^ this.commentMoreCount) * 1000003;
        String str35 = this.url;
        int iHashCode36 = (iHashCode35 ^ (str35 == null ? 0 : str35.hashCode())) * 1000003;
        String str36 = this.rawUrl;
        int iHashCode37 = (iHashCode36 ^ (str36 == null ? 0 : str36.hashCode())) * 1000003;
        String str37 = this.thumbnail;
        int iHashCode38 = (iHashCode37 ^ (str37 == null ? 0 : str37.hashCode())) * 1000003;
        String str38 = this.previewUrl;
        int iHashCode39 = (((((((((iHashCode38 ^ (str38 == null ? 0 : str38.hashCode())) * 1000003) ^ this.previewWidth) * 1000003) ^ this.previewHeight) * 1000003) ^ (this.previewInfographic ? 1231 : 1237)) * 1000003) ^ this.previewDuration) * 1000003;
        String str39 = this.videoPreview;
        int iHashCode40 = (iHashCode39 ^ (str39 == null ? 0 : str39.hashCode())) * 1000003;
        String str40 = this.selftextRaw;
        int iHashCode41 = (iHashCode40 ^ (str40 == null ? 0 : str40.hashCode())) * 1000003;
        String str41 = this.selftextProcessed;
        int iHashCode42 = (iHashCode41 ^ (str41 == null ? 0 : str41.hashCode())) * 1000003;
        String str42 = this.selftextProcessedPreview;
        int iHashCode43 = (iHashCode42 ^ (str42 == null ? 0 : str42.hashCode())) * 1000003;
        String str43 = this.embedded;
        int iHashCode44 = (iHashCode43 ^ (str43 == null ? 0 : str43.hashCode())) * 1000003;
        String str44 = this.embeddedAuthor;
        int iHashCode45 = (iHashCode44 ^ (str44 == null ? 0 : str44.hashCode())) * 1000003;
        String str45 = this.embeddedAuthorHandle;
        int iHashCode46 = (iHashCode45 ^ (str45 == null ? 0 : str45.hashCode())) * 1000003;
        String str46 = this.bodyRaw;
        int iHashCode47 = (iHashCode46 ^ (str46 == null ? 0 : str46.hashCode())) * 1000003;
        String str47 = this.bodyProcessed;
        int iHashCode48 = (((iHashCode47 ^ (str47 == null ? 0 : str47.hashCode())) * 1000003) ^ this.listsTime) * 1000003;
        String str48 = this.age;
        int iHashCode49 = (iHashCode48 ^ (str48 == null ? 0 : str48.hashCode())) * 1000003;
        String str49 = this.listsId;
        return iHashCode49 ^ (str49 != null ? str49.hashCode() : 0);
    }

    @Override // com.resync.app.model.Post
    public String getAuthorFlairRichtextU() {
        return this.authorFlairRichtextU;
    }

    @Override // com.resync.app.model.Post
    public String getLinkFlairText() {
        return this.linkFlairText;
    }

    @Override // com.resync.app.model.Post
    public boolean isVisited() {
        return this.visited;
    }

    @Override // com.resync.app.model.Post
    public String getLinkId() {
        return this.linkId;
    }

    @Override // com.resync.app.model.Post
    public String getAuthorFlairText() {
        return this.authorFlairText;
    }

    @Override // com.resync.app.model.Post
    public String getListsId() {
        return this.listsId;
    }

    @Override // com.resync.app.model.Post
    public int getAwardCount() {
        return this.awardCount;
    }

    @Override // com.resync.app.model.Post
    public int getListsTime() {
        return this.listsTime;
    }

    @Override // com.resync.app.model.Post
    public String getBannedBy() {
        return this.bannedBy;
    }

    @Override // com.resync.app.model.Post
    public boolean isLocked() {
        return this.locked;
    }

    @Override // com.resync.app.model.Post
    public String getBodyProcessed() {
        return this.bodyProcessed;
    }

    @Override // com.resync.app.model.Post
    public boolean isMod() {
        return this.mod;
    }

    @Override // com.resync.app.model.Post
    public String getBodyRaw() {
        return this.bodyRaw;
    }

    @Override // com.resync.app.model.Post
    public boolean isNewComment() {
        return this.newComment;
    }

    @Override // com.resync.app.model.Post
    public int getChildCount() {
        return this.childCount;
    }

    @Override // com.resync.app.model.Post
    public int getNewCommentCount() {
        return this.newCommentCount;
    }

    @Override // com.resync.app.model.Post
    public String getChildIds() {
        return this.childIds;
    }

    @Override // com.resync.app.model.Post
    public int getNumComments() {
        return this.numComments;
    }

    @Override // com.resync.app.model.Post
    public int getNumReports() {
        return this.numReports;
    }

    @Override // com.resync.app.model.Post
    public boolean isCollapsed() {
        return this.collapsed;
    }

    @Override // com.resync.app.model.Post
    public boolean isOver18() {
        return this.over18;
    }

    @Override // com.resync.app.model.Post
    public int getCommentMoreCount() {
        return this.commentMoreCount;
    }

    @Override // com.resync.app.model.Post
    public String getParentId() {
        return this.parentId;
    }

    public String toString() {
        return "Post{id=" + this.id + ", link_id=" + this.linkId + ", parent_id=" + this.parentId + ", hash=" + this.hash + ", kind=" + this.kind + ", sync_type=" + this.syncType + ", likes=" + this.likes + ", num_reports=" + this.numReports + ", num_comments=" + this.numComments + ", gilded=" + this.gilded + ", silvered=" + this.silvered + ", platinumed=" + this.platinumed + ", score=" + this.score + ", level=" + this.level + ", controversiality=" + this.controversiality + ", new_comment_count=" + this.newCommentCount + ", view_count=" + this.viewCount + ", award_count=" + this.awardCount + ", subreddit=" + this.subreddit + ", subreddit_id=" + this.subredditId + ", author=" + this.author + ", timestamp=" + this.timestamp + ", edited_timestamp=" + this.editedTimestamp + ", title=" + this.title + ", distinguished=" + this.distinguished + ", approved_by=" + this.approvedBy + ", banned_by=" + this.bannedBy + ", link_flair_text=" + this.linkFlairText + ", link_flair_css_class=" + this.linkFlairCssClass + ", link_flair_background_color=" + this.linkFlairBackgroundColor + ", link_flair_id=" + this.linkFlairId + ", author_flair_text=" + this.authorFlairText + ", author_flair_css_class=" + this.authorFlairCssClass + ", domain=" + this.domain + ", domain_raw=" + this.domainRaw + ", suggested_sort=" + this.suggestedSort + ", upvote_ratio=" + this.upvoteRatio + ", user_reports=" + this.userReports + ", author_flair_richtext_t=" + this.authorFlairRichtextT + ", author_flair_richtext_u=" + this.authorFlairRichtextU + ", post_cross_link=" + this.postCrossLink + ", post_cross_origin=" + this.postCrossOrigin + ", post_award_string=" + this.postAwardString + ", post_author_profile_pic=" + this.postAuthorProfilePic + ", post_fav_icon=" + this.postFavIcon + ", post_author_fullname=" + this.postAuthorFullname + ", post_created_utc=" + this.postCreatedUtc + ", permalink=" + this.permalink + ", contest_mode=" + this.contestMode + ", locked=" + this.locked + ", stickied=" + this.stickied + ", saved=" + this.saved + ", archived=" + this.archived + ", visited=" + this.visited + ", internally_visited=" + this.internallyVisited + ", hidden=" + this.hidden + ", mod=" + this.mod + ", over_18=" + this.over18 + ", edited=" + this.edited + ", score_hidden=" + this.scoreHidden + ", new_comment=" + this.newComment + ", spoiler=" + this.spoiler + ", author_cakeday=" + this.authorCakeday + ", is_original_content=" + this.isOriginalContent + ", has_awards=" + this.hasAwards + ", account_new=" + this.accountNew + ", send_replies=" + this.sendReplies + ", childIds=" + this.childIds + ", childCount=" + this.childCount + ", collapsed=" + this.collapsed + ", comment_filtered=" + this.commentFiltered + ", commentMoreIds=" + this.commentMoreIds + ", commentMoreCount=" + this.commentMoreCount + ", url=" + this.url + ", raw_url=" + this.rawUrl + ", thumbnail=" + this.thumbnail + ", preview_url=" + this.previewUrl + ", preview_width=" + this.previewWidth + ", preview_height=" + this.previewHeight + ", preview_infographic=" + this.previewInfographic + ", preview_duration=" + this.previewDuration + ", video_preview=" + this.videoPreview + ", selftext_raw=" + this.selftextRaw + ", selftext_processed=" + this.selftextProcessed + ", selftext_processed_preview=" + this.selftextProcessedPreview + ", embedded=" + this.embedded + ", embedded_author=" + this.embeddedAuthor + ", embedded_author_handle=" + this.embeddedAuthorHandle + ", body_raw=" + this.bodyRaw + ", body_processed=" + this.bodyProcessed + ", lists_time=" + this.listsTime + ", age=" + this.age + ", lists_id=" + this.listsId + "}";
    }

    @Override // com.resync.app.model.Post
    public String getCommentMoreIds() {
        return this.commentMoreIds;
    }

    @Override // com.resync.app.model.Post
    public String getPermalink() {
        return this.permalink;
    }

    @Override // com.resync.app.model.Post
    public boolean isCommentFiltered() {
        return this.commentFiltered;
    }

    @Override // com.resync.app.model.Post
    public int getPlatinumed() {
        return this.platinumed;
    }

    @Override // com.resync.app.model.Post
    public boolean isContestMode() {
        return this.contestMode;
    }

    @Override // com.resync.app.model.Post
    public int getControversiality() {
        return this.controversiality;
    }

    @Override // com.resync.app.model.Post
    public String getPostAuthorFullname() {
        return this.postAuthorFullname;
    }

    @Override // com.resync.app.model.Post
    public String getPostAuthorProfilePic() {
        return this.postAuthorProfilePic;
    }

    @Override // com.resync.app.model.Post
    public String getPostAwardString() {
        return this.postAwardString;
    }
}
