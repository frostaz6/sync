package com.resync.app.media.glide;

import android.net.Uri;
import android.os.Environment;
import androidx.core.content.FileProvider;
import com.resync.app.RedditApplication;
import java.io.File;

/* JADX INFO: renamed from: x7.b */
/* JADX INFO: compiled from: ImageHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class ImageHelper {
    public static Uri createTempImageUri() throws Exception {
        return FileProvider.getUriForFile(RedditApplication.getAppContext(), "com.resync.app.provider", File.createTempFile(Long.toString(System.currentTimeMillis()), ".jpg", RedditApplication.getAppContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
    }
}
