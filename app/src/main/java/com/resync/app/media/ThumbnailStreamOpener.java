package com.resync.app.media;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/* Recovered vendored Glide internal: ThumbnailStreamOpener. */
public class ThumbnailStreamOpener {
    private static final FileService DEFAULT_SERVICE = new FileService();

    private final ThumbnailQuery query;
    private final ArrayPool byteArrayPool;
    private final ContentResolver contentResolver;
    private final List<ImageHeaderParser> parsers;
    private final FileService service;

    public ThumbnailStreamOpener(List<ImageHeaderParser> parsers, ThumbnailQuery query, ArrayPool byteArrayPool, ContentResolver contentResolver) {
        this(DEFAULT_SERVICE, parsers, query, byteArrayPool, contentResolver);
    }

    ThumbnailStreamOpener(FileService service, List<ImageHeaderParser> parsers, ThumbnailQuery query, ArrayPool byteArrayPool, ContentResolver contentResolver) {
        this.service = service;
        this.parsers = parsers;
        this.query = query;
        this.byteArrayPool = byteArrayPool;
        this.contentResolver = contentResolver;
    }

    public int getOrientation(Uri uri) {
        InputStream is = null;
        try {
            is = contentResolver.openInputStream(uri);
            return ImageHeaderParserUtils.getOrientation(parsers, is, byteArrayPool);
        } catch (IOException | NullPointerException e) {
            return -1;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public InputStream open(Uri uri) throws FileNotFoundException {
        String path = getPath(uri);
        if (android.text.TextUtils.isEmpty(path)) {
            return null;
        }
        File file = service.get(path);
        if (!isValid(file)) {
            return null;
        }
        Uri thumbnailUri = Uri.fromFile(file);
        try {
            return contentResolver.openInputStream(thumbnailUri);
        } catch (NullPointerException e) {
            throw (FileNotFoundException) new FileNotFoundException("NPE opening uri: " + uri + " -> " + thumbnailUri).initCause(e);
        }
    }

    private String getPath(Uri uri) {
        Cursor cursor = null;
        try {
            cursor = query.query(uri);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(0);
            }
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private boolean isValid(File file) {
        return service.exists(file) && service.length(file) > 0;
    }

    static class FileService {
        public boolean exists(File file) {
            return file.exists();
        }

        public long length(File file) {
            return file.length();
        }

        public File get(String path) {
            return new File(path);
        }
    }
}
