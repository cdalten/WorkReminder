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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
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

public class SetAlarm extends AppCompatActivity {
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

    private String newDayOfWeekStartHour = ""; //Added on 12 - 22 - 2019
    private String newDayOfWeekStartMinute = "";
    private String newDayOfWeekStartAmOrPm = "";
    private String newDayOfWeekEndHour = "";
    private String newDayOfWeekEndMinute =  "";
    private String newDayOfWeekEndAmOrPm = "";
    private String previousDay = ""; //Added on 12 - 27 - 2019
    //private long currentTime = 0;

    BroadcastReceiver br;
    IntentFilter intentFilter;
    public SetAlarm() {} //Added on 11 - 22 - 2019
    @TargetApi(24)
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

                    "SATURDAY",
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

                    "SUNDAY",
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

                    "MONDAY",
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

                    "TUESDAY",
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

                    "WEDNESDAY",
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

                    "THURSDAY",
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

                    "FRIDAY",
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
    public void setNotification(
            int currentDayOfWeek,
            int dayOfWeekStartHour, int dayOfWeekStartMinute, int dayOfWeekStartAmOrPm,
            int dayOfWeekEndHour, int dayOfWeekEndMinute, int dayOfWeekEndAmOrPm,

            String previousDay,
            int endDayOfWeekStartHour, int endDayOfWeekStartMinute, int endDayOfWeekStartAmOrPm
    )
    {
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);

        newDayOfWeekStartHour = pref.getString(context.getString(dayOfWeekStartHour), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT);
        newDayOfWeekStartMinute = pref.getString(context.getString(dayOfWeekStartMinute), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT);
        newDayOfWeekStartAmOrPm = pref.getString(context.getString(dayOfWeekStartAmOrPm), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT);
        newDayOfWeekEndHour = pref.getString(context.getString(dayOfWeekEndHour), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT);
        newDayOfWeekEndMinute =  pref.getString(context.getString(dayOfWeekEndMinute), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT);
        newDayOfWeekEndAmOrPm = pref.getString(context.getString(dayOfWeekEndAmOrPm), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT);

        militaryTime = MilitaryTime.getInstance();
        AlarmTimer alarmTimer = AlarmTimer.getInstance();

        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);

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
        }
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

    @TargetApi(24)
    public long convertAlarmTime(
            int startNewAlarmMilitaryHour,
            int startNewAlarmMilitaryMinute
    )
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, startNewAlarmMilitaryHour);
        cal.set(Calendar.MINUTE, startNewAlarmMilitaryMinute);

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
    public void setNotificationDisplay(Context context, MilitaryTime militaryTime)
    {

        AlarmTimer alarmTimer = AlarmTimer.getInstance();
        setStartMilitaryHour(militaryTime.getStartMilitaryHour());
        setStartMilitaryMinute(militaryTime.getStartMilitaryMinute());
        setStartAmOrPm(militaryTime.getStartAmOrPm());
        setEndMilitaryHour(militaryTime.getEndMilitaryHour());
        setEndMilitaryMinute(militaryTime.getEndMilitaryMinute());
        setEndAmOrPm(militaryTime.getEndAmOrPm());

        long startTime = convertToStartTime(militaryTime.getStartMilitaryHour(),militaryTime.getStartMilitaryMinute());
        long endTime = convertToEndTime(militaryTime.getEndMilitaryHour(), militaryTime.getEndMilitaryMinute());
        alarmTimer.setSavedAlarmTime(context,day, militaryTime.getStartMilitaryHour(), militaryTime.getStartMilitaryMinute(), false);

        long currentAlarmTime = convertAlarmTime(alarmTimer.getNewAlarmMilitaryHour(context), alarmTimer.getNewAlarmMilitaryMinute(context));
        long currentTime = getCurrentTime();

        /*
         For all you people who have never experienced working third shift for a drunk pervert,
         third shift usually starts at 12 AM on the NEXT day. And because the shift scheduling
         is usually done by a complete dipshit, the NEXT day becomes the PREVIOUS day.

         I guess if you want to see this in action, go work as a 3rd shift at a place like Walmart
         or Target for a couple of months.
         */
        if (militaryTime.getStartMilitaryHour() == 0 && militaryTime.getStartAmOrPm().equals("AM")) {
            setNewNotificationDisplayAlarm(context.getApplicationContext(),
                    alarmTimer.getPreviousDayOfWeekSavedDayOfWeek(context.getApplicationContext()),alarmTimer);
        } else {
            if (currentTime > startTime && currentTime < endTime) {
                displayNotification(context.getApplicationContext(),"YOU'RE SUPPOSED TO BE AT WORK");
            } else if (currentTime == startTime) {
                displayNotification(context.getApplicationContext(), "YOU'RE SUPPOSED TO BE AT WORK");
            } else if (currentTime == endTime) {
                displayNotification(context.getApplicationContext(),"YOU'RE SUPPOSED TO BE AT WORK");
            } else if (currentTime > endTime) {
                displayNotification(context.getApplicationContext(), "YOU MISSED YOUR SHIFT");
            } else if(currentAlarmTime > currentTime) {


                Log.e(PRODUCTION_TAG, "-----------------------------------------------------------");
                Log.e(PRODUCTION_TAG, "THE UPDATED ALARM TIME IS: " + alarmTimer.getNewAlarmMilitaryMinute(context));

                Log.e(PRODUCTION_TAG, "THE END ALARM TIME IS: " + getStartMilitaryMinute());
                Log.e(PRODUCTION_TAG, "-------------------------------------------------------------");
                setNewNotificationDisplayAlarm(context,
                        alarmTimer.getCurrentSavedDayOfWeek(context.getApplicationContext()),alarmTimer);
            } else {
                displayNotification(context.getApplicationContext(), "DID YOU MISS YOUR SHIFT?");
            }
        }
    }

    //Added on 10 - 23 - 2019
    //A wrapper because I'm so drunk that it hurts to think correctly.
    @TargetApi(24)
    public void setNewNotificationDisplayAlarm(Context context, String day, AlarmTimer alarmTimer)
    {
        /*
         need to shift time to calculate the time time based on the minutes set
         before the start the start of the minutes
         */
        /*alarmTimer.setSavedAlarmTime(context,
                day,
                getStartMilitaryHour(),
                getStartMilitaryMinute(),
                true
        );
        */


        /*alarmTimer.setSavedAlarmTime(context,
                day,
                alarmTimer.getNewMilitaryHour(),
                alarmTimer.getNewMilitaryMinute(),
                true
        );
        */

        //Calendar cal = Calendar.getInstance();
        //if (alarmTimer.getNewAlarmMilitaryMinute(context) > cal.get(Calendar.MINUTE)) {
            displayNotification(alarmTimer, false, true,
                    "ALARM");
            setAlarm(context, alarmTimer);
        //}
        //br = new WorkAlarmReceiver();
        //intentFilter = new IntentFilter();
        //intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
        //intentFilter.addAction(Intent.ACTION_SEND);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //context.registerReceiver(br, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //context.unregisterReceiver(br);
    }

    @TargetApi(24)
    public void setAlarm(Context context, AlarmTimer alarmTimer) {

        //code copied from
        //https://developer.android.com/training/scheduling/alarms
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WorkAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        /*
          Time changes when the user changes the time before shift from something like
          20 minutes before the start of the shift to something like 10 minutes before
          the start of a shift.
         */
        calendar.set(Calendar.HOUR_OF_DAY, alarmTimer.getNewAlarmMilitaryHour(context));
        //calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, alarmTimer.getNewAlarmMilitaryMinute(context));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0); //??

        //alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
        //        1000 * 60 , alarmIntent);

        alarmMgr.setExact(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), alarmIntent);
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
    public String getEndAmOrPm() {
        return this.endAmOrPm;
    }
    //Added on 10 - 11 - 2019
    public String buildAlarmTimeFormatDisplay(AlarmTimer alarmTimer) {
        return new AlarmTimeFormatDisplay(context, alarmTimer, false).toString();
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

        DayNotification dayNotification = new DayNotification(context);
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
