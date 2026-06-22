package com.resync.app.managers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import com.resync.app.RedditApplication;
import com.resync.app.provider.RedditProvider;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;
import com.resync.app.text.parsing.ContentValuesListHelper;
import com.resync.app.media.ImageManager;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: d8.a0 */
/* JADX INFO: compiled from: WatchingHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class WatchingHelper {

    static final String TAG = "a0";

    public static void addWatchedPost(Post post) {
        if (post == null) {
            return;
        }
        ArrayList<Post> arrayListC = getWatchedPosts();
        arrayListC.add(0, post);
        saveWatchedPosts(arrayListC);
    }

    public static void clearWatchedPosts() {
        try {
            new File(ImageManager.getCacheDir(RedditApplication.getAppContext(), "serialized"), "watched_posts").delete();
        } catch (Exception e) {
            Logger.logException(e, false);
        }
    }

    static ArrayList<Post> getWatchedPosts() {
        try {
            return (ArrayList) SerializationUtils.deserialize(FileUtils.readFileToByteArray(new File(ImageManager.getCacheDir(RedditApplication.getAppContext(), "serialized"), "watched_posts")));
        } catch (Exception e) {
            Logger.logException(e, false);
            return new ArrayList<>();
        }
    }

    public static boolean isWatched(Post post) {
        Iterator<Post> it = getWatchedPosts().iterator();
        while (it.hasNext()) {
            if (post.getId().equalsIgnoreCase(it.next().getId())) {
                return true;
            }
        }
        return false;
    }

    public static void applyWatchedPosts(Context context) {
        ArrayList<Post> arrayListC = getWatchedPosts();
        Logger.verbose(TAG, "Watching pending: " + arrayListC.size());
        ArrayList arrayList = new ArrayList();
        Iterator<Post> it = arrayListC.iterator();
        while (it.hasNext()) {
            ContentValues contentValuesC1 = it.next().toContentValues();
            contentValuesC1.remove("lists_time");
            contentValuesC1.remove("lists_id");
            contentValuesC1.remove("internally_visited");
            arrayList.add(contentValuesC1);
        }
        context.getContentResolver().delete(RedditProvider.URI_DELETE_WHOLE_LIST, "list__watching", null);
        ContentValues[] contentValuesArrA = ContentValuesListHelper.attachListMetadata(arrayList, "list__watching", 0);
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = RedditProvider.URI_MULTIPLE_POSTS;
        int iBulkInsert = contentResolver.bulkInsert(uri, contentValuesArrA);
        context.getContentResolver().notifyChange(uri, null);
        Logger.verbose(TAG, "Watching: " + iBulkInsert);
    }

    public static void removeWatchedPost(Context context, Post post) {
        ArrayList<Post> arrayListC = getWatchedPosts();
        Iterator<Post> it = arrayListC.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Post next = it.next();
            if (post.getId().equalsIgnoreCase(next.getId())) {
                arrayListC.remove(next);
                break;
            }
        }
        saveWatchedPosts(arrayListC);
    }

    static void saveWatchedPosts(ArrayList<Post> arrayList) {
        try {
            FileUtils.writeByteArrayToFile(new File(ImageManager.getCacheDir(RedditApplication.getAppContext(), "serialized"), "watched_posts"), SerializationUtils.serialize(arrayList));
        } catch (Exception e) {
            Logger.logException(e, false);
        }
    }
}
