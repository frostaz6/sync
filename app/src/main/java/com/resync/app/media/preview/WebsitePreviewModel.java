package com.resync.app.media.preview;

/**
 * Glide model representing a web page whose OpenGraph preview image should be
 * loaded. A dedicated type (rather than a plain String / GlideUrl) lets Glide
 * route these loads through {@link WebsitePreviewModelLoader}, which resolves
 * the page's og:image before delegating the actual download to Glide's built-in
 * GlideUrl loader.
 */
public final class WebsitePreviewModel {

    private final String url;

    public WebsitePreviewModel(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WebsitePreviewModel)) {
            return false;
        }
        WebsitePreviewModel other = (WebsitePreviewModel) o;
        return this.url != null ? this.url.equals(other.url) : other.url == null;
    }

    @Override
    public int hashCode() {
        return this.url != null ? this.url.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "WebsitePreviewModel{" + this.url + "}";
    }
}
