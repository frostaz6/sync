package com.resync.app.sections.posts.toolbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.R;
import com.resync.app.sections.posts.toolbar.chip_groups.ChipWrapper;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.views.core.SubView;
import com.resync.app.ui.views.text.spannable.children.other.LargeToolbarDescription;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.PaddingHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.SubredditHelper;
import com.resync.app.ui.interfaces.DarkModeAware;
import com.resync.app.model.Data;
import com.resync.app.model.Subreddit;

/* JADX INFO: loaded from: classes.dex */
public class PostsLargeSubredditToolbarView extends LinearLayout implements DarkModeAware {

    private RelativeLayout expandedWrapper;
    private View titleWrapper;
    private TextView titleView;

    private LargeToolbarDescription descriptionView;

    public SubView subView;

    public FrameLayout subViewWrapper;
    private ChipWrapper chipWrapper;

    private int section;
    private String subreddit;
    private Subreddit subredditModel;
    boolean initialBind;

    int[] locationBuffer;

    public PostsLargeSubredditToolbarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.initialBind = true;
        this.locationBuffer = new int[2];
        setOrientation(1);
        LinearLayout.inflate(context, R.layout.view_posts_large_subreddit_toolbar, this);
        this.titleWrapper = findViewById(R.id.title_wrapper);
        this.expandedWrapper = (RelativeLayout) findViewById(R.id.expanded_wrapper);
        this.titleView = (TextView) findViewById(R.id.toolbar_title_large);
        this.descriptionView = (LargeToolbarDescription) findViewById(R.id.toolbar_title_description);
        this.subView = (SubView) findViewById(R.id.subview);
        this.subViewWrapper = (FrameLayout) findViewById(R.id.subview_wrapper);
        this.chipWrapper = (ChipWrapper) findViewById(R.id.chip_wrapper);
        if (!SettingsSingleton.getSettings().subredditIcons) {
            this.subViewWrapper.setVisibility(8);
        }
        this.titleView.setOnClickListener(new View.OnClickListener() { // from class: g9.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PostsLargeSubredditToolbarView.this.onTitleClicked(view);
            }
        });
        this.titleView.setOnLongClickListener(new View.OnLongClickListener() { // from class: g9.b
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                return PostsLargeSubredditToolbarView.this.onTitleLongClicked(view);
            }
        });
        applyColors();
    }

    private void hideSubView() {
        if (SettingsSingleton.getSettings().subredditIcons) {
            this.subViewWrapper.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onTitleClicked(View view) {
        PostsSmallSubredditToolbarView.showSectionDialog(getContext(), this.section, this.subreddit);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onTitleLongClicked(View view) {
        PostsSmallSubredditToolbarView.showSectionInputDialog(getContext(), this.section, this.subreddit);
        return true;
    }

    private void showSubredditIcon(String str) {
        if (SettingsSingleton.getSettings().subredditIcons) {
            this.subView.setUsername(MaterialPostsHelper.extractProfileUsername(str));
            this.subViewWrapper.setVisibility(0);
        }
    }

    private void showMultiredditIcon(String str) {
        if (SettingsSingleton.getSettings().subredditIcons) {
            this.subView.setSubreddit(str);
            this.subViewWrapper.setVisibility(0);
        }
    }

    private void applyColors() {
        this.titleView.setTextColor(MonetUtil.getPrimaryTextColorAuto());
        this.descriptionView.setTextColor(MonetUtil.getPrimaryTextColorAuto());
        setSubreddit(this.subredditModel);
    }

    public void bind(int section, String str, String str2, String str3) {
        boolean wasSubViewVisible = this.subViewWrapper.getVisibility() == 0;
        this.section = section;
        this.subreddit = str;
        this.chipWrapper.bindSection(section, str, str2, str3);
        if (section == 2) {
            this.descriptionView.setVisibility(4);
            setTitle("Inbox");
            hideSubView();
        } else if (section == 1) {
            Subreddit subreddit = this.subredditModel;
            if (subreddit != null) {
                this.titleView.setText(subreddit.data.name);
            } else {
                this.titleView.setText(SubredditHelper.getDisplayTitle(str, false));
            }
            showSubredditIcon(str);
        } else if (section == 3) {
            this.titleView.setText(SubredditHelper.getDisplayTitle(str, false));
            hideSubView();
        } else if (section == 0) {
            this.titleView.setText(SubredditHelper.getDisplayTitle(str, false));
            if (MaterialPostsHelper.isAggregateFeed(str)) {
                hideSubView();
            } else if (MaterialPostsHelper.isStandardSubreddit(str)) {
                showMultiredditIcon(str);
            } else if (MaterialPostsHelper.isMultireddit(str) || MaterialPostsHelper.isFriends(str) || MaterialPostsHelper.isSearch(str) || MaterialPostsHelper.isSubSearch(str) || str.equals("list__watching") || MaterialPostsHelper.isDomain(str) || MaterialPostsHelper.isSeen(str)) {
                hideSubView();
            }
        }
        boolean isSubViewVisible = this.subViewWrapper.getVisibility() == 0;
        if (this.initialBind) {
            if (isSubViewVisible) {
                this.titleWrapper.setX(SizeHelper.dpToPxFloat(72));
                PaddingHelper.setPaddingEnd(this.titleView, 72);
            } else {
                PaddingHelper.setPaddingEnd(this.titleView, 0);
            }
            this.initialBind = false;
            return;
        }
        if (!isShown()) {
            if (isSubViewVisible) {
                this.titleWrapper.setX(SizeHelper.dpToPxFloat(72));
                PaddingHelper.setPaddingEnd(this.titleView, 72);
                return;
            } else {
                this.titleWrapper.setX(0.0f);
                PaddingHelper.setPaddingEnd(this.titleView, 0);
                return;
            }
        }
        if (!wasSubViewVisible && isSubViewVisible) {
            this.subViewWrapper.setAlpha(0.0f);
            this.subViewWrapper.setScaleX(0.0f);
            this.subViewWrapper.setScaleY(0.0f);
            this.subViewWrapper.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setStartDelay(300L).setInterpolator(new FastOutSlowInInterpolator());
            this.titleWrapper.animate().x(SizeHelper.dpToPxFloat(72));
            PaddingHelper.setPaddingEnd(this.titleView, 72);
            return;
        }
        if (wasSubViewVisible && !isSubViewVisible) {
            this.titleWrapper.animate().x(0.0f);
            PaddingHelper.setPaddingEnd(this.titleView, 0);
        } else {
            if (wasSubViewVisible || isSubViewVisible) {
                return;
            }
            this.titleWrapper.setX(0.0f);
            PaddingHelper.setPaddingEnd(this.titleView, 0);
        }
    }

    public void setSubreddit(Subreddit subreddit) {
        boolean wasNull = this.subredditModel == null;
        this.subredditModel = subreddit;
        if (subreddit == null) {
            this.descriptionView.clear();
            this.descriptionView.setVisibility(4);
            return;
        }
        if (this.section == 1) {
            this.descriptionView.setVisibility(0);
            this.descriptionView.bindKarma(subreddit.data.totalKarma);
            if (wasNull) {
                this.descriptionView.setAlpha(0.0f);
                this.descriptionView.animate().alpha(1.0f);
            }
        }
        if (this.section == 0) {
            this.descriptionView.setVisibility(0);
            if (MaterialPostsHelper.isStandardSubreddit(this.subreddit)) {
                LargeToolbarDescription largeToolbarDescription = this.descriptionView;
                Data data = subreddit.data;
                largeToolbarDescription.bindSubredditStats(data.subscribers, data.accountsActive);
                if (wasNull) {
                    this.descriptionView.setAlpha(0.0f);
                    this.descriptionView.animate().alpha(1.0f);
                }
            }
        }
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }

    public void setTitle(String str) {
        this.subreddit = null;
        this.titleView.setText(str);
    }

    @Override // android.view.View
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
        if (alpha == 0.0f) {
            setVisibility(4);
        } else {
            setVisibility(0);
        }
    }
}
