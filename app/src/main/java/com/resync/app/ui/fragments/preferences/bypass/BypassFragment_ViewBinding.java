package com.resync.app.ui.fragments.preferences.bypass;

import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes2.dex */
public class BypassFragment_ViewBinding implements Unbinder {
    private BypassFragment target;

    public BypassFragment_ViewBinding(BypassFragment bypassFragment, View view) {
        this.target = bypassFragment;
        bypassFragment.mListView = (ListView) Utils.findRequiredViewAsType(view, R.id.fragment_bypass_list, "field 'mListView'", ListView.class);
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        BypassFragment bypassFragment = this.target;
        if (bypassFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        bypassFragment.mListView = null;
    }
}
