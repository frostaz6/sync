package com.resync.app.ui.viewholders.sections.search;

import android.view.View;
import butterknife.Unbinder;

/* JADX INFO: loaded from: classes3.dex */
public class SubredditSearchHolder_ViewBinding implements Unbinder {
    private SubredditSearchHolder target;

    public SubredditSearchHolder_ViewBinding(SubredditSearchHolder subredditSearchHolder, View view) {
        this.target = subredditSearchHolder;
    }

    @Override
    public void unbind() {
        SubredditSearchHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
    }
}
