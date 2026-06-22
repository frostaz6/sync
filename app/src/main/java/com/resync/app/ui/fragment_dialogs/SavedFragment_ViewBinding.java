package com.resync.app.ui.fragment_dialogs;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes.dex */
public class SavedFragment_ViewBinding implements Unbinder {
    private SavedFragment target;

    public SavedFragment_ViewBinding(SavedFragment savedFragment, View view) {
        this.target = savedFragment;
        savedFragment.progressBarWrapper = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.fragment_saved_progress_wrapper, "field 'progressBarWrapper'", RelativeLayout.class);
        savedFragment.progressBar = (ProgressBar) Utils.findRequiredViewAsType(view, R.id.fragment_saved_progress, "field 'progressBar'", ProgressBar.class);
        savedFragment.errorWrapper = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.fragment_saved_error_wrapper, "field 'errorWrapper'", LinearLayout.class);
        savedFragment.errorIcon = (ImageView) Utils.findRequiredViewAsType(view, R.id.fragment_saved_error_icon, "field 'errorIcon'", ImageView.class);
        savedFragment.errorText = (TextView) Utils.findRequiredViewAsType(view, R.id.fragment_saved_error_text, "field 'errorText'", TextView.class);
    }

    @Override
    public void unbind() {
        SavedFragment target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.progressBarWrapper = null;
        target.progressBar = null;
        target.errorWrapper = null;
        target.errorIcon = null;
        target.errorText = null;
    }
}
