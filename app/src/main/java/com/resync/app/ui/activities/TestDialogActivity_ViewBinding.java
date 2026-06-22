package com.resync.app.ui.activities;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.resync.app.R;

/* JADX INFO: loaded from: classes.dex */
public class TestDialogActivity_ViewBinding implements Unbinder {
    private TestDialogActivity target;
    private View viewAlert;
    private View viewAlertSecond;
    private View viewAlertAsync;
    private View viewInput;
    private View viewSelection;
    private View viewAsyncSelection;
    private View viewRadioSelection;
    private View viewAsyncRadioSelection;
    private View viewProgress;

    class AlertClickListener extends com.resync.app.util.DebouncingOnClickListener {
        final /* synthetic */ TestDialogActivity activity;

        AlertClickListener(TestDialogActivity testDialogActivity) {
            this.activity = testDialogActivity;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.activity.onAlertClicked();
        }
    }

    class SecondAlertClickListener extends com.resync.app.util.DebouncingOnClickListener {
        final /* synthetic */ TestDialogActivity activity;

        SecondAlertClickListener(TestDialogActivity testDialogActivity) {
            this.activity = testDialogActivity;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.activity.onSecondAlertClicked();
        }
    }

    class AsyncAlertClickListener extends com.resync.app.util.DebouncingOnClickListener {
        final /* synthetic */ TestDialogActivity activity;

        AsyncAlertClickListener(TestDialogActivity testDialogActivity) {
            this.activity = testDialogActivity;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.activity.onAsyncAlertClicked();
        }
    }

    class InputClickListener extends com.resync.app.util.DebouncingOnClickListener {
        final /* synthetic */ TestDialogActivity activity;

        InputClickListener(TestDialogActivity testDialogActivity) {
            this.activity = testDialogActivity;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.activity.onInputClicked();
        }
    }

    class SelectionClickListener extends com.resync.app.util.DebouncingOnClickListener {
        final /* synthetic */ TestDialogActivity activity;

        SelectionClickListener(TestDialogActivity testDialogActivity) {
            this.activity = testDialogActivity;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.activity.onSelectionClicked();
        }
    }

    class AsyncSelectionClickListener extends com.resync.app.util.DebouncingOnClickListener {
        final /* synthetic */ TestDialogActivity activity;

        AsyncSelectionClickListener(TestDialogActivity testDialogActivity) {
            this.activity = testDialogActivity;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.activity.onAsyncSelectionClicked();
        }
    }

    class RadioSelectionClickListener extends com.resync.app.util.DebouncingOnClickListener {
        final /* synthetic */ TestDialogActivity activity;

        RadioSelectionClickListener(TestDialogActivity testDialogActivity) {
            this.activity = testDialogActivity;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.activity.onRadioSelectionClicked();
        }
    }

    class AsyncRadioSelectionClickListener extends com.resync.app.util.DebouncingOnClickListener {
        final /* synthetic */ TestDialogActivity activity;

        AsyncRadioSelectionClickListener(TestDialogActivity testDialogActivity) {
            this.activity = testDialogActivity;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.activity.onAsyncRadioSelectionClicked();
        }
    }

    class ProgressClickListener extends com.resync.app.util.DebouncingOnClickListener {
        final /* synthetic */ TestDialogActivity activity;

        ProgressClickListener(TestDialogActivity testDialogActivity) {
            this.activity = testDialogActivity;
        }

        @Override // com.resync.app.util.DebouncingOnClickListener
        public void doClick(View view) {
            this.activity.onProgressClicked();
        }
    }

    public TestDialogActivity_ViewBinding(TestDialogActivity testDialogActivity, View view) {
        this.target = testDialogActivity;
        View viewC = Utils.findRequiredView(view, R.id.button_alert, "method 'onAlertClicked'");
        this.viewAlert = viewC;
        viewC.setOnClickListener(new AlertClickListener(testDialogActivity));
        View viewC2 = Utils.findRequiredView(view, R.id.button_alert_second, "method 'onSecondAlertClicked'");
        this.viewAlertSecond = viewC2;
        viewC2.setOnClickListener(new SecondAlertClickListener(testDialogActivity));
        View viewC3 = Utils.findRequiredView(view, R.id.button_alert_async, "method 'onAsyncAlertClicked'");
        this.viewAlertAsync = viewC3;
        viewC3.setOnClickListener(new AsyncAlertClickListener(testDialogActivity));
        View viewC4 = Utils.findRequiredView(view, R.id.button_input, "method 'onInputClicked'");
        this.viewInput = viewC4;
        viewC4.setOnClickListener(new InputClickListener(testDialogActivity));
        View viewC5 = Utils.findRequiredView(view, R.id.button_selection, "method 'onSelectionClicked'");
        this.viewSelection = viewC5;
        viewC5.setOnClickListener(new SelectionClickListener(testDialogActivity));
        View viewC6 = Utils.findRequiredView(view, R.id.button_async_selection, "method 'onAsyncSelectionClicked'");
        this.viewAsyncSelection = viewC6;
        viewC6.setOnClickListener(new AsyncSelectionClickListener(testDialogActivity));
        View viewC7 = Utils.findRequiredView(view, R.id.button_radio_selection, "method 'onRadioSelectionClicked'");
        this.viewRadioSelection = viewC7;
        viewC7.setOnClickListener(new RadioSelectionClickListener(testDialogActivity));
        View viewC8 = Utils.findRequiredView(view, R.id.button_async_radio_selection, "method 'onAsyncRadioSelectionClicked'");
        this.viewAsyncRadioSelection = viewC8;
        viewC8.setOnClickListener(new AsyncRadioSelectionClickListener(testDialogActivity));
        View viewC9 = Utils.findRequiredView(view, R.id.button_progress, "method 'onProgressClicked'");
        this.viewProgress = viewC9;
        viewC9.setOnClickListener(new ProgressClickListener(testDialogActivity));
    }
    @Override // butterknife.Unbinder
    public void unbind() {
        TestDialogActivity testDialogActivity = this.target;
        if (testDialogActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        this.viewAlert = null;
        this.viewAlertSecond = null;
        this.viewAlertAsync = null;
        this.viewInput = null;
        this.viewSelection = null;
        this.viewAsyncSelection = null;
        this.viewRadioSelection = null;
        this.viewAsyncRadioSelection = null;
        this.viewProgress = null;
    }
}
