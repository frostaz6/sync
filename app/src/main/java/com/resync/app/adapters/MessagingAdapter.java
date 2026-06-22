package com.resync.app.adapters;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.ui.viewholders.messaging.MessageHolder;
import com.resync.app.model.Message;
import com.resync.app.presenters.MessagingPresenter;

/* JADX INFO: renamed from: p9.b */
/* JADX INFO: compiled from: MessagingAdapter.java */
/* JADX INFO: loaded from: classes.dex */
public class MessagingAdapter extends RecyclerView.Adapter<MessageHolder> {
    protected Context context;

    protected MessagingPresenter presenter;

    public MessagingAdapter(Context context, MessagingPresenter messagingPresenter) {
        this.context = context;
        this.presenter = messagingPresenter;
        setHasStableIds(true);
    }

    public Message getMessage(int position) {
        return this.presenter.getMessages().get(position);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: O, reason: merged with bridge method [inline-methods] */
    public void onBindViewHolder(MessageHolder messageHolder, int position) {
        messageHolder.bind(getMessage(position));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: P, reason: merged with bridge method [inline-methods] */
    public MessageHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return MessageHolder.create(this.context, viewGroup, this.presenter);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: n */
    public int getItemCount() {
        return this.presenter.getMessageCount();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: o */
    public long getItemId(int position) {
        return getMessage(position).id.hashCode();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return 14;
    }
}
