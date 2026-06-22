package com.resync.app.ui.viewholders.drawer;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.buttons.HideButton;
import com.resync.app.ui.views.buttons.MoreButton;
import com.resync.app.ui.views.core.SubView;
import com.resync.app.ui.views.drawer.AccountSectionLinearLayout;
import com.resync.app.ui.views.responsive.CustomEditText;
import com.resync.app.ui.views.text.basic.ProductSansTextView;

/* JADX INFO: loaded from: classes3.dex */
public class DrawerHeaderHolder_ViewBinding implements Unbinder {
    private DrawerHeaderHolder target;
    private View titleWrapperView;
    private View moreButtonView;
    private View profileRowView;
    private View profileExpandIconView;
    private View messagingView;
    private View messagingExpandIconView;
    private View modView;
    private View modExpandIconView;
    private View submitView;
    private View chatView;
    private View searchView;
    private View settingsView;
    private View removeAdsView;
    private View ultraView;

    class ChatClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ DrawerHeaderHolder boundTarget;

        ChatClickListener(DrawerHeaderHolder drawerHeaderHolder) {
            this.boundTarget = drawerHeaderHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onChatClicked();
        }
    }

    class SearchClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ DrawerHeaderHolder boundTarget;

        SearchClickListener(DrawerHeaderHolder drawerHeaderHolder) {
            this.boundTarget = drawerHeaderHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onSearchClicked();
        }
    }

    class SettingsClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ DrawerHeaderHolder boundTarget;

        SettingsClickListener(DrawerHeaderHolder drawerHeaderHolder) {
            this.boundTarget = drawerHeaderHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onSettingsClicked();
        }
    }

    class RemoveAdsClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ DrawerHeaderHolder boundTarget;

        RemoveAdsClickListener(DrawerHeaderHolder drawerHeaderHolder) {
            this.boundTarget = drawerHeaderHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onRemovedsClicked(view);
        }
    }

    class UltraClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ DrawerHeaderHolder boundTarget;

        UltraClickListener(DrawerHeaderHolder drawerHeaderHolder) {
            this.boundTarget = drawerHeaderHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onUltraCliclked(view);
        }
    }

    class ProfileIconClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ DrawerHeaderHolder boundTarget;

        ProfileIconClickListener(DrawerHeaderHolder drawerHeaderHolder) {
            this.boundTarget = drawerHeaderHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onProfileIconClicked();
        }
    }

    class MoreClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ DrawerHeaderHolder boundTarget;

        MoreClickListener(DrawerHeaderHolder drawerHeaderHolder) {
            this.boundTarget = drawerHeaderHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onMoreClicked(view);
        }
    }

    class ProfileClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ DrawerHeaderHolder boundTarget;

        ProfileClickListener(DrawerHeaderHolder drawerHeaderHolder) {
            this.boundTarget = drawerHeaderHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onProfileClicked();
        }
    }

    class ProfileWrapperClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ DrawerHeaderHolder boundTarget;

        ProfileWrapperClickListener(DrawerHeaderHolder drawerHeaderHolder) {
            this.boundTarget = drawerHeaderHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onProfileWrapperClicked();
        }
    }

    class MessagingClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ DrawerHeaderHolder boundTarget;

        MessagingClickListener(DrawerHeaderHolder drawerHeaderHolder) {
            this.boundTarget = drawerHeaderHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onMessaingClicked();
        }
    }

    class MessagingWrapperClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ DrawerHeaderHolder boundTarget;

        MessagingWrapperClickListener(DrawerHeaderHolder drawerHeaderHolder) {
            this.boundTarget = drawerHeaderHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onMessagingWrapperClicked();
        }
    }

    class ModClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ DrawerHeaderHolder boundTarget;

        ModClickListener(DrawerHeaderHolder drawerHeaderHolder) {
            this.boundTarget = drawerHeaderHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onModClicked();
        }
    }

    class ModWrapperClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ DrawerHeaderHolder boundTarget;

        ModWrapperClickListener(DrawerHeaderHolder drawerHeaderHolder) {
            this.boundTarget = drawerHeaderHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onModWrapperClicked();
        }
    }

    class SubmitClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ DrawerHeaderHolder boundTarget;

        SubmitClickListener(DrawerHeaderHolder drawerHeaderHolder) {
            this.boundTarget = drawerHeaderHolder;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.boundTarget.onSubmitClicked();
        }
    }

    public DrawerHeaderHolder_ViewBinding(DrawerHeaderHolder drawerHeaderHolder, View view) {
        this.target = drawerHeaderHolder;
        drawerHeaderHolder.profileView = (SubView) Utils.findRequiredViewAsType(view, R.id.material_toolbar_profile, "field 'profileView'", SubView.class);
        View viewC = Utils.findRequiredView(view, R.id.legacy_drawer_header_title_wrapper, "field 'titleWrapper' and method 'onProfileIconClicked'");
        drawerHeaderHolder.titleWrapper = (LinearLayout) Utils.castView(viewC, R.id.legacy_drawer_header_title_wrapper, "field 'titleWrapper'", LinearLayout.class);
        this.titleWrapperView = viewC;
        viewC.setOnClickListener(new ProfileIconClickListener(drawerHeaderHolder));
        drawerHeaderHolder.titleTextView = (ProductSansTextView) Utils.findRequiredViewAsType(view, R.id.legacy_drawer_header_title, "field 'titleTextView'", ProductSansTextView.class);
        View viewC2 = Utils.findRequiredView(view, R.id.legacy_drawer_header_more, "field 'moreButton' and method 'onMoreClicked'");
        drawerHeaderHolder.moreButton = (MoreButton) Utils.castView(viewC2, R.id.legacy_drawer_header_more, "field 'moreButton'", MoreButton.class);
        this.moreButtonView = viewC2;
        viewC2.setOnClickListener(new MoreClickListener(drawerHeaderHolder));
        drawerHeaderHolder.accountWrapper = (AccountSectionLinearLayout) Utils.findRequiredViewAsType(view, R.id.legacy_drawer_header_account_wrapper, "field 'accountWrapper'", AccountSectionLinearLayout.class);
        drawerHeaderHolder.accountExpandIcon = (AppCompatImageView) Utils.findRequiredViewAsType(view, R.id.legacy_drawer_header_title_expand_icon, "field 'accountExpandIcon'", AppCompatImageView.class);
        drawerHeaderHolder.profileRow = Utils.findRequiredView(view, R.id.legacy_drawer_header_profile_row_wrapper, "field 'profileRow'");
        View viewC3 = Utils.findRequiredView(view, R.id.legacy_drawer_header_profile, "field 'profile' and method 'onProfileClicked'");
        drawerHeaderHolder.profile = viewC3;
        this.profileRowView = viewC3;
        viewC3.setOnClickListener(new ProfileClickListener(drawerHeaderHolder));
        View viewC4 = Utils.findRequiredView(view, R.id.legacy_drawer_header_profile_expand_icon, "field 'profileExpandIcon' and method 'onProfileWrapperClicked'");
        drawerHeaderHolder.profileExpandIcon = (AppCompatImageView) Utils.castView(viewC4, R.id.legacy_drawer_header_profile_expand_icon, "field 'profileExpandIcon'", AppCompatImageView.class);
        this.profileExpandIconView = viewC4;
        viewC4.setOnClickListener(new ProfileWrapperClickListener(drawerHeaderHolder));
        drawerHeaderHolder.profileViewWrapper = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.legacy_drawer_header_profile_wrapper, "field 'profileViewWrapper'", LinearLayout.class);
        drawerHeaderHolder.messagingRow = Utils.findRequiredView(view, R.id.legacy_drawer_header_messaging_row_wrapper, "field 'messagingRow'");
        View viewC5 = Utils.findRequiredView(view, R.id.legacy_drawer_header_messaging, "field 'messaging' and method 'onMessaingClicked'");
        drawerHeaderHolder.messaging = viewC5;
        this.messagingView = viewC5;
        viewC5.setOnClickListener(new MessagingClickListener(drawerHeaderHolder));
        View viewC6 = Utils.findRequiredView(view, R.id.legacy_drawer_header_messaging_expand_icon, "field 'messagingExpandIcon' and method 'onMessagingWrapperClicked'");
        drawerHeaderHolder.messagingExpandIcon = (AppCompatImageView) Utils.castView(viewC6, R.id.legacy_drawer_header_messaging_expand_icon, "field 'messagingExpandIcon'", AppCompatImageView.class);
        this.messagingExpandIconView = viewC6;
        viewC6.setOnClickListener(new MessagingWrapperClickListener(drawerHeaderHolder));
        drawerHeaderHolder.messagingViewWrapper = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.legacy_drawer_header_messaging_wrapper, "field 'messagingViewWrapper'", LinearLayout.class);
        drawerHeaderHolder.modRowWrapper = (FrameLayout) Utils.findRequiredViewAsType(view, R.id.legacy_drawer_header_mod_row_wrapper, "field 'modRowWrapper'", FrameLayout.class);
        View viewC7 = Utils.findRequiredView(view, R.id.legacy_drawer_header_mod, "field 'mod' and method 'onModClicked'");
        drawerHeaderHolder.mod = viewC7;
        this.modView = viewC7;
        viewC7.setOnClickListener(new ModClickListener(drawerHeaderHolder));
        View viewC8 = Utils.findRequiredView(view, R.id.legacy_drawer_header_mod_expand_icon, "field 'modExpandIcon' and method 'onModWrapperClicked'");
        drawerHeaderHolder.modExpandIcon = (AppCompatImageView) Utils.castView(viewC8, R.id.legacy_drawer_header_mod_expand_icon, "field 'modExpandIcon'", AppCompatImageView.class);
        this.modExpandIconView = viewC8;
        viewC8.setOnClickListener(new ModWrapperClickListener(drawerHeaderHolder));
        drawerHeaderHolder.modViewWrapper = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.legacy_drawer_header_mod_wrapper, "field 'modViewWrapper'", LinearLayout.class);
        View viewC9 = Utils.findRequiredView(view, R.id.legacy_drawer_header_submit, "field 'submit' and method 'onSubmitClicked'");
        drawerHeaderHolder.submit = viewC9;
        this.submitView = viewC9;
        viewC9.setOnClickListener(new SubmitClickListener(drawerHeaderHolder));
        View viewC10 = Utils.findRequiredView(view, R.id.legacy_drawer_header_chat, "field 'chat' and method 'onChatClicked'");
        drawerHeaderHolder.chat = viewC10;
        this.chatView = viewC10;
        viewC10.setOnClickListener(new ChatClickListener(drawerHeaderHolder));
        View viewC11 = Utils.findRequiredView(view, R.id.legacy_drawer_header_search, "field 'search' and method 'onSearchClicked'");
        drawerHeaderHolder.search = viewC11;
        this.searchView = viewC11;
        viewC11.setOnClickListener(new SearchClickListener(drawerHeaderHolder));
        View viewC12 = Utils.findRequiredView(view, R.id.legacy_drawer_header_settings, "field 'settings' and method 'onSettingsClicked'");
        drawerHeaderHolder.settings = viewC12;
        this.settingsView = viewC12;
        viewC12.setOnClickListener(new SettingsClickListener(drawerHeaderHolder));
        View viewC13 = Utils.findRequiredView(view, R.id.legacy_drawer_header_remove_ads, "field 'removeAds' and method 'onRemovedsClicked'");
        drawerHeaderHolder.removeAds = viewC13;
        this.removeAdsView = viewC13;
        viewC13.setOnClickListener(new RemoveAdsClickListener(drawerHeaderHolder));
        View viewC14 = Utils.findRequiredView(view, R.id.legacy_drawer_header_ultra, "field 'ultra' and method 'onUltraCliclked'");
        drawerHeaderHolder.ultra = viewC14;
        this.ultraView = viewC14;
        viewC14.setOnClickListener(new UltraClickListener(drawerHeaderHolder));
        drawerHeaderHolder.searchBox = (CustomEditText) Utils.findRequiredViewAsType(view, R.id.search_box, "field 'searchBox'", CustomEditText.class);
        drawerHeaderHolder.searchBoxParent = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.search_box_parent, "field 'searchBoxParent'", ViewGroup.class);
        drawerHeaderHolder.deleteButton = (HideButton) Utils.findRequiredViewAsType(view, R.id.row_delete, "field 'deleteButton'", HideButton.class);
    }

    @Override
    public void unbind() {
        DrawerHeaderHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        target.moreButton = null;
        target.messagingRow = null;
        target.messaging = null;
        target.messagingExpandIcon = null;
        target.messagingViewWrapper = null;
        target.modRowWrapper = null;
        target.mod = null;
        target.modExpandIcon = null;
        target.modViewWrapper = null;
        this.titleWrapperView.setOnClickListener(null);
        this.titleWrapperView = null;
        this.moreButtonView.setOnClickListener(null);
        this.moreButtonView = null;
        this.profileRowView.setOnClickListener(null);
        this.profileRowView = null;
        this.profileExpandIconView.setOnClickListener(null);
        this.profileExpandIconView = null;
        this.messagingView.setOnClickListener(null);
        this.messagingView = null;
        this.messagingExpandIconView.setOnClickListener(null);
        this.messagingExpandIconView = null;
        this.modView.setOnClickListener(null);
        this.modView = null;
        this.modExpandIconView.setOnClickListener(null);
        this.modExpandIconView = null;
        this.submitView.setOnClickListener(null);
        this.submitView = null;
        this.chatView.setOnClickListener(null);
        this.chatView = null;
        this.searchView.setOnClickListener(null);
        this.searchView = null;
        this.settingsView.setOnClickListener(null);
        this.settingsView = null;
        this.removeAdsView.setOnClickListener(null);
        this.removeAdsView = null;
        this.ultraView.setOnClickListener(null);
        this.ultraView = null;
    }
}
