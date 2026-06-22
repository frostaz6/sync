package com.resync.app.ui.views.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import com.bumptech.glide.RedditHelper;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.ui.interfaces.DarkModeAware;

/* JADX INFO: loaded from: classes3.dex */
public class BaseTextView extends AppCompatTextView implements DarkModeAware {
    private int typefaceIndex;
    private int textSizeIndex;
    private int textColorType;
    private boolean accentColored;
    private boolean linkColored;
    private boolean visited;
    private boolean disableVisitedDim;

    public BaseTextView(Context context, int typefaceIndex, int textSizeIndex, int textColorType) {
        super(context);
        this.typefaceIndex = typefaceIndex;
        this.textSizeIndex = textSizeIndex;
        this.textColorType = textColorType;
        setup();
    }

    private int resolveTextColor(int colorType, boolean disableVisitedDim) {
        if (this.accentColored) {
            return MonetUtil.getAccent3ColorAuto();
        }
        if (this.linkColored) {
            return MonetUtil.getLinkColorAuto();
        }
        if (colorType == 4) {
            return getCurrentTextColor();
        }
        if (colorType == 1) {
            return StyleSingleton.getTextColorAuto(getContext(), !disableVisitedDim && isVisited());
        }
        if (colorType == 2) {
            return MonetUtil.getSecondaryTextColorAuto();
        }
        if (colorType == 3) {
            return getLinkColor();
        }
        throw new IllegalArgumentException("Unsupported text color: " + colorType);
    }

    private Typeface resolveTypeface(int index) {
        return TypefaceManager.get(index);
    }

    private int getLinkColor() {
        return MonetUtil.getLinkColorAuto();
    }

    public static int getTextSize(int sizeIndex) {
        if (sizeIndex == 5) {
            return RedditHelper.getBodyTextSize(SettingsSingleton.getSettings().fontSize);
        }
        if (sizeIndex == 1) {
            return RedditHelper.getSmallHeaderTextSize(SettingsSingleton.getSettings().fontSize);
        }
        if (sizeIndex == 2) {
            return RedditHelper.getMediumTextSize(SettingsSingleton.getSettings().fontSize);
        }
        if (sizeIndex == 3) {
            return RedditHelper.getSubtitleTextSize(SettingsSingleton.getSettings().fontSize);
        }
        if (sizeIndex == 4) {
            return RedditHelper.getHeaderTextSize(SettingsSingleton.getSettings().fontSize);
        }
        if (sizeIndex == 6) {
            return RedditHelper.getFixedLargeTextSize();
        }
        throw new IllegalArgumentException("Unsupported text size");
    }

    private void setup() {
        setTypeface(resolveTypeface(this.typefaceIndex));
        setTextSize(1, getTextSize(this.textSizeIndex));
        setLinkTextColor(getLinkColor());
        refreshTextColor();
    }

    private void applyStyleKey(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        if (key.equals("slide_title")) {
            this.typefaceIndex = SettingsSingleton.getSettings().slide_font_title;
            this.textSizeIndex = SettingsSingleton.getSettings().slide_size_title;
            this.textColorType = 1;
            return;
        }
        if (key.equals("slide_desc")) {
            this.typefaceIndex = SettingsSingleton.getSettings().slide_font_description;
            this.textSizeIndex = SettingsSingleton.getSettings().slide_size_description;
            this.textColorType = 2;
            return;
        }
        if (key.equals("slide_selftext")) {
            this.typefaceIndex = SettingsSingleton.getSettings().slide_font_selftext;
            this.textSizeIndex = SettingsSingleton.getSettings().slide_size_selftext;
            this.textColorType = 1;
            return;
        }
        if (key.equals("card_title")) {
            this.typefaceIndex = SettingsSingleton.getSettings().card_font_title;
            this.textSizeIndex = SettingsSingleton.getSettings().card_size_title;
            this.textColorType = 1;
            return;
        }
        if (key.equals("card_desc")) {
            this.typefaceIndex = SettingsSingleton.getSettings().card_font_description;
            this.textSizeIndex = SettingsSingleton.getSettings().card_size_description;
            this.textColorType = 2;
            return;
        }
        if (key.equals("card_selftext")) {
            this.typefaceIndex = SettingsSingleton.getSettings().card_font_selftext;
            this.textSizeIndex = SettingsSingleton.getSettings().card_size_selftext;
            this.textColorType = 1;
            return;
        }
        if (key.equals("small_card_title")) {
            this.typefaceIndex = SettingsSingleton.getSettings().small_card_font_title;
            this.textSizeIndex = SettingsSingleton.getSettings().small_card_size_title;
            this.textColorType = 1;
            return;
        }
        if (key.equals("small_card_desc")) {
            this.typefaceIndex = SettingsSingleton.getSettings().small_card_font_description;
            this.textSizeIndex = SettingsSingleton.getSettings().small_card_size_description;
            this.textColorType = 2;
            return;
        }
        if (key.equals("smaller_card_title")) {
            this.typefaceIndex = SettingsSingleton.getSettings().smaller_card_font_title;
            this.textSizeIndex = SettingsSingleton.getSettings().smaller_card_size_title;
            this.textColorType = 1;
            return;
        }
        if (key.equals("smaller_card_desc")) {
            this.typefaceIndex = SettingsSingleton.getSettings().smaller_card_font_description;
            this.textSizeIndex = SettingsSingleton.getSettings().smaller_card_size_description;
            this.textColorType = 2;
            return;
        }
        if (key.equals("compact_title")) {
            this.typefaceIndex = SettingsSingleton.getSettings().compact_font_title;
            this.textSizeIndex = SettingsSingleton.getSettings().compact_size_title;
            this.textColorType = 1;
            return;
        }
        if (key.equals("compact_desc")) {
            this.typefaceIndex = SettingsSingleton.getSettings().compact_font_description;
            this.textSizeIndex = SettingsSingleton.getSettings().compact_size_description;
            this.textColorType = 2;
            return;
        }
        if (key.equals("list_title")) {
            this.typefaceIndex = SettingsSingleton.getSettings().list_font_title;
            this.textSizeIndex = SettingsSingleton.getSettings().list_size_title;
            this.textColorType = 1;
            return;
        }
        if (key.equals("list_desc")) {
            this.typefaceIndex = SettingsSingleton.getSettings().list_font_description;
            this.textSizeIndex = SettingsSingleton.getSettings().list_size_description;
            this.textColorType = 2;
            return;
        }
        if (key.equals("comments_description")) {
            this.typefaceIndex = SettingsSingleton.getSettings().comments_description_typeface;
            this.textSizeIndex = SettingsSingleton.getSettings().comments_description_size;
            this.textColorType = 2;
        } else {
            if (key.equals("comments_body")) {
                this.typefaceIndex = SettingsSingleton.getSettings().comments_body_typeface;
                this.textSizeIndex = SettingsSingleton.getSettings().comments_body_size;
                setLineSpacing(0.0f, SettingsSingleton.getSettings().comments_body_space);
                this.textColorType = 1;
                return;
            }
            if (key.equals("sidebar") || key.equals("wiki")) {
                this.typefaceIndex = SettingsSingleton.getSettings().card_font_selftext;
                this.textSizeIndex = SettingsSingleton.getSettings().card_size_selftext;
                this.textColorType = 1;
            }
        }
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        refreshTextColor();
    }

    public boolean isVisitedDimDisabled() {
        return this.disableVisitedDim;
    }

    public boolean isVisited() {
        return this.visited;
    }

    public void refreshTextColor() {
        setTextColor(resolveTextColor(this.textColorType, isVisitedDimDisabled()));
    }

    @Override // android.view.View
    public void scrollTo(int x, int y) {
    }

    public void setUnread(boolean unread) {
    }

    public void setVisitedDimDisabled(boolean disabled) {
        this.disableVisitedDim = disabled;
    }

    public void setLinkColored(boolean linkColored) {
        if (this.linkColored != linkColored) {
            this.linkColored = linkColored;
            refreshTextColor();
        }
    }

    public void setVisited(boolean visited) {
        if (this.visited != visited) {
            this.visited = visited;
            refreshTextColor();
        }
    }

    public BaseTextView(Context context, String styleKey) {
        super(context);
        this.typefaceIndex = 9;
        this.textSizeIndex = 5;
        this.textColorType = 4;
        applyStyleKey(styleKey);
        setup();
    }

    public BaseTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.typefaceIndex = 9;
        this.textSizeIndex = 5;
        this.textColorType = 4;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, SyncStyleable.BASE_TEXT_VIEW);
        if (typedArrayObtainStyledAttributes.hasValue(0)) {
            applyStyleKey(typedArrayObtainStyledAttributes.getString(0));
        }
        this.typefaceIndex = typedArrayObtainStyledAttributes.getInt(3, this.typefaceIndex);
        this.textSizeIndex = typedArrayObtainStyledAttributes.getInt(2, this.textSizeIndex);
        this.textColorType = typedArrayObtainStyledAttributes.getInt(1, this.textColorType);
        typedArrayObtainStyledAttributes.recycle();
        setup();
    }
}
