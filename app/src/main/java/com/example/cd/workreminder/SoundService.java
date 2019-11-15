package com.example.cd.workreminder;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SoundService extends AppCompatActivity {
    private Ringtone ringtone;
    private AudioAttributes aa;

    public static final String ACTION_DISMISS = "com.example.cd.workreminder.action.DISMISS";
    public static final String ACTION_SNOOZE = "com.example.cd.workreminder.action.SNOOZE";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*String alarmUri = getIntent().getStringExtra("ALARM_RINGTONE");
        Uri uri = Uri.parse(alarmUri);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
        */
        playRingtone();
        //finish();
        //setResult(0, getIntent());
    }

    private void playRingtone() {
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
}//end class
