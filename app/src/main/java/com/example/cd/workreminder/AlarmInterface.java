package com.example.cd.workreminder;

import android.content.Context;

public interface AlarmInterface {
    public void setAlarmTime(Context context, int startMilitaryHour, int startMilitaryMinute, int timeBeforeShift);
}
