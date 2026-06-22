package com.resync.app.ui.fragment_dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.appcompat.app.AppCompatDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.helpers.ui.AlertDialogHelper;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.ui.SubredditArrayList;
import com.resync.app.managers.SubredditsManager;
import com.resync.app.events.SavedFilterSubredditEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.model.Subreddits;
import com.resync.app.helpers.VisitedManager;

/* JADX INFO: loaded from: classes.dex */
public class SavedSubredditFilterFragment extends AppCompatDialogFragment {

    @BindView(R.id.fragment_saved_subreddit)
    AutoCompleteTextView subredditInput;

    class FilterClickListener implements DialogInterface.OnClickListener {
        FilterClickListener() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            String strTrim = SavedSubredditFilterFragment.this.subredditInput.getText().toString().trim();
            if (TextUtils.isEmpty(strTrim)) {
                StaticToast.showWithContext(SavedSubredditFilterFragment.this.getActivity(), "No subreddit entered!");
            } else {
                BusSingleton.getBus().post(new SavedFilterSubredditEvent(strTrim));
            }
            SavedSubredditFilterFragment.this.dismissSafely();
        }
    }

    public static SavedSubredditFilterFragment newInstance() {
        return new SavedSubredditFilterFragment();
    }

    @Override // androidx.appcompat.app.AppCompatDialogFragment, androidx.fragment.app.c
    public Dialog onCreateDialog(Bundle bundle) {
        View viewInflate = View.inflate(getActivity(), R.layout.fragment_saved_subreddit_filter, null);
        ButterKnife.bind(this, viewInflate);
        SubredditArrayList subredditArrayList = new SubredditArrayList();
        subredditArrayList.addAll(SubredditsManager.getInstance().getSubreddits());
        subredditArrayList.addAll(Subreddits.DEFAULTS);
        if (VisitedManager.hasVisited()) {
            subredditArrayList.addAll(VisitedManager.getVisitedArray());
        }
        subredditArrayList.sort(SubredditArrayList.SORT_DEFAULT);
        this.subredditInput.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, subredditArrayList.toStringArray()));
        androidx.appcompat.app.AlertDialog dialog = AlertDialogHelper.newBuilder(getActivity()).setView(viewInflate).setCancelable(true).setTitle("Filter saved by subreddit").setPositiveButton("Filter", new FilterClickListener()).setNegativeButton("Cancel", null).create();
        dialog.getWindow().setSoftInputMode(20);
        return dialog;
    }

    protected void dismissSafely() {
        try {
            dismiss();
        } catch (Exception unused) {
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        dismissSafely();
        super.onPause();
    }
}
