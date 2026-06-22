package com.resync.app.dialogs;

import android.content.ContentValues;
import android.os.Bundle;
import com.bumptech.glide.RedditHelper;
import com.google.android.exoplayer2.C;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.resync.app.RedditApplication;
import com.resync.app.provider.RedditProvider;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractProgressDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.text.html.Options;
import com.resync.app.text.html.SyncHtmlParser;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.widgets.text.SpannableTextViewStringBuilder;
import com.resync.app.dialogs.FragmentUtils;
import com.resync.app.helpers.TranslationHelper;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;

/* JADX INFO: renamed from: da.d */
/* JADX INFO: compiled from: TranslateAsyncAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class TranslateAsyncAlertDialogBottomSheet extends AbstractProgressDialogBottomSheet {
    private String overrideLanguage;

    /* JADX INFO: renamed from: da.d$a */
    /* JADX INFO: compiled from: TranslateAsyncAlertDialogBottomSheet.java */
    class DetectFailureListener implements OnFailureListener {
        DetectFailureListener() {
        }

        @Override // com.google.android.gms.tasks.OnFailureListener
        public void onFailure(Exception exc) {
            TranslateAsyncAlertDialogBottomSheet.this.onError("Error detecting language");
        }
    }

    /* JADX INFO: renamed from: da.d$b */
    /* JADX INFO: compiled from: TranslateAsyncAlertDialogBottomSheet.java */
    class LanguageDetectedListener implements OnSuccessListener<String> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ String sourceText;

        LanguageDetectedListener(String str) {
            this.sourceText = str;
        }

        @Override // com.google.android.gms.tasks.OnSuccessListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onSuccess(String str) {
            if (str.equals(C.LANGUAGE_UNDETERMINED)) {
                StaticToast.show("Could not detect language");
                FragmentUtils.showWithPost(TranslationSelectionDialogBottomSheet.class, TranslateAsyncAlertDialogBottomSheet.this.getChildFragmentManager(), TranslateAsyncAlertDialogBottomSheet.this.getPost());
                TranslateAsyncAlertDialogBottomSheet.this.dismissAllowingStateLoss();
            } else if (str.equals("en")) {
                TranslateAsyncAlertDialogBottomSheet.this.onError("Language detected was already English");
            } else {
                TranslateAsyncAlertDialogBottomSheet.this.downloadAndTranslate(this.sourceText, str);
            }
        }
    }

    /* JADX INFO: renamed from: da.d$c */
    /* JADX INFO: compiled from: TranslateAsyncAlertDialogBottomSheet.java */
    class ModelDownloadFailureListener implements OnFailureListener {
        ModelDownloadFailureListener() {
        }

        @Override // com.google.android.gms.tasks.OnFailureListener
        public void onFailure(Exception exc) {
            TranslateAsyncAlertDialogBottomSheet.this.onError("Error downloading translation model");
        }
    }

    /* JADX INFO: renamed from: da.d$d */
    /* JADX INFO: compiled from: TranslateAsyncAlertDialogBottomSheet.java */
    class ModelDownloadedListener implements OnSuccessListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Translator translator;
        final /* synthetic */ String sourceText;
        final /* synthetic */ String langCode;

        /* JADX INFO: renamed from: da.d$d$a */
        /* JADX INFO: compiled from: TranslateAsyncAlertDialogBottomSheet.java */
        class TranslateFailureListener implements OnFailureListener {
            TranslateFailureListener() {
            }

            @Override // com.google.android.gms.tasks.OnFailureListener
            public void onFailure(Exception exc) {
                TranslateAsyncAlertDialogBottomSheet.this.onError("Could not translate text: " + exc.getClass().getSimpleName());
            }
        }

        /* JADX INFO: renamed from: da.d$d$b */
        /* JADX INFO: compiled from: TranslateAsyncAlertDialogBottomSheet.java */
        class TranslationSuccessListener implements OnSuccessListener {
            TranslationSuccessListener() {
            }

            @Override // com.google.android.gms.tasks.OnSuccessListener
            public void onSuccess(Object obj) {
                if (!(obj instanceof String)) {
                    TranslateAsyncAlertDialogBottomSheet.this.onError("Error translating text (unknown object)");
                } else {
                    ModelDownloadedListener listener = ModelDownloadedListener.this;
                    TranslateAsyncAlertDialogBottomSheet.this.applyTranslation(listener.langCode, (String) obj);
                }
            }
        }

        ModelDownloadedListener(Translator translator, String str, String str2) {
            this.translator = translator;
            this.sourceText = str;
            this.langCode = str2;
        }

        @Override // com.google.android.gms.tasks.OnSuccessListener
        public void onSuccess(Object obj) {
            this.translator.translate(this.sourceText).addOnSuccessListener(new TranslationSuccessListener()).addOnFailureListener(new TranslateFailureListener());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onError(final String str) {
        Logger.verbose("ERROR: " + str);
        try {
            try {
                getView().postDelayed(new Runnable() { // from class: da.c
                    @Override // java.lang.Runnable
                    public final void run() {
                        TranslateAsyncAlertDialogBottomSheet.this.showToastAndDismiss(str);
                    }
                }, 200L);
            } catch (Exception e) {
                Logger.logException(e);
            }
        } catch (Exception unused) {
            dismissAllowingStateLoss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downloadAndTranslate(String str, String str2) {
        Translator translatorA = Translation.getClient(new TranslatorOptions.Builder().setSourceLanguage(str2).setTargetLanguage("en").build());
        translatorA.downloadModelIfNeeded(new DownloadConditions.Builder().requireWifi().build()).addOnSuccessListener(new ModelDownloadedListener(translatorA, str, str2)).addOnFailureListener(new ModelDownloadFailureListener());
    }

    private void updateBody(String str, String str2) {
        String rawBody = TranslationHelper.buildSelftextTranslation(getPost(), str, str2);
        String processedBody = RedditHelper.processMarkdownToHtml(null, rawBody);
        ContentValues contentValues = new ContentValues();
        contentValues.put("body_raw", rawBody);
        contentValues.put("body_processed", processedBody);
        RedditApplication.getAppContext().getContentResolver().update(RedditProvider.URI_RESTORE_REMOVED, contentValues, getPost().getId(), null);
        RedditApplication.getAppContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
        dismissAllowingStateLoss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyTranslation(String str, String str2) {
        if (getPost().getSyncType() == 11) {
            updateBody(str, str2);
        } else {
            updateSelftext(str, str2);
        }
    }

    private void updateSelftext(String str, String str2) {
        String rawSelftext = TranslationHelper.buildCommentTranslation(getPost(), str, str2);
        String processedSelftext = RedditHelper.processMarkdownToHtml(null, rawSelftext);
        ContentValues contentValues = new ContentValues();
        contentValues.put("selftext_raw", rawSelftext);
        contentValues.put("selftext_processed", processedSelftext);
        RedditApplication.getAppContext().getContentResolver().update(RedditProvider.URI_RESTORE_REMOVED, contentValues, getPost().getId(), null);
        RedditApplication.getAppContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
        dismissAllowingStateLoss();
    }

    public static Bundle buildArgs(Post post, String str) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("translate_post", post);
        bundle.putString("override_language", str);
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void showToastAndDismiss(String str) {
        StaticToast.show(str);
        dismissAllowingStateLoss();
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        setPost((Post) getArguments().getSerializable("translate_post"));
        this.overrideLanguage = getArguments().getString("override_language");
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractProgressDialogBottomSheet
    public void onProgressReady() {
        SpannableTextViewStringBuilder spannableTextViewStringBuilder = new SpannableTextViewStringBuilder();
        String processedSource = getPost().getSyncType() == 11 ? getPost().getBodyProcessed() : getPost().getSelftextProcessed();
        SyncHtmlParser.parse(spannableTextViewStringBuilder, Options.disabled(), processedSource);
        String string = spannableTextViewStringBuilder.toString();
        if (!StringUtils.isNotEmpty(string) || StringUtils.equalsIgnoreCase("null", string) || StringUtils.contains(processedSource, "Translated by Google")) {
            if (StringUtils.contains(processedSource, "Translated by Google")) {
                onError("Already translated");
                return;
            } else {
                onError("No text to translate");
                return;
            }
        }
        if (StringUtils.isNotEmpty(this.overrideLanguage)) {
            downloadAndTranslate(string, this.overrideLanguage);
        } else {
            LanguageIdentification.getClient().identifyLanguage(string).addOnSuccessListener(new LanguageDetectedListener(string)).addOnFailureListener(new DetectFailureListener());
        }
    }
}
