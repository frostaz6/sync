package com.resync.app.ui.views.comments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.events.AutoRefreshEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: loaded from: classes3.dex */
public class SportsButton extends FrameLayout {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    RelativeLayout wrapper;
    TextView label;
    TextView stopButton;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    TextView refreshButton;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private CountdownRunnable countdown;

    class StopClickListener implements View.OnClickListener {
        StopClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SportsButton.this.cancelCountdown();
            SportsButton.this.hide();
        }
    }

    class RefreshClickListener implements View.OnClickListener {
        RefreshClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SportsButton.this.cancelCountdown();
            SportsButton.this.showRefreshing();
            BusSingleton.getBus().post(new AutoRefreshEvent());
        }
    }

    class ShowAnimatorListener implements Animator.AnimatorListener {
        ShowAnimatorListener() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            for (int childIndex = 0; childIndex < SportsButton.this.wrapper.getChildCount(); childIndex++) {
                SportsButton.this.wrapper.getChildAt(childIndex).animate().setDuration(220L).alpha(1.0f);
            }
            animator.removeListener(this);
            animator.removeAllListeners();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    class ShowUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        ShowUpdateListener() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            SportsButton.this.getLayoutParams().height = (int) ((Float) valueAnimator.getAnimatedValue()).floatValue();
            SportsButton.this.requestLayout();
        }
    }

    class HideAnimatorListener implements Animator.AnimatorListener {
        HideAnimatorListener() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            SportsButton.this.setVisibility(8);
            animator.removeListener(this);
            animator.removeAllListeners();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    class HideUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        HideUpdateListener() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            SportsButton.this.getLayoutParams().height = (int) ((Float) valueAnimator.getAnimatedValue()).floatValue();
            SportsButton.this.requestLayout();
        }
    }

    class CountdownRunnable implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private boolean active = true;
        private int seconds;

        public CountdownRunnable(int seconds) {
            this.seconds = seconds;
        }

        public void cancel() {
            this.active = false;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.active) {
                SportsButton.this.scheduleCountdown(this.seconds - 1);
            }
        }
    }

    public SportsButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        addView(View.inflate(getContext(), R.layout.view_sports, null));
        setVisibility(8);
        this.wrapper = (RelativeLayout) findViewById(R.id.sports_wrapper);
        this.label = (TextView) findViewById(R.id.sports_label);
        TextView textView = (TextView) findViewById(R.id.sports_stop);
        this.stopButton = textView;
        textView.setOnClickListener(new StopClickListener());
        TextView textView2 = (TextView) findViewById(R.id.sports_refresh);
        this.refreshButton = textView2;
        textView2.setOnClickListener(new RefreshClickListener());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleCountdown(int seconds) {
        if (seconds == 0) {
            showRefreshing();
            BusSingleton.getBus().post(new AutoRefreshEvent());
            return;
        }
        updateCountdown(seconds);
        CountdownRunnable existingCountdown = this.countdown;
        if (existingCountdown != null) {
            existingCountdown.cancel();
        }
        CountdownRunnable newCountdown = new CountdownRunnable(seconds);
        this.countdown = newCountdown;
        postDelayed(newCountdown, 1000L);
    }

    public void cancelCountdown() {
        CountdownRunnable existingCountdown = this.countdown;
        if (existingCountdown != null) {
            existingCountdown.cancel();
            removeCallbacks(this.countdown);
        }
    }

    public void hide() {
        cancelCountdown();
        for (int childIndex = 0; childIndex < this.wrapper.getChildCount(); childIndex++) {
            this.wrapper.getChildAt(childIndex).setAlpha(0.0f);
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(getHeight(), 0.0f);
        valueAnimatorOfFloat.setDuration(220L);
        valueAnimatorOfFloat.addListener(new HideAnimatorListener());
        valueAnimatorOfFloat.addUpdateListener(new HideUpdateListener());
        valueAnimatorOfFloat.start();
    }

    public void showRefreshing() {
        this.label.setText("Comments refreshing");
        this.refreshButton.setEnabled(false);
        this.refreshButton.setAlpha(0.25f);
    }

    public void updateCountdown(int seconds) {
        this.label.setText("Refreshing in " + seconds + "s");
        this.refreshButton.setEnabled(true);
        this.refreshButton.setAlpha(1.0f);
    }

    public void show() {
        setVisibility(0);
        float targetHeight = SizeHelper.dpToPx(48);
        getLayoutParams().height = 0;
        requestLayout();
        for (int childIndex = 0; childIndex < this.wrapper.getChildCount(); childIndex++) {
            this.wrapper.getChildAt(childIndex).setAlpha(0.0f);
        }
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(0.0f, targetHeight);
        valueAnimatorOfFloat.setDuration(220L);
        valueAnimatorOfFloat.addListener(new ShowAnimatorListener());
        valueAnimatorOfFloat.addUpdateListener(new ShowUpdateListener());
        valueAnimatorOfFloat.start();
    }

    public void startCountdown() {
        cancelCountdown();
        scheduleCountdown(SettingsSingleton.getSettings().commentAutoRefreshValue);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        cancelCountdown();
        super.onDetachedFromWindow();
    }
}
