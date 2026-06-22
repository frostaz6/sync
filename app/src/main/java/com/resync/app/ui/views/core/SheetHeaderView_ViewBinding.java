package com.resync.app.ui.views.core;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.buttons.DragButton;
import com.resync.app.ui.views.buttons.MoreButton;
import com.resync.app.ui.views.images.CustomImageView;
import com.resync.app.ui.views.responsive.CustomSubtleDividerView;

/* JADX INFO: loaded from: classes3.dex */
public class SheetHeaderView_ViewBinding implements Unbinder {
    private SheetHeaderView target;

    public SheetHeaderView_ViewBinding(SheetHeaderView sheetHeaderView, View view) {
        this.target = sheetHeaderView;
        sheetHeaderView.subViewWrapper = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.section_header_view_subview_wrapper, "field 'subViewWrapper'", RelativeLayout.class);
        sheetHeaderView.bannerView = (CustomImageView) Utils.findRequiredViewAsType(view, R.id.section_header_view_banner, "field 'bannerView'", CustomImageView.class);
        sheetHeaderView.subView = (SubView) Utils.findRequiredViewAsType(view, R.id.section_header_view_subview, "field 'subView'", SubView.class);
        sheetHeaderView.textWrapper = (LinearLayoutCompat) Utils.findRequiredViewAsType(view, R.id.section_header_view_text_wrapper, "field 'textWrapper'", LinearLayoutCompat.class);
        sheetHeaderView.textView = (TextView) Utils.findRequiredViewAsType(view, R.id.section_header_view_textview, "field 'textView'", TextView.class);
        sheetHeaderView.moreButton = (MoreButton) Utils.findRequiredViewAsType(view, R.id.section_header_view_more, "field 'moreButton'", MoreButton.class);
        sheetHeaderView.dragButton = (DragButton) Utils.findRequiredViewAsType(view, R.id.section_header_view_drag_button, "field 'dragButton'", DragButton.class);
        sheetHeaderView.dividerView = (CustomSubtleDividerView) Utils.findRequiredViewAsType(view, R.id.section_header_view_divider, "field 'dividerView'", CustomSubtleDividerView.class);
    }

    @Override
    public void unbind() {
        SheetHeaderView sheetHeaderView = this.target;
        if (sheetHeaderView == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        sheetHeaderView.subViewWrapper = null;
        sheetHeaderView.bannerView = null;
        sheetHeaderView.subView = null;
        sheetHeaderView.textWrapper = null;
        sheetHeaderView.textView = null;
        sheetHeaderView.moreButton = null;
        sheetHeaderView.dragButton = null;
        sheetHeaderView.dividerView = null;
    }
}
