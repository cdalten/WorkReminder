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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class MainActivity extends AppCompatActivity {
    public static final int NOTIFICATION_ID = 0; //Added on 10 - 14 - 2019
    private final String PRODUCTION_TAG = "MAIN_ACTIVITY_LG: "; //used for hardware only
    //private SharedPreferences pref; //added on 9 - 21 - 2018
    SharedPreferences.Editor editor;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.getSupportActionBar().hide();
        setContentView(R.layout.activity_update_job_schedule);

        Log.d(PRODUCTION_TAG, "ONCREATE() BEFORE SAVEDINSTANCE()");

        //initConstants();
        dispatchCurrentWeekSchedule();

        if (savedInstanceState == null) {
            Log.d(PRODUCTION_TAG, "ONCREATE() WHEN SAVEDINSTANCE() IS NULL");

            //Attempt to invoke interface method 'java.lang.String android.content.SharedPreferences.getString(java.lang.String, java.lang.String)'
            //on a null object reference
            SharedPreferences pref = this.getSharedPreferences("BECAUSE_INTENTS_SUCK_MASSIVE_DICK", MODE_PRIVATE);

            //12AM represents midnight on my phone

        } else {
            Log.d(PRODUCTION_TAG, "ONCREATE() WHEN SAVEDINSTANCE() IS NOT NULL");
            savedInstanceState.getString("UPDATED_SCHEDULE"); //???

            SharedPreferences pref = getSharedPreferences("BECAUSE_INTENTS_SUCK_MASSIVE_DICK", MODE_PRIVATE); //redudant??
        }
    }

    private void initConstants() {

            //  public static class WorkEntry implements BaseColumns {


        SharedPreferences preferences = getApplicationContext().getSharedPreferences("CONSTANTS", MODE_PRIVATE);
        editor = preferences.edit();
            //public static final int ALARM_MINUTE_DEFAULT = 20; //20 minutes before start of shift
            //public static final int ALARM_HOUR_DEFAULT = 0; //0 hours before start of shift
        editor.putString("DAY_OFF_DEFAULT", "OFF");
        editor.putString("START_HOUR_DEFAULT", "12");
        editor.putString("START_MINUTE_DEFAULT", "00");
        editor.putString("START_AM_OR_PM_DEFAULT", "AM");
        editor.putString("END_HOUR_DEFAULT", "12");
        editor.putString("END_MINUTE_DEFAULT", "00");
        editor.putString("END_AM_OR_PM_DEFAULT", "AM");
            //public static final String DAY_OFF_DEFAULT = "OFF"; //Added on 11 - 15 - 2019
            //public static final String START_HOUR_DEFAULT = "12"; //Added on 5 - 22 - 2019
            //public static final String START_MINUTE_DEFAULT = "00";
            //public static final String START_AM_OR_PM_DEFAULT = "AM";
            //public static final String END_HOUR_DEFAULT = "12";
            //public static final String END_MINUTE_DEFAULT = "00";
            //public static final String END_AM_OR_PM_DEFAULT = "AM";


        editor.putInt("SUNDAY", 0);
        editor.putInt("MONDAY", 1);
        editor.putInt("TUESDAY", 2);
        editor.putInt("WEDNESDAY", 3);
        editor.putInt("THURSDAY", 4);
        editor.putInt("FRIDAY", 5);
        editor.putInt("SATURDAY", 6);
        editor.putInt("OFF", 7);

            //public static final int SUNDAY = 0;
            //public static final int MONDAY = 1;
            //public static final int TUESDAY = 2;
            //public static final int WEDNESDAY = 3;
            //public static final int THURSDAY = 4;
            //public static final int FRIDAY = 5;
            //public static final int SATURDAY = 6;
            //public static final int OFF = 7; //Added on 5 - 28 - 2019

        editor.putInt("DAY_OF_WEEK", 0);
        editor.putInt("START_HOUR", 1);
        editor.putInt("START_MINUTE", 2);
        editor.putInt("START_AM_OR_PM", 3);
        editor.putInt("END_HOUR", 4);
        editor.putInt("END_MINUTE", 5);
        editor.putInt("END_AM_OR_PM", 6);

            //public static final int DAY_OF_WEEK = 0; //Added on 11 - 15 - 2019
            //public static final int START_HOUR = 1; //Added on 5 - 29 - 2019
            //public static final int START_MINUTE = 2;
            //public static final int START_AM_OR_PM = 3;
            //public static final int END_HOUR = 4;
            //public static final int END_MINUTE = 5;
            //public static final int END_AM_OR_PM = 6;

        editor.putInt("RESULT_OK_WORK",1);
        editor.putInt("RESULT_FAILED", 2);
        editor.putInt("RESULT_OKAY_NO_WORK", 3);
        editor.putInt("RESULT_OKAY_UPDATE_WORK_ALARM_TIME", 4);
            //public static final int RESULT_OK_WORK = 1; //Added on 11 - 12 - 2019
            //public static final int RESULT_FAILED = 2;
            //public static final int RESULT_OKAY_NO_WORK = 3; //Added on 11 - 13 - 2019
            //public static final int RESULT_OKAY_UPDATE_WORK_ALARM_TIME = 4; //Added on 12 - 19 - 2019

        editor.putBoolean("ALARM_NOTIFICATION_RINGS", true);
        editor.putBoolean("ALARM_NOTIFICASTION_SILENT", false);
        editor.putBoolean("SNOOZE_ON", true);
        editor.putBoolean("SNOOZE_OFF", false);
        editor.putBoolean("ALARM_RINGS", false);
        editor.putBoolean("ALARM_SILENT", false);
            //public static final boolean ALARM_NOTIFICATION_RINGS = true;
            //public static final boolean ALARM_NOTIFICATION_SILENT = false;
            //public static final boolean SNOOZE_ON = true;
            //public static final boolean SNOOZE_OFF = false;
            //public static final boolean ALARM_RINGS = true; //Added on 1 - 31 - 2020
            //public static final boolean ALARM_SILENT = false; //Added on 1 - 31 - 2020


         editor.putInt("HOUR", 60);
         editor.putInt("ALARM_DEFAULT", 20);
            //public static final int hour = 60;
            //public static int alarm_default = 20;


         editor.putInt("ON_DAY", 0);
         editor.putInt("OFF_DAY", 1);
         editor.putInt("SELECTION_DEFAULT_VALUE", 0);
         editor.putBoolean("amCoonnected", false);
            //public static final int ON_DAY = 0; //Added on 10 - 12 - 2022
            //public static final int OFF_DAY = 1; //Added onn 10 - 12 - 2022
            //public static int SELECTION_DEFAULT_VALUE = 0; //Added on 10 - 12 - 2022
            //public static boolean amAconnected = false;

         editor.commit();
            //}

    }

    private void dispatchCurrentWeekSchedule() {
        Intent currentWeekScheduleIntent = new Intent(MainActivity.this, CurrentWeekSchedule.class);
        if (currentWeekScheduleIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(currentWeekScheduleIntent);
            //finish();;
        }

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }//end onStart()

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(PRODUCTION_TAG, "ON RESUME GOT CALLED");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(PRODUCTION_TAG, "ON PAUSE GOT CALLED");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(PRODUCTION_TAG, "ON DESTROY GOT CALLED");
    }
}//end Main// Activity

