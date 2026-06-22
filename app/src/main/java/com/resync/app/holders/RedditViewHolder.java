package com.resync.app.holders;

import android.animation.Animator;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import com.resync.app.widgets.RecyclableView;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: ob.b */
/* JADX INFO: compiled from: RedditViewHolder.java */
/* JADX INFO: loaded from: classes3.dex */
public abstract class RedditViewHolder extends BaseViewHolder {
    protected Animator animator;

    public RedditViewHolder(Context context, View view) {
        super(context, view);
    }

    public void cancelAnimation() {
        Logger.verbose("Cancelled");
        Animator animator = this.animator;
        if (animator != null) {
            animator.cancel();
            this.animator = null;
            if (ViewCompat.getTranslationX(this.itemView) != 0.0f) {
                ViewCompat.setTranslationX(this.itemView, 0.0f);
            }
            if (ViewCompat.getTranslationY(this.itemView) != 0.0f) {
                ViewCompat.setTranslationY(this.itemView, 0.0f);
            }
            if (ViewCompat.getAlpha(this.itemView) != 1.0f) {
                ViewCompat.setAlpha(this.itemView, 1.0f);
            }
        }
    }

    public View getItemView() {
        return this.itemView;
    }

    public void recycleChildren(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            KeyEvent.Callback childAt = viewGroup.getChildAt(i);
            if (childAt instanceof ViewGroup) {
                recycleChildren((ViewGroup) childAt);
                if (childAt instanceof RecyclableView) {
                    ((RecyclableView) childAt).recycle();
                }
            } else if (childAt instanceof RecyclableView) {
                ((RecyclableView) childAt).recycle();
            }
        }
    }

    public void recycle() {
        cancelAnimation();
        KeyEvent.Callback callback = this.itemView;
        if (callback instanceof ViewGroup) {
            recycleChildren((ViewGroup) callback);
        } else if (callback instanceof RecyclableView) {
            ((RecyclableView) callback).recycle();
        }
    }

    public Animator setAnimator(Animator animator) {
        cancelAnimation();
        this.animator = animator;
        return animator;
    }
}
