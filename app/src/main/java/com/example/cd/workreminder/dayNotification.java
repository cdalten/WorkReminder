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
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
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
    private Ringtone ringtone; //Added on 11 - 22 - 2019
    private static final String PRODUCTION_TAG = "DAY NOTIFICATION TAG"; //Added on 11 - 15 - 2019

    private String dayOfWeek = ""; //Added on 12 - 16 - 2019
    private int startMilitaryHour = 0; //Added on 12 - 15 - 2019
    private int startMilitaryMinute = 0;
    private String startAmOrPm = ""; //Added on 12 - 18 - 2019
    private int endMilitaryHour = 0;
    private int endMilitaryMinute = 0;
    private String endAmOrPm = "";
    private  MilitaryTime militaryTime; //Added on 12 - 18 - 2019

    public dayNotification() {} //Added on 11 - 22 - 2019
    public dayNotification(Context context) {
        this.context = context;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("LG_WORK_PHONE", "DAY NOTIFICATION ONCREATE() GOT CALLED");
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
        militaryTime = MilitaryTime.getInstance();
        //AlarmTimer alarmTimer = AlarmTimer.getInstance();

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

            setMilitaryTimeForWorkPreferences(militaryTime);
            setNotificationDisplay(
                    context.getString(dayOfWeek), militaryTime);
        }
    }

    //Added on 12 - 18 - 2019
    private void setMilitaryTimeForWorkPreferences(MilitaryTime militaryTime) {
        this.militaryTime = militaryTime;
    }

    public MilitaryTime getCurrentMilitaryTime() {
        return this.militaryTime;
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


    //Added on 11 - 2  2019
    //Reason 1007 why Java sucks massive dick. Fuck Java. Fuck OOP. And fuck this cold weather.
    public long getNewAlarmTime() {
        return this.newAlarmTime;
    }

    //Added on 10 - 18 - 2019
    @TargetApi(24)
    public void setNotificationDisplay(String dayOfWeek, MilitaryTime militaryTime)
    {

        setDayOfWeek(dayOfWeek);
        //I'm not that sure that I need thse methodds
        setStartMilitaryHour(militaryTime.getStartMilitaryHour());
        setStartMilitaryMinute(militaryTime.getStartMilitaryMinute());
        setStartAmOrPm(militaryTime.getStartAmOrPm());
        setEndMilitaryHour(militaryTime.getEndMilitaryHour());
        setEndMilitaryMinute(militaryTime.getEndMilitaryMinute());
        setEndAmOrPm(militaryTime.getEndAmOrPm());

        long startTime = convertToStartTime(militaryTime.getStartMilitaryHour(),militaryTime.getStartMilitaryMinute());
        long endTime = convertToEndTime(militaryTime.getEndMilitaryHour(), militaryTime.getEndMilitaryMinute());
        long currentTime = getCurrentTime();

        AlarmTimer alarmTimer = AlarmTimer.getInstance();

        if (currentTime > startTime && currentTime < endTime) {
            displayNotification("YOU'RE SUPPOSED TO BE AT WORK");
        } else if (currentTime == startTime) {
            alarmTimer.setStartMilitaryHour(alarmTimer.getUpdatedHour());
            displayNotification(alarmTimer,
                    "ALARM");

        } else if (currentTime == endTime) {
            alarmTimer.setStartMilitaryHour(alarmTimer.getStartMilitaryHour());
            displayNotification(alarmTimer,
                    "ALARM");
        } else {
            //alarmTimer.setStartMilitaryHour(getStartMilitaryHour());
            //alarmTimer.setStartMilitaryMinute(getEndMilitaryMinute());
            setNewNotificationDisplayAlarm();

        }
    }

    //Added on 10 - 23 - 2019
    //vs trying to overload the current method?
    @TargetApi(24)
    public void setNewNotificationDisplayAlarm()
    {
        AlarmTimer alarmTimer = AlarmTimer.getInstance();
        alarmTimer.setSavedAlarmTime(context,
                getDayOfWeek(),
                getStartMilitaryHour(),
                getStartMilitaryMinute(),
                true
        );
        setAlarm(alarmTimer);
    }

    @TargetApi(24)
    public void setAlarm(AlarmTimer alarmTimer)
    {
        displayNotification(alarmTimer,
                "ALARM");
        //code copied from
        //https://developer.android.com/training/scheduling/alarms
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WorkAlarmReceiver.class);

        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, getStartMilitaryHour());
        calendar.set(Calendar.MINUTE, getStartMilitaryMinute());
        this.newAlarmTime = calendar.getTime().getTime();

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 20, alarmIntent);

    }

    //Added on 12 - 16 - 2019
    private void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    private String getDayOfWeek() {
        return this.dayOfWeek;
    }

    //Added on 12 - 15 - 2019
    private void setStartMilitaryHour(int startMilitaryHour) {
        this.startMilitaryHour = startMilitaryHour;
    }

    private void setStartMilitaryMinute(int startMilitaryMinute) {
        this.startMilitaryMinute = startMilitaryMinute;
    }

    //Added on 12 - 18 - 2019
    private void setStartAmOrPm(String startAmOrPm) {
        this.startAmOrPm = startAmOrPm;
    }

    //Added on 12 - 18 - 2019
    private void setEndMilitaryHour(int endMilitaryHour) {
        this.endMilitaryHour = endMilitaryHour;
    }

    private void setEndMilitaryMinute(int endMilitaryMinute) {
        this.endMilitaryMinute = startMilitaryMinute;
    }

    //Added on 12 - 18 - 2019
    private void setEndAmOrPm(String endAmOrPm) {
        this.endAmOrPm = endAmOrPm;
    }

    public int getStartMilitaryHour() {
        return this.startMilitaryHour;
    }

    public int getStartMilitaryMinute() {
        return this.startMilitaryMinute;
    }

    //Added on 12 - 18 - 2019
    public String getStartAmOrPm() {
        return this.startAmOrPm;
    }
    public int getEndMilitaryHour() {
        return this.endMilitaryHour;
    }

    public int getEndMilitaryMinute() {
        return this.endMilitaryMinute;
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
    public void displayNotification(AlarmTimer alarmTimer,
                                    String notificationTitle) {
        String notificationText = buildAlarmTimeFormatDisplay(
                alarmTimer.getDayOfWeek(),
                alarmTimer.getUpdatedHour(),
                alarmTimer.getUpdatedMinute(),
                alarmTimer.getUpdatedStartAmOrPm());

        //Intent snoozeIntent = new Intent(context, WorkAlarmReceiver.class);
        Intent snoozeIntent = new Intent(context, AlarmIntentService.class);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        snoozeIntent.setAction(WorkAlarmReceiver.ACTION_SNOOZE);

        //Uri uri = Uri.parse(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString());
        //ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), uri);
        snoozeIntent.putExtra("ALARM_RINGTONE", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        //snoozeIntent.putExtra("ALARM_RINGTONE", ringtone);
        //snoozeIntent.putExtra("ACTION_SNOOZE", "ACTION_SNOOZE");
        PendingIntent snoozePendingIntent = PendingIntent.getService(context, 0, snoozeIntent, 0);
        //context.sendBroadcast(snoozeIntent);
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
        //dismissIntent.putExtra("ALARM_RINGTONE", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        dismissIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        dismissIntent.setAction(WorkAlarmReceiver.ACTION_DISMISS);
        //context.sendBroadcast(dismissIntent);
        PendingIntent dismissPendingIntent = PendingIntent.getService(context, 0, dismissIntent, 0);
        //PendingIntent dismissPendingIntent =
        //        PendingIntent.getBroadcast(context, 0, dismissIntent, 0);


        Intent alarmSoundIntent = new Intent(context, AlarmIntentService.class);
        alarmSoundIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        alarmSoundIntent.putExtra("ALARM_RINGTONE", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));

        context.startService(alarmSoundIntent);

        PendingIntent alarmSoundPendingIntent = PendingIntent.getService(context, 0, alarmSoundIntent, 0);

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

        //dismissPendingIntent.cancel();
    }


    //Added on 11 - 21 - 2019. I'm not that sure if I need this method.
    //Ringtone from WorkAlarmReceiver
    public void setNotificationAlarmSound(Ringtone ringtone) {
        this.ringtone = ringtone;
        Log.e("DAY NOTIFICATION TAG", "THE WORK ALARM RECEIVER INSTANCE IS: " + ringtone);
    }

    //Added on 11 - 22 - 2019;
    public Ringtone getRingtone() {
        return ringtone;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("DAY NOTIFICATION"," DAY NOTIFICATION PAUSE CALLED");
    }

}//end class
