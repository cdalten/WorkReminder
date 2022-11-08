package com.example.cd.workreminder;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class DayNotification {
    private Context context; //Added on 1 - 6 - 2020
    public static final int NOTIFICATION_ID = 0; //Added on 10 - 14 - 2019
    private boolean displaySnooze = false; //Added on 1 - 7 - 2020
    private boolean notificationAlarm = false; //Added on 1 - 16 - 2020
    private boolean amPlaying = false; //Added on 1 - 31 - 202-
    private static String notificationTitle; //Added on 1 - 7 - 2020
    private static String notificationText; //Added on 1 - 7 - 2020
    private Uri alarmUri; //Added on 1 - 16 - 2020
    private Ringtone ringtone;
    private PendingIntent alarmIntent; //Added on 1 - 31 - 201
    private final String PRODUCTION_TAG = "DAY_NOTIFICATION";

    public  DayNotification() {}
    public DayNotification(Context context){
        this.context = context;
    }

    public DayNotification(Context context, PendingIntent alarmIntent){
        this.alarmIntent = alarmIntent;
        this.context = context;
    }

    public PendingIntent getAlarmIntent() {
        return alarmIntent;
    }

    public void createNotification(String notificationTitle, String notificationText ) {
        setNotificationTitle(notificationTitle);
        seteNotificationText(notificationText);
        Log.d(PRODUCTION_TAG, "createNotification() got called");
        //Unimplemented****************************************************************************
        Intent intent = new Intent(context, WorkPreferences.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, EXTRA_NOTIFICATION_ID)
                .setSmallIcon(R.drawable.ic_stat_work)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        //------------------------------------

        //Intent dismissIntent = new Intent(context, WorkAlarmReceiver.class);
        Intent dismissIntent = new Intent(context, AlarmIntentService.class);
        //dismissIntent.putExtra("ALARM_RINGTONE", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        dismissIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        dismissIntent.setAction(AlarmIntentService.ACTION_DISMISS);
        //context.sendBroadcast(dismissIntent);
        PendingIntent dismissPendingIntent = PendingIntent.getService(context, 0, dismissIntent, 0);

        NotificationCompat.Action dismissAction =
                new NotificationCompat.Action.Builder(
                        R.drawable.ic_action_stat_share,
                        "Dismiss",
                        dismissPendingIntent)
                        .build();

        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(context.getApplicationContext(), "0");
        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);
        notificationCompatBuilder.setSmallIcon(R.drawable.ic_stat_work)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(dismissPendingIntent)
                .addAction(dismissAction);

        if (getDisplaySnoozeButton() == true) {
            if (notificationAlarm == true ) { //play when the button with snooze first appears
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ringtone = RingtoneManager.getRingtone(context, alarmUri);

                    AudioAttributes aa = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build();


                    ringtone.setAudioAttributes(aa);
                    Log.d(PRODUCTION_TAG, "GREATER THAN LOLLIPOP");
                } else {
                    Log.d(PRODUCTION_TAG, "LESS THAN LOLLIPOP");
                    ringtone.setStreamType(AudioManager.STREAM_ALARM);
                }
                Log.e(PRODUCTION_TAG, "THE RINGTONE (IN GLOBAL MAIN ACTIVITY) IS: " + ringtone);


                if (getAmPlaying() == WorkReaderContract.ALARM_RINGS) {
                    CurrrentRingtoneInstance.getInstance().getArrayList().add(ringtone);
                    Ringtone ringtone = (Ringtone) CurrrentRingtoneInstance.getInstance().getArrayList().get(0);
                    ringtone.stop(); //stop previous ringtone
                    ringtone.play();
                }
            }

            Intent snoozeIntent = new Intent(context, AlarmIntentService.class);
            snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
            snoozeIntent.setAction(AlarmIntentService.ACTION_SNOOZE);

            PendingIntent snoozePendingIntent =
                    PendingIntent.getService(context, 0, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Action snoozeAction =
                    new NotificationCompat.Action.Builder(
                            R.drawable.ic_action_stat_reply,
                            "Snooze",
                            snoozePendingIntent)
                            .build();

            notificationCompatBuilder.setFullScreenIntent(snoozePendingIntent, true);
            notificationCompatBuilder.addAction(snoozeAction);
        }


        notificationCompatBuilder.setContentTitle(notificationTitle);
        notificationCompatBuilder.setContentText(notificationText);
        NotificationManagerCompat mNotificationManagerCompat;
        mNotificationManagerCompat = NotificationManagerCompat.from(context.getApplicationContext());
        Notification notification = notificationCompatBuilder.build();
        mNotificationManagerCompat.notify(0, notification);
    }


    //Added on 1 - 31 - 2020
    public void setAmPlaying(boolean amPlaying) {
        this.amPlaying = amPlaying;
    }

    public boolean getAmPlaying() {
        return this.amPlaying;
    }
    //Added on 1 - 16 - 2020
    public void setNotificationAlarm(Uri alarmUri) {
        this.alarmUri = alarmUri;
    }

    public void addAlarmNotificationRingtone(boolean notificationAlarm) {
        this.notificationAlarm =  notificationAlarm;
    }

    //Added on 1 - 7 - 2020
    public void setDisplaySnoozeButton(boolean displaySnooze) {
        this.displaySnooze = displaySnooze;
    }

    public boolean getDisplaySnoozeButton() {
        return this.displaySnooze;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationTitle() {
        return this.notificationTitle;
    }

    public void seteNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public String getNotificationText() {
        return this.notificationText;
    }

    public void updateDisplayTime(Context context) {
        final AlarmTimer alarmTimer = AlarmTimer.getInstance();
        AlarmTimeFormatDisplay alarmTimeFormatDisplay =  new AlarmTimeFormatDisplay(context, alarmTimer, true);
        createNotification("NEW ALARM TIME", alarmTimeFormatDisplay.displayCurrentTime() );
    }

}//end class
