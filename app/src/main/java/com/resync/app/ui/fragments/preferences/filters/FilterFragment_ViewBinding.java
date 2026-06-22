package com.resync.app.ui.fragments.preferences.filters;

import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes2.dex */
public class FilterFragment_ViewBinding implements Unbinder {
    private FilterFragment target;

    public FilterFragment_ViewBinding(FilterFragment filterFragment, View view) {
        this.target = filterFragment;
        filterFragment.mListView = (ListView) Utils.findRequiredViewAsType(view, R.id.fragment_filter_list, "field 'mListView'", ListView.class);
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        FilterFragment filterFragment = this.target;
        if (filterFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        filterFragment.mListView = null;
    }
}
