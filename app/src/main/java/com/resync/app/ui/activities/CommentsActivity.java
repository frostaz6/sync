package com.resync.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import com.google.android.exoplayer2.C;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.ui.fragments.CommentsFragment;
import com.squareup.otto.Subscribe;
import com.resync.app.events.VolumeDownEvent;
import com.resync.app.events.VolumeUpEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.events.DragStartingEvent;
import com.resync.app.helpers.FragmentHelper;
import com.resync.app.util.Logger;
import com.resync.app.util.TestUtils;
import com.resync.app.util.TransparentUtils;
import com.resync.app.model.Post;

/* JADX INFO: loaded from: classes.dex */
public class CommentsActivity extends BaseMaterialDragActivity {
    public static Intent createContinueIntent(Context context, String str, String str2, String str3) {
        Intent intent = new Intent(context, (Class<?>) CommentsActivity.class);
        intent.putExtra("POST_ID", str);
        intent.putExtra("COMMENT_ID", str2);
        intent.putExtra("SUB", str3);
        intent.putExtra("continue_instance", true);
        return intent;
    }

    public static Intent createIntent(Context context, String str, String str2, String str3, boolean z) {
        Intent intent = new Intent(context, (Class<?>) CommentsActivity.class);
        intent.putExtra("POST_ID", str);
        intent.putExtra("COMMENT_ID", str2);
        intent.putExtra("SUB", str3);
        intent.putExtra("NP", z);
        Logger.verbose("WAS NP: " + z);
        return intent;
    }

    public static Intent createPostIntent(Context context, Post post) {
        Intent intent = new Intent(context, (Class<?>) CommentsActivity.class);
        intent.putExtra("Post", post);
        return intent;
    }

    public static Intent createBackgroundIntent(Context context, Post post) {
        Intent intent = new Intent(context, (Class<?>) CommentsActivity.class);
        intent.putExtra("Post", post);
        intent.setFlags(524288);
        intent.addFlags(C.BUFFER_FLAG_FIRST_SAMPLE);
        intent.putExtra("background_multi", true);
        return intent;
    }

    public CommentsFragment getCommentsFragment() {
        if (getSupportFragmentManager().findFragmentById(R.id.content_wrapper) instanceof CommentsFragment) {
            return (CommentsFragment) getSupportFragmentManager().findFragmentById(R.id.content_wrapper);
        }
        return null;
    }

    public void addCommentsFragment() {
        if (TestUtils.isRobolectric()) {
            return;
        }
        String str = SettingsSingleton.getSettings().commentSort;
        if (getIntent().hasExtra("Post")) {
            FragmentHelper.replaceFragment(this, CommentsFragment.newInstanceFromPost((Post) getIntent().getSerializableExtra("Post"), str, true), R.id.content_wrapper);
        } else if (getIntent().hasExtra("POST_ID")) {
            FragmentHelper.replaceFragment(this, CommentsFragment.newInstance(getIntent().getStringExtra("POST_ID"), getIntent().getStringExtra("COMMENT_ID"), getIntent().getStringExtra("SUB"), str, true, getIntent().getBooleanExtra("NP", false), getIntent().getBooleanExtra("continue_instance", false)), R.id.content_wrapper);
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.core.app.ComponentActivity, android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (SettingsSingleton.getSettings().volumeKeyNavigation) {
            if (keyCode == 25) {
                if (keyEvent.getAction() == 0) {
                    BusSingleton.getBus().post(new VolumeDownEvent(getCreatedTime()));
                }
                return true;
            }
            if (keyCode == 24) {
                if (keyEvent.getAction() == 0) {
                    BusSingleton.getBus().post(new VolumeUpEvent(getCreatedTime()));
                }
                return true;
            }
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    @Override // com.resync.app.widgets.drag.DragActivity
    public int getDragLayoutRes() {
        return R.layout.activity_base_material_drag;
    }

    @Override // com.resync.app.ui.interfaces.SnackbarInterface
    public boolean isContentLoading() {
        CommentsFragment commentsFragment = (CommentsFragment) findFragmentByType(CommentsFragment.class, R.id.content_wrapper);
        if (commentsFragment != null) {
            return commentsFragment.isLoadingViewActive();
        }
        return false;
    }

    @Override // com.resync.app.ui.activities.BaseMaterialDragActivity, com.resync.app.ui.activities.BaseMaterialActivity, com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null && getIntent().hasExtra("background_multi")) {
            moveTaskToBack(true);
        }
        if (bundle == null) {
            addCommentsFragment();
        }
    }

    @Subscribe
    public void startingDrag(DragStartingEvent dragStartingEvent) {
        TransparentUtils.convertToTranslucent(this);
    }
}
