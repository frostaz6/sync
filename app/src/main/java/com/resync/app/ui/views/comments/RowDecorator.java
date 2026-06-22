package com.resync.app.ui.views.comments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: renamed from: com.resync.app.ui.views.comments.a */
/* JADX INFO: compiled from: RowDecorator.java */
/* JADX INFO: loaded from: classes3.dex */
public class RowDecorator {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    Context context;
    View parentView;
    Paint paint;
    int paddingLeft;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    int paddingVertical;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    int paddingVerticalCompact;
    int depth;
    int indentWidth;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    int indentTotal;
    int viewHeight;
    int viewWidth;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    boolean drawDivider;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    boolean compact;
    private int position;

    /* JADX INFO: renamed from: com.resync.app.ui.views.comments.a$a */
    /* JADX INFO: compiled from: RowDecorator.java */
    public interface RowDecoratorHolder {
    }

    public RowDecorator(Context context, View view, boolean compact) {
        if (!(view instanceof RowDecoratorHolder)) {
            throw new RuntimeException("Parent view does not implement RowDecoratorHolder");
        }
        this.compact = compact;
        this.context = context;
        this.parentView = view;
        this.paint = new Paint();
        if (SettingsSingleton.getSettings().commentPadding == 0) {
            this.indentWidth = SizeHelper.dpToPx(16);
            this.paddingLeft = (int) SizeHelper.dpToPxFloat(16);
            this.paddingVertical = (int) SizeHelper.dpToPxFloat(16);
            this.paddingVerticalCompact = (int) SizeHelper.dpToPxFloat(10);
            return;
        }
        if (SettingsSingleton.getSettings().commentPadding == 1) {
            this.indentWidth = SizeHelper.dpToPx(16);
            this.paddingLeft = (int) SizeHelper.dpToPxFloat(16);
            this.paddingVertical = (int) SizeHelper.dpToPxFloat(12);
            this.paddingVerticalCompact = (int) SizeHelper.dpToPxFloat(10);
            return;
        }
        if (SettingsSingleton.getSettings().commentPadding == 2) {
            this.indentWidth = SizeHelper.dpToPx(8);
            this.paddingLeft = (int) SizeHelper.dpToPxFloat(8);
            this.paddingVertical = (int) SizeHelper.dpToPxFloat(8);
            this.paddingVerticalCompact = (int) SizeHelper.dpToPxFloat(8);
            return;
        }
        if (SettingsSingleton.getSettings().commentPadding == 3) {
            this.indentWidth = SizeHelper.dpToPx(10);
            this.paddingLeft = (int) SizeHelper.dpToPxFloat(16);
            this.paddingVertical = (int) SizeHelper.dpToPxFloat(16);
            this.paddingVerticalCompact = (int) SizeHelper.dpToPxFloat(8);
            return;
        }
        if (SettingsSingleton.getSettings().commentPadding == 4) {
            this.indentWidth = (int) view.getResources().getDimension(R.dimen.comment_row_spacer);
            this.paddingLeft = (int) SizeHelper.dpToPxFloat(10);
        }
    }

    private int getDepthColor(int depth) {
        if (!SettingsSingleton.getSettings().colorfulComments) {
            return depth % 2 == 0 ? -11974327 : -7500403;
        }
        int colorIndex = depth % 5;
        if (colorIndex == 0) {
            return -1499549;
        }
        if (colorIndex == 1) {
            return -12286730;
        }
        if (colorIndex == 2) {
            return -6543440;
        }
        if (colorIndex != 3) {
            return colorIndex != 4 ? 0 : -26624;
        }
        return -11751600;
    }

    private boolean isFullWidthMode() {
        return SettingsSingleton.getSettings().commentPadding == 4;
    }

    public void draw(Canvas canvas) {
        if (isFullWidthMode()) {
            this.paint.setColor(MonetUtil.getBackgroundColorAuto());
            canvas.drawRect(this.indentTotal - this.indentWidth, 0.0f, this.viewWidth, this.viewHeight, this.paint);
            int depth = this.depth;
            if (depth > 0) {
                this.paint.setColor(getDepthColor(depth));
                canvas.drawRect(this.indentTotal - this.indentWidth, 0.0f, this.indentTotal, this.viewHeight, this.paint);
                this.paint.setColor(MonetUtil.getWindowBackgroundColorAuto());
                canvas.drawRect(0.0f, 0.0f, (this.depth - 1) * this.indentWidth, this.viewHeight, this.paint);
            }
            if (this.drawDivider && SettingsSingleton.getSettings().commentDividers) {
                this.paint.setColor(ColorUtils.setAlphaComponent(MonetUtil.getNeutralTextColorAuto(), 63));
                canvas.drawRect(this.indentTotal - this.indentWidth, 0.0f, this.viewWidth, SizeHelper.dpToPx(1), this.paint);
                return;
            }
            return;
        }
        this.paint.setAlpha(255);
        this.paint.setColor(MonetUtil.getBackgroundColorAuto());
        canvas.drawRect(0.0f, 0.0f, this.viewWidth, this.viewHeight, this.paint);
        if (this.depth > 0) {
            for (int level = 1; level <= this.depth; level++) {
                if (SettingsSingleton.getSettings().colorfulComments) {
                    this.paint.setAlpha(255);
                    this.paint.setColor(getDepthColor(level));
                } else {
                    this.paint.setColor(MonetUtil.getNeutralTextColorAuto());
                    this.paint.setAlpha(85);
                }
                int left = this.paddingLeft;
                int levelIndex = level - 1;
                int indentWidth = this.indentWidth;
                canvas.drawRect((levelIndex * indentWidth) + left, 0.0f, (levelIndex * indentWidth) + left + SizeHelper.dpToPx(2), this.viewHeight, this.paint);
            }
        }
        if (this.drawDivider && SettingsSingleton.getSettings().commentDividers) {
            this.paint.setColor(ColorUtils.setAlphaComponent(MonetUtil.getNeutralTextColorAuto(), 63));
            int dividerLevel = this.depth + 1;
            int indentWidth = this.indentWidth;
            canvas.drawRect((dividerLevel * indentWidth) - indentWidth, 0.0f, this.viewWidth, SizeHelper.dpToPx(1), this.paint);
        }
    }

    public void setSize(int width, int height) {
        this.viewWidth = width;
        this.viewHeight = height;
    }

    public void setup(int position, int depth, boolean drawDivider, boolean unused) {
        this.position = position;
        if (isFullWidthMode()) {
            this.depth = depth;
            this.drawDivider = drawDivider;
            int indentTotal = depth * this.indentWidth;
            this.indentTotal = indentTotal;
            View view = this.parentView;
            int left = this.paddingLeft;
            view.setPadding(indentTotal + left, left, left, left);
        } else {
            this.depth = depth;
            this.drawDivider = drawDivider;
            int indentTotal = depth * this.indentWidth;
            this.indentTotal = indentTotal;
            if (this.compact) {
                View view2 = this.parentView;
                int left = this.paddingLeft;
                int verticalPadding = this.paddingVerticalCompact;
                view2.setPadding(indentTotal + left, verticalPadding, left, verticalPadding);
            } else {
                View view3 = this.parentView;
                int left = this.paddingLeft;
                int verticalPadding = this.paddingVertical;
                view3.setPadding(indentTotal + left, verticalPadding, left, verticalPadding);
            }
        }
        this.parentView.invalidate();
    }
}
