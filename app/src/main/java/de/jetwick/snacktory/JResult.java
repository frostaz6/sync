package de.jetwick.snacktory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class JResult implements Serializable {
    private String title;
    private String url;
    private String originalUrl;
    private String canonicalUrl;
    private String imageUrl;
    private String videoUrl;
    private String rssUrl;
    private String text;
    private String faviconUrl;
    private String description;
    private String date;
    private Collection<String> keywords;
    private List<ImageResult> images = null;

    public String getCanonicalUrl() {
        return this.canonicalUrl;
    }

    public String getDate() {
        return this.date;
    }

    public String getDescription() {
        String str = this.description;
        return str == null ? "" : str;
    }

    public String getFaviconUrl() {
        String str = this.faviconUrl;
        return str == null ? "" : str;
    }

    public String getImageUrl() {
        String str = this.imageUrl;
        return str == null ? "" : str;
    }

    public List<ImageResult> getImages() {
        List<ImageResult> list = this.images;
        return list == null ? Collections.emptyList() : list;
    }

    public int getImagesCount() {
        List<ImageResult> list = this.images;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public Collection<String> getKeywords() {
        return this.keywords;
    }

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    public String getRssUrl() {
        String str = this.rssUrl;
        return str == null ? "" : str;
    }

    public String getText() {
        String str = this.text;
        return str == null ? "" : str;
    }

    public String getTitle() {
        String str = this.title;
        return str == null ? "" : str;
    }

    public String getUrl() {
        String str = this.url;
        return str == null ? "" : str;
    }

    public String getVideoUrl() {
        String str = this.videoUrl;
        return str == null ? "" : str;
    }

    public JResult setCanonicalUrl(String str) {
        this.canonicalUrl = str;
        return this;
    }

    public JResult setDate(String str) {
        this.date = str;
        return this;
    }

    public JResult setDescription(String str) {
        this.description = str;
        return this;
    }

    public JResult setFaviconUrl(String str) {
        this.faviconUrl = str;
        return this;
    }

    public JResult setImageUrl(String str) {
        this.imageUrl = str;
        return this;
    }

    public void setImages(List<ImageResult> list) {
        this.images = list;
    }

    public void setKeywords(Collection<String> collection) {
        this.keywords = collection;
    }

    public JResult setOriginalUrl(String str) {
        this.originalUrl = str;
        return this;
    }

    public JResult setRssUrl(String str) {
        this.rssUrl = str;
        return this;
    }

    public JResult setText(String str) {
        this.text = str;
        return this;
    }

    public JResult setTitle(String str) {
        this.title = str;
        return this;
    }

    public JResult setUrl(String str) {
        this.url = str;
        return this;
    }

    public JResult setVideoUrl(String str) {
        this.videoUrl = str;
        return this;
    }

    public String toString() {
        return "title:" + getTitle() + " imageUrl:" + getImageUrl() + " text:" + this.text;
    }
}
