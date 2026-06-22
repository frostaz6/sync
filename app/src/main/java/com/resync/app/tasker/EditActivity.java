package com.resync.app.tasker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.ui.activities.BaseActivity;
import com.resync.app.helpers.ui.StaticToast;
import com.resync.app.helpers.ui.SubredditArrayList;
import java.util.Collections;
import java.util.Locale;
import com.resync.app.managers.SubredditsManager;
import com.resync.app.model.Subreddits;
import com.resync.app.util.TextUtils;

/* JADX INFO: loaded from: classes.dex */
public class EditActivity extends BaseActivity {

    @BindView(R.id.tasker_sync_comments)
    CheckBox mCommentsCheckbox;

    @BindView(R.id.tasker_sync_pages)
    TextView mPagesInput;

    @BindView(R.id.tasker_sync_subreddit)
    AutoCompleteTextView mSubredditInput;

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        StaticToast.showWithContext(getActivity(), "Setup cancelled");
        super.onBackPressed();
    }

    @Override // com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tasker, menu);
        return true;
    }

    @Override // com.resync.app.ui.activities.BaseActivity, androidx.activity.ComponentActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            StaticToast.showWithContext(getActivity(), "Setup cancelled");
            finish();
            return true;
        }
        if (menuItem.getItemId() != R.id.tasker_done) {
            return super.onOptionsItemSelected(menuItem);
        }
        submit();
        return true;
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected void setupContentView() {
        setContentView(R.layout.tasker);
        ButterKnife.bind(this);
        SubredditArrayList subredditArrayList = new SubredditArrayList();
        subredditArrayList.addAll(SubredditsManager.getInstance().getSubreddits());
        int i = 0;
        while (true) {
            String[] strArr = Subreddits.DEFAULTS;
            if (i >= strArr.length) {
                Collections.sort(subredditArrayList);
                String[] strArr2 = (String[]) subredditArrayList.toArray(new String[0]);
                this.mSubredditInput.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, strArr2));
                return;
            }
            if (!subredditArrayList.contains(strArr[i])) {
                subredditArrayList.add(strArr[i]);
            }
            i++;
        }
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected void setupToolbar() {
        super.setupToolbar();
        this.toolbar.setTitle("Automatic sync setup");
    }

    void submit() {
        String strTrim = this.mSubredditInput.getText().toString().toLowerCase(Locale.ENGLISH).trim();
        if (TextUtils.isEmpty(strTrim)) {
            this.mSubredditInput.setError("No subreddit entered");
            return;
        }
        ((InputMethodManager) getActivity().getSystemService("input_method")).hideSoftInputFromWindow(this.mSubredditInput.getWindowToken(), 0);
        String string = this.mPagesInput.getText().toString();
        if (TextUtils.isEmpty(string)) {
            string = "5";
        }
        int iMin = Math.min(20, Integer.parseInt(string));
        Intent intent = new Intent();
        intent.putExtra("com.resync.app.subreddit", strTrim);
        intent.putExtra("com.resync.app.comments", this.mCommentsCheckbox.isChecked());
        intent.putExtra("com.resync.app.pages", iMin);
        setResult(-1, intent);
        finish();
    }
}
