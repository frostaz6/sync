package com.resync.app.ui.views.image_progressbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.core.graphics.ColorUtils;
import butterknife.ButterKnife;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.resync.app.R;
import com.resync.app.widgets.ImageEventListener;
import com.resync.app.util.Logger;

/* JADX INFO: loaded from: classes3.dex */
public class ImageProgressBar extends FrameLayout {

    static final String TAG = ImageProgressBar.class.getSimpleName();

    int state;
    ShowRunnable showRunnable;
    ImageEventListener listener;

    ImageView cancelButton;

    ProgressBar downloadingProgressBar;

    CircularProgressIndicator interminateProgressBar;

    class CancelClickListener implements View.OnClickListener {
        CancelClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ImageProgressBar imageProgressBar = ImageProgressBar.this;
            if (imageProgressBar.listener != null && imageProgressBar.getState() == 1) {
                ImageProgressBar.this.setState(2);
                ImageProgressBar.this.listener.onCancel();
                return;
            }
            ImageProgressBar imageProgressBar2 = ImageProgressBar.this;
            if (imageProgressBar2.listener == null || imageProgressBar2.getState() != 2) {
                return;
            }
            ImageProgressBar.this.setState(1);
            ImageProgressBar.this.listener.onRetry();
        }
    }

    class ShowRunnable implements Runnable {

        boolean cancelled;

        ShowRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.cancelled) {
                return;
            }
            ImageProgressBar.this.setVisibility(0);
            ImageProgressBar.this.showRunnable = null;
        }
    }

    public ImageProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    void cancelShow() {
        ShowRunnable runnable = this.showRunnable;
        if (runnable != null) {
            runnable.cancelled = true;
            removeCallbacks(runnable);
        }
    }

    public int getState() {
        return this.state;
    }

    public void hide() {
        Logger.verbose(TAG, "Hiding progress bar!");
        cancelShow();
        setVisibility(8);
    }

    void init() {
        ButterKnife.bind(this, View.inflate(getContext(), R.layout.view_image_progress_bar, this));
        setState(0);
        this.cancelButton.setOnClickListener(new CancelClickListener());
        this.interminateProgressBar.setIndicatorColor(-1);
        this.interminateProgressBar.setTrackColor(ColorUtils.setAlphaComponent(-1, 102));
    }

    public void setListener(ImageEventListener imageEventListener) {
        this.listener = imageEventListener;
    }

    public void onStateChanged(int state) {
    }

    public void setState(int state) {
        this.state = state;
        onStateChanged(0);
        this.interminateProgressBar.setFocusable(false);
        this.cancelButton.setFocusable(false);
        int currentState = this.state;
        if (currentState == 0) {
            this.interminateProgressBar.setVisibility(0);
            this.cancelButton.setVisibility(8);
        } else if (currentState == 1) {
            this.interminateProgressBar.setVisibility(0);
            this.cancelButton.setVisibility(8);
        } else {
            if (currentState != 2) {
                throw new InvalidStateException(this.state);
            }
            this.interminateProgressBar.setVisibility(8);
            this.cancelButton.setVisibility(0);
            this.cancelButton.setImageResource(R.drawable.ic_refresh_white_24dp);
        }
    }

    public void show() {
        ShowRunnable runnable = this.showRunnable;
        if ((runnable == null || runnable.cancelled) && getVisibility() == 8) {
            Logger.verbose(TAG, "Showing progress bar!");
            ShowRunnable newRunnable = new ShowRunnable();
            this.showRunnable = newRunnable;
            postDelayed(newRunnable, 380L);
        }
    }
}
