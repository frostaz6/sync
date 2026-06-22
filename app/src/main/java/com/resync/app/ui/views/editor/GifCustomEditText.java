package com.resync.app.ui.views.editor;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import androidx.core.os.BuildCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import com.resync.app.RedditApplication;
import com.resync.app.ui.views.responsive.CustomEditText;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.events.OnImageKeyboardImageEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.singletons.StyleSingleton;

/* JADX INFO: loaded from: classes3.dex */
public class GifCustomEditText extends CustomEditText {

    class ImageCommitListener implements InputConnectionCompat.OnCommitContentListener {
        ImageCommitListener() {
        }

        @Override // androidx.core.view.inputmethod.InputConnectionCompat.OnCommitContentListener
        public boolean onCommitContent(InputContentInfoCompat inputContentInfoCompat, int flags, Bundle bundle) {
            if (BuildCompat.isAtLeastNMR1() && (flags & 1) != 0) {
                try {
                    inputContentInfoCompat.requestPermission();
                } catch (Exception unused) {
                    return false;
                }
            }
            BusSingleton.getBus().post(new OnImageKeyboardImageEvent(inputContentInfoCompat.getContentUri(), inputContentInfoCompat.getDescription().getMimeType(0)));
            return true;
        }
    }

    public GifCustomEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setTextColor(StyleSingleton.getTextColorAuto(RedditApplication.getAppContext(), false));
        setHintTextColor(MonetUtil.getSecondaryTextColorAuto());
    }

    @Override // android.widget.TextView, android.view.View
    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        InputConnection inputConnectionOnCreateInputConnection = super.onCreateInputConnection(editorInfo);
        EditorInfoCompat.setContentMimeTypes(editorInfo, new String[]{"image/gif", "image/png"});
        return InputConnectionCompat.createWrapper(inputConnectionOnCreateInputConnection, editorInfo, new ImageCommitListener());
    }
}
