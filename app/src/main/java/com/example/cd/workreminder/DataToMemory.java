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
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/*
  Now before some jackass genius makes a comment about how this might be a bad design decision...
  I put *all* the save preference methods in one class, that way, if I make a change to the
  application context, I don't have to do this across a few different files. This might not sound right
  in the short term. But I don't always remember what and where I put one a few months later. By going
  this route, all this is one spot. And thus, I can avoid anykind of memory leaks that the
  over engineered POS IDE can't detect.

  Also I do this if I want to change the backend from storing in a file, to say, maybe either a
  database or the cloud.

  You can thank the brains at Google for this crap.
 */
public class DataToMemory extends AppCompatActivity{
    private SharedPreferences pref; //Added on 1 - 13 - 2019
    private Context context; //Added on 1 - 14 - 2019

    public DataToMemory(Context context) {
        this.context = context;
        pref =  context.getSharedPreferences(WorkReaderContract.WorkEntry.SAVED_PREFERENCESS, MODE_PRIVATE);
    }    //From CurrentWeekSchedule.java
    public void saveDataToMemory(
            int day,
            int startHour,
            int startMinute,
            int startAmOrPm,
            int endHour,
            int endMinute,
            int endAmOrPM,

            String dayOnOrOff,
            String newStartHour,
            String newStartMinute,
            String newStartAmOrPm,
            String newEndHour,
            String newEndMinute,
            String newEndAmOrPm

    )
    {
        //pref =  this.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if (!dayOnOrOff.equals(WorkReaderContract.WorkEntry.DAY_OFF_DEFAULT)) {
            editor.putString(context.getString(day), context.getString(day));
            editor.putString(context.getString(startHour), newStartHour);
            editor.putString(context.getString(startMinute), newStartMinute);
            editor.putString(context.getString(startAmOrPm), newStartAmOrPm);
            editor.putString(context.getString(endHour), newEndHour);
            editor.putString(context.getString(endMinute), newEndMinute);
            editor.putString(context.getString(endAmOrPM), newEndAmOrPm);
        } else {
            editor.putString(context.getString(day), WorkReaderContract.WorkEntry.DAY_OFF_DEFAULT);
            editor.putString(context.getString(startHour), newStartHour);
            editor.putString(context.getString(startMinute), newStartMinute);
            editor.putString(context.getString(startAmOrPm), newStartAmOrPm);
            editor.putString(context.getString(endHour), newEndHour);
            editor.putString(context.getString(endMinute), newEndMinute);
            editor.putString(context.getString(endAmOrPM), newEndAmOrPm);
        }

        editor.apply();
    }

    public void saveDataOnRotation(Bundle outState,
                                    String key,
                                    int day,
                                    int startHour,
                                    int startMinute,
                                    int startAmOrPM,
                                    int endHour,
                                    int endMinute,
                                    int endAmOrPM)
    {
        //pref =  this.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        outState.putString(key,  pref.getString(context.getString(day), context.getString(day)));
        outState.putString(context.getString(startHour),
                pref.getString(context.getString(startHour), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
        outState.putString(context.getString(startMinute),
                pref.getString(context.getString(startMinute), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
        outState.putString(context.getString(startAmOrPM),
                pref.getString(context.getString(startAmOrPM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
        outState.putString(context.getString(endHour),
                pref.getString(context.getString(endHour), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
        outState.putString(context.getString(endMinute),
                pref.getString(context.getString(endMinute), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
        outState.putString(context.getString(endAmOrPM),
                pref.getString(context.getString(endAmOrPM), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));

        editor.apply();
    }

    //From AlarmTimer.java
    //Added on 12 - 16 - 2019
    @TargetApi(24)
    public void saveCivilianAlarmTime(
            String dayOfWeek,
            int newMilitaryHour,
            int newMilitaryMinute,
            boolean shouldISaveTheAlarmTime) {

        //pref =  this.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis()); //?
        //cal.set(Calendar.HOUR, newMilitaryHour);
        cal.set(Calendar.HOUR, newMilitaryHour);
        cal.set(Calendar.MINUTE, newMilitaryMinute);

        Log.e("LG_WORK_PHONE", "ALARM GOT CALLED");
        //editor.putInt("MILITARY_HOUR", cal.get(Calendar.HOUR)); //military. Do I need?
        editor.putString("DAY_OF_WEEK", dayOfWeek);
        editor.putInt("NEW_ALARM_HOUR", cal.get(Calendar.HOUR)); // gets passed to alarm receiver
        editor.putInt("NEW_ALARM_MINUTES", cal.get(Calendar.MINUTE));
        editor.apply();
    }


    public int getNewAlarmCivilianHour(Context context) {
        pref = context.getSharedPreferences(WorkReaderContract.WorkEntry.SAVED_PREFERENCESS, MODE_PRIVATE);
        return pref.getInt("NEW_ALARM_HOUR", 0);
    }

    public int getNewAlarmCivilianMinutes(Context context) {
        pref = context.getSharedPreferences(WorkReaderContract.WorkEntry.SAVED_PREFERENCESS, MODE_PRIVATE);
        return pref.getInt("NEW_ALARM_MINUTES", 0);
    }

    public void saveNewAlarmMilitaryHour(Context context, int newMilitaryHour) {
        //pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        //pref.getInt(context.getString(R.string.ALARM_MINUTES), WorkReaderContract.WorkEntry.ALARM_DEFAULT);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("NEW_MILITARY_ALARM_HOUR", newMilitaryHour);
        editor.apply();
    }

    public int getNewAlarmMilitaryHour(Context context) {
        pref = context.getSharedPreferences(WorkReaderContract.WorkEntry.SAVED_PREFERENCESS, MODE_PRIVATE);
        return pref.getInt("NEW_MILITARY_ALARM_HOUR", 0);
    }

    public void saveNewAlarmMilitaryMinute(Context context, int newMilitaryMinute) {
        //pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        //pref.getInt(context.getString(R.string.ALARM_MINUTES), WorkReaderContract.WorkEntry.ALARM_DEFAULT);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("NEW_ALARM_MINUTES",newMilitaryMinute);
        editor.apply();
    }

    public int getNewAlarmMilitaryMinute(Context context) {
        pref = context.getSharedPreferences(WorkReaderContract.WorkEntry.SAVED_PREFERENCESS, MODE_PRIVATE);
        return pref.getInt("NEW_ALARM_MINUTES", 0);
    }
    //Added on 10 - 21 - 2019
    public void saveMinutesBeforeShift(int minutesBeforeShift) {
        //pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        //pref.getInt(context.getString(R.string.ALARM_MINUTES), WorkReaderContract.WorkEntry.ALARM_DEFAULT);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(context.getString(R.string.ALARM_MINUTES),minutesBeforeShift);
        editor.apply();
    }

    public void saveHoursBeforeShift(int hoursBeforeShift) {
        //pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        //pref.getInt(context.getString(R.string.ALARM_MINUTES), WorkReaderContract.WorkEntry.ALARM_DEFAULT);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("ALARM_HOUR",hoursBeforeShift);
        editor.apply();
    }
    //Added on 12 - 27 - 2019
    //Just a lame attempt at database emulation, since like, this app doesn't use a database.
    void saveCurrentDayOfWeek(Context context, String currentDayOfWeek){
        //pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("CURRENT_DAY", currentDayOfWeek);
        editor.apply();
    }

    public String getCurrentSavedDayOfWeek(Context context) {
        pref = context.getSharedPreferences(WorkReaderContract.WorkEntry.SAVED_PREFERENCESS, MODE_PRIVATE);
        return pref.getString("CURRENT_DAY", "");
    }

    void savePreviousDayOfWeek(Context context, String previousDayOfWeek){
        //pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("PREVIOUS_DAY", previousDayOfWeek);
        editor.apply();
    }


    public void saveCurrentEndMilitaryHour(Context context, int currentEndMilitaryHour) {
        //pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("CURRENT_MILITARY_HOUR", currentEndMilitaryHour);
        editor.apply();
    }

    public int getCurrentEndMilitaryHour(Context context){
        //pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        return pref.getInt("CURRENT_MILITARY_HOUR", 0);
    }

    public void saveCurrentEndMilitaryMinute(Context context, int currentEndMilitaryMinute) {
        //pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("CURRENT_MILITARY_MINUTE", currentEndMilitaryMinute);
        editor.apply();
    }

    public int getCurrentEndMilitaryMinute(Context context){
        //pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        return pref.getInt("CURRENT_MILITARY_MINUTE", 0);
    }

    //from TimePickerFragment
    public void saveCurrentCivilianHour(Context context,
                                        String civlianHour,
                                        int currentCivilianHour) {
        //pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(civlianHour, currentCivilianHour);
        editor.apply();
    }

    public void saveCurrentCivilianMinute(Context context,
                                          String civilianMinute,
                                          int currentCivilianMinute) {
        //pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(civilianMinute, currentCivilianMinute);
        editor.apply();
    }

    public void saveCurrentCivilianAmOrPm(Context context,
                                          String amOrPm,
                                          String currentAmOrPm) {
        //pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(amOrPm, currentAmOrPm);
        editor.apply();
    }
}//end class
