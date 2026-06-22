package com.resync.app.ui.viewholders.sections.search;

import android.view.View;
import butterknife.Unbinder;

/* JADX INFO: loaded from: classes3.dex */
public class SubredditSearchHeaderHolder_ViewBinding implements Unbinder {
    private SubredditSearchHeaderHolder target;

    public SubredditSearchHeaderHolder_ViewBinding(SubredditSearchHeaderHolder subredditSearchHeaderHolder, View view) {
        this.target = subredditSearchHeaderHolder;
    }

    @Override
    public void unbind() {
        SubredditSearchHeaderHolder target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
    }
}
