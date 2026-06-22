package com.resync.app.ui.views.core;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.buttons.CustomAppCompatButton;
import com.resync.app.ui.views.buttons.MoreButton;

/* JADX INFO: loaded from: classes3.dex */
public class MaterialRow_ViewBinding implements Unbinder {
    private MaterialRow target;

    public MaterialRow_ViewBinding(MaterialRow materialRow, View view) {
        this.target = materialRow;
        materialRow.mTitle = (TextView) Utils.findRequiredViewAsType(view, R.id.view_material_row_title, "field 'mTitle'", TextView.class);
        materialRow.mSecondary = (TextView) Utils.findRequiredViewAsType(view, R.id.view_material_row_secondary, "field 'mSecondary'", TextView.class);
        materialRow.mItemsWrapper = (ViewGroup) Utils.findRequiredViewAsType(view, R.id.view_material_row_items_wrapper, "field 'mItemsWrapper'", ViewGroup.class);
        materialRow.mIcon = (CustomAppCompatButton) Utils.findRequiredViewAsType(view, R.id.view_material_row_icon, "field 'mIcon'", CustomAppCompatButton.class);
        materialRow.mSubView = (SubView) Utils.findRequiredViewAsType(view, R.id.view_material_row_sub_view, "field 'mSubView'", SubView.class);
        materialRow.mImage = (AppCompatImageView) Utils.findRequiredViewAsType(view, R.id.view_material_row_image, "field 'mImage'", AppCompatImageView.class);
        materialRow.mMoreButton = (MoreButton) Utils.findRequiredViewAsType(view, R.id.more, "field 'mMoreButton'", MoreButton.class);
    }

    @Override
    public void unbind() {
        MaterialRow materialRow = this.target;
        if (materialRow == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        materialRow.mTitle = null;
        materialRow.mSecondary = null;
        materialRow.mItemsWrapper = null;
        materialRow.mIcon = null;
        materialRow.mSubView = null;
        materialRow.mImage = null;
        materialRow.mMoreButton = null;
    }
}
