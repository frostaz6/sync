package com.resync.app.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import com.resync.app.R;
import com.resync.app.singleton.SettingsSingleton;
import com.resync.app.events.TimeSelectedEvent;
import com.resync.app.singletons.BusSingleton;
import com.resync.app.helpers.SizeHelper;
import com.resync.app.thirdparty.timerangepicker.TimeRangePickerDialog;

/* JADX INFO: renamed from: r9.d */
/* JADX INFO: compiled from: CustomTimeRangePickerDialog.java */
/* JADX INFO: loaded from: classes.dex */
public class CustomTimeRangePickerDialog extends TimeRangePickerDialog {
    public static CustomTimeRangePickerDialog newInstance(TimeRangePickerDialog.OnTimeRangeSetListener listener, boolean z) {
        CustomTimeRangePickerDialog customTimeRangePickerDialog = new CustomTimeRangePickerDialog();
        customTimeRangePickerDialog.setListener(listener, z);
        return customTimeRangePickerDialog;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ((TimePicker) getView().findViewById(R.id.startTimePicker)).setCurrentHour(Integer.valueOf(SettingsSingleton.getSettings().autoNightStartHour));
        ((TimePicker) getView().findViewById(R.id.startTimePicker)).setCurrentMinute(Integer.valueOf(SettingsSingleton.getSettings().autoNightStartMin));
        ((TimePicker) getView().findViewById(R.id.endTimePicker)).setCurrentHour(Integer.valueOf(SettingsSingleton.getSettings().autoNightEndHour));
        ((TimePicker) getView().findViewById(R.id.endTimePicker)).setCurrentMinute(Integer.valueOf(SettingsSingleton.getSettings().autoNightEndMin));
        TimePicker timePicker = (TimePicker) getView().findViewById(R.id.startTimePicker);
        Boolean bool = Boolean.TRUE;
        timePicker.setIs24HourView(bool);
        ((TimePicker) getView().findViewById(R.id.endTimePicker)).setIs24HourView(bool);
        ((Button) view.findViewById(R.id.bSetTimeRange)).setPadding((int) SizeHelper.dpToPxFloat(20), (int) SizeHelper.dpToPxFloat(20), (int) SizeHelper.dpToPxFloat(20), (int) SizeHelper.dpToPxFloat(20));
    }

    @Override // com.resync.app.thirdparty.timerangepicker.TimeRangePickerDialog, android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.bSetTimeRange) {
            dismiss();
            BusSingleton.getBus().post(new TimeSelectedEvent(((TimePicker) getView().findViewById(R.id.startTimePicker)).getCurrentHour().intValue(), ((TimePicker) getView().findViewById(R.id.startTimePicker)).getCurrentMinute().intValue(), ((TimePicker) getView().findViewById(R.id.endTimePicker)).getCurrentHour().intValue(), ((TimePicker) getView().findViewById(R.id.endTimePicker)).getCurrentMinute().intValue()));
        }
    }
}
