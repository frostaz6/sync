package com.resync.app.singletons;

import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.text.spans.AwardImageSpan;

/* JADX INFO: renamed from: j9.f */
/* JADX INFO: compiled from: SpanSingleton.java */
/* JADX INFO: loaded from: classes.dex */
public final class SpanSingleton {
    static final Object LOCK = new Object();
    static SpanSingleton instance;

    private ForegroundColorSpan goldSpan;
    private ForegroundColorSpan nsfwSpan;
    private AwardImageSpan awardImageSpan;
    private ForegroundColorSpan crosspostSpan;

    private StyleSpan boldSpan;

    private StyleSpan boldSpanAlt;
    private StyleSpan italicSpan;
    private ForegroundColorSpan highlightSpan;

    private ForegroundColorSpan spoilerSpan;

    private SpanSingleton() {
        rebuild();
    }

    public static SpanSingleton getInstance() {
        SpanSingleton spanSingleton;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new SpanSingleton();
            }
            spanSingleton = instance;
        }
        return spanSingleton;
    }

    public static AwardImageSpan getAwardImageSpan() {
        return getInstance().awardImageSpan;
    }

    public static StyleSpan getBoldSpan() {
        return getInstance().boldSpan;
    }

    public static ForegroundColorSpan newLinkColorSpan() {
        return new ForegroundColorSpan(MonetUtil.getLinkColorAuto());
    }

    public static ForegroundColorSpan getSpoilerSpan() {
        return getInstance().spoilerSpan;
    }

    public static ForegroundColorSpan newLinkColorSpan2() {
        return new ForegroundColorSpan(MonetUtil.getLinkColorAuto());
    }

    public static StyleSpan getItalicSpan() {
        return getInstance().italicSpan;
    }

    public static ForegroundColorSpan newLinkColorSpan3() {
        return new ForegroundColorSpan(MonetUtil.getLinkColorAuto());
    }

    public static ForegroundColorSpan getNsfwSpan() {
        return getInstance().nsfwSpan;
    }

    public static ForegroundColorSpan getCrosspostSpan() {
        return getInstance().crosspostSpan;
    }

    public static ForegroundColorSpan getGoldSpan() {
        return getInstance().goldSpan;
    }

    public static StyleSpan getBoldSpanAlt() {
        return getInstance().boldSpanAlt;
    }

    public static ForegroundColorSpan newLinkColorSpan4() {
        return new ForegroundColorSpan(MonetUtil.getLinkColorAuto());
    }

    public static ForegroundColorSpan newLinkColorSpan5() {
        return new ForegroundColorSpan(MonetUtil.getLinkColorAuto());
    }

    public static ForegroundColorSpan newLinkColorSpan6() {
        return new ForegroundColorSpan(MonetUtil.getLinkColorAuto());
    }

    public static ForegroundColorSpan getHighlightSpan() {
        return getInstance().highlightSpan;
    }

    public static void rebuildInstance() {
        instance = new SpanSingleton();
    }

    public void rebuild() {
        this.goldSpan = new ForegroundColorSpan(-360334);
        this.nsfwSpan = new ForegroundColorSpan(-2818048);
        this.awardImageSpan = new AwardImageSpan();
        this.crosspostSpan = new ForegroundColorSpan(-360334);
        this.boldSpan = new StyleSpan(1);
        this.boldSpanAlt = new StyleSpan(1);
        this.italicSpan = new StyleSpan(2);
        this.highlightSpan = new ForegroundColorSpan(-35584);
        this.spoilerSpan = new ForegroundColorSpan(-7039745);
    }
}
