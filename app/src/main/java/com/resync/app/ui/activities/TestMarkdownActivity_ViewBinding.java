package com.resync.app.ui.activities;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes.dex */
public class TestMarkdownActivity_ViewBinding implements Unbinder {
    private TestMarkdownActivity target;
    private View viewSpoiler;
    private View viewAwards;
    private View viewUserFlair;
    private View viewInlineGifNoSize;
    private View viewInlineGifs;
    private View viewSelftextGif;
    private View viewEmoteAnimated;
    private View viewEmote;

    class SpoilerClickListener extends com.resync.app.util.DebouncingOnClickListener {
        final /* synthetic */ TestMarkdownActivity activity;

        SpoilerClickListener(TestMarkdownActivity testMarkdownActivity) {
            this.activity = testMarkdownActivity;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.activity.onSpoilerClicked();
        }
    }

    class AwardsClickListener extends com.resync.app.util.DebouncingOnClickListener {
        final /* synthetic */ TestMarkdownActivity activity;

        AwardsClickListener(TestMarkdownActivity testMarkdownActivity) {
            this.activity = testMarkdownActivity;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.activity.onLotsOfAwardsClicked();
        }
    }

    class UserFlairClickListener extends com.resync.app.util.DebouncingOnClickListener {
        final /* synthetic */ TestMarkdownActivity activity;

        UserFlairClickListener(TestMarkdownActivity testMarkdownActivity) {
            this.activity = testMarkdownActivity;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.activity.onUserImageFlairClicked();
        }
    }

    class InlineGifNoSizeClickListener extends com.resync.app.util.DebouncingOnClickListener {
        final /* synthetic */ TestMarkdownActivity activity;

        InlineGifNoSizeClickListener(TestMarkdownActivity testMarkdownActivity) {
            this.activity = testMarkdownActivity;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.activity.onInlineGifNoSizeClicked();
        }
    }

    class InlineGifClickListener extends com.resync.app.util.DebouncingOnClickListener {
        final /* synthetic */ TestMarkdownActivity activity;

        InlineGifClickListener(TestMarkdownActivity testMarkdownActivity) {
            this.activity = testMarkdownActivity;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.activity.onInlineGifClicked();
        }
    }

    class SelftextGifClickListener extends com.resync.app.util.DebouncingOnClickListener {
        final /* synthetic */ TestMarkdownActivity activity;

        SelftextGifClickListener(TestMarkdownActivity testMarkdownActivity) {
            this.activity = testMarkdownActivity;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.activity.onSelftextGifClicked();
        }
    }

    class AnimatedEmoteClickListener extends com.resync.app.util.DebouncingOnClickListener {
        final /* synthetic */ TestMarkdownActivity activity;

        AnimatedEmoteClickListener(TestMarkdownActivity testMarkdownActivity) {
            this.activity = testMarkdownActivity;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.activity.onAnimatedEmoteClicked();
        }
    }

    class EmoteClickListener extends com.resync.app.util.DebouncingOnClickListener {
        final /* synthetic */ TestMarkdownActivity activity;

        EmoteClickListener(TestMarkdownActivity testMarkdownActivity) {
            this.activity = testMarkdownActivity;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.activity.onEmoteClicked();
        }
    }

    public TestMarkdownActivity_ViewBinding(TestMarkdownActivity testMarkdownActivity, View view) {
        this.target = testMarkdownActivity;
        View viewC = Utils.findRequiredView(view, R.id.button_spoiler, "method 'onSpoilerClicked'");
        this.viewSpoiler = viewC;
        viewC.setOnClickListener(new SpoilerClickListener(testMarkdownActivity));
        View viewC2 = Utils.findRequiredView(view, R.id.button_awards, "method 'onLotsOfAwardsClicked'");
        this.viewAwards = viewC2;
        viewC2.setOnClickListener(new AwardsClickListener(testMarkdownActivity));
        View viewC3 = Utils.findRequiredView(view, R.id.button_user_flair, "method 'onUserImageFlairClicked'");
        this.viewUserFlair = viewC3;
        viewC3.setOnClickListener(new UserFlairClickListener(testMarkdownActivity));
        View viewC4 = Utils.findRequiredView(view, R.id.button_inline_gif_no_size, "method 'onInlineGifNoSizeClicked'");
        this.viewInlineGifNoSize = viewC4;
        viewC4.setOnClickListener(new InlineGifNoSizeClickListener(testMarkdownActivity));
        View viewC5 = Utils.findRequiredView(view, R.id.button_inline_gifs, "method 'onInlineGifClicked'");
        this.viewInlineGifs = viewC5;
        viewC5.setOnClickListener(new InlineGifClickListener(testMarkdownActivity));
        View viewC6 = Utils.findRequiredView(view, R.id.button_selftext_gif, "method 'onSelftextGifClicked'");
        this.viewSelftextGif = viewC6;
        viewC6.setOnClickListener(new SelftextGifClickListener(testMarkdownActivity));
        View viewC7 = Utils.findRequiredView(view, R.id.button_emote_animated, "method 'onAnimatedEmoteClicked'");
        this.viewEmoteAnimated = viewC7;
        viewC7.setOnClickListener(new AnimatedEmoteClickListener(testMarkdownActivity));
        View viewC8 = Utils.findRequiredView(view, R.id.button_emote, "method 'onEmoteClicked'");
        this.viewEmote = viewC8;
        viewC8.setOnClickListener(new EmoteClickListener(testMarkdownActivity));
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        TestMarkdownActivity testMarkdownActivity = this.target;
        if (testMarkdownActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        this.viewSpoiler = null;
        this.viewAwards = null;
        this.viewUserFlair = null;
        this.viewInlineGifNoSize = null;
        this.viewInlineGifs = null;
        this.viewSelftextGif = null;
        this.viewEmoteAnimated = null;
        this.viewEmote = null;
    }
}
