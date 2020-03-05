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

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;


//Used to set the Alarm ringone
public class AlarmRingtone {
    private static final String PRODUCTION_TAG = "LG_WORK_PHONE"; //Added on 4 - 16 - 2019
    private static AudioAttributes aa;
    private static SharedPreferences pref; //Added on 10 - 25 - 2019
    private static Ringtone ringtone; //Added on 10 - 25 - 2019
    private AlarmRingtone() {
    }

    public static void handleActionDismiss(Context context) {
        Log.e(PRODUCTION_TAG, "handleActionDismiss()");

        playRingtone(context);
        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(context.getApplicationContext());
        notificationManagerCompat.cancel(MainActivity.NOTIFICATION_ID);
    }

    public static void handleActionSnooze(Context context) {
        Log.d(PRODUCTION_TAG, "handleActionSnooze()");
        playRingtone(context);
    }

    //Added on 10 - 23 - 2019
    private static void playRingtone(Context context) {
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        //Part of the code copied and pasted from stackoverflow
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        if (pref.getBoolean("RINGTONE", false) == true) {
            ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), alarmUri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                aa = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build();

                ringtone.setAudioAttributes(aa);
                Log.e(PRODUCTION_TAG, "GREATER THAN LOLLIPOP");
            } else {
                Log.e(PRODUCTION_TAG, "LESS THAN LOLLIPOP");
                ringtone.setStreamType(AudioManager.STREAM_ALARM);
            }
        }

        Log.e(PRODUCTION_TAG, "THE RINGTONE INSTANCE IS: " + ringtone);

        if (pref.getBoolean("RINGTONE", false) == true) {
            ringtone.play();
        } else {
            ringtone.stop();
        }
    }
}//end class
