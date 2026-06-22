package com.resync.app.ui.util.animation.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.util.Property;
import android.view.View;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.resync.app.ui.util.animation.recycler.helper.TransientHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import com.resync.app.widgets.CustomItemAnimator;
import com.resync.app.holders.RedditViewHolder;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: com.resync.app.ui.util.animation.base.a */
/* JADX INFO: compiled from: AbstractAnimator.java */
/* JADX INFO: loaded from: classes2.dex */
public abstract class AbstractAnimator extends SimpleItemAnimator implements CustomItemAnimator {
    private static final boolean DEBUG = false;
    protected static final int MODE_ADDITION = 1;
    protected static final int MODE_MOVE = 2;
    protected static final int MODE_REMOVAL = 0;
    private final String TAG;
    protected ArrayList<Animator> mAnimations = new ArrayList<>();
    protected ArrayList<RedditViewHolder> mPendingRemovals = new LoggingRemovalsList();
    protected ArrayList<RedditViewHolder> mPendingAdditions = new LoggingAdditionsList();
    protected ArrayList<RedditViewHolder> mPendingMoves = new LoggingMovesList();
    protected int mRemovalHeight = 0;
    protected int mRemovalFirstPosition = 0;
    protected int mAdditionsHeight = 0;
    protected int mAdditionsFirstPosition = 0;

    /* JADX INFO: renamed from: com.resync.app.ui.util.animation.base.a$a */
    /* JADX INFO: compiled from: AbstractAnimator.java */
    class LoggingRemovalsList extends ArrayList<RedditViewHolder> {
        LoggingRemovalsList() {
        }

        @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(RedditViewHolder redditViewHolder) {
            Logger.verbose(AbstractAnimator.this.TAG, "Animate remove added: " + redditViewHolder);
            return super.add(redditViewHolder);
        }
    }

    /* JADX INFO: renamed from: com.resync.app.ui.util.animation.base.a$b */
    /* JADX INFO: compiled from: AbstractAnimator.java */
    class LoggingAdditionsList extends ArrayList<RedditViewHolder> {
        LoggingAdditionsList() {
        }

        @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(RedditViewHolder redditViewHolder) {
            Logger.verbose(AbstractAnimator.this.TAG, "Animate addition added: " + redditViewHolder);
            return super.add(redditViewHolder);
        }
    }

    /* JADX INFO: renamed from: com.resync.app.ui.util.animation.base.a$c */
    /* JADX INFO: compiled from: AbstractAnimator.java */
    class LoggingMovesList extends ArrayList<RedditViewHolder> {
        LoggingMovesList() {
        }

        @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(RedditViewHolder redditViewHolder) {
            Logger.verbose(AbstractAnimator.this.TAG, "Animate move added: " + redditViewHolder);
            return super.add(redditViewHolder);
        }
    }

    /* JADX INFO: renamed from: com.resync.app.ui.util.animation.base.a$d */
    /* JADX INFO: compiled from: AbstractAnimator.java */
    class AnimatorFinishListener extends AnimatorListenerAdapter {

        final /* synthetic */ RedditViewHolder viewHolder;
        final /* synthetic */ int mode;
        final /* synthetic */ Animator animator;

        AnimatorFinishListener(RedditViewHolder redditViewHolder, int mode, Animator animator) {
            this.viewHolder = redditViewHolder;
            this.mode = mode;
            this.animator = animator;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            AbstractAnimator.this.onAnimatorFinished(this.viewHolder, this.animator, this.mode);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            AbstractAnimator.this.onAnimatorFinished(this.viewHolder, this.animator, this.mode);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            AbstractAnimator.this.onAnimationStarted(this.viewHolder, this.mode);
        }
    }

    /* JADX INFO: renamed from: com.resync.app.ui.util.animation.base.a$e */
    /* JADX INFO: compiled from: AbstractAnimator.java */
    class RemovalsComparator implements Comparator<RedditViewHolder> {
        RemovalsComparator() {
        }

        @Override // java.util.Comparator
        public int compare(RedditViewHolder redditViewHolder, RedditViewHolder redditViewHolder2) {
            return Integer.compare(redditViewHolder.getLayoutPosition(), redditViewHolder2.getLayoutPosition());
        }
    }

    /* JADX INFO: renamed from: com.resync.app.ui.util.animation.base.a$f */
    /* JADX INFO: compiled from: AbstractAnimator.java */
    class MovesComparator implements Comparator<RedditViewHolder> {
        MovesComparator() {
        }

        @Override // java.util.Comparator
        public int compare(RedditViewHolder redditViewHolder, RedditViewHolder redditViewHolder2) {
            return Integer.compare(redditViewHolder.getLayoutPosition(), redditViewHolder2.getLayoutPosition());
        }
    }

    /* JADX INFO: renamed from: com.resync.app.ui.util.animation.base.a$g */
    /* JADX INFO: compiled from: AbstractAnimator.java */
    class AdditionsComparator implements Comparator<RedditViewHolder> {
        AdditionsComparator() {
        }

        @Override // java.util.Comparator
        public int compare(RedditViewHolder redditViewHolder, RedditViewHolder redditViewHolder2) {
            return Integer.compare(redditViewHolder.getLayoutPosition(), redditViewHolder2.getLayoutPosition());
        }
    }

    /* JADX INFO: renamed from: com.resync.app.ui.util.animation.base.a$h */
    /* JADX INFO: compiled from: AbstractAnimator.java */
    class InvalidAnimationModeException extends RuntimeException {
        public InvalidAnimationModeException() {
            super("Invalid animation mode");
        }
    }

    public AbstractAnimator() {
        setSupportsChangeAnimations(true);
        String simpleName = getClass().getSimpleName();
        this.TAG = simpleName;
        Logger.verbose(simpleName, "Creating a new animator: " + simpleName);
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        endAnimation(holder);
        return true;
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public final boolean animateChange(RecyclerView.ViewHolder holder, RecyclerView.ViewHolder holder2, int fromX, int fromY, int toX, int toY) {
        if (holder2 instanceof RedditViewHolder) {
            return animateMove(holder, fromX, fromY, toX, toY);
        }
        if (holder2 != null) {
            dispatchChangeFinished(holder2, false);
        } else {
            dispatchChangeFinished(holder, true);
        }
        return false;
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        endAnimation(holder);
        if (!(holder instanceof RedditViewHolder)) {
            dispatchMoveFinished(holder);
            return false;
        }
        View view = holder.itemView;
        if (view != null) {
            if (toY - ((int) (fromY + androidx.core.view.ViewCompat.getTranslationY(view))) == 0) {
                dispatchMoveFinished(holder);
                return false;
            }
            androidx.core.view.ViewCompat.setTranslationY(view, -(toY - ((int) (fromY + androidx.core.view.ViewCompat.getTranslationY(view)))));
        }
        this.mPendingMoves.add((RedditViewHolder) holder);
        return true;
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        endAnimation(holder);
        return true;
    }

    protected void calculateViewHolderValues() {
        this.mRemovalHeight = 0;
        this.mRemovalFirstPosition = 0;
        this.mAdditionsHeight = 0;
        Iterator<RedditViewHolder> it = this.mPendingRemovals.iterator();
        while (it.hasNext()) {
            this.mRemovalHeight += it.next().itemView.getHeight();
        }
        if (this.mPendingRemovals.size() > 0) {
            this.mRemovalFirstPosition = this.mPendingRemovals.get(0).getLayoutPosition();
        }
        Iterator<RedditViewHolder> it2 = this.mPendingAdditions.iterator();
        while (it2.hasNext()) {
            this.mAdditionsHeight += it2.next().itemView.getHeight();
        }
        if (this.mPendingAdditions.size() > 0) {
            this.mAdditionsFirstPosition = this.mPendingAdditions.get(0).getLayoutPosition();
        }
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public final boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder holder) {
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public final boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder holder, List<Object> list) {
        return true;
    }

    protected final void cancelPendingList(int mode) {
        ArrayList<RedditViewHolder> pendingList = getPendingList(mode);
        for (int size = pendingList.size() - 1; size >= 0; size--) {
            RedditViewHolder redditViewHolderRemove = pendingList.remove(size);
            endAnimation(redditViewHolderRemove);
            if (mode == 0) {
                dispatchRemoveFinished(redditViewHolderRemove);
            } else if (1 == mode) {
                dispatchAddFinished(redditViewHolderRemove);
            } else {
                if (2 != mode) {
                    throw new InvalidAnimationModeException();
                }
                dispatchMoveFinished(redditViewHolderRemove);
            }
        }
    }

    protected void dispatchFinishedWhenDone() {
        if (isRunning()) {
            return;
        }
        Logger.verbose(this.TAG, "All moves finished!");
        dispatchAnimationsFinished();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public final void endAnimation(RecyclerView.ViewHolder holder) {
        if (holder instanceof RedditViewHolder) {
            ((RedditViewHolder) holder).cancelAnimation();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public void endAnimations() {
        Logger.verbose(this.TAG, "End all animations!");
        try {
            if (this.mAnimations.size() > 0) {
                for (int size = this.mAnimations.size() - 1; size >= 0; size--) {
                    Animator animatorRemove = this.mAnimations.remove(size);
                    Logger.verbose(this.TAG, "Cancelled an active animation");
                    animatorRemove.cancel();
                    this.mAnimations.remove(animatorRemove);
                }
            }
        } catch (Exception e) {
            Logger.logException(e);
        }
        if (this.mPendingMoves.size() > 0) {
            Logger.verbose(this.TAG, "Cancelling pending moves: " + this.mPendingMoves.size());
            cancelPendingList(2);
        }
        if (this.mPendingRemovals.size() > 0) {
            Logger.verbose(this.TAG, "Cancelling pending removals: " + this.mPendingRemovals.size());
            cancelPendingList(0);
        }
        if (this.mPendingAdditions.size() > 0) {
            Logger.verbose(this.TAG, "Cancelling pending additions: " + this.mPendingAdditions.size());
            cancelPendingList(1);
        }
        dispatchAnimationsFinished();
    }

    public Animator getMoveAnimator(RedditViewHolder redditViewHolder) {
        return ObjectAnimator.ofFloat(redditViewHolder.itemView, (Property<View, Float>) View.TRANSLATION_Y, 0.0f);
    }

    protected final Animator getObjectAnimator(RedditViewHolder redditViewHolder, int mode) {
        Animator moveAnimator;
        if (mode == 0) {
            moveAnimator = getRemoveAnimator(redditViewHolder);
        } else if (1 == mode) {
            moveAnimator = getAddAnimator(redditViewHolder);
        } else {
            if (2 != mode) {
                throw new InvalidAnimationModeException();
            }
            moveAnimator = getMoveAnimator(redditViewHolder);
        }
        moveAnimator.setInterpolator(new FastOutSlowInInterpolator());
        if (getStartDelay() > 0) {
            moveAnimator.setStartDelay(moveAnimator.getStartDelay() + (((long) getPendingList(mode).indexOf(redditViewHolder)) * getStartDelay()));
        }
        moveAnimator.setDuration(getDuration());
        TransientHelper.setHasTransientState(redditViewHolder.getItemView(), true);
        moveAnimator.addListener(new AnimatorFinishListener(redditViewHolder, mode, moveAnimator));
        return redditViewHolder.setAnimator(moveAnimator);
    }

    protected final ArrayList<RedditViewHolder> getPendingList(int mode) {
        if (mode == 0) {
            return this.mPendingRemovals;
        }
        if (1 == mode) {
            return this.mPendingAdditions;
        }
        if (2 == mode) {
            return this.mPendingMoves;
        }
        throw new InvalidAnimationModeException();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public final boolean isRunning() {
        if (!this.mAnimations.isEmpty()) {
            Iterator<Animator> it = this.mAnimations.iterator();
            while (it.hasNext()) {
                if (it.next().isRunning()) {
                    return true;
                }
            }
        }
        return (this.mPendingRemovals.isEmpty() && this.mPendingAdditions.isEmpty() && this.mPendingMoves.isEmpty()) ? false : true;
    }

    protected final void onAnimationStarted(RedditViewHolder redditViewHolder, int mode) {
        if (mode == 0) {
            Logger.verbose(this.TAG, "Remove started");
            dispatchRemoveStarting(redditViewHolder);
        } else if (1 == mode) {
            Logger.verbose(this.TAG, "Add started");
            dispatchAddStarting(redditViewHolder);
        } else {
            if (2 != mode) {
                throw new InvalidAnimationModeException();
            }
            Logger.verbose(this.TAG, "Move started");
            dispatchMoveStarting(redditViewHolder);
        }
    }

    protected final void onAnimatorFinished(RedditViewHolder redditViewHolder, Animator animator, int mode) {
        if (redditViewHolder != null && redditViewHolder.getItemView() != null) {
            androidx.core.view.ViewCompat.setAlpha(redditViewHolder.getItemView(), 1.0f);
            androidx.core.view.ViewCompat.setTranslationX(redditViewHolder.getItemView(), 0.0f);
            androidx.core.view.ViewCompat.setTranslationY(redditViewHolder.getItemView(), 0.0f);
            TransientHelper.setHasTransientState(redditViewHolder.getItemView(), false);
        }
        if (animator != null) {
            animator.removeAllListeners();
        }
        if (mode == 0) {
            Logger.verbose(this.TAG, "Remove finished");
            dispatchRemoveFinished(redditViewHolder);
        } else if (1 == mode) {
            Logger.verbose(this.TAG, "Add finished");
            dispatchAddFinished(redditViewHolder);
        } else {
            if (2 != mode) {
                throw new InvalidAnimationModeException();
            }
            Logger.verbose(this.TAG, "Move finished");
            dispatchMoveFinished(redditViewHolder);
        }
        boolean zRemove = this.mAnimations.remove(animator);
        Logger.verbose(this.TAG, "Removed: " + zRemove);
        redditViewHolder.setAnimator(null);
        dispatchFinishedWhenDone();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public final void runPendingAnimations() {
        boolean hasRemovals = !this.mPendingRemovals.isEmpty();
        boolean hasMoves = !this.mPendingMoves.isEmpty();
        boolean hasAdditions = !this.mPendingAdditions.isEmpty();
        Logger.verbose(this.TAG, "Pending removals: " + this.mPendingRemovals.size());
        Logger.verbose(this.TAG, "Pending moves: " + this.mPendingMoves.size());
        Logger.verbose(this.TAG, "Pending additions: " + this.mPendingAdditions.size());
        Collections.sort(this.mPendingRemovals, new RemovalsComparator());
        Collections.sort(this.mPendingMoves, new MovesComparator());
        Collections.sort(this.mPendingAdditions, new AdditionsComparator());
        calculateViewHolderValues();
        if (hasRemovals || hasMoves || hasAdditions) {
            if (hasRemovals) {
                Iterator<RedditViewHolder> it = this.mPendingRemovals.iterator();
                while (it.hasNext()) {
                    this.mAnimations.add(getObjectAnimator(it.next(), 0));
                }
                this.mPendingRemovals.clear();
            }
            if (hasMoves) {
                Iterator<RedditViewHolder> it2 = this.mPendingMoves.iterator();
                while (it2.hasNext()) {
                    Animator objectAnimator = getObjectAnimator(it2.next(), 2);
                    if (getStaggerAnimations() && hasRemovals) {
                        objectAnimator.setStartDelay(objectAnimator.getStartDelay() + getDuration());
                    }
                    this.mAnimations.add(objectAnimator);
                }
                this.mPendingMoves.clear();
            }
            if (hasAdditions) {
                Iterator<RedditViewHolder> it3 = this.mPendingAdditions.iterator();
                while (it3.hasNext()) {
                    Animator objectAnimator2 = getObjectAnimator(it3.next(), 1);
                    if (getStaggerAnimations()) {
                        long startDelay = objectAnimator2.getStartDelay();
                        if (hasRemovals) {
                            startDelay += getDuration();
                        }
                        if (hasMoves) {
                            startDelay += getDuration();
                        }
                        objectAnimator2.setStartDelay(startDelay);
                    }
                    this.mAnimations.add(objectAnimator2);
                }
                this.mPendingAdditions.clear();
            }
            Iterator<Animator> it4 = this.mAnimations.iterator();
            while (it4.hasNext()) {
                it4.next().start();
            }
        }
    }
}
