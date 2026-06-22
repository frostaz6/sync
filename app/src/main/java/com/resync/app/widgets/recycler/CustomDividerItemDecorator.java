package com.resync.app.widgets.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.singleton.UiSingleton;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: renamed from: lb.d */
/* JADX INFO: compiled from: CustomDividerItemDecorator.java */
/* JADX INFO: loaded from: classes3.dex */
public class CustomDividerItemDecorator extends RecyclerView.ItemDecoration {

    int mode;
    Paint paint = new Paint();

    public CustomDividerItemDecorator(Context context, int mode) {
        this.mode = mode;
    }

    private void setRectInDp(Rect rect, int left, int top, int right, int bottom) {
        rect.set(SizeHelper.dpToPx(left), SizeHelper.dpToPx(top), SizeHelper.dpToPx(right), SizeHelper.dpToPx(bottom));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        int mode = this.mode;
        if (mode == 5) {
            if (SettingsSingleton.getSettings().slidesThickDivider) {
                setRectInDp(rect, 0, 0, 0, 8);
                return;
            } else {
                setRectInDp(rect, 0, 0, 0, 1);
                return;
            }
        }
        if (mode == 4) {
            if (UiSingleton.isFullWidth(mode)) {
                setRectInDp(rect, 0, 0, 0, 8);
                return;
            } else {
                setRectInDp(rect, 8, 8, 8, 8);
                return;
            }
        }
        if (mode == 3) {
            setRectInDp(rect, 8, 8, 8, 8);
            return;
        }
        if (mode == 2) {
            setRectInDp(rect, 16, 8, 16, 8);
        } else if (mode == 1 || mode == 0) {
            setRectInDp(rect, 0, 0, 0, 1);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        int extraSpace;
        if (MonetUtil.isAmoled() && SettingsSingleton.getSettings().amoled_divider) {
            int paddingLeft = recyclerView.getPaddingLeft();
            int width = recyclerView.getWidth() - recyclerView.getPaddingRight();
            int childCount = recyclerView.getChildCount();
            for (int childIndex = 0; childIndex < childCount; childIndex++) {
                View childAt = recyclerView.getChildAt(childIndex);
                int bottom = childAt.getBottom();
                int mode = this.mode;
                if (mode == 5) {
                    extraSpace = SettingsSingleton.getSettings().slidesThickDivider ? SizeHelper.dpToPx(4) : SizeHelper.dpToPx(1) / 2;
                } else if (mode == 4 || mode == 3 || mode == 2) {
                    extraSpace = SizeHelper.dpToPx(8);
                } else {
                    extraSpace = 0;
                }
                bottom += extraSpace;
                int dividerBottom = SizeHelper.dpToPx(1) + bottom + 1;
                this.paint.setColor(ColorUtils.setAlphaComponent(MonetUtil.getPrimaryTextColor(true), 50));
                canvas.drawRect(paddingLeft, bottom, width, dividerBottom, this.paint);
            }
        }
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
