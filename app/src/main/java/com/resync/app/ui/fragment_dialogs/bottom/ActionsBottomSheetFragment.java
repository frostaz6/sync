package com.resync.app.ui.fragment_dialogs.bottom;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.PopupMenu;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.activities.CasualActivity;
import com.resync.app.ui.activities.MessagingActivity;
import com.resync.app.ui.activities.PreferencesActivity;
import com.resync.app.ui.activities.SwipeActivity;
import com.resync.app.ui.views.chips.ContextualChipsDropInView;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.text.basic.ProductSansTextView;
import com.resync.app.ui.views.tooltips.TooltipView;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.PopupMenuHelper;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.widgets.recycler.ActionItemMoveCallback;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.PerSubSortHelper;
import com.resync.app.events.CloseActionsBottomsheetEvent;
import com.resync.app.events.RefreshEvent;
import com.resync.app.events.SubredditSelectedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.singletons.SingletonManager;
import com.resync.app.widgets.MoreActionsItemView;
import com.resync.app.events.HideReadEvent;
import com.resync.app.events.ScrollToTopOfPostsEvent;
import com.resync.app.dialogs.CustomBottomSheetDialogFragment;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ActionsManager;
import com.resync.app.helpers.ActivityHelper;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.helpers.RecentHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.SyncManager;
import com.resync.app.dialogs.ChangeViewSelectionDialogBottomSheet;
import com.resync.app.dialogs.RecentlyViewedSelectionDialogBottomSheet;
import com.resync.app.dialogs.SubmitSelectionDialogBottomSheet;
import com.resync.app.helpers.DataSavingHelper;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public class ActionsBottomSheetFragment extends CustomBottomSheetDialogFragment {
    boolean firstTime = false;

    @BindView(R.id.contextual_chips)
    ContextualChipsDropInView contextualChipsDropInView;

    @BindView(R.id.header)
    SheetHeaderView headerView;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.tooltip)
    TooltipView tooltipView;

    static class AccountSwitchLoaderListener implements Loader.OnLoadCompleteListener<Cursor> {

        final /* synthetic */ FragmentActivity activity;
        final /* synthetic */ CursorLoader loader;

        class SwitchAccountRunnable implements Runnable {

            final /* synthetic */ String accountName;
            final /* synthetic */ String refreshToken;

            SwitchAccountRunnable(String str, String str2) {
                this.accountName = str;
                this.refreshToken = str2;
            }

            @Override // java.lang.Runnable
            public void run() {
                StaticToast.show("Switching to account: " + this.accountName);
                AccountSingleton.getInstance().updateSession(this.accountName, this.refreshToken, true);
                SingletonManager.reloadAll(true);
                AccountSwitchLoaderListener.this.activity.getContentResolver().delete(RedditProvider.URI_SWAP_ACCOUNTS, null, null);
                ActivityHelper.restartToMain(AccountSwitchLoaderListener.this.activity);
            }
        }

        AccountSwitchLoaderListener(FragmentActivity fragmentActivity, CursorLoader cursorLoader) {
            this.activity = fragmentActivity;
            this.loader = cursorLoader;
        }

        @Override // android.content.Loader.OnLoadCompleteListener
        public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
            if (this.activity == null || cursor == null) {
                return;
            }
            this.loader.unregisterListener(this);
            if (cursor.getCount() == 2) {
                StaticToast.show("You need two accounts to toggle!");
                return;
            }
            int position = 0;
            while (true) {
                if (position >= cursor.getCount()) {
                    position = -1;
                    break;
                }
                cursor.moveToPosition(position);
                if (AccountSingleton.getInstance().getUsername().equalsIgnoreCase(cursor.getString(cursor.getColumnIndex("account_name")))) {
                    break;
                } else {
                    position++;
                }
            }
            if (position > -1) {
                int nextPosition = position + 1;
                cursor.moveToPosition(nextPosition <= cursor.getCount() - 1 ? nextPosition : 1);
                String string = cursor.getString(cursor.getColumnIndex("account_name"));
                ActionsBottomSheetFragment.runWithBiometricLock(this.activity, string, new SwitchAccountRunnable(string, cursor.getString(cursor.getColumnIndex("account_refresh_token"))));
            }
        }
    }

    static class BiometricAuthCallback extends BiometricPrompt.AuthenticationCallback {

        final /* synthetic */ FragmentActivity activity;
        final /* synthetic */ Runnable callback;

        BiometricAuthCallback(FragmentActivity fragmentActivity, Runnable runnable) {
            this.activity = fragmentActivity;
            this.callback = runnable;
        }

        @Override // androidx.biometric.BiometricPrompt.AuthenticationCallback
        public void onAuthenticationError(int errorCode, CharSequence charSequence) {
            super.onAuthenticationError(errorCode, charSequence);
            StaticToast.show("Authentication error: " + ((Object) charSequence));
        }

        @Override // androidx.biometric.BiometricPrompt.AuthenticationCallback
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            StaticToast.show("Authentication failed");
        }

        @Override // androidx.biometric.BiometricPrompt.AuthenticationCallback
        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            if (this.activity.getWindow().getDecorView() != null) {
                this.activity.getWindow().getDecorView().post(this.callback);
            }
        }
    }

    class ActionMenuClickListener implements PopupMenu.OnMenuItemClickListener {
        final /* synthetic */ MenuItem scrollToTopItem;

        final /* synthetic */ MenuItem contextualChipsItem;
        final /* synthetic */ MenuItem fullyExpandItem;
        final /* synthetic */ MenuItem resetOrderItem;
        final /* synthetic */ MenuItem hideReadItem;

        final /* synthetic */ MenuItem savedItem;

        final /* synthetic */ MenuItem profileItem;
        final /* synthetic */ MenuItem submitItem;
        final /* synthetic */ MenuItem viewItem;

        final /* synthetic */ MenuItem randomItem;
        final /* synthetic */ MenuItem randomNsfwItem;
        final /* synthetic */ MenuItem darkModeItem;

        final /* synthetic */ MenuItem friendsItem;

        final /* synthetic */ MenuItem settingsItem;
        final /* synthetic */ MenuItem dataSavingItem;
        final /* synthetic */ MenuItem filtersItem;
        final /* synthetic */ MenuItem recentlyViewedItem;

        final /* synthetic */ MenuItem toggleAccountItem;

        final /* synthetic */ MenuItem refreshItem;

        final /* synthetic */ MenuItem aboutItem;
        final /* synthetic */ MenuItem syncItem;

        final /* synthetic */ MenuItem searchItem;
        final /* synthetic */ MenuItem watchedItem;
        final /* synthetic */ MenuItem swipeItem;
        final /* synthetic */ MenuItem messagingItem;

        final /* synthetic */ MenuItem subredditsItem;
        final /* synthetic */ MenuItem darkOverlayItem;

        ActionMenuClickListener(MenuItem menuItem, MenuItem menuItem2, MenuItem menuItem3, MenuItem menuItem4, MenuItem menuItem5, MenuItem menuItem6, MenuItem menuItem7, MenuItem menuItem8, MenuItem menuItem9, MenuItem menuItem10, MenuItem menuItem11, MenuItem menuItem12, MenuItem menuItem13, MenuItem menuItem14, MenuItem menuItem15, MenuItem menuItem16, MenuItem menuItem17, MenuItem menuItem18, MenuItem menuItem19, MenuItem menuItem20, MenuItem menuItem21, MenuItem menuItem22, MenuItem menuItem23, MenuItem menuItem24, MenuItem menuItem25, MenuItem menuItem26, MenuItem menuItem27) {
            this.contextualChipsItem = menuItem;
            this.fullyExpandItem = menuItem2;
            this.resetOrderItem = menuItem3;
            this.hideReadItem = menuItem4;
            this.savedItem = menuItem5;
            this.profileItem = menuItem6;
            this.submitItem = menuItem7;
            this.viewItem = menuItem8;
            this.randomItem = menuItem9;
            this.randomNsfwItem = menuItem10;
            this.darkModeItem = menuItem11;
            this.friendsItem = menuItem12;
            this.settingsItem = menuItem13;
            this.dataSavingItem = menuItem14;
            this.filtersItem = menuItem15;
            this.recentlyViewedItem = menuItem16;
            this.toggleAccountItem = menuItem17;
            this.refreshItem = menuItem18;
            this.aboutItem = menuItem19;
            this.syncItem = menuItem20;
            this.searchItem = menuItem21;
            this.watchedItem = menuItem22;
            this.swipeItem = menuItem23;
            this.messagingItem = menuItem24;
            this.subredditsItem = menuItem25;
            this.darkOverlayItem = menuItem26;
            this.scrollToTopItem = menuItem27;
        }

        @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.equals(this.contextualChipsItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsContextualChips;
                SettingsSingleton.getSettings().actionsContextualChips = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_contextual_chips", enabled);
                ActionsBottomSheetFragment.this.updateContextualChips();
                return true;
            }
            if (menuItem.equals(this.fullyExpandItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsFullyExpand;
                SettingsSingleton.getSettings().actionsFullyExpand = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_fully_expand", enabled);
                return true;
            }
            if (menuItem.equals(this.resetOrderItem)) {
                ActionsManager.getInstance().reset();
                ActionsBottomSheetFragment.this.recyclerView.getAdapter().notifyDataSetChanged();
                return true;
            }
            if (menuItem.equals(this.hideReadItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsHideRead;
                SettingsSingleton.getSettings().actionsHideRead = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_hide_read", enabled);
            } else if (menuItem.equals(this.savedItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsSaved;
                SettingsSingleton.getSettings().actionsSaved = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_saved", enabled);
            } else if (menuItem.equals(this.profileItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsProfile;
                SettingsSingleton.getSettings().actionsProfile = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_profile", enabled);
            } else if (menuItem.equals(this.submitItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsSubmit;
                SettingsSingleton.getSettings().actionsSubmit = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_submit", enabled);
            } else if (menuItem.equals(this.viewItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsView;
                SettingsSingleton.getSettings().actionsView = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_view", enabled);
            } else if (menuItem.equals(this.randomItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsRandom;
                SettingsSingleton.getSettings().actionsRandom = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_random", enabled);
            } else if (menuItem.equals(this.randomNsfwItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsRandomNsfw;
                SettingsSingleton.getSettings().actionsRandomNsfw = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_random_nsfw", enabled);
            } else if (menuItem.equals(this.darkModeItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsDarkMode;
                SettingsSingleton.getSettings().actionsDarkMode = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_dark", enabled);
            } else if (menuItem.equals(this.friendsItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsFriends;
                SettingsSingleton.getSettings().actionsFriends = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_friends", enabled);
            } else if (menuItem.equals(this.settingsItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsSettings;
                SettingsSingleton.getSettings().actionsSettings = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_settings", enabled);
            } else if (menuItem.equals(this.dataSavingItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsDataSaving;
                SettingsSingleton.getSettings().actionsDataSaving = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_data_saving", enabled);
            } else if (menuItem.equals(this.filtersItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsFilters;
                SettingsSingleton.getSettings().actionsFilters = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_filters", enabled);
            } else if (menuItem.equals(this.recentlyViewedItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsRecentlyViewed;
                SettingsSingleton.getSettings().actionsRecentlyViewed = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_recently_viewed", enabled);
            } else if (menuItem.equals(this.toggleAccountItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsToggleAccount;
                SettingsSingleton.getSettings().actionsToggleAccount = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_toggle_account", enabled);
            } else if (menuItem.equals(this.refreshItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsRefresh;
                SettingsSingleton.getSettings().actionsRefresh = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_refresh", enabled);
            } else if (menuItem.equals(this.aboutItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsAbout;
                SettingsSingleton.getSettings().actionsAbout = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_about", enabled);
            } else if (menuItem.equals(this.syncItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsSync;
                SettingsSingleton.getSettings().actionsSync = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_sync", enabled);
            } else if (menuItem.equals(this.searchItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsSearch;
                SettingsSingleton.getSettings().actionsSearch = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_search", enabled);
            } else if (menuItem.equals(this.watchedItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsWatched;
                SettingsSingleton.getSettings().actionsWatched = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_watched", enabled);
            } else if (menuItem.equals(this.swipeItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsSwipe;
                SettingsSingleton.getSettings().actionsSwipe = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_swipe", enabled);
            } else if (menuItem.equals(this.messagingItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsMessaging;
                SettingsSingleton.getSettings().actionsMessaging = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_messaging", enabled);
            } else if (menuItem.equals(this.subredditsItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsSubreddits;
                SettingsSingleton.getSettings().actionsSubreddits = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_subreddits", enabled);
            } else if (menuItem.equals(this.darkOverlayItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsDarkOverlay;
                SettingsSingleton.getSettings().actionsDarkOverlay = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_dark_overlay", enabled);
            } else if (menuItem.equals(this.scrollToTopItem)) {
                boolean enabled = !SettingsSingleton.getSettings().actionsScrollToTop;
                SettingsSingleton.getSettings().actionsScrollToTop = enabled;
                SettingsSingleton.getInstance().putBoolean("actions_scroll_top", enabled);
            }
            ActionsManager.getInstance().refreshFromSettings();
            ActionsBottomSheetFragment.this.recyclerView.getAdapter().notifyDataSetChanged();
            return true;
        }
    }

    public class ActionHolder extends RecyclerView.ViewHolder {

        private int actionId;

        public ActionHolder(View view) {
            super(view);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void onActionClicked(int actionId, View view) {
            ActionsBottomSheetFragment.performAction(ActionsBottomSheetFragment.this.getActivity(), ActionsBottomSheetFragment.this.getParentFragmentManager(), ActionsBottomSheetFragment.this.getSubreddit(), actionId, ActionsBottomSheetFragment.this);
        }

        public void bind(final int actionId) {
            this.actionId = actionId;
            boolean selected = true;
            ((MoreActionsItemView) this.itemView).setEnabled(true);
            if (actionId == 14 && !ActionsBottomSheetFragment.this.hasSubreddit()) {
                ((MoreActionsItemView) this.itemView).setEnabled(false);
            }
            ((MoreActionsItemView) this.itemView).setLabel(ActionsManager.getLabel(actionId));
            ((MoreActionsItemView) this.itemView).setIcon(ActionsManager.getIconRes(actionId));
            if (actionId == 7) {
                MoreActionsItemView moreActionsItemView = (MoreActionsItemView) this.itemView;
                if (!NightHelper.isNight() && !NightHelper.isFollowSystem()) {
                    selected = false;
                }
                moreActionsItemView.setSelected(selected);
            } else if (actionId == 10) {
                ((MoreActionsItemView) this.itemView).setSelected(DataSavingHelper.isFullDataSaving());
            } else if (actionId == 23) {
                ((MoreActionsItemView) this.itemView).setSelected(SettingsSingleton.getSettings().darkOverlay);
            } else {
                ((MoreActionsItemView) this.itemView).setSelected(false);
            }
            this.itemView.setOnClickListener(new View.OnClickListener() { // from class: s9.a
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ActionsBottomSheetFragment.ActionHolder.this.onActionClicked(actionId, view);
                }
            });
        }

        public int getActionId() {
            return this.actionId;
        }
    }

    class ActionsAdapter extends RecyclerView.Adapter<ActionHolder> implements ActionItemMoveCallback.Callback {
        public ActionsAdapter() {
            setHasStableIds(true);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: N, reason: merged with bridge method [inline-methods] */
        public void onBindViewHolder(ActionHolder holder, int position) {
            holder.bind(ActionsManager.getInstance().getActions().get(position).intValue());
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: O, reason: merged with bridge method [inline-methods] */
        public ActionHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            return ActionsBottomSheetFragment.this.new ActionHolder(new MoreActionsItemView(viewGroup.getContext()));
        }

        @Override // com.resync.app.widgets.recycler.ActionItemMoveCallback.Callback
        public void onItemMove(int fromAction, int toAction) {
            Logger.verbose("blep", "First action: " + fromAction);
            Logger.verbose("blep", "Second action: " + toAction);
            int fromIndex = ActionsManager.getInstance().getActions().indexOf(Integer.valueOf(fromAction));
            Logger.verbose("blep", "Start pos: " + fromIndex);
            int toIndex = ActionsManager.getInstance().getActions().indexOf(Integer.valueOf(toAction));
            ActionsManager.getInstance().getActions().remove(fromIndex);
            ActionsManager.getInstance().getActions().add(toIndex, Integer.valueOf(fromAction));
            int newIndex = ActionsManager.getInstance().getActions().indexOf(Integer.valueOf(fromAction));
            Logger.verbose("blep", "End pos: " + newIndex);
            ActionsManager.getInstance().save();
            notifyItemMoved(fromIndex, newIndex);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: n */
        public int getItemCount() {
            return ActionsManager.getInstance().getActions().size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* JADX INFO: renamed from: o */
        public long getItemId(int position) {
            return ActionsManager.getInstance().getActions().get(position).intValue();
        }
    }

    private void setupExpandedOffset() {
        MoreActionsItemView moreActionsItemView = new MoreActionsItemView(getContext());
        moreActionsItemView.setIcon(R.drawable.outline_star_outline_24);
        moreActionsItemView.setLabel("Example");
        moreActionsItemView.measure(0, 0);
        ProductSansTextView productSansTextView = new ProductSansTextView(getContext());
        productSansTextView.setTextSize(1, 18.0f);
        productSansTextView.setText("Actions");
        productSansTextView.measure(0, 0);
        this.bottomSheetBehavior.setExpandedOffset((moreActionsItemView.getMeasuredHeight() / 3) + moreActionsItemView.getMeasuredHeight() + moreActionsItemView.getMeasuredHeight() + productSansTextView.getMeasuredHeight() + productSansTextView.getMeasuredHeight() + SizeHelper.dpToPx(40) + SizeHelper.dpToPx(1));
    }

    public static void runWithBiometricLock(FragmentActivity fragmentActivity, String str, Runnable runnable) {
        if (PreferencesHelper.getAccountPrefsForUser(fragmentActivity, str, "DrawerFragment").getBoolean("biometric", false)) {
            new BiometricPrompt(fragmentActivity, ContextCompat.getMainExecutor(fragmentActivity), new BiometricAuthCallback(fragmentActivity, runnable)).authenticate(new BiometricPrompt.PromptInfo.Builder().setTitle("Biometric account lock").setConfirmationRequired(true).setDeviceCredentialAllowed(false).build());
        } else {
            fragmentActivity.getWindow().getDecorView().post(runnable);
        }
    }

    public static void dismissExisting(Context context) {
        FragmentManager fragmentManagerG = ContextHelper.getFragmentManager(context);
        if (fragmentManagerG != null) {
            Fragment fragmentJ0 = fragmentManagerG.findFragmentByTag(ActionsBottomSheetFragment.class.getSimpleName());
            if (fragmentJ0 instanceof ActionsBottomSheetFragment) {
                fragmentManagerG.beginTransaction().remove(fragmentJ0).commit();
            }
        }
    }

    public static Bundle buildArgs(String str, boolean expanded) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(FragmentUtils.keySubreddit(), str);
        bundle.putSerializable("expanded", Boolean.FALSE);
        bundle.putSerializable("first_time", Boolean.valueOf(SettingsSingleton.getSettings().first_time_full_height));
        return bundle;
    }

    public static void performAction(FragmentActivity fragmentActivity, FragmentManager fragmentManager, String str, int actionCode, ActionsBottomSheetFragment actionsBottomSheetFragment) {
        if (actionCode == 0) {
            BusSingleton.getBus().post(new HideReadEvent());
        } else if (actionCode == 1) {
            if (!AccountSingleton.getInstance().isLoggedIn()) {
                StaticToast.show(fragmentActivity, R.string.common_generic_error_logged_out);
                return;
            }
            CasualActivity.openSaved(fragmentActivity, AccountSingleton.getInstance().getUsername());
        } else if (actionCode == 2) {
            if (!AccountSingleton.getInstance().isLoggedIn()) {
                StaticToast.show(fragmentActivity, R.string.common_generic_error_logged_out);
                return;
            }
            CasualActivity.openUser(fragmentActivity, AccountSingleton.getInstance().getUsername());
        } else if (actionCode == 3) {
            if (!AccountSingleton.getInstance().isLoggedIn()) {
                StaticToast.show(fragmentActivity, R.string.common_generic_error_logged_out);
                return;
            } else if (StringUtils.isEmpty(str)) {
                FragmentUtils.show(SubmitSelectionDialogBottomSheet.class, fragmentManager);
            } else {
                FragmentUtils.showWithSubreddit(SubmitSelectionDialogBottomSheet.class, fragmentManager, str);
            }
        } else if (actionCode == 4) {
            FragmentUtils.show(ChangeViewSelectionDialogBottomSheet.class, fragmentManager);
        } else if (actionCode == 5) {
            BusSingleton.getBus().post(new SubredditSelectedEvent("random", false));
        } else if (actionCode == 6) {
            BusSingleton.getBus().post(new SubredditSelectedEvent("random_nsfw", false));
        } else if (actionCode == 7) {
            if (!NightHelper.isNight() && !NightHelper.isFollowSystem()) {
                SettingsSingleton.getInstance().putBoolean("night_mode_follow", false);
                SettingsSingleton.getInstance().putBoolean("night_mode_force_off", false);
                SettingsSingleton.getInstance().putBoolean("night_mode_force_on", true);
                SettingsSingleton.getInstance().putBoolean("night_mode_auto", false);
            } else if (!NightHelper.isNight() || NightHelper.isFollowSystem()) {
                SettingsSingleton.getInstance().putBoolean("night_mode_follow", false);
                SettingsSingleton.getInstance().putBoolean("night_mode_force_off", true);
                SettingsSingleton.getInstance().putBoolean("night_mode_force_on", false);
                SettingsSingleton.getInstance().putBoolean("night_mode_auto", false);
            } else {
                StaticToast.show("Automatic dark mode");
                SettingsSingleton.getInstance().putBoolean("night_mode_follow", true);
                SettingsSingleton.getInstance().putBoolean("night_mode_force_off", false);
                SettingsSingleton.getInstance().putBoolean("night_mode_force_on", false);
                SettingsSingleton.getInstance().putBoolean("night_mode_auto", false);
            }
            NightHelper.refresh(true);
        } else if (actionCode == 8) {
            if (!AccountSingleton.getInstance().isLoggedIn()) {
                StaticToast.show(fragmentActivity, R.string.common_generic_error_logged_out);
                return;
            }
            BusSingleton.getBus().post(new SubredditSelectedEvent("Friends", true));
        } else if (actionCode == 9) {
            fragmentActivity.startActivity(new Intent(fragmentActivity, (Class<?>) PreferencesActivity.class));
        } else if (actionCode == 10) {
            if (DataSavingHelper.isFullDataSaving()) {
                SettingsSingleton.getInstance().enableDataSaving();
            } else {
                SettingsSingleton.getInstance().enableFullDataSaving();
            }
            SingletonManager.reloadAll(false);
            ActivityHelper.restartToMain(fragmentActivity);
        } else if (actionCode == 11) {
            Intent intent = new Intent(fragmentActivity, (Class<?>) PreferencesActivity.class);
            intent.putExtra("mode", RedditApplication.getInteger(R.integer.FILTERS));
            fragmentActivity.startActivity(intent);
        } else if (actionCode == 12) {
            if (RecentHelper.getRecent().size() == 0) {
                StaticToast.show("You haven't read any posts recently!");
            } else {
                FragmentUtils.show(RecentlyViewedSelectionDialogBottomSheet.class, fragmentManager);
            }
        } else if (actionCode == 13) {
            CursorLoader cursorLoader = new CursorLoader(fragmentActivity, RedditProvider.URI_ACCOUNTS, new String[]{"account_name", "account_refresh_token"}, null, null, null);
            cursorLoader.registerListener(0, new AccountSwitchLoaderListener(fragmentActivity, cursorLoader));
            cursorLoader.startLoading();
        } else if (actionCode == 15) {
            BusSingleton.getBus().post(new RefreshEvent());
        } else if (actionCode == 17) {
            Pair<String, String> pairB = PerSubSortHelper.getSortPair(str);
            SyncManager.showSyncDialog(fragmentActivity, (String) pairB.second, (String) pairB.first, str);
        } else if (actionCode == 18) {
            FragmentUtils.showWithArguments(SubredditsBottomSheetFragment.class, ContextHelper.getFragmentManager(fragmentActivity), SubredditsBottomSheetFragment.buildArgs());
        } else if (actionCode == 19) {
            CasualActivity.openWatching(fragmentActivity);
        } else if (actionCode == 20) {
            SwipeActivity.start(fragmentActivity, str);
        } else if (actionCode == 21) {
            MessagingActivity.start(fragmentActivity);
        } else if (actionCode == 22) {
            FragmentUtils.showWithSubreddit(SubredditsBottomSheetFragment.class, fragmentActivity.getSupportFragmentManager(), str);
        } else if (actionCode == 23) {
            SettingsSingleton.getInstance().putBoolean("dark_overlay", !SettingsSingleton.getSettings().darkOverlay);
            fragmentActivity.recreate();
        } else if (actionCode == 16) {
            ContextHelper.openAbout(fragmentActivity, str);
        } else if (actionCode == 24) {
            BusSingleton.getBus().post(new ScrollToTopOfPostsEvent());
        } else if (actionCode == 25) {
            // Sign in — native credential form driving a hidden reddit.com login WebView
            // (NativeLoginFragment). Replaces Sync's dead OAuth-app flow. OAuthLoginFragment
            // remains as the all-WebView implementation if ever needed.
            com.resync.app.auth.NativeLoginFragment.newInstance().show(fragmentActivity.getSupportFragmentManager(), null);
        } else if (actionCode == 26) {
            // Chat — reddit chat over Matrix (matrix.redditspace.com), reusing the web-session bearer.
            com.resync.app.ui.activities.ChatRoomsActivity.start(fragmentActivity);
        }
        if (actionsBottomSheetFragment != null) {
            actionsBottomSheetFragment.dismiss();
        }
    }

    public static void performFirstAction(Context context) {
        if (ActionsManager.getInstance().getActions().size() < 1) {
            StaticToast.show("You need at least one action for this to work");
            return;
        }
        int iIntValue = ActionsManager.getInstance().getActions().get(0).intValue();
        performAction(ContextHelper.getBaseActivity(context), ContextHelper.getFragmentManager(context), ContextHelper.getCurrentSubredditName(context), iIntValue, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateContextualChips() {
        if (SettingsSingleton.getSettings().actionsContextualChips || !SettingsSingleton.getSettings().toolbarExpanded) {
            this.contextualChipsDropInView.setVisibility(0);
        } else {
            this.contextualChipsDropInView.setVisibility(8);
        }
    }

    public static void show(Context context) {
        FragmentUtils.showWithArguments(ActionsBottomSheetFragment.class, ContextHelper.getFragmentManager(context), buildArgs(ContextHelper.getCurrentSubredditName(context), true));
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public final boolean shouldPadTopSheet() {
        return false;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isInitiallyExpanded() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isTopSheet() {
        return this.topSheet;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.firstTime = getArguments().getBoolean("first_time");
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    protected void onSheetReady() {
        if (isDialogShowing()) {
            setupExpandedOffset();
        }
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.dialog_fragment_actions_bottom_sheet;
    }

    @Override // androidx.fragment.app.c, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        ActionsAdapter adapter = new ActionsAdapter();
        this.recyclerView.setAdapter(adapter);
        new ItemTouchHelper(new ActionItemMoveCallback(adapter)).attachToRecyclerView(this.recyclerView);
        updateContextualChips();
        this.headerView.setDragButtonVisible(!isTopSheet());
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    protected boolean dismissWithAnimation() {
        return false;
    }

    @Subscribe
    public void onClose(CloseActionsBottomsheetEvent closeActionsBottomsheetEvent) {
        dismiss();
    }

    @OnClick
    public void showMore(View view) {
        PopupMenu popupMenuC = PopupMenuHelper.create(view, 5);
        SubMenu subMenuAddSubMenu = popupMenuC.getMenu().addSubMenu("Action visibility");
        MenuItem menuItemAdd = popupMenuC.getMenu().add("Contextual chips");
        menuItemAdd.setCheckable(true);
        menuItemAdd.setChecked(SettingsSingleton.getSettings().actionsContextualChips);
        if (!SettingsSingleton.getSettings().toolbarExpanded) {
            menuItemAdd.setVisible(false);
        }
        MenuItem menuItemAdd2 = subMenuAddSubMenu.add("About");
        menuItemAdd2.setCheckable(true);
        menuItemAdd2.setChecked(SettingsSingleton.getSettings().actionsAbout);
        MenuItem menuItemAdd3 = subMenuAddSubMenu.add("Dark overlay");
        menuItemAdd3.setCheckable(true);
        menuItemAdd3.setChecked(SettingsSingleton.getSettings().actionsDarkOverlay);
        MenuItem menuItemAdd4 = subMenuAddSubMenu.add("Dark mode");
        menuItemAdd4.setCheckable(true);
        menuItemAdd4.setChecked(SettingsSingleton.getSettings().actionsDarkMode);
        MenuItem menuItemAdd5 = subMenuAddSubMenu.add("Data saving");
        menuItemAdd5.setCheckable(true);
        menuItemAdd5.setChecked(SettingsSingleton.getSettings().actionsDataSaving);
        MenuItem menuItemAdd6 = subMenuAddSubMenu.add("Explore");
        menuItemAdd6.setCheckable(true);
        menuItemAdd6.setChecked(SettingsSingleton.getSettings().actionsSubreddits);
        MenuItem menuItemAdd7 = popupMenuC.getMenu().add("Expand on open");
        menuItemAdd7.setCheckable(true);
        menuItemAdd7.setChecked(SettingsSingleton.getSettings().actionsFullyExpand);
        menuItemAdd7.setVisible(false);
        MenuItem menuItemAdd8 = subMenuAddSubMenu.add("Filters");
        menuItemAdd8.setCheckable(true);
        menuItemAdd8.setChecked(SettingsSingleton.getSettings().actionsFilters);
        MenuItem menuItemAdd9 = subMenuAddSubMenu.add("Friends");
        menuItemAdd9.setCheckable(true);
        menuItemAdd9.setChecked(SettingsSingleton.getSettings().actionsFriends);
        MenuItem menuItemAdd10 = subMenuAddSubMenu.add("Hide read");
        menuItemAdd10.setCheckable(true);
        menuItemAdd10.setChecked(SettingsSingleton.getSettings().actionsHideRead);
        MenuItem menuItemAdd11 = subMenuAddSubMenu.add("Inbox");
        menuItemAdd11.setCheckable(true);
        menuItemAdd11.setChecked(SettingsSingleton.getSettings().actionsMessaging);
        MenuItem menuItemAdd12 = subMenuAddSubMenu.add("Profile");
        menuItemAdd12.setCheckable(true);
        menuItemAdd12.setChecked(SettingsSingleton.getSettings().actionsProfile);
        MenuItem menuItemAdd13 = subMenuAddSubMenu.add("Random");
        menuItemAdd13.setCheckable(true);
        menuItemAdd13.setChecked(SettingsSingleton.getSettings().actionsRandom);
        MenuItem menuItemAdd14 = subMenuAddSubMenu.add("Random nsfw");
        menuItemAdd14.setCheckable(true);
        menuItemAdd14.setChecked(SettingsSingleton.getSettings().actionsRandomNsfw);
        menuItemAdd14.setVisible(!SettingsSingleton.getSettings().nsfw);
        MenuItem menuItemAdd15 = subMenuAddSubMenu.add("Recently viewed");
        menuItemAdd15.setCheckable(true);
        menuItemAdd15.setChecked(SettingsSingleton.getSettings().actionsRecentlyViewed);
        MenuItem menuItemAdd16 = subMenuAddSubMenu.add("Refresh");
        menuItemAdd16.setCheckable(true);
        menuItemAdd16.setChecked(SettingsSingleton.getSettings().actionsRefresh);
        MenuItem menuItemAdd17 = subMenuAddSubMenu.add("Saved");
        menuItemAdd17.setCheckable(true);
        menuItemAdd17.setChecked(SettingsSingleton.getSettings().actionsSaved);
        MenuItem menuItemAdd18 = subMenuAddSubMenu.add("Scroll to top");
        menuItemAdd18.setCheckable(true);
        menuItemAdd18.setChecked(SettingsSingleton.getSettings().actionsScrollToTop);
        MenuItem menuItemAdd19 = subMenuAddSubMenu.add("Search");
        menuItemAdd19.setCheckable(true);
        menuItemAdd19.setChecked(SettingsSingleton.getSettings().actionsSearch);
        MenuItem menuItemAdd20 = subMenuAddSubMenu.add("Settings");
        menuItemAdd20.setCheckable(true);
        menuItemAdd20.setChecked(SettingsSingleton.getSettings().actionsSettings);
        MenuItem menuItemAdd21 = subMenuAddSubMenu.add("Submit");
        menuItemAdd21.setCheckable(true);
        menuItemAdd21.setChecked(SettingsSingleton.getSettings().actionsSubmit);
        MenuItem menuItemAdd22 = subMenuAddSubMenu.add("Sync");
        menuItemAdd22.setCheckable(true);
        menuItemAdd22.setChecked(SettingsSingleton.getSettings().actionsSync);
        MenuItem menuItemAdd23 = subMenuAddSubMenu.add("Swipe mode");
        menuItemAdd23.setCheckable(true);
        menuItemAdd23.setChecked(SettingsSingleton.getSettings().actionsSwipe);
        MenuItem menuItemAdd24 = subMenuAddSubMenu.add("Toggle account");
        menuItemAdd24.setCheckable(true);
        menuItemAdd24.setChecked(SettingsSingleton.getSettings().actionsToggleAccount);
        MenuItem menuItemAdd25 = subMenuAddSubMenu.add("View");
        menuItemAdd25.setCheckable(true);
        menuItemAdd25.setChecked(SettingsSingleton.getSettings().actionsView);
        MenuItem menuItemAdd26 = subMenuAddSubMenu.add("Watched");
        menuItemAdd26.setCheckable(true);
        menuItemAdd26.setChecked(SettingsSingleton.getSettings().actionsWatched);
        popupMenuC.setOnMenuItemClickListener(new ActionMenuClickListener(menuItemAdd, menuItemAdd7, popupMenuC.getMenu().add("Reset order"), menuItemAdd10, menuItemAdd17, menuItemAdd12, menuItemAdd21, menuItemAdd25, menuItemAdd13, menuItemAdd14, menuItemAdd4, menuItemAdd9, menuItemAdd20, menuItemAdd5, menuItemAdd8, menuItemAdd15, menuItemAdd24, menuItemAdd16, menuItemAdd2, menuItemAdd22, menuItemAdd19, menuItemAdd26, menuItemAdd23, menuItemAdd11, menuItemAdd6, menuItemAdd3, menuItemAdd18));
        popupMenuC.show();
    }

    boolean hasSubreddit() {
        return StringUtils.isNotEmpty(getSubreddit());
    }
}
