package com.resync.app.ui.views.media;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.resync.app.R;
import com.resync.app.ui.views.posts.simple.IndicatorView;

/* JADX INFO: loaded from: classes3.dex */
public class SmartMediaView_ViewBinding implements Unbinder {
    private SmartMediaView target;
    private View view1;
    private View view2;

    class ImageClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SmartMediaView target;

        ImageClickListener(SmartMediaView smartMediaView) {
            this.target = smartMediaView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onImageClicked();
        }
    }

    class IndicatorClickListener extends com.resync.app.util.DebouncingOnClickListener {

        final /* synthetic */ SmartMediaView target;

        IndicatorClickListener(SmartMediaView smartMediaView) {
            this.target = smartMediaView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onIndicatorClicked();
        }
    }

    public SmartMediaView_ViewBinding(SmartMediaView smartMediaView, View view) {
        this.target = smartMediaView;
        View viewC = Utils.findRequiredView(view, R.id.subsampling_image, "field 'subsamplingImageView' and method 'onImageClicked'");
        smartMediaView.subsamplingImageView = (SubsamplingScaleImageView) Utils.castView(viewC, R.id.subsampling_image, "field 'subsamplingImageView'", SubsamplingScaleImageView.class);
        this.view1 = viewC;
        viewC.setOnClickListener(new ImageClickListener(smartMediaView));
        View viewC2 = Utils.findRequiredView(view, R.id.indicator, "field 'indicatorView' and method 'onIndicatorClicked'");
        smartMediaView.indicatorView = (IndicatorView) Utils.castView(viewC2, R.id.indicator, "field 'indicatorView'", IndicatorView.class);
        this.view2 = viewC2;
        viewC2.setOnClickListener(new IndicatorClickListener(smartMediaView));
    }

    @Override
    public void unbind() {
        SmartMediaView smartMediaView = this.target;
        if (smartMediaView == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        smartMediaView.subsamplingImageView = null;
        smartMediaView.indicatorView = null;
        this.view1.setOnClickListener(null);
        this.view1 = null;
        this.view2.setOnClickListener(null);
        this.view2 = null;
    }
}
