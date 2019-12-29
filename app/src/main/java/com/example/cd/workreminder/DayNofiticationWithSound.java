package com.example.cd.workreminder;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class DayNofiticationWithSound extends AppCompatActivity{
    private Context context; //Added on 12 - 29 - 2019
    public DayNofiticationWithSound(Context context) {
        this.context = context;
    }

    public String buildAlarmTimeFormatDisplay(String dayOfWeek, int hour, int minute, String amOrPm) {
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
    public void displayNotification(AlarmTimer alarmTimer,
                                    boolean amSnoozed,
                                    boolean canDisplaySnoozeButton,
                                    String notificationTitle) {

        String notificationText = "";
        if (amSnoozed == false) {
            notificationText = buildAlarmTimeFormatDisplay(
                    alarmTimer.getDayOfWeek(context),
                    alarmTimer.getUpdatedHour(context),
                    alarmTimer.getUpdatedMinute(context),
                    alarmTimer.getUpdatedStartAmOrPm(context));
        } else {
            notificationText = buildAlarmTimeFormatDisplay(
                    alarmTimer.getDayOfWeek(context),
                    alarmTimer.getUpdatedHour(context),
                    alarmTimer.getUpdatedMinute(context) + alarmTimer.getAlarmSnooze(),
                    alarmTimer.getUpdatedStartAmOrPm(context));
        }


        Intent snoozeIntent = new Intent(context, AlarmIntentService.class);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        snoozeIntent.setAction(WorkAlarmReceiver.ACTION_SNOOZE);
        PendingIntent snoozePendingIntent = PendingIntent.getService(context, 0, snoozeIntent, 0);

        NotificationCompat.Action snoozeAction =
                new NotificationCompat.Action.Builder(
                        R.drawable.ic_action_stat_reply,
                        "Snooze",
                        snoozePendingIntent)
                        .build();

        Intent dismissIntent = new Intent(context, AlarmIntentService.class);
        dismissIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        dismissIntent.setAction(WorkAlarmReceiver.ACTION_DISMISS);
        PendingIntent dismissPendingIntent = PendingIntent.getService(context, 0, dismissIntent, 0);

        NotificationCompat.Action dismissAction =
                new NotificationCompat.Action.Builder(
                        R.drawable.ic_action_stat_share,
                        "Dismiss",
                        dismissPendingIntent)
                        .build();



        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(context, "0");
        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);
        notificationCompatBuilder.setSmallIcon(R.drawable.ic_stat_work)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(dismissPendingIntent)
                .addAction(snoozeAction)
                .addAction(dismissAction);
        notificationCompatBuilder.setContentTitle(notificationTitle);
        notificationCompatBuilder.setContentText(notificationText);
        NotificationManagerCompat mNotificationManagerCompat;
        mNotificationManagerCompat = NotificationManagerCompat.from(context);
        Notification notification = notificationCompatBuilder.build();
        mNotificationManagerCompat.notify(0, notification);
    }

}//End class
