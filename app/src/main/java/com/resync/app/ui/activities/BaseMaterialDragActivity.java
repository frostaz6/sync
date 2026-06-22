package com.resync.app.ui.activities;

import android.os.Bundle;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.widgets.drag.DragManager;
import com.resync.app.widgets.drag.DragManagerImpl;
import com.resync.app.widgets.drag.DragActivity;
import com.resync.app.ui.interfaces.SnackbarInterface;
import com.resync.app.util.Logger;
import com.resync.app.util.TransparentUtils;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseMaterialDragActivity extends BaseMaterialActivity implements DragActivity, SnackbarInterface {

    protected DragManager dragManager;

    class PostResumeRunnable implements Runnable {
        PostResumeRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (BaseMaterialDragActivity.this.isFinishing()) {
                return;
            }
            BaseMaterialDragActivity baseMaterialDragActivity = BaseMaterialDragActivity.this;
            if (baseMaterialDragActivity.closing) {
                return;
            }
            TransparentUtils.convertToTranslucent(baseMaterialDragActivity);
            BaseMaterialDragActivity.this.dragManager.setDragEnabled(false);
            Logger.verbose("BaseActivity", "Calling onPostResume: " + BaseMaterialDragActivity.this.getClass().getSimpleName());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void finishWithAnimation() {
        super.finish();
        if (SettingsSingleton.getSettings().animateTransitionComments && (this instanceof CommentsActivity)) {
            overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit);
        }
    }

    public void restoreFromTranslucent() {
        if (!SettingsSingleton.getSettings().commentsSwipe || this.closing || isFinishing()) {
            return;
        }
        TransparentUtils.convertFromTranslucent(this);
        this.dragManager.setDragEnabled(true);
    }

    @Override // com.resync.app.ui.activities.BaseActivity, android.app.Activity
    public void finish() {
        DragManager dragManager = this.dragManager;
        if (dragManager != null && dragManager.getDragLayout() != null && !this.dragManager.getDragLayout().isClosed()) {
            TransparentUtils.convertFromTranslucent(this);
            getWindow().getDecorView().post(new Runnable() { // from class: m9.b
                @Override // java.lang.Runnable
                public final void run() {
                    BaseMaterialDragActivity.this.finishWithAnimation();
                }
            });
            return;
        }
        super.finish();
        if (SettingsSingleton.getSettings().animateTransitionComments && (this instanceof CommentsActivity)) {
            overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit);
        }
    }

    @Override // com.resync.app.ui.activities.BaseMaterialActivity, com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // com.resync.app.ui.activities.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.dragManager.onDestroy();
    }

    @Override // com.resync.app.ui.activities.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPostResume() {
        super.onPostResume();
        if (isFinishing() || this.closing || !SettingsSingleton.getSettings().commentsSwipe) {
            return;
        }
        try {
            getWindow().getDecorView().postDelayed(new PostResumeRunnable(), 280L);
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    @Override // com.resync.app.ui.activities.BaseMaterialActivity, com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.dragManager.refresh(this);
    }

    @Override // com.resync.app.ui.activities.BaseMaterialActivity, com.resync.app.ui.activities.BaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        restoreFromTranslucent();
    }

    @Override // com.resync.app.ui.activities.BaseActivity
    protected final void setupContentView() {
        DragManagerImpl dragManagerImpl = new DragManagerImpl();
        this.dragManager = dragManagerImpl;
        dragManagerImpl.attach(this, getDragLayoutRes());
    }
}
