package com.example.cd.workreminder;

import android.content.Context;
import android.util.Log;

public class AlarmTimeFormatDisplay {
    private String dayOfWeek;
    private int hour;
    private int minute;
    private String amOrPm;
    private AlarmTimer alarmTimer;
    private Context context;
    private static int snoozeTime = 0;
    private boolean amSnoozed = false;

    public AlarmTimeFormatDisplay() {

    }

    public AlarmTimeFormatDisplay(Context context, AlarmTimer alarmTimer, boolean amSnoozed) {
        this.amSnoozed = amSnoozed;
        this.context = context;
        this.alarmTimer = alarmTimer;
    }


    public void setSnoozeTime(int snoozeTime) {
        this.snoozeTime = snoozeTime;
    }
    //Added on 1 - 16 - 2020
    //I use dynamic binding because the Android notification API doesn't support callbacks
    @Override
    public String toString() {
        String timeFormat = "";

        /*
          When the Alarm is set to something like 12:15 PM, and the alarm is set 15 minutes before,
          we want it to show 12:00 PM
         */
        if ((alarmTimer.getUpdatedHour(context) == 0) && alarmTimer.getUpdatedStartAmOrPm(context).equals("AM") ) {
            hour = 12;
            amOrPm = "PM";
        } else  if ((alarmTimer.getUpdatedHour(context) == 12) && alarmTimer.getUpdatedStartAmOrPm(context).equals("PM") ) {
            hour = 12;
            amOrPm = "AM";
        } else if ((alarmTimer.getUpdatedHour(context) == 0) &&  alarmTimer.getUpdatedStartAmOrPm(context).equals("PM") ){
            hour = 12;
        }

        if (amSnoozed == false) {
            //Something like 1:5 PM becomes while 1:05 PM while something like 1:10 PM stays 1:10 PM
            if (alarmTimer.getUpdatedMinute(context) < 10) {
                timeFormat = alarmTimer.getDayOfWeek(context) + " "
                        + alarmTimer.getUpdatedHour(context) + ":0"
                        + alarmTimer.getUpdatedMinute(context) + " "
                        + alarmTimer.getUpdatedStartAmOrPm(context);
            } else {
                timeFormat = alarmTimer.getDayOfWeek(context) + " "
                        + alarmTimer.getUpdatedHour(context) + ":"
                        + alarmTimer.getUpdatedMinute(context) + " "
                        + alarmTimer.getUpdatedStartAmOrPm(context);
            }
        } else {
            snoozeTime = alarmTimer.getUpdatedMinute(context) + alarmTimer.getAlarmSnooze();
            alarmTimer.setUpdatedMinute(snoozeTime);
            Log.e("LG_WORK_PHONE", "THE SNOOZE TIME IS: " + snoozeTime);
            if (snoozeTime < 10) {
                timeFormat = alarmTimer.getDayOfWeek(context) + " "
                        + alarmTimer.getUpdatedHour(context) + ":0"
                        + snoozeTime + " "
                        + alarmTimer.getUpdatedStartAmOrPm(context);
            } else {
                timeFormat = alarmTimer.getDayOfWeek(context) + " "
                        + alarmTimer.getUpdatedHour(context) + ":"
                        + snoozeTime + " "
                        + alarmTimer.getUpdatedStartAmOrPm(context);
            }
        }

        return timeFormat;
    }
}//end class
