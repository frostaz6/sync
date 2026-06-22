package com.theartofdev.edmodo.cropper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.File;
import java.io.IOException;
import okhttp3.internal.http.HttpStatusCodesKt;

/* Stub: hosts a CropImageView programmatically (no R resources available).
 * Preserves the CropImageView.OnSetImageUriCompleteListener /
 * CropImageView.OnCropImageCompleteListener contract and the
 * result wiring (request code 203, result codes -1 / 204 / 0).
 * Full crop UI is not wired up. */
public class CropImageActivity extends AppCompatActivity implements CropImageView.OnSetImageUriCompleteListener, CropImageView.OnCropImageCompleteListener {
    private CropImageView mCropImageView;
    private Uri mCropImageUri;
    private CropImageOptions mOptions;

    protected void cropImage() {
        if (this.mOptions.noOutputImage) {
            setResult(null, null, 1);
            return;
        }
        Uri outputUri = getOutputUri();
        CropImageOptions options = this.mOptions;
        this.mCropImageView.startCropWorkerTask(outputUri, options.outputCompressFormat, options.outputCompressQuality, options.outputRequestWidth, options.outputRequestHeight, options.outputRequestSizeOptions);
    }

    protected Uri getOutputUri() {
        Uri uri = this.mOptions.outputUri;
        if (uri != null && !uri.equals(Uri.EMPTY)) {
            return uri;
        }
        try {
            Bitmap.CompressFormat compressFormat = this.mOptions.outputCompressFormat;
            return Uri.fromFile(File.createTempFile("cropped", compressFormat == Bitmap.CompressFormat.JPEG ? ".jpg" : compressFormat == Bitmap.CompressFormat.PNG ? ".png" : ".webp", getCacheDir()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp file for output image", e);
        }
    }

    protected Intent getResultIntent(Uri uri, Exception exc, int sampleSize) {
        CropImage.ActivityResult activityResult = new CropImage.ActivityResult(this.mCropImageView.getImageUri(), uri, exc, this.mCropImageView.getCropPoints(), this.mCropImageView.getCropRect(), this.mCropImageView.getRotatedDegrees(), this.mCropImageView.getWholeImageRect(), sampleSize);
        Intent intent = new Intent();
        intent.putExtras(getIntent());
        intent.putExtra("CROP_IMAGE_EXTRA_RESULT", activityResult);
        return intent;
    }

    @Override // com.theartofdev.edmodo.cropper.CropImageView.OnSetImageUriCompleteListener
    public void onSetImageUriComplete(CropImageView cropImageView, Uri uri, Exception exc) {
        if (exc != null) {
            setResult(null, exc, 1);
            return;
        }
        Rect rect = this.mOptions.initialCropWindowRectangle;
        if (rect != null) {
            this.mCropImageView.setCropRect(rect);
        }
        int initialRotation = this.mOptions.initialRotation;
        if (initialRotation > -1) {
            this.mCropImageView.setRotatedDegrees(initialRotation);
        }
    }

    protected void rotateImage(int degrees) {
        this.mCropImageView.rotateImage(degrees);
    }

    protected void setResult(Uri uri, Exception exc, int sampleSize) {
        setResult(exc == null ? -1 : HttpStatusCodesKt.HTTP_NO_CONTENT, getResultIntent(uri, exc, sampleSize));
        finish();
    }

    protected void setResultCancel() {
        setResult(0);
        finish();
    }

    @Override // com.theartofdev.edmodo.cropper.CropImageView.OnCropImageCompleteListener
    public void onCropImageComplete(CropImageView cropImageView, CropImageView.CropResult result) {
        setResult(result.getUri(), result.getError(), result.getRotation());
    }

    @Override
    @SuppressLint({"NewApi"})
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 200) {
            if (resultCode == 0) {
                setResultCancel();
            }
            if (resultCode == -1) {
                Uri pickImageResultUri = CropImage.getPickImageResultUri(this, intent);
                this.mCropImageUri = pickImageResultUri;
                this.mCropImageView.setImageUriAsync(pickImageResultUri);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResultCancel();
    }

    @Override
    @SuppressLint({"NewApi"})
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mCropImageView = new CropImageView(this, (android.util.AttributeSet) null);
        setContentView(this.mCropImageView, new ViewGroup.LayoutParams(-1, -1));
        Bundle bundleExtra = getIntent().getBundleExtra("CROP_IMAGE_EXTRA_BUNDLE");
        if (bundleExtra != null) {
            this.mCropImageUri = (Uri) bundleExtra.getParcelable("CROP_IMAGE_EXTRA_SOURCE");
            this.mOptions = (CropImageOptions) bundleExtra.getParcelable("CROP_IMAGE_EXTRA_OPTIONS");
        }
        if (this.mOptions == null) {
            this.mOptions = new CropImageOptions();
        }
        if (bundle == null) {
            Uri uri = this.mCropImageUri;
            if (uri != null && !uri.equals(Uri.EMPTY)) {
                this.mCropImageView.setImageUriAsync(uri);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.mCropImageView.setOnSetImageUriCompleteListener(this);
        this.mCropImageView.setOnCropImageCompleteListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.mCropImageView.setOnSetImageUriCompleteListener(null);
        this.mCropImageView.setOnCropImageCompleteListener(null);
    }
}
