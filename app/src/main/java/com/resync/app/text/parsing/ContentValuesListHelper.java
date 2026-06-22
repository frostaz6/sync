package com.resync.app.text.parsing;

import android.content.ContentValues;
import java.util.List;
import com.resync.app.db.RedditDb;

/* JADX INFO: renamed from: c9.b */
/* JADX INFO: compiled from: ContentValuesListHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class ContentValuesListHelper {
    public static ContentValues[] attachListMetadata(List<ContentValues> list, String listId, int startTime) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("lists_id", listId);
            contentValues.put("lists_post_id", list.get(i).getAsString("_id"));
            contentValues.put("lists_age", RedditDb.now());
            contentValues.put("lists_time", Integer.valueOf(startTime + i));
            list.add(contentValues);
        }
        return (ContentValues[]) list.toArray(new ContentValues[list.size()]);
    }
}
