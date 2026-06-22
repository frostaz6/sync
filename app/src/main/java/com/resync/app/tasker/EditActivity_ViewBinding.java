package com.resync.app.tasker;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes.dex */
public class EditActivity_ViewBinding implements Unbinder {
    private EditActivity target;

    public EditActivity_ViewBinding(EditActivity editActivity, View view) {
        this.target = editActivity;
        editActivity.mCommentsCheckbox = (CheckBox) Utils.findRequiredViewAsType(view, R.id.tasker_sync_comments, "field 'mCommentsCheckbox'", CheckBox.class);
        editActivity.mSubredditInput = (AutoCompleteTextView) Utils.findRequiredViewAsType(view, R.id.tasker_sync_subreddit, "field 'mSubredditInput'", AutoCompleteTextView.class);
        editActivity.mPagesInput = (TextView) Utils.findRequiredViewAsType(view, R.id.tasker_sync_pages, "field 'mPagesInput'", TextView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        EditActivity editActivity = this.target;
        if (editActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        editActivity.mCommentsCheckbox = null;
        editActivity.mSubredditInput = null;
        editActivity.mPagesInput = null;
    }
}
