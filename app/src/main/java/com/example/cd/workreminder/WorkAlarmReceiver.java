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
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.lang.reflect.Method;


public class WorkAlarmReceiver extends BroadcastReceiver {
    public static final String ACTION_DISMISS = "com.example.cd.workreminder.action.DISMISS";
    public static final String ACTION_SNOOZE = "com.example.cd.workreminder.action.SNOOZE";

    private dayNotification dayNotification; //Added on 10 - 31 - 2019
    private SharedPreferences pref; //Added on 11 - 4 - 2019
    private Ringtone ringtone;
    private AudioAttributes aa;

    @Override
    public void onReceive(Context context, Intent intent) {
        //String alarmUri = intent.getStringExtra("ALARM_RINGTONE");
        //String alarmUri = intent.getExtras().get("ALARM_RINGTONE").toString();
        //Uri uri = Uri.parse(alarmUri);
        //ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), uri);

        //Uri uri = Uri.parse(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString());
        //ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), uri);

        //Log.e("WORK ALARM RECEIVER", "THE WORK ALARM RECEIVER INSTANCE IS: " + ringtone);
        //Log.e("LG_WORK: ", "DEBUG ALARM GOT CALLED");

        AlarmTimer alarmTimer = AlarmTimer.getInstance();

        dayNotification = new dayNotification(context);
        dayNotification.displayNotification(
                alarmTimer,
                "ALARM (ON RECEIVE)");


        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        //Intent i = new Intent(context, dayNotification.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.putExtra("ALARM_RINGTONE", alarmUri);
        //context.startActivity(i);

        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }


        //if (pref.getBoolean("RINGTONE", false) == true) {
        ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), alarmUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            ringtone.setAudioAttributes(aa);
        } else {
            ringtone.setStreamType(AudioManager.STREAM_ALARM);
        }
        //}

        //Log.e(TAG, "THE RINGTONE INSTANCE IS: " + ringtone);

        //if (pref.getBoolean("RINGTONE", false) == true) {
        ringtone.play();


    }




}//end class
