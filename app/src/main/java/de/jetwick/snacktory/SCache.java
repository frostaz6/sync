package de.jetwick.snacktory;

/* JADX INFO: loaded from: classes3.dex */
public interface SCache {
    JResult get(String str);

    int getSize();

    void put(String str, JResult jResult);
}
