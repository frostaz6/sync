package com.resync.app.media.images;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.ui.viewholders.posts.PostCommentHolder;
import com.resync.app.ui.viewholders.posts.card.CardImageHolder;
import com.resync.app.ui.viewholders.posts.card.CardLinkHolder;
import com.resync.app.ui.viewholders.posts.card.CardSelftextHolder;
import com.resync.app.ui.viewholders.posts.compact.CompactHolder;
import com.resync.app.ui.viewholders.posts.simple.SimpleHolder;
import com.resync.app.ui.viewholders.posts.simple_swipe.SimpleSwipeHolder;
import com.resync.app.ui.viewholders.posts.small_cards.SmallCardHolder;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.media.imgur.ImgurImageHelper;
import com.resync.app.holders.DividerHolder;
import com.resync.app.helpers.DataSavingHelper;
import com.resync.app.util.TextUtils;
import com.resync.app.util.WebsafeUtils;
import com.resync.app.util.links.LinkTypeHelper;
import com.resync.app.presenters.BaseCursorPresenter;

/* JADX INFO: renamed from: c8.e */
/* JADX INFO: compiled from: SyncTypeHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class SyncTypeHelper {

    /* JADX INFO: renamed from: c8.e$a */
    /* JADX INFO: compiled from: SyncTypeHelper.java */
    public static final class UnsupportedSyncTypeException extends RuntimeException {
        public UnsupportedSyncTypeException(String str) {
            super("Unsupported Sync Type: " + str);
        }

        public UnsupportedSyncTypeException(int syncType) {
            super("Unsupported Sync Type: " + syncType);
        }
    }

    public static boolean supportsSwipeActions(int syncType) {
        return (syncType == 11 || syncType == 12 || syncType == 15 || syncType == 17) ? false : true;
    }

    public static RecyclerView.ViewHolder createCardHolder(Context context, ViewGroup viewGroup, BaseCursorPresenter baseCursorPresenter, int syncType, int uiMode) {
        if (DataSavingHelper.isFullDataSaving() && (syncType == 1 || syncType == 3 || syncType == 6)) {
            return CardLinkHolder.create(context, viewGroup, baseCursorPresenter, uiMode);
        }
        if (syncType == 1) {
            return CardImageHolder.create(context, viewGroup, baseCursorPresenter, uiMode);
        }
        if (syncType == 2) {
            return CardSelftextHolder.create(context, viewGroup, baseCursorPresenter, uiMode);
        }
        if (syncType == 3 || syncType == 6) {
            return CardLinkHolder.create(context, viewGroup, baseCursorPresenter, uiMode);
        }
        if (syncType == 11) {
            return PostCommentHolder.create(context, viewGroup, baseCursorPresenter, uiMode);
        }
        if (syncType == 17) {
            return DividerHolder.create(context, viewGroup);
        }
        throw new UnsupportedSyncTypeException(syncType);
    }

    public static int getCardViewType(int syncType) {
        int viewType = 1;
        if (syncType != 1 && syncType != 7 && syncType != 4 && syncType != 10 && syncType != 9) {
            viewType = 2;
            if (syncType != 2 && syncType != 5) {
                viewType = 3;
                if (syncType != 3 && syncType != 6) {
                    if (syncType == 11) {
                        return 11;
                    }
                    if (syncType == 17) {
                        return 17;
                    }
                    throw new UnsupportedSyncTypeException("Unsupported mode: " + syncType);
                }
            }
        }
        return viewType;
    }

    public static int getSimpleViewType(int syncType) {
        if (syncType == 1 || syncType == 2 || syncType == 3 || syncType == 4 || syncType == 6 || syncType == 7 || syncType == 9 || syncType == 10) {
            return 1;
        }
        if (syncType == 11) {
            return 11;
        }
        if (syncType == 17) {
            return 17;
        }
        throw new UnsupportedSyncTypeException("Unsupported mode: " + syncType);
    }

    public static int getSwipeViewType(int syncType) {
        if (syncType == 1 || syncType == 2 || syncType == 3 || syncType == 4 || syncType == 6 || syncType == 7 || syncType == 9 || syncType == 10) {
            return 1;
        }
        if (syncType == 11) {
            return 11;
        }
        if (syncType == 17) {
            return 17;
        }
        throw new UnsupportedSyncTypeException("Unsupported mode: " + syncType);
    }

    public static int getCompactViewType(int syncType) {
        if (syncType == 1 || syncType == 7 || syncType == 4 || syncType == 10 || syncType == 9 || syncType == 2 || syncType == 5 || syncType == 3 || syncType == 6) {
            return 3;
        }
        if (syncType == 11) {
            return 11;
        }
        if (syncType == 17) {
            return 17;
        }
        throw new UnsupportedSyncTypeException("Unsupported mode: " + syncType);
    }

    public static RecyclerView.ViewHolder createCompactHolder(Context context, ViewGroup viewGroup, BaseCursorPresenter baseCursorPresenter, int syncType, int uiMode) {
        if (syncType == 3) {
            return CompactHolder.create(context, viewGroup, baseCursorPresenter, uiMode);
        }
        if (syncType == 11) {
            return PostCommentHolder.create(context, viewGroup, baseCursorPresenter, uiMode);
        }
        if (syncType == 17) {
            return DividerHolder.create(context, viewGroup);
        }
        throw new UnsupportedSyncTypeException(syncType);
    }

    public static int resolveSyncType(JSONObject jSONObject, int syncType) throws JSONException {
        if (syncType == 1) {
            return 11;
        }
        if (syncType == 9) {
            return 12;
        }
        if (syncType != 3) {
            throw new UnsupportedSyncTypeException(syncType);
        }
        String strOptString = jSONObject.optString("id");
        String url = WebsafeUtils.unescapeHtml(jSONObject.optString("url"));
        String strTrim = jSONObject.optString("selftext").trim();
        String strOptString2 = jSONObject.optString("subreddit");
        if (LinkTypeHelper.isTwitterVideo(url)) {
            return 3;
        }
        if (!TextUtils.isEmpty(url) && url.contains("imgur.com/gallery/xIolo")) {
            return 1;
        }
        if (ImgurImageHelper.isAlbum(url) || ImgurImageHelper.isMultiImage(url)) {
            return 6;
        }
        if (LinkTypeHelper.isYouTube(url)) {
            return 1;
        }
        if (url.endsWith(".gif") || url.endsWith(".gifv") || LinkTypeHelper.isStreamable(url) || LinkTypeHelper.isGfycat(url) || LinkTypeHelper.isRedditVideo(url)) {
            return 4;
        }
        if (LinkTypeHelper.isMixtape(url)) {
            return 3;
        }
        if (LinkTypeHelper.isImage(url)) {
            return 1;
        }
        String str = "https://www.reddit.com/r/" + strOptString2 + "/comments/" + strOptString;
        Locale locale = Locale.ENGLISH;
        String lowerCase = str.toLowerCase(locale);
        if (!android.text.TextUtils.isEmpty(url) && !url.toLowerCase(locale).startsWith(lowerCase)) {
            return 3;
        }
        if (android.text.TextUtils.isEmpty(strTrim)) {
            return (android.text.TextUtils.isEmpty(strTrim) && !android.text.TextUtils.isEmpty(url) && url.toLowerCase(locale).startsWith(lowerCase)) ? 2 : 3;
        }
        return 2;
    }

    public static RecyclerView.ViewHolder createSimpleHolder(Context context, ViewGroup viewGroup, BaseCursorPresenter baseCursorPresenter, int syncType) {
        if (syncType == 1 || syncType == 2 || syncType == 3 || syncType == 4 || syncType == 6 || syncType == 7 || syncType == 9 || syncType == 10) {
            return SimpleHolder.create(context, viewGroup, baseCursorPresenter);
        }
        if (syncType == 11) {
            return PostCommentHolder.create(context, viewGroup, baseCursorPresenter, 5);
        }
        if (syncType == 17) {
            return DividerHolder.create(context, viewGroup);
        }
        throw new UnsupportedSyncTypeException(syncType);
    }

    public static RecyclerView.ViewHolder createSwipeHolder(Context context, ViewGroup viewGroup, BaseCursorPresenter baseCursorPresenter, int syncType) {
        if (syncType == 1 || syncType == 2 || syncType == 3 || syncType == 4 || syncType == 6 || syncType == 7 || syncType == 9 || syncType == 10 || syncType == 11) {
            return SimpleSwipeHolder.create(context, viewGroup, baseCursorPresenter, 8);
        }
        if (syncType == 17) {
            return DividerHolder.create(context, viewGroup);
        }
        throw new UnsupportedSyncTypeException(syncType);
    }

    public static RecyclerView.ViewHolder createSmallCardHolder(Context context, ViewGroup viewGroup, BaseCursorPresenter baseCursorPresenter, int syncType) {
        if (syncType == 3) {
            return SmallCardHolder.create(context, viewGroup, baseCursorPresenter, 3);
        }
        if (syncType == 11) {
            return PostCommentHolder.create(context, viewGroup, baseCursorPresenter, 3);
        }
        if (syncType == 17) {
            return DividerHolder.create(context, viewGroup);
        }
        throw new UnsupportedSyncTypeException(syncType);
    }

    public static boolean isMediaPost(int syncType) {
        return syncType == 1 || syncType == 7 || syncType == 4 || syncType == 9 || syncType == 10;
    }
}
