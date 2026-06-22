package com.resync.app.helpers;

import java.util.Locale;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: t7.p0 */
/* JADX INFO: compiled from: TranslationHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class TranslationHelper {
    public static final String capitalizeFirst(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String buildSelftextTranslation(Post post, String str, String str2) {
        return post.getBodyRaw() + "\n\n-----\n" + str2 + "\n\n[Translated by Google](translate)" + buildTranslationFooter(str);
    }

    public static String buildTranslationFooter(String str) {
        return "&nbsp;&nbsp;&nbsp;^*&#40;" + getLanguageName(str) + "* ^*to* ^*English&#41;*";
    }

    public static String getLanguageName(String str) {
        return capitalizeFirst(new Locale(str).getDisplayLanguage(new Locale("en")));
    }

    public static String buildCommentTranslation(Post post, String str, String str2) {
        return post.getSelftextRaw() + "\n\n-----\n" + str2.replaceAll("\n", "\n>") + "\n\n[Translated by Google](translate)" + buildTranslationFooter(str);
    }
}
