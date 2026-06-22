package com.resync.app.ui.views.tooltips;

import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.card.MaterialCardView;
import com.resync.app.R;
import com.resync.app.ui.views.text.BaseTextView;

/* JADX INFO: loaded from: classes3.dex */
public class TooltipView_ViewBinding implements Unbinder {
    private TooltipView target;

    public TooltipView_ViewBinding(TooltipView tooltipView, View view) {
        this.target = tooltipView;
        tooltipView.cardView = (MaterialCardView) Utils.findRequiredViewAsType(view, R.id.tooltip_card, "field 'cardView'", MaterialCardView.class);
        tooltipView.textView = (BaseTextView) Utils.findRequiredViewAsType(view, R.id.tooltip_text, "field 'textView'", BaseTextView.class);
        tooltipView.tooltipIcon = (AppCompatImageView) Utils.findRequiredViewAsType(view, R.id.tooltip_icon, "field 'tooltipIcon'", AppCompatImageView.class);
        tooltipView.closeView = (AppCompatImageView) Utils.findRequiredViewAsType(view, R.id.tooltip_close, "field 'closeView'", AppCompatImageView.class);
    }

    @Override
    public void unbind() {
        TooltipView tooltipView = this.target;
        if (tooltipView == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        tooltipView.cardView = null;
        tooltipView.textView = null;
        tooltipView.tooltipIcon = null;
        tooltipView.closeView = null;
    }
}
