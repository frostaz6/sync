package com.resync.app.ui.fragments.sidebar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.MaterialPostsHelper;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.ui.fragments.RedditFragment;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.fragment_dialogs.EditFragment;
import com.resync.app.ui.fragment_dialogs.bottom.SubredditRulesBottomSheetFragment;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.flair.UserPickFlairRadioSelectionDialogBottomSheet;
import com.resync.app.ui.views.chips.ContextualChipsDropInView;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.text.spannable.children.HtmlTableView;
import com.resync.app.ui.views.text.spannable.children.other.LargeToolbarDescription;
import com.resync.app.helpers.ui.PopupMenuHelper;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.dialogs.AddToMultiAsyncRadioSelectionDialogBottomSheet;
import com.resync.app.dialogs.BlockAlertDialogBottomSheet;
import com.resync.app.dialogs.PostFlairsAsyncSelectionDialogBottomSheet;
import com.resync.app.dialogs.ViewModsAsyncSelectionDialogBottomSheet;
import com.resync.app.util.Logger;
import com.resync.app.util.links.LinkHelper;
import com.resync.app.net.oauth.OAuthSubredditInfoRequest;
import com.resync.app.model.Data;
import com.resync.app.model.Subreddit;

/* JADX INFO: loaded from: classes2.dex */
public class AboutFragment extends RedditFragment implements Response.Listener<Subreddit>, Response.ErrorListener {

    @BindView(R.id.contextual_chips)
    ContextualChipsDropInView mChips;

    @BindView(R.id.content)
    HtmlTableView mContent;

    @BindView(R.id.header)
    SheetHeaderView mHeader;

    @BindView(R.id.scrollview)
    NestedScrollView mScrollView;
    LargeToolbarDescription toolbarDescription;

    /* JADX INFO: renamed from: s0, reason: collision with root package name */
    String subreddit;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambdaProfileMenu(MenuItem menuItem, MenuItem menuItem2, MenuItem menuItem3) {
        String username = MaterialPostsHelper.extractProfileUsername(this.subreddit);
        if (!menuItem3.equals(menuItem)) {
            if (!menuItem3.equals(menuItem2)) {
                return true;
            }
            FragmentUtils.showWithUser(BlockAlertDialogBottomSheet.class, getContextFragmentManager(), username);
            return true;
        }
        RedditHelper.shareTextWithSubject(getContext(), username, "https://reddit.com/u/" + username);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambdaSubredditMenu(MenuItem menuItem, MenuItem menuItem2, MenuItem menuItem3, MenuItem menuItem4, MenuItem menuItem5, MenuItem menuItem6, MenuItem menuItem7, MenuItem menuItem8, MenuItem menuItem9) {
        if (menuItem9.equals(menuItem)) {
            LinkHelper.open(getContext(), "https://www.reddit.com/r/" + this.subreddit + "/wiki/index");
            return true;
        }
        if (menuItem9.equals(menuItem2)) {
            FragmentUtils.showWithSubreddit(PostFlairsAsyncSelectionDialogBottomSheet.class, getContextFragmentManager(), this.subreddit);
            return true;
        }
        if (menuItem9.equals(menuItem3)) {
            FragmentUtils.showWithSubreddit(SubredditRulesBottomSheetFragment.class, getContextFragmentManager(), this.subreddit);
            return true;
        }
        if (menuItem9.equals(menuItem4)) {
            RedditHelper.shareTextWithSubject(getActivity(), "/r/" + this.subreddit, "https://reddit.com/r/" + this.subreddit);
            return true;
        }
        if (menuItem9.equals(menuItem5)) {
            if (!AccountSingleton.getInstance().isLoggedIn()) {
                StaticToast.show(getContext(), R.string.common_generic_error_logged_out);
                return true;
            }
            EditFragment.newMessageInstance("/r/" + this.subreddit).show(getContextFragmentManager(), "EditFragment");
            return true;
        }
        if (menuItem9.equals(menuItem6)) {
            FragmentUtils.showWithSubreddit(ViewModsAsyncSelectionDialogBottomSheet.class, getChildFragmentManager(), this.subreddit);
            return true;
        }
        if (menuItem9.equals(menuItem7)) {
            FragmentUtils.showWithSubreddit(UserPickFlairRadioSelectionDialogBottomSheet.class, getContextFragmentManager(), this.subreddit);
            return true;
        }
        if (!menuItem9.equals(menuItem8)) {
            return true;
        }
        FragmentUtils.showWithSubreddit(AddToMultiAsyncRadioSelectionDialogBottomSheet.class, getContextFragmentManager(), this.subreddit);
        return true;
    }

    private void load() {
        Logger.verbose("LOADING: " + this.subreddit);
        if (MaterialPostsHelper.isUserProfile(this.subreddit)) {
            this.mHeader.setTitle(MaterialPostsHelper.extractProfileUsername(this.subreddit));
            this.mHeader.setUsername(MaterialPostsHelper.extractProfileUsername(this.subreddit));
        } else {
            this.mHeader.setTitle(this.subreddit);
            this.mHeader.setSubreddit(this.subreddit);
        }
        this.mScrollView.scrollTo(0, 0);
        this.mHeader.clearBanner();
        this.mChips.refresh();
        this.mContent.reset();
        if (StringUtils.equalsAnyIgnoreCase(ContextHelper.getCurrentSubredditName(getActivity()), this.subreddit)) {
            onResponse(ContextHelper.getCurrentSubreddit(getContext()));
        } else {
            OAuthRequestManager.sendWithAppContext(new OAuthSubredditInfoRequest(getContext(), null, this.subreddit, this, this));
        }
    }

    public static AboutFragment newInstance(String str, boolean z) {
        AboutFragment aboutFragment = new AboutFragment();
        Bundle bundle = new Bundle();
        bundle.putString("subreddit", str);
        bundle.putBoolean("drawer_mode", z);
        aboutFragment.setArguments(bundle);
        return aboutFragment;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showMoreMenu(View view) {
        if (MaterialPostsHelper.isUserProfile(this.subreddit)) {
            PopupMenu popupMenuC = PopupMenuHelper.create(view, 5);
            final MenuItem menuItemAdd = popupMenuC.getMenu().add("Share profile");
            final MenuItem menuItemAdd2 = popupMenuC.getMenu().add("Block profile");
            popupMenuC.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: sa.b
                @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
                public final boolean onMenuItemClick(MenuItem menuItem) {
                    return AboutFragment.this.lambdaProfileMenu(menuItemAdd, menuItemAdd2, menuItem);
                }
            });
            popupMenuC.show();
            return;
        }
        PopupMenu popupMenuC2 = PopupMenuHelper.create(view, 5);
        final MenuItem menuItemAdd3 = popupMenuC2.getMenu().add("View wiki");
        final MenuItem menuItemAdd4 = popupMenuC2.getMenu().add("Filter by flair");
        final MenuItem menuItemAdd5 = popupMenuC2.getMenu().add("View rules");
        final MenuItem menuItemAdd6 = popupMenuC2.getMenu().add("Share");
        final MenuItem menuItemAdd7 = popupMenuC2.getMenu().add("Message mods");
        final MenuItem menuItemAdd8 = popupMenuC2.getMenu().add("View mods");
        final MenuItem menuItemAdd9 = popupMenuC2.getMenu().add("Edit flair");
        final MenuItem menuItemAdd10 = popupMenuC2.getMenu().add("Add to multi");
        popupMenuC2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: sa.c
            @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
            public final boolean onMenuItemClick(MenuItem menuItem) {
                return AboutFragment.this.lambdaSubredditMenu(menuItemAdd3, menuItemAdd4, menuItemAdd5, menuItemAdd6, menuItemAdd7, menuItemAdd8, menuItemAdd9, menuItemAdd10, menuItem);
            }
        });
        popupMenuC2.show();
    }

    private void animateIn(View... viewArr) {
        for (View view : viewArr) {
            view.setAlpha(0.0f);
            view.animate().alpha(1.0f).setInterpolator(new FastOutSlowInInterpolator());
        }
    }

    private FragmentManager getContextFragmentManager() {
        return ContextHelper.getFragmentManager(getContext());
    }

    @Override // com.android.volley.Response.Listener
    /* JADX INFO: renamed from: E3, reason: merged with bridge method [inline-methods] */
    public void onResponse(Subreddit subreddit) {
        if (isAttached()) {
            if (subreddit == null || subreddit.data == null) {
                this.mContent.buildContent("Error loading about (2)");
                return;
            }
            if (MaterialPostsHelper.isUserProfile(this.subreddit)) {
                LargeToolbarDescription largeToolbarDescription = this.toolbarDescription;
                Data data = subreddit.data;
                largeToolbarDescription.bindUserStats(data.linkKarma, data.commentKarma, data.formattedDate);
                if (StringUtils.isNotEmpty(subreddit.data.publicDescriptionHtml)) {
                    this.mContent.setText(subreddit.data.publicDescriptionHtml);
                } else {
                    this.mContent.setText("This user has no public description");
                }
            } else {
                LargeToolbarDescription largeToolbarDescription2 = this.toolbarDescription;
                Data data2 = subreddit.data;
                largeToolbarDescription2.bindSubredditStats(data2.subscribers, data2.accountsActive);
                this.mContent.setText(subreddit.data.descriptionHtml);
            }
            this.mHeader.setBanner(subreddit);
            animateIn(this.toolbarDescription, this.mContent);
        }
    }

    public void setSubreddit(String str) {
        if (StringUtils.equalsAnyIgnoreCase(this.subreddit, str)) {
            return;
        }
        this.subreddit = str;
        load();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(getLayoutResId(), (ViewGroup) null);
        ButterKnife.bind(this, viewInflate);
        return viewInflate;
    }

    @Override // com.resync.app.ui.interfaces.RedditFragmentInterface
    public int getLayoutResId() {
        return R.layout.fragment_about;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.subreddit = getArguments().getString("subreddit");
        if (getArguments().getBoolean("drawer_mode")) {
            this.mHeader.setDragButtonVisible(false);
            this.mHeader.clearBannerOutline();
        }
        this.mChips.minimize();
        this.mHeader.setMoreVisible(true);
        this.mHeader.setDividerVisible(true);
        this.mHeader.setMoreClickListener(new View.OnClickListener() { // from class: sa.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                AboutFragment.this.showMoreMenu(view2);
            }
        });
        this.mContent.setForeground(null);
        this.mContent.setBackground(null);
        this.mContent.applyCardBackground();
        this.mContent.applyDefaultPadding();
        LargeToolbarDescription largeToolbarDescription = new LargeToolbarDescription(getContext(), 9, 1, 2);
        this.toolbarDescription = largeToolbarDescription;
        largeToolbarDescription.setTextSize(1, 10.0f);
        LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(-2, -2);
        ((LinearLayout.LayoutParams) layoutParams).gravity = 1;
        ((LinearLayout.LayoutParams) layoutParams).topMargin = SizeHelper.dpToPx(4);
        this.toolbarDescription.setLayoutParams(layoutParams);
        this.mHeader.addHeaderView(this.toolbarDescription);
        load();
    }

    @Override // com.android.volley.Response.ErrorListener
    public void onErrorResponse(VolleyError volleyError) {
        if (isAttached()) {
            Logger.verbose("ERROR: " + volleyError);
            this.mContent.setText("Error loading about (1)");
        }
    }
}
