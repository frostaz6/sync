package com.resync.app.ui.views.monet.buttons;

import android.view.View;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class MonetActionButtons_ViewBinding implements Unbinder {
    private MonetActionButtons target;

    public MonetActionButtons_ViewBinding(MonetActionButtons monetActionButtons, View view) {
        this.target = monetActionButtons;
        monetActionButtons.negativeNeutralWrapper = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.action_button_negative_neural_wrapper, "field 'negativeNeutralWrapper'", LinearLayout.class);
        monetActionButtons.buttonNegative = (MonetContentHighlightButton) Utils.findRequiredViewAsType(view, R.id.action_button_negative, "field 'buttonNegative'", MonetContentHighlightButton.class);
        monetActionButtons.buttonNeutral = (MonetContentHighlightButton) Utils.findRequiredViewAsType(view, R.id.action_button_neutral, "field 'buttonNeutral'", MonetContentHighlightButton.class);
        monetActionButtons.buttonPositive = (MonetContentHighlightButton) Utils.findRequiredViewAsType(view, R.id.action_button_positive, "field 'buttonPositive'", MonetContentHighlightButton.class);
    }

    @Override
    public void unbind() {
        MonetActionButtons monetActionButtons = this.target;
        if (monetActionButtons == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        monetActionButtons.negativeNeutralWrapper = null;
        monetActionButtons.buttonNegative = null;
        monetActionButtons.buttonNeutral = null;
        monetActionButtons.buttonPositive = null;
    }
}
