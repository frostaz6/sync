package com.resync.app.dialogs;

import android.graphics.Bitmap;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.TextView;
import androidx.core.content.FileProvider;
import com.google.android.exoplayer2.util.MimeTypes;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.jobs.media.AbstractDownloadMediaJob;
import com.resync.app.receiver.download.ShareReceiver;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet;
import com.resync.app.ui.views.preferences.ThemePreferencePreview;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.StaticToast;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.commons.io.FileUtils;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.TypefaceManager;
import com.resync.app.util.Logger;

/* JADX INFO: renamed from: ca.a */
/* JADX INFO: compiled from: ExportThemeAlertDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class ExportThemeAlertDialogBottomSheet extends AbstractAlertDialogBottomSheet {
    ThemePreferencePreview themePreview;

    /* JADX INFO: compiled from: ExportThemeAlertDialogBottomSheet.java */
    class RoundedOutlineProvider extends ViewOutlineProvider {
        RoundedOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), SizeHelper.dpToPx(12));
        }
    }

    /* JADX INFO: compiled from: ExportThemeAlertDialogBottomSheet.java */
    class ShareThread extends Thread {

        final /* synthetic */ Bitmap bitmap;

        ShareThread(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                File file = new File(ExportThemeAlertDialogBottomSheet.this.getContext().getExternalCacheDir(), "shared");
                if (!file.exists()) {
                    file.mkdirs();
                }
                File file2 = new File(file, System.currentTimeMillis() + ".jpg");
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                this.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                File file3 = new File(RedditApplication.getAppContext().getExternalCacheDir(), "shared/" + System.currentTimeMillis() + ".jpg");
                FileUtils.copyFile(file2, file3);
                ShareReceiver.share(new AbstractDownloadMediaJob.ShareInfo(ExportThemeAlertDialogBottomSheet.this.getUrl(), FileProvider.getUriForFile(RedditApplication.getAppContext(), "com.resync.app.provider", file3), MimeTypes.IMAGE_JPEG));
            } catch (Exception e) {
                Logger.logException(e);
                StaticToast.showWithContext(ExportThemeAlertDialogBottomSheet.this.getActivity(), "Error sharing post preview");
            }
        }
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractAlertDialogBottomSheet
    public void onContentSetup() {
        super.onContentSetup();
        addContentLayout(R.layout.fragment_export_theme);
        this.themePreview = (ThemePreferencePreview) getView().findViewById(R.id.fragment_export_theme_text_preview);
        TextView textView = (TextView) getView().findViewById(R.id.fragment_export_theme_text);
        textView.setText(getUrl());
        textView.setTextSize(1, 10.0f);
        textView.setTypeface(TypefaceManager.get(15));
        textView.setBackgroundColor(MonetUtil.getCardColorAuto());
        textView.setOutlineProvider(new RoundedOutlineProvider());
        textView.setClipToOutline(true);
    }

    public void shareThemePreview() {
        this.themePreview.setDrawingCacheEnabled(true);
        this.themePreview.buildDrawingCache();
        Bitmap drawingCache = this.themePreview.getDrawingCache();
        if (drawingCache == null) {
            StaticToast.showWithContext(getActivity(), "Error sharing preview (no bitmap)");
        } else {
            new ShareThread(drawingCache).start();
            dismiss();
        }
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Share";
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public void onPositiveButtonClicked() {
        shareThemePreview();
        dismiss();
    }

    @Override // com.resync.app.dialogs.interfaces.AlertDialogBottomSheetInterface
    public String getTitle() {
        return "Share theme";
    }
}
