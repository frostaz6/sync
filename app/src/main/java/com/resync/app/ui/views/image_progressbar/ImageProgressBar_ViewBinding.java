package com.resync.app.ui.views.image_progressbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.resync.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class ImageProgressBar_ViewBinding implements Unbinder {
    private ImageProgressBar target;

    public ImageProgressBar_ViewBinding(ImageProgressBar imageProgressBar, View view) {
        this.target = imageProgressBar;
        imageProgressBar.downloadingProgressBar = (ProgressBar) Utils.findRequiredViewAsType(view, R.id.ipb_downloading, "field 'downloadingProgressBar'", ProgressBar.class);
        imageProgressBar.interminateProgressBar = (CircularProgressIndicator) Utils.findRequiredViewAsType(view, R.id.ipb_indeterminate, "field 'interminateProgressBar'", CircularProgressIndicator.class);
        imageProgressBar.cancelButton = (ImageView) Utils.findRequiredViewAsType(view, R.id.ipb_cancel, "field 'cancelButton'", ImageView.class);
    }

    @Override
    public void unbind() {
        ImageProgressBar imageProgressBar = this.target;
        if (imageProgressBar == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        imageProgressBar.downloadingProgressBar = null;
        imageProgressBar.interminateProgressBar = null;
        imageProgressBar.cancelButton = null;
    }
}
