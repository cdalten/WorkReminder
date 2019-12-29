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
    private int endMilitaryHour = 0;
    private int endMilitaryMinute = 0;
    private int newMilitaryHour = 0;
    private int newMilitaryMinute = 0;
    private int timeBeforeShift = 0; //Added on 10 - 21 - 2019
    private String dayOfWeek; //Added on 11 - 4 - 2019
    private String updatedAmOrPm = ""; //Added on 12 - 18 - 2019
    private int snoozeTime = 5; //5 minute default. Added on 12 - 20 - 2019
    private int milliSecondsToMinutes = 60000; //Added on 12 - 22 - 2019
    private String currenDayOfWeek = ""; //Added on 12 - 27 - 2019
    private String previousDayOfWeek = "";

    private int currentEndMilitaryMinute = 0; //Added on 12 - 29 - 2019

    private static AlarmTimer instance = new AlarmTimer();

    private AlarmTimer() {

    }

    //Added on 6 - 13 - 2019
    public static AlarmTimer getInstance(){
        return instance;
    }

    public void setSavedAlarmTime(Context context,
                             String dayOfWeek,
                             int startMilitaryHour,
                             int startMilitaryMinute,
                             boolean shouldISaveTheAlarmTime )
    {
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        this.dayOfWeek = dayOfWeek;
        this.startMilitaryMinute = startMilitaryMinute;
        this.startMilitaryHour = startMilitaryHour;
        updatedAmOrPm = getAMorPM(startMilitaryHour);

        /*
          The algorithm was taken from a math book that I got from an Elementary School teacher
          in Oakland. This code is a direct translation found in the 4th grade book.
         */
        if (timeBeforeShift < 60) {
            newMilitaryHour = startMilitaryHour;
            //endMilitaryMinute = timeBeforeShift;
            endMilitaryMinute = pref.getInt(context.getString(R.string.ALARM_MINUTES), WorkReaderContract.WorkEntry.ALARM_DEFAULT);
        }

        //newMilitaryHour = startMilitaryHour - endMilitaryHour;
        //newMilitaryMinute = startMilitaryMinute - endMilitaryMinute;
        newMilitaryMinute = startMilitaryMinute - pref.getInt(context.getString(R.string.ALARM_MINUTES), WorkReaderContract.WorkEntry.ALARM_DEFAULT);

        if (newMilitaryMinute < 0) {
            newMilitaryMinute = newMilitaryMinute + 60;
            newMilitaryHour = newMilitaryHour - 1;
        } else {
            newMilitaryHour = newMilitaryHour - endMilitaryHour;
        }

        saveAlarmTime(true);

    }//setAlarmTime

    //Added on 12 - 16 - 2019
    @TargetApi(24)
    private void saveAlarmTime(boolean shouldISaveTheAlarmTime) {
        cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis()); //?
        //cal.set(Calendar.HOUR, newMilitaryHour);
        cal.set(Calendar.HOUR, newMilitaryHour);
        cal.set(Calendar.MINUTE, newMilitaryMinute);

        Log.e("LG_WORK_PHONE", "ALARM GOT CALLED");
        SharedPreferences.Editor editor = pref.edit();
        //editor.putInt("MILITARY_HOUR", cal.get(Calendar.HOUR)); //military. Do I need?
        editor.putString("DAY_OF_WEEK", dayOfWeek);
        editor.putInt("HOUR", cal.get(Calendar.HOUR)); // gets passed to alarm receiver
        editor.putInt("MINUTES", cal.get(Calendar.MINUTE));
        editor.apply();
    }

    //Added on 10 - 21 - 2019
    public void setMinutesBeforeShift(Context context, int minutesBeforeShift) {
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        //pref.getInt(context.getString(R.string.ALARM_MINUTES), WorkReaderContract.WorkEntry.ALARM_DEFAULT);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(context.getString(R.string.ALARM_MINUTES),minutesBeforeShift);
        editor.apply();
    }


    //Added on 12 - 27 - 2019
    //Just a lame attempt at database emulation, since like, this app doesn't use a database.
    void saveCurrentDayOfWeek(Context context, String currentDayOfWeek){
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("CURRENT_DAY", currentDayOfWeek);
        editor.apply();
    }

    public String getCurrentSavedDayOfWeek(Context context) {
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        return pref.getString("CURRENT_DAY", "");
    }

    void savePreviousDayOfWeek(Context context, String previousDayOfWeek){
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("PREVIOUS_DAY", previousDayOfWeek);
        editor.apply();
    }

    public String getPreviousDayOfWeekSavedDayOfWeek(Context context) {
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        return pref.getString("PREVIOUS_DAY", "");
    }

    //Added on 12 - 20 - 2019
    public void setAlarmSnoooze(int snoozeTime) {
        this.snoozeTime = snoozeTime/milliSecondsToMinutes;
    }

    public int getAlarmSnooze() {
        return this.snoozeTime;
    }

    //Added on 10 - 23 - 2019
    public int getNewMilitaryHour() {
        return newMilitaryHour;
    }

    //Added on 10 - 23 - 2019
    public int getNewMilitaryMinute(){
        return newMilitaryMinute;
    }

    //Added on 10 - 23 - 2019
    public void setStartMilitaryHour(int startMilitaryHour) {
        this.startMilitaryHour = startMilitaryHour;
    }

    public void setStartMilitaryMinute(int startMilitaryMinute) {
        this.startMilitaryMinute = startMilitaryMinute;
    }

    public int getMilitaryMinute() {
        return this.startMilitaryMinute;
    }

    public int getStartMilitaryHour() {
        return this.startMilitaryHour;
    }

    public int getStartMilitaryMinute() {
        return this.startMilitaryMinute;
    }

    //Added on 12 - 29 - 2019
    void saveCurrentEndMilitaryHour(Context context, String currentEndMilitaryHour) {
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("CURRENT_MILITARY_HOUR", currentEndMilitaryHour);
        editor.apply();
    }

    public String getCurrentEndMilitaryHour(Context context){
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        return pref.getString("CURRENT_MILITARY_HOUR", "");
    }

    //Needed if the device reboots
    public int getUpdatedMinute(Context context) {
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        return pref.getInt("MINUTES", 0);
    }

    //ditto
    public int getUpdatedHour(Context context) {
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        return pref.getInt("HOUR", 0);
    }

    //Added on 12 - 18 - 2019
    public String getUpdatedStartAmOrPm(Context context) {
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        return pref.getString("AMORPM", updatedAmOrPm);
    }

    //Added on 11 - 4 - 2019
    public String getDayOfWeek(Context context) {
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        return pref.getString("DAY_OF_WEEK", "");
    }

    public int getMinutesBeforeShift(Context context) {
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        return pref.getInt(context.getString(R.string.ALARM_MINUTES), 0);
    }

    //Added on 6 - 28 - 2019
    private String getAMorPM (int startMilitaryHour) {
        /*
          When the alarm 12:00 PM, and the alarm is set 20 minutes before the shift,
          we want it to display 11:40 AM.
         */
        if (startMilitaryHour == 12) {
            return "AM";
        }

        //Same thing, except military time runs from 0 to 23
        if (startMilitaryHour == 0) {
            return "PM";
        }

        if (startMilitaryHour > 12){
            return "PM";
        } else {
            return "AM";
        }
    }


}//end class