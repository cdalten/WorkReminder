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
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

//Added on 10 - 8 - 2019. Handle snooze, dismiss
public class AlarmIntentService extends IntentService {
    private static final String TAG = "AlarmService";

    public static final String ACTION_DISMISS =
            "com.example.cd.workreminder.action.DISMISS";
    public static final String ACTION_SNOOZE =
            "com.example.cd.workreminder.action.SNOOZE";

    private static final long SNOOZE_TIME = TimeUnit.SECONDS.toMillis(60); //Need to change
    //public static boolean amSnoozed = false; //Added on 12 - 22 - 2010

    private AlarmManager alarmMgr; //Added on 10 - 30- 2019
    private PendingIntent alarmIntent; //Added on 10 - 30 - 2019

    public AlarmIntentService() {
        super("AlarmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Log.e(TAG, "onHandleIntent(): " + intent)


        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DISMISS.equals(action)) {
                handleActionDismiss();
            } else if (ACTION_SNOOZE.equals(action)) {
                handleActionSnooze();
            }
        }
    }

    /**
     * Handles action Dismiss in the provided background thread.
     */
    @TargetApi(24)
    private void handleActionDismiss() {
        Log.e(TAG, "handleActionDismiss()");

        //Because some dumbass thought that hitting the dismiss button SHOULDN'T clear the notification

        if (CurrrentRingtoneInstance.getInstance().getArrayList().size() == 0) {

            NotificationCompat.Builder notificationCompatBuilder =
                    GlobalNotificationBuilder.getNotificationCompatBuilderInstance();

            Notification notification;
            notification = notificationCompatBuilder.build();

            if (notification != null) {
                NotificationManagerCompat notificationManagerCompat =
                        NotificationManagerCompat.from(getApplicationContext());

                notificationManagerCompat.cancel(MainActivity.NOTIFICATION_ID);
            }
        } else {
            Ringtone ringtone = (Ringtone) CurrrentRingtoneInstance.getInstance().getArrayList().get(0);
            ringtone.stop();

            NotificationCompat.Builder notificationCompatBuilder =
                    GlobalNotificationBuilder.getNotificationCompatBuilderInstance();

            Notification notification;
            notification = notificationCompatBuilder.build();

            if (notification != null) {
                NotificationManagerCompat notificationManagerCompat =
                        NotificationManagerCompat.from(getApplicationContext());

                notificationManagerCompat.cancel(MainActivity.NOTIFICATION_ID);
            }

        }
    }

    /**
     * Handles action Snooze in the provided background thread.
     */
    @TargetApi(24)
    private void handleActionSnooze() {
        Log.e(TAG, "handleActionSnooze()");

        AlarmTimer alarmTimer = AlarmTimer.getInstance();
        alarmTimer.setAlarmSnoooze((int)SNOOZE_TIME);
        //AlarmIntentService.amSnoozed = true;

        // You could use NotificationManager.getActiveNotifications() if you are targeting SDK 23
        // and above, but we are targeting devices with lower SDK API numbers, so we saved the
        // builder globally and get the notification back to recreate it later.
        NotificationCompat.Builder notificationCompatBuilder =
                GlobalNotificationBuilder.getNotificationCompatBuilderInstance();

        // Recreate builder from persistent state if app process is killed
        if (notificationCompatBuilder == null) {
            // Note: New builder set globally in the method
            notificationCompatBuilder = recreateBuilderWithBigTextStyle();
        }

        Notification notification;
        notification = notificationCompatBuilder.
                setContentTitle("ALARM GOT UPDATED").
                setContentText(new AlarmTimeFormatDisplay(this, alarmTimer, true).toString()).
                build();

        if (notification != null) {

            NotificationManagerCompat notificationManagerCompat =
                    NotificationManagerCompat.from(getApplicationContext());

            notificationManagerCompat.cancel(MainActivity.NOTIFICATION_ID);

            try {
                //AlarmIntentService.amSnoozed = true;
                Ringtone ringtone = (Ringtone) CurrrentRingtoneInstance.getInstance().getArrayList().get(0);
                ringtone.stop();
                Thread.sleep(SNOOZE_TIME);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }



            /*final Ringtone ringtone = (Ringtone) CurrrentRingtoneInstance.getInstance().getArrayList().get(0);
            Timer timer = new Timer();
            TimerTask timerTaskObj = new TimerTask() {
                @Override
                public void run() {
                    ringtone.stop();
                }

            };

            //timer.schedule(timerTaskObj, 0, 5000L);
            timer.scheduleAtFixedRate(timerTaskObj, 500, 5000);
            ringtone.play();
            */

            /*alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getApplicationContext(), WorkAlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            calendar.set(Calendar.HOUR_OF_DAY, alarmTimer.getNewAlarmMilitaryHour(getApplicationContext()));
            calendar.set(Calendar.MINUTE, alarmTimer.getNewAlarmMilitaryMinute(getApplicationContext()));
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0); //??

            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    1000*60 * SNOOZE_TIME
                    , alarmIntent);

            */

            //Ringtone ringtone = (Ringtone) CurrrentRingtoneInstance.getInstance().getArrayList().get(0);
            //ringtone.play();
            notificationManagerCompat.notify(MainActivity.NOTIFICATION_ID, notification);
        }

    }


    private NotificationCompat.Builder recreateBuilderWithBigTextStyle() {

        // Main steps for building a BIG_TEXT_STYLE notification (for more detailed comments on
        // building this notification, check StandaloneMainActivity.java):
        //      0. Get your data
        //      1. Retrieve Notification Channel for O and beyond devices (26+)
        //      2. Build the BIG_TEXT_STYLE
        //      3. Set up main Intent for notification
        //      4. Create additional Actions for the Notification
        //      5. Build and issue the notification

        // 0. Get your data (everything unique per Notification).

        //MockDatabase.BigTextStyleReminderAppData bigTextStyleReminderAppData =
        //        MockDatabase.getBigTextStyleData();

        // 1. Retrieve Notification Channel for O and beyond devices (26+). We don't need to create
        //    the NotificationChannel, since it was created the first time this Notification was
        //    created.
        //String notificationChannelId = bigTextStyleReminderAppData.getChannelId();

        // 2. Build the BIG_TEXT_STYLE.
        //BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
        //        .bigText(bigTextStyleReminderAppData.getBigText())
        //        .setBigContentTitle(bigTextStyleReminderAppData.getBigContentTitle())
        //        .setSummaryText(bigTextStyleReminderAppData.getSummaryText());


        // 3. Set up main Intent for notification.
        Intent mainIntent = new Intent(this, AlarmNotificationMainActivity.class);

        PendingIntent mainPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        // 4. Create additional Actions (Intents) for the Notification.

        // Snooze Action.
        Intent snoozeIntent = new Intent(this, AlarmIntentService.class);

        snoozeIntent.setAction(AlarmIntentService.ACTION_SNOOZE);

        PendingIntent snoozePendingIntent = PendingIntent.getService(this, 0, snoozeIntent, 0);
        NotificationCompat.Action snoozeAction =
                new NotificationCompat.Action.Builder(
                        R.drawable.ic_action_stat_reply,
                        "Snooze",
                        snoozePendingIntent)
                        .build();


        // Dismiss Action
        Intent dismissIntent = new Intent(this, AlarmIntentService.class);
        dismissIntent.setAction(AlarmIntentService.ACTION_DISMISS);

        PendingIntent dismissPendingIntent = PendingIntent.getService(this, 0, dismissIntent, 0);
        NotificationCompat.Action dismissAction =
                new NotificationCompat.Action.Builder(
                        R.drawable.ic_action_stat_reply,
                        "Dismiss",
                        dismissPendingIntent)
                        .build();


        // 5. Build and issue the notification.
        String notificationChannelId = "";
        // Notification Channel Id is ignored for Android pre O (26).
        NotificationCompat.Builder notificationCompatBuilder =
                new NotificationCompat.Builder(
                        getApplicationContext(), notificationChannelId);

        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);

        //Handle when app is killed. 10 - 29 - 2019

        notificationCompatBuilder
                //.setStyle(bigTextStyle)
                //.setContentTitle(bigTextStyleReminderAppData.getContentTitle())
                .setContentTitle("TITLE DEBUG MODE")
                //.setContentText(bigTextStyleReminderAppData.getContentText())
                .setContentText("ALARM INTENT SERVICE")
                .setSmallIcon(R.drawable.ic_stat_work)
                .setLargeIcon(BitmapFactory.decodeResource(
                        getResources(),
                        R.drawable.ic_stat_work))
                .setContentIntent(mainPendingIntent)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setCategory(Notification.CATEGORY_REMINDER)
                //.setPriority(bigTextStyleReminderAppData.getPriority())
                //.setVisibility(bigTextStyleReminderAppData.getChannelLockscreenVisibility())
                .addAction(snoozeAction)
                .addAction(dismissAction);


        /* REPLICATE_NOTIFICATION_STYLE_CODE:
         * You can replicate Notification Style functionality on Wear 2.0 (24+) by not setting the
         * main content intent, that is, skipping the call setContentIntent(). However, you need to
         * still allow the user to open the native Wear app from the Notification itself, so you
         * add an action to launch the app.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            // Enables launching app in Wear 2.0 while keeping the old Notification Style behavior.
            //NotificationCompat.Action mainAction = new NotificationCompat.Action.Builder(
            //        R.drawable.ic_launcher,
            //        "Open",
            //        mainPendingIntent)
            //        .build();

            //notificationCompatBuilder.addAction(mainAction);

        } else {
            // Wear 1.+ still functions the same, so we set the main content intent.
            //notificationCompatBuilder.setContentIntent(mainPendingIntent);
        }

        return notificationCompatBuilder;
    }
    }

