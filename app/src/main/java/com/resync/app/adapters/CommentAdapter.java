package com.resync.app.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.ui.viewholders.comments.CommentFooterHolder;
import com.resync.app.ui.viewholders.comments.CommentHolder;
import com.resync.app.media.images.SyncTypeHelper;
import com.resync.app.holders.CommentHeaderHolder;
import com.resync.app.holders.CommentMoreHolder;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.presenters.CommentsPresenter;

/* JADX INFO: renamed from: p9.a */
/* JADX INFO: compiled from: CommentAdapter.java */
/* JADX INFO: loaded from: classes.dex */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;

    CommentsPresenter presenter;

    Cursor cursor;

    public CommentAdapter(Context context, CommentsPresenter commentsPresenter) {
        this.context = context;
        this.presenter = commentsPresenter;
        setHasStableIds(true);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentHeaderHolder) {
            ((CommentHeaderHolder) holder).bind(this.presenter);
            return;
        }
        if (holder instanceof CommentFooterHolder) {
            ((CommentFooterHolder) holder).bind(this.presenter);
        } else if (holder instanceof CommentHolder) {
            ((CommentHolder) holder).bind(getPost(position));
        } else if (holder instanceof CommentMoreHolder) {
            ((CommentMoreHolder) holder).bind(getPost(position));
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == 99) {
            return CommentHeaderHolder.create(getContext(), viewGroup, this.presenter);
        }
        if (viewType == 1001) {
            return CommentFooterHolder.create(getContext(), viewGroup, this.presenter);
        }
        if (viewType == 11) {
            return CommentHolder.create(getContext(), viewGroup, this.presenter);
        }
        if (viewType == 12) {
            return CommentMoreHolder.create(getContext(), viewGroup, this.presenter);
        }
        throw new SyncTypeHelper.UnsupportedSyncTypeException(viewType);
    }

    public int getLastCommentPosition() {
        return getCount() - 1;
    }

    final Context getContext() {
        return this.context;
    }

    final Cursor getCursor() {
        return this.cursor;
    }

    public final int getCount() {
        Cursor cursor = this.cursor;
        if (cursor != null) {
            return cursor.getCount();
        }
        return 0;
    }

    int getSyncType(int position) {
        getCursor().moveToPosition(position);
        return getCursor().getInt(getCursor().getColumnIndex("sync_type"));
    }

    public Post getPost(int position) {
        return Post.fromCursorPosition(getCursor(), position);
    }

    public void setCursor(Cursor cursor) {
        if (cursor == null || cursor.isClosed()) {
            Logger.verbose("CommentAdapter", "Set a null cursor");
            this.cursor = null;
            notifyDataSetChanged();
            return;
        }
        Cursor cursor2 = this.cursor;
        if (cursor2 == null || !cursor2.equals(cursor)) {
            Logger.verbose("CommentAdapter", "Setup a regular cursor");
            this.cursor = cursor;
            notifyDataSetChanged();
        } else {
            Logger.verbose("CommentAdapter", "The new cursor is the same as the old");
            this.cursor = cursor;
            notifyDataSetChanged();
        }
        prefetchWebsitePreviews(cursor);
    }

    /**
     * Warm website-preview images for the loaded comments so they're cached before the user scrolls
     * to them (instead of popping in seconds later). Deferred to after the initial render and bounded.
     */
    private void prefetchWebsitePreviews(final Cursor cursor) {
        new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (cursor == null || cursor.isClosed()) {
                    return;
                }
                try {
                    java.util.ArrayList<String> bodies = new java.util.ArrayList<>();
                    int count = Math.min(cursor.getCount(), 40);
                    for (int i = 0; i < count; i++) {
                        try {
                            Post comment = Post.fromCursorPosition(cursor, i);
                            if (comment != null && comment.getSyncType() == 11) {
                                String body = comment.getBodyProcessed();
                                if (body != null && !body.isEmpty()) {
                                    bodies.add(body);
                                }
                            }
                        } catch (Exception ignored) {
                        }
                    }
                    com.resync.app.media.preview.WebsitePreviewPrefetcher.prefetch(bodies);
                } catch (Throwable ignored) {
                }
            }
        });
    }

    final boolean shouldShowEmptyState() {
        CommentsPresenter commentsPresenter;
        return ((getCursor() != null && (getCursor() == null || getCursor().getCount() != 0)) || (commentsPresenter = this.presenter) == null || commentsPresenter.getPost() == null || this.presenter.getPost().getKind() != 3 || this.presenter.isSearch()) ? false : true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: n */
    public int getItemCount() {
        if (shouldShowEmptyState()) {
            return 1;
        }
        return (this.presenter.isSearch() || getCount() < 1) ? getCount() : getCount() + 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* JADX INFO: renamed from: o */
    public long getItemId(int position) {
        if (position == 0) {
            return 99L;
        }
        if (position == getCount()) {
            return 1001L;
        }
        getCursor().moveToPosition(position);
        return getCursor().getString(getCursor().getColumnIndex("_id")).hashCode();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        if (position == 0 && !this.presenter.isSearch()) {
            return 99;
        }
        if (position == getCount()) {
            return 1001;
        }
        int syncType = getSyncType(position);
        if (syncType == 11 || syncType == 12) {
            return syncType;
        }
        throw new RuntimeException("Unsupported type: " + syncType + " - Pos: " + position);
    }
}
