package com.resync.app.ui.views.core;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.R;
import com.resync.app.ui.views.images.CustomImageView;
import com.squareup.otto.Subscribe;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.helpers.ui.MonetUtil;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.SubredditColorManager;
import com.resync.app.managers.SubredditIconManager;
import com.resync.app.managers.UserIconManager;
import com.resync.app.events.IconsUpdatedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.widgets.ColorFilterProvider;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.SubredditHelper;
import com.resync.app.helpers.TransitionHelper;
import com.resync.app.util.ColorUtils;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes3.dex */
public class SubView extends CustomImageView {
    String subreddit;

    /* JADX INFO: renamed from: y, reason: collision with root package name */
    String username;

    class IconClickListener implements View.OnClickListener {
        IconClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (StringUtils.isNotEmpty(SubView.this.subreddit)) {
                if (StringUtils.isNotEmpty(SubredditIconManager.getInstance().getIcon(SubView.this.subreddit))) {
                    com.resync.app.util.links.LinkHandler.openImage(SubView.this.getContext(), TransitionHelper.buildSharedElements(ContextHelper.getBaseActivity(SubView.this.getContext()), SubView.this), SubView.this.subreddit, SubredditIconManager.getInstance().getIcon(SubView.this.subreddit), SubredditIconManager.getInstance().getIcon(SubView.this.subreddit), null, null, false, false, false);
                }
            } else if (StringUtils.isNotEmpty(SubView.this.username) && StringUtils.isNotEmpty(UserIconManager.getInstance().getIcon(SubView.this.username))) {
                com.resync.app.util.links.LinkHandler.openImage(SubView.this.getContext(), TransitionHelper.buildSharedElements(ContextHelper.getBaseActivity(SubView.this.getContext()), SubView.this), null, UserIconManager.getInstance().getIcon(SubView.this.username), UserIconManager.getInstance().getIcon(SubView.this.username), null, null, false, false, false);
            }
        }
    }

    class OvalOutlineProvider extends ViewOutlineProvider {
        OvalOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, view.getWidth(), view.getHeight());
        }
    }

    public SubView(Context context) {
        super(context);
    }

    private int getSubredditIconRes() {
        return MaterialPostsHelper.isOpenUrl(this.subreddit) ? R.drawable.outline_link_24 : "frontpage".equalsIgnoreCase(this.subreddit) ? R.drawable.bottom_nav_outline_home_24 : "all".equalsIgnoreCase(this.subreddit) ? R.drawable.outline_bar_chart_24 : "popular".equalsIgnoreCase(this.subreddit) ? R.drawable.outline_trending_up_24 : StringUtils.startsWithIgnoreCase(this.subreddit, "multi_") ? R.drawable.ic_basic_multi : (MaterialPostsHelper.isSearch(this.subreddit) || MaterialPostsHelper.isSubSearch(this.subreddit)) ? R.drawable.ic_search_black_24dp : MaterialPostsHelper.isDomain(this.subreddit) ? R.drawable.outline_explore_24 : MaterialPostsHelper.isModAbout(this.subreddit) ? R.drawable.outline_shield : "random".equalsIgnoreCase(this.subreddit) ? R.drawable.ic_dice : "Friends".equalsIgnoreCase(this.subreddit) ? R.drawable.account_supervisor_outline : R.drawable.ic_basic_subreddit;
    }

    public void loadUserIcon() {
        if (StringUtils.isEmpty(this.username)) {
            return;
        }
        String iconUrl = UserIconManager.getInstance().getIcon(this.username);
        if (StringUtils.isEmpty(iconUrl)) {
            return;
        }
        load(iconUrl, false, false, -1, -1, null, false);
    }

    public void loadSubredditIcon() {
        if (StringUtils.isEmpty(this.subreddit)) {
            return;
        }
        String iconUrl = SubredditIconManager.getInstance().getIcon(this.subreddit);
        if (StringUtils.isEmpty(iconUrl)) {
            return;
        }
        load(iconUrl, false, false, -1, -1, null, false);
    }

    public boolean hasSubredditIcon() {
        if (StringUtils.isEmpty(this.subreddit)) {
            return false;
        }
        return StringUtils.isNotEmpty(SubredditIconManager.getInstance().getIcon(this.subreddit));
    }

    public boolean hasUserIcon() {
        if (StringUtils.isEmpty(this.username)) {
            return false;
        }
        return StringUtils.isNotEmpty(UserIconManager.getInstance().getIcon(this.username));
    }

    public void applyOvalOutline() {
        setOutlineProvider(new OvalOutlineProvider());
        setClipToOutline(true);
    }

    public void setSubreddit(String str) {
        recycle();
        Logger.verbose("XXX SETTING SUBREDDIT: " + str);
        this.subreddit = SubredditHelper.getDisplayTitle(str, false);
        if (hasSubredditIcon()) {
            setImageDrawable(null);
            loadSubredditIcon();
            return;
        }
        setBackgroundColor(SubredditColorManager.getInstance().getColor(this.subreddit).intValue());
        setImageResource(getSubredditIconRes());
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (!ColorUtils.isDark(SubredditColorManager.getInstance().getColor(this.subreddit).intValue())) {
            setColorFilter(ColorFilterProvider.getColorFilter(-16777216));
        } else {
            setColorFilter(ColorFilterProvider.getColorFilter(-1));
        }
    }

    public void setSubredditFromPost(Post post) {
        setSubreddit(post.getSubreddit());
    }

    public void setUsername(String str) {
        recycle();
        this.username = str;
        Logger.verbose("XXX SETTING USERNAME: " + str);
        Logger.verbose("XXX HAS USER IMAGE: " + hasUserIcon());
        if (hasUserIcon()) {
            setImageDrawable(null);
            loadUserIcon();
            return;
        }
        setBackgroundColor(MonetUtil.getProgressColorAuto());
        setImageResource(R.drawable.outline_person_24);
        setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (!ColorUtils.isDark(MonetUtil.getProgressColorAuto())) {
            setColorFilter(ColorFilterProvider.getColorFilter(-16777216));
        } else {
            setColorFilter(ColorFilterProvider.getColorFilter(-1));
        }
    }

    public void enableIconClick() {
        setOnClickListener(new IconClickListener());
    }

    @Override // com.resync.app.ui.views.images.CustomImageView, android.widget.ImageView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            BusSingleton.getBus().register(this);
        } catch (Exception e) {
            Logger.logException(e, false);
        }
    }

    @Override // com.resync.app.ui.views.images.CustomImageView, android.widget.ImageView, android.view.View
    protected void onDetachedFromWindow() {
        try {
            BusSingleton.getBus().unregister(this);
        } catch (Exception e) {
            Logger.logException(e, false);
        }
        super.onDetachedFromWindow();
    }

    @Subscribe
    public void onSubredditIconsUpdated(IconsUpdatedEvent iconsUpdatedEvent) {
        if (StringUtils.isNotEmpty(this.username)) {
            loadUserIcon();
        } else if (StringUtils.isNotEmpty(this.subreddit)) {
            loadSubredditIcon();
        }
    }

    @Override // com.resync.app.ui.views.images.CustomImageView
    protected void onImageReady() {
        if (this.subreddit != null) {
            setBackgroundColor(0);
        } else {
            setBackgroundColor(MonetUtil.getBackgroundColorAuto());
        }
        setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    @Override // com.resync.app.ui.views.images.CustomImageView
    protected void onRecycled() {
        setBackgroundColor(0);
        this.subreddit = null;
        this.username = null;
    }

    public SubView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, SyncStyleable.SUB_VIEW);
        boolean oval = typedArrayObtainStyledAttributes.getBoolean(1, false);
        boolean clickable = typedArrayObtainStyledAttributes.getBoolean(0, false);
        typedArrayObtainStyledAttributes.recycle();
        if (oval) {
            applyOvalOutline();
        }
        if (clickable) {
            setTransitionName("card_preview");
            enableIconClick();
        }
    }
}
