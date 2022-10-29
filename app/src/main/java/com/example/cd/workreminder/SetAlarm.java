/*
 Copyright © 2017-2022 Chad Altenburg <cdalten@PumpingDansHotLookingStepMom.com>

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
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;


/*
 Sets the notification based on the current day of the week. It does this by retrieving the stored time,
 which is in military format. Then it converts it to civilian time. From there, it sets the alarm
 notification.

 The reason for storing the time in military time format is so that I can compare times.
 */
public class SetAlarm extends AppCompatActivity {
    SharedPreferences pref;
    Context context; //Added on 10 - 14 - 2019
    private AlarmManager alarmMgr; //Added on 10 - 30- 2019
    private PendingIntent alarmIntent; //Added on 10 - 30 - 2019
    private String day; //Added on 10 - 31 - 2019
    private long newAlarmTime; //Added on 11 - 2 - 2019
    private Ringtone ringtone; //Added on 11 - 22 - 2019
    private static final String PRODUCTION_TAG = "SET_ALARM_TAG:"; //Added on 11 - 15 - 2019

    private int startMilitaryHour = 0; //Added on 12 - 15 - 2019
    private int startMilitaryMinute = 0;
    private String startAmOrPm = ""; //Added on 12 - 18 - 2019
    private int endMilitaryHour = 0;
    private int endMilitaryMinute = 0;
    private String endAmOrPm = "";
    private  MilitaryTime militaryTime; //Added on 12 - 18 - 2019

    private String newDayOfWeekStartHour = ""; //Added on 12 - 22 - 2019
    private String newDayOfWeekStartMinute = "";
    private String newDayOfWeekStartAmOrPm = "";
    private String newDayOfWeekEndHour = "";
    private String newDayOfWeekEndMinute =  "";
    private String newDayOfWeekEndAmOrPm = "";
    private String previousDay = ""; //Added on 12 - 27 - 2019

    BroadcastReceiver br;
    IntentFilter intentFilter;
    public SetAlarm() {} //Added on 11 - 22 - 2019

    public SetAlarm(Context context) {
        this.context = context;
        // calendar = Calendar.getInstance();
        //currentTime = calendar.getTime().getTime();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("LG_WORK_PHONE", "DAY NOTIFICATION ONCREATE() GOT CALLED");
    }

    //Google Calendar only works with API level 24 and higher
    //Added on 7 - 1 - 2019
    //Schizophrenic method call ripped off from the Android Notification source code -)
    public int handleThirdShift() {
        //final Calendar cal = Calendar.getInstance();
        //int currentDay = cal.get(Calendar.DAY_OF_WEEK); //vs inside if??
        //CurrentWorkHours currentWorkHours = new CurrentWorkHours();
        int day = getDayOfWeek();
        if (day == WorkReaderContract.WorkEntry.SUNDAY) {
            setNotification(
                    R.string.SUNDAY,
                    R.string.SUNDAY_START_HOUR,
                    R.string.SUNDAY_START_MINUTE,
                    R.string.SUNDAY_START_AM_OR_PM,
                    R.string.SUNDAY_END_HOUR,
                    R.string.SUNDAY_END_MINUTE,
                    R.string.SUNDAY_END_AM_OR_PM,

                    "SATURDAY",
                    R.string.MONDAY_START_HOUR,
                    R.string.MONDAY_START_MINUTE,
                    R.string.MONDAY_START_AM_OR_PM
            );
        } else if (day ==  WorkReaderContract.WorkEntry.MONDAY) {
            //if(!week.get(position).get(0).equals("OFF")) {
            setNotification(
                    R.string.MONDAY,
                    R.string.MONDAY_START_HOUR,
                    R.string.MONDAY_START_MINUTE,
                    R.string.MONDAY_START_AM_OR_PM,
                    R.string.MONDAY_END_HOUR,
                    R.string.MONDAY_END_MINUTE,
                    R.string.MONDAY_END_AM_OR_PM,

                    "SUNDAY",
                    R.string.TUESDAY_START_HOUR,
                    R.string.TUESDAY_START_MINUTE,
                    R.string.TUESDAY_START_AM_OR_PM
            );
        } else if (day ==  WorkReaderContract.WorkEntry.TUESDAY) {
            setNotification(
                    R.string.TUESDAY,
                    R.string.TUESDAY_START_HOUR,
                    R.string.TUESDAY_START_MINUTE,
                    R.string.TUESDAY_START_AM_OR_PM,
                    R.string.TUESDAY_END_HOUR,
                    R.string.TUESDAY_END_MINUTE,
                    R.string.TUESDAY_END_AM_OR_PM,

                    "MONDAY",
                    R.string.WEDNESDAY_START_HOUR,
                    R.string.WEDNESDAY_START_MINUTE,
                    R.string.WEDNESDAY_START_AM_OR_PM
            );
        } else if (day ==  WorkReaderContract.WorkEntry.WEDNESDAY) {
            setNotification(
                    R.string.WEDNESDAY,
                    R.string.WEDNESDAY_START_HOUR,
                    R.string.WEDNESDAY_START_MINUTE,
                    R.string.WEDNESDAY_START_AM_OR_PM,
                    R.string.WEDNESDAY_END_HOUR,
                    R.string.WEDNESDAY_END_MINUTE,
                    R.string.WEDNESDAY_END_AM_OR_PM,

                    "TUESDAY",
                    R.string.THURSDAY_START_HOUR,
                    R.string.THURSDAY_START_MINUTE,
                    R.string.THURSDAY_START_AM_OR_PM
            );
        }else if (day ==  WorkReaderContract.WorkEntry.THURSDAY) {
            setNotification(
                    R.string.THURSDAY,
                    R.string.THURSDAY_START_HOUR,
                    R.string.THURSDAY_START_MINUTE,
                    R.string.THURSDAY_START_AM_OR_PM,
                    R.string.THURSDAY_END_HOUR,
                    R.string.THURSDAY_END_MINUTE,
                    R.string.THURSDAY_END_AM_OR_PM,

                    "WEDNESDAY",
                    R.string.FRIDAY_START_HOUR,
                    R.string.FRIDAY_START_MINUTE,
                    R.string.FRIDAY_START_AM_OR_PM
            );
        } else if (day ==  WorkReaderContract.WorkEntry.FRIDAY) {
            //if(!week.get(position).get(0).equals("OFF")) {
            setNotification(
                    R.string.FRIDAY,
                    R.string.FRIDAY_START_HOUR,
                    R.string.FRIDAY_START_MINUTE,
                    R.string.FRIDAY_START_AM_OR_PM,
                    R.string.FRIDAY_END_HOUR,
                    R.string.FRIDAY_END_MINUTE,
                    R.string.FRIDAY_END_AM_OR_PM,

                    "THURSDAY",
                    R.string.SATURDAY_START_HOUR,
                    R.string.SATURDAY_START_MINUTE,
                    R.string.SATURDAY_START_AM_OR_PM
            );
        }
        else if (day == WorkReaderContract.WorkEntry.SATURDAY) {
            setNotification(
                    R.string.SATURDAY,
                    R.string.SATURDAY_START_HOUR,
                    R.string.SATURDAY_START_MINUTE,
                    R.string.SATURDAY_START_AM_OR_PM,
                    R.string.SATURDAY_END_HOUR,
                    R.string.SATURDAY_END_MINUTE,
                    R.string.SATURDAY_END_AM_OR_PM,

                    "FRIDAY",
                    R.string.SUNDAY_START_HOUR,
                    R.string.SUNDAY_START_MINUTE,
                    R.string.SUNDAY_START_AM_OR_PM
            );
        }

        return day;
    }


    //Added on 10 - 7 - 2019
    //Do I need the last three args in the function??
    public void setNotification(
            int currentDayOfWeek,
            int dayOfWeekStartHour, int dayOfWeekStartMinute, int dayOfWeekStartAmOrPm,
            int dayOfWeekEndHour, int dayOfWeekEndMinute, int dayOfWeekEndAmOrPm,

            String previousDay,
            int endDayOfWeekStartHour, int endDayOfWeekStartMinute, int endDayOfWeekStartAmOrPm
    )
    {
        pref = context.getSharedPreferences(WorkReaderContract.WorkEntry.SAVED_PREFERENCESS, MODE_PRIVATE);

        newDayOfWeekStartHour = pref.getString(context.getString(dayOfWeekStartHour), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT);
        newDayOfWeekStartMinute = pref.getString(context.getString(dayOfWeekStartMinute), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT);
        newDayOfWeekStartAmOrPm = pref.getString(context.getString(dayOfWeekStartAmOrPm), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT);
        newDayOfWeekEndHour = pref.getString(context.getString(dayOfWeekEndHour), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT);
        newDayOfWeekEndMinute =  pref.getString(context.getString(dayOfWeekEndMinute), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT);
        newDayOfWeekEndAmOrPm = pref.getString(context.getString(dayOfWeekEndAmOrPm), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT);

        final MilitaryTime militaryTime = MilitaryTime.getInstance();
        final AlarmTimer alarmTimer = AlarmTimer.getInstance();

        pref = context.getSharedPreferences(WorkReaderContract.WorkEntry.SAVED_PREFERENCESS, MODE_PRIVATE);

        this.day = pref.getString(context.getString(currentDayOfWeek), "OFF");

        if (!(pref.getString(context.getString(currentDayOfWeek), "OFF").equals("OFF"))) {
            //pref.getString( getString(R.string.FRIDAY_START_HOUR), "" );
            militaryTime.convertStartCivilianTimeToMilitaryTime(
                    newDayOfWeekStartHour, newDayOfWeekStartMinute, newDayOfWeekStartAmOrPm);

            militaryTime.convertEndCivilianTimeToMilitaryTime(
                    newDayOfWeekEndHour, newDayOfWeekEndMinute, newDayOfWeekEndAmOrPm);


            saveAlarmTime(alarmTimer, militaryTime);
            setMilitaryTimeForWorkPreferences(militaryTime);
            setNotificationDisplay(context, militaryTime);
        } else {
            //final Calendar cal = Calendar.getInstance();
            int currentDay = getDayOfWeek();
                    //cal.get(Calendar.DAY_OF_WEEK);
            storeHoursInGUI currentWeek = new storeHoursInGUI(context.getApplicationContext());
            ArrayList<ArrayList<String>> week = currentWeek.addHours();
//week.get(currentDay).get(0)
            int i = 0;
            for (i = currentDay; i < week.size() - 2; i++) {
                if (!week.get(currentDay).get(0).equals("OFF")) {
                    break;
                }
            }//end for

            displayNotification(context.getApplicationContext(),

                    week.get(i).get(0)  //day
                    + " " + week.get(i).get(1) //hour
                    + ":" + week.get(i).get(2) //minute
                    + " " + week.get(i).get(3)); //Am or Pm



        }
    }//end method

    //I don't use Goog;e Calendar get day of week because it produces an off by one error on the
    //p
    public int getDayOfWeek() {
        int day = 0;
        //SimpleDateFormat sdf = new SimpleDateFormat(" ");
        //Date d = new Date();
        //String dayOfTheWeek = sdf.format(d);
        //String[] dayofWeek = dayOfTheWeek.split(" ");
        SimpleDateFormat simpleformat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
        simpleformat = new SimpleDateFormat("E");
        String dayofWeek = simpleformat.format(new Date());
        if (dayofWeek.equals("Sun")) {
            day = WorkReaderContract.WorkEntry.SUNDAY;
        } else if (dayofWeek.equals("Mon")) {
            day = WorkReaderContract.WorkEntry.MONDAY;
        } else if (dayofWeek.equals("Tue")) {
            day = WorkReaderContract.WorkEntry.TUESDAY;
        } else if (dayofWeek.equals("Wed")) {
            day = WorkReaderContract.WorkEntry.WEDNESDAY;;
        } else if (dayofWeek.equals("Thu")) {
            day = WorkReaderContract.WorkEntry.THURSDAY;;
        }else if( dayofWeek.equals("Fri")) {
            day = WorkReaderContract.WorkEntry.FRIDAY;;
        }else if( dayofWeek.equals("Sat")) {
            day = WorkReaderContract.WorkEntry.SATURDAY;
        }

        return day;
    }

    //Added on 1 - 14 - 2010
    private void saveAlarmTime(AlarmTimer alarmTimer, MilitaryTime militaryTime) {
        alarmTimer.saveCurrentEndMilitaryHour(context, alarmTimer.getCurrentEndMilitaryHour(context));
        alarmTimer.saveCurrentEndMilitaryMinute(context, alarmTimer.getCurrentEndMilitaryMinute(context));
        alarmTimer.saveCurrentDayOfWeek(context.getApplicationContext(),day);
        alarmTimer.savePreviousDayOfWeek(context.getApplicationContext(),previousDay);
    }

    //Added on 12 - 18 - 2019
    private void setMilitaryTimeForWorkPreferences(MilitaryTime militaryTime) {
        this.militaryTime = militaryTime;
    }

    //Added on 11 - 2  2019
    //Reason 1007 why Java sucks massive dick. Fuck Java. Fuck OOP. And fuck this cold weather.
    public long getNewAlarmTime() {
        return this.newAlarmTime;
    }

    //Added on 10 - 18 - 2019
    public void setNotificationDisplay(Context context, MilitaryTime militaryTime) {

        final AlarmTimer alarmTimer = AlarmTimer.getInstance();
        setStartAmOrPm(militaryTime.getStartAmOrPm());
        setEndAmOrPm(militaryTime.getEndAmOrPm());

        alarmTimer.setSavedAlarmTime(context, day, militaryTime.getStartMilitaryHour(), militaryTime.getStartMilitaryMinute(), false);

        int currentHour = 0;
        int currentMinute = 0;


        long currentTime = 0;
        long startTime = 0;
        long endTime = 0;

        // /java.text.DateFormat.getTimeInstance().format(new Date());//new SimpleDateFormat("HH:mm", Locale.getDefault().fot);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currentHour = android.icu.util.Calendar.getInstance().get(android.icu.util.Calendar.HOUR_OF_DAY);
            currentMinute = android.icu.util.Calendar.getInstance().get(android.icu.util.Calendar.MINUTE);
        } else {
            currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
            currentMinute = java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE);
            //get(java.util.Calendar.MILLISECOND);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currentTime = android.icu.util.Calendar
                    .getInstance()
                    .getTime()
                    .getTime();
        } else {
            currentTime = java.util.Calendar
                    .getInstance()
                    .getTime()
                    .getTime();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            final android.icu.util.Calendar calendarStartTime =  android.icu.util.Calendar.getInstance();
            calendarStartTime.set(android.icu.util.Calendar.HOUR_OF_DAY, militaryTime.getStartMilitaryHour());
            calendarStartTime.set(Calendar.MINUTE, militaryTime.getStartMilitaryMinute());
            startTime = calendarStartTime.getTime().getTime();
        } else {
            final java.util.Calendar calendarStartTime = java.util.Calendar.getInstance();
            calendarStartTime.set(java.util.Calendar.HOUR_OF_DAY, militaryTime.getStartMilitaryHour());
            calendarStartTime.set(java.util.Calendar.MINUTE, militaryTime.getStartMilitaryMinute());
            startTime = calendarStartTime.getTime().getTime();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            final android.icu.util.Calendar calendarEndTime = android.icu.util.Calendar.getInstance();
            calendarEndTime.set(android.icu.util.Calendar.HOUR_OF_DAY, militaryTime.getEndMilitaryHour());
            calendarEndTime.set(android.icu.util.Calendar.MINUTE, militaryTime.getStartMilitaryMinute());
            endTime = calendarEndTime.getTime().getTime();
        } else {
            final java.util.Calendar calendarEndTime = java.util.Calendar.getInstance();
            calendarEndTime.set(java.util.Calendar.HOUR_OF_DAY, militaryTime.getEndMilitaryHour());
            calendarEndTime.set(java.util.Calendar.MINUTE, militaryTime.getEndMilitaryMinute());
            endTime = calendarEndTime.getTime().getTime();
        }
        //This doesn't work for after hours
        /*
         For all you people who have never experienced working third shift for a drunk pervert,
         third shift usually starts at 12 AM on the NEXT day. And because the shift scheduling
         is usually done by a complete dipshit, the NEXT day becomes the PREVIOUS day.

         I guess if you want to see this in action, go work as a 3rd shift at a place like Walmart
         or Target for a couple of months.
         */
        //if (militaryTime.getStartMilitaryHour() == 0 && militaryTime.getStartAmOrPm().equals("AM")) {
        //    setNewNotificationDisplayAlarm(context.getApplicationContext(),
        //            alarmTimer.getPreviousDayOfWeekSavedDayOfWeek(context.getApplicationContext()),alarmTimer);
        //} else {
        /*if (currentHour >= militaryTime.getStartMilitaryHour()
                && currentMinute <= militaryTime.getStartMilitaryMinute() // :09 < :15
                && currentHour <= militaryTime.getEndMilitaryHour() //6 <= 8
                && currentMinute < militaryTime.getEndMilitaryMinute() // :11 < 45
                ) {
            displayNotification(context.getApplicationContext(), "YOU'RE SUPPOSED TO BE AT WORK");
        }
        else if(currentHour >= militaryTime.getStartMilitaryHour()
                &&currentMinute > militaryTime.getStartMilitaryMinute()
                && currentHour < militaryTime.getEndMilitaryHour()
                && currentMinute > militaryTime.getEndMilitaryMinute()) {
            displayNotification(context.getApplicationContext(), "YOU ARE SUPPOSED TO BE AT WORK");

        }else if (currentHour >= militaryTime.getStartMilitaryHour()
                && currentMinute >= militaryTime.getStartMilitaryMinute() // :14 > :00
                && currentHour >= militaryTime.getEndMilitaryHour() //6 >=8/
                && currentMinute > militaryTime.getEndMilitaryMinute() // < :15 <45
                )

        {
            displayNotification(context.getApplicationContext(), "YOU MISSED YOUR SHIFT");

        } else if (currentHour >= militaryTime.getStartMilitaryHour()
                && currentMinute >= militaryTime.getStartMilitaryMinute()
                && currentHour >= militaryTime.getEndMilitaryHour() // 6 >= 8
                && currentMinute < militaryTime.getEndMilitaryMinute()
                ){
            displayNotification(context.getApplicationContext(), "YOU MISSED YOUR SHIFT");
    }

            else if (currentHour >= militaryTime.getEndMilitaryHour()
                    && currentMinute > militaryTime.getEndMilitaryMinute()
                    )
            {
                //displayNotification(context.getApplicationContext(),"YOU'RE SUPPOSED TO BE AT WORK");
                displayNotification(context.getApplicationContext(), "YOU MISSED YOUR SHIFT");

            //} else if(currentAlarmTime > currentTime) {

*/
        if (militaryTime.getStartMilitaryHour() == militaryTime.getEndMilitaryHour()) {
            Log.d(PRODUCTION_TAG, "DON'T SET HOURS");
        }
        else if (currentTime > startTime && currentTime < endTime) {
            displayNotification(context.getApplicationContext(), "YOU ARE SUPPOSED TO BE AT WORK");

                Log.d(PRODUCTION_TAG, "-----------------------------------------------------------");
                Log.d(PRODUCTION_TAG, "THE UPDATED ALARM TIME IS: " + alarmTimer.getNewAlarmMilitaryMinute(context));
                Log.d(PRODUCTION_TAG, "-------------------------------------------------------------");
                //setNewNotificationDisplayAlarm(context,
                //        alarmTimer.getCurrentSavedDayOfWeek(context.getApplicationContext()),alarmTimer);
        } else if (currentTime > endTime) {
            displayNotification(context.getApplicationContext(), "YOU MISSED YOUR SHIFT");
        }

        else if (currentTime < startTime) {
                //displayNotification(context.getApplicationContext(), "DID YOU MISS YOUR SHIFT?");


    //Added on 10 - 23 - 2019
    //A wrapper because I'm so drunk that it hurts to think correctly.
        setNewNotificationDisplayAlarm(context,
                                   alarmTimer.getCurrentSavedDayOfWeek(context.getApplicationContext()),alarmTimer);
        }
    }
    public void setNewNotificationDisplayAlarm(Context context, String day, AlarmTimer alarmTimer)
    {
        /*
         need to shift time to calculate the time time based on the minutes set
         before the start the start of the minutes
         */


        //Calendar cal = Calendar.getInstance();
        //if (alarmTimer.getNewAlarmMilitaryMinute(context) > cal.get(Calendar.MINUTE)) {
            displayNotification(alarmTimer, false, true,
                    "ALARM");
            setAlarm(context, alarmTimer);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //context.unregisterReceiver(br);
    }

    public void setAlarm(Context context, AlarmTimer alarmTimer) {

        //code copied from
        //https://developer.android.com/training/scheduling/alarms
        //enableBootReceiver();
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WorkNotificationReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //final Calendar calendar = Calendar.getInstance();
            android.icu.util.Calendar.getInstance().setTimeInMillis(System.currentTimeMillis());
        /*
          Time changes when the user changes the time before shift from something like
          20 minutes before the start of the shift to something like 10 minutes before
          the start of a shift.
         */
            if (alarmTimer.getNewAlarmMilitaryHour(context) == 24) {
                android.icu.util.Calendar.getInstance().set(android.icu.util.Calendar.HOUR_OF_DAY, 0);
            } else {
                android.icu.util.Calendar.getInstance().set(android.icu.util.Calendar.HOUR_OF_DAY, alarmTimer.getNewAlarmMilitaryHour(context));
            }
            ;
            android.icu.util.Calendar.getInstance().set(
                    android.icu.util.Calendar.MINUTE, alarmTimer.getNewAlarmMilitaryMinute(context));
            android.icu.util.Calendar.getInstance().set(android.icu.util.Calendar.SECOND, 0);
            android.icu.util.Calendar.getInstance().set(android.icu.util.Calendar.MILLISECOND, 0); //??

            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, android.icu.util.Calendar.getInstance().getTimeInMillis(),
                    1000 * 60, alarmIntent);
        } else {
            java.util.Calendar.getInstance().setTimeInMillis(System.currentTimeMillis());

            if (alarmTimer.getNewAlarmMilitaryHour(context)== 24) {
                java.util.Calendar.getInstance().set(java.util.Calendar.HOUR_OF_DAY, 0);

            } else {
                java.util.Calendar.getInstance().set(java.util.Calendar.HOUR_OF_DAY, alarmTimer.getNewAlarmMilitaryHour(context));
            }

            java.util.Calendar.getInstance().set(java.util.Calendar.MINUTE, alarmTimer.getNewAlarmMilitaryMinute(context));
            java.util.Calendar.getInstance().set(java.util.Calendar.SECOND, 0);
            java.util.Calendar.getInstance().set(java.util.Calendar.MILLISECOND, 0);
        }

    }


    //Added on 12 - 18 - 2019
    private void setStartAmOrPm(String startAmOrPm) {
        this.startAmOrPm = startAmOrPm;
    }


    //Added on 12 - 18 - 2019
    private void setEndAmOrPm(String endAmOrPm) {
        this.endAmOrPm = endAmOrPm;
    }


    //Added on 12 - 18 - 2019
    public String getStartAmOrPm() {
        return this.startAmOrPm;
    }

    public int getEndMilitaryMinute() {
        return this.endMilitaryMinute;
    }
    public String getEndAmOrPm() {
        return this.endAmOrPm;
    }
    //Added on 10 - 11 - 2019
    public String buildAlarmTimeFormatDisplay(AlarmTimer alarmTimer) {
        return new AlarmTimeFormatDisplay(context, alarmTimer, false).displayCurrentTime();
    }

    //Added on 10 - 18 -2019. Change channelID to NOTIFICATION_ID???
    public void displayNotification(Context context, String notificationTitle) {
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
                                    boolean amSnoozed,
                                    boolean canDisplaySnoozeButton,
                                    String notificationTitle) {
        String notificationText = "";
        if (amSnoozed == false) {
        notificationText = /*buildAlarmTimeFormatDisplay(
                alarmTimer.getDayOfWeek(context.getApplicationContext()),
                alarmTimer.getUpdatedHour(context.getApplicationContext()),
                alarmTimer.getUpdatedMinute(context.getApplicationContext()),
                alarmTimer.getUpdatedStartAmOrPm(context.getApplicationContext()));
                */
            buildAlarmTimeFormatDisplay(alarmTimer);
        } else {
            notificationText = buildAlarmTimeFormatDisplay(alarmTimer);
            /*buildAlarmTimeFormatDisplay(
                    alarmTimer.getDayOfWeek(context.getApplicationContext()),
                    alarmTimer.getUpdatedHour(context.getApplicationContext()),
                    alarmTimer.getUpdatedMinute(context.getApplicationContext()) + alarmTimer.getAlarmSnooze(),
                    alarmTimer.getUpdatedStartAmOrPm(context.getApplicationContext()));
                    */
        }

        DayNotification dayNotification = new DayNotification(context, alarmIntent);
        dayNotification.createNotification(notificationTitle, notificationText);
        Log.e(PRODUCTION_TAG, "");
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
