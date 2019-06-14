package com.example.cd.workreminder;
//package com.example.cd.shiftreminder;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.lang.annotation.Target;

public class AlarmTimer extends AppCompatActivity {
    //private final String PRODUCTION_TAG = "LG_WORK_PHONE"; //Added on 4 - 17 - 2019
    private static Calendar cal;
    private SharedPreferences pref; //Added on 5 - 14 - 2019
    private static AlarmTimer instance = new AlarmTimer();

    private AlarmTimer() {

    }

    //Added on 6 - 13 - 2019
    public static AlarmTimer getInstance(){
        return instance;
    }

    @TargetApi(24)
    public void setAlarmTime(Context context, int startMilitaryHour, int startMilitaryMinute, int timeBeforeShift) {
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        int endMilitaryHour = 0;
        int endMilitaryMinute = 0;
        int newMilitaryHour = 0;
        int newMilitaryMinute = 0;

        if (timeBeforeShift < 60) {
            newMilitaryHour = startMilitaryHour;
            endMilitaryMinute = timeBeforeShift;
        }

        //newMilitaryHour = startMilitaryHour - endMilitaryHour;
        newMilitaryMinute = startMilitaryMinute - endMilitaryMinute;

        if (newMilitaryMinute < 0) {
            newMilitaryMinute = newMilitaryMinute + 60;
            newMilitaryHour = newMilitaryHour - 1;

        } else {
            newMilitaryHour = newMilitaryHour - endMilitaryHour;
        }

        cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, newMilitaryHour);
        cal.set(Calendar.MINUTE, newMilitaryMinute);

        Log.e("LG_WORK_PHONE", "ALARM GOT CALLED");
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("HOUR", cal.get(Calendar.HOUR)); //military
        editor.putInt("MINUTES", cal.get(Calendar.MINUTE));
        editor.commit();

        /*cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, newMilitaryHour);
        cal.set(Calendar.MINUTE, newMilitaryMinute);
        */

        //This part doesn't quite work. I *think* I need to get the URI??
        //Log.e("LG_WORK PHONE: ", "CAL MILLSECONDS ARE: " + cal.getTimeInMillis());
        //Log.e("LG_WORK PHONE: ", "SYSTEM MILLSECONDS ARE: " + System.currentTimeMillis());
        //if (cal.getTimeInMillis() == System.currentTimeMillis()) {
        /*Log.e("LG_WORK_PHONE", "ALARM GOT CALLED");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(AlarmTimer.this, WorkAlarmReceiver.class);
        intent.putExtra("HOUR_OF_DAY", cal.get(Calendar.HOUR_OF_DAY));
        //intent.putExtra("MINUTE", (cal.get(Calendar.MINUTE) - timeBeforeShift));
        intent.putExtra("MINUTE", newMilitaryMinute);
        intent.putExtra("MILLISECONDS", cal.getTimeInMillis());


        startActivity(intent);
        //context.sendBroadcast(intent);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
        //        AlarmManager.INTERVAL_DAY, pendingIntent);
        //if (true) {
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                1000 * 60 * 20, pendingIntent);
        //}
        //}
        */
    }//setAlarmTime

}//end class