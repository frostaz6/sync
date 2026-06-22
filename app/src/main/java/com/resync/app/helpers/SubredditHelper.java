package com.resync.app.helpers;

import android.text.TextUtils;
import com.bumptech.glide.MaterialPostsHelper;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: renamed from: t7.i0 */
/* JADX INFO: compiled from: SubredditHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class SubredditHelper {
    public static String appendSortSuffix(String str, String str2) {
        String str3 = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (!TextUtils.isEmpty(str2)) {
            String strReplaceAll = str2.replaceAll("sort=", "").replaceAll("t=", "");
            if (!str2.toLowerCase(Locale.ENGLISH).equals(strReplaceAll)) {
                str3 = strReplaceAll;
            }
        }
        if (StringUtils.equalsIgnoreCase("Hour", str3)) {
            str3 = " (H)";
        } else if (StringUtils.equalsIgnoreCase("Day", str3)) {
            str3 = " (D)";
        } else if (StringUtils.equalsIgnoreCase("Week", str3)) {
            str3 = " (W)";
        } else if (StringUtils.equalsIgnoreCase("Month", str3)) {
            str3 = " (M)";
        } else if (StringUtils.equalsIgnoreCase("Year", str3)) {
            str3 = " (Y)";
        } else if (StringUtils.equalsIgnoreCase("All", str3)) {
            str3 = " (A)";
        }
        if (str3 == null || str3.equalsIgnoreCase(str)) {
            return str;
        }
        if (str3.equals("rising")) {
            return "Rising";
        }
        return str + StringUtils.SPACE + str3;
    }

    public static String getDisplayTitle(String str, boolean z) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (MaterialPostsHelper.isOpenUrl(str)) {
            return str;
        }
        if (MaterialPostsHelper.isSubSearch(str)) {
            if (z) {
                return "Sub search \"" + MaterialPostsHelper.extractSubSearchQuery(str) + "\"";
            }
            return "\"" + MaterialPostsHelper.extractSubSearchQuery(str) + "\"";
        }
        if (MaterialPostsHelper.isSearch(str)) {
            if (StringUtils.containsIgnoreCase(str, "flair%3A")) {
                String flairSubreddit = MaterialPostsHelper.extractSearchSubreddit(str);
                if (StringUtils.isNotEmpty(flairSubreddit)) {
                    return flairSubreddit + " (flair)";
                }
            }
            if (StringUtils.containsIgnoreCase(str, "author%3A")) {
                String authorSubreddit = MaterialPostsHelper.extractSearchSubreddit(str);
                if (StringUtils.isNotEmpty(authorSubreddit)) {
                    return authorSubreddit + " (author)";
                }
            }
            String str2 = "\"" + MaterialPostsHelper.extractSearchQuery(str) + "\"";
            if (!z || !StringUtils.isNotEmpty(MaterialPostsHelper.extractSearchSubreddit(str))) {
                return str2;
            }
            return str2 + " in /r/" + MaterialPostsHelper.extractSearchSubreddit(str);
        }
        if (MaterialPostsHelper.isModAbout(str)) {
            return "Moderator";
        }
        if (MaterialPostsHelper.isDomain(str)) {
            return "Explore domain";
        }
        if (MaterialPostsHelper.isUserProfile(str)) {
            String profileSection = MaterialPostsHelper.extractProfileSection(str);
            String profileUsername = MaterialPostsHelper.extractProfileUsername(str);
            if (!z || profileSection.equalsIgnoreCase("overview")) {
                return profileUsername;
            }
            return profileUsername + " - " + profileSection;
        }
        if (str.startsWith("multi_") && str.contains("/")) {
            return str.split("/")[2] + " [M]";
        }
        if (!str.startsWith("multi_")) {
            return MaterialPostsHelper.isSeen(str) ? "Other discussions" : str.equals("list__watching") ? "Watching" : str;
        }
        return str.replace("multi_", "") + " [M]";
    }

    public static String getShortTitle(String str, boolean z) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (MaterialPostsHelper.isSearch(str)) {
            return "\"" + MaterialPostsHelper.extractSearchQuery(str) + "\"";
        }
        if (MaterialPostsHelper.isSubSearch(str)) {
            return "\"" + MaterialPostsHelper.extractSubSearchQuery(str) + "\"";
        }
        if (MaterialPostsHelper.isModAbout(str)) {
            return "Moderator";
        }
        if (MaterialPostsHelper.isDomain(str)) {
            return "Explore domain";
        }
        if (!MaterialPostsHelper.isUserProfile(str)) {
            return (str.startsWith("multi_") && str.contains("/")) ? str.split("/")[2] : str.startsWith("multi_") ? str.replace("multi_", "") : MaterialPostsHelper.isSeen(str) ? "Other discussions" : str;
        }
        String profileSection = MaterialPostsHelper.extractProfileSection(str);
        String profileUsername = MaterialPostsHelper.extractProfileUsername(str);
        if (!z || profileSection.equalsIgnoreCase("overview")) {
            return profileUsername;
        }
        return profileUsername + " - " + profileSection;
    }
}
