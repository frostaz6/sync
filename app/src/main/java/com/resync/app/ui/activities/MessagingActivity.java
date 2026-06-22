package com.resync.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.resync.app.R;
import com.resync.app.ui.fragments.MessagingFragment;
import com.resync.app.ui.fragments.posts.VerticalPostsFragment;

/* JADX INFO: loaded from: classes.dex */
public class MessagingActivity extends BaseMaterialDragActivity {
    public static void start(Context context) {
        Intent intent = new Intent(context, (Class<?>) MessagingActivity.class);
        intent.putExtra("section", 0);
        context.startActivity(intent);
    }

    public static void startSection(Context context, int section) {
        Intent intent = new Intent(context, (Class<?>) MessagingActivity.class);
        intent.putExtra("section", section);
        context.startActivity(intent);
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected int getAdMode() {
        return 0;
    }

    @Override // com.resync.app.widgets.drag.DragActivity
    public int getDragLayoutRes() {
        return R.layout.activity_messaging;
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
            getSupportFragmentManager().beginTransaction().add(R.id.content_wrapper, MessagingFragment.newInstance(getIntent().getIntExtra("section", 0))).commit();
        }
    }
}
