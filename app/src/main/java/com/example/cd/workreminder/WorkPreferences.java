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

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

//Set the alarm time preferences.
public class WorkPreferences extends AppCompatActivity {
    Spinner dayPreference;
    private EditText alarmMinutesPreference; //Added on 4 - 5- 2019
    private EditText alarmHourPreference; //Added on 1 - 15 - 2019
    private Intent getWorkPrefTime;
    private SharedPreferences pref;
    private int newAlarmTimeMinutes; //Added on 10 - 28 - 2019
    private int newAlarmTimeHour;

    private Button save; //Added on 6 - 24 - 2019
    private Button discard; //Added on 12 - 28 - 2019

    /*
      The Diazepam, which I use for Vertigo, was making me a bit loopy when I wrote this. So yeah,
      I don't know the reasoning behind it since I was high at the time. But I don't touch it
      because the shit don't break.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_preferences);
        //dayPreference = (Spinner) findViewById(R.id.dayPreference);
        this.pref = getSharedPreferences("BECAUSE_INTENTS_SUCK_MASSIVE_DICK", MODE_PRIVATE);
        alarmMinutesPreference = (EditText) findViewById(R.id.alarmMinutesPreference);
        alarmHourPreference = (EditText) findViewById(R.id.alarmHourPreferences);
        save = (Button) findViewById(R.id.save);
        discard = (Button) findViewById(R.id.discard);

        getWorkPrefTime = getIntent();

        pref = getSharedPreferences("BECAUSE_INTENTS_SUCK_MASSIVE_DICK", MODE_PRIVATE);
        newAlarmTimeMinutes = pref.getInt(getString(R.string.ALARM_MINUTES), WorkReaderContract.ALARM_MINUTE_DEFAULT);
        newAlarmTimeHour = pref.getInt(getString(R.string.ALARM_HOURS), WorkReaderContract.ALARM_HOUR_DEFAULT);
        alarmMinutesPreference.setText(newAlarmTimeMinutes + "");
        alarmHourPreference.setText(newAlarmTimeHour + "");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updateMinutes = alarmMinutesPreference.getText().toString().trim();
                String updateHour = alarmHourPreference.getText().toString().trim();

                /*
                  Because every other forced update seems to undo my XML preference settings.
                */
                if (isValid(updateMinutes)) {
                    updateMinutes =  pref.getString("NEW_ALARM_TIME", "20");
                    Toast.makeText(getApplicationContext(), "Invalid Input.", Toast.LENGTH_LONG).show();
                }


                else if (updateMinutes.equals("") || updateMinutes == null ||
                        Integer.parseInt(updateMinutes) < 0
                        || Integer.parseInt(updateMinutes) > WorkReaderContract.hour
                        )
                {
                    updateMinutes =  pref.getString("NEW_ALARM_TIME", "20");
                    Toast.makeText(getApplicationContext(), "Invalid Input.", Toast.LENGTH_LONG).show();
                }

                double currentEndTime = getEndShiftInMinutes() +
                        convertInputTimeToHoursAndMinutes(Integer.parseInt(updateMinutes),
                                Integer.parseInt(updateHour));

                //Acquire End time
                final AlarmTimer alarmTimer = AlarmTimer.getInstance();
                alarmTimer.saveMinutesBeforeShift(getApplicationContext(), Integer.parseInt(updateMinutes));
                alarmTimer.saveHoursBeforeShift(getApplicationContext(), Integer.parseInt(updateHour));

                //Log.e("LG_WORK_PHONE", "NEW ALARM TIME AGAIN IS: " + updateTime);

                /*
                  This is done because I don't want the notification calls to be spread across a
                  bunch of different files. Yeah, this might suck. But it makes debugging 10x
                  easier.
                 */
                setResult(WorkReaderContract.RESULT_OKAY_UPDATE_WORK_ALARM_TIME, getWorkPrefTime);
                finish();
            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }//end onCreate()


    //Added on 12 - 29 - 2019
    private double getEndShiftInMinutes() {
        final AlarmTimer alarmTimer = AlarmTimer.getInstance();
        double endMilitaryMinuteInDecimals = 0.0;
        double totalTime = 0.0;

        int endMilitaryMinute = alarmTimer.getCurrentEndMilitaryMinute(getApplication());
        double endMilitaryHour = alarmTimer.getCurrentEndMilitaryHour(getApplication());

        //For hourly workers, a lot of places use 15 minute intervals.
        switch (endMilitaryMinute) {
            case 0:
                endMilitaryMinuteInDecimals = 0.0;
                break;
            case 15:
                endMilitaryMinuteInDecimals = 0.15;
                break;
            case 30:
                endMilitaryMinuteInDecimals = 0.30;
                break;
            case 45:
                endMilitaryMinuteInDecimals = 0.45;
                break;
        }

        totalTime = endMilitaryHour + endMilitaryMinuteInDecimals;
        Log.e("THE TOTAL TIME IS ", "" + totalTime);

        return totalTime;

    }

    //Added 1 - 14 - 2020
    private double convertInputTimeToHoursAndMinutes(int minutes, int hour) {
        double currentTime = 0.0;

        if (hour != 0) {
            currentTime = hour + minutes/100.00;
        } else {
            currentTime = minutes / 100.00;
        }

        return currentTime;
    }

    //Copied from stackoverflow

    private boolean isValid(String str) {
        //if (str.length() != 1) return false;
        char c = Character.toLowerCase(str.charAt(0));
        return c >= 'a' && c <= 'z';

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder =
        new AlertDialog.Builder(this);
        builder.setTitle("Discard Changes");

        builder.setMessage("ARE YOU SURE YOU WANT TO DISCARD ANY POSSIBLE CHANGES?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                finish();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.create().show();
        ;
    }

}//end class}
