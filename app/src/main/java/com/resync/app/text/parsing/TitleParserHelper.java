package com.resync.app.text.parsing;

import org.apache.commons.lang3.StringUtils;
import com.resync.app.util.TextUtils;
import com.resync.app.util.WebsafeUtils;

/* JADX INFO: renamed from: c9.f */
/* JADX INFO: compiled from: TitleParserHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class TitleParserHelper {
    public static final String parseTitle(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String unescapedTitle = WebsafeUtils.unescapeHtml(str);
        if (TextUtils.isEmpty(unescapedTitle)) {
            return unescapedTitle;
        }
        return unescapedTitle + StringUtils.CR;
    }
}
