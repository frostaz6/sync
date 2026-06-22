package com.resync.app.ui.activities.media;

import android.view.View;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.AdWrapper;

/* JADX INFO: loaded from: classes.dex */
public class AbstractImageActivity_ViewBinding implements Unbinder {
    private AbstractImageActivity target;

    public AbstractImageActivity_ViewBinding(AbstractImageActivity abstractImageActivity, View view) {
        this.target = abstractImageActivity;
        abstractImageActivity.mRootView = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.activity_image_root, "field 'mRootView'", RelativeLayout.class);
        abstractImageActivity.mAdWrapper = (AdWrapper) Utils.findRequiredViewAsType(view, R.id.ad_wrapper, "field 'mAdWrapper'", AdWrapper.class);
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        AbstractImageActivity abstractImageActivity = this.target;
        if (abstractImageActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        abstractImageActivity.mRootView = null;
        abstractImageActivity.mAdWrapper = null;
    }
}
