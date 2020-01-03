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
import android.app.Notification;
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
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;


public class WorkAlarmReceiver extends BroadcastReceiver {
    public static final String ACTION_DISMISS = "com.example.cd.workreminder.action.DISMISS";
    public static final String ACTION_SNOOZE = "com.example.cd.workreminder.action.SNOOZE";

    private DayNotificationWithSnooze dayNofiticationWithSnooze; //Added on 10 - 31 - 2019
    private SharedPreferences pref; //Added on 11 - 4 - 2019
    private static final long SNOOZE_TIME = TimeUnit.SECONDS.toMillis(60); //Need to change

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.e("PRODUCTION TAG", "WORK ALARM RECEIVER GOT CALLED WITH: " + intent.getAction());
        //Log.e("PRODUCTION TAG", "WORK ALARM RECEIVER GOT CALLED WITH: " + intent.getExtras());


        final PendingResult pendingResult = goAsync();
        Task asyncTask = new Task(context, pendingResult, intent);
        asyncTask.execute();

        //pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", context.MODE_PRIVATE);
        //MilitaryTime militaryTime = MilitaryTime.getInstance();
        //AlarmTimer alarmTimer = AlarmTimer.getInstance();

        //if (pref.getBoolean("ALARM_DISPLAY", true) ==true) {

            //dayNofiticationWithSnooze = new DayNotificationWithSnooze(context.getApplicationContext());
            //dayNofiticationWithSnooze.setNotificationDisplay(context.getApplicationContext(), militaryTime);
        //}

        /*
         Otherwise the alarm won't fire when the user changes the alarm time after the device after
         the phone reboots. More info on this can be found at...
         https://developer.android.com/training/scheduling/alarms
         */

         /*try {
                if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                    Toast.makeText(context, "Boot Detected.", Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {
                Log.e("PRODUCTION TAG", "BOOT ERROR IS: " + e);
            }
            */

        // i = new Intent(context, AlarmSound.class);
        //context.startService(i);
        //}

    }


    /*
      Recreate the original notification with the snooze button added. Any other way results in the
      GUI thread getting blocked for so long that the displayed alarm time might be off by a few
      minutes. This despite the fact that the alarmManager fires the alarm on time.
     */
    private static class Task extends AsyncTask<String, Integer, String> {

        private final PendingResult pendingResult;
        private final Intent intent;
        private Context context;

        private Task(Context context, PendingResult pendingResult, Intent intent) {
            this.pendingResult = pendingResult;
            this.intent = intent;
            this.context = context;
        }


        @Override
        protected String doInBackground(String... strings) {

            boolean amSnoozed = false;
            NotificationCompat.Builder notificationCompatBuilder =
                    GlobalNotificationBuilder.getNotificationCompatBuilderInstance();

            if (notificationCompatBuilder != null) {
                Notification notification = notificationCompatBuilder.build();

                /*
                 Unfinished because the meds are making me high. As a result, my concentration has gone
                 down like the Titanic -_-
                 */
                Intent snoozeIntent = new Intent(context, dayNotification.class);
                snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
                snoozeIntent.setAction(WorkAlarmReceiver.ACTION_SNOOZE);
                PendingIntent snoozePendingIntent = PendingIntent.getService(context, 0, snoozeIntent, 0);

                NotificationCompat.Action snoozeAction =
                        new NotificationCompat.Action.Builder(
                                R.drawable.ic_action_stat_reply,
                                "Snooze",
                                snoozePendingIntent)
                                .build();

                //****END UNFINISHED CODE *****************************************

                notificationCompatBuilder.addAction(snoozeAction).build();


                NotificationManagerCompat notificationManagerCompat =
                        NotificationManagerCompat.from(context.getApplicationContext());

                notificationManagerCompat.cancel(MainActivity.NOTIFICATION_ID);

                /*try {
                    amSnoozed = true;
                    Thread.sleep(SNOOZE_TIME);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }*/
                notificationManagerCompat.notify(MainActivity.NOTIFICATION_ID, notification);
            } else {
                Log.e("LG_WORK_PHONE", "CANT RECREATE THE NOTIFICATION");
            }

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Must call finish() so the BroadcastReceiver can be recycled.
            pendingResult.finish();
        }

    }

    }//end class
