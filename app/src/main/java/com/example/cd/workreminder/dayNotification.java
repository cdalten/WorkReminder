package com.example.cd.workreminder;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class dayNotification extends AppCompatActivity {
    SharedPreferences pref;
    Context context; //Added on 10 - 14 - 2019

    public dayNotification(Context context) {
        this.context = context;
    }

    //Added on 7 - 1 - 2019
    //Schizophrenic method call ripped off from the Android Notification source code -)
    @TargetApi(24)
    public int handleThirdShift() {
        Log.e("NOTIFICATION TAG", "handleThirdShift()");
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
    ) {
        Calendar cal = Calendar.getInstance();
        MilitaryTime militaryTime = MilitaryTime.getInstance();
        Log.e("NOTIFICATION TAG", "2)SET NOTIFICATION GOT CALLED BEFORE ALARM TIMER");
        AlarmTimer alarmTimer = AlarmTimer.getInstance();
        Log.e("NOTIFICATION TAG", "3)SET NOTIFICATION GOT CALLED AFTER ALARM TIMER");

        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);


        if (!(pref.getString(context.getString(dayOfWeek), "OFF").equals("OFF"))) {
            int currentHour = cal.get(Calendar.HOUR);
            int currentMinute = cal.get(Calendar.MINUTE);
            //pref.getString( getString(R.string.FRIDAY_START_HOUR), "" );
            militaryTime.convertStartCivilianTimeToMilitaryTime(
                    pref.getString(context.getString(dayOfWeekStartHour), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                    pref.getString(context.getString(dayOfWeekStartMinute), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT),
                    pref.getString(context.getString(dayOfWeekStartAmOrPm), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));

            militaryTime.convertEndCivilianTimeToMilitaryTime(
                    pref.getString(context.getString(dayOfWeekEndHour), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT),
                    pref.getString(context.getString(dayOfWeekEndMinute), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT),
                    pref.getString(context.getString(dayOfWeekEndAmOrPm), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));

            alarmTimer.setAlarmTime(context, militaryTime.getStartMilitaryHour(), militaryTime.getStartMilitaryMinute(),
                    pref.getInt(context.getString(R.string.ALARM_MINUTES), WorkReaderContract.WorkEntry.ALARM_DEFAULT));
            if (currentHour > militaryTime.getStartMilitaryHour() && currentHour < militaryTime.getEndMilitaryHour()) {
                displayNotification("YOU'RE SUPPOSED TO BE AT WORK", "");
            } else if (currentHour == militaryTime.getStartMilitaryHour()) {
                //setCurrentAlarm(militaryTime.getStartMilitaryHour(), militaryTime.getEndMilitaryMinute());
                if (currentMinute > militaryTime.getStartMilitaryMinute()) {
                    displayNotification("YOU'RE SUPPOSED TO BE AT WORK", "");
                }
            } else if (currentHour == militaryTime.getEndMilitaryHour()) {
                if (currentMinute < militaryTime.getEndMilitaryMinute()) {
                    displayNotification("YOU'RE SUPPOSED TO BE AT WORK", "");
                }
            } else if (  pref.getInt("ALARM_HOUR", 0) == 0) {
                displayNotification( buildAlarmTimeFormatDisplay(context.getString(dayOfWeek),
                        alarmTimer.getUpdatedHour(),
                        alarmTimer.getUpdatedMinute(),
                        alarmTimer.getAMorPM()),
                        "ALARM");
            }else {
                Log.e("NOTIFICATION TAG", "BEFORE DISPLAY NOTIFICATION GOT CALLED");
                displayNotification( buildAlarmTimeFormatDisplay(context.getString(dayOfWeek),
                        alarmTimer.getUpdatedHour(),
                        alarmTimer.getUpdatedMinute(),
                        alarmTimer.getAMorPM()),
                        "ALARM");
            }

        }
    }

    //Added on 10 - 11 - 2019
    private String buildAlarmTimeFormatDisplay(String dayOfWeek, int hour, int minute, String amOrPm) {
        String timeFormat = "";

        //Something like 1:5 becomes while 1:05 while something like 1:10 stays 1:10
        if (minute < 10) {
            timeFormat = dayOfWeek + " " + hour + ":0" + minute + " " + amOrPm;
        } else {
            timeFormat = dayOfWeek + " " + hour + ":" + minute + " " + amOrPm;
        }

        return timeFormat;
    }

    //Added on 10 - 7 - 2019
    public void displayNotification(String notificationText, String notificationTitle) {
        Intent dismissIntent = new Intent(context, WorkAlarmReceiver.class);
        //snoozeIntent.setAction(ACTION_SNOOZE);
        dismissIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        dismissIntent.setAction(WorkAlarmReceiver.ACTION_DISMISS);
        Log.e("NOTIFICATION TAG", "4)ACTION DISMISS GOT CALLED");
        PendingIntent dismissPendingIntent =
                PendingIntent.getBroadcast(context, 0, dismissIntent, 0);
        Log.e("NOTIFICATION TAG", "5)BROADCAST CALLED");

        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(context.getApplicationContext(), "0");

        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);
        notificationCompatBuilder.setSmallIcon(R.drawable.ic_stat_work)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(dismissPendingIntent)
                .addAction(R.drawable.ic_action_stat_share,
                        context.getResources().getString(R.string.action_share), dismissPendingIntent);
        notificationCompatBuilder.setContentTitle(notificationTitle);
        notificationCompatBuilder.setContentText(notificationText);
        NotificationManagerCompat mNotificationManagerCompat;
        mNotificationManagerCompat = NotificationManagerCompat.from(context.getApplicationContext());
        Notification notification = notificationCompatBuilder.build();
        mNotificationManagerCompat.notify(0, notification);
    }

}//end class
