package com.resync.app.util;

import android.database.Cursor;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.URLSpan;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.singleton.AccountSingleton;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.FriendsManager;
import com.resync.app.util.ArrayUtil;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: renamed from: va.a */
/* JADX INFO: compiled from: CommentNavigation.java */
/* JADX INFO: loaded from: classes.dex */
public class CommentNavigation implements Serializable {
    String searchKeyword;

    ArrayList<Integer> opIndices = new ArrayList<>();
    ArrayList<Integer> meIndices = new ArrayList<>();
    ArrayList<Integer> friendIndices = new ArrayList<>();

    ArrayList<Integer> gildedIndices = new ArrayList<>();

    ArrayList<Integer> linkIndices = new ArrayList<>();

    ArrayList<Integer> imageIndices = new ArrayList<>();
    ArrayList<Integer> videoIndices = new ArrayList<>();

    ArrayList<Integer> searchAuthorIndices = new ArrayList<>();
    ArrayList<Integer> searchKeywordIndices = new ArrayList<>();
    ArrayList<Integer> threadIndices = new ArrayList<>();
    ArrayList<Integer> newCommentIndices = new ArrayList<>();

    int mode = 7;
    String searchAuthor;

    static class UnsupportedModeException extends RuntimeException {
        public UnsupportedModeException(int mode) {
            super("Unsupported mode: " + mode);
        }
    }

    public static int getModeIcon(int mode) {
        if (mode == 0) {
            return R.drawable.account_supervisor_outline;
        }
        if (mode == 1) {
            return R.drawable.outline_person_outline_24;
        }
        switch (mode) {
            case 4:
                return R.drawable.ic_link_black_24dp;
            case 5:
                return R.drawable.outline_image_24;
            case 6:
                return R.drawable.outline_videocam_24;
            case 7:
                return R.drawable.outline_explore_24;
            case 8:
                return R.drawable.outline_mode_comment_24;
            case 9:
                return R.drawable.account_search_outline;
            case 10:
                return R.drawable.comment_search_outline;
            default:
                throw new RuntimeException("Unsupported mode");
        }
    }

    public void clear() {
        this.opIndices.clear();
        this.meIndices.clear();
        this.friendIndices.clear();
        this.gildedIndices.clear();
        this.linkIndices.clear();
        this.imageIndices.clear();
        this.videoIndices.clear();
        this.searchAuthorIndices.clear();
        this.searchKeywordIndices.clear();
        this.threadIndices.clear();
        this.newCommentIndices.clear();
    }

    public void process(Cursor cursor, Post post) {
        boolean hasVideo;
        int hasLink;
        int hasImage;
        String[] friends;
        Cursor cursor2 = cursor;
        if (cursor2 == null || cursor.isClosed()) {
            return;
        }
        int one = 1;
        if (cursor.getCount() >= 1 && post != null) {
            clear();
            long jCurrentTimeMillis = System.currentTimeMillis();
            int position = 1;
            while (position < cursor.getCount()) {
                cursor2.moveToPosition(position);
                String string = cursor2.getString(cursor2.getColumnIndex("author"));
                String string2 = cursor2.getString(cursor2.getColumnIndex("body_processed"));
                int gilded = cursor2.getInt(cursor2.getColumnIndex("gilded"));
                int level = cursor2.getInt(cursor2.getColumnIndex("level"));
                int friendIndex = 0;
                int isNewComment = cursor2.getInt(cursor2.getColumnIndex("new_comment")) == one ? one : 0;
                SpannableStringBuilder spannableStringBuilder = null;
                if (TextUtils.isEmpty(string2)) {
                    hasVideo = false;
                    hasLink = 0;
                    hasImage = 0;
                } else {
                    spannableStringBuilder = (SpannableStringBuilder) RedditHelper.htmlToTrimmedText(string2);
                    URLSpan[] uRLSpanArr = (URLSpan[]) spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), URLSpan.class);
                    hasLink = (ArrayUtil.isEmpty(uRLSpanArr) ? 1 : 0) ^ one;
                    if (hasLink != 0) {
                        int length = uRLSpanArr.length;
                        int imageSpanIndex = 0;
                        while (true) {
                            if (imageSpanIndex >= length) {
                                hasImage = 0;
                                break;
                            } else {
                                if (LinkTypeHelper.isImage(uRLSpanArr[imageSpanIndex].getURL())) {
                                    hasImage = one;
                                    break;
                                }
                                imageSpanIndex++;
                            }
                        }
                        int length2 = uRLSpanArr.length;
                        int videoSpanIndex = 0;
                        while (true) {
                            if (videoSpanIndex >= length2) {
                                hasVideo = false;
                                break;
                            } else {
                                if (LinkTypeHelper.isYouTube(uRLSpanArr[videoSpanIndex].getURL())) {
                                    hasVideo = true;
                                    break;
                                }
                                videoSpanIndex++;
                            }
                        }
                    } else {
                        hasVideo = false;
                        hasImage = 0;
                    }
                }
                if (post.getAuthor().equalsIgnoreCase(string)) {
                    this.opIndices.add(Integer.valueOf(position));
                }
                if (AccountSingleton.getInstance().getUsername().equalsIgnoreCase(string)) {
                    this.meIndices.add(Integer.valueOf(position));
                }
                if (FriendsManager.getInstance().getFriends() != null && FriendsManager.getInstance().getFriends().length > 0) {
                    String[] strArrC = FriendsManager.getInstance().getFriends();
                    int length3 = strArrC.length;
                    while (friendIndex < length3) {
                        if (StringUtils.equals(strArrC[friendIndex], string)) {
                            friends = strArrC;
                            this.friendIndices.add(Integer.valueOf(position));
                        } else {
                            friends = strArrC;
                        }
                        friendIndex++;
                        strArrC = friends;
                    }
                }
                if (gilded > 0) {
                    this.gildedIndices.add(Integer.valueOf(position));
                }
                if (hasLink != 0) {
                    this.linkIndices.add(Integer.valueOf(position));
                }
                if (hasImage != 0) {
                    this.imageIndices.add(Integer.valueOf(position));
                }
                if (hasVideo) {
                    this.videoIndices.add(Integer.valueOf(position));
                }
                if (!TextUtils.isEmpty(string) && string.equalsIgnoreCase(this.searchAuthor)) {
                    this.searchAuthorIndices.add(Integer.valueOf(position));
                }
                if (!TextUtils.isEmpty(this.searchKeyword) && spannableStringBuilder != null && spannableStringBuilder.length() > 0 && spannableStringBuilder.toString().toLowerCase(Locale.ENGLISH).contains(this.searchKeyword)) {
                    this.searchKeywordIndices.add(Integer.valueOf(position));
                }
                if (level == 0) {
                    this.threadIndices.add(Integer.valueOf(position));
                }
                if (isNewComment != 0) {
                    this.newCommentIndices.add(Integer.valueOf(position));
                }
                position++;
                cursor2 = cursor;
                one = 1;
            }
            Logger.verbose("CommentNavigation", "Search time: " + (System.currentTimeMillis() - jCurrentTimeMillis));
            Logger.verbose("CommentNavigation", "OP instances: " + this.opIndices.size());
            Logger.verbose("CommentNavigation", "Me instances: " + this.meIndices.size());
            Logger.verbose("CommentNavigation", "Friend instances: " + this.friendIndices.size());
            Logger.verbose("CommentNavigation", "Gilded instances: " + this.gildedIndices.size());
            Logger.verbose("CommentNavigation", "Links: " + this.linkIndices.size());
            Logger.verbose("CommentNavigation", "Images: " + this.imageIndices.size());
            Logger.verbose("CommentNavigation", "Videos: " + this.videoIndices.size());
            Logger.verbose("CommentNavigation", "Search author instances: " + this.searchAuthorIndices.size());
            Logger.verbose("CommentNavigation", "Search keyword instances: " + this.searchKeywordIndices.size());
            Logger.verbose("CommentNavigation", "Threads: " + this.threadIndices.size());
            Logger.verbose("CommentNavigation", "New comments: " + this.newCommentIndices.size());
        }
    }

    public int getCount(int mode) {
        return getIndices(mode).size();
    }

    public int getMode() {
        return this.mode;
    }

    public int getCurrentModeIcon() {
        return getModeIcon(getMode());
    }

    protected ArrayList<Integer> getIndices(int mode) {
        if (mode == 0) {
            return this.opIndices;
        }
        if (1 == mode) {
            return this.meIndices;
        }
        if (2 == mode) {
            return this.friendIndices;
        }
        if (3 == mode) {
            return this.gildedIndices;
        }
        if (5 == mode) {
            return this.imageIndices;
        }
        if (6 == mode) {
            return this.videoIndices;
        }
        if (9 == mode) {
            return this.searchAuthorIndices;
        }
        if (10 == mode) {
            return this.searchKeywordIndices;
        }
        if (4 == mode) {
            return this.linkIndices;
        }
        if (7 == mode) {
            return this.threadIndices;
        }
        if (8 == mode) {
            return this.newCommentIndices;
        }
        throw new UnsupportedModeException(mode);
    }

    public int findNext(int firstVisible) {
        ArrayList<Integer> arrayListG = getIndices(getMode());
        Logger.verbose("CommentNavigation", "First visible: " + firstVisible);
        for (Integer num : arrayListG) {
            Logger.verbose("CommentNavigation", "Checking: " + num);
            if (firstVisible < num.intValue()) {
                Logger.verbose("CommentNavigation", "Found next: " + num);
                return num.intValue();
            }
        }
        return -1;
    }

    public int findPrevious(int firstVisible) {
        ArrayList<Integer> arrayListG = getIndices(getMode());
        Logger.verbose("CommentNavigation", "Total: " + arrayListG.size());
        Logger.verbose("CommentNavigation", "First visible: " + firstVisible);
        for (int size = arrayListG.size() + (-1); size >= 0; size--) {
            Integer num = arrayListG.get(size);
            Logger.verbose("CommentNavigation", "Checking: " + num);
            if (firstVisible > num.intValue()) {
                Logger.verbose("CommentNavigation", "Found previous: " + num);
                return num.intValue();
            }
        }
        Logger.verbose("CommentNavigation", "Failed to find previous");
        return -1;
    }

    public ArrayList<Integer> getThreadIndices() {
        return this.threadIndices;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setSearchAuthor(String author) {
        this.searchKeyword = null;
        this.searchAuthor = author;
        if (!TextUtils.isEmpty(author)) {
            this.searchAuthor = this.searchAuthor.toLowerCase(Locale.ENGLISH).trim();
        }
        Logger.verbose("CommentNavigation", "Setting author: " + author);
    }

    public void setSearchKeyword(String keyword) {
        this.searchKeyword = keyword;
        if (!TextUtils.isEmpty(keyword)) {
            this.searchKeyword = this.searchKeyword.toLowerCase(Locale.ENGLISH).trim();
        }
        Logger.verbose("CommentNavigation", "Setting keyword: " + keyword);
    }
}
