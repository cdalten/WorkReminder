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
    private int startMilitaryMinute; //Added on 6 - 27 -2019
    private int startMilitaryHour; //Added on 6 - 27 -2017
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
        this.startMilitaryMinute = startMilitaryMinute;
        this.startMilitaryHour = startMilitaryHour;

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
        cal.set(Calendar.HOUR, newMilitaryHour);
        //cal.set(Calendar.HOUR_OF_DAY, newMilitaryHour); //24 hour
        cal.set(Calendar.MINUTE, newMilitaryMinute);

        Log.e("LG_WORK_PHONE", "ALARM GOT CALLED");
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("ALARM_HOUR", cal.get(Calendar.HOUR)); //military
        editor.putInt("HOUR", cal.get(Calendar.HOUR_OF_DAY)); // gets passed to alarm receiver
        editor.putInt("MINUTES", cal.get(Calendar.MINUTE));
        editor.apply();


    }//setAlarmTime

    public int getMilitaryMinute() {
        return this.startMilitaryMinute;
    }

    public int getStartMilitaryHour() {
        return this.startMilitaryHour;
    }

    //Added on 6 - 28 - 2019
    public String getAMorPM () {
        if (startMilitaryHour >= 12){
            return "PM";
        } else {
            return "AM";
        }
    }
}//end class