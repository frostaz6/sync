package com.resync.app.ui.views.accounts;

import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.buttons.CustomAppCompatButton;
import com.resync.app.ui.views.core.SubView;

/* JADX INFO: loaded from: classes3.dex */
public class AccountRow_ViewBinding implements Unbinder {
    private AccountRow target;

    public AccountRow_ViewBinding(AccountRow accountRow, View view) {
        this.target = accountRow;
        accountRow.mTitle = (TextView) Utils.findRequiredViewAsType(view, R.id.view_account_row_title, "field 'mTitle'", TextView.class);
        accountRow.mIcon = (SubView) Utils.findRequiredViewAsType(view, R.id.view_account_row_icon, "field 'mIcon'", SubView.class);
        accountRow.mRemoveIcon = (CustomAppCompatButton) Utils.findRequiredViewAsType(view, R.id.view_account_row_remove, "field 'mRemoveIcon'", CustomAppCompatButton.class);
    }

    @Override
    public void unbind() {
        AccountRow accountRow = this.target;
        if (accountRow == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        accountRow.mTitle = null;
        accountRow.mIcon = null;
        accountRow.mRemoveIcon = null;
    }
}
