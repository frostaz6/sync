package com.resync.app.dialogs;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.ui.views.core.SheetHeaderView;
import com.resync.app.ui.views.monet.buttons.MonetActionButtons;
import com.resync.app.model.Award;

/* JADX INFO: renamed from: s9.c */
/* JADX INFO: compiled from: AwardBottomSheetFragment.java */
/* JADX INFO: loaded from: classes.dex */
public class AwardBottomSheetFragment extends CustomBottomSheetDialogFragment {
    Award award;

    public static Bundle buildArgs(Award award) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("award", award);
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void dismissOnClick(View view) {
        dismiss();
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
    public void loadCustomArguments() {
        this.award = (Award) getArguments().getSerializable("award");
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.fragment_award;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ((SheetHeaderView) view.findViewById(R.id.header)).setTitle(this.award.name);
        Glide.with(RedditApplication.getAppContext()).load(this.award.iconUrl).into((ImageView) view.findViewById(R.id.image));
        ((TextView) view.findViewById(R.id.description)).setText(this.award.description.replaceAll("\\%\\{coin_symbol\\}", ""));
        MonetActionButtons monetActionButtons = (MonetActionButtons) view.findViewById(R.id.monet_action_buttons);
        monetActionButtons.setButtonLabels("Close");
        monetActionButtons.setListeners(new View.OnClickListener() { // from class: s9.b
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                AwardBottomSheetFragment.this.dismissOnClick(view2);
            }
        }, null);
    }
}
