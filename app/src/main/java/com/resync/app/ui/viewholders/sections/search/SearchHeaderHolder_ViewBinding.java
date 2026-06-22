package com.resync.app.ui.viewholders.sections.search;

import android.view.View;
import butterknife.Unbinder;

/* JADX INFO: loaded from: classes3.dex */
public class SearchHeaderHolder_ViewBinding implements Unbinder {
    private SearchHeaderHolder target;

    public SearchHeaderHolder_ViewBinding(SearchHeaderHolder searchHeaderHolder, View view) {
        this.target = searchHeaderHolder;
    }

    @Override
    public void unbind() {
        SearchHeaderHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
    }
}
