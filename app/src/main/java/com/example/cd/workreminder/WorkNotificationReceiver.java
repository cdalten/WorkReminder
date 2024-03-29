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

import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;


/*
  I keep the display notification in a different receiver because the receiver doesn't have
  that much time complete it's task when it calls the alarm. In other words, th is is a lame
  attempt at avoid race conditions.
 */
public class WorkNotificationReceiver extends BroadcastReceiver {

    private final String PRODUCTION_TAG = "WORK_RECEIVER: ";
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(PRODUCTION_TAG, "BROADCAST RECEIVER GOT CALLED");
        if (intent.getBooleanExtra("AMCONNECTED", false) == true) {
            Log.d(PRODUCTION_TAG, "AM CONNECTED GOT CALLED");
            intent = new Intent(context, AlarmNotification.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }

        Log.d(PRODUCTION_TAG, "WORK ALARM RECEIVER GOT CALLED WITH: " + intent.getAction());
        Log.d(PRODUCTION_TAG, "ONRECEIVE() GOT CALLED");

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        DayNotification dayNotification = new DayNotification(context);
        dayNotification.setDisplaySnoozeButton(true);
        dayNotification.setNotificationAlarm(alarmUri);
        dayNotification.addAlarmNotificationRingtone(WorkReaderContract.ALARM_NOTIFICATION_RINGS);
        dayNotification.setAmPlaying(WorkReaderContract.ALARM_RINGS);
        dayNotification.createNotification(dayNotification.getNotificationTitle(), dayNotification.getNotificationText());

        dayNotification.setNotificationAlarm(alarmUri);
        dayNotification.addAlarmNotificationRingtone(WorkReaderContract.ALARM_NOTIFICATION_SILENT);
        //dayNotification.updateDisplayNotification(context);


    }
}//end class
