package com.example.cd.workreminder;
//package com.example.cd.shiftreminder;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class AlarmTimer extends AppCompatActivity {
    //private final String PRODUCTION_TAG = "LG_WORK_PHONE"; //Added on 4 - 17 - 2019
    private static Calendar cal;
    private SharedPreferences pref; //Added on 5 - 14 - 2019
    private int startMilitaryMinute; //Added on 6 - 27 -2019
    private int startMilitaryHour; //Added on 6 - 27 -2017
    private int listPosition; //Added on 10 - 18 - 2019

    private int endMilitaryHour = 0;
    private int endMilitaryMinute = 0;
    private int newMilitaryHour = 0;
    private int newMilitaryMinute = 0;
    private int timeBeforeShift = 0; //Added on 10 - 21 - 2019

    private static AlarmTimer instance = new AlarmTimer();

    private AlarmTimer() {

    }

    //Added on 6 - 13 - 2019
    public static AlarmTimer getInstance(){
        return instance;
    }

    @TargetApi(24)
    public void setAlarmTime(Context context,
                             int startMilitaryHour,
                             int startMilitaryMinute)
    {
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        this.startMilitaryMinute = startMilitaryMinute;
        this.startMilitaryHour = startMilitaryHour;

        if (timeBeforeShift < 60) {
            newMilitaryHour = startMilitaryHour;
            //endMilitaryMinute = timeBeforeShift;
            endMilitaryMinute = pref.getInt(context.getString(R.string.ALARM_MINUTES), WorkReaderContract.WorkEntry.ALARM_DEFAULT);
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
        cal.setTimeInMillis(System.currentTimeMillis()); //?
        //cal.set(Calendar.HOUR, newMilitaryHour);
        cal.set(Calendar.HOUR, newMilitaryHour);
        cal.set(Calendar.MINUTE, newMilitaryMinute);

        Log.e("LG_WORK_PHONE", "ALARM GOT CALLED");
        SharedPreferences.Editor editor = pref.edit();
        //editor.putInt("MILITARY_HOUR", cal.get(Calendar.HOUR)); //military. Do I need?
        editor.putInt("HOUR", cal.get(Calendar.HOUR)); // gets passed to alarm receiver
        editor.putInt("MINUTES", cal.get(Calendar.MINUTE));
        editor.apply();
    }//setAlarmTime

    //Added on 10 - 21 - 2019
    public void setMinutesBeforeShift(Context context, int minutesBeforeShift) {
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        //pref.getInt(context.getString(R.string.ALARM_MINUTES), WorkReaderContract.WorkEntry.ALARM_DEFAULT);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(context.getString(R.string.ALARM_MINUTES),minutesBeforeShift);
        editor.apply();
    }

    //Added on 10 - 23 - 2019
    public int getNewMilitaryHour() {
        return newMilitaryHour;
    }

    //Added on 10 - 23 - 2019
    public int getStartMilitaryMinute(){
        return newMilitaryMinute;
    }

    //Added on 10 - 23 - 2019
    public void setStartMilitaryHour(int startMilitaryHour) {
        this.startMilitaryHour = startMilitaryHour;
    }

    public int getMilitaryMinute() {
        return this.startMilitaryMinute;
    }

    public int getStartMilitaryHour() {
        return this.startMilitaryHour;
    }

    public int getUpdatedMinute() {
        return pref.getInt("MINUTES", 0);
    }

    public int getUpdatedHour() {
        return pref.getInt("HOUR", 0);
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