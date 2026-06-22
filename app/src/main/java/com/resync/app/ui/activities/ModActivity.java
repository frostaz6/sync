package com.resync.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.bumptech.glide.MaterialPostsHelper;
import com.resync.app.R;
import com.resync.app.ui.fragments.posts.BasePostsFragment;
import com.resync.app.ui.fragments.posts.VerticalPostsFragment;
import com.resync.app.helpers.FragmentHelper;

/* JADX INFO: loaded from: classes.dex */
public class ModActivity extends BaseMaterialDragActivity {
    public static void start(Context context) {
        Intent intent = new Intent(context, (Class<?>) ModActivity.class);
        intent.putExtra("section", "Modqueue");
        context.startActivity(intent);
    }

    public static void startSection(Context context, String str) {
        Intent intent = new Intent(context, (Class<?>) ModActivity.class);
        intent.putExtra("section", str);
        context.startActivity(intent);
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected int getAdMode() {
        return 0;
    }

    @Override // com.resync.app.widgets.drag.DragActivity
    public int getDragLayoutRes() {
        return R.layout.activity_mod;
    }

    @Override // com.resync.app.ui.interfaces.SnackbarInterface
    public boolean isContentLoading() {
        VerticalPostsFragment verticalPostsFragment = (VerticalPostsFragment) findFragmentByType(VerticalPostsFragment.class, R.id.content_wrapper);
        if (verticalPostsFragment != null) {
            return verticalPostsFragment.isLoadingViewActive();
        }
        return false;
    }

    @Override // com.resync.app.ui.activities.BaseMaterialDragActivity, com.resync.app.ui.activities.BaseMaterialActivity, com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            FragmentHelper.replaceFragment(this, BasePostsFragment.newVertical(MaterialPostsHelper.buildModAboutAddress(getIntent().getStringExtra("section")), true), R.id.content_wrapper);
        }
    }
}
