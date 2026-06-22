package com.resync.app.ui.views.ultra;

import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.responsive.CustomProgressBar;
import com.resync.app.ui.views.text.basic.ProductSansTextView;

/* JADX INFO: loaded from: classes3.dex */
public class UltraPurchasedView_ViewBinding implements Unbinder {
    private UltraPurchasedView target;

    public UltraPurchasedView_ViewBinding(UltraPurchasedView ultraPurchasedView, View view) {
        this.target = ultraPurchasedView;
        ultraPurchasedView.icon = (ImageView) Utils.findRequiredViewAsType(view, R.id.view_ultra_purchase_icon, "field 'icon'", ImageView.class);
        ultraPurchasedView.priceTextView = (ProductSansTextView) Utils.findRequiredViewAsType(view, R.id.view_ultra_purchase_price, "field 'priceTextView'", ProductSansTextView.class);
        ultraPurchasedView.priceDescriptionTextView = (ProductSansTextView) Utils.findRequiredViewAsType(view, R.id.view_ultra_purchase_duration_description, "field 'priceDescriptionTextView'", ProductSansTextView.class);
        ultraPurchasedView.progressBar = (CustomProgressBar) Utils.findRequiredViewAsType(view, R.id.view_ultra_purchase_progress_bar, "field 'progressBar'", CustomProgressBar.class);
        ultraPurchasedView.errorImageView = (AppCompatImageView) Utils.findRequiredViewAsType(view, R.id.view_ultra_purchase_error, "field 'errorImageView'", AppCompatImageView.class);
    }

    @Override
    public void unbind() {
        UltraPurchasedView ultraPurchasedView = this.target;
        if (ultraPurchasedView == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        ultraPurchasedView.icon = null;
        ultraPurchasedView.priceTextView = null;
        ultraPurchasedView.priceDescriptionTextView = null;
        ultraPurchasedView.progressBar = null;
        ultraPurchasedView.errorImageView = null;
    }
}
