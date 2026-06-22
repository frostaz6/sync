package com.resync.app.ui.views.responsive;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import com.resync.app.ui.views.comments.RowDecorator;
import com.resync.app.ui.views.text.BaseTextView;

/* JADX INFO: loaded from: classes3.dex */
public class LoadingTextView extends BaseTextView implements RowDecorator.RowDecoratorHolder {
    String loadingText0;
    String loadingText1;
    String loadingText2;
    String loadingText3;
    private RowDecorator rowDecorator;
    private boolean loading;
    private int frame;
    private Runnable tickRunnable;

    class TickRunnable implements Runnable {
        TickRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (LoadingTextView.this.loading) {
                LoadingTextView loadingTextView = LoadingTextView.this;
                loadingTextView.setLoading(loadingTextView.loading);
            }
        }
    }

    public LoadingTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.loadingText0 = "Loading";
        this.loadingText1 = "Loading.";
        this.loadingText2 = "Loading..";
        this.loadingText3 = "Loading...";
        if (Build.VERSION.SDK_INT >= 29) {
            setForceDarkAllowed(false);
        }
        this.rowDecorator = new RowDecorator(context, this, true);
    }

    public void stop() {
        Runnable runnable = this.tickRunnable;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
        this.loading = false;
    }

    public void setLoading(boolean loading) {
        if (!this.loading || !loading) {
            this.frame = 0;
        }
        this.loading = loading;
        if (loading) {
            int frame = this.frame;
            if (frame == 0) {
                setText(this.loadingText0);
            } else if (frame == 1) {
                setText(this.loadingText1);
            } else if (frame == 2) {
                setText(this.loadingText2);
            } else if (frame == 3) {
                setText(this.loadingText3);
            }
            int nextFrame = this.frame + 1;
            this.frame = nextFrame;
            if (nextFrame > 3) {
                this.frame = 0;
            }
        }
        if (this.loading) {
            TickRunnable tickRunnable = new TickRunnable();
            this.tickRunnable = tickRunnable;
            postDelayed(tickRunnable, 350L);
        }
    }

    public void configure(int position, int depth, boolean argA, boolean compact, boolean drawDivider) {
        this.rowDecorator.setup(position, depth, compact, drawDivider);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        this.rowDecorator.draw(canvas);
        super.onDraw(canvas);
    }

    @Override // android.view.View
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        this.rowDecorator.setSize(width, height);
        super.onSizeChanged(width, height, oldWidth, oldHeight);
    }
}
