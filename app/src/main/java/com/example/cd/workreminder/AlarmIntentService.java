package com.example.cd.workreminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.concurrent.TimeUnit;

//Added on 10 - 8 - 2019. Handle snooze, dismiss
public class AlarmIntentService extends IntentService {
    private static final String TAG = "AlarmService";

    public static final String ACTION_DISMISS =
            "com.example.cd.workreminder.action.DISMISS";  //need to change
    public static final String ACTION_SNOOZE =
            "com.example.cd.workreminder.action.SNOOZE"; //need to change

    private static final long SNOOZE_TIME = TimeUnit.SECONDS.toMillis(5);

    public AlarmIntentService() {
        super("AlarmIntentService");
        Log.e(TAG, "ALARM INTENT SERVICE GOT CALLED");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "onHandleIntent(): " + intent);

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
    private void handleActionDismiss() {
        Log.e(TAG, "handleActionDismiss()");

        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.cancel(MainActivity.NOTIFICATION_ID);
    }


    /**
     * Handles action Snooze in the provided background thread.
     */
    private void handleActionSnooze() {
        Log.e(TAG, "handleActionSnooze()");

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
        notification = notificationCompatBuilder.build();

        if (notification != null) {
            NotificationManagerCompat notificationManagerCompat =
                    NotificationManagerCompat.from(getApplicationContext());

            notificationManagerCompat.cancel(MainActivity.NOTIFICATION_ID);

            try {
                Thread.sleep(SNOOZE_TIME);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
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
        //Intent mainIntent = new Intent(this, BigTextMainActivity.class);

        //PendingIntent mainPendingIntent =
        //        PendingIntent.getActivity(
        //                this,
        //                0,
        //                mainIntent,
        //                PendingIntent.FLAG_UPDATE_CURRENT
        //        );


        // 4. Create additional Actions (Intents) for the Notification.

        // Snooze Action.
        //Intent snoozeIntent = new Intent(this, BigTextIntentService.class);
        //snoozeIntent.setAction(BigTextIntentService.ACTION_SNOOZE);

        //PendingIntent snoozePendingIntent = PendingIntent.getService(this, 0, snoozeIntent, 0);
        //NotificationCompat.Action snoozeAction =
        //        new NotificationCompat.Action.Builder(
        //                R.drawable.ic_alarm_white_48dp,
        //                "Snooze",
        //                snoozePendingIntent)
        //                .build();


        // Dismiss Action
        //Intent dismissIntent = new Intent(this, BigTextIntentService.class);
        //dismissIntent.setAction(BigTextIntentService.ACTION_DISMISS);

        //PendingIntent dismissPendingIntent = PendingIntent.getService(this, 0, dismissIntent, 0);
        //NotificationCompat.Action dismissAction =
        //        new NotificationCompat.Action.Builder(
        //                R.drawable.ic_cancel_white_48dp,
        //                "Dismiss",
        //                dismissPendingIntent)
        //                .build();


        // 5. Build and issue the notification.

        String notificationChannelId = "";
        // Notification Channel Id is ignored for Android pre O (26).
        NotificationCompat.Builder notificationCompatBuilder =
                new NotificationCompat.Builder(
                        getApplicationContext(), notificationChannelId);

        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);

        /*notificationCompatBuilder
                .setStyle(bigTextStyle)
                .setContentTitle(bigTextStyleReminderAppData.getContentTitle())
                .setContentText(bigTextStyleReminderAppData.getContentText())
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(
                        getResources(),
                        R.drawable.ic_alarm_white_48dp))
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setCategory(Notification.CATEGORY_REMINDER)
                .setPriority(bigTextStyleReminderAppData.getPriority())
                .setVisibility(bigTextStyleReminderAppData.getChannelLockscreenVisibility())
                .addAction(snoozeAction)
                .addAction(dismissAction);
                */
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

