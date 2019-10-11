package com.example.cd.workreminder;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class dayNotification extends AppCompatActivity {
    SharedPreferences pref;
    //Added on 7 - 1 - 2019
    @TargetApi(24)
    public int handleThirdShift() {
        Calendar cal = Calendar.getInstance();
        int currentDay = cal.get(Calendar.DAY_OF_WEEK); //vs inside if??
        //CurrentWorkHours currentWorkHours = new CurrentWorkHours();
        if (cal.get(Calendar.DAY_OF_WEEK) == java.util.Calendar.SUNDAY) {
            setNotification(
                    getString(R.string.SUNDAY),
                    getString(R.string.SUNDAY_START_HOUR),
                    getString(R.string.SUNDAY_START_MINUTE),
                    getString(R.string.SUNDAY_START_AM_OR_PM),
                    getString(R.string.SUNDAY_END_HOUR),
                    getString(R.string.SUNDAY_END_MINUTE),
                    getString(R.string.SUNDAY_END_AM_OR_PM),

                    getString(R.string.MONDAY_START_HOUR),
                    getString(R.string.MONDAY_START_MINUTE),
                    getString(R.string.MONDAY_START_AM_OR_PM)
            );
        } else if (cal.get(Calendar.DAY_OF_WEEK) == java.util.Calendar.MONDAY) {
            //if(!week.get(position).get(0).equals("OFF")) {
            setNotification(
                    getString(R.string.MONDAY),
                    getString(R.string.MONDAY_START_HOUR),
                    getString(R.string.MONDAY_START_MINUTE),
                    getString(R.string.MONDAY_START_AM_OR_PM),
                    getString(R.string.MONDAY_END_HOUR),
                    getString(R.string.MONDAY_END_MINUTE),
                    getString(R.string.MONDAY_END_AM_OR_PM),

                    getString(R.string.TUESDAY_START_HOUR),
                    getString(R.string.TUESDAY_START_MINUTE),
                    getString(R.string.TUESDAY_START_AM_OR_PM)
            );
        } else if (cal.get(Calendar.DAY_OF_WEEK) == java.util.Calendar.TUESDAY) {
            setNotification(
                    getString(R.string.TUESDAY),
                    getString(R.string.TUESDAY_START_HOUR),
                    getString(R.string.TUESDAY_START_MINUTE),
                    getString(R.string.TUESDAY_START_AM_OR_PM),
                    getString(R.string.TUESDAY_END_HOUR),
                    getString(R.string.TUESDAY_END_MINUTE),
                    getString(R.string.TUESDAY_END_AM_OR_PM),

                    getString(R.string.WEDNESDAY_START_HOUR),
                    getString(R.string.WEDNESDAY_START_MINUTE),
                    getString(R.string.WEDNESDAY_START_AM_OR_PM)
            );
        } else if (cal.get(Calendar.DAY_OF_WEEK) == java.util.Calendar.WEDNESDAY) {
            setNotification(
                    getString(R.string.WEDNESDAY),
                    getString(R.string.WEDNESDAY_START_HOUR),
                    getString(R.string.WEDNESDAY_START_MINUTE),
                    getString(R.string.WEDNESDAY_START_AM_OR_PM),
                    getString(R.string.WEDNESDAY_END_HOUR),
                    getString(R.string.WEDNESDAY_END_MINUTE),
                    getString(R.string.WEDNESDAY_END_AM_OR_PM),

                    getString(R.string.THURSDAY_START_HOUR),
                    getString(R.string.THURSDAY_START_MINUTE),
                    getString(R.string.THURSDAY_START_AM_OR_PM)
            );
        }else if (cal.get(Calendar.DAY_OF_WEEK) == java.util.Calendar.THURSDAY) {
            setNotification(
                    getString(R.string.THURSDAY),
                    getString(R.string.THURSDAY_START_HOUR),
                    getString(R.string.THURSDAY_START_MINUTE),
                    getString(R.string.THURSDAY_START_AM_OR_PM),
                    getString(R.string.THURSDAY_END_HOUR),
                    getString(R.string.THURSDAY_END_MINUTE),
                    getString(R.string.THURSDAY_END_AM_OR_PM),

                    getString(R.string.FRIDAY_START_HOUR),
                    getString(R.string.FRIDAY_START_MINUTE),
                    getString(R.string.FRIDAY_START_AM_OR_PM)
            );
        } else if (cal.get(Calendar.DAY_OF_WEEK) == java.util.Calendar.FRIDAY) {
            //if(!week.get(position).get(0).equals("OFF")) {
            setNotification(
                    getString(R.string.FRIDAY),
                    getString(R.string.FRIDAY_START_HOUR),
                    getString(R.string.FRIDAY_START_MINUTE),
                    getString(R.string.FRIDAY_START_AM_OR_PM),
                    getString(R.string.FRIDAY_END_HOUR),
                    getString(R.string.FRIDAY_END_MINUTE),
                    getString(R.string.FRIDAY_END_AM_OR_PM),

                    getString(R.string.SATURDAY_START_HOUR),
                    getString(R.string.SATURDAY_START_MINUTE),
                    getString(R.string.SATURDAY_START_AM_OR_PM)
            );
        }
        else if (cal.get(Calendar.DAY_OF_WEEK) == java.util.Calendar.SATURDAY) {
            setNotification(
                    getString(R.string.SATURDAY),
                    getString(R.string.SATURDAY_START_HOUR),
                    getString(R.string.SATURDAY_START_MINUTE),
                    getString(R.string.SATURDAY_START_AM_OR_PM),
                    getString(R.string.SATURDAY_END_HOUR),
                    getString(R.string.SATURDAY_END_MINUTE),
                    getString(R.string.SATURDAY_END_AM_OR_PM),

                    getString(R.string.SUNDAY_START_HOUR),
                    getString(R.string.SUNDAY_START_MINUTE),
                    getString(R.string.SUNDAY_START_AM_OR_PM)
            );
        }

        return currentDay;
    }

    //Added on 10 - 7 - 2019
    //Do I need the last three args in the function??
    @TargetApi(24)
    private void setNotification(
            String dayOfWeek,
            String dayOfWeekStartHour, String dayOfWeekStartMinute, String dayOfWeekStartAmOrPm,
            String dayOfWeekEndHour, String dayOfWeekEndMinute, String dayOfWeekEndAmOrPm,

            String endDayOfWeekStartHour, String endDayOfWeekStartMinute, String endDayOfWeekStartAmOrPm
    ) {
        Calendar cal = Calendar.getInstance();
        MilitaryTime militaryTime = MilitaryTime.getInstance();
        AlarmTimer alarmTimer = AlarmTimer.getInstance();
        pref = this.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);


        if (!(pref.getString(dayOfWeek, "OFF").equals("OFF"))) {
            int currentHour = cal.get(Calendar.HOUR);
            int currentMinute = cal.get(Calendar.MINUTE);

            //pref.getString( getString(R.string.FRIDAY_START_HOUR), "" );
            militaryTime.convertStartCivilianTimeToMilitaryTime(
                    pref.getString(dayOfWeekStartHour, WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                    pref.getString(dayOfWeekStartMinute, WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT),
                    pref.getString(dayOfWeekStartAmOrPm, WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));

            militaryTime.convertEndCivilianTimeToMilitaryTime(pref.getString(dayOfWeekEndHour, WorkReaderContract.WorkEntry.END_HOUR_DEFAULT),
                    pref.getString(dayOfWeekEndMinute, WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT),
                    pref.getString(dayOfWeekEndAmOrPm, WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));

            alarmTimer.setAlarmTime(this, militaryTime.getStartMilitaryHour(), militaryTime.getStartMilitaryMinute(),
                    pref.getInt(getString(R.string.ALARM_MINUTES), WorkReaderContract.WorkEntry.ALARM_DEFAULT));
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
                displayNotification( buildAlarmTimeFormatDisplay(dayOfWeek,
                        alarmTimer.getUpdatedHour(),
                        alarmTimer.getUpdatedMinute(),
                        alarmTimer.getAMorPM()),
                        "ALARM");
            }else {
                displayNotification( buildAlarmTimeFormatDisplay(dayOfWeek,
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
        Intent snoozeIntent = new Intent(this, WorkAlarmReceiver.class);
        //snoozeIntent.setAction(ACTION_SNOOZE);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(this, 0, snoozeIntent, 0);

        //NotificationCompat.Builder
        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(getApplicationContext(), "0");

        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);
        notificationCompatBuilder.setSmallIcon(R.drawable.ic_stat_work)
                //.setContentTitle("My notification")
                //.setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(snoozePendingIntent)
                .addAction(R.drawable.ic_action_stat_share,
                        getResources().getString(R.string.action_share), snoozePendingIntent);
        notificationCompatBuilder.setContentTitle(notificationTitle);
        notificationCompatBuilder.setContentText(notificationText);
        NotificationManagerCompat mNotificationManagerCompat;
        mNotificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        Notification notification = notificationCompatBuilder.build();
        mNotificationManagerCompat.notify(0, notification);
    }

}//end class
