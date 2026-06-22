package com.resync.app.thirdparty.timerangepicker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TimePicker;

/* compiled from: TimeRangePickerDialog.java (third-party start/end time-range picker dialog) */
public class TimeRangePickerDialog extends androidx.fragment.app.DialogFragment implements View.OnClickListener {
    TabHost tabHost;
    Button setButton;
    TimePicker startTimePicker;
    TimePicker endTimePicker;
    OnTimeRangeSetListener listener;
    boolean useDarkTheme;

    public interface OnTimeRangeSetListener {
        void onTimeRangeSet(int startHour, int startMinute, int endHour, int endMinute);
    }

    public void setListener(OnTimeRangeSetListener listener, boolean useDarkTheme) {
        this.listener = listener;
        this.useDarkTheme = useDarkTheme;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(Layouts.TIME_RANGE_PICKER_DIALOG, viewGroup, false);
        getDialog().getWindow().requestFeature(1);
        int tabHostId = com.resync.app.thirdparty.timerangepicker.Ids.TAB_HOST;
        this.tabHost = (TabHost) viewInflate.findViewById(tabHostId);
        this.setButton = (Button) viewInflate.findViewById(com.resync.app.thirdparty.timerangepicker.Ids.SET_TIME_RANGE_BUTTON);
        this.startTimePicker = (TimePicker) viewInflate.findViewById(com.resync.app.thirdparty.timerangepicker.Ids.START_TIME_PICKER);
        this.endTimePicker = (TimePicker) viewInflate.findViewById(com.resync.app.thirdparty.timerangepicker.Ids.END_TIME_PICKER);
        this.setButton.setOnClickListener(this);
        this.tabHost.findViewById(tabHostId);
        this.tabHost.setup();
        TabHost.TabSpec tabSpecNewTabSpec = this.tabHost.newTabSpec("one");
        tabSpecNewTabSpec.setContent(com.resync.app.thirdparty.timerangepicker.Ids.START_TIME_GROUP);
        tabSpecNewTabSpec.setIndicator("Start Time");
        TabHost.TabSpec tabSpecNewTabSpec2 = this.tabHost.newTabSpec("two");
        tabSpecNewTabSpec2.setContent(com.resync.app.thirdparty.timerangepicker.Ids.END_TIME_GROUP);
        tabSpecNewTabSpec2.setIndicator("End Time");
        this.tabHost.addTab(tabSpecNewTabSpec);
        this.tabHost.addTab(tabSpecNewTabSpec2);
        return viewInflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (getDialog() == null) {
            return;
        }
        getDialog().getWindow().setLayout(-1, -2);
    }

    public void onClick(View view) {
        if (view.getId() == com.resync.app.thirdparty.timerangepicker.Ids.SET_TIME_RANGE_BUTTON) {
            dismiss();
            this.listener.onTimeRangeSet(this.startTimePicker.getCurrentHour().intValue(), this.startTimePicker.getCurrentMinute().intValue(), this.endTimePicker.getCurrentHour().intValue(), this.endTimePicker.getCurrentMinute().intValue());
        }
    }
}
