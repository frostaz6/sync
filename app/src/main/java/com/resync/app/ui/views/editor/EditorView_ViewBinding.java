package com.resync.app.ui.views.editor;

import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.buttons.FormattingButton;
import com.resync.app.ui.views.responsive.CustomSubtleDividerView;
import com.resync.app.ui.views.text.BaseTextView;

/* JADX INFO: loaded from: classes3.dex */
public class EditorView_ViewBinding implements Unbinder {
    private EditorView target;
    private View view1;
    private View view2;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private View view3;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private View view4;
    private View view5;
    private View view6;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private View view7;
    private View view8;
    private View view9;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private View view10;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private View view11;
    private View view12;
    private View view13;
    private View view14;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private View view15;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private View view16;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private View view17;

    class SuperscriptClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ EditorView target;

        SuperscriptClickListener(EditorView editorView) {
            this.target = editorView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onSuperscriptClicked();
        }
    }

    class SpeechClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ EditorView target;

        SpeechClickListener(EditorView editorView) {
            this.target = editorView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onSpeechClicked();
        }
    }

    class BulletClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ EditorView target;

        BulletClickListener(EditorView editorView) {
            this.target = editorView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onBulletClicked();
        }
    }

    class NumberedClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ EditorView target;

        NumberedClickListener(EditorView editorView) {
            this.target = editorView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onNumberedClicked();
        }
    }

    class PromoClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ EditorView target;

        PromoClickListener(EditorView editorView) {
            this.target = editorView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onPromoClicked();
        }
    }

    class InfoClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ EditorView target;

        InfoClickListener(EditorView editorView) {
            this.target = editorView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onInfoClicked();
        }
    }

    class BugReportClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ EditorView target;

        BugReportClickListener(EditorView editorView) {
            this.target = editorView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onBugReportClicked();
        }
    }

    class SettingsClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ EditorView target;

        SettingsClickListener(EditorView editorView) {
            this.target = editorView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onSettingsClicked();
        }
    }

    class PreviewClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ EditorView target;

        PreviewClickListener(EditorView editorView) {
            this.target = editorView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onPreviewClicked();
        }
    }

    class EmojiClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ EditorView target;

        EmojiClickListener(EditorView editorView) {
            this.target = editorView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onEmojiClicked(view);
        }
    }

    class DraftClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ EditorView target;

        DraftClickListener(EditorView editorView) {
            this.target = editorView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onDraftClicked();
        }
    }

    class ImageClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ EditorView target;

        ImageClickListener(EditorView editorView) {
            this.target = editorView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onImageClicked();
        }
    }

    class SpoilerClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ EditorView target;

        SpoilerClickListener(EditorView editorView) {
            this.target = editorView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onSpoilerClicked();
        }
    }

    class LinkClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ EditorView target;

        LinkClickListener(EditorView editorView) {
            this.target = editorView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onLinkClicked();
        }
    }

    class BoldClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ EditorView target;

        BoldClickListener(EditorView editorView) {
            this.target = editorView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onBoldClicked();
        }
    }

    class ItalicClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ EditorView target;

        ItalicClickListener(EditorView editorView) {
            this.target = editorView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onItalicClicked();
        }
    }

    class StrikethroughClickListener extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ EditorView target;

        StrikethroughClickListener(EditorView editorView) {
            this.target = editorView;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target.onStrikethroughClicked();
        }
    }

    public EditorView_ViewBinding(EditorView editorView, View view) {
        this.target = editorView;
        editorView.editorPreviewWrapper = (ScrollView) Utils.findOptionalViewAsType(view, R.id.editor_preview_wrapper, "field 'editorPreviewWrapper'", ScrollView.class);
        editorView.editorPreview = (BaseTextView) Utils.findOptionalViewAsType(view, R.id.editor_preview, "field 'editorPreview'", BaseTextView.class);
        editorView.dividerView = (CustomSubtleDividerView) Utils.findOptionalViewAsType(view, R.id.editor_preview_divider, "field 'dividerView'", CustomSubtleDividerView.class);
        editorView.editor = (GifCustomEditText) Utils.findRequiredViewAsType(view, R.id.editor_input, "field 'editor'", GifCustomEditText.class);
        editorView.mUserDisplay = (BaseTextView) Utils.findRequiredViewAsType(view, R.id.user_display, "field 'mUserDisplay'", BaseTextView.class);
        editorView.mFormattingWrapper = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.formatting_wrapper, "field 'mFormattingWrapper'", LinearLayout.class);
        editorView.mMaxLength = (TextView) Utils.findRequiredViewAsType(view, R.id.max_length, "field 'mMaxLength'", TextView.class);
        editorView.mHorScroll = (HorizontalScrollView) Utils.findRequiredViewAsType(view, R.id.format_hor_scroll, "field 'mHorScroll'", HorizontalScrollView.class);
        View viewC = Utils.findRequiredView(view, R.id.format_preview, "field 'buttonPreview' and method 'onPreviewClicked'");
        editorView.buttonPreview = (FormattingButton) Utils.castView(viewC, R.id.format_preview, "field 'buttonPreview'", FormattingButton.class);
        this.view1 = viewC;
        viewC.setOnClickListener(new PreviewClickListener(editorView));
        editorView.buttonUser = (FormattingButton) Utils.findRequiredViewAsType(view, R.id.format_user, "field 'buttonUser'", FormattingButton.class);
        View viewC2 = Utils.findRequiredView(view, R.id.format_emoji, "field 'buttonEmoji' and method 'onEmojiClicked'");
        editorView.buttonEmoji = (FormattingButton) Utils.castView(viewC2, R.id.format_emoji, "field 'buttonEmoji'", FormattingButton.class);
        this.view2 = viewC2;
        viewC2.setOnClickListener(new EmojiClickListener(editorView));
        View viewC3 = Utils.findRequiredView(view, R.id.format_draft, "field 'buttonDraft' and method 'onDraftClicked'");
        editorView.buttonDraft = (FormattingButton) Utils.castView(viewC3, R.id.format_draft, "field 'buttonDraft'", FormattingButton.class);
        this.view3 = viewC3;
        viewC3.setOnClickListener(new DraftClickListener(editorView));
        View viewC4 = Utils.findRequiredView(view, R.id.format_image, "field 'buttonImage' and method 'onImageClicked'");
        editorView.buttonImage = (FormattingButton) Utils.castView(viewC4, R.id.format_image, "field 'buttonImage'", FormattingButton.class);
        this.view4 = viewC4;
        viewC4.setOnClickListener(new ImageClickListener(editorView));
        View viewC5 = Utils.findRequiredView(view, R.id.format_spoiler, "field 'buttonSpoiler' and method 'onSpoilerClicked'");
        editorView.buttonSpoiler = (FormattingButton) Utils.castView(viewC5, R.id.format_spoiler, "field 'buttonSpoiler'", FormattingButton.class);
        this.view5 = viewC5;
        viewC5.setOnClickListener(new SpoilerClickListener(editorView));
        View viewC6 = Utils.findRequiredView(view, R.id.format_link, "field 'buttonLink' and method 'onLinkClicked'");
        editorView.buttonLink = (FormattingButton) Utils.castView(viewC6, R.id.format_link, "field 'buttonLink'", FormattingButton.class);
        this.view6 = viewC6;
        viewC6.setOnClickListener(new LinkClickListener(editorView));
        View viewC7 = Utils.findRequiredView(view, R.id.format_bold, "field 'buttonBold' and method 'onBoldClicked'");
        editorView.buttonBold = (FormattingButton) Utils.castView(viewC7, R.id.format_bold, "field 'buttonBold'", FormattingButton.class);
        this.view7 = viewC7;
        viewC7.setOnClickListener(new BoldClickListener(editorView));
        View viewC8 = Utils.findRequiredView(view, R.id.format_italic, "field 'buttonItalic' and method 'onItalicClicked'");
        editorView.buttonItalic = (FormattingButton) Utils.castView(viewC8, R.id.format_italic, "field 'buttonItalic'", FormattingButton.class);
        this.view8 = viewC8;
        viewC8.setOnClickListener(new ItalicClickListener(editorView));
        View viewC9 = Utils.findRequiredView(view, R.id.format_strikethrough, "field 'buttonStrikethrough' and method 'onStrikethroughClicked'");
        editorView.buttonStrikethrough = (FormattingButton) Utils.castView(viewC9, R.id.format_strikethrough, "field 'buttonStrikethrough'", FormattingButton.class);
        this.view9 = viewC9;
        viewC9.setOnClickListener(new StrikethroughClickListener(editorView));
        View viewC10 = Utils.findRequiredView(view, R.id.format_superscript, "field 'buttonSuperscript' and method 'onSuperscriptClicked'");
        editorView.buttonSuperscript = (FormattingButton) Utils.castView(viewC10, R.id.format_superscript, "field 'buttonSuperscript'", FormattingButton.class);
        this.view10 = viewC10;
        viewC10.setOnClickListener(new SuperscriptClickListener(editorView));
        View viewC11 = Utils.findRequiredView(view, R.id.format_speech, "field 'buttonSpeech' and method 'onSpeechClicked'");
        editorView.buttonSpeech = (FormattingButton) Utils.castView(viewC11, R.id.format_speech, "field 'buttonSpeech'", FormattingButton.class);
        this.view11 = viewC11;
        viewC11.setOnClickListener(new SpeechClickListener(editorView));
        View viewC12 = Utils.findRequiredView(view, R.id.format_bullet, "field 'buttonBullet' and method 'onBulletClicked'");
        editorView.buttonBullet = (FormattingButton) Utils.castView(viewC12, R.id.format_bullet, "field 'buttonBullet'", FormattingButton.class);
        this.view12 = viewC12;
        viewC12.setOnClickListener(new BulletClickListener(editorView));
        View viewC13 = Utils.findRequiredView(view, R.id.format_numbered, "field 'buttonNumbered' and method 'onNumberedClicked'");
        editorView.buttonNumbered = (FormattingButton) Utils.castView(viewC13, R.id.format_numbered, "field 'buttonNumbered'", FormattingButton.class);
        this.view13 = viewC13;
        viewC13.setOnClickListener(new NumberedClickListener(editorView));
        View viewC14 = Utils.findRequiredView(view, R.id.format_promo, "field 'buttonPromo' and method 'onPromoClicked'");
        editorView.buttonPromo = (FormattingButton) Utils.castView(viewC14, R.id.format_promo, "field 'buttonPromo'", FormattingButton.class);
        this.view14 = viewC14;
        viewC14.setOnClickListener(new PromoClickListener(editorView));
        View viewC15 = Utils.findRequiredView(view, R.id.format_info, "field 'buttonInfo' and method 'onInfoClicked'");
        editorView.buttonInfo = (FormattingButton) Utils.castView(viewC15, R.id.format_info, "field 'buttonInfo'", FormattingButton.class);
        this.view15 = viewC15;
        viewC15.setOnClickListener(new InfoClickListener(editorView));
        View viewC16 = Utils.findRequiredView(view, R.id.format_bug_report, "field 'buttonBugReport' and method 'onBugReportClicked'");
        editorView.buttonBugReport = (FormattingButton) Utils.castView(viewC16, R.id.format_bug_report, "field 'buttonBugReport'", FormattingButton.class);
        this.view16 = viewC16;
        viewC16.setOnClickListener(new BugReportClickListener(editorView));
        View viewC17 = Utils.findRequiredView(view, R.id.format_settings, "field 'buttonSettings' and method 'onSettingsClicked'");
        editorView.buttonSettings = (FormattingButton) Utils.castView(viewC17, R.id.format_settings, "field 'buttonSettings'", FormattingButton.class);
        this.view17 = viewC17;
        viewC17.setOnClickListener(new SettingsClickListener(editorView));
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        EditorView editorView = this.target;
        if (editorView == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        editorView.editorPreviewWrapper = null;
        editorView.editorPreview = null;
        editorView.dividerView = null;
        editorView.editor = null;
        editorView.mUserDisplay = null;
        editorView.mFormattingWrapper = null;
        editorView.mMaxLength = null;
        editorView.mHorScroll = null;
        editorView.buttonPreview = null;
        editorView.buttonUser = null;
        editorView.buttonEmoji = null;
        editorView.buttonDraft = null;
        editorView.buttonImage = null;
        editorView.buttonSpoiler = null;
        editorView.buttonLink = null;
        editorView.buttonBold = null;
        editorView.buttonItalic = null;
        editorView.buttonStrikethrough = null;
        editorView.buttonSuperscript = null;
        editorView.buttonSpeech = null;
        editorView.buttonBullet = null;
        editorView.buttonNumbered = null;
        editorView.buttonPromo = null;
        editorView.buttonInfo = null;
        editorView.buttonBugReport = null;
        editorView.buttonSettings = null;
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
        this.view6.setOnClickListener(null);
        this.view6 = null;
        this.view7.setOnClickListener(null);
        this.view7 = null;
        this.view8.setOnClickListener(null);
        this.view8 = null;
        this.view9.setOnClickListener(null);
        this.view9 = null;
        this.view10.setOnClickListener(null);
        this.view10 = null;
        this.view11.setOnClickListener(null);
        this.view11 = null;
        this.view12.setOnClickListener(null);
        this.view12 = null;
        this.view13.setOnClickListener(null);
        this.view13 = null;
        this.view14.setOnClickListener(null);
        this.view14 = null;
        this.view15.setOnClickListener(null);
        this.view15 = null;
        this.view16.setOnClickListener(null);
        this.view16 = null;
        this.view17.setOnClickListener(null);
        this.view17 = null;
    }
}
