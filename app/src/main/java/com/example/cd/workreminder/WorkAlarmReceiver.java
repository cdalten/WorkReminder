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
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;


public class WorkAlarmReceiver extends BroadcastReceiver {

    private static final String PRODUCTION_TAG = "LG_WORK_PHONE"; //Added on 4 - 16 - 2019
    public static final String ACTION_SNOOZE = "com.example.cd.workreminder.action.SNOOZE";
    public static final String ACTION_DISMISS = "com.example.cd.workreminder.action.DISMISS";
    private Ringtone ringtone; //Added on 10 - 21 - 2019

    @Override
    public void onReceive(Context context, Intent intent) { ;
        final String action = intent.getAction();

        //ringtone = getRingtone(context);

        if (intent != null) {
            if (ACTION_DISMISS.equals(action)) {
                Log.e(PRODUCTION_TAG, "ACTION_DISMISS GOT CALLED");
                //ringtone.stop();
                handleActionDismiss(context);
            } else if (ACTION_SNOOZE.equals(action)) {
                Log.e(PRODUCTION_TAG, "ACTION_SNOOZE GOT CALLED");
                //ringtone.play();
                handleActionSnooze(context);
            }
        }

        Log.e(PRODUCTION_TAG, "THE ACTION IS: " + action);
    }

    //Added on 10 - 21 - 2019
    //Method ripped of from...
    //https://github.com/googlearchive/android-Notifications/blob/master/Wearable/src/main/java/com/example/android/wearable/wear/wearnotifications/handlers/BigTextIntentService.java
    private void handleActionDismiss(Context context) {
        Log.e(PRODUCTION_TAG, "handleActionDismiss()");
        //getRingtone(context).stop(); //Because cancel doesn't mean cancel.

        //RingtoneManager ringMan = new RingtoneManager(context.getApplicationContext());
        //ringMan.stopPreviousRingtone();

        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(context.getApplicationContext());
        notificationManagerCompat.cancel(MainActivity.NOTIFICATION_ID);
    }

    private void handleActionSnooze(Context context) {
        Log.d(PRODUCTION_TAG, "handleActionSnooze()");
        getRingtone(context).play();
    }

    //Added on 10 - 23 - 2019
    private Ringtone getRingtone(Context context) {
        //Part of the code copied and pasted from stackoverflow
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        Ringtone ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), alarmUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            ringtone.setAudioAttributes(aa);
        } else {
            ringtone.setStreamType(AudioManager.STREAM_ALARM);
        }
        return ringtone;
    }
}//end class
