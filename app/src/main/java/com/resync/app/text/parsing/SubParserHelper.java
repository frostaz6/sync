package com.resync.app.text.parsing;

import android.content.ContentValues;
import android.content.Context;
import com.bumptech.glide.RedditHelper;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.media.images.PostValuehelper;
import com.resync.app.managers.SubredditIconManager;
import com.resync.app.helpers.TimestampHelper;
import com.resync.app.util.TextUtils;
import com.resync.app.util.WebsafeUtils;

/* JADX INFO: renamed from: c9.e */
/* JADX INFO: compiled from: SubParserHelper.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class SubParserHelper {
    public static void parseSubreddit(Context context, int kind, JSONObject jSONObject, ArrayList<ContentValues> arrayList) throws JSONException {
        if (context == null || jSONObject == null || arrayList == null) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        String string = jSONObject.getString("display_name");
        contentValues.put("_id", jSONObject.getString("id"));
        contentValues.put("hash", Integer.valueOf(jSONObject.getString("id").hashCode()));
        contentValues.put("kind", Integer.valueOf(kind));
        contentValues.put("subreddit", string);
        contentValues.put("title", jSONObject.getString("title"));
        contentValues.put("author", RedditHelper.processMarkdownToHtml(null, jSONObject.getString("public_description")));
        contentValues.put("likes", Integer.valueOf(jSONObject.optInt("subscribers")));
        contentValues.put("timestamp", TimestampHelper.getRelativeTime(jSONObject.optLong("created_utc")));
        contentValues.put("over_18", Integer.valueOf(PostValuehelper.boolToInt(jSONObject.optBoolean("over18"))));
        contentValues.put("sync_type", (Integer) 15);
        contentValues.put("hidden", (Integer) 0);
        String strOptString = jSONObject.optString("community_icon");
        if (TextUtils.isEmpty(strOptString)) {
            String strOptString2 = jSONObject.optString("icon_img");
            if (!TextUtils.isEmpty(strOptString2)) {
                SubredditIconManager.getInstance().setIcon(string, WebsafeUtils.unescapeHtml(strOptString2));
            }
        } else {
            SubredditIconManager.getInstance().setIcon(string, WebsafeUtils.unescapeHtml(strOptString));
        }
        arrayList.add(contentValues);
    }
}
