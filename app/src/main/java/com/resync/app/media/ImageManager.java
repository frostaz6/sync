package com.resync.app.media;

import android.content.Context;
import com.resync.app.RedditApplication;
import com.resync.app.singleton.AccountSingleton;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Locale;
import kotlin.UByte;
import org.apache.commons.lang3.time.DateUtils;
import com.resync.app.helpers.CrashlyticsHelper;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;

/* JADX INFO: renamed from: e8.a */
/* JADX INFO: compiled from: ImageManager.java */
/* JADX INFO: loaded from: classes.dex */
public class ImageManager {

    static String[] KNOWN_CACHE_DIRS = {"images", "okHttpCache", "volley", "serialized", "redditVideoCache", "org.chromium.android_webview", "Webview", "redditVideoCache", "manifestCache", "shared"};
    static String[] PROTECTED_DIRS = {"image_manager_disk_cache"};

    private static String toHex(byte[] bArr) {
        StringBuilder sb2 = new StringBuilder();
        for (byte value : bArr) {
            String hexString = Integer.toHexString(value & UByte.MAX_VALUE);
            if (hexString.length() == 1) {
                sb2.append('0');
            }
            sb2.append(hexString);
        }
        return sb2.toString();
    }

    public static void clearOldCache(Context context, int maxAgeDays) {
        new Date().getTime();
        clearDirectory(new File(RedditApplication.getAppContext().getExternalCacheDir(), "redditVideoCache"), maxAgeDays, "redditVideoCache");
        clearDirectory(new File(RedditApplication.getAppContext().getExternalCacheDir(), "shared"), maxAgeDays, "shared");
        clearDirectory(new File(RedditApplication.getAppContext().getExternalCacheDir(), "images"), maxAgeDays, "images");
        clearDirectory(new File(RedditApplication.getAppContext().getExternalCacheDir(), "manifestCache"), maxAgeDays, "manifestCache");
        clearDirectory(new File(RedditApplication.getAppContext().getExternalCacheDir(), "okHttpCache"), maxAgeDays, "okHttpCache");
        clearDirectory(new File(RedditApplication.getAppContext().getExternalCacheDir(), "images"), maxAgeDays, "images");
        clearDirectory(new File(context.getCacheDir(), "images"), maxAgeDays, "images");
        clearDirectory(context.getCacheDir(), maxAgeDays, "cache");
    }

    private static void clearDirectory(File file, int maxAgeDays, String str) {
        String[] list;
        long time = new Date().getTime();
        if (file == null || !file.exists() || !file.canWrite() || (list = file.list()) == null) {
            return;
        }
        for (int length = list.length - 1; length >= 0; length--) {
            try {
                File file2 = new File(file, list[length]);
                if (file2.isDirectory()) {
                    Logger.verbose("ImageCache", "Sub-directory encounced " + file2.getAbsolutePath());
                    boolean clearable = true;
                    int i = 0;
                    while (true) {
                        String[] strArr = PROTECTED_DIRS;
                        if (i >= strArr.length) {
                            break;
                        }
                        if (strArr[i].equalsIgnoreCase(file2.getName())) {
                            clearable = false;
                        }
                        i++;
                    }
                    if (clearable) {
                        clearDirectory(file2, maxAgeDays, str);
                        Logger.verbose("ImageCache", "Clearing directory: " + file2.getAbsolutePath());
                    } else {
                        Logger.verbose("ImageCache", "Skipping unknown directory: " + file2.getAbsolutePath());
                    }
                } else if (((int) ((time - new Date(file2.lastModified()).getTime()) / DateUtils.MILLIS_PER_DAY)) >= maxAgeDays) {
                    if (file2.isDirectory()) {
                        CrashlyticsHelper.logEvent(6, "ImageCache", "Directory encountered: " + file2.getAbsolutePath());
                    } else {
                        Logger.verbose("ImageCache", "Deleting " + str + ": " + file2.getAbsolutePath());
                        file2.delete();
                    }
                }
            } catch (Exception e) {
                Logger.logException(e);
            }
        }
    }

    public static File getImageCacheDir(Context context) {
        if (context == null) {
            return null;
        }
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir != null && externalCacheDir.exists() && externalCacheDir.canWrite()) {
            File file = new File(externalCacheDir, "images");
            if (!file.exists()) {
                file.mkdir();
            }
            return file;
        }
        File file2 = new File(context.getCacheDir(), "images");
        if (!file2.exists()) {
            file2.mkdir();
        }
        return file2;
    }

    public static String md5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            return toHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException unused) {
            return String.valueOf(str.hashCode());
        }
    }

    public static File getCacheDir(Context context, String str) {
        if (context == null) {
            return null;
        }
        String username = AccountSingleton.getInstance().getUsername();
        if (!TextUtils.isEmpty(username)) {
            String lowerCase = username.toLowerCase(Locale.ENGLISH);
            if (!lowerCase.equalsIgnoreCase("logged out")) {
                str = str + "_" + lowerCase;
            }
        }
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir != null && externalCacheDir.exists() && externalCacheDir.canWrite()) {
            File file = new File(externalCacheDir, str);
            if (!file.exists()) {
                file.mkdir();
            }
            return file;
        }
        File file2 = new File(context.getCacheDir(), str);
        if (!file2.exists()) {
            file2.mkdir();
        }
        return file2;
    }
}
