package com.resync.app.ui.activities.media;

import android.view.View;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.images.CustomImageView;

/* JADX INFO: loaded from: classes.dex */
public class SingleImageActivity_ViewBinding extends AbstractImageActivity_ViewBinding {
    private SingleImageActivity target;

    public SingleImageActivity_ViewBinding(SingleImageActivity singleImageActivity, View view) {
        super(singleImageActivity, view);
        this.target = singleImageActivity;
        singleImageActivity.mImageView = (CustomImageView) Utils.findRequiredViewAsType(view, R.id.placeholder_image_preview, "field 'mImageView'", CustomImageView.class);
    }
}
