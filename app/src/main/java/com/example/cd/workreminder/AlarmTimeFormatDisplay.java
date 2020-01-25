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
    private static int offset = 0;
    private boolean amSnoozed = false;

    public AlarmTimeFormatDisplay() {

    }

    public AlarmTimeFormatDisplay(Context context, AlarmTimer alarmTimer, boolean amSnoozed) {
        this.amSnoozed = amSnoozed;
        this.context = context;
        this.alarmTimer = alarmTimer;
    }


    //Added on 10 - 11 - 2019
    public String buildAlarmTimeFormatDisplay(String dayOfWeek,
                                              int hour,
                                              int minute,
                                              String amOrPm) {
        String timeFormat = "";

        /*
          When the Alarm is set to something like 12:15 PM, and the alarm is set 15 minutes before,
          we want it to show 12:00 PM
         */
        if ((hour == 0) && amOrPm.equals("AM") ) {
            hour = 12;
            amOrPm = "PM";
        } else  if ((hour == 0) && amOrPm.equals("PM") ) {
            hour = 12;
            amOrPm = "AM";
        }

        //Something like 1:5 PM becomes while 1:05 PM while something like 1:10 PM stays 1:10 PM
        if (minute < 10) {
            timeFormat = dayOfWeek + " " + hour + ":0" + minute + " " + amOrPm;
        } else {
            timeFormat = dayOfWeek + " " + hour + ":" + minute + " " + amOrPm;
        }

        return timeFormat;
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


        if (amSnoozed == false) {
            //Something like 1:5 PM becomes while 1:05 PM while something like 1:10 PM stays 1:10 PM
            timeFormat = buildAlarmTimeFormatDisplay(
                    alarmTimer.getDayOfWeek(context.getApplicationContext()),
                    alarmTimer.getNewAlarmCivilianHour(context.getApplicationContext()),
                    alarmTimer.getNewAlarmCivilianMinutes(context.getApplicationContext()),
                    alarmTimer.getUpdatedStartAmOrPm(context.getApplicationContext()));
        } else {

            snoozeTime = alarmTimer.getNewAlarmMilitaryMinute(context) + alarmTimer.getAlarmSnooze() + offset;
            offset = offset + 1;
            Log.e("LG_WORK_PHONE", "THE SNOOZE TIME IS: " + snoozeTime);
            if (snoozeTime < 10) {
                /*timeFormat = alarmTimer.getDayOfWeek(context) + " "
                        + alarmTimer.getUpdatedHour(context) + ":0"
                        + snoozeTime + " "
                        + alarmTimer.getUpdatedStartAmOrPm(context);
                        */

                timeFormat = buildAlarmTimeFormatDisplay(
                        alarmTimer.getDayOfWeek(context.getApplicationContext()),
                        alarmTimer.getNewAlarmMilitaryHour(context.getApplicationContext()),
                        snoozeTime,
                        alarmTimer.getUpdatedStartAmOrPm(context.getApplicationContext()) );
            } else {
                /*timeFormat = alarmTimer.getDayOfWeek(context) + " "
                        + alarmTimer.getUpdatedHour(context) + ":"
                        + snoozeTime + " "
                        + alarmTimer.getUpdatedStartAmOrPm(context);
                        */
                timeFormat = buildAlarmTimeFormatDisplay(
                        alarmTimer.getDayOfWeek(context.getApplicationContext()),
                        alarmTimer.getNewAlarmMilitaryHour(context.getApplicationContext()),
                        snoozeTime,
                        alarmTimer.getUpdatedStartAmOrPm(context.getApplicationContext()));
            }
        }

        return timeFormat;
    }
}//end class
