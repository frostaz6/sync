package com.resync.app.dialogs;

import android.os.Bundle;
import com.resync.app.RedditApplication;
import com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.model.AbstractFlair;
import com.resync.app.model.RichTextFlair;
import com.resync.app.model.TextFlair;
import com.resync.app.model.Post;
import com.resync.app.net.oauth.OAuthSelectFlairPostRequest;

/* JADX INFO: renamed from: y9.a */
/* JADX INFO: compiled from: EditPostFlairInputDialogBottomSheet.java */
/* JADX INFO: loaded from: classes.dex */
public class EditPostFlairInputDialogBottomSheet extends AbstractInputDialogBottomSheet {
    Post post;
    AbstractFlair flair;

    public static Bundle buildArguments(Post post, AbstractFlair abstractFlair) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Post", post);
        bundle.putSerializable("flair", abstractFlair);
        return bundle;
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public void onInputSetup() {
        AbstractFlair abstractFlair = this.flair;
        if (abstractFlair instanceof RichTextFlair) {
            setInputText(((RichTextFlair) abstractFlair).getDisplayString());
        } else if (abstractFlair instanceof TextFlair) {
            setInputText(((TextFlair) abstractFlair).getDisplayText().toString());
        }
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public void onPositiveButtonClicked(String str) {
        AbstractFlair abstractFlair = this.flair;
        TextFlair textFlair = new TextFlair(abstractFlair.id, abstractFlair.getCssClass());
        textFlair.setText(this.mTextInputEditText.getText().toString());
        OAuthRequestManager.sendWithAppContext(new OAuthSelectFlairPostRequest(RedditApplication.getAppContext(), this.post, textFlair));
        dismissAllowingStateLoss();
    }

    @Override // com.resync.app.ui.fragment_dialogs.bottom.material_dialogs.base.AbstractInputDialogBottomSheet
    public void setInputText(String str) {
        this.mTextInputEditText.setText(str);
        if (StringUtils.isNotEmpty(str)) {
            this.mTextInputEditText.setSelection(str.length());
        }
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public boolean requiresArguments() {
        return true;
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getPositiveButtonText() {
        return "Submit";
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getHint() {
        return "Flair text";
    }

    @Override // com.resync.app.dialogs.CustomBottomSheetDialogFragment
    public void loadCustomArguments() {
        this.post = (Post) getArguments().getSerializable("Post");
        this.flair = (AbstractFlair) getArguments().getSerializable("flair");
    }

    @Override // com.resync.app.dialogs.interfaces.InputDialogBottomSheetInterface
    public String getTitle() {
        return "Edit post flair";
    }
}
