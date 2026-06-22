package com.resync.app.widgets.text;

import android.os.Build;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.media.imgur.ImgurImageHelper;
import com.resync.app.media.preview.WebsitePreviewModel;
import com.resync.app.model.media.Image;
import com.resync.app.text.spans.EfficientImageSpan;
import com.resync.app.text.spans.EmoteImageSpan;
import com.resync.app.text.spans.InlineImageSpan;
import com.resync.app.text.spans.VectorImageSpan;
import com.resync.app.text.spans.VectorInlineImageSpan;
import com.resync.app.text.spans.WebPreviewImageSpan;
import com.resync.app.text.spans.RedditQuoteSpan;
import com.resync.app.media.preview.RedditQuoteFetcher;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.util.Logger;
import com.resync.app.util.WebsitePreviewHelper;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: renamed from: oc.c */
/* JADX INFO: compiled from: SpannableTextViewStringBuilder.java */
/* JADX INFO: loaded from: classes3.dex */
public class SpannableTextViewStringBuilder {

    private StringBuilder builder = new StringBuilder();
    private ArrayList<Span> spans = new ArrayList<>();

    /* JADX INFO: renamed from: oc.c$a */
    /* JADX INFO: compiled from: SpannableTextViewStringBuilder.java */
    public class Span {

        public int start;
        public int end;
        public int flags;
        public Object span;

        public Span(int start, int end, int flags, Object span) {
            this.start = start;
            this.end = end;
            this.flags = flags;
            this.span = span;
        }
    }

    public void append(char c) {
        this.builder.append(c);
    }

    public void append(CharSequence charSequence) {
        this.builder.append(charSequence);
    }

    public void appendWithSpans(String str, Object... objArr) {
        int length = this.builder.length();
        this.builder.append(str);
        int length2 = this.builder.length();
        for (Object obj : objArr) {
            addSpan(obj, length, length2, 33);
        }
    }

    public CharSequence build() {
        if (this.spans.size() <= 0) {
            return this.builder.toString().trim();
        }
        SpannableString spannableString = new SpannableString(this.builder);
        for (Span span : this.spans) {
            int start = span.start;
            if (start > span.end || start > spannableString.length()) {
                Logger.verbose("SPANS", "Span out of range: " + span);
            } else {
                Logger.verbose("Setting span: " + span.span);
                spannableString.setSpan(span.span, span.start, Math.min(span.end, spannableString.length()), span.flags);
            }
        }
        return spannableString;
    }

    public boolean canUsePrecomputedText() {
        if (Build.VERSION.SDK_INT < 29) {
            return true;
        }
        Iterator<Span> it = this.spans.iterator();
        while (it.hasNext()) {
            if (it.next().span instanceof StrikethroughSpan) {
                return false;
            }
        }
        return true;
    }

    public char charAt(int index) {
        return this.builder.charAt(index);
    }

    public void reset() {
        if (this.spans.size() > 0) {
            clearSpans();
            this.spans.clear();
        }
        if (this.builder.length() > 0) {
            this.builder.setLength(0);
        }
    }

    public void clearSpans() {
        if (this.spans.size() > 0) {
            Logger.verbose("SPANS", "Clearing spans: " + this.spans.size());
            Iterator<Span> it = this.spans.iterator();
            while (it.hasNext()) {
                Object obj = it.next().span;
                if (obj instanceof VectorImageSpan) {
                    Glide.with(RedditApplication.getAppContext()).clear((VectorImageSpan) obj);
                } else if (obj instanceof EfficientImageSpan) {
                    Glide.with(RedditApplication.getAppContext()).clear((EfficientImageSpan) obj);
                } else if (obj instanceof InlineImageSpan) {
                    Glide.with(RedditApplication.getAppContext()).clear((InlineImageSpan) obj);
                } else if (obj instanceof VectorInlineImageSpan) {
                    Glide.with(RedditApplication.getAppContext()).clear((VectorInlineImageSpan) obj);
                } else if (obj instanceof EmoteImageSpan) {
                    Glide.with(RedditApplication.getAppContext()).clear((EmoteImageSpan) obj);
                } else if (obj instanceof WebPreviewImageSpan) {
                    Glide.with(RedditApplication.getAppContext()).clear((WebPreviewImageSpan) obj);
                } else if (obj instanceof RedditQuoteSpan) {
                    java.util.concurrent.Future<?> f = ((RedditQuoteSpan) obj).getFuture();
                    if (f != null) {
                        f.cancel(true);
                    }
                    ((RedditQuoteSpan) obj).clearThumbnail();
                }
            }
        }
    }

    public boolean endsWithNewline() {
        return StringUtils.endsWith(this.builder, "\n");
    }

    public void trimTrailingNewlines() {
        while (this.builder.length() >= 1) {
            StringBuilder sb2 = this.builder;
            if (sb2.charAt(sb2.length() - 1) != '\n') {
                return;
            }
            StringBuilder sb3 = this.builder;
            sb3.deleteCharAt(sb3.length() - 1);
        }
    }

    public Span findLastSpan(Class cls) {
        ArrayList<Span> arrayListL = getSpans();
        for (int size = arrayListL.size() - 1; size >= 0; size--) {
            if (arrayListL.get(size).span.getClass() == cls) {
                return arrayListL.get(size);
            }
        }
        return null;
    }

    public ArrayList<Span> getSpans() {
        return this.spans;
    }

    public boolean hasUnloadedImages() {
        Iterator<Span> it = this.spans.iterator();
        while (it.hasNext()) {
            Object obj = it.next().span;
            if ((obj instanceof EfficientImageSpan) && !((EfficientImageSpan) obj).isLoaded()) {
                return true;
            }
        }
        return false;
    }

    public int length() {
        return this.builder.length();
    }

    public void loadImages(SpannableTextView spannableTextView) {
        Iterator<Span> it = this.spans.iterator();
        while (it.hasNext()) {
            Object obj = it.next().span;
            if (obj instanceof VectorImageSpan) {
                VectorImageSpan vectorImageSpan = (VectorImageSpan) obj;
                vectorImageSpan.setTextView(spannableTextView);
                int imageSize = (int) (vectorImageSpan.getImageSize() * 0.8f);
                Glide.with(RedditApplication.getAppContext()).load(Integer.valueOf(vectorImageSpan.resId)).override(imageSize, imageSize).priority(Priority.LOW).into(vectorImageSpan);
            } else if (obj instanceof EfficientImageSpan) {
                EfficientImageSpan efficientImageSpan = (EfficientImageSpan) obj;
                Logger.verbose("EfficientImageSpan", "Starting loading of: " + efficientImageSpan);
                efficientImageSpan.setTextView(spannableTextView);
                int imageSize = (int) (((float) efficientImageSpan.getImageSize()) * 0.8f);
                Glide.with(RedditApplication.getAppContext()).load(efficientImageSpan.getUrl()).override(imageSize, imageSize).priority(Priority.LOW).into(efficientImageSpan);
            } else if (obj instanceof InlineImageSpan) {
                InlineImageSpan inlineImageSpan = (InlineImageSpan) obj;
                inlineImageSpan.setTextView(spannableTextView);
                String url = inlineImageSpan.getUrl();
                Object imageUrl = LinkTypeHelper.isImgur(url) ? new Image(url).getHugeThumbnailUrl() : url;
                if (LinkTypeHelper.isGfycat(url)) {
                    imageUrl = "https://ap.syncforreddit.com/image?url=" + url;
                }
                if (LinkTypeHelper.isGiphy(url)) {
                    imageUrl = "https://media.giphy.com/media/" + com.resync.app.util.links.LinkHandler.getGiphyId(url) + "/100.gif";
                }
                if (LinkTypeHelper.isYouTube(url)) {
                    imageUrl = "https://i.ytimg.com/vi/" + com.resync.app.util.links.LinkHandler.getYouTubeId(url) + "/hqdefault.jpg";
                }
                if (url.contains("tenor.com")) {
                    imageUrl = WebsitePreviewHelper.getPreviewUrl(url);
                }
                if (ImgurImageHelper.isAlbum(url)) {
                    url = ImgurImageHelper.getId(ImgurImageHelper.normalizeUrl(url));
                    imageUrl = "https://ap.syncforreddit.com/image?url=imgur-album-" + url;
                }
                if (StringUtils.startsWith(url, "Settings shortcut")) {
                    imageUrl = Integer.valueOf(R.drawable.outline_settings_24);
                }
                Glide.with(RedditApplication.getAppContext()).load(imageUrl).override(SizeHelper.dpToPx(56), SizeHelper.dpToPx(56)).priority(Priority.LOW).transform(new CenterCrop(), new GranularRoundedCorners(SizeHelper.dpToPx(12), 0.0f, 0.0f, SizeHelper.dpToPx(12))).into(inlineImageSpan);
            } else if (obj instanceof VectorInlineImageSpan) {
                VectorInlineImageSpan vectorInlineImageSpan = (VectorInlineImageSpan) obj;
                vectorInlineImageSpan.setTextView(spannableTextView);
                Glide.with(RedditApplication.getAppContext()).load(Integer.valueOf(vectorInlineImageSpan.getResId())).override(SizeHelper.dpToPx(56), SizeHelper.dpToPx(56)).priority(Priority.LOW).transform(new CenterCrop(), new GranularRoundedCorners(SizeHelper.dpToPx(12), 0.0f, 0.0f, SizeHelper.dpToPx(12))).into(vectorInlineImageSpan);
            } else if (obj instanceof EmoteImageSpan) {
                EmoteImageSpan emoteImageSpan = (EmoteImageSpan) obj;
                emoteImageSpan.setTextView(spannableTextView);
                Glide.with(RedditApplication.getAppContext()).load(emoteImageSpan.getUrl()).override(emoteImageSpan.getWidth(), emoteImageSpan.getWidth()).priority(Priority.LOW).transform(new RoundedCorners(SizeHelper.dpToPx(8))).into(emoteImageSpan);
            } else if (obj instanceof WebPreviewImageSpan) {
                WebPreviewImageSpan webPreviewImageSpan = (WebPreviewImageSpan) obj;
                // Skip if already loading/loaded — refresh() re-runs loadImages to expand a loaded span,
                // and re-fetching here would loop (onResourceReady -> refresh -> loadImages -> ...).
                if (webPreviewImageSpan.isAttempted()) {
                    continue;
                }
                webPreviewImageSpan.setAttempted(true);
                webPreviewImageSpan.setTextView(spannableTextView);
                Glide.with(RedditApplication.getAppContext()).load(new WebsitePreviewModel(webPreviewImageSpan.getUrl())).priority(Priority.LOW).override(webPreviewImageSpan.getMaxWidth(), webPreviewImageSpan.getMaxHeight()).centerCrop().into(webPreviewImageSpan);
            } else if (obj instanceof RedditQuoteSpan) {
                final RedditQuoteSpan quoteSpan = (RedditQuoteSpan) obj;
                // Skip if already loading/loaded — refresh() re-runs loadImages to expand a loaded
                // span, and re-fetching here would loop (onQuoteLoaded -> refresh -> loadImages -> ...).
                if (quoteSpan.isAttempted()) {
                    continue;
                }
                quoteSpan.setAttempted(true);
                quoteSpan.setTextView(spannableTextView);
                quoteSpan.setFuture(RedditQuoteFetcher.fetch(RedditApplication.getAppContext(), quoteSpan.getHref(),
                        new RedditQuoteFetcher.Callback() {
                            @Override
                            public void onQuote(RedditQuoteFetcher.Quote quote) {
                                quoteSpan.onQuoteLoaded(quote);
                            }
                        }));
            }
        }
    }

    public void clearAllSpans() {
        if (this.spans.size() > 0) {
            clearSpans();
            this.spans.clear();
        }
    }

    public void removeSpan(Span span) {
        this.spans.remove(span);
    }

    public String cutFrom(Span span) {
        StringBuilder sb2 = this.builder;
        String strSubstring = sb2.substring(span.start, sb2.length());
        StringBuilder sb3 = this.builder;
        sb3.delete(span.start, sb3.length());
        for (int size = this.spans.size() - 1; size >= 0; size--) {
            if (this.spans.get(size).start >= this.builder.length()) {
                ArrayList<Span> arrayList = this.spans;
                arrayList.remove(arrayList.get(size));
            }
        }
        return strSubstring;
    }

    public void addSpan(Object obj, int start, int end, int flags) {
        Logger.verbose("SPANS", "Adding span: " + obj.getClass() + " - " + end + " - " + this.builder.length());
        if (start <= end) {
            this.spans.add(new Span(start, Math.min(end, this.builder.length()), flags, obj));
        }
    }

    public String substringFrom(Span span) {
        StringBuilder sb2 = this.builder;
        return sb2.substring(span.start, sb2.length());
    }

    public String toString() {
        return this.builder.toString().trim();
    }

    public void trimTrailingNewlinesIfSpans() {
        if (this.spans.size() > 0) {
            while (this.builder.length() >= 1) {
                StringBuilder sb2 = this.builder;
                if (sb2.charAt(sb2.length() - 1) != '\n') {
                    return;
                }
                StringBuilder sb3 = this.builder;
                sb3.deleteCharAt(sb3.length() - 1);
            }
        }
    }
}
