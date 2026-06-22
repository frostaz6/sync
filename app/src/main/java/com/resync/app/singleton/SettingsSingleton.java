package com.resync.app.singleton;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Pair;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import java.io.Serializable;
import java.lang.reflect.Field;
import com.resync.app.helpers.ChromeCustomTabsHelper;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.helpers.PostsHelper;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.helpers.DataSavingHelper;
import com.resync.app.util.Logger;
import com.resync.app.util.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public final class SettingsSingleton {
    private static final Object LOCK = new Object();
    private static SettingsSingleton instance;

    private Settings settings;

    public static class Settings implements Serializable {
        public boolean actionsAbout;
        public boolean actionsContextualChips;
        public boolean actionsDarkMode;
        public boolean actionsDarkOverlay;
        public boolean actionsDataSaving;
        public boolean actionsFilters;
        public boolean actionsFriends;
        public boolean actionsFullyExpand;
        public boolean actionsHideRead;
        public boolean actionsMessaging;
        public boolean actionsProfile;
        public boolean actionsRandom;
        public boolean actionsRandomNsfw;
        public boolean actionsRecentlyViewed;
        public boolean actionsRefresh;
        public boolean actionsSaved;
        public boolean actionsScrollToTop;
        public boolean actionsSearch;
        public boolean actionsSettings;
        public boolean actionsSort;
        public boolean actionsSubmit;
        public boolean actionsSubreddits;
        public boolean actionsSwipe;
        public boolean actionsSync;
        public boolean actionsToggleAccount;
        public boolean actionsView;
        public boolean actionsWatched;
        public boolean alignThumbnailsRight;
        public boolean amoled_divider;
        public boolean analyticsFabric;
        public boolean analyticsFlurry;
        public boolean analyticsGoogle;
        public boolean animateCommentActions;
        public boolean animateCommentCollapse;
        public boolean animateCommentInsert;
        public boolean animateCommentMore;
        public boolean animateCommentsNavigation;
        public boolean animateEnter;
        public boolean animatePostHide;
        public boolean animateTransitionComments;
        public boolean animateTransitionImage;
        public int autoNightEndHour;
        public int autoNightEndMin;
        public int autoNightStartHour;
        public int autoNightStartMin;
        public boolean autohideToolbar;
        public int autoplay;
        public boolean awardsMinimal;
        public boolean awardsModern;
        public boolean awardsNone;
        public boolean biometric;
        public boolean biometric_start;
        public boolean blackNavBar;
        public boolean blackNavBarNight;
        public boolean bypass_amp;
        public boolean cacheStreamingVideos;
        public boolean cachedComments;
        public boolean cardFullWidth;
        public boolean cardImageAbove;
        public boolean cardImageFullHeight;
        public boolean cardSelftextExpand;
        public boolean cardSelftextPreviews;
        public boolean cardTextLabels;
        public boolean card_buttons_hide;
        public boolean card_buttons_mod;
        public boolean card_buttons_save;
        public boolean card_buttons_share;
        public int card_font_description;
        public int card_font_selftext;
        public int card_font_title;
        public int card_size_description;
        public int card_size_selftext;
        public int card_size_title;
        public boolean cctPreload;
        public boolean changelogs;
        public boolean collapseCustomFeed;
        public boolean collapseFav;
        public boolean collapseFeeds;
        public boolean collapseFollows;
        public boolean collapseModerated;
        public boolean collapseParent;
        public boolean collapseSearchAction;
        public boolean collapseSearchRecent;
        public boolean collapseSearchResults;
        public boolean collapseSearchSubscribed;
        public boolean collapseSubscriptions;
        public boolean collapseTop;
        public boolean coloredNavBar;
        public boolean coloredNavBarNight;
        public boolean colorfulCommentQuotes;
        public boolean colorfulComments;
        public int commentAutoRefreshValue;
        public boolean commentDividers;
        public boolean commentMediaCropped;
        public boolean commentMediaFull;
        public boolean commentMediaThumbnail;
        public boolean commentNavBar;
        public boolean commentNavBarHide;
        public int commentNavigationFirst;
        public int commentNavigationPosition;
        public int commentNavigationSecond;
        public int commentNavigationThird;
        public int commentPadding;
        public String commentSort;
        public boolean commentSwipeActions;
        public boolean commentSwipeActionsVibrate;
        public boolean comment_action_about;
        public boolean comment_action_collapse_all;
        public boolean comment_action_images;
        public boolean comment_action_other;
        public boolean comment_action_recents;
        public boolean comment_action_refresh;
        public boolean comment_action_reply;
        public boolean comment_action_search;
        public boolean comment_action_settings;
        public boolean comment_action_sort;
        public boolean comment_action_sports_mode;
        public boolean comment_action_translate;
        public boolean commentsAlternative;
        public boolean commentsAlwaysShow;
        public boolean commentsAnimatedImages;
        public boolean commentsAuthorProfilePic;
        public boolean commentsCollapseAllDefault;
        public boolean commentsControversial;
        public int commentsCount;
        public int commentsDefaultNavigation;
        public boolean commentsDoubleSpacing;
        public boolean commentsEmotes;
        public boolean commentsFlair;
        public boolean commentsHideAutomod;
        public boolean commentsHighlightNew;
        public boolean commentsHighlightNewBackground;
        public boolean commentsLinksExpanded;
        public int commentsParentButton;
        public boolean commentsRememberPos;
        public boolean commentsReplyFab;
        public boolean commentsReplyFabAutoHide;
        public boolean commentsReverseActions;
        public boolean commentsScrollbar;
        public boolean commentsSingle;
        public boolean commentsSwipe;
        public boolean commentsTopLevelPadding;
        public int comments_body_size;
        public float comments_body_space;
        public int comments_body_typeface;
        public int comments_description_size;
        public int comments_description_typeface;
        public boolean compactSingleLine;
        public boolean compactThumbnails;
        public int compact_font_description;
        public int compact_font_title;
        public int compact_size_description;
        public int compact_size_title;
        public int contentColor;
        public int contentColorNight;
        public boolean darkOverlay;
        public int dataSavingMode;
        public int deepzoomDpi;
        public int deepzoomMax;
        public int defaultBrowser;
        public String defaultSub;
        public boolean delimitCommentScores;
        public boolean developerVideo;
        public boolean dimImages;
        public boolean doh;
        public boolean drawerBack;
        public boolean drawerSubIcons;
        public boolean drawerUnread;
        public boolean dualLandscape;
        public boolean dualPortait;
        public boolean duplicateFavs;
        public boolean embedYouTube;
        public boolean enhancedAutoPlay;
        public boolean exitConfirmation;
        public boolean extendedSort;
        public boolean fastScroller;
        public boolean filtersContentAlbum;
        public boolean filtersContentGif;
        public boolean filtersContentImage;
        public boolean filtersContentLink;
        public boolean filtersContentNsfw;
        public boolean filtersContentText;
        public boolean filtersContentVideo;
        public boolean first_time_full_height;
        public boolean first_time_legacy_comment_settings;
        public boolean first_time_legacy_settings;
        public boolean first_time_monet_intro_settings;
        public boolean first_time_paint;
        public boolean first_time_tag;
        public boolean fixedHeightVideo;
        public boolean flairMinimal;
        public boolean flairModern;
        public boolean flairNone;
        public int fontSize;
        public boolean forceAutoRotate;
        public boolean formattingButtonBold;
        public boolean formattingButtonBugReport;
        public boolean formattingButtonBullet;
        public boolean formattingButtonDraft;
        public boolean formattingButtonEmoji;
        public boolean formattingButtonImage;
        public boolean formattingButtonInfo;
        public boolean formattingButtonItalic;
        public boolean formattingButtonLink;
        public boolean formattingButtonNumbered;
        public boolean formattingButtonPreview;
        public boolean formattingButtonPromo;
        public boolean formattingButtonSpeech;
        public boolean formattingButtonSpoiler;
        public boolean formattingButtonStrikethrough;
        public boolean formattingButtonSuperscript;
        public boolean formattingButtonUser;
        public boolean gfycat;
        public boolean gifSeek;
        public boolean gifSpeed;
        public boolean hideReddit;
        public boolean hide_username;
        public boolean highlightDefault;
        public boolean highlightFriends;
        public boolean highlightNewAccounts;
        public boolean highlightOp;
        public boolean highlightYourself;
        public boolean iconShortcuts;
        public int imageCacheDuration;
        public boolean imageColorfulBackground;
        public boolean imageDebug;
        public boolean imageHandling;
        public boolean imageShareTitle;
        public boolean images_immersive;
        public boolean imgurGallery;
        public boolean incognitoKeyboard;
        public boolean incognitoTabs;
        public boolean inlineImagePreviews;
        public boolean inlineLinkSources;
        public boolean largeLinkSources;
        public boolean limitImage;
        public int list_font_description;
        public int list_font_title;
        public int list_size_description;
        public int list_size_title;
        public boolean mainFab;
        public int mainFabAction;
        public boolean mainFabAutohide;
        public boolean markAsRead;
        public boolean markAsReadOnScroll;
        public boolean markAsReadSync;
        public boolean mixtape;
        public boolean monetForceAmoled;
        public boolean monet_boost_dark_color;
        public boolean monet_boost_light_color;
        public float monet_color_intensity;
        public boolean monet_custom_palette;
        public int monet_manual_theme_color;
        public int monet_override_dark_link_color;
        public int monet_override_dark_primary_text_color;
        public int monet_override_dark_secondary_text_color;
        public int monet_override_light_link_color;
        public int monet_override_light_primary_text_color;
        public int monet_override_light_secondary_text_color;
        public boolean monet_system;
        public boolean navigationBottom;
        public boolean navigationBottomActions;
        public boolean navigationBottomHide;
        public boolean navigationBottomLabels;
        public int navigationRailGravity;
        public boolean nsfw;
        public boolean nsfwMute;
        public boolean nsfwPreviews;
        public boolean nsfwRecents;
        public boolean peek;
        public boolean peekMute;
        public boolean peekOptions;
        public int postColumnsLandscape;
        public int postColumnsPortrait;
        public boolean postDividers;
        public int postNavigationFirst;
        public int postNavigationSecond;
        public int postNavigationThird;
        public boolean postOc;
        public boolean postScoresShort;
        public boolean postScrollbar;
        public transient Pair<String, String> postSort;
        public boolean postSwipeActions;
        public boolean postSwipeActionsVibrate;
        public boolean postsCommentsNewActivity;
        public boolean postsDisplayNew;
        public boolean postsHome;
        public boolean postsLongPress;
        public boolean postsQuickScroll;
        public boolean postsShowAuthor;
        public int preloadGifs;
        public int preloadImages;
        public int preloadPreviews;
        public int preloadThumbs;
        public int primaryColor;
        public int primaryColorNight;
        public int primaryTextColor;
        public int primaryTextColorNight;
        public boolean pushNotifications;
        public boolean quickDismiss;
        public boolean recordHistory;
        public boolean recordTopSubreddits;
        public boolean redditVideo;
        public int redditVideoQuality;
        public boolean redditVideoQualityCheck;
        public boolean redgifs;
        public boolean reverseDrawers;
        public boolean saveNomedia;
        public boolean searchFocusOnOpen;
        public int secondaryTextColor;
        public int secondaryTextColorNight;
        public boolean secretPanel;
        public boolean selectiveNsfwPreviews;
        public boolean selftextLinksExpanded;
        public boolean slideFullWidth;
        public boolean slideImageFullHeight;
        public boolean slideImagePreviews;
        public boolean slideImageTop;
        public boolean slideSelftextBorder;
        public boolean slideSelftextPreviews;
        public boolean slideSubredditThumbnail;
        public int slideThumbnailSize;
        public boolean slideTwitterPreviews;
        public boolean slide_button_vote_right;
        public boolean slide_buttons_hide;
        public boolean slide_buttons_mod;
        public boolean slide_buttons_save;
        public boolean slide_buttons_share;
        public int slide_font_description;
        public int slide_font_selftext;
        public int slide_font_title;
        public boolean slide_hide_all_buttons;
        public int slide_size_description;
        public int slide_size_selftext;
        public int slide_size_title;
        public boolean slidesThickDivider;
        public boolean smallCardsAlignThumbnailsLeft;
        public boolean small_card_buttons_downvote;
        public boolean small_card_buttons_hide;
        public boolean small_card_buttons_mod;
        public boolean small_card_buttons_save;
        public boolean small_card_buttons_share;
        public boolean small_card_buttons_upvote;
        public int small_card_font_description;
        public int small_card_font_title;
        public int small_card_size_description;
        public int small_card_size_title;
        public int smaller_card_font_description;
        public int smaller_card_font_title;
        public int smaller_card_size_description;
        public int smaller_card_size_title;
        public boolean snap;
        public boolean stickyHighlight;
        public boolean streamable;
        public boolean subFolders;
        public boolean subredditIcons;
        public boolean subredditsFocusOnOpen;
        public boolean subredditsFullyExpand;
        public boolean subredditsRememberPosition;
        public boolean subredditsShowCustom;
        public boolean subredditsShowFav;
        public boolean subredditsShowFeeds;
        public boolean subredditsShowFollows;
        public boolean subredditsShowModerator;
        public boolean subredditsShowSubscriptions;
        public boolean subredditsShowTop;
        public boolean swipeDim;
        public boolean swipeDismiss;
        public boolean swipeHidePosts;
        public float swipeSensitivity;
        public boolean swipeSubs;
        public boolean tabletDualPaneLandscape;
        public boolean tabletDualPanePortrait;
        public boolean tabletInsets;
        public boolean tabletMode;
        public boolean tabletModeFoldable;
        public boolean tabletTabs;
        public boolean tapDomainsOpen;
        public boolean tapSubredditsOpen;
        public boolean tapUsernamesOpen;
        public boolean toolbarExpanded;
        public boolean transparentStatus;
        public boolean tumblrPosts;
        public boolean typeIndicators;
        public boolean ultra_default_to_logged_out;
        public boolean ultra_rickroll;
        public boolean ultra_website_previews;
        public boolean updateReminder;
        public boolean useDrawer;
        public boolean usePanel;
        public String userAgent;
        public boolean videoMute;
        public boolean vidme;
        public boolean viewedPeek;
        public boolean viewerShowSave;
        public boolean volumeKeyNavigation;
        public boolean whiteNavBar;
        public boolean whiteNavBarNight;
        public boolean zoomTall;
        public boolean zoomVideo;

        public Settings getCopy() {
            return null;
        }

        public int getHash(boolean excludeVolatile) {
            StringBuilder sb2 = new StringBuilder();
            try {
                for (Field field : Settings.class.getDeclaredFields()) {
                    if (!excludeVolatile) {
                        sb2.append(field.get(this));
                    } else if (!field.getName().startsWith("monet") && !field.getName().startsWith("defaultSub") && !field.getName().startsWith("first_time") && !field.getName().startsWith("reddit_video_") && !field.getName().startsWith("directory_media") && !field.getName().startsWith("actions") && !field.getName().startsWith("comment_action_") && !field.getName().startsWith("previous_version")) {
                        sb2.append(field.get(this));
                    }
                }
            } catch (Exception e) {
                Logger.logException(e);
            }
            return sb2.toString().hashCode();
        }

        public void printDifferences(Settings settings) {
        }
    }

    private SettingsSingleton(Context context) {
        this.settings = buildSettings(context);
    }

    public static SettingsSingleton getInstance() {
        SettingsSingleton settingsSingleton;
        synchronized (LOCK) {
            if (instance == null) {
                instance = new SettingsSingleton(RedditApplication.getAppContext());
            }
            settingsSingleton = instance;
        }
        return settingsSingleton;
    }

    public static String getOauthUrl() {
        return "https://oauth.reddit.com/";
    }

    @Deprecated
    public static String getOauthUrl(Context context) {
        return "https://oauth.reddit.com/";
    }

    public static boolean shouldPreload(int type) {
        int preloadSetting;
        if (DataSavingHelper.isFullDataSaving()) {
            return false;
        }
        if (type == 0) {
            preloadSetting = getInstance().getCurrentSettings().preloadImages;
        } else if (type == 1) {
            preloadSetting = getInstance().getCurrentSettings().preloadPreviews;
        } else if (type == 2) {
            preloadSetting = getInstance().getCurrentSettings().preloadThumbs;
        } else {
            if (type != 3) {
                throw new IllegalArgumentException("Invalid type");
            }
            preloadSetting = getInstance().getCurrentSettings().preloadGifs;
        }
        if (preloadSetting == 0) {
            return true;
        }
        if (preloadSetting == 1) {
            return ConnectivityHelper.isWifiConnected();
        }
        return false;
    }

    @Deprecated
    public static boolean shouldPreload(Context context, int type) {
        return shouldPreload(type);
    }

    private Settings buildSettings(Context context) {
        if (TextUtils.isEmpty(PreferencesHelper.getAccountPrefs("SettingsSingleton: default browser").getString("default_browser", null))) {
            PreferencesHelper.getAccountPrefs("SettingsSingleton: default browser").edit().putString("default_browser", getDefaultBrowserValue()).apply();
        }
        if (!TextUtils.isEmpty(PreferencesHelper.getAccountPrefs("SettingsSingleton: remove recent").getString("recently_viewed_final", null))) {
            PreferencesHelper.getAccountPrefs("SettingsSingleton: remove recent").edit().remove("recently_viewed_final").apply();
        }
        SharedPreferences sharedPreferencesG = PreferencesHelper.getAccountPrefs("SettingsSingleton: main");
        Settings settings = new Settings();
        settings.doh = sharedPreferencesG.getBoolean("doh", false);
        settings.enhancedAutoPlay = sharedPreferencesG.getBoolean("enhanced_autoplay", false);
        settings.fixedHeightVideo = sharedPreferencesG.getBoolean("fixed_height_video", false);
        settings.amoled_divider = sharedPreferencesG.getBoolean("amoled_divider", false);
        settings.stickyHighlight = sharedPreferencesG.getBoolean("sticky_highlight", false);
        settings.commentsReplyFab = sharedPreferencesG.getBoolean("comments_reply_fab", true);
        settings.commentsReplyFabAutoHide = sharedPreferencesG.getBoolean("comments_reply_fab_autohide", true);
        sharedPreferencesG.getBoolean("developer_video", false);
        settings.developerVideo = false;
        settings.incognitoKeyboard = sharedPreferencesG.getBoolean("incognito_keyboard", false);
        settings.incognitoTabs = sharedPreferencesG.getBoolean("incognito_tabs", false);
        settings.tabletMode = sharedPreferencesG.getBoolean("tablet_mode", context.getResources().getBoolean(R.bool.tablet));
        settings.tabletModeFoldable = sharedPreferencesG.getBoolean("tablet_foldable", false);
        settings.tabletDualPaneLandscape = sharedPreferencesG.getBoolean("tablet_dual_pane_landscape", true);
        settings.tabletDualPanePortrait = sharedPreferencesG.getBoolean("tablet_dual_pane_portrait", false);
        settings.tabletTabs = sharedPreferencesG.getBoolean("tablet_tabs", true);
        settings.tabletInsets = sharedPreferencesG.getBoolean("tablet_insets", false);
        settings.monetForceAmoled = sharedPreferencesG.getBoolean("force_amoled", false);
        settings.darkOverlay = sharedPreferencesG.getBoolean("dark_overlay", false);
        settings.imageShareTitle = sharedPreferencesG.getBoolean("image_share_title", true);
        settings.ultra_rickroll = sharedPreferencesG.getBoolean("ultra_rickroll", true);
        settings.ultra_website_previews = sharedPreferencesG.getBoolean("ultra_website_previews", true);
        settings.ultra_default_to_logged_out = sharedPreferencesG.getBoolean("ultra_default_to_logged_out", false);
        settings.commentSwipeActions = sharedPreferencesG.getBoolean("comment_swipe_actions", true);
        settings.commentSwipeActionsVibrate = sharedPreferencesG.getBoolean("comment_swipe_actions_vibrate", false);
        settings.postSwipeActions = sharedPreferencesG.getBoolean("post_swipe_actions", true);
        settings.postSwipeActionsVibrate = sharedPreferencesG.getBoolean("post_swipe_actions_vibrate", false);
        settings.commentsEmotes = sharedPreferencesG.getBoolean("comments_emotes", true);
        settings.commentsAnimatedImages = sharedPreferencesG.getBoolean("comments_animated_images", true);
        settings.commentsAuthorProfilePic = sharedPreferencesG.getBoolean("comments_author_profile_pic", false);
        settings.awardsModern = sharedPreferencesG.getBoolean("awards_modern", true);
        settings.awardsMinimal = sharedPreferencesG.getBoolean("awards_minimal", false);
        settings.awardsNone = sharedPreferencesG.getBoolean("awards_none", false);
        settings.flairModern = sharedPreferencesG.getBoolean("flair_modern", true);
        settings.flairMinimal = sharedPreferencesG.getBoolean("flair_minimal", false);
        settings.flairNone = sharedPreferencesG.getBoolean("flair_none", false);
        settings.commentDividers = sharedPreferencesG.getBoolean("comments_dividers_", false);
        settings.commentsTopLevelPadding = sharedPreferencesG.getBoolean("comments_top_level_padding", true);
        settings.commentMediaThumbnail = sharedPreferencesG.getBoolean("media_preview_thumbnail", false);
        settings.commentMediaCropped = sharedPreferencesG.getBoolean("media_preview_cropped", true);
        settings.commentMediaFull = sharedPreferencesG.getBoolean("media_preview_full", false);
        settings.snap = sharedPreferencesG.getBoolean("snap", true);
        settings.secretPanel = sharedPreferencesG.getBoolean("secret_panel", false);
        settings.recordHistory = sharedPreferencesG.getBoolean("record_history", true);
        settings.recordTopSubreddits = sharedPreferencesG.getBoolean("record_top_subreddits", true);
        settings.transparentStatus = sharedPreferencesG.getBoolean("transparent_status", false);
        settings.subredditIcons = sharedPreferencesG.getBoolean("subreddit_icons", true);
        settings.actionsContextualChips = sharedPreferencesG.getBoolean("actions_contextual_chips", false);
        settings.actionsFullyExpand = sharedPreferencesG.getBoolean("actions_fully_expand", false);
        settings.actionsHideRead = sharedPreferencesG.getBoolean("actions_hide_read", true);
        settings.actionsSaved = sharedPreferencesG.getBoolean("actions_saved", false);
        settings.actionsProfile = sharedPreferencesG.getBoolean("actions_profile", true);
        settings.actionsSubmit = sharedPreferencesG.getBoolean("actions_submit", true);
        settings.actionsView = sharedPreferencesG.getBoolean("actions_view", true);
        settings.actionsRandom = sharedPreferencesG.getBoolean("actions_random", true);
        settings.actionsRandomNsfw = sharedPreferencesG.getBoolean("actions_random_nsfw", false);
        settings.actionsDarkMode = sharedPreferencesG.getBoolean("actions_dark", true);
        settings.actionsFriends = sharedPreferencesG.getBoolean("actions_friends", false);
        settings.actionsSettings = sharedPreferencesG.getBoolean("actions_settings", true);
        settings.actionsDataSaving = sharedPreferencesG.getBoolean("actions_data_saving", false);
        settings.actionsFilters = sharedPreferencesG.getBoolean("actions_filters", false);
        settings.actionsRecentlyViewed = sharedPreferencesG.getBoolean("actions_recently_viewed", false);
        settings.actionsToggleAccount = sharedPreferencesG.getBoolean("actions_toggle_account", false);
        settings.actionsSort = sharedPreferencesG.getBoolean("actions_sort", true);
        settings.actionsRefresh = sharedPreferencesG.getBoolean("actions_refresh", true);
        settings.actionsAbout = sharedPreferencesG.getBoolean("actions_about", true);
        settings.actionsSync = sharedPreferencesG.getBoolean("actions_sync", false);
        settings.actionsSearch = sharedPreferencesG.getBoolean("actions_search", true);
        settings.actionsWatched = sharedPreferencesG.getBoolean("actions_watched", false);
        settings.actionsSwipe = sharedPreferencesG.getBoolean("actions_swipe", true);
        settings.actionsMessaging = sharedPreferencesG.getBoolean("actions_messaging", false);
        settings.actionsSubreddits = sharedPreferencesG.getBoolean("actions_subreddits", false);
        settings.actionsDarkOverlay = sharedPreferencesG.getBoolean("actions_dark_overlay", false);
        settings.actionsScrollToTop = sharedPreferencesG.getBoolean("actions_scroll_top", false);
        settings.comment_action_reply = sharedPreferencesG.getBoolean("comment_action_reply", true);
        settings.comment_action_refresh = sharedPreferencesG.getBoolean("comment_action_refresh", true);
        settings.comment_action_other = sharedPreferencesG.getBoolean("comment_action_other", true);
        settings.comment_action_about = sharedPreferencesG.getBoolean("comment_action_about", true);
        settings.comment_action_images = sharedPreferencesG.getBoolean("comment_action_images", true);
        settings.comment_action_collapse_all = sharedPreferencesG.getBoolean("comment_action_collapse_all", true);
        settings.comment_action_sports_mode = sharedPreferencesG.getBoolean("comment_action_sports_mode", true);
        settings.comment_action_settings = sharedPreferencesG.getBoolean("comment_action_settings", true);
        settings.comment_action_translate = sharedPreferencesG.getBoolean("comment_action_translate", true);
        settings.comment_action_sort = sharedPreferencesG.getBoolean("comment_action_sort", true);
        settings.comment_action_recents = sharedPreferencesG.getBoolean("comment_action_recents", true);
        settings.comment_action_search = sharedPreferencesG.getBoolean("comment_action_search", true);
        settings.searchFocusOnOpen = sharedPreferencesG.getBoolean("search_focus_on_open", true);
        settings.highlightDefault = sharedPreferencesG.getBoolean("highlight_default", true);
        settings.duplicateFavs = sharedPreferencesG.getBoolean("duplicate_favs", false);
        settings.fastScroller = sharedPreferencesG.getBoolean("subreddits_fast_scroller", false);
        settings.subredditsFullyExpand = sharedPreferencesG.getBoolean("subreddits_fully_expand", false);
        settings.subredditsFocusOnOpen = sharedPreferencesG.getBoolean("subreddits_focus_on_open", false);
        settings.subredditsRememberPosition = sharedPreferencesG.getBoolean("subreddits_remember_position", false);
        settings.subredditsShowFeeds = sharedPreferencesG.getBoolean("subreddits_show_feeds", true);
        settings.subredditsShowFav = sharedPreferencesG.getBoolean("subreddits_show_fav", true);
        settings.subredditsShowCustom = sharedPreferencesG.getBoolean("subreddits_show_custom", true);
        settings.subredditsShowSubscriptions = sharedPreferencesG.getBoolean("subreddits_show_subscriptions", true);
        settings.subredditsShowTop = sharedPreferencesG.getBoolean("subreddits_show_top", true);
        settings.subredditsShowModerator = sharedPreferencesG.getBoolean("subreddits_show_moderator", true);
        settings.subredditsShowFollows = sharedPreferencesG.getBoolean("subreddits_show_follows", true);
        settings.collapseFeeds = sharedPreferencesG.getBoolean("collapse_feeds", false);
        settings.collapseFav = sharedPreferencesG.getBoolean("collapse_fav", false);
        settings.collapseCustomFeed = sharedPreferencesG.getBoolean("collapse_custom_feed", false);
        settings.collapseSubscriptions = sharedPreferencesG.getBoolean("collapse_subscriptions", false);
        settings.collapseTop = sharedPreferencesG.getBoolean("collapse_top", false);
        settings.collapseModerated = sharedPreferencesG.getBoolean("collapse_moderated", false);
        settings.collapseFollows = sharedPreferencesG.getBoolean("collapse_follows", false);
        settings.collapseSearchRecent = sharedPreferencesG.getBoolean("collapse_search_recent", false);
        settings.collapseSearchSubscribed = sharedPreferencesG.getBoolean("collapse_search_subscribed", false);
        settings.collapseSearchResults = sharedPreferencesG.getBoolean("collapse_search_results", false);
        settings.collapseSearchAction = sharedPreferencesG.getBoolean("collapse_search_actions", false);
        settings.drawerSubIcons = sharedPreferencesG.getBoolean("drawer_sub_icons", true);
        settings.postsCommentsNewActivity = sharedPreferencesG.getBoolean("posts_comments_new_activity", false);
        settings.tapUsernamesOpen = sharedPreferencesG.getBoolean("inline_usernames", false);
        settings.tapSubredditsOpen = sharedPreferencesG.getBoolean("inline_subreddits", false);
        settings.tapDomainsOpen = sharedPreferencesG.getBoolean("inline_domains", false);
        settings.first_time_full_height = sharedPreferencesG.getBoolean("first_time_full_height", true);
        settings.first_time_paint = sharedPreferencesG.getBoolean("first_time_paint", true);
        settings.first_time_tag = sharedPreferencesG.getBoolean("first_time_tag", true);
        settings.first_time_legacy_settings = sharedPreferencesG.getBoolean("first_time_legacy_settings", true);
        settings.first_time_legacy_comment_settings = sharedPreferencesG.getBoolean("first_time_legacy_comment_settings", true);
        settings.first_time_monet_intro_settings = sharedPreferencesG.getBoolean("first_time_monet_intro_settings", true);
        settings.hideReddit = sharedPreferencesG.getBoolean("posts_hide_reddit", true);
        settings.postsDisplayNew = sharedPreferencesG.getBoolean("posts_display_new", false);
        settings.postsLongPress = sharedPreferencesG.getBoolean("post_long_press", false);
        settings.viewedPeek = sharedPreferencesG.getBoolean("viewed_peek", false);
        settings.viewerShowSave = sharedPreferencesG.getBoolean("viewer_show_save", false);
        settings.dimImages = sharedPreferencesG.getBoolean("dim_images", false);
        settings.reverseDrawers = sharedPreferencesG.getBoolean("reverse_drawers", false);
        settings.postScoresShort = sharedPreferencesG.getBoolean("post_scores_short", true);
        settings.biometric = sharedPreferencesG.getBoolean("biometric", false);
        settings.biometric_start = sharedPreferencesG.getBoolean("biometric_start", false);
        settings.hide_username = sharedPreferencesG.getBoolean("hide_username", false);
        settings.highlightOp = sharedPreferencesG.getBoolean("highlight_op", true);
        settings.highlightYourself = sharedPreferencesG.getBoolean("highlight_yourself", true);
        settings.highlightFriends = sharedPreferencesG.getBoolean("highlight_friends", true);
        settings.highlightNewAccounts = sharedPreferencesG.getBoolean("highlight_new_accounts", false);
        settings.pushNotifications = sharedPreferencesG.getBoolean("push_notifications", false);
        settings.delimitCommentScores = sharedPreferencesG.getBoolean("delimit_preference", false);
        settings.analyticsGoogle = sharedPreferencesG.getBoolean("tracking_google_analytics", true);
        settings.analyticsFabric = sharedPreferencesG.getBoolean("tracking_fabric", true);
        settings.analyticsFlurry = sharedPreferencesG.getBoolean("tracking_flurry", true);
        settings.card_buttons_save = sharedPreferencesG.getBoolean("card_buttons_save", true);
        settings.card_buttons_hide = sharedPreferencesG.getBoolean("card_buttons_hide", false);
        settings.card_buttons_share = sharedPreferencesG.getBoolean("card_buttons_share", false);
        settings.card_buttons_mod = sharedPreferencesG.getBoolean("card_buttons_mod", true);
        settings.slide_button_vote_right = sharedPreferencesG.getBoolean("slide_button_vote_right", false);
        settings.slide_buttons_save = sharedPreferencesG.getBoolean("slide_buttons_save", true);
        settings.slide_buttons_hide = sharedPreferencesG.getBoolean("slide_buttons_hide", false);
        settings.slide_buttons_share = sharedPreferencesG.getBoolean("slide_buttons_share", false);
        settings.slide_buttons_mod = sharedPreferencesG.getBoolean("slide_buttons_mod", true);
        settings.slide_hide_all_buttons = sharedPreferencesG.getBoolean("slide_hide_all_buttons", false);
        settings.small_card_buttons_upvote = sharedPreferencesG.getBoolean("small_card_buttons_upvote", true);
        settings.small_card_buttons_downvote = sharedPreferencesG.getBoolean("small_card_buttons_downvote", true);
        settings.small_card_buttons_save = sharedPreferencesG.getBoolean("small_card_buttons_save", true);
        settings.small_card_buttons_hide = sharedPreferencesG.getBoolean("small_card_buttons_hide", false);
        settings.small_card_buttons_share = sharedPreferencesG.getBoolean("small_card_buttons_share", false);
        settings.small_card_buttons_mod = sharedPreferencesG.getBoolean("small_card_buttons_mod", true);
        settings.slidesThickDivider = sharedPreferencesG.getBoolean("slide_thick_divider", true);
        settings.commentsHideAutomod = sharedPreferencesG.getBoolean("comments_hide_automod", false);
        settings.peek = sharedPreferencesG.getBoolean("peek", true);
        settings.peekOptions = sharedPreferencesG.getBoolean("peek_options", true);
        settings.peekMute = sharedPreferencesG.getBoolean("peek_mute", false);
        settings.zoomTall = sharedPreferencesG.getBoolean("zoom_tall", true);
        settings.forceAutoRotate = sharedPreferencesG.getBoolean("view_auto_rotate", false);
        settings.imageColorfulBackground = sharedPreferencesG.getBoolean("image_colorful_background", true);
        settings.markAsRead = sharedPreferencesG.getBoolean("history_mark_read", true);
        settings.markAsReadSync = sharedPreferencesG.getBoolean("history_mark_read_reddit", true);
        settings.markAsReadOnScroll = sharedPreferencesG.getBoolean("history_mark_read_on_scroll", false);
        settings.filtersContentLink = sharedPreferencesG.getBoolean("new_filters_content_link", true);
        settings.filtersContentImage = sharedPreferencesG.getBoolean("new_filters_content_image", true);
        settings.filtersContentGif = sharedPreferencesG.getBoolean("new_filters_content_gif", true);
        settings.filtersContentAlbum = sharedPreferencesG.getBoolean("new_filters_content_album", true);
        settings.filtersContentVideo = sharedPreferencesG.getBoolean("new_filters_content_video", true);
        settings.filtersContentText = sharedPreferencesG.getBoolean("new_filters_content_text", true);
        settings.filtersContentNsfw = sharedPreferencesG.getBoolean("new_filters_content_nsfw", true);
        settings.formattingButtonUser = sharedPreferencesG.getBoolean("formatting_button_user", true);
        settings.formattingButtonPreview = sharedPreferencesG.getBoolean("formatting_button_preview", true);
        settings.formattingButtonEmoji = sharedPreferencesG.getBoolean("formatting_button_emoji", true);
        settings.formattingButtonDraft = sharedPreferencesG.getBoolean("formatting_button_draft", true);
        settings.formattingButtonImage = sharedPreferencesG.getBoolean("formatting_button_image", true);
        settings.formattingButtonLink = sharedPreferencesG.getBoolean("formatting_button_link", true);
        settings.formattingButtonSpoiler = sharedPreferencesG.getBoolean("formatting_button_spoiler", true);
        settings.formattingButtonBold = sharedPreferencesG.getBoolean("formatting_button_bold", true);
        settings.formattingButtonItalic = sharedPreferencesG.getBoolean("formatting_button_italic", true);
        settings.formattingButtonStrikethrough = sharedPreferencesG.getBoolean("formatting_button_strikethrough", true);
        settings.formattingButtonSuperscript = sharedPreferencesG.getBoolean("formatting_button_superscript", true);
        settings.formattingButtonSpeech = sharedPreferencesG.getBoolean("formatting_button_speech", true);
        settings.formattingButtonBullet = sharedPreferencesG.getBoolean("formatting_button_bullet", true);
        settings.formattingButtonNumbered = sharedPreferencesG.getBoolean("formatting_button_numbered", true);
        settings.formattingButtonPromo = sharedPreferencesG.getBoolean("formatting_button_promo", true);
        settings.formattingButtonInfo = sharedPreferencesG.getBoolean("formatting_button_info", true);
        settings.formattingButtonBugReport = sharedPreferencesG.getBoolean("formatting_button_bug_report", true);
        settings.commentsAlwaysShow = sharedPreferencesG.getBoolean("comments_always_show_bar", false);
        settings.commentsRememberPos = sharedPreferencesG.getBoolean("comments_remember_pos", true);
        settings.commentsReverseActions = sharedPreferencesG.getBoolean("comments_reverse_actions", false);
        settings.cardImageAbove = sharedPreferencesG.getBoolean("card_image_above", true);
        settings.cardImageFullHeight = sharedPreferencesG.getBoolean("card_image_full_height", false);
        settings.cardSelftextPreviews = sharedPreferencesG.getBoolean("card_selftext_previews", true);
        settings.cardTextLabels = sharedPreferencesG.getBoolean("card_text_labels", false);
        settings.cardFullWidth = sharedPreferencesG.getBoolean("card_full_width", false);
        settings.cardSelftextExpand = sharedPreferencesG.getBoolean("card_selftext_expand", true);
        settings.slideImageTop = sharedPreferencesG.getBoolean("slide_image_top", false);
        settings.slideImageFullHeight = sharedPreferencesG.getBoolean("slide_image_full_height", true);
        settings.slideFullWidth = sharedPreferencesG.getBoolean("slide_full_width", false);
        settings.slideSelftextPreviews = sharedPreferencesG.getBoolean("slide_selftext_previews", true);
        settings.slideImagePreviews = sharedPreferencesG.getBoolean("slide_image_previews", true);
        settings.slideSubredditThumbnail = sharedPreferencesG.getBoolean("slide_subreddit_thumbnail", true);
        settings.slideTwitterPreviews = sharedPreferencesG.getBoolean("slide_twitter_previews", true);
        settings.postsQuickScroll = sharedPreferencesG.getBoolean("posts_quick_scroll", true);
        settings.redditVideoQualityCheck = sharedPreferencesG.getBoolean("reddit_video_quality_check", false);
        settings.postsHome = sharedPreferencesG.getBoolean("posts_home", true);
        settings.postOc = sharedPreferencesG.getBoolean("post_oc", true);
        settings.postDividers = sharedPreferencesG.getBoolean("post_dividers", false);
        settings.images_immersive = sharedPreferencesG.getBoolean("images_immersive", true);
        settings.cacheStreamingVideos = sharedPreferencesG.getBoolean("cache_streaming_videos", true);
        settings.commentsScrollbar = sharedPreferencesG.getBoolean("comments_scrollbar", false);
        settings.postScrollbar = sharedPreferencesG.getBoolean("posts_scrollbar", false);
        settings.slideSelftextBorder = sharedPreferencesG.getBoolean("slide_selftext_border", true);
        settings.postsShowAuthor = sharedPreferencesG.getBoolean("posts_show_author", true);
        settings.commentsControversial = sharedPreferencesG.getBoolean("comments_controversial", true);
        settings.subFolders = sharedPreferencesG.getBoolean("save_folders_", false);
        settings.saveNomedia = sharedPreferencesG.getBoolean("save_nomedia", false);
        settings.zoomVideo = sharedPreferencesG.getBoolean("zoom_videos", true);
        settings.limitImage = sharedPreferencesG.getBoolean("limit_image_size", true);
        settings.iconShortcuts = sharedPreferencesG.getBoolean("icon_shotcuts", true);
        settings.changelogs = sharedPreferencesG.getBoolean("changelog_preference", true);
        settings.updateReminder = sharedPreferencesG.getBoolean("update_reminder", true);
        settings.gifSeek = sharedPreferencesG.getBoolean("gif_seek", true);
        settings.gifSpeed = sharedPreferencesG.getBoolean("gif_speed", true);
        settings.videoMute = sharedPreferencesG.getBoolean("video_mute", false);
        settings.commentsCollapseAllDefault = sharedPreferencesG.getBoolean("comments_collapse_all_default", false);
        settings.commentsFlair = sharedPreferencesG.getBoolean("comment_flair_preference", true);
        settings.commentsHighlightNew = sharedPreferencesG.getBoolean("comments_highlight_new", true);
        settings.commentsHighlightNewBackground = sharedPreferencesG.getBoolean("comments_highlight_new_background", false);
        settings.animateTransitionImage = sharedPreferencesG.getBoolean("animate_transition_image", true);
        settings.animateTransitionComments = sharedPreferencesG.getBoolean("animate_comments_transition", true);
        settings.animateEnter = sharedPreferencesG.getBoolean("animate_enter_exit", true);
        settings.animatePostHide = sharedPreferencesG.getBoolean("animate_hide_posts", true);
        settings.animateCommentCollapse = sharedPreferencesG.getBoolean("animate_comment_collapse", true);
        settings.animateCommentMore = sharedPreferencesG.getBoolean("animate_comment_more", true);
        settings.animateCommentActions = sharedPreferencesG.getBoolean("animate_comment_action", true);
        settings.animateCommentInsert = sharedPreferencesG.getBoolean("animate_comment_insert", true);
        settings.animateCommentsNavigation = sharedPreferencesG.getBoolean("animate_comment_nav", true);
        settings.volumeKeyNavigation = sharedPreferencesG.getBoolean("comments_volume_nav", false);
        settings.dualLandscape = sharedPreferencesG.getBoolean("dual_landscape", true);
        settings.dualPortait = sharedPreferencesG.getBoolean("dual_portrait", false);
        settings.selftextLinksExpanded = sharedPreferencesG.getBoolean("posts_links_expanded", false);
        settings.commentsLinksExpanded = sharedPreferencesG.getBoolean("comments_links_expanded", false);
        settings.colorfulCommentQuotes = sharedPreferencesG.getBoolean("comments_colored_quotes", false);
        settings.colorfulComments = sharedPreferencesG.getBoolean("comments_colored_preference", false);
        settings.commentsDoubleSpacing = sharedPreferencesG.getBoolean("comments_row_double", false);
        settings.swipeHidePosts = sharedPreferencesG.getBoolean("swipe_hide_posts_new", false);
        settings.swipeDim = sharedPreferencesG.getBoolean("swipe_dim", true);
        settings.cctPreload = sharedPreferencesG.getBoolean("cct_preload_", false);
        settings.commentNavBar = sharedPreferencesG.getBoolean("md2_comment_nav_bar", false);
        settings.commentNavBarHide = sharedPreferencesG.getBoolean("md2_comment_nav_bar_hide", true);
        settings.cachedComments = sharedPreferencesG.getBoolean("cached_comments", false);
        settings.inlineLinkSources = sharedPreferencesG.getBoolean("inline_link_sources", true);
        settings.largeLinkSources = sharedPreferencesG.getBoolean("large_link_sources", true);
        settings.collapseParent = sharedPreferencesG.getBoolean("collapse_parent", false);
        settings.typeIndicators = sharedPreferencesG.getBoolean("type_indicators", true);
        settings.compactThumbnails = sharedPreferencesG.getBoolean("compact_thumbnails", true);
        settings.compactSingleLine = sharedPreferencesG.getBoolean("compact_single_lines", false);
        settings.smallCardsAlignThumbnailsLeft = sharedPreferencesG.getBoolean("small_cards_thumbnail_align_left", false);
        settings.alignThumbnailsRight = sharedPreferencesG.getBoolean("thumbnail_align", false);
        settings.inlineImagePreviews = sharedPreferencesG.getBoolean("inline_image_previews", true);
        settings.quickDismiss = sharedPreferencesG.getBoolean("quick_dismiss_preference", true);
        settings.swipeDismiss = sharedPreferencesG.getBoolean("swipe_dismiss_preference", true);
        settings.streamable = sharedPreferencesG.getBoolean("streamable_preference", true);
        settings.mixtape = sharedPreferencesG.getBoolean("mixtape_preference", false);
        settings.vidme = sharedPreferencesG.getBoolean("vidme_preference", true);
        settings.gfycat = sharedPreferencesG.getBoolean("gfycat_preference", true);
        settings.redgifs = sharedPreferencesG.getBoolean("redgifs_preference", true);
        settings.redditVideo = sharedPreferencesG.getBoolean("reddit_video_preference", true);
        settings.imageDebug = sharedPreferencesG.getBoolean("debug_image", true);
        settings.swipeSubs = sharedPreferencesG.getBoolean("swipe_subs_preference", false);
        settings.commentsSingle = sharedPreferencesG.getBoolean("comments_single", false);
        settings.coloredNavBar = sharedPreferencesG.getBoolean("colored_navigation_preference", false);
        settings.blackNavBar = sharedPreferencesG.getBoolean("black_navigation_preference", false);
        settings.whiteNavBar = sharedPreferencesG.getBoolean("white_navigation_preference", RedditApplication.getAppContext().getResources().getBoolean(R.bool.white_default));
        settings.coloredNavBarNight = sharedPreferencesG.getBoolean("colored_navigation_preference_night", false);
        settings.blackNavBarNight = sharedPreferencesG.getBoolean("black_navigation_preference_night", true);
        settings.whiteNavBarNight = sharedPreferencesG.getBoolean("white_navigation_preference_night", false);
        settings.navigationBottom = sharedPreferencesG.getBoolean("navigation_bottom", true);
        settings.navigationBottomHide = sharedPreferencesG.getBoolean("navigation_bottom_hide", true);
        settings.navigationBottomLabels = sharedPreferencesG.getBoolean("navigation_bottom_labels_new", true);
        settings.navigationBottomActions = sharedPreferencesG.getBoolean("navigation_bottom_actions", false);
        settings.drawerUnread = sharedPreferencesG.getBoolean("message_drawer", true);
        settings.useDrawer = sharedPreferencesG.getBoolean("use_drawer", false);
        settings.usePanel = sharedPreferencesG.getBoolean("use_panel", false);
        settings.commentsSwipe = sharedPreferencesG.getBoolean("swipe_hide", true);
        settings.exitConfirmation = sharedPreferencesG.getBoolean("exit_preference", true);
        settings.drawerBack = sharedPreferencesG.getBoolean("drawer_back_preference", false);
        settings.embedYouTube = sharedPreferencesG.getBoolean("youtubeembed_preference", true);
        settings.tumblrPosts = sharedPreferencesG.getBoolean("tumblr_post_preference", true);
        settings.imgurGallery = sharedPreferencesG.getBoolean("album_preference", true);
        settings.extendedSort = sharedPreferencesG.getBoolean("extended_sort_preference", false);
        settings.imageHandling = sharedPreferencesG.getBoolean("appimages_preference", true);
        settings.nsfw = sharedPreferencesG.getBoolean("nsfw_preference", true);
        settings.nsfwPreviews = sharedPreferencesG.getBoolean("nsfw_previews_preference", false);
        settings.selectiveNsfwPreviews = sharedPreferencesG.getBoolean("selective_nsfw_previews_preference", true);
        settings.nsfwRecents = sharedPreferencesG.getBoolean("nsfw_recents", false);
        settings.nsfwMute = sharedPreferencesG.getBoolean("nsfw_mute", false);
        settings.bypass_amp = sharedPreferencesG.getBoolean("bypass_amp", true);
        settings.commentsAlternative = sharedPreferencesG.getBoolean("comments_alternative", true);
        settings.autohideToolbar = sharedPreferencesG.getBoolean("toolbar_autohide_preference", true);
        settings.toolbarExpanded = sharedPreferencesG.getBoolean("toolbar_expanded", true);
        settings.mainFab = sharedPreferencesG.getBoolean("main_fab_display", true);
        settings.mainFabAutohide = sharedPreferencesG.getBoolean("main_fab_hide", true);
        loadTypefaces(settings, sharedPreferencesG);
        settings.navigationRailGravity = Integer.parseInt(sharedPreferencesG.getString("rail_gravity", "0"));
        settings.commentsDefaultNavigation = Integer.parseInt(sharedPreferencesG.getString("comments_default_navigation_new", "7"));
        settings.preloadImages = Integer.parseInt(sharedPreferencesG.getString("images_preload", "0"));
        settings.preloadPreviews = Integer.parseInt(sharedPreferencesG.getString("preview_preload", "0"));
        settings.preloadThumbs = Integer.parseInt(sharedPreferencesG.getString("thumbs_preload", "0"));
        settings.preloadGifs = Integer.parseInt(sharedPreferencesG.getString("gifs_preload", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.redditVideoQuality = Integer.parseInt(sharedPreferencesG.getString("reddit_video_quality_new", "2"));
        settings.defaultBrowser = Integer.parseInt(sharedPreferencesG.getString("default_browser", "0"));
        settings.commentsCount = Integer.parseInt(sharedPreferencesG.getString("comments_load", "75"));
        settings.commentsParentButton = Integer.parseInt(sharedPreferencesG.getString("comments_parent_button", "2"));
        settings.fontSize = Integer.parseInt(sharedPreferencesG.getString("fontsize_preference", settings.tabletMode ? "2" : IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.commentAutoRefreshValue = Integer.parseInt(sharedPreferencesG.getString("live_comments_refresh", "15"));
        settings.commentPadding = Integer.parseInt(sharedPreferencesG.getString("comment_padding", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.postColumnsPortrait = Integer.parseInt(sharedPreferencesG.getString("post_columns_portrait", context.getResources().getString(R.string.post_columns_portrait)));
        settings.postColumnsLandscape = Integer.parseInt(sharedPreferencesG.getString("post_columns_landscape", context.getResources().getString(R.string.post_columns_landscape)));
        settings.dataSavingMode = Integer.parseInt(sharedPreferencesG.getString("data_saving", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.imageCacheDuration = Integer.parseInt(sharedPreferencesG.getString("cache_preference", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.mainFabAction = Integer.parseInt(sharedPreferencesG.getString("main_fab_action", "2"));
        settings.slideThumbnailSize = Integer.parseInt(sharedPreferencesG.getString("slide_thumbnail_size", "0"));
        settings.autoNightStartHour = sharedPreferencesG.getInt("auto_night_start_hour", 23);
        settings.autoNightStartMin = sharedPreferencesG.getInt("auto_night_start_min", 0);
        settings.autoNightEndHour = sharedPreferencesG.getInt("auto_night_end_hour", 6);
        settings.autoNightEndMin = sharedPreferencesG.getInt("auto_night_end_min", 0);
        settings.deepzoomDpi = Integer.parseInt(sharedPreferencesG.getString("deepzoom_dpi", "200"));
        settings.deepzoomMax = Integer.parseInt(sharedPreferencesG.getString("deepzoom_max", "30"));
        settings.commentSort = context.getResources().getStringArray(R.array.comment_sort)[Integer.parseInt(sharedPreferencesG.getString("comment_sort_preference", "6"))];
        settings.postSort = PostsHelper.parseSortPair(sharedPreferencesG.getString("post_sort_preference", "sort_0"));
        settings.autoplay = Integer.parseInt(sharedPreferencesG.getString("autoplay", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.swipeSensitivity = Float.parseFloat(sharedPreferencesG.getString("swipe_sens", "1.00"));
        settings.defaultSub = sharedPreferencesG.getString("default_sub", "Frontpage");
        settings.userAgent = sharedPreferencesG.getString("user-agent", null);
        settings.commentNavigationPosition = Integer.parseInt(sharedPreferencesG.getString("comment_navigation_position", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.commentNavigationFirst = Integer.parseInt(sharedPreferencesG.getString("comment_navigation_first", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.commentNavigationSecond = Integer.parseInt(sharedPreferencesG.getString("comment_navigation_second", "3"));
        settings.commentNavigationThird = Integer.parseInt(sharedPreferencesG.getString("comment_navigation_third", "0"));
        settings.postNavigationFirst = Integer.parseInt(sharedPreferencesG.getString("post_navigation_first", "0"));
        settings.postNavigationSecond = Integer.parseInt(sharedPreferencesG.getString("post_navigation_second", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.postNavigationThird = Integer.parseInt(sharedPreferencesG.getString("post_navigation_third", "5"));
        settings.monet_system = sharedPreferencesG.getBoolean("monet_system", Build.VERSION.SDK_INT >= 31);
        settings.monet_custom_palette = false;
        settings.monet_color_intensity = Float.parseFloat(sharedPreferencesG.getString("monet_color_intensity", "1.0"));
        settings.monet_boost_light_color = sharedPreferencesG.getBoolean("monet_boost_light_color", false);
        settings.monet_boost_dark_color = sharedPreferencesG.getBoolean("monet_boost_dark_color", false);
        settings.monet_manual_theme_color = Color.parseColor(sharedPreferencesG.getString("monet_manual_theme_color", "#2a68c1"));
        settings.monet_override_dark_link_color = sharedPreferencesG.contains("monet_override_dark_link_color") ? Color.parseColor(sharedPreferencesG.getString("monet_override_dark_link_color", "")) : 0;
        settings.monet_override_dark_primary_text_color = sharedPreferencesG.contains("monet_override_dark_primary_text_color") ? Color.parseColor(sharedPreferencesG.getString("monet_override_dark_primary_text_color", "")) : 0;
        settings.monet_override_dark_secondary_text_color = sharedPreferencesG.contains("monet_override_dark_secondary_text_color") ? Color.parseColor(sharedPreferencesG.getString("monet_override_dark_secondary_text_color", "")) : 0;
        settings.monet_override_light_link_color = sharedPreferencesG.contains("monet_override_light_link_color") ? Color.parseColor(sharedPreferencesG.getString("monet_override_light_link_color", "")) : 0;
        settings.monet_override_light_primary_text_color = sharedPreferencesG.contains("monet_override_light_primary_text_color") ? Color.parseColor(sharedPreferencesG.getString("monet_override_light_primary_text_color", "")) : 0;
        settings.monet_override_light_secondary_text_color = sharedPreferencesG.contains("monet_override_light_secondary_text_color") ? Color.parseColor(sharedPreferencesG.getString("monet_override_light_secondary_text_color", "")) : 0;
        return settings;
    }

    public static void loadTypefaces(Settings settings, SharedPreferences sharedPreferences) {
        settings.slide_font_title = Integer.parseInt(sharedPreferences.getString("slide_font_title", "11"));
        settings.slide_size_title = Integer.parseInt(sharedPreferences.getString("slide_size_title", "3"));
        settings.slide_font_description = Integer.parseInt(sharedPreferences.getString("slide_font_description", "9"));
        settings.slide_size_description = Integer.parseInt(sharedPreferences.getString("slide_size_description", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.slide_font_selftext = Integer.parseInt(sharedPreferences.getString("slide_font_selftext", "9"));
        settings.slide_size_selftext = Integer.parseInt(sharedPreferences.getString("slide_size_selftext", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.card_font_title = Integer.parseInt(sharedPreferences.getString("card_font_title", "11"));
        settings.card_size_title = Integer.parseInt(sharedPreferences.getString("card_size_title", "2"));
        settings.card_font_description = Integer.parseInt(sharedPreferences.getString("card_font_description", "9"));
        settings.card_size_description = Integer.parseInt(sharedPreferences.getString("card_size_description", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.card_font_selftext = Integer.parseInt(sharedPreferences.getString("card_font_selftext", "9"));
        settings.card_size_selftext = Integer.parseInt(sharedPreferences.getString("card_size_selftext", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.small_card_font_title = Integer.parseInt(sharedPreferences.getString("small_card_font_title", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.small_card_size_title = Integer.parseInt(sharedPreferences.getString("small_card_size_title", "3"));
        settings.small_card_font_description = Integer.parseInt(sharedPreferences.getString("small_card_font_description", "9"));
        settings.small_card_size_description = Integer.parseInt(sharedPreferences.getString("small_card_size_description", "5"));
        settings.smaller_card_font_title = Integer.parseInt(sharedPreferences.getString("smaller_card_font_title", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.smaller_card_size_title = Integer.parseInt(sharedPreferences.getString("smaller_card_size_title", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.smaller_card_font_description = Integer.parseInt(sharedPreferences.getString("smaller_card_font_description", "9"));
        settings.smaller_card_size_description = Integer.parseInt(sharedPreferences.getString("smaller_card_size_description", "5"));
        settings.compact_font_title = Integer.parseInt(sharedPreferences.getString("compact_font_title", "9"));
        settings.compact_size_title = Integer.parseInt(sharedPreferences.getString("compact_size_title", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.compact_font_description = Integer.parseInt(sharedPreferences.getString("compact_font_description", "9"));
        settings.compact_size_description = Integer.parseInt(sharedPreferences.getString("compact_size_description", "5"));
        settings.list_font_title = Integer.parseInt(sharedPreferences.getString("list_font_title", "9"));
        settings.list_size_title = Integer.parseInt(sharedPreferences.getString("list_size_title", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.list_font_description = Integer.parseInt(sharedPreferences.getString("list_font_description", "9"));
        settings.list_size_description = Integer.parseInt(sharedPreferences.getString("list_size_description", "5"));
        settings.comments_description_typeface = Integer.parseInt(sharedPreferences.getString("comments_description_typeface", "9"));
        settings.comments_description_size = Integer.parseInt(sharedPreferences.getString("comments_description_size", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.comments_body_typeface = Integer.parseInt(sharedPreferences.getString("comments_body_typeface", "9"));
        settings.comments_body_size = Integer.parseInt(sharedPreferences.getString("comments_body_size", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE));
        settings.comments_body_space = Float.parseFloat(sharedPreferences.getString("comments_body_space", IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE).replaceAll("x", ""));
    }

    public static void reloadTypefaces() {
        loadTypefaces(getSettings(), PreferencesHelper.getAccountPrefs("SettingsSingleton: load typefaces"));
    }

    public static Settings getSettings() {
        return getInstance().settings;
    }

    public void putIntAsString(String str, int value) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        SharedPreferences.Editor editorEdit = PreferencesHelper.getAccountPrefs("SettingsSingleton: update int").edit();
        editorEdit.putString(str, Integer.toString(value));
        editorEdit.apply();
    }

    public void putInt(String str, int value) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        SharedPreferences.Editor editorEdit = PreferencesHelper.getAccountPrefs("SettingsSingleton: update int").edit();
        editorEdit.putInt(str, value);
        editorEdit.apply();
    }

    public void putString(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        SharedPreferences.Editor editorEdit = PreferencesHelper.getAccountPrefs("SettingsSingleton: update string").edit();
        editorEdit.putString(str, str2);
        editorEdit.apply();
    }

    public void enableDataSaving() {
        getSettings().dataSavingMode = 1;
        putIntAsString("data_saving", 1);
        getSettings().redditVideoQuality = 2;
        putIntAsString("reddit_video_quality_new", 2);
    }

    public void enableFullDataSaving() {
        getSettings().dataSavingMode = 4;
        putIntAsString("data_saving", 4);
        getSettings().redditVideoQuality = 2;
        putIntAsString("reddit_video_quality_new", 2);
    }

    public String getDefaultBrowserValue() {
        return ChromeCustomTabsHelper.isAvailable() ? "0" : IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE;
    }

    public int getIntField(String str) {
        try {
            Field field = getSettings().getClass().getField(str);
            if (field.getType().toString().equals("int")) {
                return field.getInt(getSettings());
            }
            throw new RuntimeException("Not found: " + str);
        } catch (Exception unused) {
            throw new RuntimeException("Error grabbing key: " + str);
        }
    }

    public Object getFieldValue(String str) {
        try {
            return getSettings().getClass().getField(str).get(getSettings());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Class getFieldType(String str) {
        try {
            Class<?> type = getSettings().getClass().getField(str).getType();
            if (type.toString().equals("int")) {
                return Integer.TYPE;
            }
            if (type.toString().equals("float")) {
                return Float.TYPE;
            }
            if (type.toString().equals("String")) {
                return String.class;
            }
            if (!type.toString().equals("boolean") && !type.toString().equals("Boolean")) {
                throw new RuntimeException("Unsupported class: " + type);
            }
            return Boolean.TYPE;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Settings getCurrentSettings() {
        return this.settings;
    }

    public boolean hasSettingsChanged(int hash, boolean excludeVolatile) {
        Settings settingsP = buildSettings(RedditApplication.getAppContext());
        return (settingsP == null || this.settings == null || settingsP.getHash(excludeVolatile) == hash) ? false : true;
    }

    public Settings buildFreshSettings() {
        return buildSettings(RedditApplication.getAppContext());
    }

    public boolean isNsfwEnabled() {
        return getCurrentSettings().nsfw;
    }

    public void reload() {
        Logger.verbose("Reloading settings");
        synchronized (LOCK) {
            instance = new SettingsSingleton(RedditApplication.getAppContext());
        }
    }

    public void removeKey(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        SharedPreferences.Editor editorEdit = PreferencesHelper.getAccountPrefs("SettingsSingleton: remove key").edit();
        editorEdit.remove(str);
        editorEdit.apply();
    }

    public void setBooleanField(String str, boolean z) {
        try {
            Field field = getSettings().getClass().getField(str);
            if (field.getType().toString().equals("boolean")) {
                field.setBoolean(getSettings(), z);
            }
        } catch (Exception unused) {
            throw new RuntimeException("Error grabbing key: " + str);
        }
    }

    public void setIntField(String str, int value) {
        try {
            Field field = getSettings().getClass().getField(str);
            if (field.getType().toString().equals("int")) {
                field.setInt(getSettings(), value);
            }
        } catch (Exception unused) {
            throw new RuntimeException("Error grabbing key: " + str);
        }
    }

    public void setEmbedYouTube(boolean embed) {
        this.settings.embedYouTube = embed;
        putBoolean("youtubeembed_preference", embed);
    }

    public void putBoolean(String str, boolean value) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        SharedPreferences.Editor editorEdit = PreferencesHelper.getAccountPrefs("SettingsSingleton: update boolean").edit();
        editorEdit.putBoolean(str, value);
        editorEdit.apply();
    }

    public void putFloat(String str, float value) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        SharedPreferences.Editor editorEdit = PreferencesHelper.getAccountPrefs("SettingsSingleton: update string").edit();
        editorEdit.putFloat(str, value);
        editorEdit.apply();
    }
}
