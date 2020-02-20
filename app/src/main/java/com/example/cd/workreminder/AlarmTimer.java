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
import android.provider.ContactsContract;
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
    private int snoozeTime = 2; //5 minute default. Added on 12 - 20 - 2019
    private int milliSecondsToMinutes = 60000; //Added on 12 - 22 - 2019
    private String currenDayOfWeek = ""; //Added on 12 - 27 - 2019
    private String previousDayOfWeek = "";
    private int currentSnoozeTime = 0; //Added on 1 - 16 - 2020
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


        /*
          The algorithm was taken from a math book that I got from an Elementary School teacher
          in Oakland. This code is a direct translation found in the 4th grade book.
         */
        if (timeBeforeShift < 60) {
            newMilitaryHour = startMilitaryHour;
            //endMilitaryMinute = timeBeforeShift;
            endMilitaryMinute = pref.getInt(context.getString(R.string.ALARM_MINUTES), WorkReaderContract.WorkEntry.ALARM_MINUTE_DEFAULT);
            endMilitaryHour = pref.getInt("ALARM_HOUR", 0);
        }

        //newMilitaryHour = startMilitaryHour - endMilitaryHour;
        //newMilitaryMinute = startMilitaryMinute - endMilitaryMinute;
        newMilitaryMinute = startMilitaryMinute - pref.getInt(context.getString(R.string.ALARM_MINUTES), WorkReaderContract.WorkEntry.ALARM_MINUTE_DEFAULT);
        endMilitaryHour = pref.getInt("ALARM_HOUR", 0);

        if (startMilitaryHour == 0) {
            newMilitaryHour = 24;
        }
        if (newMilitaryMinute < 0 && endMilitaryHour == 0) {
            newMilitaryMinute = newMilitaryMinute + 60;
            newMilitaryHour = newMilitaryHour - 1;
        } else if (newMilitaryMinute < 0 && endMilitaryHour > 0) {
            newMilitaryMinute = newMilitaryMinute + 60;
            //newMilitaryHour = newMilitaryHour - endMilitaryHour - 1;
            newMilitaryHour = newMilitaryHour - 1;
        }

        else {
            newMilitaryHour = newMilitaryHour - endMilitaryHour;
        }
        //updatedAmOrPm = getAMorPM(startMilitaryHour);

        updatedAmOrPm = getAMorPM(newMilitaryHour);

        //Used for Alarm since alarm only takes military format
        setNewAlarmMilitaryHour(context, newMilitaryHour);
        setNewAlarmMilitaryMinute(context, newMilitaryMinute);

        DataToMemory dataToMemory =  new DataToMemory(context);
        //Save something like 18:29 pm as 6:29 pm in the notification display
        dataToMemory.saveCivilianAlarmTime(dayOfWeek, newMilitaryHour, newMilitaryMinute, true);



    }//setAlarmTime

    public int getNewAlarmCivilianHour(Context context) {
        DataToMemory dataToMemory = new DataToMemory(context);
        return dataToMemory.getNewAlarmCivilianHour(context);
    }

    public int getNewAlarmCivilianMinutes(Context context) {
        DataToMemory dataToMemory = new DataToMemory(context);
        return dataToMemory.getNewAlarmCivilianMinutes(context);
    }
    //Added on 1 - 23 - 2020
    //----USED TO GET CURRENT TIME IN ALARM------------------------
    public void setNewAlarmMilitaryHour(Context context, int newMilitaryHour) {
        DataToMemory dataToMemory = new DataToMemory(context);
        dataToMemory.saveNewAlarmMilitaryHour(context, newMilitaryHour);
    }

    public int getNewAlarmMilitaryHour(Context context) {
        DataToMemory dataToMemory = new DataToMemory(context);
        return dataToMemory.getNewAlarmMilitaryHour(context);
    }

    public void setNewAlarmMilitaryMinute(Context context, int newMilitaryMinute) {
        DataToMemory dataToMemory = new DataToMemory(context);
        dataToMemory.saveNewAlarmMilitaryMinute(context, newMilitaryMinute);
    }

    public int getNewAlarmMilitaryMinute(Context context) {
        DataToMemory dataToMemory = new DataToMemory(context);
        return dataToMemory.getNewAlarmMilitaryMinute(context);
    }

    //----------------------------------------------------------

    //Added on 10 - 21 - 2019
    public void saveMinutesBeforeShift(Context context, int minutesBeforeShift) {
        DataToMemory dataToMemory = new DataToMemory(context);
        dataToMemory.saveMinutesBeforeShift(minutesBeforeShift);
    }


    public void saveHoursBeforeShift(Context context, int hoursBeforeShift) {
        DataToMemory dataToMemory = new DataToMemory(context);
        dataToMemory.saveHoursBeforeShift(hoursBeforeShift);
    }

    //Added on 12 - 27 - 2019
    //Just a lame attempt at database emulation, since like, this app doesn't use a database.
    void saveCurrentDayOfWeek(Context context, String currentDayOfWeek){
        DataToMemory dataToMemory = new DataToMemory(context);
        dataToMemory.saveCurrentDayOfWeek(context, currentDayOfWeek);
    }

    public String getCurrentSavedDayOfWeek(Context context) {
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        return pref.getString("CURRENT_DAY", "");
    }

    void savePreviousDayOfWeek(Context context, String previousDayOfWeek){
        DataToMemory dataToMemory = new DataToMemory(context);
        dataToMemory.savePreviousDayOfWeek(context, previousDayOfWeek);
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


    //Added on 12 - 29 - 2019
    void saveCurrentEndMilitaryHour(Context context, int currentEndMilitaryHour) {
        DataToMemory dataToMemory = new DataToMemory(context);
        dataToMemory.saveCurrentEndMilitaryHour(context, currentEndMilitaryHour);
    }

    public int getCurrentEndMilitaryHour(Context context){
        DataToMemory dataToMemory = new DataToMemory(context);
        return dataToMemory.getCurrentEndMilitaryHour(context);
    }


    //Added on 1 - 14 - 2019
    void saveCurrentEndMilitaryMinute(Context context, int currentEndMilitaryMinute) {
        DataToMemory dataToMemory = new DataToMemory(context);
        dataToMemory.saveCurrentEndMilitaryMinute(context, currentEndMilitaryMinute);
    }

    public int getCurrentEndMilitaryMinute(Context context){
        DataToMemory dataToMemory = new DataToMemory(context);
        return dataToMemory.getCurrentEndMilitaryMinute(context);
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