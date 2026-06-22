package com.resync.app.ui.activities;

import android.graphics.Outline;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.resync.app.R;
import com.resync.app.helpers.ui.MonetUtil;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.dialogs.ExampleAlertDialogBottomSheet;
import com.resync.app.dialogs.ExampleAsyncAlertDialogBottomSheet;
import com.resync.app.dialogs.ExampleAsyncRadioSelectionDialogBottomSheet;
import com.resync.app.dialogs.ExampleAsyncSelectionDialogBottomSheet;
import com.resync.app.dialogs.ExampleInputDialogBottomSheet;
import com.resync.app.dialogs.ExampleProgressDialogBottomSheet;
import com.resync.app.dialogs.ExampleRadioSelectionDialogBottomSheet;
import com.resync.app.dialogs.ExampleSelectionDialogBottomSheet;
import com.resync.app.dialogs.ExampleThreeButtonedAlertDialogBottomSheet;

/* JADX INFO: loaded from: classes.dex */
public class TestDialogActivity extends BaseMaterialActivity {

    class RoundedOutlineProvider extends ViewOutlineProvider {
        RoundedOutlineProvider() {
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), SizeHelper.dpToPxFloat(12));
        }
    }

    @OnClick
    public void onAlertClicked() {
        ExampleAlertDialogBottomSheet.show(getSupportFragmentManager());
    }

    @OnClick
    public void onAsyncAlertClicked() {
        ExampleAsyncAlertDialogBottomSheet.show(getSupportFragmentManager());
    }

    @OnClick
    public void onAsyncRadioSelectionClicked() {
        ExampleAsyncRadioSelectionDialogBottomSheet.show(getSupportFragmentManager());
    }

    @OnClick
    public void onAsyncSelectionClicked() {
        ExampleAsyncSelectionDialogBottomSheet.show(getSupportFragmentManager());
    }

    @Override // com.resync.app.ui.activities.BaseMaterialActivity, com.resync.app.ui.activities.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_test_dialog);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(MonetUtil.getWindowBackgroundColorAuto()));
        findViewById(R.id.buttons_wrapper).setBackgroundColor(MonetUtil.getBackgroundColorAuto());
        findViewById(R.id.buttons_wrapper).setOutlineProvider(new RoundedOutlineProvider());
        findViewById(R.id.buttons_wrapper).setClipToOutline(true);
    }

    @OnClick
    public void onInputClicked() {
        ExampleInputDialogBottomSheet.show(getSupportFragmentManager());
    }

    @OnClick
    public void onProgressClicked() {
        ExampleProgressDialogBottomSheet.showDialog(getSupportFragmentManager());
    }

    @OnClick
    public void onRadioSelectionClicked() {
        ExampleRadioSelectionDialogBottomSheet.show(getSupportFragmentManager());
    }

    @OnClick
    public void onSecondAlertClicked() {
        ExampleThreeButtonedAlertDialogBottomSheet.show(getSupportFragmentManager());
    }

    @OnClick
    public void onSelectionClicked() {
        ExampleSelectionDialogBottomSheet.show(getSupportFragmentManager());
    }
}
