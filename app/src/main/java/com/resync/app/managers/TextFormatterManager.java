package com.resync.app.managers;

import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import com.resync.app.R;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.premium.UserPaintManager;
import com.resync.app.helpers.ui.MonetUtil;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.media.images.SyncTypeHelper;
import com.resync.app.managers.FriendsManager;
import com.resync.app.managers.NewUserTagManager;
import com.resync.app.singletons.SpanSingleton;
import com.resync.app.text.spans.CustomUrlSpan;
import com.resync.app.text.spans.MonetRoundedCornerSpan;
import com.resync.app.text.spans.RadiusBgSpan;
import com.resync.app.text.spans.SilentCustomUrlSpan;
import com.resync.app.text.spans.EfficientImageSpan;
import com.resync.app.text.spans.VectorImageSpan;
import com.resync.app.model.Message;
import com.resync.app.widgets.text.SpannableTextViewStringBuilder;
import com.resync.app.helpers.CrashlyticsHelper;
import com.resync.app.helpers.ScoreHelper;
import com.resync.app.helpers.ViewsHelper;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.util.links.LinkTypeHelper;
import com.resync.app.presenters.BaseCursorPresenter;
import com.resync.app.presenters.CommentsPresenter;

/* JADX INFO: renamed from: q8.e */
/* JADX INFO: compiled from: TextFormatterManager.java */
/* JADX INFO: loaded from: classes.dex */
public class TextFormatterManager {
    protected static void appendFlair(SpannableTextViewStringBuilder spannableTextViewStringBuilder, TextView textView, Post post) {
        if (!StringUtils.isNotEmpty(post.getAuthorFlairRichtextU()) || StringUtils.equals(".", post.getAuthorFlairRichtextU().trim())) {
            return;
        }
        spannableTextViewStringBuilder.append(StringUtils.SPACE);
        Logger.verbose("URLS: " + post.getAuthorFlairRichtextU());
        String[] strArrSplit = post.getAuthorFlairRichtextU().split("\\,\\,");
        int lineHeight = textView.getLineHeight();
        TextPaint paint = textView.getPaint();
        if (paint != null) {
            lineHeight = (int) (((double) Math.abs(paint.getFontMetricsInt().ascent)) * 1.6d);
        }
        int length = strArrSplit.length;
        for (int i = 0; i < length; i++) {
            String strReplaceAll = strArrSplit[i];
            if (strReplaceAll != null) {
                strReplaceAll = StringUtils.trimToEmpty(strReplaceAll).replaceAll("\n", StringUtils.SPACE);
            }
            if (LinkTypeHelper.isImage(strReplaceAll)) {
                Logger.verbose("Adding image flair: " + strReplaceAll);
                appendSpan(spannableTextViewStringBuilder, new EfficientImageSpan(strReplaceAll, lineHeight), strReplaceAll);
            } else if (StringUtils.isNotEmpty(strReplaceAll) && strReplaceAll.length() > 1) {
                int flairStart = spannableTextViewStringBuilder.length();
                appendSpan(spannableTextViewStringBuilder, SpanSingleton.newLinkColorSpan2(), strReplaceAll.length() > 20 ? strReplaceAll.substring(0, 20) + "…" : strReplaceAll);
                int flairEnd = spannableTextViewStringBuilder.length();
                spannableTextViewStringBuilder.addSpan(new RadiusBgSpan(ColorUtils.setAlphaComponent(MonetUtil.getSecondaryTextColorAuto(), 20), ColorUtils.setAlphaComponent(MonetUtil.getSecondaryTextColorAuto(), 31), MonetUtil.getSecondaryTextColorAuto(), 10), flairStart, flairEnd, 33);
                spannableTextViewStringBuilder.addSpan(new CustomUrlSpan("sync-flair://" + strReplaceAll), flairStart, flairEnd, 33);
                spannableTextViewStringBuilder.append(StringUtils.SPACE);
            }
        }
    }

    public static void appendSpan(SpannableTextViewStringBuilder spannableTextViewStringBuilder, Object obj, String str) {
        int spanStart = spannableTextViewStringBuilder.length();
        spannableTextViewStringBuilder.append(str);
        spannableTextViewStringBuilder.addSpan(obj, spanStart, spannableTextViewStringBuilder.length(), 33);
    }

    public static void appendPostScore(BaseCursorPresenter baseCursorPresenter, SpannableTextViewStringBuilder spannableTextViewStringBuilder, Post post, boolean z) {
        int scoreStart = spannableTextViewStringBuilder.length();
        if (SettingsSingleton.getSettings().postScoresShort) {
            spannableTextViewStringBuilder.append(ScoreHelper.formatPoints(post.getScore()));
        } else {
            spannableTextViewStringBuilder.append(ScoreHelper.formatPointsLabel(post.getScore()));
        }
        int scoreEnd = spannableTextViewStringBuilder.length();
        if (post.isUpvoted()) {
            spannableTextViewStringBuilder.addSpan(SpanSingleton.getHighlightSpan(), scoreStart, scoreEnd, 17);
        } else if (post.isDownvoted()) {
            spannableTextViewStringBuilder.addSpan(SpanSingleton.getSpoilerSpan(), scoreStart, scoreEnd, 17);
        }
        spannableTextViewStringBuilder.append("\n");
        spannableTextViewStringBuilder.append(Integer.toString(post.getNumComments()));
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" comment");
        String str = "";
        sb2.append(post.getNumComments() == 1 ? "" : "s");
        spannableTextViewStringBuilder.append(sb2.toString());
        if (SettingsSingleton.getSettings().postsDisplayNew) {
            if (baseCursorPresenter instanceof CommentsPresenter) {
                CommentsPresenter commentsPresenter = (CommentsPresenter) baseCursorPresenter;
                if (StringUtils.isNotEmpty(commentsPresenter.getNewCommentSuffix())) {
                    spannableTextViewStringBuilder.appendWithSpans(commentsPresenter.getNewCommentSuffix(), new ForegroundColorSpan(-65536));
                    return;
                }
                return;
            }
            if (post.getNewCommentCount() > 0) {
                str = " (+" + post.getNewCommentCount() + ")";
            }
            spannableTextViewStringBuilder.appendWithSpans(str, new ForegroundColorSpan(-65536));
        }
    }

    public static void formatCommentInfo(SpannableTextViewStringBuilder spannableTextViewStringBuilder, TextView textView, Post post, Post post2, boolean z) {
        boolean badgeApplied;
        int prefixAdded;
        int newAccountSpanLength;
        int cakedaySpanLength;
        int specialSpanLength;
        int adminSpanLength;
        int modSpanLength;
        int automodSpanLength;
        if (post2 == null) {
            return;
        }
        if (post2.getSyncType() != 11) {
            throw new SyncTypeHelper.UnsupportedSyncTypeException("Type: " + post2.getSyncType());
        }
        spannableTextViewStringBuilder.append("\u200e");
        int authorStart = spannableTextViewStringBuilder.length();
        spannableTextViewStringBuilder.append(post2.getAuthor());
        int authorEnd = spannableTextViewStringBuilder.length();
        if (SettingsSingleton.getSettings().tapUsernamesOpen) {
            spannableTextViewStringBuilder.addSpan(new SilentCustomUrlSpan("/u/" + post2.getAuthor()), authorStart, authorEnd, 33);
        }
        int one = 1;
        if (!post2.getAuthor().equalsIgnoreCase("[deleted]")) {
            spannableTextViewStringBuilder.addSpan(SpanSingleton.getBoldSpan(), authorStart, authorEnd, 33);
            if (com.resync.app.premium.UserPaintManager.getInstance().hasPaint(post2.getAuthor())) {
                spannableTextViewStringBuilder.addSpan(new MonetRoundedCornerSpan(com.resync.app.premium.UserPaintManager.getInstance().getColor(post2.getAuthor()).intValue()), authorStart, authorEnd, 33);
                badgeApplied = true;
            } else {
                badgeApplied = false;
            }
            // ReSync: removed original-developer ("ljdawson") badge.
            prefixAdded = 0;
            if (post != null && post2.getAuthor().equalsIgnoreCase(post.getAuthor()) && SettingsSingleton.getSettings().highlightOp) {
                if (prefixAdded == 0) {
                    spannableTextViewStringBuilder.append(StringUtils.SPACE);
                    prefixAdded = 1;
                }
                spannableTextViewStringBuilder.appendWithSpans("OP", new VectorImageSpan(textView, R.drawable.account_voice, 0));
                spannableTextViewStringBuilder.addSpan(SpanSingleton.newLinkColorSpan5(), authorStart, authorEnd, 33);
                if (!badgeApplied) {
                    spannableTextViewStringBuilder.addSpan(new MonetRoundedCornerSpan(), authorStart, authorEnd, 33);
                    badgeApplied = true;
                }
            }
            if (post == null || !post2.getAuthor().equalsIgnoreCase(AccountSingleton.getInstance().getUsername())) {
                if (FriendsManager.getInstance().getFriends() != null && FriendsManager.getInstance().getFriends().length > 0) {
                    String[] strArrC = FriendsManager.getInstance().getFriends();
                    int length = strArrC.length;
                    int i = 0;
                    while (i < length) {
                        if (StringUtils.equalsIgnoreCase(strArrC[i], post2.getAuthor())) {
                            if (prefixAdded == 0) {
                                spannableTextViewStringBuilder.append(StringUtils.SPACE);
                                prefixAdded = one;
                            }
                            Object[] objArr = new Object[one];
                            objArr[0] = new VectorImageSpan(textView, R.drawable.emoticon_happy, 0);
                            spannableTextViewStringBuilder.appendWithSpans("friend", objArr);
                            if (!badgeApplied) {
                                spannableTextViewStringBuilder.addSpan(new MonetRoundedCornerSpan(), authorStart, authorEnd, 33);
                                badgeApplied = true;
                            }
                        }
                        i++;
                        one = 1;
                    }
                }
            } else if (SettingsSingleton.getSettings().highlightYourself) {
                if (prefixAdded == 0) {
                    spannableTextViewStringBuilder.append(StringUtils.SPACE);
                    prefixAdded = 1;
                }
                spannableTextViewStringBuilder.appendWithSpans("you", new VectorImageSpan(textView, R.drawable.filled_in_person, 0));
                if (!badgeApplied) {
                    spannableTextViewStringBuilder.addSpan(new MonetRoundedCornerSpan(), authorStart, authorEnd, 33);
                }
            }
            if ("automoderator".equalsIgnoreCase(post2.getAuthor())) {
                if (prefixAdded == 0) {
                    spannableTextViewStringBuilder.append(StringUtils.SPACE);
                    automodSpanLength = 1;
                    prefixAdded = 1;
                } else {
                    automodSpanLength = 1;
                }
                Object[] objArr2 = new Object[automodSpanLength];
                objArr2[0] = new VectorImageSpan(textView, R.drawable.robot_excited, 0);
                spannableTextViewStringBuilder.appendWithSpans("automod", objArr2);
            }
            if ("moderator".equalsIgnoreCase(post2.getDistinguished())) {
                if (prefixAdded == 0) {
                    spannableTextViewStringBuilder.append(StringUtils.SPACE);
                    modSpanLength = 1;
                    prefixAdded = 1;
                } else {
                    modSpanLength = 1;
                }
                Object[] objArr3 = new Object[modSpanLength];
                objArr3[0] = new VectorImageSpan(textView, R.drawable.shield_half_full, 0);
                spannableTextViewStringBuilder.appendWithSpans("mod", objArr3);
            }
            if ("admin".equalsIgnoreCase(post2.getDistinguished())) {
                if (prefixAdded == 0) {
                    spannableTextViewStringBuilder.append(StringUtils.SPACE);
                    adminSpanLength = 1;
                    prefixAdded = 1;
                } else {
                    adminSpanLength = 1;
                }
                Object[] objArr4 = new Object[adminSpanLength];
                objArr4[0] = new VectorImageSpan(textView, R.drawable.ic_snoo_24dp, 0);
                spannableTextViewStringBuilder.appendWithSpans("admin", objArr4);
            }
            if ("special".equalsIgnoreCase(post2.getDistinguished())) {
                if (prefixAdded == 0) {
                    spannableTextViewStringBuilder.append(StringUtils.SPACE);
                    specialSpanLength = 1;
                    prefixAdded = 1;
                } else {
                    specialSpanLength = 1;
                }
                Object[] objArr5 = new Object[specialSpanLength];
                objArr5[0] = new VectorImageSpan(textView, R.drawable.ic_snoo_24dp, 0);
                spannableTextViewStringBuilder.appendWithSpans("special", objArr5);
            }
            if (post2.isAuthorCakeday()) {
                if (prefixAdded == 0) {
                    spannableTextViewStringBuilder.append(StringUtils.SPACE);
                    cakedaySpanLength = 1;
                    prefixAdded = 1;
                } else {
                    cakedaySpanLength = 1;
                }
                Object[] objArr6 = new Object[cakedaySpanLength];
                objArr6[0] = new VectorImageSpan(textView, R.drawable.cake_variant, 0);
                spannableTextViewStringBuilder.appendWithSpans("cakeday", objArr6);
            }
            if (post2.isStickied()) {
                if (prefixAdded == 0) {
                    spannableTextViewStringBuilder.append(StringUtils.SPACE);
                    prefixAdded = 1;
                }
                spannableTextViewStringBuilder.appendWithSpans("Pinned", new VectorImageSpan(textView, R.drawable.ic_pin_black_24dp, 0));
            }
            if (post2.isAccountNew() && SettingsSingleton.getSettings().highlightNewAccounts) {
                if (prefixAdded == 0) {
                    spannableTextViewStringBuilder.append(StringUtils.SPACE);
                    newAccountSpanLength = 1;
                    prefixAdded = 1;
                } else {
                    newAccountSpanLength = 1;
                }
                Object[] objArr7 = new Object[newAccountSpanLength];
                objArr7[0] = new VectorImageSpan(textView, R.drawable.baby_carriage, 0);
                spannableTextViewStringBuilder.appendWithSpans("new-account", objArr7);
            }
            if (NewUserTagManager.getInstance().hasTag(post2.getAuthor())) {
                if (prefixAdded == 0) {
                    spannableTextViewStringBuilder.append(StringUtils.SPACE);
                }
                spannableTextViewStringBuilder.appendWithSpans("(" + NewUserTagManager.getInstance().getTag(post2.getAuthor()) + ")", SpanSingleton.newLinkColorSpan6());
            }
        }
        if (SettingsSingleton.getSettings().commentsFlair) {
            appendFlair(spannableTextViewStringBuilder, textView, post2);
        }
        spannableTextViewStringBuilder.append(StringUtils.SPACE);
        if (post2.getNumReports() > 0) {
            spannableTextViewStringBuilder.appendWithSpans("Reports: " + post2.getNumReports() + StringUtils.SPACE, SpanSingleton.getGoldSpan());
        }
        if (!TextUtils.isEmpty(post2.getBannedBy())) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("[Removed: ");
            sb2.append("true".equals(post2.getBannedBy()) ? "Auto" : post2.getBannedBy());
            sb2.append("] ");
            spannableTextViewStringBuilder.appendWithSpans(sb2.toString(), SpanSingleton.getCrosspostSpan());
        }
        if (SettingsSingleton.getSettings().delimitCommentScores) {
            spannableTextViewStringBuilder.append("• ");
        }
        int scoreStart = spannableTextViewStringBuilder.length();
        if (post2.isScoreHidden()) {
            spannableTextViewStringBuilder.append("[score hidden]");
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(post2.getScore());
            sb3.append(" point");
            sb3.append(post2.getScore() == 1 ? "" : "s");
            spannableTextViewStringBuilder.append(sb3.toString());
            if (post2.getControversiality() > 0 && SettingsSingleton.getSettings().commentsControversial) {
                int daggerStart = spannableTextViewStringBuilder.length();
                spannableTextViewStringBuilder.append("†");
                int daggerEnd = spannableTextViewStringBuilder.length();
                spannableTextViewStringBuilder.addSpan(new SuperscriptSpan(), daggerStart, daggerEnd, 33);
                spannableTextViewStringBuilder.addSpan(new RelativeSizeSpan(0.7f), daggerStart, daggerEnd, 33);
            }
        }
        int scoreEnd = spannableTextViewStringBuilder.length();
        if (post2.getLikes() == 1) {
            spannableTextViewStringBuilder.addSpan(SpanSingleton.getHighlightSpan(), scoreStart, scoreEnd, 17);
        } else if (post2.getLikes() == -1) {
            spannableTextViewStringBuilder.addSpan(SpanSingleton.getSpoilerSpan(), scoreStart, scoreEnd, 17);
        }
        spannableTextViewStringBuilder.append(" • ");
        spannableTextViewStringBuilder.append(post2.getTimestamp());
        if (StringUtils.isNotEmpty(post2.getEditedTimestamp())) {
            spannableTextViewStringBuilder.append("*");
            spannableTextViewStringBuilder.append(" (last edited ");
            spannableTextViewStringBuilder.append(post2.getEditedTimestamp());
            spannableTextViewStringBuilder.append(")");
        }
        if (!TextUtils.isEmpty(post2.getApprovedBy())) {
            spannableTextViewStringBuilder.append(StringUtils.SPACE);
            spannableTextViewStringBuilder.append("✓");
        }
        if (SettingsSingleton.getSettings().awardsMinimal && post2.hasAwards()) {
            spannableTextViewStringBuilder.append(" • ");
            int gildedStart = spannableTextViewStringBuilder.length();
            spannableTextViewStringBuilder.append("gilded");
            spannableTextViewStringBuilder.addSpan(SpanSingleton.getAwardImageSpan(), gildedStart, spannableTextViewStringBuilder.length(), 33);
            spannableTextViewStringBuilder.append(StringUtils.SPACE);
            spannableTextViewStringBuilder.append(post2.getAwardCount() + StringUtils.SPACE);
            spannableTextViewStringBuilder.append("award");
            if (post2.getAwardCount() > 1) {
                spannableTextViewStringBuilder.append("s");
            }
        }
        if (z) {
            spannableTextViewStringBuilder.append(" • ");
            spannableTextViewStringBuilder.append(post2.getSubreddit());
        }
    }

    public static void formatMessageSubject(SpannableTextViewStringBuilder spannableTextViewStringBuilder, Message message) {
        spannableTextViewStringBuilder.reset();
        appendSpan(spannableTextViewStringBuilder, SpanSingleton.getBoldSpan(), message.subject);
        spannableTextViewStringBuilder.append(StringUtils.SPACE);
        if (message.kind == 1) {
            appendSpan(spannableTextViewStringBuilder, SpanSingleton.getItalicSpan(), message.linkTitle);
        }
    }

    public static void formatMessageHeader(SpannableTextViewStringBuilder spannableTextViewStringBuilder, Message message) {
        if (!message.author.equalsIgnoreCase(AccountSingleton.getInstance().getUsername())) {
            spannableTextViewStringBuilder.append("from ");
            int authorStart = spannableTextViewStringBuilder.length();
            spannableTextViewStringBuilder.append(message.author);
            int authorEnd = spannableTextViewStringBuilder.length();
            if (SettingsSingleton.getSettings().tapUsernamesOpen) {
                spannableTextViewStringBuilder.addSpan(new CustomUrlSpan("/u/" + message.author), authorStart, authorEnd, 33);
            }
            spannableTextViewStringBuilder.addSpan(SpanSingleton.getBoldSpan(), authorStart, authorEnd, 33);
            spannableTextViewStringBuilder.addSpan(SpanSingleton.newLinkColorSpan3(), authorStart, authorEnd, 33);
            if (message.depth > 0) {
                spannableTextViewStringBuilder.append(" to ");
                int destStart = spannableTextViewStringBuilder.length();
                spannableTextViewStringBuilder.append(message.dest);
                int destEnd = spannableTextViewStringBuilder.length();
                if (SettingsSingleton.getSettings().tapUsernamesOpen) {
                    spannableTextViewStringBuilder.addSpan(new CustomUrlSpan("/u/" + message.dest), destStart, destEnd, 33);
                }
            }
        } else if (message.depth > 0) {
            spannableTextViewStringBuilder.append("to ");
            int destStart = spannableTextViewStringBuilder.length();
            spannableTextViewStringBuilder.append(message.dest);
            int destEnd = spannableTextViewStringBuilder.length();
            if (SettingsSingleton.getSettings().tapUsernamesOpen) {
                spannableTextViewStringBuilder.addSpan(new CustomUrlSpan("/u/" + message.dest), destStart, destEnd, 33);
            }
            spannableTextViewStringBuilder.append(StringUtils.SPACE);
            spannableTextViewStringBuilder.append("from ");
            int authorStart = spannableTextViewStringBuilder.length();
            spannableTextViewStringBuilder.append(message.author);
            int authorEnd = spannableTextViewStringBuilder.length();
            if (SettingsSingleton.getSettings().tapUsernamesOpen) {
                spannableTextViewStringBuilder.addSpan(new CustomUrlSpan("/u/" + message.author), authorStart, authorEnd, 33);
            }
            spannableTextViewStringBuilder.addSpan(SpanSingleton.getBoldSpan(), authorStart, authorEnd, 33);
            spannableTextViewStringBuilder.addSpan(SpanSingleton.newLinkColorSpan3(), authorStart, authorEnd, 33);
        } else {
            spannableTextViewStringBuilder.append("to ");
            int destStart = spannableTextViewStringBuilder.length();
            spannableTextViewStringBuilder.append(message.dest);
            int destEnd = spannableTextViewStringBuilder.length();
            if (SettingsSingleton.getSettings().tapUsernamesOpen) {
                spannableTextViewStringBuilder.addSpan(new CustomUrlSpan("/u/" + message.dest), destStart, destEnd, 33);
            }
            spannableTextViewStringBuilder.addSpan(SpanSingleton.getBoldSpan(), destStart, destEnd, 33);
            spannableTextViewStringBuilder.addSpan(SpanSingleton.newLinkColorSpan3(), destStart, destEnd, 33);
            spannableTextViewStringBuilder.append(StringUtils.SPACE);
            spannableTextViewStringBuilder.append("from ");
            int authorStart = spannableTextViewStringBuilder.length();
            spannableTextViewStringBuilder.append(message.author);
            int authorEnd = spannableTextViewStringBuilder.length();
            if (SettingsSingleton.getSettings().tapUsernamesOpen) {
                spannableTextViewStringBuilder.addSpan(new CustomUrlSpan("/u/" + message.author), authorStart, authorEnd, 33);
            }
        }
        int kind = message.kind;
        if (kind == 1 || (kind == 4 && message.comment)) {
            spannableTextViewStringBuilder.append(" via ");
            int subredditStart = spannableTextViewStringBuilder.length();
            spannableTextViewStringBuilder.append("/r/");
            spannableTextViewStringBuilder.append(message.subreddit);
            int subredditEnd = spannableTextViewStringBuilder.length();
            if (SettingsSingleton.getSettings().tapSubredditsOpen) {
                spannableTextViewStringBuilder.addSpan(new CustomUrlSpan("/r/" + message.subreddit), subredditStart, subredditEnd, 33);
            }
            spannableTextViewStringBuilder.addSpan(SpanSingleton.newLinkColorSpan4(), subredditStart, subredditEnd, 33);
            spannableTextViewStringBuilder.addSpan(SpanSingleton.getBoldSpanAlt(), subredditStart, subredditEnd, 33);
        }
        spannableTextViewStringBuilder.append(" sent ");
        spannableTextViewStringBuilder.append(message.timeAgo);
    }

    public static void formatPostInfo(SpannableTextViewStringBuilder spannableTextViewStringBuilder, TextView textView, Post post, int section, boolean showSubreddit, boolean showScore, boolean expanded, boolean showDomain) {
        boolean prefixAdded;
        if (!expanded && showSubreddit) {
            if (section == 5 || section == 8) {
                spannableTextViewStringBuilder.append("r/");
                int subredditStart = spannableTextViewStringBuilder.length();
                spannableTextViewStringBuilder.append(post.getSubreddit());
                int subredditEnd = spannableTextViewStringBuilder.length();
                spannableTextViewStringBuilder.addSpan(SpanSingleton.getBoldSpanAlt(), subredditStart, subredditEnd, 33);
                if (SettingsSingleton.getSettings().tapSubredditsOpen) {
                    spannableTextViewStringBuilder.addSpan(new SilentCustomUrlSpan("/r/" + post.getSubreddit()), subredditStart, subredditEnd, 33);
                }
                spannableTextViewStringBuilder.addSpan(SpanSingleton.newLinkColorSpan5(), subredditStart, subredditEnd, 33);
                spannableTextViewStringBuilder.append("\n");
            } else {
                int subredditStart = spannableTextViewStringBuilder.length();
                spannableTextViewStringBuilder.append(post.getSubreddit());
                int subredditEnd = spannableTextViewStringBuilder.length();
                spannableTextViewStringBuilder.addSpan(SpanSingleton.getBoldSpanAlt(), subredditStart, subredditEnd, 33);
                if (SettingsSingleton.getSettings().tapSubredditsOpen) {
                    spannableTextViewStringBuilder.addSpan(new SilentCustomUrlSpan("/r/" + post.getSubreddit()), subredditStart, subredditEnd, 33);
                }
                spannableTextViewStringBuilder.addSpan(SpanSingleton.newLinkColorSpan5(), subredditStart, subredditEnd, 33);
                spannableTextViewStringBuilder.append(" • ");
            }
        }
        if (SettingsSingleton.getSettings().postsShowAuthor || expanded) {
            int authorStart = spannableTextViewStringBuilder.length();
            spannableTextViewStringBuilder.append(post.getAuthor());
            int authorEnd = spannableTextViewStringBuilder.length();
            if (SettingsSingleton.getSettings().tapUsernamesOpen) {
                spannableTextViewStringBuilder.addSpan(new SilentCustomUrlSpan("/u/" + post.getAuthor()), authorStart, authorEnd, 33);
            }
            if (com.resync.app.premium.UserPaintManager.getInstance().hasPaint(post.getAuthor())) {
                spannableTextViewStringBuilder.addSpan(new MonetRoundedCornerSpan(com.resync.app.premium.UserPaintManager.getInstance().getColor(post.getAuthor()).intValue()), authorStart, authorEnd, 33);
            }
            // ReSync: removed original-developer ("ljdawson") badge.
            prefixAdded = false;
            if (post.getAuthor().equalsIgnoreCase(AccountSingleton.getInstance().getUsername())) {
                if (SettingsSingleton.getSettings().highlightYourself) {
                    if (!prefixAdded) {
                        spannableTextViewStringBuilder.append(StringUtils.SPACE);
                        prefixAdded = true;
                    }
                    spannableTextViewStringBuilder.appendWithSpans("you", new VectorImageSpan(textView, R.drawable.filled_in_person, 0));
                }
            } else if (FriendsManager.getInstance().getFriends() != null && FriendsManager.getInstance().getFriends().length > 0) {
                for (String str : FriendsManager.getInstance().getFriends()) {
                    if (StringUtils.equalsIgnoreCase(str, post.getAuthor())) {
                        if (!prefixAdded) {
                            spannableTextViewStringBuilder.append(StringUtils.SPACE);
                            prefixAdded = true;
                        }
                        spannableTextViewStringBuilder.appendWithSpans("friend", new VectorImageSpan(textView, R.drawable.emoticon_happy, 0));
                    }
                }
            }
            if ("automoderator".equalsIgnoreCase(post.getAuthor())) {
                if (!prefixAdded) {
                    spannableTextViewStringBuilder.append(StringUtils.SPACE);
                    prefixAdded = true;
                }
                spannableTextViewStringBuilder.appendWithSpans("automod", new VectorImageSpan(textView, R.drawable.robot_excited, 0));
            }
            if ("moderator".equalsIgnoreCase(post.getDistinguished())) {
                if (!prefixAdded) {
                    spannableTextViewStringBuilder.append(StringUtils.SPACE);
                    prefixAdded = true;
                }
                spannableTextViewStringBuilder.appendWithSpans("mod", new VectorImageSpan(textView, R.drawable.shield_half_full, 0));
            }
            if ("admin".equalsIgnoreCase(post.getDistinguished())) {
                if (!prefixAdded) {
                    spannableTextViewStringBuilder.append(StringUtils.SPACE);
                    prefixAdded = true;
                }
                spannableTextViewStringBuilder.appendWithSpans("admin", new VectorImageSpan(textView, R.drawable.ic_snoo_24dp, 0));
            }
            if ("special".equalsIgnoreCase(post.getDistinguished())) {
                if (!prefixAdded) {
                    spannableTextViewStringBuilder.append(StringUtils.SPACE);
                    prefixAdded = true;
                }
                spannableTextViewStringBuilder.appendWithSpans("special", new VectorImageSpan(textView, R.drawable.ic_snoo_24dp, 0));
            }
            if (post.isAuthorCakeday()) {
                if (!prefixAdded) {
                    spannableTextViewStringBuilder.append(StringUtils.SPACE);
                    prefixAdded = true;
                }
                spannableTextViewStringBuilder.appendWithSpans("cakeday", new VectorImageSpan(textView, R.drawable.cake_variant, 0));
            }
            if (post.isAccountNew() && SettingsSingleton.getSettings().highlightNewAccounts) {
                if (!prefixAdded) {
                    spannableTextViewStringBuilder.append(StringUtils.SPACE);
                    prefixAdded = true;
                }
                spannableTextViewStringBuilder.appendWithSpans("new-account", new VectorImageSpan(textView, R.drawable.baby_carriage, 0));
            }
            if (NewUserTagManager.getInstance().hasTag(post.getAuthor())) {
                if (!prefixAdded) {
                    spannableTextViewStringBuilder.append(StringUtils.SPACE);
                }
                spannableTextViewStringBuilder.appendWithSpans("(" + NewUserTagManager.getInstance().getTag(post.getAuthor()) + ")", SpanSingleton.newLinkColorSpan6());
            }
            if (SettingsSingleton.getSettings().commentsFlair) {
                appendFlair(spannableTextViewStringBuilder, textView, post);
            }
            spannableTextViewStringBuilder.append(" • ");
        }
        if (SettingsSingleton.getSettings().postOc && post.isOriginalContent()) {
            int ocStart = spannableTextViewStringBuilder.length();
            spannableTextViewStringBuilder.append("OC ");
            spannableTextViewStringBuilder.addSpan(new RadiusBgSpan(ColorUtils.setAlphaComponent(MonetUtil.getSecondaryTextColorAuto(), 20), ColorUtils.setAlphaComponent(MonetUtil.getSecondaryTextColorAuto(), 31), MonetUtil.getSecondaryTextColorAuto(), 10), ocStart, spannableTextViewStringBuilder.length() - 1, 33);
            spannableTextViewStringBuilder.append(" • ");
        }
        if (!TextUtils.isEmpty(post.getBannedBy())) {
            ForegroundColorSpan foregroundColorSpanK = SpanSingleton.getGoldSpan();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("[Removed: ");
            sb2.append("true".equals(post.getBannedBy()) ? "Auto" : post.getBannedBy());
            sb2.append("] ");
            appendSpan(spannableTextViewStringBuilder, foregroundColorSpanK, sb2.toString());
            spannableTextViewStringBuilder.append(" • ");
        }
        if (post.getNumReports() > 0) {
            appendSpan(spannableTextViewStringBuilder, SpanSingleton.getGoldSpan(), "Reports: " + post.getNumReports());
            spannableTextViewStringBuilder.append(" • ");
        }
        if (post.isOver18() && !expanded) {
            appendSpan(spannableTextViewStringBuilder, SpanSingleton.getNsfwSpan(), "NSFW");
            spannableTextViewStringBuilder.append(" • ");
        }
        if (TextUtils.isEmpty(post.getTimestamp())) {
            CrashlyticsHelper.logEvent(6, "TextFormatterManager", post.toString());
        }
        spannableTextViewStringBuilder.append(post.getTimestamp());
        if (StringUtils.isNotEmpty(post.getEditedTimestamp())) {
            spannableTextViewStringBuilder.append("*");
            spannableTextViewStringBuilder.append(" (last edited ");
            spannableTextViewStringBuilder.append(post.getEditedTimestamp());
            spannableTextViewStringBuilder.append(")");
        }
        if (showDomain && !TextUtils.isEmpty(post.getDomain()) && !TextUtils.isEmpty(post.getSubreddit()) && !post.getSubreddit().equalsIgnoreCase(post.getDomainRaw())) {
            spannableTextViewStringBuilder.append(" • ");
            int domainStart = spannableTextViewStringBuilder.length();
            spannableTextViewStringBuilder.append(post.getDomain());
            int domainEnd = spannableTextViewStringBuilder.length();
            if (SettingsSingleton.getSettings().tapDomainsOpen) {
                spannableTextViewStringBuilder.addSpan(new SilentCustomUrlSpan("https://reddit.com/domain/" + post.getDomain()), domainStart, domainEnd, 33);
            }
        }
        if ((section == 8 || SettingsSingleton.getSettings().flairMinimal) && StringUtils.isNotEmpty(post.getLinkFlairText())) {
            spannableTextViewStringBuilder.append(" • ");
            String[] strArrSplit = StringUtils.split(post.getLinkFlairText(), ",");
            int i = 0;
            while (i < strArrSplit.length) {
                String strTrimToEmpty = strArrSplit[i];
                if (strTrimToEmpty != null) {
                    strTrimToEmpty = StringUtils.trimToEmpty(strTrimToEmpty);
                }
                if (!LinkTypeHelper.isImage(strTrimToEmpty) && StringUtils.isNotEmpty(strTrimToEmpty) && strTrimToEmpty.length() > 1) {
                    int flairStart = spannableTextViewStringBuilder.length();
                    spannableTextViewStringBuilder.append(strTrimToEmpty);
                    spannableTextViewStringBuilder.addSpan(SpanSingleton.newLinkColorSpan5(), flairStart, spannableTextViewStringBuilder.length(), 33);
                }
                i++;
                if (i < strArrSplit.length) {
                    spannableTextViewStringBuilder.append(StringUtils.SPACE);
                }
            }
        }
        if ((section == 8 || SettingsSingleton.getSettings().awardsMinimal) && post.hasAwards()) {
            spannableTextViewStringBuilder.append(" • ");
            int gildedStart = spannableTextViewStringBuilder.length();
            spannableTextViewStringBuilder.append("gilded");
            spannableTextViewStringBuilder.addSpan(SpanSingleton.getAwardImageSpan(), gildedStart, spannableTextViewStringBuilder.length(), 33);
            spannableTextViewStringBuilder.append(StringUtils.SPACE);
            spannableTextViewStringBuilder.append(post.getAwardCount() + StringUtils.SPACE);
            spannableTextViewStringBuilder.append("award");
            if (post.getAwardCount() > 1) {
                spannableTextViewStringBuilder.append("s");
            }
        }
        if (post.getViewCount() > 0) {
            spannableTextViewStringBuilder.append(" • ");
            spannableTextViewStringBuilder.append(ViewsHelper.formatViews(post.getViewCount()));
        }
        if (!TextUtils.isEmpty(post.getUpvoteRatio()) && expanded) {
            spannableTextViewStringBuilder.append(" • ");
            spannableTextViewStringBuilder.append(post.getUpvoteRatio());
            spannableTextViewStringBuilder.append(" upvoted");
        }
        if (showScore) {
            spannableTextViewStringBuilder.append("\n");
            int scoreStart = spannableTextViewStringBuilder.length();
            if (SettingsSingleton.getSettings().postScoresShort) {
                spannableTextViewStringBuilder.append(ScoreHelper.formatPoints(post.getScore()));
            } else {
                spannableTextViewStringBuilder.append(ScoreHelper.formatPointsLabel(post.getScore()));
            }
            int scoreEnd = spannableTextViewStringBuilder.length();
            if (post.isUpvoted()) {
                spannableTextViewStringBuilder.addSpan(SpanSingleton.getHighlightSpan(), scoreStart, scoreEnd, 17);
            } else if (post.isDownvoted()) {
                spannableTextViewStringBuilder.addSpan(SpanSingleton.getSpoilerSpan(), scoreStart, scoreEnd, 17);
            }
            spannableTextViewStringBuilder.append(" • ");
            spannableTextViewStringBuilder.append(post.getNumComments() + " comments");
            if (!expanded && post.getNewCommentCount() > 0 && SettingsSingleton.getSettings().postsDisplayNew && post.isVisitedOrInternal()) {
                appendSpan(spannableTextViewStringBuilder, new ForegroundColorSpan(-65536), " (+" + post.getNewCommentCount() + ")");
            }
        }
        if (StringUtils.isNotEmpty(post.getPostCrossLink())) {
            spannableTextViewStringBuilder.appendWithSpans(" • xpost from " + post.getPostCrossOrigin(), SpanSingleton.newLinkColorSpan(), new CustomUrlSpan(post.getPostCrossLink()));
        }
        spannableTextViewStringBuilder.append(StringUtils.CR);
    }

    public static void formatPostTitle(SpannableTextViewStringBuilder spannableTextViewStringBuilder, TextView textView, Post post) {
        if (!com.resync.app.util.TextUtils.isEmpty(post.getTitle())) {
            spannableTextViewStringBuilder.append(post.getTitle().trim());
            if (!TextUtils.isEmpty(post.getApprovedBy())) {
                spannableTextViewStringBuilder.appendWithSpans(" ✓", new RelativeSizeSpan(0.8f));
            }
        }
        if (post.isStickied()) {
            spannableTextViewStringBuilder.append(StringUtils.SPACE);
            spannableTextViewStringBuilder.appendWithSpans("Pinned", new VectorImageSpan((int) (textView.getLineHeight() * 0.8f), R.drawable.ic_pin_black_24dp, 0));
        }
        if (post.isArchived()) {
            spannableTextViewStringBuilder.append(StringUtils.SPACE);
            spannableTextViewStringBuilder.appendWithSpans("Archived", new VectorImageSpan((int) (textView.getLineHeight() * 0.8f), R.drawable.baseline_archive_24, 0));
        }
        if (post.isLocked()) {
            spannableTextViewStringBuilder.append(StringUtils.SPACE);
            spannableTextViewStringBuilder.appendWithSpans("Locked", new VectorImageSpan((int) (textView.getLineHeight() * 0.8f), R.drawable.baseline_lock_24, 0));
        }
    }
}
