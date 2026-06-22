package com.resync.app.media.images;

import android.text.Html;
import android.text.TextUtils;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.resync.app.widgets.preview.AbstractRedditPreview;
import com.resync.app.widgets.preview.ImagePreview;
import com.resync.app.widgets.preview.RedditAlbumPreview;
import com.resync.app.widgets.preview.RedditImagePreview;
import com.resync.app.widgets.preview.RedditLinkPreview;
import com.resync.app.widgets.preview.RedditSelftextPreview;
import com.resync.app.util.links.LinkTypeHelper;
import com.resync.app.util.Logger;
import com.resync.app.util.WebsafeUtils;

/* JADX INFO: renamed from: c8.b */
/* JADX INFO: compiled from: PostPreviewHelper.java */
/* JADX INFO: loaded from: classes.dex */
public final class PostPreviewHelper {
    public static AbstractRedditPreview buildPreview(JSONObject jSONObject, int kind, int width, int height) throws JSONException {
        String crosspostHls;
        if (jSONObject.optJSONArray("crosspost_parent_list") != null && jSONObject.optJSONArray("crosspost_parent_list").length() > 0) {
            JSONObject crosspost = jSONObject.optJSONArray("crosspost_parent_list").getJSONObject(0);
            if (crosspost.has("media")) {
                crosspost = crosspost.optJSONObject("media");
                if (crosspost != null && crosspost.has("reddit_video")) {
                    crosspostHls = crosspost.getJSONObject("reddit_video").optString("hls_url");
                } else {
                    crosspostHls = null;
                }
            } else {
                crosspostHls = null;
            }
        } else {
            crosspostHls = null;
        }

        String rawUrl = jSONObject.optString("url");
        String url = rawUrl;
        if (!TextUtils.isEmpty(rawUrl)) {
            rawUrl = WebsafeUtils.unescapeHtml(rawUrl);
            url = rawUrl;
            if (rawUrl.contains("imgur.com/gallery/xIolo")) {
                url = "http://i.imgur.com/xIolo.gif";
            }
        }

        if (url.contains("twitter.com")) {
            RedditLinkPreview twitterPreview = new RedditLinkPreview();
            twitterPreview.url = url;
            return twitterPreview;
        }

        String secureEmbedContent;
        if (jSONObject.has("secure_media_embed")) {
            secureEmbedContent = jSONObject.getJSONObject("secure_media_embed").optString("content");
        } else {
            secureEmbedContent = null;
        }

        int videoDuration;
        String videoHls;
        String videoUrl;
        if (jSONObject.has("media")) {
            JSONObject media = jSONObject.optJSONObject("media");
            if (media != null && media.has("reddit_video")) {
                String hls = media.getJSONObject("reddit_video").optString("hls_url");
                int duration = media.getJSONObject("reddit_video").optInt("duration");
                videoUrl = crosspostHls;
                videoHls = hls;
                videoDuration = duration;
                if (TextUtils.isEmpty(crosspostHls)) {
                    videoUrl = media.getJSONObject("reddit_video").optString("hls_url");
                    videoHls = hls;
                    videoDuration = duration;
                }
            } else {
                videoDuration = 0;
                videoHls = null;
                videoUrl = crosspostHls;
            }
        } else {
            videoDuration = 0;
            videoHls = null;
            videoUrl = crosspostHls;
        }

        String mediaUrl = url;
        if (!TextUtils.isEmpty(url)) {
            mediaUrl = url;
            if (url.contains("v.redd.it")) {
                mediaUrl = com.resync.app.util.links.LinkHandler.getRedditVideoId(url);
                mediaUrl = "https://v.redd.it/" + mediaUrl + "/HLSPlaylist.m3u8";
            }
        }

        boolean spoiler = jSONObject.optBoolean("spoiler");
        if (!spoiler) {
            spoiler = StringUtils.containsIgnoreCase(jSONObject.optString("link_flair_text"), "spoiler");
        }

        boolean hasResolutions;
        JSONObject source;
        JSONArray resolutions;
        JSONObject preview = jSONObject.optJSONObject("preview");
        String previewVideoUrl = videoUrl;
        if (preview != null) {
            JSONObject redditVideoPreview = preview.optJSONObject("reddit_video_preview");
            if (redditVideoPreview != null) {
                previewVideoUrl = redditVideoPreview.optString("hls_url");
            }

            JSONArray images = preview.optJSONArray("images");
            if (images != null && images.length() > 0) {
                source = images.getJSONObject(0).optJSONObject("source");
                if (spoiler) {
                    JSONObject variants = images.getJSONObject(0).optJSONObject("variants");
                    if (variants != null) {
                        variants = variants.optJSONObject("obfuscated");
                        if (variants != null) {
                            resolutions = variants.optJSONArray("resolutions");
                            hasResolutions = resolutions != null && resolutions.length() > 0;
                        } else {
                            hasResolutions = false;
                            resolutions = null;
                        }
                    } else {
                        hasResolutions = false;
                        resolutions = null;
                    }
                } else {
                    resolutions = images.getJSONObject(0).optJSONArray("resolutions");
                    boolean notNull = resolutions != null;
                    boolean nonEmpty = resolutions.length() > 0;
                    hasResolutions = notNull && nonEmpty;
                }
            } else {
                hasResolutions = false;
                source = null;
                resolutions = null;
            }
        } else {
            hasResolutions = false;
            source = null;
            resolutions = null;
        }

        if (2 == kind && hasResolutions) {
            RedditSelftextPreview selftextPreview = new RedditSelftextPreview();
            selftextPreview.type = 2;
            selftextPreview.image = buildImagePreview(spoiler, mediaUrl, selftextPreview, source, resolutions, width, height);
            return selftextPreview;
        }

        int type = kind;
        if (1 == kind) {
            if (spoiler && !hasResolutions) {
                type = 3;
            }
        }

        String workUrl = mediaUrl;
        if (preview != null) {
            workUrl = mediaUrl;
            if (!TextUtils.isEmpty(mediaUrl)
                && (mediaUrl.contains("redd.it") || mediaUrl.contains("reddituploads.com") || mediaUrl.contains("redditmedia"))) {
                JSONArray images = preview.optJSONArray("images");
                workUrl = mediaUrl;
                if (images != null && images.length() > 0) {
                    JSONObject variants = images.getJSONObject(0).optJSONObject("variants");
                    workUrl = mediaUrl;
                    if (variants != null && variants.has("mp4")) {
                        variants = variants.getJSONObject("mp4");
                        workUrl = mediaUrl;
                        if (variants != null && variants.has("source")) {
                            variants = variants.getJSONObject("source");
                            workUrl = mediaUrl;
                            if (variants != null && variants.has("url")) {
                                String mp4Url = variants.getString("url");
                                workUrl = mediaUrl;
                                if (!TextUtils.isEmpty(mp4Url)) {
                                    mediaUrl = WebsafeUtils.unescapeHtml(mp4Url);
                                    workUrl = mediaUrl;
                                    if (TextUtils.isEmpty(previewVideoUrl)) {
                                        previewVideoUrl = mediaUrl;
                                        workUrl = mediaUrl;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        mediaUrl = workUrl;

        if (1 != type && 7 != type && 4 != type) {
            if (6 == type) {
                RedditAlbumPreview albumPreview = new RedditAlbumPreview();
                albumPreview.url = mediaUrl;
                if (hasResolutions) {
                    ImagePreview img = buildImagePreview(spoiler, mediaUrl, null, source, resolutions, width, height);
                    albumPreview.image = img;
                    if (img != null) {
                        albumPreview.type = 10;
                    }
                }
                return albumPreview;
            } else if (3 == type) {
                RedditLinkPreview linkPreview = new RedditLinkPreview();
                linkPreview.url = mediaUrl;
                if (hasResolutions) {
                    linkPreview.image = buildImagePreview(spoiler, mediaUrl, null, source, resolutions, width, height);
                    linkPreview.type = 9;
                }
                return linkPreview;
            } else {
                return null;
            }
        } else {
            if (mediaUrl.contains("xkcd.com") && !hasResolutions) {
                RedditLinkPreview xkcdPreview = new RedditLinkPreview();
                xkcdPreview.url = mediaUrl;
                return xkcdPreview;
            }

            if (mediaUrl.contains("mediadownloads.mlb.com") && !hasResolutions) {
                RedditLinkPreview mlbPreview = new RedditLinkPreview();
                mlbPreview.url = mediaUrl;
                return mlbPreview;
            }

            RedditImagePreview imagePreview = new RedditImagePreview();
            imagePreview.type = type;
            secureEmbedContent = ImageHelper.resolveImageUrl(mediaUrl, secureEmbedContent);
            imagePreview.url = secureEmbedContent;
            imagePreview.image = new ImagePreview(secureEmbedContent, 0, 0);
            imagePreview.videoUrl = previewVideoUrl;
            if (source != null) {
                imagePreview.tallImage = com.resync.app.media.ImageUtils.isTallImage(source.optInt("width"), source.optInt("height"));
            }

            if (LinkTypeHelper.isYouTube(mediaUrl)) {
                imagePreview.image = new ImagePreview("https://i.ytimg.com/vi/" + com.resync.app.util.links.LinkHandler.getYouTubeId(mediaUrl) + "/hqdefault.jpg", 0, 0);
                Logger.verbose("USING YOUTUBE");
                return imagePreview;
            }

            if (com.resync.app.media.imgur.ImgurImageHelper.isImgur(mediaUrl)) {
                if (!TextUtils.isEmpty(imagePreview.url)
                    && (imagePreview.url.endsWith("mp4") || imagePreview.url.endsWith("gifv") || imagePreview.url.endsWith("gif"))) {
                    imagePreview.type = 7;
                }

                imagePreview.image = new ImagePreview(new com.resync.app.model.media.Image(mediaUrl).getHugeThumbnailUrl(), 0, 0);
            }

            if (LinkTypeHelper.isGfycat(mediaUrl)) {
                secureEmbedContent = com.resync.app.util.links.LinkHandler.getGfycatId(mediaUrl);
                if (!secureEmbedContent.equals(secureEmbedContent.toLowerCase(Locale.ENGLISH))) {
                    String poster = "https://thumbs.gfycat.com/" + secureEmbedContent + "-poster.jpg";
                    ImagePreview gfyImg;
                    if (hasResolutions) {
                        gfyImg = buildImagePreview(spoiler, mediaUrl, imagePreview, source, resolutions, width, height);
                    } else {
                        gfyImg = null;
                    }

                    imagePreview.type = 4;
                    imagePreview.url = mediaUrl;
                    int w;
                    if (gfyImg != null) {
                        w = gfyImg.width;
                    } else {
                        w = 0;
                    }
                    int h;
                    if (gfyImg != null) {
                        h = gfyImg.height;
                    } else {
                        h = 0;
                    }
                    imagePreview.image = new ImagePreview(poster, w, h);
                    imagePreview.videoUrl = previewVideoUrl;
                    return imagePreview;
                }
            }

            if (LinkTypeHelper.isRedgifs(mediaUrl) && hasResolutions) {
                ImagePreview redgifImg = buildImagePreview(spoiler, mediaUrl, imagePreview, source, resolutions, width, height);
                imagePreview.type = 4;
                imagePreview.url = mediaUrl;
                imagePreview.image = new ImagePreview(redgifImg.url, redgifImg.width, redgifImg.height);
                imagePreview.videoUrl = previewVideoUrl;
                return imagePreview;
            }

            if (hasResolutions) {
                imagePreview.image = buildImagePreview(spoiler, mediaUrl, imagePreview, source, resolutions, width, height);
            }

            if (com.resync.app.media.imgur.ImgurImageHelper.isImgur(mediaUrl) && hasResolutions && !spoiler) {
                secureEmbedContent = new com.resync.app.model.media.Image(mediaUrl).getHugeThumbnailUrl();
                ImagePreview prev = imagePreview.image;
                imagePreview.image = new ImagePreview(secureEmbedContent, prev.width, prev.height);
            }

            if (mediaUrl.contains("v.redd.it") && StringUtils.equalsIgnoreCase(imagePreview.image.url, imagePreview.url)) {
                RedditLinkPreview vReddPreview = new RedditLinkPreview();
                vReddPreview.url = mediaUrl;
                return vReddPreview;
            }

            if (LinkTypeHelper.isStreamable(mediaUrl) && imagePreview.url.equals(imagePreview.image.url)) {
                RedditLinkPreview linkPreview2 = new RedditLinkPreview();
                linkPreview2.url = mediaUrl;
                return linkPreview2;
            }

            if (7 == type && imagePreview.url.equals(imagePreview.image.url)) {
                RedditLinkPreview linkPreview3 = new RedditLinkPreview();
                linkPreview3.url = mediaUrl;
                return linkPreview3;
            }

            if (mediaUrl.contains("v.redd.it") && !TextUtils.isEmpty(videoHls)) {
                imagePreview.url = videoHls;
                imagePreview.videoDuration = videoDuration;
                imagePreview.videoUrl = previewVideoUrl;
                Logger.verbose("Video preview: " + previewVideoUrl);
            }

            return imagePreview;
        }
    }

    private static ImagePreview buildImagePreview(boolean spoiler, String str, AbstractRedditPreview abstractRedditPreview, JSONObject jSONObject, JSONArray jSONArray, int width, int height) throws JSONException {
        if (jSONArray.length() == 1 && jSONObject != null) {
            Logger.debug("There was only one preview so lets use the source!");
            return new ImagePreview(Html.fromHtml(jSONObject.getString("url")).toString(), jSONObject.getInt("width"), jSONObject.getInt("height"));
        }
        if (jSONArray.length() == 0) {
            return null;
        }
        JSONObject jSONObject2 = jSONArray.getJSONObject(jSONArray.length() - 1);
        return new ImagePreview(Html.fromHtml(jSONObject2.getString("url")).toString(), jSONObject2.getInt("width"), jSONObject2.getInt("height"));
    }
}
