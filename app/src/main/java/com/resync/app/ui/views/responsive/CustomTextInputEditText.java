package com.resync.app.ui.views.responsive;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.singletons.StyleSingleton;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.SizeHelper;

/* JADX INFO: loaded from: classes3.dex */
public class CustomTextInputEditText extends AppCompatAutoCompleteTextView {
    public CustomTextInputEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyColors(NightHelper.isNight());
        int padding = SizeHelper.dpToPx(16);
        setPadding(padding, padding, padding, padding);
        setThreshold(1);
    }

    public void applyColors(boolean night) {
        setTextColor(StyleSingleton.getTextColorAuto(getContext(), false));
        setHighlightColor(ColorUtils.setAlphaComponent(MonetUtil.getHighlightColorAuto(), 63));
    }

    @Override // androidx.appcompat.widget.AppCompatAutoCompleteTextView, android.widget.TextView, android.view.View
    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        InputConnection inputConnectionOnCreateInputConnection = super.onCreateInputConnection(editorInfo);
        if (inputConnectionOnCreateInputConnection != null && editorInfo.hintText == null) {
            ViewParent parent = getParent();
            if (parent instanceof TextInputLayout) {
                editorInfo.hintText = ((TextInputLayout) parent).getHint();
            }
        }
        return inputConnectionOnCreateInputConnection;
    }
}
