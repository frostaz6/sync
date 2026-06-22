package com.resync.app.text.spans;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.resync.app.RedditApplication;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.media.preview.RedditQuoteFetcher;
import com.resync.app.text.html.Options;
import com.resync.app.text.html.SyncHtmlParser;
import com.resync.app.util.Logger;
import com.resync.app.util.links.LinkHelper;
import com.resync.app.widgets.text.SpannableTextView;
import com.resync.app.widgets.text.SpannableTextViewStringBuilder;

/**
 * Forum-style inline quote of the reddit comment/post a permalink points to. Modeled on
 * {@link WebPreviewImageSpan}: a {@link ReplacementSpan} that starts <b>collapsed</b> (size 0, no box)
 * while the quote is fetched, then expands once {@link RedditQuoteFetcher} delivers. The card is drawn
 * directly on the canvas (no child View), so it lives entirely inside the comment's existing TextView.
 *
 * <p>Tap toggles the body between a clamped preview and full text; long-press opens the real thread.
 * On fetch failure the span stays collapsed (no box) and the plain link the user can already tap remains.
 */
public class RedditQuoteSpan extends ReplacementSpan implements CustomClickableSpan, SyncSpan {

    private static final int COLLAPSED_LINES = 3;
    private static final int MARGIN = SizeHelper.dpToPx(4);     // gap from comment text edges
    private static final int ACCENT_W = SizeHelper.dpToPx(3);   // left accent bar width
    private static final int PAD = SizeHelper.dpToPx(10);       // inner padding
    private static final int RADIUS = SizeHelper.dpToPx(8);
    private static final int GAP_BELOW = SizeHelper.dpToPx(6);  // space under the card

    private final String href;
    private final int width;

    private WeakReference<TextView> textViewRef;
    private Future<?> future;

    private boolean attempted = false;
    private RedditQuoteFetcher.Quote quote;   // null while loading; FAILED on failure
    private boolean expanded = false;

    private Bitmap thumbnail;                  // first image in the quoted content (or null)
    private CustomTarget<Bitmap> thumbTarget;  // strong ref so Glide doesn't GC the load
    private final int thumbHeight = SizeHelper.dpToPx(140);
    private final Rect thumbSrc = new Rect();
    private final Rect thumbDst = new Rect();

    // Cached measurement (rebuilt when width or expanded state changes).
    private StaticLayout bodyLayout;
    private int measuredForWidth = -1;
    private boolean measuredExpanded = false;
    private float bodyTextSize = 0f;
    private int totalHeight = 0;

    private TextPaint bodyPaint;
    private TextPaint headerPaint;
    private final RectF rect = new RectF();

    public RedditQuoteSpan(String href, int width) {
        this.href = href;
        this.width = width;
    }

    public String getHref() {
        return this.href;
    }

    public boolean isAttempted() {
        return this.attempted;
    }

    public void setAttempted(boolean attempted) {
        this.attempted = attempted;
    }

    public void setTextView(TextView textView) {
        this.textViewRef = new WeakReference<>(textView);
    }

    public Future<?> getFuture() {
        return this.future;
    }

    public void setFuture(Future<?> future) {
        this.future = future;
    }

    /** Called (on the main thread) by the fetcher; expands the span once the quote resolves. */
    public void onQuoteLoaded(RedditQuoteFetcher.Quote loaded) {
        this.quote = loaded;
        this.measuredForWidth = -1;   // force re-measure
        if (isRenderable() && !StringUtils.isEmpty(loaded.imageUrl)) {
            loadThumbnail(loaded.imageUrl);
        }
        requestRelayout();
    }

    /** Loads the first embedded image into {@link #thumbnail}; re-lays-out the card once decoded. */
    private void loadThumbnail(String imageUrl) {
        int cardWidth = this.width - 2 * MARGIN;
        final int target = Math.max(1, cardWidth - (ACCENT_W + PAD) - PAD);
        this.thumbTarget = new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                thumbnail = resource;
                measuredForWidth = -1;   // re-measure to make room for the image
                requestRelayout();
            }

            @Override
            public void onLoadCleared(@Nullable android.graphics.drawable.Drawable placeholder) {
                thumbnail = null;
            }
        };
        try {
            Glide.with(RedditApplication.getAppContext())
                    .asBitmap()
                    .load(imageUrl)
                    .override(target, this.thumbHeight)
                    .transform(new CenterCrop(), new RoundedCorners(SizeHelper.dpToPx(6)))
                    .into(this.thumbTarget);
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    private boolean isRenderable() {
        return this.quote != null && !this.quote.isFailed();
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        if (!isRenderable()) {
            // Collapsed: reserve no space (no box) while loading or on failure.
            if (fm != null) {
                fm.ascent = 0;
                fm.descent = 0;
                fm.top = 0;
                fm.bottom = 0;
            }
            return 0;
        }
        ensureMeasured(paint);
        if (fm != null) {
            int neg = -(this.totalHeight + GAP_BELOW);
            fm.ascent = neg;
            fm.top = neg;
            fm.descent = 0;
            fm.bottom = 0;
        }
        return this.width;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        if (!isRenderable()) {
            return;
        }
        ensureMeasured(paint);

        canvas.save();
        canvas.translate(x + MARGIN, bottom - this.totalHeight - GAP_BELOW);

        int cardWidth = this.width - 2 * MARGIN;

        // Card background.
        this.rect.set(0, 0, cardWidth, this.totalHeight);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(MonetUtil.getCardColorAuto());
        canvas.drawRoundRect(this.rect, RADIUS, RADIUS, p);

        // Left accent bar.
        p.setColor(MonetUtil.getLinkColorAuto());
        canvas.drawRoundRect(new RectF(0, 0, ACCENT_W + RADIUS, this.totalHeight), RADIUS, RADIUS, p);
        // Square off the accent bar's right side (overdraw card colour on the rounded overhang).
        p.setColor(MonetUtil.getCardColorAuto());
        canvas.drawRect(ACCENT_W, 0, ACCENT_W + RADIUS, this.totalHeight, p);

        int contentLeft = ACCENT_W + PAD;
        int contentWidth = cardWidth - contentLeft - PAD;
        float cursorY = PAD;

        // Header line(s).
        RedditQuoteFetcher.Quote q = this.quote;
        if (q.isComment) {
            cursorY = drawHeaderLine(canvas, buildCommentHeader(q), contentLeft, cursorY, contentWidth, true);
        } else {
            cursorY = drawHeaderLine(canvas, q.title, contentLeft, cursorY, contentWidth, true);
            cursorY += SizeHelper.dpToPx(2);
            cursorY = drawHeaderLine(canvas, buildPostMeta(q), contentLeft, cursorY, contentWidth, false);
        }
        cursorY += SizeHelper.dpToPx(4);

        // Body (clipped to the visible line count when collapsed).
        if (this.bodyLayout != null && this.bodyLayout.getLineCount() > 0) {
            int visibleLines = this.expanded
                    ? this.bodyLayout.getLineCount()
                    : Math.min(COLLAPSED_LINES, this.bodyLayout.getLineCount());
            int clipBottom = this.bodyLayout.getLineTop(visibleLines);
            canvas.save();
            canvas.translate(contentLeft, cursorY);
            canvas.clipRect(0, 0, contentWidth, clipBottom);
            this.bodyLayout.draw(canvas);
            canvas.restore();
            cursorY += clipBottom;
        }

        // Embedded image thumbnail (the screenshot/image the quoted content links to).
        if (this.thumbnail != null && !this.thumbnail.isRecycled()) {
            cursorY += SizeHelper.dpToPx(6);
            int drawW = Math.min(contentWidth, this.thumbnail.getWidth());
            this.thumbSrc.set(0, 0, this.thumbnail.getWidth(), this.thumbnail.getHeight());
            this.thumbDst.set(contentLeft, (int) cursorY, contentLeft + drawW,
                    (int) cursorY + this.thumbnail.getHeight());
            canvas.drawBitmap(this.thumbnail, this.thumbSrc, this.thumbDst, null);
            cursorY += this.thumbnail.getHeight();
        }

        // Footer hint when the body is longer than the collapsed clamp.
        if (this.bodyLayout != null && this.bodyLayout.getLineCount() > COLLAPSED_LINES) {
            cursorY += SizeHelper.dpToPx(4);
            String hint = this.expanded
                    ? "▴ Show less"
                    : "▾ Show " + (this.bodyLayout.getLineCount() - COLLAPSED_LINES) + " more lines";
            this.headerPaint.setColor(MonetUtil.getLinkColorAuto());
            canvas.drawText(hint, contentLeft, cursorY - this.headerPaint.ascent(), this.headerPaint);
        }

        canvas.restore();
    }

    /** Draws one ellipsized header line; returns the new Y cursor (below the line). */
    private float drawHeaderLine(Canvas canvas, String s, int left, float topY, int maxWidth, boolean primary) {
        if (StringUtils.isEmpty(s)) {
            return topY;
        }
        this.headerPaint.setFakeBoldText(primary);
        this.headerPaint.setColor(primary
                ? MonetUtil.getPrimaryTextColorAuto()
                : MonetUtil.getSecondaryTextColorAuto());
        CharSequence line = TextUtils.ellipsize(s, this.headerPaint, maxWidth, TextUtils.TruncateAt.END);
        float baseline = topY - this.headerPaint.ascent();
        canvas.drawText(line, 0, line.length(), left, baseline, this.headerPaint);
        this.headerPaint.setFakeBoldText(false);
        return topY + (this.headerPaint.descent() - this.headerPaint.ascent());
    }

    private static String buildCommentHeader(RedditQuoteFetcher.Quote q) {
        StringBuilder sb = new StringBuilder("u/").append(q.author);
        if (!StringUtils.isEmpty(q.subreddit)) {
            sb.append("  ·  r/").append(q.subreddit);
        }
        sb.append("  ·  ").append(formatScore(q.score)).append(" pts");
        return sb.toString();
    }

    private static String buildPostMeta(RedditQuoteFetcher.Quote q) {
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isEmpty(q.subreddit)) {
            sb.append("r/").append(q.subreddit).append("  ·  ");
        }
        sb.append("u/").append(q.author);
        sb.append("  ·  ").append(formatScore(q.score)).append(" pts");
        sb.append("  ·  ").append(q.numComments).append(" comments");
        return sb.toString();
    }

    private static String formatScore(int score) {
        if (score >= 10000) {
            return (score / 1000) + "k";
        }
        return String.valueOf(score);
    }

    private void ensureMeasured(Paint parentPaint) {
        if (this.measuredForWidth == this.width
                && this.measuredExpanded == this.expanded
                && this.bodyLayout != null) {
            return;
        }
        if (this.bodyTextSize == 0f) {
            this.bodyTextSize = parentPaint.getTextSize();
        }
        if (this.bodyPaint == null) {
            this.bodyPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            this.bodyPaint.setTextSize(this.bodyTextSize);
            this.bodyPaint.setColor(MonetUtil.getPrimaryTextColorAuto());
        }
        if (this.headerPaint == null) {
            this.headerPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            this.headerPaint.setTextSize(this.bodyTextSize * 0.92f);
        }

        int cardWidth = this.width - 2 * MARGIN;
        int contentLeft = ACCENT_W + PAD;
        int contentWidth = Math.max(1, cardWidth - contentLeft - PAD);

        CharSequence body = renderBody(this.quote);
        this.bodyLayout = new StaticLayout(body, this.bodyPaint, contentWidth,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, false);

        float headerLineH = this.headerPaint.descent() - this.headerPaint.ascent();
        float height = PAD;                       // top pad
        height += headerLineH;                    // header line 1 (comment header or post title)
        if (!this.quote.isComment) {
            height += SizeHelper.dpToPx(2) + headerLineH;   // post meta line
        }
        height += SizeHelper.dpToPx(4);           // gap header→body

        int visibleLines = this.expanded
                ? this.bodyLayout.getLineCount()
                : Math.min(COLLAPSED_LINES, this.bodyLayout.getLineCount());
        height += this.bodyLayout.getLineTop(visibleLines);

        if (this.thumbnail != null && !this.thumbnail.isRecycled()) {
            height += SizeHelper.dpToPx(6) + this.thumbnail.getHeight();   // image thumbnail
        }

        if (this.bodyLayout.getLineCount() > COLLAPSED_LINES) {
            height += SizeHelper.dpToPx(4) + headerLineH;   // footer hint line
        }
        height += PAD;                            // bottom pad

        this.totalHeight = (int) Math.ceil(height);
        this.measuredForWidth = this.width;
        this.measuredExpanded = this.expanded;
    }

    /**
     * Renders the quoted body HTML to a styled CharSequence with previews/inline-images DISABLED
     * (so the quote can never recurse into another quote/preview and never spawns image loads).
     */
    private CharSequence renderBody(RedditQuoteFetcher.Quote q) {
        String html = q.bodyHtml;
        if (StringUtils.isEmpty(html) || StringUtils.isBlank(html)) {
            // Don't show a "[no text]" placeholder when there's an image to display.
            return (q.isComment && StringUtils.isEmpty(q.imageUrl)) ? "[no text]" : "";
        }
        try {
            SpannableTextViewStringBuilder b = new SpannableTextViewStringBuilder();
            SyncHtmlParser.parse(b, Options.disabled(), html);
            CharSequence cs = b.build();
            return cs != null ? cs : "";
        } catch (Exception e) {
            Logger.logException(e);
            return "";
        }
    }

    @Override
    public void onClick(View view) {
        if (!isRenderable()) {
            return;
        }
        // Only toggle when the body is actually clamp-able; otherwise open the thread.
        if (this.bodyLayout != null && this.bodyLayout.getLineCount() > COLLAPSED_LINES) {
            this.expanded = !this.expanded;
            this.measuredForWidth = -1;
            requestRelayout();
        } else {
            LinkHelper.open(view.getContext(), this.href);
        }
    }

    @Override
    public void onLongClick(View view) {
        LinkHelper.open(view.getContext(), this.href);
    }

    /** Cancel the in-flight image load and drop the bitmap (called when the span is recycled). */
    public void clearThumbnail() {
        if (this.thumbTarget != null) {
            try {
                Glide.with(RedditApplication.getAppContext()).clear(this.thumbTarget);
            } catch (Exception e) {
                Logger.logException(e);
            }
            this.thumbTarget = null;
        }
        this.thumbnail = null;
    }

    private void requestRelayout() {
        TextView textView = this.textViewRef != null ? this.textViewRef.get() : null;
        if (textView == null) {
            return;
        }
        if (textView instanceof SpannableTextView) {
            ((SpannableTextView) textView).refresh();
        } else {
            textView.requestLayout();
            textView.invalidate();
        }
    }
}
