package com.resync.app.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import com.resync.app.R;
import com.resync.app.ui.fragments.WebViewFragment;
import com.resync.app.helpers.ui.AlertDialogHelper;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.BypassHelper;
import com.resync.app.helpers.FragmentHelper;
import com.resync.app.ui.interfaces.ToolbarInterface;
import com.resync.app.util.links.LinkTypeHelper;

/* JADX INFO: loaded from: classes.dex */
public class WebViewActivity extends BaseMaterialDragActivity implements ToolbarInterface {

    class FixClickListener implements DialogInterface.OnClickListener {

        final /* synthetic */ String url;

        FixClickListener(String str) {
            this.url = str;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
            StaticToast.showWithContext(WebViewActivity.this.getActivity(), "All fixed!");
            BypassHelper.getInstance().remove(LinkTypeHelper.getHost(this.url));
        }
    }

    class IgnoreClickListener implements DialogInterface.OnClickListener {
        IgnoreClickListener() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int which) {
        }
    }

    public static Intent createIntent(Context context, String str) {
        Intent intent = new Intent(context, (Class<?>) WebViewActivity.class);
        intent.putExtra("url", str);
        return intent;
    }

    @Override // com.resync.app.widgets.drag.DragActivity
    public int getDragLayoutRes() {
        return R.layout.activity_base_no_toolbar;
    }

    @Override // com.resync.app.ui.interfaces.SnackbarInterface
    public boolean isContentLoading() {
        return false;
    }

    @Override // com.resync.app.ui.activities.BaseMaterialDragActivity, com.resync.app.ui.activities.BaseMaterialActivity, com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            String stringExtra = getIntent().getStringExtra("url");
            FragmentHelper.replaceFragment(this, WebViewFragment.newInstance(stringExtra), R.id.content_wrapper);
            if (getIntent().getBooleanExtra("linker_error", false)) {
                AlertDialogHelper.newBuilder(getContext()).setTitle("We seem to have a slight problem here...").setMessage("Sync is set to open this type of link on a system level but you've also set Sync to open these links externally in-app.\n\nYou can either ignore this error message or let Sync fix this for you.").setPositiveButton("Fix", new FixClickListener(stringExtra)).setNegativeButton("Ignore", null).create().show();
            }
            if (getIntent().getBooleanExtra("external_error", false)) {
                AlertDialogHelper.newBuilder(getContext()).setTitle("We seem to have a slight problem here...").setMessage("Sync is set to open this type of link on a system level but you've also set Sync to open all links externally...\n\nTo stop an infinite loop Sync will handle this link.").setPositiveButton("OK", new IgnoreClickListener()).create().show();
            }
        }
    }

    @Override // com.resync.app.ui.activities.BaseActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() != 4) {
            return super.onKeyUp(keyCode, keyEvent);
        }
        WebViewFragment webViewFragment = (WebViewFragment) findFragmentByType(WebViewFragment.class, R.id.content_wrapper);
        if (webViewFragment != null && webViewFragment.goBack()) {
            return true;
        }
        this.closing = true;
        finish();
        return true;
    }
}
