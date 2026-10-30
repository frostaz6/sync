package com.resync.app.ui.fragment_dialogs;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.biometric.BiometricPrompt;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.resync.app.R;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.activities.ModActivity;
import com.resync.app.ui.activities.PreferencesActivity;
import com.resync.app.ui.views.accounts.AccountRow;
import com.resync.app.ui.views.buttons.CustomAppCompatButton;
import com.resync.app.ui.views.core.MaterialRow;
import com.resync.app.ui.views.core.SubView;
import com.resync.app.ui.views.text.basic.ProductSansTextView;
import com.resync.app.ui.views.text.basic.RobotoTextView;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.ui.TabletHelper;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.managers.ModeratedSubredditManager;
import com.resync.app.events.RemoveAccountEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.singletons.SingletonManager;
import com.resync.app.premium.SyncIapHelper;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ActivityHelper;
import com.resync.app.helpers.PreferencesHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.WindowHelper;
import com.resync.app.dialogs.RemoveAccountAlertDialogBottomSheet;
import com.resync.app.premium.UltraHelper;
import com.resync.app.auth.OAuthLoginFragment;
import com.resync.app.auth.WebSessionAccountStore;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public class AccountPickerFragment extends AppCompatDialogFragment {
    public static final String TAG = AccountPickerFragment.class.getSimpleName();

    @BindView(R.id.fragment_account_picker_accounts_wrapper)
    LinearLayout mAccountsWrapper;

    @BindView(R.id.fragment_account_picker_card)
    CardView mBaseCard;

    @BindView(R.id.fragment_account_picker_info)
    RobotoTextView mInfo;

    @BindView(R.id.fragment_account_picker_mod)
    MaterialRow mModRow;

    @BindView(R.id.fragment_account_picker_profile)
    MaterialRow mProfileRow;

    @BindView(R.id.fragment_account_picker_remove)
    CustomAppCompatButton mRemoveButton;

    @BindView(R.id.fragment_account_picker_settings)
    MaterialRow mSettingsRow;

    @BindView(R.id.fragment_account_picker_ultra)
    MaterialRow mSyncUltraRow;

    @BindView(R.id.fragment_account_picker_thumbnail)
    SubView mThumbnail;

    @BindView(R.id.fragment_account_picker_upgrade)
    MaterialRow mUpgradeRow;

    @BindView(R.id.fragment_account_picker_username)
    ProductSansTextView mUsernameTextView;

    class AccountSwitchLoaderListener implements Loader.OnLoadCompleteListener<Cursor> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ CursorLoader loader;

        /* JADX INFO: renamed from: com.resync.app.ui.fragment_dialogs.AccountPickerFragment$a$a, reason: collision with other inner class name */
        class SwitchAccountRunnable implements Runnable {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ String accountName;
            final /* synthetic */ String refreshToken;

            SwitchAccountRunnable(String str, String str2) {
                this.accountName = str;
                this.refreshToken = str2;
            }

            @Override // java.lang.Runnable
            public void run() {
                if ("Logged out".equalsIgnoreCase(this.accountName)) {
                    AccountSingleton.getInstance().logout();
                    SingletonManager.reloadAll(true);
                    AccountPickerFragment.this.getActivity().getContentResolver().delete(RedditProvider.URI_SWAP_ACCOUNTS, null, null);
                    ActivityHelper.restartToMain(AccountPickerFragment.this.getActivity());
                    return;
                }
                WebSessionAccountStore.switchTo(AccountPickerFragment.this.getActivity(), this.accountName, ok -> {
                    if (!ok) {
                        StaticToast.showWithContext("Session unavailable for " + this.accountName
                                + ". Remove and re-add that account.", AccountPickerFragment.this.getActivity());
                        return;
                    }
                    AccountSingleton.getInstance().updateSession(this.accountName, "web_session", true);
                    SingletonManager.reloadAll(true);
                    AccountPickerFragment.this.getActivity().getContentResolver().delete(RedditProvider.URI_SWAP_ACCOUNTS, null, null);
                    ActivityHelper.restartToMain(AccountPickerFragment.this.getActivity());
                });
            }
        }

        AccountSwitchLoaderListener(CursorLoader cursorLoader) {
            this.loader = cursorLoader;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void onAccountClicked(String str, String str2, View view) {
            AccountPickerFragment.runWithBiometricCheck(AccountPickerFragment.this.getActivity(), AccountPickerFragment.this.getView(), str, new SwitchAccountRunnable(str, str2));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void onRemoveClicked(String str, View view) {
            FragmentUtils.showWithUser(RemoveAccountAlertDialogBottomSheet.class, AccountPickerFragment.this.getParentFragmentManager(), str);
        }

        @Override // android.content.Loader.OnLoadCompleteListener
        /* JADX INFO: renamed from: e, reason: merged with bridge method [inline-methods] */
        public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
            if (AccountPickerFragment.this.getActivity() == null || cursor == null) {
                return;
            }
            AccountPickerFragment.this.mAccountsWrapper.removeAllViews();
            this.loader.unregisterListener(this);
            if (cursor.getCount() == 1) {
                return;
            }
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                final String string = cursor.getString(cursor.getColumnIndex("account_name"));
                final String string2 = cursor.getString(cursor.getColumnIndex("account_refresh_token"));
                if (!StringUtils.equalsIgnoreCase(string, AccountSingleton.getInstance().getUsername())) {
                    AccountRow accountRow = new AccountRow(AccountPickerFragment.this.getContext());
                    accountRow.setTitle(AccountSingleton.obfuscateUsername(string));
                    accountRow.setUsername(string);
                    Logger.verbose("User: " + string);
                    accountRow.setOnClickListener(new View.OnClickListener() { // from class: com.resync.app.ui.fragment_dialogs.b
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            AccountPickerFragment.AccountSwitchLoaderListener.this.onAccountClicked(string, string2, view);
                        }
                    });
                    accountRow.setRemoveClickListener(new View.OnClickListener() { // from class: com.resync.app.ui.fragment_dialogs.a
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            AccountPickerFragment.AccountSwitchLoaderListener.this.onRemoveClicked(string, view);
                        }
                    });
                    if (i == 0) {
                        accountRow.hideRemoveIcon();
                    }
                    LinearLayout linearLayout = AccountPickerFragment.this.mAccountsWrapper;
                    linearLayout.addView(accountRow, linearLayout.getChildCount() - 1);
                }
            }
        }
    }

    static class BiometricAuthCallback extends BiometricPrompt.AuthenticationCallback {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ View view;
        final /* synthetic */ Runnable callback;

        BiometricAuthCallback(View view, Runnable runnable) {
            this.view = view;
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
            View view = this.view;
            if (view != null) {
                view.post(this.callback);
            }
        }
    }

    public static void runWithBiometricCheck(FragmentActivity fragmentActivity, View view, String str, Runnable runnable) {
        if (PreferencesHelper.getAccountPrefsForUser(fragmentActivity, str, "DrawerFragment").getBoolean("biometric", false)) {
            new BiometricPrompt(fragmentActivity, ContextCompat.getMainExecutor(fragmentActivity), new BiometricAuthCallback(view, runnable)).authenticate(new BiometricPrompt.PromptInfo.Builder().setTitle("Biometric account lock").setConfirmationRequired(true).setDeviceCredentialAllowed(false).build());
        } else {
            view.post(runnable);
        }
    }

    private void loadAccounts() {
        CursorLoader cursorLoader = new CursorLoader(getActivity(), RedditProvider.URI_ACCOUNTS, new String[]{"account_name", "account_refresh_token"}, null, null, null);
        cursorLoader.registerListener(0, new AccountSwitchLoaderListener(cursorLoader));
        cursorLoader.startLoading();
    }

    public static void show(FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            String str = TAG;
            if (fragmentManager.findFragmentByTag(str) == null) {
                new AccountPickerFragment().show(fragmentManager, str);
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_account_picker, (ViewGroup) null);
    }

    @Override // androidx.fragment.app.c, androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: m2 */
    public void onStart() {
        super.onStart();
        BusSingleton.getBus().register(this);
        WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
        if (TabletHelper.isTabletMode()) {
            ((ViewGroup.LayoutParams) attributes).width = -2;
            ((ViewGroup.LayoutParams) attributes).height = -2;
        } else {
            ((ViewGroup.LayoutParams) attributes).width = -1;
            if (WindowHelper.isLandscape(getContext())) {
                ((ViewGroup.LayoutParams) attributes).height = -1;
            } else {
                ((ViewGroup.LayoutParams) attributes).height = -2;
            }
        }
        getDialog().getWindow().setAttributes(attributes);
        getDialog().getWindow().getDecorView().setBackgroundColor(0);
        if (WindowHelper.isLandscape(getContext())) {
            return;
        }
        getDialog().getWindow().getDecorView().setPadding(SizeHelper.dpToPx(16), 0, SizeHelper.dpToPx(16), 0);
    }

    @Override // androidx.fragment.app.c, androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: n2 */
    public void onStop() {
        BusSingleton.getBus().unregister(this);
        super.onStop();
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        Window window = getDialog().getWindow();
        window.setGravity(48);
        if (!WindowHelper.isLandscape(getContext())) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.y = WindowHelper.getActionBarHeight(getContext()) + SizeHelper.dpToPx(8);
            window.setAttributes(attributes);
        }
        this.mInfo.setText("Sync for Reddit v23.06.30-13:39");
        this.mBaseCard.setCardBackgroundColor(MonetUtil.getBackgroundColorAuto());
        this.mUsernameTextView.setText(AccountSingleton.getInstance().getDisplayUsername());
        this.mThumbnail.setUsername(AccountSingleton.getInstance().getUsername());
        if (!AccountSingleton.getInstance().isLoggedIn()) {
            this.mProfileRow.setVisibility(8);
            this.mRemoveButton.setVisibility(8);
        }
        this.mSyncUltraRow.setVisibility(UltraHelper.isUltra() ? 8 : 0);
        this.mSyncUltraRow.setTitle("Get Sync Ultra");
        if (ModeratedSubredditManager.getInstance().hasModerated()) {
            this.mModRow.setVisibility(0);
        }
        loadAccounts();
        if (SyncIapHelper.areAdsEnabled(getActivity())) {
            this.mUpgradeRow.setVisibility(0);
        }
    }

    @OnClick
    public void onAddAccountClicked() {
        OAuthLoginFragment.newInstance().show(getActivity().getSupportFragmentManager(), null);
        dismiss();
    }

    @OnClick
    public void onModClicked() {
        ModActivity.start(getContext());
        dismiss();
    }

    @OnClick
    public void onProfileCLicked() {
        com.resync.app.util.links.LinkHandler.openProfile(getActivity(), AccountSingleton.getInstance().getUsername());
        dismiss();
    }

    @Subscribe
    public void onRemoveAccountEvent(RemoveAccountEvent removeAccountEvent) {
        if (!StringUtils.equalsAnyIgnoreCase(removeAccountEvent.username, AccountSingleton.getInstance().getUsername())) {
            WebSessionAccountStore.remove(getActivity(), removeAccountEvent.username);
            getActivity().getContentResolver().delete(RedditProvider.URI_ACCOUNTS, removeAccountEvent.username, null);
            loadAccounts();
            StaticToast.showWithContext("Account removed", getActivity());
        } else {
            WebSessionAccountStore.remove(getActivity(), removeAccountEvent.username);
            getActivity().getContentResolver().delete(RedditProvider.URI_ACCOUNTS, removeAccountEvent.username, null);
            getActivity().getContentResolver().delete(RedditProvider.URI_SWAP_ACCOUNTS, null, null);
            AccountSingleton.getInstance().logout();
            SingletonManager.reloadAll(true);
            ActivityHelper.restartToMain(getActivity());
        }
    }

    @OnClick
    public void onRemoveCurrentAccountClicked() {
        FragmentUtils.showWithUser(RemoveAccountAlertDialogBottomSheet.class, getParentFragmentManager(), AccountSingleton.getInstance().getUsername());
    }

    @OnClick
    public void onSettingsClicked() {
        startActivity(new Intent(getActivity(), (Class<?>) PreferencesActivity.class));
        dismiss();
    }

    @OnClick
    public void onSyncUltraClicked() {
        dismiss();
        Intent intent = new Intent(getActivity(), (Class<?>) PreferencesActivity.class);
        intent.putExtra("mode", getResources().getInteger(R.integer.ULTRA));
        startActivity(intent);
    }

    @OnClick
    public void onUpgradeClicked() {
        // ReSync: in-app purchases removed (Ultra is unlocked for everyone).
        dismiss();
    }
}
