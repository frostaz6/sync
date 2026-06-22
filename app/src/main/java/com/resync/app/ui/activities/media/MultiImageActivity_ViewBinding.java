package com.resync.app.ui.activities.media;

import android.view.View;
import butterknife.internal.Utils;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.resync.app.R;
import com.resync.app.ui.views.hacky.HackyViewPager;

/* JADX INFO: loaded from: classes.dex */
public class MultiImageActivity_ViewBinding extends AbstractImageActivity_ViewBinding {
    private MultiImageActivity target;

    public MultiImageActivity_ViewBinding(MultiImageActivity multiImageActivity, View view) {
        super(multiImageActivity, view);
        this.target = multiImageActivity;
        multiImageActivity.mViewPager = (HackyViewPager) Utils.findRequiredViewAsType(view, R.id.fragment_multi_image_pager, "field 'mViewPager'", HackyViewPager.class);
        multiImageActivity.mAlbumProgressBar = (CircularProgressIndicator) Utils.findRequiredViewAsType(view, R.id.fragment_multi_image_progress, "field 'mAlbumProgressBar'", CircularProgressIndicator.class);
    }
}
