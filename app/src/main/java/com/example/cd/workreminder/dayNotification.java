/*
 Copyright © 2017-2019 Chad Altenburg <cdalten@PumpingDansHotLookingStepMom.com>

 Permission to use, copy, modify, distribute, and sell this software and its
 documentation for any purpose is hereby granted without fee, provided that
 the above copyright notice appear in all copies and that both that
 copyright notice and this permission notice appear in supporting
 documentation.  No representations are made about the suitability of this
 software for any purpose.  It is provided "as is" without express or
 implied warranty.
*/

package com.example.cd.workreminder;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;


/*private static AlarmTimer instance = new AlarmTimer();

private AlarmTimer() {

        }

//Added on 6 - 13 - 2019
public static AlarmTimer getInstance(){
        return instance;
        }
*/

public class dayNotification extends AppCompatActivity {
    SharedPreferences pref;
    Context context; //Added on 10 - 14 - 2019
    private AlarmManager alarmMgr; //Added on 10 - 30- 2019
    private PendingIntent alarmIntent; //Added on 10 - 30 - 2019
    private String day; //Added on 10 - 31 - 2019
    private long newAlarmTime; //Added on 11 - 2 - 2019

    public dayNotification(Context context) {
        this.context = context;
    }

    //Added on 7 - 1 - 2019
    //Schizophrenic method call ripped off from the Android Notification source code -)
    @TargetApi(24)
    public int handleThirdShift() {
        Calendar cal = Calendar.getInstance();
        int currentDay = cal.get(Calendar.DAY_OF_WEEK); //vs inside if??
        //CurrentWorkHours currentWorkHours = new CurrentWorkHours();
        if (cal.get(Calendar.DAY_OF_WEEK) == java.util.Calendar.SUNDAY) {
            setNotification(
                    R.string.SUNDAY,
                    R.string.SUNDAY_START_HOUR,
                    R.string.SUNDAY_START_MINUTE,
                    R.string.SUNDAY_START_AM_OR_PM,
                    R.string.SUNDAY_END_HOUR,
                    R.string.SUNDAY_END_MINUTE,
                    R.string.SUNDAY_END_AM_OR_PM,

                    R.string.MONDAY_START_HOUR,
                    R.string.MONDAY_START_MINUTE,
                    R.string.MONDAY_START_AM_OR_PM
            );
        } else if (cal.get(Calendar.DAY_OF_WEEK) == java.util.Calendar.MONDAY) {
            //if(!week.get(position).get(0).equals("OFF")) {
            setNotification(
                    R.string.MONDAY,
                    R.string.MONDAY_START_HOUR,
                    R.string.MONDAY_START_MINUTE,
                    R.string.MONDAY_START_AM_OR_PM,
                    R.string.MONDAY_END_HOUR,
                    R.string.MONDAY_END_MINUTE,
                    R.string.MONDAY_END_AM_OR_PM,

                    R.string.TUESDAY_START_HOUR,
                    R.string.TUESDAY_START_MINUTE,
                    R.string.TUESDAY_START_AM_OR_PM
            );
        } else if (cal.get(Calendar.DAY_OF_WEEK) == java.util.Calendar.TUESDAY) {
            setNotification(
                    R.string.TUESDAY,
                    R.string.TUESDAY_START_HOUR,
                    R.string.TUESDAY_START_MINUTE,
                    R.string.TUESDAY_START_AM_OR_PM,
                    R.string.TUESDAY_END_HOUR,
                    R.string.TUESDAY_END_MINUTE,
                    R.string.TUESDAY_END_AM_OR_PM,

                    R.string.WEDNESDAY_START_HOUR,
                    R.string.WEDNESDAY_START_MINUTE,
                    R.string.WEDNESDAY_START_AM_OR_PM
            );
        } else if (cal.get(Calendar.DAY_OF_WEEK) == java.util.Calendar.WEDNESDAY) {
            setNotification(
                    R.string.WEDNESDAY,
                    R.string.WEDNESDAY_START_HOUR,
                    R.string.WEDNESDAY_START_MINUTE,
                    R.string.WEDNESDAY_START_AM_OR_PM,
                    R.string.WEDNESDAY_END_HOUR,
                    R.string.WEDNESDAY_END_MINUTE,
                    R.string.WEDNESDAY_END_AM_OR_PM,

                    R.string.THURSDAY_START_HOUR,
                    R.string.THURSDAY_START_MINUTE,
                    R.string.THURSDAY_START_AM_OR_PM
            );
        }else if (cal.get(Calendar.DAY_OF_WEEK) == java.util.Calendar.THURSDAY) {
            setNotification(
                    R.string.THURSDAY,
                    R.string.THURSDAY_START_HOUR,
                    R.string.THURSDAY_START_MINUTE,
                    R.string.THURSDAY_START_AM_OR_PM,
                    R.string.THURSDAY_END_HOUR,
                    R.string.THURSDAY_END_MINUTE,
                    R.string.THURSDAY_END_AM_OR_PM,

                    R.string.FRIDAY_START_HOUR,
                    R.string.FRIDAY_START_MINUTE,
                    R.string.FRIDAY_START_AM_OR_PM
            );
        } else if (cal.get(Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
            //if(!week.get(position).get(0).equals("OFF")) {
            setNotification(
                    R.string.FRIDAY,
                    R.string.FRIDAY_START_HOUR,
                    R.string.FRIDAY_START_MINUTE,
                    R.string.FRIDAY_START_AM_OR_PM,
                    R.string.FRIDAY_END_HOUR,
                    R.string.FRIDAY_END_MINUTE,
                    R.string.FRIDAY_END_AM_OR_PM,

                    R.string.SATURDAY_START_HOUR,
                    R.string.SATURDAY_START_MINUTE,
                    R.string.SATURDAY_START_AM_OR_PM
            );
        }
        else if (cal.get(Calendar.DAY_OF_WEEK) == java.util.Calendar.SATURDAY) {
            setNotification(
                    R.string.SATURDAY,
                    R.string.SATURDAY_START_HOUR,
                    R.string.SATURDAY_START_MINUTE,
                    R.string.SATURDAY_START_AM_OR_PM,
                    R.string.SATURDAY_END_HOUR,
                    R.string.SATURDAY_END_MINUTE,
                    R.string.SATURDAY_END_AM_OR_PM,

                    R.string.SUNDAY_START_HOUR,
                    R.string.SUNDAY_START_MINUTE,
                    R.string.SUNDAY_START_AM_OR_PM
            );
        }

        return currentDay;
    }


    //Added on 10 - 7 - 2019
    //Do I need the last three args in the function??
    @TargetApi(24)
    private void setNotification(
            int dayOfWeek,
            int dayOfWeekStartHour, int dayOfWeekStartMinute, int dayOfWeekStartAmOrPm,
            int dayOfWeekEndHour, int dayOfWeekEndMinute, int dayOfWeekEndAmOrPm,

            int endDayOfWeekStartHour, int endDayOfWeekStartMinute, int endDayOfWeekStartAmOrPm
    )
    {
        MilitaryTime militaryTime = MilitaryTime.getInstance();
        AlarmTimer alarmTimer = AlarmTimer.getInstance();

        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);

        this.day = pref.getString(context.getString(dayOfWeek), "OFF");

        if (!(pref.getString(context.getString(dayOfWeek), "OFF").equals("OFF"))) {
            //pref.getString( getString(R.string.FRIDAY_START_HOUR), "" );
            militaryTime.convertStartCivilianTimeToMilitaryTime(
                    pref.getString(context.getString(dayOfWeekStartHour), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                    pref.getString(context.getString(dayOfWeekStartMinute), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT),
                    pref.getString(context.getString(dayOfWeekStartAmOrPm), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));

            militaryTime.convertEndCivilianTimeToMilitaryTime(
                    pref.getString(context.getString(dayOfWeekEndHour), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT),
                    pref.getString(context.getString(dayOfWeekEndMinute), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT),
                    pref.getString(context.getString(dayOfWeekEndAmOrPm), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));

            alarmTimer.setAlarmTime(context,
                    militaryTime.getStartMilitaryHour(), militaryTime.getStartMilitaryMinute());

            setNotificationDisplay(
                    context.getString(dayOfWeek),
                    militaryTime.getStartMilitaryHour(),
                    militaryTime.getStartMilitaryMinute(),
                    militaryTime.getEndMilitaryHour(),
                    militaryTime.getStartMilitaryMinute()                    );
        }
    }

    //Added on 10 - 31 - 2019
    public String getDayOfWeek() {
        return this.day;
    }

    //Added on 10 - 23 - 2019
    @TargetApi(24)
    public long convertToStartTime(
            int startMilitaryHour,
            int startMilitaryMinute
    )
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, startMilitaryHour);
        cal.set(Calendar.MINUTE, startMilitaryMinute);

        return cal.getTime().getTime();
    }

    //Added on 10 -23 - 2019
    @TargetApi(24)
    public long convertToEndTime(
            int endMilitaryHour,
            int endMilitaryMinute
    )
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, endMilitaryHour);
        cal.set(Calendar.MINUTE, endMilitaryMinute);

        return cal.getTime().getTime();
    }

    //Added on 10 - 23 - 2019
    @TargetApi(24)
    public long getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime().getTime();
    }


    //Added on 10 - 23 - 2019
    //vs trying to overload the current method?
    @TargetApi(24)
    public void setNewNotificationDisplay(
            String dayOfWeek,
            long startTime,
            long endTime,
            long currentTime
    )
    {
        AlarmTimer alarmTimer = AlarmTimer.getInstance();

        setAlarm( alarmTimer.getNewMilitaryHour(), alarmTimer.getNewMilitaryMinute());

        if (currentTime > startTime && currentTime < endTime) {
            displayNotification("YOU'RE SUPPOSED TO BE AT WORK");
        } else if (currentTime == startTime) {
            alarmTimer.setStartMilitaryHour(alarmTimer.getNewMilitaryHour());
            displayNotification( dayOfWeek,
                    alarmTimer.getUpdatedHour(),
                    alarmTimer.getUpdatedMinute(),
                    alarmTimer.getAMorPM(),
                    "ALARM");

        } else if (currentTime == endTime) {
            alarmTimer.setStartMilitaryHour(alarmTimer.getStartMilitaryHour());
            displayNotification( dayOfWeek,
                    alarmTimer.getUpdatedHour(),
                    alarmTimer.getUpdatedMinute(),
                    alarmTimer.getAMorPM(),
                    "ALARM");
        } /*else if (  pref.getInt("ALARM_HOUR", 0) == 0) {
            displayNotification(dayOfWeek,
                    alarmTimer.getUpdatedHour(),
                    alarmTimer.getUpdatedMinute(),
                    alarmTimer.getAMorPM(),
                    "ALARM");
        } */
        /*else {
            alarmTimer.setStartMilitaryHour(alarmTimer.getNewMilitaryHour());
            displayNotification( dayOfWeek,
                    alarmTimer.getUpdatedHour(),
                    alarmTimer.getUpdatedMinute(),
                    alarmTimer.getAMorPM(),
                    "ALARM (DEBUG MODE)");
        }
        */

    }

    @TargetApi(24)
    public void setAlarm(int newMilitaryHour, int newMilitaryMinute) {
        //code copied from
        //https://developer.android.com/training/scheduling/alarms
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WorkAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, newMilitaryHour);
        calendar.set(Calendar.MINUTE, newMilitaryMinute);
        this.newAlarmTime = calendar.getTime().getTime();

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 20, alarmIntent);
    }

    //Added on 11 - 2  2019
    //Reason 1007 why Java sucks massive dick. Fuck Java. Fuck OOP. And fuck this cold weather.
    public long getNewAlarmTime() {
        return this.newAlarmTime;
    }

    //Added on 10 - 18 - 2019
    @TargetApi(24)
    private void setNotificationDisplay(
            String dayOfWeek,
            int startMilitaryHour,
            int startMilitaryMinute,
            int endMilitaryHour,
            int endMilitaryMinute
    )
    {
        long startTime = convertToStartTime(startMilitaryHour, startMilitaryMinute);
        long endTime = convertToEndTime(endMilitaryHour, endMilitaryMinute);
        long currentTime = getCurrentTime();

        setNewNotificationDisplay(dayOfWeek, startTime, endTime, currentTime);
    }

    //Added on 10 - 11 - 2019
    private String buildAlarmTimeFormatDisplay(String dayOfWeek, int hour, int minute, String amOrPm) {
        String timeFormat = "";

        if (hour == 0) {
            hour = 12;
        }


        //Something like 1:5 becomes while 1:05 while something like 1:10 stays 1:10
        if (minute < 10) {
            timeFormat = dayOfWeek + " " + hour + ":0" + minute + " " + amOrPm;
        } else {
            timeFormat = dayOfWeek + " " + hour + ":" + minute + " " + amOrPm;
        }

        return timeFormat;
    }

    //Added on 10 - 18 -2019. Change channelID to NOTIFICATION_ID???
    public void displayNotification(String notificationTitle) {
        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(context.getApplicationContext(), "0");

        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);
        notificationCompatBuilder.setSmallIcon(R.drawable.ic_stat_work)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationCompatBuilder.setContentTitle(notificationTitle);
        NotificationManagerCompat mNotificationManagerCompat;
        mNotificationManagerCompat = NotificationManagerCompat.from(context.getApplicationContext());
        Notification notification = notificationCompatBuilder.build();
        mNotificationManagerCompat.notify(0, notification);
    }


    //Added on 10 - 7 - 2019
    public void displayNotification(String dayOfWeek,
                                    int newHour,
                                    int newMinute,
                                    String newAmOrPm,
                                    String notificationTitle) {
        String notificationText = buildAlarmTimeFormatDisplay(dayOfWeek,
                newHour,
                newMinute,
                newAmOrPm);


        //Intent snoozeIntent = new Intent(context, WorkAlarmReceiver.class);
        Intent snoozeIntent = new Intent(context, AlarmIntentService.class);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        //.setAction(WorkAlarmReceiver.ACTION_SNOOZE);
        PendingIntent snoozePendingIntent = PendingIntent.getService(context, 0, snoozeIntent, 0);
        //PendingIntent snoozePendingIntent =
        //        PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);
        NotificationCompat.Action snoozeAction =
                new NotificationCompat.Action.Builder(
                        R.drawable.ic_action_stat_reply,
                        "Snooze",
                        snoozePendingIntent)
                        .build();

        //Intent dismissIntent = new Intent(context, WorkAlarmReceiver.class);
        Intent dismissIntent = new Intent(context, AlarmIntentService.class);
        dismissIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        //dismissIntent.setAction(WorkAlarmReceiver.ACTION_DISMISS);
        //dismissIntent.setAction(WorkAlarmReceiver.ACTION_DISMISS);
        PendingIntent dismissPendingIntent = PendingIntent.getService(context, 0, dismissIntent, 0);
        //PendingIntent dismissPendingIntent =
        //        PendingIntent.getBroadcast(context, 0, dismissIntent, 0);
        NotificationCompat.Action dismissAction =
                new NotificationCompat.Action.Builder(
                        R.drawable.ic_action_stat_share,
                        "Dismiss",
                        dismissPendingIntent)
                        .build();


        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(context.getApplicationContext(), "0");

        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);
        notificationCompatBuilder.setSmallIcon(R.drawable.ic_stat_work)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(dismissPendingIntent)
                .addAction(snoozeAction)
                .addAction(dismissAction);
        notificationCompatBuilder.setContentTitle(notificationTitle);
        notificationCompatBuilder.setContentText(notificationText);
        NotificationManagerCompat mNotificationManagerCompat;
        mNotificationManagerCompat = NotificationManagerCompat.from(context.getApplicationContext());
        Notification notification = notificationCompatBuilder.build();
        mNotificationManagerCompat.notify(0, notification);
    }


}//end class
