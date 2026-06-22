package com.resync.app.ui.fragment_dialogs.bottom.material_dialogs;

import android.os.Bundle;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.ui.views.text.spannable.children.HtmlTableView;
import com.resync.app.dialogs.CustomBottomSheetDialogFragment;

/* JADX INFO: loaded from: classes.dex */
public class TableDialogBottomSheet extends CustomBottomSheetDialogFragment {

    @BindView(R.id.fragment_table_tableview)
    HtmlTableView mTextView;

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.fragment_table;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        this.mTextView.setText(getUrl());
    }
}
