package com.resync.app.ui.views.theme_manager;

import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class MonetColorSwatchRow_ViewBinding implements Unbinder {
    private MonetColorSwatchRow target;

    public MonetColorSwatchRow_ViewBinding(MonetColorSwatchRow monetColorSwatchRow, View view) {
        this.target = monetColorSwatchRow;
        monetColorSwatchRow.neutral1Swatch = (TextView) Utils.findRequiredViewAsType(view, R.id.n1, "field 'n1'", TextView.class);
        monetColorSwatchRow.neutral2Swatch = (TextView) Utils.findRequiredViewAsType(view, R.id.n2, "field 'n2'", TextView.class);
        monetColorSwatchRow.accent1Swatch = (TextView) Utils.findRequiredViewAsType(view, R.id.a1, "field 'a1'", TextView.class);
        monetColorSwatchRow.accent2Swatch = (TextView) Utils.findRequiredViewAsType(view, R.id.a2, "field 'a2'", TextView.class);
        monetColorSwatchRow.accent3Swatch = (TextView) Utils.findRequiredViewAsType(view, R.id.a3, "field 'a3'", TextView.class);
    }

    @Override
    public void unbind() {
        MonetColorSwatchRow monetColorSwatchRow = this.target;
        if (monetColorSwatchRow == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        monetColorSwatchRow.neutral1Swatch = null;
        monetColorSwatchRow.neutral2Swatch = null;
        monetColorSwatchRow.accent1Swatch = null;
        monetColorSwatchRow.accent2Swatch = null;
        monetColorSwatchRow.accent3Swatch = null;
    }
}
