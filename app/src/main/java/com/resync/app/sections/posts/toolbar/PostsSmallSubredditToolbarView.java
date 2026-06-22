package com.resync.app.sections.posts.toolbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.fragment_dialogs.bottom.SubredditsBottomSheetFragment;
import com.resync.app.ui.views.core.SubView;
import com.resync.app.helpers.ui.MonetUtil;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.RefreshPostsEvent;
import com.resync.app.events.ScrollToTopOfPostsEvent;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.SubredditHelper;
import com.resync.app.dialogs.ChangeSubredditInputDialogBottomSheet;
import com.resync.app.dialogs.MessagingSectionSelectionDialogBottomSheet;
import com.resync.app.dialogs.ModSectionSelectionDialogBottomSheet;
import com.resync.app.dialogs.ProfileSectionSelectionDialogBottomSheet;
import com.resync.app.ui.interfaces.DarkModeAware;
import com.resync.app.model.Subreddit;

/* JADX INFO: loaded from: classes.dex */
public class PostsSmallSubredditToolbarView extends FrameLayout implements DarkModeAware {

    public SubView subView;
    private LinearLayout textWrapper;
    private TextView titleView;

    private ImageView dropdownIcon;

    private TextView sortView;

    private int section;
    private String subreddit;

    private String sort;
    private String period;
    private Subreddit subredditModel;

    class RefreshLongClickListener implements View.OnLongClickListener {
        RefreshLongClickListener() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            BusSingleton.getBus().post(new RefreshPostsEvent());
            return false;
        }
    }

    public PostsSmallSubredditToolbarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        FrameLayout.inflate(context, R.layout.view_posts_small_subreddit_toolbar, this);
        this.subView = (SubView) findViewById(R.id.subview);
        this.textWrapper = (LinearLayout) findViewById(R.id.text);
        this.titleView = (TextView) findViewById(R.id.toolbar_title_small);
        this.dropdownIcon = (ImageView) findViewById(R.id.toolbar_title_dropdown);
        this.sortView = (TextView) findViewById(R.id.toolbar_sort_small);
        setOnClickListener(new View.OnClickListener() { // from class: g9.d
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PostsSmallSubredditToolbarView.this.onClicked(view);
            }
        });
        setOnLongClickListener(new RefreshLongClickListener());
        this.textWrapper.setOnClickListener(new View.OnClickListener() { // from class: g9.c
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PostsSmallSubredditToolbarView.this.onTextClicked(view);
            }
        });
        this.textWrapper.setOnLongClickListener(new View.OnLongClickListener() { // from class: g9.e
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                return PostsSmallSubredditToolbarView.this.onTextLongClicked(view);
            }
        });
        applyColors();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onClicked(View view) {
        scrollToTop();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onTextClicked(View view) {
        showSectionDialog(getContext(), this.section, this.subreddit);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onTextLongClicked(View view) {
        showSectionInputDialog(getContext(), this.section, this.subreddit);
        return true;
    }

    private void scrollToTop() {
        if (SettingsSingleton.getSettings().postsQuickScroll) {
            BusSingleton.getBus().post(new ScrollToTopOfPostsEvent());
        }
    }

    private void applyColors() {
        this.titleView.setTextColor(MonetUtil.getPrimaryTextColorAuto());
        this.sortView.setTextColor(MonetUtil.getPrimaryTextColorAuto());
        this.dropdownIcon.setColorFilter(MonetUtil.getPrimaryTextColorAuto());
    }

    public static void showSectionDialog(Context context, int section, String str) {
        FragmentManager fragmentManagerG;
        if (ContextHelper.isActivityFinishing(context) || (fragmentManagerG = ContextHelper.getFragmentManager(context)) == null) {
            return;
        }
        if (section == 2) {
            FragmentUtils.show(MessagingSectionSelectionDialogBottomSheet.class, fragmentManagerG);
            return;
        }
        if (section == 3) {
            FragmentUtils.show(ModSectionSelectionDialogBottomSheet.class, fragmentManagerG);
        } else if (section == 1) {
            FragmentUtils.showWithUser(ProfileSectionSelectionDialogBottomSheet.class, fragmentManagerG, MaterialPostsHelper.extractProfileUsername(str));
        } else {
            if (section != 0) {
                throw new RuntimeException("Unsupported section");
            }
            FragmentUtils.showWithArguments(SubredditsBottomSheetFragment.class, fragmentManagerG, SubredditsBottomSheetFragment.buildArgs(true));
        }
    }

    public static void showSectionInputDialog(Context context, int section, String str) {
        FragmentManager fragmentManagerG;
        if (ContextHelper.isActivityFinishing(context) || (fragmentManagerG = ContextHelper.getFragmentManager(context)) == null || section == 2 || section == 3) {
            return;
        }
        if (section == 1) {
            FragmentUtils.showWithSubreddit(ChangeSubredditInputDialogBottomSheet.class, fragmentManagerG, str);
        } else {
            if (section != 0) {
                throw new RuntimeException("Unsupported section");
            }
            FragmentUtils.showWithSubreddit(ChangeSubredditInputDialogBottomSheet.class, fragmentManagerG, str);
        }
    }

    public String getSort() {
        return this.sort;
    }

    public String getPeriod() {
        return this.period;
    }

    public TextView getSortView() {
        return this.sortView;
    }

    public String getSubreddit() {
        return this.subreddit;
    }

    @Override // com.resync.app.ui.interfaces.DarkModeAware
    public void onDarkModeChanged() {
        applyColors();
    }

    public TextView getTitleView() {
        return this.titleView;
    }

    public void setSection(int section, String str, String str2, String str3) {
        this.section = section;
        this.subreddit = str;
        this.sort = str2;
        this.period = str3;
        if (section == 2) {
            this.titleView.setText("Inbox");
            this.sortView.setText(MaterialPostsHelper.getMessagingSectionTitleFromString(str));
            return;
        }
        if (section == 1) {
            Subreddit subreddit = this.subredditModel;
            if (subreddit != null) {
                this.titleView.setText(subreddit.data.name);
            } else {
                this.titleView.setText(SubredditHelper.getDisplayTitle(str, false));
            }
            this.sortView.setText(MaterialPostsHelper.extractProfileSection(str));
            return;
        }
        if (section == 3) {
            this.titleView.setText(SubredditHelper.getDisplayTitle(str, false));
            this.sortView.setText(MaterialPostsHelper.extractModSection(str));
            return;
        }
        if (section != 0) {
            throw new RuntimeException("Unsupported section");
        }
        this.titleView.setText(SubredditHelper.getDisplayTitle(str, false));
        if (MaterialPostsHelper.isAggregateFeed(str)) {
            setSortText(str2, str3);
            return;
        }
        if (MaterialPostsHelper.isStandardSubreddit(str)) {
            setSortText(str2, str3);
            return;
        }
        if (MaterialPostsHelper.isMultireddit(str)) {
            setSortText(str2, str3);
            return;
        }
        if (MaterialPostsHelper.isFriends(str)) {
            setSortText(str2, str3);
            return;
        }
        if (MaterialPostsHelper.isSearch(str)) {
            setSortText(str2, str3);
            return;
        }
        if (MaterialPostsHelper.isSubSearch(str)) {
            setSortText(str2, str3);
            return;
        }
        if (str.equals("list__watching")) {
            setSortText(str2, str3);
        } else if (MaterialPostsHelper.isDomain(str)) {
            setSortText(str2, str3);
        } else if (MaterialPostsHelper.isSeen(str)) {
            setSortText(str2, str3);
        }
    }

    public void setSortText(String str, String str2) {
        this.sortView.setText(SubredditHelper.appendSortSuffix(str, str2));
    }

    public void setSortLabel(String str) {
        this.sortView.setText(str);
    }

    public void setSubreddit(Subreddit subreddit) {
        this.subredditModel = subreddit;
    }

    public void bindSubredditIcon(String str) {
        this.dropdownIcon.setVisibility(8);
        if (!MaterialPostsHelper.isStandardSubreddit(str) || !SettingsSingleton.getSettings().subredditIcons || !StringUtils.isNotEmpty(str)) {
            this.subView.setVisibility(8);
            ((FrameLayout.LayoutParams) this.textWrapper.getLayoutParams()).leftMargin = SizeHelper.dpToPx(16);
        } else {
            this.subView.setVisibility(0);
            this.subView.setSubreddit(str);
            ((FrameLayout.LayoutParams) this.textWrapper.getLayoutParams()).leftMargin = SizeHelper.dpToPx(48);
        }
    }

    public void setTitle(String str) {
        this.subreddit = null;
        this.titleView.setText(str);
    }
}
