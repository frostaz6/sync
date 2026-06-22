package com.resync.app.ui.views.monet;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class MonetSearchBoxView_ViewBinding implements Unbinder {
    private MonetSearchBoxView target;

    public MonetSearchBoxView_ViewBinding(MonetSearchBoxView monetSearchBoxView, View view) {
        this.target = monetSearchBoxView;
        monetSearchBoxView.searchWrapper = (RelativeLayout) Utils.findRequiredViewAsType(view, R.id.search_wrapper, "field 'searchWrapper'", RelativeLayout.class);
        monetSearchBoxView.editText = (EditText) Utils.findRequiredViewAsType(view, R.id.search_edit_text, "field 'editText'", EditText.class);
        monetSearchBoxView.textView = (TextView) Utils.findRequiredViewAsType(view, R.id.search_text_view, "field 'textView'", TextView.class);
        monetSearchBoxView.moreIcon = (ImageView) Utils.findRequiredViewAsType(view, R.id.search_more, "field 'moreIcon'", ImageView.class);
        monetSearchBoxView.searchIcon = (ImageView) Utils.findRequiredViewAsType(view, R.id.search_icon, "field 'searchIcon'", ImageView.class);
    }

    @Override
    public void unbind() {
        MonetSearchBoxView monetSearchBoxView = this.target;
        if (monetSearchBoxView == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        monetSearchBoxView.searchWrapper = null;
        monetSearchBoxView.editText = null;
        monetSearchBoxView.textView = null;
        monetSearchBoxView.moreIcon = null;
        monetSearchBoxView.searchIcon = null;
    }
}
