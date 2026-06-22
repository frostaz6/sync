package com.resync.app.ui.views;

import android.view.View;
import android.widget.ImageButton;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class TTSView_ViewBinding implements Unbinder {
    private TTSView target;
    private View view1;
    private View view2;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private View view3;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private View view4;
    private View view5;

    class StopClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ TTSView target;

        StopClickListener(TTSView tTSView) {
            this.target = tTSView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onStopButtonClicked();
        }
    }

    class PreviousClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ TTSView target;

        PreviousClickListener(TTSView tTSView) {
            this.target = tTSView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onPreviousButtonClicked();
        }
    }

    class PlayClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ TTSView target;

        PlayClickListener(TTSView tTSView) {
            this.target = tTSView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onPlayButtonClicked();
        }
    }

    class NextClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ TTSView target;

        NextClickListener(TTSView tTSView) {
            this.target = tTSView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onNextButtonClicked();
        }
    }

    class MoreClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ TTSView target;

        MoreClickListener(TTSView tTSView) {
            this.target = tTSView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onMoreButtonClicked();
        }
    }

    public TTSView_ViewBinding(TTSView tTSView, View view) {
        this.target = tTSView;
        View viewC = Utils.findRequiredView(view, R.id.tts_stop, "field 'buttonStop' and method 'onStopButtonClicked'");
        tTSView.buttonStop = (ImageButton) Utils.castView(viewC, R.id.tts_stop, "field 'buttonStop'", ImageButton.class);
        this.view1 = viewC;
        viewC.setOnClickListener(new StopClickListener(tTSView));
        View viewC2 = Utils.findRequiredView(view, R.id.tts_previous, "field 'buttonPrevious' and method 'onPreviousButtonClicked'");
        tTSView.buttonPrevious = (ImageButton) Utils.castView(viewC2, R.id.tts_previous, "field 'buttonPrevious'", ImageButton.class);
        this.view2 = viewC2;
        viewC2.setOnClickListener(new PreviousClickListener(tTSView));
        View viewC3 = Utils.findRequiredView(view, R.id.tts_play, "field 'buttonPlay' and method 'onPlayButtonClicked'");
        tTSView.buttonPlay = (ImageButton) Utils.castView(viewC3, R.id.tts_play, "field 'buttonPlay'", ImageButton.class);
        this.view3 = viewC3;
        viewC3.setOnClickListener(new PlayClickListener(tTSView));
        View viewC4 = Utils.findRequiredView(view, R.id.tts_next, "field 'buttonNext' and method 'onNextButtonClicked'");
        tTSView.buttonNext = (ImageButton) Utils.castView(viewC4, R.id.tts_next, "field 'buttonNext'", ImageButton.class);
        this.view4 = viewC4;
        viewC4.setOnClickListener(new NextClickListener(tTSView));
        View viewC5 = Utils.findRequiredView(view, R.id.tts_more, "field 'buttonMore' and method 'onMoreButtonClicked'");
        tTSView.buttonMore = (ImageButton) Utils.castView(viewC5, R.id.tts_more, "field 'buttonMore'", ImageButton.class);
        this.view5 = viewC5;
        viewC5.setOnClickListener(new MoreClickListener(tTSView));
    }

    @Override
    public void unbind() {
        TTSView tTSView = this.target;
        if (tTSView == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;
        tTSView.buttonStop = null;
        tTSView.buttonPrevious = null;
        tTSView.buttonPlay = null;
        tTSView.buttonNext = null;
        tTSView.buttonMore = null;
        this.view1.setOnClickListener(null);
        this.view1 = null;
        this.view2.setOnClickListener(null);
        this.view2 = null;
        this.view3.setOnClickListener(null);
        this.view3 = null;
        this.view4.setOnClickListener(null);
        this.view4 = null;
        this.view5.setOnClickListener(null);
        this.view5 = null;
    }
}
