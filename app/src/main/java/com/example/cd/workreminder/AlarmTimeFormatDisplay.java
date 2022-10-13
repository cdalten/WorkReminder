package com.example.cd.workreminder;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

//Used to format the the alarm time.
public class AlarmTimeFormatDisplay {
    private AlarmTimer alarmTimer;
    private Context context;
    private static int snoozeTime = 0;
    private static int offset = 0;
    private boolean amSnoozed = false;
    private int currentAlarmTime = 0; //Added on 1 - 27 - 2020
    private final String PRODUCTION_TAG = "ALARM_TIME_FORMAT:";

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
            timeFormat = hour + ":0" + minute + " " + amOrPm;
        } else {
            timeFormat = hour + ":" + minute + " " + amOrPm;
        }

        return timeFormat;
    }

    //Added on 1 - 16 - 2020
    public String displayCurrentTime()  {
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

            /*
             currentAlarmTime = 5
             snoozeTime       = 5 + 1 + 0
                              = 5 + 1 + 1
                              = 5 + 1 + 2
                              = 5 + 1 + 3
                              = 5 + 1 + 4
                              =

             */
            currentAlarmTime = alarmTimer.getNewAlarmMilitaryMinute(context);

            snoozeTime = currentAlarmTime + alarmTimer.getAlarmSnooze() + offset;
            if (snoozeTime == 60) {
                offset = 0;
            }
            offset = offset + 1;
            Log.d(PRODUCTION_TAG, "THE SNOOZE TIME IS: " + snoozeTime);
            Date d=new Date();
            SimpleDateFormat sdf=new SimpleDateFormat("h:mm a");
            String[] time =  d.toString().split(":");;

            timeFormat= sdf.format(d);
        }

        return timeFormat;
    }

    public String formatTime(int hour, int minute, String amOrPm) {
        Date d=new Date();
        String timeFormat = hour + ":" + minute + " " + amOrPm;
        //SimpleDateFormat sdf=new SimpleDateFormat("h:mm a");
        SimpleDateFormat sdf=new SimpleDateFormat(timeFormat);
        return  sdf.format(d);
    }
}//end class
