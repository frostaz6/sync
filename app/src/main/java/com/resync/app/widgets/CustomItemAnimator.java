package com.resync.app.widgets;

import android.animation.Animator;
import com.resync.app.holders.RedditViewHolder;

/* JADX INFO: renamed from: ib.a */
/* JADX INFO: compiled from: CustomItemAnimator.java */
/* JADX INFO: loaded from: classes2.dex */
public interface CustomItemAnimator {
    Animator getAddAnimator(RedditViewHolder redditViewHolder);

    long getDuration();

    Animator getRemoveAnimator(RedditViewHolder redditViewHolder);

    boolean getStaggerAnimations();

    long getStartDelay();
}
