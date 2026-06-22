package com.resync.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import butterknife.ButterKnife;
import com.resync.app.R;
import com.resync.app.ui.fragments.posts.BasePostsFragment;
import com.resync.app.ui.views.app_bars.PostsAppBarLayout;
import org.apache.commons.lang3.StringUtils;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.util.TestUtils;
import com.resync.app.util.links.LinkTypeHelper;
import com.resync.app.util.links.LinkHandler;

/* JADX INFO: loaded from: classes.dex */
public class SwipeActivity extends BaseMaterialActivity {

    protected String url;

    public static void start(Context context, String str) {
        String target;
        Intent intent = new Intent(context, (Class<?>) SwipeActivity.class);
        if (LinkTypeHelper.isMulti(str)) {
            target = "multi_" + com.resync.app.util.links.LinkHandler.getUserFromPath(str);
        } else {
            target = com.resync.app.util.links.LinkHandler.getSubreddit(str);
        }
        intent.putExtra("url", target);
        if (TestUtils.isRobolectric()) {
            intent.setFlags(268435456);
        }
        context.startActivity(intent);
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected int getAdMode() {
        return 2;
    }

    @Override // com.resync.app.ui.activities.BaseMaterialActivity, com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        String stringExtra = getIntent().getStringExtra("url");
        this.url = stringExtra;
        StringUtils.isEmpty(stringExtra);
        super.onCreate(bundle);
        ButterKnife.bind(this);
        if (bundle == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.content_wrapper, BasePostsFragment.newVerticalForUrl(this.url), "posts").commit();
        }
        ((PostsAppBarLayout) findViewById(R.id.appbar)).onUpdate(SizeHelper.dpToPx(4), 0);
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected void setupContentView() {
        setContentView(R.layout.activity_swipe);
    }
}
