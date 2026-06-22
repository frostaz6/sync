package com.resync.app.ui.fragment_dialogs;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;
import com.resync.app.ui.views.buttons.upload.CameraButton;
import com.resync.app.ui.views.images.CustomImageView;

/* JADX INFO: loaded from: classes.dex */
public class UploadImageDialogFragment_ViewBinding implements Unbinder {
    private UploadImageDialogFragment target;
    private View dialogUploadImageButtonCameraClickView;
    private View dialogUploadImageButtonGalleryClickView;

    class ClickListener0 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ UploadImageDialogFragment target0;

        ClickListener0(UploadImageDialogFragment uploadImageDialogFragment) {
            this.target0 = uploadImageDialogFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target0.onCameraButtonClicked();
        }
    }

    class ClickListener1 extends com.resync.app.util.DebouncingOnClickListener {

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        final /* synthetic */ UploadImageDialogFragment target1;

        ClickListener1(UploadImageDialogFragment uploadImageDialogFragment) {
            this.target1 = uploadImageDialogFragment;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.target1.onGalleryButtonClicked();
        }
    }

    public UploadImageDialogFragment_ViewBinding(UploadImageDialogFragment uploadImageDialogFragment, View view) {
        this.target = uploadImageDialogFragment;
        View viewC = Utils.findRequiredView(view, R.id.dialog_upload_image_button_camera, "field 'mCameraButton' and method 'onCameraButtonClicked'");
        uploadImageDialogFragment.mCameraButton = (CameraButton) Utils.castView(viewC, R.id.dialog_upload_image_button_camera, "field 'mCameraButton'", CameraButton.class);
        this.dialogUploadImageButtonCameraClickView = viewC;
        viewC.setOnClickListener(new ClickListener0(uploadImageDialogFragment));
        uploadImageDialogFragment.mPreview = (CustomImageView) Utils.findRequiredViewAsType(view, R.id.dialog_upload_image_preview, "field 'mPreview'", CustomImageView.class);
        uploadImageDialogFragment.mButtonWrapper = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.dialog_upload_image_button_wrapper, "field 'mButtonWrapper'", LinearLayout.class);
        uploadImageDialogFragment.mImageIndicator = (ImageView) Utils.findRequiredViewAsType(view, R.id.dialog_upload_image_indicator, "field 'mImageIndicator'", ImageView.class);
        View viewC2 = Utils.findRequiredView(view, R.id.dialog_upload_image_button_gallery, "method 'onGalleryButtonClicked'");
        this.dialogUploadImageButtonGalleryClickView = viewC2;
        viewC2.setOnClickListener(new ClickListener1(uploadImageDialogFragment));
    }

    @Override
    public void unbind() {
        UploadImageDialogFragment target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.mCameraButton = null;
        target.mPreview = null;
        target.mButtonWrapper = null;
        target.mImageIndicator = null;
        this.dialogUploadImageButtonCameraClickView.setOnClickListener(null);
        this.dialogUploadImageButtonCameraClickView = null;
        this.dialogUploadImageButtonGalleryClickView.setOnClickListener(null);
        this.dialogUploadImageButtonGalleryClickView = null;
    }
}
