package com.resync.app.widgets;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.resync.app.RedditApplication;
import uk.co.senab.photoview.PhotoView;

/* JADX INFO: renamed from: ec.a */
/* JADX INFO: compiled from: CustomPhotoView.java */
/* JADX INFO: loaded from: classes3.dex */
public class CustomPhotoView extends PhotoView {
    public CustomPhotoView(Context context) {
        super(context);
    }

    public void clearImage() {
        if (!isGif()) {
            setImageDrawable(null);
            return;
        }
        ((GifDrawable) getDrawable()).stop();
        setImageDrawable(null);
        Glide.with(RedditApplication.getAppContext()).clear(this);
    }

    public int getCurrentPosition() {
        return 0;
    }

    public int getDuration() {
        return 0;
    }

    public boolean hasImage() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return false;
        }
        return drawable instanceof BitmapDrawable ? ((BitmapDrawable) drawable).getBitmap() != null : drawable instanceof GifDrawable;
    }

    @Override // android.widget.ImageView
    public Drawable getDrawable() {
        return super.getDrawable();
    }

    public boolean isGif() {
        return getDrawable() != null && (getDrawable() instanceof GifDrawable);
    }

    public void stopGif() {
        if (isGif()) {
            ((GifDrawable) getDrawable()).stop();
        }
    }

    public void recycleGif() {
        if (isGif()) {
            ((GifDrawable) getDrawable()).stop();
            ((GifDrawable) getDrawable()).recycle();
        }
    }

    public void startGif() {
        if (isGif()) {
            ((GifDrawable) getDrawable()).start();
        }
    }
}
