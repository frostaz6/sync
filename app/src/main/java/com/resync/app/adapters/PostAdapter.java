package com.resync.app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.ui.viewholders.SearchSubsDetailedHolder;
import com.resync.app.media.images.SyncTypeHelper;
import com.resync.app.holders.BasePostViewHolder;
import com.resync.app.holders.DividerHolder;
import com.resync.app.model.Post;
import com.resync.app.presenters.PostsPresenter;

/* JADX INFO: renamed from: p9.c */
/* JADX INFO: compiled from: PostAdapter.java */
/* JADX INFO: loaded from: classes.dex */
public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;

    private final RecyclerView recyclerView;

    private final PostsPresenter presenter;
    private int bindMode = 0;
    private Cursor cursor;

    private int itemCount;

    public PostAdapter(Context context, RecyclerView recyclerView, PostsPresenter postsPresenter) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.presenter = postsPresenter;
        setHasStableIds(true);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SearchSubsDetailedHolder) {
            ((SearchSubsDetailedHolder) holder).bind(getPost(position));
            return;
        }
        if (holder instanceof BasePostViewHolder) {
            ((BasePostViewHolder) holder).bindPost(getPost(position), this.bindMode);
        } else {
            if (holder instanceof DividerHolder) {
                ((DividerHolder) holder).bind(getPost(position));
                return;
            }
            throw new RuntimeException("Unsupported viewholder: " + holder.getClass().getSimpleName());
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == 15) {
            return SearchSubsDetailedHolder.create(viewGroup.getContext(), viewGroup);
        }
        if (UiSingleton.isSlides(getUiMode())) {
            return SyncTypeHelper.createSimpleHolder(getContext(), viewGroup, this.presenter, viewType);
        }
        if (getUiMode() == 0 || getUiMode() == 1 || getUiMode() == 2) {
            return SyncTypeHelper.createCompactHolder(getContext(), viewGroup, this.presenter, viewType, getUiMode());
        }
        if (getUiMode() == 3) {
            return SyncTypeHelper.createSmallCardHolder(getContext(), viewGroup, this.presenter, viewType);
        }
        if (UiSingleton.isCards(getUiMode())) {
            return SyncTypeHelper.createCardHolder(getContext(), viewGroup, this.presenter, viewType, getUiMode());
        }
        if (UiSingleton.isSwipe(getUiMode())) {
            return SyncTypeHelper.createSwipeHolder(getContext(), viewGroup, this.presenter, viewType);
        }
        throw new UiSingleton.UnsupportedUiModeException();
    }

    public void stopItemAnimator() {
        if (this.recyclerView.getItemAnimator() == null || !this.recyclerView.getItemAnimator().isRunning()) {
            return;
        }
        this.recyclerView.setItemAnimator(null);
    }

    public Context getContext() {
        return this.context;
    }

    public final Cursor getCursor() {
        return this.cursor;
    }

    public Post getPost(int position) {
        return Post.fromCursorPosition(getCursor(), position);
    }

    public int getUiMode() {
        return this.presenter.getUiMode();
    }

    public void setCursor(Cursor cursor) {
        stopItemAnimator();
        this.itemCount = cursor != null ? cursor.getCount() : 0;
        Cursor cursor2 = this.cursor;
        this.cursor = cursor;
        notifyDataSetChanged();
        if (cursor2 == null || cursor == null || cursor2.equals(cursor) || cursor2.isClosed()) {
            return;
        }
        cursor2.close();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: n */
    public int getItemCount() {
        return this.itemCount;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @SuppressLint({"Range"})
    /* JADX INFO: renamed from: o */
    public long getItemId(int position) {
        if (position == -1) {
            return 0L;
        }
        getCursor().moveToPosition(position);
        return getCursor().getInt(getCursor().getColumnIndex("hash"));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @SuppressLint({"Range"})
    public int getItemViewType(int position) {
        getCursor().moveToPosition(position);
        int syncType = getCursor().getInt(getCursor().getColumnIndex("sync_type"));
        if (syncType == 15) {
            return 15;
        }
        if (UiSingleton.isSlides(getUiMode())) {
            return SyncTypeHelper.getSimpleViewType(syncType);
        }
        if (UiSingleton.isListLike(getUiMode())) {
            return SyncTypeHelper.getCompactViewType(syncType);
        }
        if (UiSingleton.isSmallCards(getUiMode())) {
            return SyncTypeHelper.getCompactViewType(syncType);
        }
        if (UiSingleton.isCards(getUiMode())) {
            return SyncTypeHelper.getCardViewType(syncType);
        }
        if (UiSingleton.isSwipe(getUiMode())) {
            return SyncTypeHelper.getSwipeViewType(syncType);
        }
        throw new RuntimeException("Unsupported type: " + position);
    }
}
