package com.resync.app.ui.util.animation.recycler;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.util.Property;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.ui.util.animation.base.AbstractAnimator;
import com.resync.app.ui.util.animation.recycler.comments.OnFinishedListener;
import com.resync.app.ui.viewholders.comments.CommentFooterHolder;
import com.resync.app.holders.RedditViewHolder;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes.dex */
public class EnterExitAnimator extends AbstractAnimator {
    private OnFinishedListener mListener;

    public EnterExitAnimator() {
    }

    @Override // com.resync.app.ui.util.animation.base.AbstractAnimator, androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
        super.animateAdd(viewHolder);
        if (viewHolder instanceof CommentFooterHolder) {
            ViewCompat.setAlpha(viewHolder.itemView, 0.0f);
            this.mPendingAdditions.add((RedditViewHolder) viewHolder);
            return true;
        }
        if (viewHolder instanceof RedditViewHolder) {
            viewHolder.itemView.setTranslationY(SizeHelper.dpToPxFloat(50));
            viewHolder.itemView.setAlpha(0.0f);
            this.mPendingAdditions.add((RedditViewHolder) viewHolder);
            return true;
        }
        Logger.verbose("WAS INVALID: " + viewHolder);
        dispatchAddFinished(viewHolder);
        return false;
    }

    @Override // com.resync.app.ui.util.animation.base.AbstractAnimator, androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateRemove(RecyclerView.ViewHolder viewHolder) {
        super.animateRemove(viewHolder);
        if (viewHolder instanceof RedditViewHolder) {
            this.mPendingRemovals.add((RedditViewHolder) viewHolder);
            return true;
        }
        Logger.verbose("WAS INVALID: " + viewHolder);
        dispatchRemoveFinished(viewHolder);
        return false;
    }

    @Override // com.resync.app.ui.util.animation.base.AbstractAnimator
    protected void dispatchFinishedWhenDone() {
        OnFinishedListener onFinishedListener;
        super.dispatchFinishedWhenDone();
        if (isRunning() || (onFinishedListener = this.mListener) == null) {
            return;
        }
        onFinishedListener.onFinished();
    }

    @Override // com.resync.app.ui.util.animation.base.AbstractAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public void endAnimations() {
        this.mListener = null;
        super.endAnimations();
    }

    @Override // com.resync.app.widgets.CustomItemAnimator
    public long getDuration() {
        return 220L;
    }

    @Override // com.resync.app.widgets.CustomItemAnimator
    public boolean getStaggerAnimations() {
        return true;
    }

    @Override // com.resync.app.widgets.CustomItemAnimator
    public long getStartDelay() {
        return 0L;
    }

    public EnterExitAnimator(OnFinishedListener onFinishedListener) {
        this.mListener = onFinishedListener;
    }

    @Override // com.resync.app.widgets.CustomItemAnimator
    public ObjectAnimator getAddAnimator(RedditViewHolder redditViewHolder) {
        if (redditViewHolder instanceof CommentFooterHolder) {
            return ObjectAnimator.ofFloat(redditViewHolder.itemView, (Property<View, Float>) View.ALPHA, 1.0f).setDuration(1000L);
        }
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(redditViewHolder.itemView, PropertyValuesHolder.ofFloat((Property<?, Float>) View.TRANSLATION_Y, 0.0f), PropertyValuesHolder.ofFloat((Property<?, Float>) View.ALPHA, 1.0f));
        objectAnimatorOfPropertyValuesHolder.setInterpolator(new FastOutSlowInInterpolator());
        return objectAnimatorOfPropertyValuesHolder;
    }

    @Override // com.resync.app.widgets.CustomItemAnimator
    public ObjectAnimator getRemoveAnimator(RedditViewHolder redditViewHolder) {
        if (redditViewHolder instanceof CommentFooterHolder) {
            return ObjectAnimator.ofFloat(redditViewHolder.itemView, (Property<View, Float>) View.ALPHA, 1.0f).setDuration(1000L);
        }
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(redditViewHolder.itemView, PropertyValuesHolder.ofFloat((Property<?, Float>) View.TRANSLATION_Y, SizeHelper.dpToPxFloat(25)), PropertyValuesHolder.ofFloat((Property<?, Float>) View.ALPHA, 0.0f));
        objectAnimatorOfPropertyValuesHolder.setInterpolator(new FastOutSlowInInterpolator());
        return objectAnimatorOfPropertyValuesHolder;
    }
}
