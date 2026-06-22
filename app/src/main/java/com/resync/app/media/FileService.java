package com.resync.app.media;

import java.io.File;

/* JADX INFO: renamed from: c3.a */
/* JADX INFO: compiled from: FileService.java */
/* JADX INFO: loaded from: classes.dex */
class FileService {
    FileService() {
    }

    public boolean exists(File file) {
        return file.exists();
    }

    public File get(String str) {
        return new File(str);
    }

    public long length(File file) {
        return file.length();
    }
}
