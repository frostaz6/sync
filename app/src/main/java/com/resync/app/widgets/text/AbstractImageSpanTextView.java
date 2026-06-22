package com.resync.app.widgets.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import com.resync.app.internal.SyncStyleable;
import com.resync.app.helpers.ui.MaterialUtil;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.widgets.text.SpannableTextView;
import com.resync.app.helpers.PaddingHelper;

/* JADX INFO: renamed from: pc.b */
/* JADX INFO: compiled from: AbstractImageSpanTextView.java */
/* JADX INFO: loaded from: classes3.dex */
public abstract class AbstractImageSpanTextView extends SpannableTextView {
    public AbstractImageSpanTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, SyncStyleable.IMAGE_SPAN_TEXT_VIEW);
        if (typedArrayObtainStyledAttributes.getBoolean(1, false)) {
            applyChipStyle();
        }
        setTextSize(0, getTextSize() * typedArrayObtainStyledAttributes.getFloat(0, 1.0f));
        typedArrayObtainStyledAttributes.recycle();
        setOnClickListener(new View.OnClickListener() { // from class: pc.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AbstractImageSpanTextView.this.onImageSpanClick(view);
            }
        });
    }

    protected final int getAscent() {
        TextPaint paint = getPaint();
        return paint != null ? Math.abs(paint.getFontMetricsInt().ascent) : (int) (((double) getLineHeight()) * 0.9d);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onImageSpanClick(View view) {
    }

    protected void applyChipStyle() {
        PaddingHelper.setPaddingLTRB(this, 10, 4, 10, 4);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(MonetUtil.getCardColorAuto());
        gradientDrawable.setCornerRadius(MaterialUtil.getCornerRadius());
        setBackground(gradientDrawable);
        setTextColor(MonetUtil.getTertiaryTextColorAuto());
    }
}
