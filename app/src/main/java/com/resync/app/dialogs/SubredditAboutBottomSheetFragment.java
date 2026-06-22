package com.resync.app.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.resync.app.R;
import com.resync.app.ui.fragments.sidebar.AboutFragment;
import com.resync.app.helpers.ContextHelper;

/* JADX INFO: renamed from: s9.k */
/* JADX INFO: compiled from: SubredditAboutBottomSheetFragment.java */
/* JADX INFO: loaded from: classes.dex */
public class SubredditAboutBottomSheetFragment extends CustomBottomSheetDialogFragment {
    public static void dismissExisting(Context context) {
        FragmentManager fragmentManagerG = ContextHelper.getFragmentManager(context);
        if (fragmentManagerG != null) {
            Fragment fragmentJ0 = fragmentManagerG.findFragmentByTag(SubredditAboutBottomSheetFragment.class.getSimpleName());
            if (fragmentJ0 instanceof SubredditAboutBottomSheetFragment) {
                fragmentManagerG.beginTransaction().remove(fragmentJ0).commit();
            }
        }
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetInterface
    public int getLayoutRes() {
        return R.layout.fragment_bottomsheet_empty;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragment_bottomsheet_empty_container, AboutFragment.newInstance(getSubreddit(), false)).commit();
    }
}
