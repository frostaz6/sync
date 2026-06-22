package com.resync.app.ui.views;

import android.content.Context;
import android.database.Cursor;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.core.view.WindowInsetsCompat;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.exoplayer2.C;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.internal.ViewUtils;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.resync.app.R;
import com.resync.app.ui.views.TTSView;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.StartTts;
import com.resync.app.events.StopTts;
import com.resync.app.helpers.TtsNavigation;
import com.resync.app.util.Logger;
import com.resync.app.util.RippleUtils;
import com.resync.app.model.Post;
import com.resync.app.presenters.CommentsPresenter;

/* JADX INFO: loaded from: classes3.dex */
public class TTSView extends LinearLayout {

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    public static final String TAG = TTSView.class.getSimpleName();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private TextToSpeech tts;
    private boolean initialized;

    ImageButton buttonMore;

    ImageButton buttonNext;

    ImageButton buttonPlay;

    ImageButton buttonPrevious;

    ImageButton buttonStop;
    private CommentsPresenter presenter;

    class TtsInitListener implements TextToSpeech.OnInitListener {
        TtsInitListener() {
        }

        @Override // android.speech.tts.TextToSpeech.OnInitListener
        public void onInit(int status) {
            if (status == 0) {
                TTSView.this.initialized = true;
            }
        }
    }

    class LanguageFailureListener implements OnFailureListener {
        LanguageFailureListener() {
        }

        @Override // com.google.android.gms.tasks.OnFailureListener
        public void onFailure(Exception exc) {
        }
    }

    class LanguageSuccessListener implements OnSuccessListener<String> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String text;

        LanguageSuccessListener(String str) {
            this.text = str;
        }

        @Override // com.google.android.gms.tasks.OnSuccessListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onSuccess(String str) {
            Logger.verbose("reddit_sync", "Language: " + str);
            Locale locale = (str.equals(C.LANGUAGE_UNDETERMINED) || str.equals("en")) ? TTSView.this.getResources().getConfiguration().locale : new Locale(str);
            Logger.verbose(TTSView.TAG, "Setting locale: " + locale.getCountry());
            TTSView.this.tts.setLanguage(locale);
            TTSView.this.speakText(this.text);
        }
    }

    class UtteranceListener extends UtteranceProgressListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String[] segments;

        class KeywordRunnable implements Runnable {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ String keyword;

            KeywordRunnable(String str) {
                this.keyword = str;
            }

            @Override // java.lang.Runnable
            public void run() {
                TTSView.this.presenter.setKeywordFilter(this.keyword);
            }
        }

        class NextRunnable implements Runnable {
            NextRunnable() {
            }

            @Override // java.lang.Runnable
            public void run() {
                TTSView.this.onNextButtonClicked();
            }
        }

        UtteranceListener(String[] strArr) {
            this.segments = strArr;
        }

        @Override // android.speech.tts.UtteranceProgressListener
        public void onDone(String str) {
            Logger.verbose(TTSView.TAG, "Is done: " + str);
            if (str.equals(this.segments[this.segments.length - 1])) {
                TTSView.this.post(new NextRunnable());
            }
        }

        @Override // android.speech.tts.UtteranceProgressListener
        public void onError(String str) {
            Logger.verbose(TTSView.TAG, "On error: " + str);
        }

        @Override // android.speech.tts.UtteranceProgressListener
        public void onStart(String str) {
            Logger.verbose(TTSView.TAG, "On start: " + str);
            TTSView.this.post(new KeywordRunnable(str));
        }
    }

    public TTSView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.initialized = false;
        setOrientation(0);
        setBackgroundColor(MonetUtil.getToolbarColorAuto());
        LayoutInflater.from(context).inflate(R.layout.view_tts, (ViewGroup) this, true);
        ButterKnife.bind(this);
        this.buttonStop.setBackground(RippleUtils.getNoBoundsRipple());
        this.buttonPrevious.setBackground(RippleUtils.getNoBoundsRipple());
        this.buttonPlay.setBackground(RippleUtils.getNoBoundsRipple());
        this.buttonNext.setBackground(RippleUtils.getNoBoundsRipple());
        this.buttonMore.setBackground(RippleUtils.getNoBoundsRipple());
        ViewUtils.doOnApplyWindowInsets(this, new ViewUtils.OnApplyWindowInsetsListener() { // from class: wb.a
            @Override // com.google.android.material.internal.ViewUtils.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
                return TTSView.applyBottomInset(view, windowInsetsCompat, relativePadding);
            }
        });
        this.tts = new TextToSpeech(getContext(), new TtsInitListener());
    }

    private int findCurrentPosition() {
        if (!this.presenter.hasScrollTarget()) {
            return -1;
        }
        String targetId = this.presenter.getScrollTarget().getId();
        Cursor cursorS = this.presenter.getCursor();
        if (cursorS != null && !cursorS.isClosed()) {
            if (cursorS.getCount() > 1) {
                for (int position = 1; position < cursorS.getCount(); position++) {
                    cursorS.moveToPosition(position);
                    if (StringUtils.equals(targetId, cursorS.getString(cursorS.getColumnIndex("_id")))) {
                        return position;
                    }
                }
            }
        }
        return -1;
    }

    private int nextPosition(int position) {
        int next;
        if (position == -1 || (next = position + 1) == this.presenter.getCursor().getCount()) {
            return -1;
        }
        return Post.fromCursorPosition(this.presenter.getCursor(), next).getSyncType() == 12 ? nextPosition(next) : next;
    }

    private int previousPosition(int position) {
        if (position == -1) {
            return -1;
        }
        int previous = position - 1;
        return previous == 0 ? position : Post.fromCursorPosition(this.presenter.getCursor(), previous).getSyncType() == 12 ? previousPosition(previous) : previous;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ WindowInsetsCompat applyBottomInset(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
        relativePadding.bottom += windowInsetsCompat.getSystemWindowInsetBottom();
        relativePadding.applyToView(view);
        return windowInsetsCompat;
    }

    private void speakCurrent() {
        if (this.initialized && this.tts != null && this.presenter.hasScrollTarget()) {
            String ttsText = TtsNavigation.getTtsText(this, this.presenter.getScrollTarget());
            LanguageIdentification.getClient().identifyLanguage(ttsText).addOnSuccessListener(new LanguageSuccessListener(ttsText)).addOnFailureListener(new LanguageFailureListener());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void speakText(String text) {
        String[] strArrSplit = text.split("\\n");
        this.tts.setOnUtteranceProgressListener(new UtteranceListener(strArrSplit));
        int index = 0;
        while (true) {
            int queueMode = 1;
            if (index >= strArrSplit.length) {
                updatePlayButton(true);
                return;
            }
            TextToSpeech textToSpeech = this.tts;
            String segment = strArrSplit[index];
            if (index == 0) {
                queueMode = 0;
            }
            textToSpeech.speak(segment, queueMode, null, strArrSplit[index]);
            index++;
        }
    }

    public void stop() {
        onStopButtonClicked();
    }

    public void setPresenter(CommentsPresenter commentsPresenter) {
        this.presenter = commentsPresenter;
    }

    protected void updatePlayButton(boolean playing) {
        if (playing) {
            this.buttonPlay.setImageResource(R.drawable.outline_pause_24);
        } else {
            this.buttonPlay.setImageResource(R.drawable.outline_play_arrow_24);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        BusSingleton.getBus().register(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BusSingleton.getBus().unregister(this);
        TextToSpeech textToSpeech = this.tts;
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    @OnClick
    public void onMoreButtonClicked() {
        StaticToast.show("What options would we like to see here?");
    }

    @OnClick
    public void onNextButtonClicked() {
        Logger.verbose("reddit_sync", "Next clicked");
        int nextCommentPosition = nextPosition(findCurrentPosition());
        if (nextCommentPosition == -1) {
            onStopButtonClicked();
            return;
        }
        this.presenter.scrollToComment(Post.fromCursorPosition(this.presenter.getCursor(), nextCommentPosition));
        speakCurrent();
    }

    @OnClick
    public void onPlayButtonClicked() {
        if (this.tts.isSpeaking()) {
            this.tts.stop();
            updatePlayButton(false);
        } else {
            speakCurrent();
            updatePlayButton(true);
        }
    }

    @OnClick
    public void onPreviousButtonClicked() {
        int previousCommentPosition = previousPosition(findCurrentPosition());
        if (previousCommentPosition != -1) {
            this.presenter.scrollToComment(Post.fromCursorPosition(this.presenter.getCursor(), previousCommentPosition));
            speakCurrent();
        }
    }

    @Subscribe
    public void onStartTts(StartTts startTts) {
        setVisibility(0);
        this.presenter.scrollToComment(startTts.post);
        speakCurrent();
    }

    @OnClick
    public void onStopButtonClicked() {
        this.tts.stop();
        setVisibility(8);
        this.presenter.scrollToComment(null);
    }

    @Subscribe
    public void onStopTts(StopTts stopTts) {
        TextToSpeech textToSpeech = this.tts;
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }
}
