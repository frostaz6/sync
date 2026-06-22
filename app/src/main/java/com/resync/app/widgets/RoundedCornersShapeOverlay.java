package com.resync.app.widgets;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: renamed from: jb.b */
/* JADX INFO: compiled from: RoundedCornersShapeOverlay.java */
/* JADX INFO: loaded from: classes3.dex */
public class RoundedCornersShapeOverlay implements ShapeOverlay {

    private int colorMode;

    private int height;

    private Paint paint = new Paint();
    private Path topPath = new Path();
    private Path bottomPath = new Path();
    private float cornerRadius = SizeHelper.dpToPxFloat(12);

    public RoundedCornersShapeOverlay(int colorMode) {
        this.colorMode = colorMode;
    }

    @Override // com.resync.app.widgets.ShapeOverlay
    public void update(int width, int height, int oldWidth, int oldHeight) {
        this.height = height;
        if (this.cornerRadius <= 0.0f || width <= 0 || height <= 0 || width == oldWidth) {
            return;
        }
        this.topPath.rewind();
        this.bottomPath.rewind();
        this.topPath.moveTo(this.cornerRadius, 0.0f);
        this.topPath.quadTo(0.0f, 0.0f, 0.0f, this.cornerRadius);
        this.topPath.lineTo(0.0f, 0.0f);
        float widthF = width;
        this.topPath.moveTo(widthF - this.cornerRadius, 0.0f);
        this.topPath.quadTo(widthF, 0.0f, widthF, this.cornerRadius);
        this.topPath.lineTo(widthF, 0.0f);
        this.bottomPath.moveTo(0.0f, 0.0f);
        Path path = this.bottomPath;
        float radius = this.cornerRadius;
        path.quadTo(0.0f, radius, radius, radius);
        this.bottomPath.lineTo(0.0f, this.cornerRadius);
        this.bottomPath.moveTo(widthF, 0.0f);
        Path path2 = this.bottomPath;
        float radius2 = this.cornerRadius;
        path2.quadTo(widthF, radius2, widthF - radius2, radius2);
        this.bottomPath.lineTo(widthF, this.cornerRadius);
    }

    @Override // com.resync.app.widgets.ShapeOverlay
    public void draw(Canvas canvas) {
        if (this.topPath == null || this.bottomPath == null) {
            return;
        }
        this.paint.setColor(ShapeOverlayUtils.resolveColor(this.colorMode));
        canvas.drawPath(this.topPath, this.paint);
        canvas.save();
        canvas.translate(0.0f, this.height - this.cornerRadius);
        canvas.drawPath(this.bottomPath, this.paint);
        canvas.restore();
    }
}
