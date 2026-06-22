package com.resync.app.ui.views.drawer;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentManager;
import com.resync.app.R;
import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.fragment_dialogs.AccountPickerFragment;
import com.resync.app.ui.views.accounts.AccountRow;
import com.resync.app.ui.views.core.MaterialRow;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.events.RemoveAccountEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.singletons.SingletonManager;
import com.resync.app.events.CloseDrawerEvent;
import com.resync.app.events.DrawerClosedEvent;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.ActivityHelper;
import com.resync.app.helpers.AnimationHelper;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.dialogs.RemoveAccountAlertDialogBottomSheet;
import com.resync.app.auth.OAuthLoginFragment;

/* JADX INFO: loaded from: classes3.dex */
public class AccountSectionLinearLayout extends SectionLinearLayout {

    private TextView titleTextView;
    private AppCompatImageView expandIcon;

    class AccountsLoadListener implements Loader.OnLoadCompleteListener<Cursor> {

        final /* synthetic */ CursorLoader loader;

        class SwapAccountRunnable implements Runnable {

            final /* synthetic */ String accountName;
            final /* synthetic */ String refreshToken;

            SwapAccountRunnable(String str, String str2) {
                this.accountName = str;
                this.refreshToken = str2;
            }

            @Override // java.lang.Runnable
            public void run() {
                AccountSingleton.getInstance().updateSession(this.accountName, this.refreshToken, true);
                SingletonManager.reloadAll(true);
                AccountSectionLinearLayout.this.getContext().getContentResolver().delete(RedditProvider.URI_SWAP_ACCOUNTS, null, null);
                ActivityHelper.restartToMain((Activity) AccountSectionLinearLayout.this.getContext());
            }
        }

        AccountsLoadListener(CursorLoader cursorLoader) {
            this.loader = cursorLoader;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void onAccountClicked(AccountRow accountRow, String str, String str2, View view) {
            AccountPickerFragment.runWithBiometricCheck((BaseActivity) AccountSectionLinearLayout.this.getContext(), accountRow, str, new SwapAccountRunnable(str, str2));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void onRemoveClicked(String str, View view) {
            FragmentUtils.showWithUser(RemoveAccountAlertDialogBottomSheet.class, AccountSectionLinearLayout.this.getFragmentManager(), str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void onAddClicked(View view) {
            OAuthLoginFragment.newInstance().show(AccountSectionLinearLayout.this.getFragmentManager(), null);
            BusSingleton.getBus().post(new CloseDrawerEvent());
        }

        @Override // android.content.Loader.OnLoadCompleteListener
        public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
            if (AccountSectionLinearLayout.this.getContext() == null || cursor == null) {
                return;
            }
            this.loader.unregisterListener(this);
            if (cursor.getCount() == 1) {
                AccountSectionLinearLayout.this.titleTextView.setText("Add account");
                AccountSectionLinearLayout.this.expandIcon.setVisibility(8);
                return;
            }
            for (int index = 0; index < cursor.getCount(); index++) {
                cursor.moveToPosition(index);
                final String string = cursor.getString(cursor.getColumnIndex("account_name"));
                final String string2 = cursor.getString(cursor.getColumnIndex("account_refresh_token"));
                if (AccountSingleton.isLoggedIn(AccountSectionLinearLayout.this.getContext()) || !string.equalsIgnoreCase("Logged out")) {
                    final AccountRow accountRow = new AccountRow(AccountSectionLinearLayout.this.getContext());
                    accountRow.setTitle(AccountSingleton.obfuscateUsername(string));
                    accountRow.setUsername(string);
                    accountRow.setOnClickListener(new View.OnClickListener() { // from class: com.resync.app.ui.views.drawer.b
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            AccountsLoadListener.this.onAccountClicked(accountRow, string, string2, view);
                        }
                    });
                    accountRow.setRemoveClickListener(new View.OnClickListener() { // from class: com.resync.app.ui.views.drawer.c
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            AccountsLoadListener.this.onRemoveClicked(string, view);
                        }
                    });
                    if (index == 0) {
                        accountRow.hideRemoveIcon();
                    }
                    AccountSectionLinearLayout.this.addView(accountRow);
                }
            }
            MaterialRow materialRow = new MaterialRow(AccountSectionLinearLayout.this.getContext());
            materialRow.setTitle("Add account");
            materialRow.setIconResource(R.drawable.outline_person_add_24);
            AccountSectionLinearLayout.this.addView(materialRow);
            materialRow.setOnClickListener(new View.OnClickListener() { // from class: com.resync.app.ui.views.drawer.a
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AccountsLoadListener.this.onAddClicked(view);
                }
            });
        }
    }

    public AccountSectionLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public FragmentManager getFragmentManager() {
        return ContextHelper.getFragmentManager(getContext());
    }

    public void reload() {
        removeAllViews();
        CursorLoader cursorLoader = new CursorLoader(getContext(), RedditProvider.URI_ACCOUNTS, new String[]{"account_name", "account_refresh_token"}, null, null, null);
        cursorLoader.registerListener(0, new AccountsLoadListener(cursorLoader));
        cursorLoader.startLoading();
    }

    public void setExpandIcon(AppCompatImageView appCompatImageView) {
        this.expandIcon = appCompatImageView;
    }

    public void setTitleTextView(TextView textView) {
        this.titleTextView = textView;
    }

    @Override // com.resync.app.ui.views.drawer.SectionLinearLayout
    @Subscribe
    public void onDrawerClosed(DrawerClosedEvent drawerClosedEvent) {
        setVisibility(8);
    }

    @Subscribe
    public void onRemoveAccountEvent(RemoveAccountEvent removeAccountEvent) {
        if (StringUtils.equalsAnyIgnoreCase(removeAccountEvent.username, AccountSingleton.getInstance().getUsername())) {
            getContext().getContentResolver().delete(RedditProvider.URI_ACCOUNTS, removeAccountEvent.username, null);
            getContext().getContentResolver().delete(RedditProvider.URI_SWAP_ACCOUNTS, null, null);
            AccountSingleton.getInstance().logout();
            SingletonManager.reloadAll(true);
            ActivityHelper.restartToMain((BaseActivity) getContext());
            return;
        }
        getContext().getContentResolver().delete(RedditProvider.URI_ACCOUNTS, removeAccountEvent.username, null);
        reload();
        AnimationHelper.collapse(this);
        this.expandIcon.animate().setDuration(250L).rotation(0.0f);
        StaticToast.showWithContext("Account removed", getContext());
    }
}
