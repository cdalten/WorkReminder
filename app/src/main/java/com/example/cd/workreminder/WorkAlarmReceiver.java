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


public class WorkAlarmReceiver extends BroadcastReceiver {
    public static final String ACTION_DISMISS = "com.example.cd.workreminder.action.DISMISS";
    public static final String ACTION_SNOOZE = "com.example.cd.workreminder.action.SNOOZE";

    @Override
    public void onReceive(Context context, Intent intent) {


        Log.e("PRODUCTION TAG", "WORK ALARM RECEIVER GOT CALLED WITH: " + intent.getAction());
        Log.e("PRODUCTION TAG", "WORK ALARM RECEIVER GOT CALLED WITH: " + intent.getExtras());

        Log.e("LG_WORK_PHONE", "ONRECEIVE() GOT CALLED");

        /*
         Otherwise the alarm won't fire when the user changes the alarm time after the device after
         the phone reboots. More info on this can be found at...
         https://developer.android.com/training/scheduling/alarms
         */

        //Intent.ACTION_BOOT_COMPLETED
         /*try {
                if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                    //Toast.makeText(context, "Boot Detected.", Toast.LENGTH_LONG).show();
                    Log.e("LG_WORK_PHONE: ", "DEVICE BOOT DETECTED");

                } else {
                    Log.e("LG_WORK_PHONE", "DEVICE BOOT NOT DETECTED");
                }


         } catch (Exception e) {
             Log.e("LG_WORK_PHONE ", "BOOT ERROR IS: " + e);
         }*/



        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        DayNotification dayNotification = new DayNotification(context);
        dayNotification.setDisplaySnoozeButton(true);
        dayNotification.setNotificationAlarm(alarmUri,true); //pass uri or overload method?
        dayNotification.createNotification(dayNotification.getNotificationTitle(), dayNotification.getNotificationText());



    }
}//end class
