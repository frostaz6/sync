package com.resync.app.ui.fragments;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.monet.MonetSwipeRefreshLayout;
import com.resync.app.ui.views.recycler.MessagingRecyclerView;

/* JADX INFO: loaded from: classes2.dex */
public class MessagingFragment_ViewBinding implements Unbinder {
    private MessagingFragment target;

    public MessagingFragment_ViewBinding(MessagingFragment messagingFragment, View view) {
        this.target = messagingFragment;
        messagingFragment.mSwipeRefreshLayout = (MonetSwipeRefreshLayout) Utils.findRequiredViewAsType(view, R.id.fragment_messages_str, "field 'mSwipeRefreshLayout'", MonetSwipeRefreshLayout.class);
        messagingFragment.mRecyclerView = (MessagingRecyclerView) Utils.findRequiredViewAsType(view, R.id.fragment_messages_recycler, "field 'mRecyclerView'", MessagingRecyclerView.class);
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        MessagingFragment messagingFragment = this.target;
        if (messagingFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        messagingFragment.mSwipeRefreshLayout = null;
        messagingFragment.mRecyclerView = null;
    }
}
