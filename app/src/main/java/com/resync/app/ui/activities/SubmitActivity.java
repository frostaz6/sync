package com.resync.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.resync.app.R;
import com.resync.app.ui.views.core.MaterialYouToolbar;
import com.squareup.otto.Subscribe;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.fragments.SubmitFragment;
import com.resync.app.events.DraftCheckerDismissed;
import com.resync.app.singletons.BusSingleton;

/* JADX INFO: loaded from: classes.dex */
public class SubmitActivity extends BaseMaterialActivity {

    private int submitType;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void onNavigationClicked(View view) {
        Fragment fragmentFindFragmentById = getSupportFragmentManager().findFragmentById(R.id.content_wrapper);
        if (!(fragmentFindFragmentById instanceof SubmitFragment) || ((SubmitFragment) fragmentFindFragmentById).canExit()) {
            finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean onMenuItemClicked(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.menu_submit) {
            return true;
        }
        submit();
        return true;
    }

    public static void start(Context context, int submitType, String str) {
        Intent intent = new Intent(context, (Class<?>) SubmitActivity.class);
        intent.putExtra("submit_type", submitType);
        intent.putExtra("subreddit", str);
        context.startActivity(intent);
    }

    private void submit() {
        Fragment fragmentFindFragmentById = getSupportFragmentManager().findFragmentById(R.id.content_wrapper);
        if (fragmentFindFragmentById == null || !(fragmentFindFragmentById instanceof SubmitFragment)) {
            return;
        }
        ((SubmitFragment) fragmentFindFragmentById).onSubmitClicked();
    }

    @Override // com.resync.app.ui.activities.BaseMaterialActivity, com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        String str;
        String string = null;
        super.onCreate(bundle);
        MaterialYouToolbar materialYouToolbar = (MaterialYouToolbar) findViewById(R.id.toolbar);
        materialYouToolbar.setTitle("Submit");
        materialYouToolbar.inflateMenu(R.menu.editor);
        materialYouToolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: m9.q
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SubmitActivity.this.onNavigationClicked(view);
            }
        });
        materialYouToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() { // from class: m9.r
            @Override // androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
            public final boolean onMenuItemClick(MenuItem menuItem) {
                return SubmitActivity.this.onMenuItemClicked(menuItem);
            }
        });
        getWindow().getDecorView().setBackgroundColor(MonetUtil.getBackgroundColorAuto());
        String stringExtra = getIntent().getStringExtra("title");
        String stringExtra2 = getIntent().getStringExtra("default_text");
        this.submitType = getIntent().getIntExtra("submit_type", 0);
        String stringExtra3 = getIntent().getStringExtra("subreddit");
        int currentSubmitType = this.submitType;
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (!"android.intent.action.SEND".equals(action) || type == null) {
            str = null;
        } else {
            if ("text/plain".equals(type)) {
                this.submitType = 1;
                string = intent.getStringExtra("android.intent.extra.TEXT");
            } else {
                if (type.startsWith("image/")) {
                    this.submitType = 2;
                    string = intent.getParcelableExtra("android.intent.extra.STREAM").toString();
                }
                str = null;
            }
            str = string;
        }
        if (bundle == null) {
            FragmentTransaction fragmentTransactionM = getSupportFragmentManager().beginTransaction();
            int submitType = this.submitType;
            fragmentTransactionM.add(R.id.content_wrapper, SubmitFragment.newInstance(submitType, stringExtra3, str, str != null && submitType == 2, stringExtra, stringExtra2));
            fragmentTransactionM.commit();
        }
    }

    @Subscribe
    public void onDraftCheckedDismissed(DraftCheckerDismissed draftCheckerDismissed) {
        finish();
    }

    @Override // com.resync.app.ui.activities.BaseActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
        Fragment fragmentFindFragmentById = getSupportFragmentManager().findFragmentById(R.id.content_wrapper);
        if (keyCode != 4 || !(fragmentFindFragmentById instanceof SubmitFragment)) {
            return super.onKeyDown(keyCode, keyEvent);
        }
        if (((SubmitFragment) fragmentFindFragmentById).canExit()) {
            finish();
        }
        return true;
    }

    @Override // com.resync.app.ui.activities.BaseMaterialActivity, com.resync.app.ui.activities.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        BusSingleton.getBus().register(this);
    }

    @Override // com.resync.app.ui.activities.BaseMaterialActivity, com.resync.app.ui.activities.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        BusSingleton.getBus().unregister(this);
        super.onStop();
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected void setupContentView() {
        setContentView(R.layout.activity_submit);
    }
}
