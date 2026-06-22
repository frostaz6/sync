package com.resync.app.ui.util.animation.recycler.comments;

import android.animation.ObjectAnimator;
import android.util.Property;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.ui.util.animation.base.AbstractAnimator;
import com.resync.app.holders.RedditViewHolder;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: loaded from: classes3.dex */
public class CommentsActionsAnimator extends AbstractAnimator {
    @Override // com.resync.app.ui.util.animation.base.AbstractAnimator, androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
        super.animateAdd(viewHolder);
        ViewCompat.setTranslationY(viewHolder.itemView, SizeHelper.dpToPxFloat(48));
        this.mPendingAdditions.add((RedditViewHolder) viewHolder);
        return true;
    }

    @Override // com.resync.app.ui.util.animation.base.AbstractAnimator, androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateRemove(RecyclerView.ViewHolder viewHolder) {
        super.animateRemove(viewHolder);
        this.mPendingRemovals.add((RedditViewHolder) viewHolder);
        return true;
    }

    @Override // com.resync.app.widgets.CustomItemAnimator
    public long getDuration() {
        return 100L;
    }

    @Override // com.resync.app.widgets.CustomItemAnimator
    public boolean getStaggerAnimations() {
        return false;
    }

    @Override // com.resync.app.widgets.CustomItemAnimator
    public long getStartDelay() {
        return 0L;
    }

    @Override // com.resync.app.widgets.CustomItemAnimator
    public ObjectAnimator getAddAnimator(RedditViewHolder redditViewHolder) {
        return ObjectAnimator.ofFloat(redditViewHolder.itemView, (Property<View, Float>) View.TRANSLATION_Y, 0.0f);
    }

    @Override // com.resync.app.widgets.CustomItemAnimator
    public ObjectAnimator getRemoveAnimator(RedditViewHolder redditViewHolder) {
        return ObjectAnimator.ofFloat(redditViewHolder.itemView, (Property<View, Float>) View.TRANSLATION_Y, SizeHelper.dpToPxFloat(48));
    }
}
