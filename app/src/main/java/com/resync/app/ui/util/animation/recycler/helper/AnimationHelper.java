package com.resync.app.ui.util.animation.recycler.helper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.PowerManager;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewPropertyAnimator;
import android.widget.ImageButton;
import com.resync.app.R;
import com.resync.app.singleton.AccountSingleton;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.widgets.ColorFilterProvider;
import com.resync.app.managers.SaveManager;
import com.resync.app.managers.VoteManager;
import com.resync.app.helpers.ConnectivityHelper;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.util.Logger;
import com.resync.app.util.TestUtils;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes3.dex */
public final class AnimationHelper {
    public static final int MODE_DOWNVOTE = 1;
    public static final int MODE_SAVE = 2;
    public static final int MODE_UPVOTE = 0;

    public interface OnAlphaAnimationListener {
        void onAnimationEnd();
    }

    private AnimationHelper() {
    }

    private static boolean canPerformPostAction(Context context) {
        return AccountSingleton.getInstance().isLoggedIn();
    }

    private static int getModeColor(int mode) {
        if (mode == 0) {
            return -29856;
        }
        if (mode == 1) {
            return -7039745;
        }
        if (mode == 2) {
            return -1324481;
        }
        throw new RuntimeException("Unsupported mode");
    }

    public static void performAction(Context context, Post post, int mode) {
        if (!canPerformPostAction(context)) {
            StaticToast.show(context, R.string.common_generic_error_logged_out);
        } else if (!post.isArchived() || mode == 2) {
            performPostAction(context, post, mode);
        } else {
            StaticToast.show(context, R.string.error_archived);
        }
    }

    public static void performAnimatedPostAction(final Context context, final Post post, final int mode, final View view, final ImageButton imageButton) {
        if (!canPerformPostAction(context)) {
            StaticToast.show(context, R.string.common_generic_error_logged_out);
            return;
        }
        if (post.isArchived() && mode != 2) {
            StaticToast.show(context, R.string.error_archived);
            return;
        }
        if (!ConnectivityHelper.isConnected(context)) {
            StaticToast.showWithContext(context, "You must be online to perform post actions");
            return;
        }
        boolean activating = mode != 0 ? mode != 1 ? !(mode == 2 && post.isSaved()) : !post.isDownvoted() : !post.isUpvoted();
        if (context.getSystemService("power") == null || !(context.getSystemService("power") instanceof PowerManager) || ((PowerManager) context.getSystemService("power")).isPowerSaveMode()) {
            performPostAction(context, post, mode);
            return;
        }
        view.setBackgroundColor(getModeColor(mode));
        imageButton.setColorFilter(ColorFilterProvider.getColorFilter(getModeColor(mode)));
        int right = imageButton.getRight() - (imageButton.getWidth() / 2);
        int bottom = imageButton.getBottom() - (imageButton.getHeight() / 2);
        int revealRadius = (int) SizeHelper.dpToPxFloat(48);
        Animator animatorCreateCircularReveal = activating ? ViewAnimationUtils.createCircularReveal(view, right, bottom, 0.0f, revealRadius) : ViewAnimationUtils.createCircularReveal(view, right, bottom, revealRadius, 0.0f);
        animatorCreateCircularReveal.setDuration(350L);
        animatorCreateCircularReveal.addListener(new AnimatorListenerAdapter() { // from class: com.resync.app.ui.util.animation.recycler.helper.AnimationHelper.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(4);
                imageButton.setEnabled(true);
                AnimationHelper.performPostAction(context, post, mode);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                view.setVisibility(0);
                imageButton.setEnabled(false);
            }
        });
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f);
        objectAnimatorOfFloat.setDuration(350L);
        if (activating) {
            objectAnimatorOfFloat.setStartDelay(50L);
        }
        ObjectAnimator objectAnimatorOfFloat2 = ObjectAnimator.ofFloat(imageButton, "scaleX", 1.35f);
        objectAnimatorOfFloat2.setRepeatCount(1);
        objectAnimatorOfFloat2.setRepeatMode(2);
        objectAnimatorOfFloat2.setDuration(150L);
        ObjectAnimator objectAnimatorOfFloat3 = ObjectAnimator.ofFloat(imageButton, "scaleY", 1.35f);
        objectAnimatorOfFloat3.setRepeatCount(1);
        objectAnimatorOfFloat3.setRepeatMode(2);
        objectAnimatorOfFloat3.setDuration(150L);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimatorOfFloat2, objectAnimatorOfFloat3, animatorCreateCircularReveal, objectAnimatorOfFloat);
        animatorSet.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void performPostAction(Context context, Post post, int mode) {
        if (mode == 0) {
            VoteManager.voteOnPost(context, 0, post, 1);
        } else if (mode == 1) {
            VoteManager.voteOnPost(context, 0, post, -1);
        } else if (mode == 2) {
            SaveManager.toggleSave(context, post);
        }
    }

    public static void startAlphaHide(View view) {
        startAlphaHide(view, null);
    }

    public static void startAlphaReveal(View view) {
        startAlphaReveal(view, null);
    }

    public static void startAlphaHide(final View view, final OnAlphaAnimationListener onAlphaAnimationListener) {
        if (view.animate() != null) {
            view.animate().cancel();
        }
        if (TestUtils.isRobolectric()) {
            view.setVisibility(8);
            return;
        }
        if (view.getVisibility() == 0) {
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
            final ViewPropertyAnimator duration = view.animate().scaleX(0.0f).scaleY(0.0f).setDuration(120L);
            duration.setListener(new AnimatorListenerAdapter() { // from class: com.resync.app.ui.util.animation.recycler.helper.AnimationHelper.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    view.setScaleX(0.0f);
                    view.setScaleY(0.0f);
                    view.setVisibility(8);
                    duration.setListener(null);
                    animator.removeListener(this);
                    animator.removeAllListeners();
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    view.setVisibility(8);
                    duration.setListener(null);
                    animator.removeListener(this);
                    animator.removeAllListeners();
                    OnAlphaAnimationListener onAlphaAnimationListener2 = onAlphaAnimationListener;
                    if (onAlphaAnimationListener2 != null) {
                        onAlphaAnimationListener2.onAnimationEnd();
                    }
                }
            });
            duration.withLayer();
            duration.start();
        }
    }

    public static void startAlphaReveal(final View view, final OnAlphaAnimationListener onAlphaAnimationListener) {
        if (view.animate() != null) {
            view.animate().cancel();
        }
        if (TestUtils.isRobolectric()) {
            view.setVisibility(0);
            return;
        }
        if (view.getVisibility() == 8) {
            view.setVisibility(0);
            view.setScaleX(0.0f);
            view.setScaleY(0.0f);
            final ViewPropertyAnimator duration = view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(120L);
            duration.setListener(new AnimatorListenerAdapter() { // from class: com.resync.app.ui.util.animation.recycler.helper.AnimationHelper.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    view.setScaleX(1.0f);
                    view.setScaleY(1.0f);
                    duration.setListener(null);
                    animator.removeListener(this);
                    animator.removeAllListeners();
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    Logger.verbose("Animation ended");
                    Logger.verbose("ScaleX: " + view.getScaleX());
                    Logger.verbose("ScaleY: " + view.getScaleY());
                    duration.setListener(null);
                    animator.removeListener(this);
                    animator.removeAllListeners();
                    OnAlphaAnimationListener onAlphaAnimationListener2 = onAlphaAnimationListener;
                    if (onAlphaAnimationListener2 != null) {
                        onAlphaAnimationListener2.onAnimationEnd();
                    }
                }
            });
            duration.withLayer();
            duration.start();
        }
    }
}
