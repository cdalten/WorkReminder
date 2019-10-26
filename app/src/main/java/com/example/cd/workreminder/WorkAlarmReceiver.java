package com.example.cd.workreminder;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;


public class WorkAlarmReceiver extends BroadcastReceiver {

    public static final String ACTION_SNOOZE = "com.example.cd.workreminder.action.SNOOZE";
    public static final String ACTION_DISMISS = "com.example.cd.workreminder.action.DISMISS";
    private static AudioAttributes aa;
    private SharedPreferences pref; //Added on 10 - 25 - 2019

    @Override
    public void onReceive(Context context, Intent intent) { ;
        final String action = intent.getAction();

        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        if (intent != null) {
            if (ACTION_DISMISS.equals(action)) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("RINGTONE", false);
                editor.apply();

                AlarmRingtone.handleActionDismiss(context);
            } else if (ACTION_SNOOZE.equals(action)) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("RINGTONE", true);
                editor.apply();

                AlarmRingtone.handleActionSnooze(context);
            }
        }

    }




}//end class
