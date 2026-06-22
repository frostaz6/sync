package com.resync.app.helpers;

import com.bumptech.glide.MaterialPostsHelper;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: renamed from: t7.w */
/* JADX INFO: compiled from: PostInfoHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class PostInfoHelper {
    public static boolean isSpecialSubreddit(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return "frontpage".equalsIgnoreCase(str) || "all".equalsIgnoreCase(str) || "popular".equalsIgnoreCase(str) || str.contains("multi_") || MaterialPostsHelper.isSearch(str) || MaterialPostsHelper.isFriends(str) || MaterialPostsHelper.isSubSearch(str) || MaterialPostsHelper.isDomain(str) || MaterialPostsHelper.isSeen(str) || (str.startsWith("user###") && !str.startsWith("user###Overview")) || str.startsWith("r/mod/");
    }
}
