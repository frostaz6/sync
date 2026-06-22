package com.resync.app.ui.views.text.spannable.children;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import androidx.core.graphics.ColorUtils;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.helpers.ui.MaterialUtil;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.text.html.Options;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.TableHelper;
import com.resync.app.helpers.WindowUtils;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes3.dex */
public class HtmlTableView extends LinearLayout {

    private HtmlTextView htmlTextView;
    private String styleKey;
    private Post post;

    public HtmlTableView(Context context, String str) {
        super(context);
        this.styleKey = str;
        init();
    }

    private void init() {
        setOrientation(1);
        setClipToPadding(false);
        setBaselineAligned(false);
        HtmlTextView htmlTextViewA = createHtmlTextView();
        this.htmlTextView = htmlTextViewA;
        addView(htmlTextViewA);
    }

    protected HtmlTextView createHtmlTextView() {
        HtmlTextView htmlTextView = new HtmlTextView(getContext(), this.styleKey);
        htmlTextView.setTextColor(StyleSingleton.getTextColorAuto(getContext(), false));
        return htmlTextView;
    }

    public boolean hasGradientBackground() {
        return getBackground() instanceof GradientDrawable;
    }

    public void applyDefaultPadding() {
        int padding = SizeHelper.dpToPx(16);
        setPadding(padding, padding, padding, padding);
    }

    public void applySlidePadding() {
        if (getBackground() instanceof GradientDrawable) {
            ((GradientDrawable) getBackground()).setCornerRadius(0.0f);
        }
        if (!SettingsSingleton.getSettings().slideSelftextBorder) {
            int horizontalPadding = SizeHelper.dpToPx(16);
            setPadding(horizontalPadding, 0, horizontalPadding, 0);
        } else {
            int horizontalPadding = SizeHelper.dpToPx(16);
            int verticalPadding = SizeHelper.dpToPx(8);
            setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
        }
    }

    public void setText(String str) {
        if (str != null) {
            str = str.trim();
            Logger.verbose("text: " + str.length());
        }
        if ("<p>&#x200B;</p>".equalsIgnoreCase(str)) {
            str = null;
        }
        if (TextUtils.isEmpty(str)) {
            setVisibility(8);
        } else {
            setVisibility(0);
            buildContent(str);
        }
    }

    public void bindPost(Post post) {
        this.post = post;
        setText(post.getSelftextProcessed());
    }

    public void buildContent(String str) {
        int contentWidth = (((WindowUtils.getDisplayWidth() - getPaddingLeft()) - getPaddingRight()) - ((View) getParent()).getPaddingLeft()) - ((View) getParent()).getPaddingRight();
        if (!StringUtils.containsIgnoreCase(str, "<table>")) {
            this.htmlTextView.setHtmlWithOptions(new Options().withWidth(contentWidth).withPost(this.post), str);
            return;
        }
        removeAllViews();
        String[] strArrSplit = str.replaceAll("<table>", "###TABLE###<table>").replaceAll("</table>", "</table>###TABLE###").trim().split("###TABLE###");
        Logger.verbose("Sections: " + strArrSplit.length);
        for (String str2 : strArrSplit) {
            if (!TextUtils.isEmpty(str2)) {
                if (str2.startsWith("<table>")) {
                    Logger.verbose("Adding table section");
                    View viewA = TableHelper.buildTableView(getContext(), str2, this.post);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
                    layoutParams.topMargin = (int) SizeHelper.dpToPxFloat(16);
                    layoutParams.bottomMargin = (int) SizeHelper.dpToPxFloat(16);
                    viewA.setLayoutParams(layoutParams);
                    addView(viewA);
                }
                if (!TextUtils.isEmpty(str2)) {
                    HtmlTextView htmlTextViewA = createHtmlTextView();
                    addView(htmlTextViewA);
                    htmlTextViewA.setHtmlWithOptions(new Options().withWidth(contentWidth).withPost(this.post), str2);
                }
            }
        }
    }

    public void applyCardBackground() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        if (MonetUtil.isAmoled()) {
            int strokeColor = ColorUtils.setAlphaComponent(MonetUtil.getPrimaryTextColor(true), 50);
            gradientDrawable.setCornerRadius(MaterialUtil.getCornerRadius());
            gradientDrawable.setStroke(SizeHelper.dpToPx(1), strokeColor);
        } else {
            gradientDrawable.setColor(MonetUtil.getCardColorAuto());
        }
        gradientDrawable.setCornerRadius(SizeHelper.dpToPx(8));
        setBackground(gradientDrawable);
        int horizontalPadding = SizeHelper.dpToPx(8);
        int verticalPadding = SizeHelper.dpToPx(8);
        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
    }

    public void reset() {
        removeAllViews();
        init();
    }

    public HtmlTableView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, SyncStyleable.HTML_TABLE_VIEW);
        this.styleKey = typedArrayObtainStyledAttributes.getString(0);
        typedArrayObtainStyledAttributes.recycle();
        init();
    }
}
