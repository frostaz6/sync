package com.resync.app.dialogs;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
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
import java.util.ArrayList;
import com.resync.app.text.html.Options;
import com.resync.app.text.html.SyncHtmlParser;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.widgets.text.SpannableTextViewStringBuilder;
import com.resync.app.managers.PostsManager;
import com.resync.app.helpers.DatabaseIdHelper;
import com.resync.app.helpers.TranslationHelper;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;

/* JADX INFO: compiled from: TranslateAllAsyncAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class TranslateAllAsyncAlertDialogBottomSheet extends AbstractProgressDialogBottomSheet {
    ArrayList<Post> posts = new ArrayList<>();
    ContentValues[] contentValues;

    /* JADX INFO: renamed from: da.b$a */
    /* JADX INFO: compiled from: TranslateAllAsyncAlertDialogBottomSheet.java */
    class PostsLoadedListener implements Loader.OnLoadCompleteListener<Cursor> {
        PostsLoadedListener() {
        }

        @Override // android.content.Loader.OnLoadCompleteListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
            if (cursor == null || cursor.getCount() == 0) {
                TranslateAllAsyncAlertDialogBottomSheet.this.showErrorAndDismiss("Error translating all (no items found)");
                return;
            }
            for (int i = 0; i < cursor.getCount(); i++) {
                TranslateAllAsyncAlertDialogBottomSheet.this.posts.add(Post.fromCursorPosition(cursor, i));
            }
            TranslateAllAsyncAlertDialogBottomSheet translateAllAsyncAlertDialogBottomSheet = TranslateAllAsyncAlertDialogBottomSheet.this;
            translateAllAsyncAlertDialogBottomSheet.contentValues = new ContentValues[translateAllAsyncAlertDialogBottomSheet.posts.size()];
            TranslateAllAsyncAlertDialogBottomSheet.this.getProgressIndicator().setMax(TranslateAllAsyncAlertDialogBottomSheet.this.posts.size());
            TranslateAllAsyncAlertDialogBottomSheet.this.translateAt(0);
        }
    }

    /* JADX INFO: renamed from: da.b$b */
    /* JADX INFO: compiled from: TranslateAllAsyncAlertDialogBottomSheet.java */
    class DetectFailureListener implements OnFailureListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ int position;

        DetectFailureListener(int position) {
            this.position = position;
        }

        @Override // com.google.android.gms.tasks.OnFailureListener
        public void onFailure(Exception exc) {
            TranslateAllAsyncAlertDialogBottomSheet.this.onItemFinished(this.position, "Error detecting language");
        }
    }

    /* JADX INFO: renamed from: da.b$c */
    /* JADX INFO: compiled from: TranslateAllAsyncAlertDialogBottomSheet.java */
    class LanguageDetectedListener implements OnSuccessListener<String> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ int position;
        final /* synthetic */ String sourceText;

        LanguageDetectedListener(int position, String str) {
            this.position = position;
            this.sourceText = str;
        }

        @Override // com.google.android.gms.tasks.OnSuccessListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onSuccess(String str) {
            if (!str.equals(C.LANGUAGE_UNDETERMINED)) {
                if (str.equals("en")) {
                    TranslateAllAsyncAlertDialogBottomSheet.this.onItemFinished(this.position, "Was already English");
                    return;
                } else {
                    TranslateAllAsyncAlertDialogBottomSheet.this.downloadAndTranslateAt(this.position, this.sourceText, str);
                    return;
                }
            }
            TranslateAllAsyncAlertDialogBottomSheet.this.onItemFinished(this.position, "Can't detect language: " + this.sourceText);
        }
    }

    /* JADX INFO: renamed from: da.b$d */
    /* JADX INFO: compiled from: TranslateAllAsyncAlertDialogBottomSheet.java */
    class ModelDownloadFailureListener implements OnFailureListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ int position;

        ModelDownloadFailureListener(int position) {
            this.position = position;
        }

        @Override // com.google.android.gms.tasks.OnFailureListener
        public void onFailure(Exception exc) {
            TranslateAllAsyncAlertDialogBottomSheet.this.onItemFinished(this.position, "Error downloading translation model");
        }
    }

    /* JADX INFO: renamed from: da.b$e */
    /* JADX INFO: compiled from: TranslateAllAsyncAlertDialogBottomSheet.java */
    class ModelDownloadedListener implements OnSuccessListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ Translator translator;
        final /* synthetic */ String sourceText;
        final /* synthetic */ int position;

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ String langCode;

        /* JADX INFO: renamed from: da.b$e$a */
        /* JADX INFO: compiled from: TranslateAllAsyncAlertDialogBottomSheet.java */
        class TranslateFailureListener implements OnFailureListener {
            TranslateFailureListener() {
            }

            @Override // com.google.android.gms.tasks.OnFailureListener
            public void onFailure(Exception exc) {
                ModelDownloadedListener listener = ModelDownloadedListener.this;
                TranslateAllAsyncAlertDialogBottomSheet.this.onItemFinished(listener.position, "Could not translate text: " + exc.getClass().getSimpleName());
            }
        }

        /* JADX INFO: renamed from: da.b$e$b */
        /* JADX INFO: compiled from: TranslateAllAsyncAlertDialogBottomSheet.java */
        class TranslationSuccessListener implements OnSuccessListener {
            TranslationSuccessListener() {
            }

            @Override // com.google.android.gms.tasks.OnSuccessListener
            public void onSuccess(Object obj) {
                if (obj instanceof String) {
                    ModelDownloadedListener listener = ModelDownloadedListener.this;
                    TranslateAllAsyncAlertDialogBottomSheet.this.applyTranslationAt(listener.position, listener.langCode, (String) obj);
                } else {
                    ModelDownloadedListener listener = ModelDownloadedListener.this;
                    TranslateAllAsyncAlertDialogBottomSheet.this.onItemFinished(listener.position, "Error translating text (unknown object)");
                }
            }
        }

        ModelDownloadedListener(Translator translator, String str, int position, String str2) {
            this.translator = translator;
            this.sourceText = str;
            this.position = position;
            this.langCode = str2;
        }

        @Override // com.google.android.gms.tasks.OnSuccessListener
        public void onSuccess(Object obj) {
            this.translator.translate(this.sourceText).addOnSuccessListener(new TranslationSuccessListener()).addOnFailureListener(new TranslateFailureListener());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void showToastAndDismiss(String str) {
        StaticToast.show(str);
        dismissAllowingStateLoss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onItemFinished(int position, String str) {
        if (StringUtils.isNotEmpty(str)) {
            Logger.verbose("ERROR: (" + position + ") " + str);
        }
        int nextPosition = position + 1;
        if (nextPosition < this.posts.size()) {
            translateAt(nextPosition);
        } else if (isDialogShowing()) {
            StaticToast.show("Translation complete");
            RedditApplication.getAppContext().getContentResolver().bulkInsert(RedditProvider.URI_RESTORE_REMOVED, this.contentValues);
            RedditApplication.getAppContext().getContentResolver().notifyChange(RedditProvider.URI_MULTIPLE_POSTS, null);
            dismissAllowingStateLoss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showErrorAndDismiss(final String str) {
        try {
            try {
                getView().postDelayed(new Runnable() { // from class: da.a
                    @Override // java.lang.Runnable
                    public final void run() {
                        TranslateAllAsyncAlertDialogBottomSheet.this.showToastAndDismiss(str);
                    }
                }, 200L);
            } catch (Exception unused) {
                dismissAllowingStateLoss();
            }
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downloadAndTranslateAt(int position, String str, String str2) {
        Translator translatorA = Translation.getClient(new TranslatorOptions.Builder().setSourceLanguage(str2).setTargetLanguage("en").build());
        translatorA.downloadModelIfNeeded(new DownloadConditions.Builder().requireWifi().build()).addOnSuccessListener(new ModelDownloadedListener(translatorA, str, position, str2)).addOnFailureListener(new ModelDownloadFailureListener(position));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void translateAt(int position) {
        if (isDialogShowing()) {
            getProgressIndicator().setProgress(position);
            Logger.verbose("TranslateAllAsyncAlertDialogBottomSheet", "Translating pos: " + position);
            Post post = this.posts.get(position);
            SpannableTextViewStringBuilder spannableTextViewStringBuilder = new SpannableTextViewStringBuilder();
            String processedSource = post.getSyncType() == 11 ? post.getBodyProcessed() : post.getSelftextProcessed();
            SyncHtmlParser.parse(spannableTextViewStringBuilder, Options.disabled(), processedSource);
            String string = spannableTextViewStringBuilder.toString();
            if (!StringUtils.isNotEmpty(string) || StringUtils.equalsIgnoreCase("null", string) || StringUtils.contains(processedSource, "Translated by Google")) {
                onItemFinished(position, "No string");
            } else {
                LanguageIdentification.getClient().identifyLanguage(string).addOnSuccessListener(new LanguageDetectedListener(position, string)).addOnFailureListener(new DetectFailureListener(position));
            }
        }
    }

    private void storeBodyTranslation(int position, String str, String str2) {
        Post post = this.posts.get(position);
        String rawBody = TranslationHelper.buildSelftextTranslation(post, str, str2);
        String processedBody = RedditHelper.processMarkdownToHtml(null, rawBody);
        this.contentValues[position] = new ContentValues();
        this.contentValues[position].put("_id", post.getId());
        this.contentValues[position].put("body_raw", rawBody);
        this.contentValues[position].put("body_processed", processedBody);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyTranslationAt(int position, String str, String str2) {
        Post post = this.posts.get(position);
        Logger.verbose("TranslateAllAsyncAlertDialogBottomSheet", "Translation complete: " + position + " - " + str + " - " + str2);
        if (post.getSyncType() == 11) {
            storeBodyTranslation(position, str, str2);
        } else {
            storeSelftextTranslation(position, str, str2);
        }
        onItemFinished(position, null);
    }

    private void storeSelftextTranslation(int position, String str, String str2) {
        Post post = this.posts.get(position);
        String rawSelftext = TranslationHelper.buildCommentTranslation(post, str, str2);
        String processedSelftext = RedditHelper.processMarkdownToHtml(null, rawSelftext);
        this.contentValues[position] = new ContentValues();
        this.contentValues[position].put("_id", post.getId());
        this.contentValues[position].put("selftext_raw", rawSelftext);
        this.contentValues[position].put("selftext_processed", processedSelftext);
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractProgressDialogBottomSheet
    public boolean isIndeterminate() {
        return false;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractProgressDialogBottomSheet
    public void onProgressReady() {
        CursorLoader cursorLoaderA = PostsManager.createSearchLoader(getActivity(), DatabaseIdHelper.buildId(getPostId(), null), false, null);
        cursorLoaderA.registerListener(0, new PostsLoadedListener());
        cursorLoaderA.startLoading();
    }
}
