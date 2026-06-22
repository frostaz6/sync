package com.resync.app.ui.fragment_dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.R;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.ui.views.core.MaterialYouToolbar;
import com.resync.app.ui.views.editor.EditorView;
import com.resync.app.ui.views.responsive.CustomEditText;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.AlertDialogHelper;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.ui.ProgressDialogHelper;
import com.resync.app.helpers.ui.StaticToast;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.events.DraftCheckerDismissed;
import com.resync.app.events.TempAccountSelectedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.widgets.ColorFilterProvider;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.text.spans.DivSpan;
import com.resync.app.model.UserAccount;
import com.resync.app.fragments.RedditDialogFragment;
import com.resync.app.helpers.NightHelper;
import com.resync.app.helpers.ThemeHelper;
import com.resync.app.util.ColorUtils;
import com.resync.app.util.Logger;
import com.resync.app.model.Post;
import com.resync.app.net.oauth.OAuthCommentRequest;
import com.resync.app.net.oauth.OAuthEditPostRequest;
import com.resync.app.net.oauth.OAuthMessageRequest;

/* JADX INFO: loaded from: classes.dex */
public class EditFragment extends RedditDialogFragment {
    private int mMode;
    private String mCursorId;
    private Post mParentPost;
    private Post mCommentPost;
    private boolean mParentEqualsComment;
    private Post mMessagePost;
    private String mMessageUser;
    private Post mEditPost;
    private UserAccount mTempAccount;

    @BindView(R.id.toolbar)
    MaterialYouToolbar mComposeToolbar;

    @BindView(R.id.fragment_edit_toolbar_buttons_discard)
    FrameLayout mDiscardButton;

    @BindView(R.id.fragment_edit_toolbar_buttons_discard_textview)
    TextView mDiscardButtonTextView;

    @BindView(R.id.fragment_edit_toolbar_buttons_done)
    FrameLayout mDoneButton;

    @BindView(R.id.fragment_edit_toolbar_buttons_done_textview)
    TextView mDoneButtonTextView;

    @BindView(R.id.editor_view)
    EditorView mEditorView;

    @BindView(R.id.fragment_edit_subject)
    CustomEditText mSubjectInput;

    @BindView(R.id.fragment_edit_toolbar_buttons)
    LinearLayout mToolbarButtons;

    @BindView(R.id.fragment_edit_username)
    CustomEditText mUsernameInput;

    class DialogKeyListener implements DialogInterface.OnKeyListener {
        DialogKeyListener() {
        }

        @Override // android.content.DialogInterface.OnKeyListener
        public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
            if (keyCode != 4 || keyEvent.getAction() != 1) {
                return false;
            }
            EditFragment.this.attemptDismiss();
            return true;
        }
    }

    class DialogClickListener implements DialogInterface.OnClickListener {
        DialogClickListener() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            EditFragment.this.save();
        }
    }

    class EditCheckListener implements Response.Listener<Boolean> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ ProgressDialog progressDialog;

        EditCheckListener(ProgressDialog progressDialog) {
            this.progressDialog = progressDialog;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(Boolean bool) {
            if (EditFragment.this.getActivity() == null) {
                return;
            }
            this.progressDialog.dismiss();
            if (bool.booleanValue() && EditFragment.this.getDialog() != null && EditFragment.this.getDialog().isShowing()) {
                StaticToast.showWithContext("Message sent", EditFragment.this.getActivity());
                EditFragment.this.dismiss();
            }
        }
    }

    class EditCheckErrorListener implements Response.ErrorListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ ProgressDialog progressDialog;

        EditCheckErrorListener(ProgressDialog progressDialog) {
            this.progressDialog = progressDialog;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            if (EditFragment.this.getActivity() == null) {
                return;
            }
            this.progressDialog.dismiss();
            String message = volleyError.getMessage();
            if (!TextUtils.isEmpty(message) && message.equalsIgnoreCase("care to try these again?")) {
                message = "Bad captcha";
            }
            if (StringUtils.isNotEmpty(message)) {
                Toast.makeText(EditFragment.this.getActivity(), message, 1).show();
            } else {
                StaticToast.show("Unknown error");
            }
        }
    }

    public EditFragment() {
        if (NightHelper.isNight()) {
            setStyle(2, R.style.EditFragmentStyle_Night);
        } else {
            setStyle(2, R.style.EditFragmentStyle_Day);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void attemptDismiss() {
        if (this.mEditorView.checkDraft(this.mParentEqualsComment)) {
            dismiss();
        }
    }

    private String getTempRefreshToken() {
        if (hasChanges()) {
            return this.mTempAccount.refreshToken;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onNavigationClicked(View view) {
        attemptDismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onMenuItemClicked(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.menu_submit) {
            return true;
        }
        if (!hasChanges()) {
            save();
            return true;
        }
        AlertDialogHelper.newBuilder(getContext()).setMessage("You are using the temporary account " + this.mTempAccount.name).setPositiveButton("Continue", new DialogClickListener()).setNegativeButton("Cancel", null).show();
        return true;
    }

    public static EditFragment newCommentInstance(String str, Post post, Post post2) {
        EditFragment editFragment = new EditFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cursor_id", str);
        bundle.putSerializable("post_parent", post);
        bundle.putSerializable("post_comment", post2);
        bundle.putInt("mode", 0);
        editFragment.setArguments(bundle);
        return editFragment;
    }

    public static EditFragment newMessageInstance(String str) {
        EditFragment editFragment = new EditFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("message_user", str);
        bundle.putInt("mode", 2);
        editFragment.setArguments(bundle);
        return editFragment;
    }

    public static EditFragment newMessageInstanceWithSubject(String str, String str2, String str3) {
        EditFragment editFragment = new EditFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("message_user", str);
        bundle.putInt("mode", 2);
        if (!TextUtils.isEmpty(str2)) {
            bundle.putString("subject", str2);
        }
        if (!TextUtils.isEmpty(str3)) {
            bundle.putString("message_message", str3);
        }
        editFragment.setArguments(bundle);
        return editFragment;
    }

    public static EditFragment newMessageReplyInstance(Post post) {
        EditFragment editFragment = new EditFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("message", post);
        bundle.putInt("mode", 2);
        editFragment.setArguments(bundle);
        return editFragment;
    }

    public static EditFragment newEditPostInstance(Post post) {
        EditFragment editFragment = new EditFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("post", post);
        bundle.putInt("mode", 1);
        editFragment.setArguments(bundle);
        return editFragment;
    }

    private void loadFromPost() {
        Post post;
        Logger.verbose("Parent post: " + this.mParentPost);
        Logger.verbose("Comment post: " + this.mCommentPost);
        Post post2 = this.mParentPost;
        if (post2 != null && (post = this.mCommentPost) != null && post2.equals(post)) {
            this.mEditorView.setTextAndFocus(this.mCommentPost.getBodyRaw());
            return;
        }
        Post post3 = this.mCommentPost;
        if (post3 != null) {
            this.mEditorView.showPreview(post3.getBodyRaw());
            return;
        }
        Post post4 = this.mParentPost;
        if (post4 == null || post4.getKind() == 1) {
            this.mEditorView.focusEditor();
            return;
        }
        if (TextUtils.isEmpty(this.mParentPost.getSelftextProcessed())) {
            this.mEditorView.focus();
            return;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append((CharSequence) this.mParentPost.getTitle());
        spannableStringBuilder.append((CharSequence) "\n\n");
        int length = spannableStringBuilder.length();
        spannableStringBuilder.append((CharSequence) "-");
        spannableStringBuilder.setSpan(new DivSpan(), length, spannableStringBuilder.length(), 33);
        spannableStringBuilder.append((CharSequence) "\n\n");
        spannableStringBuilder.append((CharSequence) this.mParentPost.getSelftextRaw());
        this.mEditorView.showPreviewSpannable(spannableStringBuilder);
    }

    private void setupTitle() {
        Post post = this.mMessagePost;
        if (post != null) {
            this.mEditorView.showPreviewHtml(post.getBodyProcessed());
            return;
        }
        this.mUsernameInput.setVisibility(0);
        this.mSubjectInput.setVisibility(0);
        if (TextUtils.isEmpty(this.mMessageUser)) {
            this.mUsernameInput.requestFocus();
        } else {
            this.mUsernameInput.setText(this.mMessageUser);
            this.mSubjectInput.requestFocus();
        }
        this.mEditorView.focusMessage();
    }

    private void setupBody() {
        this.mEditorView.setSelftext(this.mEditPost.getSelftextRaw());
    }

    private void restoreState() {
        Post post;
        String body = this.mEditorView.getBody();
        if (!this.mEditorView.hasContent()) {
            this.mEditorView.showError();
            return;
        }
        Post post2 = this.mParentPost;
        if (post2 != null && (post = this.mCommentPost) != null && post2.equals(post)) {
            OAuthRequestManager.send(getActivity(), new OAuthCommentRequest(getActivity(), this.mCursorId, null, this.mCommentPost, body, 3, false, null));
        } else if (this.mCommentPost != null) {
            OAuthRequestManager.sendWithTempTokenExchange(getActivity(), new OAuthCommentRequest(getActivity(), this.mCursorId, this.mParentPost, this.mCommentPost, body, 1, false, getTempRefreshToken()), getTempRefreshToken());
        } else {
            OAuthRequestManager.sendWithTempTokenExchange(getActivity(), new OAuthCommentRequest(getActivity(), this.mCursorId, this.mParentPost, null, body, 0, false, getTempRefreshToken()), getTempRefreshToken());
        }
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void save() {
        int mode = this.mMode;
        if (mode == 0) {
            restoreState();
        } else if (mode == 2) {
            submitEdit(null, null);
        } else {
            if (mode != 1) {
                throw new RuntimeException("TODO");
            }
            sendEdit();
        }
    }

    private void submitEdit(String str, String str2) {
        String body = this.mEditorView.getBody();
        if (!this.mEditorView.hasContent()) {
            this.mEditorView.showError();
            return;
        }
        if (this.mMessagePost != null) {
            OAuthRequestManager.send(getActivity(), new OAuthCommentRequest(getActivity(), null, null, this.mMessagePost, body, 2, false, null));
            dismiss();
            return;
        }
        String string = this.mUsernameInput.getText().toString();
        if (TextUtils.isEmpty(string)) {
            this.mUsernameInput.setError("Enter a username");
            this.mUsernameInput.requestFocus();
            return;
        }
        String string2 = this.mSubjectInput.getText().toString();
        if (TextUtils.isEmpty(string2)) {
            this.mSubjectInput.setError("Enter a subject");
            this.mSubjectInput.requestFocus();
        } else {
            ProgressDialog progressDialogA = ProgressDialogHelper.create(getActivity());
            progressDialogA.setMessage("Sending message");
            progressDialogA.show();
            OAuthRequestManager.sendWithTempTokenExchange(getActivity(), new OAuthMessageRequest(getActivity(), string, string2, body, str, str2, new EditCheckListener(progressDialogA), new EditCheckErrorListener(progressDialogA)), getTempRefreshToken());
        }
    }

    private void sendEdit() {
        OAuthRequestManager.send(getActivity(), new OAuthEditPostRequest(getActivity(), this.mEditPost.getId(), this.mEditorView.getBody()));
        dismiss();
    }

    private boolean hasChanges() {
        return this.mTempAccount != null;
    }

    @Override // androidx.appcompat.app.AppCompatDialogFragment, androidx.fragment.app.c
    public Dialog onCreateDialog(Bundle bundle) {
        Dialog dialogC3 = super.onCreateDialog(bundle);
        int backgroundColor = MonetUtil.getBackgroundColorAuto();
        if (NightHelper.isNight()) {
            dialogC3.getWindow().setNavigationBarColor(backgroundColor);
        } else {
            dialogC3.getWindow().setNavigationBarColor(backgroundColor);
            if (!ColorUtils.isDark(backgroundColor)) {
                dialogC3.getWindow().getDecorView().setSystemUiVisibility(16);
            }
        }
        return dialogC3;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mMode = getArguments().getInt("mode");
        View viewInflate = layoutInflater.inflate(R.layout.fragment_edit, (ViewGroup) null);
        ButterKnife.bind(this, viewInflate);
        if (getArguments().containsKey("post_parent")) {
            this.mComposeToolbar.setTitle("Comment");
            this.mParentPost = (Post) getArguments().getSerializable("post_parent");
        }
        if (getArguments().containsKey("post_comment")) {
            this.mComposeToolbar.setTitle("Comment");
            this.mCommentPost = (Post) getArguments().getSerializable("post_comment");
        }
        if (getArguments().containsKey("cursor_id")) {
            this.mCursorId = getArguments().getString("cursor_id");
        }
        if (getArguments().containsKey("message")) {
            this.mComposeToolbar.setTitle("Message");
            this.mMessagePost = (Post) getArguments().getSerializable("message");
        }
        if (getArguments().containsKey("message_user")) {
            this.mComposeToolbar.setTitle("Message");
            this.mMessageUser = getArguments().getString("message_user");
        }
        if (getArguments().containsKey("post")) {
            this.mComposeToolbar.setTitle("New post");
            this.mEditPost = (Post) getArguments().getSerializable("post");
        }
        if (getArguments().containsKey("subject")) {
            this.mSubjectInput.setText(getArguments().getString("subject"));
        }
        if (getArguments().containsKey("message_message")) {
            this.mEditorView.setText(getArguments().getString("message_message"));
        }
        this.mParentEqualsComment = ObjectUtils.equals(this.mParentPost, this.mCommentPost);
        return viewInflate;
    }

    @Override // androidx.fragment.app.c, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        if (hasChanges()) {
            bundle.putSerializable("UserAccountKey", this.mTempAccount);
        }
        super.onSaveInstanceState(bundle);
    }

    @Override // androidx.fragment.app.c, androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: m2 */
    public void onStart() {
        super.onStart();
        BusSingleton.getBus().register(this);
    }

    @Override // androidx.fragment.app.c, androidx.fragment.app.Fragment
    /* JADX INFO: renamed from: n2 */
    public void onStop() {
        BusSingleton.getBus().unregister(this);
        super.onStop();
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        Post post;
        super.onViewCreated(view, bundle);
        if (bundle != null) {
            UserAccount userAccount = (UserAccount) bundle.getSerializable("UserAccountKey");
            this.mTempAccount = userAccount;
            if (userAccount != null) {
                this.mEditorView.setTempAccount(userAccount);
            }
        }
        if (getActivity() instanceof BaseActivity) {
            int windowBackgroundColor = MonetUtil.getWindowBackgroundColorAuto();
            this.mToolbarButtons.setBackgroundDrawable(new ColorDrawable(windowBackgroundColor));
            boolean isLight = !ColorUtils.isDark(windowBackgroundColor);
            this.mDiscardButtonTextView.setTextColor(isLight ? -16777216 : -1);
            this.mDoneButtonTextView.setTextColor(isLight ? -16777216 : -1);
            PorterDuffColorFilter porterDuffColorFilterA = ColorFilterProvider.getColorFilter(isLight ? -16777216 : -1);
            Drawable drawableMutate = this.mDoneButtonTextView.getCompoundDrawables()[0].mutate();
            drawableMutate.setColorFilter(porterDuffColorFilterA);
            this.mDoneButtonTextView.setCompoundDrawables(drawableMutate, null, null, null);
            Drawable drawableMutate2 = this.mDiscardButtonTextView.getCompoundDrawables()[0].mutate();
            drawableMutate2.setColorFilter(porterDuffColorFilterA);
            this.mDiscardButtonTextView.setCompoundDrawables(drawableMutate2, null, null, null);
        }
        ThemeHelper.applyButtonBackground(getActivity(), this.mDiscardButton);
        ThemeHelper.applyButtonBackground(getActivity(), this.mDoneButton);
        getDialog().getWindow().setSoftInputMode(20);
        getDialog().setOnKeyListener(new DialogKeyListener());
        int mode = this.mMode;
        if (mode == 0) {
            loadFromPost();
        } else if (mode == 2) {
            setupTitle();
        } else {
            if (mode != 1) {
                throw new RuntimeException("TODO");
            }
            setupBody();
        }
        this.mEditorView.setActivity((BaseActivity) getActivity());
        if (this.mMode == 0) {
            Post post2 = this.mParentPost;
            if (post2 == null || (post = this.mCommentPost) == null || !post2.equals(post)) {
                this.mEditorView.setMessaging(true);
            } else {
                this.mEditorView.setMessaging(false);
            }
        }
        this.mComposeToolbar.inflateMenu(R.menu.editor);
        this.mComposeToolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: r9.e
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                EditFragment.this.onNavigationClicked(view2);
            }
        });
        this.mComposeToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() { // from class: r9.f
            @Override // androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
            public final boolean onMenuItemClick(MenuItem menuItem) {
                return EditFragment.this.onMenuItemClicked(menuItem);
            }
        });
        int backgroundColor = MonetUtil.getBackgroundColorAuto();
        getDialog().getWindow().setStatusBarColor(backgroundColor);
        getDialog().getWindow().setNavigationBarColor(backgroundColor);
        if (ColorUtils.isDark(backgroundColor) || Build.VERSION.SDK_INT < 26) {
            return;
        }
        getDialog().getWindow().getDecorView().setSystemUiVisibility(8208);
    }

    @Subscribe
    public void onDraftCheckedDismissed(DraftCheckerDismissed draftCheckerDismissed) {
        dismiss();
    }

    @Subscribe
    public void onTempAccountSelected(TempAccountSelectedEvent tempAccountSelectedEvent) {
        this.mTempAccount = tempAccountSelectedEvent.account;
    }
}
