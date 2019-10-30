/*
Copyright 2016 The Android Open Source Project
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.example.cd.workreminder;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class AlarmNotificationMainActivity extends AppCompatActivity {

    private static Ringtone ringtone; //Added on 10 - 30 - 2019
    private static final String TAG = "LG_MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_notification_main);


        playRingtone();
        // Cancel Notification
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(MainActivity.NOTIFICATION_ID);
    }

    //Added on 10 - 30 - 2019
    private void playRingtone() {
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmUri);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            ringtone.setAudioAttributes(aa);
            Log.e(TAG, "GREATER THAN LOLLIPOP");
        } else {
            Log.e(TAG, "LESS THAN LOLLIPOP");
            ringtone.setStreamType(AudioManager.STREAM_ALARM);
        }

        Log.e(TAG, "THE RINGTONE INSTANCE (IN GLOBAL MAIN ACTIVITY) IS: " + ringtone);

    }
}//end class
