package com.resync.app.ui.views.posts.simple;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.widgets.CircularShapeOverlay;
import com.resync.app.widgets.RoundedCornersShapeOverlay;
import com.resync.app.widgets.ShapeOverlay;

/* JADX INFO: loaded from: classes3.dex */
public class ImageFrameLayout extends FrameLayout {

    private ShapeOverlay shapeOverlay;

    public ImageFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setWillNotDraw(false);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, SyncStyleable.SHAPED_IMAGE_FRAME);
        boolean roundedCorners = typedArrayObtainStyledAttributes.getBoolean(1, false);
        boolean circular = typedArrayObtainStyledAttributes.getBoolean(2, false);
        int radius = typedArrayObtainStyledAttributes.getInt(0, 2);
        typedArrayObtainStyledAttributes.recycle();
        if (roundedCorners && circular) {
            throw new RuntimeException("Cannot have rounded corners and circle shape");
        }
        if (circular) {
            setRoundedCorners(radius);
        } else if (roundedCorners) {
            setCircular(radius);
        }
    }

    public void clearShape() {
        this.shapeOverlay = null;
    }

    public void setCircular(int radius) {
        this.shapeOverlay = new CircularShapeOverlay(radius);
    }

    public void setRoundedCorners(int radius) {
        this.shapeOverlay = new RoundedCornersShapeOverlay(radius);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        ShapeOverlay shapeOverlay = this.shapeOverlay;
        if (shapeOverlay != null) {
            shapeOverlay.draw(canvas);
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        ShapeOverlay shapeOverlay = this.shapeOverlay;
        if (shapeOverlay != null) {
            shapeOverlay.update(width, height, oldWidth, oldHeight);
        }
    }
}
