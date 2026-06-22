package com.resync.app.ui.viewholders.drawer;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import com.bumptech.glide.RedditHelper;
import com.resync.app.R;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.PreferencesActivity;
import com.resync.app.ui.viewholders.drawer.DrawerHeaderHolder;
import com.resync.app.ui.views.buttons.HideButton;
import com.resync.app.ui.views.buttons.MoreButton;
import com.resync.app.ui.views.core.SubView;
import com.resync.app.ui.views.drawer.AccountSectionLinearLayout;
import com.resync.app.ui.views.responsive.CustomEditText;
import com.resync.app.ui.views.text.basic.ProductSansTextView;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.SubredditListMenuHelper;
import okhttp3.internal.http2.Http2Connection;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.ModeratedSubredditManager;
import com.resync.app.events.SearchSubredditsEvent;
import com.resync.app.events.SubredditFilterClickedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.events.CloseDrawerEvent;
import com.resync.app.events.DrawerOpenMessagingEvent;
import com.resync.app.events.DrawerOpenModEvent;
import com.resync.app.events.DrawerOpenProfileEvent;
import com.resync.app.events.DrawerOpenSettingsEvent;
import com.resync.app.events.DrawerOpenSubmitEvent;
import com.resync.app.events.OpenMessagingSection;
import com.resync.app.events.OpenModSection;
import com.resync.app.events.OpenProfileSection;
import com.resync.app.premium.SyncIapHelper;
import com.resync.app.helpers.AnimationHelper;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.premium.UltraHelper;
import com.resync.app.auth.OAuthLoginFragment;

/* JADX INFO: loaded from: classes3.dex */
public class DrawerHeaderHolder extends RecyclerView.ViewHolder {

    Context context;

    AppCompatImageView accountExpandIcon;

    AccountSectionLinearLayout accountWrapper;

    View chat;

    HideButton deleteButton;

    View messaging;

    AppCompatImageView messagingExpandIcon;

    View messagingRow;

    LinearLayout messagingViewWrapper;

    View mod;

    AppCompatImageView modExpandIcon;

    FrameLayout modRowWrapper;

    LinearLayout modViewWrapper;

    MoreButton moreButton;

    View profile;

    AppCompatImageView profileExpandIcon;

    View profileRow;

    SubView profileView;

    LinearLayout profileViewWrapper;

    View removeAds;

    View search;

    CustomEditText searchBox;

    ViewGroup searchBoxParent;

    View settings;

    View submit;

    ProductSansTextView titleTextView;

    LinearLayout titleWrapper;

    View ultra;

    class SearchTextWatcher implements TextWatcher {
        SearchTextWatcher() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            DrawerHeaderHolder.this.deleteButton.setVisibility(StringUtils.isEmpty(charSequence) ? 4 : 0);
            DrawerHeaderHolder.this.moreButton.setVisibility(StringUtils.isEmpty(charSequence) ? 0 : 4);
            BusSingleton.getBus().post(new SearchSubredditsEvent(charSequence.toString()));
        }
    }

    public DrawerHeaderHolder(Context context, View view) {
        super(view);
        this.context = context;
        ButterKnife.bind(this, view);
        this.profileView.setUsername(AccountSingleton.getInstance().getUsername());
        this.accountExpandIcon.setImageTintList(ColorStateList.valueOf(NightHelper.isNight() ? -8947849 : -6645094));
        this.profileExpandIcon.setImageTintList(ColorStateList.valueOf(NightHelper.isNight() ? -8947849 : -6645094));
        this.messagingExpandIcon.setImageTintList(ColorStateList.valueOf(NightHelper.isNight() ? -8947849 : -6645094));
        this.modExpandIcon.setImageTintList(ColorStateList.valueOf(NightHelper.isNight() ? -8947849 : -6645094));
        if (!AccountSingleton.isLoggedIn(context)) {
            this.profileRow.setVisibility(8);
            this.messagingRow.setVisibility(8);
        }
        if (!ModeratedSubredditManager.getInstance().hasModerated()) {
            this.modRowWrapper.setVisibility(8);
        }
        if (SyncIapHelper.areAdsEnabled(this.context)) {
            this.removeAds.setVisibility(0);
        }
        this.ultra.setVisibility(UltraHelper.isUltra() ? 8 : 0);
        this.chat.setVisibility(8);
        if (!AccountSingleton.isLoggedIn(context)) {
            this.searchBox.setImeOptions(Http2Connection.OKHTTP_CLIENT_WINDOW_SIZE);
        }
        this.searchBox.setHintTextColor(MonetUtil.getSecondaryTextColorAuto());
        this.searchBox.setTextColor(StyleSingleton.getTextColorAuto(this.context, false));
        this.searchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: qb.e
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view2, boolean hasFocus) {
                DrawerHeaderHolder.this.onSearchBoxFocusChanged(view2, hasFocus);
            }
        });
        this.searchBox.addTextChangedListener(new SearchTextWatcher());
        this.deleteButton.setOnClickListener(new View.OnClickListener() { // from class: qb.b
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                DrawerHeaderHolder.this.onDeleteClicked(view2);
            }
        });
        this.titleWrapper.setBackgroundResource(R.drawable.button_light);
        this.titleTextView.setText(AccountSingleton.getInstance().getDisplayUsername());
        addProfileRow("Comments");
        addProfileRow("Submitted");
        addProfileRow("Upvoted");
        addProfileRow("Hidden");
        addProfileRow("Saved");
        addProfileRow("Friends");
        addProfileRow("Watching");
        addMessagingRow("All", 0);
        addMessagingRow("Unread", 1);
        addMessagingRow("Messages", 2);
        addMessagingRow("Sent", 3);
        addMessagingRow("Comment replies", 4);
        addMessagingRow("Post replies", 5);
        addMessagingRow("Mentions", 6);
        addModRow("Modqueue");
        addModRow("Reports");
        addModRow("Spam");
        addModRow("Edited");
        addModRow("Unmoderated");
        addModRow("Comments");
        this.accountWrapper.setTitleTextView(this.titleTextView);
        this.accountWrapper.setExpandIcon(this.accountExpandIcon);
        this.accountWrapper.reload();
        collapseAllSections();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void openMessagingSection(int section, View view) {
        BusSingleton.getBus().post(new OpenMessagingSection(section));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void openModSection(String section, View view) {
        BusSingleton.getBus().post(new OpenModSection(section));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void openProfileSection(String section, View view) {
        BusSingleton.getBus().post(new OpenProfileSection(section));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void notifyFilterClicked(boolean filtered) {
        BusSingleton.getBus().post(new SubredditFilterClickedEvent(filtered));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onSearchBoxFocusChanged(View view, final boolean hasFocus) {
        collapseAllSections();
        this.profileViewWrapper.post(new Runnable() { // from class: qb.f
            @Override // java.lang.Runnable
            public final void run() {
                DrawerHeaderHolder.notifyFilterClicked(hasFocus);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onDeleteClicked(View view) {
        this.searchBox.setText((CharSequence) null);
    }

    public static DrawerHeaderHolder create(Context context, ViewGroup viewGroup) {
        return new DrawerHeaderHolder(context, LayoutInflater.from(context).inflate(R.layout.holder_drawer_header, viewGroup, false));
    }

    private void collapseAllSections() {
        this.profileViewWrapper.setVisibility(8);
        this.messagingViewWrapper.setVisibility(8);
        this.modViewWrapper.setVisibility(8);
        this.accountWrapper.setVisibility(8);
    }

    void addMessagingRow(String title, final int section) {
        int padding = (int) SizeHelper.dpToPxFloat(12);
        TextView textView = new TextView(this.context);
        textView.setBackgroundResource(R.drawable.button_light);
        textView.setPadding(((int) SizeHelper.dpToPxFloat(52)) + padding, padding, padding, padding);
        textView.setTextColor(StyleSingleton.getTextColorAuto(this.context, false));
        textView.setTypeface(TypefaceManager.get(9));
        textView.setTextSize(1, RedditHelper.getSmallHeaderTextSize(SettingsSingleton.getSettings().fontSize));
        textView.setText(title);
        textView.setOnClickListener(new View.OnClickListener() { // from class: qb.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DrawerHeaderHolder.openMessagingSection(section, view);
            }
        });
        this.messagingViewWrapper.addView(textView);
    }

    void addModRow(final String title) {
        int padding = (int) SizeHelper.dpToPxFloat(12);
        TextView textView = new TextView(this.context);
        textView.setBackgroundResource(R.drawable.button_light);
        textView.setPadding(((int) SizeHelper.dpToPxFloat(52)) + padding, padding, padding, padding);
        textView.setTextColor(StyleSingleton.getTextColorAuto(this.context, false));
        textView.setTypeface(TypefaceManager.get(9));
        textView.setTextSize(1, RedditHelper.getSmallHeaderTextSize(SettingsSingleton.getSettings().fontSize));
        textView.setText(title);
        textView.setOnClickListener(new View.OnClickListener() { // from class: qb.c
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DrawerHeaderHolder.openModSection(title, view);
            }
        });
        this.modViewWrapper.addView(textView);
    }

    void addProfileRow(final String title) {
        int padding = (int) SizeHelper.dpToPxFloat(12);
        TextView textView = new TextView(this.context);
        textView.setBackgroundResource(R.drawable.button_light);
        textView.setPadding(((int) SizeHelper.dpToPxFloat(52)) + padding, padding, padding, padding);
        textView.setTextColor(StyleSingleton.getTextColorAuto(this.context, false));
        textView.setTypeface(TypefaceManager.get(9));
        textView.setTextSize(1, RedditHelper.getSmallHeaderTextSize(SettingsSingleton.getSettings().fontSize));
        textView.setText(title);
        textView.setOnClickListener(new View.OnClickListener() { // from class: qb.d
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DrawerHeaderHolder.openProfileSection(title, view);
            }
        });
        this.profileViewWrapper.addView(textView);
    }

    public void onChatClicked() {
    }

    public void onMessagingWrapperClicked() {
        if (this.messagingViewWrapper.getVisibility() != 8) {
            AnimationHelper.collapse(this.messagingViewWrapper);
            this.messagingExpandIcon.animate().setDuration(250L).rotation(0.0f);
        } else {
            this.searchBox.clearFocus();
            this.messagingViewWrapper.setVisibility(0);
            AnimationHelper.expand(this.messagingViewWrapper);
            this.messagingExpandIcon.animate().setDuration(250L).rotation(180.0f);
        }
    }

    public void onMessaingClicked() {
        BusSingleton.getBus().post(new DrawerOpenMessagingEvent());
    }

    public void onModClicked() {
        BusSingleton.getBus().post(new DrawerOpenModEvent());
    }

    public void onModWrapperClicked() {
        if (this.modViewWrapper.getVisibility() != 8) {
            AnimationHelper.collapse(this.modViewWrapper);
            this.modExpandIcon.animate().setDuration(250L).rotation(0.0f);
        } else {
            this.searchBox.clearFocus();
            this.modViewWrapper.setVisibility(0);
            AnimationHelper.expand(this.modViewWrapper);
            this.modExpandIcon.animate().setDuration(250L).rotation(180.0f);
        }
    }

    public void onMoreClicked(View view) {
        SubredditListMenuHelper.showOptionsMenu(view, false);
    }

    public void onProfileClicked() {
        BusSingleton.getBus().post(new DrawerOpenProfileEvent());
    }

    public void onProfileIconClicked() {
        if (this.accountWrapper.getChildCount() == 0) {
            OAuthLoginFragment.newInstance().show(ContextHelper.getFragmentManager(this.context), null);
            BusSingleton.getBus().post(new CloseDrawerEvent());
        } else if (this.accountWrapper.getVisibility() != 8) {
            AnimationHelper.collapse(this.accountWrapper);
            this.accountExpandIcon.animate().setDuration(250L).rotation(0.0f);
        } else {
            this.searchBox.clearFocus();
            this.accountWrapper.setVisibility(0);
            AnimationHelper.expand(this.accountWrapper);
            this.accountExpandIcon.animate().setDuration(250L).rotation(180.0f);
        }
    }

    public void onProfileWrapperClicked() {
        if (this.profileViewWrapper.getVisibility() != 8) {
            AnimationHelper.collapse(this.profileViewWrapper);
            this.profileExpandIcon.animate().setDuration(250L).rotation(0.0f);
        } else {
            this.searchBox.clearFocus();
            this.profileViewWrapper.setVisibility(0);
            AnimationHelper.expand(this.profileViewWrapper);
            this.profileExpandIcon.animate().setDuration(250L).rotation(180.0f);
        }
    }

    public void onRemovedsClicked(View view) {
        // ReSync: in-app purchases removed (Ultra is unlocked for everyone).
        BusSingleton.getBus().post(new CloseDrawerEvent());
    }

    public void onSearchClicked() {
    }

    public void onSettingsClicked() {
        BusSingleton.getBus().post(new DrawerOpenSettingsEvent());
    }

    public void onSubmitClicked() {
        BusSingleton.getBus().post(new DrawerOpenSubmitEvent());
    }

    public void onUltraCliclked(View view) {
        Intent intent = new Intent(view.getContext(), (Class<?>) PreferencesActivity.class);
        intent.putExtra("mode", view.getContext().getResources().getInteger(R.integer.ULTRA));
        view.getContext().startActivity(intent);
        BusSingleton.getBus().post(new CloseDrawerEvent());
    }
}
