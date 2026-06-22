package com.resync.app.ui.views.preferences;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.helpers.TypefaceManager;

/* JADX INFO: loaded from: classes3.dex */
public class PreferenceCheckedView extends View {

    Paint paint;
    boolean checked;

    public PreferenceCheckedView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public void init() {
        setWillNotDraw(false);
        Paint paint = new Paint();
        this.paint = paint;
        paint.setAntiAlias(true);
        this.paint.setTextSize(SizeHelper.dpToPxFloat(18));
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        String str;
        super.onDraw(canvas);
        if (this.checked) {
            this.paint.setColor(-7617718);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, this.paint);
        } else {
            this.paint.setColor(-1762269);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, this.paint);
        }
        this.paint.setColor(-1);
        if (this.checked) {
            this.paint.setTypeface(TypefaceManager.getRegular());
            str = "✓";
        } else {
            this.paint.setTypeface(TypefaceManager.getBold());
            str = "!";
        }
        Rect bounds = new Rect();
        this.paint.getTextBounds(str, 0, str.length(), bounds);
        canvas.drawText(str, (getWidth() / 2) - (this.paint.measureText(str) / 2.0f), (getHeight() / 2) + (bounds.height() / 2.0f), this.paint);
    }
}
