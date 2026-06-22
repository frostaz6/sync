package com.bumptech.glide;
import com.resync.app.R;
import java.net.URLEncoder;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: renamed from: d7.d */
/* JADX INFO: compiled from: MaterialPostsHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class MaterialPostsHelper {
    public static int getSortIconRes(String str, String str2) {
        return StringUtils.equalsIgnoreCase("best", str) ? R.drawable.ic_star_outline_white_24dp : StringUtils.equalsIgnoreCase("hot", str) ? R.drawable.outline_whatshot_24 : StringUtils.equalsIgnoreCase("new", str) ? R.drawable.outline_new_24 : StringUtils.equalsIgnoreCase("Rising", str) ? R.drawable.ic_trending_up_white_24dp : StringUtils.equalsIgnoreCase("Top", str) ? R.drawable.outline_bar_chart_24 : StringUtils.equalsIgnoreCase("Controversial", str) ? R.drawable.outline_trending_down_24 : StringUtils.equalsIgnoreCase("Comments", str) ? R.drawable.outline_forum_24 : StringUtils.equalsIgnoreCase("Relevance", str) ? R.drawable.comment_search_outline : R.drawable.outline_cancel_24;
    }

    public static boolean isSubSearch(String str) {
        return StringUtils.startsWithIgnoreCase(str, "subs-search###");
    }

    public static boolean isOpenUrl(String str) {
        return StringUtils.startsWithIgnoreCase(str, "open-url###");
    }

    public static boolean isStandardSubreddit(String str) {
        return ("Messaging".equalsIgnoreCase(str) || isAggregateFeed(str) || isMultireddit(str) || isFriends(str) || isSearch(str) || isSubSearch(str) || isUserProfile(str) || isModAbout(str) || isDomain(str) || isSeen(str) || "list__watching".equals(str)) ? false : true;
    }

    public static boolean isAggregateFeed(String str) {
        return StringUtils.equalsAnyIgnoreCase(str, "frontpage", "all", "popular");
    }

    public static boolean isDomain(String str) {
        return StringUtils.startsWithIgnoreCase(str, "domain###");
    }

    public static boolean isFriends(String str) {
        return StringUtils.equalsIgnoreCase(str, "friends");
    }

    public static boolean isModAbout(String str) {
        return StringUtils.startsWithIgnoreCase(str, "r/mod/about/");
    }

    public static boolean isMultireddit(String str) {
        return StringUtils.startsWithIgnoreCase(str, "multi_");
    }

    public static boolean isUserProfile(String str) {
        return StringUtils.startsWithIgnoreCase(str, "user###");
    }

    public static boolean isSearch(String str) {
        return StringUtils.startsWithIgnoreCase(str, "search###");
    }

    public static boolean isSeen(String str) {
        return StringUtils.startsWithIgnoreCase(str, "seen###");
    }

    public static String buildDomainAddress(String str) {
        return "domain###" + LinkTypeHelper.getHost(str);
    }

    public static String buildSubSearchAddress(String str) {
        return "subs-search######" + str;
    }

    public static String buildModAboutAddress(String str) {
        return "r/mod/about/" + str;
    }

    public static String buildMultiredditAddress(String str) {
        return "multi_" + str;
    }

    public static String buildOpenUrlAddress(String str) {
        return "open-url###" + str;
    }

    public static String buildOverviewProfileAddress(String str) {
        return "user###Overview###" + StringUtils.trim(str) + "###null";
    }

    public static String buildProfileSectionAddress(String str, String str2) {
        return "user###" + str2 + "###" + StringUtils.trim(str) + "###null";
    }

    public static String buildSearchAddress(String str, String str2) {
        StringBuilder sb2 = new StringBuilder();
        sb2.append("search###");
        sb2.append(str2 != null ? StringUtils.trim(str2) : "");
        sb2.append("###");
        sb2.append(str);
        return sb2.toString();
    }

    public static String buildSeenAddress(String str) {
        return "seen###" + URLEncoder.encode(str);
    }

    public static String extractDomain(String str) {
        if (isDomain(str)) {
            return str.split("###")[1];
        }
        throw new RuntimeException("This was not a domain: " + str);
    }

    public static String extractSubSearchQuery(String str) {
        if (isSubSearch(str)) {
            return str.replace("subs-search######", "");
        }
        throw new RuntimeException("This was not a sub search: " + str);
    }

    public static int getMessagingSectionIconRes(int sectionIndex) {
        return sectionIndex == 0 ? R.drawable.outline_inbox_24 : sectionIndex == 1 ? R.drawable.inbox_full_outline : sectionIndex == 2 ? R.drawable.bottom_nav_outline_mail_outline_24 : sectionIndex == 3 ? R.drawable.email_send_outline : sectionIndex == 4 ? R.drawable.outline_mode_comment_24 : sectionIndex == 5 ? R.drawable.outline_post_24 : sectionIndex == 6 ? R.drawable.outline_person_outline_24 : sectionIndex == 7 ? R.drawable.outline_shield : sectionIndex == 8 ? R.drawable.shield_half_full : R.drawable.outline_inbox_24;
    }

    public static int getMessagingSectionIconResFromString(String str) {
        return getMessagingSectionIconRes(Integer.parseInt(str));
    }

    public static String getMessagingSectionTitle(int sectionIndex) {
        switch (sectionIndex) {
            case 0:
                return "All";
            case 1:
                return "Unread";
            case 2:
                return "DMs";
            case 3:
                return "Sent";
            case 4:
                return "Comment replies";
            case 5:
                return "Post replies";
            case 6:
                return "Mentions";
            case 7:
                return "Mod";
            case 8:
                return "Mod unread";
            default:
                throw new RuntimeException("Unknown mode");
        }
    }

    public static String getMessagingSectionTitleFromString(String str) {
        return getMessagingSectionTitle(Integer.parseInt(str));
    }

    public static String extractModSection(String str) {
        if (isModAbout(str)) {
            return str.split("r/mod/about/")[1];
        }
        throw new RuntimeException("This was not a mod section: " + str);
    }

    public static int getModSectionIconRes(String str) {
        if (StringUtils.containsIgnoreCase(str, "modqueue")) {
            return R.drawable.tray_full;
        }
        if (StringUtils.containsIgnoreCase(str, "reports")) {
            return R.drawable.outline_report_24;
        }
        if (StringUtils.containsIgnoreCase(str, "spam")) {
            return R.drawable.outline_remove_circle_outline_24;
        }
        if (StringUtils.containsIgnoreCase(str, "edited")) {
            return R.drawable.outline_edit_24;
        }
        if (StringUtils.containsIgnoreCase(str, "unmoderated")) {
            return R.drawable.outline_shield;
        }
        if (StringUtils.containsIgnoreCase(str, "comments")) {
            return R.drawable.outline_mode_comment_24;
        }
        throw new RuntimeException("Unsupported section: " + str);
    }

    public static String extractMultiredditName(String str) {
        if (isMultireddit(str)) {
            return str.split("multi_")[1];
        }
        throw new RuntimeException("This was not a multireddit link: " + str);
    }

    public static String extractOpenUrl(String str) {
        if (isOpenUrl(str)) {
            return str.split("open-url###")[1];
        }
        throw new RuntimeException("This was not an open URL section: " + str);
    }

    public static int getProfileSectionIconRes(String str) {
        return StringUtils.containsIgnoreCase(str, "overview") ? R.drawable.outline_person_outline_24 : StringUtils.containsIgnoreCase(str, "comments") ? R.drawable.outline_mode_comment_24 : StringUtils.containsIgnoreCase(str, "submitted") ? R.drawable.outline_post_24 : StringUtils.containsIgnoreCase(str, "upvoted") ? R.drawable.ic_arrow_upward_white_24dp : StringUtils.containsIgnoreCase(str, "downvoted") ? R.drawable.ic_arrow_downward_white_24dp : StringUtils.containsIgnoreCase(str, "saved") ? R.drawable.outline_bookmark_border_24 : StringUtils.containsIgnoreCase(str, "hidden") ? R.drawable.ic_close_white_24dp : R.drawable.outline_person_outline_24;
    }

    public static String extractProfileSection(String str) {
        if (isUserProfile(str)) {
            return str.split("###")[1];
        }
        throw new RuntimeException("This was not a profile: " + str);
    }

    public static String extractProfileUsername(String str) {
        if (isUserProfile(str)) {
            return str.split("###")[2];
        }
        throw new RuntimeException("This was not a profile: " + str);
    }

    public static String extractSearchSubreddit(String str) {
        if (isSearch(str)) {
            return str.split("###")[1];
        }
        throw new RuntimeException("This was not a search: " + str);
    }

    public static String extractSearchQuery(String str) {
        if (isSearch(str)) {
            return str.split("###")[2];
        }
        throw new RuntimeException("This was not a search: " + str);
    }

    public static String extractSeenSubreddit(String str) {
        if (isSeen(str)) {
            return str.split("###")[1];
        }
        throw new RuntimeException("This was not a seen subreddit link: " + str);
    }

    public static boolean isSubscribable(String str) {
        return (isSearch(str) || isSubSearch(str) || isDomain(str) || isUserProfile(str) || isMultireddit(str) || isAggregateFeed(str) || isFriends(str)) ? false : true;
    }
}
