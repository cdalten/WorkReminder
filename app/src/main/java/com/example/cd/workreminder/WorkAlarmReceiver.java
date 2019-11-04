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


public class WorkAlarmReceiver extends BroadcastReceiver {
    public static final String ACTION_DISMISS = "com.example.cd.workreminder.action.DISMISS";
    public static final String ACTION_SNOOZE = "com.example.cd.workreminder.action.SNOOZE";

    private dayNotification dayNotification; //Added on 10 - 31 - 2019
    private SharedPreferences pref; //Added on 11 - 4 - 2019
    private Ringtone ringtone;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("LG_WORK: ", "DEBUG ALARM GOT CALLED");

        AlarmTimer alarmTimer =AlarmTimer.getInstance();
        dayNotification = new dayNotification(context);

        dayNotification.displayNotification(
                alarmTimer,
                "ALARM");

        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", context.MODE_PRIVATE);

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), alarmUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            ringtone.setAudioAttributes(aa);
            //Log.e(TAG, "GREATER THAN LOLLIPOP");
        } else {
            //Log.e(TAG, "LESS THAN LOLLIPOP");
            ringtone.setStreamType(AudioManager.STREAM_ALARM);
        }

        ringtone.play();

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DISMISS.equals(action)) {
                //SharedPreferences.Editor editor = pref.edit();
                //editor.putBoolean("RINGTONE", false);
                //editor.apply();

                handleActionDismiss(context);
            } else if (ACTION_SNOOZE.equals(action)) {
                //SharedPreferences.Editor editor = pref.edit();
                //editor.putBoolean("RINGTONE", true);
                //editor.apply();

                handleActionSnooze();
            }
        }

    }

    private void handleActionDismiss(Context context) {
        //playRingtone();
        ringtone.stop();
        //Log.e(TAG, "THE DISMISS RINGTONE INSTANCE IS: " + playRingtone());

        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(context.getApplicationContext());
        notificationManagerCompat.cancel(MainActivity.NOTIFICATION_ID);
    }

    private void handleActionSnooze() {

    }

    private void playRingtone() {

    }

}//end class
