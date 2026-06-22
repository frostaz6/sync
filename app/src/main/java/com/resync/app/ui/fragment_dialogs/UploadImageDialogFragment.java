package com.resync.app.ui.fragment_dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.R;
import com.resync.app.RedditApplication;
import com.resync.app.ui.views.buttons.upload.CameraButton;
import com.resync.app.ui.views.editor.EditorView;
import com.resync.app.ui.views.images.CustomImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.resync.app.helpers.ui.AlertDialogHelper;
import com.resync.app.helpers.ui.ProgressDialogHelper;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.net.thirdparty.ImgurUploadError;
import com.resync.app.net.thirdparty.UploadManager;
import com.resync.app.util.Logger;
import com.resync.app.media.glide.ImageHelper;

/* JADX INFO: loaded from: classes.dex */
public class UploadImageDialogFragment extends AppCompatDialogFragment {
    public static final String TAG = UploadImageDialogFragment.class.getSimpleName();
    private int mMode;
    private Uri mImageUri;
    private EditorView mEditorView;

    @BindView(R.id.dialog_upload_image_button_wrapper)
    LinearLayout mButtonWrapper;

    @BindView(R.id.dialog_upload_image_button_camera)
    CameraButton mCameraButton;

    @BindView(R.id.dialog_upload_image_indicator)
    ImageView mImageIndicator;

    @BindView(R.id.dialog_upload_image_preview)
    CustomImageView mPreview;

    class DialogShowListener implements DialogInterface.OnShowListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ androidx.appcompat.app.AlertDialog dialog;

        /* JADX INFO: renamed from: com.resync.app.ui.fragment_dialogs.UploadImageDialogFragment$a$a, reason: collision with other inner class name */
        class SelectImageClickListener implements View.OnClickListener {
            SelectImageClickListener() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                UploadImageDialogFragment.this.uploadImage();
            }
        }

        class CropImageClickListener implements View.OnClickListener {
            CropImageClickListener() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (UploadImageDialogFragment.this.mImageUri == null) {
                    StaticToast.showWithContext(UploadImageDialogFragment.this.getActivity(), "No image selected");
                    return;
                }
                try {
                    CropImage.activity(UploadImageDialogFragment.this.mImageUri).setGuidelines(CropImageView.Guidelines.ON).setOutputUri(ImageHelper.createTempImageUri()).start(UploadImageDialogFragment.this.getContext(), UploadImageDialogFragment.this);
                } catch (Exception e) {
                    StaticToast.show("Error loa");
                    Logger.logException(e);
                }
            }
        }

        DialogShowListener(androidx.appcompat.app.AlertDialog dialog) {
            this.dialog = dialog;
        }

        @Override // android.content.DialogInterface.OnShowListener
        public void onShow(DialogInterface dialogInterface) {
            this.dialog.getButton(-1).setOnClickListener(new SelectImageClickListener());
            this.dialog.getButton(-3).setOnClickListener(new CropImageClickListener());
        }
    }

    class UploadResponseListener implements Response.Listener<String> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ ProgressDialog progressDialog;

        UploadResponseListener(ProgressDialog progressDialog) {
            this.progressDialog = progressDialog;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(String str) {
            ProgressDialog progressDialog = this.progressDialog;
            if (progressDialog == null || !progressDialog.isShowing()) {
                return;
            }
            try {
                this.progressDialog.dismiss();
                UploadImageDialogFragment.this.onUploadComplete(str);
            } catch (Exception e) {
                Logger.logException(e);
            }
        }
    }

    class UploadErrorListener implements Response.ErrorListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ ProgressDialog progressDialog;

        UploadErrorListener(ProgressDialog progressDialog) {
            this.progressDialog = progressDialog;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (volleyError instanceof ImgurUploadError) {
                try {
                    ProgressDialog progressDialog = this.progressDialog;
                    if (progressDialog == null || !progressDialog.isShowing()) {
                        return;
                    }
                    this.progressDialog.dismiss();
                    StaticToast.showWithContext(UploadImageDialogFragment.this.getActivity(), volleyError.getMessage());
                    return;
                } catch (Exception e) {
                    Logger.logException(e);
                    return;
                }
            }
            try {
                ProgressDialog progressDialog2 = this.progressDialog;
                if (progressDialog2 == null || !progressDialog2.isShowing()) {
                    return;
                }
                this.progressDialog.dismiss();
                StaticToast.showWithContext(UploadImageDialogFragment.this.getActivity(), "Failed to upload image");
            } catch (Exception e) {
                Logger.logException(e);
            }
        }
    }

    private void loadPreview(Uri uri) {
        this.mButtonWrapper.setVisibility(8);
        com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).load(uri).into(this.mPreview);
    }

    public static UploadImageDialogFragment newInstance(Uri uri, String str) {
        UploadImageDialogFragment uploadImageDialogFragment = new UploadImageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("mode", 0);
        if (uri != null) {
            bundle.putString("uri", uri.toString());
        }
        if (str != null) {
            bundle.putString("mime", str);
        }
        uploadImageDialogFragment.setArguments(bundle);
        return uploadImageDialogFragment;
    }

    @Override // androidx.appcompat.app.AppCompatDialogFragment, androidx.fragment.app.c
    public Dialog onCreateDialog(Bundle bundle) {
        this.mMode = getArguments().getInt("mode");
        View viewInflate = View.inflate(getActivity(), R.layout.dialog_upload_image, null);
        ButterKnife.bind(this, viewInflate);
        androidx.appcompat.app.AlertDialog dialog = AlertDialogHelper.newBuilder(getContext()).setTitle(getTitle()).setPositiveButton("Upload", null).setNegativeButton("Cancel", null).setNeutralButton("Edit", null).setView(viewInflate).create();
        dialog.setOnShowListener(new DialogShowListener(dialog));
        if (getArguments().containsKey("uri")) {
            String string = getArguments().getString("uri");
            this.mImageUri = Uri.parse(string);
            if ("image/gif".equalsIgnoreCase(getArguments().getString("mime"))) {
                this.mImageIndicator.setImageBitmap(RedditApplication.playGifBitmap);
            }
            this.mButtonWrapper.setVisibility(8);
            com.bumptech.glide.Glide.with(RedditApplication.getAppContext()).asGif().load(string).into(this.mPreview);
        }
        return dialog;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 100) {
            if (intent == null) {
                return;
            }
            Uri uri = Uri.parse(intent.getDataString());
            this.mImageUri = uri;
            loadPreview(uri);
        } else if (requestCode == 101 && this.mImageUri != null) {
            getActivity().getContentResolver().notifyChange(this.mImageUri, null);
            loadPreview(this.mImageUri);
        }
        if (requestCode == 203) {
            CropImage.ActivityResult activityResultB = CropImage.getActivityResult(intent);
            if (resultCode != -1) {
                if (resultCode == 204) {
                    StaticToast.showWithContext(getContext(), "Error editing image!");
                }
            } else {
                if (intent == null) {
                    return;
                }
                this.mPreview.setImageBitmap(null);
                Uri uriG = activityResultB.getUri();
                this.mImageUri = uriG;
                loadPreview(uriG);
            }
        }
    }

    public void setEditorView(EditorView editorView) {
        this.mEditorView = editorView;
    }

    public void onUploadComplete(String str) {
        if (getDialog() != null) {
            getDialog().dismiss();
            EditorView editorView = this.mEditorView;
            if (editorView != null) {
                editorView.insertImageDialog(str);
            }
        }
    }

    public void uploadImage() {
        if (this.mImageUri == null) {
            StaticToast.showWithContext(getActivity(), "No image selected");
            return;
        }
        ProgressDialog progressDialogA = ProgressDialogHelper.create(getActivity());
        progressDialogA.setProgressStyle(0);
        progressDialogA.setMessage("Uploading...");
        progressDialogA.setIndeterminate(true);
        progressDialogA.show();
        UploadManager.upload(getActivity(), this.mImageUri, new UploadResponseListener(progressDialogA), new UploadErrorListener(progressDialogA));
    }

    String getTitle() {
        int mode = this.mMode;
        if (mode == 0) {
            return "Upload single image";
        }
        if (mode == 1) {
            return "Select image";
        }
        throw new RuntimeException("Unsupported mode: " + this.mMode);
    }

    @OnClick
    public void onCameraButtonClicked() {
        try {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            Uri uriA = ImageHelper.createTempImageUri();
            intent.putExtra("output", uriA);
            intent.setFlags(1);
            intent.setFlags(2);
            this.mImageUri = uriA;
            startActivityForResult(intent, 101);
        } catch (Exception e) {
            StaticToast.show("Error starting camera");
            Logger.logException(e);
        }
    }

    @OnClick
    public void onGalleryButtonClicked() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT", (Uri) null);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }
}
