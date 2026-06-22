package com.resync.app.dialogs;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.resync.app.color.Palette;
import com.resync.app.R;
import com.resync.app.ui.views.theme_manager.MonetColorSwatchRow;
import com.resync.app.helpers.ui.MonetUtil;
import okhttp3.internal.http.HttpStatusCodesKt;

/* JADX INFO: renamed from: s9.h */
/* JADX INFO: compiled from: MonetBottomSheetFragment.java */
/* JADX INFO: loaded from: classes.dex */
public class MonetBottomSheetFragment extends CustomBottomSheetDialogFragment {
    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean isInitiallyExpanded() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.fragment_bottomsheet_monet;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.content);
        Palette interfaceC0246cJ = MonetUtil.getPalette();
        linearLayout.addView(new MonetColorSwatchRow(getContext(), interfaceC0246cJ, 0));
        linearLayout.addView(new MonetColorSwatchRow(getContext(), interfaceC0246cJ, 10));
        linearLayout.addView(new MonetColorSwatchRow(getContext(), interfaceC0246cJ, 50));
        linearLayout.addView(new MonetColorSwatchRow(getContext(), interfaceC0246cJ, 100));
        linearLayout.addView(new MonetColorSwatchRow(getContext(), interfaceC0246cJ, HttpStatusCodesKt.HTTP_OK));
        linearLayout.addView(new MonetColorSwatchRow(getContext(), interfaceC0246cJ, HttpStatusCodesKt.HTTP_MULT_CHOICE));
        linearLayout.addView(new MonetColorSwatchRow(getContext(), interfaceC0246cJ, HttpStatusCodesKt.HTTP_BAD_REQUEST));
        linearLayout.addView(new MonetColorSwatchRow(getContext(), interfaceC0246cJ, HttpStatusCodesKt.HTTP_INTERNAL_SERVER_ERROR));
        linearLayout.addView(new MonetColorSwatchRow(getContext(), interfaceC0246cJ, 600));
        linearLayout.addView(new MonetColorSwatchRow(getContext(), interfaceC0246cJ, 700));
        linearLayout.addView(new MonetColorSwatchRow(getContext(), interfaceC0246cJ, 800));
        linearLayout.addView(new MonetColorSwatchRow(getContext(), interfaceC0246cJ, 900));
        linearLayout.addView(new MonetColorSwatchRow(getContext(), interfaceC0246cJ, 1000));
    }
}
