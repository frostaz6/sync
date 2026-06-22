package com.resync.app.ui.fragments.preferences;

import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.core.MaterialYouToolbar;
import com.resync.app.ui.views.transparent_status.CustomCoordinatorLayout;

/* JADX INFO: loaded from: classes2.dex */
public class SearchPreferenceWrapperFragment_ViewBinding implements Unbinder {
    private SearchPreferenceWrapperFragment target;

    public SearchPreferenceWrapperFragment_ViewBinding(SearchPreferenceWrapperFragment searchPreferenceWrapperFragment, View view) {
        this.target = searchPreferenceWrapperFragment;
        searchPreferenceWrapperFragment.coordinatorLayout = (CustomCoordinatorLayout) Utils.findRequiredViewAsType(view, R.id.coordinator, "field 'coordinatorLayout'", CustomCoordinatorLayout.class);
        searchPreferenceWrapperFragment.search = (EditText) Utils.findRequiredViewAsType(view, R.id.input, "field 'search'", EditText.class);
        searchPreferenceWrapperFragment.toolbar = (MaterialYouToolbar) Utils.findRequiredViewAsType(view, R.id.toolbar, "field 'toolbar'", MaterialYouToolbar.class);
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        SearchPreferenceWrapperFragment searchPreferenceWrapperFragment = this.target;
        if (searchPreferenceWrapperFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        searchPreferenceWrapperFragment.coordinatorLayout = null;
        searchPreferenceWrapperFragment.search = null;
        searchPreferenceWrapperFragment.toolbar = null;
    }
}
