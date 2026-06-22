package com.resync.app.text.spans;

import android.content.ContentValues;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;
import android.view.View;
import com.bumptech.glide.RedditHelper;
import com.resync.app.RedditApplication;
import com.resync.app.provider.RedditProvider;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.text.spans.CustomClickableSpan;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: mb.k */
/* JADX INFO: compiled from: SpoilerSpan.java */
/* JADX INFO: loaded from: classes3.dex */
public class SpoilerSpan extends CharacterStyle implements UpdateAppearance, CustomClickableSpan {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Post post;
    private final int spoilerIndex;
    private final int spoilerColor = MonetUtil.getLinkColorAuto();

    public SpoilerSpan(Post post, int spoilerIndex) {
        this.post = post;
        this.spoilerIndex = spoilerIndex + 1;
    }

    public int getSpoilerColor() {
        return this.spoilerColor;
    }

    @Override // com.resync.app.text.spans.CustomClickableSpan
    public void onClick(View view) {
        Post post = this.post;
        if (post != null) {
            if (post.getSyncType() == 11) {
                try {
                    Logger.debug("LOOKING FOR: " + this.spoilerIndex);
                    int iOrdinalIndexOf = StringUtils.ordinalIndexOf(this.post.getBodyRaw(), ">!", this.spoilerIndex);
                    int iOrdinalIndexOf2 = StringUtils.ordinalIndexOf(this.post.getBodyRaw(), "!<", this.spoilerIndex);
                    Logger.debug("START INDEX: " + iOrdinalIndexOf);
                    Logger.debug("END INDEX: " + iOrdinalIndexOf2);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(this.post.getBodyRaw().substring(0, iOrdinalIndexOf));
                    sb2.append(this.post.getBodyRaw().substring(iOrdinalIndexOf + 2, iOrdinalIndexOf2));
                    sb2.append(this.post.getBodyRaw().substring(iOrdinalIndexOf2 + 2));
                    Logger.debug("BEFORE: " + this.post.getBodyRaw());
                    Logger.debug("AFTER: " + ((Object) sb2));
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("body_raw", sb2.toString());
                    contentValues.put("body_processed", RedditHelper.processMarkdownToHtml(null, sb2.toString()));
                    RedditApplication.getAppContext().getContentResolver().update(RedditProvider.URI_RESTORE_REMOVED, contentValues, this.post.getId(), null);
                    RedditApplication.getAppContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
                    return;
                } catch (Exception unused) {
                    StaticToast.show("Error removing spoiler");
                }
            } else if (this.post.getSyncType() == 2) {
                try {
                    Logger.debug("LOOKING FOR: " + this.spoilerIndex);
                    int iOrdinalIndexOf3 = StringUtils.ordinalIndexOf(this.post.getSelftextRaw(), ">!", this.spoilerIndex);
                    int iOrdinalIndexOf4 = StringUtils.ordinalIndexOf(this.post.getSelftextRaw(), "!<", this.spoilerIndex);
                    Logger.debug("START INDEX: " + iOrdinalIndexOf3);
                    Logger.debug("END INDEX: " + iOrdinalIndexOf4);
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(this.post.getSelftextRaw().substring(0, iOrdinalIndexOf3));
                    sb3.append(this.post.getSelftextRaw().substring(iOrdinalIndexOf3 + 2, iOrdinalIndexOf4));
                    sb3.append(this.post.getSelftextRaw().substring(iOrdinalIndexOf4 + 2));
                    Logger.debug("BEFORE: " + this.post.getBodyRaw());
                    Logger.debug("AFTER: " + ((Object) sb3));
                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put("selftext_raw", sb3.toString());
                    contentValues2.put("selftext_processed", RedditHelper.processMarkdownToHtml(null, sb3.toString()));
                    RedditApplication.getAppContext().getContentResolver().update(RedditProvider.URI_RESTORE_REMOVED, contentValues2, this.post.getId(), null);
                    RedditApplication.getAppContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
                    return;
                } catch (Exception unused2) {
                    StaticToast.show("Error removing spoiler");
                    return;
                }
            }
        }
        StaticToast.show("Missing post information");
    }

    @Override // com.resync.app.text.spans.CustomClickableSpan
    public void onLongClick(View view) {
    }

    @Override // android.text.style.CharacterStyle
    public void updateDrawState(TextPaint textPaint) {
        textPaint.bgColor = this.spoilerColor;
        textPaint.setColor(getSpoilerColor());
    }
}
