package com.resync.app.ui.views.accounts;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.ui.views.buttons.CustomAppCompatButton;
import com.resync.app.ui.views.core.SubView;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: loaded from: classes3.dex */
public class AccountRow extends RelativeLayout {

    SubView mIcon;

    CustomAppCompatButton mRemoveIcon;

    TextView mTitle;

    public AccountRow(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        RelativeLayout.inflate(context, R.layout.view_account_row, this);
        ButterKnife.bind(this);
        int horizontalPadding = SizeHelper.dpToPx(16);
        int verticalPadding = SizeHelper.dpToPx(8);
        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
        setGravity(16);
        setBackgroundResource(R.drawable.button_light);
        setClickable(true);
    }

    public void setRemoveClickListener(View.OnClickListener onClickListener) {
        this.mRemoveIcon.setOnClickListener(onClickListener);
    }

    public void hideRemoveIcon() {
        this.mRemoveIcon.setVisibility(8);
    }

    public void setTitle(String str) {
        this.mTitle.setText(str);
    }

    public void setUsername(String str) {
        this.mIcon.setUsername(str);
    }
}
