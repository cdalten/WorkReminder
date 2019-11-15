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
        Log.e("LG_WORK: ", "DEBUG ALARM GOT CALLED");

        AlarmTimer alarmTimer =AlarmTimer.getInstance();
        dayNotification = new dayNotification(context);

        dayNotification.displayNotification(
                alarmTimer,
                "ALARM (ON RECEIVE)");


        //String alarmUri = intent.getStringExtra("ALARM_RINGTONE");
        //Uri uri = Uri.parse(alarmUri);
        //ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), uri);

        /*Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), alarmUri);
        //if (pref.getBoolean("RINGTONE", false) == true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            aa = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            ringtone.setAudioAttributes(aa);
        } else {
            ringtone.setStreamType(AudioManager.STREAM_ALARM);
        }
        //}

        //if (pref.getBoolean("RINGTONE", false) == true) {
        ringtone.play();
        */

        //Intent soundService = new Intent(context, SoundService.class);
        //soundService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.startActivity(soundService);
        //--------------------
        /*if (intent != null) {
            final String action = intent.getAction();
            if (intent.getStringExtra("ACTION_DISMISS").equals("ACTION_DISMISS")) {
                //abortBroadcast();
                handleActionDismiss(context);
            } else if (intent.getStringExtra("ACTION_SNOOZE").equals("ACTION_SNOOZE")) {
                handleActionSnooze();
            } else {


            }
        }
        */


        //context.startActivity(i);

        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", context.MODE_PRIVATE);


    }

    private void handleActionDismiss(Context context) {
        Log.e("LG WORK", "DISMISS GOT CALLED");
        /*SharedPreferences.Editor editor = pref.edit();
        editor = pref.edit();
        editor.putBoolean("RINGTONE", false);
        editor.apply();
        */

        /*Method[] m = ringtone.getClass().getMethods();

        for (int i = 0; i < m.length; i++) {
            if (m[i].getName().startsWith("stop")) {
                Log.e("AUDIO TAG", " THE METHODS ARE: " + m[i]);
                try {
                    m[i].invoke(m, null);
                } catch (Exception e) {
                    //pass
                }
            }//end if
        }//end for
        */

        AlarmManager aManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context.getApplicationContext(), WorkAlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent,  PendingIntent.FLAG_UPDATE_CURRENT);
        aManager.cancel(pIntent);

        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(context.getApplicationContext());
        notificationManagerCompat.cancel(MainActivity.NOTIFICATION_ID);
        //abortBroadcast();
    }

    private void handleActionSnooze() {
        Log.e("LG WORK", "SNOOZE GOT CALLED");
    }

    private void playRingtone() {

    }


}//end class
