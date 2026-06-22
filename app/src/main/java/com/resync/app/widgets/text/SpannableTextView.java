package com.resync.app.widgets.text;

import android.content.Context;
import android.os.Handler;
import android.text.Layout;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;
import androidx.core.text.PrecomputedTextCompat;
import androidx.core.widget.TextViewCompat;
import com.resync.app.RedditApplication;
import com.resync.app.ui.views.text.BaseTextView;
import java.util.concurrent.Future;
import com.resync.app.widgets.RecyclableView;
import com.resync.app.text.spans.CustomClickableSpan;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: oc.b */
/* JADX INFO: compiled from: SpannableTextView.java */
/* JADX INFO: loaded from: classes3.dex */
public abstract class SpannableTextView extends BaseTextView implements RecyclableView {
    private SpannableTextViewStringBuilder builder;
    private Handler handler;
    private boolean longClicked;
    private Future<PrecomputedTextCompat> textFuture;

    public SpannableTextView(Context context, String str) {
        super(context, str);
        this.longClicked = false;
        init();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void triggerLongClick(CustomClickableSpan[] customClickableSpanArr) {
        clearFocus();
        customClickableSpanArr[0].onLongClick(this);
        this.longClicked = true;
    }

    private final void init() {
        this.builder = new SpannableTextViewStringBuilder();
        this.handler = new Handler();
        setLongClickable(false);
        setClickable(false);
        setFocusable(false);
        setHyphenationFrequency(0);
    }

    protected final SpannableTextViewStringBuilder resetBuilder() {
        this.builder.reset();
        return this.builder;
    }

    public boolean hasUnloadedImages() {
        return this.builder.hasUnloadedImages();
    }

    public final void setSpannableText(String str) {
        resetBuilder().append(str);
        commitText();
    }

    /**
     * Re-run the text layout (e.g. after a collapsed {@code WebPreviewImageSpan} loads its image and
     * needs to expand). loadImages skips already-attempted preview spans, so this won't re-fetch/loop.
     */
    public final void refresh() {
        commitText();
    }

    public final void commitText() {
        CharSequence charSequenceD = this.builder.build();
        if (!this.builder.canUsePrecomputedText()) {
            Logger.verbose("SPANS", "Using legacy setText(): " + this);
            setText(charSequenceD, TextView.BufferType.SPANNABLE);
            this.builder.loadImages(this);
            return;
        }
        Logger.verbose("SPANS", "Using text future " + this);
        Future<PrecomputedTextCompat> futureD = PrecomputedTextCompat.getTextFuture(charSequenceD, TextViewCompat.getTextMetricsParams(this), RedditApplication.threadPool);
        this.textFuture = futureD;
        setTextFuture(futureD);
        this.builder.loadImages(this);
    }

    @Override // com.resync.app.widgets.RecyclableView
    public final void recycle() {
        Logger.verbose("SPANS", "Recycling: " + this);
        this.builder.reset();
        Future<PrecomputedTextCompat> future = this.textFuture;
        if (future != null) {
            if (!future.isDone() && !this.textFuture.isCancelled()) {
                Logger.verbose("CANCELLING: " + this.textFuture);
                try {
                    this.textFuture.cancel(true);
                } catch (Exception e) {
                    Logger.logException(e);
                }
            }
            this.textFuture = null;
        }
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        Handler handler;
        if (getText() instanceof Spanned) {
            Spanned spanned = (Spanned) getText();
            int action = motionEvent.getAction();
            if (action == 3 && (handler = this.handler) != null) {
                handler.removeCallbacksAndMessages(null);
            }
            if (action == 1 || action == 0) {
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                int totalPaddingLeft = x - getTotalPaddingLeft();
                int totalPaddingTop = y - getTotalPaddingTop();
                int scrollX = totalPaddingLeft + getScrollX();
                int scrollY = totalPaddingTop + getScrollY();
                Layout layout = getLayout();
                int lineForVertical = layout.getLineForVertical(scrollY);
                float xCoord = scrollX;
                int offsetForHorizontal = layout.getOffsetForHorizontal(lineForVertical, xCoord);
                float lineWidth = layout.getLineWidth(lineForVertical);
                final CustomClickableSpan[] customClickableSpanArr = (CustomClickableSpan[]) spanned.getSpans(offsetForHorizontal, offsetForHorizontal, CustomClickableSpan.class);
                if (customClickableSpanArr.length != 0 && xCoord <= lineWidth) {
                    if (action == 1) {
                        Handler handler2 = this.handler;
                        if (handler2 != null) {
                            handler2.removeCallbacksAndMessages(null);
                        }
                        if (!this.longClicked) {
                            customClickableSpanArr[0].onClick(this);
                        }
                        this.longClicked = false;
                    } else {
                        this.handler.postDelayed(new Runnable() { // from class: oc.a
                            @Override // java.lang.Runnable
                            public final void run() {
                                SpannableTextView.this.triggerLongClick(customClickableSpanArr);
                            }
                        }, 250L);
                    }
                    return true;
                }
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public SpannableTextView(Context context, int typefaceIndex, int textSizeIndex, int textColorType) {
        super(context, typefaceIndex, textSizeIndex, textColorType);
        this.longClicked = false;
        init();
    }

    public SpannableTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.longClicked = false;
        init();
    }
}
