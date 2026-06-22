package com.resync.app.internal;

import com.resync.app.R;

/**
 * Minimal reconstruction of the obfuscated R$styleable arrays the app actually uses.
 * R8 had merged the app's custom styleables into this synthetic class (com.resync.app.internal.SyncStyleable). Only the
 * arrays referenced by app code are reconstructed here, each from custom app attrs that
 * exist in res/values/attrs.xml. The FULL decompiled R$styleable copy is intentionally
 * NOT restored — it referenced framework attrs absent from the app R and produced ~1800
 * spurious errors. Attr ordering matches the original (verified vs jadx-dexkeyed
 * d7/e.java); ordering matters because consumers index the resulting TypedArray positionally.
 * Field names were de-obfuscated to the view/preference that each styleable belongs to.
 */
public final class SyncStyleable {

    public static final int[] IMAGE_SPAN_TEXT_VIEW = {R.attr.line_multiplier, R.attr.show_border};
    public static final int[] REDDIT_BUTTON = {R.attr.filterColor, R.attr.showExtendedRipple, R.attr.showRipple};
    public static final int[] BASE_TEXT_VIEW = {R.attr.layoutSource, R.attr.textColor, R.attr.textSize, R.attr.typeface};
    public static final int[] ROUND_CORNERS_FRAME = {R.attr.round_corners};
    public static final int[] ROUND_CORNERS_LINEAR = {R.attr.round_corners};
    public static final int[] CONTENT_CHIP = {R.attr.content_highlight_color};
    public static final int[] ZOOMABLE_VIDEO = {R.attr.zoomable};
    public static final int[] SHAPED_IMAGE_VIEW = {R.attr.shape_background, R.attr.shape_circular, R.attr.shape_round_corners};
    public static final int[] MATERIAL_ROW = {R.attr.rowImageSrc, R.attr.rowTitle};
    public static final int[] MONET_COLOR_VIEW = {R.attr.swatchColor};
    public static final int[] MONET_HIGHLIGHT_BUTTON = {R.attr.selected};
    public static final int[] SINGLE_THEME_PREVIEW = {R.attr.dark};
    public static final int[] SEARCH_BOX_VIEW = {R.attr.foreground_clickable, R.attr.hide_more, R.attr.hint};
    public static final int[] SHEET_HEADER_VIEW = {R.attr.show_divider, R.attr.show_menu, R.attr.title_text};
    public static final int[] SUB_VIEW = {R.attr.enable_click_preview, R.attr.force_circular_outline};
    public static final int[] CATEGORY_HEADER = {R.attr.categoryNight, R.attr.categoryTitle};
    public static final int[] CATEGORY_INFO_HEADER = {R.attr.categoryInfoNight, R.attr.categoryInfoTitle};
    public static final int[] FORMATTING_BUTTON = {R.attr.imageSource};
    public static final int[] SHAPED_IMAGE_FRAME = {R.attr.shape_background, R.attr.shape_circular, R.attr.shape_round_corners};
    public static final int[] NAVIGATION_TRANSITION = {R.attr.nt_transitionMode};
    public static final int[] SYNC_PREFERENCE = {R.attr.custom_icon, R.attr.night, R.attr.preference_ref};
    public static final int[] HTML_TABLE_VIEW = {R.attr.layoutSource};
    public static final int[] TOOLTIP_VIEW = {R.attr.tooltip_icon, R.attr.tooltip_setting_name, R.attr.tooltip_setting_show_once, R.attr.tooltip_text};
    public static final int[] VIEW_TYPE_PREVIEW = {R.attr.ui_mode};
    public static final int[] ULTRA_PURCHASE_VIEW = {R.attr.purchase_selected};
}
