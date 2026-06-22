package com.resync.app.dialogs;

import android.os.Bundle;
import android.view.View;
import com.resync.app.R;
import com.resync.app.ui.views.text.spannable.children.HtmlTextView;

/* JADX INFO: renamed from: s9.j */
/* JADX INFO: compiled from: PreviewCommentBottomSheetFragment.java */
/* JADX INFO: loaded from: classes.dex */
public class PreviewCommentBottomSheetFragment extends CustomBottomSheetDialogFragment {
    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isInitiallyExpanded() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.fragment_bottomsheet_preview_comment;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        HtmlTextView htmlTextView = (HtmlTextView) view.findViewById(R.id.html);
        htmlTextView.setHtml(getSubreddit());
        showKeyboard(htmlTextView);
    }
}
