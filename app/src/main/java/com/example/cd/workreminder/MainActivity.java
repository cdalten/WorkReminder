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
    public static final String ALARM_RINGTONE = "ALARM_RINGTONE"; //Added on 11  - 26 - 2019
    private final String PRODUCTION_TAG = "MAIN_ACTIVITY_LG: "; //used for hardware only
    private SharedPreferences pref; //added on 9 - 21 - 2018
    
    private static boolean scheduleGotUpdated = false; //Added on 1 - 22 - 2019

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.getSupportActionBar().hide();
        setContentView(R.layout.activity_update_job_schedule);

        Log.e(PRODUCTION_TAG, "ONCREATE() BEFORE SAVEDINSTANCE()");

        dispatchCurrentWeekSchedule();
        enableBootReceiver();
        enablWorkNotifcationReceiver();

        if (savedInstanceState == null) {
            Log.e(PRODUCTION_TAG, "ONCREATE() WHEN SAVEDINSTANCE() IS NULL");

            //Attempt to invoke interface method 'java.lang.String android.content.SharedPreferences.getString(java.lang.String, java.lang.String)'
            //on a null object reference

            pref = this.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);

            //12AM represents midnight on my phone

        } else {
            Log.e(PRODUCTION_TAG, "ONCREATE() WHEN SAVEDINSTANCE() IS NOT NULL");
            savedInstanceState.getString("UPDATED_SCHEDULE"); //???

            pref = getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE); //redudant??
        }
    }

    //Added on 2 - 28 - 2020
    private void enableBootReceiver() {
        ComponentName receiver = new ComponentName(this, BootReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void enablWorkNotifcationReceiver() {
        ComponentName receiver = new ComponentName(this, WorkNotificationReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    //Added on 10 - 10 - 2022
    private void disableBootReceiver() {
        ComponentName receiver = new ComponentName(this, BootReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        //I'm not that sure where to put this function at.
        Log.d(PRODUCTION_TAG, "BOOT RECEIVER GOT DISABLED");
    }

    //Added on 10 - 10 - 2022
    public void disableWorkNoticationReceiver() {
        ComponentName receiver = new ComponentName(this, WorkNotificationReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }



    private void dispatchCurrentWeekSchedule() {
        Intent currentWeekScheduleIntent = new Intent(MainActivity.this, CurrentWeekSchedule.class);
        if (currentWeekScheduleIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(currentWeekScheduleIntent);
        }

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }//end onStart()

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MAIN", "ON PAUSE GOT CALLED");
    }

    @Override
    /*
     * Attempt to suppress "Failed to locate a binder for interface: autofill::mojom::PasswordManagerDriver"
     */
    protected void onResume() {
        super.onResume();
        Log.e("MAIN", "ON RESUME GOT CALLED");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("MAIN", "ON RESTART GOT CALLED");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disableBootReceiver();
        disableWorkNoticationReceiver();
        Log.e("MAIN", "ON DESTROY GOT CALLED");
    }
}//end Main// Activity

