package com.example.cd.workreminder;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class WorkAlarmReceiver extends BroadcastReceiver {

    private static final String PRODUCTION_TAG = "LG_WORK_PHONE"; //Added on 4 - 16 - 2019
    public static final String ACTION_DISMISS = "com.example.cd.workreminder.action.DISMISS";
    SharedPreferences pref;

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (ACTION_DISMISS.equals(action)) {
            Log.e(PRODUCTION_TAG, "THE INTENT ACTION IS: " + intent.getAction());
        }

        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        Calendar c=Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        //c.setTimeInMillis(SystemClock.elapsedRealtime());
        //Log.e(PRODUCTION_TAG, "THE ALARM TIME IN MILLISECONDS IS: " + intent.getLongExtra("MILLISECONDS", 0));
        //Log.e(PRODUCTION_TAG, "THE SYSTEM TIME SECONDS IS: " + (int)((System.currentTimeMillis()/1000)%3600));
        Log.e(PRODUCTION_TAG, "THE SYSTEM ELAPSED TIME TIME IS: " + SystemClock.elapsedRealtime());
        Log.e(PRODUCTION_TAG, "THE SYSTEM ALARM HOUR IS: " + c.get(Calendar.HOUR));
        Log.e(PRODUCTION_TAG, "THE SYSTEM ALARM MINUTES ARE: " + c.get(Calendar.MINUTE));
        Log.e(PRODUCTION_TAG, "THE ALARM HOUR is: " + pref.getInt("HOUR", 0));
        Log.e(PRODUCTION_TAG, "THE ALARM MINUTES ARE: " + pref.getInt("MINUTES", 0));
        //if (intent.getIntExtra("MINUTE", 0) == cal.get(Calendar.MINUTE)) {
        //if ( pref.getInt("MINUTES", 0) == c.get(Calendar.MINUTE)
        //        && pref.getInt("HOUR", 0) == c.get(Calendar.HOUR)) {
            Log.e(PRODUCTION_TAG, "ALARM RINGER GOT CALLED");

            //code copy and pasted from stackoverflow
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }

            Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes aa = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build();
                ringtone.setAudioAttributes(aa);
            } else {
                ringtone.setStreamType(AudioManager.STREAM_ALARM);
            }

            ringtone.play();
        }
    //}
}
