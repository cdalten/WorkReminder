package com.example.cd.workreminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class AlarmSound extends IntentService{
    private Context context; //Added on 12 - 29 - 2019
    private Ringtone ringtone;
    private AudioAttributes aa;
    private SharedPreferences pref; //Added on 12  30 - 2019
    private final String PRODUCTION_TAG = "ALARM_SOUND";

    public AlarmSound() {
        super(" DayNofiticationWithSnooze");
    }

    public AlarmSound(Context context) {
        super(" DayNofiticationWithSnooze");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(PRODUCTION_TAG, "DayNofiticationWithSnooze onCreate()");
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmUri);
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

    }

    @Override
    public void onDestroy() {
        Log.d(PRODUCTION_TAG, "DayNofiticationWithSnooze onDestroy()");
        super.onDestroy();
    }
}//End class
