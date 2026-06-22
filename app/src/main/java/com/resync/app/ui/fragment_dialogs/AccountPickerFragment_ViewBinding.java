package com.resync.app.ui.fragment_dialogs;

import android.view.View;
import android.widget.LinearLayout;
import androidx.cardview.widget.CardView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.buttons.CustomAppCompatButton;
import com.resync.app.ui.views.core.MaterialRow;
import com.resync.app.ui.views.core.SubView;
import com.resync.app.ui.views.text.basic.ProductSansTextView;
import com.resync.app.ui.views.text.basic.RobotoTextView;

/* JADX INFO: loaded from: classes.dex */
public class AccountPickerFragment_ViewBinding implements Unbinder {
    private AccountPickerFragment target;
    private View fragmentAccountPickerRemoveClickView;
    private View fragmentAccountPickerProfileClickView;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private View view1;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private View view2;
    private View fragmentAccountPickerModClickView;
    private View fragmentAccountPickerSettingsClickView;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private View view3;

    class ClickListener0 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ AccountPickerFragment target0;

        ClickListener0(AccountPickerFragment accountPickerFragment) {
            this.target0 = accountPickerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target0.onRemoveCurrentAccountClicked();
        }
    }

    class ClickListener1 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ AccountPickerFragment target1;

        ClickListener1(AccountPickerFragment accountPickerFragment) {
            this.target1 = accountPickerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target1.onProfileCLicked();
        }
    }

    class ClickListener2 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ AccountPickerFragment target2;

        ClickListener2(AccountPickerFragment accountPickerFragment) {
            this.target2 = accountPickerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target2.onUpgradeClicked();
        }
    }

    class ClickListener3 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ AccountPickerFragment target3;

        ClickListener3(AccountPickerFragment accountPickerFragment) {
            this.target3 = accountPickerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target3.onSyncUltraClicked();
        }
    }

    class ClickListener4 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ AccountPickerFragment target4;

        ClickListener4(AccountPickerFragment accountPickerFragment) {
            this.target4 = accountPickerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target4.onModClicked();
        }
    }

    class ClickListener5 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ AccountPickerFragment target5;

        ClickListener5(AccountPickerFragment accountPickerFragment) {
            this.target5 = accountPickerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target5.onSettingsClicked();
        }
    }

    class ClickListener6 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ AccountPickerFragment target6;

        ClickListener6(AccountPickerFragment accountPickerFragment) {
            this.target6 = accountPickerFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target6.onAddAccountClicked();
        }
    }

    public AccountPickerFragment_ViewBinding(AccountPickerFragment accountPickerFragment, View view) {
        this.target = accountPickerFragment;
        accountPickerFragment.mBaseCard = (CardView) Utils.findRequiredViewAsType(view, R.id.fragment_account_picker_card, "field 'mBaseCard'", CardView.class);
        accountPickerFragment.mInfo = (RobotoTextView) Utils.findRequiredViewAsType(view, R.id.fragment_account_picker_info, "field 'mInfo'", RobotoTextView.class);
        accountPickerFragment.mAccountsWrapper = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.fragment_account_picker_accounts_wrapper, "field 'mAccountsWrapper'", LinearLayout.class);
        accountPickerFragment.mUsernameTextView = (ProductSansTextView) Utils.findRequiredViewAsType(view, R.id.fragment_account_picker_username, "field 'mUsernameTextView'", ProductSansTextView.class);
        accountPickerFragment.mThumbnail = (SubView) Utils.findRequiredViewAsType(view, R.id.fragment_account_picker_thumbnail, "field 'mThumbnail'", SubView.class);
        View viewC = Utils.findRequiredView(view, R.id.fragment_account_picker_remove, "field 'mRemoveButton' and method 'onRemoveCurrentAccountClicked'");
        accountPickerFragment.mRemoveButton = (CustomAppCompatButton) Utils.castView(viewC, R.id.fragment_account_picker_remove, "field 'mRemoveButton'", CustomAppCompatButton.class);
        this.fragmentAccountPickerRemoveClickView = viewC;
        viewC.setOnClickListener(new ClickListener0(accountPickerFragment));
        View viewC2 = Utils.findRequiredView(view, R.id.fragment_account_picker_profile, "field 'mProfileRow' and method 'onProfileCLicked'");
        accountPickerFragment.mProfileRow = (MaterialRow) Utils.castView(viewC2, R.id.fragment_account_picker_profile, "field 'mProfileRow'", MaterialRow.class);
        this.fragmentAccountPickerProfileClickView = viewC2;
        viewC2.setOnClickListener(new ClickListener1(accountPickerFragment));
        View viewC3 = Utils.findRequiredView(view, R.id.fragment_account_picker_upgrade, "field 'mUpgradeRow' and method 'onUpgradeClicked'");
        accountPickerFragment.mUpgradeRow = (MaterialRow) Utils.castView(viewC3, R.id.fragment_account_picker_upgrade, "field 'mUpgradeRow'", MaterialRow.class);
        this.view1 = viewC3;
        viewC3.setOnClickListener(new ClickListener2(accountPickerFragment));
        View viewC4 = Utils.findRequiredView(view, R.id.fragment_account_picker_ultra, "field 'mSyncUltraRow' and method 'onSyncUltraClicked'");
        accountPickerFragment.mSyncUltraRow = (MaterialRow) Utils.castView(viewC4, R.id.fragment_account_picker_ultra, "field 'mSyncUltraRow'", MaterialRow.class);
        this.view2 = viewC4;
        viewC4.setOnClickListener(new ClickListener3(accountPickerFragment));
        View viewC5 = Utils.findRequiredView(view, R.id.fragment_account_picker_mod, "field 'mModRow' and method 'onModClicked'");
        accountPickerFragment.mModRow = (MaterialRow) Utils.castView(viewC5, R.id.fragment_account_picker_mod, "field 'mModRow'", MaterialRow.class);
        this.fragmentAccountPickerModClickView = viewC5;
        viewC5.setOnClickListener(new ClickListener4(accountPickerFragment));
        View viewC6 = Utils.findRequiredView(view, R.id.fragment_account_picker_settings, "field 'mSettingsRow' and method 'onSettingsClicked'");
        accountPickerFragment.mSettingsRow = (MaterialRow) Utils.castView(viewC6, R.id.fragment_account_picker_settings, "field 'mSettingsRow'", MaterialRow.class);
        this.fragmentAccountPickerSettingsClickView = viewC6;
        viewC6.setOnClickListener(new ClickListener5(accountPickerFragment));
        View viewC7 = Utils.findRequiredView(view, R.id.fragment_account_picker_add_account, "method 'onAddAccountClicked'");
        this.view3 = viewC7;
        viewC7.setOnClickListener(new ClickListener6(accountPickerFragment));
    }

    @Override
    public void unbind() {
        AccountPickerFragment target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.mBaseCard = null;
        target.mInfo = null;
        target.mAccountsWrapper = null;
        target.mUsernameTextView = null;
        target.mThumbnail = null;
        target.mRemoveButton = null;
        target.mProfileRow = null;
        target.mUpgradeRow = null;
        target.mSyncUltraRow = null;
        target.mModRow = null;
        target.mSettingsRow = null;
        this.fragmentAccountPickerRemoveClickView.setOnClickListener(null);
        this.fragmentAccountPickerRemoveClickView = null;
        this.fragmentAccountPickerProfileClickView.setOnClickListener(null);
        this.fragmentAccountPickerProfileClickView = null;
        this.view1.setOnClickListener(null);
        this.view1 = null;
        this.view2.setOnClickListener(null);
        this.view2 = null;
        this.fragmentAccountPickerModClickView.setOnClickListener(null);
        this.fragmentAccountPickerModClickView = null;
        this.fragmentAccountPickerSettingsClickView.setOnClickListener(null);
        this.fragmentAccountPickerSettingsClickView = null;
        this.view3.setOnClickListener(null);
        this.view3 = null;
    }
}
