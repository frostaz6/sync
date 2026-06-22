package com.resync.app.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.resync.app.R;
import com.resync.app.ui.views.core.MaterialYouToolbar;
import com.resync.app.helpers.ui.ProgressDialogHelper;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.net.OAuthRequestManager;
import com.resync.app.net.oauth.OAuthModBanRequest;

/* JADX INFO: loaded from: classes.dex */
public class BanActivity extends BaseMaterialDragActivity {

    private MaterialYouToolbar toolbar;
    private String user;
    private String subreddit;
    private EditText durationInput;
    private EditText whyInput;
    private EditText noteInput;

    class MenuClickListener implements Toolbar.OnMenuItemClickListener {
        MenuClickListener() {
        }

        @Override // androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.getItemId() != R.id.ban_submit) {
                return false;
            }
            BanActivity.this.submitBan();
            return true;
        }
    }

    class BanSuccessListener implements Response.Listener<Void> {

        final /* synthetic */ ProgressDialog progressDialog;

        BanSuccessListener(ProgressDialog progressDialog) {
            this.progressDialog = progressDialog;
        }

        @Override // com.android.volley.Response.Listener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onResponse(Void response) {
            this.progressDialog.dismiss();
            StaticToast.showWithContext(BanActivity.this.getActivity(), "Ban submitted!");
            BanActivity.this.getActivity().finish();
        }
    }

    class BanErrorListener implements Response.ErrorListener {

        final /* synthetic */ ProgressDialog progressDialog;

        BanErrorListener(ProgressDialog progressDialog) {
            this.progressDialog = progressDialog;
        }

        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(VolleyError volleyError) {
            this.progressDialog.dismiss();
            BanActivity.this.showError("Error submitting ban");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onNavigationClicked(View view) {
        finish();
    }

    public static Intent createIntent(Context context, String str, String str2) {
        Intent intent = new Intent(context, (Class<?>) BanActivity.class);
        intent.putExtra("user", str);
        intent.putExtra("subreddit", str2);
        return intent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showError(String str) {
        this.noteInput.setError(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void submitBan() {
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.noteInput.getWindowToken(), 0);
        ProgressDialog progressDialogA = ProgressDialogHelper.create(getActivity());
        progressDialogA.setMessage("Submitting ban");
        progressDialogA.setCancelable(false);
        progressDialogA.show();
        OAuthRequestManager.send(getActivity(), new OAuthModBanRequest(getActivity(), this.user, this.subreddit, this.whyInput.getText().toString(), this.durationInput.getText().toString(), this.noteInput.getText().toString(), new BanSuccessListener(progressDialogA), new BanErrorListener(progressDialogA)));
    }

    @Override // com.resync.app.widgets.drag.DragActivity
    public int getDragLayoutRes() {
        return R.layout.activity_ban;
    }

    @Override // com.resync.app.ui.interfaces.SnackbarInterface
    public boolean isContentLoading() {
        return false;
    }

    @Override // com.resync.app.ui.activities.BaseMaterialDragActivity, com.resync.app.ui.activities.BaseMaterialActivity, com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.user = getIntent().getStringExtra("user");
        this.subreddit = getIntent().getStringExtra("subreddit");
        MaterialYouToolbar materialYouToolbar = (MaterialYouToolbar) findViewById(R.id.toolbar);
        this.toolbar = materialYouToolbar;
        materialYouToolbar.setTitle("Ban " + this.user);
        this.toolbar.inflateMenu(R.menu.ban);
        this.toolbar.setNavigationIcon(R.drawable.ic_close_activity_white_24dp);
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: m9.a
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BanActivity.this.onNavigationClicked(view);
            }
        });
        this.toolbar.setOnMenuItemClickListener(new MenuClickListener());
        this.durationInput = (EditText) findViewById(R.id.ban_duration_input);
        this.whyInput = (EditText) findViewById(R.id.ban_why_input);
        this.noteInput = (EditText) findViewById(R.id.ban_note_input);
    }
}
