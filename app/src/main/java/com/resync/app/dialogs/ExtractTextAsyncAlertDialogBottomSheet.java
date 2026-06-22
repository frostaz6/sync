package com.resync.app.dialogs;

import android.graphics.Bitmap;
import android.widget.TextView;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RedditHelper;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.StringUtils;

/* JADX INFO: renamed from: t9.l0 */
/* JADX INFO: compiled from: ExtractTextAsyncAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ExtractTextAsyncAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    String extractedText;

    /* JADX INFO: renamed from: t9.l0$a */
    /* JADX INFO: compiled from: ExtractTextAsyncAlertDialogBottomSheet.java */
    class BitmapLoadedTarget extends SimpleTarget<Bitmap> {
        BitmapLoadedTarget() {
        }

        @Override // com.bumptech.glide.request.target.Target
        /* JADX INFO: renamed from: h, reason: merged with bridge method [inline-methods] */
        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
            ExtractTextAsyncAlertDialogBottomSheet.this.recognizeText(bitmap);
        }
    }

    /* JADX INFO: renamed from: t9.l0$b */
    /* JADX INFO: compiled from: ExtractTextAsyncAlertDialogBottomSheet.java */
    class ExtractFailureListener implements OnFailureListener {
        ExtractFailureListener() {
        }

        @Override // com.google.android.gms.tasks.OnFailureListener
        public void onFailure(Exception exc) {
            ExtractTextAsyncAlertDialogBottomSheet.this.showError("Error extracting text");
        }
    }

    /* JADX INFO: renamed from: t9.l0$c */
    /* JADX INFO: compiled from: ExtractTextAsyncAlertDialogBottomSheet.java */
    class TextRecognizedListener implements OnSuccessListener<Text> {
        TextRecognizedListener() {
        }

        @Override // com.google.android.gms.tasks.OnSuccessListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onSuccess(Text text) {
            ExtractTextAsyncAlertDialogBottomSheet.this.extractedText = text.getText();
            if (StringUtils.isNotEmpty(ExtractTextAsyncAlertDialogBottomSheet.this.extractedText)) {
                ExtractTextAsyncAlertDialogBottomSheet.this.showResult(text.getText());
            } else {
                ExtractTextAsyncAlertDialogBottomSheet.this.showError("No text found");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recognizeText(Bitmap bitmap) {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS).process(InputImage.fromBitmap(bitmap, 0)).addOnSuccessListener(new TextRecognizedListener()).addOnFailureListener(new ExtractFailureListener());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showError(String str) {
        StaticToast.show(str);
        dismiss();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public void onContentSetup() {
        ((TextView) getView().findViewById(R.id.abstract_alert_dialog_bottom_sheet_message)).setTextIsSelectable(true);
        com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).asBitmap().load(getUrl()).priority(Priority.IMMEDIATE).into(new BitmapLoadedTarget());
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Copy text";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        RedditHelper.copyToClipboardWithToast(this.extractedText, getContext());
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Extracted text";
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public boolean isAsync() {
        return true;
    }
}
