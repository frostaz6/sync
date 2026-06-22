package com.resync.app.ui.activities;

import android.os.Bundle;
import android.view.View;
import com.resync.app.R;
import com.resync.app.ui.activities.TestActivity;
import com.resync.app.ui.fragments.TabbedCommentsFragment;
import java.util.ArrayList;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes.dex */
public class TestActivity extends BaseActivity {
    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void onTestButtonClicked(ArrayList arrayList, TabbedCommentsFragment tabbedCommentsFragment, View view) {
        tabbedCommentsFragment.addOrSelectTab(Post.createForUrl((String) arrayList.remove(0), "android", "https://www.reddit.com/r/Android/comments/tc4hpm/some_pixel_phones_lose_contactless_pay_after/"));
    }

    @Override // com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        final TabbedCommentsFragment tabbedCommentsFragment;
        super.onCreate(bundle);
        if (bundle == null) {
            tabbedCommentsFragment = new TabbedCommentsFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.tabbed_comments, tabbedCommentsFragment).commit();
        } else {
            tabbedCommentsFragment = (TabbedCommentsFragment) getSupportFragmentManager().findFragmentById(R.id.tabbed_comments);
        }
        final ArrayList arrayList = new ArrayList();
        arrayList.add("tceqsa");
        arrayList.add("tc26kv");
        arrayList.add("tceqsa");
        arrayList.add("tc2ny2");
        arrayList.add("tbt6h1");
        arrayList.add("tbnfjk");
        arrayList.add("tceqsa");
        arrayList.add("tc26kv");
        arrayList.add("tceqsa");
        arrayList.add("tc2ny2");
        arrayList.add("tbt6h1");
        arrayList.add("tbnfjk");
        findViewById(R.id.test_button).setOnClickListener(new View.OnClickListener() { // from class: m9.s
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TestActivity.onTestButtonClicked(arrayList, tabbedCommentsFragment, view);
            }
        });
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected void setupContentView() {
        setContentView(R.layout.activity_test);
    }
}
