package com.resync.app.widgets;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/* JADX INFO: renamed from: jb.a */
/* JADX INFO: compiled from: CircularShapeOverlay.java */
/* JADX INFO: loaded from: classes3.dex */
public class CircularShapeOverlay implements ShapeOverlay {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int colorMode;
    private Paint paint = new Paint();
    private Path path = new Path();

    public CircularShapeOverlay(int colorMode) {
        this.colorMode = colorMode;
    }

    @Override // com.resync.app.widgets.ShapeOverlay
    public void update(int width, int height, int oldWidth, int oldHeight) {
        if (width == oldWidth || height == oldHeight || width <= 0 || height <= 0) {
            return;
        }
        this.path.rewind();
        this.path.toggleInverseFillType();
        this.path.addOval(0.0f, 0.0f, width, height, Path.Direction.CW);
    }

    @Override // com.resync.app.widgets.ShapeOverlay
    public void draw(Canvas canvas) {
        if (this.path.isEmpty()) {
            return;
        }
        this.paint.setColor(ShapeOverlayUtils.resolveColor(this.colorMode));
        canvas.drawPath(this.path, this.paint);
    }
}
