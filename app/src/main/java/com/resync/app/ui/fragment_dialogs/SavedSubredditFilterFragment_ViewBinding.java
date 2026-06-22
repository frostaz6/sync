package com.resync.app.ui.fragment_dialogs;

import android.view.View;
import android.widget.AutoCompleteTextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes.dex */
public class SavedSubredditFilterFragment_ViewBinding implements Unbinder {
    private SavedSubredditFilterFragment target;

    public SavedSubredditFilterFragment_ViewBinding(SavedSubredditFilterFragment savedSubredditFilterFragment, View view) {
        this.target = savedSubredditFilterFragment;
        savedSubredditFilterFragment.subredditInput = (AutoCompleteTextView) Utils.findRequiredViewAsType(view, R.id.fragment_saved_subreddit, "field 'subredditInput'", AutoCompleteTextView.class);
    }

    @Override
    public void unbind() {
        SavedSubredditFilterFragment target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.subredditInput = null;
    }
}
